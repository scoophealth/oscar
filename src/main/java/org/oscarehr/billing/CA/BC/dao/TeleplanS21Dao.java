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

import org.oscarehr.billing.CA.BC.model.TeleplanS21;
import org.oscarehr.common.dao.AbstractDao;
import org.springframework.stereotype.Repository;

@Repository
public class TeleplanS21Dao extends AbstractDao<TeleplanS21>{

	public TeleplanS21Dao() {
		super(TeleplanS21.class);
	}
	
	public List<TeleplanS21> findByFilenamePaymentPayeeNo(String filename, String payment, String payeeNo) {
		Query q = entityManager.createQuery("SELECT t from TeleplanS21 t WHERE t.fileName=? AND t.payment=? AND t.payeeNo=? ORDER BY t.payment");
		q.setParameter(1, filename);
		q.setParameter(2, payment);
		q.setParameter(3, payeeNo);
		
		@SuppressWarnings("unchecked")
		List<TeleplanS21> results = q.getResultList();
		
		return results;
	}
	
	public List<TeleplanS21> search_all_tahd(String excludeStatus) {
		Query q = entityManager.createQuery("SELECT t from TeleplanS21 t WHERE t.status <> ? ORDER BY t.payment desc");
		q.setParameter(1, excludeStatus);
		
		
		@SuppressWarnings("unchecked")
		List<TeleplanS21> results = q.getResultList();
		
		return results;
	}
}
