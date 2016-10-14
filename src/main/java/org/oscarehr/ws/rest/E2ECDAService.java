/**
 * Copyright (c) 2013-2015. Leverage Analytics. All Rights Reserved.
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
 */
package org.oscarehr.ws.rest;


import java.io.StringWriter;

import javax.ws.rs.GET;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.stream.XMLStreamException;

import org.apache.log4j.Logger;
import org.marc.everest.formatters.interfaces.IFormatterGraphResult;
import org.marc.everest.formatters.xml.its1.XmlIts1Formatter;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ClinicalDocument;
import org.marc.everest.xml.XMLStateStreamWriter;
import org.oscarehr.common.exception.AccessDeniedException;
import org.oscarehr.common.exception.CDAValidationException;
import org.oscarehr.common.exception.InternalServerException;
import org.oscarehr.common.exception.NotFoundException;
import org.oscarehr.common.exception.PatientDirectiveException;
import org.oscarehr.common.exception.XMLValidationException;
import org.oscarehr.common.model.Provider;
import org.oscarehr.e2e.constant.Constants;
import org.oscarehr.e2e.director.E2ECreator;
import org.oscarehr.e2e.util.E2EEverestValidator;
import org.oscarehr.e2e.util.E2EXSDValidator;
import org.oscarehr.e2e.util.EverestUtils;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import oscar.OscarProperties;
import oscar.log.LogAction;


/**
 * Defines a service contract for main operations of E2ECDASErvice. 
 */
@Path("/e2eCDA")
@Component("e2eCDAService")
@Produces("application/xml")
public class E2ECDAService extends AbstractServiceImpl {
	
	@Autowired
	protected SecurityInfoManager securityInfoManager;
	
	protected Logger logger = MiscUtils.getLogger();
	/**
	 * This service produces an E2E CDA document for the demographic number which is provided as a parameter
	 * The CDA document style sheet is configured is oscarproperties.
	 * 
	 * @param demographic: the demographic # of the patient whose chart is to be viewed
	 * 
	 * @param id
	 * 		Id of the demographic to get E2E CDA for 
	 * @return
	 * 		Returns data for the demographic provided 
	 */
	@GET
	@Path("/{dataId}")
	@Produces({MediaType.APPLICATION_JSON , MediaType.APPLICATION_XML})
	public Response getDemographicData(@PathParam("dataId") Integer id) throws PatientDirectiveException {
		
		LoggedInInfo loggedInInfo = getLoggedInInfo();
		
		if(!securityInfoManager.isAllowedAccessToPatientRecord(loggedInInfo, id)) {
			String accessDeniedToDemographic = "Access Denied to Demographic ";//TODO: internationalize
			Provider provider = loggedInInfo.getLoggedInProvider();
			String firstName = provider.getFirstName();
			String lastName = provider.getLastName();
			String message = firstName + " " + lastName + ": " + accessDeniedToDemographic + " " + id;
			AccessDeniedException ade = new AccessDeniedException(message);
			MiscUtils.getLogger().error(ade.getMessage(), ade);
			addLogSynchronous(loggedInInfo, message, "id=" + id);
			throw ade;
		}

	    ClinicalDocument clinicalDocument = createEmrConversionDocument(id);
	    
	    if(clinicalDocument==null)
	    {
	    	String noRecordFound = "No record found for demographic ";//TODO: Internationalization
	    	String message = noRecordFound + id;
	    	MiscUtils.getLogger().error(message);
	    	throw new NotFoundException(message);
	    }
	    
	    try{
		    StringWriter sw = new StringWriter();
		    XMLStateStreamWriter xssw = EverestUtils.getXMLStateStreamWriter(sw);

		    XmlIts1Formatter fmtr = EverestUtils.getFormatter(true);
		    IFormatterGraphResult details = fmtr.graph(xssw, clinicalDocument);
		    
		    xssw.writeEndElement();
			xssw.writeEndDocument();
			xssw.close();
			
			if(!isValidCDA(details)) {//TODO: Handle this error in the E2E code better
				CDAValidationException cdaValidationException = new CDAValidationException("Invalid CDA created for demographic " + id);
				logger.error(cdaValidationException.getCause().getMessage());
			}
			
			String output = prettyFormatXML(sw.toString(), Constants.XML.INDENT);
			// Temporary Everest Bugfixes
			output = EverestUtils.everestBugFixes(output);
			
			if(!isValidXML(output)) {
				throw new XMLValidationException("CDA that is not conformant to E2E schema created for demographic " + id);
			}
	
		    if(output==null)
		    {
		    	String demographicNotFound = "Demographic not found";//TODO: internationalization
		    	String message = demographicNotFound + ": " + id;
		    	MiscUtils.getLogger().error(message);
		    	throw new NotFoundException(message);
		    }
		    
		    //inject the stylesheet
		    OscarProperties oscarProperties = OscarProperties.getInstance();
		    
		    int styleSheetIndex = output.indexOf('\n');
		    String styleSheetPath = oscarProperties.getProperty("STYLE_SHEET_PATH", "../stylesheets/lantana.xsl");
		    String styleSheetTag = "<?xml-stylesheet type=\"text/xsl\" href=\"" + styleSheetPath + "\" ?>";
		    
		    //emit the page
		    output = output.substring(0, styleSheetIndex) + "\n" + 
		    		styleSheetTag + 
		    		output.substring(styleSheetIndex+1, output.length());
		    return Response.status(Response.Status.OK).entity(output).build();
	    } catch(XMLStreamException e) {
	    	throw new InternalServerException( e.getMessage() );
	    }
	}
	
	protected ClinicalDocument createEmrConversionDocument(int id) {
		return E2ECreator.createEmrConversionDocument(id);
	}
	
	protected Boolean isValidCDA(IFormatterGraphResult details) {
		return E2EEverestValidator.isValidCDA(details);
	}
	
	protected Boolean isValidXML(String output) {
		return E2EXSDValidator.isValidXML(output);
	}
	
	protected String prettyFormatXML(String string, Integer indent) {
		return EverestUtils.prettyFormatXML(string, indent);
	}
	
	protected void addLogSynchronous(LoggedInInfo loggedInInfo, String message, String paramValue) {
		LogAction.addLogSynchronous(loggedInInfo, message, paramValue);
	}
	
}
