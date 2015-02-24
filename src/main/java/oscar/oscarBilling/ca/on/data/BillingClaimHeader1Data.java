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


public class BillingClaimHeader1Data {
	String id;
	String bh_id;
	String transc_id;
	String rec_id;
	String hin;
	String ver;
	String dob;
	String acc_num;
	String pay_program;
	String payee;
	String ref_num;
	String facilty_num;
	String admission_date;
	String ref_lab_num;
	String man_review;
	String location;
	String clinic;
	String billto;

	String demographic_no;
	String provider_no;
	String appointment_no;
	String demographic_name;
	String last_name;
	String first_name;
	String sex;
	String province;

	String billing_date;
	String billing_time;
        private String settle_date;
	String total;
	String paid;
	String status;
	String comment;
	String visittype;
	String provider_ohip_no;
	String provider_rma_no;
	String apptProvider_no;
	String asstProvider_no;
	String creator;
	String update_datetime;
	String billing_on_item_id; // just used for 3rd party bill invoice report
	String ser_num;

	double cashTotal;
	double debitTotal;
	String providerName;
	int numItems;
	
	public String getSer_num() {
		return ser_num;
	}

	public void setSer_num(String ser_num) {
		this.ser_num = ser_num;
	}


	public String getClinic() {
		return clinic;
	}

	public void setClinic(String clinic) {
		this.clinic = clinic;
	}

	public String getAcc_num() {
		return acc_num;
	}

	public void setAcc_num(String acc_num) {
		this.acc_num = acc_num;
	}

	public String getAdmission_date() {
		return admission_date;
	}

	public void setAdmission_date(String admission_date) {
		this.admission_date = admission_date;
	}

	public String getAppointment_no() {
		return appointment_no;
	}

	public void setAppointment_no(String appointment_no) {
		this.appointment_no = appointment_no;
	}

	public String getApptProvider_no() {
		return apptProvider_no;
	}

	public void setApptProvider_no(String apptProvider_no) {
		this.apptProvider_no = apptProvider_no;
	}

	public String getAsstProvider_no() {
		return asstProvider_no;
	}

	public void setAsstProvider_no(String asstProvider_no) {
		this.asstProvider_no = asstProvider_no;
	}

	public String getBh_id() {
		return bh_id;
	}

	public void setBh_id(String bh_id) {
		this.bh_id = bh_id;
	}

	public String getBilling_date() {
		return billing_date;
	}

	public void setBilling_date(String billing_date) {
		this.billing_date = billing_date;
	}

	public String getBilling_time() {
		return billing_time;
	}

