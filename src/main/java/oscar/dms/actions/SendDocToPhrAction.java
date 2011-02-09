/*
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 *  This software is published under the GPL GNU General Public License.
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version. *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 *
 *  This software was written for the
 *  Department of Family Medicine
 *  McMaster University
 *  Hamilton
 *  Ontario, Canada
 *
 * Send2IndivoAction.java
 *
 * Created on January 8, 2007, 4:39 PM
 * 
 */

package oscar.dms.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.phr.PHRConstants;
import org.oscarehr.phr.model.PHRDocument;
import org.oscarehr.phr.service.PHRService;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.dms.EDoc;
import oscar.dms.EDocUtil;
import oscar.oscarDemographic.data.DemographicData;
import oscar.oscarProvider.data.ProviderData;

/**
 * @author rjonasz
 */
public class SendDocToPhrAction extends Action {

	private static final Logger logger = MiscUtils.getLogger();

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		logger.debug("called execute()");

		String[] files = request.getParameterValues("docNo");
		String demographicId=request.getParameter("demoId");
		
		if (files != null) {

			logger.debug("Preparing to send " + files.length + " files");

			DemographicData.Demographic demo = new DemographicData().getDemographic(demographicId);
			LoggedInInfo loggedInfo=LoggedInInfo.loggedInInfo.get();
			ProviderData prov = new ProviderData(loggedInfo.loggedInProvider.getProviderNo());

			for (int idx = 0; idx < files.length; ++idx) {
				logger.debug("sending file : "+files[idx]);
				EDoc doc = EDocUtil.getDoc(files[idx]);
				addOrUpdate(request, demo, prov, doc);
			}

		}
		return mapping.findForward("finished");
	}

	private static void addOrUpdate(HttpServletRequest request, DemographicData.Demographic demo, ProviderData prov, EDoc doc) {
		logger.debug("called addOrUpdate()");

		PHRService phrService = (PHRService) SpringUtils.getBean("phrService");
		
		try {
	    	if (phrService.isIndivoRegistered(PHRConstants.DOCTYPE_BINARYDATA(), doc.getDocId())) {
	    		// update
	    		logger.debug("called update");
	    		String phrIndex = phrService.getPhrIndex(PHRConstants.DOCTYPE_BINARYDATA(), doc.getDocId());
	    		phrService.sendUpdateBinaryData(prov, demo.getChartNo(), PHRDocument.TYPE_DEMOGRAPHIC, demo.getPin(), doc, phrIndex);
	    	} else {
	    		// add
	    		logger.debug("called add");
	    		phrService.sendAddBinaryData(prov, demo.getChartNo(), PHRDocument.TYPE_DEMOGRAPHIC, demo.getPin(), doc);
	    	}
	    } catch (Exception e) {
	    	logger.error("Error", e);
	    	request.setAttribute("error_msg", e.getMessage());
	    }
    }
}
