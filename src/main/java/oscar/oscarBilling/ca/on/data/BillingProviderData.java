/**
 * Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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
 */

package oscar.oscarBilling.ca.on.data;

public class BillingProviderData {
	   String providerNo;
	   String lastName;
	   String firstName;
	   String providerType;
	   String specialtyCode;
	   String team;
	   String ohipNo;
	   String rmaNo;
	   String billingNo;
	   String hso_no;
	   String status;
	   String billingGroupNo;
	public String getBillingGroupNo() {
		return billingGroupNo;
	}
	public void setBillingGroupNo(String billingGroupNo) {
		this.billingGroupNo = billingGroupNo;
	}
	public String getBillingNo() {
		return billingNo;
	}
	public void setBillingNo(String billingNo) {
		this.billingNo = billingNo;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getHso_no() {
		return hso_no;
	}
	public void setHso_no(String hso_no) {
		this.hso_no = hso_no;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getOhipNo() {
		return ohipNo;
	}
	public void setOhipNo(String ohipNo) {
		this.ohipNo = ohipNo;
	}
	public String getProviderNo() {
		return providerNo;
	}
	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}
	public String getProviderType() {
		return providerType;
	}
	public void setProviderType(String providerType) {
		this.providerType = providerType;
	}
	public String getRmaNo() {
		return rmaNo;
	}
	public void setRmaNo(String rmaNo) {
		this.rmaNo = rmaNo;
	}
	public String getSpecialtyCode() {
		return specialtyCode;
	}
	public void setSpecialtyCode(String specialtyCode) {
		this.specialtyCode = specialtyCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTeam() {
		return team;
	}
	public void setTeam(String team) {
		this.team = team;
	}

}
