// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
// * This software is published under the GPL GNU General Public License. 
// * This program is free software; you can redistribute it and/or 
// * modify it under the terms of the GNU General Public License 
// * as published by the Free Software Foundation; either version 2 
// * of the License, or (at your option) any later version. * 
// * This program is distributed in the hope that it will be useful, 
// * but WITHOUT ANY WARRANTY; without even the implied warranty of 
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
// * along with this program; if not, write to the Free Software 
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
// * 
// * <OSCAR TEAM>
// * This software was written for the 
// * Department of Family Medicine 
// * McMaster Unviersity 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar.oscarResearch.oscarDxResearch.bean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;

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
        int NameLength = 10; //max length of quicklistname in quicklistuser table
        String codSys = "";
        if ( codingSystem != null ){
            codSys = " where codingSystem = '"+codingSystem+"' ";
        } 
        
        boolean verdict = true;
        try {
            ResultSet rsLastUsed;
            ResultSet rs;
            String quickListName;
            boolean truncateName = false;
            
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String lastUsed = "";
            String sql;
            
            lastUsed = OscarProperties.getInstance().getProperty("DX_QUICK_LIST_DEFAULT");
            if( lastUsed == null ) {
                sql = "SELECT DISTINCT quickListName FROM quickList ORDER BY quickListName LIMIT 1";
                rs = db.GetSQL(sql);
                if(rs.next()) 
                    lastUsed = db.getString(rs,"quickListName");                                           

                rs.close();
            }

            sql = "Select quickListName, createdByProvider from quickList "+codSys+" group by quickListName";            
            rs = db.GetSQL(sql);
            while(rs.next()){                
                dxQuickListBean bean = new dxQuickListBean(db.getString(rs,"quickListName"),
                                                           db.getString(rs,"createdByProvider"));
                quickListName = db.getString(rs,"quickListName");
                                    
                if(lastUsed.equals(quickListName)){
                    //System.out.println("lastused: " + lastUsed);
                    bean.setLastUsed("Selected");
                    lastUsedQuickList = db.getString(rs,"quickListName");
                }                
                dxQuickListBeanVector.add(bean);
            }
            rs.close();
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
            verdict = false;
        }
        return verdict;
    }

    public boolean init() {
        
        boolean verdict = true;
        try {
            ResultSet rsLastUsed;
            ResultSet rs;
            
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String lastUsed = "";
            String sql = "SELECT DISTINCT quickListName FROM quickList ORDER BY quickListName"; 
            //System.out.println(sql);
            rs = db.GetSQL(sql);
            while(rs.next()){                
                dxQuickListBean bean = new dxQuickListBean(db.getString(rs,"quickListName"));                              
                dxQuickListBeanVector.add(bean);
            }
            rs.close();
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
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

