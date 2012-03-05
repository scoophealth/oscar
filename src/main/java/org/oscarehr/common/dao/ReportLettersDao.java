package org.oscarehr.common.dao;

import org.oscarehr.common.model.ReportLetters;
import org.springframework.stereotype.Repository;

@Repository
public class ReportLettersDao extends AbstractDao<ReportLetters>{

	public ReportLettersDao() {
		super(ReportLetters.class);
	}

}
