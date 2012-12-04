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

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Facility;
import org.oscarehr.common.model.HnrDataValidation;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.SpringUtils;

public class HnrDataValidationDaoTest extends DaoTestFixtures {

	protected HnrDataValidationDao dao = SpringUtils.getBean(HnrDataValidationDao.class);
	protected FacilityDao facilityDao = SpringUtils.getBean(FacilityDao.class);
	protected DemographicDao demographicDao  =SpringUtils.getBean(DemographicDao.class);
	protected ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);


	@Before
	public void before() throws Exception {
		this.beforeForInnoDB();
		SchemaUtils.restoreTable("Facility","provider","demographic","HnrDataValidation");
	}

	@Test
	public void testCreate() throws Exception {
		Facility f = new Facility();
		f.setDescription("test");
		f.setDisabled(false);
		f.setName("test");
		f.setOcanServiceOrgNumber("0");
		f.setOrgId(0);
		f.setSectorId(0);
		facilityDao.persist(f);
		
		Demographic d = new Demographic();
		d.setFirstName("a");
		d.setLastName("b");
		d.setYearOfBirth("2000");
		d.setMonthOfBirth("1");
		d.setDateOfBirth("1");
		d.setSex("M");
		demographicDao.save(d);
		
		Provider p = new Provider();
		p.setLastName("x");
		p.setFirstName("y");
		p.setProviderNo("111111");
		p.setProviderType("doctor");
		p.setSex("M");
		p.setDob(new java.util.Date());
		p.setSpecialty("MD");
		providerDao.saveProvider(p);
		
		HnrDataValidation entity = new HnrDataValidation();
		EntityDataGenerator.generateTestDataForModelClass(entity);
		entity.setFacilityId(f.getId());
		entity.setClientId(d.getDemographicNo());
		entity.setValidatorProviderNo(p.getProviderNo());
		entity.setValidationType(HnrDataValidation.Type.HC_INFO);
		dao.persist(entity);
		assertNotNull(entity.getId());
	}
}
