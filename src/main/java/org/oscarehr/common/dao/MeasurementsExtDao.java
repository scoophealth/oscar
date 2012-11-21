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

import org.oscarehr.common.model.MeasurementsExt;
import org.springframework.stereotype.Repository;

@Repository
public class MeasurementsExtDao extends AbstractDao<MeasurementsExt>{

	public MeasurementsExtDao() {
		super(MeasurementsExt.class);
	}
	
	public List<MeasurementsExt> getMeasurementsExtByMeasurementId(Integer measurementId) {
		String queryStr = "select m FROM MeasurementsExt m WHERE m.measurementId = ?1";
		Query q = entityManager.createQuery(queryStr);
		q.setParameter(1, measurementId);
		
		@SuppressWarnings("unchecked")
		List<MeasurementsExt> rs = q.getResultList();

		return rs;
	}
	
	public MeasurementsExt getMeasurementsExtByMeasurementIdAndKeyVal(Integer measurementId, String keyVal) {
		String queryStr = "select m FROM MeasurementsExt m WHERE m.measurementId = ?1 AND m.keyVal = ?2";
		Query q = entityManager.createQuery(queryStr);
		q.setParameter(1, measurementId);
		q.setParameter(2, keyVal);
		
		@SuppressWarnings("unchecked")
		List<MeasurementsExt> rs = q.getResultList();

		if(rs.isEmpty()) {
			return null;
		}
		return rs.get(0);
	}
	
	public Integer getMeasurementIdByKeyValue(String key, String value) {
		String queryStr = "select m FROM MeasurementsExt m WHERE m.keyVal=?1 AND m.val=?2";
		Query q = entityManager.createQuery(queryStr);
		q.setParameter(1, key);
		q.setParameter(2, value);
		
		@SuppressWarnings("unchecked")
		List<MeasurementsExt> rs =q.getResultList();
		
		if (rs.size()>0) return rs.get(0).getMeasurementId();
		return null;
	}
	
	public List<MeasurementsExt> findByKeyValue(String key, String value) {
		String queryStr = "select m FROM MeasurementsExt m WHERE m.keyVal=?1 AND m.val=?2";
		Query q = entityManager.createQuery(queryStr);
		q.setParameter(1, key);
		q.setParameter(2, value);
		
		@SuppressWarnings("unchecked")
		List<MeasurementsExt> rs = q.getResultList();
		
		return rs;
	}
}
