package org.oscarehr.PMmodule.streethealth;

import java.util.ArrayList;
import java.util.List;

public class DataPoint {
	private String category;
	
	private List<Integer> data = new ArrayList<Integer>();
	
	public DataPoint() {
		
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public List<Integer> getData() {
		return data;
	}

	public void setData(List<Integer> data) {
		this.data = data;
	}
	
	
}
