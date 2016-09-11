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

package org.oscarehr.billing.CA.BC.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.billing.CA.BC.model.TeleplanC12;
import org.oscarehr.common.dao.AbstractDao;
import org.springframework.stereotype.Repository;

@Repository
public class TeleplanC12Dao extends AbstractDao<TeleplanC12> {

	public TeleplanC12Dao() {
		super(TeleplanC12.class);
	}

	@SuppressWarnings("unchecked")
	public List<TeleplanC12> findCurrent() {
		Query query = createQuery("t", "t.status <> 'E'");
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<TeleplanC12> findByOfficeClaimNo(String claimNo) { 
		Query query = createQuery("t", "t.officeFolioClaimNo = :claimNo");
		query.setParameter("claimNo", claimNo);
		return query.getResultList();
    }

	@SuppressWarnings("unchecked")
	public List<Object[]> findRejected() {
		String sql = "FROM TeleplanC12 tc, TeleplanS21 ts " +
				"WHERE tc.s21Id = ts.id " +
				"AND tc.status != 'E'";
		Query query = entityManager.createQuery(sql);
		return query.getResultList();	    
    }
	
	@SuppressWarnings("unchecked")
	public List<TeleplanC12> select_c12_record(String status, String claimNo) { 
		Query query = createQuery("t", "t.status = :status and t.officeFolioClaimNo = :claimNo");
		query.setParameter("claimNo", claimNo);
		query.setParameter("status", status);
		return query.getResultList();
    }
	
}
