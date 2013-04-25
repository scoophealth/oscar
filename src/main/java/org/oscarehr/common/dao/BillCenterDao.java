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

import org.oscarehr.common.model.BillCenter;
import org.springframework.stereotype.Repository;

@Repository
public class BillCenterDao extends AbstractDao<BillCenter>{

	public BillCenterDao() {
		super(BillCenter.class);
	}
	
	public List<BillCenter> findAll() {
		Query query = entityManager.createQuery("SELECT b FROM BillCenter b");
		
		@SuppressWarnings("unchecked")
        List<BillCenter> results = query.getResultList();
		return results;
	}
	
	public List<BillCenter> findByBillCenterDesc(String descr) {
		Query query = entityManager.createQuery("SELECT b FROM BillCenter b WHERE b.billCenterDesc like ?");
		query.setParameter(1,descr);
		@SuppressWarnings("unchecked")
        List<BillCenter> results = query.getResultList();
		return results;
	}
}
