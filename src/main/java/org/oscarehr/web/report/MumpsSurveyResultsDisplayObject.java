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


package org.oscarehr.web.report;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.oscarehr.myoscar_server.ws.SurveyResultTransfer;
import org.oscarehr.phr.util.MumpsResultWrapper;
import org.xml.sax.SAXException;

import oscar.util.DateUtils;

public class MumpsSurveyResultsDisplayObject {
	private SurveyResultTransfer surveyResultTransfer;
	private MumpsResultWrapper mumpsResultWrapper;
	
	public MumpsSurveyResultsDisplayObject(SurveyResultTransfer surveyResultTransfer) throws IOException, SAXException, ParserConfigurationException
	{
		this.surveyResultTransfer=surveyResultTransfer;
		this.mumpsResultWrapper=MumpsResultWrapper.getMumpsResultWrapper(surveyResultTransfer.getData());
	}

	public SurveyResultTransfer getSurveyResultTransfer() {
    	return surveyResultTransfer;
    }

	public MumpsResultWrapper getMumpsResultWrapper() {
    	return mumpsResultWrapper;
    }
	
	public String getDateIsoFormatted()
	{
		return(DateUtils.getISODateTimeFormatNoT(surveyResultTransfer.getDateOfData()));
	}
}
