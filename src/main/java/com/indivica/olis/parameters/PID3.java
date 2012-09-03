/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

package com.indivica.olis.parameters;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Patient Identifier
 * @author jen
 *
 */
public class PID3 implements Parameter {

	private String idNumber;
	private String universalId;
	private String universalIdType;
	private String idTypeCode;
	private String assigningJurisdiction;
	private String assigningJurisdictionCodingSystem;
	private String sex;
	private String dateOfBirth;
	
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd");
	
	
	public PID3(String idNumber, String universalId, String universalIdType, String idTypeCode, String assigningJurisdiction, String assigningJurisdictionCodingSystem, String sex, String dateOfBirth) {
	    this.idNumber = idNumber;
	    this.universalId = universalId;
	    this.universalIdType = universalIdType;
	    this.idTypeCode = idTypeCode;
	    this.assigningJurisdiction = assigningJurisdiction;
	    this.assigningJurisdictionCodingSystem = assigningJurisdictionCodingSystem;
	    this.sex = sex;
	    this.dateOfBirth = dateOfBirth;
    }

	public PID3() {
    }

	@Override
	public String toOlisString() {
		return getQueryCode() + ".1^" + idNumber + "~" + 
			getQueryCode() + ".4.2" + (universalId != null ? "^"+universalId : "") + "~" +
			getQueryCode() + ".4.3" + (universalIdType != null ? "^"+universalIdType : "") + "~" +
			getQueryCode() + ".5" + (idTypeCode != null ? "^"+idTypeCode : "") + "~" +
			getQueryCode() + ".9.1" + (assigningJurisdiction != null ? "^"+assigningJurisdiction : "") + "~" +
			getQueryCode() + ".9.3" + (assigningJurisdictionCodingSystem != null ? "^"+assigningJurisdictionCodingSystem : "") + "~" +
			"@PID.8" + (sex != null ? "^"+sex : "") + "~" +
			"@PID.7" + (dateOfBirth != null ? "^"+dateOfBirth : "");
	}

	@Override
	public void setValue(Object value) {
		if (value instanceof String)
			idNumber = (String) value;
	}

	@Override
	public void setValue(Integer part, Object value) {
		if (part == 5)
			idTypeCode = (String) value;
		else if (part == 8)
			sex = (String) value;
		else if (part == 7)
			dateOfBirth = dateFormatter.format((Date) value);
	}

	@Override
	public void setValue(Integer part, Integer part2, Object value) {
		if (part == 9 && part2 == 1) {
			assigningJurisdiction = (String) value;
		} else if (part == 9 && part2 == 3) {
			assigningJurisdictionCodingSystem = (String) value;
		} else if (part == 4 && part2 == 2) {
			universalId = (String) value;
		} else if (part == 4 && part2 == 3) {
			universalIdType = (String) value;
		}
	}

	@Override
	public String getQueryCode() {
		return "@PID.3";
	}

}
