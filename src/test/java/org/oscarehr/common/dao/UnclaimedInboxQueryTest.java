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

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.junit.Test;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.util.SpringUtils;

public class UnclaimedInboxQueryTest extends DaoTestFixtures {

	protected InboxResultsDao dao = SpringUtils.getBean(InboxResultsDao.class);

	@Test
	public void testQueryParsingForUnclaimedSearch() throws Exception {
		SchemaUtils.restoreAllTables();

		EntityManagerFactory emf = (EntityManagerFactory) SpringUtils.getBean("entityManagerFactory");
		EntityManager em = null;
		try {
			em = emf.createEntityManager();
			
			for (String sql : getQueries()) {				
				Query query = em.createNativeQuery(sql);
				List<?> resultList = query.getResultList();
				assertNotNull(resultList);
			}
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	private List<String> getQueries() {
		List<String> result = new ArrayList<String>();

		UnclaimedInboxQueryBuilder builder = new UnclaimedInboxQueryBuilder();
		builder.setMixLabsAndDocs(false);
		builder.setPaged(false);
		result.add(builder.buildQuery());

		builder.setMixLabsAndDocs(false);
		builder.setPaged(true);
		builder.setPage(0);
		builder.setPageSize(10);
		result.add(builder.buildQuery());

		builder.setPage(10);
		builder.setPageSize(10);
		result.add(builder.buildQuery());

		builder.setMixLabsAndDocs(true);
		builder.setPaged(false);
		result.add(builder.buildQuery());

		builder.setMixLabsAndDocs(true);
		builder.setPaged(true);
		builder.setPage(0);
		builder.setPageSize(10);
		result.add(builder.buildQuery());

		builder.setPage(10);
		builder.setPageSize(10);
		result.add(builder.buildQuery());

		return result;
	}

}
