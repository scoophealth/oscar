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
// *
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

public class WLPatientWaitingListBeanHandler {
    
    Vector patientWaitingListVector = new Vector();
    
    public WLPatientWaitingListBeanHandler(String demographicNo) {
        init(demographicNo);
    }

    public boolean init(String demographicNo) {
        
        boolean verdict = true;
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            
            updateWaitingList(demographicNo);
            
            String sql = "SELECT wn.name, w.position, w.note, w.onListSince FROM waitingListName wn, waitingList w WHERE wn.ID = w.ListID AND demographic_no ='"+ demographicNo + "'";
            ResultSet rs;        
            for(rs = db.GetSQL(sql); rs.next(); )
            {                
                WLPatientWaitingListBean wLBean = new WLPatientWaitingListBean( demographicNo,
                                                                                rs.getString("name"),
                                                                                rs.getString("position"), 
                                                                                rs.getString("note"),
                                                                                rs.getString("onListSince"));   
                patientWaitingListVector.add(wLBean);
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
        
        static public void updateWaitingList(String demographicNo) {
                
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);

            String sql = "select a.demographic_no, a.appointment_date, wl.onListSince from appointment a, waitingList wl where a.appointment_date >= wl.onListSince AND a.demographic_no=wl.demographic_no AND a.demographic_no='"
                          + demographicNo + "'";
            ResultSet rs = db.GetSQL(sql);        
            if(rs.next())
            {                
                sql = "SELECT DISTINCT listID FROM waitingList WHERE demographic_no='" + demographicNo + "'";
                ResultSet rsWL;
                for(rsWL = db.GetSQL(sql); rsWL.next(); ){
                    sql = "DELETE FROM waitingList WHERE demographic_no='"+demographicNo +"' AND listID='" + rsWL.getString("listID") +"'";
                    db.RunSQL(sql);
                    sql = "update waitingList set position = position -1 where listID='" + rsWL.getString("listID") +"'";
                    db.RunSQL(sql);
                }
                rsWL.close();
            }
                            
            rs.close();            
            db.CloseConn();
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());         
        }        
    }       
    
    public Vector getPatientWaitingListVector(){
        return patientWaitingListVector;
    }    
}

