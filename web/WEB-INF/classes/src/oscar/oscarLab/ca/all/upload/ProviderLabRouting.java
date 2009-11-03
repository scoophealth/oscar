/*
 * ProviderLabRouting.java
 *
 * Created on July 17, 2007, 9:43 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package oscar.oscarLab.ca.all.upload;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import oscar.OscarProperties;
import oscar.oscarDB.DBHandler;
import oscar.oscarLab.ForwardingRules;

/**
 *
 * @author wrighd
 */
public class ProviderLabRouting {
    
    Logger logger = Logger.getLogger(ProviderLabRouting.class);
    
    /** Creates a new instance of ProviderLabRouting */
    public ProviderLabRouting() {
    }
    
    public void route(String labId, String provider_no, Connection conn, String labType) throws SQLException{
        route(Integer.parseInt(labId), provider_no, conn, labType);
    }
    
    public void route(int labId, String provider_no, Connection conn, String labType) throws SQLException{
        PreparedStatement pstmt;
        ForwardingRules fr = new ForwardingRules();
        OscarProperties props = OscarProperties.getInstance();
        String autoFileLabs = props.getProperty("AUTO_FILE_LABS");
        
        String sql = "SELECT status FROM providerLabRouting WHERE provider_no='"+provider_no+"' AND lab_no='"+labId+"' AND lab_type='"+labType+"'";
        pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();        
        
        if (!rs.next()){
            
            String status = fr.getStatus(provider_no);
            ArrayList forwardProviders = fr.getProviders(provider_no);
            sql = "insert into providerLabRouting (provider_no, lab_no, status, lab_type) values('"+provider_no+"', '"+labId+"', '"+status+"', '"+labType+"')";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
            
            //forward lab to specified providers
            for (int j=0; j < forwardProviders.size(); j++){
                logger.info("FORWARDING PROVIDER: "+((String) ((ArrayList) forwardProviders.get(j)).get(0)));
                route(labId, ((String) ((ArrayList) forwardProviders.get(j)).get(0)), conn, labType);
            }
            
        // If the lab has already been sent to this provider check to make sure that 
        // it is set as a new lab for at least one provider if AUTO_FILE_LABS=yes is not
        // set in the oscar.properties file
        }else if (autoFileLabs == null || !autoFileLabs.equalsIgnoreCase("yes")){
            sql = "SELECT provider_no FROM providerLabRouting WHERE lab_no='"+labId+"' AND status='N' AND lab_type='"+labType+"'";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if (!rs.next()){
                sql = "UPDATE providerLabRouting set status='N' WHERE lab_no='"+labId+"' AND lab_type='"+labType+"'";
                pstmt = conn.prepareStatement(sql);
                pstmt.executeUpdate();
            }
        }
        pstmt.close();
    }
    
    public static Hashtable getInfo(String lab_no) throws SQLException {
	Hashtable info = new Hashtable();
	String sql = "SELECT * FROM providerLabRouting WHERE lab_no='"+lab_no+"'";
	DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
	ResultSet rs = db.GetSQL(sql);
	
	if (rs.next()) {
	    info.put("lab_no", lab_no);
	    info.put("provider_no", rs.getString("provider_no"));
	    info.put("status", rs.getString("status"));
	    info.put("comment", rs.getString("comment"));
	    info.put("timestamp", rs.getString("timestamp"));
	    info.put("lab_type", rs.getString("lab_type"));
	    info.put("id", rs.getInt("id"));
	}
	return info;
    }
}
