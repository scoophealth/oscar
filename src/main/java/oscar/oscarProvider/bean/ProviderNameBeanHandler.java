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


package oscar.oscarProvider.bean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;

import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;
import oscar.oscarProvider.data.ProviderData;

public class ProviderNameBeanHandler {
    
    Vector providerNameVector = new Vector();
    Vector doctorNameVector = new Vector();
    Vector thisGroupProviderVector = new Vector();
 
    public ProviderNameBeanHandler() {
        init();
    }
    
    public boolean init() {
        
        boolean verdict = true;
        try {
            
            String sql = "SELECT DISTINCT provider_no, provider_type from provider ORDER BY last_name";

            ResultSet rs;
            for(rs = DBHandler.GetSQL(sql); rs.next(); )
            {
                ProviderData pData = new ProviderData(oscar.Misc.getString(rs, "provider_no"));
                ProviderNameBean pNameBean = new ProviderNameBean(pData.getLast_name() + ", " + pData.getFirst_name(), oscar.Misc.getString(rs, "provider_no"));
                providerNameVector.add(pNameBean);
                if(oscar.Misc.getString(rs, "provider_type").equalsIgnoreCase("doctor")){
                    doctorNameVector.add(pNameBean);

                }
                    
            }

            rs.close();
        }
        catch(SQLException e) {
            MiscUtils.getLogger().error("Error", e);
            verdict = false;
        }
        return verdict;
    }

    public Collection getProviderNameVector(){
        return providerNameVector;
    }
    
    public Collection getDoctorNameVector(){
        return doctorNameVector;
    }
    
    public void setThisGroupProviderVector(String groupNo){
        try{
            
            String sql = "select provider_no, last_name, first_name from mygroup where mygroup_no='" + groupNo + "' order by first_name";
            ResultSet rs;
            for(rs = DBHandler.GetSQL(sql); rs.next(); )
            {                
                ProviderNameBean pNameBean = new ProviderNameBean(oscar.Misc.getString(rs, "last_name") + ", " + oscar.Misc.getString(rs, "first_name"), oscar.Misc.getString(rs, "provider_no"));
                thisGroupProviderVector.add(pNameBean);
            }
        }
         catch(SQLException e) {
            MiscUtils.getLogger().error("Error", e);         
        }
    }
    
    public Collection getThisGroupProviderVector(){
        return thisGroupProviderVector;
    }
}
