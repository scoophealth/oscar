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
package org.oscarehr.billing.CA.filters;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;



public class CodeFilter {
	
	String CODE = null; 
	String Desc = null; 
	String Price =null ;
	Boolean FHOoutofbasket =null;
	Boolean time = null;
	Boolean office =null;
	Boolean home = null;
	Boolean ltc =null;
	Boolean hosp = null;
	Boolean phone = null;
	Boolean groupvisit =null;
	Boolean Previous = null;
	Boolean BillWith =null; 
	String[] Dx =null;
	String Max =null;
	Boolean  Female =null;
	Boolean Commonprocedure =null;
	Boolean Rareprocedure =null;
	Boolean lessThan6w = null;
	Boolean a6wto12m = null;
	Boolean  a12to24m = null;
	Boolean  a2to10y = null;
	Boolean  a10to15y =null;
	Boolean  a16to17y = null;
	Boolean  a18to20y =null;
	Boolean  a21to49 = null;
	Boolean  a50to64 = null;
	Boolean  a65to70 = null;
	Boolean  a71to74 = null;
	Boolean  a75older = null;
	Boolean  Mtof7to5 =null;
	Boolean  MtoF = null;
	Boolean  MtoF5tomidnight = null;
	Boolean  SandS = null;
	Boolean  Nights = null;
	
	
	public CodeFilter(){}
	
	public CodeFilter(String CODE, 
	String Desc ,
	String Price ,
	Boolean FHOoutofbasket,
	Boolean time,
	Boolean office,
	Boolean home,
	Boolean ltc,
	Boolean hosp,
	Boolean phone ,
	Boolean groupvisit,
	Boolean Previous,
	Boolean BillWith,
	String[] Dx,
	String Max,
	Boolean  Female,
	Boolean Commonprocedure,
	Boolean Rareprocedure,
	Boolean lessThan6w,
	Boolean a6wto12m,
	Boolean  a12to24m,
	Boolean  a2to10y,
	Boolean  a10to15y,
	Boolean  a16to17y,
	Boolean  a18to20y,
	Boolean  a21to49,
	Boolean  a50to64,
	Boolean  a65to70,
	Boolean  a71to74,
	Boolean  a75older,
	Boolean  Mtof7to5,
	Boolean  MtoF,
	Boolean  MtoF5tomidnight,
	Boolean  SandS,
	Boolean  Nights){
		this.CODE = CODE;
				this.Desc = Desc;
				this.Price = Price;
				this.FHOoutofbasket = FHOoutofbasket;
				this.time = time;
				this.office = office;
				this.home = home;
				this.ltc = ltc;
				this.hosp = hosp;
				this.phone  = phone;
				this.groupvisit = groupvisit;
				this.Previous = Previous;
				this.BillWith = BillWith;
				this.Dx = Dx;
				this.Max = Max;
				this.Female = Female;
				this.Commonprocedure = Commonprocedure;
				this.Rareprocedure = Rareprocedure;
				this.lessThan6w = lessThan6w;
				this.a6wto12m = a6wto12m;
				this.a12to24m = a12to24m;
				this.a2to10y = a2to10y;
				this.a10to15y = a10to15y;
				this.a16to17y = a16to17y;
				this.a18to20y = a18to20y;
				this.a21to49 = a21to49;
				this.a50to64 = a50to64;
				this.a65to70 = a65to70;
				this.a71to74 = a71to74;
				this.a75older = a75older;
				this.Mtof7to5 = Mtof7to5;
				this.MtoF = MtoF;
				this.MtoF5tomidnight = MtoF5tomidnight;
				this.SandS = SandS;
				this.Nights =   Nights;
		
	}
	
	//FHOOutofBasket	
	//Time	
	//Location //Office	Home	LTC	Hosp	Phone	
	//Group Visit	
	//Previous	
	//Bill With	
	//Dx	
	//Max	
	//M/F	
	//C common procedures
	//R rare procedures
	//isLess than <6w	6
	//6w-12mo	
	//17-24mo	
	//2-10y	
	//10-15y	
	//16-17y	
	//18-20y	
	//21-49	
	//50-64	
	//65-70y	
	//71-74	
	//>75	
	//M-F 7-5	
	//M-F	
	//M-F 5-midnight	
	//S+S	
	//Nights
	
	String location;
	boolean procedure; 
	
	
	@Override
    public String toString(){
		return(ReflectionToStringBuilder.toString(this));
	}
	
}
