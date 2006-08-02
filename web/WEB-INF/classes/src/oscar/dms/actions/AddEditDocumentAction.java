
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
            addDocument(fm, request);
        } else if (!fm.getMode().equals("add")) {
            editDocument(fm, request);
        }
        return mapping.findForward("finished");
    }
    
    private void addDocument(AddEditDocumentForm fm, HttpServletRequest request) {
        Hashtable errors = new Hashtable();
        try {
            if ((fm.getDocDesc().length() == 0) || (fm.getDocDesc().equals("Enter Document Title"))) {
                errors.put("descmissing", "dms.error.descriptionInvalid");
                throw new Exception();
            }
            if (fm.getDocType().length() == 0) {
                errors.put("typemissing", "dms.error.typeMissing");
                throw new Exception();
            }
            FormFile docFile = fm.getDocFile();
            String fileName = docFile.getFileName();
            EDoc newDoc = new EDoc(fm.getDocDesc(), fm.getDocType(), fileName, "", fm.getDocCreator(), 'A', fm.getFunction(), fm.getFunctionId());
            
            fileName = newDoc.getFileName();
            //save local file;
            if (docFile.getFileSize() == 0) {
                errors.put("uploaderror", "dms.error.uploadError");
                throw new FileNotFoundException();
            }
            writeLocalFile(docFile, fileName);
            newDoc.setContentType(docFile.getContentType());
            //---
            EDocUtil.addDocumentSQL(newDoc);
        } catch (Exception e) {
            request.setAttribute("errors", errors);
            request.setAttribute("completedForm", fm);
            request.setAttribute("function", request.getParameter("function"));
            request.setAttribute("functionid", request.getParameter("functionid"));
            e.printStackTrace();
            return;
        }
        request.setAttribute("errors", errors);  //Allows the JSP to check if the document was just submitted
        request.setAttribute("function", request.getParameter("function"));
        request.setAttribute("functionid", request.getParameter("functionid"));
    }
    
    private void editDocument(AddEditDocumentForm fm, HttpServletRequest request) {
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

            EDoc newDoc = new EDoc(fm.getDocDesc(), fm.getDocType(), fileName, "", fm.getDocCreator(), 'A', fm.getFunction(), fm.getFunctionId());
            newDoc.setDocId(fm.getMode());
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
            request.setAttribute("errors", errors);
            request.setAttribute("completedForm", fm);
            request.setAttribute("function", request.getParameter("function"));
            request.setAttribute("functionid", request.getParameter("functionid"));
            request.setAttribute("editDocumentNo", fm.getMode());
            e.printStackTrace();
            return;
        }
        request.setAttribute("function", request.getParameter("function"));
        request.setAttribute("functionid", request.getParameter("functionid"));
    }
    
    private void writeLocalFile(FormFile docFile, String fileName) throws Exception {
         byte[] docdata = docFile.getFileData();
         String savePath = oscar.OscarProperties.getInstance().getProperty("DOCUMENT_DIR") + "/" + fileName;
         FileOutputStream fileStream = new FileOutputStream(new File(savePath));
         fileStream.write(docdata);
    }
    
}
