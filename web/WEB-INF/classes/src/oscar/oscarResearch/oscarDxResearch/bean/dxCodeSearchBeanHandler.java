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

public class dxCodeSearchBeanHandler {
    
    Vector dxCodeSearchBeanVector = new Vector();
 
    public dxCodeSearchBeanHandler(String[] keywords) {
        init(keywords);
    }
    
    public boolean init(String[] keywords) {
        
        boolean verdict = true;
        try {
            ResultSet rs;
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "";
            boolean orFlag = false;
            for(int i=0; i<keywords.length; i++){
                if(!keywords[i].equals("")){
                    if (!orFlag){
                        sql = "select ichppccode, description from ichppccode where ichppccode like '%" + keywords[i] + "%' or description like '%" + keywords[i] +"%' ";
                        orFlag = true;
                    }
                    else
                        sql = sql + "or ichppccode like '%" + keywords[i] + "%' or description like '%" + keywords[i] +"%' ";
                }
            }
            System.out.println("Sql Statement: " + sql);  
            rs = db.GetSQL(sql);
            while(rs.next()){
                dxCodeSearchBean bean = new dxCodeSearchBean(rs.getString("description"),                                                                 
                                                             rs.getString("ichppccode"));
                for(int i=0; i<keywords.length; i++){
                    if(keywords[i].equals(rs.getString("ichppccode")))
                        bean.setExactMatch("checked");                    
                }
                

                dxCodeSearchBeanVector.add(bean);
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

    public Vector getDxCodeSearchBeanVector(){
        return dxCodeSearchBeanVector;
    }
}

