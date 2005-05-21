/*
 * MsgDemoMap.java
 *
 * Created on April 24, 2005, 3:47 PM
 */

package oscar.oscarMessenger.util;

import oscar.oscarDB.DBHandler;

import java.sql.ResultSet;
import java.util.Vector;
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
        
        //System.out.println("input msgId: " + msgId + "  input demographic_no: " + demographic_no);
       
        //both msgId + demographic_no is the the primary key
        //if the combination of both msgId and demographic_no value exsit in the table, new data will not be added
        try{            
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "";                   
            sql = "insert into msgDemoMap values("+msgId+"," +demographic_no+ ")";
            db.RunSQL(sql);
            db.CloseConn();
        }
        catch (java.sql.SQLException e){ 
            e.printStackTrace(System.out); 
        }
    }
    
    public Vector getDemoVector(String msgId){
        Vector demoVector= new Vector();
        try{            
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "";                               
            sql = "select d.last_name, d.first_name from msgDemoMap m, demographic d where messageID ='"+msgId + 
                  "' and d.demographic_no = m.demographic_no order by d.last_name, d.first_name";
                                
            ResultSet rs = db.GetSQL(sql);
            while(rs.next()){
                demoVector.add(rs.getString("last_name")+", "+rs.getString("first_name"));
            }
            db.CloseConn();
        }
        catch (java.sql.SQLException e){ 
            demoVector = null;
        }
        
        return demoVector;
    }
    
    public Vector getMsgVector(String demographic_no){
        Vector msgVector= new Vector();
        try{            
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "";                               
            //sql = "select tbl.thedate, tbl.thesubject from msgDemoMap map, messagetbl tbl where demographic_no ='"+ demographic_no 
            //        + "' and tbl.messageid = map.messageID order by tbl.thedate";
            sql = "select map.messageID from msgDemoMap map, messagetbl m where m.messageid=map.messageID and demographic_no='"+demographic_no+"' order by m.thedate desc";
            
            ResultSet rs = db.GetSQL(sql);
            while(rs.next()){
                msgVector.add(rs.getString("messageID"));
            }
            db.CloseConn();
        }
        catch (java.sql.SQLException e){ 
            msgVector = null;
        }
        return msgVector;
    }
    
    public void unlinkMsg (String demographic_no, String messageID){
        Vector msgVector= new Vector();
        try{          
            System.out.println("input msgId: " + messageID + "  input demographic_no: " + demographic_no);
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "";                               
            //sql = "select tbl.thedate, tbl.thesubject from msgDemoMap map, messagetbl tbl where demographic_no ='"+ demographic_no 
            //        + "' and tbl.messageid = map.messageID order by tbl.thedate";
            sql = "delete from msgDemoMap where demographic_no='"+demographic_no+"' and messageID='"+messageID+"'";
            
            db.RunSQL(sql);
            
            db.CloseConn();
        }
        catch (java.sql.SQLException e){ 
            msgVector = null;
        }
    }
}
