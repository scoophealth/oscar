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
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.ConsultationServices;
import org.oscarehr.util.SpringUtils;

public class ConsultationServicesDaoTest extends DaoTestFixtures {

	protected ConsultationServiceDao dao = (ConsultationServiceDao) SpringUtils.getBean("consultationServiceDao");

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("consultationServices", "serviceSpecialists", "professionalSpecialists");
	}

	@Test
	public void testCreate() throws Exception {
		ConsultationServices entity = new ConsultationServices();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}

	@Test
	public void testFindAll() throws Exception {
		ConsultationServices consultation1 = new ConsultationServices();
		EntityDataGenerator.generateTestDataForModelClass(consultation1);
		ConsultationServices consultation2 = new ConsultationServices();
		EntityDataGenerator.generateTestDataForModelClass(consultation2);
		ConsultationServices consultation3 = new ConsultationServices();
		EntityDataGenerator.generateTestDataForModelClass(consultation3);
		dao.persist(consultation1);
		dao.persist(consultation2);
		dao.persist(consultation3);
		List<ConsultationServices> result = dao.findAll();
		List<ConsultationServices> expectedResult = new ArrayList<ConsultationServices>(Arrays.asList(consultation1, consultation2, consultation3));

		assertTrue(result.containsAll(expectedResult));
	}

	@Test
	public void testFindActive() throws Exception {
		ConsultationServices activeConsult = new ConsultationServices();
		EntityDataGenerator.generateTestDataForModelClass(activeConsult);
		activeConsult.setActive("1");
		ConsultationServices inactiveConsult = new ConsultationServices();
		EntityDataGenerator.generateTestDataForModelClass(activeConsult);
		activeConsult.setActive("0");
		dao.persist(activeConsult);
		dao.persist(inactiveConsult);
		List<ConsultationServices> all = dao.findAll();
		assertTrue(all.contains(inactiveConsult));
	}

}
