package oscar.billing.popup;

import org.apache.log4j.Category;

import org.apache.struts.action.*;

import oscar.billing.cad.dao.CadAtividadeSaudeDAO;

import oscar.util.OscarAction;
import oscar.util.PagerDef;

import java.util.List;

import javax.servlet.http.*;


public class AtividadeSaudeAction extends OscarAction {
    static Category cat = Category.getInstance(AtividadeSaudeAction.class.getName());

    public ActionForward perform(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response) {
        ActionForward myforward = null;
        String myaction = mapping.getParameter();
        cat.debug(" [AtividadeSaudeAction] My action = " + myaction);

        if (isCancelled(request)) {
            cat.info(" [FormBillingAction] " + mapping.getAttribute() +
                " - acao foi cancelada");

            return mapping.findForward("cancel");
        }

        if ("".equalsIgnoreCase(myaction)) {
            myforward = mapping.findForward("failure");
        } else if ("INIT".equalsIgnoreCase(myaction)) {
            myforward = performInit(mapping, form, request, response);
        } else if ("FIND_ATIV".equalsIgnoreCase(myaction)) {
            myforward = performFindAtividadeSaude(mapping, form, request, response);
        } else {
            myforward = mapping.findForward("failure");
        }

        return myforward;
    }

    private ActionForward performInit(ActionMapping mapping,
        ActionForm actionForm, HttpServletRequest request,
        HttpServletResponse response) {
        cat.info(" [AtividadeSaudeAction] INIT");

        AtividadeSaudeForm form = (AtividadeSaudeForm) actionForm;

        try {
            form.clear();
            cat.info(" [AtividadeSaudeAction] Limpou form");
        } catch (Exception e) {
            generalError(request, e, "error.general");
            cat.error("erro: ", e);

            return mapping.findForward("failure");
        }

        return mapping.findForward("success");
    }

    private ActionForward performFindAtividadeSaude(ActionMapping mapping,
        ActionForm actionForm, HttpServletRequest request,
        HttpServletResponse response) {
        cat.info(" [AtividadeSaudeAction] FIND_ATIV");

        AtividadeSaudeForm form = (AtividadeSaudeForm) actionForm;

        try {
			CadAtividadeSaudeDAO ativDAO = new CadAtividadeSaudeDAO(getPropertiesDb(
			request));

            if (request.getParameter("coAtiv") != null) {
                form.setCodigo(request.getParameter("coAtiv"));
            }

            if (request.getParameter("dsAtiv") != null) {
                form.setDesc(request.getParameter("dsAtiv"));
            }

            List ativ = ativDAO.list(form.getCodigo(), form.getDesc());

            PagerDef pagerDef = pagination(mapping, request, ativ);

            request.setAttribute("offset", new Integer(pagerDef.offset));
            request.setAttribute("pagerHeader", pagerDef.pagerHeader);
            request.setAttribute("length", new Integer(pagerDef.length));

            form.setAtividades(ativ);

            cat.info(" [AtividadeSaudeAction] setou atividadeSaudes");
        } catch (Exception e) {
            generalError(request, e, "error.general");
            cat.error("erro: ", e);

            return mapping.findForward("failure");
        }

        return mapping.findForward("success");
    }

}
