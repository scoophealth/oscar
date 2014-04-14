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
package oscar.oscarEncounter.data.myoscar;

import java.util.Date;

import org.apache.struts.action.ActionForm;
import org.oscarehr.myoscar.commons.MedicalDataType;
import org.oscarehr.util.DateRange;

import oscar.util.ConversionUtils;
import oscar.util.DateUtils;

public class EctMyOscarFilterForm extends ActionForm {

	private static final long serialVersionUID = 1L;

	private String type;
	
	private String demoNo;

	private String from;

	private String to;
	
	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public MedicalDataType getMedicalDataType() {
		if (getType() == null) {
			return null;
		}
		
		String type = getType().toLowerCase();
		
		for(MedicalDataType m : MedicalDataType.values()) {
			if (m.name().toLowerCase().equals(type)) {
				return m;
			}
		}
		
		return null;
	}
	
	public DateRange getDateRange() {
		Date fromDate = null;
		if (getFrom() != null && !getFrom().isEmpty()) {
			try {
				fromDate = DateUtils.toDate(getFrom());			
			} catch (Exception e) {
				// swallow
			}
		}
		
		Date toDate = null;
		if (getTo() != null && !getTo().isEmpty()) {
			try {
				toDate = DateUtils.toDate(getTo());
			} catch (Exception e) {
				// swallow
			}
		}
		
		DateRange range = new DateRange(fromDate, toDate);
		return range;
	}

	public String getDemoNo() {
		return demoNo;
	}
	
	public Integer getDemoNoAsInt() {
		return ConversionUtils.fromIntString(getDemoNo());
	}

	public void setDemoNo(String demoNo) {
		this.demoNo = demoNo;
	}

}
