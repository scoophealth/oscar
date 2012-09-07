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

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.StringUtils;

@Entity
@Table(name = "prescription")
public class Prescription extends AbstractModel<Integer> implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "script_no")
	private Integer id;

	@Column(name = "provider_no")
	private String providerNo;

	@Column(name = "demographic_no")
	private Integer demographicId;

	@Column(name = "date_prescribed")
	@Temporal(TemporalType.DATE)
	private Date datePrescribed;

	@Column(name = "date_printed")
	@Temporal(TemporalType.DATE)
	private Date datePrinted;

	@Column(name = "dates_reprinted")
	private String datesReprinted;

	private String textView;

	@Column(name = "rx_comments")
	private String comments;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdateDate;

	@PreRemove
	protected void jpaPreventDelete() {
		throw (new UnsupportedOperationException("Remove is not allowed for this type of item."));
	}

	@PreUpdate
	@PrePersist
	protected void autoSetUpdateTime() {
		lastUpdateDate = new Date();
	}

	@Override
	public Integer getId() {
		return id;
	}

	public String getProviderNo() {
		return (providerNo);
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	public Integer getDemographicId() {
		return (demographicId);
	}

	public void setDemographicId(Integer demographicId) {
		this.demographicId = demographicId;
	}

	public Date getDatePrescribed() {
		return (datePrescribed);
	}

	public void setDatePrescribed(Date datePrescribed) {
		this.datePrescribed = datePrescribed;
	}

	public Date getDatePrinted() {
		return (datePrinted);
	}

	public void setDatePrinted(Date datePrinted) {
		this.datePrinted = datePrinted;
	}

	/**
	 * Gets a comma-separated string containing dates this prescription has been reprinted.
	 * 
	 * @return
	 * 		Returns the dates reprinted.
	 */
	public String getDatesReprinted() {
		return (datesReprinted);
	}

	public void setDatesReprinted(String datesReprinted) {
		this.datesReprinted = StringUtils.trimToNull(datesReprinted);
	}

	public String getTextView() {
		return (textView);
	}

	public void setTextView(String textView) {
		this.textView = StringUtils.trimToNull(textView);
	}

	public String getComments() {
		return (comments);
	}

	public void setComments(String comments) {
		this.comments = StringUtils.trimToNull(comments);
	}

	public Date getLastUpdateDate() {
		return (lastUpdateDate);
	}

	/**
	 * Checks if this prescription has been reprinted
	 * 
	 * @return
	 * 		Returns true if there is a reprint date available
	 */
	public boolean isReprinted() {
		return getDatesReprinted() != null && !getDatesReprinted().isEmpty();
	}

	/**
	 * Gets the number of times the prescription has been reprinted
	 * 
	 * @return
	 * 		Returns 0, if it hasn't been reprinted, or a number corresponding to the number of reprint dates
	 */
	public int getReprintCount() {
		if (!isReprinted()) return 0;
		return getDatesReprinted().split(",").length;
	}
}
