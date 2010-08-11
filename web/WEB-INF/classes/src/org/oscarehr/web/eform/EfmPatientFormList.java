package org.oscarehr.web.eform;

import org.apache.commons.lang.StringUtils;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;

public final class EfmPatientFormList {
	
	private static DemographicDao demographicDao=(DemographicDao) SpringUtils.getBean("demographicDao");
	
	private EfmPatientFormList() {
		// not meant to instantiate this
	}
	
	/**
	 * MyOscar is only available if 2 conditions are met :
	 * - oscar.properties must have MY_OSCAR=yes
	 * - the given demographic must have a myoscar account i.e. demographic.pin
	 */
	public static boolean isMyOscarAvailable(int demographicId)
	{
        String myOscar = OscarProperties.getInstance().getProperty("MY_OSCAR");
        if (!"yes".equals(myOscar)) return(false);
		
        Demographic demographic=demographicDao.getDemographicById(demographicId);
        if (demographic!=null)
        {
        	String temp=StringUtils.trimToNull(demographic.getPin());
        	return(temp!=null);
        }
        
		return(false);
	}
}
