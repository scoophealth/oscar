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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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
import org.oscarehr.PMmodule.model.Formintakea;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.PMmodule.service.AdmissionManager;
import org.oscarehr.PMmodule.service.ClientManager;
import org.oscarehr.PMmodule.service.IntakeAManager;
import org.oscarehr.PMmodule.service.LogManager;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.PMmodule.service.ProviderManager;
import org.oscarehr.PMmodule.web.formbean.IntakeAFormBean;
import org.oscarehr.PMmodule.web.formbean.IntakeFormBean;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.util.MiscUtils;

public class IntakeAAction2 extends BaseAction {
    private static Logger log = MiscUtils.getLogger();
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
    private AdmissionManager admissionManager;
    private ClientManager clientManager;
    private IntakeAManager intakeAManager;
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


    public ActionForward form(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm intakeForm = (DynaActionForm) form;
        IntakeFormBean intakeFormBean = (IntakeFormBean) intakeForm.get("bean");

        log.debug("loading Intake A");

        intakeForm.set("intake", new Formintakea());

        String demographicNo = request.getParameter("demographicNo");
        if (demographicNo == null) {
            demographicNo = (String) request.getAttribute("demographicNo");
        }

        Formintakea intakeAForm = null;
        if (demographicNo != null) {
            intakeAForm = intakeAManager.getCurrIntakeAByDemographicNo(demographicNo);
        }
        boolean update = (intakeAForm != null);
        if (!update) {
            intakeAForm = new Formintakea();
        }

        Demographic client = clientManager.getClientByDemographicNo(demographicNo);
        if (client != null) {
            this.updateForm(intakeAForm, client);
        }

        //this is the one set when the integrator/local imported a match
        Demographic demographic = (Demographic) request.getSession().getAttribute("demographic");
        if (demographic != null) {
            log.debug("have demographic information from integrator/local");
            intakeFormBean.setDemographicId(demographic.getDemographicNo().longValue());
            //request.getSession().setAttribute("demographic",null);
        }

        if (update) {
            intakeAForm.setDemographicNo(Long.valueOf(demographicNo));

            if (client == null) {
                ActionMessages messages = new ActionMessages();
                messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("intake.invalid_client"));
                saveMessages(request, messages);
                return mapping.findForward("success");
            }
        } else {
            //intakeAForm = new Formintakea();
            intakeAForm.setAssessDate(formatter.format(new Date()));
            //set client name
            if (client != null) {
                intakeAForm.setDemographicNo(Long.valueOf(demographicNo));
                intakeAForm.setClientFirstName(client.getFirstName());
                intakeAForm.setClientSurname(client.getLastName());
                this.updateForm(intakeAForm, client);
            }
            if (demographic != null) {
                intakeAForm.setClientFirstName(demographic.getFirstName());
                intakeAForm.setClientSurname(demographic.getLastName());
                intakeAForm.setDay(demographic.getDateOfBirth());
                intakeAForm.setMonth(demographic.getMonthOfBirth());
                intakeAForm.setYear(demographic.getYearOfBirth());

                if (demographic.getSex() != null) {
                    if (demographic.getSex().equalsIgnoreCase("M")) {
                        intakeAForm.setRadioSex("male");
                    } else if (demographic.getSex().equalsIgnoreCase("F")) {
                        intakeAForm.setRadioSex("female");
                    } else if (demographic.getSex().equalsIgnoreCase("T")) {
                        intakeAForm.setRadioSex("transgendered");
                    }
                }
                if (demographic.getHin() != null && demographic.getHin().length() > 0) {
                    intakeAForm.setCboxHealthcard("Y");
                    intakeAForm.setHealthCardNum(demographic.getHin());
                    intakeAForm.setHealthCardVer(demographic.getVer());
                    if (demographic.getEffDate() != null) {
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        intakeAForm.setEffDate(formatter.format(demographic.getEffDate()));
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

        intakeAForm.setProviderNo(Long.valueOf(getProviderNo(request)));
        intakeForm.set("intake", intakeAForm);

        List origProgramDomain = providerManager.getProgramDomain(getProviderNo(request));
        request.setAttribute("programDomainBed", this.getProgramDomain_Bed(origProgramDomain));
        request.setAttribute("programDomainService", this.getProgramDomain_Service(origProgramDomain));

        logManager.log("read", "intakea", demographicNo, request);
        request.setAttribute("demographicNo", demographicNo);
        return mapping.findForward("form");
    }


    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm intakeForm = (DynaActionForm) form;
        IntakeAFormBean formBean = (IntakeAFormBean) intakeForm.get("view2");
        //IntakeFormBean intakeFormBean = (IntakeFormBean) intakeForm.get("bean");

        Formintakea intakea = (Formintakea) intakeForm.get("intake");
        //String demographicNo = request.getParameter("demographicNo");

        boolean update = false;

        if (intakea.getDemographicNo() != null && intakea.getDemographicNo().longValue() > 0) {
            update = true;
        }

        intakea.setProviderNo(Long.valueOf(this.getProviderNo(request)));
        intakeAManager.saveNewIntake(intakea);

        if (update) {
            updateClientInfo(intakea, String.valueOf(intakea.getDemographicNo()));
        }

//		see if we can admit them
        if (!update) {
            long admissionProgramId = formBean.getAdmissionProgram();
            log.debug(String.valueOf(admissionProgramId));

            Program admissionProgram = null;

            if (admissionProgramId == 0) {
                //holding tank!
                if (admissionManager.getCurrentBedProgramAdmission(intakea.getDemographicNo().intValue()) == null) {
                    admissionProgram = programManager.getHoldingTankProgram();
                }
            } else {
                admissionProgram = programManager.getProgram(String.valueOf(admissionProgramId));
            }

            if (admissionProgram != null) {
                try {
                    admissionManager.processInitialAdmission(intakea.getDemographicNo().intValue(), getProviderNo(request), admissionProgram, "initial admission", null);
                } catch (Exception e) {
                    log.warn("warn", e);
                }
            } else {
                ActionMessages messages = new ActionMessages();
                messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("intake.no_admission"));
                saveMessages(request, messages);
            }
        }

        if (update) {
            long admissionProgramId = formBean.getAdmissionProgram();

            if (admissionProgramId != 0) {
                Admission commAdmission = admissionManager.getCurrentCommunityProgramAdmission(intakea.getDemographicNo().intValue());
                Program admissionProgram = programManager.getProgram(String.valueOf(admissionProgramId));

                if (commAdmission != null && admissionProgram != null && !admissionProgram.isFull()) {
                    try {
                        admissionManager.processAdmission(intakea.getDemographicNo().intValue(), getProviderNo(request), admissionProgram, null, "Intake Based Admission");
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
            for (int x = 0; x < servicePrograms.length; x++) {
                Program serviceProgram = programManager.getProgram(servicePrograms[x]);
                if (serviceProgram != null) {
                    try {
                        admissionManager.processInitialAdmission(intakea.getDemographicNo().intValue(), getProviderNo(request), serviceProgram, "initial admission", null);
                    } catch (Exception e) {
                        log.warn("warn", e);
                    }
                }
            }
        }
        request.setAttribute("demographicNo", String.valueOf(intakea.getDemographicNo()));
        intakeForm.reset(mapping, request);
        intakeForm.set("view2", new IntakeAFormBean());

        // Intake A saved exposed to Oscar-Triggers as event
        // begin-event-code
//        OscarCaisiEvent ev = new OscarCaisiEvent("pmm.intakea.saved", intakea);
//        ev.getOscarCaisiContext().setProviderID(this.getProviderNo(request));
//        ev.getOscarCaisiContext().setClientID(intakea.getDemographicNo().toString());
//        ev.addBean("form", intakea);
//        WebApplicationContextUtils.getWebApplicationContext(getServlet().getServletContext()).publishEvent(ev);
        // end-event-code

        request.getSession().setAttribute("demographic", null);

        return mapping.findForward("success");
    }

    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaActionForm intakeForm = (DynaActionForm) form;
        Formintakea intakea = (Formintakea) intakeForm.get("intake");
        boolean update = false;
        if (intakea.getId() != null && intakea.getId().longValue() > 0) {
            update = true;
        }
        Long demographicNo = intakea.getDemographicNo();
        request.setAttribute("demographicNo", demographicNo);
        intakeForm.reset(mapping, request);
        if (update) {
            return mapping.findForward("cancel-update");
        } else {
            //return mapping.findForward("cancel-new");
            return new ActionForward("/PMmodule/ProviderInfo.do?method=view");
        }
    }

    protected List<Program> getProgramDomain_Bed(List origProgramDomain) {
        List<Program> programDomain = new ArrayList<Program>();

        for (Iterator iter = origProgramDomain.iterator(); iter.hasNext();) {
            ProgramProvider pp = (ProgramProvider) iter.next();
            Program p = programManager.getProgram(String.valueOf(pp.getProgramId()));
            boolean add = true;
            if (!p.getType().equalsIgnoreCase("bed")) {
                add = false;
            }
            if (p.getNumOfMembers().intValue() >= p.getMaxAllowed().intValue()) {
                add = false;
            }
            //pp.setProgramName(p.getName());
            if (add) {
                programDomain.add(p);
            }
        }
        return programDomain;
    }

    protected List<Program> getProgramDomain_Service(List origProgramDomain) {
        List<Program> programDomain = new ArrayList<Program>();

        for (Iterator iter = origProgramDomain.iterator(); iter.hasNext();) {
            ProgramProvider pp = (ProgramProvider) iter.next();
            Program p = programManager.getProgram(String.valueOf(pp.getProgramId()));
            boolean add = true;
            if (!p.getType().equalsIgnoreCase("service")) {
                add = false;
            }
            if (p.getNumOfMembers().intValue() >= p.getMaxAllowed().intValue()) {
                add = false;
            }
            //pp.setProgramName(p.getName());
            if (add) {
                programDomain.add(p);
            }
        }
        return programDomain;
    }

    protected void updateClientInfo(Formintakea intake, String demographicNo) {
        Demographic client = clientManager.getClientByDemographicNo(demographicNo);
        client.setFirstName(intake.getClientFirstName());
        client.setLastName(intake.getClientSurname());

        if (intake.getYear() != null && intake.getYear().length() > 0) {
            client.setYearOfBirth(intake.getYear());
        }
        if (intake.getMonth() != null && intake.getMonth().length() > 0) {
            client.setMonthOfBirth(intake.getMonth());
        }
        if (intake.getDay() != null && intake.getDay().length() > 0) {
            client.setDateOfBirth(intake.getDay());
        }

        if (intake.getHealthCardNum() != null && intake.getHealthCardNum().length() > 0) {
            client.setHin(intake.getHealthCardNum());
        }

        if (intake.getHealthCardVer() != null && intake.getHealthCardVer().length() > 0) {
            client.setVer(intake.getHealthCardVer());
        }

        if (intake.getRadioSex() != null && intake.getRadioSex().length() > 0) {
            client.setSex(intake.getRadioSex().toUpperCase().substring(0, 1));
        }

        clientManager.saveClient(client);
    }

    protected void updateForm(Formintakea intake, Demographic client) {
        if (client.getYearOfBirth() != null && client.getYearOfBirth().length() > 0) {
            intake.setYear(client.getYearOfBirth());
        }
        if (client.getMonthOfBirth() != null && client.getMonthOfBirth().length() > 0) {
            intake.setMonth(client.getMonthOfBirth());
        }
        if (client.getDateOfBirth() != null && client.getDateOfBirth().length() > 0) {
            intake.setDay(client.getDateOfBirth());
        }

        if (client.getHin() != null && client.getHin().length() > 0) {
            intake.setHealthCardNum(client.getHin());
        }
        if (client.getVer() != null && client.getVer().length() > 0) {
            intake.setHealthCardVer(client.getVer());
        }
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

    public void setIntakeAManager(IntakeAManager mgr) {
    	this.intakeAManager = mgr;
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
