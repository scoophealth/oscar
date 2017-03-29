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


package org.oscarehr.learning.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.DemographicExtDao;
import org.oscarehr.common.dao.SecRoleDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.DemographicExt;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.SecRole;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

/**
 * TODO: Error handling, and access checks.
 *
 * @author marc
 *
 */
public class CourseManagerAction extends DispatchAction {

	private static Logger logger = MiscUtils.getLogger();
	private static ProgramDao programDao = (ProgramDao) SpringUtils.getBean("programDao");
	private static ProgramManager programManager = (ProgramManager) SpringUtils.getBean("programManager");
	private static DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");
	private static ProviderDao providerDao = (ProviderDao) SpringUtils.getBean("providerDao");
	private static DemographicExtDao demographicExtDao = SpringUtils.getBean(DemographicExtDao.class);

	public ActionForward createCourse(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws IOException
	{
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);

		DynaActionForm courseForm = (DynaActionForm) form;
		String name = (String)courseForm.get("name");
		logger.info("creating course: " + name);

		String result = "";

		int facilityId = loggedInInfo.getCurrentFacility().getId();
		try {
			Program p = new Program();
			p.setName(name);
			p.setMaxAllowed(999);
			p.setFacilityId(facilityId);
			p.setSiteSpecificField("course");
			p.setAddress("");
			p.setAbstinenceSupport("");
			p.setDescription(name);
			p.setEmail("");
			p.setEmergencyNumber("");
			p.setExclusiveView("");
			p.setFacilityDesc("");
			p.setFax("");
			p.setLocation("");
			p.setPhone("");
			p.setType("service");
			p.setUrl("");
			programDao.saveProgram(p);
			result="Course sucessfully created.";
		}catch(Exception e) {
			logger.error("Error", e);
			result = "Error Occured: " + e.getMessage();
		}

    	response.getWriter().print(result);
    	return null;

	}

	public static List<Program> getCoursesByModerator(String providerNo) {
		SecRoleDao roleDao = (SecRoleDao)SpringUtils.getBean("secRoleDao");
		SecRole role = roleDao.findByName("moderator");

		return programManager.getAllProgramsByRole(providerNo, role.getId());
	}

	public static List<Program> getCourses() {
		List<Program> programs = programDao.getAllPrograms();
		List<Program> results = new ArrayList<Program>();
		for(Program p:programs) {
			if(p.getSiteSpecificField()!=null && p.getSiteSpecificField().equals("course")) {
				results.add(p);
			}
		}
		return results;
	}

	public ActionForward getAllCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    	throws IOException
    {
		//check rights

		//get data, filter it only where this is defined as a course
		List<Program> results = getCourses();

		//serialize and return
    	JSONArray jsonArray = JSONArray.fromObject( results );
    	response.getWriter().print(jsonArray);
		return null;
    }

	public ActionForward getCourseDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws IOException
	{
		Integer id = null;
		try {
			id = Integer.parseInt(request.getParameter("id"));
		}catch(NumberFormatException e) {
			logger.warn("no valid course id passed");
			return null;
		}

		//p.get
		ProgramManager programManager = (ProgramManager) SpringUtils.getBean("programManager");
		@SuppressWarnings("unchecked")
		List<ProgramProvider> programProviders = programManager.getProgramProviders(String.valueOf(id));
		Map<String,CourseDetailBean> results = new HashMap<String,CourseDetailBean>();

		for(ProgramProvider pp:programProviders) {
			//pp.get
			CourseDetailBean result = new CourseDetailBean();
			result.setProviderNo(pp.getProviderNo());
			result.setProviderName(pp.getProvider().getFormattedName());
			result.setRoleId(pp.getRole().getId());
			result.setRoleName(pp.getRole().getName());
			results.put(pp.getProviderNo(),result);
		}

		ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
		List<Provider> providerList = providerDao.getActiveProviders();
		List<CourseDetailBean> r = new ArrayList<CourseDetailBean>();

		for(Provider provider:providerList) {
			CourseDetailBean bean = new CourseDetailBean();
			CourseDetailBean ab = results.get(provider.getProviderNo());
			if(ab!=null) {
				bean.setChecked(true);
				bean.setRoleId(ab.getRoleId());
				bean.setRoleName(ab.getRoleName());
			}
			bean.setProviderName(provider.getFormattedName());
			bean.setProviderNo(provider.getProviderNo());
			r.add(bean);
		}

		JSONArray jsonArray = JSONArray.fromObject( r );
    	response.getWriter().print(jsonArray);

		return null;
	}

