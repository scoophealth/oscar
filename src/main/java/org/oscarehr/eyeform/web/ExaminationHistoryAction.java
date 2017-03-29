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
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.eyeform.dao.EyeformSpecsHistoryDao;
import org.oscarehr.eyeform.model.EyeformSpecsHistory;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.util.StringUtils;

public class ExaminationHistoryAction extends DispatchAction {

	Logger logger = MiscUtils.getLogger();	
	protected MeasurementDao measurementsDao = SpringUtils.getBean(MeasurementDao.class);
	DemographicDao demographicDao= (DemographicDao)SpringUtils.getBean("demographicDao");
	OscarAppointmentDao appointmentDao = (OscarAppointmentDao)SpringUtils.getBean("oscarAppointmentDao");
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
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

		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_demographic", "r", demographicNo)) {
        		throw new SecurityException("missing required security object (_demographic)");
        	}
		
		oscar.OscarProperties props1 = oscar.OscarProperties.getInstance();
        	String eyeform = props1.getProperty("cme_js");
        
		request.setAttribute("demographic",demographicDao.getClientByDemographicNo(Integer.parseInt(demographicNo)));
		
		String[] fields = request.getParameterValues("fromlist2");
		List<String> fieldList = new ArrayList<String>();
		if(fields != null) {
			for(String field:fields) {
				fieldList.add(field);
			}
		}
		
		String[] field1 = request.getParameterValues("fromlist1");
		List<String> fieldList1 = new ArrayList<String>();
		if(field1 != null) {
			for(String field:field1) {
				fieldList1.add(field);
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
			List<Appointment> appts = appointmentDao.getAllByDemographicNoOrderByApptDate(Integer.parseInt(demographicNo));
			if(appts.size()>0) {
				startDate = appts.get(0).getAppointmentDate();
				if((!"eyeform3".equals(eyeform)) && (!"eyeform3.1".equals(eyeform)) && (!"eyeform3.2".equals(eyeform))){
					strStartDate = dformate.format(startDate.getTime());
				}
			} else {
				Calendar enddate = Calendar.getInstance();									
				enddate.roll(Calendar.YEAR, -1);
				if((!"eyeform3".equals(eyeform)) && (!"eyeform3.1".equals(eyeform)) && (!"eyeform3.2".equals(eyeform))){
					strStartDate = dformate.format(enddate.getTime());
				}
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
		
		EyeformSpecsHistoryDao dao = (EyeformSpecsHistoryDao)SpringUtils.getBean("eyeformSpecsHistoryDao");
		if((!"eyeform3".equals(eyeform)) && (!"eyeform3.1".equals(eyeform)) && (!"eyeform3.2".equals(eyeform))){

			//simple fields
			//exclude complex ones ar,k,manifest_refraction,cycloplegic_refraction, angle, EOM
			List<String> simpleFieldNames = new ArrayList<String>();
			//if((!"eyeform3".equals(eyeform)) && (!"eyeform3.1".equals(eyeform)) && (!"eyeform3.2".equals(eyeform))){
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
					Measurement m = measurementsDao.getLatestMeasurementByAppointment(apptId, field);	
					simpleFields[x][y] = m;
				}
			}
			request.setAttribute("simpleFieldNames", simpleFieldNames);
			request.setAttribute("simpleFields", simpleFields);
			request.setAttribute("appointments", appointments);
			if(refPage == null || refPage=="") { refPage="1";}
			request.setAttribute("refPage", Integer.parseInt(refPage));
	    }
		
		if(("eyeform3".equals(eyeform)) || ("eyeform3.1".equals(eyeform)) || ("eyeform3.2".equals(eyeform))){
			//if(fieldList.contains("Glasses Rx")){
			if(fieldList1.contains("GLASSES HISTORY")){
				List<Map<String,String>> glasses = new ArrayList<Map<String,String>>();
				List<EyeformSpecsHistory> specs = null;
				String oldglasses = request.getParameter("oldglasses");
				if((oldglasses!= null) && (oldglasses.equals("true"))){
					specs = dao.getByDemographicNo(Integer.parseInt(demographicNo));
				}else{
					specs = dao.getByDateRange(Integer.parseInt(demographicNo),startDate, endDate);
				}
				for(EyeformSpecsHistory spec:specs){
					Map<String,String> map = new HashMap<String,String>();
					if((oldglasses!= null) && (oldglasses.equals("true"))){
						
					}else{
						if(spec.getDate() == null){
							continue;
						}
					}
					if(spec.getType() != null){
						map.put("gl_type", spec.getType());
					}else{
						map.put("gl_type", "");
					}
					if(spec.getOdSph() != null){
						map.put("gl_rs",spec.getOdSph());
					}else{
						map.put("gl_rs","");
					}
					if(spec.getOdCyl() != null){
						map.put("gl_rc", spec.getOdCyl());
					}else{
						map.put("gl_rc","");
					}
					if(spec.getOdAxis() != null){
						map.put("gl_rx", spec.getOdAxis());
					}else{
						map.put("gl_rx", "");
					}
					if(spec.getOdAdd() != null){
						map.put("gl_ra", spec.getOdAdd());
					}else{
						map.put("gl_ra", "");
					}
					if(spec.getOdPrism() != null){
						map.put("gl_rp", spec.getOdPrism());
					}else{
						map.put("gl_rp", "");
					}
					if(spec.getOsSph() != null){
						map.put("gl_ls",spec.getOsSph());
					}else{
						map.put("gl_ls","");
					}
					if(spec.getOsCyl() != null){
						map.put("gl_lc", spec.getOsCyl());
					}else{
						map.put("gl_lc","");
					}
					if(spec.getOsAxis() != null){
						map.put("gl_lx", spec.getOsAxis());
					}else{
						map.put("gl_lx", "");
					}
					if(spec.getOsAdd() != null){
						map.put("gl_la", spec.getOsAdd());
					}else{
						map.put("gl_la", "");
					}
					if(spec.getOsPrism() != null){
						map.put("gl_lp", spec.getOsPrism());
					}else{
						map.put("gl_lp", "");
					}
					if(spec.getDateStr() != null){
						map.put("gl_date", spec.getDateStr());
					}else{
						map.put("gl_date", "");
					}
					if(spec.getNote() != null){
						map.put("gl_note", spec.getNote());
					}else{
						map.put("gl_note", "");
					}

					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
					if((oldglasses!= null) && (oldglasses.equals("true"))){
						if(spec.getDate() == null){
							map.put("date", "");
						}else{
							map.put("date", formatter.format(spec.getDate()));
						}
					}else{
						map.put("date", formatter.format(spec.getDate()));
					}
					glasses.add(map);
				}
				request.setAttribute("glasses", glasses);
			}
			//if(fieldList.contains("Distance vision (sc)")){
			if(fieldList1.contains("VISION ASSESSMENT")){
				List<Map<String,String>> distance_vision = new ArrayList<Map<String,String>>();
				List<Appointment> appts = this.getAppointmentsForDistancevision(demographicNo, startDate, endDate);
				for(Appointment appt:appts) {
					if(appt==null) continue;
					Map<String,String> map = new HashMap<String,String>();
					Measurement m = null;
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_rdsc");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_rdsc", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_ldsc");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_ldsc", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_dsc");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_dsc", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_rdcc");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_rdcc", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_ldcc");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_ldcc", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_dcc");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_dcc", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_rph");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_rph", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_lph");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_lph", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_risc");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_risc", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_lisc");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_lisc", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_isc");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_isc", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_ricc");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_ricc", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_licc");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_licc", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_icc");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_icc", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_rnsc");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_rnsc", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_lnsc");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_lnsc", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_nsc");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_nsc", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_rncc");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_rncc", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_lncc");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_lncc", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_ncc");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_ncc", "");}				
					
					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
					map.put("date", formatter.format(appt.getAppointmentDate()));
					distance_vision.add(map);
				}
				request.setAttribute("distance_vision", distance_vision);
			}
			//if(fieldList.contains("Fly test")){
			if(fieldList1.contains("STEREO VISION")){
				List<Map<String,String>> fly_test = new ArrayList<Map<String,String>>();
				List<Appointment> appts = this.getAppointmentsForFlytest(demographicNo, startDate, endDate);
				for(Appointment appt:appts) {
					Map<String,String> map = new HashMap<String,String>();
					Measurement m = null;
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_fly");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_fly", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_stereo");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_stereo", "");}	
					
					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
					map.put("date", formatter.format(appt.getAppointmentDate()));
					fly_test.add(map);
				}
				request.setAttribute("fly_test", fly_test);
			}
			//if(fieldList.contains("Keratometry")){
			if(fieldList1.contains("VISION MEASUREMENT")){
				List<Map<String,String>> keratometry = new ArrayList<Map<String,String>>();
				List<Appointment> appts = this.getAppointmentsForKeratometry(demographicNo, startDate, endDate);
				for(Appointment appt:appts) {
					Map<String,String> map = new HashMap<String,String>();
					Measurement m = null;
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_rk1");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_rk1", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_rk2");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_rk2", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_rkx");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_rkx", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_lk1");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_lk1", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_lk2");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_lk2", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_lkx");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_lkx", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_rs");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_rs", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_rc");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_rc", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_rx");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_rx", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_rar");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_rar", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_ls");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_ls", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_lc");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_lc", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_lx");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_lx", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_lar");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_lar", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_rds");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_rds", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_rdc");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_rdc", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_rdx");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_rdx", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_rdv");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_rdv", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_lds");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_lds", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_ldc");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_ldc", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_ldx");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_ldx", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_ldv");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_ldv", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_dv");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_dv", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_rns");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_rns", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_rnc");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_rnc", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_rnx");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_rnx", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_rnv");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_rnv", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_lns");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_lns", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_lnc");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_lnc", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_lnx");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_lnx", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_lnv");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_lnv", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_nv");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_nv", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_rcs");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_rcs", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_rcc");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_rcc", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_rcx");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_rcx", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_rcv");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_rcv", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_lcs");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_lcs", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_lcc");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_lcc", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_lcx");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_lcx", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"v_lcv");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("v_lcv", "");}
					
					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
					map.put("date", formatter.format(appt.getAppointmentDate()));
					keratometry.add(map);
				}
				request.setAttribute("keratometry", keratometry);
			}
			//if(fieldList.contains("NCT")){
			if(fieldList1.contains("INTRAOCULAR PRESSURE")){
				List<Map<String,String>> nct = new ArrayList<Map<String,String>>();
				List<Appointment> appts = this.getAppointmentsForNCT(demographicNo, startDate, endDate);
				for(Appointment appt:appts) {
					Map<String,String> map = new HashMap<String,String>();
					Measurement m = null;
					Date d1 = null;
					Date d2 = null;
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"iop_rn");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("iop_rn", "");}
					if(m != null){
						d1 = m.getDateObserved();
					}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"iop_ln");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("iop_ln", "");}
					if(m != null){
						d2 = m.getDateObserved();
					}
					Date d = d2;
					if((d1 != null) &&(d2 != null)){
						if(d1.after(d2))
							d=d1;
					}
					if((d1 != null) &&(d2 == null)){
						d=d1;
					}
					if((d1 == null) &&(d2 != null)){
						d=d2;
					}
					SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
					if(d != null){
						map.put("iop_ntime", sdf.format(d));
					}else{
						map.put("iop_ntime", "");
					}
					//m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"iop_ntime");
					//if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("iop_ntime", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"iop_ra");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("iop_ra", "");}
					if(m != null){
						d1 = m.getDateObserved();
					}else{
						d1 = null;
					}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"iop_la");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("iop_la", "");}
					if(m != null){
						d2 = m.getDateObserved();
					}else{
						d2 = null;
					}
					Date d3= d2;
					if((d1 != null) &&(d2 != null)){
						if(d1.after(d2))
							d3=d1;
					}
					if((d1 != null) &&(d2 == null)){
						d3=d1;
					}
					if((d1 == null) &&(d2 != null)){
						d3=d2;
					}
					SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
					if(d3 != null){
						map.put("iop_atime", sdf.format(d3));
					}else{
						map.put("iop_atime", "");
					}
					
					//m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"iop_atime");
					//if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("iop_atime", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"cct_r");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("cct_r", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"cct_l");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("cct_l", "");}
					
					
					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
					map.put("date", formatter.format(appt.getAppointmentDate()));
					nct.add(map);
				}
				request.setAttribute("nct", nct);
			}
			//if(fieldList.contains("Dominance")){
			if(fieldList1.contains("REFRACTIVE")){
				List<Map<String,String>> dominance = new ArrayList<Map<String,String>>();
				List<Appointment> appts = this.getAppointmentsForDominance(demographicNo, startDate, endDate);
				for(Appointment appt:appts) {
					Map<String,String> map = new HashMap<String,String>();
					Measurement m = null;
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"ref_rdom");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("ref_rdom", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"ref_ldom");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("ref_ldom", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"ref_rpdim");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("ref_rpdim", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"ref_lpdim");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("ref_lpdim", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"ref_rkappa");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("ref_rkappa", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"ref_lkappa");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("ref_lkappa", "");}
					
					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
					map.put("date", formatter.format(appt.getAppointmentDate()));
					dominance.add(map);
				}
				request.setAttribute("dominance", dominance);
			}
			//if(fieldList.contains("Colour vision")){
			if(fieldList1.contains("OTHER EXAM")){
				List<Map<String,String>> colour_vision = new ArrayList<Map<String,String>>();
				List<Appointment> appts = this.getAppointmentsForColourvision(demographicNo, startDate, endDate);
				for(Appointment appt:appts) {
					Map<String,String> map = new HashMap<String,String>();
					Measurement m = null;
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"o_rcolour");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("o_rcolour", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"o_lcolour");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("o_lcolour", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"o_rpupil");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("o_rpupil", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"o_lpupil");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("o_lpupil", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"o_ramsler");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("o_ramsler", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"o_lamsler");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("o_lamsler", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"o_rpam");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("o_rpam", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"o_lpam");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("o_lpam", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"o_rconf");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("o_rconf", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"o_lconf");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("o_lconf", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"o_mad");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("o_mad", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"o_bag");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("o_bag", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"o_w4dd");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("o_w4dd", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"o_w4dn");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("o_w4dn", "");}
					
					
					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
					map.put("date", formatter.format(appt.getAppointmentDate()));
					colour_vision.add(map);
				}
				request.setAttribute("colour_vision", colour_vision);
			}
			//if(fieldList.contains("DUCTION/DIPLOPIA TESTING")){
			if(fieldList1.contains("DUCTION/DIPLOPIA TESTING")){
				List<Map<String,String>> ductlion = new ArrayList<Map<String,String>>();
				List<Appointment> appts = this.getAppointmentsForDUCTION(demographicNo, startDate, endDate);
				for(Appointment appt:appts) {
					Map<String,String> map = new HashMap<String,String>();
					Measurement m = null;
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"duc_rur");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("duc_rur", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"duc_rul");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("duc_rul", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"duc_lur");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("duc_lur", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"duc_lul");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("duc_lul", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"dip_ur");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("dip_ur", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"dip_u");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("dip_u", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"dip_ul");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("dip_ul", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"duc_rr");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("duc_rr", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"duc_rl");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("duc_rl", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"duc_lr");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("duc_lr", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"duc_ll");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("duc_ll", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"dip_r");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("dip_r", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"dip_p");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("dip_p", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"dip_l");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("dip_l", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"duc_rdr");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("duc_rdr", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"duc_rdl");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("duc_rdl", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"duc_ldr");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("duc_ldr", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"duc_ldl");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("duc_ldl", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"dip_dr");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("dip_dr", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"dip_d");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("dip_d", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"dip_dl");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("dip_dl", "");}								
					
					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
					map.put("date", formatter.format(appt.getAppointmentDate()));
					ductlion.add(map);
				}
				request.setAttribute("ductlion", ductlion);
			}
			//if(fieldList.contains("Primary gaze")){
			if(fieldList1.contains("DEVIATION MEASUREMENT")){
				List<Map<String,String>> primary = new ArrayList<Map<String,String>>();
				List<Appointment> appts = this.getAppointmentsForPrimary(demographicNo, startDate, endDate);
				for(Appointment appt:appts) {
					Map<String,String> map = new HashMap<String,String>();
					Measurement m = null;
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"dev_p");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("dev_p", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"dev_u");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("dev_u", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"dev_d");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("dev_d", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"dev_r");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("dev_r", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"dev_l");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("dev_l", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"dev_rt");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("dev_rt", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"dev_lt");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("dev_lt", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"dev_near");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("dev_near", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"dev_plus3");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("dev_plus3", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"dev_far");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("dev_far", "");}
					
					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
					map.put("date", formatter.format(appt.getAppointmentDate()));
					primary.add(map);
				}
				request.setAttribute("primary", primary);
			}
			//if(fieldList.contains("Retropulsion")){
			if(fieldList1.contains("EXTERNAL/ORBIT")){
				List<Map<String,String>> retropulsion = new ArrayList<Map<String,String>>();
				List<Appointment> appts = this.getAppointmentsForRetropulsion(demographicNo, startDate, endDate);
				for(Appointment appt:appts) {
					Map<String,String> map = new HashMap<String,String>();
					Measurement m = null;
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"ext_rface");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("ext_rface", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"ext_lface");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("ext_lface", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"ext_rretro");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("ext_rretro", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"ext_lretro");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("ext_lretro", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"ext_rhertel");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("ext_rhertel", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"ext_lhertel");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("ext_lhertel", "");}
					
					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
					map.put("date", formatter.format(appt.getAppointmentDate()));
					retropulsion.add(map);
				}
				request.setAttribute("retropulsion", retropulsion);
			}
			//if(fieldList.contains("Upper lid")){
			if(fieldList1.contains("EYELID/NASOLACRIMAL DUCT")){
				List<Map<String,String>> upper = new ArrayList<Map<String,String>>();
				List<Appointment> appts = this.getAppointmentsForUpper(demographicNo, startDate, endDate);
				for(Appointment appt:appts) {
					Map<String,String> map = new HashMap<String,String>();
					Measurement m = null;
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"ext_rul");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("ext_rul", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"ext_lul");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("ext_lul", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"ext_rll");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("ext_rll", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"ext_lll");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("ext_lll", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"ext_rlake");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("ext_rlake", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"ext_llake");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("ext_llake", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"ext_rirrig");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("ext_rirrig", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"ext_lirrig");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("ext_lirrig", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"ext_rpunc");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("ext_rpunc", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"ext_lpunc");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("ext_lpunc", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"ext_rnld");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("ext_rnld", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"ext_lnld");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("ext_lnld", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"ext_rdye");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("ext_rdye", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"ext_ldye");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("ext_ldye", "");}

					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
					map.put("date", formatter.format(appt.getAppointmentDate()));
					upper.add(map);
				}
				request.setAttribute("upper", upper);
			}
			//if(fieldList.contains("Margin reflex distance")){
			if(fieldList1.contains("EYELID MEASUREMENT")){
				List<Map<String,String>> margin = new ArrayList<Map<String,String>>();
				List<Appointment> appts = this.getAppointmentsForMargin(demographicNo, startDate, endDate);
				for(Appointment appt:appts) {
					Map<String,String> map = new HashMap<String,String>();
					Measurement m = null;
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"lid_rmrd");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("lid_rmrd", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"lid_lmrd");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("lid_lmrd", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"lid_riss");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("lid_riss", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"lid_liss");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("lid_liss", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"lid_rlev");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("lid_rlev", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"lid_llev");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("lid_llev", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"lid_rlag");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("lid_rlag", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"lid_llag");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("lid_llag", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"lid_rblink");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("lid_rblink", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"lid_lblink");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("lid_lblink", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"lid_rcn7");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("lid_rcn7", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"lid_lcn7");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("lid_lcn7", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"lid_rbell");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("lid_rbell", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"lid_lbell");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("lid_lbell", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"lid_rschirm");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("lid_rschirm", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"lid_lschirm");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("lid_lschirm", "");}
					
					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
					map.put("date", formatter.format(appt.getAppointmentDate()));
					margin.add(map);
				}
				request.setAttribute("margin", margin);
			}
			//if(fieldList.contains("Cornea")){
			if(fieldList1.contains("ANTERIOR SEGMENT")){
				List<Map<String,String>> cornea = new ArrayList<Map<String,String>>();
				List<Appointment> appts = this.getAppointmentsForCornea(demographicNo, startDate, endDate);
				for(Appointment appt:appts) {
					Map<String,String> map = new HashMap<String,String>();
					Measurement m = null;
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"a_rk");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("a_rk", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"a_lk");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("a_lk", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"a_rconj");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("a_rconj", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"a_lconj");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("a_lconj", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"a_rac");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("a_rac", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"a_lac");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("a_lac", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"a_rangle_1");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("a_rangle_1", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"a_rangle_2");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("a_rangle_2", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"a_rangle_3");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("a_rangle_3", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"a_rangle_4");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("a_rangle_4", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"a_rangle_5");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("a_rangle_5", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"a_langle_1");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("a_langle_1", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"a_langle_2");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("a_langle_2", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"a_langle_3");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("a_langle_3", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"a_langle_4");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("a_langle_4", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"a_langle_5");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("a_langle_5", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"a_riris");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("a_riris", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"a_liris");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("a_liris", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"a_rlens");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("a_rlens", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"a_llens");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("a_llens", "");}
					
					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
					map.put("date", formatter.format(appt.getAppointmentDate()));
					cornea.add(map);
				}
				request.setAttribute("cornea", cornea);
			}
			//if(fieldList.contains("Optic disc")){
			if(fieldList1.contains("POSTERIOR SEGMENT")){
				List<Map<String,String>> optic = new ArrayList<Map<String,String>>();
				List<Appointment> appts = this.getAppointmentsForOptic(demographicNo, startDate, endDate);
				for(Appointment appt:appts) {
					Map<String,String> map = new HashMap<String,String>();
					Measurement m = null;
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"p_rdisc");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("p_rdisc", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"p_ldisc");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("p_ldisc", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"p_rcd");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("p_rcd", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"p_lcd");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("p_lcd", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"p_rmac");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("p_rmac", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"p_lmac");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("p_lmac", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"p_rret");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("p_rret", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"p_lret");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("p_lret", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"p_rvit");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("p_rvit", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"p_lvit");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("p_lvit", "");}
					
					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
					map.put("date", formatter.format(appt.getAppointmentDate()));
					optic.add(map);
				}
				request.setAttribute("optic", optic);
			}
		}else{		
			//complex fields
			//for each appt, get the od and os for AR
			if(fieldList.contains("ar")) {
				//logger.info("figure out AR");
				List<Map<String,String>> ars = new ArrayList<Map<String,String>>();
				List<Appointment> appts = this.getAppointmentsForAr(demographicNo, startDate, endDate);
				for(Appointment appt:appts) {
					Map<String,String> map = new HashMap<String,String>();
					Measurement m = null;
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"od_ar_sph");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("od_ar_sph", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"od_ar_cyl");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("od_ar_cyl", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"od_ar_axis");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("od_ar_axis", "");}
					
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"os_ar_sph");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("os_ar_sph", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"os_ar_cyl");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("os_ar_cyl", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"os_ar_axis");
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
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"od_k1");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("od_k1", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"od_k2");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("od_k2", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"od_k2_axis");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("od_k2_axis", "");}
					
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"os_k1");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("os_k1", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"os_k2");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("os_k2", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"os_k2_axis");
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
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"od_manifest_refraction_sph");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("od_manifest_refraction_sph", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"od_manifest_refraction_cyl");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("od_manifest_refraction_cyl", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"od_manifest_refraction_axis");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("od_manifest_refraction_axis", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"od_manifest_refraction_add");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("od_manifest_refraction_add", "");}
					
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"os_manifest_refraction_sph");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("os_manifest_refraction_sph", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"os_manifest_refraction_cyl");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("os_manifest_refraction_cyl", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"os_manifest_refraction_axis");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("os_manifest_refraction_axis", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"os_manifest_refraction_add");
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
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"od_cycloplegic_refraction_sph");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("od_cycloplegic_refraction_sph", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"od_cycloplegic_refraction_cyl");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("od_cycloplegic_refraction_cyl", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"od_cycloplegic_refraction_axis");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("od_cycloplegic_refraction_axis", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"od_cycloplegic_refraction_add");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("od_cycloplegic_refraction_add", "");}
					
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"os_cycloplegic_refraction_sph");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("os_cycloplegic_refraction_sph", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"os_cycloplegic_refraction_cyl");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("os_cycloplegic_refraction_cyl", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"os_cycloplegic_refraction_axis");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("os_cycloplegic_refraction_axis", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"os_cycloplegic_refraction_add");
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
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"od_angle_up");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("od_angle_up", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"od_angle_middle0");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("od_angle_middle0", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"od_angle_middle1");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("od_angle_middle1", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"od_angle_middle2");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("od_angle_middle2", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"od_angle_down");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("od_angle_down", "");}
	
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"os_angle_up");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("os_angle_up", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"os_angle_middle0");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("os_angle_middle0", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"os_angle_middle1");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("os_angle_middle1", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"os_angle_middle2");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("os_angle_middle2", "");}
					m = measurementsDao.getLatestMeasurementByAppointment(appt.getId(),"os_angle_down");
					if(m!=null){map.put(m.getType(), m.getDataField());}else{map.put("os_angle_down", "");}
					
					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
					map.put("date", formatter.format(appt.getAppointmentDate()));
					angle.add(map);
				}
				request.setAttribute("angle", angle);
			}
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
			if(appointmentDao.find(id)!=null)
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
			if(appointmentDao.find(id)!=null)
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
			if(appointmentDao.find(id)!=null)
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
			if(appointmentDao.find(id)!=null)
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
			if(appointmentDao.find(id)!=null)
				appointments.add(0,appointmentDao.find(id));
		}
		
		return appointments;
	}
	
	private List<Appointment> getAppointmentsForGlassess(String demographicNo, Date startDate, Date endDate) {
		SortedSet<Integer> appointmentIds = new TreeSet<Integer>();
		String fields[] = {"gl_rs","gl_rc","gl_rx","gl_ra","gl_rp","gl_ls","gl_lc","gl_lx","gl_la","gl_lp","gl_date","gl_note"};
				
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
	
	private List<Appointment> getAppointmentsForDistancevision(String demographicNo, Date startDate, Date endDate) {
		SortedSet<Integer> appointmentIds = new TreeSet<Integer>();
		String fields[] = {"v_rdsc","v_ldsc","v_dsc","v_rdcc","v_ldcc","v_dcc","v_rph","v_lph","v_risc","v_lisc","v_isc","v_ricc","v_licc","v_icc","v_rnsc","v_lnsc","v_nsc","v_rncc","v_lncc","v_ncc"};
				
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
	
	private List<Appointment> getAppointmentsForFlytest(String demographicNo, Date startDate, Date endDate) {
		SortedSet<Integer> appointmentIds = new TreeSet<Integer>();
		String fields[] = {"v_fly","v_stereo"};
				
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
	
	private List<Appointment> getAppointmentsForKeratometry(String demographicNo, Date startDate, Date endDate) {
		SortedSet<Integer> appointmentIds = new TreeSet<Integer>();
		String fields[] = {"v_rk1","v_rk2","v_rkx","v_lk1","v_lk2","v_lkx","v_rs","v_rc","v_rx","v_rar","v_ls","v_lar","v_rds","v_rdc","v_rdx","v_rdv","v_lds","v_ldc","v_ldx","v_ldv","v_dv","v_rns","v_rnc","v_rnx","v_rnv","v_lns","v_lnc","v_lnx","v_lnv","v_nv","v_rcs","v_rcc","v_rcx","v_rcv","v_lcs","v_lcc","v_lcx","v_lcv"};
				
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
	
	private List<Appointment> getAppointmentsForNCT(String demographicNo, Date startDate, Date endDate) {
		SortedSet<Integer> appointmentIds = new TreeSet<Integer>();
		String fields[] = {"iop_rn","iop_ln","iop_ntime","iop_ra","iop_la","iop_atime","cct_r","cct_l"};
				
		for(String f:fields) {
			Set<Integer> apptNos =null;
			apptNos = measurementsDao.getAppointmentNosByDemographicNoAndType(Integer.parseInt(demographicNo),f,startDate,endDate);
			appointmentIds.addAll(apptNos);
		}
				
		List<Appointment> appointments = new ArrayList<Appointment>();		
		for(Integer id:appointmentIds) {
			if(appointmentDao.find(id)!=null)
				appointments.add(0,appointmentDao.find(id));
		}
		
		return appointments;
	}
	
	private List<Appointment> getAppointmentsForDominance(String demographicNo, Date startDate, Date endDate) {
		SortedSet<Integer> appointmentIds = new TreeSet<Integer>();
		String fields[] = {"ref_rpdim","ref_lpdim","ref_rkappa","ref_lkappa"};
				
		for(String f:fields) {
			Set<Integer> apptNos =null;
			apptNos = measurementsDao.getAppointmentNosByDemographicNoAndType(Integer.parseInt(demographicNo),f,startDate,endDate);
			appointmentIds.addAll(apptNos);
		}
				
		List<Appointment> appointments = new ArrayList<Appointment>();		
		for(Integer id:appointmentIds) {
			if(appointmentDao.find(id)!=null)
				appointments.add(0,appointmentDao.find(id));
		}
		
		return appointments;
	}
	
	private List<Appointment> getAppointmentsForColourvision(String demographicNo, Date startDate, Date endDate) {
		SortedSet<Integer> appointmentIds = new TreeSet<Integer>();
		String fields[] = {"o_rcolour","o_lcolour","o_rpupil","o_lpupil","o_ramsler","o_lamsler","o_rpam","o_lpam","o_rconf","o_lconf","o_mad","o_bag","o_w4dd","o_w4dn"};
				
		for(String f:fields) {
			Set<Integer> apptNos =null;
			apptNos = measurementsDao.getAppointmentNosByDemographicNoAndType(Integer.parseInt(demographicNo),f,startDate,endDate);
			appointmentIds.addAll(apptNos);
		}
				
		List<Appointment> appointments = new ArrayList<Appointment>();		
		for(Integer id:appointmentIds) {
			if(appointmentDao.find(id)!=null)
				appointments.add(0,appointmentDao.find(id));
		}
		
		return appointments;
	}
	
	private List<Appointment> getAppointmentsForDUCTION(String demographicNo, Date startDate, Date endDate) {
		SortedSet<Integer> appointmentIds = new TreeSet<Integer>();
		String fields[] = {"duc_rur","duc_rul","duc_lur","duc_lul","dip_ur","dip_u","dip_ul","duc_rr","duc_rl","duc_lr","duc_ll","dip_r","dip_p","dip_l","duc_rdr","duc_rdl","duc_ldr","duc_ldl","dip_dr","dip_d","dip_dl"};
				
		for(String f:fields) {
			Set<Integer> apptNos =null;
			apptNos = measurementsDao.getAppointmentNosByDemographicNoAndType(Integer.parseInt(demographicNo),f,startDate,endDate);
			appointmentIds.addAll(apptNos);
		}
				
		List<Appointment> appointments = new ArrayList<Appointment>();		
		for(Integer id:appointmentIds) {
			if(appointmentDao.find(id)!=null)
				appointments.add(0,appointmentDao.find(id));
		}
		
		return appointments;
	}
	
	private List<Appointment> getAppointmentsForPrimary(String demographicNo, Date startDate, Date endDate) {
		SortedSet<Integer> appointmentIds = new TreeSet<Integer>();
		String fields[] = {"dev_p","dev_u","dev_d","dev_r","dev_l","dev_rt","dev_near","dev_plus3","dev_far"};
				
		for(String f:fields) {
			Set<Integer> apptNos =null;
			apptNos = measurementsDao.getAppointmentNosByDemographicNoAndType(Integer.parseInt(demographicNo),f,startDate,endDate);
			appointmentIds.addAll(apptNos);
		}
				
		List<Appointment> appointments = new ArrayList<Appointment>();		
		for(Integer id:appointmentIds) {
			if(appointmentDao.find(id)!=null)
				appointments.add(0,appointmentDao.find(id));
		}
		
		return appointments;
	}
	
	private List<Appointment> getAppointmentsForRetropulsion(String demographicNo, Date startDate, Date endDate) {
		SortedSet<Integer> appointmentIds = new TreeSet<Integer>();
		String fields[] = {"ext_rface","ext_lface","ext_rretro","ext_lretro","ext_rhertel","ext_lhertel"};
				
		for(String f:fields) {
			Set<Integer> apptNos =null;
			apptNos = measurementsDao.getAppointmentNosByDemographicNoAndType(Integer.parseInt(demographicNo),f,startDate,endDate);
			appointmentIds.addAll(apptNos);
		}
				
		List<Appointment> appointments = new ArrayList<Appointment>();		
		for(Integer id:appointmentIds) {
			if(appointmentDao.find(id)!=null)
				appointments.add(0,appointmentDao.find(id));
		}
		
		return appointments;
	}
	
	private List<Appointment> getAppointmentsForUpper(String demographicNo, Date startDate, Date endDate) {
		SortedSet<Integer> appointmentIds = new TreeSet<Integer>();
		String fields[] = {"ext_rul","ext_lul","ext_rll","ext_lll","ext_rlake","ext_llake","ext_rirrig","ext_lirrig","ext_rpunc","ext_lpunc","ext_rnld","ext_lnld","ext_rdye","ext_ldye"};
				
		for(String f:fields) {
			Set<Integer> apptNos =null;
			apptNos = measurementsDao.getAppointmentNosByDemographicNoAndType(Integer.parseInt(demographicNo),f,startDate,endDate);
			appointmentIds.addAll(apptNos);
		}
				
		List<Appointment> appointments = new ArrayList<Appointment>();		
		for(Integer id:appointmentIds) {
			if(appointmentDao.find(id)!=null)
				appointments.add(0,appointmentDao.find(id));
		}
		
		return appointments;
	}
	
	private List<Appointment> getAppointmentsForMargin(String demographicNo, Date startDate, Date endDate) {
		SortedSet<Integer> appointmentIds = new TreeSet<Integer>();
		String fields[] = {"lid_rmrd","lid_lmrd","lid_riss","lid_liss","lid_rlev","lid_llev","lid_rlag","lid_llag","lid_rblink","lid_lblink","lid_rcn7","lid_lcn7","lid_rbell","lid_lbell","lid_rschirm","lid_lschirm"};
				
		for(String f:fields) {
			Set<Integer> apptNos =null;
			apptNos = measurementsDao.getAppointmentNosByDemographicNoAndType(Integer.parseInt(demographicNo),f,startDate,endDate);
			appointmentIds.addAll(apptNos);
		}
				
		List<Appointment> appointments = new ArrayList<Appointment>();		
		for(Integer id:appointmentIds) {
			if(appointmentDao.find(id)!=null)
				appointments.add(0,appointmentDao.find(id));
		}
		
		return appointments;
	}
	
	private List<Appointment> getAppointmentsForCornea(String demographicNo, Date startDate, Date endDate) {
		SortedSet<Integer> appointmentIds = new TreeSet<Integer>();
		String fields[] = {"a_rk","a_lk","a_rconj","a_lconj","a_rac","a_lac","a_rangle_1","a_rangle_2","a_rangle_3","a_rangle_4","a_rangle_5","a_langle_1","a_langle_2","a_langle_3","a_langle_4","a_langle_5","a_riris","a_liris","a_rlens","a_llens"};
				
		for(String f:fields) {
			Set<Integer> apptNos =null;
			apptNos = measurementsDao.getAppointmentNosByDemographicNoAndType(Integer.parseInt(demographicNo),f,startDate,endDate);
			appointmentIds.addAll(apptNos);
		}
				
		List<Appointment> appointments = new ArrayList<Appointment>();		
		for(Integer id:appointmentIds) {
			if(appointmentDao.find(id)!=null)
				appointments.add(0,appointmentDao.find(id));
		}
		
		return appointments;
	}
	
	private List<Appointment> getAppointmentsForOptic(String demographicNo, Date startDate, Date endDate) {
		SortedSet<Integer> appointmentIds = new TreeSet<Integer>();
		String fields[] = {"p_rdisc","p_ldisc","p_rcd","p_lcd","p_rmac","p_lmac","p_rret","p_lret","p_rvit","p_lvit"};
				
		for(String f:fields) {
			Set<Integer> apptNos =null;
			apptNos = measurementsDao.getAppointmentNosByDemographicNoAndType(Integer.parseInt(demographicNo),f,startDate,endDate);
			appointmentIds.addAll(apptNos);
		}
				
		List<Appointment> appointments = new ArrayList<Appointment>();		
		for(Integer id:appointmentIds) {
			if(appointmentDao.find(id)!=null)
				appointments.add(0,appointmentDao.find(id));
		}
		
		return appointments;
	}
	
	private List<Appointment> getAppointmentsForAngle(String demographicNo, Date startDate, Date endDate) {
		SortedSet<Integer> appointmentIds = new TreeSet<Integer>();
		String fields[] = {"od_angle_up","od_angle_middle0","od_angle_middle1","od_angle_middle2","od_angle_down",
				"os_angle_up","os_angle_middle0","os_angle_middle1","os_angle_middle2","os_angle_down"};
				
		for(String f:fields) {
			Set<Integer> apptNos =null;
			apptNos = measurementsDao.getAppointmentNosByDemographicNoAndType(Integer.parseInt(demographicNo),f,startDate,endDate);
			appointmentIds.addAll(apptNos);
		}
				
		List<Appointment> appointments = new ArrayList<Appointment>();		
		for(Integer id:appointmentIds) {
			if(appointmentDao.find(id)!=null)
				appointments.add(0,appointmentDao.find(id));
		}
		
		return appointments;
	}
}
