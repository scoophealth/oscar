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
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import org.oscarehr.PMmodule.model.Demographic;
import org.oscarehr.PMmodule.model.Intake;
import org.oscarehr.PMmodule.model.Program;

public class GenericIntakeEditFormBean extends ActionForm {
	
    private static final long serialVersionUID = 1L;

    private String method;
    
    private Demographic client;
    private LabelValueBean[] genders;
    private LabelValueBean[] months;
    private LabelValueBean[] days;
	
    private List<LabelValueBean> bedCommunityPrograms;
    private String bedCommunityProgramId;

	private List<LabelValueBean> servicePrograms;
    private String[] serviceProgramIds;
    
    private Intake intake;
    private Integer addIntakeNodeId;
    
    public GenericIntakeEditFormBean() {
    	setGenders(GenericIntakeConstants.GENDERS);
		setMonths(GenericIntakeConstants.MONTHS);
		setDays(GenericIntakeConstants.DAYS);
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

	public LabelValueBean[] getGenders() {
		return genders;
	}

	public void setGenders(LabelValueBean[] genders) {
		this.genders = genders;
	}

	public LabelValueBean[] getMonths() {
		return months;
	}
	
	public void setMonths(LabelValueBean[] months) {
	    this.months = months;
    }
	
	public LabelValueBean[] getDays() {
    	return days;
    }
	
	public void setDays(LabelValueBean[] days) {
	    this.days = days;
    }
	
	public Integer getSelectedBedCommunityProgramId() {
    	Integer programId = null;
    	
    	if (bedCommunityProgramId != null && bedCommunityProgramId.length() > 0) {
    		programId = Integer.valueOf(bedCommunityProgramId);
    	}
    	
    	return programId;
    }

	public void setCurrentBedCommunityProgramId(Integer programId) {
    	if (programId != null) {
    		setBedCommunityProgramId(programId.toString());
    	}
    }

	public void setBedCommunityProgramLabelValues(List<Program> programs) {
		List<LabelValueBean> labelValues = new ArrayList<LabelValueBean>();
		
		if (programs != null) {
			for (Program program : programs) {
				labelValues.add(new LabelValueBean(program.getName(), program.getId().toString()));
			}
		}
		
		setBedCommunityPrograms(labelValues);
	}

	public List<LabelValueBean> getBedCommunityPrograms() {
    	return bedCommunityPrograms;
    }
	
	public void setBedCommunityPrograms(List<LabelValueBean> bedCommunityPrograms) {
    	this.bedCommunityPrograms = bedCommunityPrograms;
    }

	public String getBedCommunityProgramId() {
		return bedCommunityProgramId;
	}

	public void setBedCommunityProgramId(String currentBedCommunityProgramId) {
		this.bedCommunityProgramId = currentBedCommunityProgramId;
	}

	public List<Integer> getSelectedServiceProgramIds() {
    	List<Integer> programIds = new ArrayList<Integer>();
    	
    	if (serviceProgramIds != null) {
    		for (String programId : serviceProgramIds) {
    			programIds.add(Integer.valueOf(programId));
    		}
    	}
    	
    	return programIds;
    }

	public void setCurrentServiceProgramIds(Set<Integer> currentProgramIds) {
    	List<String> programIds = new ArrayList<String>();
    	
    	if (currentProgramIds != null) {
    		for (Integer currentProgramId : currentProgramIds) {
    			programIds.add(currentProgramId.toString());
    		}
    	}
    	
    	setServiceProgramIds((String[]) programIds.toArray(new String[programIds.size()]));
    }

	public void setServiceProgramLabelValues(List<Program> programs) {
		List<LabelValueBean> labelValues = new ArrayList<LabelValueBean>();
		
		for (Program program : programs) {
			labelValues.add(new LabelValueBean(program.getName(), program.getId().toString()));
		}
		
		setServicePrograms(labelValues);
	}
	
	public List<LabelValueBean> getServicePrograms() {
    	return servicePrograms;
    }
	
	public void setServicePrograms(List<LabelValueBean> servicePrograms) {
    	this.servicePrograms = servicePrograms;
    }
	
	public String[] getServiceProgramIds() {
		return serviceProgramIds;
	}

	public void setServiceProgramIds(String[] currentServiceProgramIds) {
		this.serviceProgramIds = currentServiceProgramIds;
	}

	public Intake getIntake() {
    	return intake;
    }

	public void setIntake(Intake instance) {
    	this.intake = instance;
    }
	
	public Integer getAddIntakeNodeId() {
		return addIntakeNodeId;
	}

	public void setAddIntakeNodeId(Integer addIntakeNodeId) {
		this.addIntakeNodeId = addIntakeNodeId;
	}

	public String getTitle() {
		return intake.getNode().getLabelStr();
	}

	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		setBedCommunityProgramId("");
		setServiceProgramIds(new String[] {});
		
		if (intake != null) {
			for (String id : intake.getBooleanAnswerNodeIds()) {
				if (request.getParameter("answerMapped[" + id + "].value") == null) {
					intake.setAnswerMapped(id, "F");
				}
			}
		}
	}
	
	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		// TODO Intake validate answers
	    return new ActionErrors();
	}
    
}
