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

package org.oscarehr.ws.transfer_objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.oscarehr.common.model.Drug;
import org.oscarehr.common.model.Prescription;
import org.oscarehr.managers.PrescriptionManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;
import org.springframework.beans.BeanUtils;

public final class PrescriptionTransfer {

	private Integer id;
	private String providerNo;
	private Integer demographicId;
	private Date datePrescribed;
	private Date datePrinted;
	private String datesReprinted;
	private String textView;
	private String comments;
	private Date lastUpdateDate;

	private DrugTransfer[] drugs;

	public Integer getId() {
		return (id);
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getDatesReprinted() {
		return (datesReprinted);
	}

	public void setDatesReprinted(String datesReprinted) {
		this.datesReprinted = datesReprinted;
	}

	public String getTextView() {
		return (textView);
	}

	public void setTextView(String textView) {
		this.textView = textView;
	}

	public String getComments() {
		return (comments);
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Date getLastUpdateDate() {
		return (lastUpdateDate);
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public DrugTransfer[] getDrugs() {
		return (drugs);
	}

	public void setDrugs(DrugTransfer[] drugs) {
		this.drugs = drugs;
	}

	/**
	 * If prescription is null, then null is returned.
	 * If prescription is not null, then drugs must not be null, it should at least be an empty list to signify no drugs.
	 */
	public static PrescriptionTransfer toTransfer(Prescription prescription, List<Drug> drugs) {
		if (prescription == null) return (null);

		PrescriptionTransfer prescriptionTransfer = new PrescriptionTransfer();
		BeanUtils.copyProperties(prescription, prescriptionTransfer);

		prescriptionTransfer.setDrugs(DrugTransfer.toTransfers(drugs));

		return (prescriptionTransfer);
	}

	public static PrescriptionTransfer[] getTransfers(LoggedInInfo loggedInInfo, List<Prescription> prescriptions) {
		PrescriptionManager prescriptionManager = SpringUtils.getBean(PrescriptionManager.class);

		ArrayList<PrescriptionTransfer> results = new ArrayList<PrescriptionTransfer>();

		for (Prescription prescription : prescriptions) {
			List<Drug> drugs = prescriptionManager.getDrugsByScriptNo(loggedInInfo, prescription.getId(), false);
			PrescriptionTransfer prescriptionTransfer = PrescriptionTransfer.toTransfer(prescription, drugs);
			results.add(prescriptionTransfer);
		}

		return (results.toArray(new PrescriptionTransfer[0]));

	}

	@Override
	public String toString() {
		return (ReflectionToStringBuilder.toString(this));
	}
}
