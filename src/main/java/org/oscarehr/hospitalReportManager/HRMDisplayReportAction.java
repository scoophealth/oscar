package org.oscarehr.hospitalReportManager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.hospitalReportManager.dao.HRMCategoryDao;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentDao;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentSubClassDao;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentToDemographicDao;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentToProviderDao;
import org.oscarehr.hospitalReportManager.dao.HRMSubClassDao;
import org.oscarehr.hospitalReportManager.model.HRMCategory;
import org.oscarehr.hospitalReportManager.model.HRMDocument;
import org.oscarehr.hospitalReportManager.model.HRMDocumentSubClass;
import org.oscarehr.hospitalReportManager.model.HRMDocumentToDemographic;
import org.oscarehr.hospitalReportManager.model.HRMDocumentToProvider;
import org.oscarehr.hospitalReportManager.model.HRMSubClass;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

public class HRMDisplayReportAction extends DispatchAction {

	HRMDocumentDao hrmDocumentDao = (HRMDocumentDao) SpringUtils.getBean("HRMDocumentDao");
	HRMDocumentToDemographicDao hrmDocumentToDemographicDao = (HRMDocumentToDemographicDao) SpringUtils.getBean("HRMDocumentToDemographicDao");
	HRMDocumentToProviderDao hrmDocumentToProviderDao = (HRMDocumentToProviderDao) SpringUtils.getBean("HRMDocumentToProviderDao");
	HRMDocumentSubClassDao hrmDocumentSubClassDao = (HRMDocumentSubClassDao) SpringUtils.getBean("HRMDocumentSubClassDao");
	HRMSubClassDao hrmSubClassDao = (HRMSubClassDao) SpringUtils.getBean("HRMSubClassDao");
	HRMCategoryDao hrmCategoryDao = (HRMCategoryDao) SpringUtils.getBean("HRMCategoryDao");
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String hrmDocumentId = request.getParameter("id");
		
		if (hrmDocumentId != null) {
			HRMDocument document = hrmDocumentDao.findById(Integer.parseInt(hrmDocumentId)).get(0);
			
			if (document != null) {
				HRMReport report = HRMReportParser.parseReport(document.getReportFile());
				
				request.setAttribute("hrmDocument", document);
				
				request.setAttribute("hrmReport", report);
				request.setAttribute("hrmReportId", document.getId());
				request.setAttribute("hrmReportTime", document.getTimeReceived().toString());
				request.setAttribute("hrmDuplicateNum", document.getNumDuplicatesReceived());
				
				List<HRMDocumentToDemographic> demographicLinkList = hrmDocumentToDemographicDao.findByHrmDocumentId(document.getId().toString());
				HRMDocumentToDemographic demographicLink = (demographicLinkList.size() > 0 ? demographicLinkList.get(0) : null);
				request.setAttribute("demographicLink", demographicLink);
				
				List<HRMDocumentToProvider> providerLinkList = hrmDocumentToProviderDao.findByHrmDocumentIdNoSystemUser(document.getId().toString());
				request.setAttribute("providerLinkList", providerLinkList);
				
				List<HRMDocumentSubClass> subClassList = hrmDocumentSubClassDao.getSubClassesByDocumentId(document.getId());
				request.setAttribute("subClassList", subClassList);
				
				String loggedInProviderNo = LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo();
				HRMDocumentToProvider thisProviderLink = hrmDocumentToProviderDao.findByHrmDocumentIdAndProviderNo(document.getId().toString(), loggedInProviderNo);
				request.setAttribute("thisProviderLink", thisProviderLink);
				
				HRMCategory category = null;
				HRMSubClass thisReportSubClassMapping = null;
				
				if (report.getFirstReportClass().equalsIgnoreCase("Diagnostic Imaging Report") || report.getFirstReportClass().equalsIgnoreCase("Cardio Respiratory Report")) {
					// We'll only care about the first one, as long as there is at least one
					if (subClassList != null && subClassList.size() > 0) {
						HRMDocumentSubClass firstSubClass = subClassList.get(0);
						thisReportSubClassMapping = hrmSubClassDao.findApplicableSubClassMapping(report.getFirstReportClass(), firstSubClass.getSubClass(), firstSubClass.getSubClassMnemonic(), report.getSendingFacilityId());
					}
				} else {
					// Medical records report
					String[] reportSubClass = report.getFirstReportSubClass().split("\\^");
					thisReportSubClassMapping = hrmSubClassDao.findApplicableSubClassMapping(report.getFirstReportClass(), reportSubClass[0], null, report.getSendingFacilityId());
				}
				
				if (thisReportSubClassMapping != null) {
					category = thisReportSubClassMapping.getHrmCategory();
				}
				
				request.setAttribute("category", category);
				request.setAttribute("subClassMapping", thisReportSubClassMapping);
				
				// Get all the other HRM documents that are either a child, sibling, or parent
				List<HRMDocument> allDocumentsWithRelationship = hrmDocumentDao.findAllDocumentsWithRelationship(document.getId());
				request.setAttribute("allDocumentsWithRelationship", allDocumentsWithRelationship);
				
				
			}
			
		}
		
		
		return mapping.findForward("display");
	}
	
}
