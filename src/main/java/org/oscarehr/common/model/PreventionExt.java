package org.oscarehr.common.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "preventionsExt")
public class PreventionExt extends AbstractModel<Integer> implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id = null;

	@Column(name = "prevention_id")
	private Integer preventionId = null;

	private String keyval = null;
	private String val = null;

	
	public Integer getPreventionId() {
		return preventionId;
	}
	
	public void setPreventionId(Integer preventionId) {
		this.preventionId = preventionId;
	}
	
	public String getkeyval() {
		return keyval;
	}
	
	public void setKeyval(String keyval) {
		this.keyval = keyval;
	}
	
	public String getVal() {
		return val;
	}
	
	public void setVal(String val) {
		this.val = val;
	}

	@Override
    public Integer getId() {
		return id;
	}
}
