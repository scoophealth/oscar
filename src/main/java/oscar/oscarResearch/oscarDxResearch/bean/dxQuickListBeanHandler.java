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


package oscar.oscarResearch.oscarDxResearch.bean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;

import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;
import oscar.oscarDB.DBHandler;

public class dxQuickListBeanHandler {
    
    Vector dxQuickListBeanVector = new Vector();
    String lastUsedQuickList = "default";
     
    public dxQuickListBeanHandler(String providerNo) {
        init(providerNo);
    } 
    
    public dxQuickListBeanHandler(String providerNo,String codingSystem) {
        init(providerNo,codingSystem);
    } 
    
    public dxQuickListBeanHandler() {
        init();
    }
    
    public boolean init(String providerNo){
        return init(providerNo,null);
    }
    
    public boolean init(String providerNo,String codingSystem) {
        
        String codSys = "";
        if ( codingSystem != null ){
            codSys = " where codingSystem = '"+codingSystem+"' ";
        } 
        
        boolean verdict = true;
        try {
          
            ResultSet rs;
            String quickListName;
           
           
            
            String lastUsed = "";
            String sql;
            
            lastUsed = OscarProperties.getInstance().getProperty("DX_QUICK_LIST_DEFAULT");
            if( lastUsed == null ) {
                sql = "SELECT DISTINCT quickListName FROM quickList ORDER BY quickListName LIMIT 1";
                rs = DBHandler.GetSQL(sql);
                if(rs.next()) 
                    lastUsed = oscar.Misc.getString(rs, "quickListName");                                           

                rs.close();
            }

            sql = "Select quickListName, createdByProvider from quickList "+codSys+" group by quickListName";            
            rs = DBHandler.GetSQL(sql);
            while(rs.next()){                
                dxQuickListBean bean = new dxQuickListBean(oscar.Misc.getString(rs, "quickListName"),
                                                           oscar.Misc.getString(rs, "createdByProvider"));
                quickListName = oscar.Misc.getString(rs, "quickListName");
                                    
                if(lastUsed.equals(quickListName)){

                    bean.setLastUsed("Selected");
                    lastUsedQuickList = oscar.Misc.getString(rs, "quickListName");
                }                
                dxQuickListBeanVector.add(bean);
            }
            rs.close();
        }
        catch(SQLException e) {
            MiscUtils.getLogger().error("Error", e);
            verdict = false;
        }
        return verdict;
    }

    public boolean init() {
        
        boolean verdict = true;
        try {
            
            ResultSet rs;
            
            
           
            String sql = "SELECT DISTINCT quickListName FROM quickList ORDER BY quickListName"; 

            rs = DBHandler.GetSQL(sql);
            while(rs.next()){                
                dxQuickListBean bean = new dxQuickListBean(oscar.Misc.getString(rs, "quickListName"));                              
                dxQuickListBeanVector.add(bean);
            }
            rs.close();
        }
        catch(SQLException e) {
            MiscUtils.getLogger().error("Error", e);
            verdict = false;
        }
        return verdict;
    }
    
    public Collection getDxQuickListBeanVector(){
        return dxQuickListBeanVector;
    }
    
    public String getLastUsedQuickList(){
        return lastUsedQuickList;
    }
}
