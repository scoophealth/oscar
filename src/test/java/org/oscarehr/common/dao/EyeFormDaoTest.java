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
import org.oscarehr.eyeform.dao.EyeFormDao;
import org.oscarehr.eyeform.model.EyeForm;
import org.oscarehr.util.SpringUtils;

public class EyeFormDaoTest extends DaoTestFixtures {

	protected EyeFormDao dao = SpringUtils.getBean(EyeFormDao.class);
	
	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("Eyeform");
	}

        @Test
        public void testCreate() throws Exception {
                EyeForm entity = new EyeForm();
                EntityDataGenerator.generateTestDataForModelClass(entity);
                dao.persist(entity);
                assertNotNull(entity.getId());
        }


	@Test
	public void testGetByAppointmentNo() throws Exception {
		
		int appointmentNo1 = 101;
		int appointmentNo2 = 202;
		int appointmentNo3 = 303;
		
		EyeForm eyeform1 = new EyeForm();
		EntityDataGenerator.generateTestDataForModelClass(eyeform1);
		eyeform1.setAppointmentNo(appointmentNo1);
		dao.persist(eyeform1);
		
		EyeForm eyeform2 = new EyeForm();
		EntityDataGenerator.generateTestDataForModelClass(eyeform2);
		eyeform2.setAppointmentNo(appointmentNo2);
		dao.persist(eyeform2);
		
		EyeForm eyeform3 = new EyeForm();
		EntityDataGenerator.generateTestDataForModelClass(eyeform3);
		eyeform3.setAppointmentNo(appointmentNo3);
		dao.persist(eyeform3);
		
		EyeForm expectedResult = eyeform2;
		EyeForm result = dao.getByAppointmentNo(appointmentNo2);
		
		assertEquals(expectedResult, result);
	}
}
