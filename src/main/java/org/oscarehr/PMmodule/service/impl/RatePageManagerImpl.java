/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.PMmodule.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.oscarehr.PMmodule.dao.RatePageDao;
import org.oscarehr.PMmodule.model.RatePage;
import org.oscarehr.PMmodule.service.RatePageManager;
import org.oscarehr.PMmodule.web.RatingView;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class RatePageManagerImpl implements RatePageManager {
	private RatePageDao rateDao;

	public void setRatePageDao(RatePageDao rateDao) {
		this.rateDao = rateDao;
	}

	public void rate(String pageName, int score) {
		rateDao.rate(pageName, score);
	}

	public List<RatingView>  allStatistic() {
		List<RatePage> rs = rateDao.getAllRecord();
		RatePage rp;
		List<RatingView> al=new ArrayList<RatingView>();
		int i=0;
		RatingView rv=null;
		while (i<rs.size()) {
			rp= rs.get(i);
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
