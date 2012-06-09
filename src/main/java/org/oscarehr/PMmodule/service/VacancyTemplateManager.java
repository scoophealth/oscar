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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.oscarehr.PMmodule.dao.CriteriaDAO;
import org.oscarehr.PMmodule.dao.CriteriaSelectionOptionDAO;
import org.oscarehr.PMmodule.dao.CriteriaTypeDAO;
import org.oscarehr.PMmodule.dao.CriteriaTypeOptionDAO;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.dao.VacancyDAO;
import org.oscarehr.PMmodule.dao.VacancyTemplateDAO;
import org.oscarehr.PMmodule.model.Criteria;
import org.oscarehr.PMmodule.model.CriteriaSelectionOption;
import org.oscarehr.PMmodule.model.CriteriaType;
import org.oscarehr.PMmodule.model.CriteriaTypeOption;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.Vacancy;
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
	private static VacancyDAO vacancyDAO= (VacancyDAO) SpringUtils.getBean("vacancyDAO");
	
	public static List<Program> getPrograms(Integer facilityId) {
		return programDao.getProgramsByFacilityId(facilityId);
	}
	
	public static List<VacancyTemplate> getVacancyTemplateByWlProgramId(Integer wlProgramId) {
		List<VacancyTemplate> results = vacancyTemplateDAO.getVacancyTemplateByWlProgramId(wlProgramId);
		return (results);
	} 
	
	public static List<Vacancy> getVacanciesByWlProgramId(Integer wlProgramId) {
		List<Vacancy> results = vacancyDAO.getVacanciesByWlProgramId(wlProgramId);
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
	
		
	public static Criteria getSelectedCriteria(Integer templateId, Integer vacancyId, Integer typeId) {
		if(templateId == null && typeId == null)
			return null;
		if(vacancyId == null && typeId == null)
			return null;
		if(vacancyId != null && typeId != null)
			return criteriaDAO.getCriteriaByTemplateIdVacancyIdTypeId(null, vacancyId, typeId);	
		else if (templateId != null && typeId != null)
			return criteriaDAO.getCriteriaByTemplateIdVacancyIdTypeId(templateId, null, typeId);	
		else
			return criteriaDAO.getCriteriaByTemplateIdVacancyIdTypeId(templateId, vacancyId, typeId);	
	}
	
	public static List<CriteriaType> getAllCriteriaTypes() {
		return criteriaTypeDAO.getAllCriteriaTypes();
	}
	
	public static List<Criteria> getRefinedCriteriasByVacancyId(Integer vacancyId) {
		return criteriaDAO.getRefinedCriteriasByVacancyId(vacancyId);
	}
	
	public static List<Criteria> getCriteriasByVacancyId(Integer vacancyId) {
		return criteriaDAO.getCriteriasByVacancyId(vacancyId);
	}
	
	public static List<Criteria> getRefinedCriteriasByTemplateId(Integer templateId) {
		return criteriaDAO.getRefinedCriteriasByTemplateId(templateId);
	}
	
	public static List<VacancyTemplate> getActiveVacancyTemplatesByProgramId(Integer programId) {
		if(programId == null)
			return null;
		return vacancyTemplateDAO.getActiveVacancyTemplatesByProgramId(programId);
	}
	
	public static List<VacancyTemplate> getActiveVacancyTemplatesByWlProgramId(Integer programId) {
		if(programId == null)
			return null;
		return vacancyTemplateDAO.getActiveVacancyTemplatesByWlProgramId(programId);
	}
	
	public static CriteriaType getCriteriaTypeById(Integer id) {
		return criteriaTypeDAO.find(id);
	}
	
	public static Vacancy getVacancyById(Integer id) {
		return vacancyDAO.find(id);
	}
	
	/**
	 * This method is meant to return a bunch of html <option> tags for each list element.
	 */
	
	public static String renderAllSelectOptions(Integer templateId, Integer vacancyId, Integer typeId) {
		Criteria criteria = new Criteria();
		criteria = getSelectedCriteria(templateId, vacancyId, typeId);
		List<CriteriaSelectionOption> selectedOptions = new ArrayList<CriteriaSelectionOption>();
		String min="", max="", required="", value="";
		
		if(criteria !=null) {				
			if(criteria.getRangeStartValue() != null)
				min = String.valueOf(criteria.getRangeStartValue());
			
			if(criteria.getRangeEndValue() != null) 
				max = String.valueOf(criteria.getRangeEndValue());
			
			value = criteria.getCriteriaValue(); //value is criteria type option id if "select_one", or number if type is "number".
			required = (criteria.getCanBeAdhoc()==true?"checked":"");
			
			selectedOptions = criteriaSelectionOptionDAO.getCriteriaSelectedOptionsByCriteriaId(criteria.getId());
		}
		
		
		//get type 
		CriteriaType ctype = criteriaTypeDAO.find(typeId);
		String type = ctype.getFieldName();
		List<CriteriaTypeOption> options = criteriaTypeOptionDAO.getCriteriaTypeOptionByTypeId(typeId);		
			
		StringBuilder sb = new StringBuilder();

		sb.append("<table width=\"100%\" border=\"1\" cellspacing=\"2\" cellpadding=\"3\"> ");
		sb.append("<tr class=\"b\">");
		sb.append("<td width=\"30%\" class=\"beright\">Requires ");sb.append(type);sb.append(":</td>");
		sb.append("<td><input type=\"checkbox\" value=\"on\" ");
		sb.append(required);
		sb.append(" name=\"");
		sb.append(type.toLowerCase().replaceAll(" ","_"));  //
		sb.append("Required\"></td>");
		sb.append("</tr>");
		
		if(ctype.getFieldType().equalsIgnoreCase("number")) {	
			sb.append("<tr class=\"b\">");
			sb.append("<td class=\"beright\">");
			sb.append(type);
			sb.append(" Value:</td>");
			sb.append("<td><input type=\"text\" size=\"50\" maxlength=\"50\" value=\"");
			sb.append(value);
			sb.append("\" name=\"");
			sb.append(type.toLowerCase().replaceAll(" ","_"));
			sb.append("Number\"></td>");
			sb.append("</tr>");
		}
		
		if(ctype.getFieldType().equalsIgnoreCase("select_one_range")) {
			sb.append("<tr class=\"b\">");
			sb.append("<td class=\"beright\">");
			sb.append(type);
			sb.append(" Range Minimum:</td>");
			sb.append("<td><input type=\"text\" size=\"50\" maxlength=\"50\" value=\"");
			sb.append(min);
			sb.append("\" name=\"");
			sb.append(type.toLowerCase().replaceAll(" ","_"));
			sb.append("Minimum\"></td>");
			sb.append("</tr>");
			sb.append("<tr class=\"b\">");
			sb.append("<td class=\"beright\">");
			sb.append(type);
			sb.append(" Range Maximum:</td>");
			sb.append("<td><input type=\"text\" size=\"50\" maxlength=\"50\" value=\"");
			sb.append(max);
			sb.append("\" name=\"");
			sb.append(type.toLowerCase().replaceAll(" ","_"));
			sb.append("Maximum\"></td>");
			sb.append("</tr>");			
			//sb.append("<tr>");
		}
			
		if(!ctype.getFieldType().equalsIgnoreCase("number")) {			
		
			sb.append("<tr class=\"b\">");
			sb.append("<td colspan=\"2\" style=\"padding-left: 10%;\">");
			sb.append("<div class=\"horizonton\">");
			sb.append("<div style=\"margin-bottom: 3px;\">");
			sb.append(type); sb.append(" List</div>");
			sb.append("<div>");
			sb.append("<select id=\"sourceOf");
			sb.append(type.toLowerCase().replaceAll(" ","_"));
			sb.append("\" name=\"sourceOf");
			sb.append(type.toLowerCase().replaceAll(" ","_"));
			sb.append("\"");
			
			if(ctype.getFieldType().equalsIgnoreCase("select_multiple")) {
				sb.append(" multiple=\"multiple\" size=\"7\" ");	
			}
			
			sb.append(" style=\"width: 200px;\">");
			
			if(ctype.getFieldType().equalsIgnoreCase("select_one") || ctype.getFieldType().equalsIgnoreCase("select_one_range") )
				sb.append("<option value=\"\"></option>");
			
			for (CriteriaTypeOption option : options) {
				boolean skip = false;
				String label = option.getOptionLabel();
				String htmlEscapedName = StringEscapeUtils.escapeHtml(label);
				String selectedOrNot = "";			
				if(option.getOptionValue()!=null && option.getOptionValue().equalsIgnoreCase(value))
				//if(option.getId()!=null && String.valueOf(option.getId()).equalsIgnoreCase(value))
					selectedOrNot = "selected";
				
				if(selectedOptions != null) {
					for(CriteriaSelectionOption cso : selectedOptions) {
						//If the options selected, should be removed from source of list.
						if( cso.getOptionValue() != null && cso.getOptionValue().equals(option.getOptionValue()))
								skip = true;
					}
				}
				
				if(skip)
					continue;
				
				sb.append("<option " + selectedOrNot + " value=\"" + StringEscapeUtils.escapeHtml(option.getOptionValue()) + "\" title=\"" + htmlEscapedName + "\">" + htmlEscapedName + "</option>");
						
			}
			
			sb.append("</select>");
			sb.append("</div>");
			sb.append("</div>");
		}
		
	if(ctype.getFieldType().equalsIgnoreCase("select_multiple")) {
		
		sb.append("<div class=\"horizonton\" style=\"padding-top: 40px;\">");
		sb.append("<div>");
		sb.append("<input type=\"button\" id=\"add_"); 
		sb.append(type.toLowerCase().replaceAll(" ","_")); 
		sb.append("\" name=\"add_"); sb.append(type.toLowerCase().replaceAll(" ","_"));
		sb.append("\" value=\">>\">");
		sb.append("</div>");
		sb.append("<div>");
		sb.append("<input type=\"button\" id=\"remove_");
		sb.append(type.toLowerCase().replaceAll(" ","_"));
		sb.append("\" name=\"remove_");
		sb.append(type.toLowerCase().replaceAll(" ","_"));
		sb.append("\" value=\"<<\">");
		sb.append("</div>");
		sb.append("</div>");
				
		sb.append("<div class=\"horizonton\">");
		sb.append("<div style=\"margin-bottom: 3px;\">Required ");
		sb.append(type);
		sb.append("</div>");
		sb.append("<div>");
		sb.append("<select id=\"targetOf");
		sb.append(type.toLowerCase().replaceAll(" ","_"));
		sb.append("\" name=\"targetOf");
		sb.append(type.toLowerCase().replaceAll(" ","_"));
		sb.append("\" multiple=\"multiple\" size=\"7\" ");
		sb.append("style=\"width: 200px;\">");
		
		for(CriteriaSelectionOption cso : selectedOptions) {
				//criteria_type_option's value is unique?
				//value in criteria_selection_option is the same value in criteria_type_option? 
				CriteriaTypeOption option2 = criteriaTypeOptionDAO.getByValueAndTypeId(cso.getOptionValue(), ctype.getId());
				
				//value in criteria_selection_option is the id in criteria_type_option, this makes more sense as the value may not be unique or may be null
				//CriteriaTypeOption option2 = criteriaTypeOptionDAO.getCriteriaTypeOptionByOptionId(Integer.parseInt(cso.getOptionValue()));
				String label = option2.getOptionLabel();
				String htmlEscapedName = StringEscapeUtils.escapeHtml(label);
				//String selected = (CdsClientFormData.containsAnswer(existingAnswers, option.getCdsDataCategory()) ? "selected=\"selected\"" : "");
				String selected = "selected";
				sb.append("<option " + selected + " value=\"" + StringEscapeUtils.escapeHtml(option2.getOptionValue()) + "\" title=\"" + htmlEscapedName + "\">" + htmlEscapedName + "</option>");
		}
		
		sb.append("</select>");
		sb.append("</div>");
		sb.append("</div>");
		sb.append("</td>");
		sb.append("</tr>");	
		}
		sb.append("</table>");
		sb.append("<br>");
		
		if(ctype.getFieldType().equalsIgnoreCase("select_multiple")) {
			sb.append("<script type=\"text/javascript\">");
			sb.append(" $(document).ready(");
		    sb.append("function () { ");		    
		    sb.append("$('#");
		    sb.append("remove_");
			sb.append(type.toLowerCase().replaceAll(" ","_"));
		    sb.append("').click(");
            sb.append("function (e) {");
            sb.append("$('#targetOf");
            sb.append(type.toLowerCase().replaceAll(" ","_"));
            sb.append(" > option:selected').appendTo('#sourceOf");
            sb.append(type.toLowerCase().replaceAll(" ","_"));
            sb.append("');");
            sb.append("e.preventDefault();");
            sb.append("});");
            
            sb.append("$('#");
		    sb.append("add_");
			sb.append(type.toLowerCase().replaceAll(" ","_"));
		    sb.append("').click(");
            sb.append("function (e) {");
            sb.append("$('#sourceOf");
            sb.append(type.toLowerCase().replaceAll(" ","_"));
            sb.append(" > option:selected').appendTo('#targetOf");
            sb.append(type.toLowerCase().replaceAll(" ","_"));
            sb.append("');");
            sb.append("e.preventDefault();");
            sb.append("});");
            
		 	sb.append("});");
		 	sb.append("</script>");
		}
		
		return (sb.toString());
	}
		
	public static VacancyTemplate createVacancyTemplate(String templateId)
	{		
		if(StringUtils.isBlank(String.valueOf(templateId)) || "0".equals(templateId) || templateId.equalsIgnoreCase("null")) {
			VacancyTemplate vt = new VacancyTemplate();
			vt.setActive(true);
			return vt;
		} else {
			return vacancyTemplateDAO.getVacancyTemplate(Integer.valueOf(templateId));
		}					
	}
	
	public static void saveVacancy(Vacancy v) {			
		if(v.getId()!=null) {
			vacancyDAO.merge(v);			
		} else {			
			vacancyDAO.persist(v);
		}
		
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
