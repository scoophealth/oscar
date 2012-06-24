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

package org.oscarehr.PMmodule.streethealth;


public class StreetHealthReportKey {
	String cohort;
	String label;
	String answer;
	
	public StreetHealthReportKey() {
		
	}
	
	public StreetHealthReportKey(int cohort, String label, String answer) {
		setCohort(String.valueOf(cohort));
		setLabel(label);
		setAnswer(answer);
	}
	
	public String getCohort() {
		return cohort;
	}
	public void setCohort(String cohort) {
		this.cohort = cohort;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	public boolean equals(Object obj) {
		StreetHealthReportKey o;
        if (null == obj)
            return false;
        if (!(obj instanceof StreetHealthReportKey))
            return false;
        else {
        	o = (StreetHealthReportKey)obj;
        	return (this.getCohort().equals(o.getCohort()) && this.getAnswer().equals(o.getAnswer()) && this.getLabel().equals(o.getLabel()));
        }
	}
	
    public int hashCode() {
    	String hashStr = this.getClass().getName() + ":" + this.getCohort() + ":" + this.getAnswer() + ":" + this.getLabel();
    	return hashStr.hashCode();
    }

}
