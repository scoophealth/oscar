/**
 * Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */

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
import org.oscarehr.util.SpringUtils;

/**
 *
 * @author Toby
 */

public class BillingreferralEditAction extends DispatchAction{

    private BillingreferralDao bDao = (BillingreferralDao)SpringUtils.getBean("BillingreferralDAO");



    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return list(mapping,form,request,response);
    }

    public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	List<Billingreferral> referrals = bDao.getBillingreferrals();
        request.setAttribute("referrals", referrals);
        request.setAttribute("searchBy", "searchByName");
        return mapping.findForward("list");
    }

    public ActionForward searchByNo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaBean lazyForm = (DynaBean) form;
        String referralNo = (String)lazyForm.get("search");

        List<Billingreferral> referrals = bDao.getBillingreferral(referralNo);
        request.setAttribute("referrals", referrals);
        request.setAttribute("searchBy", "searchByNo");

        return mapping.findForward("list");
    }

    public ActionForward searchBySpecialty(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaBean lazyForm = (DynaBean) form;
        String specialty = (String)lazyForm.get("search");

        List<Billingreferral> referrals = bDao.getBillingreferralBySpecialty(specialty);
        request.setAttribute("referrals", referrals);
        request.setAttribute("searchBy", "searchBySpecialty");

        return mapping.findForward("list");
    }


    public ActionForward searchByName(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
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
        request.setAttribute("referrals", referrals);
        request.setAttribute("searchBy", "searchByName");

        return mapping.findForward("list");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaBean lazyForm = (DynaBean) form;

        List<Billingreferral> referrals = bDao.getBillingreferral(request.getParameter("referralNo"));
        Billingreferral referral = referrals.get(0);

        lazyForm.set("referral", referral);

        return mapping.findForward("detail");
    }

    public ActionForward add(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaBean lazyForm = (DynaBean) form;

        Billingreferral referral = new Billingreferral();

        lazyForm.set("referral", referral);

        return mapping.findForward("detail");
    }

    public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaBean lazyForm = (DynaBean) form;

        Billingreferral referral = (Billingreferral)lazyForm.get("referral");
        bDao.updateBillingreferral(referral);

        return list(mapping,form,request,response);
    }


}
