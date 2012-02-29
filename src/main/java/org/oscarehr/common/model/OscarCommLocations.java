package org.oscarehr.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="oscarcommlocations")
public class OscarCommLocations extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="locationId")
	private Integer id;

	private String locationDesc;

	private String locationAuth;

	private int current1;

	private String addressBook;

	private String remoteServerURL;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getLocationDesc() {
    	return locationDesc;
    }

	public void setLocationDesc(String locationDesc) {
    	this.locationDesc = locationDesc;
    }

	public String getLocationAuth() {
    	return locationAuth;
    }

	public void setLocationAuth(String locationAuth) {
    	this.locationAuth = locationAuth;
    }

	public int getCurrent1() {
    	return current1;
    }

	public void setCurrent1(int current1) {
    	this.current1 = current1;
    }

	public String getAddressBook() {
    	return addressBook;
    }

	public void setAddressBook(String addressBook) {
    	this.addressBook = addressBook;
    }

	public String getRemoteServerURL() {
    	return remoteServerURL;
    }

	public void setRemoteServerURL(String remoteServerURL) {
    	this.remoteServerURL = remoteServerURL;
    }


}
