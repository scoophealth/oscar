package oscar.appt.status.web;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.LazyValidatorForm;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import oscar.appt.status.model.AppointmentStatus;
import oscar.appt.status.service.AppointmentStatusMgr;

public class AppointmentStatusAction extends DispatchAction {

    private Log logger = LogFactory.getLog(this.getClass());

    public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        logger.warn("view");
        populateAllStatus(request);
        return mapping.findForward("success");
    }

    public ActionForward reset(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        logger.warn("reset");
        AppointmentStatusMgr apptStatusMgr = getApptStatusMgr();
        apptStatusMgr.reset();
        populateAllStatus(request);
        return mapping.findForward("success");
    }

    public ActionForward changestatus(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        logger.warn("changestatus");
        AppointmentStatusMgr apptStatusMgr = getApptStatusMgr();
        int ID = Integer.parseInt(request.getParameter("statusID"));
        int iActive = Integer.parseInt(request.getParameter("iActive"));
        apptStatusMgr.changeStatus(ID, iActive);
        populateAllStatus(request);
        return mapping.findForward("success");
    }

    public ActionForward modify(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        logger.warn("modify");
        AppointmentStatusMgr apptStatusMgr = getApptStatusMgr();
        int ID = Integer.parseInt(request.getParameter("statusID"));
        AppointmentStatus appt = apptStatusMgr.getStatus(ID);
        LazyValidatorForm lazyForm = (LazyValidatorForm) form;
        lazyForm.set("ID", ID);
        lazyForm.set("apptStatus", appt.getStatus());
        lazyForm.set("apptDesc", appt.getDescription());
        lazyForm.set("apptOldColor", appt.getColor());

        return mapping.findForward("edit");
    }

    public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        logger.warn("update");
        AppointmentStatusMgr apptStatusMgr = getApptStatusMgr();
        LazyValidatorForm lazyForm = (LazyValidatorForm) form;

        int ID = Integer.parseInt(lazyForm.get("ID").toString());
        String strDesc = lazyForm.get("apptDesc").toString();
        String strColor = lazyForm.get("apptColor").toString();
        if (null==strColor || strColor.equals(""))
            strColor = lazyForm.get("apptOldColor").toString();
        apptStatusMgr.modifyStatus(ID, strDesc, strColor);
        populateAllStatus(request);
        return mapping.findForward("success");
    }

    public WebApplicationContext getApptContext() {
        return WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
    }

    public AppointmentStatusMgr getApptStatusMgr() {
        return (AppointmentStatusMgr) getApptContext().getBean("AppointmentStatusMgr");
    }

    private void populateAllStatus(HttpServletRequest request) {
        AppointmentStatusMgr apptStatusMgr = getApptStatusMgr();
        List allStatus = apptStatusMgr.getAllStatus();
        request.setAttribute("allStatus", allStatus);
        int iUseStatus = apptStatusMgr.checkStatusUsuage(allStatus);
        if (iUseStatus > 0) {
            request.setAttribute("useStatus", apptStatusMgr.getStatus(iUseStatus + 1).getStatus());
        }
    }
}
