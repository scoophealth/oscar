/*
 * EctWindowSizes.java
 *
 * Created on April 2, 2004, 2:47 PM
 */

package oscar.oscarEncounter.pageUtil;

import oscar.oscarDB.*;
import java.util.Properties;
import java.sql.*;

/**
 *
 * @author  root
 */
public class EctWindowSizes {
    
    /** Creates a new instance of EctWindowSizes */
    public EctWindowSizes() {
    }
    
    public static Properties getWindowSizes(String provNo) {
        Properties props = new Properties();
        
        try {

            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "select * from encounterWindow where provider_no='"+provNo+"'";
            ResultSet rs = db.GetSQL(sql);
            
            rs.next();
           
            props.setProperty("rowOneSize", rs.getString("rowOneSize"));
            props.setProperty("rowTwoSize", rs.getString("rowTwoSize"));
            props.setProperty("rowThreeSize", rs.getString("rowThreeSize"));
            props.setProperty("presBoxSize", rs.getString("presBoxSize"));                           
           
        } catch (Exception e) {
            e.printStackTrace(System.out);
            props.setProperty("rowOneSize", "60");
            props.setProperty("rowTwoSize", "60");
            props.setProperty("rowThreeSize", "378");
            props.setProperty("presBoxSize", "30");
        }
        return props;
    }
}
