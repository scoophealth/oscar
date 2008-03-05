package org.oscarehr.PMmodule.web.admin;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;
import org.oscarehr.PMmodule.model.Facility;
import org.oscarehr.PMmodule.service.FacilityManager;
import org.oscarehr.PMmodule.web.BaseAction;
import org.springframework.beans.factory.annotation.Required;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class FacilityManagerAction extends BaseAction {
    private static final Log log = LogFactory.getLog(FacilityManagerAction.class);

    private FacilityManager facilityManager;    
    
    private static final String FORWARD_EDIT = "edit";
    private static final String FORWARD_VIEW = "view";
    private static final String FORWARD_LIST = "list";

    private static final String BEAN_FACILITIES = "facilities";
    private static final String BEAN_ASSOCIATED_PROGRAMS = "associatedPrograms";

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return list(mapping, form, request, response);
    }

    public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        List<Facility> facilities = facilityManager.getFacilities();
        List<Facility> filteredFacilities = new ArrayList<Facility>();
        for (Facility facility : facilities) {
            if (!facility.isDisabled())
                filteredFacilities.add(facility);
        }
        request.setAttribute(BEAN_FACILITIES, filteredFacilities);

        //get agency's organization list from caisi editor table        
        request.setAttribute("orgList", lookupManager.LoadCodeList("OGN", true, null, null));
        
        //get agency's sector list from caisi editor table
        request.setAttribute("sectorList", lookupManager.LoadCodeList("SEC", true,null, null));
    	
        return mapping.findForward(FORWARD_LIST);
    }

    public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String idStr = request.getParameter("id");
        Integer id = Integer.valueOf(idStr);
        Facility facility = facilityManager.getFacility(id);
        
        FacilityManagerForm facilityForm = (FacilityManagerForm) form;
        facilityForm.setFacility(facility);
        
        request.setAttribute(BEAN_ASSOCIATED_PROGRAMS, facilityManager.getAssociatedPrograms(id));
        request.setAttribute("id", facility.getId());

        return mapping.findForward(FORWARD_VIEW);
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        Facility facility = facilityManager.getFacility(Integer.valueOf(id));
        
        FacilityManagerForm managerForm = (FacilityManagerForm) form;
        managerForm.setFacility(facility);
                
        request.setAttribute("id", facility.getId());
        request.setAttribute("orgId",facility.getOrgId());
        request.setAttribute("sectorId",facility.getSectorId());
        
        //get agency's organization list from caisi editor table        
        request.setAttribute("orgList", lookupManager.LoadCodeList("OGN", true,null,null));
        
        //get agency's sector list from caisi editor table
        request.setAttribute("sectorList", lookupManager.LoadCodeList("SEC",true,null,null));
        
        return mapping.findForward(FORWARD_EDIT);
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        Facility facility = facilityManager.getFacility(Integer.valueOf(id));
        facility.setDisabled(true);
        facilityManager.saveFacility(facility);

        return list(mapping, form, request, response);
    }

    public ActionForward add(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        Facility facility = new Facility("", "");
        ((FacilityManagerForm) form).setFacility(facility);
        
        //get agency's organization list from caisi editor table        
        request.setAttribute("orgList", lookupManager.LoadCodeList("OGN",true,null,null));
        
        //get agency's sector list from caisi editor table
        request.setAttribute("sectorList", lookupManager.LoadCodeList("OGN", true, null, null));
        
        return mapping.findForward(FORWARD_EDIT);
    }

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        FacilityManagerForm mform = (FacilityManagerForm) form;        
    	Facility facility = mform.getFacility();
    	    	
        if(request.getParameter("facility.hic") == null)
            facility.setHic(false);

        if (isCancelled(request)) {
            request.getSession().removeAttribute("facilityManagerForm");

            return list(mapping, form, request, response);
        }

        try {
            facilityManager.saveFacility(facility);

            ActionMessages messages = new ActionMessages();
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("facility.saved", facility.getName()));
            saveMessages(request, messages);

            request.setAttribute("id", facility.getId());

            logManager.log("write", "facility", facility.getId().toString(), request);

            return list(mapping, form, request, response);
        }
        catch (Exception e) {
            ActionMessages messages = new ActionMessages();
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("duplicateKey", "The name "+facility.getName()));
            saveMessages(request, messages);
            
            return mapping.findForward(FORWARD_EDIT);
        }
    }

    public FacilityManager getFacilityManager() {
        return facilityManager;
    }

    @Required
    public void setFacilityManager(FacilityManager facilityManager) {
        this.facilityManager = facilityManager;
    }
}
