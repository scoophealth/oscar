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

package org.oscarehr.PMmodule.exporter;

public class DATISField {

	private String name;
	private DATISType type;
	private int columnPosition;
	private int maxSize;
	private String description;
	private String dateFormat;
	private String question;
	
	public DATISField() {}
	
	public DATISField(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getColumnPosition() {
		return columnPosition;
	}
	public void setColumnPosition(int columnPosition) {
		this.columnPosition = columnPosition;
	}
	public int getMaxSize() {
		return maxSize;
	}
	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDateFormat() {
		return dateFormat;
	}
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public DATISType getType() {
		return type;
	}

	public void setType(DATISType type) {
		this.type = type;
	}
	
	public void setType(String type) {
		if(type.equalsIgnoreCase(DATISType.TEXT.getValue())) {
			this.type = DATISType.TEXT;
		} else if(type.equalsIgnoreCase(DATISType.DATETIME.getValue())) {
			this.type = DATISType.DATETIME;
		} else if(type.equalsIgnoreCase(DATISType.DOUBLE.getValue())) {
			this.type = DATISType.DOUBLE;
		} else if(type.equalsIgnoreCase(DATISType.INTEGER.getValue())) {
			this.type = DATISType.INTEGER;
		} else if(type.equalsIgnoreCase(DATISType.NUMBER.getValue())) {
			this.type = DATISType.NUMBER;
		} else if(type.equalsIgnoreCase(DATISType.SELECT_NUMERIC.getValue())) {
			this.type = DATISType.SELECT_NUMERIC;
		} else if(type.equalsIgnoreCase(DATISType.SELECT_YN.getValue())) {
			this.type = DATISType.SELECT_YN;
		} else if(type.equalsIgnoreCase(DATISType.SELECT_YNU.getValue())) {
			this.type = DATISType.SELECT_YNU;
		}
	}
	
}
