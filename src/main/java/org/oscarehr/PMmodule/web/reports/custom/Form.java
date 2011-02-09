package org.oscarehr.PMmodule.web.reports.custom;

import java.util.ArrayList;
import java.util.List;

public class Form {
	private String name;
	private List<Item> items = new ArrayList<Item>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Item> getItems() {
		return items;
	}
	public void setItems(List<Item> items) {
		this.items = items;
	}
	public void addItem(Item i) {
		getItems().add(i);
	}
}
