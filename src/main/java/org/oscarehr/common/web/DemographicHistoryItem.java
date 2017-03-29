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
package org.oscarehr.common.web;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;
/*
 * Used by address/phone history popup in master demographic.
 * type can be address,phone,phone2,cell at the moment
 */
public  class DemographicHistoryItem  implements Serializable {
	private String name;
	private String dateSeen;
	private String type;
	private String provider;
	
	public DemographicHistoryItem() {
		
	}
	
	public DemographicHistoryItem(String name, String type, String provider) {
		this.name=name;
		this.type = type;
		this.provider = provider;
	}
	public DemographicHistoryItem(String name, String type, String dateSeen, String provider) {
		this(name,type,provider);
		this.dateSeen = dateSeen;
	}
	public DemographicHistoryItem(String name, String type, Date dateSeen, String provider) {
		this(name,type,provider);
		
		if( dateSeen == null ) {
			this.dateSeen = null;
		}
		else {
			this.dateSeen = DateFormatUtils.ISO_DATE_FORMAT.format(dateSeen);
		}
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDateSeen() {
		return dateSeen;
	}
	public void setDateSeen(String dateSeen) {
		this.dateSeen = dateSeen;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}
	
	
	
	
}
