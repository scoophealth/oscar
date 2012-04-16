/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
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

package org.oscarehr.olis;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

import oscar.log.LogAction;
import oscar.log.LogConst;
import oscar.oscarDB.DBHandler;
import oscar.oscarLab.FileUploadCheck;
import oscar.oscarLab.ca.all.upload.HandlerClassFactory;
import oscar.oscarLab.ca.all.upload.handlers.OLISHL7Handler;
import oscar.oscarLab.ca.on.CommonLabResultData;


public class OLISAddToInboxAction extends DispatchAction {
	
	static Logger logger = MiscUtils.getLogger();

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		String uuidToAdd = request.getParameter("uuid");
		String pFile = request.getParameter("file");
		String pAck = request.getParameter("ack");
		boolean doFile=false, doAck=false;
		if(pFile != null && pFile.equals("true")) {
			doFile=true;
		}
		if(pAck != null && pAck.equals("true")) {
			doAck=true;
		}

		String fileLocation = System.getProperty("java.io.tmpdir") + "/olis_" + uuidToAdd + ".response";
		File file = new File(fileLocation);
		OLISHL7Handler msgHandler = (OLISHL7Handler) HandlerClassFactory.getHandler("OLIS_HL7");
		
		InputStream is = null;
		try {
			is = new FileInputStream(fileLocation);
			String provNo =  LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo();
			int check = FileUploadCheck.addFile(file.getName(), is, provNo);
			
			if (check != FileUploadCheck.UNSUCCESSFUL_SAVE) {
				if (msgHandler.parse("OLIS_HL7",fileLocation, check, true) != null) {
					request.setAttribute("result", "Success");
					if(doFile) {
						ArrayList<String[]> labsToFile = new ArrayList<String[]>();
						String item[] = new String[]{String.valueOf(msgHandler.getLastSegmentId()),"HL7"};
						labsToFile.add(item);
						CommonLabResultData.fileLabs(labsToFile, provNo);
					}
					if(doAck) {
						 String demographicID = getDemographicIdFromLab("HL7", msgHandler.getLastSegmentId());				           
						 LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.ACK, LogConst.CON_HL7_LAB, ""+msgHandler.getLastSegmentId(), request.getRemoteAddr(),demographicID);
						 CommonLabResultData.updateReportStatus(msgHandler.getLastSegmentId(), provNo, 'A', "comment","HL7");
				            
					}
				} else {
					request.setAttribute("result", "Error");
				}
			} else {
				request.setAttribute("result", "Already Added");
			}			

		} catch (Exception e) {
			MiscUtils.getLogger().error("Couldn't add requested OLIS lab to Inbox.", e);
			request.setAttribute("result", "Error");
		} finally {
			try {
				is.close();
			} catch(IOException e){
				//ignore
			}
		}

		return mapping.findForward("ajax");
	}
	
    private static String getDemographicIdFromLab(String labType, int labNo)
    {
    	String demographicID="";
        try{
            String sql = "SELECT demographic_no FROM patientLabRouting WHERE lab_type = '"+labType+"' and lab_no='"+labNo+"'";
            
            ResultSet rs = DBHandler.GetSQL(sql);

            while(rs.next()){
                demographicID = oscar.Misc.getString(rs, "demographic_no");
            }
            rs.close();
        }catch(Exception e){
        	logger.error("Error", e);
        }
        
        return(demographicID);
    }
}
