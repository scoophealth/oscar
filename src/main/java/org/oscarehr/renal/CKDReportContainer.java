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

import org.oscarehr.common.model.Demographic;

public class CKDReportContainer {
	private Demographic demographic;
	private String aboriginalStr;
	private boolean diabetic;
	private boolean hypertensive;
	private String bp;
	private boolean hx;
	private String labs;
	
	
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
	public String getBp() {
		return bp;
	}
	public void setBp(String bp) {
		this.bp = bp;
	}
	public boolean isHx() {
		return hx;
	}
	public void setHx(boolean hx) {
		this.hx = hx;
	}
	public String getLabs() {
		return labs;
	}
	public void setLabs(String labs) {
		this.labs = labs;
	}
	
	
}
