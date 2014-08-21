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


package oscar.oscarReport.pageUtil;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;
import oscar.oscarReport.bean.RptByExampleQueryBeanHandler;

public class RptByExamplesFavoriteAction extends Action {

    
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
                                                    
                        
                        String sql = "SELECT * from reportByExamplesFavorite WHERE query LIKE '" + StringEscapeUtils.escapeSql(frm.getNewQuery()) + "'";
                        MiscUtils.getLogger().debug("HERE "+sql);
                        ResultSet rs = DBHandler.GetSQL(sql);
                        if(rs.next())
                            frm.setFavoriteName(rs.getString("name"));
                    }
                    catch(SQLException e) {
                        MiscUtils.getLogger().error("Error", e);            
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
            MiscUtils.getLogger().debug("STEP:1 "+frm.getQuery());
            String favoriteName = frm.getFavoriteName();
            String query = frm.getQuery();   
            //oscar.oscarReport.data.RptByExampleData exampleData  = new oscar.oscarReport.data.RptByExampleData();
            //String queryWithEscapeChar = exampleData.replaceSQLString ("\"","\'",query);
           
            StringEscapeUtils strEscUtils = new StringEscapeUtils();                                
            String queryWithEscapeChar = StringEscapeUtils.escapeSql(query   );///queryWithEscapeChar);
            MiscUtils.getLogger().debug("escapeSql: " + queryWithEscapeChar);
            write2Database(providerNo, favoriteName, queryWithEscapeChar);            
        }
        RptByExampleQueryBeanHandler hd = new RptByExampleQueryBeanHandler(providerNo);  
        request.setAttribute("allFavorites", hd);  
        return mapping.findForward("success");        
    }        
    
    public void write2Database(String providerNo, String favoriteName, String query){
        if (query!=null && query.compareTo("")!=0){
            try {
            	favoriteName = StringEscapeUtils.escapeSql(favoriteName);
                MiscUtils.getLogger().debug("Fav "+favoriteName+" query "+query);
                
                String sql = "SELECT * from reportByExamplesFavorite WHERE providerNo = '"+providerNo+"' and name LIKE '" + favoriteName + "' OR query LIKE '" + query + "'";
                ResultSet rs = DBHandler.GetSQL(sql);
                if(!rs.next()){
                    sql = "INSERT INTO reportByExamplesFavorite(providerNo, name, query) VALUES('" + providerNo + "','" 
                          + favoriteName + "','" + query + "')";
                    DBHandler.RunSQL(sql);
                }
                else{
                    sql = "UPDATE reportByExamplesFavorite SET name='" + favoriteName + "', query='" + query + 
                          "' WHERE id ='" + rs.getString("id") + "'";
                    DBHandler.RunSQL(sql);
                }
            }
            catch(SQLException e) {
                MiscUtils.getLogger().error("Error", e);            
            }
        }
    }
    
    public void deleteQuery(String id){
        
        try {
                               

            String sql = "DELETE FROM reportByExamplesFavorite WHERE id = '" + id + "'";                
            DBHandler.RunSQL(sql);
        }
        catch(SQLException e) {
            MiscUtils.getLogger().error("Error", e);            
        }

    }
}
