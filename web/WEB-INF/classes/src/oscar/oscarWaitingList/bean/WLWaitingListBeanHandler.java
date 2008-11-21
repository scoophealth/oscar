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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jfree.util.Log;

import oscar.oscarDB.DBHandler;
import oscar.oscarWaitingList.util.WLWaitingListUtil;

public class WLWaitingListBeanHandler {
    
    List waitingListArrayList = new ArrayList();
    String waitingListName ="";
    private static Logger log = Logger.getLogger(WLWaitingListBeanHandler.class);
    
    public WLWaitingListBeanHandler(String waitingListID) {
        init(waitingListID);
    }

    public boolean init(String waitingListID) {
        
        boolean verdict = true;
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            
            String sql = " SELECT CONCAT(d.last_name, ', ', d.first_name) AS patientName, d.demographic_no, " + 
            			 " d.phone, w.listID, w.position, w.note, w.onListSince FROM waitingList w, demographic d " + 
            			 " WHERE w.demographic_no = d.demographic_no AND w.listID='"+ waitingListID + "' " + 
            			 " AND w.is_history = 'N' " + " ORDER BY w.position ";
            log.debug(sql);
            ResultSet rs;      
            String onListSinceDateOnly = "";
            for(rs = db.GetSQL(sql); rs.next(); )
            {                
            	onListSinceDateOnly = db.getString(rs,"onListSince").substring(0, 10);//2007-01-01
            	
                WLPatientWaitingListBean wLBean = new WLPatientWaitingListBean( db.getString(rs,"demographic_no"),
                                                                                db.getString(rs,"listID"),
                                                                                db.getString(rs,"position"),
                                                                                db.getString(rs,"patientName"), 
                                                                                db.getString(rs,"phone"),
                                                                                db.getString(rs,"note"),
                                                                                onListSinceDateOnly);   
                waitingListArrayList.add(wLBean);
            }                            
            
            sql = "SELECT * FROM waitingListName where ID="+waitingListID + " AND is_history = 'N' ";
            log.debug(sql);
            rs = db.GetSQL(sql);
            if(rs.next()){
                waitingListName = db.getString(rs,"name");
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
    
    static public void updateWaitingList(String waitingListID) {
                
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = " SELECT demographic_no FROM waitingList WHERE listID=" + waitingListID + 
                         " AND is_history = 'N' ";
            log.debug(sql);
            ResultSet rs;
            boolean needUpdate = false;
            //go thru all the patient on the list
            for(rs = db.GetSQL(sql); rs.next();){
                
                //check if the patient has an appointment already
                sql = "select a.demographic_no, a.appointment_date, wl.onListSince from appointment a, waitingList wl where a.appointment_date >= wl.onListSince AND a.demographic_no=wl.demographic_no AND a.demographic_no="
                      + db.getString(rs,"demographic_no") + "";
                log.debug(sql);
                ResultSet rsCheck = db.GetSQL(sql);        
                
                if(rsCheck.next())
                {                
                    //delete patient from the waitingList
                    //System.out.println("patient to be deleted: " + db.getString(rs,"demographic_no"));
                	
                	WLWaitingListUtil.removeFromWaitingList(waitingListID, db.getString(rs,"demographic_no"));
                    needUpdate = true;
                }
                rsCheck.close();
            }
            rs.close();
            //update the list
            if(needUpdate){
                sql = " SELECT * FROM waitingList WHERE listID=" + waitingListID + "  AND is_history = 'N' ORDER BY onListSince";
                log.debug(sql);
                int i=1;            
                for(rs = db.GetSQL(sql); rs.next();){                    
                    sql =   " UPDATE waitingList SET position="+ i + 
                    		" WHERE listID=" + waitingListID + 
                            " AND demographic_no=" + db.getString(rs,"demographic_no") +
                            " AND is_history = 'N' ";
                    //System.out.println("update query from waiting list view: " + sql);
                    log.debug(sql);
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
        
    
    
    public List getWaitingListArrayList(){
        return waitingListArrayList;
    }    
    
    public String getWaitingListName(){
        return waitingListName;
    }
}

