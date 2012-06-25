/**
 * Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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
 */

package oscar.oscarProvider.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;

/**
 *
 * @author Toby
 */
public class ProviderBillCenter {
    
    /** Creates a new instance of ProviderBillCenter */
    public ProviderBillCenter() {
    }
    
    public boolean hasBillCenter(String provider_no){
        boolean retval = false;
        try {
            
            String sql = "select billcenter_code from providerbillcenter where provider_no = '"+provider_no+"' ";
            ResultSet rs = DBHandler.GetSQL(sql);
            if(rs.next())
                retval = true;
            rs.close();
        } catch(SQLException e) {
            MiscUtils.getLogger().debug("There has been an error while checking if a provider had a bill center");
            MiscUtils.getLogger().error("Error", e);
        }
        
        return retval;
    }

    public boolean hasProvider(String provider_no){
        boolean retval = false;
        try {
            
            String sql = "select provider_no from providerbillcenter where provider_no = '"+provider_no+"' ";
            ResultSet rs = DBHandler.GetSQL(sql);
            if(rs.next())
                retval = true;
            rs.close();
        } catch(SQLException e) {
            MiscUtils.getLogger().debug("There has been an error while checking if a provider had a bill center");
            MiscUtils.getLogger().error("Error", e);
        }

        return retval;
    }
    
    public void addBillCenter(String provider_no, String billCenterCode){
        
        try{
            
            String sql = "insert into  providerbillcenter (provider_no,billcenter_code) values ('"+provider_no+"' ,'"+billCenterCode+"') ";
            DBHandler.RunSQL(sql);
        } catch(SQLException e){
            MiscUtils.getLogger().debug("There has been an error while adding a provider's bill center");
            MiscUtils.getLogger().error("Error", e);
        }
    }
    
    public String getBillCenter(String provider_no){
        String billCenterCode = "";
        try{
            
            String sql = "select billcenter_code from providerbillcenter where provider_no = '"+provider_no+"' ";
            ResultSet rs = DBHandler.GetSQL(sql);
            if(rs.next())
                billCenterCode = oscar.Misc.getString(rs, "billcenter_code");
            rs.close();
        } catch(SQLException e){
            MiscUtils.getLogger().debug("There has been an error while retrieving a provider's bill center");
            MiscUtils.getLogger().error("Error", e);
        }
        
        return billCenterCode;
    }
    
    public void updateBillCenter(String provider_no, String billCenterCode){
        if (!hasProvider(provider_no)) {
            addBillCenter(provider_no, billCenterCode);
        } else {
            try {
                
                String sql = "update providerbillcenter set billcenter_code = '" + billCenterCode + "' where provider_no = '" + provider_no + "' ";
                DBHandler.RunSQL(sql);
            } catch (SQLException e) {
                MiscUtils.getLogger().debug("There has been an error while updating a provider's bill center");
                MiscUtils.getLogger().error("Error", e);
            }
        }    
    }
    
    public Properties getAllBillCenter(){
        Properties allBillCenter = new Properties();
        
        try{
            
            String sql = "select * from billcenter" ;
            ResultSet rs = DBHandler.GetSQL(sql);
            while(rs.next())
                allBillCenter.setProperty(oscar.Misc.getString(rs, "billcenter_code"),oscar.Misc.getString(rs, "billcenter_desc")) ;
            rs.close();
        } catch(SQLException e){
            MiscUtils.getLogger().debug("There has been an error while retrieving info from table billcenter");
            MiscUtils.getLogger().error("Error", e);
        }
        
        return allBillCenter;
    }
    
}
