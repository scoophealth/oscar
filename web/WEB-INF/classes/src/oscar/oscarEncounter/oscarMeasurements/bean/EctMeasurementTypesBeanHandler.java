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
package oscar.oscarEncounter.oscarMeasurements.pageUtil;

import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import oscar.oscarDB.DBHandler;

public class EctMeasurementTypesBeanHandler {
    
    Vector measurementTypeVector = new Vector();
    Vector measuringInstrcVector = new Vector();
    Vector measuringInstrcVectorVector = new Vector();
    Vector typeVector = new Vector();

    public EctMeasurementTypesBeanHandler() {
        init();
    }
    public EctMeasurementTypesBeanHandler(String groupName) {
        init(groupName);
    }

    public boolean init() {
        
        boolean verdict = true;
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);

            String sql = "SELECT * FROM measurementType ORDER BY type";              
            ResultSet rs;        
            for(rs = db.GetSQL(sql); rs.next(); )
            {                
                String validation = rs.getString("validation");
                String sqlValidation = "SELECT name FROM validations WHERE id='" + validation + "'";
                ResultSet rsValidation = db.GetSQL(sqlValidation);
                if (rsValidation.next()){
                    EctMeasurementTypesBean measurementTypes = new EctMeasurementTypesBean(rs.getInt("id"), rs.getString("type"), 
                                                                                           rs.getString("typeDisplayName"), 
                                                                                           rs.getString("typeDescription"), 
                                                                                           rs.getString("measuringInstruction"), 
                                                                                           rsValidation.getString("name"));   
                    measurementTypeVector.add(measurementTypes);
                }
            }
                            
            rs.close();            
            db.CloseConn();
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
            verdict = false;
        }
        return verdict;
    }
    
    public boolean init(String groupName) {
        
        boolean verdict = true;
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sqlMGr = "SELECT typeDisplayName FROM measurementGroup WHERE name='" + groupName + "'ORDER BY typeDisplayName";            
            ResultSet rsMGr;
 
            for(rsMGr = db.GetSQL(sqlMGr); rsMGr.next();){
                String typeDisplayName  = rsMGr.getString("typeDisplayName");
                String sqlMT = "SELECT * FROM measurementType WHERE typeDisplayName = '" + typeDisplayName + "'";
                System.out.println("SQLMT: " + sqlMT);
                ResultSet rsMT;        
                for(rsMT = db.GetSQL(sqlMT); rsMT.next(); )
                {                
                    EctMeasuringInstructionBean mInstrc = new EctMeasuringInstructionBean(rsMT.getString("measuringInstruction"));
                    System.out.println("MeasuringInstruction: " + rsMT.getString("measuringInstruction"));
                    measuringInstrcVector.add(mInstrc);
                }
                rsMT.previous();
                EctMeasurementTypesBean measurementTypes = new EctMeasurementTypesBean(rsMT.getInt("id"), rsMT.getString("type"), rsMT.getString("typeDisplayName"), rsMT.getString("typeDescription"), rsMT.getString("measuringInstruction"), rsMT.getString("validation"));
                System.out.println(rsMT.getString("type"));
                measurementTypeVector.add(measurementTypes);
                System.out.println("Add the instructions vector to the big Vector");
                measuringInstrcVectorVector.add(measuringInstrcVector);
                measuringInstrcVector = new Vector();
                
            }
            
            rsMGr.close();            
            db.CloseConn();
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
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
    
    public Vector getMeasuringInstrcVectorVector(){
        return measuringInstrcVectorVector;
    }
}

