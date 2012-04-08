package org.oscarehr.common.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="secPrivilege")
public class SecPrivilege extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String privilege;
	private String description;

	public Integer getId() {
    	return id;
    }
	public void setId(Integer id) {
    	this.id = id;
    }
	public String getPrivilege() {
    	return privilege;
    }
	public void setPrivilege(String privilege) {
    	this.privilege = privilege;
    }
	public String getDescription() {
    	return description;
    }
	public void setDescription(String description) {
    	this.description = description;
    }



}
