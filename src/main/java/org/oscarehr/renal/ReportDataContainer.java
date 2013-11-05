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

@XmlRootElement(name = "PreImplementationReport")
public class ReportDataContainer {

	private int totalDiabetic;
	private int totalHypertensive;
	private int totalBp;
	private int totalFamHx;
	private int totalAboriginals;
	private int totalAge;
	
	private int diabeticScreened1yr;
	private int hypertensiveScreened1yr;
	private int bpScreened1yr;
	private int famHxScreened1yr;
	private int aboriginalScreened1yr;
	private int ageScreened1yr;
	
	private int diabeticScreened;
	private int hypertensiveScreened;
	private int bpScreened;
	private int famHxScreened;
	private int aboriginalScreened;
	private int ageScreened;
	
	private double diabeticScreenedPerc;
	private double hypertensiveScreenedPerc;
	private double bpScreenedPerc;
	private double famHxScreenedPerc;
	private double aboriginalScreenedPerc;
	private double ageScreenedPerc;
	
	private double diabeticScreenedPerc1yr;
	private double hypertensiveScreenedPerc1yr;
	private double bpScreenedPerc1yr;
	private double famHxScreenedPerc1yr;
	private double aboriginalScreenedPerc1yr;
	private double ageScreenedPerc1yr;
	
	
	private int ckdStage1;
	private int ckdStage2;
	private int ckdStage3;
	private int ckdStage4;
	private int ckdStage5;
	
	
	private int egfrTestsOrdered;
	private int acrTestsOrdered;
	private int pcrTestsOrdered;
	
	private int egfrTestsReceived;
	private int acrTestsReceived;
	private int pcrTestsReceived;
	
	private double egfrTestsRatio;
	private double acrTestsRatio;
	private double pcrTestsRatio;
	
	
	private int totalPatients;
	
	private int diabetesAndDrugs;
	private int ckdAndDrugs;
	private int bpAndDrugs;
	
	private int diabeticAndBpTarget;
	private int ckdAndBpTarget;
	
