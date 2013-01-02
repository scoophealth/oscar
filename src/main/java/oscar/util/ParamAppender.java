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
package oscar.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Query;

public class ParamAppender extends QueryAppender {

	private Map<String, Object> params = new HashMap<String, Object>();

	public ParamAppender() {
		super();
	}

	public ParamAppender(String baseQuery) {
		super(baseQuery);
	}

	public void or(String clause, String paramName, Object paramValue) {
		if (paramValue == null) {
			return;
		}
		or(clause);
		addParam(paramName, paramValue);
	}

	public void and(String clause, String paramName, Object paramValue) {
		if (paramValue == null) {
			return;
		}
		and(clause);
		addParam(paramName, paramValue);
	}

	public Object addParam(String paramName, Object paramValue) {
		return getParams().put(paramName, paramValue);
	}

	public Map<String, Object> getParams() {
		return params;
	}

	protected void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public Query setParams(Query query) {
		for (Entry<String, Object> param : getParams().entrySet()) {
			query.setParameter(param.getKey(), param.getValue());
		}
		return query;
	}
	
	public void and(ParamAppender appender) {
		super.and(appender);
		mergeParams(appender);
	}
	
	public void or(ParamAppender appender) {
		super.or(appender);
		mergeParams(appender);
	}
	
	/**
	 * Adds all parameters from the specified appended to this appender
	 * 
	 * @param paramAppender
	 * 		Parameter appender to merge parameters from
	 * @return
	 * 		Returns this instance.
	 */
	public ParamAppender mergeParams(ParamAppender paramAppender) {
		getParams().putAll(paramAppender.getParams());
		return this;
	}
}
