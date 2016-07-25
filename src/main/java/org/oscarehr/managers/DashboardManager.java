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
package org.oscarehr.managers;
import java.util.List;

import org.oscarehr.common.dao.DashboardDao;
import org.oscarehr.common.dao.IndicatorTemplateDao;
import org.oscarehr.common.model.Dashboard;
import org.oscarehr.common.model.IndicatorTemplate;
import org.oscarehr.dashboard.handler.IndicatorTemplateHandler;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import oscar.log.LogAction;

@Service
public class DashboardManager {

	public static enum ObjectName { IndicatorTemplate, Dashboard }
	@Autowired
	private SecurityInfoManager securityInfoManager;
	@Autowired
	private IndicatorTemplateDao indicatorTemplateDao;
	@Autowired
	private DashboardDao dashboardDao;
	
	
	/**
	 * Toggles the active status of a given class name.
	 * Options are: 
	 * - IndicatorTemplate
	 * - Dashboard
	 */
	public void toggleStatus( LoggedInInfo loggedInInfo, int objectId, ObjectName objectClassName, Boolean state ) {		
		switch( objectClassName ) {
			case IndicatorTemplate: toggleIndicatorActive( loggedInInfo, objectId, state );
			break;
			case Dashboard: toggleDashboardActive( loggedInInfo, objectId, state );
			break;
		}		
	}
	
	
	/**
	 * Retrieves all the information for each Indicator Template query
	 * that is stored in the indicatorTemplate db table.
	 */
	public List<IndicatorTemplate> getIndicatorLibrary( LoggedInInfo loggedInInfo ) {
		if( ! securityInfoManager.hasPrivilege(loggedInInfo, "_dashboardManager", SecurityInfoManager.WRITE, null ) ) {	
			LogAction.addLog(loggedInInfo, "DashboardManager.getIndicatorLibrary", null, null, null, "User missing _dashboardManager role with write access");
			return null;
        }
		
		List<IndicatorTemplate> indicatorTemplates = indicatorTemplateDao.getIndicatorTemplates();
		
		if( indicatorTemplates != null) {
			LogAction.addLog(loggedInInfo, "DashboardManager.getIndicatorLibrary", null, null, null, "returning Indicator Template entries");
		} else {
			LogAction.addLog(loggedInInfo, "DashboardManager.getIndicatorLibrary", null, null, null, "Failed to find any Indicator Templates");	
		}
		
		return indicatorTemplates;
	}
	
	/**
	 * Toggles the Indicator active boolean switch.  True for active, false for not active.
	 */
	public void toggleIndicatorActive( LoggedInInfo loggedInInfo, int indicatorId, Boolean state ) {
		
		if( ! securityInfoManager.hasPrivilege(loggedInInfo, "_dashboardManager", SecurityInfoManager.READ, null ) ) {	
			LogAction.addLog(loggedInInfo, "DashboardManager.toggleIndicatorActive", null, null, null, "User missing _dashboardManager role with write access");
			return;
		}
		
		IndicatorTemplate indicator = indicatorTemplateDao.find(indicatorId);
		
		if( indicator != null ) {
			indicator.setActive(state);
			LogAction.addLog(loggedInInfo, "DashboardManager.toggleIndicatorActive", "Active", state+"", null, "Indicator Active state set to " );			
			indicatorTemplateDao.merge(indicator);
		}
	}
	
	/**
	 * Returns all the Dashboards created for Oscar.
	 */
	public List<Dashboard> getDashboards( LoggedInInfo loggedInInfo ) {

		if( ! securityInfoManager.hasPrivilege(loggedInInfo, "_dashboardManager", SecurityInfoManager.READ, null ) ) {	
			LogAction.addLog(loggedInInfo, "DashboardManager.getDashboards", null, null, null, "User missing _dashboardManager role with write access");
			return null;
        }
		
		List<Dashboard> dashboards = dashboardDao.getDashboards();
		
		if( dashboards != null) {
			LogAction.addLog(loggedInInfo, "DashboardManager.getDashboards", null, null, null, "returning dashboard entries");
		} else {
			LogAction.addLog(loggedInInfo, "DashboardManager.getDashboards", null, null, null, "Failed to find any Dashboards");	
		}
		
		return dashboards;
	}
	
	/**
	 * Add a new Dashboard entry or edit an old one.
	 */
	public boolean addDashboard( LoggedInInfo loggedInInfo, Dashboard dashboard ) {
		
		boolean success = Boolean.FALSE;
		
		if( ! securityInfoManager.hasPrivilege(loggedInInfo, "_dashboardManager", SecurityInfoManager.READ, null ) ) {	
			LogAction.addLog(loggedInInfo, "DashboardManager.addDashboard", null, null, null, "User missing _dashboardManager role with write access");
			return success;
        }
		
		if( dashboard.getId() == null ) {
			// all new Dashboards are active.
			dashboard.setActive(Boolean.TRUE);
			dashboardDao.persist( dashboard );
		} else {
			dashboardDao.merge( dashboard );
		}
		
		if( dashboard.getId() > 0 ) {
			LogAction.addLog(loggedInInfo, "DashboardManager.addDashboard", "Dashboard", dashboard.getId()+"", null, "New Dashboard added with id");
			success = Boolean.TRUE;
		}
		
		return success;
	}
	
