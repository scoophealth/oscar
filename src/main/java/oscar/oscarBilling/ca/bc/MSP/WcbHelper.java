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


package oscar.oscarBilling.ca.bc.MSP;

import java.sql.ResultSet;
import java.util.ArrayList;

import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;

/**
 *
 * @author Jay Gallagher
 */
public class WcbHelper {
   
   ArrayList empList = null;
   ArrayList claimList = null;
   
   public WcbHelper() {
   }
   
   public WcbHelper(String demographic_no){
      getInfo(demographic_no);
   }
   
   public ArrayList getEmployers(String demographic_no){
      ArrayList employers  = new ArrayList();
      
      try{
      
      String sql = "select distinct w_empname,w_emparea,w_empphone,w_opaddress,w_opcity from wcb where demographic_no = '"+demographic_no+"'";
      ResultSet rs = DBHandler.GetSQL(sql);
      while(rs.next()){         
         WCBEmployer wcbEmp = new WCBEmployer();
            wcbEmp.w_empname   = rs.getString("w_empname");
            wcbEmp.w_emparea   = rs.getString("w_emparea");
            wcbEmp.w_empphone  = rs.getString("w_empphone");
            wcbEmp.w_opaddress = rs.getString("w_opaddress");
            wcbEmp.w_opcity    = rs.getString("w_opcity");         
         employers.add(wcbEmp);                  
      }
      rs.close();                    
      }catch (Exception e){
         MiscUtils.getLogger().error("Error", e);        
      }
      return employers;
   }
   
   
   private void getInfo(String demographic_no){
      empList = new ArrayList();
      claimList = new ArrayList();
      try{
      
      String sql = "select * from wcb where demographic_no = '"+demographic_no+"'";
      ResultSet rs = DBHandler.GetSQL(sql);
      while(rs.next()){
         MiscUtils.getLogger().debug("wcbno "+rs.getString("w_wcbno"));
         WCBClaim wcb = new WCBClaim(rs.getString("w_wcbno"));
         WCBEmployer wcbEmp = new WCBEmployer();
            wcbEmp.w_empname   = rs.getString("w_empname");
            wcbEmp.w_emparea   = rs.getString("w_emparea");
            wcbEmp.w_empphone  = rs.getString("w_empphone");
            wcbEmp.w_opaddress = rs.getString("w_opaddress");
            wcbEmp.w_opcity    = rs.getString("w_opcity");
         wcb.wcbEmp = wcbEmp;
         empList.add(wcbEmp);
         
         claimList.add(wcb);
      }
      rs.close();                    
      }catch (Exception e){
         MiscUtils.getLogger().error("Error", e);        
      }
      
   }
   
   public ArrayList getClaimInfo(String demographic_no){      
      ArrayList claimList = new ArrayList();
      try{
      
      String sql = "select distinct w_empname,w_emparea,w_empphone,w_opaddress,w_opcity,w_wcbno,w_icd9,w_bp,w_side,w_noi,w_doi from wcb where demographic_no = '"+demographic_no+"'";



      ResultSet rs = DBHandler.GetSQL(sql);
      while(rs.next()){
         
         WCBClaim wcb = new WCBClaim(rs.getString("w_wcbno"));
         wcb.w_icd9 = rs.getString("w_icd9");
         wcb.w_bp   = rs.getString("w_bp");
         wcb.w_side = rs.getString("w_side");
         wcb.w_noi  = rs.getString("w_noi");
         wcb.w_doi  = rs.getString("w_doi");

         WCBEmployer wcbEmp = new WCBEmployer();
            wcbEmp.w_empname   = rs.getString("w_empname");
            wcbEmp.w_emparea   = rs.getString("w_emparea");
            wcbEmp.w_empphone  = rs.getString("w_empphone");
            wcbEmp.w_opaddress = rs.getString("w_opaddress");
            wcbEmp.w_opcity    = rs.getString("w_opcity");
         wcb.wcbEmp = wcbEmp;                  
         claimList.add(wcb);
      }
      rs.close();                    
      }catch (Exception e){
         MiscUtils.getLogger().error("Error", e);        
      }
      return claimList;
   }
   
   /**
    * Getter for property empList.
    * @return Value of property empList.
    */
   public java.util.ArrayList getEmpList() {
      return empList;
   }
   
   /**
    * Setter for property empList.
    * @param empList New value of property empList.
    */
   public void setEmpList(java.util.ArrayList empList) {
      this.empList = empList;
   }
   
   /**
    * Getter for property claimList.
    * @return Value of property claimList.
    */
   public java.util.ArrayList getClaimList() {
      return claimList;
   }
   
   /**
    * Setter for property claimList.
    * @param claimList New value of property claimList.
    */
   public void setClaimList(java.util.ArrayList claimList) {
      this.claimList = claimList;
   }
   
   public class WCBClaim{
      public String w_wcbNo = "";
      
      public String w_icd9 = "";
      public String w_bp= "";
      public String w_side= "";
      public String w_noi= "";
      public String w_doi= "";
      public WCBEmployer wcbEmp = null;
      public WCBClaim(String clm){
         w_wcbNo = clm;
      }
      
      public String getClaimNumber(){
         return w_wcbNo;
      }
      
      
   }
   
   
   
   public class WCBEmployer{
      public String w_empname;
      public String w_emparea;
      public String w_empphone;
      public String w_opaddress;
      public String w_opcity;
   }
/*   
demographic_no

w_empname
w_emparea
w_empphone
w_wcbno
w_opaddress
w_opcity

w_rphysician
w_duration
w_problem
w_servicedate
w_diagnosis
w_icd9
w_bp
w_side
w_noi
w_work
w_workdate
w_clinicinfo
w_capability
w_capreason
w_estimate
w_rehab
w_rehabtype
w_wcbadvisor
w_ftreatment
w_estimatedate
w_tofollow
w_payeeno
w_pracno
w_doi
status
w_feeitem
w_extrafeeitem
w_servicelocation
ID
formNeeded
*/
   
}
