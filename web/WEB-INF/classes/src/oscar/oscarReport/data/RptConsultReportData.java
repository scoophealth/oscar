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
package oscar.oscarReport.data;

import java.sql.ResultSet;
import java.util.ArrayList;

import oscar.oscarDB.DBHandler;
/**
*This classes main function ConsultReportGenerate collects a group of patients with consults in the last specified date
*/
public class RptConsultReportData {

    public ArrayList demoList = null;
    public String days = null;

    public RptConsultReportData() {
    }

    public ArrayList providerList(){
        ArrayList arrayList = new ArrayList();
        try{

              DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
              ResultSet rs;
              String sql = "select provider_no, last_name, first_name from provider where provider_type = 'doctor' order by last_name";
              rs = db.GetSQL(sql);
              while (rs.next()) {
                 ArrayList a = new ArrayList (); 
                 a.add( db.getString(rs,"provider_no") );
                 a.add( db.getString(rs,"last_name") +", "+ db.getString(rs,"first_name") );
                 arrayList.add(a);
              }
              rs.close();
        }catch (java.sql.SQLException e){ System.out.println("Problems");   System.out.println(e.getMessage());  e.printStackTrace();}
    return arrayList;
    }

    public void consultReportGenerate( String providerNo, String days ){
       this.days = days;
       try{
              
              DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
              ResultSet rs;
              // mysql function for dates = select date_sub(now(),interval 1 month); 

              /* We need select the correct datbase syntax */
              /* look at oscar_mcmaster.properties to choose database */
              String db_type = oscar.OscarProperties.getInstance().getProperty("db_type").trim();
              String sql = "select distinct last_name, c.demographicNo from consultationRequests c , demographic d where referalDate >= ";

              if (db_type.equalsIgnoreCase("mysql"))
                  sql = sql + " (CURRENT_DATE - interval " + days + " month) ";
              if (db_type.equalsIgnoreCase("postgresql")){
                  sql = sql + " (CURRENT_DATE - interval '" + days + " month') ";
	      }
              sql = sql + " and c.demographicNo = d.demographic_no ";
              if (!providerNo.equals("-1")){
                 sql = sql +" and d.provider_no = '"+providerNo+"' "; 
              }
              sql = sql + "  order by d.last_name ";



              //String sql = " Select distinct demographicNo from consultationRequests where to_days(now()) - to_days(referalDate) <= 30 ";
              rs = db.GetSQL(sql);
              demoList = new ArrayList();
              DemoConsultDataStruct d;
              while (rs.next()) {
                d = new DemoConsultDataStruct();
                d.demoNo = db.getString(rs,"demographicNo");
                demoList.add(d);
              }

              rs.close();
        }catch (java.sql.SQLException e){ System.out.println("Problems");   System.out.println(e.getMessage());  }


    }

/**
*This is a inner class that stores info on demographics.  It will get Consult letters that have been scanned and consults for the patient
*/
public class DemoConsultDataStruct{
   public String demoNo;
    ArrayList consultList;
    ArrayList conReplyList;

