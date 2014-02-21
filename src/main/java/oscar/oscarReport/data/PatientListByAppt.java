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


package oscar.oscarReport.data;

import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;

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
    throws IOException {
        response.setContentType("plain/text");
        response.setHeader("Content-disposition", "attachment; filename=patientlist.txt");

        String drNo = request.getParameter("provider_no");
        String datefrom = request.getParameter("date_from");
        String dateto = request.getParameter("date_to");
        try{


            java.sql.ResultSet rs;
            String sql = "select d.last_name, d.first_name, d.phone,  d.phone2, "+
                         "       a.appointment_date, a.start_time, a.type,     "+
                         "       p.last_name, p.first_name, a.location           "+
                         "from   demographic d, appointment a, provider p       "+
                         "where  a.demographic_no=d.demographic_no              "+
                         "and    a.provider_no=p.provider_no                    "+
                         "and    a.status <> 'C'                                "+
                         "and    a.status <> 'D'                                ";


            if(!drNo.equals("all")){
                sql = sql + "and a.provider_no='"+drNo +"' ";
            }
            if(!datefrom.equals("")){
                sql = sql + "and a.appointment_date>='"+datefrom +"' ";
            }if(!dateto.equals("")){
                sql = sql + "and a.appointment_date<='"+dateto +"' ";
            }
            sql = sql + "order by a.appointment_date";

            rs = DBHandler.GetSQL(sql);

            PrintStream ps = new PrintStream(response.getOutputStream());

              while(rs.next()){
               ps.print(oscar.Misc.getString(rs, 1)+",");
               ps.print(oscar.Misc.getString(rs, 2)+",");
               ps.print(oscar.Misc.getString(rs, 3)+",");
               ps.print(oscar.Misc.getString(rs, 4)+",");
               ps.print(oscar.Misc.getString(rs, 6)+",");
               ps.print(oscar.Misc.getString(rs, 5)+",");
               ps.print(oscar.Misc.getString(rs, 7).replaceAll("\r\n","")+",");
               ps.print(oscar.Misc.getString(rs, 9)+" ");
               ps.print(rs.getString(8)+",");
               ps.print(rs.getString(10));
               ps.print("\n");
            }
            ps.println("");
        }
        catch(SQLException e){
            MiscUtils.getLogger().error("Error", e);
        }
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
