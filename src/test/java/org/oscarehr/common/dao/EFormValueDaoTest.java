package org.oscarehr.common.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.EFormValue;
import org.oscarehr.util.SpringUtils;

public class EFormValueDaoTest extends DaoTestFixtures {

	private EFormValueDao eFormValueDao = (EFormValueDao) SpringUtils.getBean("EFormValueDao");

	public EFormValueDaoTest() {
	}

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable(new String[]{"eform_values"});
	}

	@Test
	public void testFindByFormDataIdList() {
		EFormValue v1 = generateEFormValue(1);
		EFormValue v2 = generateEFormValue(1);
		EFormValue v3 = generateEFormValue(2);
		EFormValue v4 = generateEFormValue(3);
		eFormValueDao.persist(v1);
		eFormValueDao.persist(v2);
		eFormValueDao.persist(v3);
		eFormValueDao.persist(v4);

		List<Integer> searchIds = new ArrayList<Integer>();
		searchIds.add(1);
		searchIds.add(2);
		List<EFormValue> results = eFormValueDao.findByFormDataIdList(searchIds);
		assertEquals(3,results.size());
	}

	private EFormValue generateEFormValue(int fdid) {
		EFormValue v1 = new EFormValue();
		v1.setDemographicId(1);
		v1.setFormDataId(fdid);
		v1.setFormId(1);
		v1.setVarName("a");
		v1.setVarValue("b");

		return v1;
	}


}
