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

package org.oscarehr.PMmodule.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.oscarehr.PMmodule.model.Admission;
import org.oscarehr.PMmodule.model.Formintakec;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.PMmodule.service.AdmissionManager;
import org.oscarehr.PMmodule.service.ClientManager;
import org.oscarehr.PMmodule.service.IntakeCManager;
import org.oscarehr.PMmodule.service.LogManager;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.PMmodule.service.ProviderManager;
import org.oscarehr.PMmodule.web.formbean.IntakeCAddress;
import org.oscarehr.PMmodule.web.formbean.IntakeCContact;
import org.oscarehr.PMmodule.web.formbean.IntakeCFormBean;
import org.oscarehr.PMmodule.web.formbean.IntakeCHospitalization;
import org.oscarehr.PMmodule.web.formbean.IntakeCIdentification;
import org.oscarehr.PMmodule.web.formbean.IntakeFormBean;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.util.MiscUtils;

public class IntakeCAction2 extends BaseAction {

	private static final Logger log = MiscUtils.getLogger();

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd");

    private AdmissionManager admissionManager;
    private ClientManager clientManager;
    private IntakeCManager intakeCManager;
    private LogManager logManager;
    private ProgramManager programManager;
    private ProviderManager providerManager;


	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return form(mapping, form, request, response);
	}

	public ActionForward new_client(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		request.getSession().setAttribute("demographic", null);
		
		return form(mapping, form, request, response);
	}

	public IntakeCAddress[] createAddresses() {
		IntakeCAddress[] value = new IntakeCAddress[20];
		for (int x = 0; x < value.length; x++) {
			value[x] = new IntakeCAddress();
		}
		return value;
	}

	public IntakeCContact[] createContacts() {
		IntakeCContact[] value = new IntakeCContact[20];
		for (int x = 0; x < value.length; x++) {
			value[x] = new IntakeCContact();
		}
		return value;
	}

	public IntakeCIdentification[] createIds() {
		IntakeCIdentification[] value = new IntakeCIdentification[20];
		for (int x = 0; x < value.length; x++) {
			value[x] = new IntakeCIdentification();
		}
		return value;
	}

	public IntakeCHospitalization[] createHospitalizations() {
		IntakeCHospitalization[] value = new IntakeCHospitalization[20];
		for (int x = 0; x < value.length; x++) {
			value[x] = new IntakeCHospitalization();
		}
		return value;
	}

	public ActionForward form(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm intakeForm = (DynaActionForm) form;
		IntakeCFormBean formBean = (IntakeCFormBean) intakeForm.get("view2");
		IntakeFormBean intakeFormBean = (IntakeFormBean) intakeForm.get("bean");

		IntakeCAddress[] addresses = this.createAddresses();
		IntakeCContact[] contacts = this.createContacts();
		IntakeCIdentification[] identifications = this.createIds();
		IntakeCHospitalization[] hospitalizations = this.createHospitalizations();

		intakeForm.set("intake", new Formintakec());
		intakeForm.set("addresses", addresses);
		intakeForm.set("contact", contacts);
		intakeForm.set("identification", identifications);
		intakeForm.set("hospitalization", hospitalizations);
		formBean.setNumContacts(1);
		formBean.setNumIdentification(1);
		formBean.setNumPastAddresses(0);
		formBean.setNumHospitalization(1);

		String demographicNo = request.getParameter("demographicNo");

		if (demographicNo == null) {
			demographicNo = (String) request.getAttribute("demographicNo");
		}
		request.setAttribute("clientId", demographicNo);

		Formintakec intakeCForm = intakeCManager.getCurrentForm(demographicNo);
		boolean update = (intakeCForm != null);
		if (!update) {
			intakeCForm = new Formintakec();
		}

		// view form, provider
		Demographic client = clientManager.getClientByDemographicNo(demographicNo);
		if (client != null) {
			this.updateForm(intakeCForm, client);
		}

		// from using the /Intake action
		Demographic demographic = (Demographic) request.getSession().getAttribute("demographic");
		if (demographic != null) {
			log.debug("have demographic information");
			intakeFormBean.setDemographicId(demographic.getDemographicNo().longValue());
		}

		if (update) {
			if (client == null) {
				ActionMessages messages = new ActionMessages();
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("intake.invalid_client"));
				saveMessages(request, messages);
				return mapping.findForward("success");
			}
			intakeCForm.setDemographicNo(Long.valueOf(demographicNo));

			// get past addresses - have to parse out field
			String pastAddresses = intakeCForm.getPastAddresses();
			if (pastAddresses != null && pastAddresses.length() > 0) {
				String[] entries = pastAddresses.split("\\$\\$\\$");
				for (int x = 0; x < entries.length; x++) {
					String[] fields = entries[x].split("~~~");
					if (fields.length > 0) {
						addresses[x].setInfo(fields[0]);
						addresses[x].setStartDate(fields[1]);
						if (fields.length > 2) {
							addresses[x].setEndDate(fields[2]);
						}
					}
				}
				formBean.setNumPastAddresses(entries.length);
			}

			String contactsInfo = intakeCForm.getContactsInfo();
			if (contactsInfo != null && contactsInfo.length() > 0) {
				String[] entries = contactsInfo.split("\\$\\$\\$");
				for (int x = 0; x < entries.length; x++) {
					String fields[] = entries[x].split("~~~");

					String temp[] = { "*", "*", "*", "*", "*", "*", "*" };
					for (int i = 0; i < fields.length; i++) {
						temp[i] = fields[i];
					}

					fields = temp;

					if (fields.length != 0) {
						contacts[x].setAddress(fields[0]);
						contacts[x].setEmail(fields[1]);
						contacts[x].setFax(fields[2]);
						contacts[x].setName(fields[3]);
						contacts[x].setPhone(fields[4]);
						contacts[x].setRelationship(fields[5]);
						if (fields.length > 6) {
							contacts[x].setOtherInfo(fields[6]);
						}
					}
				}
				formBean.setNumContacts(entries.length);
			}

			String idsInfo = intakeCForm.getIds();
			if (idsInfo != null && idsInfo.length() > 0) {
				String[] entries = idsInfo.split("\\$\\$\\$");
				for (int x = 0; x < entries.length; x++) {
					String[] fields = entries[x].split("~~~");
					if (fields.length != 0 && fields.length==2) {
						identifications[x].setType(fields[0]);
						identifications[x].setNumber(fields[1]);
					}
					else if(fields.length==1){
						identifications[x].setType(fields[0]);
						identifications[x].setNumber("");
					}
				}
				formBean.setNumIdentification(entries.length);
				intakeForm.set("identification", identifications);
			}

			String hospitalizationInfo = intakeCForm.getHospitalizations();
			if (hospitalizationInfo != null && hospitalizationInfo.length() > 0) {
				String[] entries = hospitalizationInfo.split("\\$\\$\\$");
				for (int x = 0; x < entries.length; x++) {
					String[] fields = entries[x].split("~~~");
					if (fields.length != 0) {
						hospitalizations[x].setDate(fields[0]);
						hospitalizations[x].setLength(fields[1]);
						hospitalizations[x].setPsychiatric(Boolean.valueOf(fields[2]).booleanValue());
						hospitalizations[x].setPhysicalHealth(Boolean.valueOf(fields[3]).booleanValue());
						hospitalizations[x].setUnknown(Boolean.valueOf(fields[4]).booleanValue());
					}
				}
				formBean.setNumHospitalization(entries.length);
				intakeForm.set("hospitalization", hospitalizations);
			}
		} else {
			intakeCForm.setAdmissionDate(DATE_FORMAT.format(new Date()));
			// set client name
			if (client != null) {
				intakeCForm.setDemographicNo(Long.valueOf(demographicNo));
				intakeCForm.setClientFirstName(client.getFirstName());
				intakeCForm.setClientSurname(client.getLastName());
				this.updateForm(intakeCForm, client);
			}

			if (demographic != null) {
				intakeCForm.setClientFirstName(demographic.getFirstName());
				intakeCForm.setClientSurname(demographic.getLastName());
				if (demographic.getDateOfBirth().startsWith("0")) {
					intakeCForm.setDayOfBirth(demographic.getDateOfBirth().substring(1));
				} else {
					intakeCForm.setDayOfBirth(demographic.getDateOfBirth());
				}
				if (demographic.getMonthOfBirth().startsWith("0")) {
					intakeCForm.setMonthOfBirth(demographic.getMonthOfBirth().substring(1));
				} else {
					intakeCForm.setMonthOfBirth(demographic.getMonthOfBirth());
				}
				intakeCForm.setYearOfBirth(demographic.getYearOfBirth());
				if (demographic.getSex() != null) {
					if (demographic.getSex().equalsIgnoreCase("M")) {
						intakeCForm.setRadioGender("2");
					} else if (demographic.getSex().equalsIgnoreCase("F")) {
						intakeCForm.setRadioGender("1");
					} else if (demographic.getSex().equalsIgnoreCase("T")) {
						// other
						intakeCForm.setRadioGender("3");
					}
				}
				if (demographic.getHin() != null) {
					if (demographic.getHin() != null && demographic.getHin().length() > 0) {
						IntakeCIdentification id = new IntakeCIdentification();
						if (demographic.getHcType() != null) {
							id.setType("Health Insurance # (" + demographic.getHcType() + ")");
						} else {
							id.setType("Health Insurance # (unknown province)");
						}
						id.setNumber(demographic.getHin() + " " + demographic.getVer());
						identifications[0] = id;
						formBean.setNumIdentification(1);
					}
				}
			}
		}

		if (client != null) {
			Admission a = admissionManager.getCurrentCommunityProgramAdmission(client.getDemographicNo());
			if (a != null) {
				request.setAttribute("in_community_program", new Boolean(true));
			}
		}

		intakeCForm.setStaffName(providerManager.getProviderName(getProviderNo(request)));
		intakeForm.set("intake", intakeCForm);

		setAttributes(request, form);

		logManager.log("read", "intakec", demographicNo, request);
		request.setAttribute("demographicNo", demographicNo);
		
		return mapping.findForward("form");
	}

	public ActionForward refresh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String demographicNo = request.getParameter("demographicNo");
		request.setAttribute("demographicNo", demographicNo);

		setAttributes(request, form);

		return mapping.findForward("form");
	}

	public ActionForward add_address(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm intakeForm = (DynaActionForm) form;
		IntakeCFormBean formBean = (IntakeCFormBean) intakeForm.get("view2");
		formBean.setNumPastAddresses(formBean.getNumPastAddresses() + 1);
		setAttributes(request, form);

		return mapping.findForward("form");
	}

	public ActionForward add_contact(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm intakeForm = (DynaActionForm) form;
		IntakeCFormBean formBean = (IntakeCFormBean) intakeForm.get("view2");
		formBean.setNumContacts(formBean.getNumContacts() + 1);
		setAttributes(request, form);

		return mapping.findForward("form");
	}

	public ActionForward add_identification(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm intakeForm = (DynaActionForm) form;
		IntakeCFormBean formBean = (IntakeCFormBean) intakeForm.get("view2");
		formBean.setNumIdentification(formBean.getNumIdentification() + 1);
		setAttributes(request, form);

		return mapping.findForward("form");
	}

	public ActionForward add_hospitalization(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm intakeForm = (DynaActionForm) form;
		IntakeCFormBean formBean = (IntakeCFormBean) intakeForm.get("view2");
		formBean.setNumHospitalization(formBean.getNumHospitalization() + 1);
		setAttributes(request, form);

		return mapping.findForward("form");
	}

	public ActionForward saveAndClose(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		save(mapping, form, request, response);
		
		DynaActionForm intakeForm = (DynaActionForm) form;
		intakeForm.reset(mapping, request);
		intakeForm.set("view2", new IntakeCFormBean());
		request.getSession().setAttribute("demographic", null);
		
		return mapping.findForward("success");
	}
	
	public ActionForward saveWithoutClose(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		save(mapping, form, request, response);
		
		return refresh(mapping, form, request, response);
	}

	public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm intakeForm = (DynaActionForm) form;
		Formintakec intakec = (Formintakec) intakeForm.get("intake");
		boolean update = false;
		if (intakec.getId() != null && intakec.getId().longValue() > 0) {
			update = true;
		}
		Long demographicNo = intakec.getDemographicNo();
		request.setAttribute("demographicNo", demographicNo);
		intakeForm.reset(mapping, request);
		if (update) {
			return mapping.findForward("cancel-update");
		} else {
			return new ActionForward("/PMmodule/ProviderInfo.do?method=view");
		}
	}

    protected void setAttributes(HttpServletRequest request, ActionForm form) {
    	DynaActionForm intakeForm = (DynaActionForm) form;
    	Formintakec intake = (Formintakec) intakeForm.get("intake");
    
    	List<ProgramProvider> origProgramDomain = providerManager.getProgramDomain(getProviderNo(request));
    	request.setAttribute("programDomainBed", getProgramDomain_Bed(origProgramDomain));
    	request.setAttribute("programDomainService", getProgramDomain_Service(origProgramDomain));
    	request.setAttribute("clientId", intake.getDemographicNo());
    }

	protected List getProgramDomain_Bed(List<ProgramProvider> origProgramDomain) {
		List<Program> programDomain = new ArrayList<Program>();
		
		for (ProgramProvider provider : origProgramDomain) {
			Program program = programManager.getProgram(provider.getProgramId());
			
			if (program.isBed() && program.getNumOfMembers() < program.getMaxAllowed()) {
				programDomain.add(program);
			}
        }
		
		return programDomain;
	}

	protected List getProgramDomain_Service(List<ProgramProvider> origProgramDomain) {
		List<Program> programDomain = new ArrayList<Program>();

		for (ProgramProvider provider : origProgramDomain) {
			Program program = programManager.getProgram(provider.getProgramId());
			
			if (program.isService() && program.getNumOfMembers() < program.getMaxAllowed()) {
				programDomain.add(program);
			}
		}
		
		return programDomain;
	}

	protected void updateClientInfo(Formintakec intake, String demographicNo) {
		Demographic client = clientManager.getClientByDemographicNo(demographicNo);
		
		client.setFirstName(intake.getClientFirstName());
		client.setLastName(intake.getClientSurname());
		
		if (intake.getYearOfBirth() != null && intake.getYearOfBirth().length() > 0) {
			client.setYearOfBirth(intake.getYearOfBirth());
		}
		
		if (intake.getMonthOfBirth() != null && intake.getMonthOfBirth().length() > 0) {
			client.setMonthOfBirth(intake.getMonthOfBirth());
		}
		
		if (intake.getDayOfBirth() != null && intake.getDayOfBirth().length() > 0) {
			client.setDateOfBirth(intake.getDayOfBirth());
		}
		
		clientManager.saveClient(client);
	}

	protected void updateForm(Formintakec intake, Demographic client) {
		if (client.getYearOfBirth() != null && client.getYearOfBirth().length() > 0) {
			intake.setYearOfBirth(client.getYearOfBirth());
		}
		if (client.getMonthOfBirth() != null && client.getMonthOfBirth().length() > 0) {
			intake.setMonthOfBirth(String.valueOf(Integer.parseInt(client.getMonthOfBirth())));
		}
		if (client.getDateOfBirth() != null && client.getDateOfBirth().length() > 0) {
			intake.setDayOfBirth(String.valueOf(Integer.parseInt(client.getDateOfBirth())));
		}
	}
	
	protected void save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm intakeForm = (DynaActionForm) form;
		
		IntakeCFormBean formBean = (IntakeCFormBean) intakeForm.get("view2");
		Formintakec intakec = (Formintakec) intakeForm.get("intake");

		boolean update = (intakec.getId() != null && intakec.getId() > 0);
		
		// handle past addresses
		IntakeCAddress[] addresses = (IntakeCAddress[]) intakeForm.get("addresses");
		
		if (formBean.getNumPastAddresses() > 0) {
			StringBuilder addressesBuffer = new StringBuilder();
			
			for (int i = 0; i < formBean.getNumPastAddresses(); i++) {
				IntakeCAddress address = addresses[i];
				addressesBuffer.append(address.getInfo()).append("~~~").append(address.getStartDate()).append("~~~").append(address.getEndDate()).append("$$$");
			}
			
			intakec.setPastAddresses(addressesBuffer.toString());
		}
		
		// handle past contacts
		IntakeCContact[] contacts = (IntakeCContact[]) intakeForm.get("contact");
		
		if (formBean.getNumContacts() > 0) {
			StringBuilder contactsBuffer = new StringBuilder();
			
			for (int i = 0; i < formBean.getNumContacts(); i++) {
				IntakeCContact contact = contacts[i];
				contactsBuffer.append(contact.getAddress() == "" || contact.getAddress() == null ? "*" : contact.getAddress()).append("~~~").
					append(contact.getEmail() == "" || contact.getEmail() == null ? "*" : contact.getEmail()).append("~~~").
					append(contact.getFax() == "" || contact.getFax() == null ? "*" : contact.getFax()).append("~~~").
					append(contact.getName() == "" || contact.getName() == null ? "*" : contact.getName()).append("~~~").
					append(contact.getPhone() == "" || contact.getPhone() == null ? "*" : contact.getPhone()).append("~~~").
					append(contact.getRelationship() == "" || contact.getRelationship() == null ? "*" : contact.getRelationship()).append("~~~").
					append(contact.getOtherInfo()).append("$$$");
			}
			
			intakec.setContactsInfo(contactsBuffer.toString());
		}
		
		// handle past identifications
		IntakeCIdentification[] identifications = (IntakeCIdentification[]) intakeForm.get("identification");
		
		if (formBean.getNumIdentification() > 0) {
			StringBuilder idBuffer = new StringBuilder();
			
			for (int i = 0; i < formBean.getNumIdentification(); i++) {
				IntakeCIdentification id = identifications[i];
				idBuffer.append(id.getType()).append("~~~").append(id.getNumber()).append("$$$");
			}
			
			intakec.setIds(idBuffer.toString());
		}
		
		// handle past hospitalizations
		IntakeCHospitalization[] hospitalizations = (IntakeCHospitalization[]) intakeForm.get("hospitalization");

		if (formBean.getNumHospitalization() > 0) {
			StringBuilder hospitalizationBuffer = new StringBuilder();
			
			for (int x = 0; x < formBean.getNumHospitalization(); x++) {
				IntakeCHospitalization hospitalization = hospitalizations[x];
				hospitalizationBuffer.append(hospitalization.getDate()).append("~~~").
					append(hospitalization.getLength()).append("~~~").
					append(hospitalization.isPsychiatric()).append("~~~").
					append(hospitalization.isPhysicalHealth()).append("~~~").append(hospitalization.isUnknown()).append("~~~").
					append("$$$");
			}
			
			intakec.setHospitalizations(hospitalizationBuffer.toString());
		}
		
		String providerNo = getProviderNo(request);
		
		intakec.setProviderNo(Long.valueOf(providerNo));
		intakec.setStaffName(providerManager.getProviderName(providerNo));

		intakeCManager.saveNewIntake(intakec);
		
		if (update) {
			updateClientInfo(intakec, intakec.getDemographicNo().toString());
		}

		// more
		
		Date admissionDate = null;
		
		try {
			admissionDate = DATE_FORMAT.parse(intakec.getAdmissionDate());
		} catch (ParseException e) {
			log.warn("Warning", e);
		}

		long admissionProgramId = formBean.getAdmissionProgram();
		log.debug(admissionProgramId);
		
		if (!update) {
			// see if we can admit them
			Program admissionProgram = null;

			if (admissionProgramId == 0) {
				// holding tank!
				if (admissionManager.getCurrentBedProgramAdmission(intakec.getDemographicNo().intValue()) == null) {
					admissionProgram = programManager.getHoldingTankProgram();
				}
			} else {
				admissionProgram = programManager.getProgram(admissionProgramId);
			}

			if (admissionProgram != null) {
				try {
					admissionManager.processInitialAdmission(intakec.getDemographicNo().intValue(), providerNo, admissionProgram, "initial admission", admissionDate);
				} catch (Exception e) {
					log.warn("Warning", e);
				}
			} else {
				ActionMessages messages = new ActionMessages();
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("intake.no_admission"));
				saveMessages(request, messages);
			}
		} else if (update) {
			if (admissionProgramId != 0) {
				Admission commAdmission = admissionManager.getCurrentCommunityProgramAdmission(intakec.getDemographicNo().intValue());
				Program admissionProgram = programManager.getProgram(admissionProgramId);

				if (commAdmission != null && admissionProgram != null && !admissionProgram.isFull()) {
					try {
						admissionManager.processAdmission(intakec.getDemographicNo().intValue(), providerNo, admissionProgram, null, "Intake Based Admission", admissionDate);
					} catch (Exception e) {
						ActionMessages messages = new ActionMessages();
						messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("intake.no_admission"));
						saveMessages(request, messages);
					}
				} else {
					ActionMessages messages = new ActionMessages();
					messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("intake.no_admission"));
					saveMessages(request, messages);
				}
			}
		}

		String[] servicePrograms = request.getParameterValues("admit_service");
		
		if (servicePrograms != null) {
			for (int i = 0; i < servicePrograms.length; i++) {
				Program serviceProgram = programManager.getProgram(servicePrograms[i]);
				
				if (serviceProgram != null) {
					try {
						admissionManager.processInitialAdmission(intakec.getDemographicNo().intValue(), providerNo, serviceProgram, "initial admission", admissionDate);
					} catch (Exception e) {
						log.warn("Warning", e);
					}
				}
			}
		}

		request.setAttribute("demographicNo", String.valueOf(intakec.getDemographicNo()));
	}

	protected void saveClientExtras(int demographicNo, Demographic remoteDemographic) {
		if (remoteDemographic == null) {
			log.warn("Expected demographic session variable to be set!");
			return;
		}

		if (remoteDemographic.getExtras() == null) {
			log.info("no extras found");
			return;
		}

		for (int x = 0; x < remoteDemographic.getExtras().length; x++) {
			clientManager.saveDemographicExt(demographicNo, remoteDemographic.getExtras()[x].getKey(), remoteDemographic.getExtras()[x].getValue());
		}
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

    public void setLogManager(LogManager mgr) {
    	this.logManager = mgr;
    }

    public void setProgramManager(ProgramManager mgr) {
    	this.programManager = mgr;
    }

    public void setProviderManager(ProviderManager mgr) {
    	this.providerManager = mgr;
    }
}
