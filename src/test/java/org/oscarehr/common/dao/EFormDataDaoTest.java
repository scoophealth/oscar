package org.oscarehr.common.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.EFormData;
import org.oscarehr.util.SpringUtils;

public class EFormDataDaoTest extends DaoTestFixtures {

	private EFormDataDao eFormDataDao = (EFormDataDao) SpringUtils.getBean("EFormDataDao");

	public EFormDataDaoTest() {
	}

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable(new String[]{"eform_data"});
	}

	@Test
	public void testGetByDemographic() throws Exception {
		EFormData model = new EFormData();
		EntityDataGenerator.generateTestDataForModelClass(model);
		model.setDemographicId(1);
		eFormDataDao.persist(model);
		assertNotNull(model.getId());

		assertEquals(1,eFormDataDao.findByDemographicId(1).size());
		assertEquals(0,eFormDataDao.findByDemographicId(2).size());
	}

	@Test
	public void testGetByDemographicAndLastDate() throws Exception {
		EFormData model = new EFormData();
		EntityDataGenerator.generateTestDataForModelClass(model);
		model.setDemographicId(1);
		Calendar cal = Calendar.getInstance();
		//set to 5 mins ago
		cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE)-5);
		model.setFormTime(cal.getTime());
		model.setFormDate(cal.getTime());

		eFormDataDao.persist(model);
		assertNotNull(model.getId());

		//set to 10 mins ago .. so we only want forms in the last 10 minutes
		Calendar cal2 = Calendar.getInstance();
		cal2.set(Calendar.MINUTE, cal2.get(Calendar.MINUTE)-10);

		List<EFormData> results = eFormDataDao.findByDemographicIdSinceLastDate(1, cal2.getTime());
		assertEquals(1,results.size());

		cal = Calendar.getInstance();
		//set yesterday
		cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)-1);
		model.setFormTime(cal.getTime());
		model.setFormDate(cal.getTime());
		eFormDataDao.merge(model);

		results = eFormDataDao.findByDemographicIdSinceLastDate(1, cal2.getTime());
		assertEquals(0,results.size());

		cal = Calendar.getInstance();
		//set today, but too early
		cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE)-20);
		model.setFormTime(cal.getTime());
		model.setFormDate(cal.getTime());
		eFormDataDao.merge(model);

		results = eFormDataDao.findByDemographicIdSinceLastDate(1, cal2.getTime());
		assertEquals(0,results.size());
	}
}
