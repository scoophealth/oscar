package org.oscarehr.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="mdsZCL")
public class MdsZCL extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="segmentID")
	private Integer id;

	private String setId;

	private String consultDoc;

	private String clientAddress;

	private String route;

	private String stop;

	private String area;

	private String reportSet;

	private String clientType;

	private String clientModemPool;

	private String clientFaxNumber;

	private String clientBakFax;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getSetId() {
    	return setId;
    }

	public void setSetId(String setId) {
    	this.setId = setId;
    }

	public String getConsultDoc() {
    	return consultDoc;
    }

	public void setConsultDoc(String consultDoc) {
    	this.consultDoc = consultDoc;
    }

	public String getClientAddress() {
    	return clientAddress;
    }

	public void setClientAddress(String clientAddress) {
    	this.clientAddress = clientAddress;
    }

	public String getRoute() {
    	return route;
    }

	public void setRoute(String route) {
    	this.route = route;
    }

	public String getStop() {
    	return stop;
    }

	public void setStop(String stop) {
    	this.stop = stop;
    }

	public String getArea() {
    	return area;
    }

	public void setArea(String area) {
    	this.area = area;
    }

	public String getReportSet() {
    	return reportSet;
    }

	public void setReportSet(String reportSet) {
    	this.reportSet = reportSet;
    }

	public String getClientType() {
    	return clientType;
    }

	public void setClientType(String clientType) {
    	this.clientType = clientType;
    }

	public String getClientModemPool() {
    	return clientModemPool;
    }

	public void setClientModemPool(String clientModemPool) {
    	this.clientModemPool = clientModemPool;
    }

	public String getClientFaxNumber() {
    	return clientFaxNumber;
    }

	public void setClientFaxNumber(String clientFaxNumber) {
    	this.clientFaxNumber = clientFaxNumber;
    }

	public String getClientBakFax() {
    	return clientBakFax;
    }

	public void setClientBakFax(String clientBakFax) {
    	this.clientBakFax = clientBakFax;
    }


}