	public void setBilling_time(String billing_time) {
		this.billing_time = billing_time;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getDemographic_name() {
		return demographic_name;
	}

	public void setDemographic_name(String demographic_name) {
		this.demographic_name = demographic_name;
	}

	public String getDemographic_no() {
		return demographic_no;
	}

	public void setDemographic_no(String demographic_no) {
		this.demographic_no = demographic_no;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getFacilty_num() {
		return facilty_num;
	}

	public void setFacilty_num(String facilty_num) {
		this.facilty_num = facilty_num;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getHin() {
		return hin;
	}

	public void setHin(String hin) {
		this.hin = hin;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getMan_review() {
		return man_review;
	}

	public void setMan_review(String man_review) {
		this.man_review = man_review;
	}

	public String getPaid() {
		return paid;
	}

	public void setPaid(String paid) {
		this.paid = paid;
	}

	public String getPay_program() {
		return pay_program;
	}

	public void setPay_program(String pay_program) {
		this.pay_program = pay_program;
	}

	public String getPayee() {
		return payee;
	}

	public void setPayee(String payee) {
		this.payee = payee;
	}

	public String getProviderNo() {
		return provider_no;
	}

	public void setProviderNo(String provider_no) {
		this.provider_no = provider_no;
	}

	public String getProvider_ohip_no() {
		return provider_ohip_no;
	}

	public void setProvider_ohip_no(String provider_ohip_no) {
		this.provider_ohip_no = provider_ohip_no;
	}

	public String getProvider_rma_no() {
		return provider_rma_no;
	}

	public void setProvider_rma_no(String provider_rma_no) {
		this.provider_rma_no = provider_rma_no;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getRec_id() {
		return rec_id;
	}

	public void setRec_id(String rec_id) {
		this.rec_id = rec_id;
	}

	public String getRef_lab_num() {
		return ref_lab_num;
	}

	public void setRef_lab_num(String ref_lab_num) {
		this.ref_lab_num = ref_lab_num;
	}

	public String getRef_num() {
		return ref_num;
	}

	public void setRef_num(String ref_num) {
		this.ref_num = ref_num;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
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

	public String getTransc_id() {
		return transc_id;
	}

	public void setTransc_id(String transc_id) {
		this.transc_id = transc_id;
	}

	public String getVer() {
		return ver;
	}

	public void setVer(String ver) {
		this.ver = ver;
	}

	public String getVisittype() {
		return visittype;
	}

	public void setVisittype(String visittype) {
		this.visittype = visittype;
	}

	public String getUpdate_datetime() {
		return update_datetime;
	}

	public void setUpdate_datetime(String update_datetime) {
		this.update_datetime = update_datetime;
	}

    /**
     * @return the settle_date
     */
    public String getSettle_date() {
        return settle_date;
    }

    /**
     * @param settle_date the settle_date to set
     */
    public void setSettle_date(String settle_date) {
        this.settle_date = settle_date;
    }

	public String getBilling_on_item_id() {
		return billing_on_item_id;
	}

	public void setBilling_on_item_id(String billing_on_item_id) {
		this.billing_on_item_id = billing_on_item_id;
	}

	public String getBillto() {
		return billto;
	}

	public void setBillto(String billto) {
		this.billto = billto;
	}
	
	public void clone(BillingClaimHeader1Data ch1Data) {
		if (ch1Data == null) {
			return;
		}
		this.id = ch1Data.getId();
		this.bh_id = ch1Data.getBh_id();
		this.transc_id = ch1Data.getTransc_id();
		this.rec_id = ch1Data.getRec_id();
		this.hin = ch1Data.getHin();
		this.ver = ch1Data.getVer();
		this.dob = ch1Data.getDob();
		this.acc_num = ch1Data.getAcc_num();
		this.pay_program = ch1Data.getPay_program();
		this.payee = ch1Data.getPayee();
		this.ref_num = ch1Data.getRef_num();
		this.facilty_num = ch1Data.getFacilty_num();
		this.admission_date = ch1Data.getAdmission_date();
		this.ref_lab_num = ch1Data.getRef_lab_num();
		this.man_review = ch1Data.getMan_review();
		this.location = ch1Data.getLocation();
		this.clinic = ch1Data.getClinic();
		this.billto = ch1Data.getBillto();

		this.demographic_no = ch1Data.getDemographic_no();
		this.provider_no = ch1Data.getProviderNo();
		this.appointment_no = ch1Data.getAppointment_no();
		this.demographic_name = ch1Data.getDemographic_name();
		this.last_name = ch1Data.getLast_name();
		this.first_name = ch1Data.getFirst_name();
		this.sex = ch1Data.getSex();
		this.province = ch1Data.getProvince();

		this.billing_date = ch1Data.getBilling_date();
		this.billing_time = ch1Data.getBilling_time();
	    this.settle_date = ch1Data.getSettle_date();
		this.total = ch1Data.getTotal();
		this.paid = ch1Data.getPaid();
		this.status = ch1Data.getStatus();
		this.comment = ch1Data.getComment();
		this.visittype = ch1Data.getVisittype();
		this.provider_ohip_no = ch1Data.getProvider_ohip_no();
		this.provider_rma_no = ch1Data.getProvider_rma_no();
		this.apptProvider_no = ch1Data.getApptProvider_no();
		this.asstProvider_no = ch1Data.getAsstProvider_no();
		this.creator = ch1Data.getCreator();
		this.update_datetime = ch1Data.getUpdate_datetime();
		this.billing_on_item_id = ch1Data.getBilling_on_item_id();
		this.ser_num = ch1Data.getSer_num();
	}


	public double getCashTotal() {
		return cashTotal;
	}

	public void setCashTotal(double cashTotal) {
		this.cashTotal = cashTotal;
	}

	public double getDebitTotal() {
		return debitTotal;
	}

	public void setDebitTotal(double debitTotal) {
		this.debitTotal = debitTotal;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public String getProvider_no() {
		return provider_no;
	}

	public void setProvider_no(String provider_no) {
		this.provider_no = provider_no;
	}

	public int getNumItems() {
		return numItems;
	}

	public void setNumItems(int numItems) {
		this.numItems = numItems;
	}

    
}
