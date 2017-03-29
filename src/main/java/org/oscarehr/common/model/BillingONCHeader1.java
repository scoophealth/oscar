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

package org.oscarehr.common.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PostPersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.cxf.common.util.StringUtils;

@Entity
@Table(name = "billing_on_cheader1")
public class BillingONCHeader1 extends AbstractModel<Integer> implements Serializable {

	private static final long serialVersionUID = 1L;
        
        public static final String SETTLED = "S";
        public static final String DELETED = "D";
        
	@Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Integer id;
	@Column(name = "header_id", nullable = false)
	private Integer headerId;
	@Column(name = "transc_id")
	private String transcId = "HE";
	@Column(name = "rec_id")
	private String recId = "H";
	private String hin = null;
	private String ver = "";
	private String dob = null;
	@Column(name = "pay_program")
	private String payProgram = "HCP";
	private String payee = "P";
	@Column(name = "ref_num")
	private String refNum  = null;
	@Column(name = "facilty_num")
	private String faciltyNum = null;
	@Column(name = "admission_date")
	private String admissionDate = null;
	@Column(name = "ref_lab_num")
	private String refLabNum = null;
	@Column(name = "man_review")
	private String manReview = null;
	private String location = null;
	@Column(name = "demographic_no", nullable = false)
	private Integer demographicNo = 0;
	@Column(name = "provider_no", nullable = false)
	private String providerNo = "";
	@Column(name = "appointment_no")
	private Integer appointmentNo = null;
	@Column(name = "demographic_name")
	private String demographicName = null;
	private String sex = "1";
	private String province = "ON";
	@Column(name = "billing_date")
	private String billingDate = null;
	@Column(name = "billing_time")
	private String billingTime = null; //time format
	private BigDecimal total = null;
	private BigDecimal paid = null;
	private String status = null;
	@Column(name = "comment1")
	private String comment = null;
	@Column(name = "visittype")
	private String visitType = null;
	@Column(name = "provider_ohip_no")
	private String providerOhipNo = null;
	@Column(name = "provider_rma_no")
	private String providerRmaNo = null;
	@Column(name = "apptProvider_no")
	private String apptProviderNo = null;
	@Column(name = "asstProvider_no")
	private String asstProviderNo = null;
	private String creator = null;        
	@Column(name = "timestamp1", insertable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
        private Date timestamp;
	
	private Integer programNo;
	
	private String clinic = null;
        @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
        @JoinColumn(name="ch1_id", referencedColumnName="id")
        private List<BillingONItem>billingItems = new ArrayList<BillingONItem>(); 

	public BillingONCHeader1() {}
	
	public BillingONCHeader1(Integer headerId, String transcId, String recId,
			String hin, String ver, String dob, String payProgram,
			String payee, String refNum, String faciltyNum, Date admissionDate,
			String refLabNum, String manReview, String location,
			Integer demographicNo, String providerNo, Integer appointmentNo,
			String demographicName, String sex, String province,
			Date billingDate, Date billingTime, Integer total, Integer paid,
			String status, String comment, String visitType,
			String providerOhipNo, String providerRmaNo, String apptProviderNo,
			String asstProviderNo, String creator, Date timestamp, String clinic, Integer programNo) {
		super();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		this.headerId = headerId;
		this.transcId = transcId;
		this.recId = recId;
		this.hin = hin;
		this.ver = ver;
		this.dob = dob;
		this.payProgram = payProgram;
		this.payee = payee;
		this.refNum = refNum;
		this.faciltyNum = faciltyNum;
		this.admissionDate = df.format(admissionDate);
		this.refLabNum = refLabNum;
		this.manReview = manReview;
		this.location = location;
		this.demographicNo = demographicNo;
		this.providerNo = providerNo;
		this.appointmentNo = appointmentNo;
		this.demographicName = demographicName;
		this.sex = sex;
		this.province = province;
		this.billingDate = df.format(billingDate);
		this.billingTime = df.format(billingTime);
		this.total = BigDecimal.ZERO;
		this.paid = BigDecimal.ZERO;
		this.status = status;
		this.comment = comment;
		this.visitType = visitType;
		this.providerOhipNo = providerOhipNo;
		this.providerRmaNo = providerRmaNo;
		this.apptProviderNo = apptProviderNo;
		this.asstProviderNo = asstProviderNo;
		this.creator = creator;
		this.timestamp = timestamp;
		this.clinic = clinic;
		this.programNo = programNo;
	}

	public BillingONCHeader1(Integer headerId, String transcId, String recId,
			String hin, String ver, String dob, String payProgram,
			String payee, String refNum, String faciltyNum, Date admissionDate,
			String refLabNum, String manReview, String location,
			Integer demographicNo, String providerNo, Integer appointmentNo,
			String demographicName, String sex, String province,
			Date billingDate, Date billingTime, Integer total, Integer paid,
			String status, String comment, String visitType,
			String providerOhipNo, String providerRmaNo, String apptProviderNo,
			String asstProviderNo, String creator, Date timestamp, String clinic,
                        List<BillingONItem> billingItems, Integer programNo) {
		super();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		this.headerId = headerId;
		this.transcId = transcId;
		this.recId = recId;
		this.hin = hin;
		this.ver = ver;
		this.dob = dob;
		this.payProgram = payProgram;
		this.payee = payee;
		this.refNum = refNum;
		this.faciltyNum = faciltyNum;
		this.admissionDate = df.format(admissionDate);
		this.refLabNum = refLabNum;
		this.manReview = manReview;
		this.location = location;
		this.demographicNo = demographicNo;
		this.providerNo = providerNo;
		this.appointmentNo = appointmentNo;
		this.demographicName = demographicName;
		this.sex = sex;
		this.province = province;
		this.billingDate = df.format(billingDate);
		this.billingTime = df.format(billingTime);
		this.total = BigDecimal.ZERO;
		this.paid = BigDecimal.ZERO;
		this.status = status;
		this.comment = comment;
		this.visitType = visitType;
		this.providerOhipNo = providerOhipNo;
		this.providerRmaNo = providerRmaNo;
		this.apptProviderNo = apptProviderNo;
		this.asstProviderNo = asstProviderNo;
		this.creator = creator;
		this.timestamp = timestamp;
		this.clinic = clinic;
        this.billingItems = billingItems;
        this.programNo = programNo;
	}

        @Override
	public Integer getId() {
		return id;
	}

	public Integer getHeaderId() {
		return headerId;
	}

	public void setHeaderId(Integer headerId) {
		this.headerId = headerId;
	}

	public String getTranscId() {
		return transcId;
	}

	public void setTranscId(String transcId) {
		this.transcId = transcId;
	}

	public String getRecId() {
		return recId;
	}

	public void setRecId(String recId) {
		this.recId = recId;
	}

	public String getHin() {
		return hin;
	}

	public void setHin(String hin) {
		this.hin = hin;
	}

	public String getVer() {
		return ver;
	}

	public void setVer(String ver) {
		this.ver = ver;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getPayProgram() {
		return payProgram;
	}

	public void setPayProgram(String payProgram) {
		this.payProgram = payProgram;
	}

	public String getPayee() {
		return payee;
	}

	public void setPayee(String payee) {
		this.payee = payee;
	}

	public String getRefNum() {
		return refNum;
	}

	public void setRefNum(String refNum) {
		this.refNum = refNum;
	}

	public String getFaciltyNum() {
		return faciltyNum;
	}

	public void setFaciltyNum(String faciltyNum) {
		this.faciltyNum = faciltyNum;
	}

	public Date getAdmissionDate() throws ParseException  {
		if(StringUtils.isEmpty(this.admissionDate)) return null;
		return (new SimpleDateFormat("yyyy-MM-dd")).parse(this.admissionDate);
	}

	public void setAdmissionDate(Date admissionDate) {
		this.admissionDate = (new SimpleDateFormat("yyyy-MM-dd")).format(admissionDate);
	}

	public String getRefLabNum() {
		return refLabNum;
	}

	public void setRefLabNum(String refLabNum) {
		this.refLabNum = refLabNum;
	}

	public String getManReview() {
		return manReview;
	}

	public void setManReview(String manReview) {
		this.manReview = manReview;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Integer getDemographicNo() {
		return demographicNo;
	}

	public void setDemographicNo(Integer demographicNo) {
		this.demographicNo = demographicNo;
	}

	public String getProviderNo() {
		return providerNo;
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	public Integer getAppointmentNo() {
		return appointmentNo;
	}

	public void setAppointmentNo(Integer appointmentNo) {
		this.appointmentNo = appointmentNo;
	}

	public String getDemographicName() {
		return demographicName;
	}

	public void setDemographicName(String demographicName) {
		this.demographicName = demographicName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public Date getBillingDate(){
		try {
	        return (new SimpleDateFormat("yyyy-MM-dd")).parse(this.billingDate);
        } catch (ParseException e) {
	        // TODO Auto-generated catch block
	        return null;
        }
	}

	public void setBillingDate(Date billingDate) {
		this.billingDate = (new SimpleDateFormat("yyyy-MM-dd")).format(billingDate);
	}

	public Date getBillingTime(){
		try {
	        return (new SimpleDateFormat("HH:mm:ss")).parse(this.billingTime);
        } catch (ParseException e) {
	        // TODO Auto-generated catch block
	        return null;
        }
	}

	public void setBillingTime(Date billingTime) {
		this.billingTime = (new SimpleDateFormat("HH:mm:ss")).format(billingTime);
	}

	public BigDecimal getTotal() {
		return this.total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getPaid() {
		return this.paid;
	}

	public void setPaid(BigDecimal paid) {
		this.paid = paid;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String value) {
		this.status = value;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getVisitType() {
		return visitType;
	}

	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}

	public String getProviderOhipNo() {
		return providerOhipNo;
	}

	public void setProviderOhipNo(String providerOhipNo) {
		this.providerOhipNo = providerOhipNo;
	}

	public String getProviderRmaNo() {
		return providerRmaNo;
	}

	public void setProviderRmaNo(String providerRmaNo) {
		this.providerRmaNo = providerRmaNo;
	}

	public String getApptProviderNo() {
		return apptProviderNo;
	}

	public void setApptProviderNo(String apptProviderNo) {
		this.apptProviderNo = apptProviderNo;
	}

	public String getAsstProviderNo() {
		return asstProviderNo;
	}

	public void setAsstProviderNo(String asstProviderNo) {
		this.asstProviderNo = asstProviderNo;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public String getClinic() {
		return clinic;
	}

	public void setClinic(String clinic) {
		this.clinic = clinic;
	}
        
	
    public Integer getProgramNo() {
		return programNo;
	}

	public void setProgramNo(Integer programNo) {
		this.programNo = programNo;
	}

	public List<BillingONItem> getBillingItems() {
            return billingItems;
        }

        public void setBillingItems(List<BillingONItem> billingItems) {
            this.billingItems = billingItems;
        }
        
        @PostPersist
        public void postPersist() {            
            for (BillingONItem b : this.billingItems) {
                b.setCh1Id(this.id);
            }                        
        }
                                                     
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            BillingONCHeader1 bill = (BillingONCHeader1) o;

            if (id != null ? !id.equals(bill.id) : bill.id != null) return false;

            return true;
        }
        
        @Override
        public int hashCode() {
            return (id != null ? id.hashCode() : 0);
        }
        
}
