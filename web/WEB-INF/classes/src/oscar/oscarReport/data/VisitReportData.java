package oscar.oscarReport.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import oscar.oscarDB.DBHandler;
//import oscar.oscarEncounter.util.StringQuote;
//import oscar.oscarMessenger.util.*;


public class VisitReportData {


  private String dateBegin=null;
  private String dateEnd=null;
  private String providerNo=null;

  public VisitReportData() {}

 	  public void setDateBegin(String value) {
	    dateBegin=value;
	  }

 	  public void setDateEnd(String value) {
	    dateEnd=value;
	  }

	   public void setProviderNo(String value) {
	  	    providerNo=value;
	  }


    public String[] getCreatorCount(){
       String retval = "";
       String retcount = "";
       String[] retVisit = new String[5];
       retVisit[0] = "0";
        retVisit[1] = "0";
         retVisit[2] = "0";
          retVisit[3] = "0";
           retVisit[4] = "0";

       try{
             DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
             String sql = "select Right(visittype, 1) visit, count(*) n from billing where status<>'D' and appointment_no<>'0' and creator='"+ providerNo +"' and billing_date>='" + dateBegin + "' and billing_date<='" + dateEnd + "' group by visittype";
             ResultSet rs = db.GetSQL(sql);
             while (rs.next()){
                retval = rs.getString("visit");
                retcount =rs.getString("n");
                retVisit[Integer.parseInt(retval)] = retcount;

			}
             rs.close();
             db.CloseConn();
          }
          catch(SQLException e){
             System.out.println("There has been an error while retrieving a visit count");
             System.out.println(e.getMessage());
          }

       return retVisit;
    }

        public String[] getApptProviderCount(){
	       String retval = "";
	       String retcount = "";
	       String[] retVisit = new String[5];
	       retVisit[0] = "0";
	        retVisit[1] = "0";
	         retVisit[2] = "0";
	          retVisit[3] = "0";
	           retVisit[4] = "0";

	       try{
	             DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
	             String sql = "select Right(visittype, 1) visit, count(*) n from billing where status<>'D' and appointment_no<>'0' and apptProvider_no='"+ providerNo +"' and billing_date>='" + dateBegin + "' and billing_date<='" + dateEnd + "' group by visittype";
	             ResultSet rs = db.GetSQL(sql);
	             while (rs.next()){
	                retval = rs.getString("visit");
	                retcount =rs.getString("n");
	                retVisit[Integer.parseInt(retval)] = retcount;

				}
	             rs.close();
	             db.CloseConn();
	          }
	          catch(SQLException e){
	             System.out.println("There has been an error while retrieving a visit count");
	             System.out.println(e.getMessage());
	          }

	       return retVisit;
    }

  }
