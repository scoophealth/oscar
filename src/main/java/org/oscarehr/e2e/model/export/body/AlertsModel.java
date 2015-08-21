/**
 * Copyright (c) 2013-2015. Department of Computer Science, University of Victoria. All Rights Reserved.
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
 * Department of Computer Science
 * LeadLab
 * University of Victoria
 * Victoria, Canada
 */
package org.oscarehr.e2e.model.export.body;

import org.marc.everest.datatypes.ED;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.NullFlavor;
import org.marc.everest.datatypes.TS;
import org.marc.everest.datatypes.generic.CD;
import org.marc.everest.datatypes.generic.CE;
import org.marc.everest.datatypes.generic.IVL;
import org.marc.everest.datatypes.generic.SET;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ActStatus;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_BasicConfidentialityKind;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.e2e.constant.Constants;
import org.oscarehr.e2e.util.EverestUtils;

public class AlertsModel {
	private CaseManagementNote alert;

	private SET<II> ids;
	private CD<String> code;
	private ED text;
	private ActStatus statusCode;
	private IVL<TS> effectiveTime;
	private CE<x_BasicConfidentialityKind> confidentiality;

	public AlertsModel(CaseManagementNote alert) {
		if(alert == null) {
			this.alert = new CaseManagementNote();
		} else {
			this.alert = alert;
		}

		setIds();
		setCode();
		setText();
		setStatusCode();
		setEffectiveTime();
		setConfidentiality();
	}

	public String getTextSummary() {
		StringBuilder sb = new StringBuilder();

		if(alert.getObservation_date() != null) {
			sb.append(alert.getObservation_date());
		}
		if(!EverestUtils.isNullorEmptyorWhitespace(alert.getNote())) {
			sb.append(" ".concat(alert.getNote().replaceAll("\\\\n", "\n")));
		}

		return sb.toString();
	}

	public SET<II> getIds() {
		return ids;
	}

	private void setIds() {
		this.ids = EverestUtils.buildUniqueId(Constants.IdPrefixes.Alerts, alert.getId());
	}

	public CD<String> getCode() {
		return code;
	}

	public void setCode() {
		CD<String> code = new CD<String>();
		code.setNullFlavor(NullFlavor.NoInformation);
		this.code = code;
	}

	public ED getText() {
		return text;
	}

	private void setText() {
		ED text = new ED();
		if(!EverestUtils.isNullorEmptyorWhitespace(alert.getNote())) {
			text.setData(alert.getNote());
		} else {
			text.setNullFlavor(NullFlavor.NoInformation);
		}
		this.text = text;
	}

	public ActStatus getStatusCode() {
		return statusCode;
	}

	private void setStatusCode() {
		if(!alert.isArchived()) {
			this.statusCode = ActStatus.Active;
		} else {
			this.statusCode = ActStatus.Completed;
		}
	}

	public IVL<TS> getEffectiveTime() {
		return effectiveTime;
	}

	private void setEffectiveTime() {
		IVL<TS> ivl = new IVL<TS>();
		TS startTime = EverestUtils.buildTSFromDate(alert.getObservation_date());
		if(startTime != null) {
			ivl.setLow(startTime);
		} else {
			ivl.setNullFlavor(NullFlavor.NoInformation);
		}

		this.effectiveTime = ivl;
	}

	public CE<x_BasicConfidentialityKind> getConfidentiality() {
		return confidentiality;
	}

	private void setConfidentiality() {
		this.confidentiality = new CE<x_BasicConfidentialityKind>(x_BasicConfidentialityKind.Normal);
	}
}
