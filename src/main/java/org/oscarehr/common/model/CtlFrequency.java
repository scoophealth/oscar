package org.oscarehr.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ctl_frequency")
public class CtlFrequency extends AbstractModel<Integer>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="freqid")
	private Integer id;

	@Column(name="freqcode")
	private String freqCode;

	@Column(name="dailymin")
	private String dailyMin;

	@Column(name="dailymax")
	private String dailyMax;

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getFreqCode() {
    	return freqCode;
    }

	public void setFreqCode(String freqCode) {
    	this.freqCode = freqCode;
    }

	public String getDailyMin() {
    	return dailyMin;
    }

	public void setDailyMin(String dailyMin) {
    	this.dailyMin = dailyMin;
    }

	public String getDailyMax() {
    	return dailyMax;
    }

	public void setDailyMax(String dailyMax) {
    	this.dailyMax = dailyMax;
    }


}
