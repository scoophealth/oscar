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
import org.oscarehr.PMmodule.model.BedDemographicStatus;
import org.oscarehr.PMmodule.model.Demographic;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.Provider;
import org.oscarehr.PMmodule.model.Room;
import org.oscarehr.PMmodule.service.BedDemographicManager;

public class BedDemographicManagerImpl implements BedDemographicManager {
	
	private static final Log log = LogFactory.getLog(BedDemographicManagerImpl.class);

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

	public boolean demographicExists(Integer bedId) {
	    return bedDemographicDAO.demographicExists(bedId);
	}
	
	/**
	 * @see org.oscarehr.PMmodule.service.BedDemographicManager#getBedDemographicByBed(java.lang.Integer)
	 */
	public BedDemographic getBedDemographicByBed(Integer bedId) {
		BedDemographic bedDemographic = null;

		if (bedDemographicDAO.demographicExists(bedId)) {
			bedDemographic = bedDemographicDAO.getBedDemographicByBed(bedId);
			setAttributes(bedDemographic);

			Demographic demographic = demographicDAO.getClientByDemographicNo(bedDemographic.getId().getDemographicNo());
			String demographicName = (demographic != null) ? demographic.getFormattedName() : "N/A";
			bedDemographic.setDemographicName(demographicName);
		}

		return bedDemographic;
	}

	/**
	 * @see org.oscarehr.PMmodule.service.BedDemographicManager#getBedDemographicByDemographic(java.lang.Integer)
	 */
	public BedDemographic getBedDemographicByDemographic(Integer demographicNo) {
		BedDemographic bedDemographic = null;

		if (bedDemographicDAO.bedExists(demographicNo)) {
			bedDemographic = bedDemographicDAO.getBedDemographicByDemographic(demographicNo);
			setAttributes(bedDemographic);

			Bed bed = bedDAO.getBed(bedDemographic.getId().getBedId());
			String bedName = (bed != null) ? bed.getName() : "N/A";
			bedDemographic.setBedName(bedName);

			Room room = roomDAO.getRoom(bed.getRoomId());
			String roomName = (room != null) ? room.getName() : "N/A";
			bedDemographic.setRoomName(roomName);

			Integer programId = room.getProgramId();

			if (programId != null) {
				Program program = programDAO.getProgram(programId);
				String programName = (program != null) ? program.getName() : "N/A";
				bedDemographic.setProgramName(programName);
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

		throw new IllegalStateException("no default bed demographic status");
	}

	/**
	 * @see org.oscarehr.PMmodule.service.BedDemographicManager#getBedDemographicStatuses()
	 */
	public BedDemographicStatus[] getBedDemographicStatuses() {
		return bedDemographicDAO.getBedDemographicStatuses();
	}

	/**
	 * @see org.oscarehr.PMmodule.service.BedDemographicManager#saveBedDemographic(org.oscarehr.PMmodule.model.BedDemographic)
	 */
	public void saveBedDemographic(BedDemographic bedDemographic) {
		if (bedDemographic == null) {
			throw new IllegalArgumentException("bedDemographic is null");
		}

		validate(bedDemographic);
		bedDemographicDAO.saveBedDemographic(bedDemographic);
	}

	/**
	 * @see org.oscarehr.PMmodule.service.BedDemographicManager#deleteBedDemographic(BedDemographic)
	 */
	public void deleteBedDemographic(BedDemographic bedDemographic) {
		bedDemographicDAO.deleteBedDemographic(bedDemographic);
		
		log.debug("deleteBedDemographic: " + bedDemographic);
	}

	void setAttributes(BedDemographic bedDemographic) {
		Integer bedDemographicStatusId = bedDemographic.getBedDemographicStatusId();
		BedDemographicStatus bedDemographicStatus = bedDemographicDAO.getBedDemographicStatus(bedDemographicStatusId);
		String statusName = (bedDemographicStatus != null) ? bedDemographicStatus.getName() : "N/A";
		bedDemographic.setStatusName(statusName);

		Integer duration = (bedDemographicStatus != null) ? bedDemographicStatus.getDuration() : 0;
		bedDemographic.setReservationEnd(duration);

		String providerNo = bedDemographic.getProviderNo();
		Provider provider = providerDAO.getProvider(providerNo);
		String providerName = (provider != null) ? provider.getFormattedName() : "N/A";
		bedDemographic.setProviderName(providerName);
	}

	void validate(BedDemographic bedDemographic) {
		// mandatory
		validateBedDemographicStatus(bedDemographic.getBedDemographicStatusId());
		validateProvider(bedDemographic.getProviderNo());
		validateBed(bedDemographic.getId().getBedId());
		validateDemographic(bedDemographic.getId().getDemographicNo());
	}

	void validateBedDemographic(BedDemographic bedDemographic) {
		if (!bedDemographic.isValidReservation()) {
			throw new IllegalArgumentException("invalid reservation: " + bedDemographic.getReservationStart() + " - " + bedDemographic.getReservationEnd());
		}
	}

	void validateBedDemographicStatus(Integer bedDemographicStatusId) {
		if (bedDemographicStatusId != null && !bedDemographicDAO.bedDemographicStatusExists(bedDemographicStatusId)) {
			throw new IllegalArgumentException("no bed demographic status with id : " + bedDemographicStatusId);
		}
	}

	void validateProvider(String providerId) {
		if (providerId != null && !providerDAO.providerExists(providerId)) {
			throw new IllegalArgumentException("no provider with id : " + providerId);
		}
	}

	void validateBed(Integer bedId) {
		if (bedId != null && !bedDAO.bedExists(bedId)) {
			throw new IllegalArgumentException("no bed with id : " + bedId);
		}
	}

	void validateDemographic(Integer demographicNo) {
		if (demographicNo != null && !demographicDAO.clientExists(demographicNo)) {
			throw new IllegalArgumentException("no demographic with id : " + demographicNo);
		}
	}

}
