/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.common.model;

import java.text.NumberFormat;

import org.apache.commons.lang.math.Fraction;

public class ReportStatistic implements Comparable<ReportStatistic> {

	private String label;
	private Fraction fraction;

	public ReportStatistic(int count, int size) {
		fraction = Fraction.getFraction(count, size);
	}

	public String getLabel() {
		return label;
	}

	public int getCount() {
		return fraction.getNumerator();
	}
	
	public int getSize() {
		return fraction.getDenominator();
	}
	
	public void setSize(int size)
	{
        fraction = Fraction.getFraction(getCount(), size);
	}

	public String getPercent() {
		NumberFormat percentInstance = NumberFormat.getPercentInstance();
		percentInstance.setMinimumFractionDigits(0);
		percentInstance.setMaximumFractionDigits(2);

		return percentInstance.format(fraction.floatValue());
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int compareTo(ReportStatistic rhs) {
		int compareTo = 0;
		
		if (label != null) {
			compareTo = label.compareTo(rhs.label);
		}
		
		if (compareTo == 0 && fraction != null) {
			compareTo = fraction.compareTo(rhs.fraction);
		}
		
		return compareTo;
	}
	
}
