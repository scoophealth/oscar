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
import static org.junit.Assert.fail;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.RaHeader;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class RaHeaderDaoTest extends DaoTestFixtures {

	protected RaHeaderDao dao = SpringUtils.getBean(RaHeaderDao.class);
	DateFormat dfm = new SimpleDateFormat("yyyyMMdd");

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("raheader", "radetail", "provider", "providersite", "demographic", "provider_facility", "Facility");
	}

	@Test
	public void testCreate() throws Exception {
		RaHeader ql = new RaHeader();
		EntityDataGenerator.generateTestDataForModelClass(ql);
		dao.persist(ql);
		assertNotNull(ql.getId());
	}

	@Test
	public void testFindCurrentByFilenamePaymentDate() throws Exception {

		String filename1 = "alpha";
		String filename2 = "bravo";

		String status1 = "A";
		String status2 = "D";

		RaHeader raHeader1 = new RaHeader();
		EntityDataGenerator.generateTestDataForModelClass(raHeader1);
		raHeader1.setFilename(filename1);
		raHeader1.setStatus(status1);
		raHeader1.setPaymentDate("20110101");
		dao.persist(raHeader1);

		RaHeader raHeader2 = new RaHeader();
		EntityDataGenerator.generateTestDataForModelClass(raHeader2);
		raHeader2.setFilename(filename1);
		raHeader2.setStatus(status2);
		raHeader2.setPaymentDate("20080101");
		dao.persist(raHeader2);

		RaHeader raHeader3 = new RaHeader();
		EntityDataGenerator.generateTestDataForModelClass(raHeader3);
		raHeader3.setFilename(filename1);
		raHeader3.setStatus(status1);
		raHeader3.setPaymentDate("20050101");
		dao.persist(raHeader3);

		RaHeader raHeader4 = new RaHeader();
		EntityDataGenerator.generateTestDataForModelClass(raHeader4);
		raHeader4.setFilename(filename1);
		raHeader4.setStatus(status1);
		raHeader4.setPaymentDate("20110101");
		dao.persist(raHeader4);

		RaHeader raHeader5 = new RaHeader();
		EntityDataGenerator.generateTestDataForModelClass(raHeader5);
		raHeader5.setFilename(filename2);
		raHeader5.setStatus(status1);
		raHeader5.setPaymentDate("20110101");
		dao.persist(raHeader5);

		List<RaHeader> expectedResult = new ArrayList<RaHeader>(Arrays.asList(raHeader1, raHeader4));
		List<RaHeader> result = dao.findCurrentByFilenamePaymentDate(filename1, "20110101");

		Logger logger = MiscUtils.getLogger();

		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match.");
			fail("Array sizes do not match.");
		}
		for (int i = 0; i < expectedResult.size(); i++) {
			if (!expectedResult.get(i).equals(result.get(i))) {
				logger.warn("Items  do not match.");
				fail("Items  do not match.");
			}
		}
		assertTrue(true);
	}

	@Test
	public void testFindByFilenamePaymentDate() throws Exception {

		String filename1 = "alpha";
		String filename2 = "bravo";

		RaHeader raHeader1 = new RaHeader();
		EntityDataGenerator.generateTestDataForModelClass(raHeader1);
		raHeader1.setFilename(filename1);
		raHeader1.setPaymentDate("20110101");
		dao.persist(raHeader1);

		RaHeader raHeader2 = new RaHeader();
		EntityDataGenerator.generateTestDataForModelClass(raHeader2);
		raHeader2.setFilename(filename1);
		raHeader2.setPaymentDate("20080101");
		dao.persist(raHeader2);

		RaHeader raHeader3 = new RaHeader();
		EntityDataGenerator.generateTestDataForModelClass(raHeader3);
		raHeader3.setFilename(filename1);
		raHeader3.setPaymentDate("20050101");
		dao.persist(raHeader3);

		RaHeader raHeader4 = new RaHeader();
		EntityDataGenerator.generateTestDataForModelClass(raHeader4);
		raHeader4.setFilename(filename1);
		raHeader4.setPaymentDate("20110101");
		dao.persist(raHeader4);

		RaHeader raHeader5 = new RaHeader();
		EntityDataGenerator.generateTestDataForModelClass(raHeader5);
		raHeader5.setFilename(filename2);
		raHeader5.setPaymentDate("20110101");
		dao.persist(raHeader5);

		List<RaHeader> expectedResult = new ArrayList<RaHeader>(Arrays.asList(raHeader1, raHeader4));
		List<RaHeader> result = dao.findByFilenamePaymentDate(filename1, "20110101");

		Logger logger = MiscUtils.getLogger();

		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match.");
			fail("Array sizes do not match.");
		}
		for (int i = 0; i < expectedResult.size(); i++) {
			if (!expectedResult.get(i).equals(result.get(i))) {
				logger.warn("Items  do not match.");
				fail("Items  do not match.");
			}
		}
		assertTrue(true);
	}

	@Test
	public void testFindAllExcludeStatus() throws Exception {

		String status1 = "A";
		String status2 = "B";

		String paymentDate1 = "20110510";
		String paymentDate2 = "20091004";
		String paymentDate3 = "20080729";
		String paymentDate4 = "20041108";

		String readDate1 = "20140510";
		String readDate2 = "20131004";
		String readDate3 = "20120729";
		String readDate4 = "20101108";

		RaHeader raHeader1 = new RaHeader();
		EntityDataGenerator.generateTestDataForModelClass(raHeader1);
		raHeader1.setStatus(status1);
		raHeader1.setPaymentDate(paymentDate1);
		raHeader1.setReadDate(readDate1);
		dao.persist(raHeader1);

		RaHeader raHeader2 = new RaHeader();
		EntityDataGenerator.generateTestDataForModelClass(raHeader2);
		raHeader2.setStatus(status2);
		raHeader2.setPaymentDate(paymentDate2);
		raHeader2.setReadDate(readDate2);
		dao.persist(raHeader2);

		RaHeader raHeader3 = new RaHeader();
		EntityDataGenerator.generateTestDataForModelClass(raHeader3);
		raHeader3.setStatus(status1);
		raHeader3.setPaymentDate(paymentDate3);
		raHeader3.setReadDate(readDate3);
		dao.persist(raHeader3);

		RaHeader raHeader4 = new RaHeader();
		EntityDataGenerator.generateTestDataForModelClass(raHeader4);
		raHeader4.setStatus(status2);
		raHeader4.setPaymentDate(paymentDate4);
		raHeader4.setReadDate(readDate4);
		dao.persist(raHeader4);

		RaHeader raHeader5 = new RaHeader();
		EntityDataGenerator.generateTestDataForModelClass(raHeader5);
		raHeader5.setStatus(status1);
		raHeader5.setPaymentDate(paymentDate1);
		raHeader5.setReadDate(readDate1);
		dao.persist(raHeader5);

		List<RaHeader> expectedResult = new ArrayList<RaHeader>(Arrays.asList(raHeader2, raHeader4));
		List<RaHeader> result = dao.findAllExcludeStatus(status1);

		Logger logger = MiscUtils.getLogger();

		if (result.size() != expectedResult.size()) {
			logger.warn("Array sizes do not match.");
			fail("Array sizes do not match.");
		}
		for (int i = 0; i < expectedResult.size(); i++) {
			if (!expectedResult.get(i).equals(result.get(i))) {
				logger.warn("Items  do not match.");
				fail("Items  do not match.");
			}
		}
		assertTrue(true);
	}

	@Test
	public void testFindByHeaderDetailsAndProviderMagic() {
		assertNotNull(dao.findByHeaderDetailsAndProviderMagic("STS", "100"));
	}

    @Test
    public void testFindByStatusAndProviderMagic() {
	    assertNotNull(dao.findByStatusAndProviderMagic("STS", "100"));
    }
}