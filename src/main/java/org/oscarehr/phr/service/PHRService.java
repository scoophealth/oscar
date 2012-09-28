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


package org.oscarehr.phr.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.oscarehr.common.service.myoscar.MyOscarMedicalDataManagerUtils;
import org.oscarehr.myoscar.managers.MyOscarAccountManager;
import org.oscarehr.myoscar_server.ws.AccountWs;
import org.oscarehr.myoscar_server.ws.LoginResultTransfer;
import org.oscarehr.myoscar_server.ws.MedicalDataTransfer3;
import org.oscarehr.myoscar_server.ws.MedicalDataType;
import org.oscarehr.myoscar_server.ws.MedicalDataWs;
import org.oscarehr.myoscar_server.ws.NotAuthorisedException_Exception;
import org.oscarehr.myoscar_server.ws.PersonTransfer2;
import org.oscarehr.myoscar_server.ws.Role;
import org.oscarehr.phr.PHRAuthentication;
import org.oscarehr.phr.dao.PHRActionDAO;
import org.oscarehr.phr.dao.PHRDocumentDAO;
import org.oscarehr.phr.model.PHRAction;
import org.oscarehr.phr.model.PHRDocument;
import org.oscarehr.phr.model.PHRMedication;
import org.oscarehr.phr.model.PHRMessage;
import org.oscarehr.phr.util.MyOscarServerWebServicesManager;
import org.oscarehr.util.MiscUtils;

import oscar.oscarEncounter.data.EctProviderData;
import oscar.oscarProvider.data.ProviderData;
import oscar.oscarProvider.data.ProviderMyOscarIdData;
import oscar.oscarRx.data.RxPrescriptionData;

public class PHRService {
	private static final Logger logger = MiscUtils.getLogger();
	protected PHRDocumentDAO phrDocumentDAO;
	protected PHRActionDAO phrActionDAO;

