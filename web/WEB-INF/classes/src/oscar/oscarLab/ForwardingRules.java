/*
 * ForwardingRules.java
 *
 * Created on July 16, 2007, 10:29 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package oscar.oscarLab;

import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import oscar.oscarDB.DBHandler;

/**
 *
 * @author wrighd
 */
public class ForwardingRules {
    
    Logger logger = Logger.getLogger(ForwardingRules.class);
    
    /** Creates a new instance of ForwardingRules */
    public ForwardingRules() {
    }
    
    public ArrayList getProviders(String providerNo){
        ArrayList ret = new ArrayList();
        String sql = "SELECT p.provider_no, p.first_name, p.last_name FROM incomingLabRules i, provider p WHERE i.archive='0' AND i.provider_no='"+providerNo+"' AND p.provider_no=i.frwdProvider_no";
        try{
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs = db.GetSQL(sql);            
            while (rs.next()){
                ArrayList info = new ArrayList();
                info.add(db.getString(rs,"provider_no"));
                info.add(db.getString(rs,"first_name"));
                info.add(db.getString(rs,"last_name"));
                ret.add(info);
            }
        }catch(Exception e){
            logger.error("Could not retrieve forwarding rules", e);
        }        
        return ret;
    }
    
    public String getStatus(String providerNo){
        String ret = "N";
        String sql = "SELECT status FROM incomingLabRules WHERE archive='0' AND provider_no='"+providerNo+"'";
        try{
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs = db.GetSQL(sql);            
            if (rs.next()){
                ret = db.getString(rs,"status");
            }
        }catch(Exception e){
            logger.error("Could not retrieve forwarding rules", e);
        }
        return ret;
    }
    
    public boolean isSet(String providerNo){
        boolean ret = false;
        String sql = "SELECT status FROM incomingLabRules WHERE archive='0' AND provider_no='"+providerNo+"'";
        try{
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs = db.GetSQL(sql);            
            if (rs.next()){
                ret = true;
            }
        }catch(Exception e){
            logger.error("Could not retrieve forwarding rules", e);
        }
        return ret;
    }
    
}
