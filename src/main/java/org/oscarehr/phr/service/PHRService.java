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

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.indivo.IndivoException;
import org.indivo.client.ActionNotPerformedException;
import org.indivo.client.TalkClient;
import org.indivo.client.TalkClientImpl;
import org.indivo.xml.phr.annotation.DocumentReferenceType;
import org.indivo.xml.phr.document.DocumentClassificationType;
import org.indivo.xml.phr.document.DocumentHeaderType;
import org.indivo.xml.phr.document.DocumentVersionType;
import org.indivo.xml.phr.document.IndivoDocumentType;
import org.indivo.xml.phr.message.MessageType;
import org.indivo.xml.phr.record.IndivoRecordType;
import org.indivo.xml.talk.ReadDocumentHeaderListResultType;
import org.indivo.xml.talk.ReadDocumentResultType;
import org.indivo.xml.talk.ReadResultType;
import org.indivo.xml.talk.SendMessageResultType;
import org.oscarehr.common.service.myoscar.MyOscarMedicalDataManagerUtils;
import org.oscarehr.myoscar.managers.MyOscarAccountManager;
import org.oscarehr.myoscar_server.ws.AccountWs;
import org.oscarehr.myoscar_server.ws.LoginResultTransfer;
import org.oscarehr.myoscar_server.ws.MedicalDataTransfer3;
import org.oscarehr.myoscar_server.ws.MedicalDataType;
import org.oscarehr.myoscar_server.ws.MedicalDataWs;
import org.oscarehr.myoscar_server.ws.MessageWs;
import org.oscarehr.myoscar_server.ws.NotAuthorisedException_Exception;
import org.oscarehr.myoscar_server.ws.PersonTransfer;
import org.oscarehr.myoscar_server.ws.Relation;
import org.oscarehr.myoscar_server.ws.Role;
import org.oscarehr.myoscar_server.ws.UnsupportedEncodingException_Exception;
import org.oscarehr.phr.PHRAuthentication;
import org.oscarehr.phr.dao.PHRActionDAO;
import org.oscarehr.phr.dao.PHRDocumentDAO;
import org.oscarehr.phr.indivo.model.PHRIndivoAnnotation;
import org.oscarehr.phr.indivo.model.PHRIndivoDocument;
import org.oscarehr.phr.model.PHRAction;
import org.oscarehr.phr.model.PHRBinaryData;
import org.oscarehr.phr.model.PHRDocument;
import org.oscarehr.phr.model.PHRMedication;
import org.oscarehr.phr.model.PHRMessage;
import org.oscarehr.phr.util.MyOscarServerWebServicesManager;
import org.oscarehr.phr.util.MyOscarUtils;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.XmlUtils;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import oscar.OscarProperties;
import oscar.dms.EDoc;
import oscar.dms.EDocUtil;
import oscar.oscarDemographic.data.DemographicData;
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

	public Integer sendAddBinaryData(PHRAuthentication auth, ProviderData sender, String recipientOscarId, int recipientType, Long recipientMyOscarUserId, EDoc document) throws Exception {
		logger.debug("sendAddBinaryData:" + sender + ", " + recipientOscarId + ", " + recipientType + ", " + recipientMyOscarUserId + ", " + document);

		PHRBinaryData phrBinaryData = new PHRBinaryData(auth, sender, recipientOscarId, recipientType, recipientMyOscarUserId, document);
		PHRAction action = phrBinaryData.getAction(PHRAction.ACTION_ADD, PHRAction.STATUS_SEND_PENDING);
		action.setOscarId(document.getDocId());
		// write action to phr_actions table
		return phrActionDAO.saveAndGetId(action);
	}

	public void sendAddAnnotation(ProviderData sender, String recipientOscarId, Long recipientPhrId, String documentReferenceOscarActionId, String message) throws Exception {
		logger.debug("sendAddAnnotation:" + sender + ", " + recipientOscarId + ", " + recipientPhrId + ", " + documentReferenceOscarActionId + ", " + message);

		PHRIndivoAnnotation phrAnnotation = new PHRIndivoAnnotation(sender, recipientOscarId, recipientPhrId, documentReferenceOscarActionId, message);
		PHRAction action = phrAnnotation.getAction(PHRAction.ACTION_ADD, PHRAction.STATUS_SEND_PENDING);
		// write action to phr_actions table

		Integer oscarId = phrActionDAO.saveAndGetId(action);
		action.setOscarId(String.valueOf(oscarId)); // guaranteed unique, but there is no annotation object in oscar, so can't assign proper oscarId
		phrActionDAO.update(action);
	}

	public void sendUpdateBinaryData(PHRAuthentication auth, ProviderData sender, String recipientOscarId, int recipientType, Long recipientMyOscarUserId, EDoc document, String phrDocIndex) throws Exception {
		logger.debug("sendUpdateBinaryData:" + sender + ", " + recipientOscarId + ", " + recipientType + ", " + recipientMyOscarUserId + ", " + document + ", " + phrDocIndex);

		PHRBinaryData phrBinaryData = new PHRBinaryData(auth, sender, recipientOscarId, recipientType, recipientMyOscarUserId, document);
		PHRAction action = phrBinaryData.getAction(PHRAction.ACTION_UPDATE, PHRAction.STATUS_SEND_PENDING);
		action.setOscarId(document.getDocId());

		// set which phrIndex to update
		action.setPhrIndex(phrDocIndex);

		// write action to phr_actions table
		phrActionDAO.save(action);
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

	public void retrieveDocuments2(PHRAuthentication auth, String providerNo) throws Exception {
		logger.debug("In retrieveDocuments");
		TalkClient client = getTalkClient();
		ReadResultType readResult = client.readRecord(auth.getToken(), auth.getUserId());

		if (readResult != null) {
			IndivoRecordType record = readResult.getIndivoRecord();
			List<IndivoDocumentType> list = record.getIndivoDocument();
			logger.debug("getMessages:Msgs List has " + list.size());

			for (IndivoDocumentType document : list) {
				DocumentHeaderType docHeaderType = document.getDocumentHeader();
				DocumentClassificationType theType = docHeaderType.getDocumentClassification();
				String classification = theType.getClassification();
				String documentIndex = docHeaderType.getDocumentIndex();

				logger.debug("Document is of type " + classification + " index is " + documentIndex);

				// Check if this document has been imported before
				boolean importStatus = checkImportStatus(documentIndex);

				if (importStatus && checkClassification(classification)) {
					logger.debug("PASSED check ");
					PHRMessage mess = new PHRMessage(document);
					mess.checkImportStatus();
					PHRDocument phrdoc = new PHRDocument();
					BeanUtils.copyProperties(phrdoc, mess);
					phrDocumentDAO.save(phrdoc);
				} else if (importStatus) {
					logger.debug("need to check for updates on this message");
				}
			}
		}
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

	// seems to be getting all documents from myoscar, need to change this part
	public void retrieveDocuments(PHRAuthentication auth, String providerNo) throws Exception {
		logger.debug("In retrieveDocuments");
		TalkClient client = getTalkClient();
		ReadDocumentHeaderListResultType readResult = client.readDocumentHeaders(auth.getToken(), auth.getUserId(), org.indivo.xml.phr.urns.DocumentClassificationUrns.MESSAGE, true);
		List<DocumentHeaderType> docHeaders = readResult.getDocumentHeader();
		logger.debug("docheaders found: " + docHeaders.size());
		for (DocumentHeaderType docHeader : docHeaders) {
			// Retrieve each document
			logger.debug("Date Created set to " + new Date(docHeader.getCreationDateTime().toGregorianCalendar().getGregorianChange().getTime()));
			DocumentClassificationType theType = docHeader.getDocumentClassification();
			String classification = theType.getClassification();
			String documentIndex = docHeader.getDocumentIndex();
			logger.debug("Document is of type " + classification + " index is " + documentIndex);
			boolean importStatus = checkImportStatus(documentIndex);

			// first check if user is registered (making two separate checks - one for sent documents and one for received for faster performance)
			boolean sendingDocument = docHeader.getAuthor().getIndivoId().equals(auth.getUserId());
			DemographicData dd = new DemographicData();
			logger.debug("file created by: " + docHeader.getAuthor().getIndivoId());
			logger.debug("currentUser: " + auth.getUserId());
			logger.debug("sendingDocument: " + sendingDocument);

			if (!sendingDocument) {
				String myOscarUserName=MyOscarUtils.getMyOscarUserName(auth, Long.parseLong(docHeader.getAuthor().getIndivoId()));
				boolean userInOscar = !dd.getDemographicNoByMyOscarUserName(myOscarUserName).equals("");
				if (!userInOscar) continue;
			}
			if (importStatus) {
				logger.debug("PASSED check, retrieving the document...");
				// now get the actual document
				ReadDocumentResultType readDocResult = client.readDocument(auth.getToken(), auth.getUserId(), docHeader.getDocumentIndex(), true);
				IndivoDocumentType indivoDoc = readDocResult.getIndivoDocument();

				PHRMessage mess = new PHRMessage(indivoDoc);

				// second check for known demographics
				// this if statement can replace both checks if ((dd.getDemographic(mess.getSenderOscar()) == null) && (dd.getDemographic(mess.getReceiverOscar()) == null))
				String myOscarUserName=MyOscarUtils.getMyOscarUserName(auth, mess.getReceiverMyOscarUserId());
				if (sendingDocument && dd.getDemographicNoByMyOscarUserName(myOscarUserName).equals("")) continue;
				mess.checkImportStatus();
				PHRDocument phrdoc = new PHRDocument();
				BeanUtils.copyProperties(phrdoc, mess);
				phrDocumentDAO.save(phrdoc);// save to phr document table!
			} else {
				logger.debug("need to check for updates on this message");
			}
		}
	}

	public void sendQueuedDocuments(PHRAuthentication auth, String providerNo) throws Exception {
		// package sharing
		// IndivoAPService apService = new IndivoAPService(this);
		// apService.packageAllAccessPolicies(auth);

		List<PHRAction> actions = phrActionDAO.getQueuedActions(providerNo);
		logger.debug("Processing " + actions.size() + " actions ");

		// TalkClient client = getTalkClient();
		for (PHRAction action : actions) {

			boolean updated = false;
			Long resultId =null;
			// handle messages differently
			logger.debug("ACTION classification " + action.getPhrClassification() + " action type " + action.getActionType()+", phr_action.id="+action.getId());
			try {
				if (action.getPhrClassification().equalsIgnoreCase("MESSAGE") && (action.getActionType() == PHRAction.ACTION_ADD || action.getActionType() == PHRAction.ACTION_UPDATE)) {
					sendMessage(auth, action);
					updated = true;
				} else if (action.getPhrClassification().equalsIgnoreCase("RELATIONSHIP") && (action.getActionType() == PHRAction.ACTION_ADD || action.getActionType() == PHRAction.ACTION_UPDATE)) {
					sendRelationship(auth, action);
					updated = true;
				} else if (action.getActionType() == PHRAction.ACTION_ADD || action.getActionType() == PHRAction.ACTION_UPDATE) {// dealing with medication type document

					// if adding
					IndivoDocumentType doc = action.getIndivoDocument();
					if (action.getPhrClassification().equals(MedicalDataType.BINARY_DOCUMENT.name())) {
						doc = PHRBinaryData.mountDocument(action.getOscarId(), doc);

						MedicalDataWs medicalDataWs = MyOscarServerWebServicesManager.getMedicalDataWs(auth.getMyOscarUserId(), auth.getMyOscarPassword());
						logger.debug("sending medical data : " + action.getOscarId() + ", " + action.getDateSent() + ", " + action.getPhrClassification() + ", " + auth.getMyOscarUserId() + ", " + doc);

						GregorianCalendar dataTime = new GregorianCalendar();

						EDoc edoc = EDocUtil.getDoc(action.getOscarId());

						org.w3c.dom.Document xmlDocument = XmlUtils.newDocument("BinaryDocument");
						XmlUtils.appendChildToRoot(xmlDocument, "Filename", edoc.getFileName());
						XmlUtils.appendChildToRoot(xmlDocument, "FileDescription", edoc.getDescription());
						XmlUtils.appendChildToRoot(xmlDocument, "MimeType", edoc.getContentType());
						XmlUtils.appendChildToRoot(xmlDocument, "Data", edoc.getFileBytes());
						String xmlString = XmlUtils.toString(xmlDocument,false);

						if (doc.getDocumentHeader().getCreationDateTime() != null) dataTime = doc.getDocumentHeader().getCreationDateTime().toGregorianCalendar();

						MedicalDataTransfer3 medicalDataTransfer=new MedicalDataTransfer3();
						medicalDataTransfer.setActive(true);
						medicalDataTransfer.setCompleted(true);
						medicalDataTransfer.setData(xmlString);
						medicalDataTransfer.setDateOfData(dataTime);
						medicalDataTransfer.setMedicalDataType(action.getPhrClassification());
						medicalDataTransfer.setObserverOfDataPersonId(auth.getMyOscarUserId());
						
						LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
						medicalDataTransfer.setObserverOfDataPersonName(loggedInInfo.loggedInProvider.getFormattedName());
						medicalDataTransfer.setOriginalSourceId(loggedInInfo.currentFacility.getName()+":EDoc:"+edoc.getDocId());
						medicalDataTransfer.setOwningPersonId(action.getReceiverMyOscarUserId());
						
						resultId = medicalDataWs.addMedicalData3(medicalDataTransfer);
					} else if (action.getPhrClassification().equals("ANNOTATION")) {
						try {
							String referenceIndex = PHRIndivoAnnotation.getAnnotationReferenceIndex(doc);// temporarily stored
							PHRAction referencedDocumentAction = phrActionDAO.getActionById(referenceIndex);
							if (referencedDocumentAction == null) throw new Exception("Cannot find annotated document");
							if (referencedDocumentAction.getPhrIndex() == null) continue; // referenced document must be sent first
							doc = PHRIndivoAnnotation.mountReferenceDocument(referencedDocumentAction.getPhrClassification(), referencedDocumentAction.getPhrIndex(), referencedDocumentAction.getIndivoDocument().getDocumentVersion().size() - 1, doc);
							action = PHRIndivoDocument.setDocContent(doc, action);
							phrActionDAO.save(action);
						} catch (Exception e) {
							logger.error("Could not send the annotation with ID: '" + action.getId() + "' to PHR; skipping it...", e);
							action.setStatus(PHRAction.STATUS_OTHER_ERROR);
							phrActionDAO.update(action);
							continue; // if there is an error sending annotation, screw it...move on
						}
					} else if (action.getPhrClassification().equals(MedicalDataType.MEDICATION.name())) {

						
						MedicalDataWs medicalDataWs = MyOscarServerWebServicesManager.getMedicalDataWs(auth.getMyOscarUserId(), auth.getMyOscarPassword());

						GregorianCalendar dataTime = new GregorianCalendar();

						Node medicationNode=doc.getDocumentVersion().get(0).getVersionBody().getAny();

						org.w3c.dom.Document xmlDocument = XmlUtils.newDocument("Medication");
						Node xmlDocumentRootNode=xmlDocument.getFirstChild();
						
						NodeList nodeList=medicationNode.getChildNodes();
						for (int i=0; i<nodeList.getLength(); i++)
						{
							Node node = nodeList.item(i);
							node=xmlDocument.importNode(node, true);
							xmlDocumentRootNode.appendChild(node);
						}
						
						String xmlString = XmlUtils.toString(xmlDocument,false);

						logger.debug("sending medical data : " + action.getOscarId() + ", " + action.getDateSent() + ", " + action.getPhrClassification() + ", " + auth.getMyOscarUserId() + ", " + xmlString);

						if (doc.getDocumentHeader().getCreationDateTime() != null) dataTime = doc.getDocumentHeader().getCreationDateTime().toGregorianCalendar();

						MedicalDataTransfer3 medicalDataTransfer=new MedicalDataTransfer3();
						medicalDataTransfer.setActive(true);
						medicalDataTransfer.setCompleted(true);
						medicalDataTransfer.setData(xmlString);
						medicalDataTransfer.setDateOfData(dataTime);
						medicalDataTransfer.setMedicalDataType(action.getPhrClassification());
						medicalDataTransfer.setObserverOfDataPersonId(auth.getMyOscarUserId());

						LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
						medicalDataTransfer.setObserverOfDataPersonName(loggedInInfo.loggedInProvider.getFormattedName());
						medicalDataTransfer.setOriginalSourceId(loggedInInfo.currentFacility.getName()+":medication:"+action.getOscarId());
						medicalDataTransfer.setOwningPersonId(action.getReceiverMyOscarUserId());

						resultId = medicalDataWs.addMedicalData3(medicalDataTransfer);
					}

					// AddDocumentResultType result = client.addDocument(auth.getToken(), action.getReceiverPhr(), doc);
					String resultIndex = resultId.toString();
					action.setPhrIndex(resultId.toString());
					// updates indexes to handle the case where two operations on this file are queued
					phrActionDAO.updatePhrIndexes(action.getPhrClassification(), action.getOscarId(), action.getSenderOscar(), resultIndex);
					actions = PHRAction.updateIndexes(action.getPhrClassification(), action.getOscarId(), resultIndex, actions);
					updated = true;
					// if updating

				} else if (action.getPhrClassification().equalsIgnoreCase("MESSAGE") && action.getActionType() == PHRAction.ACTION_UPDATE) {
					logger.info("excuse me but since when can you ever update a message that's been sent? no messaging system allows that.");

					// logger.debug("HERE MESSAGE UPDATE");
					// org.indivo.xml.JAXBUtils jaxbUtils = new org.indivo.xml.JAXBUtils();
					//
					// IndivoDocumentType document = action.getIndivoDocument();
					//
					// JAXBContext messageContext = JAXBContext.newInstance("org.indivo.xml.phr.message");
					// MessageType msg = (MessageType) org.indivo.xml.phr.DocumentUtils.getDocumentAnyObject(document, messageContext.createUnmarshaller());
					// // ??? Should i use reflection to abstract the call to ObjectFactory??? how will in know what method to call? the one that will take MessageType as a param???
					// logger.debug("IS READ " + msg.isRead());
					// Element element = jaxbUtils.marshalToElement(new org.indivo.xml.phr.message.ObjectFactory().createMessage(msg), JAXBContext.newInstance("org.indivo.xml.phr.message"));
					//
					// DocumentVersionGenerator dvg = new DocumentVersionGenerator();
					// DocumentVersionType newVersion = dvg.generateDefaultDocumentVersion(auth.getUserId(), auth.getName(), auth.getRole(), element);
					// logger.debug("BEFORE UPDATE DOCUMENT calling with token " + auth.getToken() + " id " + auth.getUserId() + " idx " + action.getPhrIndex() + " v " + newVersion);
					// UpdateDocumentResultType updateDocRes = client.updateDocument(auth.getToken(), auth.getUserId(), action.getPhrIndex(), newVersion);
					// if (updateDocRes == null) {
					// logger.debug("UPDATE DOC IS NULL");
					// } else {
					// logger.debug("UPDATE DOC IS NOT NULL" + updateDocRes.toString());
					// }
					// logger.debug("AFTER UPDATE DOCUMENT");
					// updated = true;

				} else if (action.getActionType() == PHRAction.ACTION_UPDATE) {
					logger.info("sorry but there's no such thing as update for relationship / medical documents / messages, you can create or delete them only.");

					// logger.debug("else 2");
					// if (action.getPhrIndex() == null && !action.getPhrClassification().equals(PHRConstants.DOCTYPE_ACCESSPOLICIES())) throw new Exception("Error: PHR index not set");
					//
					// if (action.getPhrClassification().equals(PHRConstants.DOCTYPE_ACCESSPOLICIES())) action = apService.packageAccessPolicy(auth, action);
					// IndivoDocumentType doc = action.getIndivoDocument();
					// if (action.getPhrClassification().equals(PHRConstants.DOCTYPE_BINARYDATA())) doc = PHRBinaryData.mountDocument(action.getOscarId(), doc);
					// Element documentElement = DocumentUtils.getDocumentAnyElement(doc);
					// // Retrieve current file record from indivo
					// logger.debug("phr index " + action.getPhrIndex());
					// // ReadDocumentResultType readResult = client.readDocument(auth.getToken(), action.getSenderPhr(), action.getPhrIndex());
					// // IndivoDocumentType phrDoc = readResult.getIndivoDocument();
					//
					// IndivoDocumentType phrDoc = null;
					// if (action.getPhrClassification().equals(PHRConstants.DOCTYPE_MESSAGE())) {
					// PHRDocument phrd = phrDocumentDAO.getDocumentByIndex(action.getPhrIndex());
					// JAXBContext docContext = JAXBContext.newInstance(IndivoDocumentType.class.getPackage().getName());
					// Unmarshaller unmarshaller = docContext.createUnmarshaller();
					//
					// JAXBElement jaxment = (JAXBElement) unmarshaller.unmarshal(new StringReader(phrd.getDocContent()));
					// phrDoc = (IndivoDocumentType) jaxment.getValue();
					// } else {
					// ReadDocumentResultType readResult = client.readDocument(auth.getToken(), action.getReceiverPhr(), action.getPhrIndex());
					// phrDoc = readResult.getIndivoDocument();
					// }
					//
					// DocumentVersionType version = phrDoc.getDocumentVersion().get(phrDoc.getDocumentVersion().size() - 1);
					//
					// // send new version
					// VersionBodyType body = version.getVersionBody();
					// body.setAny(documentElement);
					// version.setVersionBody(body);
					// if (action.getPhrClassification().equals(PHRConstants.DOCTYPE_MESSAGE())) {
					// client.updateDocument(auth.getToken(), auth.getUserId(), action.getPhrIndex(), version);
					// } else {
					// client.updateDocument(auth.getToken(), action.getReceiverPhr(), action.getPhrIndex(), version);
					// }
					// updated = true;
				} else {
					logger.debug("NOTHING IS GETTING CALLED FOR THIS ");

				}
			} catch (ActionNotPerformedException anpe) {
				// assuming user does not have authorization for the action - in this case mark it as unauthorized and stop trying to send
				logger.debug("Setting Status Not Authorized");
				action.setStatus(PHRAction.STATUS_NOT_AUTHORIZED);
				phrActionDAO.update(action);
			} catch (IndivoException ie) {
				// assuming connection problems - in this case log the user off to take load off the server
				logger.debug("IndivoException thrown");
				throw new Exception(ie);
			} catch (Exception e) {
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
				logger.error("Exception Thrown that is not due to connection or Authorization...probably jaxb problem " + formatter.format(new Date()), e);
				updated = false;
				action.setStatus(PHRAction.STATUS_OTHER_ERROR);
				phrActionDAO.update(action);
			}
			if (updated) {
				action.setStatus(PHRAction.STATUS_SENT);
				phrActionDAO.update(action);
			}
		}
	}

	private void sendRelationship(PHRAuthentication auth, PHRAction action) {
		try {
	        AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(auth.getMyOscarUserId(), auth.getMyOscarPassword());
	        
	        String[] temp=action.getDocContent().split(",");
	        String relatedPerson=StringUtils.trimToNull(temp[0]);
	        String relationString=StringUtils.trimToNull(temp[1]);
	        Relation relation=Relation.valueOf(relationString);
	        
	        accountWs.createRelationshipByUserName(auth.getMyOscarUserName(), relatedPerson, relation);
        } catch (Exception e) {
	       logger.error("Error with action entry, actionId="+action.getId(), e);
        }	    
    }

	private void sendMessage(PHRAuthentication auth, PHRAction action) throws JAXBException, IndivoException, NumberFormatException, DOMException, NotAuthorisedException_Exception, UnsupportedEncodingException_Exception {
		MessageWs messageWs = MyOscarServerWebServicesManager.getMessageWs(auth.getMyOscarUserId(), auth.getMyOscarPassword());

		logger.debug("Sending Add Message");
		IndivoDocumentType doc = action.getIndivoDocument();
		logger.debug("doc is null?? " + doc);
		JAXBContext messageContext = JAXBContext.newInstance(MessageType.class.getPackage().getName());
		MessageType msg = (MessageType) org.indivo.xml.phr.DocumentUtils.getDocumentAnyObject(doc, messageContext.createUnmarshaller());
		logger.debug("doc is msg?? " + msg);
		// need to set the document index on attachments (don't know phr index at time of attachment)
		for (DocumentReferenceType attachment : msg.getReferencedIndivoDocuments()) {
			logger.debug("attaching document");
			String actionId = attachment.getDocumentIndex();
			PHRAction attachedDocumentAction = phrActionDAO.getActionById(actionId); // assuming aleady sent...
			if (attachedDocumentAction == null) continue;
			if (attachedDocumentAction.getPhrIndex() == null) continue; // attachment must be sent first
			attachment.setDocumentIndex(attachedDocumentAction.getPhrIndex());
			attachment.setClassification(attachedDocumentAction.getPhrClassification());
			attachment.setVersion(new BigInteger((attachedDocumentAction.getIndivoDocument().getDocumentVersion().size() - 1) + "")); // latest

			// wow nothing is actually done here... *shrugs*
		}
		// update action if updated phr indexes in the attached document reference
		if (msg.getReferencedIndivoDocuments().size() > 0) {
			action.setIndivoDocument(PHRMessage.getPhrMessageDocument(auth.getMyOscarUserId().toString(), auth.getNamePHRFormat(), msg));
			phrActionDAO.update(action);
		}

		// client.sendMessage(auth.getToken(), msg);
		messageWs.sendMessage(Long.parseLong(msg.getRecipient()), msg.getSubject(), msg.getMessageContent().getAny().getTextContent());
		logger.debug("message is going to " + msg.getRecipient());
		// client.sendMessage(auth.getToken(),msg.getRecipient(),msg.getPriorThreadMessageId(),msg.getSubject(),msg.getMessageContent().getAny().getTextContent() );
	}

	protected List<DocumentHeaderType> getDocumentHeadersDirect(PHRAuthentication auth, String urn) throws IndivoException, ActionNotPerformedException {
		TalkClient client = getTalkClient();
		ReadDocumentHeaderListResultType readResult = client.readDocumentHeaders(auth.getToken(), auth.getUserId(), urn, true);
		return readResult.getDocumentHeader();
	}

	// versionNumber = -1 for latest version
	protected DocumentVersionType getDocumentVersionDirect(PHRAuthentication auth, String index, int versionNumber) throws Exception {
		TalkClient client = getTalkClient();
		ReadDocumentResultType readResult = client.readDocument(auth.getToken(), auth.getUserId(), index, true);
		IndivoDocumentType indivoDoc = readResult.getIndivoDocument();
		List<DocumentVersionType> docVersions = indivoDoc.getDocumentVersion();
		DocumentVersionType docVersion = new DocumentVersionType();
		if (versionNumber == -1) {
			docVersion = docVersions.get(docVersions.size() - 1);
		} else {
			docVersion = docVersions.get(versionNumber);
		}

		return docVersion;
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

	protected void updateDocumentDirect(PHRAuthentication auth, DocumentVersionType newDocumentVersion, String urn, String documentIndex) throws IndivoException, ActionNotPerformedException {
		TalkClient client = getTalkClient();
		client.updateDocument(auth.getToken(), auth.getUserId(), documentIndex, newDocumentVersion);
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

	public void sendMessage(PHRAuthentication auth, String recipientId, String priorThreadMessage, String subject, String messageText) {
		try {
			TalkClient client = getTalkClient();
			SendMessageResultType sendMessageResultType = client.sendMessage(auth.getToken(), recipientId, priorThreadMessage, subject, messageText);
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
		}
	}

	public int countUnreadMessages(String providerNo) {
		return phrDocumentDAO.countUnreadDocuments("MESSAGE", providerNo);
	}

	public boolean hasUnreadMessages(String providerNo) {
		if (countUnreadMessages(providerNo) > 0) return true;
		return false;
	}

	private TalkClient getTalkClient() throws IndivoException {
		HashMap<String, String> m = new HashMap<String, String>();
		String indivoServer = OscarProperties.getInstance().getProperty("INDIVO_SERVER");
		m.put(TalkClient.SERVER_LOCATION, indivoServer);
		m.put(TalkClient.CERT_TRUST_KEY, "all");
		TalkClient client = new TalkClientImpl(m);
		return client;
	}

	TalkClient getTalkClient(String providerNo) throws Exception {
		return getTalkClient();
	}

	TalkClient getTalkClient(String providerNo, String demographicNo) throws Exception {
		return getTalkClient();

	}

	/**
	 * @return the myOscarUserId of the created user.
	 * @throws Exception
	 */
	public PersonTransfer sendUserRegistration(PHRAuthentication auth, HashMap<String, Object> phrRegistrationForm) throws Exception {

		AccountWs accountWs = MyOscarServerWebServicesManager.getAccountWs(auth.getMyOscarUserId(), auth.getMyOscarPassword());
		PersonTransfer newAccount = new PersonTransfer();
		newAccount.setUserName((String) phrRegistrationForm.get("username"));
		newAccount.setRole(Role.PATIENT);
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

		newAccount = accountWs.addPerson(newAccount, newAccountPassword);

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
