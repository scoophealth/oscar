/*
 * Created on Mar 10, 2004
 */
package oscar.oscarBilling.ca.bc.administration;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import oscar.oscarDB.DBHandler;
import oscar.AppointmentMainBean;
/*
 * Copyright (c) 2001-2002. Andromedia. All Rights Reserved. *
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
 * Andromedia, to be provided as
 * part of the OSCAR McMaster
 * EMR System
 *
 * @author Jef King
 * For The Oscar McMaster Project
 * Developed By Andromedia
 * www.andromedia.ca
 */
public class TeleplanCorrectionActionWCB
extends org.apache.struts.action.Action {
   private static final String sql_biling = "update_wcb_billing", //set it to be billed again in billing
   sql_billingmaster = "update_wcb_billingmaster", // set it to be billed again in billingmaster
   sql_demographic = "update_wcb_demographic", //update demographic information
   sql_wcb = "update_wcb_wcb",  //updates wcb form
   CLOSE_RECONCILIATION = "close_reconciliation"; //closes c12 record
   
   public ActionForward perform(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
   throws IOException, ServletException {
      
      String where = "success";
      TeleplanCorrectionFormWCB data = (TeleplanCorrectionFormWCB) form;
      try {
         
         AppointmentMainBean bean = (AppointmentMainBean) request.getSession().getAttribute("apptMainBean");
         
         //bean.queryExecuteUpdate(data.getDemographic(), sql_demographic);
         bean.queryExecuteUpdate(data.getBillingForStatus(), sql_billingmaster);
         bean.queryExecuteUpdate(data.getBilling(), sql_biling);
         bean.queryExecuteUpdate(data.getWcb(this.GetFeeItemAmount(data.getW_feeitem(), data.getW_extrafeeitem())), sql_wcb);
         //bean.queryExecuteUpdate(data.getBillingMaster(),CLOSE_RECONCILIATION); 
         bean.closePstmtConn();
         
         
      }
      catch (Exception ex) {
         System.err.println("WCB Teleplan Correction Query Error: "+ ex.getMessage()+ " - "+ ex.getStackTrace());
      }
      
      String newURL = mapping.findForward(where).getPath();
             newURL = newURL + "?billing_no="+data.getId();
            System.out.println(newURL);
            
      ActionForward actionForward = new ActionForward();
                    actionForward.setPath(newURL);
                    actionForward.setRedirect(true);
      return actionForward;            
   }
   
   private String GetFeeItemAmount(String fee1, String fee2) {
      String billamt = "";
      try {
         double amnt = 0;
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
         java.sql.ResultSet rs;        
         rs = db.GetSQL("SELECT value FROM billingservice WHERE service_code='"+ fee1+ "'");
         if (rs.next()) {
            amnt = rs.getDouble("value");
         }
         rs = db.GetSQL("SELECT value FROM billingservice WHERE service_code='"+ fee2 + "'");
         if (rs.next()) {
            amnt += rs.getDouble("value");
         }
         billamt = String.valueOf(amnt);
         db.CloseConn();
      }
      catch (java.sql.SQLException e) {
         System.err.println(e.getMessage());
      }
      return billamt;
   }
}