	public ActionForward getCourseStudents(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
		Integer id = null;
		try {
			id = Integer.parseInt(request.getParameter("id"));
		}catch(NumberFormatException e) {
			logger.warn("no valid course id passed");
			return null;
		}
		logger.info("course id is " + id);

		List<Demographic> demographics = new ArrayList<Demographic>();
		for(DemographicExt de :demographicExtDao.getDemographicExtByKeyAndValue("course", String.valueOf(id))) {
			demographics.add(demographicDao.getDemographicById(de.getDemographicNo()));
		}
		
		logger.info("# of demographics in that course according to ext entry: " + demographics.size());
		List<PatientDetailBean> results = new ArrayList<PatientDetailBean>();

		for(Demographic d:demographics) {
			PatientDetailBean bean = new PatientDetailBean();
			bean.setDemographicNo(d.getDemographicNo());
			bean.setName(d.getFormattedName());
			bean.setProviderNo(d.getProviderNo());
			Provider provider = providerDao.getProvider(d.getProviderNo());
			bean.setProviderName(provider.getFormattedName());
			results.add(bean);
		}

		JSONArray jsonArray = JSONArray.fromObject( results );
		response.getWriter().print(jsonArray);

		return null;

	}

	public ActionForward getCourseProviders(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws IOException
{
	Integer id = null;
	try {
		id = Integer.parseInt(request.getParameter("id"));
	}catch(NumberFormatException e) {
		logger.warn("no valid course id passed");
		return null;
	}

	//p.get
	ProgramManager programManager = (ProgramManager) SpringUtils.getBean("programManager");
	@SuppressWarnings("unchecked")
	List<ProgramProvider> programProviders = programManager.getProgramProviders(String.valueOf(id));
	List<CourseDetailBean> r = new ArrayList<CourseDetailBean>();

	for(ProgramProvider pp:programProviders) {
		CourseDetailBean bean = new CourseDetailBean();
		bean.setRoleId(pp.getRoleId());
		bean.setProviderName(pp.getProvider().getFormattedName());
		bean.setProviderNo(pp.getProvider().getProviderNo());
		r.add(bean);
	}



	JSONArray jsonArray = JSONArray.fromObject( r );
	response.getWriter().print(jsonArray);

	return null;
}


	public static List<SecRole> getRoles()  {
		SecRoleDao roleDao = (SecRoleDao)SpringUtils.getBean("secRoleDao");
		List<SecRole> roles = roleDao.findAll();
		return roles;
	}


	public static String createRoleSelectBox(String providerNo) {
		return "<select><option>test - "+providerNo+"</option></select>";
	}

	public ActionForward saveCourseDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		logger.info("save course details");

		int courseId = 0;
		String tmpCourseId = request.getParameter("courseId");
		courseId = Integer.parseInt(tmpCourseId);

		Map params = request.getParameterMap();
		List<ProgramProvider> results = new ArrayList<ProgramProvider>();

		for(Object key:params.keySet()) {
			if(((String)key).startsWith("checked_")) {
				String providerNo = ((String)key).substring(8);

				String roleId = request.getParameter("role_"+providerNo);
				//logger.info("provider " + providerNo + " is checked with role id " + roleId);

				ProgramProvider pp = new ProgramProvider();
				pp.setProgramId(new Long(courseId));
				pp.setProviderNo(providerNo);
				pp.setRoleId(Long.parseLong(roleId));

				results.add(pp);
			}
		}

		//clear the current assignments
		ProgramManager programManager = (ProgramManager) SpringUtils.getBean("programManager");
		@SuppressWarnings("unchecked")
		List<ProgramProvider> programProviders = programManager.getProgramProviders(String.valueOf(courseId));
		for(ProgramProvider pp:programProviders) {
			programManager.deleteProgramProvider(String.valueOf(pp.getId()));
		}

		for(ProgramProvider pp:results) {
			programManager.saveProgramProvider(LoggedInInfo.getLoggedInInfoFromSession(request),pp);
		}

		logger.info("saved course");
		return null;
	}

}
