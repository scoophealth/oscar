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

import org.oscarehr.common.model.MdsZRG;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("unchecked")
public class MdsZRGDao extends AbstractDao<MdsZRG>{

	public MdsZRGDao() {
		super(MdsZRG.class);
	}

    public List<Object[]> findById(Integer id) {
		String sql = "SELECT zrg.reportGroupDesc, zrg.reportGroupId, count(zrg.reportGroupID), zrg.reportGroupHeading, zrg.reportSequence " +
				"FROM MdsZRG zrg where zrg.id = :id group by zrg.reportGroupDesc, zrg.reportGroupID " +
				"ORDER BY zrg.reportSequence";
		Query query = entityManager.createQuery(sql);
		query.setParameter("id", id);
		return query.getResultList();
    }

	public List<Object> findReportGroupHeadingsById(Integer id, String reportGroupId) {
		String sql = "SELECT zrg.reportGroupHeading FROM MdsZRG zrg where zrg.id = :id " +
				"AND zrg.reportGroupId = :reportGroupId " +
				"ORDER BY zrg.reportSequence";
		Query query = entityManager.createQuery(sql);
		query.setParameter("id", id);
		query.setParameter("reportGroupId", reportGroupId);
		return query.getResultList();
    }
}
