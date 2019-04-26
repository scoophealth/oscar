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

package oscar.oscarLab.ca.all.upload.handlers;

import com.lowagie.text.pdf.PdfReader;

import ca.uhn.fhir.context.FhirContext;

import ca.uhn.fhir.parser.IParser;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;


import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.hl7.fhir.dstu3.model.Attachment;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.CommunicationRequest;

import org.hl7.fhir.dstu3.model.Reference;

import org.oscarehr.common.dao.ProviderInboxRoutingDao;

import org.oscarehr.util.LoggedInInfo;

import org.oscarehr.util.SpringUtils;

import oscar.dms.EDoc;
import oscar.dms.EDocUtil;
import oscar.log.LogAction;
import oscar.log.LogConst;
import oscar.oscarLab.ca.all.util.Utilities;

public class FHIRCommunicationRequestHandler  implements MessageHandler{

	protected static Logger logger = Logger.getLogger(FHIRCommunicationRequestHandler.class);
	private static FhirContext fhirContext = FhirContext.forDstu3();
	private ProviderInboxRoutingDao providerInboxRoutingDao = (ProviderInboxRoutingDao) SpringUtils.getBean("providerInboxRoutingDAO");
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    
    
    
    @Override
    public String parse(LoggedInInfo loggedInInfo, String serviceName, String fileName, int fileId, String ipAddr) {
    		String providerNo = "-1";
    	
    		IParser parser = fhirContext.newJsonParser();
    		BufferedReader in = null;
    		try {
    			in = new BufferedReader(new FileReader(fileName));
    			
    			CommunicationRequest communicationRequest  = parser.parseResource(CommunicationRequest.class, in);
    			List<Reference> refs = communicationRequest.getRecipient();
    			Reference patient = communicationRequest.getSubject();
    			
    			Date createDate = communicationRequest.getAuthoredOn();
    			Attachment attachment = communicationRequest.getPayloadFirstRep().getContentAttachment();
    			String title = attachment.getTitle();
    			String docType = "";
    			CodeableConcept cc = communicationRequest.getCategoryFirstRep();
    			
    			if(cc.getCodingFirstRep().hasSystem() && "http://oscarehr.org/documentType".equals(cc.getCodingFirstRep().getSystem())) {
    				docType = cc.getCodingFirstRep().getCode();
    			}
    			
    			byte[] document = attachment.getData();
    			
    			ByteArrayInputStream is = new ByteArrayInputStream(document);
    			String incomingDocumentFilename = communicationRequest.getIdentifierFirstRep().getValue().replace('/', '-')+"_"+(new Date().getTime())+".pdf";
    			String filePath = Utilities.savePdfFile(is,incomingDocumentFilename);
    			
    			int fileNameIdx = filePath.lastIndexOf("/");
    			filePath = filePath.substring(fileNameIdx+1);
    			
    			EDoc newDoc = new EDoc(title, 
    								  docType, 
    								  filePath, 
    								  "", 
    							      providerNo, 
    							      providerNo, 
    								  "", 
    								  'A',
    								  sdf.format(createDate), 
    								  "", "", "demographic", 
    								  patient.getReference().substring("Patient/".length()), false);
    			
    			 newDoc.setDocPublic("0");    
    			 newDoc.setContentType("application/pdf");
    			 PdfReader reader = new PdfReader(document);          
    	         int numPages = reader.getNumberOfPages();
    	         reader.close();
    	         newDoc.setNumberOfPages(numPages);
    	         
    	         String doc_no = EDocUtil.addDocumentSQL(newDoc);
    	         
    	         for(Reference ref:refs) {
    	        		providerInboxRoutingDao.addToProviderInbox(ref.getReference().substring("Practitioner/".length()), Integer.parseInt(doc_no), "DOC");   
    	         }
    	         
    	         LogAction.addLog(providerNo, LogConst.ADD, LogConst.CON_DOCUMENT, doc_no, ipAddr,"","DocUpload.FHIRCommunicationRequest");
    	         
    		}catch(Exception e) {
    			logger.error("error parsing Document Reference Document from :"+fileName,e);
    		}finally {
    			IOUtils.closeQuietly(in);
        }	
  		
        return "success";
    }
}
