package org.oscarehr.common.dao;

import org.oscarehr.common.model.LogLetters;
import org.springframework.stereotype.Repository;

@Repository
public class LogLettersDao extends AbstractDao<LogLetters>{

	public LogLettersDao() {
		super(LogLetters.class);
	}
}
