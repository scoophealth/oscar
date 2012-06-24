/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.PMmodule.model;

import java.io.Serializable;
import java.util.Date;

import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Provider;

/**
 * Service restriction
 */
public class ProgramClientRestriction implements Serializable {

    private Integer id;
    private int programId;
    private int demographicNo;
    private String providerNo;
    private String commentId;
    private String comments;
    private Date startDate;
    private Date endDate;
    private boolean enabled;
    private String earlyTerminationProvider;
    
    private Program program;
    private Demographic client;
    private Provider provider;

    public ProgramClientRestriction() {
    	id = 0;
    }

    public ProgramClientRestriction(Integer id, int programId, int demographicNo, String providerNo, String comments, Date startDate, Date endDate, boolean enabled, Program program, Demographic client) {
        this.id = id;
        this.programId = programId;
        this.demographicNo = demographicNo;
        this.providerNo = providerNo;
        this.comments = comments;
        this.startDate = startDate;
        this.endDate = endDate;
        this.enabled = enabled;
        this.program = program;
        this.client = client;
    }

    public String getProviderNo() {
        return providerNo;
    }

    public long getDaysRemaining() {
        return (this.getEndDate().getTime() - this.getStartDate().getTime()) / 1000 / 60 / 60 / 24;
    }

    public void setProviderNo(String providerNo) {
        this.providerNo = providerNo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getProgramId() {
        return programId;
    }

    public void setProgramId(int programId) {
        this.programId = programId;
    }

    public int getDemographicNo() {
        return demographicNo;
    }

    public void setDemographicNo(int demographicNo) {
        this.demographicNo = demographicNo;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public Demographic getClient() {
        return client;
    }

    public void setClient(Demographic client) {
        this.client = client;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProgramClientRestriction that = (ProgramClientRestriction) o;

        if (id != that.id) return false;

        return true;
    }

    public int hashCode() {   
        return (id ^ (id >>> 32));
    }

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

    public String getEarlyTerminationProvider() {
        return earlyTerminationProvider;
    }

    public void setEarlyTerminationProvider(String earlyTerminationProvider) {
        this.earlyTerminationProvider = earlyTerminationProvider;
    }

    

}
