package org.oscarehr.common.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="scheduletemplate")
public class ScheduleTemplate extends AbstractModel<ScheduleTemplatePrimaryKey> {

	@EmbeddedId     
	private ScheduleTemplatePrimaryKey id;
	private String summary;
	private String timecode;
	
	public ScheduleTemplatePrimaryKey getId() {
		return id;
	}

	public String getSummary() {
    	return summary;
    }

	public void setSummary(String summary) {
    	this.summary = summary;
    }

	public String getTimecode() {
    	return timecode;
    }

	public void setTimecode(String timecode) {
    	this.timecode = timecode;
    }
	
	
	
}
