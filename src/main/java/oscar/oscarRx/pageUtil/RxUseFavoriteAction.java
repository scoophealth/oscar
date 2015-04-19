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
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarRx.data.RxPrescriptionData;
import oscar.oscarRx.util.RxUtil;


public final class RxUseFavoriteAction extends DispatchAction {
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);

    public ActionForward unspecified(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws IOException, ServletException {


    	LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		if (!securityInfoManager.hasPrivilege(loggedInInfo, "_rx", "r", null)) {
			throw new RuntimeException("missing required security object (_rx)");
		}
    	
    	
        // Setup variables
        oscar.oscarRx.pageUtil.RxSessionBean bean =
        (oscar.oscarRx.pageUtil.RxSessionBean)request.getSession().getAttribute("RxSessionBean");
        if(bean==null){
            response.sendRedirect("error.html");
            return null;
        }

        try {
            int favoriteId = Integer.parseInt(((RxUseFavoriteForm)form).getFavoriteId());
            RxPrescriptionData rxData =
            new RxPrescriptionData();

            // get favorite
            RxPrescriptionData.Favorite fav =
            rxData.getFavorite(favoriteId);

            // create Prescription
            RxPrescriptionData.Prescription rx =
            rxData.newPrescription(bean.getProviderNo(), bean.getDemographicNo(), fav);

            bean.addAttributeName(rx.getAtcCode() + "-" + String.valueOf(bean.getStashIndex()));

            bean.setStashIndex(bean.addStashItem(loggedInInfo, rx));
            request.setAttribute("BoxNoFillFirstLoad", "true");
        }
        catch (Exception e) {
           MiscUtils.getLogger().error("Error", e);
        }

        return (mapping.findForward("success"));
    }

    public ActionForward useFav2(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws IOException {

    	LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		if (!securityInfoManager.hasPrivilege(loggedInInfo, "_rx", "r", null)) {
			throw new RuntimeException("missing required security object (_rx)");
		}
    	
        // Setup variables
        oscar.oscarRx.pageUtil.RxSessionBean bean =
        (oscar.oscarRx.pageUtil.RxSessionBean)request.getSession().getAttribute("RxSessionBean");
        if(bean==null){
            response.sendRedirect("error.html");
            return null;
        }

        try {
            int favoriteId = Integer.parseInt(request.getParameter("favoriteId"));
            String randomId=request.getParameter("randomId");


            RxPrescriptionData rxData =
            new RxPrescriptionData();

            // get favorite
            RxPrescriptionData.Favorite fav =
            rxData.getFavorite(favoriteId);

            // create Prescription
            RxPrescriptionData.Prescription rx =
            rxData.newPrescription(bean.getProviderNo(), bean.getDemographicNo(), fav);
            rx.setRandomId(Long.parseLong(randomId));

                String spec=RxUtil.trimSpecial(rx);
                rx.setSpecial(spec);

            bean.addAttributeName(rx.getAtcCode() + "-" + String.valueOf(bean.getStashIndex()));

            List<RxPrescriptionData.Prescription> listRxDrugs=new ArrayList();
            if(RxUtil.isRxUniqueInStash(bean, rx)){
                listRxDrugs.add(rx);
            }
            int rxStashIndex=bean.addStashItem(loggedInInfo, rx);
            bean.setStashIndex(rxStashIndex);


            request.setAttribute("listRxDrugs",listRxDrugs);
            request.setAttribute("BoxNoFillFirstLoad", "true");
        }
        catch (Exception e) {
           MiscUtils.getLogger().error("Error", e);
        }

        RxUtil.printStashContent(bean);

        return (mapping.findForward("useFav2"));
    }
}
