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
/**
 * @author Shazib
 */
package org.oscarehr.common.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.util.SpringUtils;

public class Hl7TextInfoDaoTest extends DaoTestFixtures {

	protected Hl7TextInfoDao dao = SpringUtils.getBean(Hl7TextInfoDao.class);

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("hl7TextInfo", "patientLabRouting", "hl7TextInfo", "providerLabRouting", "ctl_document", "demographic", "hl7TextMessage");
	}

	@Test
	public void testCreateUpdateLabelByLabNumber() {
		dao.createUpdateLabelByLabNumber("10", 10);
	}

	@Test
	public void testFindByDemographicId() {
		dao.findByDemographicId(10);
	}

	@Test
	public void testFindByHealthCardNo() {
		dao.findByHealthCardNo("HIN");
	}

	@Test
	public void testFindByLabId() {
		dao.findByLabId(10);
	}

	@Test
	public void testfindByLabIdViaMagic() {
		dao.findByLabIdViaMagic(10);
	}

	@Test
	public void testfindLabAndDocsViaMagic() {
		Integer page = 0;
		Integer pageSize = 10;

		boolean isPaged, mixLabsAndDocs, isAbnormal, searchProvider, patientSearch;
		boolean[] truthTable = new boolean[] { 
				true,   true,   true,   true,   true,   
				true, 	true, 	true, 	true, 	false, 
				true, 	true, 	true, 	false, 	true, 
				true, 	true, 	true, 	false, 	false, 
				true, 	true, 	false, 	true, 	true, 
				true, 	true, 	false, 	true, 	false, 
				true, 	true, 	false, 	false, 	true, 
				true, 	true, 	false, 	false, 	false, 
				true, 	false, 	true, 	true, 	true, 
				true, 	false, 	true, 	true, 	false, 
				true, 	false, 	true, 	false, 	true, 
				true, 	false, 	true, 	false, 	false, 
				true, 	false, 	false, 	true, 	true, 
				true, 	false, 	false, 	true, 	false, 
				true, 	false, 	false, 	false, 	true, 
				true, 	false, 	false, 	false, 	false, 
				false, 	true, 	true, 	true, 	true, 
				false, 	true, 	true, 	true, 	false, 
				false, 	true, 	true, 	false, 	true, 
				false, 	true, 	true, 	false, 	false, 
				false, 	true, 	false, 	true, 	true, 
				false, 	true, 	false, 	true, 	false, 
				false, 	true, 	false, 	false, 	true, 
				false, 	true, 	false, 	false, 	false, 
				false, 	false, 	true, 	true, 	true, 
				false, 	false, 	true, 	true, 	false, 
				false, 	false, 	true, 	false, 	true, 
				false, 	false, 	true, 	false, 	false, 
				false, 	false, 	false, 	true, 	true, 
				false, 	false, 	false, 	true, 	false, 
				false, 	false, 	false, 	false, 	true, 
				false, 	false, 	false, 	false, 	false
			};

		for (int i = 0; i < truthTable.length; i = i + 5) {
			isPaged = truthTable[i];
			mixLabsAndDocs = truthTable[i + 1];
			isAbnormal = truthTable[i + 2];
			searchProvider = truthTable[i + 3];
			patientSearch = truthTable[i + 4];

			dao.findLabAndDocsViaMagic("PROVIDER", "DEMOGRAPHIC", "FNAME", "LNAME", "HIN", "STATUS", isPaged, page, pageSize, mixLabsAndDocs, isAbnormal, searchProvider, patientSearch);
			dao.findLabAndDocsViaMagic("0", "0", "", "", "", "", isPaged, page, pageSize, mixLabsAndDocs, isAbnormal, searchProvider, patientSearch);
		}
	}

	@Test
	public void testfindLabId() {
		dao.findLabId(0);
	}

	@Test
	public void testfindLabsViaMagic() {
		dao.findLabsViaMagic("GVNO", "GVNO", "GVNO", "GVNO", "GVNO");
	}

	@Test
	public void testgetAllLabsByLabNumberResultStatus() {
		dao.getAllLabsByLabNumberResultStatus();
	}

	@Test
	public void testgetMatchingLabs() {
		dao.getMatchingLabs("BLYA");
	}

	@Test
	public void testsearchByAccessionNumber() {
		dao.searchByAccessionNumber("PZDTS");
	}

	@Test
	public void testsearchByFillerOrderNumber() {
		dao.searchByFillerOrderNumber("PRSHA", "ZHPA");
	}

	@Test
	public void testupdateReportStatusByLabId() {
		dao.updateReportStatusByLabId("STR", 0);
	}

	@Test
	public void testupdateResultStatusByLabId() {
		dao.updateResultStatusByLabId("STS", 0);
	}

    @Test
    public void test() {
	    assertNotNull(dao.findDisciplines(100));
    }

}
