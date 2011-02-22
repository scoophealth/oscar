package org.oscarehr.common.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="appointmentArchive")
public class AppointmentArchive extends AbstractModel<Integer>  {
	
	@Id
	@Column(name="appointment_no")
	private Integer id;
	
	@Column(name="provider_no")
	private String providerNo;
	
	@Temporal(TemporalType.DATE)
	@Column(name="appointment_date")
	private Date appointmentDate;
	
	@Temporal(TemporalType.TIME)
	@Column(name="start_time")
	private Date startTime;
	
	@Temporal(TemporalType.TIME)
	@Column(name="end_time")
	private Date endTime;
	
	private String name;
	
	@Column(name="demographic_no")
	private int demographicNo;
	
	@Column(name="program_id")
	private int programId;
	
	private String notes;
	private String reason;
	private String location;
	private String resources;
	private String type;
	private String style;
	private String billing;
	private String status;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="createdatetime")
	private Date createDateTime = new Date();
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updatedatetime")
	private Date updateDateTime = new Date();
	
	private String creator;
	
	@Column(name="lastupdateuser")
	private String lastUpdateUser;
	
	private String remarks;
	
	
	
	
	public String getProviderNo() {
		return providerNo;
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	public Date getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(Date appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDemographicNo() {
		return demographicNo;
	}

	public void setDemographicNo(int demographicNo) {
		this.demographicNo = demographicNo;
	}

	public int getProgramId() {
		return programId;
	}

	public void setProgramId(int programId) {
		this.programId = programId;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getResources() {
		return resources;
	}

	public void setResources(String resources) {
		this.resources = resources;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getBilling() {
		return billing;
	}

	public void setBilling(String billing) {
		this.billing = billing;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(Date createDateTime) {
		this.createDateTime = createDateTime;
	}

	public Date getUpdateDateTime() {
		return updateDateTime;
	}

	public void setUpdateDateTime(Date updateDateTime) {
		this.updateDateTime = updateDateTime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getLastUpdateUser() {
		return lastUpdateUser;
	}

	public void setLastUpdateUser(String lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public AppointmentArchive() {
		//blank
	}	
	
	public AppointmentArchive(Integer id) {
		this.id = id;
	}

	@Override
    public Integer getId() {
		return id;
	}
	
	@PreUpdate
	protected void jpaUpdateLastUpdateTime() {
		this.updateDateTime = new Date();
	}

}
