/*
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 *  This software is published under the GPL GNU General Public License.
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 *  Jason Gallagher
 *
 *  This software was written for the
 *  Department of Family Medicine
 *  McMaster University
 *  Hamilton
 *  Ontario, Canada
 *
 * IndivoServiceImpl.java
 *
 * Created on April 27, 2007, 4:24 PM
 *
 */

package org.oscarehr.phr.indivo.service.impl;

import java.io.StringReader;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.indivo.IndivoException;
import org.indivo.client.ActionNotPerformedException;
import org.indivo.client.TalkClient;
import org.indivo.client.TalkClientImpl;
import org.indivo.xml.JAXBUtils;
import org.indivo.xml.phr.DocumentGenerator;
import org.indivo.xml.phr.DocumentUtils;
import org.indivo.xml.phr.DocumentVersionGenerator;
import org.indivo.xml.phr.RecordGenerator;
import org.indivo.xml.phr.annotation.DocumentReferenceType;
import org.indivo.xml.phr.contact.ContactInformation;
import org.indivo.xml.phr.contact.ContactInformationType;
import org.indivo.xml.phr.demographics.Demographics;
import org.indivo.xml.phr.demographics.DemographicsType;
import org.indivo.xml.phr.document.DocumentClassificationType;
import org.indivo.xml.phr.document.DocumentHeaderType;
import org.indivo.xml.phr.document.DocumentVersionType;
import org.indivo.xml.phr.document.IndivoDocumentType;
import org.indivo.xml.phr.document.VersionBodyType;
import org.indivo.xml.phr.message.MessageType;
import org.indivo.xml.phr.record.IndivoRecordType;
import org.indivo.xml.phr.urns.ContentTypeQNames;
import org.indivo.xml.phr.urns.DocumentClassificationUrns;
import org.indivo.xml.talk.AddDocumentResultType;
import org.indivo.xml.talk.AuthenticateResultType;
import org.indivo.xml.talk.ReadDocumentHeaderListResultType;
import org.indivo.xml.talk.ReadDocumentResultType;
import org.indivo.xml.talk.ReadResultType;
import org.indivo.xml.talk.SendMessageResultType;
import org.indivo.xml.talk.UpdateDocumentResultType;
import org.oscarehr.phr.PHRAuthentication;
import org.oscarehr.phr.PHRConstants;
import org.oscarehr.phr.dao.PHRActionDAO;
import org.oscarehr.phr.dao.PHRDocumentDAO;
import org.oscarehr.phr.indivo.IndivoAuthentication;
import org.oscarehr.phr.indivo.IndivoUtil;
import org.oscarehr.phr.indivo.model.PHRIndivoAnnotation;
import org.oscarehr.phr.indivo.model.PHRIndivoDocument;
import org.oscarehr.phr.indivo.service.accesspolicies.IndivoAPService;
import org.oscarehr.phr.model.PHRAction;
import org.oscarehr.phr.model.PHRBinaryData;
import org.oscarehr.phr.model.PHRDocument;
import org.oscarehr.phr.model.PHRMedication;
import org.oscarehr.phr.model.PHRMessage;
import org.oscarehr.phr.service.PHRService;
import org.oscarehr.util.MiscUtils;
import org.w3c.dom.Element;

import oscar.OscarProperties;
import oscar.dms.EDoc;
import oscar.oscarDemographic.data.DemographicData;
import oscar.oscarEncounter.data.EctProviderData;
import oscar.oscarProvider.data.ProviderData;
import oscar.oscarProvider.data.ProviderMyOscarIdData;
import oscar.oscarRx.data.RxPrescriptionData;

/**
 * @author jay
 */
public class IndivoServiceImpl implements PHRService {

	private static final Logger logger = MiscUtils.getLogger();
	protected PHRDocumentDAO phrDocumentDAO;
	protected PHRActionDAO phrActionDAO;

	public boolean canAuthenticate(String providerNo) {
		String myOscarLoginId = ProviderMyOscarIdData.getMyOscarId(providerNo);
		if (myOscarLoginId == null) {
			return false;
		} else {
			return true;
		}
	}

