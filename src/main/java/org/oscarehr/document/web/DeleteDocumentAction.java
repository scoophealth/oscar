/**
 * Copyright (c) 2013-2015. Department of Computer Science, University of Victoria. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Computer Science
 * LeadLab
 * University of Victoria
 * Victoria, Canada
 */


package org.oscarehr.document.web;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.dao.*;
import org.oscarehr.common.model.*;
import org.oscarehr.integration.cdx.dao.CdxDocumentDao;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import oscar.log.LogAction;
import oscar.log.LogConst;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class DeleteDocumentAction extends DispatchAction {

	private static Logger log = MiscUtils.getLogger();

	private DocumentDao documentDao = SpringUtils.getBean(DocumentDao.class);
	private CtlDocumentDao ctlDocumentDao = SpringUtils.getBean(CtlDocumentDao.class);
	private CdxDocumentDao cdxDocumentDao = SpringUtils.getBean(CdxDocumentDao.class);
	private ProviderInboxRoutingDao providerInboxRoutingDAO = SpringUtils.getBean(ProviderInboxRoutingDao.class);
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);

	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		return null;
	}

	public ActionForward deleteDocument(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		String ret = "";

		String documentId = request.getParameter("documentId");

		int docNo = -1;


		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_edoc", "w", null)) {
        	throw new SecurityException("missing required security object (_edoc)");
        }

		LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.ADD, LogConst.CON_DOCUMENT, documentId, request.getRemoteAddr());

		try {
			docNo = Integer.parseInt(documentId);


			for (ProviderInboxItem prov : providerInboxRoutingDAO.getProvidersWithRoutingForDocument("DOC", docNo)) {
				providerInboxRoutingDAO.removeLinkFromDocument("DOC", docNo, prov.getProviderNo());
			}

			documentDao.deleteDocument(docNo);
			ctlDocumentDao.deleteDocument(docNo);

			cdxDocumentDao.deleteDocument(docNo);

		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
		}

		return null;

	}


}
