/*
 * DemographicMergedDAO.java
 *
 * Created on September 14, 2007, 1:13 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package oscar.oscarDemographic.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import oscar.oscarDB.DBHandler;
import oscar.util.UtilDateUtilities;

/**
 *
 * @author wrighd
 */
public class DemographicMerged {
    
    Logger logger = Logger.getLogger(DemographicMerged.class);
    
    /** Creates a new instance of DemographicMergedDAO */
    public DemographicMerged() {
    }
    
    public void Merge(String demographic_no, String head) throws SQLException{
        DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
        Connection conn = DBHandler.getConnection();
        
        String sql = "insert into demographic_merged (demographic_no, merged_to) values (?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        
        // always merge the head of records that have already been merged to the new head
        String record_head = getHead(demographic_no);
        if (record_head == null)
            pstmt.setInt(1, Integer.parseInt( demographic_no ));
        else
            pstmt.setInt(1, Integer.parseInt(record_head));
        
        pstmt.setInt(2, Integer.parseInt( head ));
        pstmt.executeUpdate();
        pstmt.close();
        
        sql = "insert into secObjPrivilege (roleUserGroup, objectName, privilege, priority, provider_no) values ('_all','_eChart$"+demographic_no+"','|or|','0','0')";
        pstmt = conn.prepareStatement(sql);
        pstmt.executeUpdate();
        pstmt.close();
    }
    
    public void UnMerge(String demographic_no, String curUser_no) throws SQLException{
        
        DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
        Connection conn = DBHandler.getConnection();
        
        String sql = "update demographic_merged set deleted=1 where demographic_no=?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        
        pstmt.setInt(1, Integer.parseInt( demographic_no ));
        
        pstmt.executeUpdate();
        pstmt.close();
        
        sql = "select * from secObjPrivilege where roleUserGroup='_all' and objectName='_eChart$"+demographic_no+"'";
        //rs = dbObj.searchDBRecord(sql);
        pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        String privilege = "";
        String priority = "";
        String provider_no = "";
        while (rs.next()) {
            privilege = db.getString(rs,"privilege");
            priority = db.getString(rs,"priority");
            provider_no = db.getString(rs,"provider_no");
        }
        pstmt.close();
        
        sql = "delete from secObjPrivilege where roleUserGroup='_all' and objectName='_eChart$"+demographic_no+"'";
        pstmt = conn.prepareStatement(sql);
        if(!pstmt.execute()){
            logger.info("we should be writing to the recycle bin");
            pstmt.close();
            sql = "insert into recyclebin (provider_no,updatedatetime,table_name,keyword,table_content) values(";
            sql += "'" + curUser_no + "',";
            sql += "?,";
            sql += "'" + "secObjPrivilege" + "',";
            sql += "'_all|_eChart$"+ demographic_no + "',";
            sql += "'" + "<roleUserGroup>_all</roleUserGroup>" + "<objectName>_eChart$" + demographic_no + "</objectName>";
            sql += "<privilege>" + privilege + "</privilege>" + "<priority>" + priority + "</priority>";
            sql += "<provider_no>" + provider_no + "</provider_no>" + "')";
            pstmt = conn.prepareStatement(sql);
            pstmt.setDate(1, oscar.MyDateFormat.getSysDate(UtilDateUtilities.getToday("yyyy-MM-dd HH:mm:ss")));
            pstmt.executeUpdate();
        }
        
        pstmt.close();
        
    }
    
    public String getHead(String demographic_no) throws SQLException{
        DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
        Connection conn = DBHandler.getConnection();
        ResultSet rs;
        String head = null;
        
        String sql = "select merged_to from demographic_merged where demographic_no = ? and deleted = 0";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        
        pstmt.setInt(1, Integer.parseInt(demographic_no));
        rs = pstmt.executeQuery();
        if(rs.next())
            head = db.getString(rs,"merged_to");
        
        pstmt.close();
        if (head != null)
            head = getHead(head);
        else
            head = demographic_no;
        
        return head;
        
    }
    
    public ArrayList getTail(String demographic_no) throws SQLException{
        
        DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
        Connection conn = DBHandler.getConnection();
        ResultSet rs;
        ArrayList tailArray = new ArrayList();
        
        String sql = "select demographic_no from demographic_merged where merged_to = ? and deleted = 0";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        
        pstmt.setInt(1, Integer.parseInt(demographic_no));
        rs = pstmt.executeQuery();
        while(rs.next()){
            tailArray.add(db.getString(rs,"demographic_no"));
        }
        
        pstmt.close();
        int size = tailArray.size();
        for (int i=0; i < size; i++){
            tailArray.addAll(getTail( (String) tailArray.get(i) ));
        }
        
        return tailArray;
        
    }
}
