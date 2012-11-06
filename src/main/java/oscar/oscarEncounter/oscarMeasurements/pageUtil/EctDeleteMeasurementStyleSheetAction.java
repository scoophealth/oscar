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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.oscarehr.common.dao.MeasurementCSSLocationDao;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarDB.DBHandler;


public class EctDeleteMeasurementStyleSheetAction extends Action {

	private MeasurementCSSLocationDao dao = SpringUtils.getBean(MeasurementCSSLocationDao.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        EctDeleteMeasurementStyleSheetForm frm = (EctDeleteMeasurementStyleSheetForm) form;                
        request.getSession().setAttribute("EctDeleteMeasurementStyleSheetForm", frm);
        String[] deleteCheckbox = frm.getDeleteCheckbox();
       
        try{
                                                                                                
            
            if(deleteCheckbox != null){
                for(int i=0; i<deleteCheckbox.length; i++){
                    MiscUtils.getLogger().debug(deleteCheckbox[i]);
                    String sql = "SELECT * FROM measurementGroupStyle WHERE cssID='"+ deleteCheckbox[i] +"'";
                    MiscUtils.getLogger().debug("SQL: " + sql);
                    ResultSet rs;
                    rs = DBHandler.GetSQL(sql);
                    if(rs.next()){
                        sql = "SELECT * FROM measurementCSSLocation WHERE cssID ='" + deleteCheckbox[i] + "'";
                        rs = DBHandler.GetSQL(sql);
                        if(rs.next()){
                            ActionMessages errors = new ActionMessages();  
                            errors.add(deleteCheckbox[i],
                            new ActionMessage("error.oscarEncounter.Measurements.cannotDeleteStyleSheet", oscar.Misc.getString(rs, "location")));
                            saveErrors(request, errors);
                            return (new ActionForward(mapping.getInput()));
                        }
                    }
                    else{
                    	dao.remove(Integer.parseInt(deleteCheckbox[i]));   
                    }
                }
            }
            

           
        }

        catch(SQLException e)
        {
            MiscUtils.getLogger().error("Error", e);
        }
 
        return mapping.findForward("success");
    }
     
}
