package org.oscarehr.ws.rest.to.model;
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
import java.util.Arrays;

import org.oscarehr.appointment.search.SearchConfig;

public class BookingScheduleTemplateCodeTransfer {
	
	private Integer id;
	private Character code;
	private String description;
	private String duration;
	private String color;
	private String confirm;
	private int bookinglimit;
	private boolean onlineBooking;
	private boolean openAccess;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Character getCode() {
		return code;
	}
	public void setCode(Character code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getConfirm() {
		return confirm;
	}
	public void setConfirm(String confirm) {
		this.confirm = confirm;
	}
	public int getBookinglimit() {
		return bookinglimit;
	}
	public void setBookinglimit(int bookinglimit) {
		this.bookinglimit = bookinglimit;
	}
	public boolean isOnlineBooking() {
		return onlineBooking;
	}
	public void setOnlineBooking(boolean onlineBooking) {
		this.onlineBooking = onlineBooking;
	}
	public boolean isOpenAccess() {
		return openAccess;
	}
	public void setOpenAccess(boolean openAccess) {
		this.openAccess = openAccess;
	}
	public static BookingScheduleTemplateCodeTransfer getFromTransfer(BookingScheduleTemplateCodeTransfer appointmentCodeTransfer, SearchConfig clinic) {
		BookingScheduleTemplateCodeTransfer retval = new BookingScheduleTemplateCodeTransfer();
		
		retval.code = (char) appointmentCodeTransfer.getCode();//.intValue();
		retval.description = appointmentCodeTransfer.getDescription();
		retval.duration = appointmentCodeTransfer.getDuration();
		retval.color = appointmentCodeTransfer.getColor();
		
		if(clinic != null && clinic.getAppointmentCodeDurations().containsKey(retval.code)){
			retval.onlineBooking = clinic.getAppointmentCodeDurations().containsKey(retval.code);
			if(clinic.getOpenAccessCodes() != null && Arrays.binarySearch(clinic.getOpenAccessCodes(),retval.code) >=0){
				retval.openAccess = true;
			}
		}				
		
		return retval;
	}
}
