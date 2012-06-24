/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.PMmodule.web.admin;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.caisi.model.Role;
import org.oscarehr.PMmodule.dao.ProgramProviderDAO;
import org.oscarehr.PMmodule.dao.RoleDAO;
import org.oscarehr.PMmodule.model.SecUserRole;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.PMmodule.service.ProviderManager;
import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;

import com.quatro.model.security.Secrole;
import com.quatro.service.security.RolesManager;


public class MigrateCaisiRolesAction extends BaseAdminAction {

	private static final Logger logger = MiscUtils.getLogger();

	//private CaisiRoleManager caisiRoleMgr;
	private RolesManager oscarRolesMgr;
	private ProgramProviderDAO ppDao;
	private ProgramManager programManager;
	private ProviderManager providerManager;
	private RoleDAO roleDAO;
	
	public void setRoleDAO(RoleDAO dao) {
		this.roleDAO = dao;
	}
	
	public void setProviderManager(ProviderManager mgr) {
		this.providerManager = mgr;
	}
	
	public void setProgramManager(ProgramManager mgr) {
		this.programManager = mgr;
	}
	/*
	public void setCaisiRoleManager(CaisiRoleManager mgr) {
		this.caisiRoleMgr = mgr;
	}
	*/
	public void setRolesManager(RolesManager mgr) {
		this.oscarRolesMgr = mgr;
	}
	
