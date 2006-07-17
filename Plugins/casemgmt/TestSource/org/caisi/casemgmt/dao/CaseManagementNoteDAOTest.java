package org.caisi.casemgmt.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.caisi.casemgmt.model.CaseManagementNote;


public class CaseManagementNoteDAOTest extends BaseDAOTestCase {

	private static Log log = LogFactory.getLog(CaseManagementNoteDAOTest.class);
	private CaseManagementNoteDAO dao = null;
	
	
	protected String getTestDataFile() {
		return "test/data/case_management.xml";
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		dao = (CaseManagementNoteDAO)ctx.getBean("CaseManagementNoteDAO");
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
		dao = null;
	}
	
	public void testGetNote() throws Exception {
		CaseManagementNote note = dao.getNote(new Long(1));
		assertNotNull(note);
		assertEquals(note.getDemographic_no(),"1");
		assertEquals(note.getProvider_no(),"999998");
		assertEquals(note.getBilling_code(),"0010");
		assertEquals(note.getEncounter_type(),"clinic");
		assertEquals(note.getNote(),"this is a note about an encounter");
		assertEquals(note.isSigned(),false);
		assertEquals(note.getSigning_provider_no(),"");
	}
	
	public void testSaveNote() throws Exception {
		CaseManagementNote note = new CaseManagementNote();
		note.setDemographic_no("1");
		note.setProvider_no("999998");
		note.setBilling_code("0010");
		note.setEncounter_type("clinic");
		note.setNote("this is a note about an encounter");
		note.setSigned(false);
		note.setSigning_provider_no("");
		
		dao.saveNote(note);
		
		assertNotNull(note.getId());
	}
	
	public void testGetNotesByDemographic() throws Exception {
		List results = dao.getNotesByDemographic("1");
		assertNotNull(results);
		System.out.println(results.size());
		assertEquals(results.size(),3);
	}
	
}
