package oscar.oscarDemographic.data;

import oscar.OscarProperties;
import java.util.ArrayList;

public class ProvinceNames extends ArrayList {
        
    public static ProvinceNames getInstance() {
        return pNames;
    }
    
    private static boolean isDefined = true;
    static ProvinceNames pNames = new ProvinceNames();         
    
    private ProvinceNames() {
        OscarProperties props = OscarProperties.getInstance();        
        if (props.getProperty("province_names") == null || props.getProperty("province_names").equals("")) {
            isDefined = false;
            return;
        }
        String[] pNamesStr = props.getProperty("province_names").split("\\|");
        for (int i = 0; i < pNamesStr.length; i++) {
            add(pNamesStr[i]);
        }
    }
    
    public boolean isDefined() {
        return isDefined;       
    }
}
