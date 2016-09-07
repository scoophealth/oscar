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

import java.util.Date;

//import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;


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
@XmlRootElement(name = "summaryItem")
@XmlSeeAlso(value = { DiagnosisTo1.class })
public class SummaryItemTo1 {
	Integer id;
	Long noteId;
	String type;
	String displayName;
	String action;
	Date date;
	String classification;
	String editor;
	boolean abnormalFlag;
	String indicatorClass;
	String warning;
	private Object extra;

	
	public SummaryItemTo1(){}
	
	public SummaryItemTo1(int id, String displayName,String action,String type){
		this.id = id;
		this.displayName = displayName;
		this.action = action;
		this.type = type;
	}
	
	
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer i){
		id = i;
	}
	
	public Long getNoteId() {
	    return noteId;
	}
	
	public void setNoteId(Long nId) {
	    noteId = nId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public boolean isAbnormalFlag() {
		return abnormalFlag;
	}

	public void setAbnormalFlag(boolean abnormalFlag) {
		this.abnormalFlag = abnormalFlag;
	}
		
	public String isIndicatorClass() {
		return indicatorClass;
	}

	public void setIndicatorClass(String indicatorClass) {
		this.indicatorClass = indicatorClass;
	}
	
	public String getWarning() {
		return warning;
	}

	public void setWarning(String warning) {
		this.warning = warning;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public Object getExtra() {
	    return extra;
    }

	public void setExtra(Object extra) {
	    this.extra = extra;
    }
	
	
}


