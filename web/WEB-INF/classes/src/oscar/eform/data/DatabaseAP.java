
package oscar.eform.data;

import java.util.*;

public class DatabaseAP {
    private String apName = null;
    private String apSQL = null;
    private String apOutput = null;
    
    /** Creates a new instance of DatabaseAP */
    public DatabaseAP() {
    }
    
    public DatabaseAP(String apName, String apSQL, String apOutput) {
        set(apName, apSQL, apOutput);
    }
    
    public DatabaseAP(DatabaseAP ap2) {
        this.apName = ap2.getApName();
        this.apSQL = ap2.getApSQL();
        this.apOutput = ap2.getApOutput();
    }
    public void set(String apName, String apSQL, String apOutput) {
        this.apName = apName;
        this.apSQL = apSQL;
        this.apOutput = apOutput;
    }
    
    public void setApName(String apName) { this.apName = apName; }
    public void setApSQL(String apSQL) { this.apSQL = apSQL; }
    public void setApOutput(String apOutput) { this.apOutput = apOutput; }
    
    public String getApName() { return(apName); }
    public String getApSQL() { return(apSQL); }
    public String getApOutput() { return(apOutput); }
    
    
    public static String parserReplace(String name, String var, String str) {
        //replaces <$name$> with var in str
        StringBuffer strb = new StringBuffer(str);
        int tagstart = -2;
        int tagend;
        while ((tagstart = strb.indexOf("${", tagstart+2)) >= 0) {
            tagend = strb.indexOf("}", tagstart);
            if (strb.substring(tagstart+2, tagend).equals(name)) {
                strb.replace(tagstart, tagend+1, var);
            }
        }
        return strb.toString();
    }
    public static ArrayList parserGetNames(String str) {
        StringBuffer strb = new StringBuffer(str);
        ArrayList names = new ArrayList();
        int tagstart = -2;
        int tagend;
        while ((tagstart = strb.indexOf("${", tagstart+2)) >= 0) {
            tagend = strb.indexOf("}", tagstart);
            names.add(strb.substring(tagstart+2, tagend));
        }
        return names;
    }
    public static String parserClean(String str) {
        //removes left over ${...} in str; replaces with ""
        StringBuffer strb = new StringBuffer(str);
        int tagstart = -2;
        int tagend;
        while ((tagstart = strb.indexOf("${", tagstart+2)) >=0) {
            strb.replace(tagstart, tagstart+2, "\"");
            tagend = strb.indexOf("}", tagstart);
            strb.replace(tagend, tagend+2, "\"");
        }
        return strb.toString();
    }
    
}
