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

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.AbstractCodeSystemModel;
import org.oscarehr.common.model.SnomedCore;
import org.springframework.stereotype.Repository;

@Repository
public class SnomedCoreDao extends AbstractCodeSystemDao<SnomedCore> {

	public SnomedCoreDao() {
		super(SnomedCore.class);
	}

	public List<SnomedCore> getSnomedCoreCode(String snomedCoreCode){
		Query query = entityManager.createQuery("select i from SnomedCore i where i.snomedCore=?");
		query.setParameter(1, snomedCoreCode);

		@SuppressWarnings("unchecked")
		List<SnomedCore> results = query.getResultList();

		return results;
	}


    public List<SnomedCore> getSnomedCore(String query) {
		Query q = entityManager.createQuery("select i from SnomedCore i where i.snomedCore like ? or i.description like ? order by i.description");
		q.setParameter(1, "%"+query+"%");
		q.setParameter(2, "%"+query+"%");

		@SuppressWarnings("unchecked")
		List<SnomedCore> results = q.getResultList();

		return results;
    }

    public List<SnomedCore> searchCode(String term) {
    	return getSnomedCore(term);
    }
    
    @Override
    public SnomedCore findByCode(String code) {
    	List<SnomedCore> results = getSnomedCoreCode(code);
    	if(results.isEmpty())
    		return null;
    	return results.get(0);
    }

	@Override
    public AbstractCodeSystemModel<?> findByCodingSystem(String codingSystem) {
		Query query = entityManager.createQuery("FROM SnomedCore s WHERE s.snomedCore like :cs");
		query.setParameter("cs", codingSystem);
		query.setMaxResults(1);
		return getSingleResultOrNull(query);
    }
}
