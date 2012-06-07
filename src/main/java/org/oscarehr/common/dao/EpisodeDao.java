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

import java.util.Collection;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.Episode;
import org.springframework.stereotype.Repository;

@Repository
public class EpisodeDao extends AbstractDao<Episode>{

	public EpisodeDao() {
		super(Episode.class);
	}

	public List<Episode> findAll(Integer demographicNo) {
		Query query = entityManager.createQuery("SELECT e FROM Episode e WHERE e.demographicNo=? ORDER BY e.startDate DESC");
		query.setParameter(1,demographicNo);
		@SuppressWarnings("unchecked")
        List<Episode> results = query.getResultList();
		return results;
	}
	
	public List<Episode> findAllCurrent(Integer demographicNo) {
		Query query = entityManager.createQuery("SELECT e FROM Episode e WHERE e.status='Current' AND e.demographicNo=? ORDER BY e.startDate DESC");
		query.setParameter(1,demographicNo);
		@SuppressWarnings("unchecked")
        List<Episode> results = query.getResultList();
		return results;
	}
	
	public List<Episode> findCurrentByCodeTypeAndCodes(Integer demographicNo, String codeType, Collection<String> codes) {
		Query query = entityManager.createQuery("SELECT e FROM Episode e WHERE e.status='Current' AND e.demographicNo=:demographicNo AND e.codingSystem=:codingSystem AND e.code IN (:codes) ORDER BY e.startDate DESC");
		query.setParameter("demographicNo",demographicNo);
		query.setParameter("codingSystem", codeType);
		query.setParameter("codes",codes);
		@SuppressWarnings("unchecked")
        List<Episode> results = query.getResultList();
		return results;
	}
	
	public List<Episode> findCompletedByCodeTypeAndCodes(Integer demographicNo, String codeType, Collection<String> codes) {
		Query query = entityManager.createQuery("SELECT e FROM Episode e WHERE e.status='Complete' AND e.demographicNo=:demographicNo AND e.codingSystem=:codingSystem AND e.code IN (:codes) ORDER BY e.startDate DESC");
		query.setParameter("demographicNo",demographicNo);
		query.setParameter("codingSystem", codeType);
		query.setParameter("codes",codes);
		@SuppressWarnings("unchecked")
        List<Episode> results = query.getResultList();
		return results;
	}
}
