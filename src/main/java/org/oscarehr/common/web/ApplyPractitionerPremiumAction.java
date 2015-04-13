/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * 
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */

package org.oscarehr.common.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.BillingONPremiumDao;
import org.oscarehr.common.model.BillingONPremium;
import org.oscarehr.common.model.Provider;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

/**
 *
 * @author mweston4
 */
public class ApplyPractitionerPremiumAction extends DispatchAction{
    
    private BillingONPremiumDao bPremiumDao = (BillingONPremiumDao) SpringUtils.getBean("billingONPremiumDao");
    private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
    
    public ActionForward applyPremium(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response){
        String raHeaderNoStr = request.getParameter("rano");        
        Integer raHeaderNo = Integer.parseInt(raHeaderNoStr);
        
        if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_billing", "w", null)) {
        	throw new SecurityException("missing required security object (_billing)");
        }
        
        List <BillingONPremium> bPremiumList = bPremiumDao.getRAPremiumsByRaHeaderNo(raHeaderNo);
        
        for (BillingONPremium bPremium : bPremiumList) {
            
            String premiumId = String.valueOf(bPremium.getId());
            //check this provider is in OSCAR
            String providerNo = request.getParameter("providerNo" + premiumId);
            if (providerNo != null && !providerNo.isEmpty()) {
                ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
                Provider p = providerDao.getProvider(providerNo);
                               
                String premiumSelected = request.getParameter("choosePremium" + premiumId);
            
                if (premiumSelected != null && premiumSelected.equals("Y")) {
                    bPremium.setStatus(true);                
                } else {
                    bPremium.setStatus(false);
                    providerNo = null;
                }    
            
                if (p != null) {
                    bPremium.setProviderNo(providerNo);                              
                    bPremiumDao.merge(bPremium);
                }
                else {
                    return mapping.findForward("failure"); 
                }
            }
        }
              
        return mapping.findForward("success");  
    }
}
