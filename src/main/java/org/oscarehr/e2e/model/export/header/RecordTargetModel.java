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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.marc.everest.datatypes.AD;
import org.marc.everest.datatypes.ADXP;
import org.marc.everest.datatypes.AddressPartType;
import org.marc.everest.datatypes.EntityNameUse;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.NullFlavor;
import org.marc.everest.datatypes.PN;
import org.marc.everest.datatypes.PostalAddressUse;
import org.marc.everest.datatypes.TEL;
import org.marc.everest.datatypes.TS;
import org.marc.everest.datatypes.TelecommunicationsAddressUse;
import org.marc.everest.datatypes.generic.CE;
import org.marc.everest.datatypes.generic.CS;
import org.marc.everest.datatypes.generic.SET;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.LanguageCommunication;
import org.marc.everest.rmim.uv.cdar2.vocabulary.AdministrativeGender;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.e2e.constant.Constants;
import org.oscarehr.e2e.constant.Mappings;
import org.oscarehr.e2e.constant.Constants.TelecomType;
import org.oscarehr.e2e.util.EverestUtils;

public class RecordTargetModel {
	private Demographic demographic;

	private SET<II> ids;
	private SET<AD> addresses;
	private SET<TEL> telecoms;
	private SET<PN> names;
	private CE<AdministrativeGender> gender;
	private TS birthDate;
	private ArrayList<LanguageCommunication> languages;

	public RecordTargetModel(Demographic demographic) {
		if(demographic == null) {
			this.demographic = new Demographic();
		} else {
			this.demographic = demographic;
		}

		setIds();
		setAddresses();
		setTelecoms();
		setNames();
		setGender();
		setBirthDate();
		setLanguages();
	}

	public SET<II> getIds() {
		return ids;
	}

	private void setIds() {
		II id = new II();
		if(!EverestUtils.isNullorEmptyorWhitespace(demographic.getHin())) {
			id.setRoot(Constants.DocumentHeader.BC_PHN_OID);
			id.setAssigningAuthorityName(Constants.DocumentHeader.BC_PHN_OID_ASSIGNING_AUTHORITY_NAME);
			id.setExtension(demographic.getHin());
		} else {
			id.setNullFlavor(NullFlavor.NoInformation);
		}
		this.ids = new SET<II>(id);
	}

	public SET<AD> getAddresses() {
		return addresses;
	}

	private void setAddresses() {
		ArrayList<ADXP> addrParts = new ArrayList<ADXP>();
		EverestUtils.addAddressPart(addrParts, demographic.getAddress(), AddressPartType.Delimiter);
		EverestUtils.addAddressPart(addrParts, demographic.getCity(), AddressPartType.City);
		EverestUtils.addAddressPart(addrParts, demographic.getProvince(), AddressPartType.State);
		EverestUtils.addAddressPart(addrParts, demographic.getPostal(), AddressPartType.PostalCode);
		if(!addrParts.isEmpty()) {
			CS<PostalAddressUse> use = new CS<PostalAddressUse>(PostalAddressUse.HomeAddress);
			AD addr = new AD(use, addrParts);
			this.addresses = new SET<AD>(addr);
		}
		else {
			this.addresses = null;
		}
	}

	public SET<TEL> getTelecoms() {
		return telecoms;
	}

	private void setTelecoms() {
		SET<TEL> telecoms = new SET<TEL>();
		EverestUtils.addTelecomPart(telecoms, demographic.getPhone(), TelecommunicationsAddressUse.Home, TelecomType.TELEPHONE);
		EverestUtils.addTelecomPart(telecoms, demographic.getPhone2(), TelecommunicationsAddressUse.WorkPlace, TelecomType.TELEPHONE);
		EverestUtils.addTelecomPart(telecoms, demographic.getEmail(), TelecommunicationsAddressUse.Home, TelecomType.EMAIL);
		if(!telecoms.isEmpty()) {
			this.telecoms = telecoms;
		}
		else {
			this.telecoms = null;
		}
	}

	public SET<PN> getNames() {
		return names;
	}

	private void setNames() {
		SET<PN> names = new SET<PN>();
		EverestUtils.addNamePart(names, demographic.getFirstName(), demographic.getLastName(), EntityNameUse.Legal);
		if(!names.isEmpty()) {
			this.names = names;
		}
		else {
			this.names = null;
		}
	}

	public CE<AdministrativeGender> getGender() {
		return gender;
	}

	private void setGender() {
		CE<AdministrativeGender> gender = new CE<AdministrativeGender>();
		if(EverestUtils.isNullorEmptyorWhitespace(demographic.getSex())) {
			gender.setNullFlavor(NullFlavor.NoInformation);
		}
		else {
			String sexCode = demographic.getSex().toUpperCase().replace("U", "UN");
			if(Mappings.genderCode.containsKey(sexCode)) {
				gender.setCodeEx(Mappings.genderCode.get(sexCode));
				gender.setDisplayName(Mappings.genderDescription.get(sexCode));
			}
			else {
				gender.setNullFlavor(NullFlavor.NoInformation);
			}
		}
		this.gender = gender;
	}

	public TS getBirthDate() {
		return birthDate;
	}

	private void setBirthDate() {
		TS birthDate = new TS();

		if(demographic.getYearOfBirth() != null && demographic.getMonthOfBirth() != null) {
			try {
				if(Integer.parseInt(demographic.getYearOfBirth()) >= 0 &&
						Integer.parseInt(demographic.getMonthOfBirth()) >= 1 &&
						Integer.parseInt(demographic.getMonthOfBirth()) <= 12) {
					Calendar cal = Calendar.getInstance();

					if(demographic.getDateOfBirth() != null &&
							Integer.parseInt(demographic.getDateOfBirth()) >= 1 &&
							Integer.parseInt(demographic.getDateOfBirth()) <= 31) {
						String dob = demographic.getYearOfBirth() + demographic.getMonthOfBirth() + demographic.getDateOfBirth();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
						cal.setTime(sdf.parse(dob));
						birthDate.setDateValuePrecision(TS.DAY);
					} else {
						String mob = demographic.getYearOfBirth() + demographic.getMonthOfBirth();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
						cal.setTime(sdf.parse(mob));
						birthDate.setDateValuePrecision(TS.MONTH);
					}

					birthDate.setDateValue(cal);
				} else {
					throw new NumberFormatException();
				}
			} catch (Exception e) {
				birthDate.setNullFlavor(NullFlavor.Other);
			}
		} else {
			birthDate.setNullFlavor(NullFlavor.NoInformation);
		}

		this.birthDate = birthDate;
	}

	public ArrayList<LanguageCommunication> getLanguages() {
		return languages;
	}

	private void setLanguages() {
		ArrayList<LanguageCommunication> languages = new ArrayList<LanguageCommunication>();
		EverestUtils.addLanguagePart(languages, demographic.getOfficialLanguage());
		if(!languages.isEmpty()) {
			this.languages = languages;
		}
		else {
			this.languages = null;
		}
	}
}
