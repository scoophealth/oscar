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
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
package oscar.oscarReport.pageUtil;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import java.util.Properties;
import java.util.Collection;
import oscar.oscarReport.data.*;
import oscar.oscarReport.bean.*;
import oscar.oscarDB.DBHandler;
import oscar.oscarEncounter.pageUtil.EctSessionBean;
import org.apache.commons.lang.StringEscapeUtils;

public class RptByExamplesFavoriteAction extends Action {

    Properties oscarVariables = null;
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {   
        RptByExamplesFavoriteForm frm = (RptByExamplesFavoriteForm) form;     
        String providerNo = (String) request.getSession().getAttribute("user");
        if(frm.getNewQuery()!=null){
            if(frm.getNewQuery().compareTo("")!=0){                
                frm.setQuery(frm.getNewQuery());
                if(frm.getNewName()!=null)
                    frm.setFavoriteName(frm.getNewName());
                else{
                    try {
                        DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);                            

                        String sql = "SELECT * from reportByExamplesFavorite WHERE query LIKE '" + frm.getNewQuery() + "'";
                        ResultSet rs = db.GetSQL(sql);
                        if(rs.next())
                            frm.setFavoriteName(rs.getString("name"));                                       
                        db.CloseConn();
                    }
                    catch(SQLException e) {
                        System.out.println(e.getMessage());            
                    }
                }
                return mapping.findForward("edit");    
            }
            else if(frm.getToDelete()!=null){
                if(frm.getToDelete().compareTo("true")==0){
                    deleteQuery(frm.getId());                    
                }
            }
        }
        else{
            String favoriteName = frm.getFavoriteName();
            String query = frm.getQuery();   
            oscar.oscarReport.data.RptByExampleData exampleData  = new oscar.oscarReport.data.RptByExampleData();       
            String queryWithEscapeChar = exampleData.replaceSQLString ("\"","\'",query);
           
            StringEscapeUtils strEscUtils = new StringEscapeUtils();                                
            queryWithEscapeChar = strEscUtils.escapeSql(queryWithEscapeChar);
            System.out.println("escapeSql: " + queryWithEscapeChar);
            write2Database(providerNo, favoriteName, queryWithEscapeChar);            
        }
        RptByExampleQueryBeanHandler hd = new RptByExampleQueryBeanHandler(providerNo);  
        request.setAttribute("allFavorites", hd);  
        return mapping.findForward("success");        
    }        
    
    public void write2Database(String providerNo, String favoriteName, String query){
        if (query!=null && query.compareTo("")!=0){
            try {
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                
                //StringEscapeUtils strEscUtils = new StringEscapeUtils();
                                
                //query = strEscUtils.escapeSql(query);
                
                String sql = "SELECT * from reportByExamplesFavorite WHERE name LIKE '" + favoriteName + "' OR query LIKE '" + query + "'";
                ResultSet rs = db.GetSQL(sql);
                if(!rs.next()){
                    sql = "INSERT INTO reportByExamplesFavorite(providerNo, name, query) VALUES('" + providerNo + "','" 
                          + favoriteName + "','" + query + "')";
                    db.RunSQL(sql);
                }
                else{
                    sql = "UPDATE reportByExamplesFavorite SET name='" + favoriteName + "', query='" + query + 
                          "' WHERE id ='" + rs.getString("id") + "'";
                    db.RunSQL(sql);
                }
                db.CloseConn();
            }
            catch(SQLException e) {
                System.out.println(e.getMessage());            
            }
        }
    }
    
    public void deleteQuery(String id){
        
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);                   

            String sql = "DELETE FROM reportByExamplesFavorite WHERE id = '" + id + "'";                
            db.RunSQL(sql);

            db.CloseConn();
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());            
        }

    }
}

