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

import org.oscarehr.common.model.RaHeader;
import org.springframework.stereotype.Repository;


@Repository
@SuppressWarnings("unchecked")
public class RaHeaderDao extends AbstractDao<RaHeader>{

	public RaHeaderDao() {
		super(RaHeader.class);
	}     
	
	 public List<RaHeader> findCurrentByFilenamePaymentDate(String filename, String paymentDate) {
		 Query query = entityManager.createQuery("SELECT r from RaHeader r WHERE r.filename = :filename and r.paymentDate = :pd and status != :status ORDER BY r.paymentDate");
        
		 query.setParameter("filename", filename);
		 query.setParameter("pd",paymentDate);
		 query.setParameter("status","D");

         
         List<RaHeader> results = query.getResultList();

         return results;

	 }
	 
	 public List<RaHeader> findByFilenamePaymentDate(String filename, String paymentDate) {
		 Query query = entityManager.createQuery("SELECT r from RaHeader r WHERE r.filename = :filename and r.paymentDate = :pd  ORDER BY r.paymentDate");
        
		 query.setParameter("filename", filename);
		 query.setParameter("pd",paymentDate);
		
         
         List<RaHeader> results = query.getResultList();

         return results;

	 }
	 

	 public List<RaHeader> findAllExcludeStatus(String status) {
		 Query query = entityManager.createQuery("SELECT r FROM RaHeader r WHERE r.status != :status ORDER BY r.paymentDate DESC, r.readDate DESC");
        
		 query.setParameter("status", status);
		
         
         List<RaHeader> results = query.getResultList();

         return results;

	 }

	public List<RaHeader> findByHeaderDetailsAndProviderMagic(String status, String providerNo) {
		String sql =
				"SELECT r " +
				"FROM RaHeader r, RaDetail t, Provider p " +
				"WHERE r.id = t.raHeaderNo " +
				"AND p.OhipNo = t.providerOhipNo " +
				"AND r.status <> :status " + 
				"AND (" +
				"	p.ProviderNo = :providerNo" +
				"	OR p.Team = (" +
				"		SELECT pp.Team FROM Provider pp WHERE pp.ProviderNo = :providerNo " +
				"	) " +
				") GROUP BY r.id" +
				" ORDER BY r.paymentDate DESC, r.readDate DESC";
		Query query = entityManager.createQuery(sql);
		query.setParameter("status", status);
		query.setParameter("providerNo", providerNo);
		return query.getResultList();
    }

	public List<RaHeader> findByStatusAndProviderMagic(String status, String providerNo) {
		String sql = "SELECT r " + 
				"FROM RaHeader r, RaDetail t, Provider p " +
				"WHERE r.id = t.raHeaderNo " +
				"AND p.OhipNo = t.providerOhipNo " +
				"AND r.status <> :status " + 
				"AND EXISTS (" +
				"	FROM ProviderSite s " +
				"	WHERE p.ProviderNo = s.id.providerNo " +
				"	AND s.id.siteId IN (" +
				"		SELECT ss.id.siteId FROM ProviderSite ss WHERE ss.id.providerNo = :providerNo " +
				"	) " +
				") " +
				"GROUP BY r.id " +
				"ORDER BY r.paymentDate DESC, r.readDate DESC";
		Query query = entityManager.createQuery(sql);
		query.setParameter("status", status);
		query.setParameter("providerNo", providerNo);
		return query.getResultList();
    }

	public List<Object[]> findHeadersAndProvidersById(Integer id) {
	    String sql = "FROM RaDetail r, Provider p " + 
	    		"WHERE p.OhipNo = r.providerOhipNo " +
				"AND r.raHeaderNo = :raId " +
				"GROUP BY r.providerOhipNo";
		Query query = entityManager.createQuery(sql);
		query.setParameter("raId", id);
		return query.getResultList();
    }
	
}
