package org.oscarehr.survey.web.formbean;

import java.util.HashMap;
import java.util.Map;

public class SurveyExecuteDataBean {
	private Map values = null;

	public SurveyExecuteDataBean() {
		reset();
	}
	
	public void reset() {
		this.values = new HashMap();
	}
	
    public void setValues(Map values) {
        this.values = values;
    }
    
    public Map getValues() {
        return this.values;
    }

    public void setValue(String key, Object value) {
    	getValues().put(key,value);
        /*
    	String[] parsed = key.split("_");
    	String pageNumber = parsed[0];
    	String sectionId = parsed[1];
    	String questionId = parsed[2];
    	
    	System.out.println("setting data value: pageNumber=" + pageNumber + ",sectionId=" + sectionId + ",questionId=" + questionId + "=" + value);
    	*/
    }
    public Object getValue(String key) {
    	return getValues().get(key);
    	/*
    	String[] parsed = key.split("_");
    	String pageNumber = parsed[0];
    	String sectionId = parsed[1];
    	String questionId = parsed[2];
    	
    	System.out.println("getting data value: pageNumber=" + pageNumber + ",sectionId=" + sectionId + ",questionId=" + questionId);
    	  
    	return new String("");
    	*/
    }
}
