/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */
package org.oscarehr.olis;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.dao.PatientLabRoutingDao;
import org.oscarehr.common.model.PatientLabRouting;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.log.LogAction;
import oscar.log.LogConst;
import oscar.oscarLab.FileUploadCheck;
import oscar.oscarLab.ca.all.upload.HandlerClassFactory;
import oscar.oscarLab.ca.all.upload.handlers.OLISHL7Handler;
import oscar.oscarLab.ca.on.CommonLabResultData;

public class OLISAddToInboxAction extends DispatchAction {

	static Logger logger = MiscUtils.getLogger();

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();
		
		String uuidToAdd = request.getParameter("uuid");
		String pFile = request.getParameter("file");
		String pAck = request.getParameter("ack");
		boolean doFile = false, doAck = false;
		if (pFile != null && pFile.equals("true")) {
			doFile = true;
		}
		if (pAck != null && pAck.equals("true")) {
			doAck = true;
		}
		
		String fileLocation = System.getProperty("java.io.tmpdir") + "/olis_" + uuidToAdd + ".response";
		File file = new File(fileLocation);
		OLISHL7Handler msgHandler = (OLISHL7Handler) HandlerClassFactory.getHandler("OLIS_HL7");

		InputStream is = null;
		try {
			is = new FileInputStream(fileLocation);
			int check = FileUploadCheck.addFile(file.getName(), is, providerNo);

			if (check != FileUploadCheck.UNSUCCESSFUL_SAVE) {
				if (msgHandler.parse(loggedInInfo, "OLIS_HL7", fileLocation, check, true) != null) {
					request.setAttribute("result", "Success");
					if (doFile) {
						ArrayList<String[]> labsToFile = new ArrayList<String[]>();
						String item[] = new String[] { String.valueOf(msgHandler.getLastSegmentId()), "HL7" };
						labsToFile.add(item);
						CommonLabResultData.fileLabs(labsToFile, providerNo);
					}
					if (doAck) {
						String demographicID = getDemographicIdFromLab("HL7", msgHandler.getLastSegmentId());
						LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.ACK, LogConst.CON_HL7_LAB, "" + msgHandler.getLastSegmentId(), request.getRemoteAddr(), demographicID);
						CommonLabResultData.updateReportStatus(msgHandler.getLastSegmentId(), providerNo, 'A', "comment", "HL7");

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
			} catch (IOException e) {
				//ignore
			}
		}

		return mapping.findForward("ajax");
	}

	private static String getDemographicIdFromLab(String labType, int labNo) {
		PatientLabRoutingDao dao = SpringUtils.getBean(PatientLabRoutingDao.class);
		PatientLabRouting routing = dao.findDemographics(labType, labNo);
		return routing == null ? "" : String.valueOf(routing.getDemographicNo());
	}
}
