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


public class BillingItemData {
	String id;
	String ch1_id;
	String transc_id;
	String rec_id;
	String service_code;
	String fee;
	String ser_num;
	String service_date;
	String dx;
	String dx1;
	String dx2;
	String paid;
	String refund;
	String credit;
	String discount;
	String status;
	String timestamp;
	String location;
	String patientName;
        /*
         *Default Constructor
         */
        public BillingItemData() {

        }

        /*
         *Copy constructor
         */
        public BillingItemData( BillingItemData copy ) {

            this.setId(copy.getId());
            this.setCh1_id(copy.getCh1_id());
            this.setTransc_id(copy.getTransc_id());
            this.setRec_id(copy.getRec_id());
            this.setService_code(copy.getService_code());
            this.setFee(copy.getFee());
            this.setSer_num(copy.getSer_num());
            this.setService_date(copy.getService_date());
            this.setDx(copy.getDx());
            this.setDx1(copy.getDx1());
            this.setDx2(copy.getDx2());
            this.setPaid(copy.getPaid());
            this.setRefund(copy.getRefund());
            this.setCredit(copy.getCredit());
            this.setDiscount(copy.getDiscount());
            this.setStatus(copy.getStatus());
            this.setTimestamp(copy.getTimestamp());
            this.setLocation(copy.getLocation());
            this.setPatientName(copy.getPatientName());
        }

	public String getCh1_id() {
		return ch1_id;
	}
	public void setCh1_id(String ch1_id) {
		this.ch1_id = ch1_id;
	}
	public String getDx() {
		return dx;
	}
	public void setDx(String dx) {
		this.dx = dx;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRec_id() {
		return rec_id;
	}
	public void setRec_id(String rec_id) {
		this.rec_id = rec_id;
	}
	public String getSer_num() {
		return ser_num;
	}
	public void setSer_num(String ser_num) {
		this.ser_num = ser_num;
	}
	public String getService_code() {
		return service_code;
	}
	public void setService_code(String service_code) {
		this.service_code = service_code;
	}
	public String getService_date() {
		return service_date;
	}
	public void setService_date(String service_date) {
		this.service_date = service_date;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getTransc_id() {
		return transc_id;
	}
	public void setTransc_id(String transc_id) {
		this.transc_id = transc_id;
	}
	public String getDx1() {
		return dx1;
	}
	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
	}

	public void setDx1(String dx1) {
		this.dx1 = dx1;
	}
	public String getDx2() {
		return dx2;
	}
	public void setDx2(String dx2) {
		this.dx2 = dx2;
	}

	public String getLocation() {
    	return location;
    }

	public void setLocation(String location) {
    	this.location = location;
    }

	public String getPaid() {
		return paid;
	}

	public void setPaid(String paid) {
		this.paid = paid;
	}

	public String getRefund() {
		return refund;
	}

	public void setRefund(String refund) {
		this.refund = refund;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	
	
}
