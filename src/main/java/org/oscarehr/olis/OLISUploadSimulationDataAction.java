/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */
package org.oscarehr.olis;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.DefaultFileItemFactory;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.util.MiscUtils;

import com.indivica.olis.Driver;

public class OLISUploadSimulationDataAction extends DispatchAction {
	
	@Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		Logger logger = MiscUtils.getLogger();
		
		String simulationData = null;
		boolean simulationError = false;
		
		try {
		    FileUpload upload = new FileUpload(new DefaultFileItemFactory());
		    @SuppressWarnings("unchecked")
		    List<FileItem> items = upload.parseRequest(request);
		    for(FileItem item:items) {
		    	if(item.isFormField()) {
		    		String name = item.getFieldName();
		    		if(name.equals("simulateError")) {
		    			simulationError=true;
		    		}
		    	} else {
		    		if(item.getFieldName().equals("simulateFile")) {
			    		InputStream is = item.getInputStream();
			    		StringWriter writer = new StringWriter();
			    		IOUtils.copy(is, writer, "UTF-8");
			    		simulationData = writer.toString();			    		
		    		}
		    	}
		    }
		    
		    if(simulationData != null && simulationData.length()>0) {
		    	if(simulationError) {
		    		Driver.readResponseFromXML(request, simulationData);
		    		simulationData = (String)request.getAttribute("olisResponseContent");
		    		request.getSession().setAttribute("errors",request.getAttribute("errors"));
		    	}
		    	request.getSession().setAttribute("olisResponseContent", simulationData);
		    	request.setAttribute("result", "File successfully uploaded");
		    }
		}catch(Exception e) {
			MiscUtils.getLogger().error("Error",e);
		}

		return mapping.findForward("success");
	}
}
