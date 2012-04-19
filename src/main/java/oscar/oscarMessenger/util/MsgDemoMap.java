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

import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;
/**
 *
 * @author root
 */
public class MsgDemoMap {
    
    /** Creates a new instance of MsgDemoMap */
    public MsgDemoMap() {
    }
    
    
    /*  Structure of the msgDemoMap table
     *  +----------------+--------------+------+-----+---------+-------+
     *  | Field          | Type         | Null | Key | Default | Extra |
     *  +----------------+--------------+------+-----+---------+-------+
     *  | messageID      | mediumint(9) |      | PRI | 0       |       |
     *  | demographic_no | int(10)      |      | PRI | 0       |       |
     *  +----------------+--------------+------+-----+---------+-------+
     */
    public void linkMsg2Demo(String msgId, String demographic_no){

        //both msgId + demographic_no is the the primary key
        //if the combination of both msgId and demographic_no value exsit in the table, new data will not be added
        try{            
            
            String sql = "";                   
            sql = "insert into msgDemoMap values ('"+msgId+"','"+demographic_no+"')";
            MiscUtils.getLogger().debug(sql);
            DBHandler.RunSQL(sql);
        }
        catch (java.sql.SQLException e){ 
           MiscUtils.getLogger().error("Error", e); 
        }
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
        Vector msgVector= new Vector();
        try{          
            MiscUtils.getLogger().debug("input msgId: " + messageID + "  input demographic_no: " + demographic_no);
            
            String sql = "";                               
            //sql = "select tbl.thedate, tbl.thesubject from msgDemoMap map, messagetbl tbl where demographic_no ='"+ demographic_no 
            //        + "' and tbl.messageid = map.messageID order by tbl.thedate";
            sql = "delete from msgDemoMap where demographic_no='"+demographic_no+"' and messageID='"+messageID+"'";
            
            DBHandler.RunSQL(sql);
        }
        catch (java.sql.SQLException e){ 
            msgVector = null;
        }
    }
}
