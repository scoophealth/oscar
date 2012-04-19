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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.util.MiscUtils;

import oscar.AppointmentMainBean;
import oscar.oscarBilling.ca.bc.MSP.MSPReconcile;
import oscar.oscarBilling.ca.bc.data.BillingHistoryDAO;
import oscar.oscarDB.DBHandler;
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

    static Logger log=MiscUtils.getLogger();
    private static final String sql_biling = "update_wcb_billing", //set it to be billed again in billing
             sql_wcb = "update_wcb_wcb", //updates wcb form
             provider_wcb = "update_provider_wcb",  CLOSE_RECONCILIATION = "close_reconciliation"; //closes c12 record

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

            log.debug("sql_biling " + sql_biling + " ");
            bean.queryExecuteUpdate(data.getBillingForStatus(), sql_biling);
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
        try {

        	DBHandler.RunSQL("update billingmaster set billing_unit = '" + i + "' WHERE billing_no ='" + billingno + "'");
        } catch (java.sql.SQLException e) {
            log.error("", e);
        }
    }

    private String GetFeeItemAmount(String fee1, String fee2) {
        String billamt = "0.00";

        try {

            java.sql.ResultSet rs;
            rs = DBHandler.GetSQL("SELECT value FROM billingservice WHERE service_code='" +
                    fee1 + "'");
            if (rs.next()) {
                billamt = rs.getString("value");
            }
        } catch (java.sql.SQLException e) {
            log.error("", e);
        }
        return billamt;
    }
}
