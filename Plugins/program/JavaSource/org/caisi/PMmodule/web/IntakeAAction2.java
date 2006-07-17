package org.caisi.PMmodule.web;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.LazyValidatorForm;
import org.caisi.PMmodule.exception.ProgramFullException;
import org.caisi.PMmodule.model.Demographic;
import org.caisi.PMmodule.model.Formintakea;
import org.caisi.PMmodule.model.Program;
import org.caisi.PMmodule.model.ProgramProvider;
import org.caisi.PMmodule.model.Provider;
import org.caisi.PMmodule.service.AdmissionManager;
import org.caisi.PMmodule.service.ClientManager;
import org.caisi.PMmodule.service.IntakeAManager;
import org.caisi.PMmodule.service.LogManager;
import org.caisi.PMmodule.service.ProgramManager;
import org.caisi.PMmodule.service.ProviderManager;
import org.caisi.PMmodule.web.utils.IntakeAUtils;

public class IntakeAAction2 extends DispatchAction {
	private static Log log = LogFactory.getLog(IntakeAAction2.class);

	private ProgramManager programManager;
	private AdmissionManager admissionManager;
	private ClientManager clientManager;
	private IntakeAManager intakeAManager;
	private ProviderManager providerManager;
	private LogManager logManager;
	
	public void setProgramManager(ProgramManager mgr) {
		this.programManager = mgr;
	}

	public void setAdmissionManager(AdmissionManager mgr) {
		this.admissionManager = mgr;
	}
	
	public void setClientManager(ClientManager mgr) {
		this.clientManager = mgr;
	}
	
	public void setIntakeAManager(IntakeAManager mgr) {
		this.intakeAManager = mgr;
	}
	
	public void setProviderManager(ProviderManager mgr) {
		this.providerManager = mgr;
	}
	
	public void setLogManager(LogManager mgr) {
		this.logManager = mgr;
	}
	
