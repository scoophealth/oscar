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
import oscar.oscarResearch.oscarDxResearch.util.*;

public class dxResearchBeanHandler {
    
    Vector dxResearchBeanVector = new Vector();
 
    public dxResearchBeanHandler(String demographicNo) {
        init(demographicNo);
    }
    
    public boolean init(String demographicNo) {
        
        boolean verdict = true;
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            
            dxResearchCodingSystem codingSys = new dxResearchCodingSystem();
            String codingSystem = codingSys.getCodingSystem();        
            
            String sql = "select d.start_date, d.update_date, c.description, c."+codingSystem+", d.dxresearch_no, d.status from dxresearch d, "+codingSystem+" c " +
                         "where d.dxresearch_code=c."+codingSystem+" and d.status<>'D' and d.demographic_no ='"+ demographicNo +"' and d.coding_system = '"+codingSystem+"'"
                        +" order by d.start_date desc, d.update_date desc";
            //System.out.println("Sql Statement: " + sql);
            ResultSet rs;
            for(rs = db.GetSQL(sql); rs.next(); )
            {                
                dxResearchBean bean = new dxResearchBean(   rs.getString("description"), 
                                                            rs.getString("dxresearch_no"),
                                                            rs.getString(codingSystem),
                                                            rs.getString("update_date"),
                                                            rs.getString("start_date"),
                                                            rs.getString("status"),
                                                            codingSystem);
                dxResearchBeanVector.add(bean);
                //System.out.println("ichppcode obtained: " + rs.getString("ichppccode") + " " + rs.getString("description"));                
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

    public Vector getDxResearchBeanVector(){
        return dxResearchBeanVector;
    }
    
    public Vector getActiveCodeList(){ //TODO: NEED TO CHECK STATUS
        Vector v = new Vector();
        for (int i = 0; i < dxResearchBeanVector.size(); i++){
            dxResearchBean dx = (dxResearchBean) dxResearchBeanVector.get(i);
            if( !v.contains(dx.getDxSearchCode())){
               v.add(dx.getDxSearchCode()); 
            }
        }
        return v;
    }
}

