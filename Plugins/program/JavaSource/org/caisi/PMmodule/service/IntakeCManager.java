package org.caisi.PMmodule.service;

import org.caisi.PMmodule.model.Formintakec;

public interface IntakeCManager  extends IntakeManager{
	Formintakec getCurrentForm(String demographicNo);
	public void saveNewIntake(Formintakec form);
}
