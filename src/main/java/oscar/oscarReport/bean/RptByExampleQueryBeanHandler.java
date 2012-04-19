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


package oscar.oscarReport.bean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;

import org.apache.commons.lang.StringEscapeUtils;
import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;

public class RptByExampleQueryBeanHandler {
    
    Vector favoriteVector = new Vector();
    Vector allQueryVector = new Vector();
    Vector queryVector = new Vector();
    String startDate;
    String endDate;   
    
    public RptByExampleQueryBeanHandler() {       
    }
    
    public RptByExampleQueryBeanHandler(String startDate, String endDate) {   
        this.startDate = startDate;
        this.endDate = endDate;
    }
    
    public RptByExampleQueryBeanHandler(String providerNo) {       
        getFavoriteCollection(providerNo);
    }
        
    public Collection getFavoriteCollection(String providerNo){
        try {
            
            String sql = "SELECT * from reportByExamplesFavorite WHERE providerNo='" + providerNo + "' ORDER BY name";
            MiscUtils.getLogger().debug("Sql Statement: " + sql);
            ResultSet rs;
            
            for(rs = DBHandler.GetSQL(sql); rs.next(); )
            {
            
                    StringEscapeUtils strEscUtils = new StringEscapeUtils();                                                   
                    String queryWithEscapeChar = StringEscapeUtils.escapeJava(oscar.Misc.getString(rs, "query"));                   
                    //oscar.oscarReport.data.RptByExampleData exampleData  = new oscar.oscarReport.data.RptByExampleData();
                    //queryWithEscapeChar = exampleData.replaceSQLString (";","",queryWithEscapeChar);
                    //queryWithEscapeChar = exampleData.replaceSQLString("\"", "\'", queryWithEscapeChar);            

                    String queryNameWithEscapeChar = StringEscapeUtils.escapeJava(oscar.Misc.getString(rs, "name"));
                    RptByExampleQueryBean query = new RptByExampleQueryBean(rs.getInt("id"), oscar.Misc.getString(rs, "query"), oscar.Misc.getString(rs, "name"));
                    favoriteVector.add(query);                             
            }

            rs.close();
        }
        catch(SQLException e) {
            MiscUtils.getLogger().error("Error", e);            
        }
        return favoriteVector;
    }
    
    public Vector getFavoriteVector(){
        return favoriteVector;
    }
    
    public Collection getAllQueryVector(){
        try {
            
            String sql = "SELECT r.query, r.date, p.last_name, p.first_name from reportByExamples r, provider p WHERE r.providerNo=p.provider_No ORDER BY date DESC";
            MiscUtils.getLogger().debug("Sql Statement: " + sql);
            ResultSet rs;
            for(rs = DBHandler.GetSQL(sql); rs.next(); )
            {
                //StringEscapeUtils strEscUtils = new StringEscapeUtils();                                
                //String queryWithEscapeChar = strEscUtils.escapeJava(oscar.Misc.getString(rs,"query"));
                RptByExampleQueryBean query = new RptByExampleQueryBean(oscar.Misc.getString(rs, "last_name"), oscar.Misc.getString(rs, "first_name"), oscar.Misc.getString(rs, "query"), oscar.Misc.getString(rs, "date"));
                allQueryVector.add(query);
            }

            rs.close();
        }
        catch(SQLException e) {
            MiscUtils.getLogger().error("Error", e);            
        }
        return allQueryVector;
    }
    
    public Vector getQueryVector(){
        try {
            
            String sql = "SELECT r.query, r.date, p.last_name, p.first_name from reportByExamples r, provider p "
                         + "WHERE r.providerNo=p.provider_No AND date>= '" + startDate + "' AND date <='" + endDate
                         + "' ORDER BY date DESC";
            MiscUtils.getLogger().debug("Sql Statement: " + sql);
            ResultSet rs;
            for(rs = DBHandler.GetSQL(sql); rs.next(); )
            {

                StringEscapeUtils strEscUtils = new StringEscapeUtils();                                
                String queryWithEscapeChar = StringEscapeUtils.escapeJava(oscar.Misc.getString(rs, "query"));
                RptByExampleQueryBean query = new RptByExampleQueryBean(oscar.Misc.getString(rs, "last_name"), oscar.Misc.getString(rs, "first_name"), oscar.Misc.getString(rs, "query"), oscar.Misc.getString(rs, "date"));
                queryVector.add(query);
            }

            rs.close();
        }
        catch(SQLException e) {
            MiscUtils.getLogger().error("Error", e);            
        }
        return queryVector;
    }
}
