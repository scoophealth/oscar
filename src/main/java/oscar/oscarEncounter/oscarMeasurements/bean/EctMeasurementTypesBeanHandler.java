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
import java.util.ResourceBundle;
import java.util.Vector;

import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;

public class EctMeasurementTypesBeanHandler {
    
    Vector measurementTypeVector = new Vector();
    Vector measuringInstrcVector = new Vector();
    Vector measuringInstrcHdVector = new Vector();
    Vector measuringInstrcVectorVector = new Vector();
    Vector typeVector = new Vector();
    Vector measurementsDataVector = new Vector();

    public EctMeasurementTypesBeanHandler() {
        init();
    }
    public EctMeasurementTypesBeanHandler(String groupName, String demo) {
        init(groupName, demo);
    }

    public boolean init() {
        
        boolean verdict = true;
        try {
            

            String sql = "SELECT * FROM measurementType ORDER BY type";              
            ResultSet rs;        
            for(rs = DBHandler.GetSQL(sql); rs.next(); )
            {                
                String validation = oscar.Misc.getString(rs, "validation");
                String sqlValidation = "SELECT name FROM validations WHERE id='" + validation + "'";
                ResultSet rsValidation = DBHandler.GetSQL(sqlValidation);
                if (rsValidation.next()){
                    EctMeasurementTypesBean measurementTypes = new EctMeasurementTypesBean(rs.getInt("id"), oscar.Misc.getString(rs, "type"), 
                                                                                           oscar.Misc.getString(rs, "typeDisplayName"), 
                                                                                           oscar.Misc.getString(rs, "typeDescription"), 
                                                                                           oscar.Misc.getString(rs, "measuringInstruction"), 
                                                                                           rsValidation.getString("name"));   
                    measurementTypeVector.add(measurementTypes);
                }
                rsValidation.close();
            }
                            
            rs.close();
        }
        catch(SQLException e) {
            MiscUtils.getLogger().error("Error", e);
            verdict = false;
        }
        return verdict;
    }
    
    public boolean init(String groupName, String demo) {
        
        boolean verdict = true;
        try {
            
            String sqlMGr = "SELECT typeDisplayName FROM measurementGroup WHERE name='" + groupName + "'ORDER BY typeDisplayName";            
            ResultSet rsMGr;
 
            for(rsMGr = DBHandler.GetSQL(sqlMGr); rsMGr.next();){
                String typeDisplayName  = rsMGr.getString("typeDisplayName");
                String sqlMT = "SELECT * FROM measurementType WHERE typeDisplayName = '" + typeDisplayName + "'";                
                ResultSet rsMT;        
                for(rsMT = DBHandler.GetSQL(sqlMT); rsMT.next(); )
                {                
                    EctMeasuringInstructionBean mInstrc = new EctMeasuringInstructionBean(rsMT.getString("measuringInstruction"));                    
                    measuringInstrcVector.add(mInstrc);
                }
                rsMT.previous();
                
                //Get the data last entered for the current measurement type
                String sqlData = "SELECT * FROM measurements WHERE demographicNo='"+ demo + "' AND type ='" + rsMT.getString("type")
                                 + "' ORDER BY dateEntered DESC LIMIT 1";
                ResultSet rsData = DBHandler.GetSQL(sqlData);
                boolean hasPreviousData = false;
                if(rsData.next()){
                    String providerNo = rsData.getString("providerNo");
                    String sqlProvider = "SELECT * FROM provider WHERE provider_no='" + providerNo + "'";
                    ResultSet rsProvider = DBHandler.GetSQL(sqlProvider);
                    String pFname = "";
                    String pLname = "";
                    if(rsProvider.next()){
                        pFname = rsProvider.getString("first_name");
                        pLname = rsProvider.getString("last_name");

                    }
                    else if( providerNo.equals("0") ) {
                        ResourceBundle props = ResourceBundle.getBundle("oscarResources");
                        pFname = props.getString("oscarLab.System");
                        pLname = props.getString("oscarLab.System");
                    }
                    rsProvider.close();
                    EctMeasurementTypesBean measurementTypes = new EctMeasurementTypesBean( rsMT.getInt("id"), 
                                                                                        rsMT.getString("type"), 
                                                                                        rsMT.getString("typeDisplayName"), 
                                                                                        rsMT.getString("typeDescription"), 
                                                                                        rsMT.getString("measuringInstruction"), 
                                                                                        rsMT.getString("validation"),
                                                                                        pFname,
                                                                                        pLname,
                                                                                        rsData.getString("dataField"), 
                                                                                        rsData.getString("measuringInstruction"), 
                                                                                        rsData.getString("comments"), 
                                                                                        rsData.getString("dateObserved"), 
                                                                                        rsData.getString("dateEntered"));                        
                        
                    measurementTypeVector.add(measurementTypes);
                    EctMeasuringInstructionBeanHandler hd = new EctMeasuringInstructionBeanHandler(measuringInstrcVector);
                    measuringInstrcHdVector.add(hd);
                    measuringInstrcVectorVector.add(measuringInstrcVector);
                    measuringInstrcVector = new Vector();
                    hasPreviousData = true;
                    
                }
                rsData.close();
                if(!hasPreviousData){
                    EctMeasurementTypesBean measurementTypes = new EctMeasurementTypesBean( rsMT.getInt("id"), 
                                                                                        rsMT.getString("type"), 
                                                                                        rsMT.getString("typeDisplayName"), 
                                                                                        rsMT.getString("typeDescription"), 
                                                                                        rsMT.getString("measuringInstruction"), 
                                                                                        rsMT.getString("validation")); 
                    
                    measurementTypeVector.add(measurementTypes);   
                    EctMeasuringInstructionBeanHandler hd = new EctMeasuringInstructionBeanHandler(measuringInstrcVector);
                    measuringInstrcHdVector.add(hd);
                    measuringInstrcVectorVector.add(measuringInstrcVector);
                    measuringInstrcVector = new Vector();
                }
                
                rsMT.close();
                                                                                        
                
                
            }
            
            rsMGr.close();
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
    
    public Vector getMeasuringInstrcVector(){
        return measuringInstrcVector;
    }
    
    public Vector getMeasuringInstrcHdVector(){
        return measuringInstrcHdVector;
    }
    
    public Vector getMeasuringInstrcVectorVector(){
        return measuringInstrcVectorVector;
    }
    
    public Vector getMeasurementsDataVector(){
        return measurementsDataVector;
    }
}
