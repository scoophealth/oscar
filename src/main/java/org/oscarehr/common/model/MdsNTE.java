package org.oscarehr.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="mdsNTE")
public class MdsNTE extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="segmentID")
	private Integer id;

	private String sourceOfComment;

	private String comment;

	private int associatedOBX;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getSourceOfComment() {
    	return sourceOfComment;
    }

	public void setSourceOfComment(String sourceOfComment) {
    	this.sourceOfComment = sourceOfComment;
    }

	public String getComment() {
    	return comment;
    }

	public void setComment(String comment) {
    	this.comment = comment;
    }

	public int getAssociatedOBX() {
    	return associatedOBX;
    }

	public void setAssociatedOBX(int associatedOBX) {
    	this.associatedOBX = associatedOBX;
    }



}
