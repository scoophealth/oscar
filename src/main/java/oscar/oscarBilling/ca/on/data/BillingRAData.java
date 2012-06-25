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

public class BillingRAData {
	String radetail_no;
	String raheader_no;
	String providerohip_no;
	String billing_no;
	String service_code;
	String service_count;
	String hin;
	String amountclaim;
	String amountpay;
	String service_date;
	String error_code;
	String billtype;
        String claim_no = "";
        
        public String getClaim_no() {
            return this.claim_no;
        }
        
        public void setClaim_no(String claim_no) {
            this.claim_no = claim_no;
        }
	public String getAmountclaim() {
		return amountclaim;
	}
	public void setAmountclaim(String amountclaim) {
		this.amountclaim = amountclaim;
	}
	public String getAmountpay() {
		return amountpay;
	}
	public void setAmountpay(String amountpay) {
		this.amountpay = amountpay;
	}
	public String getBilling_no() {
		return billing_no;
	}
	public void setBilling_no(String billing_no) {
		this.billing_no = billing_no;
	}
	public String getBilltype() {
		return billtype;
	}
	public void setBilltype(String billtype) {
		this.billtype = billtype;
	}
	public String getError_code() {
		return error_code;
	}
	public void setError_code(String error_code) {
		this.error_code = error_code;
	}
	public String getHin() {
		return hin;
	}
	public void setHin(String hin) {
		this.hin = hin;
	}
	public String getProviderohip_no() {
		return providerohip_no;
	}
	public void setProviderohip_no(String providerohip_no) {
		this.providerohip_no = providerohip_no;
	}
	public String getRadetail_no() {
		return radetail_no;
	}
	public void setRadetail_no(String radetail_no) {
		this.radetail_no = radetail_no;
	}
	public String getRaheader_no() {
		return raheader_no;
	}
	public void setRaheader_no(String raheader_no) {
		this.raheader_no = raheader_no;
	}
	public String getService_code() {
		return service_code;
	}
	public void setService_code(String service_code) {
		this.service_code = service_code;
	}
	public String getService_count() {
		return service_count;
	}
	public void setService_count(String service_count) {
		this.service_count = service_count;
	}
	public String getService_date() {
		return service_date;
	}
	public void setService_date(String service_date) {
		this.service_date = service_date;
	}
}
