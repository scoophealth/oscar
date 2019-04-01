package org.oscarehr.ws.rest.to.model;
/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
import java.util.Date;



/*
Data structure to capture this prevention search criteria 

eg:

FLU prevention by Jan 31st
 	name: FLU  asOfDate:Jan 31st cutoffTimefromAsOfDate: 5 

PAP prevention within 42 Months prior to March 31st 
	name: PAP  asOfDate:Mar 31st cutoffTimefromAsOfDate: 42
	
MAM prevention within 30 months prior to march 31st 
	name: MAM  asOfDate:Mar 31st cutoffTimefromAsOfDate: 30
	
FOBT prevention with 30 months prior to March 31st
	name: FOBT  asOfDate:Mar 31st cutoffTimefromAsOfDate: 30
 

Child Immunizations
All ministry supplied imms by the age of 30 months 
	


*/
public class PreventionSearchConfigTo1 {
	public final static int ASOFDATE = 1;
	public final static int BYAGE = 2;
	
	private String name;
	private int dateCalcType = 0;
	private Date asOfDate;
	private int cutoffTimefromAsOfDate;
	private String cuttoffTimeType = "M";
	private int byAge;
	private String byAgeTimeType = "M";
	private int howManyPreventions = 1;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getAsOfDate() {
		return asOfDate;
	}
	public void setAsOfDate(Date asOfDate) {
		this.asOfDate = asOfDate;
	}
	public int getCutoffTimefromAsOfDate() {
		return cutoffTimefromAsOfDate;
	}
	public void setCutoffTimefromAsOfDate(int cutoffTimefromAsOfDate) {
		this.cutoffTimefromAsOfDate = cutoffTimefromAsOfDate;
	}
	public String getCuttoffTimeType() {
		return cuttoffTimeType;
	}
	public void setCuttoffTimeType(String cuttoffTimeType) {
		this.cuttoffTimeType = cuttoffTimeType;
	}
	public int getByAge() {
		return byAge;
	}
	public void setByAge(int byAge) {
		this.byAge = byAge;
	}
	public int getDateCalcType() {
		return dateCalcType;
	}
	public void setDateCalcType(int dateCalcType) {
		this.dateCalcType = dateCalcType;
	}
	public String getByAgeTimeType() {
		return byAgeTimeType;
	}
	public void setByAgeTimeType(String byAgeTimeType) {
		this.byAgeTimeType = byAgeTimeType;
	}
	public int getHowManyPreventions() {
		return howManyPreventions;
	}
	public void setHowManyPreventions(int howManyPreventions) {
		this.howManyPreventions = howManyPreventions;
	}
	
	
	
}
