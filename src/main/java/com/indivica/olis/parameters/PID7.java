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
 * Date of Birth (Z50 Query only)
 * @author jen
 *
 */
public class PID7 implements Parameter {

	private String value;
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd");
	
	public PID7(String value) {
	    this.value = value;
    }

	public PID7() {
	}

	@Override
	public String toOlisString() {
		return getQueryCode() + "^" + (value != null ? value : "");
	}

	@Override
	public void setValue(Object value) {
		if (value instanceof Date)
			this.value = dateFormatter.format((Date) value);
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
		return "@PID.7";
	}

}
