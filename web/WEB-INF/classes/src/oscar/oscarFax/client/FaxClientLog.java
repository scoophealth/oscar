/*
 * FaxClientLog.java
 *
 * Created on February 17, 2003, 12:08 PM
 */
package oscar.oscarFax.client;
import java.io.*;
import oscar.oscarDB.*;
import java.util.*;
import java.sql.*;
/**
 *
 * @author  Jay
 */
public class FaxClientLog {
    /*
     FaxClientLog
        faxLogId int(9) primary key auto_increment
        provider_no varchar(6)
        startTime DateTime
        endTime DateTime
        faxId varchar(10)
        requestId varchar(10)
        result varchar(255)
    */
    
    String faxLogId = "";
 
    public FaxClientLog (String providerNo){
       
	try
            {
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                String sql = "insert into FaxClientLog (provider_no,startTime) values ('"+providerNo+"',now()) ";
                db.RunSQL(sql);
                ResultSet rs = db.GetSQL("SELECT LAST_INSERT_ID() ");
                if(rs.next())
                    faxLogId = Integer.toString(rs.getInt(1));
                db.CloseConn();
            }
            catch(SQLException e)
            {
                System.out.println(e.getMessage());
            }
       
    }
    
    public void setFaxRequestId(String faxId, String requestId) throws Exception{
           if (!faxLogId.equals("")){;
            try
            {
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                String sql = "update FaxClientLog set requestId = '"+requestId+"' , faxId = '"+faxId+"' , endTime = now() where faxLogId = '"+faxLogId+"' ";
                db.RunSQL(sql);
                db.CloseConn();
            }
            catch(SQLException e)
            {
                System.out.println(e.getMessage());
            }
            }else{
               throw new Exception("faxLogId must be set before you can call this method");
            }

    
    }

     public void setResult(String result) throws Exception{
           if (!faxLogId.equals("")){;
            try
            {
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                String sql = "update FaxClientLog set result = '"+result+"' , endTime = now() where faxLogId = '"+faxLogId+"' ";
                db.RunSQL(sql);
                db.CloseConn();
            }
            catch(SQLException e)
            {
                System.out.println(e.getMessage());
            }
            }else{
               throw new Exception("faxLogId must be set before you can call this method");
            }

    
    }

    
    public String getFaxLogId() { return faxLogId; }
    String requestId = null;
    String jobId = null;
    
    
    
}


