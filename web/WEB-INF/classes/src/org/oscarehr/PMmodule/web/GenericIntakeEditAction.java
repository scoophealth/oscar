package org.oscarehr.PMmodule.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.oscarehr.PMmodule.exception.AdmissionException;
import org.oscarehr.PMmodule.exception.IntegratorException;
import org.oscarehr.PMmodule.exception.ProgramFullException;
import org.oscarehr.PMmodule.model.Admission;
import org.oscarehr.PMmodule.model.Demographic;
import org.oscarehr.PMmodule.model.IntakeInstance;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.web.formbean.GenericIntakeEditFormBean;

public class GenericIntakeEditAction extends BaseAction {

	private static Log LOG = LogFactory.getLog(GenericIntakeEditAction.class);
			
	// Request Attributes
	public static final String CLIENT = "client";
	
	// Parameters
	public static final String METHOD = "method";
	public static final String TYPE = "type";
	public static final String CLIENT_ID = "clientId";
	public static final String PROGRAM_ID = "programId";
	
	// Method parameter values
	public static final String CREATE = "create";
	public static final String UPDATE = "update";

	// Type parameter values
	public static final String QUICK = "quick";
	public static final String INDEPTH = "indepth";
	public static final String PROGRAM = "program";

	// Forwards
	private static final String EDIT = "edit";
	private static final String CLIENT_EDIT = "clientEdit";
	private static final String PROVIDER_VIEW = "providerView";
	
	public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		GenericIntakeEditFormBean intakeEditBean = (GenericIntakeEditFormBean) form;
		
		Demographic client = getClient(request);
		intakeEditBean.setClient(client);
		
		List<Program> bedPrograms = getBedPrograms();
		intakeEditBean.setBedProgramLabelValues(bedPrograms);
		
		List<Program> servicePrograms = getServicePrograms();
		intakeEditBean.setServiceProgramLabelValues(servicePrograms);
		
		String intakeType = getType(request);
		String providerNo = getProviderNo(request);
		
		if (QUICK.equalsIgnoreCase(intakeType)) {
			IntakeInstance instance = genericIntakeManager.createQuickIntake(providerNo);
			intakeEditBean.setInstance(instance);
		} else if (INDEPTH.equalsIgnoreCase(intakeType)) {
			IntakeInstance instance = genericIntakeManager.createIndepthIntake(providerNo);
			intakeEditBean.setInstance(instance);
		} else if (PROGRAM.equalsIgnoreCase(intakeType)) {
			IntakeInstance instance = genericIntakeManager.createProgramIntake(getProgramId(request), providerNo);
			intakeEditBean.setInstance(instance);
		}
		
