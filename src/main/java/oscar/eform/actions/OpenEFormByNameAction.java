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


package oscar.eform.actions;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.dao.EFormDao;
import org.oscarehr.common.model.EForm;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;


public class OpenEFormByNameAction extends Action {

	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String eform_name = request.getParameter("eform_name");
		Integer demographic_no = Integer.parseInt(request.getParameter("demographic_no"));
		Integer fid = null;
		
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_eform", "w", null)) {
			throw new SecurityException("missing required security object (_eform)");
		}
		
		
		EFormDao eformDao = SpringUtils.getBean(EFormDao.class);
		EForm eform = eformDao.findByName(eform_name);
		
		if (eform!=null) fid = eform.getId();
		
		String url = request.getRequestURL().toString();
		String uri = request.getRequestURI();
		String cp = request.getContextPath();
		url = url.substring(0, url.length()-uri.length()) + cp;
		
		if (fid==null) url += "/eform_name_not_found";
		else if (demographic_no==null) url += "/demographic_no_not_provided";
		else url += "/eform/efmformadd_data.jsp?fid="+fid+"&demographic_no="+demographic_no;

		response.sendRedirect(url);
		return null;
	}
}
