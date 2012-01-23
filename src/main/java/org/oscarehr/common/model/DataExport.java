package org.oscarehr.common.model;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Timestamp;


/**
 * The persistent class for the dataExport database table.
 * 
 */
@Entity
@Table(name="dataExport")
public class DataExport extends AbstractModel<Integer> implements Serializable, Comparable<DataExport> {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Timestamp daterun;
	private String file;
	private String type;
	private String user;
	private String contactLName;
	private String contactFName;
	private String contactEmail;
	private String contactPhone;

    public DataExport() {
    }


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Override
	public Integer getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public Timestamp getDaterun() {
		return this.daterun;
	}

	public void setDaterun(Timestamp daterun) {
		this.daterun = daterun;
	}


	public String getFile() {
		return this.file;
	}

	public void setFile(String file) {
		this.file = file;
	}


	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}


	public String getUser() {
		return this.user;
	}

	public void setUser(String user) {
		this.user = user;
	}


	public String getContactLName() {
    	return contactLName;
    }


	public void setContactLName(String contactLName) {
    	this.contactLName = contactLName;
    }


	public String getContactFName() {
    	return contactFName;
    }


	public void setContactFName(String contactFName) {
    	this.contactFName = contactFName;
    }


	public String getContactEmail() {
    	return contactEmail;
    }


	public void setContactEmail(String contactEmail) {
    	this.contactEmail = contactEmail;
    }


	public String getContactPhone() {
    	return contactPhone;
    }


	public void setContactPhone(String contactPhone) {
    	this.contactPhone = contactPhone;
    }


	@Override
    public int compareTo(DataExport o) {
	    return this.getDaterun().compareTo(o.getDaterun());	    
    }

}