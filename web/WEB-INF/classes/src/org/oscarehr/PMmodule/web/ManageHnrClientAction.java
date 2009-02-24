package org.oscarehr.PMmodule.web;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.casemgmt.dao.ClientImageDAO;
import org.oscarehr.casemgmt.model.ClientImage;
import org.oscarehr.common.dao.ClientLinkDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.HnrDataValidationDao;
import org.oscarehr.common.model.ClientLink;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Facility;
import org.oscarehr.common.model.HnrDataValidation;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.SpringUtils;

public class ManageHnrClientAction {
	private static Logger logger = LogManager.getLogger(ManageHnrClientAction.class);
	private static CaisiIntegratorManager caisiIntegratorManager = (CaisiIntegratorManager) SpringUtils.getBean("caisiIntegratorManager");
	private static DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");
	private static ClientLinkDao clientLinkDao = (ClientLinkDao) SpringUtils.getBean("clientLinkDao");
	private static ClientImageDAO clientImageDAO = (ClientImageDAO) SpringUtils.getBean("clientImageDAO");
	private static HnrDataValidationDao hnrDataValidationDao = (HnrDataValidationDao) SpringUtils.getBean("hnrDataValidationDao");

	public static void copyHnrToLocal(Facility currentFacility, Provider currentProvider, Integer clientId) {
		try {
			logger.debug("copyHnrToLocal currentFacility=" + currentFacility.getId() + ", currentProvider=" + currentProvider.getProviderNo() + ", client=" + clientId);

			List<ClientLink> clientLinks = clientLinkDao.findByFacilityIdClientIdType(currentFacility.getId(), clientId, true, ClientLink.Type.HNR);

			// it might be 0 if some one unlinked the client at the same time you are looking at this screen.
			if (clientLinks.size() > 0) {
				ClientLink clientLink = clientLinks.get(0);
				org.oscarehr.caisi_integrator.ws.client.Client hnrClient = caisiIntegratorManager.getHnrClient(currentFacility, currentProvider, clientLink.getRemoteLinkId());

				Demographic demographic = demographicDao.getDemographicById(clientId);

				if (hnrClient.getBirthDate() != null) 
					{
					GregorianCalendar cal=new GregorianCalendar();
					cal.setTime(hnrClient.getBirthDate());
					demographic.setBirthDay(cal);
					}
				if (hnrClient.getCity() != null) demographic.setCity(hnrClient.getCity());
				if (hnrClient.getFirstName() != null) demographic.setFirstName(hnrClient.getFirstName());

				// genders can't be synced until gender constants are resolved
				// if (hnrClient.getGender()!=null) demographic.setSex(hnrClient.getGender().name());

				if (hnrClient.getHin() != null) demographic.setHin(hnrClient.getHin());
				if (hnrClient.getHinType() != null) demographic.setHcType(hnrClient.getHinType());
				if (hnrClient.getHinVersion() != null) demographic.setVer(hnrClient.getHinVersion());

				if (hnrClient.getHinValidStart() != null) demographic.setEffDate(hnrClient.getHinValidStart());
				if (hnrClient.getHinValidEnd() != null) demographic.setHcRenewDate(hnrClient.getHinValidEnd());

				if (hnrClient.getImage() != null) {
					ClientImage clientImage = clientImageDAO.getClientImage(clientId);
					if (clientImage == null) {
						clientImage = new ClientImage();
						clientImage.setDemographic_no(clientId);
					}

					clientImage.setImage_data(hnrClient.getImage());
					clientImage.setImage_type("jpg");
					clientImage.setUpdate_date(new Date());

					clientImageDAO.saveClientImage(clientImage);
				}

				if (hnrClient.getLastName() != null) demographic.setLastName(hnrClient.getLastName());
				if (hnrClient.getProvince() != null) demographic.setProvince(hnrClient.getProvince());
				if (hnrClient.getSin() != null) demographic.setSin(hnrClient.getSin());
				if (hnrClient.getStreetAddress() != null) demographic.setAddress(hnrClient.getStreetAddress());

				demographicDao.getHibernateTemplate().saveOrUpdate(demographic);
			}
		} catch (Exception e) {
			logger.error("Unexpected Error.", e);
		}
	}

