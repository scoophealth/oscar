/**
 * Copyright (c) 2013-2015. Department of Computer Science, University of Victoria. All Rights Reserved.
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
 * Department of Computer Science
 * LeadLab
 * University of Victoria
 * Victoria, Canada
 */

package org.oscarehr.dashboard.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.oscarehr.common.dao.DemographicExtDao;
import org.oscarehr.util.SpringUtils;

public class ExcludeDemographicHandler {
	
	static DemographicExtDao demographicExtDao = SpringUtils.getBean(DemographicExtDao.class);
	List<Integer> demoIds;
	
	public List<Integer> getDemoIds(String indicatorName) {
		demoIds = demographicExtDao.findDemographicIdsByKeyVal("excludeIndicator", indicatorName);
		return demoIds;
	}
	
	public Map<Integer,Boolean> getDemoIdMap() {
		Map<Integer,Boolean> map = new HashMap<Integer,Boolean>();
		for(Integer demoId:demoIds) {
			if(true) { //patientScreenedInLastYear(demoId)) {
				map.put(demoId, true);
			}
		}
		//r.setAboriginalScreened1yr(map.keySet().size());
		return map;
	}
	
	public void excludeDemoId(Integer demographicNo, String indicatorName) {
		demographicExtDao.saveDemographicExt(demographicNo, "excludeIndicator", indicatorName);
	}
}
