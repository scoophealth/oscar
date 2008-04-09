package org.oscarehr.common.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.commons.lang.StringUtils;

@Entity
public class IssueGroup implements Comparable<IssueGroup>{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private int id=0;
	private String name=null;

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		name=StringUtils.trimToNull(name);
		if (name==null) throw(new IllegalArgumentException("Can not be null."));
		this.name = name;
	}

	public int compareTo(IssueGroup o) {
	   return(name.compareTo(o.name));
    }
}
