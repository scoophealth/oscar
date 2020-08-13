package org.oscarehr.integration.cdx;



import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.oscarEncounter.oscarConsultationRequest.pageUtil.ConsultationAttachDocs;
import oscar.oscarEncounter.oscarConsultationRequest.pageUtil.ConsultationAttachLabs;

public class CDXMessengerAttachDocumentAction
        extends Action {

    private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)

            throws ServletException, IOException {

        if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_con", "u", null)) {
            throw new SecurityException("missing required security object (_con)");
        }

        DynaActionForm frm = (DynaActionForm)form;
        LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
        String requestId = frm.getString("requestId");
        String demoNo = frm.getString("demoNo");
      //  String requestId = frm.getString("requestId");
       // String demoNo = frm.getString("demoNo");

        if (!OscarProperties.getInstance().isPropertyActive("consultation_indivica_attachment_enabled")) {
            String[] arrDocs = frm.getStrings("attachedDocs");

            CDXMessengerAttachDocs Doc = new CDXMessengerAttachDocs(demoNo,requestId,arrDocs);
            Doc.attach(loggedInInfo);

            CDXMessengerAttachLabs Lab = new CDXMessengerAttachLabs(demoNo,requestId,arrDocs);
            Lab.attach(loggedInInfo);
            return mapping.findForward("success");
        }
        else {
            String[] labs = request.getParameterValues("labNo");
            String[] docs = request.getParameterValues("docNo");
            if (labs == null) { labs = new String[] { }; }
            if (docs == null) { docs = new String[] { }; }

            CDXMessengerAttachDocs Doc = new CDXMessengerAttachDocs(demoNo,requestId,docs);
            Doc.attach(loggedInInfo);

            CDXMessengerAttachLabs Lab = new CDXMessengerAttachLabs(demoNo,requestId,labs);
            Lab.attach(loggedInInfo);
            return mapping.findForward("success");
        }
    }
}

