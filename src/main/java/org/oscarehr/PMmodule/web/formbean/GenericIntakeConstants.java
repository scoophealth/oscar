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
	
}
