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


package oscar.oscarLab.ca.all.pageUtil;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.DataTypeUtils;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.OruR01;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.OscarToOscarUtils;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.OruR01.ObservationData;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Provider;
import org.oscarehr.ui.servlet.ContentRenderingServlet;

import oscar.oscarLab.ca.all.parsers.Factory;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v26.message.ORU_R01;
import ca.uhn.hl7v2.model.v26.segment.PID;
import ca.uhn.hl7v2.parser.EncodingNotSupportedException;

public final class ViewOruR01UIBean {
	private String segmentId;
	private ORU_R01 oruR01;
	private Demographic demographic;
	private ObservationData observationData;
	
	public ViewOruR01UIBean(String segmentId) throws EncodingNotSupportedException, HL7Exception, UnsupportedEncodingException
	{
		this.segmentId=segmentId;
		
		String hl7Message=Factory.getHL7Body(segmentId);
		oruR01=(ORU_R01) OscarToOscarUtils.pipeParserParse(hl7Message);
		
		PID pid=oruR01.getPATIENT_RESULT(0).getPATIENT().getPID();
		demographic=DataTypeUtils.parsePid(pid);
		
		observationData=OruR01.getObservationData(oruR01);
	}
	
	public String getFromProviderDisplayString() throws HL7Exception
	{
		return(getProviderDisplayString(DataTypeUtils.ACTION_ROLE_SENDER));
	}
	
	public String getToProviderDisplayString() throws HL7Exception
	{
		return(getProviderDisplayString(DataTypeUtils.ACTION_ROLE_RECEIVER));
	}
	
	private String getProviderDisplayString(String actionRole) throws HL7Exception
	{
		Provider provider=OruR01.getProviderByActionRole(oruR01, actionRole);
		
		StringBuilder sb = new StringBuilder();

		sb.append(provider.getLastName());
		sb.append(", ");
		sb.append(provider.getFirstName());

		if (provider.getProviderNo() != null) {
			sb.append(" (");
			sb.append(provider.getProviderNo());
			sb.append(')');
		}

		if (provider.getPhone() != null) {
			sb.append(", ");
			sb.append(provider.getPhone());
		}

		if (provider.getEmail() != null) {
			sb.append(", ");
			sb.append(provider.getEmail());
		}

		if (provider.getAddress() != null) {
			sb.append(", ");
			sb.append(provider.getAddress());
		}

		return (StringEscapeUtils.escapeHtml(sb.toString()));
	}
	
	public String getClientDisplayName()
	{
		StringBuilder sb=new StringBuilder();
		
		sb.append(demographic.getLastName());
		sb.append(", ");
		sb.append(demographic.getFirstName());
		
		if (demographic.getSex()!=null){
			sb.append(" (");
			sb.append(demographic.getSex());
			sb.append(')');
		}
		
		return(StringEscapeUtils.escapeHtml(sb.toString()));
	}
	
	public String getHinForDisplay()
	{
		if (demographic.getHin()==null) return("");
		return(StringEscapeUtils.escapeHtml(demographic.getHin()));
	}
	
	public String getBirthDayForDisplay()
	{
		if (demographic.getBirthDay()==null) return("");
		return(DateFormatUtils.ISO_DATE_FORMAT.format(demographic.getBirthDay()));
	}
	
	public String getSubjectForDisplay()
	{
		return(StringEscapeUtils.escapeHtml(observationData.subject));
	}
	
	public String getTextMessageForDisplay()
	{
		if (observationData.textMessage==null) return("");
		return(StringEscapeUtils.escapeHtml(observationData.textMessage));
	}

	public boolean hasBinaryFile()
	{
		return(observationData.binaryDataFileName!=null);
	}

	public String getBinaryFilenameForDisplay()
	{
		if (observationData.binaryDataFileName==null) return("");
		return(StringEscapeUtils.escapeHtml(observationData.binaryDataFileName));
	}

	public String getFilename()
	{
		return(observationData.binaryDataFileName);
	}
	
	public byte[] getFileContents()
	{
		return(observationData.binaryData);
	}
	
	/**
	 * The context path is prepended in this url
	 */
	public String getContentRenderingUrl(HttpServletRequest request, boolean download)
	{
		// http://127.0.0.1:8080/oscar/contentRenderingServlet/asdf.jpg?source=oruR01&segmentId=67&download=t
		
		StringBuilder sb=new StringBuilder();

		sb.append(request.getContextPath());
		sb.append("/contentRenderingServlet/");
		sb.append(getBinaryFilenameForDisplay());
		sb.append("?source=");
		sb.append(ContentRenderingServlet.Source.oruR01.name());
		sb.append("&segmentId=");
		sb.append(segmentId);

		if (download) sb.append("&download=true");
		
		return(sb.toString());
	}
	
	public String getPreviewFileHtml(HttpServletRequest request)
	{
		String filename=observationData.binaryDataFileName;
		
		if (filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith(".gif"))
		{
			return("<img style=\"max-width:800px;max-height:600px\" src=\""+getContentRenderingUrl(request, false)+"\" alt=\""+getBinaryFilenameForDisplay()+"\" />");
		}
		
		return("Preview not supported for this type of content.");
	}
}
