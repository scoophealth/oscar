/**
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 *  This software is published under the GPL GNU General Public License.
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version. *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 *  Jason Gallagher
 *
 *  This software was written for the
 *  Department of Family Medicine
 *  McMaster University
 *  Hamilton
 *  Ontario, Canada   Creates a new instance of GeneralProperties
 *
 *
 * GeneralProperties.java
 *
 * Created on May 24, 2005, 1:50 PM
 */
package oscar.oscarReport.data;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;

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
        }catch (java.sql.SQLException e){ System.out.println("Problems");   System.out.println(e.getMessage());  }
    return arrayList;
    }

    public void labReportGenerate( String providerNo, String days ){
       this.days = days;
       try{
              
              DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
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

              rs = db.GetSQL(sql);
              demoList = new ArrayList();
              DemoLabDataStruct d;
              while (rs.next()) {
                d = new DemoLabDataStruct();
                d.demoNo = db.getString(rs,"demographic_no");
                demoList.add(d);
              }

              rs.close();
        }catch (java.sql.SQLException e){ System.out.println("Problems");   System.out.println(e.getMessage());  }

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
          DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
          java.sql.ResultSet rs;
          String sql = " select * from formLabReq where demographic_no = '"+demoNo+"' "
                      +" and to_days(now()) - to_days(formCreated) <=  "
                      +" (to_days( now() ) - to_days( date_sub( now(), interval "+days+" month ) ) )";
          rs = db.GetSQL(sql);
          Consult con; 
          consultList = new ArrayList();
          while (rs.next()){
             con = new Consult(); 
             con.requestId   = db.getString(rs,"ID");
             con.referalDate = db.getString(rs,"formCreated");
             con.proNo       = db.getString(rs,"provider_no");
             consultList.add(con);
          }
          rs.close();
       }catch (java.sql.SQLException e2) { System.out.println(e2.getMessage()); }
      return consultList;
    }
    public ArrayList getLabReplys(){

       try{
          DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
          ResultSet rs;
          String sql = "select d.document_no, d.docdesc,d.docfilename, d.updatedatetime, d.status  from ctl_document c, document d where c.module = 'demographic' and c.document_no = d.document_no and d.doctype = 'lab' and module_id = '"+demoNo+"' ";
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


    public ArrayList getLabReports(String demographic, java.util.Date startDate){
       ArrayList list = new ArrayList();
       try{
          DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
          ResultSet rs;
          String sql = "select p.lab_no, l.collection_date, lab_status, accession_num , lab_type from patientLabRouting p , labPatientPhysicianInfo l where p.lab_type = 'CML' and p.lab_no = l.id and p.demographic_no = '"+demographic+"' ";
          System.out.println(sql);
          rs = db.GetSQL(sql);
          
          list = new ArrayList();         
          while( rs.next()){
             java.util.Date lab = getDateFromCML(db.getString(rs,"collection_date"));
             System.out.println(lab+" "+startDate+" "+lab.after(startDate));
             if (startDate != null && lab != null && lab.after(startDate) ){
                Hashtable h = new Hashtable();              
                h.put("collectionDate",getCommonDate(lab));
                h.put("id",db.getString(rs,"lab_no"));
                h.put("labType",db.getString(rs,"lab_type"));
                list.add(h);
             }
          }                  
          rs.close();
          
          sql = "select p.lab_no, m.dateTime, lab_type from patientLabRouting p , mdsMSH m where p.lab_type = 'MDS' and p.lab_no = m.segmentID and p.demographic_no = '"+demographic+"' ";
          System.out.println(sql);
          rs = db.GetSQL(sql);
                    
          while( rs.next()){
             java.util.Date lab = getDateFromMDS(db.getString(rs,"dateTime"));
             System.out.println(lab+" "+startDate+" "+lab.after(startDate));
             if (startDate != null && lab != null && lab.after(startDate) ){
               Hashtable h = new Hashtable(); 
               h.put("collectionDate",getCommonDate(lab));
               h.put("id",db.getString(rs,"lab_no"));
               h.put("labType",db.getString(rs,"lab_type"));
               list.add(h);
             }
          }                  
          rs.close(); 
       }catch (java.sql.SQLException e3) { System.out.println(e3.getMessage()); e3.printStackTrace(); }
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
          date = (java.util.Date) formatter.parse(d);
       }catch(Exception e){}
       return date;
    }
    
    private java.util.Date getDateFromMDS(String d){  //2004-11-17 16:23:23
       java.util.Date date = null;
       try{       
         DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
         date = (java.util.Date) formatter.parse(d);
       }catch(Exception e){}
       return date;
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
