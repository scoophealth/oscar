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


package oscar.oscarResearch.oscarDxResearch.pageUtil;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.dao.PartialDateDao;
import org.oscarehr.common.model.PartialDate;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarDB.DBHandler;
import oscar.util.ParameterActionForward;
import oscar.util.UtilDateUtilities;


public class dxResearchUpdateAction extends Action {
	private static final PartialDateDao partialDateDao = (PartialDateDao) SpringUtils.getBean("partialDateDao");

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        String status = request.getParameter("status");
        String did = request.getParameter("did");
        String demographicNo = request.getParameter("demographicNo");        
        String providerNo = request.getParameter("providerNo");
        String startDate = request.getParameter("startdate");
        String nowDate = UtilDateUtilities.DateToString(UtilDateUtilities.now(), "yyyy/MM/dd"); 
        String updateDate = UtilDateUtilities.DateToString(UtilDateUtilities.now(), "yyyy-MM-dd HH:mm:ss"); 
        

        partialDateDao.setPartialDate(startDate, PartialDate.DXRESEARCH, Integer.valueOf(did), PartialDate.DXRESEARCH_STARTDATE);
        startDate = partialDateDao.getFullDate(startDate);
        
        try{
                        
            String sql = "";
            if (status.equals("C")){
                sql = "update dxresearch set update_date='"+updateDate + "', status='C' where dxresearch_no='"+did+"'";
                DBHandler.RunSQL(sql);
            }
            else if (status.equals("D")){
                sql = "update dxresearch set update_date='"+updateDate + "', status='D' where dxresearch_no='"+did+"'";
                DBHandler.RunSQL(sql);
            }
            else if (status.equals("A") && startDate!=null){
                sql = "update dxresearch set update_date='"+updateDate+"', start_date='"+startDate+"' where dxresearch_no='"+did+"'";
                DBHandler.RunSQL(sql);
            }
        }

        catch(SQLException e){
            MiscUtils.getLogger().error("Error", e);
        }                                    
        
        ParameterActionForward forward = new ParameterActionForward(mapping.findForward("success"));
        forward.addParameter("demographicNo", demographicNo);
        forward.addParameter("providerNo", providerNo);
        forward.addParameter("quickList", "");        
        
        return forward;
    }
     
}
