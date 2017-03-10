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
package org.oscarehr.billing.CA.ON.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.billing.CA.ON.model.BillingONDiskName;
import org.oscarehr.common.dao.AbstractDao;
import org.springframework.stereotype.Repository;

@Repository
public class BillingONDiskNameDao extends AbstractDao<BillingONDiskName>{

	public BillingONDiskNameDao() {
		super(BillingONDiskName.class);
	}
		
	public BillingONDiskName getLatestSoloMonthCodeBatchNum(String providerOhipNo) {
		String q = "select d from BillingONDiskName d, BillingONFilename f where f.providerOhipNo=? and d.groupNo='' and d.id=f.diskId order by d.id desc";
		Query query = entityManager.createQuery(q);
		query.setParameter(1, providerOhipNo);
		query.setMaxResults(1);
		
		BillingONDiskName result = this.getSingleResultOrNull(query);
		
		return result;
	}
	
	public BillingONDiskName findByGroupNo(String groupNo) {
		String q = "SELECT b FROM BillingONDiskName b WHERE b.groupNo=? order by b.createDateTime DESC";
		Query query = entityManager.createQuery(q);
		query.setParameter(1, groupNo);
		query.setMaxResults(1);
		
		BillingONDiskName result = getSingleResultOrNull(query);
		
		return result;
	}
	
	public BillingONDiskName getPrevDiskCreateDate(Date date, String groupNo) {
		String q = "SELECT b FROM BillingONDiskName b WHERE  b.createDateTime<? and groupno=? order by createdatetime DESC";
		Query query = entityManager.createQuery(q);
		query.setParameter(1, date);
		query.setParameter(2, groupNo);
		query.setMaxResults(1);
		
		BillingONDiskName result = getSingleResultOrNull(query);
		
		return result;
	}
	
	public List<BillingONDiskName> findByCreateDateRangeAndStatus(Date sDate, Date eDate, String status) {
		String q = "SELECT b FROM BillingONDiskName b WHERE b.createDateTime >= :sd AND b.createDateTime <= :ed AND b.status IN (:status) ORDER BY b.createDateTime DESC";
		Query query = entityManager.createQuery(q);
		query.setParameter("sd", sDate);
		query.setParameter("ed", eDate);
		query.setParameter("status", status);
		
		@SuppressWarnings("unchecked")
		List<BillingONDiskName> results = query.getResultList();
		
		return results;
	}
	
	
}
