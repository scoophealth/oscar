/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
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
			getQueryCode() + ".3" + (firstName != null ? "^"+firstName : "") + "~" +
			getQueryCode() + ".4" + (secondName != null ? "^"+secondName : "");
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
