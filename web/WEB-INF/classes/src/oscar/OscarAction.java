package oscar;

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;
import oscar.oscarDB.*;

/**
 *
 * <p>Title: OscarAction</p>
 * <p>Description:SuperClass for actions in OSCAR, performs login verification and contains commonly used
 *  objects for database access </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class OscarAction
    extends Action {
  protected String target = "success";
  protected String error = "error";
  protected String propName = "";
  protected DBPreparedHandler handler = null;
  protected Properties oscarVariables = null;
  String propFile = "";

  public OscarAction() {

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

  protected Connection getDBConnection(HttpServletRequest request) {
    String[] dbParams = this.getDBParams(request);
    try {
      handler = new DBPreparedHandler(dbParams[0], dbParams[1], dbParams[2],
                                      dbParams[3]);
    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }
    return handler.getConn();
  }

}
