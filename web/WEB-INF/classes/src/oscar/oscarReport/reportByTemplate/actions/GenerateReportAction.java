//This action generates the report after the user filled in all the params

/*
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
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */

package oscar.oscarReport.reportByTemplate.actions;


import org.apache.struts.action.*;
import javax.servlet.http.*;
import java.util.*;
import oscar.oscarReport.reportByTemplate.*;
import java.sql.*;
import java.io.*;
import oscar.oscarDB.DBHandler;
import oscar.oscarReport.data.RptResultStruct;
import oscar.util.UtilMisc;
import com.Ostermiller.util.CSVPrinter;
/**
 * Created on December 21, 2006, 10:47 AM
 * @author apavel (Paul)
 */
public class GenerateReportAction extends Action {
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) {
        String templateId = request.getParameter("templateId");
        ReportObject curReport = (new ReportManager()).getReportTemplateNoParam(templateId);
        Map parameterMap = request.getParameterMap();
        String sql = curReport.getPreparedSQL(parameterMap);
        if (sql == "" || sql == null) {
            request.setAttribute("errormsg", "Error: Cannot find all parameters for the query.  Check the template.");
            request.setAttribute("templateid", templateId);
            return mapping.findForward("fail");
        }
        ResultSet rs = null;
        String rsHtml = "An SQL querry error has occured";
        String csv = "";
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            rs = db.GetSQL(sql);
            db.CloseConn();
            rsHtml = RptResultStruct.getStructure2(rs);  //makes html from the result set
            StringWriter swr = new StringWriter();
            CSVPrinter csvp = new CSVPrinter(swr);
            csvp.writeln(UtilMisc.getArrayFromResultSet(rs));
            csv = swr.toString();
            //csv = csv.replace("\\", "\"");  //natural quotes in the data create '\' characters in CSV, xls works fine
                                              //this line fixes it but messes up XLS generation.
            //csv = UtilMisc.getCSV(rs);
        } catch (Exception sqe) {
            sqe.printStackTrace();
        }
        request.setAttribute("csv", csv);
        request.setAttribute("sql", sql);
        request.setAttribute("reportobject", curReport);
        request.setAttribute("resultsethtml", rsHtml);
        return mapping.findForward("success");
    }
    
}