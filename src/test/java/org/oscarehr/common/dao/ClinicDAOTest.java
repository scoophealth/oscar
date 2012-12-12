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

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.Clinic;
import org.oscarehr.util.SpringUtils;

public class ClinicDAOTest extends DaoTestFixtures {

	protected ClinicDAO dao = (ClinicDAO)SpringUtils.getBean(ClinicDAO.class);

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable(false, "clinic");
	}

	@Test
	public void testCreate() throws Exception {
		Clinic entity = new Clinic();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}
	
	@Test
	public void testGetClinic() throws Exception {
		
		Clinic clinic1 = new Clinic();
		EntityDataGenerator.generateTestDataForModelClass(clinic1);
		dao.persist(clinic1);
		
		Clinic clinic2 = new Clinic();
		EntityDataGenerator.generateTestDataForModelClass(clinic2);
		dao.persist(clinic2);
		
		Clinic clinic3 = new Clinic();
		EntityDataGenerator.generateTestDataForModelClass(clinic3);
		dao.persist(clinic3);
		
		Clinic expectedResult = clinic1;
		Clinic result = dao.getClinic();
		
		assertEquals(expectedResult, result);
	}
}
