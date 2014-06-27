/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

package org.oscarehr.casemgmt.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsDateJsonBeanProcessor;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.tools.ant.util.DateUtils;
import org.oscarehr.common.dao.AdmissionDao;
import org.oscarehr.PMmodule.dao.ProgramAccessDAO;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.dao.ProgramProviderDAO;
import org.oscarehr.common.model.Admission;
import org.oscarehr.PMmodule.model.DefaultRoleAccess;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.ProgramAccess;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.casemgmt.dao.CaseManagementNoteDAO;
import org.oscarehr.casemgmt.dao.RoleProgramAccessDAO;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.common.dao.ProviderDefaultProgramDao;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.ProviderDefaultProgram;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import com.quatro.dao.security.SecroleDao;
import com.quatro.model.security.Secrole;

/**
 * This class describes a servlet that allows one to change and view the permissions and scope of a CaseManagementNote object.
 * @author jennifer@indivica.com
 *
 */
public class NotePermissionsAction extends DispatchAction {


	public ActionForward getNoteScope(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ProgramDao programDao = (ProgramDao) SpringUtils.getBean("programDao");
		ProgramProviderDAO programProviderDao = (ProgramProviderDAO) SpringUtils.getBean("programProviderDAO");
		CaseManagementNoteDAO noteDao = (CaseManagementNoteDAO) SpringUtils.getBean("caseManagementNoteDAO");
		ProgramAccessDAO programAccessDAO = (ProgramAccessDAO) SpringUtils.getBean("programAccessDAO");
		SecroleDao secroleDao = (SecroleDao) SpringUtils.getBean("secroleDao");
		RoleProgramAccessDAO roleProgramAccessDao = (RoleProgramAccessDAO) SpringUtils.getBean("RoleProgramAccessDAO");

		String noteId = request.getParameter("noteId");
		String programNo, role = "";
		// Get the note
		if (noteId != null && noteId.equalsIgnoreCase("0") && request.getParameter("programNo") != null) {
			programNo = request.getParameter("programNo");
		} else {
			CaseManagementNote note = noteDao.getNote(Long.parseLong(noteId));
			programNo = note.getProgram_no();
			role = note.getReporter_caisi_role();
		}
		Program program = programDao.getProgram(Integer.parseInt(programNo));

		List<ProgramAccess> programAccessList = programAccessDAO.getAccessListByProgramId(new Long(programNo));
		Map<String, ProgramAccess> programAccessMap = convertProgramAccessListToMap(programAccessList);



		if (noteId != null && noteId.equalsIgnoreCase("0") && request.getParameter("roleId") != null)
			role = request.getParameter("roleId");

		String roleName = secroleDao.getRole(Integer.parseInt(role)).getName().toLowerCase();

		@SuppressWarnings("unchecked")
		List<ProgramProvider> programProviders = programProviderDao.getProgramProviders(Long.parseLong(programNo));

		List<NotePermission> permissionList = new ArrayList<NotePermission>();

		for (ProgramProvider provider : programProviders) {
			if (programAccessMap.containsKey("read " + roleName + " notes")) {
				ProgramAccess programAccess = programAccessMap.get("read " + roleName + " notes");
				if (programAccess.isAllRoles()) {
					// all roles have access to this permission
					permissionList.add(new NotePermission(provider, NotePermission.AccessType.ALL_ROLES));
					continue;
				} else {
					boolean hasRoleAccess = false;
					for (Secrole programRole : programAccess.getRoles()) {
						if (programRole.getId().longValue() == provider.getRoleId().longValue()) {
							// This provider has access based on their role
							hasRoleAccess = true;
						}
					}

					if (hasRoleAccess) // This role is included in the list of roles granted this permission
						permissionList.add(new NotePermission(provider, NotePermission.AccessType.ROLE));
					else // The provider's role is not included in the list of roles granted this permission
						permissionList.add(new NotePermission(provider, NotePermission.AccessType.NO_ACCESS));
				}
			} else if (roleProgramAccessDao.hasAccess("read " + roleName + " notes", provider.getRoleId())) {
				// Provider's role has access based on global role permissions
				permissionList.add(new NotePermission(provider, NotePermission.AccessType.ROLE_GLOBAL));
			} else {
				// Provider does not have access
				permissionList.add(new NotePermission(provider, NotePermission.AccessType.NO_ACCESS));
			}
		}

		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("programName", program.getName());
		hashMap.put("programNo", program.getId());
		hashMap.put("roleName", roleName);
		hashMap.put("permissionList", permissionList);
		JsonConfig config = new JsonConfig();
		config.registerJsonBeanProcessor(java.sql.Date.class, new JsDateJsonBeanProcessor());

		JSONObject json = JSONObject.fromObject(hashMap, config);
		response.getOutputStream().write(json.toString().getBytes());
		return null;

	}

