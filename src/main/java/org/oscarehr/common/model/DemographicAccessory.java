package org.oscarehr.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="demographicaccessory")
public class DemographicAccessory extends AbstractModel<Integer>{

	@Id
	@Column(name="demographic_no")
	private Integer demographicNo;

	private String content;

	public Integer getDemographicNo() {
    	return demographicNo;
    }

	public void setDemographicNo(Integer demographicNo) {
    	this.demographicNo = demographicNo;
    }

	public String getContent() {
    	return content;
    }

	public void setContent(String content) {
    	this.content = content;
    }

	@Override
    public Integer getId() {
	    return getDemographicNo();
    }


}
