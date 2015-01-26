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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.oscarehr.common.model.Prevention;
import org.oscarehr.common.model.PreventionExt;
import org.oscarehr.managers.PreventionManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

public final class PreventionTransfer {

	private Integer Id;
	private Integer demographicId;
	private Date creationDate;
	private Date preventionDate;
	private String providerNo;
	private String preventionType;
	private boolean deleted;
	private boolean refused;
	private boolean never;
	private Date nextDate;
	private String creatorProviderNo;
	private Date lastUpdateDate;
	
	private PreventionExtTransfer[] preventionExts;

	public Integer getDemographicId() {
		return (demographicId);
	}

	public void setDemographicId(Integer demographicId) {
		this.demographicId = demographicId;
	}

	public Date getCreationDate() {
		return (creationDate);
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Integer getId() {
		return (Id);
	}

	public void setId(Integer id) {
		Id = id;
	}

	public String getProviderNo() {
		return (providerNo);
	}

	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}

	public Date getPreventionDate() {
		return (preventionDate);
	}

	public void setPreventionDate(Date preventionDate) {
		this.preventionDate = preventionDate;
	}

	public String getPreventionType() {
		return (preventionType);
	}

	public void setPreventionType(String preventionType) {
		this.preventionType = preventionType;
	}

	public boolean isDeleted() {
		return (deleted);
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean isRefused() {
		return (refused);
	}

	public void setRefused(boolean refused) {
		this.refused = refused;
	}

	public boolean isNever() {
		return (never);
	}

	public void setNever(boolean never) {
		this.never = never;
	}

	public Date getNextDate() {
		return (nextDate);
	}

	public void setNextDate(Date nextDate) {
		this.nextDate = nextDate;
	}

	public String getCreatorProviderNo() {
		return (creatorProviderNo);
	}

	public void setCreatorProviderNo(String creatorProviderNo) {
		this.creatorProviderNo = creatorProviderNo;
	}

	public Date getLastUpdateDate() {
		return (lastUpdateDate);
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public PreventionExtTransfer[] getPreventionExts() {
		return (preventionExts);
	}

	public void setPreventionExts(PreventionExtTransfer[] preventionExts) {
		this.preventionExts = preventionExts;
	}

	/**
	 * both preventionsExts are required, null is not allowed, pass in empty list for no ext's
	 */
	public static PreventionTransfer toTransfer(Prevention prevention, List<PreventionExt> preventionExts) {
		if (prevention == null) return (null);

		PreventionTransfer preventionTransfer = new PreventionTransfer();

		preventionTransfer.setCreationDate(prevention.getCreationDate());
		preventionTransfer.setCreatorProviderNo(prevention.getCreatorProviderNo());
		preventionTransfer.setDeleted(prevention.isDeleted());
		preventionTransfer.setDemographicId(prevention.getDemographicId());
		preventionTransfer.setId(prevention.getId());
		preventionTransfer.setLastUpdateDate(prevention.getLastUpdateDate());
		preventionTransfer.setNever(prevention.isNever());
		preventionTransfer.setNextDate(prevention.getNextDate());
		preventionTransfer.setPreventionDate(prevention.getPreventionDate());
		preventionTransfer.setPreventionType(prevention.getPreventionType());
		preventionTransfer.setProviderNo(prevention.getProviderNo());
		preventionTransfer.setRefused(prevention.isRefused());
		
		preventionTransfer.setPreventionExts(PreventionExtTransfer.toTransfers(preventionExts));

		return (preventionTransfer);
	}

	public static PreventionTransfer[] getTransfers(LoggedInInfo loggedInInfo, List<Prevention> preventions)
	{
		ArrayList<PreventionTransfer> results=new ArrayList<PreventionTransfer>();
		PreventionManager preventionManager=SpringUtils.getBean(PreventionManager.class);
		
		for (Prevention prevention : preventions)
		{
			List<PreventionExt> preventionExts = preventionManager.getPreventionExtByPrevention(loggedInInfo,prevention.getId());
			PreventionTransfer preventionTransfer=PreventionTransfer.toTransfer(prevention, preventionExts);
			results.add(preventionTransfer);
		}
		
		return(results.toArray(new PreventionTransfer[0]));
	}
	
	@Override
	public String toString() {
		return (ReflectionToStringBuilder.toString(this));
	}
}
