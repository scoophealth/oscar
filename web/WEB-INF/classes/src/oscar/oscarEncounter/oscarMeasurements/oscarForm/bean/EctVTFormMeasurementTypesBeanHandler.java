// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
// * This software is published under the GPL GNU General Public License. 
// * This program is free software; you can redistribute it and/or 
// * modify it under the terms of the GNU General Public License 
// * as published by the Free Software Foundation; either version 2 
// * of the License, or (at your option) any later version. * 
// * This program is distributed in the hope that it will be useful, 
// * but WITHOUT ANY WARRANTY; without even the implied warranty of 
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
// * along with this program; if not, write to the Free Software 
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
// * 
// * <OSCAR TEAM>
// * This software was written for the 
// * Department of Family Medicine 
// * McMaster Unviersity 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar.oscarEncounter.oscarMeasurements.oscarForm.bean;

import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import oscar.oscarDB.DBHandler;
import oscar.oscarEncounter.oscarMeasurements.bean.*;

public class EctVTFormMeasurementTypesBeanHandler {
    
    Vector measurementTypeVector = new Vector();    

    public EctVTFormMeasurementTypesBeanHandler(String demographicNo) {
        init(demographicNo);
    }
    
    public void init(String demographicNo) {       
        //Added all the data type and measuring instruction for the VT form; check measurementType table for existing data type
        measurementTypeVector.add(getMTBean("A1C", demographicNo)); //HbA1c
        measurementTypeVector.add(getMTBean("LDL", demographicNo)); //LDL
        measurementTypeVector.add(getMTBean("HDL", demographicNo)); //HDL
        measurementTypeVector.add(getMTBean("TCHL", demographicNo)); //Total Cholesterol
        measurementTypeVector.add(getMTBean("TRIG", demographicNo)); //Triglycerides
        measurementTypeVector.add(getMTBean("ALCR", demographicNo)); //Urinary Albumin Creatinine Ratio
        measurementTypeVector.add(getMTBean("24AL", demographicNo)); //24 hrs albumin
        measurementTypeVector.add(getMTBean("FTUL", demographicNo)); //Foot check: Ulcer
        measurementTypeVector.add(getMTBean("FTIS", demographicNo)); //Foot check: Ischemia
        measurementTypeVector.add(getMTBean("FTNE", demographicNo)); //Foot check: Neuropathy
        measurementTypeVector.add(getMTBean("EYED", demographicNo));  //Eye Exam: Diabetic Retinopathy
        measurementTypeVector.add(getMTBean("EYEH", demographicNo)); //Eye Exam: Hypertensive Retinopathy
        measurementTypeVector.add(getMTBean("VSPC", demographicNo)); //Vascular Specialist
        measurementTypeVector.add(getMTBean("BP", demographicNo)); //Blood pressure
        measurementTypeVector.add(getMTBean("WT", demographicNo)); //Weight
        measurementTypeVector.add(getMTBean("HT", demographicNo)); //Height
        measurementTypeVector.add(getMTBean("SMK", demographicNo)); //smoking
        measurementTypeVector.add(getMTBean("SMKP", demographicNo)); //number of pack of cigarette per day        
        measurementTypeVector.add(getMTBean("SMKC", demographicNo)); //number of cigarette per day
        measurementTypeVector.add(getMTBean("EXER", demographicNo)); //exercise
        
    }
    
        
    private EctMeasurementTypesBean getMTBean(String mType, String demo){
        EctMeasurementTypesBean measurementType = new EctMeasurementTypesBean(0,"","","","","");
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);

            String sql = "SELECT * FROM measurementType where type='" + mType + "' AND isDefault='1'";
            
            ResultSet rs = db.GetSQL(sql);        
            if(rs.next())
            {                
                //Get the data last entered for the current measurement type
                String sqlData ="select m.dataField, m.measuringInstruction, m.comments, m.dateObserved, m.dateEntered, p.last_name, " +
                                "p.first_name from measurements m, provider p where p.provider_no = m.providerNo AND m.type='" + mType +"' " +
                                "AND demographicNo='" + demo + "' ORDER BY dateEntered DESC LIMIT 1";

                ResultSet rsData = db.GetSQL(sqlData);         
                if(rsData.next()){           
                   System.out.println(mType + " has previous data");                   
                   measurementType = new EctMeasurementTypesBean(   rs.getInt("id"), 
                                                                    rs.getString("type"), 
                                                                    rs.getString("typeDisplayName"), 
                                                                    rs.getString("typeDescription"), 
                                                                    rs.getString("measuringInstruction"), 
                                                                    rs.getString("validation"),
                                                                    rsData.getString("first_name"), 
                                                                    rsData.getString("last_name"),
                                                                    rsData.getString("dataField"), 
                                                                    rsData.getString("measuringInstruction"), 
                                                                    rsData.getString("comments"), 
                                                                    rsData.getString("dateObserved"), 
                                                                    rsData.getString("dateEntered"));                   
                }                               
                else{                                            
                    measurementType = new EctMeasurementTypesBean( rs.getInt("id"), rs.getString("type"), 
                                                                       rs.getString("typeDisplayName"), 
                                                                       rs.getString("typeDescription"), 
                                                                       rs.getString("measuringInstruction"), 
                                                                       rs.getString("validation"));                    
                }
                rsData.close();
            }
            
            rs.close();            
            db.CloseConn();
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());            
        }
        return measurementType;
    }
    public Vector getMeasurementTypeVector(){
        return measurementTypeVector;
    }
        
}

