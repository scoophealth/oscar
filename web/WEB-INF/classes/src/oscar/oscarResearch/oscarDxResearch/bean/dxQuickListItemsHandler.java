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

import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.Collection;
import oscar.oscarDB.DBHandler;

public class dxQuickListItemsHandler {
    
    Vector dxQuickListItemVector = new Vector();
     
    public dxQuickListItemsHandler(String quickListName, String providerNo) {
        init(quickListName, providerNo);
    }    
        
    public boolean init(String quickListName, String providerNo) {
        
        boolean verdict = true;
        try {
            ResultSet rs;
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            //need to put the providerID as well
            String sql = "Update quickListUser set lastUsed=now() where quickListName='"+quickListName + "' AND providerNo ='"+providerNo+"'";
            db.RunSQL(sql);
            
            sql = "Select q.dxResearchCode, i.description FROM quickList q, ichppccode i where quickListName='"+ quickListName +"' AND i.ichppccode = q.dxResearchCode order by i.description";
            rs = db.GetSQL(sql);            
            while(rs.next()){                
                dxCodeSearchBean bean = new dxCodeSearchBean(rs.getString("description"),
                                                             rs.getString("dxResearchCode"));                
                dxQuickListItemVector.add(bean);
            }
            rs.close();
            db.CloseConn();
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
            verdict = false;
        }
        return verdict;
    }

    public Collection getDxQuickListItemVector(){
        return dxQuickListItemVector;
    }
}

