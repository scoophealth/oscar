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

package org.oscarehr.PMmodule.web.formbean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import org.oscarehr.PMmodule.model.Intake;
import org.oscarehr.PMmodule.model.IntakeAnswerElement;
import org.oscarehr.PMmodule.model.IntakeNodeJavascript;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.common.model.Demographic;

public class GenericIntakeEditFormBean extends ActionForm {

	private static final String BED_PROGRAM_LABEL = "Bed Program";
	private static final String EXTERNAL_PROGRAM_LABEL = "External Agency Client Referred From";
	private static final String COMMUNITY_PROGRAM_LABEL = "Residential Status";
	private static final String PROGRAM_IN_DOMAIN_LABEL = "Select where the intake is being performed if different from Admission Program";
	
	private String method;

	private DemographicExtra clientExtra;
	private Demographic client;
	private List genders;
	private LabelValueBean[] months;
	private LabelValueBean[] days;
	private LabelValueBean[] provinces;
	private LabelValueBean[] maritalStatus;
	private LabelValueBean[] recipientLocation;
	private LabelValueBean[] lhinConsumerResides;
			
	private List<LabelValueBean> bedPrograms;
	private String bedProgramId;
	private String bedProgramLabel;

	private List<LabelValueBean> communityPrograms;
	private String communityProgramId;
	private String communityProgramLabel;

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
	
	private List<IntakeNodeJavascript> jsLocation;
	
    public GenericIntakeEditFormBean() {
		months = GenericIntakeConstants.MONTHS;
		days = GenericIntakeConstants.DAYS;
		provinces = GenericIntakeConstants.PROVINCES;
		maritalStatus = GenericIntakeConstants.MARITAL_STATUS;
		recipientLocation = GenericIntakeConstants.RECIPIENT_LOCATION;
		lhinConsumerResides = GenericIntakeConstants.LHIN_CONSUMER_RESIDES;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
	
	public DemographicExtra getClientExtra() {
    	return clientExtra;
    }

	public void setClientExtra(DemographicExtra clientExtra) {
    	this.clientExtra = clientExtra;
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

	public LabelValueBean[] getMaritalStatus() {
    	return maritalStatus;
    }

	public void setMaritalStatus(LabelValueBean[] maritalStatus) {
    	this.maritalStatus = maritalStatus;
    }
	
	public LabelValueBean[] getRecipientLocation() {
    	return recipientLocation;
    }

	public void setRecipientLocation(LabelValueBean[] recipientLocation) {
    	this.recipientLocation = recipientLocation;
    }

	public LabelValueBean[] getLhinConsumerResides() {
    	return lhinConsumerResides;
    }

	public void setLhinConsumerResides(LabelValueBean[] lhinConsumerResides) {
    	this.lhinConsumerResides = lhinConsumerResides;
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
		StringBuilder buffer = new StringBuilder();

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
		StringBuilder buffer = new StringBuilder();

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
	
	//	 Bed programs

	public List<LabelValueBean> getBedPrograms() {
		return bedPrograms;
	}

	public void setBedPrograms(List<Program> bedPrograms2) {
		setBedProgramLabel(!bedPrograms2.isEmpty());
		bedPrograms = convertToLabelValues2(bedPrograms2);
	}

	public String getBedProgramLabel() {
		return bedProgramLabel;
	}

	public void setBedProgramLabel(boolean hasBedPrograms) {
		StringBuilder buffer = new StringBuilder();

		if (hasBedPrograms) {
			buffer.append(BED_PROGRAM_LABEL);
		}

		bedProgramLabel = buffer.toString();
	}

	// Selected Bed program id

	public Integer getSelectedBedProgramId() {
		return convertToInteger(bedProgramId);
	}

	public void setSelectedBedProgramId(Integer selectedId) {
		bedProgramId = convertToString(selectedId);
	}

	public String getBedProgramId() {
		return bedProgramId;
	}

	public void setBedProgramId(String bedProgramId) {
		this.bedProgramId = bedProgramId;
	}
	
	//////////////////////////////////
	//	 Community programs

	public List<LabelValueBean> getCommunityPrograms() {
		return communityPrograms;
	}

	public void setCommunityPrograms(List<Program> communityPrograms2) {
		setCommunityProgramLabel(!communityPrograms2.isEmpty());
		communityPrograms = convertToLabelValues2(communityPrograms2);
	}

	public String getCommunityProgramLabel() {
		return communityProgramLabel;
	}

	public void setCommunityProgramLabel(boolean hasCommunityPrograms) {
		StringBuilder buffer = new StringBuilder();

		if (hasCommunityPrograms) {
			buffer.append(COMMUNITY_PROGRAM_LABEL);
		}

		communityProgramLabel = buffer.toString();
	}

	// Selected Community program id

	public Integer getSelectedCommunityProgramId() {
		return convertToInteger(communityProgramId);
	}

	public void setSelectedCommunityProgramId(Integer selectedId) {
		communityProgramId = convertToString(selectedId);
	}

	public String getCommunityProgramId() {
		return communityProgramId;
	}

	public void setCommunityProgramId(String communityProgramId) {
		this.communityProgramId = communityProgramId;
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
		setBedProgramId("");
		setCommunityProgramId("");
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

	private List<LabelValueBean> convertToLabelValues2(List<Program> primary) {
		List<LabelValueBean> labelValues = new ArrayList<LabelValueBean>();

		labelValues.add(GenericIntakeConstants.EMPTY);
		labelValues.addAll(convertToLabelValues(primary));		

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

		return result.toArray(new String[result.size()]);
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

	public List<IntakeNodeJavascript> getJsLocation() {
		return jsLocation;
	}

	public void setJsLocation(List<IntakeNodeJavascript> jsLocation) {
		this.jsLocation = jsLocation;
	}

	
	
}
