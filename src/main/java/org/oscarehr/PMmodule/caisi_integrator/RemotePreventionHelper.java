package org.oscarehr.PMmodule.caisi_integrator;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.oscarehr.caisi_integrator.ws.CachedDemographicPrevention;
import org.oscarehr.caisi_integrator.ws.CachedProvider;
import org.oscarehr.caisi_integrator.ws.DemographicWs;
import org.oscarehr.caisi_integrator.ws.FacilityIdStringCompositePk;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import oscar.util.DateUtils;

public final class RemotePreventionHelper {
	private static Logger logger = MiscUtils.getLogger();

	public static ArrayList<HashMap<String, Object>> getLinkedPreventionDataMap(Integer localDemographicId) throws MalformedURLException {
		ArrayList<HashMap<String, Object>> results = new ArrayList<HashMap<String, Object>>();

		try {
			DemographicWs demographicWs = CaisiIntegratorManager.getDemographicWs();
			List<CachedDemographicPrevention> preventions = demographicWs.getLinkedCachedDemographicPreventionsByDemographicId(localDemographicId);

			for (CachedDemographicPrevention prevention : preventions) {
				results.add(getPreventionDataMap(prevention));
			}
		} catch (Exception e) {
			logger.error("Error getting remote Preventions", e);
		}

		return (results);
	}

	public static HashMap<String, Object> getPreventionDataMap(CachedDemographicPrevention prevention) throws MalformedURLException {
		HashMap<String, Object> result = new HashMap<String, Object>();

		result.put("id", prevention.getFacilityPreventionPk().getCaisiItemId().toString());
		result.put("refused", booleanTo10String(prevention.isRefused()));
		result.put("type", prevention.getPreventionType());
		result.put("provider_no", prevention.getCaisiProviderId());
		result.put("prevention_date", DateUtils.formatDate(prevention.getPreventionDate(), null));
		if (prevention.getPreventionDate()!=null)
		{
			result.put("prevention_date_asDate", prevention.getPreventionDate().getTime());
		}
		
		FacilityIdStringCompositePk providerPk=new FacilityIdStringCompositePk();
		providerPk.setIntegratorFacilityId(prevention.getFacilityPreventionPk().getIntegratorFacilityId());
		providerPk.setCaisiItemId(prevention.getCaisiProviderId());
		CachedProvider cachedProvider=CaisiIntegratorManager.getProvider(providerPk);
		if (cachedProvider!=null)
		{
			result.put("provider_name", cachedProvider.getLastName()+", "+cachedProvider.getFirstName());
		}
		
		return (result);
	}

	private static String booleanTo10String(boolean b) {
		if (b) return ("1");
		else return ("0");
	}
	
	public static HashMap<String,String> getRemotePreventionAttributesAsHashMap(CachedDemographicPrevention prevention) throws IOException, SAXException, ParserConfigurationException
	{
		Document doc=XmlUtils.toDocument(prevention.getAttributes());
		Node root=doc.getFirstChild();
		HashMap<String,String> result=new HashMap<String, String>();
		
		NodeList nodeList=root.getChildNodes();
		for (int i=0; i<nodeList.getLength(); i++)
		{
			Node node=nodeList.item(i);
			result.put(node.getNodeName(), node.getTextContent());
		}
		
		return(result);
	}
}
