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

/**
 * Facility class for appending JPQL queries. This appender encapsulates basic
 * structure of JPQL queries.
 */
public class QueryAppender {
	
	private Appender whereBuffer = new Appender();

	private boolean isWhereAppended = false;

	private String baseQuery;
	
	private Appender orderBuffer = new Appender(",");

	/**
	 * Creates a new instance and sets the empty base query
	 */
	public QueryAppender() {
		this("");
	}

	/**
	 * Creates a new instance and sets the specified base query
	 * 
	 * @param baseQuery
	 *            Base query to set
	 */
	public QueryAppender(String baseQuery) {
		setBaseQuery(baseQuery);
	}

	/**
	 * Gets base query for this appender
	 * 
	 * @return Returns the base query
	 */
	public String getBaseQuery() {
		return baseQuery;
	}

	/**
	 * Sets base query for this appender
	 * 
	 * @param baseQuery
	 *            Base query to set
	 */
	public void setBaseQuery(String baseQuery) {
		this.baseQuery = baseQuery;
	}

	/**
	 * Combines current where clause with the specified sub-clause using the
	 * logical OR operator
	 * 
	 * @param clause
	 *            Clause to combine
	 */
	public void or(String clause) {
		addWhere("OR", clause);
	}

	/**
	 * Combines current where clause with the specified sub-clause using the
	 * logical AND operator
	 * 
	 * @param clause
	 *            Clause to combine
	 */
	public void and(String clause) {
		addWhere("AND", clause);
	}
	
	/**
	 * Combines current where clause with the specified sub-clause using the
	 * logical AND operator
	 * 
	 * @param appender
	 * 		Appender to combine
	 */
	public void and(QueryAppender appender) {
		String clause = appender.getWhereClause();
		if (clause.isEmpty()) {
			return;
		}
		and("(" + clause + ")");
	}
	
	/**
	 * Combines current where clause with the specified sub-clause using the
	 * logical OR operator
	 * 
	 * @param appender
	 * 		Appender to combine
	 */
	public void or(QueryAppender appender) {
		String clause = appender.getWhereClause();
		if (clause.isEmpty()) {
			return;
		}
		or("(" + clause + ")");
	}
	
	/**
	 * Adds the specified sub-clause to the current where clause. This method
	 * doesn't insert the proper operator. Use this method only when it's
	 * necessary to add a single where sub-clause.
	 * 
	 * @param clause
	 *            Clause to be added to where clause
	 */
	public void addWhere(String clause) {
		getWhereBuffer().append(clause);
		setWhereAppended(true);
	}

	/**
	 * Adds where clause to the current sub-clause using the operator provided
	 * 
	 * @param operator
	 *            Operator to use for joining the sub-clause with the current
	 *            where clause. It should be either "or" or "and"
	 * @param clause
	 *            Clause to be added to the current where clause
	 */
	public void addWhere(String operator, String clause) {
		if (isWhereAppended()) {
			getWhereBuffer().append(operator);
		}
		getWhereBuffer().append(clause);
		setWhereAppended(true);
	}

	/**
	 * Gets the content of this appended as an JPQL string.
	 * 
	 * @return Returns the JPQL string.
	 */
	public String getQuery() {
		Appender appender = new Appender();
		if (getBaseQuery() != null) {
			appender.append(getBaseQuery());
		}
		
		if (getWhereBuffer().getBuffer().length() != 0) {
			appender.append("WHERE");
			appender.append(getWhereClause());
		}
		
		if (getOrderBuffer().getBuffer().length() != 0) {
			appender.append("ORDER BY");
			appender.append(getOrderBuffer());
		}
		
		return appender.toString();
	}

	private boolean isWhereAppended() {
		return isWhereAppended;
	}

	private void setWhereAppended(boolean isWhereAppended) {
		this.isWhereAppended = isWhereAppended;
	}

	/**
	 * Gets the current where clause
	 * 
	 * @return Returns the where clause
	 */
	public String getWhereClause() {
		return getWhereBuffer().toString();
	}

	private Appender getWhereBuffer() {
		return whereBuffer;
	}

	/**
	 * Checks if this appender contains a where clause appended.
	 * 
	 * @return Returns true if there is a where clause appended and false
	 *         otherwise
	 */
	public boolean isAppended() {
		return getWhereBuffer().length() != 0;
	}

	@Override
	public String toString() {
		return getQuery();
	}

	public void addOrder(String order) {
		getOrderBuffer().append(order);
	}

	protected Appender getOrderBuffer() {
		return orderBuffer;
	}

	protected void setOrderBuffer(Appender orderBuffer) {
		this.orderBuffer = orderBuffer;
	}
}
