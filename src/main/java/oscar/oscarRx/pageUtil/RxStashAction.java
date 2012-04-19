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
import org.oscarehr.util.MiscUtils;

public final class RxStashAction extends DispatchAction {

//public final class RxStashAction extends Action {
    public ActionForward unspecified(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws IOException, ServletException {



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
        return mapping.findForward("success");
    }

    public ActionForward setStashIndex(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    {


        String wp=null;
        try{
        wp=request.getParameter("randomId");

        int randomId;


        if(wp!=null && !wp.equals("null")){

            randomId=Integer.parseInt(wp);

        }else{

            randomId=-1;
        }


        // Setup variables
        RxSessionBean bean = (RxSessionBean)request.getSession().getAttribute("RxSessionBean");

        if(bean==null) {
            response.sendRedirect("error.html");
            return null;
        }


        //find the stashIndex corresponding to the random number
        int stashId=bean.getIndexFromRx(randomId);
        if(stashId >=0 && stashId  < bean.getStashSize()) {
            bean.setStashIndex(stashId);
        }


        }catch(Exception e){MiscUtils.getLogger().error("Error", e);}

        return mapping.findForward("success");
    }

    public ActionForward deletePrescribe(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws IOException {
                MiscUtils.getLogger().debug("===========start in deletePrescribe ===========");


        RxSessionBean bean = (RxSessionBean)request.getSession().getAttribute("RxSessionBean");

        if(bean==null) {
            response.sendRedirect("error.html");
            return null;
        }

        int randomId=Integer.parseInt(request.getParameter("randomId"));
       MiscUtils.getLogger().debug("randomId="+randomId);
       int stashId=bean.getIndexFromRx(randomId);
       if(stashId!=-1){
                bean.removeStashItem(stashId);
                if(bean.getStashIndex() >= bean.getStashSize()) {
                    bean.setStashIndex(bean.getStashSize() - 1);
                }
       }else{
        MiscUtils.getLogger().debug("stashId iss  -1");
       }

        return mapping.findForward("success");
    }


}
