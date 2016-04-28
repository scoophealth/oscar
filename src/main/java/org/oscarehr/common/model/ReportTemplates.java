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


package org.oscarehr.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="reportTemplates")
public class ReportTemplates extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="templateid")
	private Integer id;

	@Column(name="templatetitle")
	private String templateTitle;

	@Column(name="templatedescription")
	private String templateDescription;

	@Column(name="templatesql")
	private String templateSql;

	@Column(name="templatexml")
	private String templateXml;

	private int active;

	private String type;
	
	private Boolean sequence;
	
	private String uuid;
	
	private String category;
	

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getTemplateTitle() {
    	return templateTitle;
    }

	public void setTemplateTitle(String templateTitle) {
    	this.templateTitle = templateTitle;
    }

	public String getTemplateDescription() {
    	return templateDescription;
    }

	public void setTemplateDescription(String templateDescription) {
    	this.templateDescription = templateDescription;
    }

	public String getTemplateSql() {
    	return templateSql;
    }

	public void setTemplateSql(String templateSql) {
    	this.templateSql = templateSql;
    }

	public String getTemplateXml() {
    	return templateXml;
    }

	public void setTemplateXml(String templateXml) {
    	this.templateXml = templateXml;
    }

	public int getActive() {
    	return active;
    }

	public void setActive(int active) {
    	this.active = active;
    }

	public String getType() {
    	return type;
    }

	public void setType(String type) {
    	this.type = type;
    }

	public Boolean isSequence() {
		return (sequence!=null)?sequence:false;
	}

	public void setSequence(Boolean sequence) {
		this.sequence = sequence;
	}

	public String getUuid() {
    	return uuid;
    }

	public void setUuid(String uuid) {
    	this.uuid = uuid;
    }

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	

}
