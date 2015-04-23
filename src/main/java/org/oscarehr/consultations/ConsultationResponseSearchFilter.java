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
package org.oscarehr.consultations;

import java.util.Date;

public class ConsultationResponseSearchFilter {

	public static enum SORTMODE {
        Demographic, ReferringDoctor, Team, Status, Provider, AppointmentDate, FollowUpDate ,ReferralDate, ResponseDate, Urgency
	}
	
	public static enum SORTDIR {
        asc,desc
	}
	
	private Integer id;
	private Date referralStartDate;
	private Date referralEndDate;
	private Date responseStartDate;
	private Date responseEndDate;
	private Integer status;
	private String team;
	private Date appointmentStartDate;
	private Date appointmentEndDate;
	private Integer demographicNo;
	private Integer mrpNo;
	private String urgency;
	private SORTMODE sortMode = SORTMODE.ReferralDate;
	private SORTDIR sortDir = SORTDIR.desc;
	private int startIndex;
	private int numToReturn;

	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getReferralStartDate() {
		return referralStartDate;
	}
	public void setReferralStartDate(Date referralStartDate) {
		this.referralStartDate = referralStartDate;
	}
	public Date getReferralEndDate() {
		return referralEndDate;
	}
	public void setReferralEndDate(Date referralEndDate) {
		this.referralEndDate = referralEndDate;
	}
	public Date getResponseStartDate() {
		return responseStartDate;
	}
	public void setResponseStartDate(Date responseStartDate) {
		this.responseStartDate = responseStartDate;
	}
	public Date getResponseEndDate() {
		return responseEndDate;
	}
	public void setResponseEndDate(Date responseEndDate) {
		this.responseEndDate = responseEndDate;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getTeam() {
		return team;
	}
	public void setTeam(String team) {
		this.team = team;
	}
	public Date getAppointmentStartDate() {
		return appointmentStartDate;
	}
	public void setAppointmentStartDate(Date appointmentStartDate) {
		this.appointmentStartDate = appointmentStartDate;
	}
	public Date getAppointmentEndDate() {
		return appointmentEndDate;
	}
	public void setAppointmentEndDate(Date appointmentEndDate) {
		this.appointmentEndDate = appointmentEndDate;
	}
	public Integer getDemographicNo() {
		return demographicNo;
	}
	public void setDemographicNo(Integer demographicNo) {
		this.demographicNo = demographicNo;
	}
	public Integer getMrpNo() {
		return mrpNo;
	}
	public void setMrpNo(Integer mrpNo) {
		this.mrpNo = mrpNo;
	}
	public String getUrgency() {
		return urgency;
	}
	public void setUrgency(String urgency) {
		this.urgency = urgency;
	}
	public SORTMODE getSortMode() {
		return sortMode;
	}
	public void setSortMode(SORTMODE sortMode) {
		this.sortMode = sortMode;
	}
	public SORTDIR getSortDir() {
		return sortDir;
	}
	public void setSortDir(SORTDIR sortDir) {
		this.sortDir = sortDir;
	}
	public int getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	public int getNumToReturn() {
		return numToReturn;
	}
	public void setNumToReturn(int numToReturn) {
		this.numToReturn = numToReturn;
	}
}
