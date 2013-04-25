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
import org.oscarehr.common.model.Ichppccode;
import org.springframework.stereotype.Repository;

@Repository
public class IchppccodeDao extends AbstractCodeSystemDao<Ichppccode>{

	public IchppccodeDao() {
		super(Ichppccode.class);
	}
	
	/**
	 * this is ok here, only 440 entries in this code..i wouldn't put this in icd9, or snomed.
	 */
	@SuppressWarnings("unchecked")
	public List<Ichppccode> findAll() {
		Query query = createQuery("x", null);
		return query.getResultList();
	}
	
	public List<Ichppccode> getIchppccodeCode(String term){
		Query query = entityManager.createQuery("select i from Ichppccode i where i.id=?");
		query.setParameter(1, term);

		@SuppressWarnings("unchecked")
		List<Ichppccode> results = query.getResultList();

		return results;
	}


    public List<Ichppccode> getIchppccode(String query) {
		Query q = entityManager.createQuery("select i from Ichppccode i where i.id like ? or i.description like ? order by i.description");
		q.setParameter(1, "%"+query+"%");
		q.setParameter(2, "%"+query+"%");

		@SuppressWarnings("unchecked")
		List<Ichppccode> results = q.getResultList();

		return results;
    }

	@Override
    public List<Ichppccode> searchCode(String term) {
	    return getIchppccode(term);
    }
	
	@Override
    public Ichppccode findByCode(String code) {
		List<Ichppccode> results = getIchppccodeCode(code);
		if(results.isEmpty())
			return null;
		return results.get(0);
	}
	
	@Override
	public AbstractCodeSystemModel<?> findByCodingSystem(String codingSystem) {
		Query query = entityManager.createQuery("FROM Ichppccode i WHERE i.id like :cs");
		query.setParameter("cs", codingSystem);
		query.setMaxResults(1);

		return find(codingSystem);
	}
	
	@SuppressWarnings("unchecked")
	public List<Ichppccode> search_research_code(String code, String code1, String code2, String desc, String desc1, String desc2) {
		Query query = entityManager.createQuery("select i from Ichppccode i where i.id like ? or i.id like ? or i.id like ? or i.description like ? or i.description like ? or i.description like ?");
		query.setParameter(1, code);
		query.setParameter(2, code1);
		query.setParameter(3, code2);
		query.setParameter(4, desc);
		query.setParameter(5, desc1);
		query.setParameter(6, desc2);
		
		return query.getResultList();
	}
}
