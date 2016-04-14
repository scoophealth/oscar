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

package org.oscarehr.PMmodule.web.formbean;

import org.apache.struts.action.ActionForm;
import org.oscarehr.PMmodule.model.ProgramClientRestriction;
import org.oscarehr.common.model.Bed;

public class ProgramManagerViewFormBean extends ActionForm {

	public static final String[] tabs = { "General", "Staff", "Teams", "Clients", "Queue", "Access", "Bed Check" , "Client Status", "Service Restrictions", "Vacancies","Schedule","Encounter Types"};

	private String tab;
	private String subtab;
	private String clientId;
	private String queueId;
	private Bed[] reservedBeds;
	private String switchBed1;
	private String switchBed2;
	private String vacancyOrTemplateId;
	
	private String radioRejectionReason;
    private ProgramClientRestriction serviceRestriction;

    public String getRadioRejectionReason() {
		return radioRejectionReason;
	}

	public void setRadioRejectionReason(String radioRejectionReason) {
		this.radioRejectionReason = radioRejectionReason;
	}

    /**
	 * @return Returns the tab.
	 */
	public String getTab() {
		return tab;
	}
	
	/**
	 * @param tab
	 *            The tab to set.
	 */
	public void setTab(String tab) {
		this.tab = tab;
	}

	/**
     * @return the subtab
     */
    public String getSubtab() {
    	return subtab;
    }

	/**
     * @param subtab the subtab to set
     */
    public void setSubtab(String subtab) {
    	this.subtab = subtab;
    }

	/**
	 * @return Returns the clientId.
	 */
	public String getClientId() {
		return clientId;
	}
	/**
	 * @param clientId
	 *            The clientId to set.
	 */
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getQueueId() {
		return queueId;
	}

	public void setQueueId(String queueId) {
		this.queueId = queueId;
	}

	public Bed[] getReservedBeds() {
    	return reservedBeds;
    }

	public void setReservedBeds(Bed[] reservedBeds) {
    	this.reservedBeds = reservedBeds;
    }

    public ProgramClientRestriction getServiceRestriction() {
        return serviceRestriction;
    }

    public void setServiceRestriction(ProgramClientRestriction serviceRestriction) {
        this.serviceRestriction = serviceRestriction;
    }

	public String getSwitchBed1() {
		return switchBed1;
	}

	public void setSwitchBed1(String switchBed1) {
		this.switchBed1 = switchBed1;
	}

	public String getSwitchBed2() {
		return switchBed2;
	}

	public void setSwitchBed2(String switchBed2) {
		this.switchBed2 = switchBed2;
	}

	public String getVacancyOrTemplateId() {
    	return vacancyOrTemplateId;
    }

	public void setVacancyOrTemplateId(String vacancyOrTemplateId) {
    	this.vacancyOrTemplateId = vacancyOrTemplateId;
    }
	
}
