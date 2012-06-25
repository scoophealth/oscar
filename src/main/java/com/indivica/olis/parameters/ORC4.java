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
 * Placer Group Number
 * @author jen
 *
 */
public class ORC4 implements Parameter {

	private String entityIdentifier;
	private String universalId;
	private String universalIdType;
	
	public ORC4(String entityIdentifier, String universalId, String universalIdType) {
	    this.entityIdentifier = entityIdentifier;
	    this.universalId = universalId;
	    this.universalIdType = universalIdType;
    }

	public ORC4() {
    }

	@Override
	public String toOlisString() {
		return getQueryCode() + ".1^" + (entityIdentifier != null ? entityIdentifier : "") + "~" +
			getQueryCode() + ".3^" + (universalId != null ? universalId : "") + "~" +
			getQueryCode() + ".4^" + (universalIdType != null ? universalIdType : "");
	}

	@Override
	public void setValue(Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setValue(Integer part, Object value) {
		if (part == 1)
			this.entityIdentifier = (String) value;
		else if (part == 3)
			this.universalId = (String) value;
		else if (part == 4)
			this.universalIdType = (String) value;
	}

	@Override
	public void setValue(Integer part, Integer part2, Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getQueryCode() {
		return "@ORC.4";
	}

}