	public boolean validAuthentication(PHRAuthentication auth) {

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

	public PHRAuthentication authenticate(String providerNo, String password) throws Exception {
		// see authenticateIndivoId for exception explanation
		ProviderData providerData = new ProviderData();
		PHRAuthentication phrAuth = null;
		providerData.setProviderNo(providerNo);
		String indivoId = providerData.getMyOscarId();
		phrAuth = authenticateIndivoId(indivoId, password);
		phrAuth.setProviderNo(providerNo);
		return phrAuth;
	}

	// used to authenticate demographics and perhaps admin account
	protected PHRAuthentication authenticateIndivoId(String indivoId, String password) throws Exception {
		// Caution: does not set provider number in PHRAuthentication object
		PHRAuthentication phrAuth = null;
		TalkClient client = getTalkClient(); // also throws Exception
		AuthenticateResultType authResult = client.authenticate(indivoId, password);
		// Throws IndivoException & ActionNotPerformedException
		// could be incorrect password or server down
		// distinguish like this: if (e.getCause() != null && e.getCause().getClass() == java.net.ConnectException.class)
		logger.debug("actor ticket " + authResult.getActorTicket());
		phrAuth = new IndivoAuthentication(authResult);
		return phrAuth;

		/*
		 * @throws ActionNotPerformedException If there is a null result. Can be caused for a number of reasons such as: the actor performing the action isn't authorized, the record the action is being performed on doesn't exist, the password is incorrect,
		 * etc.
		 * 
		 * @throws IndivoException If there is an error in the communication to the server.
		 */
	}

	public Integer sendAddBinaryData(ProviderData sender, String recipientOscarId, int recipientType, String recipientPhrId, EDoc document) throws Exception {
		PHRBinaryData phrBinaryData = new PHRBinaryData(sender, recipientOscarId, recipientType, recipientPhrId, document);
		PHRAction action = phrBinaryData.getAction(PHRAction.ACTION_ADD, PHRAction.STATUS_SEND_PENDING);
		action.setOscarId(document.getDocId());
		// write action to phr_actions table
		return phrActionDAO.saveAndGetId(action);
	}

	public void sendAddAnnotation(ProviderData sender, String recipientOscarId, String recipientPhrId, String documentReferenceOscarActionId, String message) throws Exception {
		PHRIndivoAnnotation phrAnnotation = new PHRIndivoAnnotation(sender, recipientOscarId, recipientPhrId, documentReferenceOscarActionId, message);
		PHRAction action = phrAnnotation.getAction(PHRAction.ACTION_ADD, PHRAction.STATUS_SEND_PENDING);
		// write action to phr_actions table

		Integer oscarId = phrActionDAO.saveAndGetId(action);
		action.setOscarId(String.valueOf(oscarId)); // guaranteed unique, but there is no annotation object in oscar, so can't assign proper oscarId
		phrActionDAO.update(action);
	}

	public void sendUpdateBinaryData(ProviderData sender, String recipientOscarId, int recipientType, String recipientPhrId, EDoc document, String phrDocIndex) throws Exception {
		PHRBinaryData phrBinaryData = new PHRBinaryData(sender, recipientOscarId, recipientType, recipientPhrId, document);
		PHRAction action = phrBinaryData.getAction(PHRAction.ACTION_UPDATE, PHRAction.STATUS_SEND_PENDING);
		action.setOscarId(document.getDocId());

		// set which phrIndex to update
		action.setPhrIndex(phrDocIndex);

		// write action to phr_actions table
		phrActionDAO.save(action);
	}

	public void sendAddMedication(EctProviderData.Provider prov, String demographicNo, String demographicPhrId, RxPrescriptionData.Prescription drug) throws Exception {
		PHRMedication medication = new PHRMedication(prov, demographicNo, demographicPhrId, drug);
		PHRAction action = medication.getAction(PHRAction.ACTION_ADD, PHRAction.STATUS_SEND_PENDING);
		action.setOscarId(drug.getDrugId() + "");
		// write action to phr_actions table
		phrActionDAO.save(action);
	}

	public void sendUpdateMedication(EctProviderData.Provider prov, String demographicNo, String demographicPhrId, RxPrescriptionData.Prescription drug, String phrDrugIndex) throws Exception {
		PHRMedication medication = new PHRMedication(prov, demographicNo, demographicPhrId, drug);
		PHRAction action = medication.getAction(PHRAction.ACTION_UPDATE, PHRAction.STATUS_SEND_PENDING);
		// set which phrIndex to update
		action.setPhrIndex(phrDrugIndex);
		action.setOscarId(drug.getDrugId() + "");
		// write action to phr_actions table
		phrActionDAO.save(action);
	}

	public void sendAddDocument(PHRDocument document, String oscarId) {
		PHRAction action = document.getAction(PHRAction.ACTION_ADD, PHRAction.STATUS_SEND_PENDING);
		action.setOscarId(oscarId);
		// write action to phr_actions table
		phrActionDAO.save(action);
	}

	public void sendUpdateDocument(PHRDocument document, String phrIndex, String oscarIndex) {
		PHRAction action = document.getAction(PHRAction.ACTION_UPDATE, PHRAction.STATUS_SEND_PENDING);
		// set which phrIndex to update
		action.setPhrIndex(phrIndex);
		action.setOscarId(oscarIndex);
		// write action to phr_actions table
		phrActionDAO.save(action);
	}

	public void sendAddMessage(String subject, String priorThreadMessage, String messageBody, ProviderData sender, String recipientOscarId, int recipientType, String recipientPhrId) throws Exception {
		sendAddMessage(subject, priorThreadMessage, messageBody, sender, recipientOscarId, recipientType, recipientPhrId, new ArrayList());
	}

	public void sendAddMessage(String subject, String priorThreadMessage, String messageBody, ProviderData sender, String recipientOscarId, int recipientType, String recipientPhrId, List<String> attachmentActionIds) throws Exception {
		PHRMessage message = new PHRMessage(subject, priorThreadMessage, messageBody, sender, recipientOscarId, recipientType, recipientPhrId, attachmentActionIds);
		PHRAction action = message.getAction(PHRAction.ACTION_ADD, PHRAction.STATUS_SEND_PENDING);
		phrActionDAO.save(action);
	}

	public void sendUpdateMessage(PHRMessage msg) throws Exception {
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
			logger.debug("userInOscar: " + !dd.getDemographicNoByPIN(docHeader.getAuthor().getIndivoId()).equals(""));

			if (!sendingDocument) {
				boolean userInOscar = !dd.getDemographicNoByPIN(docHeader.getAuthor().getIndivoId()).equals("");
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
				if (sendingDocument && dd.getDemographicNoByPIN(mess.getReceiverPhr()).equals("")) continue;
				mess.checkImportStatus();
				PHRDocument phrdoc = new PHRDocument();
				BeanUtils.copyProperties(phrdoc, mess);
				phrDocumentDAO.save(phrdoc);
			} else {
				logger.debug("need to check for updates on this message");
			}
		}
	}

