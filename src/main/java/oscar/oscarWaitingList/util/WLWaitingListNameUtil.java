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

import java.util.Date;
import java.util.List;

import org.oscarehr.common.dao.WaitingListDao;
import org.oscarehr.common.dao.WaitingListNameDao;
import org.oscarehr.common.model.WaitingList;
import org.oscarehr.common.model.WaitingListName;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class WLWaitingListNameUtil {
        
	private static WaitingListNameDao nameDao = SpringUtils.getBean(WaitingListNameDao.class);
	private static WaitingListDao dao = SpringUtils.getBean(WaitingListDao.class);

	
    static public void removeFromWaitingListName(String wlNameId, String groupNo) 
    throws Exception {
		if( wlNameId == null  ||  groupNo == null   ){
			MiscUtils.getLogger().debug("WLWaitingListNameUtil/removeFromWaitingListName(): wlName or groupNo is null"); 
			return;
		}
        MiscUtils.getLogger().debug("WLWaitingListNameUtil/removeFromWaitingListName(): waiting list name: " + wlNameId + 
        		           " for groupNo " + groupNo);
        
    
        boolean isUsed = isWaitingListNameBeingUsed(wlNameId );
        if(isUsed){
            MiscUtils.getLogger().debug("WLWaitingListNameUtil/removeFromWaitingListName(): Waiting list name is being used.");
            throw new Exception("wlNameUsed");
        }
        
        //update the list and set is_history = 'Y'     
        WaitingListName w = nameDao.find(Integer.parseInt(wlNameId));
        w.setIsHistory("Y");
        dao.merge(w);
        
       
        return;
    } 
    
    static public void createWaitingListName(String wlName, String groupNo, String providerNo)
    throws Exception {
            
		if( wlName == null  ||  groupNo == null  ||
			wlName.trim().length() <= 0  ||  groupNo.trim().length() <= 0){
			MiscUtils.getLogger().debug("WLWaitingListNameUtil/createWaitingListName(): " + 
					           " wlName or groupNo or providerNo is null"); 
			return;
		}
        MiscUtils.getLogger().debug( "WLWaitingListNameUtil/createWaitingListName(): waiting list name: " + wlName + 
        					" for groupNo: " + groupNo + "/ providerNo: " + providerNo);
        
        
        
        List<WaitingListName> wlns = getWaitingListNameRecords( wlName, groupNo );
        
        boolean isExist = isWaitingListNameExist( wlns);
        
        if(isExist){
        	MiscUtils.getLogger().debug("WLWaitingListNameUtil/createWaitingListName(): The WL name already exists.");
        	throw new Exception("wlNameExists");
        }
        
        WaitingListName wln = new WaitingListName();
        wln.setName(wlName);
        wln.setGroupNo(groupNo);
        wln.setProviderNo(providerNo);
        wln.setCreateDate(new Date());
        wln.setIsHistory("N");
        nameDao.persist(wln);
        
    	
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
	throws Exception {
	    
		if( wlNameId == null  || wlName == null  ||  groupNo == null  ||  providerNo == null  ||
			wlNameId.equalsIgnoreCase("0")  || 	wlName.length() <= 0  ||  groupNo.length() <= 0 ){
			MiscUtils.getLogger().debug("WLWaitingListNameUtil/updateWaitingListName(): " + 
					           " wlNameId or wlName or groupNo or providerNo is null"); 
			return;
		}

        MiscUtils.getLogger().debug("WLWaitingListNameUtil/updateWaitingListName(): wlNameId/wlName = " + 
        		wlNameId + "/" + wlName);
	            
        
        List<WaitingListName>  wlns= getWaitingListNameRecords(wlName, groupNo );
        boolean isExist = isWaitingListNameExist( wlns );
        
        if(isExist){
        	MiscUtils.getLogger().debug("WLWaitingListNameUtil/createWaitingListName(): The WL name already exists.");
        	throw new Exception("wlNameExists");
        }
        
        WaitingListName wln = nameDao.find(Integer.parseInt(wlNameId));
        if(wln != null) {
        	wln.setName(wlName);
        	nameDao.merge(wln);
        }
      
        return;
	}


	
	static private boolean isWaitingListNameBeingUsed(String wlNameId ) {
		
		if( wlNameId == null ){
			MiscUtils.getLogger().debug("WLWaitingListNameUtil/isWaitingListNameBeingUsed(): db or rs or wlNameId is null"); 
			return true;
		}
		
		List<WaitingList> wls = dao.findByWaitingListId(Integer.parseInt(wlNameId));
		if(wls.size()>0)
			return true;
		
        
		return false;
	}
	
	static private boolean isWaitingListNameExist( List<WaitingListName> wlns) {
		if(wlns.size()>0)
			return true;
		return false;
	}
	
	static private List<WaitingListName> getWaitingListNameRecords(String wlName, String groupNo ) {
		if( wlName == null  ||  groupNo == null  ){
			MiscUtils.getLogger().debug("WLWaitingListNameUtil/getWaitingListNameRecords(): db or rs or wlName or groupNo is null"); 
			return null;
		}
		
		List<WaitingListName> results = nameDao.findCurrentByNameAndGroup(wlName, groupNo);
        
		return results;
	}
	
}
