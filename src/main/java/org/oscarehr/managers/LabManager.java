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
package org.oscarehr.managers;

import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.Hl7TextInfoDao;
import org.oscarehr.common.dao.Hl7TextMessageDao;
import org.oscarehr.common.model.Hl7TextMessage;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oscar.log.LogAction;

@Service
public class LabManager {

	private Logger logger = MiscUtils.getLogger();

	@Autowired
	Hl7TextInfoDao hl7textInfoDao;

	@Autowired
	Hl7TextMessageDao hl7TextMessageDao;
	
	@Autowired
	SecurityInfoManager securityInfoManager;

	public List<Hl7TextMessage> getHl7Messages(LoggedInInfo loggedInInfo, Integer demographicNo, int offset, int limit) {
		checkPrivilege(loggedInInfo, "r");
		
		LogAction.addLogSynchronous(loggedInInfo, "LabManager.getHl7Messages", "demographicNo="+demographicNo);
		
		List<Hl7TextMessage> results = hl7TextMessageDao.findByDemographicNo(demographicNo, offset, limit);

		return results;
	}

	private void checkPrivilege(LoggedInInfo loggedInInfo, String privilege) {
		if (!securityInfoManager.hasPrivilege(loggedInInfo, "_lab", privilege, null)) {
			throw new RuntimeException("missing required security object (_lab)");
		}
	}
}
