package org.oscarehr.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="batchEligibility")
public class BatchEligibility extends AbstractModel<Integer> {

	@Column(name="responseCode")
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String MOHResponse;
	private String reason;

	@Override
    public Integer getId() {
	    return id;
    }

	public String getMOHResponse() {
    	return MOHResponse;
    }

	public void setMOHResponse(String mOHResponse) {
    	MOHResponse = mOHResponse;
    }

	public String getReason() {
    	return reason;
    }

	public void setReason(String reason) {
    	this.reason = reason;
    }




}
