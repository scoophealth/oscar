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
package org.oscarehr.research.eaaps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.oscarehr.common.dao.DaoTestFixtures;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.DxresearchDAO;
import org.oscarehr.common.dao.MessageTblDao;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Dxresearch;
import org.oscarehr.common.model.MessageTbl;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
//Ignored until we fix foreign keys with tests
@Ignore
public class EaapsHandlerTest extends DaoTestFixtures {
	
	@Before
	public void beforeTest() {
		OscarProperties props = OscarProperties.getInstance();
		props.setProperty("DOCUMENT_DIR", System.getProperty("buildDirectory"));
	}
	
	@BeforeClass
	public static void init() throws Exception {
		SchemaUtils.restoreAllTables();
	}
	
	private String getHash(Demographic demo) {
		String hash = new EaapsHash(demo).getHash();
		return hash;
	}
	
	public String getHL7(Demographic demo) {
		String hash = getHash(demo);
		
		String result = 
				"MSH|^~\\&|SENDING APP||||" + EaapsIntegrationTest.TIMESTAMP_STRING + ".007-0400||ORU^R01|2501|01|2.2|1\r" + 
						"PID||" + hash + "\r" + 
						"OBR||||SERVICE ID: EAAPS||||||||||||" + EaapsIntegrationTest.PROVIDER_ID + "\r" + 
						"NTE|1|eaaps_" + hash + "_" + System.currentTimeMillis() + ".pdf|" + EaapsIntegrationTest.PDF + "\r" +
						"NTE|2||Note comment for message with the AAP attachment " + hash + "\r" +
						"NTE|3||MRP message for message with the AAP attachment " + hash + "\r";
		return result;
	}
	
	@Test
	public void testProviderParsing() throws Exception {
		DemographicDao demoDao = SpringUtils.getBean(DemographicDao.class);
		Demographic demo = demoDao.getDemographic("1");
		if (demo == null) {
			demo = new Demographic();
			demo.setFirstName("John");
			demo.setLastName("Johnson");
			demo.setSex("M");
			demo.setProviderNo("999998");
			
			demoDao.save(demo);
		}
		
		DxresearchDAO dxresearchDAO = SpringUtils.getBean(DxresearchDAO.class);
		Dxresearch dxresearch = new Dxresearch();
		
		dxresearch.setCodingSystem("icd9");
		dxresearch.setDemographicNo(demo.getDemographicNo());
		dxresearch.setStatus('A');
		dxresearch.setDxresearchCode("493");
		
		dxresearchDAO.persist(dxresearch);
		
		/* Study is no longer running
		StudyDao studyDao = SpringUtils.getBean(StudyDao.class);
		Study study = studyDao.findByName("eAAPS Study");
		if (study == null) {
			study = new Study();
			study.setCurrent1(1);
			study.setDescription("Test study");
			study.setProviderNo("999998");
			study.setRemoteServerUrl("http://eaaps");
			study.setStudyName("eAAPS Study");
			study.setStudyLink("http://eaaps");
			study.setTimestamp(new Date());
			studyDao.saveEntity(study);
		}
		
		StudyDataDao studyDataDao = SpringUtils.getBean(StudyDataDao.class);
		List<StudyData> data = studyDataDao.findByDemoAndStudy(demo.getDemographicNo(), study.getId());
		if (data.isEmpty()) {
			StudyData studyData = new StudyData();
			studyData.setDemographicNo(demo.getDemographicNo());
			studyData.setProviderNo("999998");
			studyData.setContent(getHash(demo));
			studyDataDao.saveEntity(studyData);
		}
		*/
		
		String hl7 = getHL7(demo);
		EaapsHandler handler = new EaapsHandler();
		handler.init(hl7);
		
		MessageTblDao mtDao = SpringUtils.getBean(MessageTblDao.class);
		List<MessageTbl> messages = mtDao.findAll(0, 100);
		assertFalse(messages.isEmpty());
		
		MessageTbl m = messages.get(0);
		assertEquals("Must be sent by system", "System", m.getSentBy());
		assertEquals("Must be sent by system", "-1", m.getSentByNo());
	}
	
}
