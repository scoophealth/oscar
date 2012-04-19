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


package oscar.oscarReport.oscarMeasurements.pageUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;

public class RptMeasurementTypesBeanHandler {
    
    Vector measurementTypeVector = new Vector();
    Vector measuringInstrcVector = new Vector();
    Vector measuringInstrcBeanVector = new Vector();
    Vector typeVector = new Vector();

    public RptMeasurementTypesBeanHandler(String groupName) {
        init(groupName);
    }

    public boolean init(String groupName) {
        
        boolean verdict = true;
        try {
            
            String sql = "SELECT typeDisplayName FROM measurementGroup WHERE name='" + groupName + "'ORDER BY typeDisplayName";            
            ResultSet rs;
 
            for(rs = DBHandler.GetSQL(sql); rs.next();){
                String typeDisplayName  = rs.getString("typeDisplayName");
                String sqlMT = "SELECT DISTINCT * FROM measurementType WHERE typeDisplayName = '" + typeDisplayName + "'ORDER BY typeDescription";
                MiscUtils.getLogger().debug("SQL: " + sqlMT);
                ResultSet rsMT = DBHandler.GetSQL(sqlMT);
                if (rsMT.next()){
                    RptMeasurementTypesBean measurementTypes = new RptMeasurementTypesBean(rsMT.getInt("id"), rsMT.getString("type"), 
                                                                                       rsMT.getString("typeDisplayName"), 
                                                                                       rsMT.getString("typeDescription"), 
                                                                                       rsMT.getString("measuringInstruction"), 
                                                                                       rsMT.getString("validation"));
                MiscUtils.getLogger().debug(rsMT.getString("type"));
                measurementTypeVector.add(measurementTypes);

                RptMeasuringInstructionBeanHandler hd = new RptMeasuringInstructionBeanHandler(typeDisplayName);
                measuringInstrcBeanVector.add(hd);  
                }
            }
            
            rs.close();
        }
        catch(SQLException e) {
            MiscUtils.getLogger().error("Error", e);
            verdict = false;
        }
        return verdict;
    }

    public Vector getMeasurementTypeVector(){
        return measurementTypeVector;
    }
    
    public Vector getMeasuringInstrcBeanVector(){
        return measuringInstrcBeanVector;
    }
}