	public int getTotalDiabetic() {
		return totalDiabetic;
	}
	public void setTotalDiabetic(int totalDiabetic) {
		this.totalDiabetic = totalDiabetic;
	}
	public int getTotalHypertensive() {
		return totalHypertensive;
	}
	public void setTotalHypertensive(int totalHypertensive) {
		this.totalHypertensive = totalHypertensive;
	}
	public int getTotalBp() {
		return totalBp;
	}
	public void setTotalBp(int totalBp) {
		this.totalBp = totalBp;
	}
	public int getTotalFamHx() {
		return totalFamHx;
	}
	public void setTotalFamHx(int totalFamHx) {
		this.totalFamHx = totalFamHx;
	}
	public int getTotalAboriginals() {
		return totalAboriginals;
	}
	public void setTotalAboriginals(int totalAboriginals) {
		this.totalAboriginals = totalAboriginals;
	}
	public int getTotalAge() {
		return totalAge;
	}
	public void setTotalAge(int totalAge) {
		this.totalAge = totalAge;
	}
	public int getDiabeticScreened1yr() {
		return diabeticScreened1yr;
	}
	public void setDiabeticScreened1yr(int diabeticScreened1yr) {
		this.diabeticScreened1yr = diabeticScreened1yr;
	}
	public int getHypertensiveScreened1yr() {
		return hypertensiveScreened1yr;
	}
	public void setHypertensiveScreened1yr(int hypertensiveScreened1yr) {
		this.hypertensiveScreened1yr = hypertensiveScreened1yr;
	}
	public int getBpScreened1yr() {
		return bpScreened1yr;
	}
	public void setBpScreened1yr(int bpScreened1yr) {
		this.bpScreened1yr = bpScreened1yr;
	}
	public int getFamHxScreened1yr() {
		return famHxScreened1yr;
	}
	public void setFamHxScreened1yr(int famHxScreened1yr) {
		this.famHxScreened1yr = famHxScreened1yr;
	}
	public int getAboriginalScreened1yr() {
		return aboriginalScreened1yr;
	}
	public void setAboriginalScreened1yr(int aboriginalScreened1yr) {
		this.aboriginalScreened1yr = aboriginalScreened1yr;
	}
	public int getAgeScreened1yr() {
		return ageScreened1yr;
	}
	public void setAgeScreened1yr(int ageScreened1yr) {
		this.ageScreened1yr = ageScreened1yr;
	}
	public int getDiabeticScreened() {
		return diabeticScreened;
	}
	public void setDiabeticScreened(int diabeticScreened) {
		this.diabeticScreened = diabeticScreened;
	}
	public int getHypertensiveScreened() {
		return hypertensiveScreened;
	}
	public void setHypertensiveScreened(int hypertensiveScreened) {
		this.hypertensiveScreened = hypertensiveScreened;
	}
	public int getBpScreened() {
		return bpScreened;
	}
	public void setBpScreened(int bpScreened) {
		this.bpScreened = bpScreened;
	}
	public int getFamHxScreened() {
		return famHxScreened;
	}
	public void setFamHxScreened(int famHxScreened) {
		this.famHxScreened = famHxScreened;
	}
	public int getAboriginalScreened() {
		return aboriginalScreened;
	}
	public void setAboriginalScreened(int aboriginalScreened) {
		this.aboriginalScreened = aboriginalScreened;
	}
	public int getAgeScreened() {
		return ageScreened;
	}
	public void setAgeScreened(int ageScreened) {
		this.ageScreened = ageScreened;
	}
	public double getDiabeticScreenedPerc() {
		return diabeticScreenedPerc;
	}
	public void setDiabeticScreenedPerc(double diabeticScreenedPerc) {
		this.diabeticScreenedPerc = diabeticScreenedPerc;
	}
	public double getHypertensiveScreenedPerc() {
		return hypertensiveScreenedPerc;
	}
	public void setHypertensiveScreenedPerc(double hypertensiveScreenedPerc) {
		this.hypertensiveScreenedPerc = hypertensiveScreenedPerc;
	}
	public double getBpScreenedPerc() {
		return bpScreenedPerc;
	}
	public void setBpScreenedPerc(double bpScreenedPerc) {
		this.bpScreenedPerc = bpScreenedPerc;
	}
	public double getFamHxScreenedPerc() {
		return famHxScreenedPerc;
	}
	public void setFamHxScreenedPerc(double famHxScreenedPerc) {
		this.famHxScreenedPerc = famHxScreenedPerc;
	}
	public double getAboriginalScreenedPerc() {
		return aboriginalScreenedPerc;
	}
	public void setAboriginalScreenedPerc(double aboriginalScreenedPerc) {
		this.aboriginalScreenedPerc = aboriginalScreenedPerc;
	}
	public double getAgeScreenedPerc() {
		return ageScreenedPerc;
	}
	public void setAgeScreenedPerc(double ageScreenedPerc) {
		this.ageScreenedPerc = ageScreenedPerc;
	}
	public double getDiabeticScreenedPerc1yr() {
		return diabeticScreenedPerc1yr;
	}
	public void setDiabeticScreenedPerc1yr(double diabeticScreenedPerc1yr) {
		this.diabeticScreenedPerc1yr = diabeticScreenedPerc1yr;
	}
	public double getHypertensiveScreenedPerc1yr() {
		return hypertensiveScreenedPerc1yr;
	}
	public void setHypertensiveScreenedPerc1yr(double hypertensiveScreenedPerc1yr) {
		this.hypertensiveScreenedPerc1yr = hypertensiveScreenedPerc1yr;
	}
	public double getBpScreenedPerc1yr() {
		return bpScreenedPerc1yr;
	}
	public void setBpScreenedPerc1yr(double bpScreenedPerc1yr) {
		this.bpScreenedPerc1yr = bpScreenedPerc1yr;
	}
	public double getFamHxScreenedPerc1yr() {
		return famHxScreenedPerc1yr;
	}
	public void setFamHxScreenedPerc1yr(double famHxScreenedPerc1yr) {
		this.famHxScreenedPerc1yr = famHxScreenedPerc1yr;
	}
	public double getAboriginalScreenedPerc1yr() {
		return aboriginalScreenedPerc1yr;
	}
	public void setAboriginalScreenedPerc1yr(double aboriginalScreenedPerc1yr) {
		this.aboriginalScreenedPerc1yr = aboriginalScreenedPerc1yr;
	}
	public double getAgeScreenedPerc1yr() {
		return ageScreenedPerc1yr;
	}
	public void setAgeScreenedPerc1yr(double ageScreenedPerc1yr) {
		this.ageScreenedPerc1yr = ageScreenedPerc1yr;
	}
	public int getCkdStage1() {
		return ckdStage1;
	}
	public void setCkdStage1(int ckdStage1) {
		this.ckdStage1 = ckdStage1;
	}
	public int getCkdStage2() {
		return ckdStage2;
	}
	public void setCkdStage2(int ckdStage2) {
		this.ckdStage2 = ckdStage2;
	}
	public int getCkdStage3() {
		return ckdStage3;
	}
	public void setCkdStage3(int ckdStage3) {
		this.ckdStage3 = ckdStage3;
	}
	public int getCkdStage4() {
		return ckdStage4;
	}
	public void setCkdStage4(int ckdStage4) {
		this.ckdStage4 = ckdStage4;
	}
	public int getCkdStage5() {
		return ckdStage5;
	}
	public void setCkdStage5(int ckdStage5) {
		this.ckdStage5 = ckdStage5;
	}
	public int getEgfrTestsOrdered() {
		return egfrTestsOrdered;
	}
	public void setEgfrTestsOrdered(int egfrTestsOrdered) {
		this.egfrTestsOrdered = egfrTestsOrdered;
	}
	public int getAcrTestsOrdered() {
		return acrTestsOrdered;
	}
	public void setAcrTestsOrdered(int acrTestsOrdered) {
		this.acrTestsOrdered = acrTestsOrdered;
	}
	public int getPcrTestsOrdered() {
		return pcrTestsOrdered;
	}
	public void setPcrTestsOrdered(int pcrTestsOrdered) {
		this.pcrTestsOrdered = pcrTestsOrdered;
	}
	public int getEgfrTestsReceived() {
		return egfrTestsReceived;
	}
	public void setEgfrTestsReceived(int egfrTestsReceived) {
		this.egfrTestsReceived = egfrTestsReceived;
	}
	public int getAcrTestsReceived() {
		return acrTestsReceived;
	}
	public void setAcrTestsReceived(int acrTestsReceived) {
		this.acrTestsReceived = acrTestsReceived;
	}
	public int getPcrTestsReceived() {
		return pcrTestsReceived;
	}
	public void setPcrTestsReceived(int pcrTestsReceived) {
		this.pcrTestsReceived = pcrTestsReceived;
	}
	public int getTotalPatients() {
		return totalPatients;
	}
	public void setTotalPatients(int totalPatients) {
		this.totalPatients = totalPatients;
	}
	public int getDiabetesAndDrugs() {
		return diabetesAndDrugs;
	}