	public void sendQueuedDocuments(PHRAuthentication auth, String providerNo) throws Exception {
		// package sharing
		IndivoAPService apService = new IndivoAPService(this);
		// apService.packageAllAccessPolicies(auth);

		List<PHRAction> actions = phrActionDAO.getQueuedActions(providerNo);

		TalkClient client = getTalkClient();
		logger.debug("Processing " + actions.size() + " actions ");
		for (PHRAction action : actions) {
			boolean updated = false;
			// handle messages differently
			logger.debug("ACTION classification " + action.getPhrClassification() + " action type " + action.getActionType());
			logger.debug("BB" + PHRConstants.DOCTYPE_MESSAGE());
			logger.debug("AA " + PHRAction.ACTION_ADD);
			try {
				if (action.getPhrClassification().equalsIgnoreCase(PHRConstants.DOCTYPE_MESSAGE()) && action.getActionType() == PHRAction.ACTION_ADD) {
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
					}
					// update action if updated phr indexes in the attached document reference
					if (msg.getReferencedIndivoDocuments().size() > 0) {
						action.setIndivoDocument(PHRMessage.getPhrMessageDocument(auth.getUserId(), auth.getNamePHRFormat(), msg));
						phrActionDAO.update(action);
					}
					client.sendMessage(auth.getToken(), msg);
					logger.debug("message is going to " + msg.getRecipient());
					// client.sendMessage(auth.getToken(),msg.getRecipient(),msg.getPriorThreadMessageId(),msg.getSubject(),msg.getMessageContent().getAny().getTextContent() );
					updated = true;
				} else if (action.getActionType() == PHRAction.ACTION_ADD) {
					// if adding
					IndivoDocumentType doc = action.getIndivoDocument();
					if (action.getPhrClassification().equals(PHRConstants.DOCTYPE_BINARYDATA())) doc = PHRBinaryData.mountDocument(action.getOscarId(), doc);
					if (action.getPhrClassification().equals(PHRConstants.DOCTYPE_ANNOTATION())) {
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
					}
					AddDocumentResultType result = client.addDocument(auth.getToken(), action.getReceiverPhr(), doc);
					String resultIndex = result.getDocumentIndex();
					action.setPhrIndex(result.getDocumentIndex());
					// updates indexes to handle the case where two operations on this file are queued
					phrActionDAO.updatePhrIndexes(action.getPhrClassification(), action.getOscarId(), action.getSenderOscar(), resultIndex);
					actions = PHRAction.updateIndexes(action.getPhrClassification(), action.getOscarId(), resultIndex, actions);
					updated = true;
					// if updating
				} else if (action.getPhrClassification().equalsIgnoreCase(PHRConstants.DOCTYPE_MESSAGE()) && action.getActionType() == PHRAction.ACTION_UPDATE) {
					logger.debug("HERE MESSAGE UPDATE");
					org.indivo.xml.JAXBUtils jaxbUtils = new org.indivo.xml.JAXBUtils();

					IndivoDocumentType document = action.getIndivoDocument();

					JAXBContext messageContext = JAXBContext.newInstance("org.indivo.xml.phr.message");
					MessageType msg = (MessageType) org.indivo.xml.phr.DocumentUtils.getDocumentAnyObject(document, messageContext.createUnmarshaller());
					// ??? Should i use reflection to abstract the call to ObjectFactory??? how will in know what method to call? the one that will take MessageType as a param???
					logger.debug("IS READ " + msg.isRead());
					Element element = jaxbUtils.marshalToElement(new org.indivo.xml.phr.message.ObjectFactory().createMessage(msg), JAXBContext.newInstance("org.indivo.xml.phr.message"));

					DocumentVersionGenerator dvg = new DocumentVersionGenerator();
					DocumentVersionType newVersion = dvg.generateDefaultDocumentVersion(auth.getUserId(), auth.getName(), auth.getRole(), element);
					logger.debug("BEFORE UPDATE DOCUMENT calling with token " + auth.getToken() + " id " + auth.getUserId() + " idx " + action.getPhrIndex() + " v " + newVersion);
					UpdateDocumentResultType updateDocRes = client.updateDocument(auth.getToken(), auth.getUserId(), action.getPhrIndex(), newVersion);
					if (updateDocRes == null) {
						logger.debug("UPDATE DOC IS NULL");
					} else {
						logger.debug("UPDATE DOC IS NOT NULL" + updateDocRes.toString());
					}
					logger.debug("AFTER UPDATE DOCUMENT");
					updated = true;

				} else if (action.getActionType() == PHRAction.ACTION_UPDATE) {
					if (action.getPhrIndex() == null && !action.getPhrClassification().equals(PHRConstants.DOCTYPE_ACCESSPOLICIES())) throw new Exception("Error: PHR index not set");

					if (action.getPhrClassification().equals(PHRConstants.DOCTYPE_ACCESSPOLICIES())) action = apService.packageAccessPolicy(auth, action);
					IndivoDocumentType doc = action.getIndivoDocument();
					if (action.getPhrClassification().equals(PHRConstants.DOCTYPE_BINARYDATA())) doc = PHRBinaryData.mountDocument(action.getOscarId(), doc);
					Element documentElement = DocumentUtils.getDocumentAnyElement(doc);
					// Retrieve current file record from indivo
					logger.debug("phr index " + action.getPhrIndex());
					// ReadDocumentResultType readResult = client.readDocument(auth.getToken(), action.getSenderPhr(), action.getPhrIndex());
					// IndivoDocumentType phrDoc = readResult.getIndivoDocument();

					IndivoDocumentType phrDoc = null;
					if (action.getPhrClassification().equals(PHRConstants.DOCTYPE_MESSAGE())) {
						PHRDocument phrd = phrDocumentDAO.getDocumentByIndex(action.getPhrIndex());
						JAXBContext docContext = JAXBContext.newInstance(IndivoDocumentType.class.getPackage().getName());
						Unmarshaller unmarshaller = docContext.createUnmarshaller();

						JAXBElement jaxment = (JAXBElement) unmarshaller.unmarshal(new StringReader(phrd.getDocContent()));
						phrDoc = (IndivoDocumentType) jaxment.getValue();
					} else {
						ReadDocumentResultType readResult = client.readDocument(auth.getToken(), action.getReceiverPhr(), action.getPhrIndex());
						phrDoc = readResult.getIndivoDocument();
					}

					DocumentVersionType version = phrDoc.getDocumentVersion().get(phrDoc.getDocumentVersion().size() - 1);

					// send new version
					VersionBodyType body = version.getVersionBody();
					body.setAny(documentElement);
					version.setVersionBody(body);
					if (action.getPhrClassification().equals(PHRConstants.DOCTYPE_MESSAGE())) {
						client.updateDocument(auth.getToken(), auth.getUserId(), action.getPhrIndex(), version);
					} else {
						client.updateDocument(auth.getToken(), action.getReceiverPhr(), action.getPhrIndex(), version);
					}
					updated = true;
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
		DemographicData dd = new DemographicData();
		DemographicData.Demographic demo = dd.getDemographic(demographic);
		String recipientId = demo.getIndivoId();
		try {
			TalkClient client = getTalkClient();
			SendMessageResultType sendMessageResultType = client.sendMessage(auth.getToken(), recipientId, priorThreadMessage, subject, messageText);
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
		}
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
		return phrDocumentDAO.countUnreadDocuments(PHRConstants.DOCTYPE_MESSAGE(), providerNo);
	}

	public boolean hasUnreadMessages(String providerNo) {
		if (countUnreadMessages(providerNo) > 0) return true;
		return false;
	}

	private TalkClient getTalkClient() throws IndivoException {
		Map m = new HashMap();
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

	public void sendUserRegistration(Hashtable phrRegistrationForm, String whoIsAdding) throws Exception {
		String iUsername = (String) phrRegistrationForm.get("username");
		String iPassword = (String) phrRegistrationForm.get("password");
		String iRole = (String) phrRegistrationForm.get("role");
		String iFirstName = (String) phrRegistrationForm.get("firstName");
		String iLastName = (String) phrRegistrationForm.get("lastName");
		String iAddress = (String) phrRegistrationForm.get("address");
		String iCity = (String) phrRegistrationForm.get("city");
		String iProvince = (String) phrRegistrationForm.get("province");
		String iPostal = (String) phrRegistrationForm.get("postal");
		String iPhone = (String) phrRegistrationForm.get("phone");
		String iPhone2 = (String) phrRegistrationForm.get("phone2");
		String iEmail = (String) phrRegistrationForm.get("email");
		String iDob = (String) phrRegistrationForm.get("dob");
		String iRegisteringProviderNo = (String) phrRegistrationForm.get("registeringProviderNo");

		String[] iGrantProviders = (String[]) phrRegistrationForm.get("list:grantProviders");

		if (iGrantProviders == null) iGrantProviders = new String[0];

		ContactInformationType indiContact = IndivoUtil.generateContactInformationType(iFirstName, iLastName, iAddress, iCity, iProvince, iPostal, iPhone, iPhone2, iEmail);
		DemographicsType indiDemographics = IndivoUtil.generateDemographicType(iDob);

		// Login to Indivo as Admin
		Hashtable adminLoginInfo = this.getAdminLogin();
		PHRAuthentication adminAuth = null;
		String adminAuthName = adminLoginInfo.get("firstName") + " " + adminLoginInfo.get("lastName");

		// adminAuth = new TestAuthentication("ticket", "admin@indivohealth.org", "Admin A", "administrator", "000000");
		try {
			adminAuth = this.authenticateIndivoId((String) adminLoginInfo.get("username"), (String) adminLoginInfo.get("password"));
		} catch (Exception e) {
			logger.error("Could not authenticate as admin");
			throw e;
		}
		try {
			// Create a record:
			String[] iRoles = new String[1];
			iRoles[0] = iRole; // at the moment this feature doesn't appear to be implemented, more investigation needed
			logger.debug("Creating user: \n" + adminAuth.getUserId() + ", \n" + adminLoginInfo.get("firstName") + ", \n" + adminLoginInfo.get("lastName") + ", \n" + adminAuth.getRole() + ", \n" + iUsername + ", \n" + iPassword + ", \n" + iRole + ", \n" + iRoles.toString() + ");");
			IndivoRecordType newRecord = RecordGenerator.generateDefaultRecord(adminAuth.getUserId(), adminAuthName, adminAuth.getRole(), iUsername, iPassword, iRole, iRoles, null, null);
			// ----Prepare Contact and Demographic docs (cast to IndivoDocumentType)

			// Contact Document
			DocumentGenerator docGenerator = new DocumentGenerator();
			JAXBContext contactInfoContext = JAXBContext.newInstance(ContactInformationType.class.getPackage().getName());
			Element indiContactEle = JAXBUtils.marshalToElement((JAXBElement) new ContactInformation(indiContact), contactInfoContext);
			IndivoDocumentType indiContactDocument = docGenerator.generateDefaultDocument(adminAuth.getUserId(), adminAuthName, adminAuth.getRole(), DocumentClassificationUrns.CONTACT, ContentTypeQNames.CONTACT, indiContactEle);

			// Demographic Document
			JAXBContext demographicsInfoContext = JAXBContext.newInstance(DemographicsType.class.getPackage().getName());
			Element indiDemographicsEle = JAXBUtils.marshalToElement((JAXBElement) new Demographics(indiDemographics), demographicsInfoContext);
			IndivoDocumentType indiDemographicsDocument = docGenerator.generateDefaultDocument(adminAuth.getUserId(), adminAuthName, adminAuth.getRole(), DocumentClassificationUrns.DEMOGRAPHICS, ContentTypeQNames.DEMOGRAPHICS, indiDemographicsEle);

			newRecord.getIndivoDocument().add(indiContactDocument);
			newRecord.getIndivoDocument().add(indiDemographicsDocument);

			logger.debug("DONE......------------" + iGrantProviders.length);

			TalkClient talkClient = getTalkClient();
			talkClient.createRecord(adminAuth.getToken(), iUsername, newRecord);

			PHRAuthentication authNewUser = this.authenticateIndivoId(iUsername, iPassword);

			IndivoAPService apUtil = new IndivoAPService(this);

			// do mutual sharing with indicated providers
			for (int i = 0; i < iGrantProviders.length; i++) {
				// get provider IndivoId
				ProviderData providerData = new ProviderData();
				providerData.setProviderNo(iGrantProviders[i]);
				String permissionRecipientProviderId = providerData.getMyOscarId();

				apUtil.sendAddAccessPolicy(authNewUser, permissionRecipientProviderId, IndivoAPService.LEVEL_PROVIDER);
				apUtil.proposeAccessPolicy(iGrantProviders[i], authNewUser.getUserId(), IndivoAPService.LEVEL_PATIENT, iRegisteringProviderNo);
			}

		} catch (JAXBException jaxbe) {
			logger.error("Failed to marshall account details");
			throw jaxbe;
		} catch (Exception e) {
			logger.error("Indivo failed to create the new record");
			throw e;
		}
	}

	// private void getDocument(

	public void setPhrDocumentDAO(PHRDocumentDAO phrDocumentDAO) {
		this.phrDocumentDAO = phrDocumentDAO;

	}

	public void setPhrActionDAO(PHRActionDAO phrActionDAO) {
		this.phrActionDAO = phrActionDAO;
	}

	private Hashtable getAdminLogin() {
		Hashtable<String, String> loginInfo = new Hashtable();
		OscarProperties props = OscarProperties.getInstance();
		loginInfo.put("username", props.getProperty("myOSCAR.admin.username"));
		loginInfo.put("password", props.getProperty("myOSCAR.admin.password"));
		loginInfo.put("firstName", props.getProperty("myOSCAR.admin.firstName"));
		loginInfo.put("lastName", props.getProperty("myOSCAR.admin.lastName"));
		loginInfo.put("role", props.getProperty("myOSCAR.admin.role"));

		return loginInfo;
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

}
