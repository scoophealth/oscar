package org.oscarehr.web.report;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang.StringEscapeUtils;

public class SymptomChecklistCompareDisplayObject {
	
	private String patientName;
	private ArrayList<MumpsSurveyResultsDisplayObject> resultsToCompare=new ArrayList<MumpsSurveyResultsDisplayObject>();
	private HashMap<String,String> questionsList=new HashMap<String,String>();
	private int lastQuestion=0;
	
	public String getPatientName() {
    	return patientName;
    }

	public void setPatientName(String patientName) {
    	this.patientName = patientName;
    }

	public String getPatientNameHtmlEscaped() {
    	return(StringEscapeUtils.escapeHtml(getPatientName()));
    }

	public ArrayList<MumpsSurveyResultsDisplayObject> getResultsToCompare() {
    	return resultsToCompare;
    }

	public HashMap<String, String> getQuestionsList() {
    	return questionsList;
	}

	public int getLastQuestion() {
    	return lastQuestion;
    }

	public void setLastQuestion(int lastQuestion) {
    	this.lastQuestion = lastQuestion;
    }
	
	
}
