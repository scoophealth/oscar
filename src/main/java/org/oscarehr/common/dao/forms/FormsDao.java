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
package org.oscarehr.common.dao.forms;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.oscarehr.common.NativeSql;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@SuppressWarnings("unchecked")
public class FormsDao {

	// private static final long serialVersionUID = 1L;

	@PersistenceContext
	protected EntityManager entityManager = null;

	/**
	 * Returns:
	 * 	ID int
	 * 	formCreated date
	 *  patientName string
	 *  
	 * @return
	 */
	@NativeSql("formLabReq07")
	public List<Object[]> findIdFormCreatedAndPatientNameFromFormLabReq07() {
		String sql = "SELECT ID, formCreated, patientName FROM formLabReq07";
		Query query = entityManager.createNativeQuery(sql);
		return query.getResultList();
	}
	
	@NativeSql("formLabReq07")
	public List<Object> findFormCreatedFromFormLabReq07ById(Integer linkReqId) {
		String sql = "SELECT formCreated FROM formLabReq07 WHERE ID = :linkReqId";
		Query query = entityManager.createNativeQuery(sql);
		query.setParameter("linkReqId", linkReqId);
		return query.getResultList();
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

}
