/*
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 * <OSCAR TEAM>
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */

package oscar.dms.actions;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.oscarehr.common.model.Provider;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.oscarehr.casemgmt.dao.CaseManagementNoteDAO;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.CaseManagementNoteLink;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.common.dao.ProviderInboxRoutingDao;
import org.oscarehr.util.SessionConstants;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import oscar.dms.EDoc;
import oscar.dms.EDocUtil;
import oscar.dms.data.AddEditDocumentForm;
import oscar.log.LogAction;
import oscar.log.LogConst;
import oscar.oscarEncounter.data.EctProgram;
import oscar.util.UtilDateUtilities;

public class AddEditDocumentAction extends DispatchAction {

    public ActionForward multifast(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception{
        System.out.println("IN MULTIFAST content len"+request.getContentLength()+" ---- "+request.getParameter("provider"));
        Hashtable errors = new Hashtable();
        AddEditDocumentForm fm = (AddEditDocumentForm) form;

        FormFile docFile = fm.getFiledata();

         String fileName = docFile.getFileName();
	 String user = (String)request.getSession().getAttribute("user");
            EDoc newDoc = new EDoc("", "", fileName, "", user, user, fm.getSource(), 'A', oscar.util.UtilDateUtilities.getToday("yyyy-MM-dd"), "", "", "demographic", "-1");
            newDoc.setDocPublic("0");
            fileName = newDoc.getFileName();
            //save local file;
            if (docFile.getFileSize() == 0) {
                errors.put("uploaderror", "dms.error.uploadError");
                throw new FileNotFoundException();
            }
            writeLocalFile(docFile, fileName);
            newDoc.setContentType(docFile.getContentType());
            if (fileName.endsWith(".PDF") || fileName.endsWith(".pdf")){
                newDoc.setContentType("application/pdf");
            }

            String doc_no = EDocUtil.addDocumentSQL(newDoc);
            LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.ADD, LogConst.CON_DOCUMENT, doc_no, request.getRemoteAddr());

        if (request.getParameter("provider") !=null){ //TODO: THIS NEEDS TO RUN THRU THE  lab forwarding rules!
            WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
            ProviderInboxRoutingDao providerInboxRoutingDao = (ProviderInboxRoutingDao) ctx.getBean("providerInboxRoutingDAO");
            String proNo = request.getParameter("provider");
            providerInboxRoutingDao.addToProviderInbox(proNo,doc_no,"DOC");
        }

        if (docFile != null){
            System.out.println("Content type "+docFile.getContentType()+" filename "+docFile.getFileName()+"  size: "+docFile.getFileSize());
        }

        return mapping.findForward("fastUploadSuccess");

    }

