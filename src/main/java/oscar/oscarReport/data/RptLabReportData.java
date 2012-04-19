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
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;

import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;
/**
 * This classes main function ConsultReportGenerate collects a group of patients with consults in the last specified date
 * Could use a rewrite
*/
public class RptLabReportData {

    public ArrayList demoList = null;
    public String days = null;


    public RptLabReportData() {
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
        }catch (java.sql.SQLException e){ MiscUtils.getLogger().debug("Problems");   MiscUtils.getLogger().error("Error", e);  }
    return arrayList;
    }

    public void labReportGenerate( String providerNo, String days ){
       this.days = days;
       try{
              
              
              ResultSet rs;
              // mysql function for dates = select date_sub(now(),interval 1 month); 
              String sql = "select distinct l.demographic_no from formLabReq l , demographic d where "
                          +" ( to_days( now() ) - to_days(formCreated) ) <= "
                          +" ( to_days( now() ) - to_days( date_sub(now(),interval "+days+" month) ) )"
                          +" and l.demographic_no = d.demographic_no ";
              if (!providerNo.equals("-1")){
                 sql = sql +" and d.provider_no = '"+providerNo+"' "; 
              }
              sql = sql + "  order by d.last_name ";

              rs = DBHandler.GetSQL(sql);
              demoList = new ArrayList();
              DemoLabDataStruct d;
              while (rs.next()) {
                d = new DemoLabDataStruct();
                d.demoNo = oscar.Misc.getString(rs, "demographic_no");
                demoList.add(d);
              }

              rs.close();
        }catch (java.sql.SQLException e){ MiscUtils.getLogger().debug("Problems");   MiscUtils.getLogger().error("Error", e);  }

    }

/**
*This is a inner class that stores info on demographics.  It will get Consult letters that have been scanned and consults for the patient
*/
public class DemoLabDataStruct{
   public String demoNo;
    ArrayList consultList;
    ArrayList conReplyList;

    public ArrayList getLabReqs(){
       try{
          
          java.sql.ResultSet rs;
          String sql = " select * from formLabReq where demographic_no = '"+demoNo+"' "
                      +" and to_days(now()) - to_days(formCreated) <=  "
                      +" (to_days( now() ) - to_days( date_sub( now(), interval "+days+" month ) ) )";
          rs = DBHandler.GetSQL(sql);
          Consult con; 
          consultList = new ArrayList();
          while (rs.next()){
             con = new Consult(); 
             con.requestId   = oscar.Misc.getString(rs, "ID");
             con.referalDate = oscar.Misc.getString(rs, "formCreated");
             con.proNo       = oscar.Misc.getString(rs, "provider_no");
             consultList.add(con);
          }
          rs.close();
       }catch (java.sql.SQLException e2) { MiscUtils.getLogger().debug(e2.getMessage()); }
      return consultList;
    }
    public ArrayList getLabReplys(){

       try{
          
          ResultSet rs;
          String sql = "select d.document_no, d.docdesc,d.docfilename, d.updatedatetime, d.status  from ctl_document c, document d where c.module = 'demographic' and c.document_no = d.document_no and d.doctype = 'lab' and module_id = '"+demoNo+"' ";
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


    public ArrayList getLabReports(String demographic, java.util.Date startDate){
       ArrayList list = new ArrayList();
       try{
          
          ResultSet rs;
          String sql = "select p.lab_no, l.collection_date, lab_status, accession_num , lab_type from patientLabRouting p , labPatientPhysicianInfo l where p.lab_type = 'CML' and p.lab_no = l.id and p.demographic_no = '"+demographic+"' ";
          MiscUtils.getLogger().debug(sql);
          rs = DBHandler.GetSQL(sql);
          
          list = new ArrayList();         
          while( rs.next()){
             java.util.Date lab = getDateFromCML(oscar.Misc.getString(rs, "collection_date"));
             MiscUtils.getLogger().debug(lab+" "+startDate+" "+lab.after(startDate));
             if (startDate != null && lab != null && lab.after(startDate) ){
                Hashtable h = new Hashtable();              
                h.put("collectionDate",getCommonDate(lab));
                h.put("id",oscar.Misc.getString(rs, "lab_no"));
                h.put("labType",oscar.Misc.getString(rs, "lab_type"));
                list.add(h);
             }
          }                  
          rs.close();
          
          sql = "select p.lab_no, m.dateTime, lab_type from patientLabRouting p , mdsMSH m where p.lab_type = 'MDS' and p.lab_no = m.segmentID and p.demographic_no = '"+demographic+"' ";
          MiscUtils.getLogger().debug(sql);
          rs = DBHandler.GetSQL(sql);
                    
          while( rs.next()){
             java.util.Date lab = getDateFromMDS(oscar.Misc.getString(rs, "dateTime"));
             MiscUtils.getLogger().debug(lab+" "+startDate+" "+lab.after(startDate));
             if (startDate != null && lab != null && lab.after(startDate) ){
               Hashtable h = new Hashtable(); 
               h.put("collectionDate",getCommonDate(lab));
               h.put("id",oscar.Misc.getString(rs, "lab_no"));
               h.put("labType",oscar.Misc.getString(rs, "lab_type"));
               list.add(h);
             }
          }                  
          rs.close(); 
       }catch (java.sql.SQLException e3) { MiscUtils.getLogger().debug(e3.getMessage()); MiscUtils.getLogger().error("Error", e3); }
       return list;
    }
    
    private String getCommonDate(java.util.Date date){
       String s = "unknown date";
       try{
          Format formatter = new SimpleDateFormat("yyyy-mm-dd");
          s = formatter.format(date);
       }catch(Exception e){}       
       return s;
    }

    private java.util.Date getDateFromCML(String d){
       java.util.Date date = null;
       try{
          DateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
          date = formatter.parse(d);
       }catch(Exception e){}
       return date;
    }
    
    private java.util.Date getDateFromMDS(String d){  //2004-11-17 16:23:23
       java.util.Date date = null;
       try{       
         DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
         date = formatter.parse(d);
       }catch(Exception e){}
       return date;
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

  

    
  public final class Consult{
     public  String requestId;
     public  String referalDate;
     public  String serviceId;
     public  String specialist;
     public  String appDate;
     public  String proNo;  
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
