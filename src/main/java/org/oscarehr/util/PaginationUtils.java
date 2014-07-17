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

package org.oscarehr.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.Action;
import org.oscarehr.common.PaginationQuery;

public class PaginationUtils extends Action {
	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final int LIMIT = 10;

	public Date parseDate(String dateValue, String strFormat) {
		if (dateValue == null) return null;
		SimpleDateFormat dateFormat = new SimpleDateFormat(strFormat);
		try {
			return dateFormat.parse(dateValue);
		} catch (ParseException pe) {
			return null;
		}
	}

	public int parseInt(String value) {
		if (StringUtils.isBlank(value)) {
			return 0;
		} else {
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException e) {
				return 0;
			}
		}
	}

	/**
	 * Load pagination request parameter for jQuery datatable plugin
	 * @param request
	 * @param query
	 */
	public void loadPaginationQuery(HttpServletRequest request, PaginationQuery query) {
		query.setKeyword(request.getParameter("sSearch"));
		int start = this.parseInt(request.getParameter("iDisplayStart"));
		query.setStart(start);
		int limit = this.parseInt(request.getParameter("iDisplayLength"));
		query.setLimit(limit == 0 ? LIMIT : limit);
		String startDate = request.getParameter("startDate");
		if (!StringUtils.isEmpty(startDate)) {
			query.setStartDate(parseDate(startDate + " 00:00:00", DATE_FORMAT));
		}
		String endDate = request.getParameter("endDate");
		if (!StringUtils.isEmpty(endDate)) {
			query.setEndDate(parseDate(endDate + " 23:59:59", DATE_FORMAT));
		}

		String sortCol = request.getParameter("iSortCol_0");
		query.setOrderby(request.getParameter("mDataProp_" + sortCol));
		query.setSort(request.getParameter("sSortDir_0"));
	}
}
