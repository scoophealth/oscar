package org.oscarehr.ws.rest.to.model;

public class ProviderApptsCountTo {
	private String providerNo;
	private String providerName;
	private Long appointmentCount = 0L;
	
	public ProviderApptsCountTo() {
		super();
	}

	public ProviderApptsCountTo(String providerNo, String providerName, Long appointmentCount) {
		super();
		this.providerNo = providerNo;
		this.providerName = providerName;
		this.appointmentCount = appointmentCount;
	}

	public String getProviderNo() {
		return providerNo;
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public Long getAppointmentCount() {
		return appointmentCount;
	}

	public void setAppointmentCount(Long appointmentCount) {
		this.appointmentCount = appointmentCount;
	}

	@Override
	public String toString() {
		return "ApptProviderCountTo [providerNo=" + providerNo + ", appointmentCount=" + appointmentCount + "]";
	}
	
}
