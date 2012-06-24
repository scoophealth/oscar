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
package org.oscarehr.util;

import org.apache.commons.lang.StringUtils;

public class HinValidator {
	/**
	 * This method will return false if the hin is clearly invalid. It will return true otherwise. This means that the default is true and anything it can't figure out will return true. As an example if the hinType is null then there's no validation
	 * algorithm so it will return true.
	 */
	public static boolean isValid(String hin, String hinType) {
		if ("on".equals(hinType)) return (isValid_on(hin));
		else return (true);
	}

	private static boolean isValid_on(String hin) {
		if (hin == null) return (false);
		if (hin.length() != 10) return (false);
		if (!StringUtils.isNumeric(hin)) return (false);

		return (true);
	}
}
