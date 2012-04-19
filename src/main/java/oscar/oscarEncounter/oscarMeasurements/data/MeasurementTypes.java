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


package oscar.oscarEncounter.oscarMeasurements.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;
import oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementTypesBean;

/**
 * @deprecated use MeasurementTypeDao instead (2012-01-23)
 */
public class MeasurementTypes {
    private static Logger log = MiscUtils.getLogger();
    static MeasurementTypes measurementTypes = new MeasurementTypes();
    boolean loaded = false;
    Hashtable byId = null;
    Hashtable byType = null;
    
    /** Creates a new instance of MeasurementTypes */
    private MeasurementTypes() {
    }
   
    public EctMeasurementTypesBean getByType(String type){
       while(loaded == false){         
          try {
            wait();
          } catch (InterruptedException e) { }
       }
       return (EctMeasurementTypesBean) byType.get(type);     
    }
    
    public EctMeasurementTypesBean getById(String type){
       while(loaded == false){         
          try {
            wait();
          } catch (InterruptedException e) { }
       }
       return (EctMeasurementTypesBean) byId.get(type);     
    }
    
    static public MeasurementTypes getInstance(){
       if (!measurementTypes.loaded) {
         measurementTypes.loadMeasurementTypes();
       }
       return measurementTypes;
    }

    public void reInit(){
        log.debug("Reloading measurement types");
        loaded = false;
        measurementTypes.loadMeasurementTypes();
    }
    
    private synchronized void loadMeasurementTypes() {
        log.debug("LOADING GETS CALLED");
        if (!loaded){
            log.debug("LOADING RUNS");
            byId = new Hashtable();
            byType = new Hashtable();

            try {
               
               String sql = "SELECT * FROM measurementType";   

               ResultSet rs = DBHandler.GetSQL(sql);        
               while(rs.next()){                
                  //log.debug("validation "+oscar.Misc.getString(rs,("validation"));  
                  EctMeasurementTypesBean ret = null;
                  ret = new EctMeasurementTypesBean(rs.getInt("id"), oscar.Misc.getString(rs, "type"), 
                                                     oscar.Misc.getString(rs, "typeDisplayName"), 
                                                     oscar.Misc.getString(rs, "typeDescription"), 
                                                     oscar.Misc.getString(rs, "measuringInstruction"),  
                                                     oscar.Misc.getString(rs, "validation")); 
                  ret.setValidationName(getValidation(oscar.Misc.getString(rs, "validation")));
                  byId.put(""+rs.getInt("id"),ret);
                  byType.put(oscar.Misc.getString(rs, "type"),ret);


                }
                rs.close();            
                loaded = true;
                notifyAll();
            }
            catch(SQLException e) {
                log.debug(e.getMessage());
            }
        }
            //return ret;
    }
    
    private String getValidation(String val){
        String validation = null;
        try {
            
            String sqlValidation = "SELECT name FROM validations WHERE id='"+val+"'";
            ResultSet rs = DBHandler.GetSQL(sqlValidation);
            if (rs.next()){ 
                validation = oscar.Misc.getString(rs, "name");
                //log.debug("setting validation to "+validation);
            }
            rs.close();
        }catch(SQLException e) {
            MiscUtils.getLogger().error("Error", e);
        }
        return validation;
    }
     
}
