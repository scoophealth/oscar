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


package oscar.dms.actions;

import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.dao.RemoteDataLogDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.RemoteDataLog;
import org.oscarehr.common.service.myoscar.MyOscarMedicalDataManagerUtils;
import org.oscarehr.myoscar.client.ws_manager.AccountManager;
import org.oscarehr.myoscar.commons.MedicalDataType;
import org.oscarehr.myoscar.utils.MyOscarLoggedInInfo;
import org.oscarehr.myoscar_server.ws.MedicalDataTransfer4;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.util.XmlUtils;
import org.w3c.dom.Document;

import oscar.dms.EDoc;
import oscar.dms.EDocUtil;
import oscar.oscarDemographic.data.DemographicData;
import oscar.oscarProvider.data.ProviderData;

/**
 * @author rjonasz
 */
public class SendDocToPhrAction extends Action {

	private static final Logger logger = MiscUtils.getLogger();
	private static RemoteDataLogDao remoteDataLogDao=(RemoteDataLogDao) SpringUtils.getBean("remoteDataLogDao");

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		logger.debug("called execute()");

		String[] files = request.getParameterValues("docNo");
		String demographicId=request.getParameter("demoId");

		if (files != null) {

			logger.debug("Preparing to send " + files.length + " files");


			LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
			Demographic demo = new DemographicData().getDemographic(LoggedInInfo.getLoggedInInfoFromSession(request), demographicId);
			ProviderData prov = new ProviderData(loggedInInfo.getLoggedInProviderNo());

			for (int idx = 0; idx < files.length; ++idx) {
				logger.debug("sending file : "+files[idx]);
				EDoc doc = EDocUtil.getDoc(files[idx]);
				addOrUpdate(request, demo, prov, doc);
			}

		}
		return mapping.findForward("finished");
	}

	private static void addOrUpdate(HttpServletRequest request, Demographic demo, ProviderData prov, EDoc eDoc) {
		logger.debug("called addOrUpdate()");

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);

		try {

			Document doc=XmlUtils.newDocument("BinaryDocument");
    		XmlUtils.appendChildToRoot(doc, "Filename", eDoc.getFileName());
    		XmlUtils.appendChildToRoot(doc, "FileDescription", eDoc.getDescription());
    		XmlUtils.appendChildToRoot(doc, "MimeType", eDoc.getContentType());
    		XmlUtils.appendChildToRoot(doc, "Data", eDoc.getFileBytes());
    		String docAsString=XmlUtils.toString(doc, false);

    		MyOscarLoggedInInfo myOscarLoggedInInfo=MyOscarLoggedInInfo.getLoggedInInfo(request.getSession());

    		Long patientMyOscarUserId=AccountManager.getUserId(myOscarLoggedInInfo, demo.getMyOscarUserName());
    		logger.debug("patient userName="+demo.getMyOscarUserName()+", patientPersonId="+patientMyOscarUserId);
    		GregorianCalendar dateOfData=new GregorianCalendar();
    		if (eDoc.getDateTimeStampAsDate()!=null) dateOfData.setTime(eDoc.getDateTimeStampAsDate());

			MedicalDataTransfer4 medicalDataTransfer=new MedicalDataTransfer4();
			medicalDataTransfer.setData(docAsString);
			medicalDataTransfer.setDateOfData(dateOfData);
			medicalDataTransfer.setMedicalDataType(MedicalDataType.BINARY_DOCUMENT.name());
			medicalDataTransfer.setObserverOfDataPersonId(myOscarLoggedInInfo.getLoggedInPersonId());
			medicalDataTransfer.setObserverOfDataPersonName(loggedInInfo.getLoggedInProvider().getFormattedName());
			medicalDataTransfer.setOriginalSourceId(loggedInInfo.getCurrentFacility().getName()+":eDoc:"+eDoc.getDocId());
			medicalDataTransfer.setOwningPersonId(patientMyOscarUserId);

    		MyOscarMedicalDataManagerUtils.addMedicalData(loggedInInfo.getLoggedInProviderNo(), myOscarLoggedInInfo, medicalDataTransfer, "eDoc", eDoc.getDocId(), true, true);    		

    		// log the send
    		RemoteDataLog remoteDataLog=new RemoteDataLog();
    		remoteDataLog.setProviderNo(loggedInInfo.getLoggedInProviderNo());
    		remoteDataLog.setDocumentId(MyOscarLoggedInInfo.getMyOscarServerBaseUrl(), "eDoc", eDoc.getDocId());
    		remoteDataLog.setAction(RemoteDataLog.Action.SEND);
    		remoteDataLog.setDocumentContents("id="+eDoc.getDocId()+", fileName="+eDoc.getFileName());
    		remoteDataLogDao.persist(remoteDataLog);

	    } catch (Exception e) {
	    	logger.error("Error", e);
	    	request.setAttribute("error_msg", e.getMessage());
	    }
    }
}
