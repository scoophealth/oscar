/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package org.oscarehr.common.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.text.CaseUtils;
import org.apache.cxf.common.util.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.dao.ProgramProviderDAO;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.DemographicExtDao;
import org.oscarehr.common.dao.OscarAppointmentDao;
import org.oscarehr.common.dao.ScheduleDateDao;
import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.DemographicExt;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.ScheduleDate;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.managers.AppointmentManager;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SessionConstants;
import org.oscarehr.util.SpringUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class GroupAppointmentAction extends DispatchAction {

	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	OscarAppointmentDao appointmentDao = SpringUtils.getBean(OscarAppointmentDao.class);
	ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
	AppointmentManager apptManager = SpringUtils.getBean(AppointmentManager.class);
	ScheduleDateDao scheduleDateDao = SpringUtils.getBean(ScheduleDateDao.class);
	DemographicExtDao demographicExtDao = SpringUtils.getBean(DemographicExtDao.class);
	CaseManagementManager cmManager = SpringUtils.getBean(CaseManagementManager.class);
	ProgramProviderDAO programProviderDao = SpringUtils.getBean(ProgramProviderDAO.class);
	UserPropertyDAO userPropertyDao = SpringUtils.getBean(UserPropertyDAO.class);
	
	public ActionForward getParticipantsForSession(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject response1 = new JSONObject();
		SimpleDateFormat formatter =new SimpleDateFormat("yyyy-MM-dd");

		String providerNo = getRequiredParameter(request, "providerNo", response1);
		String date = getRequiredParameter(request,"date",response1);
		
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_appointment", "r", null)) {
        	response1.put("error","missing required security object (_appointment)");
        }
		
		Date d = this.parseDate(date, formatter, response1);
		
		
		if(response1.get("error") == null) {
			List<Appointment> currentAppts = appointmentDao.findByDateAndProvider(d, providerNo);
			
			Collections.sort(currentAppts, new Comparator<Appointment>() {
				public int compare(Appointment a1, Appointment a2) {
					return a1.getName().compareTo(a2.getName());
				}
			});
			
			//load trackers//loop dates
			List<UserProperty> trackers = new ArrayList<UserProperty>();
			UserProperty numTrackersUp = userPropertyDao.getProp(providerNo, "series_num_trackers");
			if(numTrackersUp != null) {
				int numTrackers = Integer.parseInt(numTrackersUp.getValue());
				for(int x=1;x<=numTrackers;x++) {
					UserProperty tmp = userPropertyDao.getProp(providerNo,"series_tracker" + x);
					if(tmp != null) {
						trackers.add(tmp);
					}
				}
			}
			
			JSONArray arr = new JSONArray();
			
			for(Appointment appt: currentAppts) {
				JSONObject a = new JSONObject();
				a.put("appointmentNo", appt.getId());
				a.put("name", appt.getName());
				a.put("status", appt.getStatus());
				
				for(UserProperty tracker:trackers) {
					String ccName = CaseUtils.toCamelCase(tracker.getValue().split("\\|")[0],false);	
					a.put(ccName, loadAttribute(appt,providerNo,date,ccName));
				}
				
				arr.add(a);
			}
			response1.put("appointments", arr);
		}
		
		response1.write(response.getWriter());
		
		return null;
	}
	
	public ActionForward saveParticipantAttributes(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject response1 = new JSONObject();
		String appointmentNo = getRequiredParameter(request, "appointmentNo", response1);
		String providerNo = getRequiredParameter(request, "providerNo", response1);
		String date = getRequiredParameter(request,"date",response1);
		
		
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_demographic", "w", null)) {
			response1.put("error","missing required security object (_demographic)");
		}
		
		Appointment a = appointmentDao.find(Integer.parseInt(appointmentNo));
		if(a == null) {
			response1.put("error","appointment not found");
		}

		if(response1.get("error") == null) {
			List<String> IGNORES = Arrays.asList(new String[] {"appointmentNo","providerNo","date","method"});
			
			Enumeration<String> en =  request.getParameterNames();
			while(en.hasMoreElements()) {
				String key = en.nextElement();
				if(!IGNORES.contains(key)) {
					saveAttributeAsDemographicExt(a,providerNo,date,key,request.getParameter(key));
				}
			}
			
		}
		
		response1.write(response.getWriter());
		
		return null;
	}

	
	public ActionForward updateStatus(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject response1 = new JSONObject();
		String appointmentNo = getRequiredParameter(request, "appointmentNo", response1);
		String status = getRequiredParameter(request, "status", response1);
		
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_appointment", "w", null)) {
			response1.put("error", "missing required security object (_appointment)");
        } 
		
		Appointment a = null;
		if(!StringUtils.isEmpty(appointmentNo) && NumberUtils.isDigits(appointmentNo)) {
			a = appointmentDao.find(Integer.parseInt(appointmentNo));
			if(a == null) {
				response1.put("error", "Invalid appointment");
			}
		}
		
		if(response1.get("error") == null) {
				a.setStatus(status);
				apptManager.updateAppointment(LoggedInInfo.getLoggedInInfoFromSession(request), a);
        }
		
		response1.write(response.getWriter());
		
		return null;
	}
	
	public ActionForward saveGroupNote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  throws Exception {
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		
		JSONObject response1 = new JSONObject();
		String appointmentNos = getRequiredParameter(request, "appointmentNos", response1);
		String note = getRequiredParameter(request, "note", response1);
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-YYYY");
		SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MMM-YYYY HH:mm");
		
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_appointment", "w", null)) {
        	response1.put("error","missing required security object (_appointment)");
        }
		
		if(response1.get("error") == null && appointmentNos != null) {
			for(String an : appointmentNos.split(",")) {
				
				Appointment appt = null;
				if(!StringUtils.isEmpty(an) && NumberUtils.isDigits(an)) {
					appt = appointmentDao.find(Integer.parseInt(an));
					if(appt == null) {
						response1.put("error", "Invalid appointment");
					}
				}
				
				if(appt != null) {
					Date now = new Date();
					
					String noteData = "["+formatter.format(now)+" .: ]\n" + note + "\n" + "[Signed on "+formatter2.format(now)+" by "+ loggedInInfo.getLoggedInProvider().getFormattedName() +"]";
					
					CaseManagementNote cNote = new CaseManagementNote();
					cNote.setUpdate_date(now);
					cNote.setObservation_date(appt.getStartTimeAsFullDate());
					cNote.setDemographic_no(String.valueOf(appt.getDemographicNo()));
					cNote.setProviderNo(loggedInInfo.getLoggedInProviderNo());
					cNote.setNote(noteData);
					cNote.setSigned(true);
					cNote.setIncludeissue(false);
					cNote.setSigning_provider_no(loggedInInfo.getLoggedInProviderNo());
					cNote.setEncounter_type("face to face encounter with client");
					cNote.setBilling_code("");
					cNote.setProgram_no((String)request.getSession().getAttribute(SessionConstants.CURRENT_PROGRAM_ID));
					
					ProgramProvider pp = programProviderDao.getProgramProvider(loggedInInfo.getLoggedInProviderNo(), Long.valueOf(cNote.getProgram_no()));
					if(pp != null) {
						cNote.setReporter_caisi_role(pp.getRoleId().toString());
					}
					cNote.setReporter_program_team("0");
					cNote.setHistory(noteData);
					cNote.setPassword(null);
					cNote.setLocked(false);
					cNote.setArchived(false);
					cNote.setPosition(0);
					cNote.setUuid(UUID.randomUUID().toString());
					cNote.setAppointmentNo(appt.getId());
					cNote.setHourOfEncounterTime(0);
					cNote.setMinuteOfEncounterTime(0);
					cNote.setHourOfEncTransportationTime(null);
					cNote.setMinuteOfEncTransportationTime(null);
					
					cmManager.saveNoteSimple(cNote);
				}
			}
		}
		
		response1.write(response.getWriter());
		
		return null;
	}
	
	public ActionForward addNewSession(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject response1 = new JSONObject();
		String currentDate = getRequiredParameter(request, "currentSession", response1);
		String providerNo = getRequiredParameter(request, "providerNo", response1);
		String date = getRequiredParameter(request,"date",response1);
		
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date dt = this.parseDate(date, formatter, response1);
		Date currentDt = this.parseDate(currentDate,formatter,response1);
		
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_appointment", "w", null)) {
        	response1.put("error","missing required security object (_appointment)");
        }
		
		if(response1.get("error") == null) {
			
			//check if there's a session already on that day. If not add schedule and move appointments
			if(scheduleDateDao.findByProviderNoAndDate(providerNo, dt) == null) {
				ScheduleDate sd = new ScheduleDate();
				sd.setAvailable('1');
				sd.setCreator(loggedInInfo.getLoggedInProvider().getFormattedName());
				sd.setDate(dt);
				sd.setHour("Group Series");
				sd.setPriority('b');
				sd.setProviderNo(providerNo);
				sd.setReason("");
				sd.setStatus('A');
				scheduleDateDao.saveEntity(sd);
				
				
				//get all appointments for today
				for(Appointment a: appointmentDao.findByProviderAndDate(providerNo, currentDt)) {
					Appointment target = new Appointment();
					BeanUtils.copyProperties(target, a);
					target.setId(null);
					target.setAppointmentDate(dt);
					target.setStatus("t");
					target.setReason("");
					target.setNotes("added on " + new Date() + " by " + loggedInInfo.getLoggedInProvider().getFormattedName());
					target.setCreateDateTime(new Date());
					target.setUpdateDateTime(new Date());
					target.setCreator(loggedInInfo.getLoggedInProviderNo());
					apptManager.addAppointment(loggedInInfo, target);
				}
				
				//TODO:copy session level attributes
				addSessionLevelAttribute(providerNo, date, "facilitator", getSessionLevelAttribute(providerNo, currentDate, "facilitator"));
				addSessionLevelAttribute(providerNo, date, "facilitator2", getSessionLevelAttribute(providerNo, currentDate, "facilitator2"));
				addSessionLevelAttribute(providerNo, date, "site", getSessionLevelAttribute(providerNo, currentDate, "site"));
				
				Integer numTopics = getNumTopicsForSession(providerNo,currentDate);
				if(numTopics != null) {
					addSessionLevelAttribute(providerNo,date,"num_topics",String.valueOf(numTopics));
					for(int x=1;x<=numTopics;x++) {
						addSessionLevelAttribute(providerNo,date,"topic"+x, getSessionLevelAttribute(providerNo, currentDate, "topic"+x));
					}
				}
				
			} else  {
				response1.put("error", "There's already a session on that day!");
			}
		}
		
		response1.write(response.getWriter());
		return null;
	}
	
	public ActionForward cancelSession(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject response1 = new JSONObject();
		String date = getRequiredParameter(request, "currentSession", response1);
		String providerNo = getRequiredParameter(request, "providerNo", response1);
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		Date dt = parseDate(date, formatter, response1);
	
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_appointment", "w", null)) {
        	response1.put("error","missing required security object (_appointment)");
        }
		
		if(response1.get("error") == null) {
			ScheduleDate sd = scheduleDateDao.findByProviderNoAndDate(providerNo, dt) ;
			sd.setStatus('D');
			scheduleDateDao.merge(sd);
			
			//get all appointments for today
			for(Appointment a: appointmentDao.findByProviderAndDate(providerNo, dt)) {
				a.setStatus("C");
				a.setReason(a.getReason() + "\n" + "Cancelled Session");
				apptManager.updateAppointment(LoggedInInfo.getLoggedInInfoFromSession(request), a);
			}
			
			//move all session level attributes
			removeSessionLevelAttribute(providerNo,date,"facilitator");
			removeSessionLevelAttribute(providerNo,date,"facilitator2");
			removeSessionLevelAttribute(providerNo,date,"site");
			Integer numTopics = getNumTopicsForSession(providerNo,date);
			removeSessionLevelAttribute(providerNo,date,"num_topics");
			if(numTopics != null) {
				for(int x=1;x<=numTopics;x++) {
					removeSessionLevelAttribute(providerNo,date,"topic"+x);
				}
			}
		}
		
		response1.write(response.getWriter());
		return null;
	}
	
	private void moveSessionLevelAttribute(String providerNo, String date, String attributeName, String newDate) {
		UserPropertyDAO dao = SpringUtils.getBean(UserPropertyDAO.class);
		
		UserProperty up = dao.getProp(providerNo, "session_" + date + "_" + attributeName);
		
		if(up != null) {
			up.setName("session_" + newDate + "_" + attributeName);
			dao.merge(up);
		}
	}
	
	private void removeSessionLevelAttribute(String providerNo, String date, String attributeName) {
		UserPropertyDAO dao = SpringUtils.getBean(UserPropertyDAO.class);
		
		UserProperty up = dao.getProp(providerNo, "session_" + date + "_" + attributeName);
		
		if(up != null) {
			dao.remove(up.getId());
		}
	}
	
	private void copySessionLevelAttribute(String providerNo, String date, String attributeName, String newDate) {
		UserPropertyDAO dao = SpringUtils.getBean(UserPropertyDAO.class);
		
		UserProperty up = dao.getProp(providerNo, "session_" + date + "_" + attributeName);
		
		if(up != null) {
			UserProperty newUp = new UserProperty();
			try {
				BeanUtils.copyProperties(newUp, up);
			}catch(Exception e) {
				MiscUtils.getLogger().error("error",e);
				return;
			}
			newUp.setId(null);
			newUp.setName("session_" + newDate + "_" + attributeName);
			dao.persist(newUp);
		}
	}
	
	private String getSessionLevelAttribute(String providerNo, String date, String attributeName) {
		UserPropertyDAO dao = SpringUtils.getBean(UserPropertyDAO.class);
		
		UserProperty up = dao.getProp(providerNo, "session_" + date + "_" + attributeName);
		
		if(up != null) {
			return up.getValue();
		}
		
		return null;
	}
	
	
	private void addSessionLevelAttribute(String providerNo, String date, String attributeName, String attributeValue) {
		if(attributeValue == null) {
			return;
		}
		
		UserPropertyDAO dao = SpringUtils.getBean(UserPropertyDAO.class);
		
		UserProperty up = new UserProperty();
		up.setName("session_" + date  + "_" + attributeName);
		up.setProviderNo(providerNo);
		up.setValue(attributeValue);
		
		dao.persist(up);
		
	}
	
	
	public ActionForward rescheduleSession(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject response1 = new JSONObject();
		
		String providerNo = getRequiredParameter(request, "providerNo", response1);
		String date = getRequiredParameter(request,"date",response1);
		String currentDate = getRequiredParameter(request,"currentSession",response1);
				
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		Date dt = parseDate(date, formatter, response1);
		Date currentDt = parseDate(currentDate,formatter,response1);
		
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_appointment", "w", null)) {
        	response1.put("error","missing required security object (_appointment)");
        }
		
		if(response1.get("error") == null) {
			//check if there's a session already on that day. If not add schedule and move appointments
			if(scheduleDateDao.findByProviderNoAndDate(providerNo, dt) == null) {
				
				ScheduleDate sd = scheduleDateDao.findByProviderNoAndDate(providerNo, currentDt) ;
				sd.setDate(dt);
				scheduleDateDao.merge(sd);
				
				//get all appointments for today
				for(Appointment a: appointmentDao.findByProviderAndDate(providerNo, currentDt)) {
					a.setAppointmentDate(dt);
					apptManager.updateAppointment(LoggedInInfo.getLoggedInInfoFromSession(request), a);
				}
				
				//move all session level attributes
				moveSessionLevelAttribute(providerNo,currentDate,"facilitator",date);
				moveSessionLevelAttribute(providerNo,currentDate,"facilitator2",date);
				moveSessionLevelAttribute(providerNo,currentDate,"site",date);
				int numTopics = getNumTopicsForSession(providerNo,currentDate);
				moveSessionLevelAttribute(providerNo,currentDate,"num_topics",date);
				for(int x=1;x<=numTopics;x++) {
					moveSessionLevelAttribute(providerNo,currentDate,"topic"+x,date);
				}
			} else  {
				response1.put("error", "There's already a session on that day!");
			}	
		}
		
		response1.write(response.getWriter());
		return null;
	}
	
	private Integer getNumTopicsForSession(String providerNo, String date) {
		UserPropertyDAO dao = SpringUtils.getBean(UserPropertyDAO.class);
		
		UserProperty up = dao.getProp(providerNo, "session_" + date + "_num_topics");
		
		if(up != null) {
			return Integer.parseInt(up.getValue());
		}
		
		return null;
	}
	
	public ActionForward removeFromSeries(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		
		JSONObject response1 = new JSONObject();
		String appointmentNo = getRequiredParameter(request, "appointmentNo", response1);
		String reason = request.getParameter("reason");
		
		Appointment appt = null;
		if(!StringUtils.isEmpty(appointmentNo) && NumberUtils.isDigits(appointmentNo)) {
			appt = appointmentDao.find(Integer.parseInt(appointmentNo));
			if(appt == null) {
				response1.put("error", "Invalid appointment");
			}
		}
		
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_appointment", "w", null)) {
        	response1.put("error","missing required security object (_appointment)");
        }
		
		if(response1.get("error") == null) {
			int demographicNo = appt.getDemographicNo();
			String providerNo = appt.getProviderNo();
			
			//remove patient from all future appointments with this provider..ie CANCEL them
			List<Appointment> apptList = appointmentDao.findFutureAppointmentsWithProvider(demographicNo,providerNo,appt.getAppointmentDate());
			for(Appointment tmp:apptList) {
				tmp.setStatus("C");
				if(!StringUtils.isEmpty(reason)) {
					tmp.setReason(reason);
				}
				apptManager.updateAppointment(loggedInInfo, tmp);
			}
		}
		
		response1.write(response.getWriter());
		return null;
	}
	
	public ActionForward addParticipantToSeries(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject response1 = new JSONObject();
		String demographicNo = getRequiredParameter(request, "demographicNo", response1);
		String providerNo = getRequiredParameter(request, "providerNo", response1);
		String date = getRequiredParameter(request,"date",response1);
		
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);	
		SimpleDateFormat formatter =new SimpleDateFormat("yyyy-MM-dd");
		
		Date d = parseDate(date,formatter,response1);
		
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_appointment", "w", null)) {
        	response1.put("error","missing required security object (_appointment)");
        }
		
		if(response1.get("error") == null) {
			//find all future appts in this series
			List<ScheduleDate> sdList = scheduleDateDao.findActiveByProviderAndHour(providerNo,"Group Series");
			
			for(ScheduleDate sd:sdList) {
				if(sd.getDate().equals(d) ||  sd.getDate().after(d)) {
					Integer apptNo = addParticipantToSession(LoggedInInfo.getLoggedInInfoFromSession(request),Integer.parseInt(demographicNo),providerNo,sd.getDate());
					if(apptNo == null) {
						response1.put("error", "Operation Failed");
					}
				}
			}
			
		}
		response1.write(response.getWriter());
		
		return null;
	}
	
	public ActionForward deleteParticipant(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject response1 = new JSONObject();
		String appointmentNo = getRequiredParameter(request, "appointmentNo", response1);
		String currentSession = getRequiredParameter(request, "currentSession", response1);
		
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);	
		SimpleDateFormat formatter =new SimpleDateFormat("yyyy-MM-dd");
		
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_appointment", "w", null)) {
        	response1.put("error","missing required security object (_appointment)");
        }

		if(response1.get("error") == null) {
			Date d = parseDate(currentSession,formatter,response1);
			
			Appointment appt = appointmentDao.find(Integer.parseInt(appointmentNo));
			if(appt != null) {
				int demographicNo = appt.getDemographicNo();
				String providerNo = appt.getProviderNo();
				
				for(Appointment a :appointmentDao.findByProviderAndDemographic(demographicNo,providerNo)) {
					appointmentDao.remove(a.getId());
				}
				
			}
		}
		
		response1.write(response.getWriter());
		return null;
	}
	
	public ActionForward transferParticipants(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject response1 = new JSONObject();
		String apptListP = getRequiredParameter(request, "participants", response1);
		String providerNo = getRequiredParameter(request, "providerNo", response1);
		String date = getRequiredParameter(request,"currentSession",response1);
		String destinationProvider = getRequiredParameter(request, "toProviderNo", response1);
		
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);	
		SimpleDateFormat formatter =new SimpleDateFormat("yyyy-MM-dd");
		
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_appointment", "w", null)) {
        	response1.put("error","missing required security object (_appointment)");
        }

		Date d = parseDate(date,formatter,response1);
		
		Provider currentProvider = providerDao.getProvider(providerNo);
		
		String reason = "Transfer from " + currentProvider.getFormattedName() + " on " + formatter.format(new Date());
		
		if(response1.get("error") == null) {
			for(String apptNoP : apptListP.split(",")) {
				Appointment appt = appointmentDao.find(Integer.parseInt(apptNoP));
				int demographicNo =appt.getDemographicNo(); 
				
				//remove from the current provider - ie..cancel future appts
				//remove patient from all future appointments with this provider..ie CANCEL them
				List<Appointment> apptList = appointmentDao.findFutureAppointmentsWithProvider(demographicNo,providerNo,d);
				for(Appointment tmp:apptList) {
					tmp.setStatus("C");
					if(!StringUtils.isEmpty(reason)) {
						tmp.setReason(reason);
					}
					apptManager.updateAppointment(loggedInInfo, tmp);
				}
				
				//add to new provider - new appts from today onward
				List<ScheduleDate> sdList = scheduleDateDao.findActiveByProviderAndHour(destinationProvider,"Group Series");
				
				for(ScheduleDate sd:sdList) {
					if(sd.getDate().equals(d) ||  sd.getDate().after(d)) {
						Integer apptNo = addParticipantToSession(LoggedInInfo.getLoggedInInfoFromSession(request),demographicNo,destinationProvider,sd.getDate());
						if(apptNo == null) {
							response1.put("error", "Operation Failed");
						}
					}
				}	
			}
		}
		
		response1.write(response.getWriter());
		return null;
	}
	
	public ActionForward addParticipantToSession(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject response1 = new JSONObject();
		String demographicNo = getRequiredParameter(request, "demographicNo", response1);
		String providerNo = getRequiredParameter(request, "providerNo", response1);
		String date = getRequiredParameter(request,"date",response1);
		
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);	
		SimpleDateFormat formatter =new SimpleDateFormat("yyyy-MM-dd");
		
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_appointment", "w", null)) {
        	response1.put("error","missing required security object (_appointment)");
        }

		Date d = parseDate(date,formatter,response1);
		
		if(response1.get("error") == null) {
			Integer apptNo = addParticipantToSession(loggedInInfo,Integer.parseInt(demographicNo),providerNo,d);
			
			if(apptNo == null) {
				response1.put("error","appointment not created");
			}	
		}
		
		response1.write(response.getWriter());
		return null;
	}
	
	protected Integer addParticipantToSession(LoggedInInfo loggedInInfo, int demographicNo, String providerNo, Date date) {
	
		List<Appointment> currentAppts = appointmentDao.findByDateAndProvider(date, providerNo);
		
		//is already added?
		for(Appointment a:currentAppts) {
			if(a.getDemographicNo() == demographicNo) {
				return null;
			}
		}
		
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 8);
		c.set(Calendar.MINUTE,0);
		c.set(Calendar.SECOND,0);
		
		boolean found=false;
		
		while(c.get(Calendar.HOUR_OF_DAY) < 18) {	
			if(getExistingAppointment(currentAppts,c) == null)  {
				found=true;
				break;
			}
			c.add(Calendar.MINUTE, 15);
		}
		
		if(found) {
			//lets add the appointment here
			Date now = new Date();
			Appointment a = new Appointment();
			a.setAppointmentDate(c.getTime());
			a.setCreateDateTime(now);
			a.setCreator(loggedInInfo.getLoggedInProviderNo());
			a.setDemographicNo(demographicNo);
			a.setStartTime(c.getTime());
			c.add(Calendar.MINUTE, 14);
			a.setEndTime(c.getTime());
			//a.setLastUpdateUser(a.getCreator());
			a.setLocation("");
			a.setName(demographicDao.getDemographicById(demographicNo).getFormattedName());
			a.setNotes("added on " + new Date() + " by " + loggedInInfo.getLoggedInProvider().getFormattedName());
			a.setProviderNo(providerNo);
			a.setReason("");
			a.setRemarks("");
			a.setResources("");
			a.setStatus("t");
			a.setType("");
			a.setUpdateDateTime(now);
			a.setUrgency("");
			a.setReasonCode(17);
			
			appointmentDao.saveEntity(a);
			
			return a.getId();
		} 
		
		return null;
	}

	private void saveAttributeAsDemographicExt(Appointment a,String providerNo, String date, String attributeName, String attributeValue ) {
		String key = "group_" + providerNo + "_" + date + "_" + attributeName;
		
		DemographicExt demographicExt = null;
		
		demographicExt = demographicExtDao.getDemographicExt(a.getDemographicNo(), key);
		
		if(demographicExt == null) {
			demographicExt = new DemographicExt();
		}
		
		demographicExt.setDateCreated(new Date());
		demographicExt.setDemographicNo(a.getDemographicNo());
		demographicExt.setKey(key);
		demographicExt.setProviderNo(providerNo);
		demographicExt.setValue(attributeValue);
		
		demographicExtDao.saveEntity(demographicExt);
	}
	
	private String loadAttribute(Appointment a,String providerNo, String date, String attributeName) {
		
		DemographicExt ext = demographicExtDao.getDemographicExt(a.getDemographicNo(), "group_" + providerNo + "_" + date + "_" + attributeName);
		
		if(ext != null) {
			return ext.getValue();
		}
		return null;
	}
	
	private void moveAttribute(Appointment a,String providerNo, String date, String attributeName, String newDate) {
		
		DemographicExt ext = demographicExtDao.getDemographicExt(a.getDemographicNo(), "group_" + providerNo + "_" + date + "_" + attributeName);
		
		if(ext != null) {
			ext.setKey("group_" + providerNo + "_" + newDate + "_" + attributeName);
			demographicExtDao.merge(ext);
		}
	
	}
	
	private void copyAttribute(Appointment a,String providerNo, String date, String attributeName, String newDate) {
		
		DemographicExt ext = demographicExtDao.getDemographicExt(a.getDemographicNo(), "group_" + providerNo + "_" + date + "_" + attributeName);
		
		if(ext != null) {
			DemographicExt newExt = new DemographicExt();
			try {
				BeanUtils.copyProperties(newExt, ext);
			} catch(Exception e) {
				MiscUtils.getLogger().error("Error",e);
			}
			newExt.setId(null);
			newExt.setKey("group_" + providerNo + "_" + newDate + "_" + attributeName);
			demographicExtDao.saveEntity(newExt);
		}
	}

	
	private Appointment getExistingAppointment(List<Appointment> currentAppts,Calendar dateTime) {
				
		for(Appointment a:currentAppts) {
			Calendar c = Calendar.getInstance();
			c.setTime(a.getStartTime());
			
			if(c.get(Calendar.HOUR_OF_DAY) == dateTime.get(Calendar.HOUR_OF_DAY)  && c.get(Calendar.MINUTE) == dateTime.get(Calendar.MINUTE)) {
				return a;
			}
			
		}
		return null;
		
	}
	
	private Date parseDate(String date, SimpleDateFormat formatter, JSONObject response) {
		if(!StringUtils.isEmpty(date)) {	
			Date d = null;
			try {
				d = formatter.parse(date);
			}catch(ParseException e) {
				response.put("error","Invalid date: " + date);
			}
			return d;
		} else {
			response.put("error","Cannot parse date: " + date);
			return null;
		}
	}
	
	private String getRequiredParameter(HttpServletRequest request, String parameterName, JSONObject response) {
		String val = request.getParameter(parameterName);
		if(StringUtils.isEmpty(val)) {
			response.put("error", "Required parameter missing: " + parameterName);
			return null;
		}
		return val;
	}
}
