package org.oscarehr.eyeform.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.oscarehr.common.model.AbstractModel;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Provider;

@Entity
@Table(name="eyeform_followup")
public class FollowUp extends AbstractModel<Integer> {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="appointment_no")
	private int appointmentNo;
	
	@Transient
	private Demographic demographic;
	
	@Column(name="demographic_no")
	private int demographicNo;	
	private int timespan;
	private String timeframe;
	@Column(name="followup_provider")
	private String followupProvider;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	
	//private String ack1;
	//private String ack2;
	//private String ack3;
	@Transient
	private Provider provider;
	
	private String type;
	
	private String urgency;
	private String comment;
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Provider getProvider() {
		return provider;
	}
	public void setProvider(Provider provider) {
		this.provider = provider;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public int getAppointmentNo() {
		return appointmentNo;
	}
	public void setAppointmentNo(int appointmentNo) {
		this.appointmentNo = appointmentNo;
	}
	public Demographic getDemographic() {
		return demographic;
	}
	public void setDemographic(Demographic demographic) {
		this.demographic = demographic;
	}
	public int getTimespan() {
		return timespan;
	}
	public void setTimespan(int timespan) {
		this.timespan = timespan;
	}
	public String getTimeframe() {
		return timeframe;
	}
	public void setTimeframe(String timeframe) {
		this.timeframe = timeframe;
	}
	public String getFollowupProvider() {
		return followupProvider;
	}
	public void setFollowupProvider(String followupProvider) {
		this.followupProvider = followupProvider;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	/*
	public String getAck1() {
		return ack1;
	}
	public void setAck1(String ack1) {
		this.ack1 = ack1;
	}
	public String getAck2() {
		return ack2;
	}
	public void setAck2(String ack2) {
		this.ack2 = ack2;
	}
	public String getAck3() {
		return ack3;
	}
	public void setAck3(String ack3) {
		this.ack3 = ack3;
	}
	*/
	public int getDemographicNo() {
		return demographicNo;
	}
	public void setDemographicNo(int demographicNo) {
		this.demographicNo = demographicNo;
	}
	public String getUrgency() {
		return urgency;
	}
	public void setUrgency(String urgency) {
		this.urgency = urgency;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	
	
	
}