		return mapping.findForward(EDIT);
	}

	public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		GenericIntakeEditFormBean intakeEditBean = (GenericIntakeEditFormBean) form;

		Integer clientId = getClientId(request);
		Demographic client = getClient(clientId);
		intakeEditBean.setClient(client);
		
		List<Program> bedPrograms = getBedPrograms();
		intakeEditBean.setBedProgramLabelValues(bedPrograms);
		intakeEditBean.setCurrentBedProgramId(getClientBedProgramId(clientId));
		
		List<Program> servicePrograms = getServicePrograms();
		intakeEditBean.setServiceProgramLabelValues(servicePrograms);
		intakeEditBean.setCurrentServiceProgramIds(getClientServiceProgramIds(clientId));
		
		String intakeType = getType(request);
		String providerNo = getProviderNo(request);
		
		if (QUICK.equalsIgnoreCase(intakeType)) {
			IntakeInstance instance = genericIntakeManager.copyQuickIntake(clientId, providerNo);
			intakeEditBean.setInstance(instance);
		} else if (INDEPTH.equalsIgnoreCase(intakeType)) {
			IntakeInstance instance = genericIntakeManager.copyIndepthIntake(clientId, providerNo);
			intakeEditBean.setInstance(instance);
		} else if (PROGRAM.equalsIgnoreCase(intakeType)) {
			IntakeInstance instance = genericIntakeManager.copyProgramIntake(clientId, getProgramId(request), providerNo);
			intakeEditBean.setInstance(instance);
		}

		return mapping.findForward(EDIT);
	}

	public ActionForward addAnswer(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		// create copy of answer (addIntakeNodeId)
		// save copy
		
		return mapping.findForward(EDIT);
	}

	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		ActionForward forward = null;
		
		try {
			GenericIntakeEditFormBean intakeEditBean = (GenericIntakeEditFormBean) form;
			
			// save client
			Demographic client = intakeEditBean.getClient();
			saveClient(client);
			
			// admit client to program(s)
			Integer clientId = client.getDemographicNo();
			String providerNo = getProviderNo(request);
			Integer bedProgramId = intakeEditBean.getSelectedBedProgramId();
			List<Integer> serviceProgramIds = intakeEditBean.getSelectedServiceProgramIds();
			
			admitBedProgram(clientId, providerNo, bedProgramId);
			admitServicePrograms(clientId, providerNo, serviceProgramIds);
			
			// save instance
			IntakeInstance instance = intakeEditBean.getInstance();
			saveInstance(instance, clientId);
			
			forward = getClientEditForward(mapping, clientId);
		} catch (Exception e) {
			ActionMessages messages = new ActionMessages();
    		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message", e.getMessage()));
    		saveErrors(request, messages);
			
    		forward = mapping.findForward(EDIT);
		}
		
		return forward;
	}
	
	@Override
	protected ActionForward cancelled(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Intake Clear session
		return mapping.findForward(PROVIDER_VIEW);
	}

	// Attribute
	
	private Demographic getClient(HttpServletRequest request) {
		Demographic client = (Demographic) getAttribute(request, CLIENT);
		return (client != null) ? client : new Demographic();
	}
	
	private Object getAttribute(HttpServletRequest request, String attributeName) {
		Object attribute = request.getAttribute(attributeName);
		
		if (attribute != null) {
			request.removeAttribute(attributeName);
		}
		
		return attribute;
	}
	
	// Parameter
	
	private String getType(HttpServletRequest request) {
		return getParameter(request, TYPE);
	}

	private Integer getClientId(HttpServletRequest request) {
		return Integer.valueOf(getParameter(request, CLIENT_ID));
	}

	private Integer getProgramId(HttpServletRequest request) {
		return Integer.valueOf(getParameter(request, PROGRAM_ID));
	}
		
	private String getParameter(HttpServletRequest request, String parameterName) {
		return request.getParameter(parameterName);
	}
	
	// Forward
	
	private ActionForward getClientEditForward(ActionMapping mapping, Integer clientId) {
    	StringBuilder parameters = new StringBuilder(PARAM_AND);
    	parameters.append(ClientManagerAction.ID).append(PARAM_EQUALS).append(clientId);
    	
    	return createForward(mapping, CLIENT_EDIT, parameters);
    }
	
	// Adapt
	
	private Demographic getClient(Integer clientId) {
		return clientManager.getClientByDemographicNo(clientId.toString());
	}
	
	private List<Program> getBedPrograms() {
		List<Program> bedPrograms = new ArrayList<Program>();
		Collections.addAll(bedPrograms, programManager.getBedPrograms());
		
		return bedPrograms;
	}

	private List<Program> getServicePrograms() {
		List<Program> servicePrograms = new ArrayList<Program>();
		
		for (Object o : programManager.getServicePrograms()) {
			Program servicePorgram = (Program) o;
			servicePrograms.add(servicePorgram);
		}
		
		return servicePrograms;
	}
	
	private Integer getClientBedProgramId(Integer clientId) {
		Admission bedProgramAdmission = admissionManager.getCurrentBedProgramAdmission(clientId);
		return (bedProgramAdmission != null) ? bedProgramAdmission.getProgramId() : null;
	}
	
	
	private SortedSet<Integer> getClientServiceProgramIds(Integer clientId) {
		SortedSet<Integer> clientServiceProgramIds = new TreeSet<Integer>();
		
		List admissions = admissionManager.getCurrentServiceProgramAdmission(clientId);
		if (admissions != null) {
			for (Object o : admissions) {
				Admission servicePorgramAdmission = (Admission) o;
				clientServiceProgramIds.add(servicePorgramAdmission.getProgramId());
			}
		}
		
		return clientServiceProgramIds;
	}

	private void saveClient(Demographic client) throws IntegratorException {
		// local save
		clientManager.saveClient(client);
	
		// remote save
		try {
			integratorManager.saveClient(client);
			
			if (client.getAgencyId() != integratorManager.getLocalAgencyId() && client.getAgencyId() != 0) {
				integratorManager.mergeClient(client, client.getAgencyId(), client.getDemographicNo());
			}
		} catch (IntegratorException e) {
			LOG.error(e);
		}
	}

	private void admitBedProgram(Integer clientId, String providerNo, Integer bedProgramId) throws ProgramFullException, AdmissionException {
		Program bedProgram = null;
		Integer clientBedProgramId = getClientBedProgramId(clientId);
		
		if (bedProgramId == null && clientBedProgramId == null) {
			bedProgram = programManager.getHoldingTankProgram();
		} else if (bedProgramId != null) {
			bedProgram =  programManager.getProgram(bedProgramId);
		}
		
		if (bedProgram != null && (clientBedProgramId == null || !clientBedProgramId.equals(bedProgramId))) {
			admissionManager.processAdmission(clientId, providerNo, bedProgram, "intake discharge", "intake admit");
		}
	}

	private void admitServicePrograms(Integer clientId, String providerNo, List<Integer> serviceProgramIds) throws ProgramFullException, AdmissionException {
		SortedSet<Integer> clientServicePrograms = getClientServiceProgramIds(clientId);
		
		for (Integer serviceProgramId : serviceProgramIds) {
			if (!clientServicePrograms.contains(serviceProgramId)) {
				Program serviceProgram =  programManager.getProgram(serviceProgramId);
				admissionManager.processAdmission(clientId, providerNo, serviceProgram, "intake discharge", "intake admit");
			}
		}
	}

	private void saveInstance(IntakeInstance instance, Integer clientId) {
		instance.setClientId(clientId);
		genericIntakeManager.saveInstance(instance);
	}

}