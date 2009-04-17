/**
 * Copyright (C) 2007.
 * Centre for Research on Inner City Health, St. Michael's Hospital, Toronto, Ontario, Canada.
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.oscarehr.PMmodule.web.formbean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.PMmodule.model.Intake;
import org.oscarehr.PMmodule.model.IntakeAnswerElement;
import org.oscarehr.PMmodule.model.Program;

public class GenericIntakeEditFormBean extends ActionForm {

	private static final String BED_PROGRAM_LABEL = "Bed Program";
	private static final String EXTERNAL_PROGRAM_LABEL = "External Agency Client Referred From";
	private static final String COMMUNITY_PROGRAM_LABEL = "Residence Location";
	private static final String PROGRAM_IN_DOMAIN_LABEL = "Select where the intake is being performed if different from Admission Program";
	
	private String method;

	private Demographic client;
	private List genders;
	private LabelValueBean[] months;
	private LabelValueBean[] days;
	private LabelValueBean[] provinces;

	private List<LabelValueBean> bedCommunityPrograms;
	private String bedCommunityProgramId;
	private String bedCommunityProgramLabel;

	private List<LabelValueBean> externalPrograms;
	private String externalProgramId;
	private String externalProgramLabel;
	
	private List<LabelValueBean> servicePrograms;
	private String[] serviceProgramIds;

	private List<LabelValueBean> programsInDomain;
	private String programInDomainId;
	private String programInDomainLabel;
	
	private Intake intake;

	private Integer nodeId;
	
    public GenericIntakeEditFormBean() {
		months = GenericIntakeConstants.MONTHS;
		days = GenericIntakeConstants.DAYS;
		provinces = GenericIntakeConstants.PROVINCES;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Demographic getClient() {
		return client;
	}

	public void setClient(Demographic client) {
		this.client = client;
	}

	public List getGenders() {
		return genders;
	}
	
	public void setGenders(List genders) {
		this.genders = genders;
	}

	public LabelValueBean[] getMonths() {
		return months;
	}

	public LabelValueBean[] getDays() {
		return days;
	}

	public LabelValueBean[] getProvinces() {
		return provinces;
	}

	public void setProvinces(LabelValueBean[] provinces) {
		this.provinces = provinces;
	}

//	 programs in provider's domain --------------------

	public List<LabelValueBean> getProgramsInDomain() {
		return programsInDomain;
	}

	public void setProgramsInDomain(List<Program> programs_inDomain) {
		setProgramInDomainLabel(!programs_inDomain.isEmpty());
		
		List<LabelValueBean> labelValues = new ArrayList<LabelValueBean>();
		labelValues.add(GenericIntakeConstants.EMPTY);
		labelValues.addAll(convertToLabelValues(programs_inDomain));	
		programsInDomain = labelValues;
	}

	public String getProgramInDomainLabel() {
		return programInDomainLabel;
	}

	public void setProgramInDomainLabel(boolean hasProgramsInDomain) {
		StringBuffer buffer = new StringBuffer();

		if (hasProgramsInDomain) {
			buffer.append(PROGRAM_IN_DOMAIN_LABEL);
		}

		programInDomainLabel = buffer.toString();
	}

	// Selected program id

	public Integer getSelectedProgramInDomainId() {
		return convertToInteger(programInDomainId);
	}

	public void setSelectedProgramInDomainId(Integer selectedId) {
		programInDomainId = convertToString(selectedId);
	}

	public String getProgramInDomainId() {
		return programInDomainId;
	}

	public void setProgramInDomainId(String programInDomainId) {
		this.programInDomainId = programInDomainId;
	}
	//---------------------	
	
	
	//	 external programs --------------------

	public List<LabelValueBean> getExternalPrograms() {
		return externalPrograms;
	}

	public void setExternalPrograms(List<Program> externalPrograms_s) {
		setExternalProgramLabel(!externalPrograms_s.isEmpty());
		
		List<LabelValueBean> labelValues = new ArrayList<LabelValueBean>();
		labelValues.add(GenericIntakeConstants.EMPTY);
		labelValues.addAll(convertToLabelValues(externalPrograms_s));	
		externalPrograms = labelValues;
	}

	public String getExternalProgramLabel() {
		return externalProgramLabel;
	}

	public void setExternalProgramLabel(boolean hasExternalPrograms) {
		StringBuffer buffer = new StringBuffer();

		if (hasExternalPrograms) {
			buffer.append(EXTERNAL_PROGRAM_LABEL);
		}

		externalProgramLabel = buffer.toString();
	}

	// Selected External program id

	public Integer getSelectedExternalProgramId() {
		return convertToInteger(externalProgramId);
	}

	public void setSelectedExternalProgramId(Integer selectedId) {
		externalProgramId = convertToString(selectedId);
	}

	public String getExternalProgramId() {
		return externalProgramId;
	}

	public void setExternalProgramId(String externalProgramId) {
		this.externalProgramId = externalProgramId;
	}
	//---------------------	
	
	//	 Bed/Community programs

	public List<LabelValueBean> getBedCommunityPrograms() {
		return bedCommunityPrograms;
	}

	public void setBedCommunityPrograms(List<Program> bedPrograms, List<Program> communityPrograms) {
		setBedCommunityProgramLabel(!bedPrograms.isEmpty(), !communityPrograms.isEmpty());
		bedCommunityPrograms = convertToLabelValues(bedPrograms, communityPrograms);
	}

	public String getBedCommunityProgramLabel() {
		return bedCommunityProgramLabel;
	}

	public void setBedCommunityProgramLabel(boolean hasBedPrograms, boolean hasCommunityPrograms) {
		StringBuffer buffer = new StringBuffer();

		if (hasBedPrograms) {
			buffer.append(BED_PROGRAM_LABEL);
		}

		if (hasBedPrograms && hasCommunityPrograms) {
			buffer.append(" or ");
		}

		if (hasCommunityPrograms) {
			buffer.append(COMMUNITY_PROGRAM_LABEL);
		}

		bedCommunityProgramLabel = buffer.toString();
	}

	// Selected Bed/Community program id

	public Integer getSelectedBedCommunityProgramId() {
		return convertToInteger(bedCommunityProgramId);
	}

	public void setSelectedBedCommunityProgramId(Integer selectedId) {
		bedCommunityProgramId = convertToString(selectedId);
	}

	public String getBedCommunityProgramId() {
		return bedCommunityProgramId;
	}

	public void setBedCommunityProgramId(String bedCommunityProgramId) {
		this.bedCommunityProgramId = bedCommunityProgramId;
	}
	
	// Service programs

	public List<LabelValueBean> getServicePrograms() {
		return servicePrograms;
	}

	public void setServicePrograms(List<Program> programs) {
		servicePrograms = convertToLabelValues(programs);
	}

	// Selected service program id

	public Set<Integer> getSelectedServiceProgramIds() {
		return convertToIntegers(serviceProgramIds);
	}

	public void setSelectedServiceProgramIds(Set<Integer> selectedIds) {
		serviceProgramIds = convertToStrings(selectedIds);
	}

	public String[] getServiceProgramIds() {
		return serviceProgramIds;
	}

	public void setServiceProgramIds(String[] serviceProgramIds) {
		this.serviceProgramIds = serviceProgramIds;
	}

	public Intake getIntake() {
		return intake;
	}

	public void setIntake(Intake intake) {
		this.intake = intake;
	}

	public String getTitle() {
		return intake.getNode().getLabelStr();
	}

	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		setBedCommunityProgramId("");
		setServiceProgramIds(new String[] {});

		if (intake != null) {
			for (String id : intake.getBooleanAnswerIds()) {
				if (request.getParameter("answerMapped[" + id + "].value") == null) {
					intake.setAnswerMapped(id, IntakeAnswerElement.FALSE);
				}
			}
		}
	}

	// Private

	private List<LabelValueBean> convertToLabelValues(List<Program> primary, List<Program> secondary) {
		List<LabelValueBean> labelValues = new ArrayList<LabelValueBean>();

		labelValues.add(GenericIntakeConstants.EMPTY);
		labelValues.addAll(convertToLabelValues(primary));
		labelValues.addAll(convertToLabelValues(secondary));

		return labelValues;
	}

	private List<LabelValueBean> convertToLabelValues(List<Program> programs) {
		List<LabelValueBean> labelValues = new ArrayList<LabelValueBean>();

		if (programs != null) {
			for (Program program : programs) {
				labelValues.add(new LabelValueBean(program.getName(), program.getId().toString()));
			}
		}

		return labelValues;
	}

	private Set<Integer> convertToIntegers(String[] sources) {
		Set<Integer> result = new HashSet<Integer>();

		if (sources != null) {
			for (String source : sources) {
				result.add(convertToInteger(source));
			}
		}

		return result;
	}

	private Integer convertToInteger(String source) {
		Integer result = null;

		if (source != null && source.length() > 0) {
			result = Integer.valueOf(source);
		}

		return result;
	}

	private String[] convertToStrings(Set<Integer> sources) {
		List<String> result = new ArrayList<String>();

		if (sources != null) {
			for (Integer source : sources) {
				result.add(convertToString(source));
			}
		}

		return ((String[]) result.toArray(new String[result.size()]));
	}

	private String convertToString(Integer source) {
		String result = null;

		if (source != null) {
			result = source.toString();
		}

		return result;
	}

	public Integer getNodeId() {
		return nodeId;
	}

	public void setNodeId(Integer nodeId) {
		this.nodeId = nodeId;
	}

	
}