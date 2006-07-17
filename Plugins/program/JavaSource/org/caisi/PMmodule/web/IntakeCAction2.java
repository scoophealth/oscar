package org.caisi.PMmodule.web;

import java.text.SimpleDateFormat;
import java.util.Date;

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
import org.caisi.PMmodule.model.Demographic;
import org.caisi.PMmodule.model.Formintakec;
import org.caisi.PMmodule.model.Provider;
import org.caisi.PMmodule.service.AdmissionManager;
import org.caisi.PMmodule.service.ClientManager;
import org.caisi.PMmodule.service.IntakeCManager;
import org.caisi.PMmodule.service.LogManager;
import org.caisi.PMmodule.service.ProgramManager;
import org.caisi.PMmodule.service.ProviderManager;
import org.caisi.PMmodule.web.formbean.IntakeCAddress;
import org.caisi.PMmodule.web.formbean.IntakeCContact;
import org.caisi.PMmodule.web.formbean.IntakeCFormBean;
import org.caisi.PMmodule.web.formbean.IntakeCIdentification;

public class IntakeCAction2 extends DispatchAction {
	private static Log log = LogFactory.getLog(IntakeAAction2.class);
	private  final SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
	private ProgramManager programManager;
	private AdmissionManager admissionManager;
	private ClientManager clientManager;
	private IntakeCManager intakeCManager;
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
	
	public void setIntakeCManager(IntakeCManager mgr) {
		this.intakeCManager = mgr;
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
		DynaActionForm intakeForm = (DynaActionForm)form;
		IntakeCFormBean formBean = (IntakeCFormBean)intakeForm.get("view");
		IntakeCAddress[] addresses = (IntakeCAddress[])intakeForm.get("addresses");
		IntakeCContact[] contacts = (IntakeCContact[])intakeForm.get("contact");
		IntakeCIdentification[] identifications = (IntakeCIdentification[])intakeForm.get("identification");

		
		String demographicNo = request.getParameter("demographicNo");
		Formintakec intakeCForm = intakeCManager.getCurrentForm(demographicNo);
		boolean update=(intakeCForm != null);
		
		//view form, provider
		Demographic client  = clientManager.getClientByDemographicNo(demographicNo);
		
		if(update) {
			if(client == null) {
				ActionMessages messages = new ActionMessages();
				messages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("intake.invalid_client"));
				saveMessages(request,messages);			
				return mapping.findForward("success");
			}  
			intakeCForm.setDemographicNo(Long.valueOf(demographicNo));
			
			//get past addresses - have to parse out field
			String pastAddresses = intakeCForm.getPastAddresses();
			if(pastAddresses != null && pastAddresses.length() > 0) {
				String[] entries = pastAddresses.split("\\$\\$\\$");
				for(int x=0;x<entries.length;x++) {
					String[] fields = entries[x].split("~~~");
					addresses[x].setInfo(fields[0]);
					addresses[x].setStartDate(fields[1]);
					addresses[x].setEndDate(fields[2]);
				}
				formBean.setNumPastAddresses(entries.length);
			}

			String contactsInfo = intakeCForm.getContactsInfo();
			if(contactsInfo != null && contactsInfo.length() > 0) {
				String[] entries = contactsInfo.split("\\$\\$\\$");
				for(int x=0;x<entries.length;x++) {
					String[] fields = entries[x].split("~~~");
					if(fields.length != 0) {
						contacts[x].setAddress(fields[0]);
						contacts[x].setEmail(fields[1]);
						contacts[x].setFax(fields[2]);
						contacts[x].setName(fields[3]);
						contacts[x].setPhone(fields[4]);
						contacts[x].setRelationship(fields[5]);
						contacts[x].setOtherInfo(fields[6]);
					}
				}
				formBean.setNumContacts(entries.length);
			}
			
			String idsInfo = intakeCForm.getIds();
			if(idsInfo != null && idsInfo.length() > 0) {
				String[] entries = idsInfo.split("\\$\\$\\$");
				for(int x=0;x<entries.length;x++) {
					String[] fields = entries[x].split("~~~");
					if(fields.length != 0) {
						identifications[x].setType(fields[0]);
						identifications[x].setNumber(fields[1]);
					}
				}
				formBean.setNumIdentification(entries.length);
			}
		} else {
			intakeCForm = new Formintakec();
			intakeCForm.setAdmissionDate(formatter.format(new Date()));
			//set client name
			if(client != null) {
				intakeCForm.setDemographicNo(Long.valueOf(demographicNo));
				intakeCForm.setClientFirstName(client.getFirstName());
				intakeCForm.setClientSurname(client.getLastName());
				//TODO:set dob
			}
		}
		intakeForm.set("intake",intakeCForm);
		