	public static void copyLocalValidatedToHnr(Facility currentFacility, Provider currentProvider, Integer clientId) {
		try {
			logger.debug("copyLocalToHnr currentFacility=" + currentFacility.getId() + ", currentProvider=" + currentProvider.getProviderNo() + ", client=" + clientId);

			// there's 2 cases here
			// 1) there's a linked client at which point update the linked client on the hnr
			// 2) there is no linked client at which point create a new linked client on the hnr and create the link.

			// were ignoring the anomalie of multiple hnr links as it should never really happen though it's theorietically possible due to lack of atomic updates on this table.
			List<ClientLink> clientLinks = clientLinkDao.findByFacilityIdClientIdType(currentFacility.getId(), clientId, true, ClientLink.Type.HNR);

			org.oscarehr.caisi_integrator.ws.client.Client hnrClient = null;
			ClientLink clientLink = null;

			// try to retrieve existing linked client to update
			if (clientLinks.size() >= 1) {
				clientLink = clientLinks.get(0);
				hnrClient = caisiIntegratorManager.getHnrClient(currentFacility, currentProvider, clientLink.getRemoteLinkId());
			}

			// can be null if there's no existing link or if the data on the hnr has been revoked of consent
			if (hnrClient == null) {
				hnrClient = new org.oscarehr.caisi_integrator.ws.client.Client();
			}

			// copy any non null data to the HNR if it's validated
			// you have to check for null before setting because if it's an existing record you don't want to nullify existing data
			boolean isAtLeastOneThingValidated = false;
			Demographic demographic = demographicDao.getDemographicById(clientId);

			HnrDataValidation tempValidation = hnrDataValidationDao.findMostCurrentByFacilityIdClientIdType(currentFacility.getId(), clientId, HnrDataValidation.Type.HC_INFO);
			boolean hcInfoValidated = (tempValidation != null && tempValidation.isValidAndMatchingCrc(HnrDataValidation.getHcInfoValidationBytes(demographic)));

			if (hcInfoValidated) {
				isAtLeastOneThingValidated = true;

				if (demographic.getBirthDay() != null) hnrClient.setBirthDate(demographic.getBirthDay().getTime());
				if (demographic.getCity() != null) hnrClient.setCity(demographic.getCity());
				if (demographic.getFirstName() != null) hnrClient.setFirstName(demographic.getFirstName());
				if (demographic.getLastName() != null) hnrClient.setLastName(demographic.getLastName());
				if (demographic.getHin() != null) hnrClient.setHin(demographic.getHin());
				if (demographic.getHcType() != null) hnrClient.setHinType(demographic.getHcType());
				if (demographic.getVer() != null) hnrClient.setHinVersion(demographic.getVer());
				if (demographic.getEffDate() != null) hnrClient.setHinValidStart(demographic.getEffDate());
				if (demographic.getHcRenewDate() != null) hnrClient.setHinValidEnd(demographic.getHcRenewDate());
				if (demographic.getProvince() != null) hnrClient.setProvince(demographic.getProvince());
				if (demographic.getAddress() != null) hnrClient.setStreetAddress(demographic.getAddress());
			}

			ClientImage clientImage = clientImageDAO.getClientImage(clientId);
			tempValidation = hnrDataValidationDao.findMostCurrentByFacilityIdClientIdType(currentFacility.getId(), clientId, HnrDataValidation.Type.PICTURE);
			boolean pictureValidated = (tempValidation != null && tempValidation.isValidAndMatchingCrc(clientImage.getImage_data()));
			if (pictureValidated && clientImage != null) {
				isAtLeastOneThingValidated = true;

				hnrClient.setImage(clientImage.getImage_data());
			}

			tempValidation = hnrDataValidationDao.findMostCurrentByFacilityIdClientIdType(currentFacility.getId(), clientId, HnrDataValidation.Type.OTHER);
			boolean otherValidated = (tempValidation != null && tempValidation.isValidAndMatchingCrc(HnrDataValidation.getOtherInfoValidationBytes(demographic)));
			if (otherValidated) {
				isAtLeastOneThingValidated=true;
				
				// genders can't be synced until gender constants are resolved
				// if (demographic.getSex()!=null) hnrClient.setGender(demographic.getSex());

				if (demographic.getSin() != null) hnrClient.setSin(demographic.getSin());
			}

			if (isAtLeastOneThingValidated)
			{
				// link back to currently linked client if previously linked.
				if (clientLink != null) {
					hnrClient.setLinkingId(clientLink.getRemoteLinkId());
				}
	
				// save the client
				hnrClient.setCreatedBy("faciliy: " + currentFacility.getName() + ", provider:" + currentProvider.getFormattedName());
				Integer linkingId = caisiIntegratorManager.setHnrClient(currentFacility, currentProvider, hnrClient);
	
				// if the hnr client is new / not previously linked, save the new link
				// in theory this can lead to multiple links if 2 people run this method
				// at the same time, in reality it should really be a problem and
				// the code is set to "ignore" multiple hnr links so it should function fine anyways.
				if (clientLink == null && linkingId != null) {
					clientLink = new ClientLink();
					clientLink.setFacilityId(currentFacility.getId());
					clientLink.setClientId(clientId);
					clientLink.setLinkDate(new Date());
					clientLink.setLinkProviderNo(currentProvider.getProviderNo());
					clientLink.setLinkType(ClientLink.Type.HNR);
					clientLink.setRemoteLinkId(linkingId);
					clientLinkDao.persist(clientLink);
				}
			}
		} catch (Exception e) {
			logger.error("Unexpected Error.", e);
		}
	}

