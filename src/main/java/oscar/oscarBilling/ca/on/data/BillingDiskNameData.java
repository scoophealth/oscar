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

import java.util.Vector;

public class BillingDiskNameData {
	String id;
	String monthCode;
	String batchcount;
	String ohipfilename;
	String groupno;
	String creator;
	String claimrecord;
	String createdatetime;
	String status;
	String total;
	String updatedatetime;
	Vector vecFilenameId;
	Vector htmlfilename;
	Vector providerohipno;
	Vector providerno;
	Vector vecClaimrecord;
	Vector vecStatus;
	Vector vecTotal;
	
	public String getBatchcount() {
		return batchcount;
	}

	public void setBatchcount(String batchcount) {
		this.batchcount = batchcount;
	}

	public String getClaimrecord() {
		return claimrecord;
	}

	public void setClaimrecord(String claimrecord) {
		this.claimrecord = claimrecord;
	}

	public String getCreatedatetime() {
		return createdatetime;
	}

	public void setCreatedatetime(String createdatetime) {
		this.createdatetime = createdatetime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getGroupno() {
		return groupno;
	}

	public void setGroupno(String groupno) {
		this.groupno = groupno;
	}

	public Vector getHtmlfilename() {
		return htmlfilename;
	}

	public void setHtmlfilename(Vector htmlfilename) {
		this.htmlfilename = htmlfilename;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMonthCode() {
		return monthCode;
	}

	public void setMonthCode(String monthCode) {
		this.monthCode = monthCode;
	}

	public String getOhipfilename() {
		return ohipfilename;
	}

	public void setOhipfilename(String ohipfilename) {
		this.ohipfilename = ohipfilename;
	}

	public Vector getProviderohipno() {
		return providerohipno;
	}

	public void setProviderohipno(Vector providerohipno) {
		this.providerohipno = providerohipno;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public Vector getVecClaimrecord() {
		return vecClaimrecord;
	}

	public void setVecClaimrecord(Vector vecClaimrecord) {
		this.vecClaimrecord = vecClaimrecord;
	}

	public Vector getVecStatus() {
		return vecStatus;
	}

	public void setVecStatus(Vector vecStatus) {
		this.vecStatus = vecStatus;
	}

	public Vector getVecTotal() {
		return vecTotal;
	}

	public void setVecTotal(Vector vecTotal) {
		this.vecTotal = vecTotal;
	}

	public Vector getProviderno() {
		return providerno;
	}

	public void setProviderno(Vector providerno) {
		this.providerno = providerno;
	}

	public String getUpdatedatetime() {
		return updatedatetime;
	}

	public void setUpdatedatetime(String updatedatetime) {
		this.updatedatetime = updatedatetime;
	}

	public Vector getVecFilenameId() {
		return vecFilenameId;
	}

	public void setVecFilenameId(Vector vecFilenameId) {
		this.vecFilenameId = vecFilenameId;
	}

}
