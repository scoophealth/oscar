package org.oscarehr.common.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;


/**
 * The persistent class for the cssStyles database table.
 * 
 */
@Entity
@Table(name="cssStyles")
public class CssStyle extends AbstractModel<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String ACTIVE = "A";
	public static final String DELETED = "D";
	private int id;
	private String name;
	private String style;
	private String status;

    public CssStyle() {
    }


	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique=true, nullable=false)
	public Integer getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}


	@Column(length=255)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}


    @Lob()
	public String getStyle() {
		return this.style;
	}

	public void setStyle(String style) {
		this.style = style;
	}


	public String getStatus() {
	    return status;
    }


	public void setStatus(String status) {
	    this.status = status;
    }

}