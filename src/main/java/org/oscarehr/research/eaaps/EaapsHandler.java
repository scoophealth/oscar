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
package org.oscarehr.research.eaaps;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.CaseManagementNoteLink;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.ProviderInboxRoutingDao;
import org.oscarehr.common.dao.QueueDocumentLinkDao;
import org.oscarehr.common.dao.SecRoleDao;
import org.oscarehr.common.dao.StudyDataDao;
import org.oscarehr.common.dao.UserDSMessagePrefsDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.SecRole;
import org.oscarehr.common.model.StudyData;
import org.oscarehr.common.model.UserDSMessagePrefs;
import org.oscarehr.util.SpringUtils;

import oscar.dms.EDoc;
import oscar.dms.EDocUtil;
import oscar.log.LogAction;
import oscar.log.LogConst;
import oscar.oscarLab.ca.all.parsers.DefaultGenericHandler;
import oscar.oscarMessenger.data.MsgMessageData;
import oscar.oscarMessenger.data.MsgProviderData;
import oscar.oscarMessenger.util.MsgDemoMap;
import oscar.util.ConversionUtils;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v22.group.ORU_R01_PATIENT;
import ca.uhn.hl7v2.model.v22.message.ORU_R01;
import ca.uhn.hl7v2.model.v22.segment.MSH;
import ca.uhn.hl7v2.model.v22.segment.NTE;
import ca.uhn.hl7v2.parser.EncodingNotSupportedException;
import ca.uhn.hl7v2.parser.GenericParser;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.validation.impl.NoValidation;

import com.lowagie.text.pdf.PdfReader;

/**
 * Handler class for uploading eAAPs PDF documents.
 */
public class EaapsHandler extends DefaultGenericHandler implements oscar.oscarLab.ca.all.upload.handlers.MessageHandler {

	private static Logger logger = Logger.getLogger(EaapsHandler.class);

	private StudyDataDao studyDataDao = SpringUtils.getBean(StudyDataDao.class);

