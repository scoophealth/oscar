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
package oscar.oscarMDS.data;

import java.sql.ResultSet;
import java.util.ArrayList;

import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;

public class ProviderData {
    
    public ProviderData(String refDoctor, String conDoctor, String admDoctor) {
        referringDoctor = beautifyProviderName(refDoctor);
        consultingDoctor = beautifyProviderName(conDoctor);
        admittingDoctor = beautifyProviderName(admDoctor);
    }
    
    public String referringDoctor;
    public String consultingDoctor;
    public String admittingDoctor;
    
    public static String beautifyProviderName(String name) {
        String[] subStrings;
        
        if (name.length() > 0) {
            try {
                subStrings = name.split("\\^");
                if ( subStrings.length >= 18 ) {
                    return subStrings[5] + " " + subStrings[1] +", " + subStrings[17] + " "+ subStrings[13];
                } else if (subStrings.length >= 14) {
                    return subStrings[5] + " " + subStrings[1] +", " + subStrings[13];
                } else if ( subStrings.length >= 6 ) {
                    return subStrings[5] + " " + subStrings[1];
                } else {
                    return subStrings[1];
                }
            } catch (Exception e) {
                MiscUtils.getLogger().debug("Error in ProviderData: "+e.toString());
                return name.replace('^', ' ');
            }
        } else {
            return "";
        }
        
    }
    
    public static ArrayList getProviderList () {
        try {            
            
            ArrayList result = new ArrayList();
            
            String sql = "select provider_no, first_name, last_name from provider where provider_type='doctor' order by last_name , first_name";
            ResultSet rs = DBHandler.GetSQL(sql);            
            while ( rs.next() ) {
                ArrayList provider = new ArrayList();
                provider.add(oscar.Misc.getString(rs, "provider_no"));
                provider.add(oscar.Misc.getString(rs, "first_name"));
                provider.add(oscar.Misc.getString(rs, "last_name"));
                result.add(provider);
            }
            return result;
        }catch(Exception e){
            MiscUtils.getLogger().debug("exception in ProviderData:"+e);
            return null;
        }        
    }
    
    
    public static ArrayList getProviderListWithLabNo () {
        try {            
            
            ArrayList result = new ArrayList();
            
            String sql = "select provider_no, first_name, last_name from provider where provider_type='doctor'  and ohip_no != '' order by last_name , first_name";
            ResultSet rs = DBHandler.GetSQL(sql);            
            while ( rs.next() ) {
                ArrayList provider = new ArrayList();
                provider.add(oscar.Misc.getString(rs, "provider_no"));
                provider.add(oscar.Misc.getString(rs, "first_name"));
                provider.add(oscar.Misc.getString(rs, "last_name"));
                result.add(provider);
            }
            return result;
        }catch(Exception e){
            MiscUtils.getLogger().debug("exception in ProviderData:"+e);
            return null;
        }        
    }
    
    public static String getProviderName(String providerNo) {
           try {
            
            
                                    
            String sql = "select first_name, last_name from provider where provider_no='"+providerNo+"'";
            ResultSet rs = DBHandler.GetSQL(sql);            
            if ( rs.next() ) {            
                return ( oscar.Misc.getString(rs, "first_name") + " " + oscar.Misc.getString(rs, "last_name") );            
            } else {                            
                return "";
            }
        }catch(Exception e){
            MiscUtils.getLogger().debug("exception in ProviderData:"+e);
            return null;
        }        
    }
}
