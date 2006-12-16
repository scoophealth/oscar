package org.oscarehr.survey.web.formbean;

public class PageNavEntry {
	private String pageNumber;
	private String pageName;
	
	public PageNavEntry(String pageNumber, String pageName) {
		this.pageNumber = pageNumber;
		this.pageName = pageName;
	}
	public String getPageName() {
		return pageName;
	}
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	public String getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(String pageNumber) {
		this.pageNumber = pageNumber;
	}
	
}
