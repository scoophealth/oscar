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
package org.oscarehr.common.merge;

import java.util.List;

import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.util.SpringUtils;

/**
 * Base template class encapsulating common functionality used by extending template components.  
 */
public abstract class AbstractTemplate {

	/**
	 * Loads all merged ids for the specified demographic number
	 * 
	 * @param demographic_no
	 * 		Demographic number to fetch the merged ids for.
	 * @return
	 * 		Returns the list of demographic IDs of the records merged to the 
	 * 		master record (specified by demographic_no parameter). 
	 */
	protected List<Integer> getMergedIds(Integer demographic_no) {
		DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
		List<Integer> mergedIds = demographicDao.getMergedDemographics(demographic_no);
		return mergedIds;
	}

}
