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
 * Attending Practitioner
 * @author jen
 *
 */
public class PV17 implements Parameter {

	private String idNumber;
	private String idTypeCode;
	private String assigningJurisdiction;
	private String assigningJurisdictionCodingSystem;
	
	public PV17(String idNumber, String idTypeCode, String assigningJurisdiction, String assigningJurisdictionCodingSystem) {
	    this.idNumber = idNumber;
	    this.idTypeCode = idTypeCode;
	    this.assigningJurisdiction = assigningJurisdiction;
	    this.assigningJurisdictionCodingSystem = assigningJurisdictionCodingSystem;
    }

	@Override
    public String toOlisString() {
		return getQueryCode() + ".1^" + (idNumber != null ? idNumber : "") + "~" +
			getQueryCode() + ".13^" + (idTypeCode != null ? idTypeCode : "") + "~" +
			getQueryCode() + ".22.1^" + (assigningJurisdiction != null ? assigningJurisdiction : "") + "~" +
			getQueryCode() + ".22.3^" + (assigningJurisdictionCodingSystem != null ? assigningJurisdictionCodingSystem : "");
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
	    return "@PV1.7";
    }

}
