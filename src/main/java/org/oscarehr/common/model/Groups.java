package org.oscarehr.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="groups_tbl")
public class Groups extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="groupID")
	private Integer id;

	@Column(name="parentID")
	private int parentId;

	private String groupDesc;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public int getParentId() {
    	return parentId;
    }

	public void setParentId(int parentId) {
    	this.parentId = parentId;
    }

	public String getGroupDesc() {
    	return groupDesc;
    }

	public void setGroupDesc(String groupDesc) {
    	this.groupDesc = groupDesc;
    }



}
