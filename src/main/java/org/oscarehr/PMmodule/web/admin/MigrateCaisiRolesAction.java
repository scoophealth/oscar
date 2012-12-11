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
import org.oscarehr.PMmodule.dao.DefaultRoleAccessDAO;
import org.oscarehr.PMmodule.dao.ProgramProviderDAO;
import org.oscarehr.PMmodule.dao.RoleDAO;
import org.oscarehr.PMmodule.model.AccessType;
import org.oscarehr.PMmodule.model.DefaultRoleAccess;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.PMmodule.model.SecUserRole;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.PMmodule.service.ProviderManager;
import org.oscarehr.casemgmt.dao.CaseManagementNoteDAO;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.common.dao.ProgramAccessRolesDao;
import org.oscarehr.common.model.ProgramAccessRoles;
import org.oscarehr.common.model.ProgramAccessRolesId;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.util.ConversionUtils;

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
		boolean canContinue = true;

		List<Role> caisiRoles = roleDAO.getRoles();
		List<Secrole> oscarRoles = oscarRolesMgr.getRoles();
		Map<Long, Mapping> mappingMap = new HashMap<Long, Mapping>();

		//we need to make sure there's a mapping based on names;
		for (Role caisiRole : caisiRoles) {
			Mapping m = new Mapping();
			m.setCaisiRole(caisiRole);
			for (Secrole oscarRole : oscarRoles) {
				if (caisiRole.getName().equalsIgnoreCase(oscarRole.getName())) {
					m.setOscarRole(oscarRole);
				}
			}
			if (m.getOscarRole() == null) {
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
		ProgramProviderDAO dao = SpringUtils.getBean(ProgramProviderDAO.class);

		for (ProgramProvider p : dao.getAllProgramProviders()) {
			long programId = p.getProgramId();
			String providerNo = p.getProviderNo();
			long caisiRoleId = p.getRoleId();

			//1
			if (mappingMap.get(caisiRoleId) == null || mappingMap.get(caisiRoleId).getOscarRole() == null) {
				logger.info("no mapping found: " + caisiRoleId);
				String providerName = providerManager.getProvider(providerNo).getFormattedName();
				String programName = programManager.getProgramName(String.valueOf(programId));

				Role caisiRole = roleDAO.getRole(caisiRoleId);
				if (caisiRole != null) {
					String caisiRoleName = caisiRole.getName();
					result.getMessages().add("Found a staff assignment whose CAISI Role has no mapping. Will remove. Manual re-assignment will be necessary. (" + providerName + " assigned as " + caisiRoleName + " in program" + programName + ")");
				} else {
					result.getMessages().add("Found a staff assignment whose CAISI Role does not exist. Will remove. Manual re-assignment will be necessary. (" + providerName + " in program" + programName + ")");
				}

			}
			//2
			if (programManager.getProgram(programId) == null) {
				logger.info("program does not exist: " + programId);
				result.getMessages().add("Found a staff assignment to non-existant program (program id is " + programId + "). Will remove.");
			}
			//3
			if (providerManager.getProvider(providerNo) == null) {
				logger.info("provider does not exist: " + providerNo);
				result.getMessages().add("Found a staff assignment for non-existant staff member (provider no is " + providerNo + "). Will remove.");
			}
			//4
			Mapping mappedRole = mappingMap.get(caisiRoleId);
			String mappedRoleName = null;
			if (mappedRole != null && mappedRole.getOscarRole() != null) {
				boolean ok = false;
				mappedRoleName = mappedRole.getOscarRole().getName();
				List<SecUserRole> roles = providerManager.getSecUserRoles(providerNo);
				for (SecUserRole role : roles) {
					if (mappedRoleName.equals(role.getRoleName())) {
						ok = true;
					}
				}
				if (!ok) {
					Role caisiRole = roleDAO.getRole(caisiRoleId);
					String caisiRoleName = "";
					if (caisiRole != null) {
						caisiRoleName = caisiRole.getName();
					}
					String providerName = providerManager.getProvider(providerNo).getFormattedName();
					logger.info("Provider does not have this role in OSCAR");
					result.getMessages().add("Found a staff assignment where the provider does not have the nessary role. Will assign provider to necessary role. (" + providerName + " - " + caisiRoleName + ")");
				}
			}

		}

		//default_role_access
		DefaultRoleAccessDAO rDao = SpringUtils.getBean(DefaultRoleAccessDAO.class);
		for (Object[] ra : rDao.findAllRolesAndAccessTypes()) {
			DefaultRoleAccess a = (DefaultRoleAccess) ra[0];
			AccessType b = (AccessType) ra[1];

			int role_id = (int) a.getRoleId();
			String access_name = b.getName();

			Mapping mappedRole = mappingMap.get((long) role_id);
			if (mappedRole == null) {
				result.getMessages().add("global role access: no mapping found for access type:" + access_name);
			}
		}

		//program_access_roles
		ProgramAccessRolesDao parDao = SpringUtils.getBean(ProgramAccessRolesDao.class);
		for (ProgramAccessRoles ar : parDao.findAll()) {

			int role_id = (int) ar.getId().getRoleId();
			Mapping mappedRole = mappingMap.get((long) role_id);
			if (mappedRole == null) {
				result.getMessages().add("program access roles: no mapping found");
			}
		}

		//casemgmt_note
		CaseManagementNoteDAO cmnDao = SpringUtils.getBean(CaseManagementNoteDAO.class);
		for (CaseManagementNote cmn : cmnDao.findAll()) {
			int role_id = ConversionUtils.fromIntString(cmn.getReporter_caisi_role());
			Mapping mappedRole = mappingMap.get((long) role_id);
			if (mappedRole == null) {
				result.getMessages().add("case management note: no mapping found");
			}
		}

		if (canContinue) {
			result.setStatus("ok");
		}

		try {
			response.getWriter().print(JSONObject.fromObject(result));
		} catch (IOException e) {
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
		Map<Long, Mapping> mappingMap = new HashMap<Long, Mapping>();

		//we need to make sure there's a mapping based on names;
		for (Role caisiRole : caisiRoles) {
			Mapping m = new Mapping();
			m.setCaisiRole(caisiRole);
			for (Secrole oscarRole : oscarRoles) {
				if (caisiRole.getName().equalsIgnoreCase(oscarRole.getName())) {
					m.setOscarRole(oscarRole);
				}
			}
			if (m.getOscarRole() == null) {
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

		ProgramProviderDAO dao = SpringUtils.getBean(ProgramProviderDAO.class);
		for (ProgramProvider p : dao.getAllProgramProviders()) {
			long programId = p.getProgramId();
			String providerNo = p.getProviderNo();
			long caisiRoleId = p.getRoleId();

			//1
			if (mappingMap.get(caisiRoleId) == null || mappingMap.get(caisiRoleId).getOscarRole() == null) {
				//delete
				ppDao.deleteProgramProvider(p.getProgramId());
			} else {
				//change
				dao.updateProviderRoles(p.getId(), mappingMap.get(caisiRoleId).getOscarRole().getId());
			}

			//2
			if (programManager.getProgram(programId) == null) {
				logger.info("program does not exist: " + programId);
				ppDao.deleteProgramProvider(p.getId());
			}
			//3
			if (providerManager.getProvider(providerNo) == null) {
				logger.info("provider does not exist: " + providerNo);
				ppDao.deleteProgramProvider(p.getId());
			}
			//4
			Mapping mappedRole = mappingMap.get(caisiRoleId);
			String mappedRoleName = null;
			if (mappedRole != null && mappedRole.getOscarRole() != null) {
				boolean ok = false;
				mappedRoleName = mappedRole.getOscarRole().getName();
				List<SecUserRole> roles = providerManager.getSecUserRoles(providerNo);
				for (SecUserRole role : roles) {
					if (mappedRoleName.equals(role.getRoleName())) {
						ok = true;
					}
				}
				if (!ok) {
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
		DefaultRoleAccessDAO rDao = SpringUtils.getBean(DefaultRoleAccessDAO.class);
		for (DefaultRoleAccess a : rDao.findAll()) {
			long id = a.getId();
			int role_id = (int) a.getRoleId();

			Mapping mappedRole = mappingMap.get((long) role_id);
			if (mappedRole == null) {
				rDao.deleteDefaultRoleAccess(id);
				logger.info("delete from default_role_access where id = " + id);
			} else {
				a.setRoleId(mappedRole.getOscarRole().getId());
				rDao.saveDefaultRoleAccess(a);
			}
		}

		List<String> valuesToAdd = new ArrayList<String>();
		ProgramAccessRolesDao parDao = SpringUtils.getBean(ProgramAccessRolesDao.class);
		for (ProgramAccessRoles ar : parDao.findAll()) {

			long id = ar.getId().getId();
			int role_id = (int) ar.getId().getRoleId();

			Mapping mappedRole = mappingMap.get((long) role_id);
			if (mappedRole == null) {
				//do nothing
			} else {
				valuesToAdd.add(id + "," + mappedRole.getOscarRole().getId());
			}
		}

		parDao.removeAll();
		for (String vta : valuesToAdd) {
			String[] p = vta.split(",");
			ProgramAccessRoles pra = new ProgramAccessRoles(new ProgramAccessRolesId(ConversionUtils.fromLongString(p[0]), ConversionUtils.fromLongString(p[1])));
			logger.info("insert into program_access_roles  values (" + p[0] + "," + p[1] + ")");
		}

		CaseManagementNoteDAO cmnDao = SpringUtils.getBean(CaseManagementNoteDAO.class);
		for (CaseManagementNote cmn : cmnDao.findAll()) {
			long id = cmn.getId();
			int role_id = ConversionUtils.fromIntString(cmn.getReporter_caisi_role());

			Mapping mappedRole = mappingMap.get((long) role_id);
			if (mappedRole == null) {
				//we don't remove notes.
			} else {
				cmn.setReporter_caisi_role("" + mappedRole.getOscarRole().getId());
				cmnDao.saveAndReturn(cmn);
				logger.info("update casemgmt_note set reporter_caisi_role=" + mappedRole.getOscarRole().getId() + " where note_id=" + id);
			}
		}
		logger.info("Migrated caisi roles to oscar roles successfully!");

		try {
			response.getWriter().print(JSONObject.fromObject(result));
		} catch (IOException e) {
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

		public Mapping() {
		}

		public Mapping(Role caisiRole, Secrole oscarRole) {
			this.caisiRole = caisiRole;
			this.oscarRole = oscarRole;
		}

		public boolean hasMatch() {
			return (caisiRole != null && oscarRole != null);
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
