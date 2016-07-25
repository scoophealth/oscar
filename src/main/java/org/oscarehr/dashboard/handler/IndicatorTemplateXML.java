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
package org.oscarehr.dashboard.handler;


import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This is a JAXB generated POJO.
 * Requires a valid Indicator Template XML
 *
 */
@XmlRootElement
public class IndicatorTemplateXML {

	private String template;
	private String author;
	private String category;
	private String subCategory;
	private String framework;
	private Date frameworkVersion;
	private String name;
	private String definition;
	private String notes;
	private String indicatorQuery;
	private String drillDownQuery;

	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	public String getAuthor() {
		return author;
	}
	@XmlElement
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getCategory() {
		return category;
	}
	@XmlElement
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getSubCategory() {
		return subCategory;
	}
	@XmlElement
	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}
	
	public String getFramework() {
		return framework;
	}
	@XmlElement
	public void setFramework(String framework) {
		this.framework = framework;
	}

	public Date getFrameworkVersion() {
		return frameworkVersion;
	}
	@XmlElement
	public void setFrameworkVersion(Date frameworkVersion) {
		this.frameworkVersion = frameworkVersion;
	}
	public String getName() {
		return name;
	}
	@XmlElement
	public void setName(String name) {
		this.name = name;
	}
	public String getDefinition() {
		return definition;
	}
	@XmlElement
	public void setDefinition(String definition) {
		this.definition = definition;
	}
	public String getNotes() {
		return notes;
	}
	@XmlElement
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getIndicatorQuery() {
		return indicatorQuery;
	}
	@XmlElement
	public void setIndicatorQuery(String indicatorQuery) {
		this.indicatorQuery = indicatorQuery;
	}
	public String getDrillDownQuery() {
		return drillDownQuery;
	}
	@XmlElement
	public void setDrillDownQuery(String drillDownQuery) {
		this.drillDownQuery = drillDownQuery;
	}

}
