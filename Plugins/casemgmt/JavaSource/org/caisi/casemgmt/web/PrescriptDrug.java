package org.caisi.casemgmt.web;

import java.util.Date;

public class PrescriptDrug
{
	private Date date_prescribed;
	private String drug_special;
	private Date end_date;
	private boolean isExpired;
	private boolean drug_achived;
	private String BN;
	private Integer GCN_SEQNO;
	private String customName;
	
	public Date getDate_prescribed()
	{
		return date_prescribed;
	}
	public void setDate_prescribed(Date date_prescribed)
	{
		this.date_prescribed = date_prescribed;
	}
	public String getDrug_special()
	{
		return drug_special;
	}
	public void setDrug_special(String drug_special)
	{
		this.drug_special = drug_special;
	}
	public boolean isExpired()
	{
		return isExpired;
	}
	public void setExpired(boolean isExpired)
	{
		this.isExpired = isExpired;
	}
	public Date getEnd_date()
	{
		return end_date;
	}
	public void setEnd_date(Date end_date)
	{
		this.end_date = end_date;
	}
	public boolean isDrug_achived() {
		return drug_achived;
	}
	public void setDrug_achived(boolean drug_achived) {
		this.drug_achived = drug_achived;
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