	public static void setPictureValidation(Facility currentFacility, Provider currentProvider, Integer clientId, boolean valid) {
		try {
			logger.debug("setPictureValidation currentFacility=" + currentFacility.getId() + ", currentProvider=" + currentProvider.getProviderNo() + ", client=" + clientId + ", valid=" + valid);

			ClientImage clientImage = clientImageDAO.getClientImage(clientId);
			if (clientImage == null) throw (new IllegalStateException("Attempt to validate an image that doesn't exist. clientId=" + clientId));

			HnrDataValidation hnrDataValidation = new HnrDataValidation();
			hnrDataValidation.setClientId(clientId);
			hnrDataValidation.setCreated(new Date());
			hnrDataValidation.setFacilityId(currentFacility.getId());
			hnrDataValidation.setValid(valid);
			hnrDataValidation.setValidationCrc(clientImage.getImage_data());
			hnrDataValidation.setValidationType(HnrDataValidation.Type.PICTURE);
			hnrDataValidation.setValidatorProviderNo(currentProvider.getProviderNo());
			hnrDataValidationDao.persist(hnrDataValidation);
		} catch (Exception e) {
			logger.error("Unexpected Error.", e);
		}
	}

	public static void setHcInfoValidation(Facility currentFacility, Provider currentProvider, Integer clientId, boolean valid) {
		try {
			logger.debug("setHcInfoValidation currentFacility=" + currentFacility.getId() + ", currentProvider=" + currentProvider.getProviderNo() + ", client=" + clientId + ", valid=" + valid);

			Demographic demographic = demographicDao.getDemographicById(clientId);

			HnrDataValidation hnrDataValidation = new HnrDataValidation();
			hnrDataValidation.setClientId(clientId);
			hnrDataValidation.setCreated(new Date());
			hnrDataValidation.setFacilityId(currentFacility.getId());
			hnrDataValidation.setValid(valid);
			hnrDataValidation.setValidationCrc(HnrDataValidation.getHcInfoValidationBytes(demographic));
			hnrDataValidation.setValidationType(HnrDataValidation.Type.HC_INFO);
			hnrDataValidation.setValidatorProviderNo(currentProvider.getProviderNo());
			hnrDataValidationDao.persist(hnrDataValidation);
		} catch (Exception e) {
			logger.error("Unexpected Error.", e);
		}
	}

	public static void setOtherInfoValidation(Facility currentFacility, Provider currentProvider, Integer clientId, boolean valid) {
		try {
			logger.debug("setOtherInfoValidation currentFacility=" + currentFacility.getId() + ", currentProvider=" + currentProvider.getProviderNo() + ", client=" + clientId + ", valid=" + valid);

			Demographic demographic = demographicDao.getDemographicById(clientId);

			HnrDataValidation hnrDataValidation = new HnrDataValidation();
			hnrDataValidation.setClientId(clientId);
			hnrDataValidation.setCreated(new Date());
			hnrDataValidation.setFacilityId(currentFacility.getId());
			hnrDataValidation.setValid(valid);
			hnrDataValidation.setValidationCrc(HnrDataValidation.getOtherInfoValidationBytes(demographic));
			hnrDataValidation.setValidationType(HnrDataValidation.Type.OTHER);
			hnrDataValidation.setValidatorProviderNo(currentProvider.getProviderNo());
			hnrDataValidationDao.persist(hnrDataValidation);
		} catch (Exception e) {
			logger.error("Unexpected Error.", e);
		}
	}
}
