/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
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
			getQueryCode() + ".4.2^" + (universalId != null ? universalId : "") + "~" +
			getQueryCode() + ".4.3^" + (universalIdType != null ? universalIdType : "") + "~" +
			getQueryCode() + ".5^" + (idTypeCode != null ? idTypeCode : "") + "~" +
			getQueryCode() + ".9.1^" + (assigningJurisdiction != null ? assigningJurisdiction : "") + "~" +
			getQueryCode() + ".9.3^" + (assigningJurisdictionCodingSystem != null ? assigningJurisdictionCodingSystem : "") + "~" +
			"@PID.8^" + (sex != null ? sex : "") + "~" +
			"@PID.7^" + (dateOfBirth != null ? dateOfBirth : "");
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
