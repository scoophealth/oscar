/**
 * Copyright (c) 2001-2002. Andromedia. All Rights Reserved.
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
 * This software was written for
 * Andromedia, to be provided as
 * part of the OSCAR McMaster
 * EMR System
 */


package oscar.oscarBilling.ca.bc.administration;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.dao.BillingDao;
import org.oscarehr.common.dao.BillingServiceDao;
import org.oscarehr.common.model.Billing;
import org.oscarehr.common.model.BillingService;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.AppointmentMainBean;
import oscar.oscarBilling.ca.bc.MSP.MSPReconcile;
import oscar.oscarBilling.ca.bc.data.BillingHistoryDAO;
import oscar.oscarBilling.ca.bc.data.BillingmasterDAO;
import oscar.oscarProvider.data.ProviderData;
import oscar.util.StringUtils;

/*
 * @author Jef King
 * For The Oscar McMaster Project
 * Developed By Andromedia
 * www.andromedia.ca
 */
/*
 * Created on Mar 10, 2004
 */

public class TeleplanCorrectionActionWCB
        extends org.apache.struts.action.Action {

   private static final String sql_wcb = "update_wcb_wcb"; //updates wcb form
	private static final String provider_wcb = "update_provider_wcb";
	
	static Logger log=MiscUtils.getLogger();
	
	private BillingDao billingDao = SpringUtils.getBean(BillingDao.class);
	

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {

        String where = "success";

        TeleplanCorrectionFormWCB data = (TeleplanCorrectionFormWCB) form;

        try {

            AppointmentMainBean bean = (AppointmentMainBean) request.getSession().getAttribute("apptMainBean");
            MSPReconcile msp = new MSPReconcile();
            String status = data.getStatus();

            log.debug("adj amount " + data.getAdjAmount());
            if (request.getParameter("settle") != null && request.getParameter("settle").equals("Settle Bill")) {
                status = "S";
            }

            if (!StringUtils.isNullOrEmpty(status)) {
                status = MSPReconcile.NOTSUBMITTED.equals(data.getStatus()) ? MSPReconcile.WCB : status;
                msp.updateBillingStatusWCB(data.getBillingNo(), status, data.getId());
            }
            BillingHistoryDAO dao = new BillingHistoryDAO();
            //If the adjustment amount field isn't empty, create an archive of the adjustment
            if (data.getAdjAmount() != null && !"".equals(data.getAdjAmount())) {
                double dblAdj = Math.abs(new Double(data.getAdjAmount()).doubleValue());
                //if 1 this adjustment is a debit
                if ("1".equals(data.getAdjType())) {
                    dblAdj = dblAdj * -1.0;
                }
                dao.createBillingHistoryArchive(data.getId(), dblAdj, MSPReconcile.PAYTYPE_IA);
                msp.settleIfBalanced(data.getId());
            } else {
                /**
                 * Ensure that an audit of the currently modified bill is captured
                 */
                dao.createBillingHistoryArchive(data.getId());
            }
            updateUnitValue(data.getBillingUnit(), data.getBillingNo());

           
            Billing billing = billingDao.find(Integer.parseInt(data.getBillingNo()));
            if(billing != null) {
            	billing.setStatus(data.getStatus());
            	billingDao.merge(billing);
            }
           
            String feeItem = data.getW_feeitem();
            String extraFeeItem = data.getW_extrafeeitem();
            String getItemAmt = this.GetFeeItemAmount(feeItem, extraFeeItem);
            log.debug("fee " + feeItem + " extra " + extraFeeItem + " item amt " + getItemAmt);
            String[] wcbParams = data.getWcb(getItemAmt);
            
            bean.queryExecuteUpdate(wcbParams, sql_wcb);

            String providerNo = data.getProviderNo();

            ProviderData pd = new ProviderData(providerNo);
            String payee = pd.getBilling_no();
            String pracno = pd.getOhip_no();
            String billingNo = data.getBillingNo();

            String s[] = {providerNo, payee, pracno, billingNo};

            bean.queryExecuteUpdate(s, provider_wcb);

        } catch (Exception ex) {
            log.error("WCB Teleplan Correction Query Error: " +ex.getMessage() + " - ", ex);
        }

        String newURL = mapping.findForward(where).getPath();
        newURL = newURL + "?billing_no=" + data.getId();
        MiscUtils.getLogger().debug(newURL);

        ActionForward actionForward = new ActionForward();
        actionForward.setPath(newURL);
        actionForward.setRedirect(true);
        return actionForward;
    }
    private void updateUnitValue(String i, String billingno) {
    	BillingmasterDAO dao = (BillingmasterDAO) SpringUtils.getBean("BillingmasterDAO");
    	dao.updateBillingUnitForBillingNumber(i, Integer.parseInt(billingno));
    }

    private String GetFeeItemAmount(String fee1, String fee2) {
        BillingServiceDao dao = SpringUtils.getBean(BillingServiceDao.class);
        List<BillingService> services = dao.findByServiceCode(fee1);
        for(BillingService service : services)
        	return service.getValue(); 
        return "0.00";
    }
}
