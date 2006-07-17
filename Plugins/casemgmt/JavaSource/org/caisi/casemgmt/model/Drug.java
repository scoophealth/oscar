package org.caisi.casemgmt.model;

import java.util.Date;

import org.caisi.model.BaseObject;

public class Drug extends BaseObject {
	private Long id;
	private String provider_no;
	private String demographic_no;
	private Date rx_date;
	private Date end_date;
	private String BN;
	private Integer GCN_SEQNO;
	private String customName;
	private String special;
	private Long script_no;
	private Date create_date;
	private Boolean archived;
	
	
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	public String getDemographic_no() {
		return demographic_no;
	}
	public void setDemographic_no(String demographic_no) {
		this.demographic_no = demographic_no;
	}
	public Date getEnd_date() {
		return end_date;
	}
	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProvider_no() {
		return provider_no;
	}
	public void setProvider_no(String provider_no) {
		this.provider_no = provider_no;
	}
	public Date getRx_date() {
		return rx_date;
	}
	public void setRx_date(Date rx_date) {
		this.rx_date = rx_date;
	}
	public String getSpecial() {
		return special;
	}
	public void setSpecial(String special) {
		this.special = special;
	}
	public Long getScript_no()
	{
		return script_no;
	}
	public void setScript_no(Long script_no)
	{
		this.script_no = script_no;
	}
	public Boolean getArchived() {
		return archived;
	}
	public void setArchived(Boolean archived) {
		this.archived = archived;
	}
	public String getBN() {
		return BN;
	}
	public void setBN(String bn) {
		BN = bn;
	}
	public String getCustomName() {
		return customName;
	}
	public void setCustomName(String customName) {
		this.customName = customName;
	}
	public Integer getGCN_SEQNO() {
		return GCN_SEQNO;
	}
	public void setGCN_SEQNO(Integer gcn_seqno) {
		GCN_SEQNO = gcn_seqno;
	}
	
}
