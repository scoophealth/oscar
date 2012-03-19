package org.oscarehr.common.dao;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.EntityDataGenerator;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.DemographicArchive;
import org.oscarehr.util.SpringUtils;

public class DemographicArchiveDaoTest extends DaoTestFixtures {

	private DemographicArchiveDao dao = (DemographicArchiveDao)SpringUtils.getBean("demographicArchiveDao");

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("demographicArchive");
	}

	@Test
	public void testCreate() throws Exception {
		DemographicArchive entity = new DemographicArchive();
		EntityDataGenerator.generateTestDataForModelClass(entity);

		dao.persist(entity);
		assertNotNull(entity.getId());
	}

	@Test
	public void testArchiveRecord() throws Exception {
		Demographic d = new Demographic();
		EntityDataGenerator.generateTestDataForModelClass(d);
		dao.archiveRecord(d);

	}


}
