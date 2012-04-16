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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.util.MiscUtils;

import com.indivica.olis.Driver;

import oscar.oscarLab.ca.all.parsers.Factory;
import oscar.oscarLab.ca.all.parsers.MessageHandler;
import oscar.oscarLab.ca.all.parsers.OLISHL7Handler;
import oscar.oscarLab.ca.all.util.Utilities;

public class OLISResultsAction extends DispatchAction {

	public static HashMap<String, OLISHL7Handler> searchResultsMap = new HashMap<String, OLISHL7Handler>();
	
	@Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		try {
			String olisResultString = (String) request.getAttribute("olisResponseContent");			
			if(olisResultString == null) {
				olisResultString = oscar.Misc.getStr(request.getParameter("olisResponseContent"), "");
				request.setAttribute("olisResponseContent", olisResultString);
				
				String olisXmlResponse = oscar.Misc.getStr(request.getParameter("olisXmlResponse"), "");
				if (olisResultString.trim().equalsIgnoreCase("")) {
					if (!olisXmlResponse.trim().equalsIgnoreCase("")) {
						Driver.readResponseFromXML(request, olisXmlResponse);
					}
					
					List<String> resultList = new LinkedList<String>();
					request.setAttribute("resultList", resultList);				
					return mapping.findForward("results");
				}
			}
			
			UUID uuid = UUID.randomUUID();

			File tempFile = new File(System.getProperty("java.io.tmpdir") + "/olis_" + uuid.toString() + ".response");
			FileUtils.writeStringToFile(tempFile, olisResultString);

			
			@SuppressWarnings("unchecked")
            ArrayList<String> messages = Utilities.separateMessages(System.getProperty("java.io.tmpdir") + "/olis_" + uuid.toString() + ".response");
			
			List<String> resultList = new LinkedList<String>();
			
			if (messages != null) {
				for (String message : messages) {
					
					String resultUuid = UUID.randomUUID().toString();
										
					tempFile = new File(System.getProperty("java.io.tmpdir") + "/olis_" + resultUuid.toString() + ".response");
					FileUtils.writeStringToFile(tempFile, message);
					
					// Parse the HL7 string...
					MessageHandler h = Factory.getHandler("OLIS_HL7", message);
					if (h.getOBRCount() == 0) {
						continue;
					}
					
					searchResultsMap.put(resultUuid, (OLISHL7Handler)h);
					resultList.add(resultUuid);
				}
				
				request.setAttribute("resultList", resultList);
			}

		} catch (Exception e) {
			MiscUtils.getLogger().error("Can't pull out messages from OLIS response.", e);
		}
		return mapping.findForward("results");
	}
}
