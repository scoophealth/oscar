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
// * McMaster University 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar.oscarWaitingList.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import oscar.oscarDB.DBHandler;

public class WLWaitingListUtil {
    //Modified this method in Feb 2007 to ensure that all records cannot be deleted except hidden.    
    static public synchronized void  removeFromWaitingList(String waitingListID, String demographicNo) {
        System.out.println("WLWaitingListUtil.removeFromWaitingList(): removing waiting list: " + waitingListID + " for patient " + demographicNo);
        DBHandler db = null;
        try{
            db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql;
	        sql = " update  waitingList set is_history = 'Y' " + 
		          " where demographic_no = " + demographicNo + 
		          " and   listID = " + waitingListID;
	        
            db.RunSQL(sql);  
            //update the waiting list positions           
            rePositionWaitingList(db, waitingListID);
            
        }catch(SQLException e) {
            System.out.println("WLWaitingListUtil.removeFromWaitingList():" + e.getMessage());         
	    }finally{
	    	try{
	    	}catch(Exception ex2){
	        	System.out.println("WLWaitingListUtil.rePositionWaitingList(1):" + ex2.getMessage()); 
	    	}
	    }
        
    } 
    
        static public synchronized void add2WaitingList(
        		String waitingListID, String waitingListNote, String demographicNo, String onListSince) {
            
        DBHandler db = null;
        ResultSet rs = null;
        System.out.println("WLWaitingListUtil.add2WaitingList(): insert into waitingList: " + waitingListID + " for patient " + demographicNo);
        if(!waitingListID.equalsIgnoreCase("0")&&!demographicNo.equalsIgnoreCase("0")){
            try{
                waitingListNote = org.apache.commons.lang.StringEscapeUtils.escapeSql(waitingListNote);

                db = new DBHandler(DBHandler.OSCAR_DATA);
                String sql = " select max(position) as position from waitingList where listID=" + waitingListID + 
                			 "  AND is_history = 'N' ";
                rs = db.GetSQL(sql);
                String nxPos = "1";
                if(rs.next()){
                	nxPos = Integer.toString(rs.getInt("position") + 1);
                }
                System.out.println("WLWaitingListUtil.add2WaitingList(): position = " + nxPos);
                
                if(onListSince == null  ||  onListSince.length() <= 0){
	                sql = " insert into waitingList " + 
		          	  " (listID, demographic_no, note, position, onListSince, is_history) " +	
		          	  " values("+ waitingListID + "," + demographicNo + ",'" + 
		          	  waitingListNote + "'," + nxPos + ", now() , 'N')";
                }else{
	                sql = " insert into waitingList " + 
		          	  " (listID, demographic_no, note, position, onListSince, is_history) " +	
		          	  " values("+ waitingListID + "," + demographicNo + ",'" + 
		          	  waitingListNote + "'," + nxPos + ",'" + onListSince + "', 'N')";
                }
                System.out.println("WLWaitingListUtil.add2WaitingList(): insert sql = " + sql);

                db.RunSQL(sql);

		        //update the waiting list positions
		        rePositionWaitingList(db, waitingListID);
                
            }
            catch(SQLException e) {
                System.out.println("WLWaitingListUtil.add2WaitingList():" + e.getMessage());         
	        }finally{
	        	try{
		            rs.close();
	        	}catch(Exception ex2){
	            	System.out.println("WLWaitingListUtil.rePositionWaitingList(1):" + ex2.getMessage()); 
	        	}
	        }
        }
    }
        
