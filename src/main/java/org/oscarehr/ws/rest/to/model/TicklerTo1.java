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
package org.oscarehr.ws.rest.to.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.oscarehr.common.model.Tickler;

@XmlRootElement(name="tickler")
public class TicklerTo1 implements Serializable {

    private static final long serialVersionUID = 1L;
    
	public static enum STATUS {
        A, C, D
	}
	
	
	private Integer id;
	
	private Integer demographicNo;
	
	private Integer programId;
	
	private String message;
	
	private Tickler.STATUS status;
	
	private Date updateDate ;
	
	private Date serviceDate;
	
	private String creator;
	
	private Tickler.PRIORITY priority ;
	
	private String taskAssignedTo;

	//private Set<TicklerUpdate> updates = new HashSet<TicklerUpdate>();
	
	//private Set<TicklerComment> comments = new HashSet<TicklerComment>();
	
	private String demographicName;
	  
	private String creatorName;
	   
	private String taskAssignedToName;
	   
	private String statusName;
	   
	private List<TicklerLinkTo1> ticklerLinks = new ArrayList<TicklerLinkTo1>();
	
	private List<TicklerUpdateTo1> ticklerUpdates = new ArrayList<TicklerUpdateTo1>();
	
	private List<TicklerCommentTo1> ticklerComments = new ArrayList<TicklerCommentTo1>();
	
	private ProgramTo1 program;
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDemographicNo() {
		return demographicNo;
	}

	public void setDemographicNo(Integer demographicNo) {
		this.demographicNo = demographicNo;
	}

	public Integer getProgramId() {
		return programId;
	}

	public void setProgramId(Integer programId) {
		this.programId = programId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Tickler.STATUS getStatus() {
		return status;
	}

	public void setStatus(Tickler.STATUS status) {
		this.status = status;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Date getServiceDate() {
		return serviceDate;
	}

	public void setServiceDate(Date serviceDate) {
		this.serviceDate = serviceDate;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Tickler.PRIORITY getPriority() {
		return priority;
	}

	public void setPriority(Tickler.PRIORITY priority) {
		this.priority = priority;
	}

	public String getTaskAssignedTo() {
		return taskAssignedTo;
	}

	public void setTaskAssignedTo(String taskAssignedTo) {
		this.taskAssignedTo = taskAssignedTo;
	}

	public String getDemographicName() {
		return demographicName;
	}

	public void setDemographicName(String demographicName) {
		this.demographicName = demographicName;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getTaskAssignedToName() {
		return taskAssignedToName;
	}

	public void setTaskAssignedToName(String taskAssignedToName) {
		this.taskAssignedToName = taskAssignedToName;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public List<TicklerLinkTo1> getTicklerLinks() {
		return ticklerLinks;
	}

	public void setTicklerLinks(List<TicklerLinkTo1> ticklerLinks) {
		this.ticklerLinks = ticklerLinks;
	}

	public List<TicklerUpdateTo1> getTicklerUpdates() {
		return ticklerUpdates;
	}

	public void setTicklerUpdates(List<TicklerUpdateTo1> ticklerUpdates) {
		this.ticklerUpdates = ticklerUpdates;
	}

	public List<TicklerCommentTo1> getTicklerComments() {
		return ticklerComments;
	}

	public void setTicklerComments(List<TicklerCommentTo1> ticklerComments) {
		this.ticklerComments = ticklerComments;
	}

	public ProgramTo1 getProgram() {
		return program;
	}

	public void setProgram(ProgramTo1 program) {
		this.program = program;
	}
	
	
}
