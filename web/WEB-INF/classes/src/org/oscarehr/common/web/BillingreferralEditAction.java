/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.common.web;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.DynaBean;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.dao.BillingreferralDao;
import org.oscarehr.common.model.Billingreferral;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Toby
 */
@Transactional(propagation=Propagation.REQUIRES_NEW)
public class BillingreferralEditAction extends DispatchAction{

    private BillingreferralDao bDao;

    public void setbDao(BillingreferralDao bDao) {
        this.bDao = bDao;
    }


    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("list");
    }

    public ActionForward searchbyno(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaBean lazyForm = (DynaBean) form;
        String referralNo = (String)lazyForm.get("search");

        List<Billingreferral> referrals = bDao.getBillingreferral(referralNo);
        request.getSession().setAttribute("referrals", referrals);

        return mapping.findForward("list");
    }

    public ActionForward searchbyname(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaBean lazyForm = (DynaBean) form;
        String name = (String)lazyForm.get("search");
        String last_name="",first_name="";
        if (name != null && !name.equals("")) {
            if (name.indexOf(',') < 0) {
                last_name = name;
            } else {
                name = name.substring(0, name.indexOf(','));
                first_name = name.substring(name.indexOf(',') + 1, name.length());
            }
        }
        List<Billingreferral> referrals = bDao.getBillingreferral(last_name,first_name);
        request.getSession().setAttribute("referrals", referrals);

        return mapping.findForward("list");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaBean lazyForm = (DynaBean) form;

        List<Billingreferral> referrals = bDao.getBillingreferral(request.getParameter("referralNo"));
        Billingreferral referral = referrals.get(0);

        lazyForm.set("referral", referral);

        return mapping.findForward("detail");
    }

    public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaBean lazyForm = (DynaBean) form;

        Billingreferral referral = (Billingreferral)lazyForm.get("referral");
        bDao.updateBillingreferral(referral);

        return mapping.findForward("list");
    }
    

}
