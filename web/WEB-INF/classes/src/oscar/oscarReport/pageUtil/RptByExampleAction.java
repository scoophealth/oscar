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
import java.sql.SQLException;
import java.util.Properties;
import java.util.Collection;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.apache.commons.lang.StringEscapeUtils;

import oscar.oscarReport.data.*;
import oscar.oscarReport.bean.*;
import oscar.oscarDB.DBHandler;
import oscar.oscarEncounter.pageUtil.EctSessionBean;


public class RptByExampleAction extends Action {

    Properties oscarVariables = null;
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {   
        RptByExampleForm frm = (RptByExampleForm) form;        
        
        if(request.getSession().getAttribute("user") == null)
            response.sendRedirect("../logout.htm");        
               
        String providerNo = (String) request.getSession().getAttribute("user");
        RptByExampleQueryBeanHandler hd = new RptByExampleQueryBeanHandler();  
        Collection favorites = hd.getFavoriteCollection(providerNo);       
        request.setAttribute("favorites", favorites);        
                
        String bgcolor = "#ddddff";
        String sql = frm.getSql();
        String pros = "";
        
        if (sql!= null){            
            write2Database(sql, providerNo);
        }
        else
            sql = "";
        
        oscar.oscarReport.data.RptByExampleData exampleData  = new oscar.oscarReport.data.RptByExampleData();
        Properties proppies = (Properties)  request.getSession().getAttribute("oscarVariables");

        String results = exampleData.exampleReportGenerate(sql, proppies)==null?null: exampleData.exampleReportGenerate(sql, proppies);
        String resultText = exampleData.exampleTextGenerate(sql, proppies)==null?null: exampleData.exampleTextGenerate(sql, proppies);

        request.setAttribute("results", results);
        request.setAttribute("resultText", resultText);
        
        return mapping.findForward("success");
    }
    
    public void write2Database(String query, String providerNo){
        if (query!=null && query.compareTo("")!=0){
            try {
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                oscar.oscarReport.data.RptByExampleData exampleData  = new oscar.oscarReport.data.RptByExampleData();
                StringEscapeUtils strEscUtils = new StringEscapeUtils();
                
                //query = exampleData.replaceSQLString (";","",query);
                //query = exampleData.replaceSQLString("\"", "\'", query);            

                query = strEscUtils.escapeSql(query);
                
                String sql = "INSERT INTO reportByExamples(providerNo, query, date) VALUES('" + providerNo + "','" + query + "', NOW())";
                db.RunSQL(sql);

                db.CloseConn();
            }
            catch(SQLException e) {
                System.out.println(e.getMessage());            
            }
        }
    }
}

