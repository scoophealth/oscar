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

import java.util.HashMap;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.PreventionExt;
import org.springframework.stereotype.Repository;

@Repository
public class PreventionExtDao extends AbstractDao<PreventionExt> {

	public PreventionExtDao() {
		super(PreventionExt.class);
	}
	
	public List<PreventionExt> findByPreventionId(Integer preventionId) {
		Query query = entityManager.createQuery("select x from PreventionExt x where preventionId=?1");
		query.setParameter(1, preventionId);

		@SuppressWarnings("unchecked")
		List<PreventionExt> results = query.getResultList();

		return (results);
	}
	
	public List<PreventionExt> findByKeyAndValue(String key, String value) {
		Query query = entityManager.createQuery("select x from PreventionExt x where keyval=?1 and val=?2");
		query.setParameter(1, key);
		query.setParameter(2, value);
		
		@SuppressWarnings("unchecked")
		List<PreventionExt> results = query.getResultList();

		return (results);
	}
	
	public List<PreventionExt> findByPreventionIdAndKey(Integer preventionId, String key) {
		Query query = entityManager.createQuery("select x from PreventionExt x where preventionId=?1 and keyval=?2");
		query.setParameter(1, preventionId);
		query.setParameter(2, key);
		
		@SuppressWarnings("unchecked")
		List<PreventionExt> results = query.getResultList();

		return (results);
	}
	
	public HashMap<String, String> getPreventionExt(Integer preventionId) {
		HashMap<String, String> results=new HashMap<String, String>();
		
		List<PreventionExt> preventionExts = findByPreventionId(preventionId);
		for (PreventionExt preventionExt : preventionExts) {
			results.put(preventionExt.getkeyval(), preventionExt.getVal());
		}

		return results;
	}
	
	public int updateKeyValue(int preventionId, String keyval, String val) {
		Query q = entityManager.createQuery("update PreventionExt set val=?1 where preventionId =?2 and keyval=?3");
		q.setParameter(1, val);
		q.setParameter(2, preventionId);
		q.setParameter(3, keyval);
		
		return q.executeUpdate();
	}
}
