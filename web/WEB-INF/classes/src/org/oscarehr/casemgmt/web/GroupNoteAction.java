package org.oscarehr.casemgmt.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oscarehr.PMmodule.service.AdmissionManager;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.PMmodule.web.CreateAnonymousClientAction;
import org.oscarehr.casemgmt.dao.CaseManagementIssueDAO;
import org.oscarehr.casemgmt.dao.CaseManagementNoteDAO;
import org.oscarehr.casemgmt.dao.IssueDAO;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.casemgmt.web.formbeans.CaseManagementEntryFormBean;
import org.oscarehr.common.dao.GroupNoteDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.GroupNoteLink;
import org.oscarehr.util.SpringUtils;


public class GroupNoteAction {

	static Log logger = LogFactory.getLog(GroupNoteAction.class);
	
	private static CaseManagementManager caseManagementManager=(CaseManagementManager)SpringUtils.getBean("caseManagementManager");	  
	private static CaseManagementNoteDAO caseManagementNoteDao=(CaseManagementNoteDAO)SpringUtils.getBean("caseManagementNoteDAO");
    private static CaseManagementIssueDAO caseManagementIssueDao=(CaseManagementIssueDAO)SpringUtils.getBean("caseManagementIssueDAO");
	private static IssueDAO issueDao = (IssueDAO)SpringUtils.getBean("IssueDAO");
	private static ProgramManager programManager = (ProgramManager) SpringUtils.getBean("programManager");
    private static AdmissionManager admissionManager = (AdmissionManager) SpringUtils.getBean("admissionManager");
	private static GroupNoteDao groupNoteDao = (GroupNoteDao)SpringUtils.getBean("groupNoteDao");
	
	public static int saveGroupNote(CaseManagementEntryFormBean cform, String programId) {
		logger.info("saving group note");

		String ids[] = cform.getGroupNoteClientIds();
		int totalAnonymous = cform.getGroupNoteTotalAnonymous();
		
		logger.info("group note will have " + ids.length + " clients, and " + totalAnonymous + " anonymous clients");
				
		List<Demographic> anonymousClients = new ArrayList<Demographic>();
		
		//create anonymous clients
		for(int x=0;x<totalAnonymous;x++) {
			Demographic d = CreateAnonymousClientAction.generateAnonymousClient(Integer.valueOf(programId));
			anonymousClients.add(d);
		}
		
		logger.info("created anonymous clients");
		
		//save links
		for(String id:ids) {
			GroupNoteLink link = new GroupNoteLink();
			link.setNoteId(Integer.valueOf(cform.getNoteId()));
			link.setDemographicNo(Integer.valueOf(id));
			groupNoteDao.persist(link);
		}
		
		for(Demographic d:anonymousClients) {
			GroupNoteLink link = new GroupNoteLink();
			link.setNoteId(Integer.valueOf(cform.getNoteId()));
			link.setDemographicNo(d.getDemographicNo());
			groupNoteDao.persist(link);
		}

		logger.info("links saved");
        return 0;
	}
}
