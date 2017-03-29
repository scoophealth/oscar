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

package oscar.service;

import java.util.List;

import org.oscarehr.common.dao.SecRoleDao;
import org.oscarehr.common.dao.SiteDao;
import org.oscarehr.common.dao.SiteRoleMpgDao;
import org.oscarehr.common.model.SecRole;
import org.oscarehr.common.model.Site;
import org.oscarehr.util.SpringUtils;

import com.quatro.dao.security.SecroleDao;

public class SiteRoleManager {
	//get sites which user only can access. 
		//means sites for which user's role is configured in site_role_mpg.access_role_id
		public List<Site> getSitesWhichUserCanOnlyAccess(String providerNo) {
			SiteDao siteDao = SpringUtils.getBean(SiteDao.class);
			SiteRoleMpgDao siteRoleMpgDao = SpringUtils.getBean(SiteRoleMpgDao.class);
			SecRoleDao secRoleDao = SpringUtils.getBean(SecRoleDao.class);
			
			List<Integer> roleNos = secRoleDao.findRoleNosByProviderNo(providerNo);
			List<Integer> siteIds = siteRoleMpgDao.getSiteIdByAccRoleId(roleNos);
			
			return siteDao.findBySiteIds(siteIds, false);
		}
		
		//get sites for which user's role is configured in site_role_mpg.admit_discharge_role_id
		public List<Site> getSitesWhichUserCanOnlyAdmitDischargeTo(String providerNo) {
			SiteDao siteDao = SpringUtils.getBean(SiteDao.class);
			SiteRoleMpgDao siteRoleMpgDao = SpringUtils.getBean(SiteRoleMpgDao.class);
			SecRoleDao secRoleDao = SpringUtils.getBean(SecroleDao.class);
			
			List<Integer> roleNos = secRoleDao.findRoleNosByProviderNo(providerNo);
			List<Integer> siteIds = siteRoleMpgDao.getSiteIdByAdmitRoleId(roleNos);
			
			return siteDao.findBySiteIds(siteIds, false);
		}
			
		//get sites that all users can access
		//means sites for which there is no entry in site_role_mpg where access_role_id is not null
		public List<Site> getAllSitesAccessibleToAllUsers() {

			SiteDao siteDao = SpringUtils.getBean(SiteDao.class);
			SiteRoleMpgDao siteRoleMpgDao = SpringUtils.getBean(SiteRoleMpgDao.class);
			
			List<Integer> siteIds = siteRoleMpgDao.getSiteIdAccRoleNotNull();
			
			return siteDao.findBySiteIds(siteIds, true);
		}
		
		public String getAccessRolesAssociatedWithSiteStr(int siteId) {
			String str = "";
			
			List<SecRole> list = getAccessRolesAssociatedWithSite(siteId);
			if (list!=null && list.size()>0) {
				for(int i=0;i<list.size();i++) {
					SecRole secrole = list.get(i);
					if(i==0)
						str = secrole.getName();
					else
						str = str+", "+secrole.getName();
				}
			}
			
			return str;
		}
		
		public List<SecRole> getAccessRolesAssociatedWithSite(int siteId) {
			SecRoleDao secRoleDao = SpringUtils.getBean(SecRoleDao.class);
			SiteRoleMpgDao siteRoleMpgDao = SpringUtils.getBean(SiteRoleMpgDao.class);
			
			List<Integer> accRoleIds = siteRoleMpgDao.getAccRoleIsBySiteId(siteId);
			
			return secRoleDao.findByRoleNos(accRoleIds);
		}
		
		public List<SecRole> getAdmitDischargeRolesAssociatedWithSite(int siteId) {
			SecRoleDao secRoleDao = SpringUtils.getBean(SecRoleDao.class);
			SiteRoleMpgDao siteRoleMpgDao = SpringUtils.getBean(SiteRoleMpgDao.class);
			
			List<Integer> admitRoleIds = siteRoleMpgDao.getAdmitRoleIsBySiteId(siteId);
			
			return secRoleDao.findByRoleNos(admitRoleIds);
		}
}
