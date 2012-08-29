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
import static org.junit.Assert.assertFalse;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.MeasurementDao.SearchCriteria;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.Measurement;
import org.oscarehr.util.SpringUtils;

public class MeasurementDaoTest extends DaoTestFixtures {

	private MeasurementDao dao = SpringUtils.getBean(MeasurementDao.class);

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("measurements");
	}

	@Test
	public void testFind() {
		Measurement m = populate();

		SearchCriteria c = new SearchCriteria();
		c.setComments(m.getComments());
		c.setDataField(m.getDataField());
		c.setDateObserved(m.getDateObserved());
		c.setDemographicNo(m.getDemographicId());
		c.setMeasuringInstrc(m.getMeasuringInstruction());
		c.setType(m.getType());

		List<Measurement> ms = dao.find(c);
		assertEquals(1, ms.size());
	}

	private Measurement populate() {
		Measurement m = new Measurement();
		m.setDemographicId(999);
		m.setAppointmentNo(100);
		m.setComments("NUIOBLAHA");
		m.setDataField("DTATAHEROVATA");
		m.setDateObserved(new Date());
		m.setMeasuringInstruction("MSRNIGINSRCTIONS");
		m.setProviderNo("PRVDRE");
		m.setType("TIPPITIP");
		dao.persist(m);
		return m;
	}

	@Test
	public void testFindById() {
		populate();
		populate();

		List<Measurement> m = dao.findByIdTypeAndInstruction(999, "TIPPITIP", "MSRNIGINSRCTIONS");
		assertFalse(m.isEmpty());
	}
}
