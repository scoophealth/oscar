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
// *
// * Date         Implemented By  Company                 Comments
// * 29-09-2004   Ivy Chan        iConcept Technologies   initial version
// * This software was written for the 
// * Department of Family Medicine 
// * McMaster Unviersity 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar.oscarWaitingList.bean;

import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import oscar.oscarDB.DBHandler;

public class WLWaitingListBeanHandler {
    
    Vector waitingListVector = new Vector();
    String waitingListName ="";
    
    public WLWaitingListBeanHandler(String waitingListID) {
        init(waitingListID);
    }

    public boolean init(String waitingListID) {
        
        boolean verdict = true;
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            
            String sql = "SELECT DISTINCT demographic_no FROM waitingList";
            ResultSet rs;
            for(rs = db.GetSQL(sql); rs.next();){
                WLPatientWaitingListBeanHandler.updateWaitingList(rs.getString("demographic_no"));
            }
            

            sql = "SELECT CONCAT(d.last_name, ', ', d.first_name) AS patientName, d.phone, w.position, w.note, w.onListSince FROM waitingList w, demographic d WHERE w.demographic_no = d.demographic_no AND w.listID='"+ waitingListID + "'";
                    
            for(rs = db.GetSQL(sql); rs.next(); )
            {                
                WLPatientWaitingListBean wLBean = new WLPatientWaitingListBean( rs.getString("position"),
                                                                                rs.getString("patientName"), 
                                                                                rs.getString("phone"),
                                                                                rs.getString("note"),
                                                                                rs.getString("onListSince"));   
                System.out.println("positing: " + rs.getString("position"));
		System.out.println("patientName: " + rs.getString("patientName"));
                waitingListVector.add(wLBean);
            }                            
            rs.close();        
            
            sql = "SELECT * FROM waitingListName where ID='"+waitingListID+"'";
            rs = db.GetSQL(sql);
            if(rs.next()){
                waitingListName = rs.getString("name");
            }
            
            db.CloseConn();
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
            verdict = false;
        }
        return verdict;
    }
        
    public Vector getWaitingListVector(){
        return waitingListVector;
    }    
    
    public String getWaitingListName(){
        return waitingListName;
    }
}

