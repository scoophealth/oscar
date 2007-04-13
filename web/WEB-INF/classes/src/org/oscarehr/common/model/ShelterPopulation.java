package org.oscarehr.common.model;

public class ShelterPopulation {

	private int pastYear;
	private int current;

	public ShelterPopulation(int pastYear, int current) {
		this.pastYear = pastYear;
		this.current = current;
	}
	
	public int getPastYear() {
		return pastYear;
	}

	public int getCurrent() {
		return current;
	}

}
