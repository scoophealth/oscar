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
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.dao.DepartmentDao;
import org.oscarehr.common.model.Department;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

public class EctConEditDepartmentsAction extends Action{
	private static SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_con", "u", null)) {
			throw new SecurityException("missing required security object (_con)");
		}
		
		DepartmentDao DepartmentDao= SpringUtils.getBean(DepartmentDao.class);

		EctConEditDepartmentsForm editDepartmentsForm = (EctConEditDepartmentsForm) form;
		String id = editDepartmentsForm.getId();
		String delete = editDepartmentsForm.getDelete();
		String specialists[] = editDepartmentsForm.getSpecialists();

		ResourceBundle oscarR = ResourceBundle.getBundle("oscarResources", request.getLocale());

		if (delete.equals(oscarR.getString("oscarEncounter.oscarConsultationRequest.config.EditSpecialists.btnDeleteSpecialist"))) {
			if (specialists.length > 0) {
				for (int i = 0; i < specialists.length; i++)
				{
					DepartmentDao.remove(Integer.parseInt(specialists[i]));
				}
			}
			EctConConstructSpecialistsScriptsFile constructSpecialistsScriptsFile = new EctConConstructSpecialistsScriptsFile();
			constructSpecialistsScriptsFile.makeString(request.getLocale());
			return mapping.findForward("delete");
		}

		// not delete request, just update one entry
		Department Department=DepartmentDao.find(Integer.parseInt(id));

		int updater = 0;
		request.setAttribute("name", Department.getName());
	
		request.setAttribute("id", id);
		
		request.setAttribute("annotation", Department.getAnnotation());
       

		request.setAttribute("upd", new Integer(updater));
		EctConConstructSpecialistsScriptsFile constructSpecialistsScriptsFile = new EctConConstructSpecialistsScriptsFile();
		request.setAttribute("verd", constructSpecialistsScriptsFile.makeFile());
		constructSpecialistsScriptsFile.makeString(request.getLocale());
		return mapping.findForward("success");
	}
}
