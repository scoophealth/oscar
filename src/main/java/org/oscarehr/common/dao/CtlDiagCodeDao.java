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

import org.oscarehr.common.NativeSql;
import org.oscarehr.common.model.CtlDiagCode;
import org.springframework.stereotype.Repository;

@Repository
public class CtlDiagCodeDao extends AbstractDao<CtlDiagCode> {

	public CtlDiagCodeDao() {
		super(CtlDiagCode.class);
	}

	/**
	 * Gets diagnostics for the specified region and service type.
	 * 
	 * @param billRegion
	 * @param serviceType
	 * @return
	 * 		Returns rows containing diagnostic code and description
	 */
	@SuppressWarnings("unchecked")
	@NativeSql({"ctl_diagcode", "diagnosticcode"})
	public List<Object[]> getDiagnostics(String billRegion, String serviceType) {
		Query query = entityManager.createNativeQuery("SELECT d.diagnostic_code, d.description FROM diagnosticcode d, " 
				+ "ctl_diagcode c WHERE d.diagnostic_code=c.diagnostic_code and d.region = ? and c.servicetype = ?");
		query.setParameter(1, billRegion);
		query.setParameter(2, serviceType);
		return query.getResultList();

	}

	public List<CtlDiagCode> findByServiceType(String serviceType) {
		Query q = entityManager.createQuery("select x from CtlDiagCode x where x.serviceType = ?");
		q.setParameter(1, serviceType);
		
		@SuppressWarnings("unchecked")
		List<CtlDiagCode> results = q.getResultList();
		
		return results;
	}
}
