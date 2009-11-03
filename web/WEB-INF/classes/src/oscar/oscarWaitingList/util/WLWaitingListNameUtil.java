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
// * 06-02-2007           iConcept Technologies   initial version
// * This software was written for the 
// * Department of Family Medicine 
// * McMaster Unviersity 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar.oscarWaitingList.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import oscar.oscarDB.DBHandler;

public class WLWaitingListNameUtil {
        
    static public void removeFromWaitingListName(String wlNameId, String groupNo) 
    throws SQLException, Exception {
		if( wlNameId == null  ||  groupNo == null   ){
			System.out.println("WLWaitingListNameUtil/removeFromWaitingListName(): wlName or groupNo is null"); 
			return;
		}
        System.out.println("WLWaitingListNameUtil/removeFromWaitingListName(): waiting list name: " + wlNameId + 
        		           " for groupNo " + groupNo);
        DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
        ResultSet rs = null;
        String sql;
        if(db == null){
        	System.out.println("WLWaitingListNameUtil/removeFromWaitingListName(): dbHandler == null");    
        	throw new Exception("systemError");
        }

        boolean isUsed = isWaitingListNameBeingUsed( db, rs, wlNameId );
        if(isUsed){
            System.out.println("WLWaitingListNameUtil/removeFromWaitingListName(): Waiting list name is being used.");
            throw new Exception("wlNameUsed");
        }
        
        //update the list and set is_history = 'Y'          
        sql = " UPDATE waitingListName SET is_history ='Y' " + 
        	  " WHERE ID=" + wlNameId +
              " AND group_no='" + groupNo +"'";
        System.out.println("remove waiting list name sql: " + sql);

        db.RunSQL(sql);
        if(rs != null){
        	rs.close();
        }
        return;
    } 
    
    static public void createWaitingListName(String wlName, String groupNo, String providerNo)
    throws SQLException, Exception {
            
		if( wlName == null  ||  groupNo == null  ||
			wlName.trim().length() <= 0  ||  groupNo.trim().length() <= 0){
			System.out.println("WLWaitingListNameUtil/createWaitingListName(): " + 
					           " wlName or groupNo or providerNo is null"); 
			return;
		}
        System.out.println( "WLWaitingListNameUtil/createWaitingListName(): waiting list name: " + wlName + 
        					" for groupNo: " + groupNo + "/ providerNo: " + providerNo);
        
        DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
        ResultSet rs = null;
        
        wlName = org.apache.commons.lang.StringEscapeUtils.escapeSql(wlName);
        
        rs = getWaitingListNameRecords( db, rs, wlName, groupNo );
        boolean isExist = isWaitingListNameExist( rs );
        
        if(isExist){
        	System.out.println("WLWaitingListNameUtil/createWaitingListName(): The WL name already exists.");
        	throw new Exception("wlNameExists");
        }
        
    	String sql = " insert into waitingListName " + 
                	 " ( name, group_no, provider_no, create_date, is_history ) " +	
                	 " values('" + wlName + "','" + groupNo + "','" + providerNo + "'," + 
                	 "  now(), 'N')";
    
        System.out.println("WLWaitingListNameUtil/createWaitingListName(): sql = " + sql);
        
        db.RunSQL(sql);
        if(rs != null){
        	rs.close();
        }
        return;
    }
        
