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
 * Admitting Practitioner
 * @author jen
 *
 */
public class PV117 implements Parameter {

	private String idNumber;
	private String idTypeCode;
	private String assigningJurisdiction;
	private String assigningJurisdictionCodingSystem;
	
	public PV117(String idNumber, String idTypeCode, String assigningJurisdiction, String assigningJurisdictionCodingSystem) {
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
	    return "@PV1.17";
    }

}
