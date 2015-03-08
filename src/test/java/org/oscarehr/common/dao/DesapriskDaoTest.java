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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.Desaprisk;
import org.oscarehr.util.SpringUtils;

public class DesapriskDaoTest extends DaoTestFixtures {

	protected DesapriskDao dao = SpringUtils.getBean(DesapriskDao.class);
	DateFormat dfm = new SimpleDateFormat("yyyyMMdd");

	public DesapriskDaoTest() {
	}


	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("desaprisk");
	}

	@Test
	public void testCreate() throws Exception {
		Desaprisk entity = new Desaprisk();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		dao.persist(entity);

		assertNotNull(entity.getId());
	}
	
	@Test
	public void testSearch() throws Exception {
		//formNo <= ? and x.demographicNo=? order by x.formNo DESC, x.desapriskDate DESC, x.desapriskTime
		Date desapriskDate1 = new Date(dfm.parse("20060101").getTime());
		Date desapriskDate2 = new Date(dfm.parse("20050101").getTime());
		Date desapriskDate3 = new Date(dfm.parse("20070101").getTime());
		
		Date desapriskTime1 = new Date(dfm.parse("20060101").getTime());
		Date desapriskTime2 = new Date(dfm.parse("20050101").getTime());
		Date desapriskTime3 = new Date(dfm.parse("20070101").getTime());
		
		int formNo1 = 101;
		int formNo2 = 202;
		
		int demographicNo1 = 111;
		int demographicNo2 = 222;
			
		Desaprisk desaprisk1 = new Desaprisk();
		EntityDataGenerator.generateTestDataForModelClass(desaprisk1);
		desaprisk1.setFormNo(formNo1);
		desaprisk1.setDemographicNo(demographicNo1);
		desaprisk1.setDesapriskDate(desapriskDate1);
		desaprisk1.setDesapriskTime(desapriskTime1);
		dao.persist(desaprisk1);
		
		Desaprisk desaprisk2 = new Desaprisk();
		EntityDataGenerator.generateTestDataForModelClass(desaprisk2);
		desaprisk2.setFormNo(formNo2);
		desaprisk2.setDemographicNo(demographicNo2);
		desaprisk2.setDesapriskDate(desapriskDate2);
		desaprisk2.setDesapriskTime(desapriskTime2);
		dao.persist(desaprisk2);
		
		Desaprisk desaprisk3 = new Desaprisk();
		EntityDataGenerator.generateTestDataForModelClass(desaprisk3);
		desaprisk3.setFormNo(formNo1);
		desaprisk3.setDemographicNo(demographicNo1);
		desaprisk3.setDesapriskDate(desapriskDate3);
		desaprisk3.setDesapriskTime(desapriskTime3);
		dao.persist(desaprisk3);
		
		Desaprisk expectedResult = desaprisk3;
		Desaprisk result = dao.search(formNo1, demographicNo1);
		
		assertEquals(expectedResult, result);
	}
}