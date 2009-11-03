// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
// * This software is published under the GPL GNU General Public License. 
// * This program is free software; you can redistribute it and/or 
// * modify it under the terms of the GNU General Public License 
// * as published by the Free Software Foundation; either version 2 
// * of the License, or (at your option) any later version. * 
// * This program is distributed in the hope that it will be useful, 
// * but WITHOUT ANY WARRANTY; without even the implied warranty of 
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
// * along with this program; if not, write to the Free Software 
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
// * 
// * <OSCAR TEAM>
// * This software was written for the 
// * Department of Family Medicine 
// * McMaster Unviersity 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar.oscarEncounter.oscarMeasurements.pageUtil;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import oscar.OscarProperties;
import oscar.oscarDB.DBHandler;
import oscar.util.ParameterActionForward;

public class EctDeleteDataAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        EctDeleteDataForm frm = (EctDeleteDataForm) form;                
        request.getSession().setAttribute("EctDeleteDataForm", frm);
        String[] deleteCheckbox = frm.getDeleteCheckbox();
 
        GregorianCalendar now=new GregorianCalendar(); 
        int curYear = now.get(Calendar.YEAR);
        int curMonth = (now.get(Calendar.MONTH)+1);
        int curDay = now.get(Calendar.DAY_OF_MONTH);
        String dateDeleted = now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DATE) ;

        boolean valid = true;
                
        try{
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);                                                                                    
            
            if(deleteCheckbox != null){
                for(int i=0; i<deleteCheckbox.length; i++){
                    System.out.println(deleteCheckbox[i]);
                    String sql = "SELECT * FROM `measurements` WHERE id='"+ deleteCheckbox[i] +"'";                                        
                    System.out.println(" sql statement "+sql);
                    ResultSet rs = db.GetSQL(sql);
                    if(rs.next()){
                        sql = "INSERT INTO measurementsDeleted"
                             +"(type, demographicNo, providerNo, dataField, measuringInstruction, comments, dateObserved, dateEntered, dateDeleted)"
                             +" VALUES ('"+db.getString(rs,"type")+"','"+db.getString(rs,"demographicNo")+"','"+db.getString(rs,"providerNo")+"','"
                             + db.getString(rs,"dataField")+"','" + db.getString(rs,"measuringInstruction")+"','"+db.getString(rs,"comments")+"','"
                             + db.getString(rs,"dateObserved")+"','"+db.getString(rs,"dateEntered")+"','"+dateDeleted+"')";
                        db.RunSQL(sql);
                        rs.close();
                        sql = "DELETE FROM `measurements` WHERE id='"+ deleteCheckbox[i] +"'"; 
                        db.RunSQL(sql);
                    }
                }
            }
            

            /*select the correct db specific command */
            String db_type = OscarProperties.getInstance().getProperty("db_type").trim();
            String dbSpecificCommand;
            if (db_type.equalsIgnoreCase("mysql")) {
                dbSpecificCommand = "SELECT LAST_INSERT_ID()";
            } 
            else if (db_type.equalsIgnoreCase("postgresql")){
                dbSpecificCommand = "SELECT CURRVAL('consultationrequests_numeric')";
            }
            else
                throw new SQLException("ERROR: Database " + db_type + " unrecognized.");
        }

        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
                
        if(frm.getType()!=null){
            ParameterActionForward forward = new ParameterActionForward(mapping.findForward("success"));
            forward.addParameter("type", frm.getType());
            return forward;
        }
        return mapping.findForward("success");
    }
     
}
