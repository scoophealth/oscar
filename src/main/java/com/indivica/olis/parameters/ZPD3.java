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
 * Patient consent block-all indicator
 * @author jen
 *
 */
public class ZPD3 implements Parameter {

	private String value;
	
	public ZPD3(String value) {
	    this.value = value;
    }

	@Override
	public String toOlisString() {
		return getQueryCode() + "^" + value;
	}

	@Override
	public void setValue(Object value) {
		if (value instanceof String)
			this.value = (String) value;
	}

	@Override
	public void setValue(Integer part, Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setValue(Integer part, Integer part2, Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getQueryCode() {
		return "@ZPD.3";
	}

}
