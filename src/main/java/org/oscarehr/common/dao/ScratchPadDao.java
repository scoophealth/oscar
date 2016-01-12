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

import org.oscarehr.common.model.ScratchPad;
import org.springframework.stereotype.Repository;

@Repository
public class ScratchPadDao extends AbstractDao<ScratchPad> {

	public ScratchPadDao() {
		super(ScratchPad.class);
	}

	public boolean isScratchFilled(String providerNo) {
		String sSQL = "SELECT s FROM ScratchPad s WHERE s.providerNo = ? AND status=1 order by s.id";
		Query query = entityManager.createQuery(sSQL);
		query.setParameter(1, providerNo);

		@SuppressWarnings("unchecked")
		List<ScratchPad> results = query.getResultList();
		if (results.size() > 0 && results.get(0).getText().trim().length() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * Finds scratch pad for the specified provider.
	 * 
	 * @param providerNo Provider id to find the scratch pad for
	 * @return
	 * 		Returns the scratch pad for the specified id. 
	 */
	public ScratchPad findByProviderNo(String providerNo) {
		Query query = createQuery("sp", "sp.providerNo = :providerNo AND sp.status=1 order by sp.id DESC");
		query.setMaxResults(1);
		query.setParameter("providerNo", providerNo);
		return getSingleResultOrNull(query);
	}
	
	@SuppressWarnings("unchecked")
    public List<Object[]> findAllDatesByProviderNo(String providerNo) {
		String sql = "Select sp.dateTime, sp.id from ScratchPad sp where sp.providerNo = :providerNo AND sp.status=1 order by sp.dateTime DESC";
		Query query = entityManager.createQuery(sql);
		query.setParameter("providerNo", providerNo);
		return query.getResultList();
	}
}
