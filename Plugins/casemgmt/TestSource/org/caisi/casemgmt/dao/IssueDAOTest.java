package org.caisi.casemgmt.dao;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.caisi.casemgmt.model.Issue;


public class IssueDAOTest extends BaseDAOTestCase {

	private static Log log = LogFactory.getLog(IssueDAOTest.class);
	private IssueDAO dao = null;
	protected SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
	
	protected String getTestDataFile() {
		return "test/data/issue.xml";
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		dao = (IssueDAO)ctx.getBean("IssueDAO");
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
		dao = null;
	}
	
	public void testGetIssue() throws Exception {
		Issue issue = dao.getIssue(new Long(2));
		assertNotNull(issue);
		assertEquals(issue.getId(),new Long(2));
		assertEquals(issue.getCode(),"0010");
		assertEquals(issue.getDescription(),"CHOLERA D/T VIB CHOLERAE");
		assertEquals(issue.getRole(),"doctor");
		assertEquals(dateFormatter.format(issue.getUpdate_date()),"2005-11-08 12:49");	
	}
	
	public void testGetIssues() throws Exception {
		List results = dao.getIssues();
		assertNotNull(results);
		assertEquals(results.size(),2);
	}
	
	public void testSaveIssue() throws Exception {
		Issue issue = new Issue();
		issue.setCode("x10");
		issue.setDescription("test issue");
		issue.setRole("test");
		dao.saveIssue(issue);
		
		assertNotNull(issue.getId());
		Long id = issue.getId();
	}
}
