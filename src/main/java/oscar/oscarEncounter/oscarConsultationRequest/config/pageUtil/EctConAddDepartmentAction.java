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
package oscar.oscarEncounter.oscarConsultationRequest.config.pageUtil;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.dao.DepartmentDao;
import org.oscarehr.common.model.Department;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class EctConAddDepartmentAction extends Action {

	private static final Logger logger=MiscUtils.getLogger();

	private DepartmentDao DepartmentDao= SpringUtils.getBean(DepartmentDao.class);
	private static SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		
	  	if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_con", "w", null)) {
			throw new SecurityException("missing required security object (_con)");
		}

		Department Department=null;
		EctConAddDepartmentForm addDepartmentForm = (EctConAddDepartmentForm)form;

		int whichType = addDepartmentForm.getWhichType();
		if(whichType == 1) //create
		{
			Department=new Department();
			populateFields(Department, addDepartmentForm);
			DepartmentDao.persist(Department);
		
		}
		else if (whichType == 2) // update
		{
            request.setAttribute("upd", true);

			Integer id = Integer.parseInt(addDepartmentForm.getId());
			Department=DepartmentDao.find(id);
			populateFields(Department, addDepartmentForm);
			DepartmentDao.merge(Department);
			
		}
		else
		{
			logger.error("missed a case, whichType="+whichType);
		}

		addDepartmentForm.resetForm();

		String added=""+Department.getName();
		request.setAttribute("Added", added);
		return mapping.findForward("success");
	}



	private void populateFields(Department Department, EctConAddDepartmentForm addDepartmentForm) {
		Department.setName(addDepartmentForm.getName());
		Department.setAnnotation(addDepartmentForm.getAnnotation());
	}
}
