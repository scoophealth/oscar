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
 * McMaster University
 * Hamilton
 * Ontario, Canada
 * AUTHOR: Joel Legris
 * DATE: May 13, 2005
 * DESCRIPTION:
 * <p>Description:SuperClass for actions in OSCAR, performs login verification and contains commonly used
 *  objects for database access </p>
 */

package oscar;

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import org.oscarehr.util.DbConnectionFilter;

import oscar.oscarDB.*;

public class OscarAction
    extends Action {
  protected String target = "success";
  protected String error = "error";
  protected String propName = "";
  protected DBPreparedHandler handler = null;
  protected Properties oscarVariables = null;
  public final String MIME_PDF = "application/pdf";
  String propFile = "";


  public OscarAction() {
    if (!System.getProperties().containsKey("jasper.reports.compile.class.path")) {
     // String classpath = (String) getServlet().getServletContext().
      //    getAttribute("org.apache.catalina.jsp_classpath");
     // System.setProperty("jasper.reports.compile.class.path", classpath);
    }
  }

  /**
   * Determines if a client request is authorized to perform a specific action
   * <p>The session object is checked for the presence of a User object</p>
   * <p>If authorization is unsuccessful, the target is set to login and the request is denied access</p>
   * @param request HttpServletRequest
   * @return boolean
   */
  public boolean isAuthorized(HttpServletRequest request) {
    HttpSession session = request.getSession();
    Object user = session.getAttribute("user");

    if (user == null) {
      target = "login";
      return false;
    }

    return true;
  }

  /**
   * Returns a string array of the parameters from the session, used to connect to the Database
   * @param request HttpServletRequest
   * @return String[]
   */
  public String[] getDBParams(HttpServletRequest request) {
    Properties oscarVariables = (Properties) request.getSession().getAttribute(
        "oscarVariables");
    String[] dbParams = new String[] {
        oscarVariables.getProperty("db_driver"),
        oscarVariables.getProperty("db_uri") +
        oscarVariables.getProperty("db_name") + "?user=" +
        oscarVariables.getProperty("db_username") + "&password=" +
        oscarVariables.getProperty("db_password"),
        oscarVariables.getProperty("db_username"),
        oscarVariables.getProperty("db_password")};
    return dbParams;
  }

 

  /**
   * Configures the response header for upload of specified mime-type
   * @param response HttpServletResponse
   * @param docName String
   * @param docType String
   * @return StringBuffer
   */
  public void cfgHeader(HttpServletResponse response, String docName,
                        String docType) {
    String mimeType = "application/octet-stream";
    if (docType.equals("pdf")) {
      mimeType = "application/pdf";
    }
    else if (docType.equals("csv")) {
      mimeType = "application/csv";
    }
    response.setContentType(mimeType);
    response.setHeader("Content-Disposition",
                       "attachment;filename=" + docName + "." + docType);
  }


  /**
   * A convenience method for retrieving the servlet outputstream without
   * cluttering the calling code with verbose exception handling
   * @param response HttpServletResponse
   * @return ServletOutputStream
   */
  protected ServletOutputStream getServletOstream(HttpServletResponse response) {
    ServletOutputStream outputStream = null;
    try {
      outputStream = response.getOutputStream();
    }
    catch (IOException ex) {
      ex.printStackTrace();
    }
    return outputStream;
  }



}
