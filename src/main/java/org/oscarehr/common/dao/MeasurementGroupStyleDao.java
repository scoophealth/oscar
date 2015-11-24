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

import org.oscarehr.common.model.MeasurementGroupStyle;
import org.springframework.stereotype.Repository;

@Repository
public class MeasurementGroupStyleDao extends AbstractDao<MeasurementGroupStyle> {

	public MeasurementGroupStyleDao() {
		super(MeasurementGroupStyle.class);
	}

	@SuppressWarnings("unchecked")
	public List<MeasurementGroupStyle> findAll() {
		Query query = entityManager.createQuery("SELECT x FROM " + modelClass.getSimpleName() + " x");
		List<MeasurementGroupStyle> results = query.getResultList();
		return results;
	}
	
	public List<MeasurementGroupStyle> findByGroupName(String groupName) {
		String sql = "select x from MeasurementGroupStyle x where x.groupName=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, groupName);

		@SuppressWarnings("unchecked")
		List<MeasurementGroupStyle> results = query.getResultList();
		return results;
	}
	
	@SuppressWarnings("unchecked")
	public List<MeasurementGroupStyle> findByCssId(Integer cssId) {
		Query query = createQuery("m", "m.cssId = :cssId");
		query.setParameter("cssId", cssId);
		return query.getResultList();
	}

}
