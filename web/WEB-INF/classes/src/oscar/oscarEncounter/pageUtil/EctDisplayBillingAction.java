// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
// * This software is published under the GPL GNU General Public License. 
// * This program is free software; you can redistribute it and/or 
// * modify it under the terms of the GNU General Public License 
// * as published by the Free Software Foundation; either version 2 
// * of the License, or (at your option) any later version. * 
// * This program is distributed in the hope that it will be useful, 
// * but WITHOUT ANY WARRANTY; without even the implied warranty of 
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
// * along with this program; if not, write to the Free Software 
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
// * 
// * <OSCAR TEAM>
// * This software was written for the 
// * Department of Family Medicine 
// * McMaster Unviersity 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------

package oscar.oscarEncounter.pageUtil;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.util.MessageResources;

import oscar.OscarProperties;
import oscar.oscarBilling.ca.bc.MSP.MSPReconcile;
import oscar.oscarBilling.ca.on.data.BillingClaimHeader1Data;

//import oscar.oscarSecurity.CookieSecurity;
import oscar.oscarBilling.ca.on.data.BillingItemData;
import oscar.oscarBilling.ca.on.data.JdbcBillingReviewImpl;

public class EctDisplayBillingAction extends EctDisplayAction {
    
    private static final String cmd = "Billing";
  