	public String getProviderNo(HttpServletRequest request) {
		Provider p =  (Provider)request.getSession().getAttribute("provider");
		return p.getProviderNo();
	}
	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return form(mapping,form,request,response);
	}
	
	public ActionForward form(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String demographicNo = request.getParameter("demographicNo");
		Formintakea intakeForm = intakeAManager.getCurrIntakeAByDemographicNo(demographicNo);
		boolean view=(intakeForm != null);
		
		//view form, provider
		Demographic client  = clientManager.getClientByDemographicNo(demographicNo);
		
		if(view) {
			if(client == null) {
				ActionMessages messages = new ActionMessages();
				messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("intake.invalid_client"));
				saveMessages(request,messages);			
				return mapping.findForward("success");
			}
			
  			if(intakeForm != null) {
  				//update DOB
  				IntakeAUtils.updateBirthDateOfIntakeAFromDemographicIfDifferent(intakeForm,client);
  			}
  			request.setAttribute("viewIntakeA","Y");
  			request.setAttribute("intakeAClientInfo",intakeForm);
  	    	
		} else {
			intakeForm = intakeAManager.setNewIntakeAObj(new Formintakea());
			//set client name, dob, and HC#
			if(client != null) {
				IntakeAUtils.setDateOfBirthForIntakeA(intakeForm,client);
				IntakeAUtils.setHealthCardInfoForIntakeA(intakeForm,client);
				intakeForm.setClientFirstName(client.getFirstName());
				intakeForm.setClientSurname(client.getLastName());
			}
   			
			List origProgramDomain = providerManager.getProgramDomain(getProviderNo(request));
			List programDomain = new ArrayList();
			for(Iterator iter=origProgramDomain.iterator();iter.hasNext();) {
				ProgramProvider pp = (ProgramProvider)iter.next();
				Program p = programManager.getProgram(String.valueOf(pp.getProgramId()));
				boolean add = true;
				if(!p.getType().equalsIgnoreCase("bed")) {
					add=false;
				}
				if(p.getNumOfMembers().intValue() >= p.getMaxAllowed().intValue()) {
					add=false;
				}
				//pp.setProgramName(p.getName());
				if(add) {
					programDomain.add(p);
				}
			}
			request.setAttribute("programDomain",programDomain);
   			request.setAttribute("viewIntakeA","N");
			request.setAttribute("newIntakeA",intakeForm);
		}

		logManager.log(getProviderNo(request),"read","intakea",demographicNo,request.getRemoteAddr());
	   	request.setAttribute("demographicNo",demographicNo);
		return mapping.findForward("form");
	}

	/*
	 * has had an intakeA done before
	 * 
	 * save intakeA to db
	 * update sharing value (demographicPMM table)
	 * update dob (demographic table)
	 * update HC#
	 * update Doctor Name 
	 */
	public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		LazyValidatorForm dynaForm = (LazyValidatorForm) form;
		Formintakea intakeA = null;
		String demographicNo = request.getParameter("demographic_no");
		
		intakeA = IntakeAUtils.setIntakeA(new Formintakea(), dynaForm, "update");
		Demographic client  = clientManager.getClientByDemographicNo(demographicNo);
		
		if(intakeA == null) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("IntakeACommitAction.message.failedToAddClientToIntakeA"));
			saveMessages(request,messages);
			return mapping.findForward("error");
		}
		
		intakeAManager.saveUpdatedIntakeA(client,getProviderNo(request),intakeA);
		
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("intake.saved"));
		saveMessages(request,messages);
		
		request.setAttribute("demographicNo",demographicNo);
		logManager.log(getProviderNo(request),"write","intakea",demographicNo,request.getRemoteAddr());
	   	
		return mapping.findForward("success");
	}
	
	/*
	 * new intakeA form (shouldn't have been any done in the past)
	 * possibly need to create demographic record
	 * should be a single transaction to create intakeA & demographic records
	 */
	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		LazyValidatorForm dynaForm = (LazyValidatorForm) form;
		Formintakea intakeA = null;
	    
		intakeA = IntakeAUtils.setIntakeA(new Formintakea(), dynaForm, "save");
		
		if(intakeA == null) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("IntakeACommitAction.message.failedToAddClientToIntakeA"));
			saveMessages(request,messages);
			return mapping.findForward("error");
		}
		
		//determine if they are in demographic table
		String demographicNo = request.getParameter("demographicNo");
		log.debug("demographicNo = " + demographicNo);
		
		if(demographicNo != null && intakeAManager.getCurrIntakeAByDemographicNo(demographicNo) != null) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("IntakeACommitAction.message.failedToAddClientToIntakeA"));
			saveMessages(request,messages);
			return mapping.findForward("error");
		}
	
		//pass off to manager to guarantee transaction
		demographicNo = intakeAManager.saveNewIntakeA(demographicNo,getProviderNo(request),intakeA);

		//see if we can admit them
		long admissionProgramId = Long.valueOf(request.getParameter("admissionProgram")).longValue();
		log.debug(String.valueOf(admissionProgramId));
		
		Program admissionProgram = null;
		
		if(admissionProgramId == 0) {
			//holding tank!
			admissionProgram = programManager.getHoldingTankProgram();
		} else {
			admissionProgram = programManager.getProgram(String.valueOf(admissionProgramId));
		}
		
		if(admissionProgram != null) {
			try {
				admissionManager.processInitialAdmission(demographicNo,getProviderNo(request),admissionProgram,"initial admission");
			}catch(ProgramFullException e) {
				log.warn(e);
			}
		} else {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("intake.no_admission"));
			saveMessages(request,messages);			
		}
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("intake.success"));
		saveMessages(request,messages);

		logManager.log(getProviderNo(request),"read","intakea",demographicNo,request.getRemoteAddr());
	   	
		request.setAttribute("demographicNo",demographicNo);
		return mapping.findForward("success");
	}

   
}
