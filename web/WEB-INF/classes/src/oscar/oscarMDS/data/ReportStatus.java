package oscar.oscarMDS.data;

import java.text.SimpleDateFormat;
import java.util.*;

public class ReportStatus {
    
    private String providerName;
    private String providerNo;
    private String status;
    private String comment;
    private String timestamp;
            
    public ReportStatus (String pName, String pNo, String s, String c, String t) {
        providerName = pName;
        providerNo = pNo;
        status = s;
        comment = c;
        
        GregorianCalendar cal = new GregorianCalendar(Locale.ENGLISH);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy HH:mm", Locale.ENGLISH);
        
        // boneheaded calendar numbers months from 0
        cal.set(Integer.parseInt(t.substring(0,4)), Integer.parseInt(t.substring(4,6))-1, Integer.parseInt(t.substring(6,8)),
                Integer.parseInt(t.substring(8,10)), Integer.parseInt(t.substring(10,12)), Integer.parseInt(t.substring(12,14)));
       
        timestamp = dateFormat.format(cal.getTime());        
    }
    
    public String getProviderName() {
        return providerName;
    }
    
    public String getProviderNo() {
        return providerNo;
    }
    
    public String getStatus() {
        return status;
    }
    
    public String getComment() {
        return comment;
    }
    
    public String getTimestamp() {
        return timestamp;
    }
}

