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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.util.MiscUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import oscar.entities.WCB;
import oscar.oscarBilling.ca.bc.data.BillingmasterDAO;
import oscar.util.UtilDateUtilities;

/**
 * These Actions Handle of all the interactions with WCB Billing
 *
 * Save   -- this will always save a new form and not update an new one.
 * ================
 * Called from the WCB form.  Could be an update or a save.
 * Possible Forwarding mappings
 * - Billing form ( will need to pass
 * - Just Save
 * - Save and Close.
 * - Print ( this is just an hl7 print right now)
 *
 * @author jaygallagher
 */
public class WCBAction2 extends DispatchAction {

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (request.getSession().getAttribute("user") == null) {
            return (mapping.findForward("Logout"));
        }
        MiscUtils.getLogger().debug("In WCBAction2 Jackson");

        //Get rid of this
        WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
        BillingmasterDAO billingmasterDAO = (BillingmasterDAO) ctx.getBean("BillingmasterDAO");


        WCBForm frm = (WCBForm) form;

        //save new wcb form
        WCB wcb = frm.getWCB();
        wcb.setProvider_no((String) request.getSession().getAttribute("user"));
        if (wcb.getFormCreated() == null) {
            wcb.setFormCreated(new Date());
        }
        if (wcb.getFormEdited() == null) {
            wcb.setFormEdited(new Date());
        }
        wcb.setStatus("W");
        billingmasterDAO.save(wcb);


        if (request.getParameter("saveAndClose") != null) {
            MiscUtils.getLogger().debug("QUITINN");
            request.setAttribute("WCBFormId",wcb.getId());
            return mapping.findForward("saveAndClose");
        }

        if (request.getParameter("saveandbill") != null){
            request.setAttribute("WCBFormId",wcb.getId());
            request.setAttribute("icd9",wcb.getW_icd9());
            request.setAttribute("serviceDate",UtilDateUtilities.DateToString(wcb.getW_servicedate()));
            List list = new ArrayList();
            String feeitem = wcb.getW_feeitem();
            String xfeeitem = wcb.getW_extrafeeitem();

            if (feeitem != null && !feeitem.trim().equals("")){
                list.add(feeitem);
            }
            if (xfeeitem != null && !xfeeitem.trim().equals("")){
                list.add(xfeeitem);
            }
            request.setAttribute("billingcodes", list);


            return mapping.findForward("newbill");
        }


        MiscUtils.getLogger().debug("OVER AND OUT.");
        return (mapping.getInputForward());
    }
}
