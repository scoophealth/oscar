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

package org.oscarehr.ticklers.service;

import java.util.List;

import org.oscarehr.common.PaginationQuery;
import org.oscarehr.common.model.Tickler;
import org.oscarehr.ticklers.web.TicklerQuery;
import org.oscarehr.util.LoggedInInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import oscar.log.LogAction;

@Component
public class TicklerService {
	@Autowired
	private TicklersDao TicklerDao;

	/**
	 * Use to get ticklers count for pagination display
	 * @param paginationQuery
	 * @return
	 */
    public int getTicklersCount(PaginationQuery paginationQuery) {
	    return this.TicklerDao.getTicklersCount(paginationQuery);
    }
	
    /**
     * List ticklers
     * @param paginationQuery
     * @return
     */
	public List<Tickler> getTicklers(LoggedInInfo loggedInInfo, PaginationQuery paginationQuery) {
		TicklerQuery query = (TicklerQuery) paginationQuery;

		List<Tickler> results = TicklerDao.getTicklers(query);
		//--- log action ---
		if (results.size()>0) {
			String resultIds=Tickler.getIdsAsStringList(results);
			LogAction.addLogSynchronous(loggedInInfo, "TicklerService.getTicklers", "ids returned=" + resultIds);
		}
				
		return results;
	}
}
