package org.oscarehr.PMmodule.streethealth;

import java.util.ArrayList;
import java.util.List;

public class DataElement {
	private int id;
	
	List<DataPoint> dataPoints = new ArrayList<DataPoint>();

	public DataElement() {
		
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<DataPoint> getDataPoints() {
		return dataPoints;
	}

	public void setDataPoints(List<DataPoint> dataPoints) {
		this.dataPoints = dataPoints;
	}
	
	
}
