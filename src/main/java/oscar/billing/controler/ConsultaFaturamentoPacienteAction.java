
package oscar.billing.controler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.util.MiscUtils;

import oscar.billing.dao.AppointmentDAO;
import oscar.util.OscarAction;

@Deprecated
public class ConsultaFaturamentoPacienteAction extends OscarAction {
    private static Logger logger = MiscUtils.getLogger();

    public ActionForward execute(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response) {
        ActionForward myforward = null;
        String myaction = mapping.getParameter();
        logger.debug(" [ConsultaFaturamentoPacienteAction] My action = " + myaction);

        if (isCancelled(request)) {
            logger.info(" [ConsultaFaturamentoPacienteAction] " +
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
        logger.info(" [ConsultaFaturamentoPacienteAction] INIT");

        ConsultaFaturamentoPacienteForm form = (ConsultaFaturamentoPacienteForm) actionForm;

        try {
            form.clear();
            logger.info(" [ConsultaFaturamentoPacienteAction] Limpou form");

            String id = request.getParameter("demographic_no");
			logger.info(" [ConsultaFaturamentoPacienteAction] id " + id);
            Demographic demographic = new Demographic();
            demographic.setDemographicNo(Integer.parseInt(id));

            AppointmentDAO appDAO = new AppointmentDAO();

            //Obter lista de faturamentos
            request.setAttribute("BILLING", appDAO.listFatPatiente(demographic));
            logger.info(
                " [ConsultaFaturamentoPacienteAction] setou billings no form");
        } catch (Exception e) {
            generalError(request, e, "error.general");
            logger.error("erro: ", e);

            return mapping.findForward("failure");
        }

        return mapping.findForward("success");
    }

}
