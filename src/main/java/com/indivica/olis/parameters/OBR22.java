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
import java.util.List;

/**
 * Start Timestamp (and End Timestamp)
 * @author jen
 *
 */
public class OBR22 implements Parameter {

	private String value;
	
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMddHHmmssZZZZZ");
	
	public OBR22(String value) {
	    this.value = value;
    }

	public OBR22() {
		//
	}

	@Override
	public void setValue(Object value) {
		if (value != null) {
			if (value instanceof Date) {
				this.value = dateFormatter.format(value);
			} else if (value instanceof List) {
				this.value = dateFormatter.format(((List<Date>) value).get(0));
				this.value += "&" + dateFormatter.format(((List<Date>) value).get(1));
			}
		}
	}

	@Override
	public void setValue(Integer part, Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
    public String toOlisString() {
	    return getQueryCode() + "^" + value;
    }

	@Override
    public String getQueryCode() {
	    return "@OBR.22";
    }

	@Override
    public void setValue(Integer part, Integer part2, Object value) {
		throw new UnsupportedOperationException();
    }

}
