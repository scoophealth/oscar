/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
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
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;

import oscar.oscarRx.data.RxPrescriptionData;
import oscar.oscarRx.util.RxUtil;


public final class RxUseFavoriteAction extends DispatchAction {
    
    
    public ActionForward unspecified(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws IOException, ServletException {
     //   System.out.println("***###IN RxUseFavoriteAction.java");
        // Extract attributes we will need
        Locale locale = getLocale(request);
        MessageResources messages = getResources(request);
        
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
          //   System.out.println("***###addStathItem called22");
            bean.setStashIndex(bean.addStashItem(rx));
            request.setAttribute("BoxNoFillFirstLoad", "true");
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
        }
        
        return (mapping.findForward("success"));
    }

    public ActionForward useFav2(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws IOException, ServletException {
     //  System.out.println("==========***###IN RxUseFavoriteAction.java=============");
        // Extract attributes we will need
        Locale locale = getLocale(request);
        MessageResources messages = getResources(request);

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
        //    System.out.println("favoriteId ="+favoriteId);
         //   System.out.println("randomId ="+randomId);
            RxPrescriptionData rxData =
            new RxPrescriptionData();

            // get favorite
            RxPrescriptionData.Favorite fav =
            rxData.getFavorite(favoriteId);

            // create Prescription
            RxPrescriptionData.Prescription rx =
            rxData.newPrescription(bean.getProviderNo(), bean.getDemographicNo(), fav);
            rx.setRandomId(Long.parseLong(randomId));
            if(rx!=null){
                String spec=RxUtil.trimSpecial(rx);
                rx.setSpecial(spec);
         //       System.out.println(rx.getBrandName()+" rx.getspeiclia="+rx.getSpecial());
            }
            else{
                System.out.println("rx is null!");
            }
            
            bean.addAttributeName(rx.getAtcCode() + "-" + String.valueOf(bean.getStashIndex()));
          //   System.out.println("***###addStathItem called22");
            bean.setStashIndex(bean.addStashItem(rx));

            List<RxPrescriptionData.Prescription> listRxDrugs=new ArrayList();
            listRxDrugs.add(rx);
            request.setAttribute("listRxDrugs",listRxDrugs);
            request.setAttribute("BoxNoFillFirstLoad", "true");
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
        }

        RxUtil.printStashContent(bean);
      //  System.out.println("==========***###END RxUseFavoriteAction.java=============");
        return (mapping.findForward("useFav2"));
    }
}

