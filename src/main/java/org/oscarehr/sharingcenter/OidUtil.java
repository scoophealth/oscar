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

package org.oscarehr.sharingcenter;

import org.oscarehr.sharingcenter.dao.ClinicInfoDao;
import org.oscarehr.sharingcenter.model.ClinicInfoDataObject;
import org.oscarehr.util.SpringUtils;

public class OidUtil {
	
	private static final ClinicInfoDao clinicInfoDao = SpringUtils.getBean(ClinicInfoDao.class);
	private static final ClinicInfoDataObject clinicData = clinicInfoDao.getClinic();
	
	/**
	 * Generates an OID based on the XDS Source ID (root)
	 * 
	 * @param oidType
	 * @param oidParts
	 * @return an OID
	 */
	public static String getOid(OidType oidType, String... oidParts) {
		return getOid(oidType, oidParts);
	}
	
	/**
	 * Generates an OID based on the XDS Source ID (root) and appending the variable string arguments to the OID
	 * 
	 * @param oidParts
	 * @return an OID
	 */
	public static String getOid(String... oidParts) {
		StringBuilder retVal = new StringBuilder();
		
		retVal.append(cleanUp(clinicData.getSourceId()));
		for (int i = 0; i < oidParts.length; i++) {
			String part = cleanUp(oidParts[i]);
			
			if (!part.isEmpty())
				retVal.append(String.format(".%s", part));
		}
		
		return retVal.toString();
	}
	
	/**
	 * Cleans up the oid (by removing all periods from the beginning and end of the oid)
	 * @param oid
	 * @return a clean OID
	 */
	private static String cleanUp(String oid) {
		String retVal = oid.trim();
		
		retVal = retVal.startsWith(".") ? retVal.substring(1) : retVal;
		retVal = retVal.endsWith(".") ? retVal.substring(0, retVal.length()-1) : retVal;
		
		if (retVal.startsWith(".") || retVal.endsWith(".")) {
			retVal = cleanUp(retVal);
		}
		
		return retVal;
	}
	
}
