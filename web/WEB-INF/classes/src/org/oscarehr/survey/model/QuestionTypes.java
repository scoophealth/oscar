package org.oscarehr.survey.model;

import java.util.ArrayList;

import org.apache.struts.util.LabelValueBean;

public class QuestionTypes extends ArrayList {

	public static final String RANK = "Rank";
	public static final String DATE = "Date";
	public static final String SELECT = "Select";
	public static final String SCALE = "Rank";
	public static final String OPEN_ENDED = "Open Ended";
	
    public QuestionTypes() {
    	//this.add(new LabelValueBean(RANK, RANK));
    	this.add(new LabelValueBean(DATE, DATE));
    	this.add(new LabelValueBean(SELECT, SELECT));
    	//this.add(new LabelValueBean(SCALE, SCALE));
    	this.add(new LabelValueBean(OPEN_ENDED, OPEN_ENDED));
    }
    
}
