package oscar.dms.actions;

import java.io.*;
import org.apache.struts.action.*;
import org.apache.struts.upload.*;
import javax.servlet.http.*;
import oscar.dms.data.*;
import java.util.*;
import oscar.util.*;
import oscar.dms.*;

public class AddEditHtmlAction extends Action {
    
    /** Creates a new instance of AddLinkAction */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        AddEditDocumentForm fm = (AddEditDocumentForm) form;
        Hashtable errors = new Hashtable();
        String fileName = "";
        if ((fm.getDocDesc().length() == 0) || (fm.getDocDesc().equals("Enter Title"))) {
             errors.put("descmissing", "dms.error.descriptionInvalid");
             request.setAttribute("linkhtmlerrors", errors);
             request.setAttribute("completedForm", fm);
             request.setAttribute("function", request.getParameter("function"));
             request.setAttribute("functionid", request.getParameter("functionid"));
             request.setAttribute("editDocumentNo", fm.getMode());
             return mapping.findForward("failed");
        }
        if (fm.getDocType().length() == 0) {
             errors.put("typemissing", "dms.error.typeMissing");
             request.setAttribute("linkhtmlerrors", errors);
             request.setAttribute("completedForm", fm);
             request.setAttribute("function", request.getParameter("function"));
             request.setAttribute("functionid", request.getParameter("functionid"));
             request.setAttribute("editDocumentNo", fm.getMode());
             return mapping.findForward("failed");
        }
        if (fm.getHtml().length() == 0) {
             errors.put("urlmissing", "dms.error.htmlMissing");
             request.setAttribute("linkhtmlerrors", errors);
             request.setAttribute("completedForm", fm);
             request.setAttribute("function", request.getParameter("function"));
             request.setAttribute("functionid", request.getParameter("functionid"));

             return mapping.findForward("failed");
        }
        if (fm.getMode().equals("addLink")) {
            //the 'html' variable is the url
            //checks for http://
            String html = fm.getHtml();
            if (html.indexOf("http://") == -1) {
                html = "http://" + html;
            }
            html = "<script type=\"text/javascript\" language=\"Javascript\">\n" +
                    "window.location='" + html + "'\n" +
                    "</script>";
            fm.setDocDesc(fm.getDocDesc() + " (link)");
            fm.setHtml(html);
            fileName = "link";
        } else if (fm.getMode().equals("addHtml")) {
            fileName = "html";
        } else {
        }
        EDoc currentDoc;
        System.out.println("mode: " + fm.getMode());
        if (fm.getMode().indexOf("add") != -1) {
            currentDoc = new EDoc(fm.getDocDesc(), fm.getDocType(), fileName, org.apache.commons.lang.StringEscapeUtils.escapeJava(fm.getHtml()), fm.getDocCreator(), 'H', fm.getObservationDate(), fm.getFunction(), fm.getFunctionId());
            currentDoc.setContentType("text/html");
            currentDoc.setDocPublic(fm.getDocPublic());
            EDocUtil.addDocumentSQL(currentDoc);
        } else {
            currentDoc = new EDoc(fm.getDocDesc(), fm.getDocType(), "", org.apache.commons.lang.StringEscapeUtils.escapeJava(fm.getHtml()), fm.getDocCreator(), 'H', fm.getObservationDate(), fm.getFunction(), fm.getFunctionId());
            currentDoc.setDocId(fm.getMode());
            currentDoc.setContentType("text/html");
            currentDoc.setDocPublic(fm.getDocPublic());
            EDocUtil.editDocumentSQL(currentDoc);
        }
        ActionRedirect redirect = new ActionRedirect(mapping.findForward("success"));
        redirect.addParameter("function", request.getParameter("function"));
        redirect.addParameter("functionid", request.getParameter("functionid"));
        return redirect;
    }
    
}
