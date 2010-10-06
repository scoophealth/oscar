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
import java.util.ArrayList;
import java.util.List;

import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;

/**
 * Manages MyOscar Login Id for provider 
 * 
 */
public class ProviderMyOscarIdData {
    
    static private String strColName = "MyOscarId";    
    private String provider;
    
    /** Creates a new instance of ProviderColourUpdater */
   public ProviderMyOscarIdData(String p) {
       provider = p;       
    }
   
   public ProviderMyOscarIdData() {
         
    }
   
   /**
    *Retrieve myOscar login id for current provider first by querying property table 
    */
   public static String getMyOscarId(String providerNo) {
       String sql;
       String myOscarId = "";
       ResultSet rs;
       DBHandler db;
       
       try {
        
       
        sql = "SELECT value FROM property WHERE name = '" + strColName + "' AND provider_no = '" + providerNo + "'";
        rs = DBHandler.GetSQL(sql);
            
        if( rs.next() ) {
            myOscarId = oscar.Misc.getString(rs, "value");
        }
                
        
       }
       catch( SQLException ex ) {
           MiscUtils.getLogger().error("Error", ex);           
       }
       
       return myOscarId;
   }
      /**
       *set myOscar login id in property table
       */
   public boolean setId(String id) {
       DBHandler db;
       String sql;
       ResultSet rs;
       boolean ret = true;
       
       try {
                  
        if( idIsSet() )
           sql = "UPDATE property SET value = '" + id + "' WHERE name = '" + strColName + "' AND provider_no = '" + provider + "'";
        else
           sql = "INSERT INTO property (name,value,provider_no) VALUES('" + strColName + "', '" + id + "', '" + provider + "')";
        
        
        DBHandler.RunSQL(sql);
       
       }catch( SQLException ex ) {
           MiscUtils.getLogger().debug("Error adding provider myOscar Login Id: " + ex.getMessage());
           ret = false;
       }
       
       return ret;
   }
 
   public boolean idIsSet() throws SQLException {
       DBHandler db;
       String sql;
       ResultSet rs;       
              
       
       sql = "SELECT value FROM property WHERE name = '" + strColName + "' AND provider_no = '" + provider + "'";
       
       rs = DBHandler.GetSQL(sql);
       
       return rs.next();              
       
   }
   
   //get provider number knowing the indivo id
  public String getProviderNo(String myOscarId) {
      String sql = "";
      String providerNo = "";
      ResultSet rs = null;
      DBHandler db;
      
      try {
          
          
          sql = "SELECT provider_no FROM property WHERE name = '" + strColName + "' AND value = '" + myOscarId+ "'";
          MiscUtils.getLogger().debug(sql);
          rs = DBHandler.GetSQL(sql);
          
          if( rs.next() ) {
              providerNo = oscar.Misc.getString(rs, "provider_no");
          }
      } catch( SQLException ex ) {
          MiscUtils.getLogger().error("Error", ex);
      }
      return providerNo;
  }
  
    public static List<String> listMyOscarProviderNums() {
      String sql;
      String myOscarId = "";
      ArrayList providerList = new ArrayList();
      oscar.oscarProvider.data.ProviderData.getProviderName("sdf");
      try {
          

          sql = "SELECT provider_no FROM property WHERE name = '" + strColName + "'";
          ResultSet rs = DBHandler.GetSQL(sql);
           
          while (rs.next()) {
              providerList.add(rs.getString("provider_no"));
          }
   
      } catch (SQLException sqe) {
          MiscUtils.getLogger().error("Error", sqe);        
      }
      
      return providerList;
    }
}
