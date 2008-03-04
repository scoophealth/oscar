package org.oscarehr.PMmodule.web.admin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts.action.*;
import org.oscarehr.PMmodule.exception.BedReservedException;
import org.oscarehr.PMmodule.exception.DuplicateBedNameException;
import org.oscarehr.PMmodule.exception.DuplicateRoomNameException;
import org.oscarehr.PMmodule.exception.RoomHasActiveBedsException;
import org.oscarehr.PMmodule.model.Bed;
import org.oscarehr.PMmodule.model.BedDemographic;
import org.oscarehr.PMmodule.model.Facility;
import org.oscarehr.PMmodule.model.Room;
import org.oscarehr.PMmodule.model.RoomDemographic;
import org.oscarehr.PMmodule.service.FacilityManager;
import org.oscarehr.PMmodule.web.BaseAction;
import org.springframework.beans.factory.annotation.Required;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Responsible for managing beds
 */
public class BedManagerAction extends BaseAction {

    private static final String FORWARD_MANAGE = "manage";

    private FacilityManager facilityManager;

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        // dispatch to correct method based on which submit button was selected
        if (request.getParameter("submit.saveRooms") != null)
            return saveRooms(mapping, form, request, response);
        else if (request.getParameter("submit.deleteRoom") != null)
            return deleteRoom(mapping, form, request, response);
        else if (request.getParameter("submit.addRooms") != null)
            return addRooms(mapping, form, request, response);
        else if (request.getParameter("submit.saveBeds") != null)
            return saveBeds(mapping, form, request, response);
        else if (request.getParameter("submit.deleteBed") != null)
            return deleteBed(mapping, form, request, response);
        else if (request.getParameter("submit.addBeds") != null)
            return addBeds(mapping, form, request, response);
        else
            return manage(mapping, form, request, response);
    }

    public ActionForward manage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        BedManagerForm bForm = (BedManagerForm) form;

        Integer facilityId = Integer.valueOf(request.getParameter("facilityId"));
        Facility facility = facilityManager.getFacility(facilityId);

        bForm.setFacilityId(facilityId);
        bForm.setRooms(roomManager.getRooms(facilityId));
        bForm.setAssignedBedRooms(roomManager.getAssignedBedRooms(facilityId));
        bForm.setRoomTypes(roomManager.getRoomTypes());
        bForm.setNumRooms(1);
        bForm.setBeds(bedManager.getBedsByFacility(facilityId, false));
        bForm.setBedTypes(bedManager.getBedTypes());
        bForm.setNumBeds(1);
        bForm.setPrograms(programManager.getBedPrograms());
        bForm.setFacility(facility);
        Map statusNames = new HashMap();
        statusNames.put("1", "Active");
        statusNames.put("0", "Inactive");
        statusNames.put("2", "Any");
        bForm.setRoomStatusNames( statusNames );
        bForm.setBedStatusNames( statusNames );

        return mapping.findForward(FORWARD_MANAGE);
    }

    public ActionForward manageFilter(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

        BedManagerForm bForm = (BedManagerForm) form;

        Integer facilityId = Integer.valueOf(request.getParameter("facilityId"));
        Facility facility = facilityManager.getFacility(facilityId);

        bForm.setFacilityId(facilityId);
        bForm.setRooms(bForm.getRooms());
        bForm.setAssignedBedRooms(roomManager.getAssignedBedRooms(facilityId));
        bForm.setRoomTypes(roomManager.getRoomTypes());
        bForm.setNumRooms(1);
        bForm.setBeds(bForm.getBeds());
        bForm.setBedTypes(bedManager.getBedTypes());
        bForm.setNumBeds(1);
        bForm.setPrograms(programManager.getBedPrograms());
        bForm.setFacility(facility);
        Map statusNames = new HashMap();
        statusNames.put("1", "Active");
        statusNames.put("0", "Inactive");
        statusNames.put("2", "Any");
        bForm.setRoomStatusNames( statusNames );
        bForm.setBedStatusNames( statusNames );

        return mapping.findForward(FORWARD_MANAGE);
    }

	public ActionForward saveRooms(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        BedManagerForm bForm = (BedManagerForm) form;

		Room[] rooms = bForm.getRooms();

		// detect check box false
		for (int i = 0; i < rooms.length; i++) {
	        if (request.getParameter("rooms[" + i + "].active") == null) {
	        	rooms[i].setActive(false);
	        }
        }
		
		try {
	        roomManager.saveRooms(rooms);
        } catch (RoomHasActiveBedsException e) {
    		ActionMessages messages = new ActionMessages();
    		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("room.active.beds.error", e.getMessage()));
    		saveMessages(request, messages);
        } catch (DuplicateRoomNameException e) {
    		ActionMessages messages = new ActionMessages();
    		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("room.duplicate.name.error", e.getMessage()));
    		saveMessages(request, messages);
        }

        return doRoomFilter(mapping, form, request, response);
    }
	
	public ActionForward deleteRoom(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        
		ActionMessages messages = new ActionMessages();
		BedManagerForm bForm = (BedManagerForm) form;

		Integer roomId = bForm.getRoomToDelete();
		
		//(1)Check whether any client is assigned to this room ('room_demographic' table)-> 
		//  if yes, disallow room delete and display message.
		//(2)if no client assigned, check whether any beds assigned ('bed' table) -> 
		// if some bed assigned, retrieve all beds assigned to this room -> delete them all <-- ???
		//(3)then delete this room ('room' table)
		try {
			List<RoomDemographic> roomDemographicList = getRoomDemographicManager().getRoomDemographicByRoom(roomId);

			if(roomDemographicList != null  &&  !roomDemographicList.isEmpty()){
				throw new RoomHasActiveBedsException("The room has client(s) assigned to it and cannot be removed.");
			}
			
			Bed[] beds = bedManager.getBedsForDeleteByRoom(roomId);
			
			if(beds != null  &&  beds.length > 0){
				
				for(int i=0; i < beds.length; i++){
					try{
						bedManager.deleteBed(beds[i]);
					}catch (BedReservedException be) {
						log.debug("deleteRoom(): Exception in deleting Beds["+i+"]: " + be);
			        } 
				}
			}
			
			Room room = roomManager.getRoom(roomId);
			roomManager.deleteRoom(room);
	        
	        
        } catch (RoomHasActiveBedsException e) {
    		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("room.active.beds.error", e.getMessage()));
    		saveMessages(request, messages);
        }


        return doRoomFilter(mapping, form, request, response);
    }
	
	public ActionForward saveBeds(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        BedManagerForm bForm = (BedManagerForm) form;

		Bed[] beds = bForm.getBeds();

		for (int i = 0; i < beds.length; i++) {
	        if (request.getParameter("beds[" + i + "].active") == null) {
	        	beds[i].setActive(false);
	        }
        }
		
		Room[] rooms = roomManager.getUnfilledRoomIds(beds);
		if(rooms == null){
			rooms = bForm.getRooms();
			if(rooms == null){
				log.error("saveBeds(): No beds are assigned to rooms.");
			}
		}
		
		try {
			beds = bedManager.getBedsForUnfilledRooms(rooms, beds);
			
	        bedManager.saveBeds(beds);
	        
        } catch (BedReservedException e) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("bed.reserved.error", e.getMessage()));
			saveMessages(request, messages);
        } catch (DuplicateBedNameException e) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("bed.duplicate.name.error", e.getMessage()));
			saveMessages(request, messages);
        }

		return doBedFilter(mapping, form, request, response);
    }

	public ActionForward deleteBed(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        BedManagerForm bForm = (BedManagerForm) form;

        Integer bedId = bForm.getBedToDelete();
		//(1)Check whether any client is assigned to this bed ('bed_demographic' table)-> 
		//  if yes, disallow bed delete and display message.
		//(2)if no client assigned, delete this bed ('bed' table)
		
		try {
			
			BedDemographic bedDemographic = getBedDemographicManager().getBedDemographicByBed(bedId);

			if(bedDemographic != null){
				throw new BedReservedException("The bed has client assigned to it and cannot be removed.");
			}
			
			Bed bed = bedManager.getBedForDelete(bedId);
			bedManager.deleteBed(bed);
		
        } catch (BedReservedException e) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("bed.reserved.error", e.getMessage()));
			saveMessages(request, messages);
        }

		return doBedFilter(mapping, form, request, response);
    }

	public ActionForward addRooms(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        BedManagerForm bForm = (BedManagerForm) form;
		Integer numRooms = bForm.getNumRooms();

		if (numRooms!= null && numRooms > 0) {
			try {
	            roomManager.addRooms(bForm.getFacilityId(), numRooms);
            } catch (RoomHasActiveBedsException e) {
        		ActionMessages messages = new ActionMessages();
        		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("room.active.beds.error", e.getMessage()));
        		saveMessages(request, messages);
            }
		}

        return doRoomFilter(mapping, form, request, response);
    }

	public ActionForward addBeds(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        Integer facilityId = Integer.valueOf(request.getParameter("facilityId"));
        BedManagerForm bForm = (BedManagerForm) form;
		Integer numBeds = bForm.getNumBeds();

		if (numBeds != null && numBeds > 0) {
			try {
	            bedManager.addBeds(facilityId, numBeds);
            } catch (BedReservedException e) {
    			ActionMessages messages = new ActionMessages();
    			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("bed.reserved.error", e.getMessage()));
    			saveMessages(request, messages);
            }
		}

        return doBedFilter(mapping, form, request, response);
    }

	public ActionForward doRoomFilter(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        Integer facilityId = Integer.valueOf(request.getParameter("facilityId"));
        BedManagerForm bForm = (BedManagerForm) form;
        Integer roomStatus = bForm.getRoomStatusFilter(); 
        Integer roomFilteredProgram = bForm.getBedProgramFilterForRoom();
        Boolean roomStatusBoolean = new Boolean(false);
        
        if(roomStatus.intValue() == 1){
        	roomStatusBoolean = new Boolean(true);
        }else if(roomStatus.intValue() == 0){
        	roomStatusBoolean = new Boolean(false);
        }else{
        	roomStatusBoolean = null;
        }
        
        if(roomFilteredProgram.intValue() == 0){
        	roomFilteredProgram = null;
        }
        Room[] filteredRooms = roomManager.getAssignedBedRooms(facilityId, roomFilteredProgram, roomStatusBoolean);
        
        bForm.setRooms(filteredRooms);
        form = bForm;

        return manageFilter(mapping, form, request, response);
    }

	public ActionForward doBedFilter(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        Integer facilityId = Integer.valueOf(request.getParameter("facilityId"));
        BedManagerForm bForm = (BedManagerForm) form;
        Integer bedStatus = bForm.getBedStatusFilter(); 
        Integer bedFilteredProgram = bForm.getBedProgramFilterForBed();
        Boolean bedStatusBoolean = new Boolean(false);
        
        List<Bed> filteredBeds = new ArrayList<Bed>();
        
        if(bedStatus.intValue() == 1){
        	bedStatusBoolean = new Boolean(true);
        }else if(bedStatus.intValue() == 0){
        	bedStatusBoolean = new Boolean(false);
        }else{
        	bedStatusBoolean = null;
        }
        
        if(bedFilteredProgram.intValue() == 0){
        	bedFilteredProgram = null;
        }

        List<Bed> filteredBedsList = null;
        Room[] filteredRooms = roomManager.getAssignedBedRooms(facilityId, bedFilteredProgram, bedStatusBoolean);
        for(int i=0; filteredRooms != null  &&  i < filteredRooms.length; i++){
        	
        	if(filteredRooms[i] != null){
        		filteredBedsList = bedManager.getBedsByFilter(facilityId, filteredRooms[i].getId(), bedStatusBoolean, false);
        		filteredBeds.addAll(filteredBedsList);
        	}
        }
        
        bForm.setBeds(filteredBeds.toArray(new Bed[filteredBeds.size()]));
        form = bForm;

        return manageFilter(mapping, form, request, response);
    }

    public FacilityManager getFacilityManager() {
        return facilityManager;
    }

    @Required    
    public void setFacilityManager(FacilityManager facilityManager) {
        this.facilityManager = facilityManager;
    }
}
