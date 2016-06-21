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
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.CaseManagementNoteLink;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.DxresearchDAO;
import org.oscarehr.common.dao.ProviderInboxRoutingDao;
import org.oscarehr.common.dao.QueueDocumentLinkDao;
import org.oscarehr.common.dao.SecRoleDao;
import org.oscarehr.common.dao.UserDSMessagePrefsDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Dxresearch;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.SecRole;
import org.oscarehr.common.model.UserDSMessagePrefs;
import org.oscarehr.util.LoggedInInfo;
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

import com.lowagie.text.pdf.PdfReader;
import org.oscarehr.common.model.OscarMsgType;

/**
 * Handler class for uploading eAAPs PDF documents.
 */
public class EaapsHandler extends DefaultGenericHandler implements oscar.oscarLab.ca.all.upload.handlers.MessageHandler {

	private static final String SYSTEM_USER_ID = "-1";

	private static final String SYSTEM_PROVIDER = SYSTEM_USER_ID;

	private static Logger logger = Logger.getLogger(EaapsHandler.class);

	private DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);

	private QueueDocumentLinkDao queueDocumentLinkDao = SpringUtils.getBean(QueueDocumentLinkDao.class);

	private ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	
	private ProviderInboxRoutingDao providerInboxRoutingDao = SpringUtils.getBean(ProviderInboxRoutingDao.class);

	private CaseManagementManager caseManagementManager = SpringUtils.getBean(CaseManagementManager.class);

	private UserDSMessagePrefsDao userDsMessagePrefsDao = SpringUtils.getBean(UserDSMessagePrefsDao.class);

	private ProgramDao programDao = SpringUtils.getBean(ProgramDao.class);

	private SecRoleDao secRoleDao = SpringUtils.getBean(SecRoleDao.class);
	
	private DxresearchDAO dxresearchDAO = SpringUtils.getBean(DxresearchDAO.class);

	@Override
	public void init(String hl7Body) throws HL7Exception {
		if (logger.isInfoEnabled()) {
			logger.info("Started processing of HL7 message.");
		}

		EaapsMessageSupport message = new EaapsMessageSupport();
		try {
			// parse the message
			message.init(hl7Body);
		} catch (HL7Exception e) {
			logger.warn("Unable to parse HL7 message", e);
			throw e;
		}

		if (logger.isInfoEnabled()) {
			logger.info("Parsed HL7 message successfully");
		}

		// save PDF content out of this message
		String fileName = savePdfContent(message);

		// pull demographic information from the message
		String hash = message.getDemographicHash();
		if (logger.isInfoEnabled()) {
			logger.info("Processing hash code " + hash);
		}

	    //loop through asthma dx registry / create hash and compare it to hash in report.  If it is get demographic object . if not check the next.
		List<Dxresearch> asthmaRegList =  dxresearchDAO.findActive("icd9", "493");
		int countNumberOfDemographicsCompared = 0;
		Demographic demo = null;
		for(Dxresearch dxresearch: asthmaRegList){
			demo = demographicDao.getDemographicById(dxresearch.getDemographicNo());
			if (demo != null) {
				countNumberOfDemographicsCompared++;
				EaapsHash dxListHash = new EaapsHash(demo);
				if (hash.equals(dxListHash.getHash())){
					logger.debug(demo.getDemographicNo()+" report hash "+hash+" computed hash "+dxListHash.getHash()+" Match! no need to keep looking"); 
					break;
				}else{
					logger.debug(demo.getDemographicNo()+" report hash "+hash+" computed hash "+dxListHash.getHash());
					demo = null;//Set this to null so the last entry in the loop isn't used 
				}
			}
		}
		
		if (demo == null) {
			throw new IllegalStateException("Demographic record is not available for " + hash);
		}
		
		if (logger.isInfoEnabled()) {
			logger.info("Loaded demographic " + demo.getDemographicNo() + " for hash code " + hash+" found in "+countNumberOfDemographicsCompared+" comparisons ");
		}

		// create edoc
		if (fileName != null) {
			String provider = getProvider(message, demo); 
						
			String description = "eAAPS Action plan for " + demo.getFormattedName();

			EDoc doc = createEDoc(message, fileName, demo, description);
			// save edoc
			int documentId = saveEDoc(provider, doc);
			if (logger.isInfoEnabled()) {
				logger.info("Saved edoc " + fileName + " for " + demo.getDemographicNo() 
						+ " with provider " + provider + " successfully. Doc Id = " + documentId);
			}

			// route document to the provider
			routeDocument(provider, documentId);
			if (logger.isInfoEnabled()) {
				logger.info("Routed doc " + documentId + " to " + provider + " successfully.");
			}

			// and add a case management note so that the AAP can be seen on the eChart
			addCaseManagementNote(demo, description, CaseManagementNoteLink.DOCUMENT, true, provider);
			if (logger.isInfoEnabled()) {
				logger.info("Added case management note successfully.");
			}

			// make sure that notification will be shown for user
			clearNotifications(hash);
			if (logger.isInfoEnabled()) {
				logger.info("Cleared notification settings successfully.");
			}
		}

		String recommendations = message.getRecommendations();
		if (recommendations != null && !recommendations.isEmpty()) {
			String provider = getProvider(message, demo);
			addCaseManagementNote(demo, recommendations, CaseManagementNoteLink.CASEMGMTNOTE, false, provider);
			if (logger.isInfoEnabled()) {
				logger.info("Added recommendations successfully.");
			}
		} else {
			if (logger.isInfoEnabled()) {
				logger.info("Recommendations are not provided - skipping.");
			}
		}

		// make sure we notify the MRP as well
		notifyProvider(message, demo);

		if (logger.isInfoEnabled()) {
			logger.info("Completed processing of HL7 message.");
		}
	}

	/**
	 * Gets provider number to be associated with the file upload. In case provider number is 
	 * specified in the message, it's used. Otherwise, most responsible physician is utilized.
	 * In case demographic record doesn't have MRP associated with it, the default provider
	 * for the study is utilized.
	 * 
	 * @param message
	 * 		HL7 message 
	 * @param demo
	 * 		Demographic record to be associated with the document
	 * @return
	 * 		Returns the provider ID
	 */
	private String getProvider(EaapsMessageSupport message, Demographic demo) {
	    if (message.getOrderingProvider() != null) {
	    	return message.getOrderingProvider();
	    }
	    
	    if (demo.getProviderNo() != null) {
	    	 return demo.getProviderNo();
	    }
	    
	   
	    
	    return SYSTEM_PROVIDER;
    }

	private void notifyProvider(EaapsMessageSupport message, Demographic demo) {
		String providerId = demo.getProviderNo();
		if (providerId == null || providerId.isEmpty()) {
			if (logger.isInfoEnabled()) {
				logger.info("MRP ID is not available for " + demo + " - skipping message generation");
			}
			return;
		}
		
		Provider provider = providerDao.getProvider(providerId);
		if (provider == null) {
			if (logger.isInfoEnabled()) {
				logger.info("Can't find MRP record for " + demo + " - skipping message generation");
			}
			return;
		}

		String mrpNote = message.getProviderNote();
		if (mrpNote == null || mrpNote.isEmpty()) {
			if (logger.isInfoEnabled()) {
				logger.info("MRP note content is null for " + demo + "- skipping message generation");
			}
			return;
		}

		MsgMessageData messageData = new MsgMessageData();

		String[] providerIds = new String[] { provider.getProviderNo() };
		ArrayList<MsgProviderData> providerListing = messageData.getProviderStructure(providerIds);
		ArrayList<MsgProviderData> remoteProviders = messageData.getRemoteProvidersStructure();

		String sentToWho;
		if (messageData.isLocals()) {
			sentToWho = messageData.createSentToString(providerIds);
		} else {
			sentToWho = "";
		}

		if (messageData.isRemotes()) {
			sentToWho = sentToWho + " " + messageData.getRemoteNames(remoteProviders);
		}

		String subject = "eAAPS: Recommendations ready for " + demo.getFormattedName();
		String userName = "System";
		String userNo = SYSTEM_USER_ID;
		String attachment = null;
		String pdfAttachment = null;
		String messageId = messageData.sendMessage2(mrpNote, subject, userName, sentToWho, userNo, providerListing, attachment, pdfAttachment, OscarMsgType.GENERAL_TYPE);

		MsgDemoMap msgDemoMap = new MsgDemoMap();
		msgDemoMap.linkMsg2Demo(messageId, demo.getDemographicNo().toString());
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

	private EDoc createEDoc(EaapsMessageSupport message, String fileName, Demographic demo, String description) {
		String docType = "consults";
		String docClass = "eaap";
		String docSubClass = "eaap";
		String contentType = "application/pdf";
		String observationDate = ConversionUtils.toDateString(new Date());
		String providerId = getProvider(message,  demo); 
		String docCreator = providerId;
		String responsible = providerId;
		String reviewer = "";
		String reviewDateTime = "";

		String source = "eAAPS";
		String sourceFacility = message.getSourceFacility();
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
			reader = new PdfReader(EDocUtil.resovePath(fileName));
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

	private void addCaseManagementNote(Demographic demo, String description, int noteLink, boolean isSigned, String providerNumber) {
		CaseManagementNote cmn = new CaseManagementNote();
		cmn.setObservation_date(new Date());
		cmn.setUpdate_date(new Date());
		cmn.setDemographic_no("" + demo.getDemographicNo());
		cmn.setProviderNo(providerNumber);
		cmn.setNote(description);
		cmn.setSigned(isSigned);
		cmn.setSigning_provider_no(providerNumber);
		try {
			String programNumber = getOscarProgramNumber();
			cmn.setProgram_no(programNumber);
		} catch (Exception e) {
			if (logger.isInfoEnabled()) {
				logger.info("Unable to load OSCAR program", e);
			}
			cmn.setProgram_no("");
		}

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
		cmnl.setNoteId(cmn.getId());

		EDocUtil.addCaseMgmtNoteLink(cmnl);
	}

	private String getOscarProgramNumber() {
		Program program = programDao.getProgramByName("OSCAR");
		if (program != null) {
			return "" + program.getId();
		}
	    return "";
    }


	private String savePdfContent(EaapsMessageSupport message) {
		byte[] pdf = message.getPdf();
		String fileName = message.getPdfFileName();
		
		if (pdf == null || pdf.length <= 0 || fileName == null || fileName.isEmpty()) {
			logger.info("PDF content or file name is empty, not saving");
			return null;
		}
		
		try {
			EDocUtil.writeDocContent(fileName, pdf);
		} catch (IOException e) {
			throw new RuntimeException("Unable to save file", e);
		}
		return fileName;
	}

	@Override
	public String parse(LoggedInInfo loggedInInfo, String serviceName, String fileName, int fileId, String ipAddr) {
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
