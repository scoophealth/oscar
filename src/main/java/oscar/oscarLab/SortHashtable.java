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

package oscar.oscarLab;

import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

/**
 * Used to sort a list of Lab hashtable or map objects 
 * 
 * @author jay
 */
@SuppressWarnings("rawtypes")
public class SortHashtable implements Comparator {

	private static final String COLL_DATE_DATE = "collDateDate";

	public SortHashtable() {
	}

	public int compare(Object object, Object object0) {
		int ret = 0;

		Date date1 = getDate(object);
		Date date2 = getDate(object0);
		if (date1.after(date2)) {
			ret = -1;
		} else if (date1.before(date2)) {
			ret = 1;
		}
		return ret;
	}

    private Date getDate(Object object) {
		Object result = null;
		if (Map.class.isAssignableFrom(object.getClass())) {
			result = ((Map) object).get(COLL_DATE_DATE);
		} else if (Hashtable.class.isAssignableFrom(object.getClass())) {
			result = ((Hashtable) object).get(COLL_DATE_DATE);
		} else {
			throw new IllegalArgumentException("Unsupported map type " + object.getClass().getName());
		}
		
		if (result == null) {
			return null;
		}
		
		if (result instanceof Date) {
			return (Date) result;
		}
		
		throw new IllegalArgumentException("Invalid value type for value \"" + COLL_DATE_DATE + "\". Expected " 
				+ Date.class.getName() + " but got " + result.getClass().getName() + " ( with value " + result + ")");
    }
}
