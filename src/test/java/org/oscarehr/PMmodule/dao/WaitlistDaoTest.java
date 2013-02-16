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
package org.oscarehr.PMmodule.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.PMmodule.wlmatch.CriteriaBO;
import org.oscarehr.PMmodule.wlmatch.CriteriasBO;
import org.oscarehr.PMmodule.wlmatch.VacancyDisplayBO;
import org.oscarehr.common.dao.DaoTestFixtures;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.util.SpringUtils;

public class WaitlistDaoTest extends DaoTestFixtures {

	public WaitlistDao dao = SpringUtils.getBean(WaitlistDao.class);
	
	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("criteria","demographic","vacancy_client_match","eform_data","eform_values","vacancy_client_match","demographic","vacancy_template","vacancy","client_referral","program","criteria_selection_option");
	}
	@Test
	public void testGetClientMatches() {
		assertNotNull(dao.getClientMatches(1));
	}
	
	@Test
	public void testSearchForMatchingEforms() {
		CriteriasBO crits = new CriteriasBO();
		CriteriaBO crit = new CriteriaBO();
		crit.value="test";
		CriteriaBO[] critArray = {crit};
		crits.crits = critArray;
		
		assertEquals(0,dao.searchForMatchingEforms(crits).size());
		
	}
	
	@Test
	public void testListDisplayVacanciesForWaitListProgram() {
		assertNotNull(dao.listDisplayVacanciesForWaitListProgram(1));
	}
	
	@Test
	public void testListDisplayVacanciesForAllWaitListPrograms() {
		assertNotNull(dao.listDisplayVacanciesForAllWaitListPrograms());
	}
	
	@Test
	public void testGetDisplayVacanciesForAgencyProgram() {
		assertNotNull(dao.getDisplayVacanciesForAgencyProgram(1));	
	}

	@Test
	public void testGetDisplayVacancy() {
		assertNull(dao.getDisplayVacancy(45));
	}
	
	@Test
	public void testLoadStats() {
		VacancyDisplayBO vd = new VacancyDisplayBO();
		vd.setVacancyID(1);
		dao.loadStats(vd);
	}
	
}
