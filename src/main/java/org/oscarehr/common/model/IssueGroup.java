package org.oscarehr.common.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.commons.lang.StringUtils;

@Entity
public class IssueGroup extends AbstractModel<Integer> implements Comparable<IssueGroup>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id=null;
	private String name=null;

	@Override
    public Integer getId() {
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
