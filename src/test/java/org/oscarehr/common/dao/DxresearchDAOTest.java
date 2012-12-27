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

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.Dxresearch;
import org.oscarehr.util.SpringUtils;

public class DxresearchDAOTest extends DaoTestFixtures {

	// TODO Make it protected when test ignores are merged in
	private DxresearchDAO dao = (DxresearchDAO)SpringUtils.getBean("DxresearchDAO");

	public DxresearchDAOTest() {
	}

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("dxresearch", "demographic","lst_gender","admission","demographic_merged",
				"program","health_safety","provider","providersite","site","program_team",
				"measurements", "measurementType", "measurementsExt", "quickList", "icd9","ichppccode",
				"billing", "billingdetail");
	}

	@Test
	public void testCreate() throws Exception {
		Dxresearch dr = new Dxresearch();
		EntityDataGenerator.generateTestDataForModelClass(dr);
		dao.persist(dr);
		assertNotNull(dr.getId());
	}

	@Test
	public void testFindByDemographicNoResearchCodeAndCodingSystem() {
		List<Dxresearch> list = dao.findByDemographicNoResearchCodeAndCodingSystem(1, "CODE", "SYS");
		assertNotNull(list);
	}
	
	@Test
	public void testGetDataForInrReport() {
		List<Object[]> list = dao.getDataForInrReport(new Date(), new Date());
		assertNotNull(list);
	}


	@Test
	public void testCountResearches() {
		assertNotNull(dao.countResearches("CDE", new Date(), new Date()));
	}

	@Test
	public void testCountBillingResearches() {
		assertNotNull(dao.countBillingResearches("CDE", "DIAG", "CREATOR", new Date(), new Date()));
	}
}
