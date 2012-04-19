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


package oscar.oscarEncounter.oscarMeasurements.bean;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;
import oscar.oscarEncounter.oscarMeasurements.data.MeasurementTypes;

/**
 *
 * @author jay
 */
public class EctMeasurementTypeBeanHandler {
    private static Logger log = MiscUtils.getLogger();
    
    /** Creates a new instance of EctMeasurementTypeBeanHandler */
    public EctMeasurementTypeBeanHandler() {
    }
    
    public EctMeasurementTypesBean getMeasurementType(String mType){
        log.debug(" calling get MeasurementType");
        MeasurementTypes mt =  MeasurementTypes.getInstance();
        return mt.getByType(mType);
        
    }
    
    public String getValidation(String val){
        String validation = null;
        try {
            
            String sqlValidation = "SELECT name FROM validations WHERE id='"+val+"'";
            ResultSet rs = DBHandler.GetSQL(sqlValidation);
            if (rs.next()){ 
                validation = oscar.Misc.getString(rs, "name");

            }
            rs.close();
        }catch(SQLException e) {
            MiscUtils.getLogger().error("Error", e);
        }
        return validation;
    }
}
