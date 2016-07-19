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

/***
 * 
 * @author Rohit Prajapati (rohitprajapati54@gmail.com)
 * 
 * Utility to check whether user has rights to access site or not.
 * If user has access to site, then he can access the patients of that site.
 * 
 * This users below 2 columns of the site table.
 * # roleIdsOnlyAccessThisSite - comma separated roles id - 
 *  User can access all the sites for which user's role is there in this column.
 *  
 *  But once the user's role is configured in this column even for the single site,
 *  then user will not have access to any other site.
 *  
 *  If user's role is not configured in this column for any site, then user can 
 *  access all the sites.
 *    
 * # roleIdsCouldAmitDischarge - comma separated roles id -
 *  user whose role is configured in this column, can only enroll/discharge the 
 *  patient to/from this site. no other patients will have access to do this.
 *  
 *  if no role is configured in this column, then all user have rights to 
 *  enroll/discharge patients to/from this site.
 */

package oscar.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.DemographicSiteDao;
import org.oscarehr.common.dao.SecRoleDao;
import org.oscarehr.common.model.DemographicSite;
import org.oscarehr.common.model.SecRole;
import org.oscarehr.common.model.Site;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.springframework.jdbc.core.RowMapper;

import oscar.dao.OscarSuperDao;
import oscar.service.SiteRoleManager;


public class SuperSiteUtil extends OscarSuperDao
{

	private static Logger logger = MiscUtils.getLogger();
	
	private String currentProviderNo;
	private List<Site> userAccessSites;
	private List<Integer> userRoles;
	
	/*public SuperSiteUtil(String currentProviderNo)
	{
		this.currentProviderNo = currentProviderNo;
		userAccessSites = getSitesWhichUserCanOnlyAccess();
	}
	*/
	//this will be used.. from where checkSuperSiteAccess needs to be called
	//in this case provider no. will not be passed.. instead it will be fetched from the
	//request itself in checkSuperSiteAccess method.. and userAccessSites will be initialized there
	private SuperSiteUtil()
	{
	}
	
	/***
	 * @param userId
	 * @return
	 * @throws Exception
	 * 
	 * -- return list of sites which are accessible by the user. 
	 *  
	 * Once user's role is configured in site.roleIdsOnlyAccessThisSite column for atleast one site, 
	 * then he will not have access to any other site. 
	 * So first check whether the user's role is configured in site.roleIdsOnlyAccessThisSite for any site? 
	 * If yes then return only those sites. 
	 * If no then return those site for which site.roleIdsOnlyAccessThisSite=null
	 */
	public List<Site> getUserAccessibleSites(String userId)
	{
		List<Site> sites = null;
		
		//List<Site> userAccessSites = getSitesWhichUserCanOnlyAccess(userId);
		if(userAccessSites==null || userAccessSites.size()==0)
		{
			//if user doesn't have access to any specific site (user's role is not configured in site_role_mpg, 
			//then return all sites accessible to all users)
			SiteRoleManager siteRoleMgr = new SiteRoleManager();
			sites = siteRoleMgr.getAllSitesAccessibleToAllUsers();
		}
		else
		{
			sites = userAccessSites;
		}
		
		return sites;
	}
	
	/***
	 * check whether supplied user has access to open patient's detail (e.g from encounter, master, rx etc..)
	 * -- means if user has access to only specific site (user's role is in site_role_mpg.access_role_id),
	 * and if patient is enrolled in that site, then user has access rights..
	 *
	 * -- if user doesn't have access to any specific site (user's role is not in site_role_mpg.access_role_id),
	 * then user can access any patient
	 * 
	 * -- if user has access to specific site (user's role is in site_role_mpg.access_role_id),
	 * and if patient is not enrolled in that site, then user is not allowed to view that patient
	 * 
	 *  parameter:
	 *  userId - userid who is accessing the patient.. may be logged in userid
	 *  demographicId - patient id which is being accessed.
	 * @return
	 */
	public boolean isUserAllowedToOpenPatientDtl(String demographicId) 
	{
		boolean flg = false;
		
		if(!org.oscarehr.common.IsPropertiesOn.isMultisitesEnable())
		{
			flg = true;
			return flg;
		}
		
		//List<Site> userAccessSites = getSitesWhichUserCanOnlyAccess(userId);
		if(userAccessSites==null || userAccessSites.size()==0)
		{
			//means user doesn't have any specific site access. so it can access any site..
			flg = true;
		}
		else
		{
			//user is configured to access certain sites.. check if patient is enrolled in that site or not
			List<Integer> userSiteIdList = getSiteIdList(userAccessSites);
			
			//get patient's site
			DemographicSiteDao demographicSiteDao = (DemographicSiteDao)SpringUtils.getBean("demographicSiteDao");
			List<DemographicSite> demoSiteList = demographicSiteDao.findSitesByDemographicId(Integer.parseInt(demographicId));
			
			if(demoSiteList!=null)
			{
				for (DemographicSite demographicSite : demoSiteList)
				{
					if(demographicSite!=null && demographicSite.getSiteId()!=null)
					{
						if(userSiteIdList.contains(demographicSite.getSiteId()))
						{
							flg = true;
							break;
						}
					}
				}
			}
		}
		
		return flg;
	}
	