	public static boolean canAuthenticate(String providerNo) {
		String myOscarLoginId = ProviderMyOscarIdData.getMyOscarId(providerNo);
		if (myOscarLoginId == null) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean validAuthentication(PHRAuthentication auth) {

		if (auth == null) {
			return false;
		}

		try {
			logger.debug(auth.getExpirationDate());
		} catch (Exception e) {
			logger.debug("ERROR", e);
		}

		// Also should do quick check to see if connection is active

		return true;
	}

	public static PHRAuthentication authenticate(String providerNo, String password) {
		// see authenticateIndivoId for exception explanation
		ProviderData providerData = new ProviderData();
		providerData.setProviderNo(providerNo);
		String indivoId = providerData.getMyOscarId();

		PHRAuthentication phrAuth = null;
		phrAuth = authenticateIndivoId(indivoId, password);
		if (phrAuth != null) phrAuth.setProviderNo(providerNo);
		return phrAuth;
	}

	private static PHRAuthentication authenticateIndivoId(String indivoId, String password) {
        try {
			LoginResultTransfer loginResultTransfer=MyOscarAccountManager.login(indivoId, password);
			
			PHRAuthentication phrAuth = new PHRAuthentication();
			phrAuth.setMyOscarUserName(indivoId);
			phrAuth.setMyOscarUserId(loginResultTransfer.getPerson().getId());
			phrAuth.setMyOscarPassword(loginResultTransfer.getSecurityTokenKey());

			return phrAuth;
        } catch (NotAuthorisedException_Exception e) {
			logger.debug("MyOscar Login failed:" + indivoId+":"+password);
			return(null);
        }
	}


	public void sendAddMedication(EctProviderData.Provider prov, String demographicNo, Long demographicMyOscarUserId, RxPrescriptionData.Prescription drug) throws Exception {
		logger.debug("sendAddMedication:" + prov + ", " + demographicNo + ", " + demographicMyOscarUserId + ", " + drug);

		PHRMedication medication = new PHRMedication(prov, demographicNo, demographicMyOscarUserId, drug);
		PHRAction action = medication.getAction(PHRAction.ACTION_ADD, PHRAction.STATUS_SEND_PENDING);
		action.setOscarId(drug.getDrugId() + "");
		// write action to phr_actions table
		phrActionDAO.save(action);
	}

	public void sendUpdateMedication(EctProviderData.Provider prov, String demographicNo, Long demographicMyOscarUserId, RxPrescriptionData.Prescription drug, String phrDrugIndex) throws Exception {
		logger.debug("sendAddMedication:" + prov + ", " + demographicNo + ", " + demographicMyOscarUserId + ", " + drug + ", " + phrDrugIndex);

		PHRMedication medication = new PHRMedication(prov, demographicNo, demographicMyOscarUserId, drug);
		PHRAction action = medication.getAction(PHRAction.ACTION_UPDATE, PHRAction.STATUS_SEND_PENDING);
		// set which phrIndex to update
		action.setPhrIndex(phrDrugIndex);
		action.setOscarId(drug.getDrugId() + "");
		// write action to phr_actions table
		phrActionDAO.save(action);
	}

	public void sendAddDocument(PHRDocument document, String oscarId) {
		logger.debug("sendAddDocument:" + document + ", " + oscarId);

		PHRAction action = document.getAction(PHRAction.ACTION_ADD, PHRAction.STATUS_SEND_PENDING);
		action.setOscarId(oscarId);
		// write action to phr_actions table
		phrActionDAO.save(action);
	}

	public void sendUpdateDocument(PHRDocument document, String phrIndex, String oscarIndex) {
		logger.debug("sendUpdateDocument:" + document + ", " + phrIndex + ", " + oscarIndex);

		PHRAction action = document.getAction(PHRAction.ACTION_UPDATE, PHRAction.STATUS_SEND_PENDING);
		// set which phrIndex to update
		action.setPhrIndex(phrIndex);
		action.setOscarId(oscarIndex);
		// write action to phr_actions table
		phrActionDAO.save(action);
	}

	public void sendAddMessage(String subject, String priorThreadMessage, String messageBody, ProviderData sender, String recipientOscarId, int recipientType, Long myOscarUserId) throws Exception {
		sendAddMessage(subject, priorThreadMessage, messageBody, sender, recipientOscarId, recipientType, myOscarUserId, new ArrayList<String>());
	}

	public void sendAddMessage(String subject, String priorThreadMessage, String messageBody, ProviderData sender, String recipientOscarId, int recipientType, Long myOscarUserId, List<String> attachmentActionIds) throws Exception {
		logger.debug("sendAddMessage:" + subject + ", " + priorThreadMessage + ", " + messageBody + ", " + sender + ", " + recipientOscarId + ", " + recipientType + ", " + myOscarUserId + ", " + attachmentActionIds);

		PHRMessage message = new PHRMessage(subject, priorThreadMessage, messageBody, sender, recipientOscarId, recipientType, myOscarUserId, attachmentActionIds);
		PHRAction action = message.getAction(PHRAction.ACTION_ADD, PHRAction.STATUS_SEND_PENDING);
		phrActionDAO.save(action);
	}

	public void sendUpdateMessage(PHRMessage msg) {
		logger.debug("sendUpdateMessage:" + msg);

		PHRAction action = msg.getAction2(PHRAction.ACTION_UPDATE, PHRAction.STATUS_SEND_PENDING);
		phrActionDAO.save(action);
	}

	public boolean isIndivoRegistered(String classification, String oscarId) {
		return phrActionDAO.isIndivoRegistered(classification, oscarId);
	}

	public String getPhrIndex(String classification, String oscarId) {
		return phrActionDAO.getPhrIndex(classification, oscarId);
	}


	public void saveMed(PHRMedication m) throws Exception {
		PHRDocument phrdoc = new PHRDocument();
		BeanUtils.copyProperties(phrdoc, m);
		phrDocumentDAO.save(phrdoc);// save to phr document table!
		// Drug d=m.getDrug();
		// DrugDao drugDao = (DrugDao) SpringUtils.getBean("drugDao");
		// drugDao.addNewDrug(d);
	}

	// retrieve meds from phr and save in phr_document table
	public List<PHRMedication> retrieveSaveMedToDisplay(PHRAuthentication auth, String providerNo, String demoId, Long myOscarUserId) throws Exception {

		MedicalDataWs medicalDataWs = MyOscarServerWebServicesManager.getMedicalDataWs(auth.getMyOscarUserId(), auth.getMyOscarPassword());
		ArrayList<PHRMedication> phrMedications = new ArrayList<PHRMedication>();

		int startIndex = 0;
		int itemsToReturn = 100;
		List<MedicalDataTransfer3> medicationTransfers = null;
		do {
			medicationTransfers = medicalDataWs.getMedicalDataByType(myOscarUserId, MedicalDataType.MEDICATION.name(), true, startIndex, itemsToReturn);
			
			for (MedicalDataTransfer3 medicalDataTransfer : medicationTransfers) {
				medicalDataTransfer=MyOscarMedicalDataManagerUtils.materialiseDataIfRequired(auth, medicalDataTransfer);
				
				boolean importStatus = checkImportStatus(medicalDataTransfer.getId().toString());// check if document has been imported before
				Boolean sendByOscarBefore = isMedSentBefore(medicalDataTransfer.getId().toString());// check if this document was sent by this oscar before.
				logger.debug("medicalDataTransfer: importStatus=" + importStatus + ", sentBefore=" + sendByOscarBefore);
				if (importStatus && !sendByOscarBefore) {
					PHRMedication med = new PHRMedication(medicalDataTransfer, demoId, myOscarUserId, providerNo);
					phrMedications.add(med);
					saveMed(med);
				}
			}

			startIndex = startIndex + itemsToReturn;

		} while (medicationTransfers.size() >= itemsToReturn && startIndex < 5000); // 5000 is an arbitary limit for now

		return (phrMedications);
	}

	// check if this medication has been sent by this oscar
	public Boolean isMedSentBefore(String documentIndex) {
		return phrActionDAO.isActionPresentByPhrIndex(documentIndex);
	}

	public boolean checkImportStatus(String documentIndex) {
		return !phrDocumentDAO.hasIndex(documentIndex);
	}

	public boolean checkClassification(String classification) {
		boolean classImported = false;

		if ("urn:org:indivo:document:classification:message".equalsIgnoreCase(classification)) {
			classImported = true;
		}
		return classImported;
	}

	public void sendDemographicMessage(PHRAuthentication auth, String demographic, String priorThreadMessage, String subject, String messageText) {
// this code can't possibly be run, its using the wrong client
		
//		DemographicData dd = new DemographicData();
//		org.oscarehr.common.model.Demographic demo = dd.getDemographic(demographic);
//		String recipientId = demo.getIndivoId();
//		try {
//			TalkClient client = getTalkClient();
//			SendMessageResultType sendMessageResultType = client.sendMessage(auth.getToken(), recipientId, priorThreadMessage, subject, messageText);
//		} catch (Exception e) {
//			MiscUtils.getLogger().error("Error", e);
//		}
	}


	public int countUnreadMessages(String providerNo) {
		return phrDocumentDAO.countUnreadDocuments("MESSAGE", providerNo);
	}

	public boolean hasUnreadMessages(String providerNo) {
		if (countUnreadMessages(providerNo) > 0) return true;
		return false;
	}

	/**
	 * @return the myOscarUserId of the created user.
	 * @throws Exception
	 */
	public PersonTransfer2 sendUserRegistration(PHRAuthentication auth, HashMap<String, Object> phrRegistrationForm) throws Exception {

		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(auth.getMyOscarUserId(), auth.getMyOscarPassword());
		PersonTransfer2 newAccount = new PersonTransfer2();
		newAccount.setUserName((String) phrRegistrationForm.get("username"));
		newAccount.setRole(Role.PATIENT.name());
		newAccount.setFirstName((String) phrRegistrationForm.get("firstName"));
		newAccount.setLastName((String) phrRegistrationForm.get("lastName"));
		newAccount.setStreetAddress1((String) phrRegistrationForm.get("address"));
		newAccount.setCity((String) phrRegistrationForm.get("city"));
		newAccount.setProvince((String) phrRegistrationForm.get("province"));
		newAccount.setPostalCode((String) phrRegistrationForm.get("postal"));
		newAccount.setPhone1((String) phrRegistrationForm.get("phone"));
		newAccount.setPhone2((String) phrRegistrationForm.get("phone2"));
		newAccount.setEmailAddress((String) phrRegistrationForm.get("email"));

		String iDob = (String) phrRegistrationForm.get("dob");
		if (iDob != null) {
			String[] split = iDob.split("[/\\-\\.]");
			if (split.length == 3) {
				GregorianCalendar cal = new GregorianCalendar(Integer.parseInt(split[0]), Integer.parseInt(split[1]) - 1, Integer.parseInt(split[2]));
				newAccount.setBirthDate(cal);
			}
		}

		String newAccountPassword = (String) phrRegistrationForm.get("password");

		// if no password is set, we'll make one up, the nano time is to ensure it's not guessable.
		if (newAccountPassword == null || newAccountPassword.length() == 0) newAccountPassword = newAccount.getUserName() + System.nanoTime();

		newAccount = accountWs.addPerson2(newAccount, newAccountPassword);

		if (newAccount == null) throw (new Exception("Error creating new Myoscar Account."));

		return (newAccount);
	}

	// private void getDocument(

	public void setPhrDocumentDAO(PHRDocumentDAO phrDocumentDAO) {
		this.phrDocumentDAO = phrDocumentDAO;

	}

	public void setPhrActionDAO(PHRActionDAO phrActionDAO) {
		this.phrActionDAO = phrActionDAO;
	}

	public void approveAction(PHRAction action) {
		// not used, but the idea is there
		action.setStatus(PHRAction.STATUS_SEND_PENDING);
		phrActionDAO.update(action);
	}

	public void denyAction(PHRAction action) {
		action.setStatus(PHRAction.STATUS_NOT_SENT_DELETED);
		phrActionDAO.update(action);
	}

	public PHRActionDAO getPhrActionDao() {
		return phrActionDAO;
	}

	public PHRDocumentDAO getPhrDocumentDAO() {
		return phrDocumentDAO;
	}

	public static void main(String... argv) {
		String a = "2011-02-03";
		String b = "2011/02/03";
		String c = "2011.02.03";
		String d = "2011#02#03";

		String regex = "[/\\-\\.]";

		logger.info(Arrays.toString(a.split(regex)));
		logger.info(Arrays.toString(b.split(regex)));
		logger.info(Arrays.toString(c.split(regex)));
		logger.info(Arrays.toString(d.split(regex)));
	}
}
