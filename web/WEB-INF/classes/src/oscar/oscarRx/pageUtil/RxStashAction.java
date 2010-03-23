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
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;
import javax.servlet.http.HttpServletRequest;

public final class RxStashAction extends DispatchAction {

//public final class RxStashAction extends Action {
    public ActionForward unspecified(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws IOException, ServletException {
            //    System.out.println("===========start in rxstatshaction.java===========");
        // Extract attributes we will need
        Locale locale = getLocale(request);
        MessageResources messages = getResources(request);

        // Setup variables

        RxStashForm frm = (RxStashForm)form;
        RxSessionBean bean = (RxSessionBean)request.getSession().getAttribute("RxSessionBean");
       // bean.setStashIndex(11);
        if(bean==null) {
            response.sendRedirect("error.html");
            return null;
        }

        if(frm.getStashId()>=0 && frm.getStashId() < bean.getStashSize()) {
            if(frm.getAction().equals("edit")) {

                request.setAttribute("BoxNoFillFirstLoad", "true");

                bean.setStashIndex(frm.getStashId());
                //bean.setStashIndex(11);
            }
            if(frm.getAction().equals("delete")) {
                bean.removeStashItem(frm.getStashId());

                if(bean.getStashIndex() >= bean.getStashSize()) {
                    bean.setStashIndex(bean.getStashSize() - 1);
                }
            }
        }
   /*     System.out.println("bean.getStashIndex()="+bean.getStashIndex());
         System.out.println("bean.getStashSize()="+bean.getStashSize());
        System.out.println("===========end in rxstatshaction.java===========");
     */   return mapping.findForward("success");
    }

    public ActionForward setStashIndex(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws IOException, ServletException {
       //         System.out.println("===========start in setStashIndex rxstatshaction.java===========");
        // Extract attributes we will need
        Locale locale = getLocale(request);
        MessageResources messages = getResources(request);
        String wp=null;
        try{
        wp=request.getParameter("randomId");

        int randomId;


        if(wp!=null && !wp.equals("null")){
         //   System.out.println("in if wp="+wp);
            randomId=Integer.parseInt(wp);
         //   System.out.println("in setStashIndex randomId="+""+randomId);
        }else{
         //   System.out.println("in else wp="+wp);
            randomId=-1;
        }


        // Setup variables
        RxSessionBean bean = (RxSessionBean)request.getSession().getAttribute("RxSessionBean");

        if(bean==null) {
            response.sendRedirect("error.html");
            return null;
        }
      //  System.out.println("bean.getStashSize()="+bean.getStashSize());
      //  System.out.println("bean.getStashIndex() before setting="+bean.getStashIndex());
        //find the stashIndex corresponding to the random number
        int stashId=bean.getIndexFromRx(randomId);
        if(stashId >=0 && stashId  < bean.getStashSize()) {
            bean.setStashIndex(stashId);
        }
     //   System.out.println("set the stash index to="+bean.getStashIndex());
     //    System.out.println("the stash size becomes="+bean.getStashSize());
        }catch(Exception e){e.printStackTrace();}
      //  System.out.println("===========end in setStashIndex rxstatshaction.java===========");
        return mapping.findForward("success");
    }

    public ActionForward deletePrescribe(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws IOException, ServletException {
                System.out.println("===========start in deletePrescribe ===========");
        // Extract attributes we will need
        Locale locale = getLocale(request);
        MessageResources messages = getResources(request);

        RxSessionBean bean = (RxSessionBean)request.getSession().getAttribute("RxSessionBean");

        if(bean==null) {
            response.sendRedirect("error.html");
            return null;
        }

        int randomId=Integer.parseInt(request.getParameter("randomId"));
       System.out.println("randomId="+randomId);
       int stashId=bean.getIndexFromRx(randomId);
       if(stashId!=-1){
                bean.removeStashItem(stashId);
                if(bean.getStashIndex() >= bean.getStashSize()) {
                    bean.setStashIndex(bean.getStashSize() - 1);
                }
       }else{
        System.out.println("stashId iss  -1");
       }

        return mapping.findForward("success");
    }


}