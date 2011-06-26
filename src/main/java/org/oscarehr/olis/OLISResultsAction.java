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

import oscar.oscarLab.ca.all.parsers.Factory;
import oscar.oscarLab.ca.all.parsers.MessageHandler;
import oscar.oscarLab.ca.all.parsers.OLISHL7Handler;
import oscar.oscarLab.ca.all.util.Utilities;

public class OLISResultsAction extends DispatchAction {

	public static HashMap<String, OLISHL7Handler> searchResultsMap = new HashMap<String, OLISHL7Handler>();
	
	@Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		try {
			String olisResultString = (String) request.getAttribute("unsignedResponse");
			if (olisResultString == null) { 
				olisResultString = request.getParameter("unsignedResponse");
				request.setAttribute("unsignedResponse", olisResultString);
			}
			if(olisResultString == null) {
				List<String> resultList = new LinkedList<String>();
				request.setAttribute("resultList", resultList);				
				return mapping.findForward("results");
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
