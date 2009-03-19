/*
 *
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 * <OSCAR TEAM>
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster Unviersity
 * Hamilton
 * Ontario, Canada
 */
package oscar.oscarMDS.pageUtil;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import oscar.OscarProperties;
import oscar.log.LogAction;
import oscar.log.LogConst;
import oscar.oscarDB.DBHandler;
import oscar.oscarLab.ca.on.CommonLabResultData;

public class ReportStatusUpdateAction extends Action {
    
    Logger logger = Logger.getLogger(ReportStatusUpdateAction.class);
    
    public ReportStatusUpdateAction() {
    }
    
    public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        
        int labNo = Integer.parseInt(request.getParameter("segmentID"));
        String multiID = request.getParameter("multiID");
        String providerNo = request.getParameter("providerNo");
        char status = request.getParameter("status").charAt(0);
        String comment = request.getParameter("comment");
        String lab_type = request.getParameter("labType");
        Properties props = OscarProperties.getInstance();

        
        if(status == 'A'){
            String demographicID = "";
            try{
                String sql = "SELECT demographic_no FROM patientLabRouting WHERE lab_type = '"+lab_type+"' and lab_no='"+labNo+"'";
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                ResultSet rs = db.GetSQL(sql);

                while(rs.next()){
                    demographicID = db.getString(rs,"demographic_no");
                }
                rs.close();

                LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.ACK, LogConst.CON_HL7_LAB, ""+labNo, request.getRemoteAddr(),demographicID);
            }catch(Exception ep){

            }
        }
        
        
        try {
            CommonLabResultData data = new CommonLabResultData();
            data.updateReportStatus(props, labNo, providerNo, status, comment,lab_type);
            //MDSResultsData.updateReportStatus(props, labNo, providerNo, status, comment);
            if (multiID != null){
                String[] id = multiID.split(",");
                int i=0;
                int idNum = Integer.parseInt(id[i]);
                while(idNum != labNo){
                    data.updateReportStatus(props, idNum, providerNo, 'F', "", lab_type);
                    i++;
                    idNum = Integer.parseInt(id[i]);
                }
                
            }
            return mapping.findForward("success");
        } catch (Exception e) {
            logger.error("exception in ReportStatusUpdateAction",e);
            return mapping.findForward("failure");
        }
    }
}