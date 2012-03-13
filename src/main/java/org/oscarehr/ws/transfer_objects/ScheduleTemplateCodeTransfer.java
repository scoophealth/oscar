package org.oscarehr.ws.transfer_objects;

import java.util.List;

import org.oscarehr.common.model.ScheduleTemplateCode;
import org.springframework.beans.BeanUtils;

public final class ScheduleTemplateCodeTransfer {

	private Integer id;
	private Character code;
	private String description;
	private String duration;
	private String color;
	private String confirm;
	private int bookinglimit;

	public Integer getId() {
		return (id);
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Character getCode() {
		return (code);
	}

	public void setCode(Character code) {
		this.code = code;
	}

	public String getDescription() {
		return (description);
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDuration() {
		return (duration);
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getColor() {
		return (color);
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getConfirm() {
		return (confirm);
	}

	public void setConfirm(String confirm) {
		this.confirm = confirm;
	}

	public int getBookinglimit() {
		return (bookinglimit);
	}

	public void setBookinglimit(int bookinglimit) {
		this.bookinglimit = bookinglimit;
	}

	public static ScheduleTemplateCodeTransfer toTransfer(ScheduleTemplateCode scheduleTemplateCode) {
		ScheduleTemplateCodeTransfer scheduleTemplateCodeTransfer = new ScheduleTemplateCodeTransfer();

		BeanUtils.copyProperties(scheduleTemplateCode, scheduleTemplateCodeTransfer);

		return (scheduleTemplateCodeTransfer);
	}

	public static ScheduleTemplateCodeTransfer[] toTransfer(List<ScheduleTemplateCode> scheduleTemplateCodes) {
		ScheduleTemplateCodeTransfer[] result = new ScheduleTemplateCodeTransfer[scheduleTemplateCodes.size()];

		for (int i = 0; i < scheduleTemplateCodes.size(); i++) {
			result[i] = toTransfer(scheduleTemplateCodes.get(i));
		}

		return (result);
	}
}
