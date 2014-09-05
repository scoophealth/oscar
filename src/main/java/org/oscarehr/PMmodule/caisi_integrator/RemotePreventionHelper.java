/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


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
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import oscar.util.DateUtils;

public final class RemotePreventionHelper {
	private static Logger logger = MiscUtils.getLogger();

	public static ArrayList<HashMap<String, Object>> getLinkedPreventionDataMap(LoggedInInfo loggedInInfo, Integer localDemographicId) {
		ArrayList<HashMap<String, Object>> results = new ArrayList<HashMap<String, Object>>();

		try {
			
			
			List<CachedDemographicPrevention> preventions  = null;
			try {
				if (!CaisiIntegratorManager.isIntegratorOffline(loggedInInfo.getSession())){
				   DemographicWs demographicWs = CaisiIntegratorManager.getDemographicWs(loggedInInfo, loggedInInfo.getCurrentFacility());
				   preventions = demographicWs.getLinkedCachedDemographicPreventionsByDemographicId(localDemographicId);
				}
			} catch (Exception e) {
				MiscUtils.getLogger().error("Unexpected error.", e);
				CaisiIntegratorManager.checkForConnectionError(loggedInInfo.getSession(),e);
			}
				
			if(CaisiIntegratorManager.isIntegratorOffline(loggedInInfo.getSession())){
			   preventions = IntegratorFallBackManager.getRemotePreventions(loggedInInfo, localDemographicId);
			} 
		 
			for (CachedDemographicPrevention prevention : preventions) {
				results.add(getPreventionDataMap(loggedInInfo, prevention));
			}
		} catch (Exception e) {
			logger.error("Error getting remote Preventions", e);
		}

		return (results);
	}

	public static HashMap<String, Object> getPreventionDataMap(LoggedInInfo loggedInInfo,CachedDemographicPrevention prevention) throws MalformedURLException {
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
		CachedProvider cachedProvider=CaisiIntegratorManager.getProvider(loggedInInfo, loggedInInfo.getCurrentFacility(),providerPk);
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
