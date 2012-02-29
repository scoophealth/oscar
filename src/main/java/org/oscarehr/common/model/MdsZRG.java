package org.oscarehr.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="mdsZRG")
public class MdsZRG extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="segmentID")
	private Integer id;

	private String reportSequence;

	@Column(name="reportGroupID")
	private String reportGroupId;

	private String reportGroupVersion;

	private String reportFlags;

	private String reportGroupsDesc;

	private String MDSIndex;

	private String reportGroupHeading;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getReportSequence() {
    	return reportSequence;
    }

	public void setReportSequence(String reportSequence) {
    	this.reportSequence = reportSequence;
    }

	public String getReportGroupId() {
    	return reportGroupId;
    }

	public void setReportGroupId(String reportGroupId) {
    	this.reportGroupId = reportGroupId;
    }

	public String getReportGroupVersion() {
    	return reportGroupVersion;
    }

	public void setReportGroupVersion(String reportGroupVersion) {
    	this.reportGroupVersion = reportGroupVersion;
    }

	public String getReportFlags() {
    	return reportFlags;
    }

	public void setReportFlags(String reportFlags) {
    	this.reportFlags = reportFlags;
    }

	public String getReportGroupsDesc() {
    	return reportGroupsDesc;
    }

	public void setReportGroupsDesc(String reportGroupsDesc) {
    	this.reportGroupsDesc = reportGroupsDesc;
    }

	public String getMDSIndex() {
    	return MDSIndex;
    }

	public void setMDSIndex(String mDSIndex) {
    	MDSIndex = mDSIndex;
    }

	public String getReportGroupHeading() {
    	return reportGroupHeading;
    }

	public void setReportGroupHeading(String reportGroupHeading) {
    	this.reportGroupHeading = reportGroupHeading;
    }



}
