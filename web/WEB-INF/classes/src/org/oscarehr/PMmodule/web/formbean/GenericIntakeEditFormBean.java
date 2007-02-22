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
import org.oscarehr.PMmodule.model.IntakeInstance;
import org.oscarehr.PMmodule.model.Program;

public class GenericIntakeEditFormBean extends ActionForm {
	
    private static final long serialVersionUID = 1L;

    private String method;
    
    private Demographic client;
    private LabelValueBean[] genders;
    private LabelValueBean[] months;
    private LabelValueBean[] days;
	
    private List<LabelValueBean> bedPrograms;
    private String bedProgramId;

	private List<LabelValueBean> servicePrograms;
    private String[] serviceProgramIds;
    
    private IntakeInstance instance;
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
	
	public Integer getSelectedBedProgramId() {
    	Integer programId = null;
    	
    	if (bedProgramId != null) {
    		programId = Integer.valueOf(bedProgramId);
    	}
    	
    	return programId;
    }

	public void setCurrentBedProgramId(Integer programId) {
    	if (programId != null) {
    		setBedProgramId(programId.toString());
    	}
    }

	public void setBedProgramLabelValues(List<Program> programs) {
		List<LabelValueBean> labelValues = new ArrayList<LabelValueBean>();
		
		if (programs != null) {
			for (Program program : programs) {
				labelValues.add(new LabelValueBean(program.getName(), program.getId().toString()));
			}
		}
		
		setBedPrograms(labelValues);
	}

	public List<LabelValueBean> getBedPrograms() {
    	return bedPrograms;
    }
	
	public void setBedPrograms(List<LabelValueBean> bedPrograms) {
    	this.bedPrograms = bedPrograms;
    }

	public String getBedProgramId() {
		return bedProgramId;
	}

	public void setBedProgramId(String currentBedProgramId) {
		this.bedProgramId = currentBedProgramId;
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

	public IntakeInstance getInstance() {
    	return instance;
    }

	public void setInstance(IntakeInstance instance) {
    	this.instance = instance;
    }
	
	public Integer getAddIntakeNodeId() {
		return addIntakeNodeId;
	}

	public void setAddIntakeNodeId(Integer addIntakeNodeId) {
		this.addIntakeNodeId = addIntakeNodeId;
	}

	public String getTitle() {
		return instance.getNode().getLabel(0);
	}

	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		setBedProgramId("");
		setServiceProgramIds(new String[] {});
		
	    // TODO Reset checkboxes
	}
	
	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		
		// TODO Validate answers
		
	    return errors;
	}
    
}
