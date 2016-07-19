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


package oscar.dms.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Hashtable;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.oscarehr.PMmodule.caisi_integrator.ConformanceTestHelper;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.CaseManagementNoteLink;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.common.dao.DocumentStorageDao;
import org.oscarehr.common.dao.ProviderInboxRoutingDao;
import org.oscarehr.common.dao.QueueDocumentLinkDao;
import org.oscarehr.common.dao.SecRoleDao;
import org.oscarehr.common.dao.SiteDao;
import org.oscarehr.common.model.DocumentStorage;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.SecRole;
import org.oscarehr.common.model.Site;
import org.oscarehr.managers.ProgramManager2;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SessionConstants;
import org.oscarehr.util.SpringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.lowagie.text.pdf.PdfReader;

import oscar.MyDateFormat;
import oscar.dms.EDoc;
import oscar.dms.EDocUtil;
import oscar.dms.data.AddEditDocumentForm;
import oscar.log.LogAction;
import oscar.log.LogConst;
import oscar.oscarEncounter.data.EctProgram;
import oscar.util.UtilDateUtilities;

public class AddEditDocumentAction extends DispatchAction {
	
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
	public ActionForward html5MultiUpload(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ResourceBundle props = ResourceBundle.getBundle("oscarResources");
		
		AddEditDocumentForm fm = (AddEditDocumentForm) form;

		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_edoc", "w", null)) {
			throw new SecurityException("missing required security object (_edoc)");
		}
		
		FormFile docFile = fm.getFiledata();
		int numberOfPages = 0;
		String fileName = docFile.getFileName();
		String user = (String) request.getSession().getAttribute("user");
		EDoc newDoc = new EDoc("", "", fileName, "", user, user, fm.getSource(), 'A', oscar.util.UtilDateUtilities.getToday("yyyy-MM-dd"), "", "", "demographic", "-1", 0);
		newDoc.setDocPublic("0");
		newDoc.setAppointmentNo(Integer.parseInt(fm.getAppointmentNo()));
		
        // if the document was added in the context of a program
		ProgramManager2 programManager = SpringUtils.getBean(ProgramManager2.class);
		LoggedInInfo loggedInInfo  = LoggedInInfo.getLoggedInInfoFromSession(request);
		ProgramProvider pp = programManager.getCurrentProgramInDomain(loggedInInfo, loggedInInfo.getLoggedInProviderNo());
		if(pp != null && pp.getProgramId() != null) {
			newDoc.setProgramId(pp.getProgramId().intValue());
		}
		
		fileName = newDoc.getFileName();
		// save local file;
		if (docFile.getFileSize() == 0) {
			//errors.put("uploaderror", "dms.error.uploadError");
			response.setHeader("oscar_error",props.getString("dms.addDocument.errorZeroSize") );
			response.sendError(500,props.getString("dms.addDocument.errorZeroSize") );
			return null;
			//throw new FileNotFoundException();
		}
		File file = writeLocalFile(docFile, fileName);// write file to local dir

		if(!file.exists() || file.length() < docFile.getFileSize()) {
			response.setHeader("oscar_error",props.getString("dms.addDocument.errorNoWrite") );
			response.sendError(500,props.getString("dms.addDocument.errorNoWrite") );
			return null;
		}

