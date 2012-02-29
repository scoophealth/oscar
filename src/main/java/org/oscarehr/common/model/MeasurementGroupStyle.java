package org.oscarehr.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="measurementGroupStyle")
public class MeasurementGroupStyle extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="groupID")
	private Integer id;

	private String groupName;

	@Column(name="cssID")
	private int cssId;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getGroupName() {
    	return groupName;
    }

	public void setGroupName(String groupName) {
    	this.groupName = groupName;
    }

	public int getCssId() {
    	return cssId;
    }

	public void setCssId(int cssId) {
    	this.cssId = cssId;
    }


}
