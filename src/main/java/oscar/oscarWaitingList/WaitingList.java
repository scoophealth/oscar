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
// * McMaster University
// * Hamilton
// * Ontario, Canada
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar.oscarWaitingList;

import java.sql.ResultSet;

import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;
/*
 * This class is an interface with the file WEB-INF/classes
 * It is a singleton class. Do not instaciate it, use the method getInstance().
 * Every time that the properties file changes, tomcat must be restarted.
 */
public class WaitingList{
	
   private WaitingList() {
       
   }

    /**
	* @return WaitingList the instance of WaitingList 
	*/
	public static WaitingList getInstance() {
	   return new WaitingList();
	}

    public boolean checkWaitingListTable(){       
       
       ResultSet rs = null;
       try{
           
           String sql = "SELECT count(*) FROM waitingListName where is_history = 'N' ";
//           String sql = "SELECT * FROM waitingListName where is_history = 'N' limit 1 ";
           rs = DBHandler.GetSQL(sql);
           rs.next();
           int count = rs.getInt(1);
           return count > 0;
       } catch (Exception e) {
            MiscUtils.getLogger().error("Error", e);
            return false;
       }finally{
    	   try{
    		   if(rs != null){
    			   rs.close(); 
    		   }
    	   }catch(Exception ex2){
    		   MiscUtils.getLogger().debug("WaitingList.checkWaitingListTable():" + ex2.getMessage()); 
    	   }
       }
    }
        
    public boolean getFound(){
        return checkWaitingListTable();
    }
}

