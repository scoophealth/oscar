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
