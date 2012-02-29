package org.oscarehr.common.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="indivoDocs")
public class IndivoDocs extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private int oscarDocNo;

	private String indivoDocIdx;

	private String docType;

	@Temporal(TemporalType.DATE)
	private Date dateSent;

	private String update;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public int getOscarDocNo() {
    	return oscarDocNo;
    }

	public void setOscarDocNo(int oscarDocNo) {
    	this.oscarDocNo = oscarDocNo;
    }

	public String getIndivoDocIdx() {
    	return indivoDocIdx;
    }

	public void setIndivoDocIdx(String indivoDocIdx) {
    	this.indivoDocIdx = indivoDocIdx;
    }

	public String getDocType() {
    	return docType;
    }

	public void setDocType(String docType) {
    	this.docType = docType;
    }

	public Date getDateSent() {
    	return dateSent;
    }

	public void setDateSent(Date dateSent) {
    	this.dateSent = dateSent;
    }

	public String getUpdate() {
    	return update;
    }

	public void setUpdate(String update) {
    	this.update = update;
    }


}