		newDoc.setContentType(docFile.getContentType());
		if (fileName.endsWith(".PDF") || fileName.endsWith(".pdf")) {
			newDoc.setContentType("application/pdf");
			// get number of pages when document is pdf;
			numberOfPages = countNumOfPages(fileName);
		}
		newDoc.setNumberOfPages(numberOfPages);
		String doc_no = EDocUtil.addDocumentSQL(newDoc);
		LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.ADD, LogConst.CON_DOCUMENT, doc_no, request.getRemoteAddr());
		String providerId = request.getParameter("provider");

		if (providerId != null) { // TODO: THIS NEEDS TO RUN THRU THE lab forwarding rules!
			WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
			ProviderInboxRoutingDao providerInboxRoutingDao = (ProviderInboxRoutingDao) ctx.getBean("providerInboxRoutingDAO");
			providerInboxRoutingDao.addToProviderInbox(providerId, Integer.parseInt(doc_no), "DOC");
		}
		// add to queuelinkdocument
		String queueId = request.getParameter("queue");

		if (queueId != null && !queueId.equals("-1")) {
			WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
			QueueDocumentLinkDao queueDocumentLinkDAO = (QueueDocumentLinkDao) ctx.getBean("queueDocumentLinkDAO");
			Integer qid = Integer.parseInt(queueId.trim());
			Integer did = Integer.parseInt(doc_no.trim());
			queueDocumentLinkDAO.addActiveQueueDocumentLink(qid, did);
			request.getSession().setAttribute("preferredQueue", queueId);
		}
		
		return null;

	}

	public static int countNumOfPages(String fileName) {// count number of pages in a local pdf file

		int numOfPage = 0;
		String docdownload = oscar.OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
                if (!docdownload.endsWith(File.separator))
                {
                    docdownload += File.separator; 
                }
		String filePath = docdownload + fileName;

		try {
			PdfReader reader = new PdfReader(filePath);
			numOfPage = reader.getNumberOfPages();
			reader.close();

		} catch (IOException e) {
			MiscUtils.getLogger().error("Error", e);
		}
		return numOfPage;
	}

	public ActionForward fastUpload(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		AddEditDocumentForm fm = (AddEditDocumentForm) form;
		
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_edoc", "w", null)) {
			throw new SecurityException("missing required security object (_edoc)");
		}
		
		Hashtable errors = new Hashtable();
		FormFile docFile = fm.getDocFile();
		String fileName = docFile.getFileName();
		String user = (String) request.getSession().getAttribute("user");
		EDoc newDoc = new EDoc("", "", fileName, "", user, user, fm.getSource(), 'A', oscar.util.UtilDateUtilities.getToday("yyyy-MM-dd"), "", "", "demographic", "-1");
		newDoc.setDocPublic("0");
		newDoc.setAppointmentNo(Integer.parseInt(fm.getAppointmentNo()));
		
        // if the document was added in the context of a program
		ProgramManager2 programManager = SpringUtils.getBean(ProgramManager2.class);
		LoggedInInfo loggedInInfo  = LoggedInInfo.getLoggedInInfoFromSession(request);
		ProgramProvider pp = programManager.getCurrentProgramInDomain(loggedInInfo, loggedInInfo.getLoggedInProviderNo());
		if(pp != null && pp.getProgramId() != null) {
			newDoc.setProgramId(pp.getProgramId().intValue());
		}
		
		fileName = newDoc.getFileName();
		// save local file;
		if (docFile.getFileSize() == 0) {
			errors.put("uploaderror", "dms.error.uploadError");
			throw new FileNotFoundException();
		}
		writeLocalFile(docFile, fileName);
		newDoc.setContentType(docFile.getContentType());
                if (fileName.toLowerCase().endsWith(".pdf")) {
                    newDoc.setContentType("application/pdf");
                    int numberOfPages = countNumOfPages(fileName);
                    newDoc.setNumberOfPages(numberOfPages);                        
                }

		EDocUtil.addDocumentSQL(newDoc);

		return mapping.findForward("fastUploadSuccess");
	}

	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return execute2(mapping, form, request, response);
	}

	public ActionForward execute2(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		AddEditDocumentForm fm = (AddEditDocumentForm) form;
		
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_edoc", "w", null)) {
			throw new SecurityException("missing required security object (_edoc)");
		}
		
		if (fm.getMode().equals("") && fm.getFunction().equals("") && fm.getFunctionId().equals("")) {
			// file size exceeds the upload limit
			Hashtable errors = new Hashtable();
			errors.put("uploaderror", "dms.error.uploadError");
			request.setAttribute("docerrors", errors);
			request.setAttribute("completedForm", fm);
			request.setAttribute("editDocumentNo", "");
			return mapping.findForward("failEdit");
		} else if (fm.getMode().equals("add")) {
			// if add/edit success then send redirect, if failed send a forward (need the formdata and errors hashtables while trying to avoid POSTDATA messages)
			Integer documentNo = addDocument(fm, mapping, request);
			String siteId = request.getParameter("siteId");
			if (documentNo!=null && documentNo.intValue()>0) { // if success
				//if it's for site logo, need to update siteLogoId in site table.				
				if(siteId!=null && !"null".equalsIgnoreCase(siteId)) {
					SiteDao siteDao = (SiteDao) SpringUtils.getBean("siteDao");
					Site site = siteDao.getById(Integer.valueOf(siteId));
					site.setSiteLogoId(documentNo);
					siteDao.merge(site);	
					ActionRedirect redirect = new ActionRedirect(mapping.findForward("successAddLogo"));
					return redirect;
				} else {
					ActionRedirect redirect = new ActionRedirect(mapping.findForward("successAdd"));
					redirect.addParameter("docerrors", "docerrors"); // Allows the JSP to check if the document was just submitted
					redirect.addParameter("function", request.getParameter("function"));
					redirect.addParameter("functionid", request.getParameter("functionid"));
					redirect.addParameter("curUser", request.getParameter("curUser"));
					redirect.addParameter("appointmentNo",request.getParameter("appointmentNo"));
					String parentAjaxId = request.getParameter("parentAjaxId");
					// if we're called with parent ajax id inform jsp that parent needs to be updated
					if (!parentAjaxId.equals("")) {
						redirect.addParameter("parentAjaxId", parentAjaxId);
						redirect.addParameter("updateParent", "true");
					}
					return redirect;
				}
			} else {
				if(siteId!=null && !"null".equalsIgnoreCase(siteId)) {
					ActionRedirect redirect = new ActionRedirect(mapping.findForward("failAddLogo"));
					redirect.addParameter("method","update");
					redirect.addParameter("function",request.getParameter("function"));
					redirect.addParameter("functinoId",request.getParameter("functionId"));
					redirect.addParameter("siteId",siteId);		
					if ((fm.getDocDesc().length() == 0) || (fm.getDocDesc().equals("Enter Title"))) {
						redirect.addParameter("logoErrors","Description missing");						
					}
					if (fm.getDocType().length() == 0) {
						redirect.addParameter("logoErrors","Document type missing");								
					}
					FormFile docFile = fm.getDocFile();
					if (docFile.getFileSize() == 0) {
						redirect.addParameter("logoErrors","Document failed to upload");				
					}
					
					return redirect;
				} else {
					request.setAttribute("function", request.getParameter("function"));
					request.setAttribute("functionid", request.getParameter("functionid"));
					request.setAttribute("parentAjaxId", request.getParameter("parentAjaxId"));
					request.setAttribute("curUser", request.getParameter("curUser"));
					request.setAttribute("appointmentNo",request.getParameter("appointmentNo"));
					return mapping.findForward("failAdd");
				}
			}
		} else {
			ActionForward forward = editDocument(fm, mapping, request);
			return forward;
		}
	}

	// returns true if successful
	private Integer addDocument(AddEditDocumentForm fm, ActionMapping mapping, HttpServletRequest request) {
		Integer documentNo = -1;
		Hashtable errors = new Hashtable();
		try {
			if ((fm.getDocDesc().length() == 0) || (fm.getDocDesc().equals("Enter Title"))) {
				errors.put("descmissing", "dms.error.descriptionInvalid");
				throw new Exception();
			}
			if (fm.getDocType().length() == 0) {
				errors.put("typemissing", "dms.error.typeMissing");
				throw new Exception();
			}
			FormFile docFile = fm.getDocFile();
			if (docFile.getFileSize() == 0) {
				errors.put("uploaderror", "dms.error.uploadError");
				throw new FileNotFoundException();
			}
			// original file name
			String fileName1 = docFile.getFileName();

			EDoc newDoc = new EDoc(fm.getDocDesc(), fm.getDocType(), fileName1, "", fm.getDocCreator(), fm.getResponsibleId(), fm.getSource(), 'A', fm.getObservationDate(), "", "", fm.getFunction(), fm.getFunctionId());
			newDoc.setDocPublic(fm.getDocPublic());

			newDoc.setAppointmentNo(Integer.parseInt(fm.getAppointmentNo()));
                        newDoc.setDocClass(fm.getDocClass());
                        newDoc.setDocSubClass(fm.getDocSubClass());
			// new file name with date attached
			String fileName2 = newDoc.getFileName();
			// save local file
			File file = writeLocalFile(docFile, fileName2);
			newDoc.setContentType(docFile.getContentType());
                        if (fileName2.toLowerCase().endsWith(".pdf")) {
                            newDoc.setContentType("application/pdf");
                            int numberOfPages = countNumOfPages(fileName2);
                            newDoc.setNumberOfPages(numberOfPages);                        
                        }
		

			// if the document was added in the context of a program
			ProgramManager2 programManager = SpringUtils.getBean(ProgramManager2.class);
			LoggedInInfo loggedInInfo  = LoggedInInfo.getLoggedInInfoFromSession(request);
			ProgramProvider pp = programManager.getCurrentProgramInDomain(loggedInInfo, loggedInInfo.getLoggedInProviderNo());
			if(pp != null && pp.getProgramId() != null) {
				newDoc.setProgramId(pp.getProgramId().intValue());
			}
			
			String restrictToProgramStr = request.getParameter("restrictToProgram");
			newDoc.setRestrictToProgram("on".equals(restrictToProgramStr));
			
			// if the document was added in the context of an appointment
			if(fm.getAppointmentNo() != null && fm.getAppointmentNo().length()>0) {
				newDoc.setAppointmentNo(Integer.parseInt(fm.getAppointmentNo()));
			}
			
		 	// If a new document type is added, include it in the database to create filters 
		 	if (!EDocUtil.getDoctypes(fm.getFunction()).contains(fm.getDocType())){ 
		 		EDocUtil.addDocTypeSQL(fm.getDocType(),fm.getFunction());
		 	} 
		 	
			// ---
			String doc_no = EDocUtil.addDocumentSQL(newDoc);
			documentNo = Integer.valueOf(doc_no);
			if(ConformanceTestHelper.enableConformanceOnlyTestFeatures){
				storeDocumentInDatabase(file, Integer.parseInt(doc_no));
			}
			LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.ADD, LogConst.CON_DOCUMENT, doc_no, request.getRemoteAddr());
			// add note if document is added under a patient
			String module = fm.getFunction().trim();
			String moduleId = fm.getFunctionId().trim();
			if (module.equals("demographic")) {// doc is uploaded under a patient,moduleId become demo no.

				Date now = EDocUtil.getDmsDateTimeAsDate();

				String docDesc = EDocUtil.getLastDocumentDesc();

				CaseManagementNote cmn = new CaseManagementNote();
				cmn.setUpdate_date(now);
				java.sql.Date od1 = MyDateFormat.getSysDate(newDoc.getObservationDate());
				cmn.setObservation_date(od1);
				cmn.setDemographic_no(moduleId);
				HttpSession se = request.getSession();
				String user_no = (String) se.getAttribute("user");
				String prog_no = new EctProgram(se).getProgram(user_no);
				WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(se.getServletContext());
				CaseManagementManager cmm = (CaseManagementManager) ctx.getBean("caseManagementManager");
				cmn.setProviderNo("-1");// set the provider no to be -1 so the editor appear as 'System'.

				Provider provider = EDocUtil.getProvider(fm.getDocCreator());
				String provFirstName = "";
				String provLastName = "";
				if(provider!=null) {
					provFirstName=provider.getFirstName();
					provLastName=provider.getLastName();
				}

				String strNote = "Document" + " " + docDesc + " " + "created at " + now + " by " + provFirstName + " " + provLastName + ".";

				cmn.setNote(strNote);
				cmn.setSigned(true);
				cmn.setSigning_provider_no("-1");
				cmn.setProgram_no(prog_no);
				
				SecRoleDao secRoleDao = (SecRoleDao) SpringUtils.getBean("secRoleDao");
				SecRole doctorRole = secRoleDao.findByName("doctor");		
				cmn.setReporter_caisi_role(doctorRole.getId().toString());
								
				cmn.setReporter_program_team("0");
				cmn.setPassword("NULL");
				cmn.setLocked(false);
				cmn.setHistory(strNote);
				cmn.setPosition(0);
				
				Long note_id = cmm.saveNoteSimpleReturnID(cmn);
				 
				// Add a noteLink to casemgmt_note_link
				CaseManagementNoteLink cmnl = new CaseManagementNoteLink();
				cmnl.setTableName(CaseManagementNoteLink.DOCUMENT);
				cmnl.setTableId(Long.parseLong(EDocUtil.getLastDocumentNo()));
				cmnl.setNoteId(note_id);

				EDocUtil.addCaseMgmtNoteLink(cmnl);
			}

		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
			// ActionRedirect redirect = new ActionRedirect(mapping.findForward("failAdd"));
			request.setAttribute("docerrors", errors);
			request.setAttribute("completedForm", fm);
			return documentNo;
		}

		return documentNo;
	}

	private ActionForward editDocument(AddEditDocumentForm fm, ActionMapping mapping, HttpServletRequest request) {
		Hashtable errors = new Hashtable();
		
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_edoc", "w", null)) {
			throw new SecurityException("missing required security object (_edoc)");
		}
		
		try {
			if (fm.getDocDesc().length() == 0) {
				errors.put("descmissing", "dms.error.descriptionInvalid");
				throw new Exception();
			}
			if (fm.getDocType().length() == 0) {
				errors.put("typemissing", "dms.error.typeMissing");
				throw new Exception();
			}
			FormFile docFile = fm.getDocFile();
                        String fileName = ""; 
                        
                        if(oscar.OscarProperties.getInstance().getBooleanProperty("ALLOW_UPDATE_DOCUMENT_CONTENT", "true"))
                        {
                            fileName=docFile.getFileName();
                        }
                        
			String reviewerId = filled(fm.getReviewerId()) ? fm.getReviewerId() : "";
			String reviewDateTime = filled(fm.getReviewDateTime()) ? fm.getReviewDateTime() : "";

			if (!filled(reviewerId) && fm.getReviewDoc()) {
				reviewerId = (String) request.getSession().getAttribute("user");
				reviewDateTime = UtilDateUtilities.DateToString(new Date(), EDocUtil.REVIEW_DATETIME_FORMAT);
				if (fm.getFunction() != null && fm.getFunction().equals("demographic")) {
					LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.REVIEWED, LogConst.CON_DOCUMENT, fm.getMode(), request.getRemoteAddr(), fm.getFunctionId());
				} else {
					LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.REVIEWED, LogConst.CON_DOCUMENT, fm.getMode(), request.getRemoteAddr());

				}
			}
			EDoc newDoc = new EDoc(fm.getDocDesc(), fm.getDocType(), fileName, "", fm.getDocCreator(), fm.getResponsibleId(), fm.getSource(), 'A', fm.getObservationDate(), reviewerId, reviewDateTime, fm.getFunction(), fm.getFunctionId());
			newDoc.setSourceFacility(fm.getSourceFacility());
			newDoc.setDocId(fm.getMode());
			newDoc.setDocPublic(fm.getDocPublic());
			newDoc.setAppointmentNo(Integer.parseInt(fm.getAppointmentNo()));
            newDoc.setDocClass(fm.getDocClass());
            newDoc.setDocSubClass(fm.getDocSubClass());
            String programIdStr = (String) request.getSession().getAttribute(SessionConstants.CURRENT_PROGRAM_ID);
            if (programIdStr != null) newDoc.setProgramId(Integer.valueOf(programIdStr));

            			
			fileName = newDoc.getFileName();
			if (docFile.getFileSize() != 0 && fileName.length()!=0) {
				// save local file
				writeLocalFile(docFile, fileName);
				newDoc.setContentType(docFile.getContentType());
                                if (fileName.toLowerCase().endsWith(".pdf")) {
                                    newDoc.setContentType("application/pdf");
                                    int numberOfPages = countNumOfPages(fileName);
                                    newDoc.setNumberOfPages(numberOfPages);                        
                                }
				// ---
			} else if (docFile.getFileName().length() != 0) {
				errors.put("uploaderror", "dms.error.uploadError");
				throw new FileNotFoundException();
			}
			if(fm.getReviewDoc()) {
				newDoc.setReviewDateTime(UtilDateUtilities.DateToString(new Date(), EDocUtil.REVIEW_DATETIME_FORMAT));
			}
			EDocUtil.editDocumentSQL(newDoc, fm.getReviewDoc());

			if (fm.getFunction() != null && fm.getFunction().equals("demographic")) {
				LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.UPDATE, LogConst.CON_DOCUMENT, fm.getMode(), request.getRemoteAddr(), fm.getFunctionId());
			} else {
				LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.UPDATE, LogConst.CON_DOCUMENT, fm.getMode(), request.getRemoteAddr());

			}

		} catch (Exception e) {
			request.setAttribute("docerrors", errors);
			request.setAttribute("completedForm", fm);
			request.setAttribute("editDocumentNo", fm.getMode());
			return mapping.findForward("failEdit");
		}
		return mapping.findForward("successEdit");
	}

	private File writeLocalFile(FormFile docFile, String fileName) throws Exception {
		InputStream fis = null;
		File file= null;
		try {
			fis = docFile.getInputStream();
			file =writeLocalFile(fis, fileName);
		} finally {
			if (fis != null) fis.close();
		}
		return file;
	}

	public static File writeLocalFile(InputStream is, String fileName) throws Exception {
		FileOutputStream fos = null;
		File file = null;
		try {
			String savePath = oscar.OscarProperties.getInstance().getProperty("DOCUMENT_DIR") + "/" + fileName;
			file = new File (savePath);
			fos = new FileOutputStream(savePath);
			byte[] buf = new byte[128 * 1024];
			int i = 0;
			while ((i = is.read(buf)) != -1) {
				fos.write(buf, 0, i);
			}
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
		} finally {
			if (fos != null) fos.close();
		}
		return file;
	}

	public static int storeDocumentInDatabase(File file, Integer documentNo){
		Integer ret = 0;
		FileInputStream fin = null;
		try{
			fin=new FileInputStream(file);
			byte fileContents[] = new byte[(int)file.length()];
			fin.read(fileContents);
			DocumentStorage docStor = new DocumentStorage();
			docStor.setFileContents(fileContents);
			docStor.setDocumentNo(documentNo);
			docStor.setUploadDate(new Date());
			DocumentStorageDao documentStorageDao = (DocumentStorageDao) SpringUtils.getBean("documentStorageDao");
			documentStorageDao.persist(docStor);
			ret = docStor.getId();
		}catch(Exception e){
			MiscUtils.getLogger().error("Error putting file in database",e);
		}
		finally
		{
			IOUtils.closeQuietly(fin);
		}
		return ret;
	}


	private boolean filled(String s) {
		return (s != null && s.trim().length() > 0);
	}
}
