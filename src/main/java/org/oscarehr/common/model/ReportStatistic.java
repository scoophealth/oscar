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
