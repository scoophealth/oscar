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
import org.oscarehr.common.dao.ProfessionalSpecialistDao;
import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.util.SpringUtils;

/**
 *
 * @author Toby
 */

public class BillingreferralEditAction extends DispatchAction{

    private ProfessionalSpecialistDao psDao = SpringUtils.getBean(ProfessionalSpecialistDao.class);
    


    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return list(mapping,form,request,response);
    }

    public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	List<ProfessionalSpecialist> referrals = psDao.findAll();
        request.setAttribute("referrals", referrals);
        request.setAttribute("searchBy", "searchByName");
        return mapping.findForward("list");
    }

    public ActionForward searchByNo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaBean lazyForm = (DynaBean) form;
        String referralNo = (String)lazyForm.get("search");

        List<ProfessionalSpecialist> referrals = psDao.findByReferralNo(referralNo);
        request.setAttribute("referrals", referrals);
        
        request.setAttribute("searchBy", "searchByNo");

        return mapping.findForward("list");
    }

    public ActionForward searchBySpecialty(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaBean lazyForm = (DynaBean) form;
        String specialty = (String)lazyForm.get("search");

        List<ProfessionalSpecialist> referrals = psDao.findBySpecialty(specialty);
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
       
        List<ProfessionalSpecialist> referrals = psDao.findByFullName(last_name, first_name);
        request.setAttribute("referrals", referrals);
        request.setAttribute("searchBy", "searchByName");

        return mapping.findForward("list");
    }

    
  
}
