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

import oscar.util.StringUtils;

@Entity
@Table(name="EyeformTestBook")
public class TestBookRecord extends AbstractModel<Integer> {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;	
	private String testname;
	private String provider;
	@Column(name="appointment_no")
	private int appointmentNo;
	@Transient
	private Demographic demographic;
	@Column(name="demographic_no")
	private int demographicNo;
	@Column(name="eyeform_id")
	private long eyeformId;
	private String eye;
	private String urgency;
	private String comment;
	@Temporal(TemporalType.TIMESTAMP)
	private Date date = new Date();
	
	
	
	public int getDemographicNo() {
		return demographicNo;
	}
	public void setDemographicNo(int demographicNo) {
		this.demographicNo = demographicNo;
	}
	public Demographic getDemographic() {
		return demographic;
	}
	public void setDemographic(Demographic demographic) {
		this.demographic = demographic;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTestname() {
		return testname;
	}
	public void setTestname(String testname) {
		this.testname = testname;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	
	public int getAppointmentNo() {
		return appointmentNo;
	}
	public void setAppointmentNo(int appointmentNo) {
		this.appointmentNo = appointmentNo;
	}
	
	public long getEyeformId() {
		return eyeformId;
	}
	public void setEyeformId(long eyeformId) {
		this.eyeformId = eyeformId;
	}
	public String getEye() {
		return eye;
	}
	public void setEye(String eye) {
		this.eye = eye;
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
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getCommentStr() {
		return StringUtils.maxLenString(getComment(), 23, 20, "...");  
	}
}