    public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) {                                              
     
        
            //set text for lefthand module title
            Dao.setLeftHeading("Billing History");
        
            String billRegion = OscarProperties.getInstance().getProperty("billregion","ON");
            
            if (billRegion.equals("ON")){
            
                //set link for lefthand module title
                String winName = "ViewBillingHistory" + bean.demographicNo;  //&last_name=TEST&first_name=PATIENT&orderby=appointment_date&displaymode=appt_history&dboperation=appt_history&limit1=0&limit2=10

                String url = "popupPage(600,900,'" + winName + "','" + request.getContextPath() + "/billing/CA/ON/billinghistory.jsp?demographic_no=" + bean.demographicNo + "&last_name=" + bean.patientLastName + "&first_name=" + bean.patientFirstName + "')";
                Dao.setLeftURL(url);

                //set the right hand heading link
                winName = "NewBilling" + bean.demographicNo;
                url = "popupPage(700,960,'" + winName + "','"+ request.getContextPath() + "/billing/CA/ON/billinghistory.jsp?demographic_no=" + bean.demographicNo + "&last_name=" + bean.patientLastName + "&first_name=" + bean.patientFirstName +"'); return false;";
                Dao.setRightURL(url);
                Dao.setRightHeadingID(cmd);  //no menu so set div id to unique id for this action

                ////
                JdbcBillingReviewImpl dbObj = new JdbcBillingReviewImpl();
                List aL = null;
                try{ 
                     aL   = dbObj.getBillingHist(bean.demographicNo, 10, 0, null);
                }catch(Exception e){

                    e.printStackTrace();
                }
                int nItems=0;
                for(int i=0; i<aL.size(); i=i+2) {

                        Date date = null;
                        nItems++;
                        BillingClaimHeader1Data obj = (BillingClaimHeader1Data) aL.get(i);
                        BillingItemData itObj = (BillingItemData) aL.get(i+1);
                        String strBillType = obj.getPay_program();

                        NavBarDisplayDAO.Item item = Dao.Item();

                        String dbFormat = "yyyy-MM-dd";
                        try {                 
                            DateFormat formatter = new SimpleDateFormat(dbFormat);                                        
                            date = (Date)formatter.parse(obj.getBilling_date());
                        }
                        catch(ParseException e ) {
                                System.out.println("EctDisplayMsgAction: Error creating date " + e.getMessage());
                                date = null;
                        }                

                        item.setDate(date);
                        int hash = winName.hashCode();
                        hash = hash < 0 ? hash * -1 : hash;
                        url = "popupPage(600,900,'" + hash + "','" + request.getContextPath() + "/billing/CA/ON/billinghistory.jsp?demographic_no=" + bean.demographicNo + "&last_name=" + bean.patientLastName + "&first_name=" + bean.patientFirstName+ "'); return false;";
                        item.setURL(url);                
                        item.setTitle(itObj.getService_code()+" ("+itObj.getDx()+")");
                        item.setLinkTitle(itObj.getService_code() + " (" +itObj.getDx()+") - " + obj.getBilling_date());
                        Dao.addItem(item);    
                }

            }else{
                //billStatus.jsp?lastName=A22BLE&firstName=ALEX&filterPatient=true&demographicNo=22
                
                //set link for lefthand module title
                String winName = "ViewBillingHistory" + bean.demographicNo;  //&last_name=TEST&first_name=PATIENT&orderby=appointment_date&displaymode=appt_history&dboperation=appt_history&limit1=0&limit2=10

                String url = "popupPage(600,900,'" + winName + "','" + request.getContextPath() + "/billing/CA/BC/billStatus.jsp?filterPatient=true&demographicNo=" + bean.demographicNo + "&lastName=" + bean.patientLastName + "&firstName=" + bean.patientFirstName + "')";
                Dao.setLeftURL(url);

                //set the right hand heading link
                winName = "NewBilling" + bean.demographicNo;
                url = "popupPage(700,960,'" + winName + "','"+ request.getContextPath() + "/billing/CA/BC/billStatus.jsp?filterPatient=true&demographicNo=" + bean.demographicNo + "&lastName=" + bean.patientLastName + "&firstName=" + bean.patientFirstName +"'); return false;";
                Dao.setRightURL(url);
                Dao.setRightHeadingID(cmd);  //no menu so set div id to unique id for this action

                ////
                MSPReconcile msp = new MSPReconcile();              //"ALL", "1999-01-01" ,"9999-99-99"
                MSPReconcile.BillSearch bSearch = msp.getBills("%", null, null ,null,bean.demographicNo);//,true,true,true,true);
                ArrayList list = bSearch.list;
                
                System.out.println("list size for bills is "+list.size());
                
//                JdbcBillingReviewImpl dbObj = new JdbcBillingReviewImpl();
//                List aL = null;
//                try{ 
//                     aL   = dbObj.getBillingHist(bean.demographicNo, 10, 0, null);
//                }catch(Exception e){
//
//                    e.printStackTrace();
//                }
                int nItems=0;
                for(int i=0; i<list.size(); i++) {

                        Date date = null;
                        nItems++;
                        
                        MSPReconcile.Bill b = (MSPReconcile.Bill) list.get(i);
                        
                        String strBillType = b.getBillingtype();
                        
                        if (b != null && !b.reason.equals("D")){
                            NavBarDisplayDAO.Item item = Dao.Item();

                            String dbFormat = "yyyy-MM-dd";
                            try {                 
                                DateFormat formatter = new SimpleDateFormat(dbFormat);                                        
                                date = (Date)formatter.parse(b.getApptDate());
                            }
                            catch(ParseException e ) {
                                    System.out.println("EctDisplayMsgAction: Error creating date " + e.getMessage());
                                    date = null;
                            }                

                            item.setDate(date);
                            int hash = winName.hashCode();
                            hash = hash < 0 ? hash * -1 : hash;
                            url = "popupPage(600,900,'" + hash + "','" + request.getContextPath() + "/billing/CA/BC/billStatus.jsp?filterPatient=true&demographicNo=" + bean.demographicNo + "&lastName=" + bean.patientLastName + "&firstName=" + bean.patientFirstName+ "'); return false;";
                            item.setURL(url);                
                            item.setTitle(b.reason+"# "+b.getCode()+" ("+b.getDx1()+")");
                            item.setLinkTitle(msp.getStatusDesc(b.reason)+"# " +b.getCode() + " (" +b.getDx1()+") - " + b.getApptDate());
                            Dao.addItem(item);    
                        }
                }

            }
            
           return true;
  }
    
     public String getCmd() {
         return cmd;
     }
}
