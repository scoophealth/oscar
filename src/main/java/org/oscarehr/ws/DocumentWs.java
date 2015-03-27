/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */

package org.oscarehr.ws;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.jws.WebService;
import javax.xml.ws.WebServiceException;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.cxf.annotations.GZIP;
import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.common.model.CtlDocument;
import org.oscarehr.common.model.Document;
import org.oscarehr.managers.DocumentManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.ws.transfer_objects.DocumentTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@WebService
@Component
@GZIP(threshold = AbstractWs.GZIP_THRESHOLD)
public class DocumentWs extends AbstractWs {
	private Logger logger = MiscUtils.getLogger();

	@Autowired
	private DocumentManager documentManager;

	@Autowired
	private ProgramManager programManager;

	public DocumentTransfer getDocument(Integer documentId) {
		try {
			LoggedInInfo loggedInInfo = getLoggedInInfo();
			Document document = documentManager.getDocument(loggedInInfo, documentId);
			CtlDocument ctlDocument = documentManager.getCtlDocumentByDocumentId(loggedInInfo, documentId);
			return (DocumentTransfer.toTransfer(document, ctlDocument));
		} catch (IOException e) {
			logger.error("Unexpected error", e);
			throw (new WebServiceException(e));
		}
	}

	public DocumentTransfer[] getDocumentsUpdateAfterDate(Date updatedAfterThisDateExclusive, int itemsToReturn) {
		LoggedInInfo loggedInInfo = getLoggedInInfo();
		List<Document> documents = documentManager.getDocumentsUpdateAfterDate(loggedInInfo, updatedAfterThisDateExclusive, itemsToReturn);
		return (DocumentTransfer.getTransfers(loggedInInfo, documents));
	}

	public DocumentTransfer[] getDocumentsByProgramProviderDemographicDate(Integer programId, String providerNo, Integer demographicId, Calendar updatedAfterThisDateExclusive, int itemsToReturn) {
		LoggedInInfo loggedInInfo = getLoggedInInfo();
		List<Document> documents = documentManager.getDocumentsByProgramProviderDemographicDate(loggedInInfo, programId, providerNo, demographicId, updatedAfterThisDateExclusive, itemsToReturn);
		logger.debug("programId="+programId+", providerNo="+providerNo+", demographicId="+demographicId+", updatedAfterThisDateExclusive="+DateFormatUtils.ISO_DATETIME_FORMAT.format(updatedAfterThisDateExclusive)+", itemsToReturn="+itemsToReturn+", results="+documents.size());
		return (DocumentTransfer.getTransfers(loggedInInfo, documents));
	}
}
