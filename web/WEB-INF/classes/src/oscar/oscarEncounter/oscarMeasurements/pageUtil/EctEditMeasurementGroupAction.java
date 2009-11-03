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
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import oscar.OscarProperties;
import oscar.oscarDB.DBHandler;
import oscar.oscarMessenger.util.MsgStringQuote;


public class EctEditMeasurementGroupAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        EctEditMeasurementGroupForm frm = (EctEditMeasurementGroupForm) form;                
        request.getSession().setAttribute("EctEditMeasurementGroupForm", frm);
        String groupName = frm.getGroupName();

        MsgStringQuote str = new MsgStringQuote();
        String requestId = "";
        
        List messages = new LinkedList();
        boolean valid = true;
                
        if(frm.getForward()!=null){
            try{
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);                                                                        
                
                if (frm.getForward().compareTo("add")==0) {
                    System.out.println("the add button is pressed");
                    String[] selectedAddTypes = frm.getSelectedAddTypes();  
                    if(selectedAddTypes != null){
                        for(int i=0; i<selectedAddTypes.length; i++){
                            System.out.println(selectedAddTypes[i]);
                            String sql = "INSERT INTO measurementGroup (name, typeDisplayName) VALUES('" + groupName + "','" + selectedAddTypes[i] +"')";
                            System.out.println(" sql statement "+sql);
                            db.RunSQL(sql);                                
                        }
                    }
                }
                else if (frm.getForward().compareTo("delete")==0){
                    System.out.println("the delete button is pressed");
                    String[] selectedDeleteTypes = frm.getSelectedDeleteTypes();  
                    if(selectedDeleteTypes != null){
                        for(int i=0; i<selectedDeleteTypes.length; i++){
                            System.out.println(selectedDeleteTypes[i]);
                            String sql = "DELETE  FROM `measurementGroup` WHERE name='"+ groupName +"' AND typeDisplayName='" + selectedDeleteTypes[i] + "'";                                        
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
                    
                ResultSet rs = db.GetSQL(dbSpecificCommand);
                if(rs.next())
                    requestId = Integer.toString(rs.getInt(1));
            }
           
            catch(SQLException e)
            {
                System.out.println(e.getMessage());
            }
     
        }                          
        
        return mapping.findForward("success");
    }
     
}
