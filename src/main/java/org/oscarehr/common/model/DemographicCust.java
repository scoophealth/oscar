package org.oscarehr.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="demographiccust")
public class DemographicCust extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="demographic_no")
	private Integer id;
	private String cust1;
	private String cust2;
	private String cust3;
	private String cust4;
	private String content;
	
	public Integer getId() {
		return id;
	}

	public String getCust1() {
		return cust1;
	}

	public void setCust1(String cust1) {
		this.cust1 = cust1;
	}

	public String getCust2() {
		return cust2;
	}

	public void setCust2(String cust2) {
		this.cust2 = cust2;
	}

	public String getCust3() {
		return cust3;
	}

	public void setCust3(String cust3) {
		this.cust3 = cust3;
	}

	public String getCust4() {
		return cust4;
	}

	public void setCust4(String cust4) {
		this.cust4 = cust4;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
}
