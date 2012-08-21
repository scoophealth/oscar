
package oscar.billing.controler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.util.MiscUtils;

import oscar.billing.dao.AppointmentDAO;
import oscar.billing.dao.ProviderDAO;
import oscar.billing.model.Provider;
import oscar.util.OscarAction;

@Deprecated
public class ConsultaFaturamentoMedicoAction extends OscarAction {
    private static Logger logger = MiscUtils.getLogger();

    public ActionForward execute(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response) {
        ActionForward myforward = null;
        ConsultaFaturamentoMedicoForm consultaFaturamentoMedicoForm = (ConsultaFaturamentoMedicoForm) form;
        String myaction = mapping.getParameter();
        logger.debug(" [ConsultaFaturamentoMedicoAction] My action = " + myaction);

        if (isCancelled(request)) {
            logger.info(" [ConsultaFaturamentoMedicoAction] " +
                mapping.getAttribute() + " - acao foi cancelada");

            return mapping.findForward("cancel");
        }

        if ("".equalsIgnoreCase(myaction)) {
            myforward = mapping.findForward("failure");
        } else if ("INIT".equalsIgnoreCase(myaction)) {
            myforward = performInit(mapping, form, request, response);
        } else if ("SEARCH".equalsIgnoreCase(myaction)) {
            myforward = performSearch(mapping, form, request,
                    response);
        } else {
            myforward = mapping.findForward("failure");
        }

        return myforward;
    }

    private ActionForward performInit(ActionMapping mapping,
        ActionForm actionForm, HttpServletRequest request,
        HttpServletResponse response) {
        logger.info(" [ConsultaFaturamentoMedicoAction] INIT");

        ConsultaFaturamentoMedicoForm form = (ConsultaFaturamentoMedicoForm) actionForm;

        try {
            form.clear();
            logger.info(" [ConsultaFaturamentoMedicoAction] Limpou form");

            ProviderDAO provDAO = new ProviderDAO(getPropertiesDb(
                        request));

            //Obter lista de medicos
            request.setAttribute("PROVIDERS", provDAO.list(Provider.DOCTOR));
            logger.info(
                " [ConsultaFaturamentoMedicoAction] setou providers no form");
        } catch (Exception e) {
            generalError(request, e, "error.general");
            logger.error("erro: ", e);

            return mapping.findForward("failure");
        }

        return mapping.findForward("success");
    }

    private ActionForward performSearch(ActionMapping mapping,
        ActionForm actionForm, HttpServletRequest request,
        HttpServletResponse response) {
        logger.info(" [ConsultaFaturamentoMedicoAction] SEARCH");

        ConsultaFaturamentoMedicoForm form = (ConsultaFaturamentoMedicoForm) actionForm;

        try {
            AppointmentDAO appDAO = new AppointmentDAO();

            //selecionar procedimentos do formulario XXX
            form.setConsultas(appDAO.listFatDoctor(form.getTipoPesquisa(), form.getMedico()));
            
			ProviderDAO provDAO = new ProviderDAO(getPropertiesDb(
						request));

			//Obter lista de medicos
			request.setAttribute("PROVIDERS", provDAO.list(Provider.DOCTOR));
			logger.info(
				" [ConsultaFaturamentoMedicoAction] setou providers no form");            
        } catch (Exception e) {
            generalError(request, e, "error.general");

            return mapping.findForward("failure");
        }

        return mapping.findForward("success");
    }

}
