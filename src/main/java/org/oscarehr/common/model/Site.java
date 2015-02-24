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

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="site")
public class Site extends AbstractModel<Integer> implements java.io.Serializable {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((siteId == null) ? 0 : siteId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Site other = (Site) obj;
		if (siteId == null) {
			if (other.siteId != null)
				return false;
		} else if (!siteId.equals(other.siteId))
			return false;
		return true;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="site_id")
	private Integer siteId;
	private String name;
	@Column(name="short_name")
	private String shortName;
	private String phone;
	private String fax;
	@Column(name="bg_color")
	private String bgColor;
	private String address;
	private String city;
	private String province;
	private String postal;
	@Column(name="providerId_from")
	private Integer providerIdFrom;
	@Column(name="providerId_to")
	private Integer providerIdTo;
	private byte status;
	private Integer siteLogoId=null;
	@Column(name="siteUrl", length=100)
	private String siteUrl = "";
	
	public String getSiteUrl() {
		return siteUrl;
	}

	public void setSiteUrl(String siteUrl) {
		this.siteUrl = siteUrl;
	}

	@Transient
	private String siteLogoDesc = null;
	
	/**
	 *     <set name="providers" table="providersite" cascade="all" lazy="false" fetch="join" inverse="true">
           <key column="site_id"/>
           <many-to-many column="provider_no" class="Provider"/>
        </set>

	 */

	@ManyToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinTable(name = "providersite",
	joinColumns = {
	@JoinColumn(name="site_id")
	},
	inverseJoinColumns = {
	@JoinColumn(name="provider_no")
	}
	)
	private Set<Provider> providers;

	public Site() {
	}

	public Site(String name, String shortName, String bgColor, byte status) {
		this.name = name;
		this.shortName = shortName;
		this.bgColor = bgColor;
		this.status = status;
	}

	public Site(String name, String shortName, String phone, String fax,
			String bgColor, String address, String city, String province,
			String postal, byte status , int providerIdFrom, int providerIdTo) {
		this.name = name;
		this.shortName = shortName;
		this.phone = phone;
		this.fax = fax;
		this.bgColor = bgColor;
		this.address = address;
		this.city = city;
		this.province = province;
		this.postal = postal;
		this.status = status;
		this.providerIdFrom = providerIdFrom;
		this.providerIdTo = providerIdTo;
	}

	public Integer getId() {
		return getSiteId();
	}

	public Integer getSiteId() {
		return this.siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortName() {
		return this.shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return this.fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getBgColor() {
		return this.bgColor;
	}

	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return this.province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getPostal() {
		return this.postal;
	}

	public void setPostal(String postal) {
		this.postal = postal;
	}

	public byte getStatus() {
		return this.status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public Integer getProviderIdFrom() {
		return providerIdFrom;
	}

	public void setProviderIdFrom(Integer providerIdFrom) {
		this.providerIdFrom = providerIdFrom;
	}

	public Integer getProviderIdTo() {
		return providerIdTo;
	}

	public void setProviderIdTo(Integer providerIdTo) {
		this.providerIdTo = providerIdTo;
	}

	public Set<Provider> getProviders() {
		return providers;
	}

	public void setProviders(Set<Provider> providers) {
		this.providers = providers;
	}

	public Integer getSiteLogoId() {
		return siteLogoId;
	}

	public void setSiteLogoId(Integer siteLogoId) {
		this.siteLogoId = siteLogoId;
	}

	public String getSiteLogoDesc() {
		return siteLogoDesc;
	}

	public void setSiteLogoDesc(String siteLogoDesc) {
		this.siteLogoDesc = siteLogoDesc;
	} 
	
	
}
