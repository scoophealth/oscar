/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */
/*
 * FaxClientLog.java
 *
 * Created on February 17, 2003, 12:08 PM
 */
package oscar.oscarFax.client;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;
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
                
                String sql = "insert into FaxClientLog (provider_no,startTime) values ('"+providerNo+"',now()) ";
                DBHandler.RunSQL(sql);
                ResultSet rs = DBHandler.GetSQL("SELECT LAST_INSERT_ID() ");
                if(rs.next())
                    faxLogId = Integer.toString(rs.getInt(1));
            }
            catch(SQLException e)
            {
                MiscUtils.getLogger().error("Error", e);
            }
       
    }
    
    public void setFaxRequestId(String faxId, String requestId) throws Exception{
           if (!faxLogId.equals("")){;
            try
            {
                
                String sql = "update FaxClientLog set requestId = '"+requestId+"' , faxId = '"+faxId+"' , endTime = now() where faxLogId = '"+faxLogId+"' ";
                DBHandler.RunSQL(sql);
            }
            catch(SQLException e)
            {
                MiscUtils.getLogger().error("Error", e);
            }
            }else{
               throw new Exception("faxLogId must be set before you can call this method");
            }

    
    }

     public void setResult(String result) throws Exception{
           if (!faxLogId.equals("")){;
            try
            {
                
                String sql = "update FaxClientLog set result = '"+result+"' , endTime = now() where faxLogId = '"+faxLogId+"' ";
                DBHandler.RunSQL(sql);
            }
            catch(SQLException e)
            {
                MiscUtils.getLogger().error("Error", e);
            }
            }else{
               throw new Exception("faxLogId must be set before you can call this method");
            }

    
    }

    
    public String getFaxLogId() { return faxLogId; }
    String requestId = null;
    String jobId = null;
    
    
    
}


