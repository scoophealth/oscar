package oscar.oscarReport.data;

import oscar.oscarDB.*;
import java.sql.*;
import java.util.*;
/**
*This classes main function FluReportGenerate collects a group of patients with flu in the last specified date
*/
public class RptFluReportData {

    public ArrayList demoList = null;
    public String years= null;


    public RptFluReportData() {
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
                 a.add( rs.getString("provider_no") );
                 a.add( rs.getString("last_name") +", "+ rs.getString("first_name") );
                 arrayList.add(a);
              }
              rs.close();
              db.CloseConn();
        }catch (java.sql.SQLException e){ System.out.println("Problems");   System.out.println(e.getMessage());  }
    return arrayList;
    }

    public void fluReportGenerate( String providerNo, String years ){
       this.years = years;
       try{

              DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
              ResultSet rs;
              // mysql function for dates = select date_sub(now(),interval 1 month);
              String sql = "select demographic_no, CONCAT(last_name,',',first_name) as demoname, "
              + "phone, roster_status, patient_status, DATE_FORMAT(CONCAT((year_of_birth), '-', (month_of_birth),"
              + " '-',(date_of_birth)),'%Y-%m-%d') as dob, (YEAR(CURRENT_DATE)-"
              + "YEAR(DATE_FORMAT(CONCAT((year_of_birth), '-', (month_of_birth),'-',"
              + "(date_of_birth)),'%Y-%m-%d')))-(RIGHT(CURRENT_DATE,5)<"
              + "RIGHT(DATE_FORMAT(CONCAT((year_of_birth), '-', (month_of_birth),"
              + "'-',(date_of_birth)),'%Y-%m-%d'),5)) as age from demographic "
              + " where (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((year_of_birth),"
              + "'-', (month_of_birth),'-',(date_of_birth)),'%Y-%m-%d')))-(RIGHT(CURRENT_DATE,5)"
              +"<RIGHT(DATE_FORMAT(CONCAT((year_of_birth), '-', (month_of_birth),'-',(date_of_birth)),'%Y-%m-%d'),5)) >= 65"
			  + " and (patient_status = 'AC' or patient_status = 'UHIP') and (roster_status='RO' or roster_status='NR' or "
			  + "roster_status='FS' or roster_status='RF' or roster_status='PL')";
              if (!providerNo.equals("-1")){
                 sql = sql +" and provider_no = '"+providerNo+"' ";
              }
              sql = sql + "  order by last_name ";



              rs = db.GetSQL(sql);
              demoList = new ArrayList();
              DemoFluDataStruct d;
              while (rs.next()) {
                d = new DemoFluDataStruct();
                d.demoNo = rs.getString("demographic_no");
                d.demoName = rs.getString("demoname");
                d.demoPhone = rs.getString("phone");
                d.demoRosterStatus = rs.getString("roster_status");
                d.demoPatientStatus = rs.getString("patient_status");
                d.demoDOB = rs.getString("dob");
                d.demoAge = rs.getString("age");
                demoList.add(d);
              }

              rs.close();
              db.CloseConn();
        }catch (java.sql.SQLException e){ System.out.println("Problems");   System.out.println(e.getMessage());  }


    }

//**
//*This is a inner class that stores info on demographics.
//**

public class DemoFluDataStruct{
  public String demoNo;
  public String demoName;
  public String demoPhone;
  public String demoRosterStatus;
  public String demoAge;
  public String demoDOB;
  public String demoPatientStatus;

 public String getDemoNo(){
	 return demoNo;
 }

  public String getDemoName(){
 	 return demoName;
 }

  public String getDemoPhone(){
 	 return demoPhone;
 }

  public String getDemoAge(){
 	 return demoAge;
 }

  public String getDemoDOB(){
 	 return demoDOB;
 }

 public String getBillingDate(){

	        String retval = "&nbsp;";
	        try{
	            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
	            ResultSet rs;
	  String sql= "select b.billing_no, b.billing_date from billing b, billingdetail bd where bd.billing_no=b.billing_no and bd.status<>'D' and b.status<>'D' and (bd.service_code='G590A' or bd.service_code='G591A') and b.billing_date <= '2003-04-01' and b.demographic_no='" + demoNo + "' limit 0,1";
	            rs = db.GetSQL(sql);
	            if (rs.next()){
	               retval = rs.getString("billing_date");
	            }
	            rs.close();
	            db.CloseConn();
	        }catch ( java.sql.SQLException e4) { System.out.println(e4.getMessage()); }
	        return retval;
    }




  };



};
