package org.oscarehr.common.dao;

import org.oscarehr.common.model.SpecialistsJavascript;
import org.springframework.stereotype.Repository;

@Repository
public class SpecialistsJavascriptDao extends AbstractDao<SpecialistsJavascript>{

	public SpecialistsJavascriptDao() {
		super(SpecialistsJavascript.class);
	}
}
