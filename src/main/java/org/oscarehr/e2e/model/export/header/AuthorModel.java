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
package org.oscarehr.e2e.model.export.header;

import org.marc.everest.datatypes.EntityNameUse;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.NullFlavor;
import org.marc.everest.datatypes.PN;
import org.marc.everest.datatypes.SC;
import org.marc.everest.datatypes.TEL;
import org.marc.everest.datatypes.TelecommunicationsAddressUse;
import org.marc.everest.datatypes.generic.SET;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.AuthoringDevice;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Person;
import org.oscarehr.common.model.ProviderData;
import org.oscarehr.e2e.constant.Constants;
import org.oscarehr.e2e.constant.Constants.TelecomType;
import org.oscarehr.e2e.util.EverestUtils;

public class AuthorModel {
	private final ProviderData provider;

	protected SET<II> ids;
	private SET<TEL> telecoms;
	protected Person person;
	private SET<II> deviceIds;
	private AuthoringDevice device;

	protected AuthorModel(String providerNo) {
		ProviderData provider = EverestUtils.getProviderFromString(providerNo);
		if(provider == null) {
			this.provider = new ProviderData();
		} else {
			this.provider = provider;
		}

		constructorHelper();
	}

	public AuthorModel(ProviderData provider) {
		if(provider == null) {
			this.provider = new ProviderData();
		} else {
			this.provider = provider;
		}

		constructorHelper();
	}

	private void constructorHelper() {
		setIds();
		setTelecoms();
		setPerson();
		setDeviceIds();
		setDevice();
	}

	public SET<II> getIds() {
		return ids;
	}

	private void setIds() {
		II id = new II();
		if(!EverestUtils.isNullorEmptyorWhitespace(provider.getPractitionerNo())) {
			id.setRoot(Constants.DocumentHeader.BC_MINISTRY_OF_HEALTH_PRACTITIONER_ID_OID);
			id.setAssigningAuthorityName(Constants.DocumentHeader.BC_MINISTRY_OF_HEALTH_PRACTITIONER_NAME);
			id.setExtension(provider.getPractitionerNo());
		} else if (!EverestUtils.isNullorEmptyorWhitespace(provider.getOhipNo())) {
			id.setRoot(Constants.DocumentHeader.MEDICAL_SERVICES_PLAN_BILLING_NUMBER_OID);
			id.setAssigningAuthorityName(Constants.DocumentHeader.MEDICAL_SERVICES_PLAN_BILLING_NUMBER_NAME);
			id.setExtension(provider.getOhipNo());
		} else if (provider.getId() != null) {
			id.setRoot(Constants.DocumentHeader.LOCALLY_ASSIGNED_IDENTIFIER_OID);
			id.setAssigningAuthorityName(Constants.DocumentHeader.LOCALLY_ASSIGNED_IDENTIFIER_NAME);
			id.setExtension(provider.getId());
		} else {
			id.setNullFlavor(NullFlavor.NoInformation);
		}
		this.ids = new SET<II>(id);
	}

	public SET<TEL> getTelecoms() {
		return telecoms;
	}

	private void setTelecoms() {
		SET<TEL> telecoms = new SET<TEL>();
		EverestUtils.addTelecomPart(telecoms, provider.getPhone(), TelecommunicationsAddressUse.Home, TelecomType.TELEPHONE);
		EverestUtils.addTelecomPart(telecoms, provider.getWorkPhone(), TelecommunicationsAddressUse.WorkPlace, TelecomType.TELEPHONE);
		EverestUtils.addTelecomPart(telecoms, provider.getEmail(), TelecommunicationsAddressUse.Home, TelecomType.EMAIL);
		if(!telecoms.isEmpty()) {
			this.telecoms = telecoms;
		} else {
			this.telecoms = null;
		}
	}

	public Person getPerson() {
		return person;
	}

	private void setPerson() {
		Person person = new Person();
		SET<PN> names = new SET<PN>();
		EverestUtils.addNamePart(names, provider.getFirstName(), provider.getLastName(), EntityNameUse.OfficialRecord);
		if(names.isEmpty()) {
			PN pn = new PN();
			pn.setNullFlavor(NullFlavor.NoInformation);
			names.add(pn);
		}
		person.setName(names);
		this.person = person;
	}

	public SET<II> getDeviceIds() {
		return deviceIds;
	}

	private void setDeviceIds() {
		II id = new II();
		id.setNullFlavor(NullFlavor.NoInformation);
		this.deviceIds = new SET<II>(id);
	}

	public AuthoringDevice getDevice() {
		return device;
	}

	private void setDevice() {
		AuthoringDevice device = new AuthoringDevice();
		device.setSoftwareName(new SC(Constants.EMR.EMR_VERSION));
		this.device = device;
	}
}
