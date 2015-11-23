/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

package org.oscarehr.hospitalReportManager.model;

import java.util.Comparator;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.oscarehr.common.model.AbstractModel;

@Entity
public class HRMDocument extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Date timeReceived;
	private String reportType;
	private String reportHash;
	private String reportLessTransactionInfoHash;
	private String reportLessDemographicInfoHash;
	private String reportStatus;
	private String reportFile;
        private String sourceFacility;
	
	private String unmatchedProviders;
	private Integer numDuplicatesReceived;
	private Date reportDate;

	private Integer parentReport;

	private Integer hrmCategoryId;

	private String description = "";
	
	@Override
	public Integer getId() {
		return id;
	}

	public Date getTimeReceived() {
		return timeReceived;
	}

	public void setTimeReceived(Date timeReceived) {
		this.timeReceived = timeReceived;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getReportHash() {
		return reportHash;
	}

	public void setReportHash(String reportHash) {
		this.reportHash = reportHash;
	}

	public String getReportLessTransactionInfoHash() {
		return reportLessTransactionInfoHash;
	}

	public void setReportLessTransactionInfoHash(String reportLessTransactionInfoHash) {
		this.reportLessTransactionInfoHash = reportLessTransactionInfoHash;
	}

	public String getReportLessDemographicInfoHash() {
		return reportLessDemographicInfoHash;
	}

	public void setReportLessDemographicInfoHash(String reportLessDemographicInfoHash) {
		this.reportLessDemographicInfoHash = reportLessDemographicInfoHash;
	}

	public String getReportStatus() {
		return reportStatus;
	}

	public void setReportStatus(String reportStatus) {
		this.reportStatus = reportStatus;
	}

	public String getReportFile() {
		return reportFile;
	}

	public void setReportFile(String reportFile) {
		this.reportFile = reportFile;
	}

        public String getSourceFacility() {
        return sourceFacility;
    }

        public void setSourceFacility(String sourceFacility) {
        this.sourceFacility = sourceFacility;
    }

	public String getUnmatchedProviders() {
		return unmatchedProviders;
	}

	public void setUnmatchedProviders(String unmatchedProviders) {
		this.unmatchedProviders = unmatchedProviders;
	}

	public Integer getNumDuplicatesReceived() {
		return numDuplicatesReceived;
	}

	public void setNumDuplicatesReceived(Integer numDuplicatesReceived) {
		this.numDuplicatesReceived = numDuplicatesReceived;
	}

	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	public Integer getParentReport() {
		return parentReport;
	}

	public void setParentReport(Integer parentReport) {
		this.parentReport = parentReport;
	}

	public Integer getHrmCategoryId() {
		return (hrmCategoryId);
	}

	public void setHrmCategoryId(Integer hrmCategoryId) {
		this.hrmCategoryId = hrmCategoryId;
	}

	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	/**
	 * This comparator sorts HRM Docs ascending based on the time received
	 */
	public static final Comparator<HRMDocument> HRM_DATE_COMPARATOR = new Comparator<HRMDocument>() {
		@Override
		public int compare(HRMDocument o1, HRMDocument o2) {
			return (o1.timeReceived.compareTo(o2.timeReceived));
		}
	};

	/**
	 * This comparator sorts EFormData ascending based on the formName
	 */
	public static final Comparator<HRMDocument> HRM_TYPE_COMPARATOR = new Comparator<HRMDocument>() {
		@Override
		public int compare(HRMDocument o1, HRMDocument o2) {
			return (o1.reportType.compareTo(o2.reportType));
		}
	};

}
