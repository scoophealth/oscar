package org.oscarehr.casemgmt.model;

import java.sql.Blob;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.caisi.model.BaseObject;

public class ClientImage extends BaseObject {
	private Long id;
	private long demographic_no;
	private String image_type;
	private byte[] image_data;
	private Date update_date;
	
	public ClientImage() {
		update_date = new Date();
	}

	public long getDemographic_no() {
		return demographic_no;
	}

	public void setDemographic_no(long demographic_no) {
		this.demographic_no = demographic_no;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public byte[] getImage_data() {
		return image_data;
	}

	public void setImage_data(byte[] image_data) {
		this.image_data = image_data;
	}

	public String getImage_type() {
		return image_type;
	}

	public void setImage_type(String image_type) {
		this.image_type = image_type;
	}

	public Date getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}

	public String getImage_data2() {
		if(getImage_data() != null) {
			String encoded = new String(Base64.encodeBase64(getImage_data()));
			return encoded;
		}
		return null;
	}

	public void setImage_data2(String image_data) {
		if(image_data != null) {
			byte[] decoded = Base64.decodeBase64(image_data.getBytes());
			setImage_data(decoded);
		}
	}

}
