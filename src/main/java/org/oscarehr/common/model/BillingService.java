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

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;

import org.oscarehr.billing.CA.ON.model.BillingPercLimit;

@Entity
@Table(name = "billingservice")
public class BillingService extends AbstractModel<Integer> implements Serializable {

	@Id()
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "billingservice_no")
	private Integer billingserviceNo;

	@Column(name = "service_compositecode")
	private String serviceCompositecode;
	@Column(name = "service_code")
	private String serviceCode;
	// @Column(name = "description")
	private String description;
	// @Column(name = "value")
	private String value;
	// @Column(name = "percentage")
	private String percentage;
	@Column(name = "billingservice_date")
	@Temporal(value = javax.persistence.TemporalType.DATE)
	private Date billingserviceDate;
	// @Column(name = "specialty")
	private String specialty;
	// @Column(name = "region")
	private String region;
	// @Column(name = "anaesthesia")
	private String anaesthesia;
	@Column(name = "termination_date")
	@Temporal(value = javax.persistence.TemporalType.DATE)
	private Date terminationDate;

	@Column(name = "displaystyle")
	private Integer displayStyle;

	private Boolean gstFlag;
	private Boolean sliFlag;
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="service_code")	
	@OrderBy("effective_date DESC")
	private List<BillingPercLimit> billingPercLimit;

	@Override
    public Integer getId() {
	    // TODO Auto-generated method stub
	    return billingserviceNo;
    }

	/**
	 * @return the billingserviceNo
	 */
	public int getBillingserviceNo() {
		return billingserviceNo;
	}

	/**
	 * @param billingserviceNo the billingserviceNo to set
	 */
	public void setBillingserviceNo(int billingserviceNo) {
		this.billingserviceNo = billingserviceNo;
	}

	/**
	 * @return the serviceCompositecode
	 */
	public String getServiceCompositecode() {
		return serviceCompositecode;
	}

	/**
	 * @param serviceCompositecode the serviceCompositecode to set
	 */
	public void setServiceCompositecode(String serviceCompositecode) {
		this.serviceCompositecode = serviceCompositecode;
	}

	/**
	 * @return the service_code
	 */
	public String getServiceCode() {
		return serviceCode;
	}

	/**
	 * @param service_code the service_code to set
	 */
	public void setServiceCode(String service_code) {
		this.serviceCode = service_code;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the percentage
	 */
	public String getPercentage() {
		return percentage;
	}

	/**
	 * @param percentage the percentage to set
	 */
	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}

	/**
	 * @return the billingserviceDate
	 */
	public Date getBillingserviceDate() {
		return billingserviceDate;
	}

	/**
	 * @param billingserviceDate the billingserviceDate to set
	 */
	public void setBillingserviceDate(Date billingserviceDate) {
		this.billingserviceDate = billingserviceDate;
	}

	/**
	 * @return the specialty
	 */
	public String getSpecialty() {
		return specialty;
	}

	/**
	 * @param specialty the specialty to set
	 */
	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}

	/**
	 * @return the region
	 */
	public String getRegion() {
		return region;
	}

	/**
	 * @param region the region to set
	 */
	public void setRegion(String region) {
		this.region = region;
	}

	/**
	 * @return the anaesthesia
	 */
	public String getAnaesthesia() {
		return anaesthesia;
	}

	/**
	 * @param anaesthesia the anaesthesia to set
	 */
	public void setAnaesthesia(String anaesthesia) {
		this.anaesthesia = anaesthesia;
	}

	/**
	 * @return the terminationDate
	 */
	public Date getTerminationDate() {
		return terminationDate;
	}

	/**
	 * @param terminationDate the terminationDate to set
	 */
	public void setTerminationDate(Date terminationDate) {
		this.terminationDate = terminationDate;
	}

        /**
     * @return the Billingperc
     */
    public BillingPercLimit getBillingPercLimit() {

        if( billingPercLimit != null ) {
            Date d;
            for( int idx = 0; idx < billingPercLimit.size(); ++idx) {
                d = billingPercLimit.get(idx).getEffective_date();
                if( d != null && d.compareTo(this.billingserviceDate) < 0 || d.compareTo(this.billingserviceDate) == 0) {
                    return billingPercLimit.get(idx);
                }
            }
        }

        return null;

    }

    /**
     * @param Billingperc the Billingperc to set
     */
    public void setBillingperc(List<BillingPercLimit> Billingperc) {
        this.billingPercLimit = Billingperc;
    }

    /**
     * @return the gstFlag
     */
    public Boolean getGstFlag() {
        return gstFlag;
    }

    /**
     * @param gstFlag the gstFlag to set
     */
    public void setGstFlag(Boolean gstFlag) {
        this.gstFlag = gstFlag;
    }

	public Integer getDisplayStyle() {
	    return displayStyle;
    }

	public void setDisplayStyle(Integer displayStyle) {
	    this.displayStyle = displayStyle;
	}

	public Boolean getSliFlag() {
	    return sliFlag;
    }

	public void setSliFlag(Boolean sliFlag) {
	    this.sliFlag = sliFlag;
    }

}
