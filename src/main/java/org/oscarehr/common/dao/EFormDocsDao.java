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

import org.oscarehr.common.model.EFormDocs;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("unchecked")
public class EFormDocsDao extends AbstractDao<EFormDocs>{

	public EFormDocsDao() {
		super(EFormDocs.class);
	}

	public List<EFormDocs> findByFdidIdDocNoDocType(Integer fdid, Integer documentNo, String docType) {
	  	String sql = "select x from EFormDocs x where x.fdid=? and x.documentNo=? and x.docType=? and x.deleted is NULL";
    	Query query = entityManager.createQuery(sql);
    	query.setParameter(1,fdid);
    	query.setParameter(2,documentNo);
    	query.setParameter(3,docType);

        List<EFormDocs> results = query.getResultList();
        return results;
	}

	public List<EFormDocs> findByFdid(Integer fdid) {
	  	String sql = "select x from EFormDocs x where x.fdid=? and x.deleted is NULL";
    	Query query = entityManager.createQuery(sql);
    	query.setParameter(1,fdid);

        List<EFormDocs> results = query.getResultList();
        return results;
	}

	public List<Object[]> findLabs(Integer fdid) {
		String sql = "FROM EFormDocs cd, PatientLabRouting plr " +
				"WHERE plr.labNo = cd.documentNo " +
				"AND cd.fdid = :fdid " +
				"AND cd.docType = :docType " +
				"AND cd.deleted IS NULL " +
				"ORDER BY cd.documentNo";
		Query q = entityManager.createQuery(sql);
		q.setParameter("fdid", fdid);
		q.setParameter("docType", EFormDocs.DOCTYPE_LAB);
		return q.getResultList();
	}
}
