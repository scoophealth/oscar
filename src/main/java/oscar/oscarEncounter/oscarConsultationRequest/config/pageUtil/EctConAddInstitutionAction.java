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
import org.oscarehr.common.dao.InstitutionDao;
import org.oscarehr.common.model.Institution;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class EctConAddInstitutionAction extends Action {

	private static final Logger logger=MiscUtils.getLogger();

	private InstitutionDao institutionDao= SpringUtils.getBean(InstitutionDao.class);
	private static SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {

	  	if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_con", "w", null)) {
			throw new SecurityException("missing required security object (_con)");
		}
		
		Institution institution=null;
		EctConAddInstitutionForm addInstitutionForm = (EctConAddInstitutionForm)form;

		int whichType = addInstitutionForm.getWhichType();
		if(whichType == 1) //create
		{
			institution=new Institution();
			populateFields(institution, addInstitutionForm);
			institutionDao.persist(institution);
		
		}
		else if (whichType == 2) // update
		{
            request.setAttribute("upd", true);

			Integer id = Integer.parseInt(addInstitutionForm.getId());
			institution=institutionDao.find(id);
			populateFields(institution, addInstitutionForm);
			institutionDao.merge(institution);
			
		}
		else
		{
			logger.error("missed a case, whichType="+whichType);
		}

		addInstitutionForm.resetForm();

		String added=""+institution.getName();
		request.setAttribute("Added", added);
		return mapping.findForward("success");
	}



	private void populateFields(Institution institution, EctConAddInstitutionForm addInstitutionForm) {
		institution.setName(addInstitutionForm.getName());
		institution.setAddress(addInstitutionForm.getAddress());
		institution.setCity(addInstitutionForm.getCity());
		institution.setProvince(addInstitutionForm.getProvince());
		institution.setPostal(addInstitutionForm.getPostal());
		institution.setCountry(addInstitutionForm.getCountry());
		institution.setPhone(addInstitutionForm.getPhone());
		institution.setFax(addInstitutionForm.getFax());
		institution.setWebsite(addInstitutionForm.getWebsite());
		institution.setEmail(addInstitutionForm.getEmail());
		institution.setAnnotation(addInstitutionForm.getAnnotation());
	}
}
