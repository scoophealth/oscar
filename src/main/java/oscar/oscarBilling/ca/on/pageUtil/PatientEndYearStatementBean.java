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

package oscar.oscarBilling.ca.on.pageUtil;

import java.util.Date;

public class PatientEndYearStatementBean {
	private String patientName;
	private String patientNo;
        private String hin;
	private String address;
	private String phone;
	private String invoiced = "0.00";
	private String paid = "0.00";
	private String count = "0";
	private Date fromDate;
	private Date toDate;
	private String fromDateParam;
	private String todateParam;
	
	public PatientEndYearStatementBean(String patientName, String patientNo, Integer patientId, String hin,
			String address, String phone, Date fromDate, Date toDate, String fromDateParam, String toDateParam) {
		super();
		this.patientName = patientName;
		this.patientNo = patientNo;
                this.hin = hin;
		this.address = address;
		this.phone = phone;
                this.fromDate = fromDate;
                this.toDate = toDate;
                this.fromDateParam = fromDateParam;
                this.todateParam = toDateParam;
        }
	
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getHin() {
		return hin;
	}
	public void setHin(String hin) {
		this.hin = hin;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPatientNo() {
		return patientNo;
	}

	public void setPatientNo(String patientNo) {
		this.patientNo = patientNo;
        }

	public String getInvoiced() {
		return invoiced;
	}

	public void setInvoiced(String invoiced) {
		this.invoiced = invoiced;
	}

	public String getPaid() {
		return paid;
	}

	public void setPaid(String paid) {
		this.paid = paid;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getFromDateParam() {
		return fromDateParam;
	}

	public void setFromDateParam(String fromDateParam) {
		this.fromDateParam = fromDateParam;
	}

	public String getTodateParam() {
		return todateParam;
	}

	public void setTodateParam(String todateParam) {
		this.todateParam = todateParam;
	}

}
