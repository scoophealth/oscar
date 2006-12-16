package org.oscarehr.PMmodule.web.formbean;

import java.util.Comparator;

import org.oscarehr.PMmodule.model.Provider;

public class StaffManagerViewFormBean {
	private String tab;
	public static final String[] tabs = {"Summary","Programs"};
	private String sort;
	
	public StaffManagerViewFormBean() {
		sort = "providerName";
	}
	
	/**
	 * @return Returns the tab.
	 */
	public String getTab() {
		return tab;
	}
	/**
	 * @param tab The tab to set.
	 */
	public void setTab(String tab) {
		this.tab = tab;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	
	public static Comparator getProviderComparator() {
		return new Comparator() {
			public int compare(Object o1, Object o2) {
				Provider provider1  = (Provider)o1;
				Provider provider2  = (Provider)o2;
				
				return provider1.getFormattedName().compareTo(provider2.getFormattedName());				
			}
		};			
	}
	
	/*
	public static Comparator getRoleComparator() {
		return new Comparator() {
			public int compare(Object o1, Object o2) {
				Provider provider1  = (Provider)o1;
				Provider provider2  = (Provider)o2;
				
				return provider1.g.compareTo(provider2.getFormattedName());				
			}
		};
	}*/	
	
}
