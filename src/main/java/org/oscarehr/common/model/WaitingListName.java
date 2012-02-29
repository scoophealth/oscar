package org.oscarehr.common.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="waitingListName")
public class WaitingListName extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID")
	private Integer id;

	private String name;

	@Column(name="group_no")
	private String groupNo;

	@Column(name="provider_no")
	private String providerNo;

	@Column(name="create_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;

	@Column(name="is_history")
	private String isHistory;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getName() {
    	return name;
    }

	public void setName(String name) {
    	this.name = name;
    }

	public String getGroupNo() {
    	return groupNo;
    }

	public void setGroupNo(String groupNo) {
    	this.groupNo = groupNo;
    }

	public String getProviderNo() {
    	return providerNo;
    }

	public void setProviderNo(String providerNo) {
    	this.providerNo = providerNo;
    }

	public Date getCreateDate() {
    	return createDate;
    }

	public void setCreateDate(Date createDate) {
    	this.createDate = createDate;
    }

	public String getIsHistory() {
    	return isHistory;
    }

	public void setIsHistory(String isHistory) {
    	this.isHistory = isHistory;
    }


}
