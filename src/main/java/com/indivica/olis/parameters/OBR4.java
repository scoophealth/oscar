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
 * Test Request Code
 * @author jen
 *
 */
public class OBR4 implements Parameter {

	private String identifier;
	private String nameOfCodingSystem;
	
	public OBR4(String identifier, String nameOfCodingSystem) {
	    this.identifier = identifier;
	    this.nameOfCodingSystem = nameOfCodingSystem;
    }

	@Override
	public String toOlisString() {
		return getQueryCode() + ".1^" + (identifier != null ? identifier : "") + "~" +
			getQueryCode() + ".3^" + (nameOfCodingSystem != null ? nameOfCodingSystem : "");
	}

	@Override
	public void setValue(Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setValue(Integer part, Object value) {
		if (part == 1)
			this.identifier = (String) value;
		else if (part == 3)
			this.nameOfCodingSystem = (String) value;
	}

	@Override
	public void setValue(Integer part, Integer part2, Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getQueryCode() {
		return "@OBR.4";
	}

}
