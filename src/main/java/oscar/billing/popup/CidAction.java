
package oscar.billing.popup;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.util.MiscUtils;

import oscar.billing.cad.dao.CidDAO;
import oscar.billing.cad.model.CadCid;
import oscar.util.OscarAction;
import oscar.util.PagerDef;

@Deprecated
public class CidAction extends OscarAction {
    private static Logger logger = MiscUtils.getLogger();

    public ActionForward execute(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response) {
        ActionForward myforward = null;
        String myaction = mapping.getParameter();
        logger.debug(" [CidAction] My action = " + myaction);

        if (isCancelled(request)) {
            logger.info(" [FormBillingAction] " + mapping.getAttribute() +
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
        logger.info(" [CidAction] INIT");

        CidForm form = (CidForm) actionForm;

        try {
            form.clear();
            logger.info(" [CidAction] Limpou form");
        } catch (Exception e) {
            generalError(request, e, "error.general");
            logger.error("erro: ", e);

            return mapping.findForward("failure");
        }

        return mapping.findForward("success");
    }

    private ActionForward performFindCid(ActionMapping mapping,
        ActionForm actionForm, HttpServletRequest request,
        HttpServletResponse response) {
        logger.info(" [CidAction] FIND_CID");

        CidForm form = (CidForm) actionForm;

        try {
			CidDAO cidDAO = new CidDAO();

            if (request.getParameter("coCid") != null) {
                form.setCodigo(request.getParameter("coCid"));
            }

            if (request.getParameter("dsCid") != null) {
                form.setDesc(request.getParameter("dsCid"));
            }

            List<CadCid> cid = cidDAO.list(form.getCodigo(), form.getDesc());

            PagerDef pagerDef = pagination(mapping, request, cid);

            request.setAttribute("offset", new Integer(pagerDef.offset));
            request.setAttribute("pagerHeader", pagerDef.pagerHeader);
            request.setAttribute("length", new Integer(pagerDef.length));

            form.setCid(cid);

            logger.info(" [CidAction] setou procedimentos");
        } catch (Exception e) {
            generalError(request, e, "error.general");
            logger.error("erro: ", e);

            return mapping.findForward("failure");
        }

        return mapping.findForward("success");
    }

}
