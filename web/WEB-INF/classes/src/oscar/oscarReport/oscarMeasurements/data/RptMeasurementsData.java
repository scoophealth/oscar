package oscar.oscarReport.oscarMeasurements.data;

import oscar.oscarDB.*;
import java.sql.*;
import java.util.*;
import javax.servlet.http.*;
import org.apache.struts.util.MessageResources;
import org.apache.struts.action.*;
/**
*This classes main function ConsultReportGenerate collects a group of patients with consults in the last specified date
*/
public class RptMeasurementsData {    

      /*****************************************************************************************
     * get the number of Patient seen during aspecific time period
     *
     * @return number or Patients seen in Integer
     ******************************************************************************************/  
    public int getNbPatientSeen(DBHandler db, String startDateA, String endDateA){
        
        int nbPatient = 0;
        
        try{
            String sql = "SELECT DISTINCT demographicNo FROM measurements WHERE dateObserved >= '" + startDateA + "' AND dateObserved <= '" + endDateA + "'";
            ResultSet rs;
            rs = db.GetSQL(sql);
            System.out.println("SQL Statement: " + sql);
            rs.last();
            nbPatient = rs.getRow();

            rs.close();

        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return nbPatient;
    }
    
    
     /*****************************************************************************************
     * get the number of patients during aspecific time period
     *
     * @return ArrayList which contain the result in String format
     ******************************************************************************************/      
    public ArrayList getPatientsSeen(DBHandler db, String startDate, String endDate){

        ArrayList patients = new ArrayList();
        
        try{
            String sql = "SELECT DISTINCT demographicNo  FROM measurements WHERE dateObserved >= '" + startDate + "' AND dateObserved <= '" + endDate + "'";
            System.out.println("SQL Statement: " + sql);
            ResultSet rs;
            
            for(rs=db.GetSQL(sql); rs.next();){            
                String patient = rs.getString("demographicNo");
                patients.add(patient);                
            }
            rs.close();
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
       
        return patients;
    }        
}
