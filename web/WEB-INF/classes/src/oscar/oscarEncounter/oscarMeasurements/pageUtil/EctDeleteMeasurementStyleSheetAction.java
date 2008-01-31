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

import java.io.*;
import java.util.*;
import java.lang.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.validator.*;
import org.apache.commons.validator.*;
import org.apache.struts.util.MessageResources;
import oscar.oscarDB.DBHandler;
import oscar.oscarMessenger.util.MsgStringQuote;
import oscar.oscarEncounter.pageUtil.EctSessionBean;
import oscar.OscarProperties;


public class EctDeleteMeasurementStyleSheetAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        EctDeleteMeasurementStyleSheetForm frm = (EctDeleteMeasurementStyleSheetForm) form;                
        request.getSession().setAttribute("EctDeleteMeasurementStyleSheetForm", frm);
        String[] deleteCheckbox = frm.getDeleteCheckbox();
       
        try{
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);                                                                                    
            
            if(deleteCheckbox != null){
                for(int i=0; i<deleteCheckbox.length; i++){
                    System.out.println(deleteCheckbox[i]);
                    String sql = "SELECT * FROM measurementGroupStyle WHERE cssID='"+ deleteCheckbox[i] +"'";
                    System.out.println("SQL: " + sql);
                    ResultSet rs;
                    rs = db.GetSQL(sql);
                    if(rs.next()){
                        sql = "SELECT * FROM measurementCSSLocation WHERE cssID ='" + deleteCheckbox[i] + "'";
                        rs = db.GetSQL(sql);
                        if(rs.next()){
                            ActionMessages errors = new ActionMessages();  
                            errors.add(deleteCheckbox[i],
                            new ActionMessage("error.oscarEncounter.Measurements.cannotDeleteStyleSheet", db.getString(rs,"location")));
                            saveErrors(request, errors);
                            return (new ActionForward(mapping.getInput()));
                        }
                    }
                    else{
                        sql = "DELETE  FROM measurementCSSLocation WHERE cssID='"+ deleteCheckbox[i] +"'";                                        
                        System.out.println(" sql statement "+sql);
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

            db.CloseConn();
        }

        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
 
        return mapping.findForward("success");
    }
     
}
