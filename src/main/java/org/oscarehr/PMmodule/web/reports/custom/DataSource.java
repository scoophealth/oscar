package org.oscarehr.PMmodule.web.reports.custom;

import java.util.ArrayList;
import java.util.List;

public class DataSource {
	private String type;
	private List<Form> forms = new ArrayList<Form>();
	private String bean;
	
	
	public String getBean() {
		return bean;
	}
	public void setBean(String bean) {
		this.bean = bean;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<Form> getForms() {
		return forms;
	}
	public void setForms(List<Form> forms) {
		this.forms = forms;
	}
	
	public void addForm(Form form) {
		getForms().add(form);
	}
}
