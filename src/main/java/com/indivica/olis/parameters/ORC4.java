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


package com.indivica.olis.parameters;

/**
 * Placer Group Number
 * @author jen
 *
 */
public class ORC4 implements Parameter {

	private String entityIdentifier;
	private String universalId;
	private String universalIdType;
	
	public ORC4(String entityIdentifier, String universalId, String universalIdType) {
	    this.entityIdentifier = entityIdentifier;
	    this.universalId = universalId;
	    this.universalIdType = universalIdType;
    }

	public ORC4() {
    }

	@Override
	public String toOlisString() {
		return getQueryCode() + ".1^" + (entityIdentifier != null ? entityIdentifier : "") + "~" +
			getQueryCode() + ".3^" + (universalId != null ? universalId : "") + "~" +
			getQueryCode() + ".4^" + (universalIdType != null ? universalIdType : "");
	}

	@Override
	public void setValue(Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setValue(Integer part, Object value) {
		if (part == 1)
			this.entityIdentifier = (String) value;
		else if (part == 3)
			this.universalId = (String) value;
		else if (part == 4)
			this.universalIdType = (String) value;
	}

	@Override
	public void setValue(Integer part, Integer part2, Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getQueryCode() {
		return "@ORC.4";
	}

}
