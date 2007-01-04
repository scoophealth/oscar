package oscar.appt;

import javax.servlet.http.HttpServletRequest;

public class ApptOpt {
	JdbcApptImpl dbObj = new JdbcApptImpl();

	public boolean cutAppointment(HttpServletRequest request) {
		boolean ret = false;
		// set up appt bean String appointment_no
		ApptData obj = new ApptData();
		obj.setAppointment_date(request.getParameter("appointment_date"));
		obj.setStart_time(request.getParameter("start_time"));
		obj.setEnd_time(request.getParameter("end_time"));
		obj.setName(request.getParameter("keyword"));
		obj.setDemographic_no(request.getParameter("demographic_no"));
		obj.setNotes(request.getParameter("notes"));
		obj.setReason(request.getParameter("reason"));
		obj.setLocation(request.getParameter("location"));
		obj.setResources(request.getParameter("resources"));
		obj.setType(request.getParameter("type"));
		obj.setStyle(request.getParameter("style"));
		obj.setBilling(request.getParameter("billing"));
		obj.setStatus(request.getParameter("status"));
		obj.setRemarks(request.getParameter("remarks"));
		obj.setDuration(request.getParameter("duration"));
		obj.setChart_no(request.getParameter("chart_no"));
		// set up session bean
		request.getSession().setAttribute("apptBean", obj);

		// delete the current appt
		ret = dbObj.deleteAppt(request.getParameter("appointment_no"));
		return ret;
	}

	public boolean copyAppointment(HttpServletRequest request) {
		boolean ret = true;
		// set up appt bean String appointment_no
		ApptData obj = new ApptData();
		obj.setAppointment_date(request.getParameter("appointment_date"));
		obj.setStart_time(request.getParameter("start_time"));
		obj.setEnd_time(request.getParameter("end_time"));
		obj.setName(request.getParameter("keyword"));
		obj.setDemographic_no(request.getParameter("demographic_no"));
		obj.setNotes(request.getParameter("notes"));
		obj.setReason(request.getParameter("reason"));
		obj.setLocation(request.getParameter("location"));
		obj.setResources(request.getParameter("resources"));
		obj.setType(request.getParameter("type"));
		obj.setStyle(request.getParameter("style"));
		obj.setBilling(request.getParameter("billing"));
		obj.setStatus(request.getParameter("status"));
		obj.setRemarks(request.getParameter("remarks"));
		obj.setDuration(request.getParameter("duration"));
		obj.setChart_no(request.getParameter("chart_no"));
		// set up session bean
		request.getSession().setAttribute("apptBean", obj);

		return ret;
	}

	public ApptData getApptObj(HttpServletRequest request) {
		// get appt bean
		ApptData obj = (ApptData) request.getSession().getAttribute("apptBean");
		return obj;
	}

	public String getLocationFromSchedule(String apptDate, String provider_no) {
		String ret = dbObj.getLocationFromSchedule(apptDate, provider_no);
		return ret;
	}

	public String getColorFromLocation(String site, String colo, String loca) {
		String ret = "white";
		String[] s = site.split("\\|");
		String[] c = colo.split("\\|");
		for (int i = 0; i < site.length(); i++) {
			if (s[i].startsWith(loca)) {
				ret = c[i];
				break;
			}
		}
		return ret;
	}

}
