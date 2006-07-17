package org.caisi.PMmodule.web;

import java.util.ArrayList;
import java.util.Date;
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
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.caisi.PMmodule.model.Admission;
import org.caisi.PMmodule.model.ClientReferral;
import org.caisi.PMmodule.model.Program;
import org.caisi.PMmodule.model.ProgramProvider;
import org.caisi.PMmodule.model.Provider;
import org.caisi.PMmodule.service.AdmissionManager;
import org.caisi.PMmodule.service.ClientManager;
import org.caisi.PMmodule.service.IntakeAManager;
import org.caisi.PMmodule.service.IntakeCManager;
import org.caisi.PMmodule.service.LogManager;
import org.caisi.PMmodule.service.ProgramManager;
import org.caisi.PMmodule.service.ProviderManager;

public class ClientManagerAction extends DispatchAction {
	private static Log log = LogFactory.getLog(ClientManagerAction.class);

	private ProgramManager programManager;
	private AdmissionManager admissionManager;
	private ClientManager clientManager;
	private ProviderManager providerManager;
	private LogManager logManager;
	private IntakeAManager intakeAManager;
	private IntakeCManager intakeCManager;
	
	
	public void setProgramManager(ProgramManager mgr) {
		this.programManager = mgr;
	}

	public void setAdmissionManager(AdmissionManager mgr) {
		this.admissionManager = mgr;
	}
	
	public void setClientManager(ClientManager mgr) {
		this.clientManager = mgr;
	}
	
	public void setProviderManager(ProviderManager mgr) {
		this.providerManager = mgr;
	}
	
	public void setLogManager(LogManager mgr) {
		this.logManager = mgr;
	}
	
	public void setIntakeAManager(IntakeAManager mgr) {
		this.intakeAManager = mgr;
	}
	
	public void setIntakeCManager(IntakeCManager mgr) {
		this.intakeCManager = mgr;
	}
	
	public String getProviderNo(HttpServletRequest request) {
		Provider p =  (Provider)request.getSession().getAttribute("provider");
		return p.getProviderNo();
	}
	
	public String getIP(HttpServletRequest request) {
		return request.getRemoteAddr();
	}
	
	public void setEditAttributes(HttpServletRequest request, String clientId) {
		request.setAttribute("id",clientId);	
		request.setAttribute("client",clientManager.getClientByDemographicNo(clientId));
		request.setAttribute("admissions",admissionManager.getCurrentAdmissions(clientId));
		request.setAttribute("intakeADate",clientManager.getMostRecentIntakeADate(clientId));
		request.setAttribute("intakeBDate",clientManager.getMostRecentIntakeBDate(clientId));
		request.setAttribute("intakeCDate",clientManager.getMostRecentIntakeCDate(clientId));
		
		log.debug("intake A enabled=" + intakeAManager.getEnabled());
		log.debug("intake C enabled=" + intakeCManager.getEnabled());
		
		request.setAttribute("intakeAEnabled",String.valueOf(intakeAManager.getEnabled()));
		request.setAttribute("intakeCEnabled",String.valueOf(intakeCManager.getEnabled()));
		
		/* admit */
		List programProviders  = providerManager.getProgramDomain(getProviderNo(request));
		List programDomain = new ArrayList();
		for(Iterator iter=programProviders.iterator();iter.hasNext();) {
			ProgramProvider pp = (ProgramProvider)iter.next();
			programDomain.add(programManager.getProgram(String.valueOf(pp.getProgramId())));
		}
		request.setAttribute("programDomain",programDomain);
		/* discharge */
		request.setAttribute("serviceAdmissions",admissionManager.getCurrentServiceProgramAdmission(programManager,clientId));
		
		/* refer */
		request.setAttribute("referrals",clientManager.getActiveReferrals(clientId));
		
		/*history*/
		request.setAttribute("admissionHistory",admissionManager.getAdmissions(clientId));
		request.setAttribute("referralHistory",clientManager.getReferrals(clientId));
		
		List currentAdmissions = admissionManager.getCurrentAdmissions(clientId);
		for(int x=0;x<currentAdmissions.size();x++) {
			Admission admission = (Admission)currentAdmissions.get(x);
			if(isInDomain(admission.getProgramId().longValue(),programProviders)) {
				request.setAttribute("isInProgramDomain",new Boolean(true));
				break;
			}
		}
	}
	
