package oscar.billing.popup;

import org.apache.log4j.Category;

import org.apache.struts.action.*;

import oscar.billing.cad.dao.CidDAO;

import oscar.util.OscarAction;
import oscar.util.PagerDef;

import java.util.List;

import javax.servlet.http.*;


public class CidAction extends OscarAction {
    static Category cat = Category.getInstance(CidAction.class.getName());

    public ActionForward perform(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response) {
        ActionForward myforward = null;
        String myaction = mapping.getParameter();
        cat.debug(" [CidAction] My action = " + myaction);

        if (isCancelled(request)) {
            cat.info(" [FormBillingAction] " + mapping.getAttribute() +
                " - acao foi cancelada");

            return mapping.findForward("cancel");
        }

        if ("".equalsIgnoreCase(myaction)) {
            myforward = mapping.findForward("failure");
        } else if ("INIT".equalsIgnoreCase(myaction)) {
            myforward = performInit(mapping, form, request, response);
        } else if ("FIND_CID".equalsIgnoreCase(myaction)) {
            myforward = performFindCid(mapping, form, request, response);
        } else {
            myforward = mapping.findForward("failure");
        }

        return myforward;
    }

    private ActionForward performInit(ActionMapping mapping,
        ActionForm actionForm, HttpServletRequest request,
        HttpServletResponse response) {
        cat.info(" [CidAction] INIT");

        CidForm form = (CidForm) actionForm;

        try {
            form.clear();
            cat.info(" [CidAction] Limpou form");
        } catch (Exception e) {
            generalError(request, e, "error.general");
            cat.error("erro: ", e);

            return mapping.findForward("failure");
        }

        return mapping.findForward("success");
    }

    private ActionForward performFindCid(ActionMapping mapping,
        ActionForm actionForm, HttpServletRequest request,
        HttpServletResponse response) {
        cat.info(" [CidAction] FIND_CID");

        CidForm form = (CidForm) actionForm;

        try {
			CidDAO cidDAO = new CidDAO(getPropertiesDb(
			request));

            if (request.getParameter("coCid") != null) {
                form.setCodigo(request.getParameter("coCid"));
            }

            if (request.getParameter("dsCid") != null) {
                form.setDesc(request.getParameter("dsCid"));
            }

            List cid = cidDAO.list(form.getCodigo(), form.getDesc());

            PagerDef pagerDef = pagination(mapping, request, cid);

            request.setAttribute("offset", new Integer(pagerDef.offset));
            request.setAttribute("pagerHeader", pagerDef.pagerHeader);
            request.setAttribute("length", new Integer(pagerDef.length));

            form.setCid(cid);

            cat.info(" [CidAction] setou procedimentos");
        } catch (Exception e) {
            generalError(request, e, "error.general");
            cat.error("erro: ", e);

            return mapping.findForward("failure");
        }

        return mapping.findForward("success");
    }

}
