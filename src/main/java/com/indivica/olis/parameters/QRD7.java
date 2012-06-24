/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

package com.indivica.olis.parameters;

public class QRD7 implements Parameter {

	private Integer queryNum;
	
	public QRD7(Integer queryNum) {
	    this.queryNum = queryNum;
    }

	@Override
	public String toOlisString() {
		return getQueryCode() + ".1^" + queryNum + "~" + getQueryCode() + ".2.1^RD~" + getQueryCode() + ".2.3^HL70126";
	}

	@Override
	public void setValue(Object value) {
		if (value instanceof Integer) {
			queryNum = (Integer) value;
		}
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
		return "@QRD.7";
	}

}
