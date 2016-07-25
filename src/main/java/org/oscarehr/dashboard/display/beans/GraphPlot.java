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
package org.oscarehr.dashboard.display.beans;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class GraphPlot {

	// default denominator in percent - if not set by the query results. 
	private static final Double DEFAULT_DENOMINATOR = 100.0;
	private static final Double DEFAULT_NUMERATOR = 0.0;
	
	private String label;
	private String key;
	private Double numerator;
	private Double denominator;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Double getNumerator() {
		if( numerator == null ) {
			return DEFAULT_NUMERATOR;
		}
		return numerator;
	}
	
	public void setNumerator(Double numerator) {
		this.numerator = numerator;
	}

	public Double getDenominator() {
		if( denominator == null ) {
			return DEFAULT_DENOMINATOR;
		}
		return denominator;
	}
	
	public void setDenominator(Double denominator) {
		this.denominator = denominator;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
