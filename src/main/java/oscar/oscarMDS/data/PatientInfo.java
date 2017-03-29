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


package oscar.oscarMDS.data;	
	
public class PatientInfo implements Comparable<PatientInfo> {
	public String firstName = "",
		          lastName = "";
	public int    id,
		   		  docCount = 0,
		   		  labCount = 0;
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return lastName + ("".equals(lastName) ? "" : ", ") + firstName;			
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		PatientInfo other = (PatientInfo) obj;
		if (id != other.id) return false;
		return true;
	}


	public PatientInfo(int id, String firstName, String lastName) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	public int getDocCount() {
		return docCount;
	}
	public void setDocCount(int docCount) {
		this.docCount = docCount;
	}
	public int getLabCount() {
		return labCount;
	}
	public void setLabCount(int labCount) {
		this.labCount = labCount;
	}
	@Override
	public int compareTo(PatientInfo that) {
		return this.lastName.equals(that.lastName) ? this.firstName.compareTo(that.firstName) : this.lastName.compareTo(that.lastName);
	}		
}
