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

package oscar.oscarLab.ca.all.pageUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.dao.Hl7TextInfoDao;
import org.oscarehr.util.SpringUtils;


public class CreateLabelTDISAction extends Action{
	Logger logger = Logger.getLogger(CreateLabelTDISAction.class);
	
	public ActionForward execute (ActionMapping mapping,ActionForm form, HttpServletRequest request, HttpServletResponse response){
		
		CreateLabelTDISForm frm = (CreateLabelTDISForm) form;
		String label = frm.getLabel();//request.getParameter("label");
		logger.info("Label before db insert ="+label);
		String lab_no = frm.getLab_no();//request.getParameter("lab_no");
		String accessionNum = frm.getAccessionNum();//request.getParameter("accessionNum");
		String ajaxcall=request.getParameter("ajaxcall");
		
		if (label==null || label.equals("")) {
			request.setAttribute("error", "Please enter a label");
			
		}
		response.setContentType("application/json");
		Hl7TextInfoDao hl7dao = (Hl7TextInfoDao) SpringUtils.getBean("hl7TextInfoDao");

		try {
			
			int labNum = Integer.parseInt(lab_no);
			hl7dao.createUpdateLabelByLabNumber(label, labNum);
			
			logger.info("Label created successfully.");
			
			
		} catch (Exception e){
			logger.error("Error inserting label into hl7TextInfo" + e);
			request.setAttribute("error", "There was an error creating a label.");
			
		}
		
		logger.info("Label ="+label);
		label = StringEscapeUtils.escapeJavaScript(label);
		return mapping.findForward("complete");
	}

}
