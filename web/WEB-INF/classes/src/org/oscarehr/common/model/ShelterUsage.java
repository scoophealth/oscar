package org.oscarehr.common.model;

public class ShelterUsage {

	private int low;
	private int medium;
	private int high;

	public ShelterUsage(int low, int medium, int high) {
		this.low = low;
		this.medium = medium;
		this.high = high;
	}

	public int getLow() {
    	return low;
    }

	public int getMedium() {
    	return medium;
    }

	public int getHigh() {
    	return high;
    }
	
}