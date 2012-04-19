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


package com.indivica.olis.parameters;

/**
 * Requesting HIC
 * @author jen
 *
 */
public class ZRP1 implements Parameter {

	private String idNumber;
	private String idTypeCode;
	private String assigningJurisdiction;
	private String assigningJurisdictionCodingSystem;
	private String name;
	private String firstName;
	private String secondName;
	
	public ZRP1(String idNumber, String idTypeCode, String assigningJurisdiction, String assigningJurisdictionCodingSystem, String name, String firstName, String secondName) {
	    this.idNumber = idNumber;
	    this.idTypeCode = idTypeCode;
	    this.assigningJurisdiction = assigningJurisdiction;
	    this.assigningJurisdictionCodingSystem = assigningJurisdictionCodingSystem;
	    this.name = name;
	    this.firstName = firstName;
	    this.secondName = secondName;
    }

	public ZRP1() {
    }

	@Override
	public String toOlisString() {
		return getQueryCode() + ".1^" + idNumber + "~" + getQueryCode() + ".13^" + idTypeCode + "~" +
			getQueryCode() + ".22.1^" + (assigningJurisdiction != null ? assigningJurisdiction : "") + "~" +
			getQueryCode() + ".22.3^" + (assigningJurisdictionCodingSystem != null ? assigningJurisdictionCodingSystem : "") + "~" +
			getQueryCode() + ".2^" + (name != null ? name : "") + "~" +
			getQueryCode() + ".3^" + (firstName != null ? firstName : "") + "~" +
			getQueryCode() + ".4^" + (secondName != null ? secondName : "");
	}

	@Override
	public void setValue(Object value) {
		if (value instanceof String)
			idNumber = (String) value;
	}

	@Override
	public void setValue(Integer part, Object value) {
		if (part == 13) {
			idTypeCode = (String) value;
		} else if (part == 2) {
			name = (String) value;
		} else if (part == 3) {
			firstName = (String) value;
		} else if (part == 4) {
			secondName = (String) value;
		}
	}

	@Override
	public void setValue(Integer part, Integer part2, Object value) {
		if (part == 22 && part2 == 1) {
			assigningJurisdiction = (String) value;
		} else if (part == 22 && part2 == 3) {
			assigningJurisdictionCodingSystem = (String) value;
		}
	}

	@Override
	public String getQueryCode() {
		return "@ZRP.1";
	}

}