    /*
     * This method adds the Waiting List note to the same position in the waitingListName table but
     * do not delete previous ones - later on EditWaitingListName.jsp will display only the most
     * current Waiting List Note record.
     * 
     * This update involves 8 calls to 2 separate tables and must be able to roll back if any of those calls failed !!!
     * Currently it seems rather hard to implement the roll back function due to using existing DBHandler, so using the
     * alternate synchronized feature for the 2 methods involved instead ... 
     */
	static public void updateWaitingListName(String wlNameId, String wlName, String groupNo, String providerNo)
	throws SQLException, Exception {
	    
		if( wlNameId == null  || wlName == null  ||  groupNo == null  ||  providerNo == null  ||
			wlNameId.equalsIgnoreCase("0")  || 	wlName.length() <= 0  ||  groupNo.length() <= 0 ){
			System.out.println("WLWaitingListNameUtil/updateWaitingListName(): " + 
					           " wlNameId or wlName or groupNo or providerNo is null"); 
			return;
		}

        System.out.println("WLWaitingListNameUtil/updateWaitingListName(): wlNameId/wlName = " + 
        		wlNameId + "/" + wlName);
	    
        DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
        ResultSet rs = null;

        if(db == null){
        	System.out.println("WLWaitingListNameUtil/updateWaitingListName(): dbHandler == null");    
        	throw new Exception("systemError");
        }

        wlName = org.apache.commons.lang.StringEscapeUtils.escapeSql(wlName);
        
        rs = getWaitingListNameRecords( db, rs, wlName, groupNo );
        boolean isExist = isWaitingListNameExist( rs );
        
        if(isExist){
        	System.out.println("WLWaitingListNameUtil/createWaitingListName(): The WL name already exists.");
        	throw new Exception("wlNameExists");
        }
        
        String sql = " UPDATE waitingListName " + 
          	  " SET name = '" + wlName + "' " +
          	  " WHERE  ID=" + wlNameId;

        System.out.println("WLWaitingListNameUtil/updateWaitingListName(): sql = " + sql);

        db.RunSQL(sql);
        if(rs != null){
        	rs.close();
        }
        return;
	}


	
	static private boolean isWaitingListNameBeingUsed( DBHandler db, ResultSet rs, String wlNameId ) throws SQLException{
		
		if( db == null  ||  wlNameId == null ){
			System.out.println("WLWaitingListNameUtil/isWaitingListNameBeingUsed(): db or rs or wlNameId is null"); 
			return true;
		}
        String sql = " SELECT ID FROM waitingList " + 
		             " WHERE listID = " + wlNameId +
		             " AND is_history='N'";
			        
        rs = db.GetSQL(sql); 
        if(rs == null){
        	System.out.println("WLWaitingListNameUtil/isWaitingListNameBeingUsed(): result set == null");    
        	return true;
        }
        if(rs.next()){
	       	return true;
        }
		return false;
	}
	
	static private boolean isWaitingListNameExist( ResultSet rs ) 
	throws SQLException{
		if( rs == null ){
			System.out.println("WLWaitingListNameUtil/isWaitingListNameExist(): result set == null"); 
			return false;
		}
        if(rs.next()){
        	System.out.println("WLWaitingListNameUtil/isWaitingListNameExist(): wlName2 = " + oscar.Misc.getString(rs,"name")); 
	       	return true;
        }
		
		return false;
	}
	
	static private ResultSet getWaitingListNameRecords( DBHandler db, ResultSet rs, String wlName, String groupNo ) 
	throws SQLException{
		if( db == null  ||  wlName == null  ||  groupNo == null  ){
			System.out.println("WLWaitingListNameUtil/getWaitingListNameRecords(): db or rs or wlName or groupNo is null"); 
			return null;
		}
        String sql = " SELECT * FROM waitingListName " + 
		             " WHERE name = '" + wlName + "' " +
		             " AND group_no = '" + groupNo + "' " +
		             " AND is_history='N'";
			        
        System.out.println("WLWaitingListNameUtil/getWaitingListNameRecords(): sql = " + sql);   
        rs = db.GetSQL(sql); 
        
        if(rs == null){
        	System.out.println("WLWaitingListNameUtil/getWaitingListNameRecords(): result set == null");    
        	return null;
        }
		return rs;
	}

	
	static private ResultSet getWaitingListNameRecords( DBHandler db, ResultSet rs, String wlNameId ) 
	throws SQLException{
		if( db == null  || wlNameId == null  ){
			System.out.println("WLWaitingListNameUtil/getWaitingListNameRecords(): db or rs or wlNameId is null"); 
			return null;
		}
        String sql = " SELECT * FROM waitingListName " + 
		             " WHERE ID = " + wlNameId + 
		             " AND is_history='N'";
			        
        rs = db.GetSQL(sql); 
        
        if(rs == null){
        	System.out.println("WLWaitingListNameUtil/getWaitingListNameRecords(): result set == null");    
        	return null;
        }
		return rs;
	}


	
}
