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
