/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


package oscar.scratch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;

/**
 create table scratch_pad (
           id int(10) not null auto_increment primary key,
           provider_no varchar(6),
           date_time datetime,
           scratch_text text
       );
 * @author jay
 */
public class ScratchData {
    
    /** Creates a new instance of ScratchData */
    public ScratchData() {
    }
    
    public Hashtable getLatest(String providerNo){
        Hashtable retval = null;
        try {
            //Get Provider from database
            
            ResultSet rs;
            String sql = "SELECT * FROM scratch_pad WHERE provider_no = " + providerNo + " order by id  desc limit 1";
            rs = DBHandler.GetSQL(sql);
   
            if (rs.next()){
                retval = new Hashtable();
                retval.put("id",oscar.Misc.getString(rs, "id"));
                retval.put("text",oscar.Misc.getString(rs, "scratch_text"));
                retval.put("date",oscar.Misc.getString(rs, "date_time"));
            }
            rs.close();
        } catch (SQLException e) {
           MiscUtils.getLogger().error("Error", e);
        }
        return retval;
    }
    
    public String insert2(String providerNo,String text){
        String scratch_id = null;
        try {
            //Get Provider from database
            
            ResultSet rs;
            String sql = "INSERT into scratch_pad (provider_no, scratch_text,date_time ) values ('" + providerNo + "','"+text+"',now())";
            DBHandler.RunSQL(sql);
            rs = DBHandler.GetSQL("SELECT LAST_INSERT_ID() ");
   
            if(rs.next()){
               scratch_id = Integer.toString( rs.getInt(1) );
            }
            rs.close();
        } catch (SQLException e) {
           MiscUtils.getLogger().error("Error", e);
        }
        return scratch_id;
    }
    
    
    public String insert(String providerNo,String text){
        String scratch_id = null;
        try {
            //Get Provider from database
            // //unused variable db
             String sql = "INSERT into scratch_pad (provider_no, scratch_text,date_time ) values (?,?,now())";
             Connection conn = DbConnectionFilter.getThreadLocalDbConnection();
             PreparedStatement pstat = conn.prepareStatement(sql);
              pstat.setString(1,providerNo);
              pstat.setString(2,text);
              pstat.executeUpdate();
            
              ResultSet rs = pstat.getGeneratedKeys();
               if(rs.next()){
                  scratch_id = ""+rs.getInt(1);
               }
              rs.close();
             pstat.close();
        } catch (SQLException e) {
           MiscUtils.getLogger().error("Error", e);
        }
        return scratch_id;
    }
    
}
