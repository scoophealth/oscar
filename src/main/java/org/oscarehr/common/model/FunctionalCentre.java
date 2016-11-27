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

import java.util.Comparator;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class FunctionalCentre extends AbstractModel<String> {

	public static final Comparator<FunctionalCentre> ACCOUNT_ID_COMPARATOR=new Comparator<FunctionalCentre>()
	{
		public int compare(FunctionalCentre o1, FunctionalCentre o2) {
			return(o1.accountId.compareTo(o2.accountId));
		}	
	};

	@Id
	private String accountId;
    private String description;
	private boolean enableCbiForm = false;
	
	public FunctionalCentre () {
	}

	public FunctionalCentre(String id, String description) {
		this.accountId = id;
		this.description = description;
	}
	
	@Override
    public String getId() {
		return accountId;
	}

	public String getAccountId() {
		return accountId;
	}
	
	public void setAccountId(String accountId) {
    	this.accountId = accountId;
    }

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
    	this.description = description;
    }

	public boolean isEnableCbiForm() {
    	return enableCbiForm;
    }

	public void setEnableCbiForm(boolean enableCbiForm) {
    	this.enableCbiForm = enableCbiForm;
    }
	
	
}