    public ArrayList getConsults(){
       try{
          DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
          java.sql.ResultSet rs;
          String sql = " select * from consultationRequests where demographicNo = '"+demoNo+"' "
                      +" and to_days(now()) - to_days(referalDate) <=  "
                      +" (to_days( now() ) - to_days( date_sub( now(), interval "+days+" month ) ) )";
          rs = db.GetSQL(sql);
          Consult con; 
          consultList = new ArrayList();
          while (rs.next()){
             con = new Consult(); 
             con.requestId   = db.getString(rs,"requestId");
             con.referalDate = db.getString(rs,"referalDate");
             con.serviceId   = db.getString(rs,"serviceId");
             con.specialist  = db.getString(rs,"specId");
             con.appDate     = db.getString(rs,"appointmentDate");
             consultList.add(con);
          }
          rs.close();
       }catch (java.sql.SQLException e2) { System.out.println(e2.getMessage()); }
      return consultList;
    }
    public ArrayList getConReplys(){

       try{
          DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
          ResultSet rs;
          String sql = "select d.document_no, d.docdesc,d.docfilename, d.updatedatetime, d.status  from ctl_document c, document d where c.module = 'demographic' and c.document_no = d.document_no and d.doctype = 'consult' and module_id = '"+demoNo+"' ";
          rs = db.GetSQL(sql);
          ConLetter conLetter;
          conReplyList = new ArrayList();
          while( rs.next()){
             conLetter = new ConLetter();
             conLetter.document_no = db.getString(rs,"document_no"); 
             conLetter.docdesc     = db.getString(rs,"docdesc");
             conLetter.docfileName = db.getString(rs,"docfilename");
             conLetter.docDate     = rs.getDate("updatedatetime");     
             conLetter.docStatus   = db.getString(rs,"status");
             conReplyList.add(conLetter);
          }         
          rs.close(); 
       }catch (java.sql.SQLException e3) { System.out.println(e3.getMessage()); }
    return conReplyList;
    }

    public String getDemographicName(){
       String retval = "&nbsp;";
       try{
           DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
           ResultSet rs;
           String sql = "Select last_name, first_name from demographic where demographic_no = '"+demoNo+"' ";
           rs = db.GetSQL(sql);
           if (rs.next()){
              retval = db.getString(rs,"last_name")+", "+db.getString(rs,"first_name");
           }
           rs.close();
       }catch ( java.sql.SQLException e4) { System.out.println(e4.getMessage()); }
       return retval;
    }

    public String getService(String serId){
       String retval = "";
       try{
           DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
           ResultSet rs;
           String sql = "Select serviceDesc from consultationServices where serviceId = '"+serId+"' ";
           rs = db.GetSQL(sql);
           if (rs.next()){
              retval = db.getString(rs,"last_name")+", "+db.getString(rs,"first_name");
           }
           rs.close();
       }catch ( java.sql.SQLException e4) { System.out.println(e4.getMessage()); }
       return retval;
    }

    public String getSpecialist(String specId){
        String retval = "";
       try{
           DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
           ResultSet rs;
           String sql = "Select lname, fname from professionalSpecialists where specId = '"+specId+"' ";
           rs = db.GetSQL(sql);
           if (rs.next()){
              retval = db.getString(rs,"lname")+", "+db.getString(rs,"fname");
           }
           rs.close();
       }catch ( java.sql.SQLException e4) { System.out.println(e4.getMessage()); }
       return retval;
    }

    
  public final class Consult{
     public  String requestId;
     public  String referalDate;
     public  String serviceId;
     public  String specialist;
     public  String appDate;

      public String getService(String serId){
       String retval = "&nbsp;";
       try{
           DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
           ResultSet rs;
           String sql = "Select serviceDesc from consultationServices where serviceId = '"+serId+"' ";
           rs = db.GetSQL(sql);
           if (rs.next()){
              retval = db.getString(rs,"serviceDesc");
           }
           rs.close();
       }catch ( java.sql.SQLException e4) { System.out.println(e4.getMessage()); }
       return retval;
    }

    public String getSpecialist(String specId){
        String retval = "&nbsp;";
       try{
           DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
           ResultSet rs;
           String sql = "Select lname, fname from professionalSpecialists where specId = '"+specId+"' ";
           rs = db.GetSQL(sql);
           if (rs.next()){
              retval = db.getString(rs,"lname")+", "+db.getString(rs,"fname");
           }
           rs.close();
       }catch ( java.sql.SQLException e4) { System.out.println(e4.getMessage()); }
       return retval;
    }

  };
  public final class ConLetter{
     public String document_no;
     public String docdesc;
     public String docfileName;
     public String docStatus;
     public java.sql.Date   docDate;
  };


};
}
