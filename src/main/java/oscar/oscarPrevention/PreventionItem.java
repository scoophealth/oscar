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

package oscar.oscarPrevention;

import java.util.Date;

import org.oscarehr.caisi_integrator.ws.CachedDemographicPrevention;
import org.oscarehr.common.model.Prevention;

import oscar.util.ConversionUtils;

/**
 * 
 * @author Jay Gallagher
 */
public class PreventionItem {

	String id = null;
	String name = null;
	Date datePreformed = null;
	Date nextDate = null;
	String never = null;
	boolean refused;
	private boolean inelligible = false;
	private boolean remoteEntry = false;

	public PreventionItem() {
	}

	public PreventionItem(String name, Date d) {
		this.name = name;
		datePreformed = d;
	}

	public PreventionItem(String name, Date dPreformed, String never, Date dNext) {
		this.name = name;
		this.datePreformed = dPreformed;
		this.never = never;
		this.nextDate = dNext;
	}

	public PreventionItem(String name, Date dPreformed, String never, Date dNext, String result) {
		this.name = name;
		this.datePreformed = dPreformed;
		this.never = never;
		this.nextDate = dNext;
		this.inelligible = result.equalsIgnoreCase("2");
	}

	public PreventionItem(Prevention pp) {
		this.name = pp.getPreventionType();
		this.datePreformed = pp.getPreventionDate();
		this.never = ConversionUtils.toBoolString(pp.isNever()); 
		this.nextDate = pp.getNextDate();
		this.refused = pp.isRefused();
    }
	
	public PreventionItem(CachedDemographicPrevention pp) {
		this.name = pp.getPreventionType();
		this.datePreformed = pp.getPreventionDate().getTime();
		this.never = ConversionUtils.toBoolString(pp.isNever());
		this.nextDate = pp.getNextDate() == null ? null : pp.getNextDate().getTime();
		this.refused = pp.isRefused();
	}

	public boolean getNeverVal() {
		boolean ret = false;
		if (never != null && never.equals("1")) {
			ret = true;
		}
		return ret;
	}

	/**
	 * Getter for property datePreformed.
	 * 
	 * @return Value of property datePreformed.
	 */
	public java.util.Date getDatePreformed() {
		return datePreformed;
	}

	/**
	 * Setter for property datePreformed.
	 * 
	 * @param datePreformed
	 *            New value of property datePreformed.
	 */
	public void setDatePreformed(java.util.Date datePreformed) {
		this.datePreformed = datePreformed;
	}

	/**
	 * Getter for property next_date.
	 * 
	 * @return Value of property next_date.
	 */
	public java.util.Date getNextDate() {
		return nextDate;
	}

	/**
	 * Setter for property next_date.
	 * 
	 * @param nextDate
	 *            New value of property next_date.
	 */
	public void setNextDate(java.util.Date nextDate) {
		this.nextDate = nextDate;
	}

	public boolean isRemoteEntry() {
		return remoteEntry;
	}

	public void setRemoteEntry(boolean remoteEntry) {
		this.remoteEntry = remoteEntry;
	}

	public boolean isInelligible() {
		return this.inelligible;
	}

	public void setInelligible(boolean inelligible) {
		this.inelligible = inelligible;
	}
}
