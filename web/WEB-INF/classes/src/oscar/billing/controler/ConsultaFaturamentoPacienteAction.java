package oscar.billing.controler;

import org.apache.log4j.Category;

import org.apache.struts.action.*;

import oscar.billing.dao.AppointmentDAO;

import oscar.billing.model.Demographic;

import oscar.util.OscarAction;

import javax.servlet.http.*;


public class ConsultaFaturamentoPacienteAction extends OscarAction {
    static Category cat = Category.getInstance(ConsultaFaturamentoPacienteAction.class.getName());

    public ActionForward perform(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response) {
        ActionForward myforward = null;
        String myaction = mapping.getParameter();
        cat.debug(" [ConsultaFaturamentoPacienteAction] My action = " + myaction);

        if (isCancelled(request)) {
            cat.info(" [ConsultaFaturamentoPacienteAction] " +
                mapping.getAttribute() + " - acao foi cancelada");

            return mapping.findForward("cancel");
        }

        if ("".equalsIgnoreCase(myaction)) {
            myforward = mapping.findForward("failure");
        } else if ("INIT".equalsIgnoreCase(myaction)) {
            myforward = performInit(mapping, form, request, response);
        } else {
            myforward = mapping.findForward("failure");
        }

        return myforward;
    }

    private ActionForward performInit(ActionMapping mapping,
        ActionForm actionForm, HttpServletRequest request,
        HttpServletResponse response) {
        cat.info(" [ConsultaFaturamentoPacienteAction] INIT");

        ConsultaFaturamentoPacienteForm form = (ConsultaFaturamentoPacienteForm) actionForm;

        try {
            form.clear();
            cat.info(" [ConsultaFaturamentoPacienteAction] Limpou form");
            
            String id = request.getParameter("demographic_no");
			cat.info(" [ConsultaFaturamentoPacienteAction] id " + id);
            Demographic demographic = new Demographic();
            demographic.setDemographicNo(Long.parseLong(id));

            AppointmentDAO appDAO = new AppointmentDAO(getPropertiesDb(
                        request));

            //Obter lista de faturamentos
            request.setAttribute("BILLING", appDAO.listFatPatiente(demographic));
            cat.info(
                " [ConsultaFaturamentoPacienteAction] setou billings no form");
        } catch (Exception e) {
            generalError(request, e, "error.general");
            cat.error("erro: ", e);

            return mapping.findForward("failure");
        }

        return mapping.findForward("success");
    }

}
