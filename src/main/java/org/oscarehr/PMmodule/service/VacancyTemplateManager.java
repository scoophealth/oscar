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

package org.oscarehr.PMmodule.service;

import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.oscarehr.PMmodule.dao.CriteriaDAO;
import org.oscarehr.PMmodule.dao.CriteriaSelectionOptionDAO;
import org.oscarehr.PMmodule.dao.CriteriaTypeDAO;
import org.oscarehr.PMmodule.dao.CriteriaTypeOptionDAO;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.dao.VacancyTemplateDAO;
import org.oscarehr.PMmodule.model.Criteria;
import org.oscarehr.PMmodule.model.CriteriaSelectionOption;
import org.oscarehr.PMmodule.model.CriteriaType;
import org.oscarehr.PMmodule.model.CriteriaTypeOption;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.VacancyTemplate;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class VacancyTemplateManager {
	
	private static VacancyTemplateDAO vacancyTemplateDAO = (VacancyTemplateDAO) SpringUtils.getBean("vacancyTemplateDAO");
	private static CriteriaDAO criteriaDAO = (CriteriaDAO) SpringUtils.getBean("criteriaDAO");
	private static CriteriaTypeDAO criteriaTypeDAO = (CriteriaTypeDAO) SpringUtils.getBean("criteriaTypeDAO");
	private static CriteriaTypeOptionDAO criteriaTypeOptionDAO = (CriteriaTypeOptionDAO) SpringUtils.getBean("criteriaTypeOptionDAO");
	private static CriteriaSelectionOptionDAO criteriaSelectionOptionDAO = (CriteriaSelectionOptionDAO) SpringUtils.getBean("criteriaSelectionOptionDAO");
	private static ProgramDao programDao = (ProgramDao) SpringUtils.getBean("programDao");
	
	public static List<Program> getPrograms(Integer facilityId) {
		return programDao.getProgramsByFacilityId(facilityId);
	}
	
	public static List<VacancyTemplate> getVacancyTemplateByWlProgramId(Integer wlProgramId) {
		List<VacancyTemplate> results = vacancyTemplateDAO.getVacancyTemplateByWlProgramId(wlProgramId);
		return (results);
	} 
	
	public static List<CriteriaTypeOption> getCriteriaTypeOptions(Integer typeId) {
		List<CriteriaTypeOption> results = criteriaTypeOptionDAO.getCriteriaTypeOptionByTypeId(typeId);
		return (results);
	} 

	public static List<CriteriaTypeOption> getAllCriteriaTypeOptions() {
		List<CriteriaTypeOption> results = criteriaTypeOptionDAO.getAllCriteriaTypeOptions();
		return (results);
	} 
		
	public static VacancyTemplate getVacancyTemplateByTemplateId(Integer templateId) {
		VacancyTemplate vacancyTemplate = vacancyTemplateDAO.find(templateId);
		return (vacancyTemplate);
	}
	
	public static Criteria getCriteriaByCriteriaId(Integer id) {
		Criteria c = criteriaDAO.find(id);
		return (c);
	}
	
	public static String getSelectedOneValue(Integer templateId, String typeName) {
		if(templateId==null)
			return "";
		CriteriaType ct = criteriaTypeDAO.findByName(typeName);
		if(ct==null) 
			return "";		
		Criteria c = criteriaDAO.getCriteriaByTemplateIdAndTypeId(templateId, ct.getId());
		if(c==null)
			return "";
		return c.getCriteriaValue();
	}
	
	public static Criteria getSelectedCriteria(Integer templateId, String typeName) {
		if(templateId==null)
			return null;
		CriteriaType ct = criteriaTypeDAO.findByName(typeName);
		if(ct==null) 
			return null;		
		return criteriaDAO.getCriteriaByTemplateIdAndTypeId(templateId, ct.getId());		
	}
	/**
	 * This method is meant to return a bunch of html <option> tags for each list element.
	 */
	public static String renderAsSelectOptions(String type) {
		//get type id
		CriteriaType ctype = criteriaTypeDAO.findByName(type);
		List<CriteriaTypeOption> options = criteriaTypeOptionDAO.getCriteriaTypeOptionByTypeId(ctype.getId());
		StringBuilder sb = new StringBuilder();

		//sb.append("<option value=\"\" title=\"\">--- no selection ---</option>");

		for (CriteriaTypeOption option : options) {
			String label = option.getOptionLabel();
			String htmlEscapedName = StringEscapeUtils.escapeHtml(label);
			//String selected = (CdsClientFormData.containsAnswer(existingAnswers, option.getCdsDataCategory()) ? "selected=\"selected\"" : "");
			String selectedOrNot = "selected";
			sb.append("<option " + selectedOrNot + " value=\"" + StringEscapeUtils.escapeHtml(String.valueOf(option.getId())) + "\" title=\"" + htmlEscapedName + "\">" + htmlEscapedName + "</option>");
		}

		return (sb.toString());
	}

	public static String renderAnswersAsSelectOptions(Integer templateId , String type) {		
		if(templateId == null) 
			return null;
		
		//get type id <- type name
		CriteriaType ctype = criteriaTypeDAO.findByName(type);
		
		//get criteria id <- type id, template id
		Criteria criteria = criteriaDAO.getCriteriaByTemplateIdAndTypeId(templateId, ctype.getId());
		if(criteria == null)
			return null;
		
		StringBuilder sb = new StringBuilder();
		List<CriteriaSelectionOption> selectedOptions = criteriaSelectionOptionDAO.getCriteriaSelectedOptionsByCriteriaId(criteria.getId());
		for(CriteriaSelectionOption cso : selectedOptions) {
			//value is unique
			CriteriaTypeOption option = criteriaTypeOptionDAO.getByValueAndTypeId(cso.getOptionValue(), ctype.getId());
			String label = option.getOptionLabel();
			String htmlEscapedName = StringEscapeUtils.escapeHtml(label);
			//String selected = (CdsClientFormData.containsAnswer(existingAnswers, option.getCdsDataCategory()) ? "selected=\"selected\"" : "");
			String selected = "selected";
			sb.append("<option " + selected + " value=\"" + StringEscapeUtils.escapeHtml(String.valueOf(option.getId())) + "\" title=\"" + htmlEscapedName + "\">" + htmlEscapedName + "</option>");
		}
		return (sb.toString());
		
	}
	
	public static VacancyTemplate createVacancyTemplate(String templateId)
	{
		LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();		
		VacancyTemplate vt =new VacancyTemplate();
		if(StringUtils.isBlank(String.valueOf(templateId)) || "0".equals(templateId) || templateId.equalsIgnoreCase("null")) {
			vt.setActive(true);
		} else {
			vt = vacancyTemplateDAO.getVacancyTemplate(Integer.valueOf(templateId));
		}		
		return(vt);		
	}
	
	public static void saveVacancyTemplate(VacancyTemplate vt) {			
		if(vt.getId()!=null) {
			vacancyTemplateDAO.merge(vt);			
		} else {			
			vacancyTemplateDAO.persist(vt);
		}
		
	}
	
	public static Criteria createCriteria(String criteriaId)
	{
		LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();		
		Criteria c =new Criteria();
		if( !(StringUtils.isBlank(criteriaId) || "0".equals(criteriaId))) {		
			c = criteriaDAO.find(Integer.valueOf(criteriaId));
		}		
		return(c);		
	}
	
	public static void saveCriteria(Criteria c) {	
		if(c.getId()!=null) {
			criteriaDAO.merge(c);			
		} else {			
			criteriaDAO.persist(c);
		}
		
	}
	
	public static void saveCriteriaSelectedOption(CriteriaSelectionOption c) {	
		if(c.getId()!=null) {
			criteriaSelectionOptionDAO.merge(c);			
		} else {			
			criteriaSelectionOptionDAO.persist(c);
		}
		
	}
	

}
