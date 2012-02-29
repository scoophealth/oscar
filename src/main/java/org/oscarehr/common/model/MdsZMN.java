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
