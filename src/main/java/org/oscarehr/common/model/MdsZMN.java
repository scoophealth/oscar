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


package org.oscarehr.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="mdsZMN")
public class MdsZMN extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="segmentID")
	private Integer id;

	private String resultMnemonic;

	private String resultMnemonicVersion;

	private String reportName;

	private String units;

	@Column(name="cumulativeSequence")
	private String cummulativeSequence;

	private String referenceRange;

	private String resultCode;

	private String reportForm;

	private String reportGroup;

	private String reportGroupVersion;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getResultMnemonic() {
    	return resultMnemonic;
    }

	public void setResultMnemonic(String resultMnemonic) {
    	this.resultMnemonic = resultMnemonic;
    }

	public String getResultMnemonicVersion() {
    	return resultMnemonicVersion;
    }

	public void setResultMnemonicVersion(String resultMnemonicVersion) {
    	this.resultMnemonicVersion = resultMnemonicVersion;
    }

	public String getReportName() {
    	return reportName;
    }

	public void setReportName(String reportName) {
    	this.reportName = reportName;
    }

	public String getUnits() {
    	return units;
    }

	public void setUnits(String units) {
    	this.units = units;
    }

	public String getCummulativeSequence() {
    	return cummulativeSequence;
    }

	public void setCummulativeSequence(String cummulativeSequence) {
    	this.cummulativeSequence = cummulativeSequence;
    }

	public String getReferenceRange() {
    	return referenceRange;
    }

	public void setReferenceRange(String referenceRange) {
    	this.referenceRange = referenceRange;
    }

	public String getResultCode() {
    	return resultCode;
    }

	public void setResultCode(String resultCode) {
    	this.resultCode = resultCode;
    }

	public String getReportForm() {
    	return reportForm;
    }

	public void setReportForm(String reportForm) {
    	this.reportForm = reportForm;
    }

	public String getReportGroup() {
    	return reportGroup;
    }

	public void setReportGroup(String reportGroup) {
    	this.reportGroup = reportGroup;
    }

	public String getReportGroupVersion() {
    	return reportGroupVersion;
    }

	public void setReportGroupVersion(String reportGroupVersion) {
    	this.reportGroupVersion = reportGroupVersion;
    }


}