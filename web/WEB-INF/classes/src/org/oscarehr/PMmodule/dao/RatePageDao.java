package org.oscarehr.PMmodule.dao;

import java.util.List;

public interface RatePageDao {

	public void rate(String pageName, int score);
	public List getScoreByName(String pageName);
	public List getAllRecord();
}