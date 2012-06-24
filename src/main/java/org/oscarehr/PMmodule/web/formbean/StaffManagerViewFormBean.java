/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.PMmodule.web.formbean;

import java.util.Comparator;

import org.oscarehr.common.model.Provider;

public class StaffManagerViewFormBean {
	private String tab;
	public static final String[] tabs = {"Summary","Programs","Facilities"};
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
