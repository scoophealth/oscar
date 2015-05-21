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
package org.oscarehr.common.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.MiscUtils;

import oscar.OscarAction;
import oscar.OscarDocumentCreator;
import oscar.util.ConcatPDF;

/**
* Originally developed by Prylynx for SJHCG
*/
public class PrintReferralLabelAction extends OscarAction {

	public PrintReferralLabelAction() {
	}

	@SuppressWarnings("resource")
    private InputStream getInputStream()  {
		InputStream ins = null;
		try {
			ins = new FileInputStream(System.getProperty("user.home") + "/reflabel.xml");
		} catch (IOException e) {
			MiscUtils.getLogger().warn("no reflabel.xml found in user's home directory, going to backup");
		} 
		if (ins == null) {
			ins = getClass().getResourceAsStream("/org/oscarehr/common/web/reflabel.xml");
		}
		return ins;
	}
	
	public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
		//patient
		String classpath = (String) request.getSession().getServletContext().getAttribute("org.apache.catalina.jsp_classpath");
		System.setProperty("jasper.reports.compile.class.path", classpath);

		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("billingreferral_no", request.getParameter("billingreferralNo"));
		ServletOutputStream sos = null;
		InputStream ins = null;

		try {
			sos = response.getOutputStream();

			response.setHeader("Content-disposition", getHeader(response).toString());

			String idList = request.getParameter("ids");
			
			if("true".equals(request.getParameter("useCheckList"))) {
				idList = "";
				List<ProfessionalSpecialist> checkedSpecs = (List<ProfessionalSpecialist>) request.getSession().getAttribute("billingReferralAdminCheckList");
		    	if(checkedSpecs != null && checkedSpecs.size()>0) {
		    		for(ProfessionalSpecialist ps:checkedSpecs) {
		    			idList  += ("," +ps.getId()); 
		    		}
		    		idList = idList.substring(1);
		    	}
			}
			if (!StringUtils.isEmpty(idList)) {
				String[] ids = idList.split(",");
				ArrayList<Object> printList = new ArrayList<Object>();
				OscarDocumentCreator osc = new OscarDocumentCreator();
				
				for (int x = 0; x < ids.length; x++) {
					FileOutputStream fos = null;
					try {
						File f = File.createTempFile("physlabel", ".pdf");
						fos = new FileOutputStream(f);
						ins = getInputStream();
						parameters.put("billingreferral_no", ids[x]);
						osc.fillDocumentStream(parameters, fos, "pdf", ins, DbConnectionFilter.getThreadLocalDbConnection());
						printList.add(f.getAbsolutePath());
					}finally {
						IOUtils.closeQuietly(fos);
						IOUtils.closeQuietly(ins);
					}
				}
				ConcatPDF.concat(printList, sos);
			} else {
				ins = getInputStream();
				OscarDocumentCreator osc = new OscarDocumentCreator();
				osc.fillDocumentStream(parameters, sos, "pdf", ins, DbConnectionFilter.getThreadLocalDbConnection());
			}

		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
		} finally {
			IOUtils.closeQuietly(ins);
		}

		if("true".equals(request.getParameter("useCheckList"))) {
			request.getSession().setAttribute("billingReferralAdminCheckList",new ArrayList<ProfessionalSpecialist>());
		}
		return actionMapping.findForward(this.target);
	}

	private StringBuilder getHeader(HttpServletResponse response) {
		StringBuilder strHeader = new StringBuilder();
		strHeader.append("label_");
		strHeader.append(".pdf");
		response.setHeader("Cache-Control", "max-age=0");
		response.setDateHeader("Expires", 0);
		response.setContentType("application/pdf");
		StringBuilder sbContentDispValue = new StringBuilder();
		sbContentDispValue.append("inline; filename="); //inline - display
		sbContentDispValue.append(strHeader);
		return sbContentDispValue;
	}

}
