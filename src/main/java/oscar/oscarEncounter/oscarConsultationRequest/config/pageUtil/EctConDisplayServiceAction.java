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

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.dao.ServiceSpecialistsDao;
import org.oscarehr.common.model.ServiceSpecialists;
import org.oscarehr.common.model.ServiceSpecialistsPK;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class EctConDisplayServiceAction extends Action {
   
	private ServiceSpecialistsDao dao = SpringUtils.getBean(ServiceSpecialistsDao.class);
	private static SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
   public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
   throws ServletException, IOException {
	   
	   if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_con", "r", null)) {
		   throw new SecurityException("missing required security object (_con)");
	   }
	   
      EctConDisplayServiceForm displayServiceForm = (EctConDisplayServiceForm)form;
      String serviceId = displayServiceForm.getServiceId();
      String specialists[] = displayServiceForm.getSpecialists();
      MiscUtils.getLogger().debug("service id ".concat(String.valueOf(String.valueOf(serviceId))));
      MiscUtils.getLogger().debug("num specs".concat(String.valueOf(String.valueOf(specialists.length))));
      
      for(ServiceSpecialists s:dao.findByServiceId(Integer.parseInt(serviceId))) {
    	  dao.remove(s.getId());
      }
      for(int i = 0; i < specialists.length; i++) {
     	 ServiceSpecialists ss = new ServiceSpecialists();
     	 ss.setId(new ServiceSpecialistsPK(Integer.parseInt(serviceId),Integer.parseInt(specialists[i])));
     	 dao.persist(ss);
      }
      
      
      EctConConstructSpecialistsScriptsFile constructSpecialistsScriptsFile = new EctConConstructSpecialistsScriptsFile();
      constructSpecialistsScriptsFile.makeString(request.getLocale());
      return mapping.findForward("success");
   }
}
