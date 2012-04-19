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


package oscar.oscarReport.data;

import java.sql.ResultSet;
import java.util.ArrayList;

import org.oscarehr.common.dao.ConsultationServiceDao;
import org.oscarehr.common.model.ConsultationServices;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarDB.DBHandler;
/**
*This classes main function ConsultReportGenerate collects a group of patients with consults in the last specified date
*/
public class RptConsultReportData {

	private ConsultationServiceDao consultationServiceDao = (ConsultationServiceDao)SpringUtils.getBean("consultationServiceDao");

    public ArrayList demoList = null;
    public String days = null;

    public RptConsultReportData() {
    }

    public ArrayList providerList(){
        ArrayList arrayList = new ArrayList();
        try{


              ResultSet rs;
              String sql = "select provider_no, last_name, first_name from provider where provider_type = 'doctor' order by last_name";
              rs = DBHandler.GetSQL(sql);
              while (rs.next()) {
                 ArrayList a = new ArrayList ();
                 a.add( oscar.Misc.getString(rs, "provider_no") );
                 a.add( oscar.Misc.getString(rs, "last_name") +", "+ oscar.Misc.getString(rs, "first_name") );
                 arrayList.add(a);
              }
              rs.close();
        }catch (java.sql.SQLException e){ MiscUtils.getLogger().debug("Problems");   MiscUtils.getLogger().error("Error", e);  MiscUtils.getLogger().error("Error", e);}
    return arrayList;
    }

    public void consultReportGenerate( String providerNo, String days ){
       this.days = days;
       try{


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
              rs = DBHandler.GetSQL(sql);
              demoList = new ArrayList();
              DemoConsultDataStruct d;
              while (rs.next()) {
                d = new DemoConsultDataStruct();
                d.demoNo = oscar.Misc.getString(rs, "demographicNo");
                demoList.add(d);
              }

              rs.close();
        }catch (java.sql.SQLException e){ MiscUtils.getLogger().debug("Problems");   MiscUtils.getLogger().error("Error", e);  }


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

          java.sql.ResultSet rs;
          String sql = " select * from consultationRequests where demographicNo = '"+demoNo+"' "
                      +" and to_days(now()) - to_days(referalDate) <=  "
                      +" (to_days( now() ) - to_days( date_sub( now(), interval "+days+" month ) ) )";
          rs = DBHandler.GetSQL(sql);
          Consult con;
          consultList = new ArrayList();
          while (rs.next()){
             con = new Consult();
             con.requestId   = oscar.Misc.getString(rs, "requestId");
             con.referalDate = oscar.Misc.getString(rs, "referalDate");
             con.serviceId   = oscar.Misc.getString(rs, "serviceId");
             con.specialist  = oscar.Misc.getString(rs, "specId");
             con.appDate     = oscar.Misc.getString(rs, "appointmentDate");
             consultList.add(con);
          }
          rs.close();
       }catch (java.sql.SQLException e2) { MiscUtils.getLogger().debug(e2.getMessage()); }
      return consultList;
    }
    public ArrayList getConReplys(){

       try{

          ResultSet rs;
          String sql = "select d.document_no, d.docdesc,d.docfilename, d.updatedatetime, d.status  from ctl_document c, document d where c.module = 'demographic' and c.document_no = d.document_no and d.doctype = 'consult' and module_id = '"+demoNo+"' ";
          rs = DBHandler.GetSQL(sql);
          ConLetter conLetter;
          conReplyList = new ArrayList();
          while( rs.next()){
             conLetter = new ConLetter();
             conLetter.document_no = oscar.Misc.getString(rs, "document_no");
             conLetter.docdesc     = oscar.Misc.getString(rs, "docdesc");
             conLetter.docfileName = oscar.Misc.getString(rs, "docfilename");
             conLetter.docDate     = rs.getDate("updatedatetime");
             conLetter.docStatus   = oscar.Misc.getString(rs, "status");
             conReplyList.add(conLetter);
          }
          rs.close();
       }catch (java.sql.SQLException e3) { MiscUtils.getLogger().debug(e3.getMessage()); }
    return conReplyList;
    }

    public String getDemographicName(){
       String retval = "&nbsp;";
       try{

           ResultSet rs;
           String sql = "Select last_name, first_name from demographic where demographic_no = '"+demoNo+"' ";
           rs = DBHandler.GetSQL(sql);
           if (rs.next()){
              retval = oscar.Misc.getString(rs, "last_name")+", "+oscar.Misc.getString(rs, "first_name");
           }
           rs.close();
       }catch ( java.sql.SQLException e4) { MiscUtils.getLogger().debug(e4.getMessage()); }
       return retval;
    }

    public String getService(String serId){
       String retval = "";
       ConsultationServices cs = consultationServiceDao.find(Integer.parseInt(serId));
       if(cs != null) {
    	   retval = cs.getServiceDesc();
       }

       return retval;
    }

    public String getSpecialist(String specId){
        String retval = "";
       try{

           ResultSet rs;
           String sql = "Select lname, fname from professionalSpecialists where specId = '"+specId+"' ";
           rs = DBHandler.GetSQL(sql);
           if (rs.next()){
              retval = oscar.Misc.getString(rs, "lname")+", "+oscar.Misc.getString(rs, "fname");
           }
           rs.close();
       }catch ( java.sql.SQLException e4) { MiscUtils.getLogger().debug(e4.getMessage()); }
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
       ConsultationServices cs = consultationServiceDao.find(Integer.parseInt(serId));
       if(cs != null) {
    	   retval = cs.getServiceDesc();
       }
       return retval;
    }

    public String getSpecialist(String specId){
        String retval = "&nbsp;";
       try{

           ResultSet rs;
           String sql = "Select lname, fname from professionalSpecialists where specId = '"+specId+"' ";
           rs = DBHandler.GetSQL(sql);
           if (rs.next()){
              retval = oscar.Misc.getString(rs, "lname")+", "+oscar.Misc.getString(rs, "fname");
           }
           rs.close();
       }catch ( java.sql.SQLException e4) { MiscUtils.getLogger().debug(e4.getMessage()); }
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
