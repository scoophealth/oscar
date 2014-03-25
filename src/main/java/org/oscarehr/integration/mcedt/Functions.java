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
package org.oscarehr.integration.mcedt;

import static org.oscarehr.integration.mcedt.ActionUtils.getUpdateList;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.datatype.XMLGregorianCalendar;

import ca.ontario.health.edt.UpdateRequest;

/**
 * JSTL functions for display of certain JAXB-generated fields
 *
 */
public class Functions {

	public static Date toDate(XMLGregorianCalendar calendar) {
		if (calendar == null) {
			return null;
		}
		return calendar.toGregorianCalendar().getTime();
	}

	public static int toInt(BigInteger i) {
		if (i == null) {
			return 0;
		}
		return i.intValue();
	}
	
	public static boolean isUpdateSelected(HttpServletRequest request, BigInteger id) {
		List<UpdateRequest> updates = getUpdateList(request);
		for(UpdateRequest r : updates) {
			if (r.getResourceID().equals(id)) {
				return true;
			}
		}
		return false;
	}
}
