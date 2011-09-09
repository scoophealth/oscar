package org.oscarehr.common.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

@Entity
@Table(name = "property")
public class Property extends AbstractModel<Integer> implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String value;
	
    @Column(name = "provider_no")
	private String providerNo;

	@Override
	public Integer getId() {
		return id;
	}

	public String getName() {
		return (name);
	}

	public void setName(String name) {
		this.name = StringUtils.trimToNull(name);
	}

	public String getValue() {
		return (value);
	}

	public void setValue(String value) {
		this.value = StringUtils.trimToNull(value);
	}

	public String getProviderNo() {
		return (providerNo);
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = StringUtils.trimToNull(providerNo);
	}

}
