/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

package org.oscarehr.hospitalReportManager;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.hospitalReportManager.dao.HRMCategoryDao;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentCommentDao;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentDao;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentSubClassDao;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentToDemographicDao;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentToProviderDao;
import org.oscarehr.hospitalReportManager.dao.HRMProviderConfidentialityStatementDao;
import org.oscarehr.hospitalReportManager.model.HRMCategory;
import org.oscarehr.hospitalReportManager.model.HRMDocument;
import org.oscarehr.hospitalReportManager.model.HRMDocumentComment;
import org.oscarehr.hospitalReportManager.model.HRMDocumentSubClass;
import org.oscarehr.hospitalReportManager.model.HRMDocumentToDemographic;
import org.oscarehr.hospitalReportManager.model.HRMDocumentToProvider;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class HRMDisplayReportAction extends DispatchAction {

	private static Logger logger=MiscUtils.getLogger();
	
	private static HRMDocumentDao hrmDocumentDao = (HRMDocumentDao) SpringUtils.getBean("HRMDocumentDao");
	private static HRMDocumentToDemographicDao hrmDocumentToDemographicDao = (HRMDocumentToDemographicDao) SpringUtils.getBean("HRMDocumentToDemographicDao");
	private static HRMDocumentToProviderDao hrmDocumentToProviderDao = (HRMDocumentToProviderDao) SpringUtils.getBean("HRMDocumentToProviderDao");
	private static HRMDocumentSubClassDao hrmDocumentSubClassDao = (HRMDocumentSubClassDao) SpringUtils.getBean("HRMDocumentSubClassDao");
	//private static HRMSubClassDao hrmSubClassDao = (HRMSubClassDao) SpringUtils.getBean("HRMSubClassDao");
	private static HRMCategoryDao hrmCategoryDao = (HRMCategoryDao) SpringUtils.getBean("HRMCategoryDao");
	private static HRMDocumentCommentDao hrmDocumentCommentDao = (HRMDocumentCommentDao) SpringUtils.getBean("HRMDocumentCommentDao");
	private static HRMProviderConfidentialityStatementDao hrmProviderConfidentialityStatementDao = (HRMProviderConfidentialityStatementDao) SpringUtils.getBean("HRMProviderConfidentialityStatementDao");
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
    
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String hrmDocumentId = request.getParameter("id");
		
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);

		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_hrm", "r", null)) {
        	throw new SecurityException("missing required security object (_hrm)");
        }
		
		if (hrmDocumentId != null) {
                    HRMDocument document = hrmDocumentDao.findById(Integer.parseInt(hrmDocumentId)).get(0);

                    if (document != null) {
                        logger.debug("reading repotFile : "+document.getReportFile());
                        HRMReport report = HRMReportParser.parseReport(loggedInInfo, document.getReportFile());
                        
                        request.setAttribute("hrmDocument", document);

                        if (report != null) {
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

                            HRMDocumentToProvider thisProviderLink = hrmDocumentToProviderDao.findByHrmDocumentIdAndProviderNo(document.getId().toString(), loggedInInfo.getLoggedInProviderNo());
                            request.setAttribute("thisProviderLink", thisProviderLink);

                            if (thisProviderLink != null) {
                                thisProviderLink.setViewed(1);
                                hrmDocumentToProviderDao.merge(thisProviderLink);
                            }

                            HRMDocumentSubClass hrmDocumentSubClass=null;
                            if (subClassList!= null)
                            {
                            	for (HRMDocumentSubClass temp : subClassList)
                            	{
                            		if (temp.isActive())
                            		{
                            			hrmDocumentSubClass=temp;
                            			break;
                            		}
                            	}
                            }
                            
                            HRMCategory category = null;
                            if (hrmDocumentSubClass != null) {
                                category = hrmCategoryDao.findBySubClassNameMnemonic(hrmDocumentSubClass.getSubClass()+':'+hrmDocumentSubClass.getSubClassMnemonic());
                            }
                            else
                            {
                            	category=hrmCategoryDao.findBySubClassNameMnemonic("DEFAULT");
                            }
                            
                            request.setAttribute("category", category);                            

                            // Get all the other HRM documents that are either a child, sibling, or parent
                            List<HRMDocument> allDocumentsWithRelationship = hrmDocumentDao.findAllDocumentsWithRelationship(document.getId());
                            request.setAttribute("allDocumentsWithRelationship", allDocumentsWithRelationship);


                            List<HRMDocumentComment> documentComments = hrmDocumentCommentDao.getCommentsForDocument(Integer.parseInt(hrmDocumentId));
                            request.setAttribute("hrmDocumentComments", documentComments);


                            String confidentialityStatement = hrmProviderConfidentialityStatementDao.getConfidentialityStatementForProvider(loggedInInfo.getLoggedInProviderNo());
                            request.setAttribute("confidentialityStatement", confidentialityStatement);
                            
                            String duplicateLabIdsString=StringUtils.trimToNull(request.getParameter("duplicateLabIds"));
                            Map<Integer,Date> dupReportDates = new HashMap<Integer,Date>();
                            Map<Integer,Date> dupTimeReceived = new HashMap<Integer,Date>();
                            
                            if (duplicateLabIdsString!=null) {
                            	String[] duplicateLabIdsStringSplit=duplicateLabIdsString.split(",");
                            	for (String tempId : duplicateLabIdsStringSplit) {
                            		HRMDocument doc = hrmDocumentDao.find(Integer.parseInt(tempId));
                            		dupReportDates.put(Integer.parseInt(tempId),doc.getReportDate());
                            		dupTimeReceived.put(Integer.parseInt(tempId),doc.getTimeReceived());
                            	}
                            
                            }
                            
                            request.setAttribute("dupReportDates",dupReportDates);
                            request.setAttribute("dupTimeReceived", dupTimeReceived);
                        }
                    }
			
		}
		
		
		return mapping.findForward("display");
	}
	
	public static HRMDocumentToProvider getHRMDocumentFromProvider(String providerNo, Integer hrmDocumentId)
	{
		return(hrmDocumentToProviderDao.findByHrmDocumentIdAndProviderNo(hrmDocumentId.toString(), providerNo));
	}
}
