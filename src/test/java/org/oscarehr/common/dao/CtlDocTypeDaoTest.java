package org.oscarehr.common.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.model.CtlDocType;
import org.oscarehr.util.SpringUtils;

public class CtlDocTypeDaoTest extends DaoTestFixtures {

	private CtlDocTypeDao dao = (CtlDocTypeDao)SpringUtils.getBean("ctlDocTypeDao");

	public CtlDocTypeDaoTest() {
	}

	@Before
	public void before() throws Exception {
		SchemaUtils.restoreTable("ctl_doctype");
	}

	@Test
	public void findByStatusAndModuleTest() {
		CtlDocType tmp = new CtlDocType();
		tmp.setModule("provider");
		tmp.setDocType("test1");
		tmp.setStatus("H");
		dao.persist(tmp);
		assertNotNull(tmp.getId());

		tmp = new CtlDocType();
		tmp.setModule("provider");
		tmp.setDocType("test2");
		tmp.setStatus("I");
		dao.persist(tmp);
		assertNotNull(tmp.getId());

		int aCount=0;
		List<CtlDocType> result = dao.findByStatusAndModule(new String[]{"A"}, "provider");
		assertNotNull(result);
		aCount = result.size();

		result = dao.findByStatusAndModule(new String[]{"A","H"}, "provider");
		assertNotNull(result);
		assertEquals(aCount+1,result.size());

		result = dao.findByStatusAndModule(new String[]{"A","H","I"}, "provider");
		assertNotNull(result);
		assertEquals(aCount+2,result.size());
	}

	@Test
	public void findByDocTypeAndModuleTest() {
		CtlDocType tmp = new CtlDocType();
		tmp.setModule("provider");
		tmp.setDocType("test1");
		tmp.setStatus("H");
		dao.persist(tmp);
		assertNotNull(tmp.getId());

		assertEquals(dao.findByDocTypeAndModule("test1", "provider").size(),1);
		assertEquals(dao.findByDocTypeAndModule("test1", "demographic").size(),0);
	}
}
