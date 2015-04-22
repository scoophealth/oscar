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

import java.util.Date;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.CaseManagementNoteLink;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.managers.ProgramManager2;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import oscar.dms.EDoc;
import oscar.dms.EDocUtil;
import oscar.dms.data.AddEditDocumentForm;
import oscar.util.UtilDateUtilities;

public class AddEditHtmlAction extends Action {
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
    /** Creates a new instance of AddLinkAction */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
	
    	if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_edoc", "w", null)) {
			throw new SecurityException("missing required security object (_edoc)");
		}
    	
        AddEditDocumentForm fm = (AddEditDocumentForm) form;
        Hashtable errors = new Hashtable();
        String fileName = "";
        if (!EDocUtil.getDoctypes(fm.getFunction()).contains(fm.getDocType())){ 
        	EDocUtil.addDocTypeSQL(fm.getDocType(),fm.getFunction());
        } 
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
	}
	
	String reviewerId = filled(fm.getReviewerId()) ? fm.getReviewerId() : "";
	String reviewDateTime = filled(fm.getReviewDateTime()) ? fm.getReviewDateTime() : "";

	if (!filled(reviewerId) && fm.getReviewDoc()) {
	    reviewerId = (String)request.getSession().getAttribute("user");
	    reviewDateTime = UtilDateUtilities.DateToString(new Date(), EDocUtil.REVIEW_DATETIME_FORMAT);
	}
        EDoc currentDoc;
        MiscUtils.getLogger().debug("mode: " + fm.getMode());
        if (fm.getMode().indexOf("add") != -1) {
            currentDoc = new EDoc(fm.getDocDesc(), fm.getDocType(), fileName, fm.getHtml(), fm.getDocCreator(), fm.getResponsibleId(), fm.getSource(), 'H', fm.getObservationDate(), reviewerId, reviewDateTime, fm.getFunction(), fm.getFunctionId());
            currentDoc.setContentType("text/html");
            currentDoc.setDocPublic(fm.getDocPublic());
            currentDoc.setDocClass(fm.getDocClass());
            currentDoc.setDocSubClass(fm.getDocSubClass());
           
            // if the document was added in the context of a program
    		ProgramManager2 programManager = SpringUtils.getBean(ProgramManager2.class);
    		LoggedInInfo loggedInInfo  = LoggedInInfo.getLoggedInInfoFromSession(request);
    		ProgramProvider pp = programManager.getCurrentProgramInDomain(loggedInInfo, loggedInInfo.getLoggedInProviderNo());
    		if(pp != null && pp.getProgramId() != null) {
    			currentDoc.setProgramId(pp.getProgramId().intValue());
    		}
    		
            String docId = EDocUtil.addDocumentSQL(currentDoc);
	    
	    /* Save annotation */
	    String attrib_name = request.getParameter("annotation_attrib");
	    HttpSession se = request.getSession();
	    WebApplicationContext  ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(se.getServletContext());
	    CaseManagementManager cmm = (CaseManagementManager) ctx.getBean("caseManagementManager");
	    if (attrib_name!=null) {
		CaseManagementNote cmn = (CaseManagementNote)se.getAttribute(attrib_name);
		if (cmn!=null) {
		    cmm.saveNoteSimple(cmn);
		    CaseManagementNoteLink cml = new CaseManagementNoteLink();
		    cml.setTableName(CaseManagementNoteLink.DOCUMENT);
		    cml.setTableId(Long.valueOf(docId));
		    cml.setNoteId(cmn.getId());
		    cmm.saveNoteLink(cml);

		    se.removeAttribute(attrib_name);
		}
	    }
        } else {
            currentDoc = new EDoc(fm.getDocDesc(), fm.getDocType(), "", fm.getHtml(), fm.getDocCreator(), fm.getResponsibleId(), fm.getSource(), 'H', fm.getObservationDate(), reviewerId, reviewDateTime, fm.getFunction(), fm.getFunctionId());
            currentDoc.setDocId(fm.getMode());
            currentDoc.setContentType("text/html");
            currentDoc.setDocPublic(fm.getDocPublic());
            currentDoc.setDocClass(fm.getDocClass());
            currentDoc.setDocSubClass(fm.getDocSubClass());
            EDocUtil.editDocumentSQL(currentDoc, fm.getReviewDoc());
        }
        ActionRedirect redirect = new ActionRedirect(mapping.findForward("success"));
        redirect.addParameter("function", request.getParameter("function"));
        redirect.addParameter("functionid", request.getParameter("functionid"));
        return redirect;
    }
    
    private boolean filled(String s) {
	return (s!=null && s.trim().length()>0);
    }
}