	/**
	 * Toggles the Dashboard active boolean switch.  True for active, false for not active.
	 */
	public void toggleDashboardActive( LoggedInInfo loggedInInfo, int dashboardId, Boolean state ) {
		
		if( ! securityInfoManager.hasPrivilege(loggedInInfo, "_dashboardManager", SecurityInfoManager.READ, null ) ) {	
			LogAction.addLog(loggedInInfo, "DashboardManager.toggleDashboardActive", null, null, null, "User missing _dashboardManager role with write access");
			return;
		}
		
		Dashboard dashboard = dashboardDao.find(dashboardId);
		
		if( dashboard != null ) {
			dashboard.setActive(state);
			LogAction.addLog(loggedInInfo, "DashboardManager.toggleIndicatorActive", "Active State", state+"", null, "Dashboard Active state set to " );	
			dashboardDao.merge(dashboard);
		}
	}
	
	
	/**
	 * Retrieves an XML file from a servlet request object and then saves it to
	 * the local file directory and finally writes an entry in the Indicator Template db table.
	 */
	public boolean importIndicatorTemplate(LoggedInInfo loggedInInfo, byte[] bytearray ) {
		boolean success = Boolean.FALSE;
		IndicatorTemplate indicatorTemplate = null;
		
		if( ! securityInfoManager.hasPrivilege(loggedInInfo, "_dashboardManager", SecurityInfoManager.WRITE, null ) ) {	
			LogAction.addLog(loggedInInfo, "DashboardManager.importIndicatorTemplat", null, null, null, "User missing _dashboardManager role with write access");
			return success;
        }

		if( bytearray != null && bytearray.length > 0) {
			
			MiscUtils.getLogger().debug("Indicator XML Template: " + new String(bytearray) );
			
			IndicatorTemplateHandler templateHandler = new IndicatorTemplateHandler(bytearray);
			indicatorTemplate = templateHandler.getIndicatorTemplateEntity();
		}
		
		// needs to be a check here to ensure the template is not being uploaded again. 
		// If the indicator has a unique Id then an update is in order.
		// The unique ID is removed when the template is exported.
		// only a regulator knows the real key. 
		
		if( indicatorTemplate != null ) {
			this.indicatorTemplateDao.persist( indicatorTemplate );
			if( indicatorTemplate.getId() > 0) {
				success = Boolean.TRUE;
			}
		}

		return success;
	}
	
	/**
	 * Overload method with a indicatorId list parameter.
	 */
	public boolean assignIndicatorToDashboard(LoggedInInfo loggedInInfo, int dashboardId, List<Integer> indicatorId ) {
		boolean success = Boolean.FALSE;
		
		for(Integer id : indicatorId) {
			success = assignIndicatorToDashboard(loggedInInfo, dashboardId, id );
			if( ! success ) {
				break;
			}
		}
		
		return success;
	}
	
	/**
	 * Assign an Indicator the Dashboard where the Indicator will be displayed. 
	 */
	public boolean assignIndicatorToDashboard(LoggedInInfo loggedInInfo, int dashboardId, int indicatorId ) {
		boolean success = Boolean.FALSE;
		if( ! securityInfoManager.hasPrivilege(loggedInInfo, "_dashboardManager", SecurityInfoManager.WRITE, null ) ) {	
			LogAction.addLog(loggedInInfo, "DashboardManager.assignIndicatorToDashboard", null, null, null, "User missing _dashboardManager role with write access");
			return success;
        }
		
		IndicatorTemplate indicatorTemplate = null;
		
		if( indicatorId > 0 ) {
			indicatorTemplate = indicatorTemplateDao.find( indicatorId );
		}
		
		if( indicatorTemplate != null ) {
			
			if( dashboardId > 0 ) {			
				indicatorTemplate.setDashboardId( dashboardId );				
			} else {				
				indicatorTemplate.setDashboardId(null);
			}

			indicatorTemplateDao.merge(indicatorTemplate);
			
			if( indicatorTemplate.getId() > 0 ) {	
				success = Boolean.TRUE;
				LogAction.addLog(loggedInInfo, "DashboardManager.assignIndicatorToDashboard", "Indicator id: ", indicatorId+"", null, " assigned to Dashboard id: " + dashboardId);					
			}
		}

		return success;
	}
	
	/**
	 * Returns the raw indicator template XML for download and editing.
	 */
	public String exportIndicatorTemplate( LoggedInInfo loggedInInfo, int indicatorId ) {
		
		String template = null;
		
		if( ! securityInfoManager.hasPrivilege(loggedInInfo, "_dashboardManager", SecurityInfoManager.READ, null ) ) {	
			LogAction.addLog(loggedInInfo, "DashboardManager.exportIndicatorTemplate", null, null, null,"User missing _dashboardManager role with write access");
			return template;
        }
		
		IndicatorTemplate indicatorTemplate = indicatorTemplateDao.find(indicatorId);
		if( indicatorTemplate != null ) {
			
			template = indicatorTemplate.getTemplate();
			LogAction.addLog(loggedInInfo, "DashboardManager.exportIndicatorTemplate", "Indicator Template", indicatorTemplate.getId()+"", null, "Exporting template. Name: " + indicatorTemplate.getName() );
		}
		
		return template;
	}
}
