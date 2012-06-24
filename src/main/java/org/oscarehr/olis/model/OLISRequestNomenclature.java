/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */
package org.oscarehr.olis.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.oscarehr.common.model.AbstractModel;

@Entity
public class OLISRequestNomenclature extends AbstractModel<Integer> {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String nameId;
	private String name;
	private String category;

	public OLISRequestNomenclature(){
		super();
	}	

	@Override
    public Integer getId() {
	    return id;
    }

	public String getNameId() {
    	return nameId;
    }

	public void setNameId(String nameId) {
    	this.nameId = nameId;
    }
	
	public String getName() {
    	return name;
    }

	public void setName(String name) {
    	this.name = name;
    }

	public String getCategory() {
    	return category;
    }

	public void setCategory(String category) {
    	this.category = category;
	}

}
