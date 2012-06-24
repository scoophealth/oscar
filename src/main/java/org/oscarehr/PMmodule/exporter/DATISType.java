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

package org.oscarehr.PMmodule.exporter;

public enum DATISType {
	TEXT("text"),
	DATETIME("datetime"),
	NUMBER("number"),
	INTEGER("integer"),
	DOUBLE("double"),
	SELECT_NUMERIC("selectnumeric"),
	SELECT_YN("selectyn"),
	SELECT_YNU("selectynu");
	
	private String value;
	
	private DATISType(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	public static boolean isNumeric(DATISType t) {
		if(t == null) {
			return false;
		}
		return t.equals(SELECT_NUMERIC) || t.equals(NUMBER) || t.equals(INTEGER);
	}
	
	public static boolean isDecimal(DATISType t) {
		if(t == null) {
			return false;
		}
		return t.equals(DOUBLE);
	}
}
