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

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.Allergy;

public class AllergyDao extends AbstractDao<Allergy> {

	public AllergyDao() {
		super(Allergy.class);
	}

	/**
	 * This method will return a list of allergies starting from the provided ID.
	 * It is an efficient method for iterating through all Allergies (more efficient than using a startIndex).
	 *
	 * @param archived can be null for all deleted and non-deleted items 
	 */
    public List<Allergy> findAllergiesByIdStart(Boolean archived, Integer startIdInclusive, int itemsToReturn) {
    	String sql = "select x from "+modelClass.getSimpleName()+" x where x.id>=?1 ";
    	if (archived!=null)	sql=sql+"and x.archived=?2 ";
    	sql=sql+"order by x.id";
    	
    	Query query = entityManager.createQuery(sql);
    	query.setParameter(1, startIdInclusive);
    	if (archived!=null)	query.setParameter(2, archived);
    	
    	setLimit(query, itemsToReturn);

        @SuppressWarnings("unchecked")
        List<Allergy> allergies = query.getResultList();
        return allergies;
    }
    
    public List<Allergy> findAllergies(Integer demographic_no) {
    	String sql = "select x from "+modelClass.getSimpleName()+" x where x.demographicNo=?1 order by x.archived,x.severityOfReaction desc";
    	Query query = entityManager.createQuery(sql);
    	query.setParameter(1,demographic_no);

        @SuppressWarnings("unchecked")
        List<Allergy> allergies = query.getResultList();
        return allergies;
    }

    public List<Allergy> findActiveAllergies(Integer demographic_no) {
    	String sql = "select x from "+modelClass.getSimpleName()+" x where x.demographicNo=?1 and x.archived = 0 order by x.position, x.severityOfReaction";
    	Query query = entityManager.createQuery(sql);
    	query.setParameter(1,demographic_no);

        @SuppressWarnings("unchecked")
        List<Allergy> allergies = query.getResultList();
        return allergies;
    }

    public List<Allergy> findActiveAllergiesOrderByDescription(Integer demographic_no) {
    	String sql = "select x from "+modelClass.getSimpleName()+" x where x.demographicNo=?2 and x.archived = 0 order by x.description";
    	Query query = entityManager.createQuery(sql);
    	query.setParameter(2,demographic_no);

        @SuppressWarnings("unchecked")
        List<Allergy> allergies = query.getResultList();
        return allergies;
    }

	public List<Allergy> findByDemographicIdUpdatedAfterDate(Integer demographicId, Date updatedAfterThisDate) {
		String sqlCommand = "select x from "+modelClass.getSimpleName()+" x where x.demographicNo=?1 and x.lastUpdateDate>?2";

		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, demographicId);
		query.setParameter(2, updatedAfterThisDate);

		@SuppressWarnings("unchecked")
		List<Allergy> results = query.getResultList();

		return (results);
	}

	public Integer getMaxPosition() {
		String sqlCommand = "select MAX(position) from Allergy ";

		Query query = entityManager.createQuery(sqlCommand);

		Integer pos = (Integer)query.getSingleResult();

		return pos;
	}

}
