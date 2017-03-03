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
import java.security.Security;
import java.util.List;

import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.oscarehr.common.dao.ClinicDAO;
import org.oscarehr.common.dao.DashboardDao;
import org.oscarehr.common.dao.IndicatorTemplateDao;
import org.oscarehr.common.model.Dashboard;
import org.oscarehr.common.model.IndicatorTemplate;
import org.oscarehr.common.model.Provider;
import org.oscarehr.dashboard.display.beans.DashboardBean;
import org.oscarehr.dashboard.display.beans.DrilldownBean;
import org.oscarehr.dashboard.display.beans.IndicatorBean;
import org.oscarehr.dashboard.factory.DashboardBeanFactory;
import org.oscarehr.dashboard.factory.DrilldownBeanFactory;
import org.oscarehr.dashboard.factory.IndicatorBeanFactory;
import org.oscarehr.dashboard.handler.ExportQueryHandler;
import org.oscarehr.dashboard.handler.IndicatorTemplateHandler;
import org.oscarehr.dashboard.handler.IndicatorTemplateXML;
import org.oscarehr.integration.dashboard.model.Clinic;
import org.oscarehr.integration.dashboard.model.User;
import org.oscarehr.integration.dashboard.model.User.Province;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.json.JSONObject;
import oscar.OscarProperties;
import oscar.log.LogAction;

@Service
public class DashboardManager {

	public static final boolean MULTI_THREAD_ON = Boolean.TRUE;
	
	public static enum ObjectName { IndicatorTemplate, Dashboard }
	@Autowired
	private SecurityInfoManager securityInfoManager;
	@Autowired
	private IndicatorTemplateDao indicatorTemplateDao;
	@Autowired
	private DashboardDao dashboardDao;
	
	@Autowired
	ClinicDAO clinicDAO;
	
	
	private Logger logger = MiscUtils.getLogger();
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
	public List<IndicatorTemplate> getIndicatorLibrary( LoggedInInfo loggedInInfo, boolean sharedOnly ) {
		if( ! securityInfoManager.hasPrivilege(loggedInInfo, "_dashboardManager", SecurityInfoManager.WRITE, null ) ) {	
			LogAction.addLog(loggedInInfo, "DashboardManager.getIndicatorLibrary", null, null, null, "User missing _dashboardManager role with write access");
			return null;
        }
		
		List<IndicatorTemplate> indicatorTemplates = null;
		
		if(!sharedOnly) {
			indicatorTemplates = indicatorTemplateDao.getIndicatorTemplates();
		} else {
			indicatorTemplates = indicatorTemplateDao.getSharedIndicatorTemplates();
		}
		
		if( indicatorTemplates != null) {
			LogAction.addLog(loggedInInfo, "DashboardManager.getIndicatorLibrary", null, null, null, "returning Indicator Template entries");
		} else {
			LogAction.addLog(loggedInInfo, "DashboardManager.getIndicatorLibrary", null, null, null, "Failed to find any Indicator Templates");	
		}
		
		return indicatorTemplates;
	}
	
