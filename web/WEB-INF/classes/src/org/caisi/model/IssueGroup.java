package org.caisi.model;

import org.apache.commons.lang.StringUtils;

public class IssueGroup {

	private int id=0;
	private String name=null;

	private void setId(int id)
	{
		this.id=id;
	}
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		name=StringUtils.trimToNull(name);
		if (name==null) throw(new IllegalArgumentException("Can not be null."));
		this.name = name;
	}
}
