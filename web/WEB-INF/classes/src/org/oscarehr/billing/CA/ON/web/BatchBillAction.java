/*
*
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
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
* This software was written for
* Centre for Research on Inner City Health, St. Michael's Hospital,
* Toronto, Ontario, Canada
*/

package org.oscarehr.billing.CA.ON.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.billing.CA.ON.dao.BillingClaimDAO;
import org.oscarehr.util.MiscUtils;

/**
 *
 * @author rjonasz
 */
public class BatchBillAction extends Action {

    private BillingClaimDAO billClaimDAO;

    public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest request,
                               HttpServletResponse servletResponse) {

        Date billingDate;
        SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = request.getParameter("BillDate");
        if( strDate == null) {
            billingDate = new Date();
        }
        else {
            try {
                billingDate = dateFmt.parse(strDate);
            }
            catch(ParseException e) {
                MiscUtils.getLogger().error("Error", e);
                servletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return null;
            }
        }

        String clinic_view = request.getParameter("clinic_view");
        String curUser = (String)request.getSession().getAttribute("user");
        String[] billingInfo = request.getParameterValues("bill");

        if( billingInfo != null ) {
        
            String[] temp;
            for( int idx = 0; idx < billingInfo.length; ++idx ) {
                temp = billingInfo[idx].split(";");
                this.billClaimDAO.createBill(temp[2], temp[1], temp[0], clinic_view, billingDate, curUser);
            }

        }
        return null;

    }

    /**
     * @return the billClaimDAO
     */
    public BillingClaimDAO getBillClaimDAO() {
        return billClaimDAO;
    }

    /**
     * @param billClaimDAO the billClaimDAO to set
     */
    public void setBillClaimDAO(BillingClaimDAO billClaimDAO) {
        this.billClaimDAO = billClaimDAO;
    }


}