    /*
     * This method adds the Waiting List note to the same position in the waitingList table but
     * do not delete previous ones - later on DisplayWaitingList.jsp will display only the most
     * current Waiting List Note record.
     */
	static public synchronized void updateWaitingListRecord(String waitingListID, String waitingListNote, 
			String demographicNo, String onListSince) {
	    
	    System.out.println("WLWaitingListUtil.updateWaitingListRecord(): waitingListID: " + waitingListID + " for patient " + demographicNo);
	    DBHandler db = null;
	    ResultSet rs = null;
	    if(!waitingListID.equalsIgnoreCase("0")&&!demographicNo.equalsIgnoreCase("0")){
		    try{
		        db = new DBHandler(DBHandler.OSCAR_DATA);
	             
	            int pos = 1;
	            String sql = " SELECT * FROM waitingList " + 
				             " where demographic_no = " + demographicNo + 
				             " and   listID = " + waitingListID + 
					         " AND is_history = 'N' ";
		        if(db == null){
		        	System.out.println("WLWaitingListUtil.updateWaitingListRecord(): dbHandler == null");    
		        	return;
		        }
	            rs = db.GetSQL(sql); 
	            if(rs == null){
		        	System.out.println("WLWaitingListUtil.updateWaitingListRecord(): result set == null");    
		        	return;
	            }
	            while(rs.next()){
	            	pos = rs.getInt("position");
	            }
		        
		        waitingListNote = org.apache.commons.lang.StringEscapeUtils.escapeSql(waitingListNote);
		        
	        
		        //set all previous records 'is_history' fielf to 'N' --> to keep as record but never display
		        sql = " update  waitingList set is_history = 'Y' " + 
		                     " where demographic_no = " + demographicNo + 
		                     " and   listID = " + waitingListID;
	
	            db.RunSQL(sql);
	            System.out.println("WLWaitingListUtil.updateWaitingListRecord(): update sql = " + sql);
	            
                sql = " insert into waitingList " + 
		          	  " (listID, demographic_no, note, position, onListSince, is_history) " +	
		          	  " values("+ waitingListID + "," + demographicNo + ",'" + 
		          	  waitingListNote + "'," + pos + ", '" + onListSince + "', 'N')";
	
		        System.out.println("WLWaitingListUtil.updateWaitingListRecord(): insert sql = " + sql);
		        db.RunSQL(sql);
		        
		        //update the waiting list positions
		        rePositionWaitingList(db, waitingListID);
	            
	        }
	
	        catch(SQLException e) {
	            System.out.println("WLWaitingListUtil.updateWaitingListRecord():" + e.getMessage());         
	        }finally{
	        	try{
		            rs.close();
	        	}catch(Exception ex2){
	            	System.out.println("WLWaitingListUtil.updateWaitingListRecord(1):" + ex2.getMessage()); 
	        	}
	        }
	    }
	}//end of updateWaitingListRecord()
	
	
    /*
     * This method adds the Waiting List note to the same position in the waitingList table but
     * do not delete previous ones - later on DisplayWaitingList.jsp will display only the most
     * current Waiting List Note record.
     */
	static public synchronized void updateWaitingList(String id, String waitingListID, String waitingListNote, 
			String demographicNo, String onListSince) {
	    
	    System.out.println("WLWaitingListUtil.updateWaitingList(): waitingListID: " + waitingListID + " for patient " + demographicNo);
	    DBHandler db = null;
	    ResultSet rs = null;
	    if(!waitingListID.equalsIgnoreCase("0")&&!demographicNo.equalsIgnoreCase("0")){
		    try{
		        db = new DBHandler(DBHandler.OSCAR_DATA);
		        String sql = "";
		        if(db == null){
		        	System.out.println("WLWaitingListUtil.updateWaitingList(): dbHandler == null");    
		        	return;
		        }
		        
		        waitingListNote = org.apache.commons.lang.StringEscapeUtils.escapeSql(waitingListNote);
		        
		        if(id != null  &&  !id.equals("")){
		        	
			        sql = " update  waitingList set listID = " + waitingListID + ", " +
		                  " note = '" + waitingListNote + "', " + 
		                  " onListSince = '" + onListSince + "' " +
		                  " where id=" + id;
		            System.out.println("WLWaitingListUtil.updateWaitingList(): update sql = " + sql);

			        db.RunSQL(sql);
		        	
		        }
	        }
	
	        catch(SQLException e) {
	            System.out.println("WLWaitingListUtil.updateWaitingList():" + e.getMessage());         
	        }finally{
	        	try{
		            rs.close();
	        	}catch(Exception ex2){
	            	System.out.println("WLWaitingListUtil.updateWaitingList(1):" + ex2.getMessage()); 
	        	}
	        }
	    }
	}//end of updateWaitingListRecord()
	
	
	static private void rePositionWaitingList(DBHandler db, String waitingListID){
		
        int i=1;  
        String sql = "";
        ResultSet rs = null;
        try{
            sql = " SELECT * FROM waitingList WHERE listID=" + waitingListID + " AND is_history='N' ORDER BY onListSince";
            rs = db.GetSQL(sql);
            
            while( rs.next()){     
            	
                sql =   " UPDATE waitingList SET position="+ i + 
                		" WHERE listID=" + waitingListID + 
                        " AND demographic_no=" + db.getString(rs,"demographic_no") + 
                        " AND is_history = 'N' ";
                System.out.println("WLWaitingListUtil.rePositionWaitingList(2): " + sql);
                db.RunSQL(sql);
                i++;
            }   
            
        }catch(SQLException sqlex){
        	System.out.println("WLWaitingListUtil.rePositionWaitingList(2):" + sqlex.getMessage()); 
        }catch(Exception ex){
        	System.out.println("WLWaitingListUtil.rePositionWaitingList(2):" + ex.getMessage()); 
        }finally{
        	try{
	            rs.close(); 
        	}catch(Exception ex2){
            	System.out.println("WLWaitingListUtil.rePositionWaitingList():" + ex2.getMessage()); 
        	}
        }
	}
        
	static public synchronized void rePositionWaitingList(String waitingListID){
		
        int i=1;  
        String sql = "";
        ResultSet rs = null;
        DBHandler db = null;
        try{
            db = new DBHandler(DBHandler.OSCAR_DATA);
        	
            sql = " SELECT * FROM waitingList WHERE listID=" + waitingListID + " AND is_history='N' ORDER BY onListSince";
            rs = db.GetSQL(sql);
            
            while( rs.next()){     
            	
                sql =   " UPDATE waitingList SET position="+ i + 
                		" WHERE listID=" + waitingListID + 
                        " AND demographic_no=" + db.getString(rs,"demographic_no") + 
                        " AND is_history = 'N' ";
                System.out.println("WLWaitingListUtil.rePositionWaitingList(1): " + sql);
                db.RunSQL(sql);
                i++;
            }   
        }catch(SQLException sqlex){
        	System.out.println("WLWaitingListUtil.rePositionWaitingList(1):" + sqlex.getMessage()); 
        }catch(Exception ex){
        	System.out.println("WLWaitingListUtil.rePositionWaitingList(1):" + ex.getMessage()); 
        }finally{
        	try{
	            rs.close();
        	}catch(Exception ex2){
            	System.out.println("WLWaitingListUtil.rePositionWaitingList(1):" + ex2.getMessage()); 
        	}
        }
	}
	
}//end of class WLWaitingListUtil
