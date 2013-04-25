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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.billing.CA.BC.model.TeleplanS00;
import org.oscarehr.common.dao.AbstractDao;
import org.springframework.stereotype.Repository;

@Repository
public class TeleplanS00Dao extends AbstractDao<TeleplanS00> {

	public TeleplanS00Dao() {
		super(TeleplanS00.class);
	}

	@SuppressWarnings("unchecked")
	public List<TeleplanS00> findAll() {
		Query query = createQuery("x", null);
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<TeleplanS00> findByBillingNo(String mspCtlNo) {
		Query q = createQuery("t", "t.mspCtlNo = :no");
		q.setParameter("no", mspCtlNo);
		return q.getResultList();
	}

	public List<TeleplanS00> findByOfficeNumber(String officeNumber) {
		List<String> numbers = new ArrayList<String>();
		numbers.add(officeNumber);
		return findByOfficeNumbers(numbers);
	}

	@SuppressWarnings("unchecked")
	public List<TeleplanS00> findByOfficeNumbers(List<String> officeNumbers) {
		if (officeNumbers.isEmpty()) {
			return new ArrayList<TeleplanS00>();
		}

		Query q = createQuery("t", "t.officeNo IN (:no)");
		q.setParameter("no", officeNumbers);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<TeleplanS00> findBgs() {
		Query q = createQuery("t", "t.exp1 = :s OR t.exp2 = :s OR t.exp3 = :s OR t.exp4 = :s OR t.exp5 = :s OR t.exp6 = :s OR t.exp7 = :s");
		q.setParameter("s", "BG");
		return q.getResultList();
    }
	
	@SuppressWarnings("unchecked")
	public List<Object[]> search_taprovider(Integer s21Id) {
		Query q = entityManager.createQuery("select r.practitionerNo, p.LastName,p.FirstName from TeleplanS00 r, Provider p where p.OhipNo=r.practitionerNo and r.s21Id=? group by r.practitionerNo");
		q.setParameter(1, s21Id);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<TeleplanS00> search_taS00 (Integer s21Id, String type, String practitionerNo) {
		Query q = entityManager.createQuery("select t from TeleplanS00 t where t.s21Id=? and t.s00Type<>? and t.practitionerNo like ? order by t.id");
		q.setParameter(1, s21Id);
		q.setParameter(2, type);
		q.setParameter(3, practitionerNo);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<TeleplanS00> search_taS01 (Integer s21Id, String type, String practitionerNo) {
		Query q = entityManager.createQuery("select t from TeleplanS00 t where t.s21Id=? and t.s00Type<>? and t.practitionerNo like ? order by t.id");
		q.setParameter(1, s21Id);
		q.setParameter(2, type);
		q.setParameter(3, practitionerNo);
		return q.getResultList();
	}
}
