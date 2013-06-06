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
package org.oscarehr.managers;

import java.util.Date;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.common.dao.BedDao;
import org.oscarehr.common.dao.BedDemographicDao;
import org.oscarehr.common.dao.BedDemographicHistoricalDao;
import org.oscarehr.common.dao.BedDemographicStatusDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.RoomDao;
import org.oscarehr.common.model.Bed;
import org.oscarehr.common.model.BedDemographic;
import org.oscarehr.common.model.BedDemographicHistorical;
import org.oscarehr.common.model.BedDemographicHistoricalPK;
import org.oscarehr.common.model.BedDemographicStatus;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Room;
import org.oscarehr.util.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BedDemographicManager {

	private Logger log=MiscUtils.getLogger();
	
	private <T extends Exception> void handleException(T e) throws T {
		log.error("Error", e);
		throw e;
	}

	@Autowired
	private BedDemographicDao bedDemographicDao;
	@Autowired
	private BedDemographicStatusDao bedDemographicStatusDao;
	@Autowired
	private BedDemographicHistoricalDao bedDemographicHistoricalDao;
	@Autowired
	private ProviderDao providerDAO;
	@Autowired
	private BedDao bedDao;
	@Autowired
	private DemographicDao demographicDao;
	@Autowired
	private RoomDao roomDao;
	@Autowired
	private ProgramDao programDao;

	
	/**
	 * @see org.oscarehr.PMmodule.service.BedDemographicManager#demographicExists(java.lang.Integer)
	 */
	public boolean demographicExists(Integer bedId) {
		return bedDemographicDao.demographicExists(bedId);
	}

	/**
	 * @see org.oscarehr.PMmodule.service.BedDemographicManager#getBedDemographicByBed(java.lang.Integer)
	 */
	public BedDemographic getBedDemographicByBed(Integer bedId) {
		if (bedId == null) {
			handleException(new IllegalArgumentException("bedId must not be null"));
		}
		
		BedDemographic bedDemographic = null;

		if (bedDemographicDao.demographicExists(bedId)) {
			bedDemographic = bedDemographicDao.getBedDemographicByBed(bedId);
			setAttributes(bedDemographic);

			Demographic demographic = demographicDao.getClientByDemographicNo(bedDemographic.getId().getDemographicNo());
			bedDemographic.setDemographic(demographic);
		}

		return bedDemographic;
	}

	public BedDemographic getBedDemographicByDemographic(Integer demographicNo, Integer facilityId) {
		if (demographicNo == null) {
			handleException(new IllegalArgumentException("demographicNo must not be null"));
		}

		BedDemographic bedDemographic = null;

		if (bedDemographicDao.bedExists(demographicNo)) {
			bedDemographic = bedDemographicDao.getBedDemographicByDemographic(demographicNo);
			setAttributes(bedDemographic);

			Bed bed = bedDao.find(bedDemographic.getId().getBedId());
			bedDemographic.setBed(bed);

			Room room = roomDao.getRoom(bed.getRoomId());
			// check for facility filtering
			if (facilityId!=null && room.getFacilityId()!=null && room.getFacilityId().intValue()!=facilityId.intValue()) return(null);
			bed.setRoom(room);

			Integer programId = room.getProgramId();

			if (programId != null) {
				Program program = programDao.getProgram(programId);
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
			if (status.isDflt()) {
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
		return bedDemographicStatusDao.getBedDemographicStatuses();
	}

	/**
	 * @see org.oscarehr.PMmodule.service.BedDemographicManager#getExpiredReservations()
	 */
	public BedDemographicHistorical[] getExpiredReservations() {
		BedDemographicHistorical[] bedDemographicHistoricals = bedDemographicHistoricalDao.getBedDemographicHistoricals(new Date());
		
		for (BedDemographicHistorical historical : bedDemographicHistoricals) {
			BedDemographicHistoricalPK id = historical.getId();
			
			historical.setBed(bedDao.find(id.getBedId()));
			historical.setDemographic(demographicDao.getClientByDemographicNo(id.getDemographicNo()));
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
		bedDemographicDao.saveBedDemographic(bedDemographic);
	}

	/**
	 * @see org.oscarehr.PMmodule.service.BedDemographicManager#deleteBedDemographic(BedDemographic)
	 */
	public void deleteBedDemographic(BedDemographic bedDemographic) {
		if (bedDemographic == null) {
			handleException(new IllegalArgumentException("bedDemographic must not be null"));
		}
		
		bedDemographicDao.deleteBedDemographic(bedDemographic);
	}

	void setAttributes(BedDemographic bedDemographic) {
		Integer bedDemographicStatusId = bedDemographic.getBedDemographicStatusId();
		BedDemographicStatus bedDemographicStatus = bedDemographicStatusDao.getBedDemographicStatus(bedDemographicStatusId);
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
		if (!bedDemographicStatusDao.bedDemographicStatusExists(bedDemographicStatusId)) {
			handleException(new IllegalArgumentException("no bed demographic status with id : " + bedDemographicStatusId));
		}
	}

	void validateProvider(String providerId) {
		if (!providerDAO.providerExists(providerId)) {
			handleException(new IllegalArgumentException("no provider with id : " + providerId));
		}
	}

	void validateBed(Integer bedId) {
		if (!bedDao.bedExists(bedId)) {
			handleException(new IllegalArgumentException("no bed with id : " + bedId));
		}
	}

	void validateDemographic(Integer demographicNo) {
		if (!demographicDao.clientExists(demographicNo)) {
			handleException(new IllegalArgumentException("no demographic with id : " + demographicNo));
		}
	}

}
