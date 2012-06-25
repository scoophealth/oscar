/**
 * Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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
 */

package oscar.oscarWaitingList.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;

public class WLWaitingListNameUtil {
        
    static public void removeFromWaitingListName(String wlNameId, String groupNo) 
    throws SQLException, Exception {
		if( wlNameId == null  ||  groupNo == null   ){
			MiscUtils.getLogger().debug("WLWaitingListNameUtil/removeFromWaitingListName(): wlName or groupNo is null"); 
			return;
		}
        MiscUtils.getLogger().debug("WLWaitingListNameUtil/removeFromWaitingListName(): waiting list name: " + wlNameId + 
        		           " for groupNo " + groupNo);
        
        ResultSet rs = null;
        String sql;

        boolean isUsed = isWaitingListNameBeingUsed( rs, wlNameId );
        if(isUsed){
            MiscUtils.getLogger().debug("WLWaitingListNameUtil/removeFromWaitingListName(): Waiting list name is being used.");
            throw new Exception("wlNameUsed");
        }
        
        //update the list and set is_history = 'Y'          
        sql = " UPDATE waitingListName SET is_history ='Y' " + 
        	  " WHERE ID=" + wlNameId +
              " AND group_no='" + groupNo +"'";
        MiscUtils.getLogger().debug("remove waiting list name sql: " + sql);

        DBHandler.RunSQL(sql);
        if(rs != null){
        	rs.close();
        }
        return;
    } 
    
    static public void createWaitingListName(String wlName, String groupNo, String providerNo)
    throws SQLException, Exception {
            
		if( wlName == null  ||  groupNo == null  ||
			wlName.trim().length() <= 0  ||  groupNo.trim().length() <= 0){
			MiscUtils.getLogger().debug("WLWaitingListNameUtil/createWaitingListName(): " + 
					           " wlName or groupNo or providerNo is null"); 
			return;
		}
        MiscUtils.getLogger().debug( "WLWaitingListNameUtil/createWaitingListName(): waiting list name: " + wlName + 
        					" for groupNo: " + groupNo + "/ providerNo: " + providerNo);
        
        
        ResultSet rs = null;
        
        wlName = org.apache.commons.lang.StringEscapeUtils.escapeSql(wlName);
        
        rs = getWaitingListNameRecords( rs, wlName, groupNo );
        boolean isExist = isWaitingListNameExist( rs );
        
        if(isExist){
        	MiscUtils.getLogger().debug("WLWaitingListNameUtil/createWaitingListName(): The WL name already exists.");
        	throw new Exception("wlNameExists");
        }
        
    	String sql = " insert into waitingListName " + 
                	 " ( name, group_no, provider_no, create_date, is_history ) " +	
                	 " values('" + wlName + "','" + groupNo + "','" + providerNo + "'," + 
                	 "  now(), 'N')";
    
        MiscUtils.getLogger().debug("WLWaitingListNameUtil/createWaitingListName(): sql = " + sql);
        
        DBHandler.RunSQL(sql);
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
			MiscUtils.getLogger().debug("WLWaitingListNameUtil/updateWaitingListName(): " + 
					           " wlNameId or wlName or groupNo or providerNo is null"); 
			return;
		}

        MiscUtils.getLogger().debug("WLWaitingListNameUtil/updateWaitingListName(): wlNameId/wlName = " + 
        		wlNameId + "/" + wlName);
	            
        ResultSet rs = null;

        wlName = org.apache.commons.lang.StringEscapeUtils.escapeSql(wlName);
        
        rs = getWaitingListNameRecords( rs, wlName, groupNo );
        boolean isExist = isWaitingListNameExist( rs );
        
        if(isExist){
        	MiscUtils.getLogger().debug("WLWaitingListNameUtil/createWaitingListName(): The WL name already exists.");
        	throw new Exception("wlNameExists");
        }
        
        String sql = " UPDATE waitingListName " + 
          	  " SET name = '" + wlName + "' " +
          	  " WHERE  ID=" + wlNameId;

        MiscUtils.getLogger().debug("WLWaitingListNameUtil/updateWaitingListName(): sql = " + sql);

        DBHandler.RunSQL(sql);
        if(rs != null){
        	rs.close();
        }
        return;
	}


	
	static private boolean isWaitingListNameBeingUsed( ResultSet rs, String wlNameId ) throws SQLException{
		
		if( wlNameId == null ){
			MiscUtils.getLogger().debug("WLWaitingListNameUtil/isWaitingListNameBeingUsed(): db or rs or wlNameId is null"); 
			return true;
		}
        String sql = " SELECT ID FROM waitingList " + 
		             " WHERE listID = " + wlNameId +
		             " AND is_history='N'";
			        
        rs = DBHandler.GetSQL(sql); 
        if(rs == null){
        	MiscUtils.getLogger().debug("WLWaitingListNameUtil/isWaitingListNameBeingUsed(): result set == null");    
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
			MiscUtils.getLogger().debug("WLWaitingListNameUtil/isWaitingListNameExist(): result set == null"); 
			return false;
		}
        if(rs.next()){
        	MiscUtils.getLogger().debug("WLWaitingListNameUtil/isWaitingListNameExist(): wlName2 = " + oscar.Misc.getString(rs,"name")); 
	       	return true;
        }
		
		return false;
	}
	
	static private ResultSet getWaitingListNameRecords( ResultSet rs, String wlName, String groupNo ) 
	throws SQLException{
		if( wlName == null  ||  groupNo == null  ){
			MiscUtils.getLogger().debug("WLWaitingListNameUtil/getWaitingListNameRecords(): db or rs or wlName or groupNo is null"); 
			return null;
		}
        String sql = " SELECT * FROM waitingListName " + 
		             " WHERE name = '" + wlName + "' " +
		             " AND group_no = '" + groupNo + "' " +
		             " AND is_history='N'";
			        
        MiscUtils.getLogger().debug("WLWaitingListNameUtil/getWaitingListNameRecords(): sql = " + sql);   
        rs = DBHandler.GetSQL(sql); 
        
        if(rs == null){
        	MiscUtils.getLogger().debug("WLWaitingListNameUtil/getWaitingListNameRecords(): result set == null");    
        	return null;
        }
		return rs;
	}

	
	static private ResultSet getWaitingListNameRecords( DBHandler db, ResultSet rs, String wlNameId ) 
	throws SQLException{
		if( db == null  || wlNameId == null  ){
			MiscUtils.getLogger().debug("WLWaitingListNameUtil/getWaitingListNameRecords(): db or rs or wlNameId is null"); 
			return null;
		}
        String sql = " SELECT * FROM waitingListName " + 
		             " WHERE ID = " + wlNameId + 
		             " AND is_history='N'";
			        
        rs = DBHandler.GetSQL(sql); 
        
        if(rs == null){
        	MiscUtils.getLogger().debug("WLWaitingListNameUtil/getWaitingListNameRecords(): result set == null");    
        	return null;
        }
		return rs;
	}


	
}
