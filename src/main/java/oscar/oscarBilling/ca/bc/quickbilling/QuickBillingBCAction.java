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

package oscar.oscarBilling.ca.bc.quickbilling;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.model.ProviderData;
import org.oscarehr.util.LoggedInInfo;

import oscar.oscarBilling.ca.bc.data.BillingFormData;
import oscar.oscarBilling.ca.bc.data.BillingFormData.BillingVisit;

/**
 * @author Dennis Warren
 * Company Colcamex Resources
 * Date Jun 4, 2012
 * Revised Jun 6, 2012
 * Comment 
 *     	three actions here
 *  	1. get display
 * 		2. add entry to bean
 *   	3. remove entry from bean
 *
 */
public class QuickBillingBCAction extends Action{
	
	private QuickBillingBCFormBean quickBillingBCFormBean;
	private JSONObject billingEntry;
	private QuickBillingBCHandler quickBillingHandler;
	
	public QuickBillingBCAction(){}
	
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
    	
    	String creator = (String) request.getSession().getAttribute("user");
    	
        if (creator == null) {
            return (mapping.findForward("Logout"));
        }
        
        
		quickBillingBCFormBean = (QuickBillingBCFormBean) form;
		quickBillingHandler = new QuickBillingBCHandler(quickBillingBCFormBean);
	   	
    	if(request.getParameter("data") != null) {
    		
    		billingEntry = JSONObject.fromObject(request.getParameter("data"));
    		billingEntry.put("creator", creator);

    		// check if the main header items are set. 
    		// if not then set them otherwise go on to get the other input.
    		if(! this.quickBillingBCFormBean.getIsHeaderSet()) {
    			
				quickBillingHandler.setHeader(billingEntry);
				quickBillingBCFormBean.setIsHeaderSet(true);
    		} 
    		
			// add data to the quick billing session form bean			
			quickBillingHandler.addBill(LoggedInInfo.getLoggedInInfoFromSession(request), this.billingEntry);
				
			return mapping.findForward("success");

		// if request is to remove an entry. 
	    } else if(request.getParameter("remove") != null) {
	    	
	    	if(quickBillingHandler.removeBill(request.getParameter("remove"))) {	    		
	    		
	    		return mapping.findForward("success");
	    		
    		}
	    	
	    // if not adding or removing data then create a fresh form.	
	    } else {

	    	// add some needed form elements to the bean
	    	BillingFormData billingFormData = new BillingFormData();    	
	    	BillingVisit[] billingVisit = billingFormData.getVisitType(QuickBillingBCHandler.BILLING_PROV);

	    	List<ProviderData> activeProviders = quickBillingHandler.getProviderDao().findAll(false);

	    	quickBillingHandler.reset();
	    	quickBillingBCFormBean.setProviderList(activeProviders);
	    	quickBillingBCFormBean.setBillingVisitTypes(billingVisit);				
	    }
    	
    	return mapping.findForward("success");
    }
   
}
