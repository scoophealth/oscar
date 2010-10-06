package oscar.oscarResearch.oscarDxResearch.bean;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;

public class dxCodeHandler {
    
    public dxCodeHandler() {       
    }
    
    public String getDescription(String codingSystem,String code) {    	
    	String result = null;
      
    	try {
            ResultSet rs;
            
            String sql = "SELECT " + codingSystem +",description FROM " + codingSystem + " WHERE " + codingSystem + " = '" + code + "'";
            rs = DBHandler.GetSQL(sql);
            if(rs.next()){
            	result = oscar.Misc.getString(rs, "description"); 
            }
            rs.close();
        }
        catch(SQLException e) {
            MiscUtils.getLogger().error("Error", e);            
        }
        return result;
    }   
}
