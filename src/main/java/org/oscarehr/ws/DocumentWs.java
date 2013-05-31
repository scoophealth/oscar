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
import java.util.GregorianCalendar;
import java.util.List;

import javax.jws.WebService;
import javax.xml.ws.WebServiceException;

import org.apache.cxf.annotations.GZIP;
import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.common.model.CtlDocument;
import org.oscarehr.common.model.Document;
import org.oscarehr.managers.DocumentManager;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.ws.transfer_objects.DataIdTransfer;
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
			Document document = documentManager.getDocument(documentId);
			CtlDocument ctlDocument = documentManager.getCtlDocumentByDocumentId(documentId);
			return (DocumentTransfer.toTransfer(document, ctlDocument));
		} catch (IOException e) {
			logger.error("Unexpected error", e);
			throw (new WebServiceException(e));
		}
	}

	/**
	 * Get a list of DataIdTransfer objects for documents starting with the passed in Id.
	 */
	public DataIdTransfer[] getDocumentDataIds(Boolean active, Integer startIdInclusive, int itemsToReturn) {

		Boolean archived=null;
		if (active!=null) archived=!active;

		List<Document> documents = documentManager.getDocumentsByIdStart(archived, startIdInclusive, itemsToReturn);

		DataIdTransfer[] results = new DataIdTransfer[documents.size()];
		for (int i = 0; i < documents.size(); i++) {
			Document document = documents.get(i);
			CtlDocument ctlDocument = documentManager.getCtlDocumentByDocumentId(document.getDocumentNo());
			results[i] = getDataIdTransfer(document, ctlDocument);
		}

		return (results);
	}

	private DataIdTransfer getDataIdTransfer(Document document, CtlDocument ctlDocument) {
		DataIdTransfer result = new DataIdTransfer();

		Calendar cal = new GregorianCalendar();
		cal.setTime(document.getUpdatedatetime());
		result.setCreateDate(cal);

		result.setCreatorProviderId(document.getDoccreator());
		result.setDataId(document.getId().toString());
		result.setDataType(Document.class.getSimpleName());

		if (ctlDocument != null && "demographic".equals(ctlDocument.getId().getModule())) result.setOwnerDemographicId(ctlDocument.getId().getModuleId());

		Integer programId = document.getProgramId();
		// some one used -1 as none in this table instead of null...
		if (programId != null && programId != -1) {
			result.setClinicId(programId);

			if (programId != null) {
				Program program = programManager.getProgram(programId);
				if (program != null) {
					result.setFacilityId(program.getFacilityId());
				}
			}
		}

		return (result);
	}
}
