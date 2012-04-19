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


package oscar.oscarWorkflow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.MiscUtils;

/**
 *
 * @author jay
 *
 *
 TODO: STill need to add the indexs to this table
  create table workflow(
   ID   int(10) NOT NULL auto_increment primary key,
   provider_no varchar(6),
   workflow_type   varchar(30),
   create_date_time  datetime,
   demographic_no int (10),
   completion_date datetime,
   current_state  char(1)
  )

 */
public class WorkFlowState {
    public final static String RHWORKFLOW = "RH";
    public final static String INIT_STATE = "1";
    
    /** Creates a new instance of WorkFlowState */
    public WorkFlowState() {
    }
    //TODO: need to add which provider added it  OR i could just logg it as well
    public int addToWorkFlow(String workflowType,String providerNo, String demographicNo, Date endDate,String current_state){
        int id = -1;
        try {
            String s = "insert into workflow (workflow_type,provider_no, demographic_no, completion_date,current_state,create_date_time) values (?,?,?,?,?,now())" ;
            Connection  conn = DbConnectionFilter.getThreadLocalDbConnection();
            PreparedStatement pstmt = conn.prepareStatement(s);
            PreparedStatement lastInsert = conn.prepareStatement("SELECT LAST_INSERT_ID()");
            pstmt.setString(1,workflowType);
            pstmt.setString(2,providerNo);
            pstmt.setString(3,demographicNo);
            java.sql.Date compDate = null;
            try{
                compDate = new java.sql.Date(endDate.getTime());
            }catch(Exception e){}
            pstmt.setDate(4,compDate);
            pstmt.setString(5,current_state);
            pstmt.executeUpdate();
            pstmt.close();
            
            ResultSet rs = lastInsert.executeQuery();
            MiscUtils.getLogger().debug("CALLED LAST _INSERT");
            if(rs.next()){
                id = rs.getInt(0);
                MiscUtils.getLogger().debug("WAS DATA"+id);
            }
            
        }catch(Exception e){
            MiscUtils.getLogger().error("Error", e);
        }
        
        return id;
    }
    
    public void updateWorkFlowState(String workflowId,String state ){
        try {
            String s = "update workflow set current_state = ? where ID = ?" ;
            PreparedStatement pstmt = DbConnectionFilter.getThreadLocalDbConnection().prepareStatement(s);
            pstmt.setString(1,state);
            pstmt.setString(2,workflowId);
            pstmt.executeUpdate();
            pstmt.close();
        }catch(Exception e){
            MiscUtils.getLogger().error("Error", e);
        }
    }
    
    public void updateWorkFlowState(String workflowId,String state, Date date ){
        try {
            String s = "update workflow set current_state = ?, completion_date = ? where ID = ?" ;
            PreparedStatement pstmt = DbConnectionFilter.getThreadLocalDbConnection().prepareStatement(s);
            pstmt.setString(1,state);
            pstmt.setDate(2, new java.sql.Date( date.getTime() ) );        
            pstmt.setString(3,workflowId);
            pstmt.executeUpdate();
            pstmt.close();
        }catch(Exception e){
            MiscUtils.getLogger().error("Error", e);
        }
    }
    
    
    public ArrayList getWorkFlowList(String workflowType){
        ArrayList list = new ArrayList();
        try {
            String s = "select * from workflow where workflow_type = ?" ;
            PreparedStatement pstmt = DbConnectionFilter.getThreadLocalDbConnection().prepareStatement(s);
            pstmt.setString(1,workflowType);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()){
                Hashtable h = fillHash(rs);
                list.add(h);
            }
            
            rs.close();
            pstmt.close();
            
        }catch(Exception e){
            MiscUtils.getLogger().error("Error", e);
        }
        return list;
    }
    
    public ArrayList getActiveWorkFlowList(String workflowType){
        ArrayList list = new ArrayList();
        try {
            String s = "select * from workflow where workflow_type = ? and current_state != 'C'" ;
            PreparedStatement pstmt = DbConnectionFilter.getThreadLocalDbConnection().prepareStatement(s);
            pstmt.setString(1,workflowType);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()){
                Hashtable h = fillHash(rs);
                list.add(h);
            }
            
            rs.close();
            pstmt.close();
            
        }catch(Exception e){
            MiscUtils.getLogger().error("Error", e);
        }
        return list;
    }

    private Hashtable fillHash(final ResultSet rs) throws SQLException {
        Hashtable h = new Hashtable();
        h.put("ID",oscar.Misc.getString(rs,"ID"));
        h.put("workflow_type", oscar.Misc.getString(rs,"workflow_type"));
        h.put("create_date_time", rs.getDate("create_date_time"));
        h.put("demographic_no", oscar.Misc.getString(rs,"demographic_no"));
        if (rs.getDate("completion_date") != null){
           h.put("completion_date", rs.getDate("completion_date"));
        }
        h.put("current_state", oscar.Misc.getString(rs,"current_state"));
        return h;
    }
    
    public ArrayList getActiveWorkFlowList(String workflowType, String demographicNo){
        ArrayList list = new ArrayList();
        try {
            String s = "select * from workflow where workflow_type = ? and demographic_no = ? and current_state != 'C'" ;
            MiscUtils.getLogger().debug("workflow type "+workflowType+" demo "+demographicNo);
            PreparedStatement pstmt = DbConnectionFilter.getThreadLocalDbConnection().prepareStatement(s);
            pstmt.setString(1,workflowType);
            pstmt.setString(2,demographicNo);   
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()){
                Hashtable h = fillHash(rs);
                list.add(h);
            }
            
            rs.close();
            pstmt.close();
            
        }catch(Exception e){
            MiscUtils.getLogger().error("Error", e);
        }
        return list;
    }
    
    
    public static String rhState(Object s){
         Hashtable h = new Hashtable();
         h.put("1","No Appt made");
         h.put("2","Appt Booked");
         h.put("3","Injection 28");
         h.put("4","Missed Appt");
         h.put("C","Closed");
         String ss = (String) s;
         return (String) h.get(ss);
    }
}
