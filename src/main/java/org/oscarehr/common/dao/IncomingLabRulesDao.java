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

import org.oscarehr.common.model.IncomingLabRules;
import org.oscarehr.common.model.Provider;
import org.springframework.stereotype.Repository;

@Repository
public class IncomingLabRulesDao extends AbstractDao<IncomingLabRules>{

	public IncomingLabRulesDao() {
		super(IncomingLabRules.class);
	}
	
	public List<IncomingLabRules> findCurrentByProviderNoAndFrwdProvider(String providerNo, String frwdProvider) {
		Query q = entityManager.createQuery("select i from IncomingLabRules i where i.providerNo=?1 and i.frwdProviderNo=?2 and i.archive=?3");
		q.setParameter(1, providerNo);
		q.setParameter(2, frwdProvider);
		q.setParameter(3, "0");
		
		@SuppressWarnings("unchecked")
		List<IncomingLabRules> results = q.getResultList();
		
		return results;
	}
	
	public List<IncomingLabRules> findByProviderNoAndFrwdProvider(String providerNo, String frwdProvider) {
		Query q = entityManager.createQuery("select i from IncomingLabRules i where i.providerNo=?1 and i.frwdProviderNo=?2");
		q.setParameter(1, providerNo);
		q.setParameter(2, frwdProvider);

		
		@SuppressWarnings("unchecked")
		List<IncomingLabRules> results = q.getResultList();
		
		return results;
	}
	
	public List<IncomingLabRules> findCurrentByProviderNo(String providerNo) {
		Query q = entityManager.createQuery("select i from IncomingLabRules i where i.providerNo=?1 and i.archive=?2");
		q.setParameter(1, providerNo);
		q.setParameter(2, "0");
		
		@SuppressWarnings("unchecked")
		List<IncomingLabRules> results = q.getResultList();
		
		return results;
	}
	
	public List<IncomingLabRules> findByProviderNo(String providerNo) {
		Query q = entityManager.createQuery("select i from IncomingLabRules i where i.providerNo=?1");
		q.setParameter(1, providerNo);
		
		@SuppressWarnings("unchecked")
		List<IncomingLabRules> results = q.getResultList();
		
		return results;
	}

	/**
	 * @param providerNo
	 * @return
	 * 		Returns a list of pairs {@link IncomingLabRules}, {@link Provider}
	 */
	@SuppressWarnings("unchecked")
    public List<Object[]> findRules(String providerNo) {
    	// assume archive represents boolean with 0 == false and 1 == true
		Query q = entityManager.createQuery("FROM IncomingLabRules i, " + Provider.class.getSimpleName() + " p " +
				"WHERE i.archive <> '1' " + // non-archived rules
				"AND i.providerNo = :providerNo " +
				"AND p.id = i.frwdProviderNo");
		q.setParameter("providerNo", providerNo);
		return q.getResultList();
    }
}
