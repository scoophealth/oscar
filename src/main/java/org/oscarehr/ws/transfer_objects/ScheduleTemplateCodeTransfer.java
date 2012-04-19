/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


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
		if (scheduleTemplateCode==null) return(null);
		
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
