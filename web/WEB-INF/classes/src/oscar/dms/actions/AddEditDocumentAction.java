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

import java.io.*;
import org.apache.struts.action.*;
import org.apache.struts.upload.*;
import javax.servlet.http.*;
import oscar.dms.data.*;
import java.util.*;
import oscar.dms.*;

public class AddEditDocumentAction extends Action {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
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
            EDoc newDoc = new EDoc(fm.getDocDesc(), fm.getDocType(), fileName, "", fm.getDocCreator(), 'A', fm.getObservationDate(), fm.getFunction(), fm.getFunctionId());
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
            String programIdStr = (String) request.getSession().getAttribute("infirmaryView_programId");
            if (programIdStr!=null) newDoc.setProgramId(Integer.valueOf(programIdStr));
            
            //---
            EDocUtil.addDocumentSQL(newDoc);
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

            EDoc newDoc = new EDoc(fm.getDocDesc(), fm.getDocType(), fileName, "", fm.getDocCreator(), 'A', fm.getObservationDate(), fm.getFunction(), fm.getFunctionId());
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
            EDocUtil.editDocumentSQL(newDoc);
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
    
}
