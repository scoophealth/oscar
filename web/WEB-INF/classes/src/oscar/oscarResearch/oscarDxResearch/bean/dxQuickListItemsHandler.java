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
// * McMaster University 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar.oscarResearch.oscarDxResearch.bean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;

import oscar.oscarDB.DBHandler;
import oscar.oscarResearch.oscarDxResearch.util.dxResearchCodingSystem;

public class dxQuickListItemsHandler {
    
    Vector dxQuickListItemsVector = new Vector();
     
    public dxQuickListItemsHandler(String quickListName, String providerNo) {
        init(quickListName, providerNo);
    } 
    
    public dxQuickListItemsHandler(String quickListName) {
        init(quickListName);
    } 
        
    public boolean init(String quickListName, String providerNo) {
        int ListNameLen = 10;
        boolean verdict = true;
        try {
            ResultSet rs;
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            
            dxResearchCodingSystem codingSys = new dxResearchCodingSystem();
            String[] codingSystems = codingSys.getCodingSystems(); 
            String codingSystem;
            String name;
            
            if( quickListName.length() > ListNameLen )
                name = quickListName.substring(0,ListNameLen);
            else
                name = quickListName;
            
            //need to put the providerID as well
            String sql = "Select quickListName, providerNo from quickListUser where quickListName='"+name + "' AND providerNo ='"+providerNo+"'";
            rs = db.GetSQL(sql);
            if(rs.next()){
                sql = "Update quickListUser set lastUsed=now() where quickListName='"+name + "' AND providerNo ='"+providerNo+"'";
                db.RunSQL(sql);
            }
            else{
                sql = "Insert into quickListUser(quickListName, providerNo, lastUsed) VALUES ('"+name+"','"+providerNo+"',now())";
                db.RunSQL(sql);
            }
            
            for( int idx = 0; idx < codingSystems.length; ++idx )
            {
                codingSystem = codingSystems[idx];
                sql = "Select q.dxResearchCode, c.description FROM quickList q, "+codingSystem+" c where codingSystem = '"+codingSystem+"' and quickListName='"+ quickListName +"' AND c."+codingSystem+" = q.dxResearchCode order by c.description";
            
                rs = db.GetSQL(sql);            
                while(rs.next()){                
                    dxCodeSearchBean bean = new dxCodeSearchBean(db.getString(rs,"description"),
                                                             db.getString(rs,"dxResearchCode"));
                    bean.setType(codingSystem);
                    dxQuickListItemsVector.add(bean);
                }
                rs.close();
            }
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
            verdict = false;
        }
        return verdict;
    }

    public boolean init(String quickListName) {
        
        boolean verdict = true;
        try {            
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            dxResearchCodingSystem codingSys = new dxResearchCodingSystem();
            String[] codingSystems = codingSys.getCodingSystems(); 
            String codingSystem;
            String sql;
            
            for( int idx = 0; idx < codingSystems.length; ++idx ) {
                codingSystem = codingSystems[idx];
                sql = "Select q.dxResearchCode, c.description FROM quickList q, "+codingSystem+" c where codingSystem = '"+codingSystem+"' and quickListName='"+ quickListName +"' AND c."+codingSystem+" = q.dxResearchCode order by c.description";           
                //System.out.println("when does this get called "+sql);
                ResultSet rs = db.GetSQL(sql);            
                while(rs.next()){                
                    dxCodeSearchBean bean = new dxCodeSearchBean(db.getString(rs,"description"),
                                                             db.getString(rs,"dxResearchCode")); 
                    bean.setType(codingSystem);
                    dxQuickListItemsVector.add(bean);
                }
                rs.close();
            }
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
            verdict = false;
        }
        return verdict;
    }
    
    public Collection getDxQuickListItemsVector(){
        return dxQuickListItemsVector;
    }
    
    public Collection getDxQuickListItemsVectorNotInPatientsList(Vector dxList){
        Vector v = new Vector();
        
        for ( int j = 0; j < dxQuickListItemsVector.size();j++){
            dxCodeSearchBean dxCod = (dxCodeSearchBean) dxQuickListItemsVector.get(j);            
            if(!dxList.contains(dxCod)){
                v.add(dxCod);
            }    
        }
        return v;
    }
    
    public Collection getDxQuickListItemsVectorInPatientsList(Vector dxList){
        Vector v = new Vector();
        for ( int j = 0; j < dxQuickListItemsVector.size();j++){
            dxCodeSearchBean dxCod = (dxCodeSearchBean) dxQuickListItemsVector.get(j);
            if(dxList.contains(dxCod.getDxSearchCode())){
                v.add(dxCod);
            }    
        }
        return v;
    }
}

