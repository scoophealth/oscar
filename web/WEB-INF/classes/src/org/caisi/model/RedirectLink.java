package org.caisi.model;

import org.apache.commons.lang.StringUtils;

public class RedirectLink {

	private int id=0;
	private String url=null;

	private void setId(int id)
	{
		this.id=id;
	}
	
	public int getId() {
		return id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		url=StringUtils.trimToNull(url);
		if (url==null) throw(new IllegalArgumentException("Url can not be null."));
		this.url = url;
	}
}
