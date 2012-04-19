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


package oscar.oscarBilling.ca.bc.pageUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.dao.BillingServiceDao;
import org.oscarehr.common.model.BillingService;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarBilling.ca.bc.data.BillingCodeData;
import oscar.util.UtilDateUtilities;

public final class BillingEditCodeAction extends DispatchAction {
     private static BillingServiceDao billingServiceDao = (BillingServiceDao) SpringUtils.getBean("billingServiceDao");

    public ActionForward ajaxCodeUpdate(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)throws IOException  {
        String id  =  request.getParameter("id");
        String val =       request.getParameter("val");
        String billingServiceDate =       request.getParameter("billService");
        String termDate =        request.getParameter("termDate");
        String codeId = request.getParameter("codeId");

        BillingService itemCode = billingServiceDao.find(Integer.parseInt(codeId));

        itemCode.setValue(val);
        itemCode.setBillingserviceDate(UtilDateUtilities.StringToDate(billingServiceDate));
        itemCode.setTerminationDate(UtilDateUtilities.StringToDate(termDate));
        billingServiceDao.merge(itemCode);

        Map map = new HashMap();
        map.put("id",id);
        map.put("val",val);
        map.put("billService",billingServiceDate);
        map.put("termDate",termDate);
        JSONObject jsonObject =   JSONObject.fromObject( itemCode );  //(JSONObject) JSONSerializer.toJSON(itemCode);//
        jsonObject = jsonObject.accumulate("id", id);
        MiscUtils.getLogger().debug(jsonObject.toString());
        response.getOutputStream().write(jsonObject.toString().getBytes());
        return null;
    }

    public ActionForward returnToSearch(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) {
        String whereTo = request.getParameter("whereTo");
        ActionForward retval;
        if(whereTo == null || whereTo.equals("") || whereTo.equals("null")){
           retval = mapping.findForward("success");
        }else{
           retval = mapping.findForward("private");
        }
        return retval;
    }


    public ActionForward unspecified(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws IOException, ServletException {



        if(request.getSession().getAttribute("user") == null  ){
            return (mapping.findForward("Logout"));
        }

        BillingEditCodeForm frm = (BillingEditCodeForm) form;

        String codeId  =frm.getCodeId();
        String code    =frm.getCode();
        String desc    =frm.getDesc();
        String value   =frm.getValue();
        String whereTo =frm.getWhereTo();
        String submitButton = frm.getSubmitButton();

        MiscUtils.getLogger().debug(submitButton);
        if (submitButton.equals("Edit")){
           MiscUtils.getLogger().debug("here with codeid "+codeId);
          BillingCodeData bcd = new BillingCodeData();
          bcd.editBillingCode(code,desc, value,codeId);
        }

        ActionForward retval;
        if(whereTo == null || whereTo.equals("")){
           retval = mapping.findForward("success");
        }else{
           retval = mapping.findForward("private");
        }

        return retval;
    }

}
