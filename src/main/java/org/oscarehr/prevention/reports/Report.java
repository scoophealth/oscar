package org.oscarehr.prevention.reports;

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

import java.util.List;

import org.oscarehr.ws.rest.to.model.PreventionSearchTo1;

import oscar.oscarPrevention.reports.ReportItem;

public class Report {
	
	private int totalPatients = 0;
	private int ineligiblePatients = 0;
	private int up2date = 0;
	private PreventionSearchTo1 searchConfig = null;
	private boolean active = true;

	/*
	 h.put("up2date",""+Math.round(done));
          h.put("percent",percentStr);
          h.put("percentWithGrace",percentWithGraceStr);
          h.put("returnReport",returnReport);
          h.put("inEligible", ""+inList);
          h.put("eformSearch","Mam");
          h.put("followUpType","PAPF");
          h.put("BillCode", "Q001A"); 
	 */
	private List<ReportItem> items;

	public List<ReportItem> getItems() {
		return items;
	}

	public void setItems(List<ReportItem> items) {
		this.items = items;
	}

	public int getTotalPatients() {
		return totalPatients;
	}

	public void setTotalPatients(int totalPatients) {
		this.totalPatients = totalPatients;
	}

	public int getIneligiblePatients() {
		return ineligiblePatients;
	}

	public void setIneligiblePatients(int ineligiblePatients) {
		this.ineligiblePatients = ineligiblePatients;
	}

	public int getUp2date() {
		return up2date;
	}

	public void setUp2date(int up2date) {
		this.up2date = up2date;
	}

	public PreventionSearchTo1 getSearchConfig() {
		return searchConfig;
	}

	public void setSearchConfig(PreventionSearchTo1 searchConfig) {
		this.searchConfig = searchConfig;
	}
	
	public void incrementTotalPatients() {
		this.totalPatients++;
	}

	public void incrementIneligiblePatients() {
		this.ineligiblePatients++;
	}
	
	public void incrementUp2Date() {
		this.up2date++;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
