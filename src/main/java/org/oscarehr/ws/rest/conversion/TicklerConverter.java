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
package org.oscarehr.ws.rest.conversion;

import java.util.List;

import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.TicklerLinkDao;
import org.oscarehr.common.model.Tickler;
import org.oscarehr.common.model.Tickler.STATUS;
import org.oscarehr.common.model.TicklerComment;
import org.oscarehr.common.model.TicklerLink;
import org.oscarehr.common.model.TicklerUpdate;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.ws.rest.to.model.TicklerCommentTo1;
import org.oscarehr.ws.rest.to.model.TicklerTo1;
import org.oscarehr.ws.rest.to.model.TicklerUpdateTo1;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

//this isn't working - not being injected
@Component
public class TicklerConverter extends AbstractConverter<Tickler, TicklerTo1> {
	
	private boolean includeLinks;
	private boolean includeComments;
	private boolean includeUpdates;
	private boolean includeProgram;
	
	
	@Override
	public Tickler getAsDomainObject(LoggedInInfo loggedInInfo,TicklerTo1 t) throws ConversionException {
		Tickler d = new Tickler();
		d.setCreator(t.getCreator());
		d.setDemographicNo(t.getDemographicNo());
		d.setId(t.getId());
		d.setMessage(t.getMessage());
		d.setPriority(t.getPriority());
		d.setProgramId(t.getProgramId());
		d.setServiceDate(t.getServiceDate());
		d.setStatus(t.getStatus());
		d.setTaskAssignedTo(t.getTaskAssignedTo());
		d.setUpdateDate(t.getUpdateDate());
		
		return d;
	}

	@Override
	public TicklerTo1 getAsTransferObject(LoggedInInfo loggedInInfo,Tickler t) throws ConversionException {
		ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
		DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
		TicklerLinkDao ticklerLinkDao = SpringUtils.getBean(TicklerLinkDao.class);
		ProgramDao programDao = SpringUtils.getBean(ProgramDao.class);
		
		
		TicklerTo1 d = new TicklerTo1();
		
		d.setCreator(t.getCreator());
		d.setDemographicNo(t.getDemographicNo());
		d.setId(t.getId());
		d.setMessage(t.getMessage());
		d.setPriority(t.getPriority());
		d.setProgramId(t.getProgramId());
		d.setServiceDate(t.getServiceDate());
		d.setStatus(t.getStatus());
		d.setTaskAssignedTo(t.getTaskAssignedTo());
		d.setUpdateDate(t.getUpdateDate());
		
		//want responses to include patient name - be nice to have this configurable by a parameter map.
		
		d.setDemographicName(demographicDao.getDemographicById(t.getDemographicNo()).getFormattedName());
		
		d.setTaskAssignedToName(providerDao.getProviderName(t.getTaskAssignedTo()));
		d.setCreatorName(providerDao.getProviderName(t.getCreator()));
		
		if(d.getStatus() == STATUS.A) {
			d.setStatusName("Active");
		}
		if(d.getStatus() == STATUS.C) {
			d.setStatusName("Completed");
		}
		if(d.getStatus() == STATUS.D) {
			d.setStatusName("Deleted");
		}
		
		if(includeLinks) {
			List<TicklerLink> links = ticklerLinkDao.getLinkByTickler(d.getId()); 
			TicklerLinkConverter tlc = new TicklerLinkConverter();
			d.setTicklerLinks(tlc.getAllAsTransferObjects(loggedInInfo,links));
		}
		
		if(includeComments) {
			for(TicklerComment tc : t.getComments()) {
				TicklerCommentTo1 tct = new TicklerCommentTo1();
				tct.setMessage(tc.getMessage());
				tct.setProviderNo(tc.getProviderNo());
				tct.setUpdateDate(tc.getUpdateDate());
				tct.setProviderName(tc.getProvider()!=null?tc.getProvider().getFormattedName():"N/A");
				d.getTicklerComments().add(tct);
			}
		}
		
		if(includeUpdates) {
			for(TicklerUpdate tu : t.getUpdates()) {
				TicklerUpdateTo1 tut = new TicklerUpdateTo1();
				BeanUtils.copyProperties(tu, tut, new String[]{"id","provider"});
				tut.setProviderName(tu.getProvider()!=null?tu.getProvider().getFormattedName():"N/A");
				
				d.getTicklerUpdates().add(tut);
			}
		}
		
		if(includeProgram && t.getProgramId() != null) {
			//TODO: this should go through the manager, but I have no LoggedInInfo. Going to have to change the interface and update all the implementors
			Program p = programDao.getProgram(t.getProgramId());
			if(p != null) {
				d.setProgram(new ProgramConverter().getAsTransferObject(loggedInInfo,p));
			}
		}
		
		return d;
	}

	public boolean isIncludeLinks() {
		return includeLinks;
	}

	public void setIncludeLinks(boolean includeLinks) {
		this.includeLinks = includeLinks;
	}

	public boolean isIncludeComments() {
		return includeComments;
	}

	public void setIncludeComments(boolean includeComments) {
		this.includeComments = includeComments;
	}

	public boolean isIncludeUpdates() {
		return includeUpdates;
	}

	public void setIncludeUpdates(boolean includeUpdates) {
		this.includeUpdates = includeUpdates;
	}

	public boolean isIncludeProgram() {
		return includeProgram;
	}

	public void setIncludeProgram(boolean includeProgram) {
		this.includeProgram = includeProgram;
	}

	
	
}
