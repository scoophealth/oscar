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
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentCommentDao;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentDao;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentSubClassDao;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentToDemographicDao;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentToProviderDao;
import org.oscarehr.hospitalReportManager.model.HRMDocument;
import org.oscarehr.hospitalReportManager.model.HRMDocumentComment;
import org.oscarehr.hospitalReportManager.model.HRMDocumentSubClass;
import org.oscarehr.hospitalReportManager.model.HRMDocumentToDemographic;
import org.oscarehr.hospitalReportManager.model.HRMDocumentToProvider;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class HRMModifyDocumentAction extends DispatchAction {

	HRMDocumentDao hrmDocumentDao = (HRMDocumentDao) SpringUtils.getBean("HRMDocumentDao");
	HRMDocumentToDemographicDao hrmDocumentToDemographicDao = (HRMDocumentToDemographicDao) SpringUtils.getBean("HRMDocumentToDemographicDao");
	HRMDocumentToProviderDao hrmDocumentToProviderDao = (HRMDocumentToProviderDao) SpringUtils.getBean("HRMDocumentToProviderDao");
	HRMDocumentSubClassDao hrmDocumentSubClassDao = (HRMDocumentSubClassDao) SpringUtils.getBean("HRMDocumentSubClassDao");
	HRMDocumentCommentDao hrmDocumentCommentDao = (HRMDocumentCommentDao) SpringUtils.getBean("HRMDocumentCommentDao");

	public ActionForward undefined(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String method = request.getParameter("method");

		if (method != null) {
			if (method.equalsIgnoreCase("makeIndependent"))
				return makeIndependent(mapping, form, request, response);
			else if (method.equalsIgnoreCase("signOff"))
				return signOff(mapping, form, request, response);
			else if (method.equalsIgnoreCase("assignProvider"))
				return assignProvider(mapping, form, request, response);
			else if (method.equalsIgnoreCase("removeDemographic"))
				return removeDemographic(mapping, form, request, response);
			else if (method.equalsIgnoreCase("assignDemographic"))
				return assignDemographic(mapping, form, request, response);
			else if (method.equalsIgnoreCase("makeActiveSubClass"))
				return makeActiveSubClass(mapping, form, request, response);
			else if (method.equalsIgnoreCase("removeProvider"))
				return removeProvider(mapping, form, request, response);
			else if (method.equalsIgnoreCase("addComment"))
				return addComment(mapping, form, request, response);
			else if (method.equalsIgnoreCase("deleteComment"))
				return deleteComment(mapping, form, request, response);
		}

		return mapping.findForward("ajax");
	}

	public ActionForward makeIndependent(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String reportId = request.getParameter("reportId");

		try {
			HRMDocument document = hrmDocumentDao.find(Integer.parseInt(reportId));
			if (document.getParentReport() != null) {
				document.setParentReport(null);
				hrmDocumentDao.merge(document);
			} else {
				// This is the parent document; choose the closest one 
				List<HRMDocument> documentChildren = hrmDocumentDao.getAllChildrenOf(document.getId());
				if (documentChildren != null && documentChildren.size() > 0) {
					HRMDocument newParentDoc = documentChildren.get(0);
					newParentDoc.setParentReport(null);
					hrmDocumentDao.merge(newParentDoc);
					for (HRMDocument childDoc : documentChildren) {
						if (childDoc.getId().intValue() != newParentDoc.getId().intValue()) {
							childDoc.setParentReport(newParentDoc.getId());
							hrmDocumentDao.merge(childDoc);
						}
					}
				}
			}

			request.setAttribute("success", true);
		} catch (Exception e) {
			MiscUtils.getLogger().error("Tried to set make document independent but failed.", e); 
			request.setAttribute("success", false);
		}

		return mapping.findForward("ajax");
	}

	public ActionForward signOff(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String reportId = request.getParameter("reportId");
		String providerNo = LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo();

		try {
			String signedOff = request.getParameter("signedOff");
			HRMDocumentToProvider providerMapping = hrmDocumentToProviderDao.findByHrmDocumentIdAndProviderNo(reportId, providerNo);

			if (providerMapping != null) {
				providerMapping.setSignedOff(Integer.parseInt(signedOff));
				providerMapping.setSignedOffTimestamp(new Date());
				hrmDocumentToProviderDao.merge(providerMapping);
			}
			else
			{
				HRMDocumentToProvider hrmDocumentToProvider=new HRMDocumentToProvider();
				hrmDocumentToProvider.setHrmDocumentId(reportId);
				hrmDocumentToProvider.setProviderNo(providerNo);
				hrmDocumentToProvider.setSignedOff(Integer.parseInt(signedOff));
				hrmDocumentToProvider.setSignedOffTimestamp(new Date());
				hrmDocumentToProviderDao.persist(hrmDocumentToProvider);
			}
			
			request.setAttribute("success", true);
		} catch (Exception e) {
			MiscUtils.getLogger().error("Tried to set signed off status on document but failed.", e); 
			request.setAttribute("success", false);
		}

		return mapping.findForward("ajax");
	}

	public ActionForward assignProvider(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String hrmDocumentId = request.getParameter("reportId");
		String providerNo = request.getParameter("providerNo");

		try {
			HRMDocumentToProvider providerMapping = new HRMDocumentToProvider();

			providerMapping.setHrmDocumentId(hrmDocumentId);
			providerMapping.setProviderNo(providerNo);
			providerMapping.setSignedOff(0);

			hrmDocumentToProviderDao.merge(providerMapping);

			request.setAttribute("success", true);
		} catch (Exception e) {
			MiscUtils.getLogger().error("Tried to assign HRM document to provider but failed.", e); 
			request.setAttribute("success", false);
		}

		return mapping.findForward("ajax");
	}

	public ActionForward removeDemographic(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String hrmDocumentId = request.getParameter("reportId");

		try {
			List<HRMDocumentToDemographic> currentMappingList = hrmDocumentToDemographicDao.findByHrmDocumentId(hrmDocumentId);

			if (currentMappingList != null) {
				for (HRMDocumentToDemographic currentMapping : currentMappingList) {
					hrmDocumentToDemographicDao.remove(currentMapping.getId());
				}
			}

			request.setAttribute("success", true);
		} catch (Exception e) {
			MiscUtils.getLogger().error("Tried to remove HRM document from demographic but failed.", e); 
			request.setAttribute("success", false);
		}

		return mapping.findForward("ajax");

	}

	public ActionForward assignDemographic(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String hrmDocumentId = request.getParameter("reportId");
		String demographicNo = request.getParameter("demographicNo");

		try {
			try {
				List<HRMDocumentToDemographic> currentMappingList = hrmDocumentToDemographicDao.findByHrmDocumentId(hrmDocumentId);

				if (currentMappingList != null) {
					for (HRMDocumentToDemographic currentMapping : currentMappingList) {
						hrmDocumentToDemographicDao.remove(currentMapping);
					}
				}
			} catch (Exception e) {
				// Do nothing
			}

			HRMDocumentToDemographic demographicMapping = new HRMDocumentToDemographic();

			demographicMapping.setHrmDocumentId(hrmDocumentId);
			demographicMapping.setDemographicNo(demographicNo);
			demographicMapping.setTimeAssigned(new Date());

			hrmDocumentToDemographicDao.merge(demographicMapping);

			request.setAttribute("success", true);
		} catch (Exception e) {
			MiscUtils.getLogger().error("Tried to assign HRM document to demographic but failed.", e); 
			request.setAttribute("success", false);
		}

		return mapping.findForward("ajax");
	}

	public ActionForward makeActiveSubClass(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String hrmDocumentId = request.getParameter("reportId");
		String subClassId = request.getParameter("subClassId");

		try {
			hrmDocumentSubClassDao.setAllSubClassesForDocumentAsInactive(Integer.parseInt(hrmDocumentId));

			HRMDocumentSubClass newActiveSubClass = hrmDocumentSubClassDao.find(Integer.parseInt(subClassId));
			if (newActiveSubClass != null) {
				newActiveSubClass.setActive(true);
				hrmDocumentSubClassDao.merge(newActiveSubClass);
			}

			request.setAttribute("success", true);

		} catch (Exception e) {
			MiscUtils.getLogger().error("Tried to change active subclass but failed.", e); 
			request.setAttribute("success", false);
		}



		return mapping.findForward("ajax");
	}

	public ActionForward removeProvider(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String providerMappingId = request.getParameter("providerMappingId");

		try {
			hrmDocumentToProviderDao.remove(Integer.parseInt(providerMappingId));

			request.setAttribute("success", true);
		} catch (Exception e) {
			MiscUtils.getLogger().error("Tried to remove provider from HRM document but failed.", e); 
			request.setAttribute("success", false);
		}

		return mapping.findForward("ajax");
	}


	public ActionForward addComment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String documentId = request.getParameter("reportId");

		String commentString = request.getParameter("comment");

		try {
			HRMDocumentComment comment = new HRMDocumentComment();

			comment.setHrmDocumentId(Integer.parseInt(documentId));
			comment.setComment(commentString);
			comment.setCommentTime(new Date());
			comment.setProviderNo(LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo());

			hrmDocumentCommentDao.merge(comment);
			request.setAttribute("success", true);
		} catch (Exception e) {
			MiscUtils.getLogger().error("Couldn't add a comment for HRM document", e);
			request.setAttribute("success", false);
		}
		
		return mapping.findForward("ajax");
	}
	
	public ActionForward deleteComment(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String commentId = request.getParameter("commentId");
		
		try {
			hrmDocumentCommentDao.deleteComment(commentId);
			request.setAttribute("success", true);
		} catch (Exception e) {
			MiscUtils.getLogger().error("Couldn't delete comment on HRM document", e);
			request.setAttribute("success", false);
		}
		
		return mapping.findForward("ajax");
	}




}