	private boolean isInDomain(long programId,List programDomain) {
		for(int x=0;x<programDomain.size();x++) {
			ProgramProvider p = (ProgramProvider)programDomain.get(x);
			if(p.getProgramId().longValue() == programId) {
				return true;
			}
		}
		return false;
	}
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return edit(mapping,form,request,response);
	}
	
	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return edit(mapping,form,request,response);
	}
	
	public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm clientForm = (DynaActionForm)form;
		String id = request.getParameter("id");
		if(id == null || id.equals("")) {
			Object o = request.getAttribute("demographicNo");
			if(o instanceof String) {
				id = (String)o;
			}
			if(o instanceof Long) {
				id = String.valueOf((Long)o);
			}
		}
		setEditAttributes(request,id);
		//logManager.log(getProviderNo(request),"read","pmm client record",id,getIP(request));
		return mapping.findForward("edit");
	}
	
	public ActionForward search_programs(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm clientForm = (DynaActionForm)form;
		Program criteria = (Program)clientForm.get("program");
		request.setAttribute("programs",programManager.search(criteria));
		return mapping.findForward("search_programs");
	}

	public ActionForward admit_select_program(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm clientForm = (DynaActionForm)form;
		Program p = (Program)clientForm.get("program");
		String id = request.getParameter("id");
		setEditAttributes(request,id);
		
		Program program = programManager.getProgram(String.valueOf(p.getId()));
		/*
		 * If the user is currently enrolled in a bed program,
		 * we must warn the provider that this will also be a discharge
		 */
		if(program.getType().equalsIgnoreCase("bed")) {
			Admission currentAdmission = admissionManager.getCurrentBedProgramAdmission(programManager,id);
			if(currentAdmission != null) {
				log.warn("client already in a bed program..doing a discharge/admit if proceeding");
				request.setAttribute("current_admission",currentAdmission);
				request.setAttribute("current_program",programManager.getProgram(String.valueOf(currentAdmission.getProgramId())));
			}
		}
		request.setAttribute("do_admit",new Boolean(true));
		
		return mapping.findForward("edit");
	}
	
	public ActionForward discharge_select_program(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm clientForm = (DynaActionForm)form;
		Program p = (Program)clientForm.get("program");
		String id = request.getParameter("id");
		setEditAttributes(request,id);
		
		Program program = programManager.getProgram(String.valueOf(p.getId()));

		request.setAttribute("do_discharge",new Boolean(true));
		
		return mapping.findForward("edit");
	}
	
	public ActionForward refer_select_program(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm clientForm = (DynaActionForm)form;
		Program p = (Program)clientForm.get("program");
		String id = request.getParameter("id");
		setEditAttributes(request,id);
		
		Program program = programManager.getProgram(String.valueOf(p.getId()));

		request.setAttribute("do_refer",new Boolean(true));
		request.setAttribute("program",program);
		
		return mapping.findForward("edit");
	}

	public ActionForward refer(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm clientForm = (DynaActionForm)form;
		ClientReferral referral = (ClientReferral)clientForm.get("referral");
		Program p = (Program)clientForm.get("program");
		Program program = programManager.getProgram(String.valueOf(p.getId()));
		String id = request.getParameter("id");
		
		referral.setAgencyId(program.getAgencyId());
		referral.setClientId(Long.valueOf(id));
		referral.setProgramId(new Long(program.getId().longValue()));
		referral.setProviderNo(Long.valueOf(getProviderNo(request)));
		referral.setReferralDate(new Date());
		referral.setStatus("active");
		
		clientManager.saveClientReferral(referral);
		
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("refer.success"));
		saveMessages(request,messages);			

		clientForm.set("program",new Program());
		clientForm.set("referral",new ClientReferral());
		setEditAttributes(request,id);
		logManager.log(getProviderNo(request),"write","referral",id,getIP(request));
		
		
		return mapping.findForward("edit");
	}
	
	public ActionForward discharge(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm clientForm = (DynaActionForm)form;
		Admission admission = (Admission)clientForm.get("admission");
		Program p = (Program)clientForm.get("program");
		String id = request.getParameter("id");
		
		Admission fullAdmission = admissionManager.getAdmission(String.valueOf(p.getId()),id);
		if(fullAdmission == null) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("discharge.failure"));
			saveMessages(request,messages);			
			setEditAttributes(request,id);
			return mapping.findForward("edit");
		}
		
		fullAdmission.setDischargeDate(new Date());
		fullAdmission.setDischargeNotes(admission.getDischargeNotes());
		fullAdmission.setAdmissionStatus("discharged");
		
		admissionManager.saveAdmission(fullAdmission);
		
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("discharge.success"));
		saveMessages(request,messages);			

		logManager.log(getProviderNo(request),"write","discharge",id,getIP(request));
		
		setEditAttributes(request,id);
		return mapping.findForward("edit");
	}
	
	public ActionForward admit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm clientForm = (DynaActionForm)form;
		Admission admission = (Admission)clientForm.get("admission");
		Program p = (Program)clientForm.get("program");
		String id = request.getParameter("id");
		
		Program fullProgram = programManager.getProgram(String.valueOf(p.getId()));
		
		if(fullProgram.getType().equalsIgnoreCase("bed")) {
			//do a discharge first
			Admission fullAdmission = admissionManager.getCurrentBedProgramAdmission(programManager,id);
			if(fullAdmission == null) {
				ActionMessages messages = new ActionMessages();
				messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("discharge.failure"));
				saveMessages(request,messages);			
				setEditAttributes(request,id);
				return mapping.findForward("edit");
			}
			
			fullAdmission.setDischargeDate(new Date());
			fullAdmission.setDischargeNotes(admission.getDischargeNotes());
			fullAdmission.setAdmissionStatus("discharged");
			
			admissionManager.saveAdmission(fullAdmission);
			
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("refer.success"));
			saveMessages(request,messages);						
		}
		
		Admission newAdmission = new Admission();
		newAdmission.setAdmissionDate(new Date());
		newAdmission.setAdmissionNotes(admission.getAdmissionNotes());
		newAdmission.setAdmissionStatus("current");
		newAdmission.setClientId(Long.valueOf(id));
		newAdmission.setProgramId(new Long(fullProgram.getId().intValue()));
		newAdmission.setProviderNo(Long.valueOf(getProviderNo(request)));
		newAdmission.setTeamId(new Long(0));
		
		admissionManager.saveAdmission(newAdmission);
		logManager.log(getProviderNo(request),"write","admit",id,getIP(request));
		
		setEditAttributes(request,id);
		return mapping.findForward("edit");
	}
}
