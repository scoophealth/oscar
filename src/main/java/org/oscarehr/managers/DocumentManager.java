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

package org.oscarehr.managers;

import java.util.List;

import org.oscarehr.common.dao.CtlDocumentDao;
import org.oscarehr.common.dao.DocumentDao;
import org.oscarehr.common.model.CtlDocument;
import org.oscarehr.common.model.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oscar.log.LogAction;

@Service
public class DocumentManager {
	@Autowired
	private DocumentDao documentDao;

	@Autowired
	private CtlDocumentDao ctlDocumentDao;
	
	/**
	 * @param archived can be null for both deleted and non deleted items 
	 */
	public List<Document> getDocumentsByIdStart(Boolean archived, Integer startIdInclusive, int itemsToReturn) {
		List<Document> results = documentDao.findByIdStart(archived, startIdInclusive, itemsToReturn);

		//--- log action ---
		if (results.size()>0) {
			String resultIds=Document.getIdsAsStringList(results);
			LogAction.addLogSynchronous("DocumentManager.getDocumentsByIdStart", "ids returned=" + resultIds);
		}

		return (results);
	}
	
	public Document getDocument(Integer id)
	{
		Document result=documentDao.find(id);
		
		//--- log action ---
		if (result != null) {
			LogAction.addLogSynchronous("DocumentManager.getDocument", "id=" + id);
		}

		return(result);
	}
	
	public CtlDocument getCtlDocumentByDocumentId(Integer documentId)
	{
		CtlDocument result=ctlDocumentDao.getCtrlDocument(documentId);
		
		//--- log action ---
		if (result != null) {
			LogAction.addLogSynchronous("DocumentManager.getCtlDocumentByDocumentNoAndModule", "id=" + documentId);
		}

		return(result);
	}
}
