package oscar.form;

import oscar.oscarDB.*;
import oscar.oscarEncounter.data.*;
import oscar.util.* ;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Date;
import java.util.*;
import java.sql.*;
import java.io.*;
import java.lang.String;

public class FrmRourkeRecord extends FrmRecord {
    public Properties getFormRecord(int demographicNo, int existingID)
            throws SQLException    {
        Properties props = new Properties();

        if(existingID <= 0) {
			DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "SELECT demographic_no, CONCAT(last_name, ', ', first_name) AS pName, "
                + "year_of_birth, month_of_birth, date_of_birth, sex "
                + "FROM demographic WHERE demographic_no = " + demographicNo;
            ResultSet rs = db.GetSQL(sql);
            if(rs.next()) {
                props.setProperty("demographic_no", rs.getString("demographic_no"));
                props.setProperty("c_pName", rs.getString("pName"));
                props.setProperty("formDate", UtilDateUtilities.DateToString(UtilDateUtilities.Today(), "yyyy/MM/dd"));
                props.setProperty("formCreated", UtilDateUtilities.DateToString(UtilDateUtilities.Today(), "yyyy/MM/dd"));
                //props.setProperty("formEdited", UtilDateUtilities.DateToString(UtilDateUtilities.Today(), "yyyy/MM/dd"));
                java.util.Date dob = UtilDateUtilities.calcDate(rs.getString("year_of_birth"), rs.getString("month_of_birth"), rs.getString("date_of_birth"));
                props.setProperty("c_birthDate", UtilDateUtilities.DateToString(dob, "yyyy/MM/dd"));
                //props.setProperty("age", String.valueOf(UtilDateUtilities.calcAge(dob)));
            }
            rs.close();
			db.CloseConn();
        } else {
            String sql = "SELECT * FROM formRourke WHERE demographic_no = " +demographicNo +" AND ID = " +existingID;
			props = (new FrmRecordHelp()).getFormRecord(sql);
        }

        return props;
    }

    public int saveFormRecord(Properties props) throws SQLException {
        String demographic_no = props.getProperty("demographic_no");
        String sql = "SELECT * FROM formRourke WHERE demographic_no=" +demographic_no +" AND ID=0";

		return ((new FrmRecordHelp()).saveFormRecord(props, sql));
    }

//////////////new/ Done By Jay////
    public boolean isFemale(int demo){
	boolean retval = false;
	DBHandler db;
	ResultSet rs;
	String str = "M";
	try{
		db = new DBHandler(DBHandler.OSCAR_DATA);
		rs = db.GetSQL("select sex from demographic where demographic_no = "+demo);
		if(rs.next()){
			str = rs.getString("sex");	
			if (str.equalsIgnoreCase("F")){
				retval = true;
			}
		}
	rs.close();
	db.CloseConn();
	}catch(Exception exc){exc.printStackTrace();}	
	return retval;
    }
///////////////////////////////////

    public Properties getGraph(int demographicNo, int existingID)  throws SQLException {
        Properties props = new Properties();

        DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
        ResultSet rs;
        String sql;

        if(existingID==0) {
            return props;
        }  else {
            sql = "SELECT c_pName, c_birthDate, c_birthWeight, c_headCirc, c_length, "
                + "p1_date1w, p1_date2w, p1_date1m, p1_date2m, "
                + "p2_date4m, p2_date6m, p2_date9m, p2_date12m, p3_date18m, p3_date2y, "
                + "p1_hc1w, p1_hc2w, p1_hc1m, p1_hc2m, "
                + "p2_hc4m, p2_hc6m, p2_hc9m, p2_hc12m, p3_hc18m, "
                + "p1_wt1w, p1_wt2w, p1_wt1m, p1_wt2m, "
                + "p2_wt4m, p2_wt6m, p2_wt9m, p2_wt12m, p3_wt18m, p3_wt2y, "
                + "p1_ht1w, p1_ht2w, p1_ht1m, p1_ht2m, "
                + "p2_ht4m, p2_ht6m, p2_ht9m, p2_ht12m, p3_ht18m, p3_ht2y "
                + "FROM formRourke "
                + "WHERE demographic_no = " + demographicNo + " AND ID = " + existingID;

            rs = db.GetSQL(sql);

            if(rs.next())           {
                ResultSetMetaData md = rs.getMetaData();
                String value;

                for(int i=1; i<=md.getColumnCount(); i++)            {
                    String name = md.getColumnName(i);

                    if(md.getColumnTypeName(i).equalsIgnoreCase("date"))               {
                        value = UtilDateUtilities.DateToString(rs.getDate(i), "yyyy/MM/dd");
                    } else {
                        value = rs.getString(i);
                    }

                    if(i<=6) {
                        name = name.substring(2);
                    }  else {
                        name = name.substring(3);
                    }

                    if(value!=null) {
                        props.setProperty(name, value);
                    }
                }//end for

            }//end if
            rs.close();
        }
        db.CloseConn();

        return props;
    }

    public static double age(String dob, String today) {
        double age = -1;

        try {
            Date tToday = (oscar.util.UtilDateUtilities.StringToDate(today, "yyyy/MM/dd"));
            System.out.println("today: "+today);
            System.out.println("Date: "+tToday);
            System.out.println("Time: "+tToday.getTime());

            Date tDob = (oscar.util.UtilDateUtilities.StringToDate(dob, "yyyy/MM/dd"));
            System.out.println("Dob: "+dob);
            System.out.println("Date: "+tDob);
            System.out.println("Time: "+tDob.getTime());

            age = (tToday.getTime() - tDob.getTime())/(1000*3600*24);
            double daysPerMonth = 30.4375;
            age = age/daysPerMonth; // the approximate number of days in a month
        } catch(Exception ex) {
            System.err.println(ex);
        }
        System.out.println("this is returned: "+age);
        return age;
    }


    public String findActionValue(String submit) throws SQLException {
 		return ((new FrmRecordHelp()).findActionValue(submit));
    }

    public String createActionURL(String where, String action, String demoId, String formId) throws SQLException {
 		return ((new FrmRecordHelp()).createActionURL(where, action, demoId, formId));
    }

}
