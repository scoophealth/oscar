package org.oscarehr.common.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "secRole")
public class SecRole extends AbstractModel<Integer> implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "role_no")
	private Integer id;
	@Column(name = "role_name")
	private String name;
	private String description;

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean equals(Object o) {
		try {
			SecRole sc1 = (SecRole) o;
			return (sc1.id.equals(id));
		} catch (Exception e) {
			// do nothing let it fall through.
		}

		return (false);
	}

	public int hashCode() {
		return (id != null ? id.hashCode() : 0);
	}

}
