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
// * Date         Implemented By  Company                 Comments
// * 29-09-2004   Ivy Chan        iConcept Technologies   initial version
// * This software was written for the 
// * Department of Family Medicine 
// * McMaster Unviersity 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar.oscarWaitingList.util;

import java.io.*;
import java.util.*;
import java.lang.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.commons.lang.StringEscapeUtils;
import oscar.oscarDB.DBHandler;
import oscar.OscarProperties;
import oscar.oscarWaitingList.bean.*;
import oscar.oscarProvider.bean.*;
import oscar.util.*;

public class WLWaitingListUtil {
        
    static public void removeFromWaitingList(String waitingListID, String demographicNo) {
        System.out.println("removing waiting list: " + waitingListID + " for patient " + demographicNo);
        try{
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql;
            ResultSet rs;
            
            
            sql = "DELETE FROM waitingList WHERE demographic_no='"+ demographicNo +"' AND listID='" + waitingListID +"'";
            db.RunSQL(sql);                
            
            
            //update the list            
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
            db.CloseConn();
        }
    
        catch(SQLException e) {
            System.out.println(e.getMessage());         
        }        
    } 
    
        static public void add2WaitingList(String waitingListID, String waitingListNote, String demographicNo) {
            
            //String[] paramWLPosition = new String[1];
           // paramWLPosition[0] = request.getParameter("list_id");
            //if(paramWLPosition[0].compareTo("")!=0){
                //ResultSet rsWL = apptMainBean.queryResults(paramWLPosition, "search_waitingListPosition");
                /*if(rsWL.next()){
                    String[] paramWL = new String[4];
                    paramWL[0] = request.getParameter("list_id");
                    paramWL[1] = request.getParameter("demographic_no");
                    paramWL[2] = request.getParameter("waiting_list_note");
                    //System.out.println("max position: " + Integer.toString(rsWL.getInt("position")));
                    paramWL[3] = Integer.toString(rsWL.getInt("position") + 1);
                    apptMainBean.queryExecuteUpdate(paramWL, "add2waitinglist");
                }
            }*/
        System.out.println("update waiting list: " + waitingListID + " for patient " + demographicNo);
        if(!waitingListID.equalsIgnoreCase("0")&&!demographicNo.equalsIgnoreCase("0")){
            try{
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                String sql = "select max(position) as position from waitingList where listID='" + waitingListID + "'";
                ResultSet rs = db.GetSQL(sql);

                if(rs.next()){
                    String nxPos = Integer.toString(rs.getInt("position") + 1);
                    waitingListNote = org.apache.commons.lang.StringEscapeUtils.escapeSql(waitingListNote);
                    sql = "insert into waitingList values('"+ waitingListID + "','"
                                                            + demographicNo + "','"
                                                            + waitingListNote + "','"
                                                            + nxPos + "', now())";
                    db.RunSQL(sql);
                }
                rs.close();            
                db.CloseConn();
            }

            catch(SQLException e) {
                System.out.println(e.getMessage());         
            }        
        }
    }
}
