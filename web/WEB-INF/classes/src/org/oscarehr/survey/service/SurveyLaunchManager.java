package org.oscarehr.survey.service;

import org.oscarehr.survey.model.Survey;

public interface SurveyLaunchManager {
	public long launch(Survey survey);
	public void close(long instanceId);
	public void reopen(long instanceId);
}
