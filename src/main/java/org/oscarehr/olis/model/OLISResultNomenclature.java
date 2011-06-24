package org.oscarehr.olis.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.oscarehr.common.model.AbstractModel;

@Entity
public class OLISResultNomenclature extends AbstractModel<String> {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private String id;
	private String name;
	private String nameId;

	public String getNameId() {
    	return nameId;
    }

	public void setNameId(String nameId) {
    	this.nameId = nameId;
    }

	@Override
    public String getId() {
	    return id;
    }

	public String getName() {
    	return name;
    }

	public void setName(String name) {
    	this.name = name;
    }

	public OLISResultNomenclature(){
		super();
	}	
}
