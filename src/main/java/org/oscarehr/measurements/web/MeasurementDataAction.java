package org.oscarehr.measurements.web;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.dao.OscarAppointmentDao;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarEncounter.oscarMeasurements.dao.MeasurementsDao;
import oscar.oscarEncounter.oscarMeasurements.model.Measurements;

public class MeasurementDataAction extends DispatchAction {

	private static Logger logger = MiscUtils.getLogger();	
	private static MeasurementsDao measurementsDao = (MeasurementsDao) SpringUtils.getBean("measurementsDao");
	OscarAppointmentDao appointmentDao = (OscarAppointmentDao)SpringUtils.getBean("oscarAppointmentDao");
	
	public ActionForward getLatestValues(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String demographicNo = request.getParameter("demographicNo");
		String typeStr = request.getParameter("types");
		String appointmentNo = request.getParameter("appointmentNo");
		int apptNo = 0;
		if(appointmentNo != null && appointmentNo.length()>0) {
			apptNo = Integer.parseInt(appointmentNo);
		}
		
		int prevApptNo = 0;
		if(apptNo > 0) {
			List<Appointment> appts = appointmentDao.getAppointmentHistory(Integer.parseInt(demographicNo));
			for(int x=0;x<appts.size();x++) {
				Appointment appt = appts.get(x);
				if(appt.getId().intValue() == apptNo && x <= appts.size()-1) {
					prevApptNo = appts.get(x+1).getId();
				}
			}
		}
		
		
		String fresh =request.getParameter("fresh");
		HashMap<String,Boolean> freshMap = new HashMap<String,Boolean>();
		if(fresh!=null) {
			String tmp[] = fresh.split(",");
			for(int x=0;x<tmp.length;x++) {
				freshMap.put(tmp[x],true);
			}
		}
		if(typeStr == null || typeStr.length() == 0) {
			//error
		}
		String[] types = typeStr.split(",");
		
		Map<String,Measurements> measurementMap = measurementsDao.getMeasurements(demographicNo,types);
		
		Date nctTs = null;
		Date applanationTs=null;
		
		StringBuilder script = new StringBuilder();
		for(String key:measurementMap.keySet()) {
			Measurements value = measurementMap.get(key);
			if((freshMap.get(key)==null) ||(freshMap.get(key) != null && value.getAppointmentNo() == Integer.parseInt(appointmentNo))) {
				script.append("jQuery(\"[measurement='"+key+"']\").val(\""+value.getDataField()+"\");\n");
				if(apptNo>0 && apptNo == value.getAppointmentNo()) {
					script.append("jQuery(\"[measurement='"+key+"']\").addClass('examfieldwhite');\n");
				}
				if(prevApptNo>0 && value.getAppointmentNo() == prevApptNo) {
					script.append("jQuery(\"[measurement='"+key+"']\").attr('prev_appt','true');\n");
				}
				if(apptNo>0 && value.getAppointmentNo() == apptNo) {
					script.append("jQuery(\"[measurement='"+key+"']\").attr('current_appt','true');\n");
				}
				if(key.equals("os_iop_applanation") || key.equals("od_iop_applanation")) {
					if(applanationTs == null) {
						applanationTs = value.getDateObserved();
					} else if(value.getDateObserved().after(applanationTs)) {
						applanationTs = value.getDateObserved();
					}
				}
				if(key.equals("os_iop_nct") || key.equals("od_iop_nct")) { 
					if(nctTs == null) {
						nctTs = value.getDateObserved();
					} else if(value.getDateObserved().after(nctTs)) {
						nctTs = value.getDateObserved();
					}					
				}
			}			
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		if(applanationTs!=null)
			script.append("jQuery(\"#applanation_ts\").html('"+sdf.format(applanationTs)+"');\n");
		if(nctTs != null)
			script.append("jQuery(\"#nct_ts\").html('"+sdf.format(nctTs)+"');\n");
		
		response.getWriter().print(script);
		return null;
	}
	
	
	public ActionForward saveValues(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String demographicNo = request.getParameter("demographicNo");
		String providerNo = LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo();
		String strAppointmentNo = request.getParameter("appointmentNo");
		int appointmentNo = Integer.parseInt(strAppointmentNo);
		
		Enumeration e = request.getParameterNames();
		Map<String,String> measurements = new HashMap<String,String>();
		
		while(e.hasMoreElements()) {
			String key = (String)e.nextElement();
			String values[] = request.getParameterValues(key);
			if(key.equals("action") || key.equals("demographicNo") || key.equals("appointmentNo"))
				continue;
			if(values.length>0 && values[0]!=null && values[0].length()>0) {
				measurements.put(key,values[0]);
				Measurements m = new Measurements();
				m.setComments("");
				m.setDataField(values[0]);
				m.setDateEntered(new Date());
				m.setDateObserved(new Date());
				m.setDemographicNo(Integer.parseInt(demographicNo));
				m.setMeasuringInstruction("");
				m.setProviderNo(providerNo);
				m.setType(key);
				m.setAppointmentNo(appointmentNo);
				measurementsDao.addMeasurements(m);
			}
		}
		
		
		return null;
	}
}