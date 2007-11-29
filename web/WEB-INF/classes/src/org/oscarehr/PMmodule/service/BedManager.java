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

package org.oscarehr.PMmodule.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oscarehr.PMmodule.dao.BedDAO;
import org.oscarehr.PMmodule.dao.ProgramTeamDAO;
import org.oscarehr.PMmodule.dao.RoomDAO;
import org.oscarehr.PMmodule.exception.BedReservedException;
import org.oscarehr.PMmodule.model.Bed;
import org.oscarehr.PMmodule.model.BedDemographic;
import org.oscarehr.PMmodule.model.BedType;
import org.oscarehr.PMmodule.model.Room;
import org.oscarehr.PMmodule.service.BedDemographicManager;

/**
 * Implementation of BedManager interface
 */
public class BedManager {

    private static final Log log = LogFactory.getLog(BedManager.class);

    private static <T extends Exception> void handleException(T e) throws T {
        log.error(e);
        throw e;
    }

    private BedDAO bedDAO;
    private RoomDAO roomDAO;
    private ProgramTeamDAO teamDAO;
    private BedDemographicManager bedDemographicManager;

    public void setBedDAO(BedDAO bedDAO) {
        this.bedDAO = bedDAO;
    }

    public void setRoomDAO(RoomDAO roomDAO) {
        this.roomDAO = roomDAO;
    }

    public void setTeamDAO(ProgramTeamDAO teamDAO) {
        this.teamDAO = teamDAO;
    }

    public void setBedDemographicManager(BedDemographicManager bedDemographicManager) {
        this.bedDemographicManager = bedDemographicManager;
    }

    /**
     * Get bed
     *
     * @param bedId
     *            bed identifier
     * @return bed
     */
    public Bed getBed(Integer bedId) {
        if (bedId == null) {
            handleException(new IllegalArgumentException("bedId must not be null"));
        }

        Bed bed = bedDAO.getBed(bedId);
        setAttributes(bed);

        return bed;
    }

    /**
     * Get beds by program
     *
     * @param programId
     *            program identifier
     * @param reserved
     *            reserved flag
     * @return array of beds
     */
    public Bed[] getBedsByProgram( Integer programId, boolean reserved) {
        if (programId == null) {
            return new Bed[] {};
        }

        List<Bed> beds = new ArrayList<Bed>();

        for (Room room : roomDAO.getRooms(null, programId, Boolean.TRUE)) {
            for (Bed bed : bedDAO.getBedsByRoom(room.getId(), Boolean.TRUE)) {
                setAttributes(bed);

                if (!filterBed(bed, reserved)) {
                    beds.add(bed);
                }
            }
        }

        return beds.toArray(new Bed[beds.size()]);
    }

    /**
     * Get beds by facility
     *
     * @param facilityId
     *            facility identifier
     * @param reserved
     *            reserved flag
     * @return array of beds
     */
    public Bed[] getBedsByFacility( Integer facilityId, boolean reserved) {
        if (facilityId == null) {
            return new Bed[] {};
        }

        List<Bed> beds = new ArrayList<Bed>();


        for (Bed bed : bedDAO.getBedsByFacility(facilityId, Boolean.TRUE)) {
            setAttributes(bed);

            if (!filterBed(bed, reserved)) {
                beds.add(bed);
            }
        }

        return beds.toArray(new Bed[beds.size()]);
    }

    /**
     * Get beds by facility
     *
     * @param facilityId the facility we're looking up
     * @param reserved
     *            reserved flag
     * @return array of beds
     */
    public Bed[] getBedsByProgramAndFacility(Integer facilityId, boolean reserved) {
        List<Bed> beds = new ArrayList<Bed>();

        for (Room room : roomDAO.getRooms(facilityId, null, Boolean.TRUE)) {
            for (Bed bed : bedDAO.getBedsByRoom(room.getId(), Boolean.TRUE)) {
                setAttributes(bed);

                if (!filterBed(bed, reserved)) {
                    beds.add(bed);
                }
            }
        }

        return beds.toArray(new Bed[beds.size()]);
    }

