package org.oscarehr.PMmodule.web;

import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.casemgmt.dao.ClientImageDAO;
import org.oscarehr.casemgmt.model.ClientImage;
import org.oscarehr.common.dao.ClientLinkDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.ClientLink;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Facility;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.SpringUtils;

public class ManageHnrClientAction {
	private static Logger logger = LogManager.getLogger(ManageHnrClientAction.class);
	private static CaisiIntegratorManager caisiIntegratorManager = (CaisiIntegratorManager) SpringUtils.getBean("caisiIntegratorManager");
	private static DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");
	private static ClientLinkDao clientLinkDao = (ClientLinkDao) SpringUtils.getBean("clientLinkDao");
	private static ClientImageDAO clientImageDAO = (ClientImageDAO) SpringUtils.getBean("clientImageDAO");

	public static void copyHnrToLocal(Facility currentFacility, Provider currentProvider, Integer clientId) throws MalformedURLException
	{
		logger.debug("copyHnrToLocal currentFacility="+currentFacility.getId()+", currentProvider="+currentProvider.getProviderNo()+", client="+clientId);

		List<ClientLink> clientLinks=clientLinkDao.findByClientIdAndType(clientId, true, ClientLink.Type.HNR);
		if (clientLinks.size()!=1) throw(new IllegalArgumentException("The client passed in should have had exactly 1 hnr link. links.size="+clientLinks.size())); 

		ClientLink clientLink=clientLinks.get(0);		
		org.oscarehr.hnr.ws.client.Client hnrClient=caisiIntegratorManager.getHnrClient(currentFacility, currentProvider, clientLink.getRemoteLinkId());
		
		Demographic demographic=demographicDao.getDemographicById(clientId);

		if (hnrClient.getBirthDate()!=null)	demographic.setBirthDay(hnrClient.getBirthDate().toGregorianCalendar());
		if (hnrClient.getCity()!=null) demographic.setCity(hnrClient.getCity());
		if (hnrClient.getFirstName()!=null) demographic.setFirstName(hnrClient.getFirstName());
		
		// genders can't be synced until gender constants are resolved
		// if (hnrClient.getGender()!=null) demographic.setSex(hnrClient.getGender().name());
		
		if (hnrClient.getHin()!=null) demographic.setHin(hnrClient.getHin());
		if (hnrClient.getHinType()!=null) demographic.setHcType(hnrClient.getHinType());
		if (hnrClient.getHinVersion()!=null) demographic.setVer(hnrClient.getHinVersion());
		
		if (hnrClient.getImage()!=null)
		{
			ClientImage clientImage=clientImageDAO.getClientImage(clientId);
			if (clientImage==null)
			{
				clientImage=new ClientImage();
				clientImage.setDemographic_no(clientId);
			}
			
			clientImage.setImage_data(hnrClient.getImage());
			clientImage.setImage_type("jpg");
			clientImage.setUpdate_date(new Date());
			
			clientImageDAO.saveClientImage(clientImage);
		}
		
		if (hnrClient.getLastName()!=null) demographic.setLastName(hnrClient.getLastName());
		if (hnrClient.getProvince()!=null) demographic.setProvince(hnrClient.getProvince());
		if (hnrClient.getSin()!=null) demographic.setSin(hnrClient.getSin());
		if (hnrClient.getStreetAddress()!=null) demographic.setAddress(hnrClient.getStreetAddress());

		demographicDao.getHibernateTemplate().saveOrUpdate(demographic);
	}
	
	public static void copyLocalToHnr(Facility currentFacility, Provider currentProvider, Integer clientId)
	{
		logger.debug("copyLocalToHnr currentFacility="+currentFacility.getId()+", currentProvider="+currentProvider.getProviderNo()+", client="+clientId);

	}
}
