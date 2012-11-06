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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="oscarcommlocations")
public class OscarCommLocations extends AbstractModel<Integer> {

	@Id
	@Column(name="locationId")
	private Integer id;

	private String locationDesc;

	private String locationAuth;

	private int current1;

	private String addressBook;

	private String remoteServerURL;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getLocationDesc() {
    	return locationDesc;
    }

	public void setLocationDesc(String locationDesc) {
    	this.locationDesc = locationDesc;
    }

	public String getLocationAuth() {
    	return locationAuth;
    }

	public void setLocationAuth(String locationAuth) {
    	this.locationAuth = locationAuth;
    }

	public int getCurrent1() {
    	return current1;
    }

	public void setCurrent1(int current1) {
    	this.current1 = current1;
    }

	public String getAddressBook() {
    	return addressBook;
    }

	public void setAddressBook(String addressBook) {
    	this.addressBook = addressBook;
    }

	public String getRemoteServerURL() {
    	return remoteServerURL;
    }

	public void setRemoteServerURL(String remoteServerURL) {
    	this.remoteServerURL = remoteServerURL;
    }


}
