package org.oscarehr.common.model;

import java.text.NumberFormat;

import org.apache.commons.lang.math.Fraction;

public class PopulationReportStatistic {

	private Fraction fraction;

	public PopulationReportStatistic(int count, int size) {
		this.fraction = Fraction.getFraction(count, size);
	}

	public int getCount() {
		return fraction.getNumerator();
	}
	
	public String getPercent() {
		NumberFormat percentInstance = NumberFormat.getPercentInstance();
		percentInstance.setMinimumFractionDigits(0);
		percentInstance.setMaximumFractionDigits(2);
		
		return percentInstance.format(fraction.floatValue());
	}

}
