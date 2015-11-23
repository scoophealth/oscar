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
package org.oscarehr.sharingcenter.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.sharingcenter.dao.ClinicInfoDao;
import org.oscarehr.sharingcenter.model.ClinicInfoDataObject;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

public class ClinicInfoServlet extends Action {

	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

    	if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_admin", "r", null)) {
        	throw new SecurityException("missing required security object (_admin)");
        }
    	
        ClinicInfoDao clinicDao = SpringUtils.getBean(ClinicInfoDao.class);

        // update vs insert
        ClinicInfoDataObject existing = clinicDao.getClinic();
        if (request.getParameter("form_id") != null && !request.getParameter("form_id").isEmpty() && existing != null) {
            existing.setOid(request.getParameter("form_oid"));
            existing.setName(request.getParameter("form_name"));
            existing.setLocalAppName(request.getParameter("form_appname"));
            existing.setFacilityName(request.getParameter("form_facilityname"));
            existing.setUniversalId(request.getParameter("form_universal"));
            existing.setNamespaceId(request.getParameter("form_namespace"));
            existing.setSourceId(request.getParameter("form_source"));
            clinicDao.merge(existing);

        } else {
            ClinicInfoDataObject newInfo = new ClinicInfoDataObject();
            newInfo.setOid(request.getParameter("form_oid"));
            newInfo.setName(request.getParameter("form_name"));
            newInfo.setLocalAppName(request.getParameter("form_appname"));
            newInfo.setFacilityName(request.getParameter("form_facilityname"));
            newInfo.setUniversalId(request.getParameter("form_universal"));
            newInfo.setNamespaceId(request.getParameter("form_namespace"));
            newInfo.setSourceId(request.getParameter("form_source"));
            clinicDao.persist(newInfo);
        }

        return new ActionForward("/administration/index.jsp?show=sharingCenterMenu&load=scManageClinicLink");
        
    }

}
