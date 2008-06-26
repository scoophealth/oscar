package org.oscarehr.common.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.dao.ClinicDAO;
import org.oscarehr.common.model.Clinic;

public class ClinicManageAction extends DispatchAction {

    private ClinicDAO clinicDAO;

    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("ClinicManageAction-unspec");
        return view(mapping, form, request, response);
    }

    public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("ClinicManageAction-view");
        Clinic clinic = clinicDAO.getClinic();
        System.out.println("Clinic view :"+clinic);
        DynaActionForm frm = (DynaActionForm)form;
        frm.set("clinic",clinic);
        request.setAttribute("clinicForm",form);
        return mapping.findForward("success");
    }

    public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("ClinicManageAction-update");
        DynaActionForm frm = (DynaActionForm)form;
        Clinic clinic = (Clinic) frm.get("clinic");
        clinicDAO.save(clinic);
        
        System.out.println("clinic : "+clinic);
        
        return mapping.findForward("success");
    }

    public void setClinicDAO(ClinicDAO clinicDAO) {
        this.clinicDAO = clinicDAO;
    }
}