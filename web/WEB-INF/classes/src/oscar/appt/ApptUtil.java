package oscar.appt;

import javax.servlet.http.HttpServletRequest;

/**
 * This class contains Appointment related presentation layer helper methods.
 * 
 * @author Eugene Petruhin
 * 
 */
public class ApptUtil {

	private static final String SESSION_APPT_BEAN = "apptBean";

	public static void copyAppointmentIntoSession(HttpServletRequest request) {
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
		request.getSession().setAttribute(SESSION_APPT_BEAN, obj);
	}

	public static ApptData getAppointmentFromSession(HttpServletRequest request) {
		return (ApptData) request.getSession().getAttribute(SESSION_APPT_BEAN);
	}

	public static String getColorFromLocation(String site, String colo, String loca) {
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
