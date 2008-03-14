/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/

package org.oscarehr.PMmodule.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oscarehr.PMmodule.dao.BedDAO;
import org.oscarehr.PMmodule.dao.BedDemographicDAO;
import org.oscarehr.PMmodule.dao.ClientDao;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.PMmodule.dao.RoomDAO;
import org.oscarehr.PMmodule.model.Bed;
import org.oscarehr.PMmodule.model.BedDemographic;
import org.oscarehr.PMmodule.model.BedDemographicHistorical;
import org.oscarehr.PMmodule.model.BedDemographicHistoricalPK;
import org.oscarehr.PMmodule.model.BedDemographicStatus;
import org.oscarehr.PMmodule.model.Demographic;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.Room;
import org.oscarehr.PMmodule.service.BedDemographicManager;
import org.oscarehr.PMmodule.utility.DateTimeFormatUtils;

/**
 * Implementation of BedDemographicManager interface
 */
public class BedDemographicManagerImpl implements BedDemographicManager {

	private static final Log log = LogFactory.getLog(BedDemographicManagerImpl.class);
	
	private static <T extends Exception> void handleException(T e) throws T {
		log.error(e);
		throw e;
	}

	private BedDemographicDAO bedDemographicDAO;
	private ProviderDao providerDAO;
	private BedDAO bedDAO;
	private ClientDao demographicDAO;
	private RoomDAO roomDAO;
	private ProgramDao programDAO;

	public void setBedDemographicDAO(BedDemographicDAO BedDemographicDAO) {
		this.bedDemographicDAO = BedDemographicDAO;
	}

	public void setProviderDAO(ProviderDao providerDAO) {
		this.providerDAO = providerDAO;
	}

	public void setBedDAO(BedDAO bedDAO) {
		this.bedDAO = bedDAO;
	}

	public void setDemographicDAO(ClientDao demographicDAO) {
		this.demographicDAO = demographicDAO;
	}

	public void setRoomDAO(RoomDAO roomDAO) {
		this.roomDAO = roomDAO;
	}

	public void setProgramDAO(ProgramDao programDAO) {
		this.programDAO = programDAO;
	}

	/**
	 * @see org.oscarehr.PMmodule.service.BedDemographicManager#demographicExists(java.lang.Integer)
	 */
	public boolean demographicExists(Integer bedId) {
		return bedDemographicDAO.demographicExists(bedId);
	}

	/**
	 * @see org.oscarehr.PMmodule.service.BedDemographicManager#getBedDemographicByBed(java.lang.Integer)
	 */
	public BedDemographic getBedDemographicByBed(Integer bedId) {
		if (bedId == null) {
			handleException(new IllegalArgumentException("bedId must not be null"));
		}
		
		BedDemographic bedDemographic = null;

		if (bedDemographicDAO.demographicExists(bedId)) {
			bedDemographic = bedDemographicDAO.getBedDemographicByBed(bedId);
			setAttributes(bedDemographic);

			Demographic demographic = demographicDAO.getClientByDemographicNo(bedDemographic.getId().getDemographicNo());
			bedDemographic.setDemographic(demographic);
		}

		return bedDemographic;
	}

	/**
	 * @see org.oscarehr.PMmodule.service.BedDemographicManager#getBedDemographicByDemographic(java.lang.Integer)
	 */
	public BedDemographic getBedDemographicByDemographic(Integer demographicNo, Integer facilityId) {
		if (demographicNo == null) {
			handleException(new IllegalArgumentException("demographicNo must not be null"));
		}

		BedDemographic bedDemographic = null;

		if (bedDemographicDAO.bedExists(demographicNo)) {
			bedDemographic = bedDemographicDAO.getBedDemographicByDemographic(demographicNo);
			setAttributes(bedDemographic);

			Bed bed = bedDAO.getBed(bedDemographic.getId().getBedId());
			bedDemographic.setBed(bed);

			Room room = roomDAO.getRoom(bed.getRoomId());
			// check for facility filtering
			if (facilityId!=null && room.getFacilityId()!=null && room.getFacilityId().intValue()!=facilityId.intValue()) return(null);
			bed.setRoom(room);

			Integer programId = room.getProgramId();

			if (programId != null) {
				Program program = programDAO.getProgram(programId);
				room.setProgram(program);
			}
		}

		return bedDemographic;
	}