    public ActionForward fastUpload(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception{
        AddEditDocumentForm fm = (AddEditDocumentForm) form;
        Hashtable errors = new Hashtable();
        FormFile docFile = fm.getDocFile();
            String fileName = docFile.getFileName();
	    String user = (String)request.getSession().getAttribute("user");
            EDoc newDoc = new EDoc("", "", fileName, "", user, user, fm.getSource(), 'A', oscar.util.UtilDateUtilities.getToday("yyyy-MM-dd"), "", "", "demographic", "-1");
            newDoc.setDocPublic("0");
            fileName = newDoc.getFileName();
            //save local file;
            if (docFile.getFileSize() == 0) {
                errors.put("uploaderror", "dms.error.uploadError");
                throw new FileNotFoundException();
            }
            writeLocalFile(docFile, fileName);
            newDoc.setContentType(docFile.getContentType());

            EDocUtil.addDocumentSQL(newDoc);


          return mapping.findForward("fastUploadSuccess");
    }

    public ActionForward unspecified(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response){
        return execute2(mapping, form,request, response);
    }

    public ActionForward execute2(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        AddEditDocumentForm fm = (AddEditDocumentForm) form;
        if (fm.getMode().equals("") && fm.getFunction().equals("") && fm.getFunctionId().equals("")) {
        	//file size exceeds the upload limit
            Hashtable errors = new Hashtable();
            errors.put("uploaderror", "dms.error.uploadError");
            request.setAttribute("docerrors", errors);
            request.setAttribute("completedForm", fm);
            request.setAttribute("editDocumentNo", "");
            return mapping.findForward("failEdit");
        } else if (fm.getMode().equals("add")) {
            //if add/edit success then send redirect, if failed send a forward (need the formdata and errors hashtables while trying to avoid POSTDATA messages)
            if (addDocument(fm, mapping, request) == true) { //if success
                ActionRedirect redirect = new ActionRedirect(mapping.findForward("successAdd"));
                redirect.addParameter("docerrors", "docerrors");  //Allows the JSP to check if the document was just submitted
                redirect.addParameter("function", request.getParameter("function"));
                redirect.addParameter("functionid", request.getParameter("functionid"));
                redirect.addParameter("curUser", request.getParameter("curUser"));
                String parentAjaxId = request.getParameter("parentAjaxId");
                //if we're called with parent ajax id inform jsp that parent needs to be updated
                if( !parentAjaxId.equals("") ) {
                    redirect.addParameter("parentAjaxId", parentAjaxId);
                    redirect.addParameter("updateParent", "true");
                }
                return redirect;
            } else {
                request.setAttribute("function", request.getParameter("function"));
                request.setAttribute("functionid", request.getParameter("functionid"));
                request.setAttribute("parentAjaxId", request.getParameter("parentAjaxId"));
                request.setAttribute("curUser", request.getParameter("curUser"));
                return mapping.findForward("failAdd");
            }
        } else {
            ActionForward forward = editDocument(fm, mapping, request);
            return forward;
        }
    }

    //returns true if successful
    private boolean addDocument(AddEditDocumentForm fm, ActionMapping mapping, HttpServletRequest request) {
      //  System.out.println("=============in addDocument============");
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
            //original file name
            String fileName1 = docFile.getFileName();
        //    System.out.println("fileName1: "+fileName1);
            EDoc newDoc = new EDoc(fm.getDocDesc(), fm.getDocType(), fileName1, "", fm.getDocCreator(), fm.getResponsibleId(), fm.getSource(), 'A', fm.getObservationDate(), "", "", fm.getFunction(), fm.getFunctionId());
            newDoc.setDocPublic(fm.getDocPublic());
            //new file name with date attached
            String fileName2 = newDoc.getFileName();
            //save local file
            writeLocalFile(docFile, fileName2);
            newDoc.setContentType(docFile.getContentType());

            // if the document was added in the context of a program
            String programIdStr = (String) request.getSession().getAttribute(SessionConstants.CURRENT_PROGRAM_ID);
            if (programIdStr!=null) newDoc.setProgramId(Integer.valueOf(programIdStr));

            //---
            String doc_no = EDocUtil.addDocumentSQL(newDoc);
            LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.ADD, LogConst.CON_DOCUMENT, doc_no, request.getRemoteAddr());
            System.out.println("DEMOGRAPHIC NO: "+request.getSession().getAttribute("casemgmt_DemoNo").toString());
            System.out.println("USER: "+request.getSession().getAttribute("user").toString());

            Locale locale = getLocale(request);
            
            ResourceBundle props=ResourceBundle.getBundle("oscarResources", locale);
            
            //add a note to the newly added document.
            Date now=EDocUtil.getDmsDateTimeAsDate();
            //System.out.println("here11");
            String docDesc=EDocUtil.getLastDocumentDesc();
                       
            CaseManagementNote cmn=new CaseManagementNote();
            cmn.setUpdate_date(now);
            //java.sql.Date od1 = MyDateFormat.getSysDate(newDoc.getObservationDate());
            cmn.setObservation_date(now);
            String demoNo=request.getSession().getAttribute("casemgmt_DemoNo").toString();
            cmn.setDemographic_no(demoNo);
            HttpSession se = request.getSession();
            String user_no = (String) se.getAttribute("user");
            String prog_no = new EctProgram(se).getProgram(user_no);
            WebApplicationContext  ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(se.getServletContext());
            CaseManagementManager cmm = (CaseManagementManager) ctx.getBean("caseManagementManager");
            cmn.setProviderNo("-1");// set the provider no to be -1 so the editor appear as 'System'.
         //   System.out.println("here");

       //     System.out.println("here33 "+fm.getDocCreator());

            String provFirstName=EDocUtil.getProviderInfo("first_name", fm.getDocCreator());
            String provLastName=EDocUtil.getProviderInfo("last_name", fm.getDocCreator());
         //   System.out.println("here22");
            String strNote="Document"+" "+docDesc+" "+  "created at "+now+" by "+provFirstName+" "+provLastName+".";

        //    System.out.println("here0 "+strNote);
            cmn.setNote(strNote);
            cmn.setSigned(true);
            cmn.setSigning_provider_no("-1");
            cmn.setProgram_no(prog_no);
            cmn.setReporter_caisi_role("1");
            cmn.setReporter_program_team("0");
            cmn.setPassword("NULL");
            cmn.setLocked(false);
            cmn.setHistory(strNote);
            cmn.setPosition(0);

           
           cmm.saveNoteSimple(cmn);

            //Add a noteLink to casemgmt_note_link
            CaseManagementNoteLink cmnl=new CaseManagementNoteLink();
            cmnl.setTableName(cmnl.DOCUMENT);
            cmnl.setTableId(Long.parseLong(EDocUtil.getLastDocumentNo()));
            cmnl.setNoteId(Long.parseLong(EDocUtil.getLastNoteId()));
         //   System.out.println("ValuesSavedInCaseManagementNoteLink: ");
         //   System.out.println("5= "+cmnl.DOCUMENT+" last doc no="+ EDocUtil.getLastDocumentNo()+" last note id="+EDocUtil.getLastNoteId());
            EDocUtil.addCaseMgmtNoteLink(cmnl);

        } catch (Exception e) {
            //ActionRedirect redirect = new ActionRedirect(mapping.findForward("failAdd"));
            request.setAttribute("docerrors", errors);
            request.setAttribute("completedForm", fm);
            return false;
        }
    //    System.out.println("=============End in addDocument============");
        return true;
    }

    private ActionForward editDocument(AddEditDocumentForm fm, ActionMapping mapping, HttpServletRequest request) {
        Hashtable errors = new Hashtable();
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
            String fileName = docFile.getFileName();
	    String reviewerId = filled(fm.getReviewerId()) ? fm.getReviewerId() : "";
	    String reviewDateTime = filled(fm.getReviewDateTime()) ? fm.getReviewDateTime() : "";


	    if (!filled(reviewerId) && fm.getReviewDoc()) {
		reviewerId = (String)request.getSession().getAttribute("user");
		reviewDateTime = UtilDateUtilities.DateToString(UtilDateUtilities.now(), EDocUtil.REVIEW_DATETIME_FORMAT);
                if (fm.getFunction() != null && fm.getFunction().equals("demographic")){
                    LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.REVIEWED , LogConst.CON_DOCUMENT, fm.getMode(), request.getRemoteAddr(),fm.getFunctionId());
                }else{
                    LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.REVIEWED , LogConst.CON_DOCUMENT, fm.getMode(), request.getRemoteAddr());

                }
	    }
            EDoc newDoc = new EDoc(fm.getDocDesc(), fm.getDocType(), fileName, "", fm.getDocCreator(), fm.getResponsibleId(), fm.getSource(), 'A', fm.getObservationDate(), reviewerId, reviewDateTime, fm.getFunction(), fm.getFunctionId());
            newDoc.setDocId(fm.getMode());
            newDoc.setDocPublic(fm.getDocPublic());
            fileName = newDoc.getFileName();
            if (docFile.getFileSize() != 0) {
                //save local file
                writeLocalFile(docFile, fileName);
                newDoc.setContentType(docFile.getContentType());
                //---
            } else if (docFile.getFileName().length() != 0) {
                errors.put("uploaderror", "dms.error.uploadError");
                throw new FileNotFoundException();
            }
            EDocUtil.editDocumentSQL(newDoc, fm.getReviewDoc());

            if (fm.getFunction() != null && fm.getFunction().equals("demographic")){
                LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.UPDATE , LogConst.CON_DOCUMENT, fm.getMode(), request.getRemoteAddr(),fm.getFunctionId());
            }else{
                LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.UPDATE , LogConst.CON_DOCUMENT, fm.getMode(), request.getRemoteAddr());

            }

        } catch (Exception e) {
            request.setAttribute("docerrors", errors);
            request.setAttribute("completedForm", fm);
            request.setAttribute("editDocumentNo", fm.getMode());
            return mapping.findForward("failEdit");
        }
        return mapping.findForward("successEdit");
    }

    private void writeLocalFile(FormFile docFile, String fileName) throws Exception {
        InputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = docFile.getInputStream();
            String savePath = oscar.OscarProperties.getInstance().getProperty("DOCUMENT_DIR") + "/" + fileName;
            fos = new FileOutputStream(savePath);
            byte[] buf = new byte[128*1024];
            int i = 0;
            while ((i = fis.read(buf)) != -1) {
                fos.write(buf, 0, i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) fis.close();
            if (fos != null) fos.close();
        }
    }

    private boolean filled(String s) {
        return (s!=null && s.trim().length()>0);
    }
}