package oscar.oscarBilling.MSP;


/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
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
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */ 


import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Properties;

import oscar.oscarDB.DBHandler;

public class MSPReconcile{    
    public static String REJECTED           = "R"; 
    public static String NOTSUBMITTED       = "O";    
    public static String SUBMITTED          = "B";
    public static String SETTLED            = "S";
    public static String DELETED            = "D";
    public static String HELD               = "Z";
    public static String DATACENTERCHANGED  = "C";
    public static String PAIDWITHEXP        = "E";
    public static String REFUSED            = "F";
    public static String BADDEBT            = "X";
    public static String WCB                = "W";
    public static String CAPITATED          = "H";
    public static String DONOTBILL          = "N";
    public static String BILLPATIENT        = "P";
            
    public Properties currentC12Records(){
        Properties p = new Properties();
        try {            
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "select t_officefolioclaimno, t_exp1,t_exp2,t_exp3,t_exp4,t_exp5,t_exp6,t_exp7  from teleplanC12 where status != 'E'";
            ResultSet rs = db.GetSQL(sql);
            while(rs.next()){
                    try{
                      int i  = Integer.parseInt(rs.getString("t_officefolioclaimno"));  // this kludge rids leading zeros   
                      String exp[] = new String[7];  
                          exp[0] = rs.getString("t_exp1");
                          exp[1] = rs.getString("t_exp2");
                          exp[2] = rs.getString("t_exp3");
                          exp[3] = rs.getString("t_exp4");
                          exp[4] = rs.getString("t_exp5");
                          exp[5] = rs.getString("t_exp6");
                          exp[6] = rs.getString("t_exp7");
                      String def = createCorrectionsString(exp);
                      String s = Integer.toString(i);
                      p.put(s,def);
                    }catch(NumberFormatException intEx){
                        System.out.println("Had trouble Parsing int from "+rs.getString("t_officeno"));
                    }
            }            
            rs.close();
        }catch(Exception e){
            e.printStackTrace();
        }        
        return p;
    }
            
    //
    public String getS00String(String billingNo){
        String s = "";
         int i = 0;
        try {                       
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "select t_exp1,t_exp2,t_exp3,t_exp4,t_exp5,t_exp6,t_exp7 teleplanS00 where t_mspctlno = '"+forwardZero(billingNo,7)+"'";
            ResultSet rs = db.GetSQL(sql);
            while(rs.next()){             
                      String exp[] = new String[7];  
                          exp[0] = rs.getString("t_exp1");
                          exp[1] = rs.getString("t_exp2");
                          exp[2] = rs.getString("t_exp3");
                          exp[3] = rs.getString("t_exp4");
                          exp[4] = rs.getString("t_exp5");
                          exp[5] = rs.getString("t_exp6");
                          exp[6] = rs.getString("t_exp7");
                      s = createCorrectionsString(exp);
                      i++;
            }            
            rs.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        if ( i > 1 ){  System.out.println(" billingNo "+billingNo +" had " + i + "rows in the table"); }
        return s;
    }
     
    private String createCorrectionsString(String[] exp){
        String retval = "";
        for (int i = 0 ; i < exp.length ; i++){
           if (exp[i].length() != 0){
               retval += exp[i]+" ";
           }
        }
        return retval;
    }    
                        
    public class BillSearch {
            Properties p ;
            public ArrayList list;
            int count = 0;
            ArrayList justBillingMaster ;
            
            public Properties getCurrentErrorMessages(){
                Properties errorsProps = new Properties();
                if (count > 0) {                
                   try {            
                      DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);                                        
                      String sql = "select distinct t_officeno, t_exp1,t_exp2,t_exp3,t_exp4,t_exp5,t_exp6,t_exp7 from teleplanS00 where t_officeno in (";
                    
                      for (int i = 0; i < justBillingMaster.size() ; i++){
                          
                         sql += "'"+forwardZero( (String) justBillingMaster.get(i),7)+"'";
                         if ( i < ( justBillingMaster.size() - 1)){
                            sql += ",";
                         }
                      }
                      sql += ")";
                      
                      ResultSet rs = db.GetSQL(sql);
                      while(rs.next()){
                         try{
                            int i  = Integer.parseInt(rs.getString("t_officeno"));  // this kludge rids leading zeros   
                            String exp[] = new String[7];  
                             exp[0] = rs.getString("t_exp1");
                             exp[1] = rs.getString("t_exp2");
                             exp[2] = rs.getString("t_exp3");
                             exp[3] = rs.getString("t_exp4");
                             exp[4] = rs.getString("t_exp5");
                             exp[5] = rs.getString("t_exp6");
                             exp[6] = rs.getString("t_exp7");
                            String def = createCorrectionsString(exp);
                            String s = Integer.toString(i);
                            errorsProps.put(s,def);
                         }catch(NumberFormatException intEx){
                            System.out.println("Had trouble Parsing int from "+rs.getString("t_mspctlno"));
                         }
                      }            
                      rs.close();                   }catch(Exception e){
                      e.printStackTrace();
                   }
                }   
            return errorsProps;
            }           
    }
    
