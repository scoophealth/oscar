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

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.LabTestResults;
import org.oscarehr.util.SpringUtils;

public class LabTestResultsDaoTest extends DaoTestFixtures {

	protected LabTestResultsDao dao = SpringUtils.getBean(LabTestResultsDao.class);

	public LabTestResultsDaoTest() {
	}

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("labTestResults", "patientLabRouting", "labPatientPhysicianInfo", "providerLabRouting");
	}

	@Test
	public void testCreate() throws Exception {
		LabTestResults entity = new LabTestResults();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);

		assertNotNull(entity.getId());
	}

	@Test
	public void testFindByTitleAndLabInfoId() {
		List<LabTestResults> results = dao.findByTitleAndLabInfoId(100);
		assertNotNull(results);
	}

	@Test
	public void testFindByLabInfoId() {
		List<LabTestResults> results = dao.findByLabInfoId(100);
		assertNotNull(results);
	}

	@Test
	public void testFindByAbnAndLabInfoId() {
		List<LabTestResults> results = dao.findByAbnAndLabInfoId("A", 100);
		assertNotNull(results);
	}
	
	@Test
	public void testFindUniqueTestNames() {
		List<Object[]> results = dao.findUniqueTestNames(100, "CML");
		assertNotNull(results);
	}
	
	@Test
	public void testFindByAbnAndPhysicianId() {
		assertNotNull(dao.findByAbnAndPhysicianId("ABN", 199));
	}

    @Test
    public void testFindByLabPatientPhysicialInfoId() {
	    assertNotNull(dao.findByLabPatientPhysicialInfoId(199));
    }

}