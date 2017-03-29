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

import org.apache.struts.util.LabelValueBean;

public class GenericIntakeConstants {

	public static final LabelValueBean EMPTY = new LabelValueBean("", ""); 
	    
	public static final LabelValueBean[] MONTHS = new LabelValueBean[] { 
    	new LabelValueBean("Month", ""),
    	new LabelValueBean("January", "01"), new LabelValueBean("February", "02"),
    	new LabelValueBean("March", "03"), new LabelValueBean("April", "04"),
    	new LabelValueBean("May", "05"), new LabelValueBean("June", "06"),
    	new LabelValueBean("July", "07"), new LabelValueBean("August", "08"),
    	new LabelValueBean("September", "09"), new LabelValueBean("October", "10"),
    	new LabelValueBean("November", "11"), new LabelValueBean("December", "12")
    };
	
	public static final LabelValueBean[] DAYS = new LabelValueBean[] {
    	new LabelValueBean("Day", ""),
    	new LabelValueBean("01", "01"), new LabelValueBean("02", "02"), new LabelValueBean("03", "03"), new LabelValueBean("04", "04"),
    	new LabelValueBean("05", "05"), new LabelValueBean("06", "06"), new LabelValueBean("07", "07"), new LabelValueBean("08", "08"),
    	new LabelValueBean("09", "09"), new LabelValueBean("10", "10"), new LabelValueBean("11", "11"), new LabelValueBean("12", "12"),
    	new LabelValueBean("13", "13"), new LabelValueBean("14", "14"), new LabelValueBean("15", "15"), new LabelValueBean("16", "16"),
    	new LabelValueBean("17", "17"), new LabelValueBean("18", "18"), new LabelValueBean("19", "19"), new LabelValueBean("20", "20"),
    	new LabelValueBean("21", "21"), new LabelValueBean("22", "22"), new LabelValueBean("23", "23"), new LabelValueBean("24", "24"),
    	new LabelValueBean("25", "25"), new LabelValueBean("26", "26"), new LabelValueBean("27", "27"), new LabelValueBean("28", "28"),
    	new LabelValueBean("29", "29"), new LabelValueBean("30", "30"), new LabelValueBean("31", "31")
    };
	
	public static final LabelValueBean[] PROVINCES = new LabelValueBean[] { 
		new LabelValueBean("Ontario", "ON"), new LabelValueBean("Alberta", "AB"),
    	new LabelValueBean("British Columbia", "BC"), new LabelValueBean("Manitoba", "MB"),
    	new LabelValueBean("Newfoundland", "NL"), new LabelValueBean("New Brunswick", "NB"),
    	new LabelValueBean("Yukon", "YT"), new LabelValueBean("Nova Scotia", "NS"),
    	new LabelValueBean("Prince Edward Island", "PE"), new LabelValueBean("Saskatchewan", "SK"),
    	new LabelValueBean("Quebec", "QC"), new LabelValueBean("Nunavut", "NU"),
    	new LabelValueBean("Northwest Territory", "NT")
    };
	
	public static final LabelValueBean[] MARITAL_STATUS = new LabelValueBean[] { 		
		new LabelValueBean("Consumer Declined to answer", "CDA"), new LabelValueBean("Divorced", "20295000"),
    	new LabelValueBean("Married or in common-law relationship", "87915002"), new LabelValueBean("Partner or significant other", "42120006"),
    	new LabelValueBean("Separated", "13184001"), new LabelValueBean("Single", "125681006"),
    	new LabelValueBean("Unknown", "UNK"), new LabelValueBean("Widowed", "33553000")
    	
    };
	
