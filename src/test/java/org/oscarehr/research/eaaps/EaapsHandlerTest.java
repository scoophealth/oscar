package org.oscarehr.research.eaaps;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.oscarehr.common.dao.DaoTestFixtures;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.MessageTblDao;
import org.oscarehr.common.dao.StudyDao;
import org.oscarehr.common.dao.StudyDataDao;
import org.oscarehr.common.dao.utils.AuthUtils;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.MessageTbl;
import org.oscarehr.common.model.Study;
import org.oscarehr.common.model.StudyData;
import org.oscarehr.util.SpringUtils;

public class EaapsHandlerTest extends DaoTestFixtures {
	
	@BeforeClass
	public static void init() throws Exception {
		SchemaUtils.restoreAllTables();
		AuthUtils.initLoginContext();
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
