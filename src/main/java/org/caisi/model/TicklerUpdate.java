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

package org.caisi.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.oscarehr.common.model.Provider;

public class TicklerUpdate extends BaseObject {
	private Long id;
	private long tickler_no;
	private char status;
        private String assignedTo;
        private String priority;
        private Date serviceDate;
	private String providerNo;
	private Date update_date;
	private Provider provider;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProviderNo() {
		return providerNo;
	}
	public void setProviderNo(String provider) {
		this.providerNo = provider;
	}
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
	}        
        public String getPriority() {
            return this.priority;
        }
        public void setPriority(String priority) {
            this.priority = priority;
        }        
        public Date getServiceDate() {
            return this.serviceDate;
        }
        public String getServiceDate(Locale locale) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", locale);
            return dateFormat.format(this.serviceDate);
        }
        public void setServiceDate(Date serviceDate) {
            this.serviceDate = serviceDate;
        }
        public String getAssignedTo() {
            return this.assignedTo;
        }
        public void setAssignedTo(String assignedTo){
            this.assignedTo = assignedTo;
        }
	public long getTickler_no() {
		return tickler_no;
	}
	public void setTickler_no(long tickler_no) {
		this.tickler_no = tickler_no;
	}
	public Date getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}
	public void setProvider(Provider provider) {
		this.provider = provider;
	}
	public Provider getProvider() {
		return provider;
	}
}
