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
package org.oscarehr.ws.rest.to.model;

import java.util.ArrayList;
import java.util.List;


//import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
title: "Ongoing Concerns",displaySize: 5, 
			itemlist: [
	              			{
	              				id : 0,
	              				desc : 'INR CLINIC, A FIB/AV (bovine) replacement, TARGET 2.5 Osteopenia'
	              			},
	              			{
	              				id : 1,
	              				desc : 'Rt Knee - Dr. Who ?OA; Lt Shoulder pain incisional hernia since laparoscopy April 03 Spinal stenosis 1999 - no gurantee for surgery; sciatic pain both legs esp since fall in Feb 06'
	              			},
	              			{
	              				id : 2,
	              				desc : 'Hearing loss R worse than L - tried hearing aid'
	              			},
	              			{
	              				id : 3,
	              				desc : 'Aortic stenosis 2nd to rheumatic fever; AV replaced Jan 08 Large irreducible hiatus Hernia 11/06 HTN on Accupril & HCTZ AF; Recurrent thromboembolism of lower legs with filter insertion - on coumadin'
	              			} ]
						};
*/



@XmlRootElement(name = "summary")
public class SummaryTo1 {
	
	//summary codes
	public static final String ASSESSMENTS_CODE = "assessments";
	public static final String DECISIONSUPPORT_CODE = "dssupport"; 
	public static final String FAMILYHISTORY_CODE = "famhx";
	public static final String INCOMING_CODE = "incoming";
	public static final String MEDICALHISTORY_CODE = "medhx"; 
	public static final String MEDICATIONS_CODE = "meds";  
	public static final String ONGOINGCONCERNS_CODE = "ongoingconcerns";
	public static final String REMINDERS_CODE = "reminders";
	public static final String SOCIALFAMILYHISTORY_CODE = "socfamhx";
	public static final String SOCIALHISTORY_CODE = "sochx";
	public static final String OTHERMEDS_CODE = "othermeds";
	public static final String ALLERGIES = "allergies";
	public static final String RISK_FACTORS = "riskfactors";
	public static final String PREVENTIONS = "preventions";
	public static final String DISEASES = "diseases";

	Integer id;
	String displayName;
	String summaryCode;
	String displaySize = "5";
	List<SummaryItemTo1> summaryItem = new ArrayList<SummaryItemTo1>();
	
	public SummaryTo1(){
		
	}
	
	public SummaryTo1(String str,Integer id,String summaryCode){
		this.id = id;
		this.displayName = str;
		this.summaryCode = summaryCode;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplaySize() {
		return displaySize;
	}

	public void setDisplaySize(String displaySize) {
		this.displaySize = displaySize;
	}

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer i){
		id = i;
	}
	
	public List<SummaryItemTo1> getSummaryItem() {
		return summaryItem;
	}

	public void setSummaryItem(List<SummaryItemTo1> summaryItem) {
		this.summaryItem = summaryItem;
	}

	public String getSummaryCode() {
		return summaryCode;
	}

	public void setSummaryCode(String summaryCode) {
		this.summaryCode = summaryCode;
	}
	
	@Override
	public String toString() {
		return (ReflectionToStringBuilder.toString(this));
	}
	
	
}
