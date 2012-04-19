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


package oscar.oscarReport.oscarMeasurements.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;
/**
*This classes main function ConsultReportGenerate collects a group of patients with consults in the last specified date
*/
public class RptMeasurementsData {    

      /*****************************************************************************************
     * get the number of Patient seen during aspecific time period
     *
     * @return number or Patients seen in Integer
     ******************************************************************************************/  
    public int getNbPatientSeen(String startDateA, String endDateA){
        
        int nbPatient = 0;
        
        try{
            String sql = "SELECT DISTINCT demographicNo FROM measurements WHERE dateObserved >= '" + startDateA + "' AND dateObserved <= '" + endDateA + "'";
            ResultSet rs;
            rs = DBHandler.GetSQL(sql);
            MiscUtils.getLogger().debug("SQL Statement: " + sql);
            rs.last();
            nbPatient = rs.getRow();

            rs.close();

        }
        catch(SQLException e)
        {
            MiscUtils.getLogger().error("Error", e);
        }

        return nbPatient;
    }
    
    
     /*****************************************************************************************
     * get the number of patients during aspecific time period
     *
     * @return ArrayList which contain the result in String format
     ******************************************************************************************/      
    public ArrayList getPatientsSeen(String startDate, String endDate){

        ArrayList patients = new ArrayList();
        
        try{
            String sql = "SELECT DISTINCT demographicNo  FROM measurements WHERE dateObserved >= '" + startDate + "' AND dateObserved <= '" + endDate + "'";
            MiscUtils.getLogger().debug("SQL Statement: " + sql);
            ResultSet rs;
            
            for(rs=DBHandler.GetSQL(sql); rs.next();){            
                String patient = rs.getString("demographicNo");
                patients.add(patient);                
            }
            rs.close();
        }
        catch(SQLException e)
        {
            MiscUtils.getLogger().error("Error", e);
        }
       
        return patients;
    }        
}