	public List<IndicatorTemplate> getIndicatorLibrary( LoggedInInfo loggedInInfo ) {
		return getIndicatorLibrary(loggedInInfo,false);
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
	 * Returns ALL available Dashboards. 
	 * 
	 */
	public List<Dashboard> getDashboards( LoggedInInfo loggedInInfo ) {

		if( ! securityInfoManager.hasPrivilege(loggedInInfo, "_dashboardManager", SecurityInfoManager.READ, null ) ) {	
			LogAction.addLog(loggedInInfo, "DashboardManager.getDashboards", null, null, null, "User missing _dashboardManager role with read access");
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
	 * Returns Dashboards that are active.
	 */
	public List<Dashboard> getActiveDashboards( LoggedInInfo loggedInInfo ) {
		if( ! securityInfoManager.hasPrivilege(loggedInInfo, "_dashboardManager", SecurityInfoManager.READ, null ) ) {	
			LogAction.addLog(loggedInInfo, "DashboardManager.getActiveDashboards", null, null, null, "User missing _dashboardManager role with read access");
			return null;
        }
		
		List<Dashboard> dashboards = dashboardDao.getActiveDashboards();
		
		if( dashboards != null) {
			LogAction.addLog(loggedInInfo, "DashboardManager.getActiveDashboards", null, null, null, "returning dashboard entries");
		} else {
			LogAction.addLog(loggedInInfo, "DashboardManager.getActiveDashboards", null, null, null, "Failed to find any Dashboards");	
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
	 * 
	 * Returns a JSON string: status=success, or status=error, message=[message]
	 * 
	 */
	public String importIndicatorTemplate( LoggedInInfo loggedInInfo, byte[] bytearray ) {
		JSONObject message = new JSONObject();
		IndicatorTemplate indicatorTemplate = null;
		// Boolean isValid = Boolean.FALSE;
		
		if( ! securityInfoManager.hasPrivilege(loggedInInfo, "_dashboardManager", SecurityInfoManager.WRITE, null ) ) {	
			LogAction.addLog(loggedInInfo, "DashboardManager.importIndicatorTemplate", null, null, null, "User missing _dashboardManager role with write access");			
			message.put("status", "error");
			message.put("message", "User missing _dashboardManager role with write access");

			return message.toString();
        }

		if( bytearray != null && bytearray.length > 0) {
			
			MiscUtils.getLogger().debug("Indicator XML Template: " + new String( bytearray ) );
			
			IndicatorTemplateHandler templateHandler = new IndicatorTemplateHandler();
			templateHandler.read( bytearray );
			
			//TODO: need to validate the SQL
			
			// check Indicator query
			
			// check Drilldown query

			if( templateHandler.isValidXML() ) {
				indicatorTemplate = templateHandler.getIndicatorTemplateEntity();
			} else {
				message.put("status", "error");
				message.put("message", templateHandler.getValidationMessage() );
			}
		}
		
		if( indicatorTemplate != null ) {
			this.indicatorTemplateDao.persist( indicatorTemplate );
			if( indicatorTemplate.getId() > 0) {
				message.put("status", "success");
				message.put("message", "Template imported successfully");
			} else {
				message.put("status", "error");
				message.put("message", "Failed to persist the Indicator Template" );
			}
		}

		return message.toString();
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
		}
		
		if( template != null ) {
			LogAction.addLog(loggedInInfo, "DashboardManager.exportIndicatorTemplate", "Indicator Template", indicatorTemplate.getId()+"", null, "Exporting Indicator Template " + indicatorTemplate.getName() );			
		} else {
			LogAction.addLog(loggedInInfo, "DashboardManager.exportIndicatorTemplate", "Indicator Template", indicatorTemplate.getId()+"", null, "Failed to export Indicator Template " + indicatorTemplate.getName() );			
		}
		
		return template;
	}
	
	/**
	 * Returns a List of ACTIVE Indicator Templates based on the DashboardId 
	 */
	public List<IndicatorTemplate> getIndicatorTemplatesByDashboardId( LoggedInInfo loggedInInfo, int dashboardId ) {
		List<IndicatorTemplate> indicatorTemplates = null; 
		
		if( ! securityInfoManager.hasPrivilege(loggedInInfo, "_dashboardDisplay", SecurityInfoManager.READ, null ) ) {	
			LogAction.addLog(loggedInInfo, "DashboardManager.getIndicatorTemplatesByDashboardId", null, null, null,"User missing _dashboardManager role with write access");
			return indicatorTemplates;
        }
		
		if( dashboardId > 0 ) {
			indicatorTemplates = indicatorTemplateDao.getIndicatorTemplatesByDashboardId( dashboardId );
			LogAction.addLog(loggedInInfo, "DashboardManager.getIndicatorTemplatesByDashboardId", "Indicator Template by Dashboard Id", dashboardId+"", null, "Returning Indicator List ");			
		}
		
		return indicatorTemplates;
	}
	
	/**
	 *  Get an entire Dashboard, with all of its Indicators in a List parameter.
	 */
	public DashboardBean getDashboard( LoggedInInfo loggedInInfo, int dashboardId ) {
		
		DashboardBean dashboardBean = null;

		if( ! securityInfoManager.hasPrivilege(loggedInInfo, "_dashboardDisplay", SecurityInfoManager.READ, null ) ) {	
			LogAction.addLog(loggedInInfo, "DashboardManager.getDashboard", null, null, null,"User missing _dashboardManager role with write access");
			return dashboardBean;
        }
		
		Dashboard dashboardEntity = null;
		DashboardBeanFactory dashboardBeanFactory = null;
		
		if( dashboardId > 0 ) {			
			dashboardEntity = dashboardDao.find( dashboardId );
			List<IndicatorTemplate> indicatorTemplates = getIndicatorTemplatesByDashboardId( loggedInInfo, dashboardId );
			dashboardEntity.setIndicators( indicatorTemplates );
		}
		
		if( dashboardEntity != null ) {
			// Add the indicators and panels.
			dashboardBeanFactory = new DashboardBeanFactory( loggedInInfo, dashboardEntity );
		}

		if( dashboardBeanFactory != null ) {
			dashboardBean = dashboardBeanFactory.getDashboardBean();
		} 
		
		if( dashboardBean != null ) {
			LogAction.addLog(loggedInInfo, "DashboardManager.getDashboard", null, null, null,"Returning Dashboard results for Dashboard ID " + dashboardId );
		} else {
			LogAction.addLog(loggedInInfo, "DashboardManager.getDashboard", null, null, null,"Failed to return results for Dashboard ID " + dashboardId );
		}

		return dashboardBean;
	}
	
	/**
	 * Get an Indicator Template by Id.
	 */
	public IndicatorTemplate getIndicatorTemplate( LoggedInInfo loggedInInfo, int indicatorTemplateId ) {
		
		IndicatorTemplate indicatorTemplate = null; 
		
		if( ! securityInfoManager.hasPrivilege(loggedInInfo, "_dashboardDrilldown", SecurityInfoManager.READ, null ) ) {	
			LogAction.addLog(loggedInInfo, "DashboardManager.getIndicatorTemplate", null, null, null,"User missing _dashboardDrilldown role with read access");
			return indicatorTemplate;
        }
		
		indicatorTemplate = indicatorTemplateDao.find( indicatorTemplateId );
		
		if( indicatorTemplate != null ) {
			LogAction.addLog(loggedInInfo, "DashboardManager.getIndicatorTemplate", null, null, null,"Returning Indicator Template Id " + indicatorTemplateId );			
		} else {
			LogAction.addLog(loggedInInfo, "DashboardManager.getIndicatorTemplate", null, null, null,"Indicator Template Id " + indicatorTemplateId + " not found." );			
		}
		
		return indicatorTemplate;
	}
	
	/**
	 * Get the XML template that contains all the data and meta data for an Indicator display. 
	 */
	public IndicatorTemplateXML getIndicatorTemplateXML( LoggedInInfo loggedInInfo, int indicatorTemplateId ) {
		
		IndicatorTemplateXML indicatorTemplateXML = null;
		
		if( ! securityInfoManager.hasPrivilege(loggedInInfo, "_dashboardDrilldown", SecurityInfoManager.READ, null ) ) {	
			LogAction.addLog(loggedInInfo, "DashboardManager.getIndicatorTemplateXML", null, null, null,"User missing _dashboardDrilldown role with read access");
			return indicatorTemplateXML;
        }
		
		IndicatorTemplate indicatorTemplate = getIndicatorTemplate( loggedInInfo, indicatorTemplateId );
		IndicatorTemplateHandler templateHandler = new IndicatorTemplateHandler( loggedInInfo, indicatorTemplate.getTemplate().getBytes() );
		indicatorTemplateXML = templateHandler.getIndicatorTemplateXML();
		
		if( indicatorTemplateXML != null ) {
			LogAction.addLog(loggedInInfo, "DashboardManager.getIndicatorTemplateXML", null, null, null,"Returning IndicatorTemplateXML Id " + indicatorTemplateId );			
		} else {
			LogAction.addLog(loggedInInfo, "DashboardManager.getIndicatorTemplateXML", null, null, null,"IndicatorTemplateXML Id " + indicatorTemplateId + " not found." );			
		}
		
		return indicatorTemplateXML;
	}

	public DrilldownBean getDrilldownData( LoggedInInfo loggedInInfo, int indicatorTemplateId ) {
		return getDrilldownData(loggedInInfo,indicatorTemplateId,null);
	}
	
	/**
	 * Create a DrilldownBean that contains the query results requested from a specific Indicator by ID.
	 */
	public DrilldownBean getDrilldownData( LoggedInInfo loggedInInfo, int indicatorTemplateId, String providerNo ) {

		DrilldownBean drilldownBean = null; 
		DrilldownBeanFactory drilldownBeanFactory = null;
		
		if( ! securityInfoManager.hasPrivilege(loggedInInfo, "_dashboardDrilldown", SecurityInfoManager.READ, null ) ) {	
			LogAction.addLog(loggedInInfo, "DashboardManager.getDrilldownData", null, null, null,"User missing _dashboardDrilldown role with read access");
			return drilldownBean;
        }
		
		IndicatorTemplate indicatorTemplate = getIndicatorTemplate( loggedInInfo, indicatorTemplateId );
		IndicatorTemplateHandler templateHandler = new IndicatorTemplateHandler( loggedInInfo, indicatorTemplate.getTemplate().getBytes() );
		IndicatorTemplateXML indicatorTemplateXML = templateHandler.getIndicatorTemplateXML();
		
		if( indicatorTemplate != null ) {
			drilldownBeanFactory = new DrilldownBeanFactory( loggedInInfo, indicatorTemplate, providerNo ); 
		}
		
		if( drilldownBeanFactory != null ) {
			drilldownBean = drilldownBeanFactory.getDrilldownBean();
		}
		
		if( drilldownBean != null ) {
			LogAction.addLog(loggedInInfo, "DashboardManager.getDrilldownData", null, null, null,"Returning Drill Down data for Indicator ID " + indicatorTemplateId );	
		} else {
			LogAction.addLog(loggedInInfo, "DashboardManager.getDrilldownData", null, null, null,"Indicator Drill Down data for Indicator ID " + indicatorTemplateId + " not found." );			
		}
		
		return drilldownBean;

	}
	
	public String exportDrilldownQueryResultsToCSV( LoggedInInfo loggedInInfo, int indicatorId ) {
		
		if( ! securityInfoManager.hasPrivilege(loggedInInfo, "_dashboardDrilldown", SecurityInfoManager.READ, null ) ) {	
			LogAction.addLog(loggedInInfo, "DashboardManager.exportDrilldownQueryResultsToCSV", null, null, null,"User missing _dashboardDrilldown role with read access");
			return null;
        }
		
		IndicatorTemplateXML templateXML = getIndicatorTemplateXML( loggedInInfo, indicatorId );

		ExportQueryHandler exportQueryHandler = SpringUtils.getBean( ExportQueryHandler.class );
		exportQueryHandler.setLoggedInInfo( loggedInInfo );
		exportQueryHandler.setParameters( templateXML.getDrilldownParameters() );
		exportQueryHandler.setColumns( templateXML.getDrilldownExportColumns() );
		exportQueryHandler.setRanges( templateXML.getDrilldownRanges() );
		exportQueryHandler.setQuery( templateXML.getDrilldownQuery() );
		exportQueryHandler.execute();
		
		return exportQueryHandler.getCsvFile();

	}
	
	/**
	 * Get an Indicator Panel Bean with a fully executed query. 
	 */
	public IndicatorBean getIndicatorPanel( LoggedInInfo loggedInInfo, int indicatorId ) {
		
		IndicatorBean indicatorBean = null;
		IndicatorBeanFactory indicatorBeanFactory = null;
		
		if( ! securityInfoManager.hasPrivilege(loggedInInfo, "_dashboardDrilldown", SecurityInfoManager.READ, null ) ) {	
			LogAction.addLog(loggedInInfo, "DashboardManager.getIndicatorPanel", null, null, null,"User missing _dashboardDrilldown role with read access");
			return indicatorBean;
        }
		
		IndicatorTemplateXML indicatorTemplateXML = getIndicatorTemplateXML( loggedInInfo, indicatorId );
		
		// The id needs to be force set.
		if( indicatorTemplateXML != null ) {
			indicatorTemplateXML.setId( indicatorId );
			indicatorBeanFactory = new IndicatorBeanFactory( indicatorTemplateXML );
		}

		if( indicatorBeanFactory != null ) {
			indicatorBean = indicatorBeanFactory.getIndicatorBean();
		}
		
		if( indicatorBean != null ) {
			LogAction.addLog(loggedInInfo, "DashboardManager.getIndicatorPanel", null, null, null,"Returning IndicatorBean ID " + indicatorId );	
		} else {
			LogAction.addLog(loggedInInfo, "DashboardManager.getIndicatorPanel", null, null, null,"Failed to return IndicatorBean ID " + indicatorId );			
		}
		
		return indicatorBean;
	}
	
	/**
	 * Get an Indicator Panel Bean with a fully executed query. 
	 */
	public IndicatorBean getIndicatorPanelForProvider( LoggedInInfo loggedInInfo, String providerNo, int indicatorId ) {
		
		IndicatorBean indicatorBean = null;
		IndicatorBeanFactory indicatorBeanFactory = null;
		
		if( ! securityInfoManager.hasPrivilege(loggedInInfo, "_dashboardDrilldown", SecurityInfoManager.READ, null ) ) {	
			LogAction.addLog(loggedInInfo, "DashboardManager.getIndicatorPanelForProvider", null, null, null,"User missing _dashboardDrilldown role with read access");
			return indicatorBean;
        }
		
		IndicatorTemplateXML indicatorTemplateXML = getIndicatorTemplateXML( loggedInInfo, indicatorId );
		
		indicatorTemplateXML.setProviderNo(providerNo);
		
		// The id needs to be force set.
		if( indicatorTemplateXML != null ) {
			indicatorTemplateXML.setId( indicatorId );
			indicatorBeanFactory = new IndicatorBeanFactory( indicatorTemplateXML );
		}

		if( indicatorBeanFactory != null ) {
			indicatorBean = indicatorBeanFactory.getIndicatorBean();
		}
		
		if( indicatorBean != null ) {
			LogAction.addLog(loggedInInfo, "DashboardManager.getIndicatorPanelForProvider", null, null, null,"Returning IndicatorBean ID " + indicatorId );	
		} else {
			LogAction.addLog(loggedInInfo, "DashboardManager.getIndicatorPanelForProvider", null, null, null,"Failed to return IndicatorBean ID " + indicatorId );			
		}
		
		return indicatorBean;
	}
	
	
	// TODO add additional error check / filter class to carry out the following methods.
	
	// TODO add check queries method.
	
	// TODO add duplicate Indicator Template upload check.
	
	// TODO add duplicate Dashboard name check.
	
	
	public String getSharedOutcomesDashboardLaunchURL(LoggedInInfo loggedInInfo) {
		
		String url = OscarProperties.getInstance().getProperty("shared_outcomes_dashboard_url");
		if(url == null) {
			return null;
		}
		
		org.oscarehr.common.model.Clinic oClinic = clinicDAO.getClinic();
    	Clinic clinic = new Clinic();
    	clinic.setApplication("oscar");
    	
    	String clinicIdentifier = OscarProperties.getInstance().getProperty("shared_outcomes_dashboard_clinic_id");
		
		if(clinicIdentifier == null || clinicIdentifier.length() == 0 || clinicIdentifier.length() > 42 ) {
			clinicIdentifier = oClinic.getClinicName();
		}
		
		clinic.setIdentifier(clinicIdentifier);
		
    	clinic.setName(oClinic.getClinicName());
    	
    	
    	Provider provider = loggedInInfo.getLoggedInProvider();
    	
    	User user = new User();
    	user.setCity("");
    	user.setFirstName(provider.getFirstName());
    	//TODO: not sure yet how we want to implement this
    	//user.setAuthorizedUsernameList(null);
    	user.setLastName(provider.getLastName());
    	user.setPostalCode("");
    	user.setProvince(Province.ON);
    	//user.setUniqueIdentifier(uniqueIdentifier);
    	user.setUsername(provider.getProviderNo());
    	user.setRole("provider");
    	
    	
		JSONObject response = new JSONObject();
		response.put("clinic", clinic);
		response.put("user", user);
		
		String json = response.toString();
		
		logger.debug(json);
		
		String encrypted = null;
		String b64 = null;
		
		//system must have the UnlimitedJCEPolicyJDK7.zip installed for this to work
		try {
			Security.addProvider(new BouncyCastleProvider());
			
			StandardPBEStringEncryptor encrypter = new StandardPBEStringEncryptor();
			encrypter.setAlgorithm("PBEWITHSHA256AND256BITAES-CBC-BC");
			encrypter.setProviderName("BC");
			encrypter.setPassword(OscarProperties.getInstance().getProperty("shared_outcomes_dashboard_key"));			
			encrypted = encrypter.encrypt(json);
			b64 = Base64.toBase64String(encrypted.getBytes());
	       } catch(Exception e) {
	    	  logger.error("error",e);
	       }

		return url + "?" + "encodedParams=" + b64 + "&version=1.1";
	}
}
