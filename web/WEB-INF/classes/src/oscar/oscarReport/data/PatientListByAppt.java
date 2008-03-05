/*
 * PatientListByAppt.java
 *
 * Created on August 27, 2007, 4:53 PM
 */

package oscar.oscarReport.data;

import java.io.*;
import java.net.*;

import oscar.OscarProperties;
import oscar.oscarDB.DBHandler;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.SQLException;
import java.io.File;

/**
 *
 * @author root
 * @version
 */
public class PatientListByAppt extends HttpServlet {
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        String drNo = request.getParameter("provider_no");
        String datefrom = request.getParameter("date_from");
        String dateto = request.getParameter("date_to");
        OscarProperties props = OscarProperties.getInstance();
        String home = props.getProperty("project_home");
        String url = "/usr/local/tomcat/webapps/"+ home + "/oscarReport/patientlist.txt";
        try{
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            
            java.sql.ResultSet rs;
            String sql = "select d.last_name, d.first_name, d.phone,  d.phone2, "+
                         "       a.appointment_date, a.start_time, a.type,     "+
                         "       p.last_name, p.first_name                      "+
                         "from   demographic d, appointment a, provider p       "+
                         "where  a.demographic_no=d.demographic_no              "+
                         "and    a.provider_no=p.provider_no                    ";
                         
            
            if(!drNo.equals("all")){
                sql = sql + "and a.provider_no='"+drNo +"' ";
            }
            if(!datefrom.equals("")){
                sql = sql + "and a.appointment_date>='"+datefrom +"' ";
            }if(!dateto.equals("")){
                sql = sql + "and a.appointment_date<='"+dateto +"' ";
            }
            sql = sql + "order by a.appointment_date";
            //System.out.println(sql);
            rs = db.GetSQL(sql);
            
            File ffile = new File(url);
            FileOutputStream file = new FileOutputStream( ffile );
            PrintStream ps = new PrintStream( file );

              while(rs.next()){
               ps.print(db.getString(rs,1)+",");
               ps.print(db.getString(rs,2)+",");
               ps.print(db.getString(rs,3)+",");
               ps.print(db.getString(rs,4)+",");
               ps.print(db.getString(rs,6)+",");
               ps.print(db.getString(rs,5)+",");
               ps.print(db.getString(rs,7).replaceAll("\r\n","")+",");
               ps.print(db.getString(rs,9)+" ");
               ps.print(db.getString(rs,8));
               ps.print("\n");
            }
            ps.println("");

            db.CloseConn();
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        } 
        response.sendRedirect(response.encodeRedirectURL("oscarReport/downloadpatientlist.jsp"));
    }
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
    // </editor-fold>
}
