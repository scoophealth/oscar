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

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.billing.CA.ON.model.BillingONFilename;
import org.oscarehr.common.dao.AbstractDao;
import org.springframework.stereotype.Repository;

@Repository
public class BillingONFilenameDao extends AbstractDao<BillingONFilename>{

	public BillingONFilenameDao() {
		super(BillingONFilename.class);
	}
	
	public List<BillingONFilename> findByDiskIdAndStatus(Integer diskId, String status) {
		String q = "SELECT b FROM BillingONFilename b WHERE b.diskId = ?  AND b.status = ? ORDER BY b.id DESC";
		Query query = entityManager.createQuery(q);
		query.setParameter(1, diskId);
		query.setParameter(2, status);
		
		@SuppressWarnings("unchecked")
		List<BillingONFilename> results = query.getResultList();
		
		return results;
	}
	
	public List<BillingONFilename> findByDiskIdAndProvider(Integer diskId, String provider) {
		String q = "SELECT b FROM BillingONFilename b WHERE b.diskId = ?  AND b.providerNo = ? ORDER BY b.id DESC";
		Query query = entityManager.createQuery(q);
		query.setParameter(1, diskId);
		query.setParameter(2, provider);
		
		@SuppressWarnings("unchecked")
		List<BillingONFilename> results = query.getResultList();
		
		return results;
	}
	
	public List<BillingONFilename> findByDiskId(Integer diskId) {
		String q = "SELECT b FROM BillingONFilename b WHERE b.diskId = ?";
		Query query = entityManager.createQuery(q);
		query.setParameter(1, diskId);
		
		@SuppressWarnings("unchecked")
		List<BillingONFilename> results = query.getResultList();
		
		return results;
	}
	
	public List<BillingONFilename> findCurrentByDiskId(Integer diskId) {
		String q = "SELECT b FROM BillingONFilename b WHERE b.diskId = ?  AND b.status != ? ORDER BY b.id DESC";
		Query query = entityManager.createQuery(q);
		query.setParameter(1, diskId);
		query.setParameter(2, "D");
		
		@SuppressWarnings("unchecked")
		List<BillingONFilename> results = query.getResultList();
		
		return results;
	}
}
