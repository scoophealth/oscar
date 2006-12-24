package oscar.oscarBilling.ca.on.pageUtil;

import oscar.OscarProperties;
import oscar.appt.JdbcApptImpl;

public class BillingSiteIdPrep {
	private final String NO_SITE = "NONE";
	//private static final Logger _logger = Logger.getLogger(BillingSiteIdPrep.class);
	JdbcApptImpl dbObj = new JdbcApptImpl();

	public String[] getSiteList() {
		OscarProperties props = OscarProperties.getInstance();
		String[] ret = props.getProperty("scheduleSiteID", "").split("\\|");
		return ret;
	}

	public String getSuggestSite(String[] siteList, String thisSite, String thisServiceDate, String provider_no) {
		String ret = "";
		if (!thisSite.equals(NO_SITE)) {
			for (int i = 0; i < siteList.length; i++) {
				if (thisSite.equals(siteList[i])) {
					ret = siteList[i];
					break;
				}
			}
		} else {
			// get the previews appt date
			String prevApptDate = dbObj.getPrevApptDate(thisServiceDate);
			ret = dbObj.getLocationFromSchedule(prevApptDate, provider_no);
		}
		return ret;
	}
}
