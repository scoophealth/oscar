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
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.dao.MeasurementsDeletedDao;
import org.oscarehr.common.model.MeasurementsDeleted;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.oscarDB.DBHandler;
import oscar.util.ParameterActionForward;
import oscar.util.UtilDateUtilities;

public class EctDeleteDataAction extends Action {
	
	private static MeasurementsDeletedDao measurementsDeletedDao = (MeasurementsDeletedDao) SpringUtils.getBean("measurementsDeletedDao");

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        EctDeleteDataForm frm = (EctDeleteDataForm) form;                
        request.getSession().setAttribute("EctDeleteDataForm", frm);
        String[] deleteCheckbox = frm.getDeleteCheckbox();
 
        GregorianCalendar now=new GregorianCalendar(); 

        String dateDeleted = now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DATE) ;
                
        try{
                                                                                                
            
            if(deleteCheckbox != null){
            	MeasurementsDeleted measurementsDeleted = new MeasurementsDeleted();
            	
                for(int i=0; i<deleteCheckbox.length; i++){
                    MiscUtils.getLogger().debug(deleteCheckbox[i]);
                    String sql = "SELECT * FROM `measurements` WHERE id='"+ deleteCheckbox[i] +"'";                                        
                    MiscUtils.getLogger().debug(" sql statement "+sql);
                    ResultSet rs = DBHandler.GetSQL(sql);
                    if(rs.next()){
                    	measurementsDeleted.setType(oscar.Misc.getString(rs, "type"));
                    	measurementsDeleted.setDemographicNo(Integer.valueOf(oscar.Misc.getString(rs, "demographicNo")));
                    	measurementsDeleted.setProviderNo(oscar.Misc.getString(rs, "providerNo"));
                    	measurementsDeleted.setDataField(oscar.Misc.getString(rs, "dataField"));
                    	measurementsDeleted.setMeasuringInstruction(oscar.Misc.getString(rs, "measuringInstruction"));
                    	measurementsDeleted.setComments(oscar.Misc.getString(rs, "comments"));
                    	measurementsDeleted.setDateObserved(UtilDateUtilities.StringToDate(oscar.Misc.getString(rs, "dateObserved"), "yyyy-MM-dd hh:mm:ss"));
                    	measurementsDeleted.setDateEntered(UtilDateUtilities.StringToDate(oscar.Misc.getString(rs, "dateEntered"), "yyyy-MM-dd hh:mm:ss"));
                    	measurementsDeleted.setOriginalId(Integer.valueOf(deleteCheckbox[i]));
                    	measurementsDeletedDao.persist(measurementsDeleted);
                    	
                        rs.close();
                        sql = "DELETE FROM `measurements` WHERE id='"+ deleteCheckbox[i] +"'"; 
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
        }

        catch(SQLException e)
        {
            MiscUtils.getLogger().error("Error", e);
        }
                
        if(frm.getType()!=null){
            ParameterActionForward forward = new ParameterActionForward(mapping.findForward("success"));
            forward.addParameter("type", frm.getType());
            return forward;
        }
        return mapping.findForward("success");
    }
     
}
