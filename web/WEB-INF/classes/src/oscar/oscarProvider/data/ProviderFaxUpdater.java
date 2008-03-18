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

package oscar.oscarProvider.data;

import java.sql.ResultSet;
import java.sql.SQLException;

import oscar.oscarDB.DBHandler;

/**
 * Manages Fax number for provider 
 * 
 */
public class ProviderFaxUpdater {
    
    private String faxColName;    
    private String provider;
    
    /** Creates a new instance of ProviderFaxUpdater */
   public ProviderFaxUpdater(String p) {
       
       faxColName = new String("faxnumber");
       provider = p;       
    }
   
   /**
    *Retrieve fax number for current provider first by querying property table then clinic table
    */
   public String getFax() {
       String sql;
       String faxNum = "";
       ResultSet rs;
       DBHandler db;
       
       try {
        db = new DBHandler(DBHandler.OSCAR_DATA);
       
        sql = "SELECT value FROM property WHERE name = '" + faxColName + "' AND provider_no = '" + provider + "'";
        rs = db.GetSQL(sql);
            
        if( rs.next() ) {
            faxNum = db.getString(rs,"value");
        }
        
        if( faxNum.equals("") ) {
            sql = "SELECT clinic_fax FROM clinic";
            rs = db.GetSQL(sql);
            
            if( rs.next() ) {
                faxNum = db.getString(rs,"clinic_fax");
            }
        }
        
       }
       catch( SQLException ex ) {
           System.out.println(ex.getMessage());           
       }
       
       return faxNum;
   }
      /**
       *set fax number in property table
       */
   public boolean setFax(String fax) {
       DBHandler db;
       String sql;
       ResultSet rs;
       boolean ret = true;
       
       try {
                  
        if( haveFax() )
           sql = "UPDATE property SET value = '" + fax + "' WHERE name = '" + faxColName + "' AND provider_no = '" + provider + "'";
        else
           sql = "INSERT INTO property (name,value,provider_no) VALUES('" + faxColName + "', '" + fax + "', '" + provider + "')";
        
        db = new DBHandler(DBHandler.OSCAR_DATA);
        db.RunSQL(sql);
       
       }catch( SQLException ex ) {
           System.out.println("Error adding fax number: " + ex.getMessage());
           ret = false;
       }
       
       return ret;
   }
 
   private boolean haveFax() throws SQLException {
       DBHandler db;
       String sql;
       ResultSet rs;       
              
       db = new DBHandler(DBHandler.OSCAR_DATA);
       sql = "SELECT value FROM property WHERE name = '" + faxColName + "' AND provider_no = '" + provider + "'";
       
       rs = db.GetSQL(sql);
       
       return rs.next();              
       
   }
}
