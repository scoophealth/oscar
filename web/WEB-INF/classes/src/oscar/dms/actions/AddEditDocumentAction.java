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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.oscarehr.util.SessionConstants;

import oscar.dms.EDoc;
import oscar.dms.EDocUtil;
import oscar.dms.data.AddEditDocumentForm;
import oscar.log.LogAction;
import oscar.log.LogConst;
import oscar.util.UtilDateUtilities;

public class AddEditDocumentAction extends DispatchAction {
        
    public ActionForward multifast(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception{
        System.out.println("IN MULTIFAST content len"+request.getContentLength());
        Hashtable errors = new Hashtable();
        AddEditDocumentForm fm = (AddEditDocumentForm) form;
        
        FormFile docFile = fm.getFiledata();
        
         String fileName = docFile.getFileName();
            EDoc newDoc = new EDoc("", "", fileName, "", (String) request.getSession().getAttribute("user") , 'A', oscar.util.UtilDateUtilities.getToday("yyyy-MM-dd"), "", "", "demographic", "-1");
            newDoc.setDocPublic("0");
            fileName = newDoc.getFileName();
            //save local file;
            if (docFile.getFileSize() == 0) {
                errors.put("uploaderror", "dms.error.uploadError");
                throw new FileNotFoundException();
            }
            writeLocalFile(docFile, fileName);
            newDoc.setContentType(docFile.getContentType());
            
            String doc_no = EDocUtil.addDocumentSQL(newDoc);
        LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.ADD, LogConst.CON_DOCUMENT, doc_no, request.getRemoteAddr());
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
            EDoc newDoc = new EDoc("", "", fileName, "", (String) request.getSession().getAttribute("user") , 'A', oscar.util.UtilDateUtilities.getToday("yyyy-MM-dd"), "", "", "demographic", "-1");
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
        if (fm.getMode().equals("add")) {
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
            String fileName = docFile.getFileName();
            EDoc newDoc = new EDoc(fm.getDocDesc(), fm.getDocType(), fileName, "", fm.getDocCreator(), 'A', fm.getObservationDate(), "", "", fm.getFunction(), fm.getFunctionId());
            newDoc.setDocPublic(fm.getDocPublic());
            fileName = newDoc.getFileName();
            //save local file;
            if (docFile.getFileSize() == 0) {
                errors.put("uploaderror", "dms.error.uploadError");
                throw new FileNotFoundException();
            }
            writeLocalFile(docFile, fileName);
            newDoc.setContentType(docFile.getContentType());

            // if the document was added in the context of a program
            String programIdStr = (String) request.getSession().getAttribute(SessionConstants.CURRENT_PROGRAM_ID);
            if (programIdStr!=null) newDoc.setProgramId(Integer.valueOf(programIdStr));
            
            //---
            String doc_no = EDocUtil.addDocumentSQL(newDoc);
            LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.ADD, LogConst.CON_DOCUMENT, doc_no, request.getRemoteAddr());
        } catch (Exception e) {
            //ActionRedirect redirect = new ActionRedirect(mapping.findForward("failAdd"));
            request.setAttribute("docerrors", errors);
            request.setAttribute("completedForm", fm);
            return false;
        }
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
	    }
            EDoc newDoc = new EDoc(fm.getDocDesc(), fm.getDocType(), fileName, "", fm.getDocCreator(), 'A', fm.getObservationDate(), reviewerId, reviewDateTime, fm.getFunction(), fm.getFunctionId());
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
            LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.UPDATE, LogConst.CON_DOCUMENT, newDoc.getDocId(), request.getRemoteAddr());
        } catch (Exception e) {
            request.setAttribute("docerrors", errors);
            request.setAttribute("completedForm", fm);
            request.setAttribute("editDocumentNo", fm.getMode());
            return mapping.findForward("failEdit");
        }
        return mapping.findForward("successEdit");
    }
    
    private void writeLocalFile(FormFile docFile, String fileName) throws Exception {
        try {
             byte[] docdata = docFile.getFileData();
             String savePath = oscar.OscarProperties.getInstance().getProperty("DOCUMENT_DIR") + "/" + fileName;
             FileOutputStream fileStream = new FileOutputStream(new File(savePath));
             fileStream.write(docdata);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private boolean filled(String s) {
	return (s!=null && s.trim().length()>0);
    }
}
