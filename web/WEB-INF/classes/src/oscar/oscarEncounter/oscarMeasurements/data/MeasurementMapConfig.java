/*
 * MeasurementMapConfig.java
 *
 * Created on September 28, 2007, 10:15 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package oscar.oscarEncounter.oscarMeasurements.data;

import java.sql.*;
import java.util.*;
import org.apache.log4j.Logger;
import oscar.oscarDB.DBHandler;
import oscar.util.UtilDateUtilities;

/**
 *
 * @author wrighd
 */
public class MeasurementMapConfig {
    
    Logger logger = Logger.getLogger(MeasurementMapConfig.class);
    
    /** Creates a new instance of MeasurementMapConfig */
    public MeasurementMapConfig() {
    }
    
    public ArrayList getLoincCodes(String searchString){
        searchString = "%"+searchString.replaceAll("\\s", "%")+"%";
        ArrayList ret = new ArrayList();
        String sql = "SELECT DISTINCT loinc_code, name FROM measurementMap WHERE loinc_code=ident_code and name like '"+searchString+"' ORDER BY name";
        
        try{
            DBHandler db =  new DBHandler(DBHandler.OSCAR_DATA);
            Connection conn = db.GetConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            logger.info(sql);
            
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                Hashtable ht = new Hashtable();
                ht.put("code", db.getString(rs,"loinc_code"));
                ht.put("name", db.getString(rs,"name"));
                ret.add(ht);
            }
            
            pstmt.close();
            db.CloseConn();
        }catch(SQLException e){
            logger.error("Exception in getLoincCodes", e);
        }
        
        return ret;
        
    }
    
    public ArrayList getMeasurementMap(String searchString){
        searchString = "%"+searchString.replaceAll("\\s", "%")+"%";
        ArrayList ret = new ArrayList();
        String sql = "SELECT DISTINCT * FROM measurementMap WHERE name LIKE '"+searchString+"' ORDER BY name";
        
        try{
            DBHandler db =  new DBHandler(DBHandler.OSCAR_DATA);
            Connection conn = db.GetConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            logger.info(sql);
            
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                Hashtable ht = new Hashtable();
                ht.put("id", getString(db.getString(rs,"id")));
                ht.put("loinc_code", getString(db.getString(rs,"loinc_code")));
                ht.put("ident_code", getString(db.getString(rs,"ident_code")));
                ht.put("name", getString(db.getString(rs,"name")));
                ht.put("lab_type", getString(db.getString(rs,"lab_type")));
                ret.add(ht);
            }
            
            pstmt.close();
            db.CloseConn();
        }catch(SQLException e){
            logger.error("Exception in getMeasurementMap", e);
        }
        
        return ret;
    }
    
    public ArrayList getUnmappedMeasurements(String type){
        ArrayList ret = new ArrayList();
        String sql = "SELECT DISTINCT h.type, me1.val AS identifier, me2.val AS name " +
                "FROM measurementsExt me1 " +
                "JOIN measurementsExt me2 ON me1.measurement_id = me2.measurement_id AND me2.keyval='name' " +
                "JOIN measurementsExt me3 ON me1.measurement_id = me3.measurement_id AND me3.keyval='lab_no' " +
                "JOIN hl7TextMessage h ON me3.val = h.lab_id " +
                "WHERE me1.keyval='identifier' AND h.type LIKE '"+type+"%' " +
                "AND me1.val NOT IN (SELECT ident_code FROM measurementMap) ORDER BY h.type";
        
        try{
            DBHandler db =  new DBHandler(DBHandler.OSCAR_DATA);
            Connection conn = db.GetConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            logger.info(sql);
            
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                Hashtable ht = new Hashtable();
                ht.put("type", getString(db.getString(rs,"type")));
                ht.put("identifier", getString(db.getString(rs,"identifier")));
                ht.put("name", getString(db.getString(rs,"name")));
                ret.add(ht);
            }
            
            pstmt.close();
            db.CloseConn();
        }catch(SQLException e){
            logger.error("Exception in getUnmappedMeasurements", e);
        }
        
        return ret;
    }
    
    public void mapMeasurement(String identifier, String loinc, String name, String type) throws SQLException{
        
        String sql = "INSERT INTO measurementMap (loinc_code, ident_code, name, lab_type) VALUES ('"+loinc+"', '"+identifier+"', '"+name+"', '"+type+"')";
        
        DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
        Connection conn = db.GetConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        logger.info(sql);
        
        pstmt.executeUpdate();
        pstmt.close();
        db.CloseConn();
        
    }
    
    public void removeMapping(String id, String provider_no) throws SQLException {
        
        String ident_code = "";
        String loinc_code = "";
        String name = "";
        String lab_type="";
                
        DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
        Connection conn = db.GetConnection();

        String sql = "SELECT * FROM measurementMap WHERE id='"+id+"'";        
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()){
            ident_code = getString(db.getString(rs,"ident_code"));
            loinc_code = getString(db.getString(rs,"loinc_code"));
            name = getString(db.getString(rs,"name"));
            lab_type = getString(db.getString(rs,"lab_type"));
        }
        
        sql = "DELETE FROM measurementMap WHERE id='"+id+"'";
        pstmt = conn.prepareStatement(sql);
        if(!pstmt.execute()){
            logger.info("we should be writing to the recycle bin");
            pstmt.close();
            sql = "insert into recyclebin (provider_no,updatedatetime,table_name,keyword,table_content) values(";
            sql += "'" + provider_no + "',";
            sql += "'" + UtilDateUtilities.getToday("yyyy-MM-dd HH:mm:ss") + "',";
            sql += "'" + "measurementMap" + "',";
            sql += "'"+ id + "',";
            sql += "'" + "<id>"+id+"</id><ident_code>"+ident_code+"</ident_code><loinc_code>"+loinc_code+"</loinc_code><name>"+name+"</name><lab_type>"+lab_type+"</lab_type>')";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
        }
        
        pstmt.close();
        db.CloseConn();
        
    }
    
    /**
     *  Only one identifier per type is allowed to be mapped to a single loinc code
     *  Return true if there is already an identifier mapped to the loinc code.
     */
    public boolean checkLoincMapping(String loinc, String type) throws SQLException {
        
        boolean ret = false;
        String sql = "SELECT * from measurementMap WHERE loinc_code='"+loinc+"' AND lab_type='"+type+"'";
        
        DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
        Connection conn = db.GetConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        logger.info(sql);
        
        ResultSet rs = pstmt.executeQuery();
        ret = rs.next();
                
        return ret;
    }
    
    private String getString(String input){
        String ret = "";
        if (input != null)
            ret = input;
        
        return ret;
    }
    
}