	public void setProgramProviderDAO(ProgramProviderDAO dao) {
        this.ppDao = dao;
    }
	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return null;
	}
	
	public ActionForward preview(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Running preview");		
		List<Mapping> mappings = new ArrayList<Mapping>();
		Result result = new Result();
		result.setStatus("error");
		boolean canContinue=true;
		
		List<Role> caisiRoles = roleDAO.getRoles();
		List<Secrole> oscarRoles = oscarRolesMgr.getRoles();
		Map<Long, Mapping> mappingMap = new HashMap<Long,Mapping>();
		
		//we need to make sure there's a mapping based on names;
		for(Role caisiRole: caisiRoles) {
			Mapping m = new Mapping();
			m.setCaisiRole(caisiRole);
			for(Secrole oscarRole:oscarRoles) {
				if(caisiRole.getName().equalsIgnoreCase(oscarRole.getName())) {
					m.setOscarRole(oscarRole);
				}
			}			
			if(m.getOscarRole()==null) {
				result.getMessages().add("Unable to map CAISI role \"" + caisiRole.getName() + "\". No corresponding OSCAR role. Will create");
			}
			mappings.add(m);
			mappingMap.put(m.getCaisiRole().getId(), m);
		}
		result.setMappings(mappings);
		
		/*
		 * we need to manually load the program providers.
		 * If we use hibernate, then we'd get the messed up associations,
		 * and probably exceptions
		 * 
		 * ok, for this section we need to
		 * 
		 * 1) make sure the CAISI role ID has a mapping (not a deleted role , or something 
		 * weird like that.
		 * 
		 * 2) Make sure the program exists.
		 * 
		 * 3) Make sure the provider exists.
		 * 
		 * 4) Make sure the provider has that role in OSCAR. It would be weird to be a nurse
		 * in program A, but not a nurse in OSCAR.
		 * 
		 */
		try {
			ResultSet rs = DBHandler.GetSQL("SELECT * FROM program_provider");
			while(rs.next()) {
				long programId = rs.getInt("program_id");
				String providerNo = rs.getString("provider_no");
				long caisiRoleId = rs.getInt("role_id");
				
				//1
				if(mappingMap.get(caisiRoleId)==null || mappingMap.get(caisiRoleId).getOscarRole()==null) {
					logger.info("no mapping found: " + caisiRoleId);
					String providerName = providerManager.getProvider(providerNo).getFormattedName();
					String programName = programManager.getProgramName(String.valueOf(programId));
					
					Role caisiRole = roleDAO.getRole(caisiRoleId);
					if(caisiRole != null) {
						String caisiRoleName = caisiRole.getName();
						result.getMessages().add("Found a staff assignment whose CAISI Role has no mapping. Will remove. Manual re-assignment will be necessary. (" + providerName + " assigned as " + caisiRoleName + " in program" + programName + ")");
					} else {
						result.getMessages().add("Found a staff assignment whose CAISI Role does not exist. Will remove. Manual re-assignment will be necessary. (" + providerName + " in program" + programName + ")");
					}
					
				}
				//2
				if(programManager.getProgram(programId)==null) {
					logger.info("program does not exist: " + programId);
					result.getMessages().add("Found a staff assignment to non-existant program (program id is "+programId+"). Will remove.");
				}
				//3
				if(providerManager.getProvider(providerNo)==null) {
					logger.info("provider does not exist: " +  providerNo);
					result.getMessages().add("Found a staff assignment for non-existant staff member (provider no is " + providerNo + "). Will remove.");
				}
				//4
				Mapping mappedRole = mappingMap.get(caisiRoleId);
				String mappedRoleName = null;
				if(mappedRole != null && mappedRole.getOscarRole() != null) {
					boolean ok=false;
					mappedRoleName=mappedRole.getOscarRole().getName();
					List<SecUserRole> roles = providerManager.getSecUserRoles(providerNo);
					for(SecUserRole role:roles) {
						if(mappedRoleName.equals(role.getRoleName())) {
							ok=true;
						}
					}
					if(!ok) {
						Role caisiRole = roleDAO.getRole(caisiRoleId);
						String caisiRoleName="";
						if(caisiRole != null) {
							caisiRoleName = caisiRole.getName();
						}
						String providerName = providerManager.getProvider(providerNo).getFormattedName();						
						logger.info("Provider does not have this role in OSCAR");
						result.getMessages().add("Found a staff assignment where the provider does not have the nessary role. Will assign provider to necessary role. (" + providerName + " - " + caisiRoleName +")");
					}
				} 
				
			}
			rs.close();
			
			//default_role_access
			rs = DBHandler.GetSQL("SELECT * FROM default_role_access a,access_type b where a.access_id = b.access_id");
			while(rs.next()) {
				
				int role_id = rs.getInt("role_id");
				String access_name = rs.getString("name");
				
				Mapping mappedRole = mappingMap.get((long)role_id);
				if(mappedRole == null) {
					result.getMessages().add("global role access: no mapping found for access type:" + access_name);
				}
			}
			rs.close();
			
			//program_access_roles
			rs = DBHandler.GetSQL("SELECT * FROM program_access_roles");
			while(rs.next()) {
				
				int role_id = rs.getInt("role_id");
				
				Mapping mappedRole = mappingMap.get((long)role_id);
				if(mappedRole == null) {
					result.getMessages().add("program access roles: no mapping found");
				}
			}
			rs.close();
			
			//casemgmt_note
			rs = DBHandler.GetSQL("SELECT * FROM casemgmt_note");
			while(rs.next()) {
				
				int role_id = rs.getInt("reporter_caisi_role");
				Mapping mappedRole = mappingMap.get((long)role_id);
				if(mappedRole == null) {
					result.getMessages().add("case management note: no mapping found");
				}
			}
			rs.close();
			
		}catch(SQLException e) {
			logger.error("Error", e);
		}
		
		if(canContinue) {
			result.setStatus("ok");
		}	
		
		try {
			response.getWriter().print(JSONObject.fromObject(result));
		}catch(IOException e) {
			logger.error("Error", e);
		}
		
		return null;
	}
	


	public ActionForward migrate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		logger.info("Running migration");
		
		List<Mapping> mappings = new ArrayList<Mapping>();
		Result result = new Result();
		result.setStatus("error");
		
		List<Role> caisiRoles = roleDAO.getRoles();
		List<Secrole> oscarRoles = oscarRolesMgr.getRoles();
		Map<Long, Mapping> mappingMap = new HashMap<Long,Mapping>();
		
		//we need to make sure there's a mapping based on names;
		for(Role caisiRole: caisiRoles) {
			Mapping m = new Mapping();
			m.setCaisiRole(caisiRole);
			for(Secrole oscarRole:oscarRoles) {
				if(caisiRole.getName().equalsIgnoreCase(oscarRole.getName())) {
					m.setOscarRole(oscarRole);
				}
			}			
			if(m.getOscarRole()==null) {
				//create the oscar role
				Secrole newRole = new Secrole();
				newRole.setActive(true);
				newRole.setDescription(m.getCaisiRole().getName());
				newRole.setName(m.getCaisiRole().getName());
				this.oscarRolesMgr.save(newRole);
				result.getMessages().add("Created OSCAR Role: " + newRole.getName());
				m.setOscarRole(newRole);
			}
			mappings.add(m);
			mappingMap.put(m.getCaisiRole().getId(), m);
		}
		result.setMappings(mappings);
		
		try {
			ResultSet rs = DBHandler.GetSQL("SELECT * FROM program_provider");
			while(rs.next()) {
				long programId = rs.getInt("program_id");
				String providerNo = rs.getString("provider_no");
				long caisiRoleId = rs.getInt("role_id");
				
				//1
				if(mappingMap.get(caisiRoleId)==null || mappingMap.get(caisiRoleId).getOscarRole()==null) {
					//delete
					ppDao.deleteProgramProvider(rs.getLong("id"));					
				} else {
					//change
					DBHandler.RunSQL("update program_provider set role_id=" + mappingMap.get(caisiRoleId).getOscarRole().getId() + " where id=" + rs.getLong("id"));					
				}
				
				//2
				if(programManager.getProgram(programId)==null) {
					logger.info("program does not exist: " + programId);
					ppDao.deleteProgramProvider(rs.getLong("id"));
				}
				//3
				if(providerManager.getProvider(providerNo)==null) {
					logger.info("provider does not exist: " +  providerNo);
					ppDao.deleteProgramProvider(rs.getLong("id"));
				}	
				//4
				Mapping mappedRole = mappingMap.get(caisiRoleId);
				String mappedRoleName = null;
				if(mappedRole != null && mappedRole.getOscarRole() != null) {
					boolean ok=false;
					mappedRoleName=mappedRole.getOscarRole().getName();
					List<SecUserRole> roles = providerManager.getSecUserRoles(providerNo);
					for(SecUserRole role:roles) {						
						if(mappedRoleName.equals(role.getRoleName())) {
							ok=true;
						}
					}
					if(!ok) {
						//assign provider role of mappedrole.getoscarrole()
						SecUserRole sur = new SecUserRole();
						sur.setProviderNo(providerNo);
						sur.setRoleName(mappedRoleName);
						providerManager.saveUserRole(sur);
						String providerName = providerManager.getProvider(providerNo).getFormattedName();												
						result.getMessages().add("Assigned provider " + providerName + ":" + mappedRoleName);
						logger.info("Assigned provider " + providerName + ":" + mappedRoleName);
					}
				} 								
			}
			//default_role_access
			rs = DBHandler.GetSQL("SELECT * FROM default_role_access");
			while(rs.next()) {
				long id = rs.getLong("id");
				int role_id = rs.getInt("role_id");
				
				Mapping mappedRole = mappingMap.get((long)role_id);
				if(mappedRole == null) {
					DBHandler.RunSQL("delete from default_role_access where id = " + id);
					logger.info("delete from default_role_access where id = " + id);
				} else {
					DBHandler.RunSQL("update default_role_access set role_id=" + mappedRole.getOscarRole().getId() + " where id=" + id);
					logger.info("update default_role_access set role_id=" + mappedRole.getOscarRole().getId() + " where id=" + id);
				}
			}		
			rs.close();
			
			rs = DBHandler.GetSQL("SELECT * FROM program_access_roles");
			List<String> valuesToAdd = new ArrayList<String>();
			while(rs.next()) {
				long id = rs.getLong("id");
				int role_id = rs.getInt("role_id");
				
				Mapping mappedRole = mappingMap.get((long)role_id);
				if(mappedRole == null) {
					//DBHandler.RunSQL("delete from program_access_roles where id="+id + " and role_id=" + role_id);
				} else {
					//DBHandler.RunSQL("insert into program_access_roles  values (" + id + ","+mappedRole.getOscarRole().getId() + ")");
					valuesToAdd.add(id + "," + mappedRole.getOscarRole().getId());
				}
			}
			DBHandler.RunSQL("delete from program_access_roles");
			for(String vta:valuesToAdd) {
				String[] p = vta.split(",");
				DBHandler.RunSQL("insert into program_access_roles  values (" +p[0] + "," + p[1] + ")");
				logger.info("insert into program_access_roles  values (" +p[0] + "," + p[1] + ")");
			}
			
			rs.close();

			rs = DBHandler.GetSQL("SELECT * FROM casemgmt_note");
			while(rs.next()) {
				long id = rs.getLong("note_id");
				int role_id = rs.getInt("reporter_caisi_role");
				Mapping mappedRole = mappingMap.get((long)role_id);
				if(mappedRole == null) {
					//we don't remove notes.
					
				} else {
					DBHandler.RunSQL("update casemgmt_note set reporter_caisi_role="+mappedRole.getOscarRole().getId() + " where note_id="+id);
					logger.info("update casemgmt_note set reporter_caisi_role="+mappedRole.getOscarRole().getId() + " where note_id="+id);
				}
			}
			rs.close();			
			logger.info("Migrated caisi roles to oscar roles successfully!");
			
		} catch(SQLException e) {
			MiscUtils.getLogger().error("Error", e);
			logger.error("Error", e);
			result.setStatus("error");
		}
		
		try {
			response.getWriter().print(JSONObject.fromObject(result));
		}catch(IOException e) {
			logger.error("Error", e);
		}
		return null;
	}
	

	public class Result {
		
		private List<Mapping> mappings;
		private String status;
		private List<String> messages;
		
		public Result() {
			messages = new ArrayList<String>();
		}
		public List<Mapping> getMappings() {
			return mappings;
		}
		public void setMappings(List<Mapping> mappings) {
			this.mappings = mappings;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public List<String> getMessages() {
			return messages;
		}
		public void setMessages(List<String> messages) {
			this.messages = messages;
		}		
		
	}
	public class Mapping {
		private Role caisiRole;
		private Secrole oscarRole;
		
		public Mapping(){}
		
		public Mapping(Role caisiRole, Secrole oscarRole) {
			this.caisiRole = caisiRole;
			this.oscarRole = oscarRole;
		}
		
		public boolean hasMatch() {
			return (caisiRole!=null && oscarRole != null);
		}
		public Role getCaisiRole() {
			return caisiRole;
		}
		public void setCaisiRole(Role caisiRole) {
			this.caisiRole = caisiRole;
		}
		public Secrole getOscarRole() {
			return oscarRole;
		}
		public void setOscarRole(Secrole oscarRole) {
			this.oscarRole = oscarRole;
		}
		
		
	}
}
