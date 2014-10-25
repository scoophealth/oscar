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
package org.oscarehr.ws.rest.conversion;

import org.oscarehr.common.model.PharmacyInfo;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.ws.rest.to.model.PharmacyInfoTo1;

public class PharmacyInfoConverter extends AbstractConverter<PharmacyInfo, PharmacyInfoTo1> {

	@Override
	public PharmacyInfo getAsDomainObject(LoggedInInfo loggedInInfo,PharmacyInfoTo1 t) throws ConversionException {
		PharmacyInfo d = new PharmacyInfo();
		
		d.setId(t.getId());		
		d.setName(t.getName());
		d.setAddress(t.getAddress().getAddress());
		d.setCity(t.getAddress().getCity());
		d.setProvince(t.getAddress().getProvince());
		d.setPostalCode(t.getAddress().getPostal());
		d.setPhone1(t.getPhone1());
		d.setPhone2(t.getPhone2());
		d.setFax(t.getFax());
		d.setEmail(t.getEmail());
		d.setServiceLocationIdentifier(t.getServiceLocationIdentifier());
		d.setNotes(t.getNotes());
		d.setAddDate(t.getAddDate());
		d.setStatus(t.getStatus());
		return d;
	}

	@Override
	public PharmacyInfoTo1 getAsTransferObject(LoggedInInfo loggedInInfo,PharmacyInfo d) throws ConversionException {
		PharmacyInfoTo1 t = new PharmacyInfoTo1();
		
		t.setId(d.getId());
		t.setName(d.getName());
		t.getAddress().setAddress(d.getAddress());
		t.getAddress().setCity(d.getCity());
		t.getAddress().setProvince(d.getProvince());
		t.getAddress().setPostal(d.getPostalCode());
		t.setPhone1(d.getPhone1());
		t.setPhone2(d.getPhone2());
		t.setFax(d.getFax());
		t.setEmail(d.getEmail());
		t.setServiceLocationIdentifier(d.getServiceLocationIdentifier());
		t.setNotes(d.getNotes());
		t.setAddDate(d.getAddDate());
		t.setStatus(d.getStatus());
		return t;
	}

}
