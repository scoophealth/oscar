package oscar.oscarMDS.data;

import oscar.oscarDB.DBHandler;
import java.sql.*;
import java.util.ArrayList;

public class ProviderData {
    
    public ProviderData(String refDoctor, String conDoctor, String admDoctor) {
        referringDoctor = beautifyProviderName(refDoctor);
        consultingDoctor = beautifyProviderName(conDoctor);
        admittingDoctor = beautifyProviderName(admDoctor);
    }
    
    public String referringDoctor;
    public String consultingDoctor;
    public String admittingDoctor;
    
    public static String beautifyProviderName(String name) {
        String[] subStrings;
        
        if (name.length() > 0) {
            try {
                subStrings = name.split("\\^");
                if ( subStrings.length >= 18 ) {
                    return subStrings[5] + " " + subStrings[1] +", " + subStrings[17] + " "+ subStrings[13];
                } else if (subStrings.length >= 14) {
                    return subStrings[5] + " " + subStrings[1] +", " + subStrings[13];
                } else if ( subStrings.length >= 6 ) {
                    return subStrings[5] + " " + subStrings[1];
                } else {
                    return subStrings[1];
                }
            } catch (Exception e) {
                System.out.println("Error in ProviderData: "+e.toString());
                return name.replace('^', ' ');
            }
        } else {
            return "";
        }
        
    }
    
    public static ArrayList getProviderList () {
        try {            
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ArrayList result = new ArrayList();
            
            String sql = "select provider_no, first_name, last_name from provider where provider_type='doctor'";
            ResultSet rs = db.GetSQL(sql);            
            while ( rs.next() ) {
                ArrayList provider = new ArrayList();
                provider.add(rs.getString("provider_no"));
                provider.add(rs.getString("first_name"));
                provider.add(rs.getString("last_name"));
                result.add(provider);
            }
            db.CloseConn();            
            return result;
        }catch(Exception e){
            System.out.println("exception in ProviderData:"+e);
            return null;
        }        
    }
    
    public static String getProviderName(String providerNo) {
           try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            
                                    
            String sql = "select first_name, last_name from provider where provider_no='"+providerNo+"'";
            ResultSet rs = db.GetSQL(sql);            
            db.CloseConn();            
            if ( rs.next() ) {            
                return ( rs.getString("first_name") + " " + rs.getString("last_name") );            
            } else {                            
                return "";
            }
        }catch(Exception e){
            System.out.println("exception in ProviderData:"+e);
            return null;
        }        
    }
}
