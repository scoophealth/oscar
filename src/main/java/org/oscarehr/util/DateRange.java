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

import java.io.Serializable;
import java.util.Date;

/**
 * Represents an immutable range of dates. Null {@link #getTo()} or {@link #getFrom()} dates represent open ranges.
 */
public class DateRange implements Serializable {

    private static final long serialVersionUID = 1L;

    private Date from;
	private Date to;

	/**
	 * Creates a new instance of date range.
	 * 
	 * @param from 
	 * 		Date representing start of the date rage
	 * @param to 
	 * 		Date representing end of the date rage
	 * 
	 * @throws IllegalArgumentException 
	 * 		IllegalArgumentException is thrown  in case from occurs after the to date.
	 */
	public DateRange(Date from, Date to) {
		if (from != null && to != null) {
			if (!from.before(to) && !from.equals(to)) { // allow single day range
				throw new IllegalArgumentException("From date must preceed to date");
			}
		}
		setFrom(from);
		setTo(to);
	}

	/**
	 * Checks if the specified date falls into the range represented by this instance.
	 * 
	 * @param date
	 * 		Date to be check
	 * @return
	 * 		Returns true if the date belongs to this range and false otherwise.
	 * 
	 */
	public boolean isInRange(Date date) {
		if (getFrom() != null) {
			if (!getFrom().before(date)) {
				return false;
			}
		}

		if (getTo() != null) {
			if (!getTo().after(date)) {
				return false;
			}
		}

		return true;
	}

	public Date getFrom() {
		return from;
	}

	private void setFrom(Date from) {
		this.from = from;
	}

	public Date getTo() {
		return to;
	}

	private void setTo(Date to) {
		this.to = to;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		DateRange other = (DateRange) obj;
		if (from == null) {
			if (other.from != null) return false;
		} else if (!from.equals(other.from)) return false;
		if (to == null) {
			if (other.to != null) return false;
		} else if (!to.equals(other.to)) return false;
		return true;
	}

}
