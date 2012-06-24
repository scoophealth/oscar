/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */
package org.oscarehr.common.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.CdsFormOption;
import org.springframework.stereotype.Repository;

@Repository
public class CdsFormOptionDao extends AbstractDao<CdsFormOption> {

	public CdsFormOptionDao() {
		super(CdsFormOption.class);
	}
	
	/**
	 * @param formVersion should be the major cds version, i.e. '4' (we're assuming minor versions are compatable, if it's not we can use the full version number instead)
	 * @param mainCatgeory should be the cds main category, i.e. for '016-06' the main category should be '016'
	 * @return results are sorted by their name as per in the CDS spec aka insertion order, not alphabetically.   
	 */
	public List<CdsFormOption> findByVersionAndCategory(String formVersion, String mainCatgeory) {
		// build sql string
		String sqlCommand = "select x from CdsFormOption x where x.cdsFormVersion=?1 and x.cdsDataCategory like ?2 order by x.id";

		// set parameters
		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, formVersion);
		query.setParameter(2, mainCatgeory+'%');

		// run query
		@SuppressWarnings("unchecked")
		List<CdsFormOption> results = query.getResultList();

		return (results);
	}

	/**
	 * @param formVersion should be the major cds version, i.e. '4' (we're assuming minor versions are compatable, if it's not we can use the full version number instead)
	 */
	public List<CdsFormOption> findByVersion(String formVersion) {
		// build sql string
		String sqlCommand = "select x from CdsFormOption x where x.cdsFormVersion=?1 order by x.id";

		// set parameters
		Query query = entityManager.createQuery(sqlCommand);
		query.setParameter(1, formVersion);

		// run query
		@SuppressWarnings("unchecked")
		List<CdsFormOption> results = query.getResultList();

		return (results);
	}

}
