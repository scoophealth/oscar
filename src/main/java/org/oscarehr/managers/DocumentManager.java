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

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.oscarehr.common.dao.CtlDocumentDao;
import org.oscarehr.common.dao.DocumentDao;
import org.oscarehr.common.model.CtlDocument;
import org.oscarehr.common.model.Document;
import org.oscarehr.util.LoggedInInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.io.Files;

import oscar.OscarProperties;
import oscar.dms.EDoc;
import oscar.dms.EDocUtil;
import oscar.log.LogAction;

@Service
public class DocumentManager {
	
	private static String PARENT_DIR = OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
	
	@Autowired
	private DocumentDao documentDao;

	@Autowired
	private CtlDocumentDao ctlDocumentDao;
	
	public Document getDocument(LoggedInInfo loggedInInfo, Integer id)
	{
		Document result=documentDao.find(id);
		
		//--- log action ---
		if (result != null) {
			LogAction.addLogSynchronous(loggedInInfo, "DocumentManager.getDocument", "id=" + id);
		}

		return(result);
	}
	
	public CtlDocument getCtlDocumentByDocumentId(LoggedInInfo loggedInInfo, Integer documentId)
	{
		CtlDocument result=ctlDocumentDao.getCtrlDocument(documentId);
		
		//--- log action ---
		if (result != null) {
			LogAction.addLogSynchronous(loggedInInfo, "DocumentManager.getCtlDocumentByDocumentNoAndModule", "id=" + documentId);
		}

		return(result);
	}
	
	public List<Document> getDocumentsUpdateAfterDate(LoggedInInfo loggedInInfo, Date updatedAfterThisDateExclusive, int itemsToReturn) {
		List<Document> results = documentDao.findByUpdateDate(updatedAfterThisDateExclusive, itemsToReturn);

		LogAction.addLogSynchronous(loggedInInfo, "DocumentManager.getUpdateAfterDate", "updatedAfterThisDateExclusive=" + updatedAfterThisDateExclusive);

		return (results);
	}

	public List<Document> getDocumentsByProgramProviderDemographicDate(LoggedInInfo loggedInInfo, Integer programId, String providerNo, Integer demographicId, Calendar updatedAfterThisDateExclusive, int itemsToReturn) {
		List<Document> results = documentDao.findByProgramProviderDemographicUpdateDate(programId, providerNo, demographicId, updatedAfterThisDateExclusive.getTime(), itemsToReturn);

		LogAction.addLogSynchronous(loggedInInfo, "DocumentManager.getDocumentsByProgramProviderDemographicDate", "programId=" + programId+", providerNo="+providerNo+", demographicId="+demographicId+", updatedAfterThisDateExclusive="+updatedAfterThisDateExclusive.getTime());

		return (results);
	}
	
	public Integer saveDocument( LoggedInInfo loggedInInfo, EDoc edoc ) {
		return this.saveDocument( loggedInInfo, edoc.getDocument(), edoc.getCtlDocument() );
	}

	public Integer saveDocument( LoggedInInfo loggedInInfo, Document document, CtlDocument ctlDocument ) {
		
		DocumentManager.setDocumentFileSignature( loggedInInfo, document );
		Integer savedId = null;
	
		if( document.getId() == null ) {
			savedId = addDocument( loggedInInfo, document );
		} else if( document.getId() > 0 ) {
			savedId = updateDocument( loggedInInfo, document );
		}
		
		ctlDocument.getId().setDocumentNo( savedId );
		
		if( savedId != null ) {
			ctlDocumentDao.persist( ctlDocument );
		}
		
		return savedId;
	}
	
	private Integer addDocument( LoggedInInfo loggedInInfo, Document document ) {
		documentDao.persist( document );
		LogAction.addLogSynchronous(loggedInInfo, "DocumentManager.saveDocument", "Document saved " + document.getId() );
		return document.getId();
	}
	
	private Integer updateDocument( LoggedInInfo loggedInInfo, Document document ) {
		documentDao.merge( document );
		LogAction.addLogSynchronous(loggedInInfo, "DocumentManager.saveDocument", "Document updated " + document.getId() );
		return document.getId();
	}
	
	private static void setDocumentFileSignature( LoggedInInfo loggedInInfo, Document document ) {

		String filesignature = null;
		try {
			filesignature = EDocUtil.calculateFileSignature( new File( document.getDocumentFileFullPath() ) );
		} catch (Exception e) {
			LogAction.addLogSynchronous(loggedInInfo, "DocumentManager.setDocumentFileSignature", "Document filesignature creation failed " + document.getId() );
		} finally {
			document.setFileSignature( filesignature );
		}

	}
	
	public void moveDocument( LoggedInInfo loggedInInfo, Document document, String fromPath, String toPath ) {
		// move the PDF from the temp location to Oscar's document directory.
		try {
			if( toPath == null ) {
				toPath = getParentDirectory();
			}
			File from = new File( String.format("%1$s%2$s%3$s", fromPath, File.separator, document.getDocfilename() ) );
			File to = new File( String.format("%1$s%2$s%3$s", toPath, File.separator, document.getDocfilename() ) );
			Files.move( from , to );
			LogAction.addLogSynchronous(loggedInInfo, "EformDataManager.moveDocument", "Document moved from : " + fromPath + " to " + toPath );
		} catch (IOException e) {
			LogAction.addLogSynchronous(loggedInInfo, "EformDataManager.moveDocument", "Document failed to move from : " + fromPath + " to " + toPath );
		}
	}
	
	public static final String getParentDirectory() {
		return PARENT_DIR;
	}
}