	private boolean isUserAllowedToOpenPatientDtl(HttpServletRequest request, String demographicIdReqParamName)
	{
		logger.info("in SuperSiteUtil : isUserAllowedToOpenPatientDtl");
		
		boolean flg = true;
		if(org.oscarehr.common.IsPropertiesOn.isMultisitesEnable())
		{
			String userId = "", demographicId = "";
			if(request.getSession().getAttribute("user")!=null)
				  userId = request.getSession().getAttribute("user").toString();
			
			if(request.getParameter(demographicIdReqParamName)!=null)
				demographicId = request.getParameter(demographicIdReqParamName).trim();
			
			logger.info("userId = "+userId);
			logger.info("demographicId = "+demographicId);
			
			//here we need to initialize userAccessSites.
			//userAccessSites = getSitesWhichUserCanOnlyAccess();
			setCurrentProviderNo(userId);
			
			if(demographicId!=null && demographicId.trim().length()>0)
			{
				flg = isUserAllowedToOpenPatientDtl(demographicId);
			}
		}
		
		logger.info("out SuperSiteUtil : isUserAllowedToOpenPatientDtl ... flg = "+flg);
		return flg;
	}
	
	public boolean isUserAllowedToAdmitDischargeForSite(int siteId)
	{
		boolean flg = false;
		SiteRoleManager siteRoleMgr = new SiteRoleManager();
		
		if(org.oscarehr.common.IsPropertiesOn.isMultisitesEnable())
		{
			//get roles associated with siteid .. site_role_mpg.admit_discharge_role_id
			List<SecRole> siteAdmitDischargeRoleList = siteRoleMgr.getAdmitDischargeRolesAssociatedWithSite(siteId);
			
			//if atleast one role is configured.. and user's role is there then only user have access to
			//admin/discharge.. otherwise not
			if(siteAdmitDischargeRoleList!=null && siteAdmitDischargeRoleList.size()>0)
			{
				//admit/discharge roles are configured for site.. check if user's role is there
				if(userRoles!=null)
				{
					for (SecRole secrole : siteAdmitDischargeRoleList)
					{
						if(secrole!=null && userRoles.contains(secrole.getId()))
						{
							flg = true;
							break;
						}
					}
				}
			}
			else
			{
				//no admit/discharge role configured for site 
				flg = false;
			}
		}
		else
		{
			flg = true;
		}
		
		return flg;
	}
	
	public static SuperSiteUtil getInstance()
	{
		SuperSiteUtil superSiteUtil = (SuperSiteUtil) SpringUtils.getBean("superSiteUtil");
		return superSiteUtil;
	}
	
	public static SuperSiteUtil getInstance(String currentProviderNo)
	{
		SuperSiteUtil superSiteUtil = (SuperSiteUtil) SpringUtils.getBean("superSiteUtil");
		superSiteUtil.setCurrentProviderNo(currentProviderNo);
		return superSiteUtil;
	}
	
	//check access and if user is not allowed to access demographic then forward to error page
	public void checkSuperSiteAccess(HttpServletRequest request, HttpServletResponse response, String demographicIdReqParamName) throws IOException, ServletException
	{
		if(org.oscarehr.common.IsPropertiesOn.isMultisitesEnable())
		{
			if(!isUserAllowedToOpenPatientDtl(request, demographicIdReqParamName))
			{
				request.getRequestDispatcher("/common/superSiteAccessError.jsp").forward(request, response);
			}			
		}
	}
	
	private List<Integer> getSiteIdList(List<Site> siteList)
	{
		List<Integer> list = null;
		
		if(siteList!=null)
		{
			list = new ArrayList<Integer>();
			
			for (Site site : siteList)
			{
				list.add(site.getSiteId());
			}
		}
		
		return list;
	}
	
	@Override
	protected String[][] getDbQueries()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Map<String, RowMapper> getRowMappers()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String getCurrentProviderNo()
	{
		return currentProviderNo;
	}

	public void setCurrentProviderNo(String currentProviderNo)
	{
		this.currentProviderNo = currentProviderNo;
		SiteRoleManager siteRoleMgr = new SiteRoleManager();
		userAccessSites = siteRoleMgr.getSitesWhichUserCanOnlyAccess(currentProviderNo);
		SecRoleDao secRoleDao = SpringUtils.getBean(SecRoleDao.class);
		userRoles = secRoleDao.findRoleNosByProviderNo(currentProviderNo);
	}
}