	public int getCkdAndDrugs() {
		return ckdAndDrugs;
	}
	public void setCkdAndDrugs(int ckdAndDrugs) {
		this.ckdAndDrugs = ckdAndDrugs;
	}
	public int getBpAndDrugs() {
		return bpAndDrugs;
	}
	public void setBpAndDrugs(int bpAndDrugs) {
		this.bpAndDrugs = bpAndDrugs;
	}
	public void setDiabetesAndDrugs(int diabetesAndDrugs) {
		this.diabetesAndDrugs = diabetesAndDrugs;
	}
	public double getEgfrTestsRatio() {
		return egfrTestsRatio;
	}
	public void setEgfrTestsRatio(double egfrTestsRatio) {
		this.egfrTestsRatio = egfrTestsRatio;
	}
	public double getAcrTestsRatio() {
		return acrTestsRatio;
	}
	public void setAcrTestsRatio(double acrTestsRatio) {
		this.acrTestsRatio = acrTestsRatio;
	}
	public double getPcrTestsRatio() {
		return pcrTestsRatio;
	}
	public void setPcrTestsRatio(double pcrTestsRatio) {
		this.pcrTestsRatio = pcrTestsRatio;
	}
	public int getDiabeticAndBpTarget() {
		return diabeticAndBpTarget;
	}
	public void setDiabeticAndBpTarget(int diabeticAndBpTarget) {
		this.diabeticAndBpTarget = diabeticAndBpTarget;
	}
	public int getCkdAndBpTarget() {
		return ckdAndBpTarget;
	}
	public void setCkdAndBpTarget(int ckdAndBpTarget) {
		this.ckdAndBpTarget = ckdAndBpTarget;
	}
	
	
	
}
