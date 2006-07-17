package org.caisi.PMmodule.dao;

import org.caisi.PMmodule.model.Formintakec;

public interface IntakeCDao {
	public Formintakec getCurrentForm(String demographicNo);

	public Formintakec getCurrentForm(String firstName, String lastName);
	
    public Formintakec getForm(Long id);

   public void saveForm(Formintakec form);

}
