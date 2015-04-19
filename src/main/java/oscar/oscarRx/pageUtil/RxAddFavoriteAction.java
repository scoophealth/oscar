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


package oscar.oscarRx.pageUtil;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.dao.DrugDao;
import org.oscarehr.common.model.Drug;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarRx.data.RxPrescriptionData;
import oscar.oscarRx.util.RxUtil;


public final class RxAddFavoriteAction extends DispatchAction {
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
    
    
    public ActionForward unspecified(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws IOException, ServletException {
    	
		if (!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_rx", "w", null)) {
			throw new RuntimeException("missing required security object (_rx)");
		}
        
        // Setup variables        
        
        RxAddFavoriteForm frm = (RxAddFavoriteForm)form;
        
        String favoriteName = frm.getFavoriteName();
        
        RxSessionBean bean = (RxSessionBean)request.getSession().getAttribute("RxSessionBean");
        if(bean==null) {
            response.sendRedirect("error.html");
            return null;
        }
        
        String providerNo = bean.getProviderNo();
        
        if(frm.getDrugId()!=null) {
            int drugId = Integer.parseInt(frm.getDrugId());
            
        	DrugDao drugDao=(DrugDao) SpringUtils.getBean("drugDao");        	
            Drug drug=drugDao.find(drugId);
            RxPrescriptionData.addToFavorites(providerNo, favoriteName, drug);
        }
        else {
            int stashId = Integer.parseInt(frm.getStashId());
            
            bean.getStashItem(stashId).AddToFavorites(providerNo, favoriteName);
        }
        
        ActionForward fwd = mapping.findForward("success");
        String s = fwd.getPath() + frm.getReturnParams();
        request.setAttribute("BoxNoFillFirstLoad", "true");
        MiscUtils.getLogger().debug("fill box no");
        
        fwd = new ActionForward(s, true);
        
        return fwd;
    }

    //used with rx3
    public ActionForward addFav2(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws IOException {

		if (!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_rx", "w", null)) {
			throw new RuntimeException("missing required security object (_rx)");
		}
    	
        RxSessionBean bean = (RxSessionBean)request.getSession().getAttribute("RxSessionBean");
        if(bean==null) {
            response.sendRedirect("error.html");
            return null;
        }
        String randomId=request.getParameter("randomId");
        String favoriteName=request.getParameter("favoriteName");
        String drugIdStr=request.getParameter("drugId");
        String providerNo = bean.getProviderNo();

        if(drugIdStr!=null){
            int drugId=Integer.parseInt(drugIdStr);
        	DrugDao drugDao=(DrugDao) SpringUtils.getBean("drugDao");        	
            Drug drug=drugDao.find(drugId);
            RxPrescriptionData.addToFavorites(providerNo, favoriteName, drug);
        }
        else{
            int stashId=bean.getIndexFromRx(Integer.parseInt(randomId));
            bean.getStashItem(stashId).AddToFavorites(providerNo, favoriteName);
        }
       
        /*
        request.setAttribute("BoxNoFillFirstLoad", "true");
        MiscUtils.getLogger().debug("fill box no");
        */
        RxUtil.printStashContent(bean);

        return null;
    }
}
