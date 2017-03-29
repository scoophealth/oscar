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
package org.oscarehr.ws.rest.to.model;

import org.oscarehr.common.model.DemographicExt;

public class DemographicSearchRequest {
	
	public static enum SEARCHMODE {
        Name, Phone, HIN, Address, DOB, ChartNo, DemographicNo, BandNumber
	}
	
	public static enum SORTMODE {
        DemographicNo, Name, Phone, Address, DOB, ChartNo, Sex, ProviderName, RS, PS
	}
	
	public static enum SORTDIR {
        asc,desc
	}
	
	private SEARCHMODE mode;
	
	private String keyword;
	
	private boolean active;
	
	private boolean integrator;
	
	private boolean outOfDomain;
	
	private SORTMODE sortMode;
	
	private SORTDIR sortDir;
	
	private DemographicExt.FIRST_NATION_KEY firstNationKey;
	
	
	public DemographicExt.FIRST_NATION_KEY getFirstNationKey() {
		return firstNationKey;
	}

	public void setFirstNationKey( DemographicExt.FIRST_NATION_KEY firstNationKey) {
		this.firstNationKey = firstNationKey;
	}

	public SEARCHMODE getMode() {
		return mode;
	}

	public void setMode(SEARCHMODE mode) {
		this.mode = mode;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isIntegrator() {
		return integrator;
	}

	public void setIntegrator(boolean integrator) {
		this.integrator = integrator;
	}

	public boolean isOutOfDomain() {
		return outOfDomain;
	}

	public void setOutOfDomain(boolean outOfDomain) {
		this.outOfDomain = outOfDomain;
	}

	public SORTMODE getSortMode() {
		return sortMode;
	}

	public void setSortMode(SORTMODE sortMode) {
		this.sortMode = sortMode;
	}

	public SORTDIR getSortDir() {
		return sortDir;
	}

	public void setSortDir(SORTDIR sortDir) {
		this.sortDir = sortDir;
	}
	
}
