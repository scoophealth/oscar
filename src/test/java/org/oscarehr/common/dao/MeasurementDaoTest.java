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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.oscarehr.common.dao.MeasurementDao.SearchCriteria;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.Measurement;
import org.oscarehr.util.SpringUtils;

public class MeasurementDaoTest extends DaoTestFixtures {

	protected MeasurementDao dao = SpringUtils.getBean(MeasurementDao.class);

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("demographic", "program", "demographicExt", "measurements", "measurementType", "measurementsExt", "measurementMap", "provider");
	}

	@Test
	@Ignore
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

		c = new SearchCriteria();
		ms = dao.find(c);
		assertNotNull(ms);

		c = new SearchCriteria();
		c.setComments(m.getComments());
		ms = dao.find(c);
		assertNotNull(ms);

		c = new SearchCriteria();
		c.setDataField(m.getDataField());
		ms = dao.find(c);
		assertNotNull(ms);

		c = new SearchCriteria();
		c.setDateObserved(m.getDateObserved());
		ms = dao.find(c);
		assertNotNull(ms);

		c = new SearchCriteria();
		c.setDemographicNo(m.getDemographicId());
		ms = dao.find(c);
		assertNotNull(ms);

		c = new SearchCriteria();
		c.setMeasuringInstrc(m.getMeasuringInstruction());
		ms = dao.find(c);
		assertNotNull(ms);

		c = new SearchCriteria();
		c.setType(m.getType());
		ms = dao.find(c);
		assertNotNull(ms);

		c = new SearchCriteria();
		c.setDataField(m.getDataField());
		c.setDateObserved(m.getDateObserved());
		c.setMeasuringInstrc(m.getMeasuringInstruction());
		c.setType(m.getType());
		ms = dao.find(c);
		assertNotNull(ms);

		c = new SearchCriteria();
		c.setComments(m.getComments());
		c.setDataField(m.getDataField());
		c.setDemographicNo(m.getDemographicId());
		c.setMeasuringInstrc(m.getMeasuringInstruction());
		ms = dao.find(c);
		assertNotNull(ms);
	}

	protected Measurement populate() {
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

	@Test
	public void testFindByDemographicIdUpdatedAfterDate() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -1);

		Measurement m = new Measurement();
		m.setDemographicId(1);
		m.setAppointmentNo(100);
		m.setComments("NUIOBLAHA");
		m.setDataField("DTATAHEROVATA");
		m.setDateObserved(cal.getTime());
		m.setCreateDate(cal.getTime());
		m.setMeasuringInstruction("MSRNIGINSRCTIONS");
		m.setProviderNo("PRVDRE");
		m.setType("TIPPITIP");
		dao.persist(m);

		m = new Measurement();
		m.setDemographicId(1);
		m.setAppointmentNo(100);
		m.setComments("NUIOBLAHA");
		m.setDataField("DTATAHEROVATA");
		m.setDateObserved(new Date());
		m.setMeasuringInstruction("MSRNIGINSRCTIONS");
		m.setProviderNo("PRVDRE");
		m.setType("TIPPITIP");
		dao.persist(m);

		cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);

		assertEquals(1, dao.findByDemographicIdUpdatedAfterDate(1, cal.getTime()).size());
		
		cal=new GregorianCalendar();
		cal.add(Calendar.DAY_OF_YEAR, -1);
		List<Measurement> results=dao.findByCreateDate(cal.getTime(), 99);
		assertTrue(results.size()>0);

		cal.add(Calendar.DAY_OF_YEAR, 2);
		results=dao.findByCreateDate(cal.getTime(), 99);
		assertEquals(0, results.size());

	}

	@Test
	public void testFindMeasurementsByDemographicIdAndLocationCode() {
		assertNotNull(dao.findMeasurementsByDemographicIdAndLocationCode(100, "CDE"));
	}

	@Test
	public void testFindMeasurementsWithIdentifiersByDemographicIdAndLocationCode() {
		assertNotNull(dao.findMeasurementsWithIdentifiersByDemographicIdAndLocationCode(100, "CDE"));
	}

	@Test
	public void testFindLabNumbers() {
		assertNotNull(dao.findLabNumbers(100, "CDE"));
	}
	
	@Test
	public void testFindLastEntered() {
		dao.findLastEntered(100, "CDE");
	}

	@Test
	public void testFindMeasurementsAndProviders() {
		assertNotNull(dao.findMeasurementsAndProviders(100));
	}

	@Test
	public void testFindMeasurementsAndProvidersByType() {
		assertNotNull(dao.findMeasurementsAndProvidersByType("TYPE", 100));
	}

	@Test
	public void testFindMeasurementsAndProvidersByDemoAndType() {
		dao.findMeasurementsAndProvidersByDemoAndType(100, "TYPE");
	}
	
	@Test
	public void testFindByValue() {
		assertNotNull(dao.findByValue("ZPA", "ZPA"));
	}

    @Test
    public void testFindObservationDatesByDemographicNoTypeAndMeasuringInstruction() {
	    assertNotNull(dao.findObservationDatesByDemographicNoTypeAndMeasuringInstruction(100, "TYPE", "INSTR"));
    }

    @Test
    public void testFindByDemographicNoTypeAndDate() {
	    dao.findByDemographicNoTypeAndDate(100, "TUY", new Date());
	    
    }
}
