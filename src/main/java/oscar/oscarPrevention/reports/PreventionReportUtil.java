package oscar.oscarPrevention.reports;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.DemographicArchiveDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.DemographicArchive;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public final class PreventionReportUtil {
	private static Logger logger = MiscUtils.getLogger();

	public static DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");
	public static DemographicArchiveDao demographicArchiveDao = (DemographicArchiveDao) SpringUtils.getBean("demographicArchiveDao");

	public static boolean wasRostered(Integer demographicId, Date onThisDate) {
		logger.debug("Checking rosterd:" + demographicId);
		Demographic demographic = demographicDao.getDemographicById(demographicId);

		if (rosteredDuringThisTimeDemographic(onThisDate, demographic.getRosterDate(), demographic.getRosterTerminationDate())) return (true);

		List<DemographicArchive> archives = demographicArchiveDao.findByDemographicNo(demographicId);
		for (DemographicArchive archive : archives) {
			if (rosteredDuringThisTimeDemographicArchive(onThisDate, archive.getRosterDate(), archive.getRosterTerminationDate())) return (true);
		}

		return (false);
	}
	
	public static boolean wasRosteredToThisProvider(Integer demographicId, Date onThisDate,String providerNo) {
		logger.debug("Checking rosterd:" + demographicId+ " for this date "+onThisDate+" for this providerNo "+providerNo);
		if(providerNo == null){
			return false;
		}
		
		
		Demographic demographic = demographicDao.getDemographicById(demographicId);

		if (rosteredDuringThisTimeDemographic(onThisDate, demographic.getRosterDate(), demographic.getRosterTerminationDate()) && providerNo.equals(demographic.getProviderNo())) return (true);

		List<DemographicArchive> archives = demographicArchiveDao.findByDemographicNo(demographicId);
		for (DemographicArchive archive : archives) {
			if (rosteredDuringThisTimeDemographicArchive(onThisDate, archive.getRosterDate(), archive.getRosterTerminationDate()) && providerNo.equals(demographic.getProviderNo())) return (true);
		}

		return (false);
	}

	private static boolean rosteredDuringThisTimeDemographic(Date onThisDate, Date rosterStart, Date rosterEnd) {

		if (rosterStart != null) {
			if (rosterStart.before(onThisDate)) {
				if (rosterEnd == null || rosterEnd.after(onThisDate)) {
					logger.debug("true:" + onThisDate + ", " + rosterStart + ", " + rosterEnd);
					return (true);
				}
			}
		}

		logger.debug("false:" + onThisDate + ", " + rosterStart + ", " + rosterEnd);
		return (false);
	}

	private static boolean rosteredDuringThisTimeDemographicArchive(Date onThisDate, Date rosterStart, Date rosterEnd) {
		// algorithm for demographic archive must only look at archiv erecords with end dates as the archive is populated upon every change not just people being unrostered.
		if (rosterStart != null && rosterEnd != null) {
			if (rosterStart.before(onThisDate) && rosterEnd.after(onThisDate)) {
				logger.debug("true:" + onThisDate + ", " + rosterStart + ", " + rosterEnd);
				return (true);
			}
		}

		logger.debug("false:" + onThisDate + ", " + rosterStart + ", " + rosterEnd);
		return (false);
	}
}