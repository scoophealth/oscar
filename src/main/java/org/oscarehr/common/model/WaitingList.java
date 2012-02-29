package org.oscarehr.common.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="waitingList")
public class WaitingList extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="listID")
	private int listId;

	@Column(name="demographic_no")
	private int demographicNo;

	private String note;

	private long position;

	@Temporal(TemporalType.TIMESTAMP)
	private Date onListSince;

	@Column(name="is_history")
	private String isHistory;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public int getListId() {
    	return listId;
    }

	public void setListId(int listId) {
    	this.listId = listId;
    }

	public int getDemographicNo() {
    	return demographicNo;
    }

	public void setDemographicNo(int demographicNo) {
    	this.demographicNo = demographicNo;
    }

	public String getNote() {
    	return note;
    }

	public void setNote(String note) {
    	this.note = note;
    }

	public long getPosition() {
    	return position;
    }

	public void setPosition(long position) {
    	this.position = position;
    }

	public Date getOnListSince() {
    	return onListSince;
    }

	public void setOnListSince(Date onListSince) {
    	this.onListSince = onListSince;
    }

	public String getIsHistory() {
    	return isHistory;
    }

	public void setIsHistory(String isHistory) {
    	this.isHistory = isHistory;
    }


}
