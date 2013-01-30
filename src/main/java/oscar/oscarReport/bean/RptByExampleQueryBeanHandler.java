/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */

package oscar.oscarReport.bean;

import java.util.Collection;
import java.util.Vector;

import org.oscarehr.common.dao.ReportByExamplesDao;
import org.oscarehr.common.dao.ReportByExamplesFavoriteDao;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.ReportByExamples;
import org.oscarehr.common.model.ReportByExamplesFavorite;
import org.oscarehr.util.SpringUtils;

import oscar.util.ConversionUtils;

@SuppressWarnings("unchecked")
public class RptByExampleQueryBeanHandler {

	Vector favoriteVector = new Vector();
	Vector allQueryVector = new Vector();
	Vector<RptByExampleQueryBean> queryVector = new Vector<RptByExampleQueryBean>();
	String startDate;
	String endDate;

	public RptByExampleQueryBeanHandler() {
	}

	public RptByExampleQueryBeanHandler(String startDate, String endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public RptByExampleQueryBeanHandler(String providerNo) {
		getFavoriteCollection(providerNo);
	}

	public Collection<RptByExampleQueryBean> getFavoriteCollection(String providerNo) {
		ReportByExamplesFavoriteDao dao = SpringUtils.getBean(ReportByExamplesFavoriteDao.class);
		for (ReportByExamplesFavorite f : dao.findByProvider(providerNo)) {
			RptByExampleQueryBean query = new RptByExampleQueryBean(f.getId(), f.getQuery(), f.getName());
			favoriteVector.add(query);
		}
		return favoriteVector;
	}

	public Vector getFavoriteVector() {
		return favoriteVector;
	}

	public Collection<RptByExampleQueryBean> getAllQueryVector() {
		ReportByExamplesDao dao = SpringUtils.getBean(ReportByExamplesDao.class);
		for (Object[] o : dao.findReportsAndProviders()) {
			ReportByExamples r = (ReportByExamples) o[0];
			Provider p = (Provider) o[0];
			RptByExampleQueryBean query = toBean(r, p);
			allQueryVector.add(query);
		}
		return allQueryVector;
	}

	private RptByExampleQueryBean toBean(ReportByExamples r, Provider p) {
		RptByExampleQueryBean query = new RptByExampleQueryBean(p.getLastName(), p.getFirstName(), r.getQuery(), ConversionUtils.toDateString(r.getDate()));
		return query;
	}

	public Vector<RptByExampleQueryBean> getQueryVector() {
		ReportByExamplesDao dao = SpringUtils.getBean(ReportByExamplesDao.class);
		for (Object[] o : dao.findReportsAndProviders(ConversionUtils.fromDateString(startDate), ConversionUtils.fromDateString(endDate))) {
			ReportByExamples r = (ReportByExamples) o[0];
			Provider p = (Provider) o[1];

			RptByExampleQueryBean query = toBean(r, p);
			queryVector.add(query);
		}

		return queryVector;
	}
}
