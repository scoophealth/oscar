package org.oscarehr.PMmodule.service;

import java.util.Date;
import java.util.List;

import org.oscarehr.PMmodule.model.Formintakec;

public interface IntakeCManager  extends IntakeManager{
	Formintakec getCurrentForm(String demographicNo);
	public void saveNewIntake(Formintakec form);
	public List getCohort(Date BeginDate, Date EndDate);
}