	private DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);

	private QueueDocumentLinkDao queueDocumentLinkDao = SpringUtils.getBean(QueueDocumentLinkDao.class);

	private ProviderInboxRoutingDao providerInboxRoutingDao = SpringUtils.getBean(ProviderInboxRoutingDao.class);

	private CaseManagementManager caseManagementManager = SpringUtils.getBean(CaseManagementManager.class);

	private UserDSMessagePrefsDao userDsMessagePrefsDao = SpringUtils.getBean(UserDSMessagePrefsDao.class);

	private SecRoleDao secRoleDao = SpringUtils.getBean(SecRoleDao.class);

	@Override
	public void init(String hl7Body) throws HL7Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("Starting processing of: " + hl7Body);
		}

		// parse the message
		ORU_R01 message = toMessage(hl7Body);

		// save PDF content out of this message
		String fileName = savePdfContent(message);
		
		// pull demographic information from the message
		String hash = getDemographicHash(message);
		StudyData studyData = studyDataDao.findSingleByContent(hash);
		if (studyData == null) {
			throw new IllegalStateException("Unable to determine demographic info for " + hash);
		}
		Demographic demo = demographicDao.getDemographicById(studyData.getDemographicNo());
		if (demo == null) {
			throw new IllegalStateException("Demographic record is not available for " + hash);
		}
		
		// create edoc
		if (fileName != null) {
			String provider = studyData.getProviderNo();
			String description = "eAAPs Action plan for " + demo.getFullName();
			
			EDoc doc = createEDoc(message, fileName, demo, provider, description);
			// save edoc
			int documentId = saveEDoc(provider, doc);
			// route document to the provider
			routeDocument(provider, documentId);
			// and add a case management note so that the AAP can be seen on the eChart
			addCaseManagementNote(demo, description, CaseManagementNoteLink.DOCUMENT, true);
			// make sure that notification will be shown for user
			clearNotifications(hash);
		}

		String recommendations = getRecommendations(message);
		if (recommendations != null && !recommendations.isEmpty()) {
			addCaseManagementNote(demo, recommendations, CaseManagementNoteLink.CASEMGMTNOTE, false);
		}

		// make sure we notify the MRP as well
		notifyMostResponsiblePhysician(message, demo);
	}

	private void notifyMostResponsiblePhysician(ORU_R01 hl7Message, Demographic demo) {
		Provider mrp = demo.getProvider();
		if (mrp == null) {
			if (logger.isDebugEnabled()) {
				logger.info("MRP is not set for " + demo + " - skipping message generation");
			}
			return;
		}
		
		String mrpNote = getMostResponsiblePhysicianNote(hl7Message);
		if (mrpNote == null || mrpNote.isEmpty()) {
			if (logger.isDebugEnabled()) {
				logger.debug("MRP note content is null for " + demo + "- skipping message generation");
			}
			return;
		}
		
		MsgMessageData message = new MsgMessageData();
		
		String[] providerIds = new String[] { mrp.getProviderNo() };
		ArrayList<MsgProviderData> providerListing = message.getProviderStructure(providerIds);
		ArrayList<MsgProviderData> remoteProviders = message.getRemoteProvidersStructure();
		
		String sentToWho;
		if (message.isLocals()) {
			sentToWho = message.createSentToString(providerIds);
		} else {
			sentToWho = "";
		}

		if (message.isRemotes()) {
			sentToWho = sentToWho + " " + message.getRemoteNames(remoteProviders);
		}

		String subject = "eAAPs: Recommendations ready for " + demo.getFullName();
		String userName = "eAAPs";
		String userNo = "N/A";
		String attachment = null;
		String pdfAttachment = null;
		String messageId = message.sendMessage2(mrpNote, subject, userName, sentToWho, userNo, providerListing, attachment, pdfAttachment);

		MsgDemoMap msgDemoMap = new MsgDemoMap();
		msgDemoMap.linkMsg2Demo(messageId, demo.getDemographicNo().toString());
	}

	private String getMostResponsiblePhysicianNote(ORU_R01 message) {
		try {
			NTE nte = message.getPATIENT_RESULT().getORDER_OBSERVATION().getNTE(2);
			return nte.getComment(0).getValue();
		} catch (HL7Exception e) {
			throw new IllegalStateException("Unable to get comment field from the message", e);
		}
	}

	private String getRecommendations(ORU_R01 message) {
		try {
			NTE nte = message.getPATIENT_RESULT().getORDER_OBSERVATION().getNTE(1);
			return nte.getComment(0).getValue();
		} catch (HL7Exception e) {
			throw new IllegalStateException("Unable to get comment field from the message", e);
		}
	}

	private void clearNotifications(String hash) {
		for (UserDSMessagePrefs pref : userDsMessagePrefsDao.findAllByResourceId(hash)) {
			pref.setArchived(true);

			userDsMessagePrefsDao.merge(pref);
		}
	}

	private void routeDocument(String provider, int documentId) {
		if (provider == null || provider.isEmpty()) {
			logger.info("Provider is not set for " + documentId + ". Not routing");
			return;
		}

		providerInboxRoutingDao.addToProviderInbox(provider, documentId, "DOC");
		queueDocumentLinkDao.addToQueueDocumentLink(1, documentId);
	}

	private int saveEDoc(String provider, EDoc doc) {
		int documentId = 0;
		try {
			String doc_no = EDocUtil.addDocumentSQL(doc);
			documentId = Integer.parseInt(doc_no);
			LogAction.addLog(provider, LogConst.ADD, "eaap", doc_no, "", "", "EaapDocUpload");
		} catch (Exception e) {
			logger.error("Unable to persist document", e);
			throw new RuntimeException("Unable to persist document", e);
		}
		return documentId;
	}

	private String getDemographicHash(ORU_R01 message) {
		ORU_R01_PATIENT patientGroup = message.getPATIENT_RESULT().getPATIENT();
		String hash = patientGroup.getPID().getPatientIDExternalID().getCk1_IDNumber().getValue();
		return hash;
	}

	private EDoc createEDoc(ORU_R01 message, String fileName, Demographic demo, String provider, String description) {
		String docType = "consults";
		String docClass = "eaap";
		String docSubClass = "eaap";
		String contentType = "application/pdf";
		String observationDate = ConversionUtils.toDateString(new Date());
		String docCreator = provider;
		String responsible = provider;
		String reviewer = "";
		String reviewDateTime = "";

		MSH mshSegment = message.getMSH();
		String source = "eaaps";
		String sourceFacility = mshSegment.getSendingApplication().getValue();
		char status = 'A';
		String module = "demographic";
		String moduleId = "" + demo.getDemographicNo(); // for some reason this refers to the DEMO ID!!!
		boolean updateFileName = false;
		EDoc doc = new EDoc(description, docType, fileName, "", docCreator, responsible, source, status, observationDate, reviewer, reviewDateTime, module, moduleId, updateFileName);
		doc.setType(docType);
		doc.setDocPublic("0");
		doc.setDocClass(docClass);
		doc.setDocSubClass(docSubClass);
		doc.setContentType(contentType);
		doc.setSourceFacility(sourceFacility);
		doc.setNumberOfPages(countPages(fileName));
		return doc;
	}

	private int countPages(String fileName) {
		PdfReader reader = null;
		try {
			reader = new PdfReader(fileName);
			return reader.getNumberOfPages();
		} catch (IOException e) {
			logger.debug("Unable to count pages in " + fileName + " due to " + e.getMessage());
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		return -1;
	}

	private void addCaseManagementNote(Demographic demo, String description, int noteLink, boolean isSigned) {
		CaseManagementNote cmn = new CaseManagementNote();
		cmn.setObservation_date(new Date());
		cmn.setUpdate_date(new Date());
		cmn.setDemographic_no("" + demo.getDemographicNo());
		cmn.setProviderNo("-1");
		cmn.setNote(description);
		cmn.setSigned(isSigned);
		cmn.setSigning_provider_no("-1");
		cmn.setProgram_no("");

		SecRole doctorRole = secRoleDao.findByName("doctor");
		cmn.setReporter_caisi_role(doctorRole.getId().toString());

		cmn.setReporter_program_team("0");
		cmn.setPassword("NULL");
		cmn.setLocked(false);
		cmn.setHistory(description);
		cmn.setPosition(0);
		caseManagementManager.saveNoteSimple(cmn);

		// Add a noteLink to casemgmt_note_link
		CaseManagementNoteLink cmnl = new CaseManagementNoteLink();
		cmnl.setTableName(noteLink);
		cmnl.setTableId(Long.parseLong(EDocUtil.getLastDocumentNo()));
		cmnl.setNoteId(Long.parseLong(EDocUtil.getLastNoteId()));

		EDocUtil.addCaseMgmtNoteLink(cmnl);
	}

	private String savePdfContent(ORU_R01 message) throws HL7Exception {
		NTE nte = message.getPATIENT_RESULT().getORDER_OBSERVATION().getNTE();
		String base64EncodedPdfContent = nte.getComment(0).getValue();
		// break on empty PDFs
		if (base64EncodedPdfContent == null || base64EncodedPdfContent.isEmpty()) {
			return null;
		}
		
		byte[] pdf = Base64.decodeBase64(base64EncodedPdfContent);

		// save eAAP document
		String fileName = nte.getNte2_SourceOfComment().getValue();
		try {
			EDocUtil.writeDocContent(fileName, pdf);
		} catch (IOException e) {
			throw new RuntimeException("Unable to save file", e);
		}
		return fileName;
	}

	private ORU_R01 toMessage(String hl7Body) throws HL7Exception, EncodingNotSupportedException {
		hl7Body = preProcess(hl7Body);
		Parser parser = new GenericParser();
		parser.setValidationContext(new NoValidation());
		Message m = parser.parse(hl7Body);
		if (!(m instanceof ORU_R01)) {
			throw new HL7Exception("Unsupported message type: " + m.getName() + ". Expected ORU^R01 ver 2.2");
		}
		ORU_R01 message = (ORU_R01) m;
		int commentReps = message.getPATIENT_RESULT().getORDER_OBSERVATION().currentReps("NTE");
		if (commentReps < 2) {
			throw new HL7Exception("Expected at least 2 comments in the NTE field.");
		}
		return message;
	}

	/**
	 * Pre-processes the HL7 message before parsing.
	 * 
	 * @param hl7Body
	 * 		HL7 body to be pre-processed
	 * @return
	 * 		Returns the pre-processed content that's ready for parsing
	 */
	protected String preProcess(String hl7Body) {
		if (hl7Body == null) {
			return null;
		}
		return hl7Body.replaceAll("\n", "\r");
	}

	@Override
	public String parse(String serviceName, String fileName, int fileId, String ipAddr) {
		String hl7content = null;
		InputStream is = null;
		try {
			is = new BufferedInputStream(new FileInputStream(new File(fileName)));
			hl7content = IOUtils.toString(is);
			init(hl7content);
		} catch (Exception e) {
			logger.error("Unable to process " + fileName + " for " + serviceName, e);

			return null;
		} finally {
			IOUtils.closeQuietly(is);
		}

		return "success";
	}

}
