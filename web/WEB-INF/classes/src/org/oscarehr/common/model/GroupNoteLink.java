package org.oscarehr.common.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PreRemove;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@SuppressWarnings("serial")
public class GroupNoteLink extends AbstractModel<Integer> implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date created = new Date();
	
	private Integer noteId;
	private Integer demographicNo;
	private boolean anonymous;
	private boolean active;
	
	
	
	@Override
    public int hashCode() {
		return (id != null ? id.hashCode() : 0);
	}
	
	@PreRemove
	protected void jpaPreventDelete()
	{
		throw(new UnsupportedOperationException("Remove is not allowed for this type of item."));
	}

	/*
	@PreUpdate
	protected void jpaPreventUpdate()
	{
		throw(new UnsupportedOperationException("Update is not allowed for this type of item."));
	}
	*/

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Integer getNoteId() {
		return noteId;
	}

	public void setNoteId(Integer noteId) {
		this.noteId = noteId;
	}

	public Integer getDemographicNo() {
		return demographicNo;
	}

	public void setDemographicNo(Integer demographicNo) {
		this.demographicNo = demographicNo;
	}

	public boolean isAnonymous() {
		return anonymous;
	}

	public void setAnonymous(boolean anonymous) {
		this.anonymous = anonymous;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
