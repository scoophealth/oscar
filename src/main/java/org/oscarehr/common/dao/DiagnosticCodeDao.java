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

import org.oscarehr.common.model.DiagnosticCode;
import org.springframework.stereotype.Repository;

@Repository
public class DiagnosticCodeDao extends AbstractDao<DiagnosticCode>{

	public DiagnosticCodeDao() {
		super(DiagnosticCode.class);
	}

	public List<DiagnosticCode> findByDiagnosticCode(String diagnosticCode) {
		String sql = "select x from DiagnosticCode x where x.diagnosticCode=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, diagnosticCode);
		@SuppressWarnings("unchecked")
		List<DiagnosticCode> results = query.getResultList();
		return results;
	}

	public List<DiagnosticCode> findByDiagnosticCodeAndRegion(String diagnosticCode, String region) {
		String sql = "select x from DiagnosticCode x where x.diagnosticCode=? and x.region=?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, diagnosticCode);
		query.setParameter(2, region);
		@SuppressWarnings("unchecked")
		List<DiagnosticCode> results = query.getResultList();
		return results;
	}

	public List<DiagnosticCode> search(String searchString) {
		String sql = "select x from DiagnosticCode x where x.diagnosticCode like ? or x.description like ? order by x.diagnosticCode";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, searchString);
		query.setParameter(2, searchString);

		@SuppressWarnings("unchecked")
		List<DiagnosticCode> results = query.getResultList();
		return results;
	}

	public List<DiagnosticCode> newSearch(String a, String b, String c, String d, String e, String f) {
		String sql = "select x from DiagnosticCode x where x.diagnosticCode like ? or x.diagnosticCode like ? or x.diagnosticCode like ? or x.description like ? or x.description like ? or x.description like ?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, a);
		query.setParameter(2, b);
		query.setParameter(3, c);
		query.setParameter(4, d);
		query.setParameter(5, e);
		query.setParameter(6, f);

		@SuppressWarnings("unchecked")
		List<DiagnosticCode> results = query.getResultList();
		return results;
	}

	public List<DiagnosticCode> searchCode(String code) {
		String sql = "select x from DiagnosticCode x where x.diagnosticCode like ?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, code);

		@SuppressWarnings("unchecked")
		List<DiagnosticCode> results = query.getResultList();
		return results;
	}

	public List<DiagnosticCode> searchText(String description) {
		String sql = "select x from DiagnosticCode x where x.description like ?";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, description);

		@SuppressWarnings("unchecked")
		List<DiagnosticCode> results = query.getResultList();
		return results;
	}

    public List<DiagnosticCode> getByDxCode(String dxCode){
        Query query = entityManager.createQuery("select bdx from DiagnosticCode bdx where bdx.diagnosticCode = ?");
        query.setParameter(1,dxCode);
        @SuppressWarnings("unchecked")
        List<DiagnosticCode> results = query.getResultList();
        return results;
    }
}
