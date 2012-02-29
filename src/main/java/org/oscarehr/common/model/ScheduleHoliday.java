package org.oscarehr.common.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="scheduleholiday")
public class ScheduleHoliday extends AbstractModel<Date>{

	@Id
	@Column(name="sdate")
	private Date id;

	@Column(name="holiday_name")
	private String holidayName;

	public Date getId() {
    	return id;
    }

	public void setId(Date id) {
    	this.id = id;
    }

	public String getHolidayName() {
    	return holidayName;
    }

	public void setHolidayName(String holidayName) {
    	this.holidayName = holidayName;
    }


}
