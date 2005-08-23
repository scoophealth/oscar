/*
 * Copyright (c) 2005 - OpenSoft System . All Rights Reserved. * This software is published under
 * the GPL GNU General Public License. This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later version. * This
 * program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. * * You should have received a copy of the GNU General Public
 * License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA. * Yi Li
 */

package oscar.oscarReport.pageUtil;

import org.apache.log4j.Logger;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import oscar.oscarReport.data.RptReportConfigData;
import oscar.oscarReport.data.RptReportCreator;
import oscar.oscarReport.data.RptReportItem;

public class RptDownloadCSVServlet extends HttpServlet {
    private static final Logger _logger = Logger.getLogger(RptDownloadCSVServlet.class);

    //private static final String basePath = "/doc";

    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        HttpSession session = request.getSession(false);
        if (session == null)
            return;
        //if (session.getAttribute("user") == null
        //        || !(((String) session.getAttribute("userrole")).equalsIgnoreCase("admin")
        //                || ((String) session.getAttribute("userrole")).startsWith("admin,")
        //                || ((String) session.getAttribute("userrole")).endsWith(",admin") || (((String) session
        //                .getAttribute("userrole")).indexOf(",admin,") > 0)))
        //    return;

        ///////////////////////////////////////
        String VALUE = "value_";
        String DATE_FORMAT = "dateFormat_";
        String SAVE_AS = "default";
        String reportId = request.getParameter("id") != null ? request.getParameter("id") : "0";
        //         get form name
        String reportName = "";
        String in = "";
        String DELIMETER = "\t";
        try {
            reportName = (new RptReportItem()).getReportName(reportId);
            RptFormQuery formQuery = new RptFormQuery();
            String reportSql = formQuery.getQueryStr(reportId, request);
            //        System.out.println("SQL: " + reportSql);

            RptReportConfigData formConfig = new RptReportConfigData();
            Vector[] vecField = formConfig.getAllFieldNameValue(SAVE_AS, reportId);
            Vector vecFieldCaption = vecField[1];
            Vector vecFieldName = vecField[0];
            //        System.out.println("SQL: 1");
            Vector vecFieldValue = (new RptReportCreator()).query(reportSql, vecFieldCaption);
            //        System.out.println("SQL: 2");

            for (int i = 0; i < vecFieldCaption.size(); i++) {
                in += i == 0 ? "" : DELIMETER + (String) vecFieldCaption.get(i);
            }

            for (int i = 0; i < vecFieldValue.size(); i++) {
                Properties prop = (Properties) vecFieldValue.get(i);
                in += "\n";
                for (int j = 0; j < vecFieldCaption.size(); j++) {
                    in += j == 0 ? "" : DELIMETER + prop.getProperty((String) vecFieldCaption.get(j), "");
                }
            }
        } catch (Exception e1) {
            _logger.error("service() - form report");
        }
        ///////////////////////////////////////

        String filename = reportName + ".csv"; //request.getParameter("filename");
        OutputStream out = null;
        try {
            if (in != null) {
                out = new BufferedOutputStream(response.getOutputStream());
                byte[] b = in.getBytes();
                int len = b.length;
                int n = 0;
                int FIXED_LEN = 2048;
                String contentType = "application/unknow";
                System.out.println("contentType: " + contentType);
                response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
                while (n <= len - FIXED_LEN) {
                    out.write(b, n, FIXED_LEN); //out.write(b);
                    n += FIXED_LEN;
                }
                if (n > len - FIXED_LEN) {
                    out.write(b, n, len - n);
                }
            }
        } finally {
            if (out != null)
                try {
                    out.close();
                } catch (Exception e) {
                }
        }
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

}
