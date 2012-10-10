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


/*
 * MsgDemoMap.java
 *
 * Created on April 24, 2005, 3:47 PM
 */

package oscar.oscarMessenger.util;

import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.Vector;

import org.oscarehr.common.dao.MsgDemoMapDao;
import org.oscarehr.common.model.MsgDemoMapPK;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarDB.DBHandler;
/**
 *
 * @author root
 */
public class MsgDemoMap {
    
	private MsgDemoMapDao dao = SpringUtils.getBean(MsgDemoMapDao.class);
	
    public MsgDemoMap() {
    }
    
    public void linkMsg2Demo(String msgId, String demographic_no){

    	org.oscarehr.common.model.MsgDemoMap mdm = new org.oscarehr.common.model.MsgDemoMap();
    	mdm.setId(new MsgDemoMapPK(Integer.parseInt(msgId),Integer.parseInt(demographic_no)));
    	
    	if(dao.find(new MsgDemoMapPK(Integer.parseInt(msgId),Integer.parseInt(demographic_no))) == null)
    		dao.persist(mdm);
    	
    }
    
    public Hashtable getDemoMap (String msgId){
        Hashtable demoMap = new Hashtable();
        try{            
            
            String sql = "";                               
            sql = "select d.last_name, d.first_name, d.demographic_no from msgDemoMap m, demographic d where messageID ='"+msgId + 
                  "' and d.demographic_no = m.demographic_no order by d.last_name, d.first_name";
                                
            ResultSet rs = DBHandler.GetSQL(sql);
            while(rs.next()){
                demoMap.put(oscar.Misc.getString(rs, "d.demographic_no"), oscar.Misc.getString(rs, "last_name")+", "+oscar.Misc.getString(rs, "first_name") );
            }
        }
        catch (java.sql.SQLException e){ 
            demoMap = null;
        }  
        
        return demoMap;
    }
    
    public Vector getMsgVector(String demographic_no){
        Vector msgVector= new Vector();
        try{            
            
            String sql = "";                               
            //sql = "select tbl.thedate, tbl.thesubject from msgDemoMap map, messagetbl tbl where demographic_no ='"+ demographic_no 
            //        + "' and tbl.messageid = map.messageID order by tbl.thedate";
            sql = "select map.messageID from msgDemoMap map, messagetbl m where m.messageid=map.messageID and demographic_no='"+demographic_no+"' order by m.thedate desc , m.messageid desc ";
            
            ResultSet rs = DBHandler.GetSQL(sql);
            while(rs.next()){
                msgVector.add(oscar.Misc.getString(rs, "messageID"));
            }
        }
        catch (java.sql.SQLException e){ 
            msgVector = null;
        }
        return msgVector;
    }
    
    public void unlinkMsg (String demographic_no, String messageID){        
        MiscUtils.getLogger().debug("input msgId: " + messageID + "  input demographic_no: " + demographic_no);
        
        org.oscarehr.common.model.MsgDemoMap mdm = new org.oscarehr.common.model.MsgDemoMap();
    	mdm.setId(new MsgDemoMapPK(Integer.parseInt(messageID),Integer.parseInt(demographic_no)));
    	dao.remove(mdm);
       
    }
}