		logManager.log(getProviderNo(request),"read","intakec",demographicNo,request.getRemoteAddr());
	   	request.setAttribute("demographicNo",demographicNo);
		return mapping.findForward("form");
	}

	public ActionForward refresh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String demographicNo = request.getParameter("demographicNo");
		request.setAttribute("demographicNo",demographicNo);
		return mapping.findForward("form");
	}
		
	public ActionForward add_address(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm intakeForm = (DynaActionForm)form;
		IntakeCFormBean formBean = (IntakeCFormBean)intakeForm.get("view");
		formBean.setNumPastAddresses(formBean.getNumPastAddresses()+1);
		
		return mapping.findForward("form");
	}
	
	public ActionForward add_contact(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm intakeForm = (DynaActionForm)form;
		IntakeCFormBean formBean = (IntakeCFormBean)intakeForm.get("view");
		formBean.setNumContacts(formBean.getNumContacts()+1);
		
		return mapping.findForward("form");
	}
	
	public ActionForward add_identification(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm intakeForm = (DynaActionForm)form;
		IntakeCFormBean formBean = (IntakeCFormBean)intakeForm.get("view");
		formBean.setNumIdentification(formBean.getNumIdentification()+1);
		
		return mapping.findForward("form");
	}
		
	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm intakeForm = (DynaActionForm)form;
		IntakeCFormBean formBean = (IntakeCFormBean)intakeForm.get("view");
		Formintakec intakec = (Formintakec)intakeForm.get("intake");
		IntakeCAddress[] addresses = (IntakeCAddress[])intakeForm.get("addresses");
		IntakeCContact[] contacts = (IntakeCContact[])intakeForm.get("contact");
		IntakeCIdentification[] identifications = (IntakeCIdentification[])intakeForm.get("identification");
		
		//must handle past addresses/contacts/identification
		if(formBean.getNumPastAddresses() > 0) {
			StringBuffer addressesBuffer = new StringBuffer();
			for(int x=0;x<formBean.getNumPastAddresses();x++) {
				IntakeCAddress address = addresses[x];
				addressesBuffer.append(address.getInfo()).append("~~~").append(address.getStartDate()).append("~~~").append(address.getEndDate()).append("$$$");
			}
			intakec.setPastAddresses(addressesBuffer.toString());
		}
		if(formBean.getNumContacts() > 0) {
			StringBuffer contactsBuffer = new StringBuffer();
			for(int x=0;x<formBean.getNumContacts();x++) {
				IntakeCContact contact = contacts[x];
				contactsBuffer.append(contact.getAddress()).append("~~~")
					.append(contact.getEmail()).append("~~~")
					.append(contact.getFax()).append("~~~")
					.append(contact.getName()).append("~~~")
					.append(contact.getPhone()).append("~~~")
					.append(contact.getRelationship()).append("~~~")
					.append(contact.getOtherInfo()).append("$$$");
			}
			intakec.setContactsInfo(contactsBuffer.toString());
		}
		if(formBean.getNumIdentification() > 0) {
			StringBuffer idBuffer = new StringBuffer();
			for(int x=0;x<formBean.getNumIdentification();x++) {
				IntakeCIdentification id = identifications[x];
				idBuffer.append(
						id.getType()).append("~~~")
						.append(id.getNumber()).append("$$$");
				}
			intakec.setIds(idBuffer.toString());
		}
		intakec.setStaffName(providerManager.getProviderName(getProviderNo(request)));		
		intakec.setProviderNo(Long.valueOf(this.getProviderNo(request)));
		intakeCManager.saveNewIntake(intakec);
		request.setAttribute("demographicNo",String.valueOf(intakec.getDemographicNo()));
		intakeForm.reset(mapping,request);
		return mapping.findForward("success");
	}
	
	public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm intakeForm = (DynaActionForm)form;
		Formintakec intakec = (Formintakec)intakeForm.get("intake");
		Long demographicNo = intakec.getDemographicNo();
		request.setAttribute("demographicNo",demographicNo);
		intakeForm.reset(mapping,request);
		return mapping.findForward("success");
	}
}
