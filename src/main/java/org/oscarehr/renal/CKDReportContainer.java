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
package org.oscarehr.renal;

import javax.xml.bind.annotation.XmlRootElement;

import org.oscarehr.common.model.Demographic;

@XmlRootElement(name = "CkdScreeningReportItem")
public class CKDReportContainer {
	private Demographic demographic;
	private String aboriginalStr;
	private boolean diabetic;
	private boolean hypertensive;
	private boolean medication;
	private boolean bp;
	private boolean hx;
	private boolean labs;
	
	private String lastPatientLetter;
	private String lastLabReq;
	
	private String lastVisit = "";
	
	
	public Demographic getDemographic() {
		return demographic;
	}
	public void setDemographic(Demographic demographic) {
		this.demographic = demographic;
	}
	public String getAboriginalStr() {
		return aboriginalStr;
	}
	public void setAboriginalStr(String aboriginalStr) {
		this.aboriginalStr = aboriginalStr;
	}
	public boolean isDiabetic() {
		return diabetic;
	}
	public void setDiabetic(boolean diabetic) {
		this.diabetic = diabetic;
	}
	public boolean isHypertensive() {
		return hypertensive;
	}
	public void setHypertensive(boolean hypertensive) {
		this.hypertensive = hypertensive;
	}
	public boolean isBp() {
		return bp;
	}
	public void setBp(boolean bp) {
		this.bp = bp;
	}
	public boolean isHx() {
		return hx;
	}
	public void setHx(boolean hx) {
		this.hx = hx;
	}
	public boolean isLabs() {
		return labs;
	}
	public void setLabs(boolean labs) {
		this.labs = labs;
	}
	public boolean isMedication() {
		return medication;
	}
	public void setMedication(boolean medication) {
		this.medication = medication;
	}
	public String getLastPatientLetter() {
		return lastPatientLetter;
	}
	public void setLastPatientLetter(String lastPatientLetter) {
		this.lastPatientLetter = lastPatientLetter;
	}
	public String getLastLabReq() {
		return lastLabReq;
	}
	public void setLastLabReq(String lastLabReq) {
		this.lastLabReq = lastLabReq;
	}
	public String getLastVisit() {
		return lastVisit;
	}
	public void setLastVisit(String lastVisit) {
		this.lastVisit = lastVisit;
	}
	
	
	
}
