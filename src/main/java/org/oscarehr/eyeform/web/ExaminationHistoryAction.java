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


package org.oscarehr.eyeform.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.MeasurementDao;
import org.oscarehr.common.dao.OscarAppointmentDao;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.Measurement;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.util.StringUtils;

public class ExaminationHistoryAction extends DispatchAction {

	Logger logger = MiscUtils.getLogger();	
	protected MeasurementDao measurementsDao = SpringUtils.getBean(MeasurementDao.class);
	DemographicDao demographicDao= (DemographicDao)SpringUtils.getBean("demographicDao");
	OscarAppointmentDao appointmentDao = (OscarAppointmentDao)SpringUtils.getBean("oscarAppointmentDao");
	
	
	@Override	   
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {		   
		return display(mapping,form,request,response);   
	}
	 
	public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String demographicNo = request.getParameter("demographicNo");		
		request.setAttribute("demographic",demographicDao.getClientByDemographicNo(Integer.parseInt(demographicNo)));

	
		return mapping.findForward("success");	   
	}
	
	public ActionForward query(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String demographicNo = request.getParameter("demographicNo");				
		String strStartDate = StringUtils.transformNullInEmptyString(request.getParameter("sdate"));
		String strEndDate = StringUtils.transformNullInEmptyString(request.getParameter("edate"));
		String refPage = request.getParameter("refPage");

		request.setAttribute("demographic",demographicDao.getClientByDemographicNo(Integer.parseInt(demographicNo)));
		
		String[] fields = request.getParameterValues("fromlist2");
		List<String> fieldList = new ArrayList<String>();
		if(fields != null) {
			for(String field:fields) {
				fieldList.add(field);
			}
		}
		
		SimpleDateFormat dformate = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = null;
		Date endDate = null;
		
		if(strEndDate.equals("")) {
			Calendar enddate = Calendar.getInstance();						
			strEndDate = dformate.format(enddate.getTime());
			endDate = enddate.getTime();
		}
		
		if(strStartDate.equals("")) {
			List<Appointment> appts = appointmentDao.getAllByDemographicNo(Integer.parseInt(demographicNo));
			if(appts.size()>0) {
				startDate = appts.get(0).getAppointmentDate();
				strStartDate = dformate.format(startDate.getTime());
			} else {
				Calendar enddate = Calendar.getInstance();									
				enddate.roll(Calendar.YEAR, -1);
				strStartDate = dformate.format(enddate.getTime());
				startDate = enddate.getTime();
			}
		}
		if(startDate == null) {
			try {
				startDate = dformate.parse(strStartDate);
			}catch(ParseException e) {
				MiscUtils.getLogger().warn("Warning",e);
			}
		}
		if(endDate == null) {
			try {
				endDate = dformate.parse(strEndDate);
			}catch(ParseException e) {
				MiscUtils.getLogger().warn("Warning",e);
			}
		}
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(endDate);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		endDate = cal.getTime();
						
		//simple fields
		//exclude complex ones ar,k,manifest_refraction,cycloplegic_refraction, angle, EOM
		List<String> simpleFieldNames = new ArrayList<String>();
		for(String f:fieldList) {
			if(f.equals("ar")||f.equals("k")||f.equals("manifest_refraction")||f.equals("cycloplegic_refraction")||f.equals("angle")||f.equals("EOM")) {
				continue;
			}
			simpleFieldNames.add("od_"+f);
			simpleFieldNames.add("os_"+f);
		}
		
		//lets get all the appointments that these fields link to
		List<Appointment> appointments = getAppointments(demographicNo,simpleFieldNames,startDate,endDate);
		
		int numPages = (appointments.size()/5)+1;
		request.setAttribute("numPages", numPages);
		
		//narrow the window using the refpage variable (page number)
		appointments = filterByRefPage(appointments,refPage);
		
		Measurement simpleFields[][] = new Measurement[simpleFieldNames.size()][appointments.size()];				
		for(int x=0;x<simpleFields.length;x++) {
			for(int y=0;y<simpleFields[x].length;y++) {				
				String field =simpleFieldNames.get(x);
				int apptId = appointments.get(y).getId();				
				Measurement m = measurementsDao.findLatestByAppointmentNoAndType(apptId, field);	
				simpleFields[x][y] = m;
			}
		}
		request.setAttribute("simpleFieldNames", simpleFieldNames);
		request.setAttribute("simpleFields", simpleFields);
		request.setAttribute("appointments", appointments);
		if(refPage == null || refPage=="") { refPage="1";}
		request.setAttribute("refPage", Integer.parseInt(refPage));
		
		
		
		
		//complex fields
		//for each appt, get the od and os for AR
		if(fieldList.contains("ar")) {
			//logger.info("figure out AR");
			List<Map<String,String>> ars = new ArrayList<Map<String,String>>();
			List<Appointment> appts = this.getAppointmentsForAr(demographicNo, startDate, endDate);
			for(Appointment appt:appts) {
				Map<String,String> map = new HashMap<String,String>();
				Measurement m = null;
				m = measurementsDao.findLatestByAppointmentNoAndType(appt.getId(),"od_ar_sph");
				if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("od_ar_sph", "");}
				m = measurementsDao.findLatestByAppointmentNoAndType(appt.getId(),"od_ar_cyl");
				if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("od_ar_cyl", "");}
				m = measurementsDao.findLatestByAppointmentNoAndType(appt.getId(),"od_ar_axis");
				if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("od_ar_axis", "");}
				
				m = measurementsDao.findLatestByAppointmentNoAndType(appt.getId(),"os_ar_sph");
				if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("os_ar_sph", "");}
				m = measurementsDao.findLatestByAppointmentNoAndType(appt.getId(),"os_ar_cyl");
				if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("os_ar_cyl", "");}
				m = measurementsDao.findLatestByAppointmentNoAndType(appt.getId(),"os_ar_axis");
				if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("os_ar_axis", "");}
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				map.put("date", formatter.format(appt.getAppointmentDate()));
				ars.add(map);
			}
			request.setAttribute("ars", ars);
		}
		
		if(fieldList.contains("k")) {
			//logger.info("figure out k");
			List<Map<String,String>> ks = new ArrayList<Map<String,String>>();
			List<Appointment> appts = this.getAppointmentsForK(demographicNo, startDate, endDate);
			for(Appointment appt:appts) {
				Map<String,String> map = new HashMap<String,String>();
				Measurement m = null;
				m = measurementsDao.findLatestByAppointmentNoAndType(appt.getId(),"od_k1");
				if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("od_k1", "");}
				m = measurementsDao.findLatestByAppointmentNoAndType(appt.getId(),"od_k2");
				if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("od_k2", "");}
				m = measurementsDao.findLatestByAppointmentNoAndType(appt.getId(),"od_k2_axis");
				if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("od_k2_axis", "");}
				
				m = measurementsDao.findLatestByAppointmentNoAndType(appt.getId(),"os_k1");
				if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("os_k1", "");}
				m = measurementsDao.findLatestByAppointmentNoAndType(appt.getId(),"os_k2");
				if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("os_k2", "");}
				m = measurementsDao.findLatestByAppointmentNoAndType(appt.getId(),"os_k2_axis");
				if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("os_k2_axis", "");}
								
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				map.put("date", formatter.format(appt.getAppointmentDate()));
				ks.add(map);
			}
			request.setAttribute("ks", ks);
		}
		
		if(fieldList.contains("manifest_refraction")) {
			//logger.info("figure out manifest_refraction");
			List<Map<String,String>> manifestRefraction = new ArrayList<Map<String,String>>();
			List<Appointment> appts = this.getAppointmentsForManifestRefraction(demographicNo, startDate, endDate);
			for(Appointment appt:appts) {
				Map<String,String> map = new HashMap<String,String>();
				Measurement m = null;
				m = measurementsDao.findLatestByAppointmentNoAndType(appt.getId(),"od_manifest_refraction_sph");
				if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("od_manifest_refraction_sph", "");}
				m = measurementsDao.findLatestByAppointmentNoAndType(appt.getId(),"od_manifest_refraction_cyl");
				if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("od_manifest_refraction_cyl", "");}
				m = measurementsDao.findLatestByAppointmentNoAndType(appt.getId(),"od_manifest_refraction_axis");
				if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("od_manifest_refraction_axis", "");}
				m = measurementsDao.findLatestByAppointmentNoAndType(appt.getId(),"od_manifest_refraction_add");
				if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("od_manifest_refraction_add", "");}
				
				m = measurementsDao.findLatestByAppointmentNoAndType(appt.getId(),"os_manifest_refraction_sph");
				if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("os_manifest_refraction_sph", "");}
				m = measurementsDao.findLatestByAppointmentNoAndType(appt.getId(),"os_manifest_refraction_cyl");
				if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("os_manifest_refraction_cyl", "");}
				m = measurementsDao.findLatestByAppointmentNoAndType(appt.getId(),"os_manifest_refraction_axis");
				if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("os_manifest_refraction_axis", "");}
				m = measurementsDao.findLatestByAppointmentNoAndType(appt.getId(),"os_manifest_refraction_add");
				if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("os_manifest_refraction_add", "");}
				
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				map.put("date", formatter.format(appt.getAppointmentDate()));
				manifestRefraction.add(map);
			}
			request.setAttribute("manifestRefraction", manifestRefraction);
		}	
		
		if(fieldList.contains("cycloplegic_refraction")) {
			//logger.info("figure out cycloplegic_refraction");
			List<Map<String,String>> cycloplegicRefraction = new ArrayList<Map<String,String>>();
			List<Appointment> appts = this.getAppointmentsForCycloplegicRefraction(demographicNo, startDate, endDate);
			for(Appointment appt:appts) {
				Map<String,String> map = new HashMap<String,String>();
				Measurement m = null;
				m = measurementsDao.findLatestByAppointmentNoAndType(appt.getId(),"od_cycloplegic_refraction_sph");
				if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("od_cycloplegic_refraction_sph", "");}
				m = measurementsDao.findLatestByAppointmentNoAndType(appt.getId(),"od_cycloplegic_refraction_cyl");
				if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("od_cycloplegic_refraction_cyl", "");}
				m = measurementsDao.findLatestByAppointmentNoAndType(appt.getId(),"od_cycloplegic_refraction_axis");
				if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("od_cycloplegic_refraction_axis", "");}
				m = measurementsDao.findLatestByAppointmentNoAndType(appt.getId(),"od_cycloplegic_refraction_add");
				if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("od_cycloplegic_refraction_add", "");}
				
				m = measurementsDao.findLatestByAppointmentNoAndType(appt.getId(),"os_cycloplegic_refraction_sph");
				if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("os_cycloplegic_refraction_sph", "");}
				m = measurementsDao.findLatestByAppointmentNoAndType(appt.getId(),"os_cycloplegic_refraction_cyl");
				if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("os_cycloplegic_refraction_cyl", "");}
				m = measurementsDao.findLatestByAppointmentNoAndType(appt.getId(),"os_cycloplegic_refraction_axis");
				if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("os_cycloplegic_refraction_axis", "");}
				m = measurementsDao.findLatestByAppointmentNoAndType(appt.getId(),"os_cycloplegic_refraction_add");
				if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("os_cycloplegic_refraction_add", "");}
				
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				map.put("date", formatter.format(appt.getAppointmentDate()));
				cycloplegicRefraction.add(map);
			}
			request.setAttribute("cycloplegicRefraction", cycloplegicRefraction);
		}	
		
		if(fieldList.contains("angle")) {
			//logger.info("figure out angle");
			List<Map<String,String>> angle = new ArrayList<Map<String,String>>();
			List<Appointment> appts = this.getAppointmentsForCycloplegicRefraction(demographicNo, startDate, endDate);
			for(Appointment appt:appts) {
				Map<String,String> map = new HashMap<String,String>();
				Measurement m = null;
				m = measurementsDao.findLatestByAppointmentNoAndType(appt.getId(),"od_angle_up");
				if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("od_angle_up", "");}
				m = measurementsDao.findLatestByAppointmentNoAndType(appt.getId(),"od_angle_middle0");
				if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("od_angle_middle0", "");}
				m = measurementsDao.findLatestByAppointmentNoAndType(appt.getId(),"od_angle_middle1");
				if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("od_angle_middle1", "");}
				m = measurementsDao.findLatestByAppointmentNoAndType(appt.getId(),"od_angle_middle2");
				if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("od_angle_middle2", "");}
				m = measurementsDao.findLatestByAppointmentNoAndType(appt.getId(),"od_angle_down");
				if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("od_angle_down", "");}

				m = measurementsDao.findLatestByAppointmentNoAndType(appt.getId(),"os_angle_up");
				if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("os_angle_up", "");}
				m = measurementsDao.findLatestByAppointmentNoAndType(appt.getId(),"os_angle_middle0");
				if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("os_angle_middle0", "");}
				m = measurementsDao.findLatestByAppointmentNoAndType(appt.getId(),"os_angle_middle1");
				if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("os_angle_middle1", "");}
				m = measurementsDao.findLatestByAppointmentNoAndType(appt.getId(),"os_angle_middle2");
				if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("os_angle_middle2", "");}
				m = measurementsDao.findLatestByAppointmentNoAndType(appt.getId(),"os_angle_down");
				if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("os_angle_down", "");}
				
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				map.put("date", formatter.format(appt.getAppointmentDate()));
				angle.add(map);
			}
			request.setAttribute("angle", angle);
		}
		
		//EOM
		
		request.setAttribute("fields", fieldList);
		request.setAttribute("sdate",strStartDate);
		request.setAttribute("edate",strEndDate);
		
		return mapping.findForward("results");	   
	}
	
	private List<Appointment> filterByRefPage(List<Appointment> appointments, String refPage) {
		int pageSize=5;
		
		if(refPage == null || refPage.equals("")) {
			refPage="1";
		}
		int page = Integer.parseInt(refPage);
		
		//only applies to multi-page results
		if(appointments.size()<=pageSize) {
			return appointments;
		}
		
		List<Appointment> filteredAppointments = new ArrayList<Appointment>();
		int start = ((page-1)*pageSize);
		int end = ((page-1)*pageSize)+pageSize;
		
		for(int x=0;x<appointments.size();x++) {
			if(x<start) {
				continue;
			}
			if(x>=end) {
				break;
			}
			filteredAppointments.add(appointments.get(x));
		}
		
		return filteredAppointments;
	}
	
	
	private List<Appointment> getAppointments(String demographicNo, List<String> fieldList, Date startDate, Date endDate) {
		SortedSet<Integer> appointmentIds = new TreeSet<Integer>();
		
		for(String f:fieldList) {
			Set<Integer> apptNos =null;
			apptNos = measurementsDao.getAppointmentNosByDemographicNoAndType(Integer.parseInt(demographicNo),f,startDate,endDate);
			appointmentIds.addAll(apptNos);					
		}
		
		List<Appointment> appointments = new ArrayList<Appointment>();		
		for(Integer id:appointmentIds) {
			appointments.add(0,appointmentDao.find(id));
		}
		
		return appointments;
	}
	
	private List<Appointment> getAppointmentsForAr(String demographicNo, Date startDate, Date endDate) {
		SortedSet<Integer> appointmentIds = new TreeSet<Integer>();
		String fields[] = {"od_ar_sph","od_ar_cyl","od_ar_axis","os_ar_sph","os_ar_cyl","os_ar_axis"};
				
		for(String f:fields) {
			Set<Integer> apptNos =null;
			apptNos = measurementsDao.getAppointmentNosByDemographicNoAndType(Integer.parseInt(demographicNo),f,startDate,endDate);
			appointmentIds.addAll(apptNos);
		}
				
		List<Appointment> appointments = new ArrayList<Appointment>();		
		for(Integer id:appointmentIds) {
			appointments.add(0,appointmentDao.find(id));
		}
		
		return appointments;
	}
	
	private List<Appointment> getAppointmentsForK(String demographicNo, Date startDate, Date endDate) {
		SortedSet<Integer> appointmentIds = new TreeSet<Integer>();
		String fields[] = {"od_k1","od_k2","od_k2_axis","os_k1","os_k2","os_k2_axis"};
				
		for(String f:fields) {
			Set<Integer> apptNos =null;
			apptNos = measurementsDao.getAppointmentNosByDemographicNoAndType(Integer.parseInt(demographicNo),f,startDate,endDate);
			appointmentIds.addAll(apptNos);
		}
				
		List<Appointment> appointments = new ArrayList<Appointment>();		
		for(Integer id:appointmentIds) {
			appointments.add(0,appointmentDao.find(id));
		}
		
		return appointments;
	}
	
	private List<Appointment> getAppointmentsForManifestRefraction(String demographicNo, Date startDate, Date endDate) {
		SortedSet<Integer> appointmentIds = new TreeSet<Integer>();
		String fields[] = {"od_manifest_refraction_sph","od_manifest_refraction_cyl","od_manifest_refraction_axis","od_manifest_refraction_add",
				"os_manifest_refraction_sph","os_manifest_refraction_cyl","os_manifest_refraction_axis","os_manifest_refraction_add"};
				
		for(String f:fields) {
			Set<Integer> apptNos =null;
			apptNos = measurementsDao.getAppointmentNosByDemographicNoAndType(Integer.parseInt(demographicNo),f,startDate,endDate);
			appointmentIds.addAll(apptNos);
		}
				
		List<Appointment> appointments = new ArrayList<Appointment>();		
		for(Integer id:appointmentIds) {
			appointments.add(0,appointmentDao.find(id));
		}
		
		return appointments;
	}
	
	private List<Appointment> getAppointmentsForCycloplegicRefraction(String demographicNo, Date startDate, Date endDate) {
		SortedSet<Integer> appointmentIds = new TreeSet<Integer>();
		String fields[] = {"od_cycloplegic_refraction_sph","od_cycloplegic_refraction_cyl","od_cycloplegic_refraction_axis","od_cycloplegic_refraction_add",
				"os_cycloplegic_refraction_sph","os_cycloplegic_refraction_cyl","os_cycloplegic_refraction_axis","os_cycloplegic_refraction_add"};
				
		for(String f:fields) {
			Set<Integer> apptNos =null;
			apptNos = measurementsDao.getAppointmentNosByDemographicNoAndType(Integer.parseInt(demographicNo),f,startDate,endDate);
			appointmentIds.addAll(apptNos);
		}
				
		List<Appointment> appointments = new ArrayList<Appointment>();		
		for(Integer id:appointmentIds) {
			appointments.add(0,appointmentDao.find(id));
		}
		
		return appointments;
	}
}
