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
package org.oscarehr.casemgmt.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.oscarehr.PMmodule.dao.ProgramProviderDAO;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.PMmodule.utility.ProgramAccessCache;
import org.oscarehr.PMmodule.utility.RoleCache;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.service.impl.DefaultNoteService;
import org.oscarehr.casemgmt.web.NoteDisplay;
import org.oscarehr.common.dao.DaoTestFixtures;
import org.oscarehr.common.dao.utils.AuthUtils;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;
//Ignored until we fix foreign keys with tests
@Ignore
public class DefaultNoteServiceTest extends DaoTestFixtures {
	private static Logger logger = Logger.getLogger(DefaultNoteServiceTest.class);

	private NoteService service = SpringUtils.getBean(DefaultNoteService.class);

	private CaseManagementManager caseManagementMgr = SpringUtils.getBean(CaseManagementManager.class);

	private ProgramProviderDAO programProviderDao = SpringUtils.getBean(ProgramProviderDAO.class);

	@BeforeClass
	public static void beforeClass() throws Exception {
		SchemaUtils.restoreAllTables();
	}

	@Test
	public void testFindNotes() {
		NoteSelectionCriteria c = new NoteSelectionCriteria();
		c.setDemographicId(1);
		c.setUserRole("doctor,admin");
		c.setUserName("999998");
		c.setProgramId("10016");

		LoggedInInfo loggedInInfo = AuthUtils.initLoginContext();
		loggedInInfo.getCurrentFacility().setIntegratorEnabled(false);

		NoteSelectionResult result = service.findNotes(loggedInInfo, c);
		assertNotNull(result);
		logger.error("Num results " + result.getNotes().size());
	}

	public void createNote(String noteText, Date obsDate, String demographicNo, Provider provider, String providerNo, String programId) {

		CaseManagementNote cmn = new CaseManagementNote();
		cmn.setNote(noteText);
		cmn.setObservation_date(obsDate);

		cmn.setDemographic_no(demographicNo);
		cmn.setProvider(provider);
		cmn.setProviderNo(providerNo);
		cmn.setSigning_provider_no(providerNo);
		cmn.setProgram_no(programId);

		cmn.setReporter_caisi_role("1");

		//reporter_program_team
		cmn.setReporter_program_team("0");
		cmn.setHistory(noteText);
		caseManagementMgr.saveNoteSimple(cmn);
	}

	@Test
	public void testNoteReturnOrder() {

		LoggedInInfo loggedInInfo = AuthUtils.initLoginContext();
		loggedInInfo.getCurrentFacility().setIntegratorEnabled(false);

		String demographicNo = "1";
		String programId = "10016";
		Provider provider = loggedInInfo.getLoggedInProvider();
		String providerNo = loggedInInfo.getLoggedInProviderNo();

		//Add this provider to the program
		ProgramProvider pp = new ProgramProvider();
		pp.setProgramId((long) 10016);
		pp.setProviderNo(loggedInInfo.getLoggedInProviderNo());
		pp.setRoleId((long) 1);
		programProviderDao.saveProgramProvider(pp);

		//These are used by the CaseManagementManager so they need to be initialized
		RoleCache.reload();
		ProgramAccessCache.setAccessMap(10016);

		//Add 40 notes to the same patient advancing the day by 1 for each note. 
		Calendar calendar = new GregorianCalendar(2011, 11, 9);
		int i = 0;

		for (i = 0; i < 40; i++) {
			String noteText = "note #" + i;
			Date obsDate = calendar.getTime();
			createNote(noteText, obsDate, demographicNo, provider, providerNo, programId);
			calendar.add(Calendar.DAY_OF_YEAR, 1);
		}

		int maxResultSize = 15;
		NoteSelectionCriteria c = new NoteSelectionCriteria();
		c.setDemographicId(1);
		c.setUserRole("doctor,admin");
		c.setUserName("999998");
		c.setProgramId("10016");
		c.setMaxResults(maxResultSize);

		NoteSelectionResult result = service.findNotes(loggedInInfo, c);

		List<NoteDisplay> list = result.getNotes();

		//The latest note should be "note #39" from the loop above.  Slicing from the end should have that as the last note, and the first note should be that minus maxResultSize (if there is that many notes.)
		assertEquals(list.get(0).getNote(), "note #" + (i - maxResultSize));
		assertEquals(list.get(list.size() - 1).getNote(), "note #" + (i - 1));

		c.setSliceFromEndOfList(false);
		c.setNoteSort("observation_date_desc");
		result = service.findNotes(loggedInInfo, c);

		list = result.getNotes();
		//for(NoteDisplay noteDisplay: list){
		//	logger.debug(noteDisplay.getNote());
		//}

		//The latest note should be "note #39" from the loop above.  Slicing from the start should have that as the first note, and the last note should be that minus maxResultSize (if there is that many notes.)
		assertEquals(list.get(0).getNote(), "note #" + (i - 1));
		assertEquals(list.get(list.size() - 1).getNote(), "note #" + (i - maxResultSize));

	}

}
