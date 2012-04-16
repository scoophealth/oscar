/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
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

package com.indivica.olis.queries;

import com.indivica.olis.parameters.PID51;
import com.indivica.olis.parameters.PID52;
import com.indivica.olis.parameters.PID7;
import com.indivica.olis.parameters.PID8;
import com.indivica.olis.parameters.ZPD1;

/**
 * Z50 - Identify Patient by Name, Sex, and Date of Birth
 * @author jen
 *
 */
public class Z50Query extends Query {

	private PID52 firstName = new PID52(); // mandatory
	private PID51 lastName = new PID51(); // mandatory
	private PID8 sex = new PID8(); // mandatory
	private PID7 dateOfBirth = new PID7(); // mandatory
	
	@Override
	public String getQueryHL7String() {
		String query = "";
		
		if (firstName != null)
			query += firstName.toOlisString() + "~";
		
		if (lastName != null)
			query += lastName.toOlisString() + "~";
		
		if (sex != null)
			query += sex.toOlisString() + "~";
		
		if (dateOfBirth != null)
			query += dateOfBirth.toOlisString() + "~";
		
		if(query.endsWith("~")) {
			query = query.substring(0,query.length()-1);
		}
		
		return query;
	}

	public void setFirstName(PID52 firstName) {
    	this.firstName = firstName;
    }

	public void setLastName(PID51 lastName) {
    	this.lastName = lastName;
    }

	public void setSex(PID8 sex) {
    	this.sex = sex;
    }

	public void setDateOfBirth(PID7 dateOfBirth) {
    	this.dateOfBirth = dateOfBirth;
    }

	@Override
	public QueryType getQueryType() {
		return QueryType.Z50;
	}

	@Override
    public void setConsentToViewBlockedInformation(ZPD1 consentToViewBlockedInformation) {
		throw new RuntimeException("Not valid for this type of query.");
    }

}
