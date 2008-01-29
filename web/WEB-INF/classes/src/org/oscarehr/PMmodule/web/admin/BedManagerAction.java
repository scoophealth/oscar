package org.oscarehr.PMmodule.web.admin;

import org.apache.struts.action.*;
import org.oscarehr.PMmodule.exception.BedReservedException;
import org.oscarehr.PMmodule.exception.RoomHasActiveBedsException;
import org.oscarehr.PMmodule.model.Bed;
import org.oscarehr.PMmodule.model.Facility;
import org.oscarehr.PMmodule.model.Room;
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
        else if (request.getParameter("submit.addRooms") != null)
            return addRooms(mapping, form, request, response);
        else if (request.getParameter("submit.saveBeds") != null)
            return saveBeds(mapping, form, request, response);
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
        bForm.setBeds(bedManager.getBedsByFacility(facilityId));
        bForm.setBedTypes(bedManager.getBedTypes());
        bForm.setNumBeds(1);
        bForm.setPrograms(programManager.getBedPrograms());
        bForm.setFacility(facility);

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
        }

        return manage(mapping, form, request, response);
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
        }

		return manage(mapping, form, request, response);
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

        return manage(mapping, form, request, response);
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

        return manage(mapping, form, request, response);
    }

    public FacilityManager getFacilityManager() {
        return facilityManager;
    }

    @Required    
    public void setFacilityManager(FacilityManager facilityManager) {
        this.facilityManager = facilityManager;
    }
}
