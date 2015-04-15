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
//used to create and edit affinity domains
package org.oscarehr.sharingcenter.actions;

import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.marc.shic.core.configuration.IheAffinityDomainConfiguration;
import org.marc.shic.core.exceptions.IheConfigurationException;
import org.marc.shic.core.utils.ConfigurationUtility;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.sharingcenter.dao.AffinityDomainDao;
import org.oscarehr.sharingcenter.model.AffinityDomainDataObject;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class AffinityDomainParseServlet extends Action {

	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IheAffinityDomainConfiguration config = null;
        InputStream filecontent = null;

        if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_admin", "r", null)) {
        	throw new SecurityException("missing required security object (_admin)");
        }
        
        try {
            List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
            for (FileItem item : items) {
                if (item.isFormField()) {
                    //nothing to do here.
                } else {
                    // Process form file field (input type="file").
                    filecontent = item.getInputStream();
                }
            }
            if (filecontent != null) {
                config = ConfigurationUtility.parseConfiguration(filecontent);
                AffinityDomainDataObject affinityDomain = new AffinityDomainDataObject(config);
                AffinityDomainDao dao = SpringUtils.getBean(AffinityDomainDao.class);
                dao.persist(affinityDomain);
                Integer id = affinityDomain.getId();
                response.sendRedirect("network.jsp?id=" + id);
                return null;
            }
        } catch (FileUploadException ex) {
            MiscUtils.getLogger().warn("Problem uploading file", ex);
        } catch (IheConfigurationException ex) {
            MiscUtils.getLogger().error("Unable to parse affinity domain configuration file.", ex);
        }
        response.sendRedirect("manage.jsp");
        return null;
    }

}
