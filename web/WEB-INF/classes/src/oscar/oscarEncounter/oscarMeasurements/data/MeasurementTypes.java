/*
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
 *  Ontario, Canada   Creates a new instance of Prevention
 *
 * MeasurementTypes.java
 *
 * Created on February 15, 2006, 9:43 PM
 *
 */

package oscar.oscarEncounter.oscarMeasurements.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import oscar.oscarDB.DBHandler;
import oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementTypesBean;

/**
 *
 * @author Jay Gallagher
 */
public class MeasurementTypes {
    private static Log log = LogFactory.getLog(MeasurementTypes.class);
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
               DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
               String sql = "SELECT * FROM measurementType";   

               ResultSet rs = db.GetSQL(sql);        
               while(rs.next()){                
                  //log.debug("validation "+db.getString(rs,("validation"));  
                  EctMeasurementTypesBean ret = null;
                  ret = new EctMeasurementTypesBean(rs.getInt("id"), db.getString(rs,"type"), 
                                                     db.getString(rs,"typeDisplayName"), 
                                                     db.getString(rs,"typeDescription"), 
                                                     db.getString(rs,"measuringInstruction"),  
                                                     db.getString(rs,"validation")); 
                  ret.setValidationName(getValidation(db.getString(rs,"validation")));
                  byId.put(""+rs.getInt("id"),ret);
                  byType.put(db.getString(rs,"type"),ret);


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
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sqlValidation = "SELECT name FROM validations WHERE id='"+val+"'";
            ResultSet rs = db.GetSQL(sqlValidation);
            if (rs.next()){ 
                validation = db.getString(rs,"name");
                //log.debug("setting validation to "+validation);
            }
            rs.close();
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return validation;
    }
     
}
