/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.common.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.OcanStaffFormData;
import org.springframework.stereotype.Repository;

@Repository
public class OcanStaffFormDataDao extends AbstractDao<OcanStaffFormData>{

	public OcanStaffFormDataDao() {
		super(OcanStaffFormData.class);
	}
	
	public List<OcanStaffFormData> findByQuestion(Integer ocanStaffFormId, String question) {

		String sqlCommand = "select x from OcanStaffFormData x where x.ocanStaffFormId=?1 and x.question=?2";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, ocanStaffFormId);
		query.setParameter(2, question);

		@SuppressWarnings("unchecked")
		List<OcanStaffFormData> results=query.getResultList();
		
		return (results);
	}
	
	public OcanStaffFormData findLatestByQuestion(Integer ocanStaffFormId, String question) {

		String sqlCommand = "select x from OcanStaffFormData x where x.ocanStaffFormId=?1 and x.question=?2 order by ocanStaffFormId Desc";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, ocanStaffFormId);
		query.setParameter(2, question);

		@SuppressWarnings("unchecked")
		List<OcanStaffFormData> results=query.getResultList();
		
		if(results.isEmpty()) {
			return null;
		} else {
			return results.get(0);
		}
			
		
	}
	
	public List<OcanStaffFormData> findByForm(Integer ocanStaffFormId) {

		String sqlCommand = "select x from OcanStaffFormData x where x.ocanStaffFormId=?1";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, ocanStaffFormId);
	
		@SuppressWarnings("unchecked")
		List<OcanStaffFormData> results=query.getResultList();
		
		return (results);
	}

    /**
     * Generally speaking this method is good for getting a form if
     * the answer is a CDS category, i.e. "019-04", this method is not
     * useful for answers which are random strings like "days hosipitalised"
     * where the answer may conflict with other numeric answers.
     */
    public OcanStaffFormData findByAnswer(Integer ocanStaffFormId, String answer) {

		String sqlCommand = "select x from OcanStaffFormData x where x.ocanStaffFormId=?1 and x.answer=?2";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, ocanStaffFormId);
		query.setParameter(2, answer);

		return (getSingleResultOrNull(query));
	}

    
}