    public ArrayList getSequenceNumbers(String billingNo){
        ArrayList retval = new ArrayList();
        try {            
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs = db.GetSQL("select t_dataseq from teleplanC12 where t_officefolioclaimno = '"+forwardZero(billingNo,7)+"'");
            while(rs.next()){   
              //String exp[] = new String[7];  
              retval.add(rs.getString("t_dataseq"));                                     
            }
            rs = db.GetSQL("select t_dataseq from teleplanS00 where t_officeno = '"+forwardZero(billingNo,7)+"'");
            while(rs.next()){   
              retval.add(rs.getString("t_dataseq"));                                     
            }
            rs.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return retval;
    }
    
    public BillSearch getBills(String statusType, String providerNo, String startDate , String endDate){
        
        
        BillSearch billSearch = new BillSearch();
        
        String providerQuery = "";
        String startDateQuery = "";
        String endDateQuery = "";
        
        if (providerNo != null && !providerNo.trim().equalsIgnoreCase("all")){
            providerQuery = " and provider_no = '"+providerNo+"'" ; 
        }
        
        if (startDate != null && !startDate.trim().equalsIgnoreCase("")){
            startDateQuery = " and ( to_days(service_date) > to_days('"+startDate+"')) ";
        }
        
        if (endDate != null && !endDate.trim().equalsIgnoreCase("")){
            endDateQuery = " and ( to_days(service_date) < to_days('"+endDate+"')) ";
        }
        
        String p =" select b.billing_no, b.demographic_no, b.demographic_name, b.update_date, "
                 +" b.status, b.apptProvider_no,b.appointment_no, b.billing_date,b.billing_time, bm.billingstatus, "
                 +" bm.bill_amount, bm.billing_code, bm.dx_code1, bm.dx_code2, bm.dx_code3,"
                 +" b.provider_no, b.visitdate, b.visittype,bm.billingmaster_no from billing b, "
                 +" billingmaster bm where b.billing_no= bm.billing_no and bm.billingstatus = '"+statusType+"' "
                 +  providerQuery
                 +  startDateQuery
                 +  endDateQuery;
        
        
        //String 
        billSearch.list = new ArrayList();
        billSearch.count = 0;
        billSearch.justBillingMaster = new ArrayList();
        
        try {            
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet  rs = db.GetSQL(p);
            while(rs.next()){
            Bill b = new Bill();
              b.billing_no = rs.getString("billing_no");
              b.apptDoctorNo = rs.getString("apptProvider_no");
              b.apptNo = rs.getString("appointment_no");
              b.demoNo = rs.getString("demographic_no");
              b.demoName = rs.getString("demographic_name");
              b.userno=rs.getString("provider_no");
              b.apptDate = rs.getString("billing_date");
              b.apptTime = rs.getString("billing_time");
              b.reason = rs.getString("billingstatus");
              b.billMasterNo = rs.getString("billingmaster_no");
              
              b.amount = rs.getString("bill_amount");
              b.code   = rs.getString("billing_code");
              b.dx1    = rs.getString("dx_code1");
              b.dx2    = rs.getString("dx_code2");
              b.dx3    = rs.getString("dx_code3");
              
            billSearch.justBillingMaster.add(b.billMasterNo);
            billSearch.list.add(b);
            billSearch.count++;
            }
            rs.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return billSearch;
    }
    
    public ArrayList getBillsMaster(String statusType){        
        String p =" select b.billing_no, b.demographic_no, b.demographic_name, b.update_date, "
                 +" b.status, b.apptProvider_no,b.appointment_no, b.billing_date,b.billing_time, bm.billingstatus, "
                 +" bm.bill_amount, bm.billing_code, bm.dx_code1, bm.dx_code2, bm.dx_code3,"
                 +" b.provider_no, b.visitdate, b.visittype,bm.billingmaster_no from billing b, "
                 +" billingmaster bm where b.billing_no= bm.billing_no and bm.billingstatus = '"+statusType+"' ";
  
        ArrayList list = new ArrayList();
        try {            
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet  rs = db.GetSQL(p);
            while(rs.next()){
            Bill b = new Bill();
              b.billing_no = rs.getString("billing_no");
              b.apptDoctorNo = rs.getString("apptProvider_no");
              b.apptNo = rs.getString("appointment_no");
              b.demoNo = rs.getString("demographic_no");
              b.demoName = rs.getString("demographic_name");
              b.userno=rs.getString("provider_no");
              b.apptDate = rs.getString("billing_date");
              b.apptTime = rs.getString("billing_time");
              b.reason = rs.getString("billingstatus");
              b.billMasterNo = rs.getString("billingmaster_no");
              
              b.amount = rs.getString("bill_amount");
              b.code   = rs.getString("billing_code");
              b.dx1    = rs.getString("dx_code1");
              b.dx2    = rs.getString("dx_code2");
              b.dx3    = rs.getString("dx_code3");
              list.add(b);
            }
            rs.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }    
        
    public String getApptStyle(String s,String userno){
       String retval = "";
        if (s.equals("none")){
          retval = "No Appt / INR";
         }else{
            if (s.equals(userno)) {
                retval = "With Appt. Doctor";
            }else{
                retval = "Unmatched Appt. Doctor";
            }
         }
        
    return  retval;
    }

    public class Bill {
      public String billing_no = "";
      public String apptDoctorNo = "";
      public String apptNo = "";
      public String demoNo = "";
      public String demoName = "";
      public String userno="";
      public String apptDate = "";
      public String apptTime = "";
      public String reason = "";
      public String billMasterNo = "";
      
      public String code = "";
      public String amount = "";
      public String dx1 = "";
      public String dx2 = "";
      public String dx3 = "";
    }
    
    public ArrayList getAllC12Records(String billingNo){
        ArrayList retval = new ArrayList();
        Properties p = new Properties();
        try {
            p.load(new FileInputStream("/home/jay/documents/PEMP/mspEditCodes.properties"));
        } catch (IOException e) {
        }        
        try {            
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs = db.GetSQL("select distinct t_dataseq, t_exp1,t_exp2,t_exp3,t_exp4,t_exp5,t_exp6,t_exp7 from teleplanC12 where t_officefolioclaimno = '"+forwardZero(billingNo,7)+"'");
            while(rs.next()){   
              String exp[] = new String[7];  
              String seq = rs.getString("t_dataseq");
              exp[0] = rs.getString("t_exp1");
              exp[1] = rs.getString("t_exp2");
              exp[2] = rs.getString("t_exp3");
              exp[3] = rs.getString("t_exp4");
              exp[4] = rs.getString("t_exp5");
              exp[5] = rs.getString("t_exp6");
              exp[6] = rs.getString("t_exp7");
              for (int i = 0 ; i < exp.length ; i++){
                   if (exp[i].length() != 0){
                       retval.add(seq+"&nbsp;&nbsp;"+exp[i]+"&nbsp;&nbsp;"+p.getProperty(exp[i],""));
                   }
              }
            }
            rs.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return retval;        
    }
     
    public ArrayList getAllS00Records(String billingNo){
        ArrayList retval = new ArrayList();
        Properties p = new Properties();
        try {
            p.load(new FileInputStream("/home/jay/documents/PEMP/mspEditCodes.properties"));
        } catch (IOException e) { e.printStackTrace(); }        
        try {            
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs = db.GetSQL("select distinct t_dataseq, t_exp1,t_exp2,t_exp3,t_exp4,t_exp5,t_exp6,t_exp7 from teleplanS00 where t_officeno = '"+forwardZero(billingNo,7)+"'");
            while(rs.next()){   
              String exp[] = new String[7];  
              String seq = rs.getString("t_dataseq");
              exp[0] = rs.getString("t_exp1");
              exp[1] = rs.getString("t_exp2");
              exp[2] = rs.getString("t_exp3");
              exp[3] = rs.getString("t_exp4");
              exp[4] = rs.getString("t_exp5");
              exp[5] = rs.getString("t_exp6");
              exp[6] = rs.getString("t_exp7");
              for (int i = 0 ; i < exp.length ; i++){
                   if (exp[i].length() != 0){
                       retval.add(seq+"&nbsp;&nbsp;"+exp[i]+"&nbsp;&nbsp;"+p.getProperty(exp[i],""));
                   }
              }
            }
            rs.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return retval;        
    }
        
    public Properties getBillingMasterRecord(String billingNo){
        Properties p = null;
        String name = null;
        String value = null;
        boolean foundBill = false;
        try {            
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs = db.GetSQL("select * from billingmaster where billingmaster_no = '"+billingNo+"'");
            if(rs.next()){   
                p = new Properties();                
                ResultSetMetaData md = rs.getMetaData();                
                for(int i = 1; i <= md.getColumnCount(); i++)  {
                    name = md.getColumnName(i);                    
                    value = rs.getString(i);
                    if (value == null){ value = new String();}
                    p.setProperty(name, value);
                }	                
            }
            rs.close();
        }catch(Exception e){
            System.out.println("name: "+name+" value: "+value);
            e.printStackTrace();
        }        
        return p;
    }    
    
    public boolean updateStat(String stat,String billingNo){
        //get current status of bill
        boolean updated = true;
        String currStat ="";
        String newStat = "";
        try {            
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs = db.GetSQL("select billingstatus from billingmaster where billingmaster_no = '"+billingNo+"'");
            if(rs.next()){            
               currStat = rs.getString("billingstatus");
            }		
            rs.close();
        }catch(Exception e){
            e.printStackTrace();
        }                         
        if (!currStat.equals(SETTLED)){
            if (stat.equals(REJECTED)){
                newStat = REJECTED;
            }else if(stat.equals(NOTSUBMITTED)){
                newStat = NOTSUBMITTED;
            }else if(stat.equals(SUBMITTED)){
                newStat = SUBMITTED;
            }else if(stat.equals(SETTLED)){
                newStat = SETTLED;
            }else if(stat.equals(DELETED)){
                newStat = DELETED;
            }else if(stat.equals(HELD)){
                newStat = HELD;
            }else if(stat.equals(DATACENTERCHANGED)){
                newStat = DATACENTERCHANGED;
            }else if(stat.equals(PAIDWITHEXP)){
                newStat = PAIDWITHEXP;
            }else if(stat.equals(BADDEBT)){
                newStat = BADDEBT;
            }else if(stat.equals(WCB)){
                newStat = WCB;
            }else if(stat.equals(CAPITATED)){
                newStat = CAPITATED;
            }else if(stat.equals(DONOTBILL)){
                newStat = DONOTBILL;
            }else if(stat.equals(BILLPATIENT)){
                newStat = BILLPATIENT;
            }
        }else{
         updated = false; 
         System.out.println("billing No "+billingNo+" is settled, will not be updated");
        }            
        if (updated){
            try {              
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                System.out.println("Updating billing no "+billingNo+" to "+newStat);
                db.RunSQL("update billingmaster set billingstatus = '"+newStat+"' where billingmaster_no = '"+billingNo+"'");
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return updated;
    }

    public String forwardZero(String y, int x) {
        String returnZeroValue = new String();
        for(int i=y.length(); i < x; i++) {
            returnZeroValue += "0";
        }
        return returnZeroValue+y;
    }
    
    
    class BillRecord{
        String billingmaster_no   ;
        String billing_no              ;
        String createdate              ;
        String billingstatus           ;
        String demographic_no          ;
        String appointment_no          ;
        String claimcode               ;
        String datacenter              ;
        String payee_no                ;
        String practitioner_no         ;
        String phn                     ;
        String name_verify             ;
        String dependent_num           ;
        String billing_unit            ;
        String clarification_code      ;
        String anatomical_area         ;
        String after_hour              ;
        String new_program             ;
        String billing_code            ;
        String bill_amount             ;
        String payment_mode            ;
        String service_date            ;
        String service_to_day          ;
        String submission_code         ;
        String extended_submission_code;
        String dx_code1                ;
        String dx_code2                ;
        String dx_code3                ;
        String dx_expansion            ;
        String service_location        ;
        String referral_flag1          ;
        String referral_no1            ;
        String referral_flag2          ;
        String referral_no2            ;
        String time_call               ;
        String service_start_time      ;
        String service_end_time        ;
        String birth_date              ;
        String office_number           ;
        String correspondence_code     ;
        String claim_comment           ;
        String mva_claim_code          ;
        String icbc_claim_no           ;
        String original_claim          ;
        String facility_no             ;
        String facility_sub_no         ;
        String filler_claim            ;
        String oin_insurer_code        ;
        String oin_registration_no     ;
        String oin_birthdate           ;
        String oin_first_name          ;
        String oin_second_name         ;
        String oin_surname             ;
        String oin_sex_code            ;
        String oin_address             ;
        String oin_address2            ;
        String oin_address3            ;
        String oin_address4            ;
        String oin_postalcode  ;        
    }
    
}
    