	/**
	 * @see org.oscarehr.PMmodule.service.BedDemographicManager#getDefaultBedDemographicStatus()
	 */
	public BedDemographicStatus getDefaultBedDemographicStatus() {
		for (BedDemographicStatus status : getBedDemographicStatuses()) {
			if (status.isDefault()) {
				return status;
			}
		}

		handleException(new IllegalArgumentException("no default bed demographic status"));
		
		return null;
	}

	/**
	 * @see org.oscarehr.PMmodule.service.BedDemographicManager#getBedDemographicStatuses()
	 */
	public BedDemographicStatus[] getBedDemographicStatuses() {
		return bedDemographicDAO.getBedDemographicStatuses();
	}

	/**
	 * @see org.oscarehr.PMmodule.service.BedDemographicManager#getExpiredReservations()
	 */
	public BedDemographicHistorical[] getExpiredReservations() {
		BedDemographicHistorical[] bedDemographicHistoricals = bedDemographicDAO.getBedDemographicHistoricals(DateTimeFormatUtils.getToday());
		
		for (BedDemographicHistorical historical : bedDemographicHistoricals) {
			BedDemographicHistoricalPK id = historical.getId();
			
			historical.setBed(bedDAO.getBed(id.getBedId()));
			historical.setDemographic(demographicDAO.getClientByDemographicNo(id.getDemographicNo()));
        }
		
		return bedDemographicHistoricals;
	}

	/**
	 * @see org.oscarehr.PMmodule.service.BedDemographicManager#saveBedDemographic(org.oscarehr.PMmodule.model.BedDemographic)
	 */
	public void saveBedDemographic(BedDemographic bedDemographic) {
		if (bedDemographic == null) {
			handleException(new IllegalArgumentException("bedDemographic must not be null"));
		}
		validate(bedDemographic);
		bedDemographicDAO.saveBedDemographic(bedDemographic);
	}

	/**
	 * @see org.oscarehr.PMmodule.service.BedDemographicManager#deleteBedDemographic(BedDemographic)
	 */
	public void deleteBedDemographic(BedDemographic bedDemographic) {
		if (bedDemographic == null) {
			handleException(new IllegalArgumentException("bedDemographic must not be null"));
		}
		
		bedDemographicDAO.deleteBedDemographic(bedDemographic);
	}

	void setAttributes(BedDemographic bedDemographic) {
		Integer bedDemographicStatusId = bedDemographic.getBedDemographicStatusId();
		BedDemographicStatus bedDemographicStatus = bedDemographicDAO.getBedDemographicStatus(bedDemographicStatusId);
		bedDemographic.setBedDemographicStatus(bedDemographicStatus);

		Integer duration = (bedDemographicStatus != null) ? bedDemographicStatus.getDuration() : 0;
		bedDemographic.setReservationEnd(duration);

		String providerNo = bedDemographic.getProviderNo();
		bedDemographic.setProvider(providerDAO.getProvider(providerNo));
	}

	void validate(BedDemographic bedDemographic) {
		validateBedDemographicStatus(bedDemographic.getBedDemographicStatusId());
		validateProvider(bedDemographic.getProviderNo());
		validateBed(bedDemographic.getId().getBedId());
		validateDemographic(bedDemographic.getId().getDemographicNo());
	}

	void validateBedDemographic(BedDemographic bedDemographic) {
		if (!bedDemographic.isValidReservation()) {
			handleException(new IllegalArgumentException("invalid reservation: " + bedDemographic.getReservationStart() + " - " + bedDemographic.getReservationEnd()));
		}
	}

	void validateBedDemographicStatus(Integer bedDemographicStatusId) {
		if (!bedDemographicDAO.bedDemographicStatusExists(bedDemographicStatusId)) {
			handleException(new IllegalArgumentException("no bed demographic status with id : " + bedDemographicStatusId));
		}
	}

	void validateProvider(String providerId) {
		if (!providerDAO.providerExists(providerId)) {
			handleException(new IllegalArgumentException("no provider with id : " + providerId));
		}
	}

	void validateBed(Integer bedId) {
		if (!bedDAO.bedExists(bedId)) {
			handleException(new IllegalArgumentException("no bed with id : " + bedId));
		}
	}

	void validateDemographic(Integer demographicNo) {
		if (!demographicDAO.clientExists(demographicNo)) {
			handleException(new IllegalArgumentException("no demographic with id : " + demographicNo));
		}
	}

}