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


package oscar.oscarEncounter.oscarMeasurements.pageUtil;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;
import oscar.oscarDB.DBHandler;


public class EctEditMeasurementGroupAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        EctEditMeasurementGroupForm frm = (EctEditMeasurementGroupForm) form;                
        request.getSession().setAttribute("EctEditMeasurementGroupForm", frm);
        String groupName = frm.getGroupName();

        String requestId = "";
        
                
        if(frm.getForward()!=null){
            try{
                                                                                        
                
                if (frm.getForward().compareTo("add")==0) {
                    MiscUtils.getLogger().debug("the add button is pressed");
                    String[] selectedAddTypes = frm.getSelectedAddTypes();  
                    if(selectedAddTypes != null){
                        for(int i=0; i<selectedAddTypes.length; i++){
                            MiscUtils.getLogger().debug(selectedAddTypes[i]);
                            String sql = "INSERT INTO measurementGroup (name, typeDisplayName) VALUES('" + groupName + "','" + selectedAddTypes[i] +"')";
                            MiscUtils.getLogger().debug(" sql statement "+sql);
                            DBHandler.RunSQL(sql);                                
                        }
                    }
                }
                else if (frm.getForward().compareTo("delete")==0){
                    MiscUtils.getLogger().debug("the delete button is pressed");
                    String[] selectedDeleteTypes = frm.getSelectedDeleteTypes();  
                    if(selectedDeleteTypes != null){
                        for(int i=0; i<selectedDeleteTypes.length; i++){
                            MiscUtils.getLogger().debug(selectedDeleteTypes[i]);
                            String sql = "DELETE  FROM `measurementGroup` WHERE name='"+ groupName +"' AND typeDisplayName='" + selectedDeleteTypes[i] + "'";                                        
                            MiscUtils.getLogger().debug(" sql statement "+sql);
                            DBHandler.RunSQL(sql);                                
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
                    
                ResultSet rs = DBHandler.GetSQL(dbSpecificCommand);
                if(rs.next())
                    requestId = Integer.toString(rs.getInt(1));
            }
           
            catch(SQLException e)
            {
                MiscUtils.getLogger().error("Error", e);
            }
     
        }                          
        
        return mapping.findForward("success");
    }
     
}