    /**
     * Get beds
     *
     * @return array of beds
     */
    public Bed[] getBeds() {
        Bed[] beds = bedDAO.getBedsByRoom(null, null);

        for (Bed bed : beds) {
            setAttributes(bed);
        }

        return beds;
    }

    /**
     * @see org.oscarehr.PMmodule.service.BedManager#getBedTypes()
     */
    public BedType[] getBedTypes() {
        return bedDAO.getBedTypes();
    }

    /**
     * Add new beds
     *
     * @param numBeds
     *            number of beds
     * @throws BedReservedException
     *             bed is inactive and reserved
     */
    public void addBeds(Integer facilityId, int numBeds) throws BedReservedException {
        if (numBeds < 1) {
            handleException(new IllegalArgumentException("numBeds must be greater than or equal to 1"));
        }

        BedType defaultBedType = getDefaultBedType();

        for (int i = 0; i < numBeds; i++) {
            saveBed(Bed.create(facilityId, defaultBedType));
        }
    }

    /**
     * Save beds
     *
     * @param beds
     *            beds to save
     * @throws BedReservedException
     *             bed is inactive and reserved
     */
    public void saveBeds(Bed[] beds) throws BedReservedException {
        if (beds == null) {
            handleException(new IllegalArgumentException("beds must not be null"));
        }

        for (Bed bed : beds) {
            saveBed(bed);
        }
    }

    /**
     * Save bed
     *
     * @param bed
     *            bed to save
     * @throws BedReservedException
     *             bed is inactive and reserved
     */
    public void saveBed(Bed bed) throws BedReservedException {
        validate(bed);
        bedDAO.saveBed(bed);
    }

    BedType getDefaultBedType() {
        for (BedType bedType : getBedTypes()) {
            if (bedType.isDefault()) {
                return bedType;
            }
        }

        handleException(new IllegalStateException("no default bed type"));

        return null;
    }

    boolean filterBed(Bed bed, Boolean reserved) {
        if (reserved == null) {
            return false;
        }

        return reserved != bed.isReserved();
    }

    void setAttributes(Bed bed) {
        bed.setBedType(bedDAO.getBedType(bed.getBedTypeId()));
        if (bed.getRoomId() != null)
            bed.setRoom(roomDAO.getRoom(bed.getRoomId()));

        Integer teamId = bed.getTeamId();

        if (teamId != null) {
            bed.setTeam(teamDAO.getProgramTeam(teamId));
        }

        BedDemographic bedDemographic = bedDemographicManager.getBedDemographicByBed(bed.getId());

        if (bedDemographic != null) {
            bed.setBedDemographic(bedDemographic);
        }
    }

    void validate(Bed bed) throws BedReservedException {
        if (bed == null) {
            handleException(new IllegalStateException("bed must not be null"));
        }

        validateBed(bed.getId(), bed);
        validateBedType(bed.getBedTypeId());
        validateRoom(bed.getRoomId());
        validateTeam(bed.getTeamId());
    }

    void validateBed(Integer bedId, Bed bed) throws BedReservedException {
        if (bedId != null) {
            if (!bedDAO.bedExists(bedId)) {
                handleException(new IllegalStateException("no bed with id : " + bedId));
            }

            if (!bed.isActive() && bedDemographicManager.demographicExists(bed.getId())) {
                handleException(new BedReservedException("bed with id : " + bedId + " has a reservation"));
            }
        }
    }

    void validateBedType(Integer bedTypeId) {
        if (!bedDAO.bedTypeExists(bedTypeId)) {
            handleException(new IllegalStateException("no bed type with id : " + bedTypeId));
        }
    }

    void validateRoom(Integer roomId) {
        if (roomId != null && !roomDAO.roomExists(roomId)) {
            handleException(new IllegalStateException("no room with id : " + roomId));
        }
    }

    void validateTeam(Integer teamId) {
        if (teamId != null && !teamDAO.teamExists(teamId)) {
            handleException(new IllegalStateException("no team with id : " + teamId));
        }
    }

}