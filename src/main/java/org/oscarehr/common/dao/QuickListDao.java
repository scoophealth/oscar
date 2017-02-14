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

import org.oscarehr.common.NativeSql;
import org.oscarehr.common.model.QuickList;
import org.springframework.stereotype.Repository;

@Repository
public class QuickListDao extends AbstractDao<QuickList>{

	public QuickListDao() {
		super(QuickList.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<QuickList> findAll() {
		Query query = createQuery("x", null);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Object> findDistinct() {
    	Query query = entityManager.createQuery("select distinct ql.quickListName from QuickList ql");
    	return query.getResultList();
	}

	@SuppressWarnings("unchecked")
    public List<QuickList> findByNameResearchCodeAndCodingSystem(String quickListName, String researchCode, String codingSystem) {
	    Query query = entityManager.createQuery("from QuickList q where q.quickListName = :qlName AND q.dxResearchCode = :rc AND q.codingSystem = :cs");
	    query.setParameter("qlName", quickListName);
	    query.setParameter("rc", researchCode);
	    query.setParameter("cs", codingSystem);
	    return query.getResultList();
    }

	@SuppressWarnings("unchecked")
	public List<QuickList> findByCodingSystem(String codingSystem) {
		String csQuery = "";
        if ( codingSystem != null ){
        	csQuery = " WHERE ql.codingSystem = :cs";
        }
		Query query = entityManager.createQuery("select ql from QuickList ql " + csQuery + " GROUP BY ql.quickListName");
		if (codingSystem != null) {
			query.setParameter("cs", codingSystem);
		}
		return query.getResultList();
    }

	@NativeSql
	@SuppressWarnings("unchecked")
	public List<Object[]> findResearchCodeAndCodingSystemDescriptionByCodingSystem(String codingSystem, String quickListName) {
		try {
        	String sql = "Select q.dxResearchCode, c.description FROM quickList q, "+codingSystem
					+" c where codingSystem = '"+codingSystem+"' and quickListName='"+ quickListName +"' AND c."+codingSystem
					+" = q.dxResearchCode order by c.description";
			Query query = entityManager.createNativeQuery(sql);
			return query.getResultList();
		} catch (Exception e) {
			// TODO replace when test ignores are merged
			return new ArrayList<Object[]>();
		}
		
    }
}
