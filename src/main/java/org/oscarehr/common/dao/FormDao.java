package org.oscarehr.common.dao;

import org.oscarehr.common.model.Form;
import org.springframework.stereotype.Repository;

@Repository
public class FormDao extends AbstractDao<Form>{

	public FormDao() {
		super(Form.class);
	}
}
