/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */


package org.oscarehr.common.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name="demographicExt")
public class DemographicExt extends AbstractModel<Integer> implements Serializable {

	public static enum FIRST_NATION_KEY {
		statusNum, ethnicity, aboriginal, fNationCom, fNationFamilyNumber, fNationFamilyPosition
	}
	
	@Transient
    private int hashCode = Integer.MIN_VALUE;// primary key
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;// fields
	@Column(name="demographic_no")
    private Integer demographicNo;
	@Column(name="provider_no")
    private String providerNo;
	@Column(name="key_val")
    private String key;
    private String value;
    @Column(name="date_time")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date dateCreated;
    private boolean hidden;
	
	@PrePersist
	@PreUpdate
	protected void prePersist() {
		this.dateCreated = new Date();
	}
	
    // constructors
    public DemographicExt () {
        // do nothing
    }

    /**
     * Constructor for primary key
     */
    public DemographicExt (Integer id) {
        this.setId(id);
    }

	/**
	 * Constructor for primary key
	 */
	public DemographicExt(String providerNo, Integer demographicNo, String key, String value) {
		this.providerNo = providerNo;
		this.demographicNo = demographicNo;
		this.key = key;
		this.value = value;
	}
	
	/**
	 * Constructor for primary key
	 */
	public DemographicExt(Integer id, String providerNo, Integer demographicNo, String key, String value) {
		this.id = id;
		this.providerNo = providerNo;
		this.demographicNo = demographicNo;
		this.key = key;
		this.value = value;
	}
	
	/**
	 * Constructor for primary key
	 */
	public DemographicExt(String strId, String providerNo, Integer demographicNo, String key, String value) {
		try {
			this.id = Integer.parseInt(strId);
		} catch (NumberFormatException e) {
			this.id = null;
		}
		this.providerNo = providerNo;
		this.demographicNo = demographicNo;
		this.key = key;
		this.value = value;
	}
	
    /**
     * Return the unique identifier of this class
     * 
     *  generator-class="native"
     *  column="id"
     */
    public Integer getId () {
        return id;
    }

    /**
     * Set the unique identifier of this class
     * @param id the new ID
     */
    public void setId (Integer id) {
        this.id = id;
        this.hashCode = Integer.MIN_VALUE;
    }

    /**
     * Return the value associated with the column: demographic_no
     */
    public Integer getDemographicNo () {
        return demographicNo;
    }

    /**
     * Set the value related to the column: demographic_no
     * @param demographicNo the demographic_no value
     */
    public void setDemographicNo (Integer demographicNo) {
        this.demographicNo = demographicNo;
    }

    /**
     * Return the value associated with the column: provider_no
     */
    public String getProviderNo () {
        return providerNo;
    }

    /**
     * Set the value related to the column: provider_no
     * @param providerNo the provider_no value
     */
    public void setProviderNo (String providerNo) {
        this.providerNo = providerNo;
    }

    /**
     * Return the value associated with the column: key_val
     */
    public String getKey () {
        return key;
    }

    /**
     * Set the value related to the column: key_val
     * @param key the key_val value
     */
    public void setKey (String key) {
        this.key = key;
    }

    /**
     * Return the value associated with the column: value
     */
    public String getValue () {
        return value;
    }

    /**
     * Set the value related to the column: value
     * @param value the value value
     */
    public void setValue (String value) {
        this.value = value;
    }

    /**
     * Return the value associated with the column: date_time
     */
    public java.util.Date getDateCreated () {
        return dateCreated;
    }

    /**
     * Set the value related to the column: date_time
     * @param dateCreated the date_time value
     */
    public void setDateCreated (java.util.Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     * Return the value associated with the column: hidden
     */
    public boolean isHidden () {
        return hidden;
    }

    /**
     * Set the value related to the column: hidden
     * @param hidden the hidden value
     */
    public void setHidden (boolean hidden) {
        this.hidden = hidden;
    }

	public boolean equals (Object obj) {
        if (null == obj) return false;
        if (!(obj instanceof DemographicExt)) return false;
        else {
            DemographicExt demographicExt = (DemographicExt) obj;
            if (null == this.getId() || null == demographicExt.getId()) return false;
            else return (this.getId().equals(demographicExt.getId()));
        }
    }

    public int hashCode () {
        if (Integer.MIN_VALUE == this.hashCode) {
            if (null == this.getId()) return super.hashCode();
            else {
                String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
                this.hashCode = hashStr.hashCode();
            }
        }
        return this.hashCode;
    }

    public String toString () {
        return "DemographicExt: id=" + getId() + ",key=" + this.getKey() + ",value=" + this.getValue() + ",providerNo=" + this.getProviderNo() + ",demographicNo=" + this.getDemographicNo();
    }
}
