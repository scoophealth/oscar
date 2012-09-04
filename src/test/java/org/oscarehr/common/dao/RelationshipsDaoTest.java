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

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.Relationships;
import org.oscarehr.util.SpringUtils;

import oscar.util.ConversionUtils;

public class RelationshipsDaoTest extends DaoTestFixtures {

	private RelationshipsDao dao = SpringUtils.getBean(RelationshipsDao.class);

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("relationships");
	}

	@Test
	public void testAll() throws Exception {
		Relationships active = new Relationships();
		EntityDataGenerator.generateTestDataForModelClass(active);
		active.setDeleted(null);
		dao.persist(active);

		Relationships passive = new Relationships();
		EntityDataGenerator.generateTestDataForModelClass(passive);
		passive.setDeleted(ConversionUtils.toBoolString(Boolean.TRUE));
		dao.persist(passive);

		Relationships activeCheck = dao.findActive(active.getId());
		Relationships activeAnotherCheck = dao.find(active.getId());
		assertNotNull(activeCheck);
		assertNull(dao.findActive(passive.getId()));

		assertNull(dao.findActive(active.getDemographicNo()));
		assertNull(dao.findActive(passive.getDemographicNo()));

		List<Relationships> rs = dao.findActiveSubDecisionMaker(active.getId());
		assertNotNull(rs);

		assertNotNull(dao.findActiveByDemographicNumberAndFacility(active.getDemographicNo(), active.getFacilityId()));
	}

}