	public ActionForward setNotePermissions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		CaseManagementNoteDAO noteDao = (CaseManagementNoteDAO) SpringUtils.getBean("caseManagementNoteDAO");
		ProgramDao programDao = (ProgramDao) SpringUtils.getBean("programDao");
		SecroleDao secroleDao = (SecroleDao) SpringUtils.getBean("secroleDao");

		String noteId = request.getParameter("noteId");
		String newProgramNo = request.getParameter("program");
		String newRoleId = request.getParameter("role");

		HashMap<String, Object> hashMap = new HashMap<String, Object>();

		hashMap.put("noteId", noteId);

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();
		Provider provider = loggedInInfo.getLoggedInProvider();

		if (canNoteBeModified(providerNo, noteId)) {
			CaseManagementNote note = noteDao.getNote(Long.parseLong(noteId));


			// Are this note's permissions being changed after all?
			if (note.getReporter_caisi_role() != null && note.getReporter_caisi_role().equalsIgnoreCase(newRoleId)
					&& note.getProgram_no() != null && note.getProgram_no().equalsIgnoreCase(newProgramNo)) {
				hashMap.put("success", "NO_CHANGE");
			} else if (providerHasAccess(providerNo, newProgramNo, newRoleId, note.getDemographic_no())) {
				// Check if this provider has access to the requested program & role pair
				Program p = programDao.getProgram(Integer.parseInt(newProgramNo));
				Secrole r = secroleDao.getRole(Integer.parseInt(newRoleId));

				note.setProgram_no(newProgramNo);
				note.setReporter_caisi_role(newRoleId);
				Date newDate = new Date();
				note.setUpdate_date(newDate);

				String newNote = note.getNote();
				newNote += "\n[" + DateUtils.format(newDate, "yyyy-MM-dd") + " Program/role changed to: " + p.getName() + " (" + r.getName() + ") by " + provider.getLastName() + ", " + provider.getFirstName() + "]";
				note.setNote(newNote);

				Object id = noteDao.saveAndReturn(note);

				hashMap.put("success", "SAVED");
				hashMap.put("newProgramNo", newProgramNo);
				hashMap.put("newRoleId", newRoleId);
				hashMap.put("newProgramName", p.getName());
				hashMap.put("newRoleName", r.getName());
				hashMap.put("noteContent", newNote);
				hashMap.put("newNoteId", id);
			} else {
				// The provider making this modification won't be able to see the note if this change is made
				hashMap.put("error", "MAKES_INVISIBLE");
			}
		} else {
			// The provider doesn't have permission to see this note, so they can't modify it
			hashMap.put("error", "PERMISSION_DENIED");
		}

		JSONObject json = JSONObject.fromObject(hashMap);
		response.getOutputStream().write(json.toString().getBytes());

