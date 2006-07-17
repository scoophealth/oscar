package org.caisi.PMmodule.service;

import java.util.List;


public interface RatePageManager {

	public void rate(String pageName, int score);
	public List  allStatistic();
}
