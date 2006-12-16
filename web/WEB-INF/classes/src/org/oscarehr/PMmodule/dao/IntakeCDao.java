package org.oscarehr.PMmodule.dao;

import java.util.Date;
import java.util.List;

import org.oscarehr.PMmodule.model.Formintakec;

public interface IntakeCDao {
	public Formintakec getCurrentForm(Integer demographicNo);
	
    public Formintakec getForm(Long id);

    public void saveForm(Formintakec form);
	
	public List getCohort(Date BeginDate, Date EndDate, List clients);
}
