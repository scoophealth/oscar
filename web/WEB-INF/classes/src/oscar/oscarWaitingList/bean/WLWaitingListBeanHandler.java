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
            
            //String sql = "SELECT DISTINCT demographic_no FROM waitingList WHERE listID='" + waitingListID + "'";
            //ResultSet rs;
            //for(rs = db.GetSQL(sql); rs.next();){
            //    WLPatientWaitingListBeanHandler.updateWaitingList(rs.getString("demographic_no"));
            //}
            //updateWaitingList(waitingListID);

            String sql = "SELECT CONCAT(d.last_name, ', ', d.first_name) AS patientName, d.demographic_no, d.phone, w.position, w.note, w.onListSince FROM waitingList w, demographic d WHERE w.demographic_no = d.demographic_no AND w.listID='"+ waitingListID + "' ORDER BY w.position";
            ResultSet rs;        
            for(rs = db.GetSQL(sql); rs.next(); )
            {                
                WLPatientWaitingListBean wLBean = new WLPatientWaitingListBean( rs.getString("demographic_no"),
                                                                                rs.getString("position"),
                                                                                rs.getString("patientName"), 
                                                                                rs.getString("phone"),
                                                                                rs.getString("note"),
                                                                                rs.getString("onListSince"));   
                //System.out.println("positing: " + rs.getString("position"));
		//System.out.println("patientName: " + rs.getString("patientName"));
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
    
    static public void updateWaitingList(String waitingListID) {
                
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "SELECT demographic_no FROM waitingList WHERE listID='" + waitingListID + "'";
            ResultSet rs;
            boolean needUpdate = false;
            //go thru all the patient on the list
            for(rs = db.GetSQL(sql); rs.next();){
                
                //check if the patient has an appointment already
                sql = "select a.demographic_no, a.appointment_date, wl.onListSince from appointment a, waitingList wl where a.appointment_date >= wl.onListSince AND a.demographic_no=wl.demographic_no AND a.demographic_no='"
                      + rs.getString("demographic_no") + "'";
                
                ResultSet rsCheck = db.GetSQL(sql);        
                
                if(rsCheck.next())
                {                
                    //delete patient from the waitinglist
                    //System.out.println("patient to be deleted: " + rs.getString("demographic_no"));
                    sql = "DELETE FROM waitingList WHERE demographic_no='"+ rs.getString("demographic_no") +"' AND listID='" + waitingListID +"'";
                    db.RunSQL(sql);    
                    needUpdate = true;
                }
                rsCheck.close();
            }
            rs.close();
            //update the list
            if(needUpdate){
                sql = "SELECT * FROM waitingList WHERE listID='" + waitingListID + "' ORDER BY onListSince";
                int i=1;            
                for(rs = db.GetSQL(sql); rs.next();){                    
                    sql =   "UPDATE waitingList SET position ='"+ Integer.toString(i) + "' WHERE listID='" + waitingListID 
                            +"' AND demographic_no='" + rs.getString("demographic_no") +"'";
                    //System.out.println("update query from waiting list view: " + sql);
                    db.RunSQL(sql);
                    i++;
                }                            
                rs.close();
            }
            db.CloseConn();
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());         
        }        
    } 
        
    
    
    public Vector getWaitingListVector(){
        return waitingListVector;
    }    
    
    public String getWaitingListName(){
        return waitingListName;
    }
}

