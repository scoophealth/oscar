package org.oscarehr.measurements.web;

import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarEncounter.oscarMeasurements.dao.MeasurementsDao;
import oscar.oscarEncounter.oscarMeasurements.model.Measurements;

public class MeasurementDataAction extends DispatchAction {

	private static Logger logger = MiscUtils.getLogger();	
	private static MeasurementsDao measurementsDao = (MeasurementsDao) SpringUtils.getBean("measurementsDao");
	
	public ActionForward getLatestValues(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String demographicNo = request.getParameter("demographicNo");
		String typeStr = request.getParameter("types");
		String appointmentNo = request.getParameter("appointmentNo");
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
		
		StringBuilder script = new StringBuilder();
		for(String key:measurementMap.keySet()) {
			Measurements value = measurementMap.get(key);
			if((freshMap.get(key)==null) ||(freshMap.get(key) != null && value.getAppointmentNo() == Integer.parseInt(appointmentNo))) {
				script.append("jQuery(\"input[measurement='"+key+"']\").val(\""+value.getDataField()+"\");\n");
				script.append("jQuery(\"textarea[measurement='"+key+"']\").val(\""+value.getDataField()+"\");\n");	
			}			
		}
		
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