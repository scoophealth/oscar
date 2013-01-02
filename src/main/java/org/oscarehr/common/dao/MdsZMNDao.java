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


package org.oscarehr.common.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.MdsZMN;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("unchecked")
public class MdsZMNDao extends AbstractDao<MdsZMN>{

	public MdsZMNDao() {
		super(MdsZMN.class);
	}

	public MdsZMN findBySegmentIdAndReportName(Integer id, String reportName) {
		Query query = createQuery("z", "z.id = :id AND z.reportName = :reportName");
		query.setParameter("id", id);
		query.setParameter("reportName", reportName);
		return getSingleResultOrNull(query);
    }

	public MdsZMN findBySegmentIdAndResultMnemonic(Integer id, String rm) {
	    Query query = createQuery("z", "z.id = :id and z.resultMnemonic = :rm");
	    query.setParameter("id", id);
	    query.setParameter("rm", rm);
	    return getSingleResultOrNull(query);
    }

	
    public List<String> findResultCodes(Integer id, String reportSequence) {		
		String sql = "SELECT zmn.resultCode FROM MdsZMN zmn WHERE zmn.id = :id " +
				"AND zmn.reportGroup = :reportSequence ";
		Query query = entityManager.createQuery(sql);
		query.setParameter("id", id);
		query.setParameter("reportSequence", reportSequence);
		List<Object[]> resultCodes = query.getResultList();
		List<String> result = new ArrayList<String>(resultCodes.size());
		for(Object[] o : resultCodes) {
			result.add(String.valueOf(o[0]));
		}
		return result;
    }
}