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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.HL7HandlerMSHMapping;
import org.oscarehr.util.SpringUtils;

public class HL7HandlerMSHMappingDaoTest extends DaoTestFixtures {

	protected HL7HandlerMSHMappingDao dao = SpringUtils.getBean(HL7HandlerMSHMappingDao.class);


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("HL7HandlerMSHMapping");
	}

        @Test
        public void testCreate() throws Exception {
                HL7HandlerMSHMapping entity = new HL7HandlerMSHMapping();
                EntityDataGenerator.generateTestDataForModelClass(entity);
                dao.persist(entity);
                assertNotNull(entity.getId());
        }

	@Test
	public void testFindByFacility() throws Exception {
		
		String facility1 = "alpha";
		String facility2 = "bravo";
		String facility3 = "charlie";
		
		HL7HandlerMSHMapping hl7HandlerMSHMapping1 = new HL7HandlerMSHMapping();
		EntityDataGenerator.generateTestDataForModelClass(hl7HandlerMSHMapping1);
		hl7HandlerMSHMapping1.setFacility(facility1);
		dao.persist(hl7HandlerMSHMapping1);
		
		HL7HandlerMSHMapping hl7HandlerMSHMapping2 = new HL7HandlerMSHMapping();
		EntityDataGenerator.generateTestDataForModelClass(hl7HandlerMSHMapping2);
		hl7HandlerMSHMapping2.setFacility(facility2);
		dao.persist(hl7HandlerMSHMapping2);
		
		HL7HandlerMSHMapping hl7HandlerMSHMapping3 = new HL7HandlerMSHMapping();
		EntityDataGenerator.generateTestDataForModelClass(hl7HandlerMSHMapping3);
		hl7HandlerMSHMapping3.setFacility(facility3);
		dao.persist(hl7HandlerMSHMapping3);
		
		HL7HandlerMSHMapping expectedResult = hl7HandlerMSHMapping2;
		HL7HandlerMSHMapping result = dao.findByFacility(facility2);
		
		assertEquals(expectedResult, result);
	}
}
