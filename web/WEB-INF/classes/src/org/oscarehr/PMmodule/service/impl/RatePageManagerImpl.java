package org.oscarehr.PMmodule.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.oscarehr.PMmodule.dao.RatePageDao;
import org.oscarehr.PMmodule.model.RatePage;
import org.oscarehr.PMmodule.service.RatePageManager;
import org.oscarehr.PMmodule.web.RatingView;

public class RatePageManagerImpl implements RatePageManager {
	private RatePageDao rateDao;

	public void setRatePageDao(RatePageDao rateDao) {
		this.rateDao = rateDao;
	}

	public void rate(String pageName, int score) {
		rateDao.rate(pageName, score);
	}
	
	public List  allStatistic() {
		List rs = rateDao.getAllRecord();
		RatePage rp;
		List al=new ArrayList();
		int i=0;
		RatingView rv=null;
		while (i<rs.size()) {
			rp=(RatePage) rs.get(i);
			int v =rp.getVisitors().intValue();
			double avs=rp.getScore().intValue()/v;
			String name=rp.getPageName().substring(0, rp.getPageName().indexOf("."));
			rv=new RatingView();
			rv.setPageName(name);
			rv.setAvrgScore(Double.toString(avs));
			rv.setVstNumber(Integer.toString(v));
			
			al.add(i, rv);
			i++;
		}
		return al;
	}
}