	public static final LabelValueBean[] RECIPIENT_LOCATION = new LabelValueBean[] { 
		
		new LabelValueBean("Select an answer", ""), new LabelValueBean("Algoma District", "010-01"),
    	new LabelValueBean("Brant", "010-02"), new LabelValueBean("Bruce", "010-03"),
    	new LabelValueBean("Chatham-Kent", "010-18"), new LabelValueBean("Cochrane District", "010-04"),
    	new LabelValueBean("Dufferin", "010-05"), new LabelValueBean("Durham", "010-06"),
		new LabelValueBean("Elgin", "010-07"), new LabelValueBean("Essex", "010-08"),
		new LabelValueBean("Frontenac", "010-09"), new LabelValueBean("Grey", "010-10"),
		new LabelValueBean("Haldimand-Norfolk", "010-11"), new LabelValueBean("Haliburton", "010-12"),
		new LabelValueBean("Halton", "010-13"), new LabelValueBean("Hamilton", "010-14"),
		new LabelValueBean("Hastings", "010-15"), new LabelValueBean("Huron", "010-16"),
		new LabelValueBean("Kenora &amp; Kenora P.P.", "010-17"), new LabelValueBean("Lambton", "010-19"),
		new LabelValueBean("Lanark", "010-20"), new LabelValueBean("Leeds &amp; Grenville", "010-21"),
		new LabelValueBean("Lennox &amp; Addington", "010-22"), new LabelValueBean("Manitoulin District", "010-23"),
		new LabelValueBean("Middlesex", "010-24"), new LabelValueBean("Muskoka District Mun", "010-25"),
		new LabelValueBean("Niagara", "010-26"), new LabelValueBean("Nipissing District", "010-27"),
		new LabelValueBean("Northumberland", "010-28"), new LabelValueBean("Ottawa", "010-29"),
		new LabelValueBean("Out of Country", "010-52"), new LabelValueBean("Out Of Province", "010-30"),
		new LabelValueBean("Oxford", "010-31"), new LabelValueBean("Parry Sound District", "010-32"),
		new LabelValueBean("Peel", "010-33"), new LabelValueBean("Perth", "010-34"),
		new LabelValueBean("Peterborough", "010-35"), new LabelValueBean("Prescott &amp; Russell", "010-36"),
		new LabelValueBean("Prince Edward", "010-37"), new LabelValueBean("Rainy River District", "010-38"),
		new LabelValueBean("Renfrew", "010-39"), new LabelValueBean("Simcoe", "010-40"),
		new LabelValueBean("Stormont Dundas &amp; Glengarry", "010-41"), new LabelValueBean("Sudbury District", "010-42"),
		new LabelValueBean("Sudbury Region", "010-43"), new LabelValueBean("Thunder Bay District", "010-44"),
		new LabelValueBean("Timiskaming District", "010-45"), new LabelValueBean("Toronto", "010-46"),
		new LabelValueBean("Unknown", "UNK"), new LabelValueBean("Victoria", "010-48"),
		new LabelValueBean("Waterloo", "010-49"), new LabelValueBean("Wellington", "010-50"),
		new LabelValueBean("York", "010-51")	
		
    };
	
	public static final LabelValueBean[] LHIN_CONSUMER_RESIDES = new LabelValueBean[] { 		
		new LabelValueBean("Select an answer", ""), new LabelValueBean("Central", "8"),
		new LabelValueBean("Central East", "9"), new LabelValueBean("Central West", "5"),
		new LabelValueBean("Champlain", "11"), new LabelValueBean("Erie St.Clair", "1"),
		new LabelValueBean("Hamilton Niagara Haldimand Brant", "4"), new LabelValueBean("Mississauga-Halton", "6"),
		new LabelValueBean("North East", "13"), new LabelValueBean("North Simcoe Muskoka", "12"),
		new LabelValueBean("North West", "14"), new LabelValueBean("Out of Country", "010-52"),
		new LabelValueBean("Out Of Province", "010-30"), new LabelValueBean("South East", "10"),
		new LabelValueBean("South West", "2"), new LabelValueBean("Toronto Centra", "7"),
		new LabelValueBean("Unknown", "UNK"), new LabelValueBean("Waterloo Wellington", "3")	
		
    };
}
