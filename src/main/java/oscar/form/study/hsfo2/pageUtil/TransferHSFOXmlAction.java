/**
 * Copyright (C) 2007  Heart & Stroke Foundation
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
package oscar.form.study.hsfo2.pageUtil;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

import oscar.oscarDemographic.data.DemographicData;


public class TransferHSFOXmlAction extends Action
{
	Logger logger = MiscUtils.getLogger();
	
	public TransferHSFOXmlAction()
	{

	}

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		// parse("/Hsfo_Hbps_Data Sample ver 2007-02-12.xml");

		String providerNo = request.getParameter("xmlHsfoProviderNo");
		String demographicNo = request.getParameter("xmlHsfoDemographicNo");

		Integer demoNo = new Integer(0);
		if (demographicNo != null)
			demoNo = new Integer(demographicNo.trim());
	
		XMLTransferUtil tfutil=new XMLTransferUtil();
		if (demoNo!=0){
			//if no internal doctor assigned for designated patient,report error.
			DemographicData demoData = new DemographicData();
			String internalId=demoData.getDemographic(LoggedInInfo.getLoggedInInfoFromSession(request), demoNo.toString()).getProviderNo();
			
			if(internalId==null || internalId.length()==0){
				request.setAttribute("HSFOmessage", "Unable to upload. Please go to the master page, and assign a internal doctor to this patient.");
				return mapping.findForward("HSFORE");
			}
		}

		try
		{
	      StringBuilder memoMsg = new StringBuilder();
	      RecommitHSFOAction.uploadXmlToServer( tfutil, providerNo, demoNo, memoMsg );
	
	      request.setAttribute( "HSFOmessage", memoMsg.toString() );

		} 
		catch (IOException e)
		{
			logger.info(e.getMessage());
			throw e;
		}
		
		return mapping.findForward("HSFORE");
	}
}