		return null;
	}

	public ActionForward getDefaultProgramAndRole(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();
		String demoNo = request.getParameter("demoNo");

		ProgramProviderDAO programProviderDao = (ProgramProviderDAO) SpringUtils.getBean("programProviderDAO");
		ProviderDefaultProgramDao defaultProgramDao = (ProviderDefaultProgramDao) SpringUtils.getBean("providerDefaultProgramDao");

		@SuppressWarnings("unchecked")
		List<ProviderDefaultProgram> programs = defaultProgramDao.getProgramByProviderNo(providerNo);

		HashMap<String, Object> hashMap = new HashMap<String, Object>();

		HashMap<Program,List<Secrole>> rolesForDemo = getAllProviderAccessibleRolesForDemo(providerNo, demoNo);
		for (ProviderDefaultProgram pdp : programs) {
			for (Program p : rolesForDemo.keySet()) {
				if (pdp.getProgramId() == p.getId().intValue()) {
					List<ProgramProvider> programProviderList = programProviderDao.getProgramProviderByProviderProgramId(providerNo, (long) pdp.getProgramId());
					hashMap.put("success", true);
					hashMap.put("defaultProgram", pdp.getProgramId());
					hashMap.put("defaultProgramName", p.getName());
					hashMap.put("defaultRole", programProviderList.get(0).getRoleId());
					hashMap.put("defaultRoleName", programProviderList.get(0).getRole().getName());

					JSONObject json = JSONObject.fromObject(hashMap);
					response.getOutputStream().write(json.toString().getBytes());

					return null;
				}
			}
		}

		// The provider default program is not one of the programs that the patient is admitted to,
		// so choose the first one that they're admitted to that the provider can access
		HashMap<Program, List<Secrole>> providerDemoPrograms = getAllProviderAccessibleRolesForDemo(providerNo, demoNo);
		if (!providerDemoPrograms.isEmpty()) {
			Program program = providerDemoPrograms.keySet().iterator().next();
			ProgramProvider programProvider = programProviderDao.getProgramProvider(providerNo, (long) program.getId());

			hashMap.put("success", true);
			hashMap.put("defaultProgram", program.getId());
			hashMap.put("defaultRole", programProvider.getRoleId());
			hashMap.put("defaultProgramName", program.getName());
			hashMap.put("defaultRoleName", programProvider.getRole().getName());

		} else
			hashMap.put("success", false);

		JSONObject json = JSONObject.fromObject(hashMap);
		response.getOutputStream().write(json.toString().getBytes());

		return null;
	}

	public ActionForward visibleProgramsAndRoles(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

		String demoNo = request.getParameter("demoNo");
		HashMap<Program, List<Secrole>> visibleMap = getAllProviderAccessibleRolesForDemo(providerNo, demoNo);

		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		List<HashMap<String, Object>> mapList = new ArrayList<HashMap<String, Object>>();

		for (Program p : visibleMap.keySet()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("programNo", p.getId());
			map.put("programName", p.getName());

			map.put("roleAccess", visibleMap.get(p));

			mapList.add(map);
		}

//		for (HashMap<String, String> p : getDischargedPrograms(demoNo)) {
//			HashMap<String, Object> map = new HashMap<String, Object>();
//			map.put("programNo", p.get("programNo"));
//			map.put("programName", p.get("programName") + " (discharged)");
//
//			map.put("roleAccess", new ArrayList<String>());
//
//			mapList.add(map);
//		}

		hashMap.put("programs", mapList);

		JsonConfig config = new JsonConfig();
		config.registerJsonBeanProcessor(java.sql.Date.class, new JsDateJsonBeanProcessor());

		JSONObject json = JSONObject.fromObject(hashMap, config);
		response.getOutputStream().write(json.toString().getBytes());

		return null;
	}

	private boolean canNoteBeModified(String providerNo, String noteId) {
		ProgramProviderDAO programProviderDao = (ProgramProviderDAO) SpringUtils.getBean("programProviderDAO");
		CaseManagementNoteDAO noteDao = (CaseManagementNoteDAO) SpringUtils.getBean("caseManagementNoteDAO");
		ProgramAccessDAO programAccessDAO = (ProgramAccessDAO) SpringUtils.getBean("programAccessDAO");
		SecroleDao secroleDao = (SecroleDao) SpringUtils.getBean("secroleDao");
		RoleProgramAccessDAO roleProgramAccessDao = (RoleProgramAccessDAO) SpringUtils.getBean("RoleProgramAccessDAO");

		// Get the note
		CaseManagementNote note = noteDao.getNote(Long.parseLong(noteId));
		CaseManagementNote mostRecentNote = noteDao.getMostRecentNote(note.getUuid());
		if (note.getId().longValue() != mostRecentNote.getId().longValue())
			return false;

		String programNo = note.getProgram_no();

		List<ProgramAccess> programAccessList = programAccessDAO.getAccessListByProgramId(new Long(programNo));
		Map<String, ProgramAccess> programAccessMap = convertProgramAccessListToMap(programAccessList);


		String role = note.getReporter_caisi_role();
		String roleName = secroleDao.getRole(Integer.parseInt(role)).getName().toLowerCase();

		List<ProgramProvider> programProviderList = programProviderDao.getProgramProviderByProviderProgramId(providerNo, Long.parseLong(programNo));


		for (ProgramProvider provider : programProviderList) {
			if (programAccessMap.containsKey("read " + roleName + " notes")) {
				ProgramAccess programAccess = programAccessMap.get("read " + roleName + " notes");
				if (programAccess.isAllRoles()) {
					// all roles have access to this permission
					return true;
				} else {
					for (Secrole programRole : programAccess.getRoles()) {
						if (programRole.getId().longValue() == provider.getRoleId().longValue()) {
							// This provider has access based on their role
							return true;
						}
					}
				}
			} else if (roleProgramAccessDao.hasAccess("read " + roleName + " notes", provider.getRoleId())) {
				// Provider's role has access based on global role permissions
				return true;
			}
		}

		return false;
	}

	public boolean providerHasAccess(String providerNo, String programNo, String roleId, String demographicNo) {
		ProgramProviderDAO programProviderDao = (ProgramProviderDAO) SpringUtils.getBean("programProviderDAO");
		ProgramAccessDAO programAccessDAO = (ProgramAccessDAO) SpringUtils.getBean("programAccessDAO");
		SecroleDao secroleDao = (SecroleDao) SpringUtils.getBean("secroleDao");
		RoleProgramAccessDAO roleProgramAccessDao = (RoleProgramAccessDAO) SpringUtils.getBean("RoleProgramAccessDAO");
		AdmissionDao admissionDao = (AdmissionDao) SpringUtils.getBean("admissionDao");

		List<ProgramAccess> programAccessList = programAccessDAO.getAccessListByProgramId(new Long(programNo));
		Map<String, ProgramAccess> programAccessMap = convertProgramAccessListToMap(programAccessList);
		String roleName = secroleDao.getRole(Integer.parseInt(roleId)).getName().toLowerCase();

		List<ProgramProvider> programProviderList = programProviderDao.getProgramProviderByProviderProgramId(providerNo, Long.parseLong(programNo));

		Admission demoAdmission = admissionDao.getAdmission(Integer.parseInt(programNo), Integer.parseInt(demographicNo));

		for (ProgramProvider provider : programProviderList) {
			if (programAccessMap.containsKey("read " + roleName + " notes")) {
				for (Secrole programRole : programAccessMap.get("read " + roleName + " notes").getRoles()) {
					if (programRole.getId().longValue() == provider.getRoleId().longValue()) {
						// This provider has access based on their role
						// Return whether the demo has an active admission to this program
						return (demoAdmission != null);
					}
				}
			} else if (roleProgramAccessDao.hasAccess("read " + roleName + " notes", provider.getRoleId())) {
				// Provider's role has access based on global role permissions
				// Check whether the demo's admission is current to this program
				return (demoAdmission != null);
			}
		}

		return false;

	}

	public static HashMap<Program, List<Secrole>> getAllProviderAccessibleRolesForDemo(String providerNo, String demoNo) {
		ProgramProviderDAO programProviderDao = (ProgramProviderDAO) SpringUtils.getBean("programProviderDAO");
		ProgramAccessDAO programAccessDAO = (ProgramAccessDAO) SpringUtils.getBean("programAccessDAO");
		SecroleDao secroleDao = (SecroleDao) SpringUtils.getBean("secroleDao");
		RoleProgramAccessDAO roleProgramAccessDao = (RoleProgramAccessDAO) SpringUtils.getBean("RoleProgramAccessDAO");
		AdmissionDao admissionDao = (AdmissionDao) SpringUtils.getBean("admissionDao");

		HashMap<Program, List<Secrole>> visibleRoles = new HashMap<Program, List<Secrole>>();

		@SuppressWarnings("unchecked")
		List<ProgramProvider> programProviderList = programProviderDao.getProgramProvidersByProvider(providerNo);

		List<Integer> demoPrograms = new ArrayList<Integer>();
		for (Admission a : admissionDao.getCurrentAdmissions(Integer.parseInt(demoNo))) {
			demoPrograms.add(a.getProgramId());
		}

		for (ProgramProvider provider : programProviderList) {
			if (!demoPrograms.contains(provider.getProgram().getId()))
				continue;

			if (!visibleRoles.containsKey(provider.getProgram())) {
				visibleRoles.put(provider.getProgram(), new ArrayList<Secrole>());
			}

			List<Secrole> roleList = visibleRoles.get(provider.getProgram());
			if (!roleList.contains(provider.getRole())) {
				roleList.add(provider.getRole());

				// This role definitely has access to these permissions -> get role names and add to list
				List<DefaultRoleAccess> defaultAccess = roleProgramAccessDao.getDefaultSpecificAccessRightByRole(provider.getRoleId(), "read%notes");
				for (DefaultRoleAccess access : defaultAccess) {
					String roleName = access.getAccess_type().getName().substring(5, access.getAccess_type().getName().length()-6);
					Secrole role = secroleDao.getRoleByName(roleName);
					if (!roleList.contains(role))
						roleList.add(role);
				}

				// This role also has access to these permissions -> add them to the list as well
				List<ProgramAccess> programAccess = programAccessDAO.getProgramAccessListByType(provider.getProgramId(), "read%notes");
				for (ProgramAccess access : programAccess) {
					if (access.getRoles().contains(provider.getRole())) {
						String roleName = access.getAccessType().getName().substring(5, access.getAccessType().getName().length()-6);
						Secrole role = secroleDao.getRoleByName(roleName);
						if (!roleList.contains(role))
							roleList.add(role);
					}
				}
			}
		}

		return visibleRoles;
	}

	private Map<String, ProgramAccess> convertProgramAccessListToMap(List<ProgramAccess> paList) {
		Map<String, ProgramAccess> map = new HashMap<String, ProgramAccess>();
		if(paList==null) return map;
		for (ProgramAccess pa : paList) {
			map.put(pa.getAccessType().getName().toLowerCase(), pa);
		}
		return map;
	}
}
