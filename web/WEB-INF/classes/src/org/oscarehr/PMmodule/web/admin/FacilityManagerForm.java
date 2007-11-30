package org.oscarehr.PMmodule.web.admin;

import org.apache.struts.action.ActionForm;
import org.oscarehr.PMmodule.model.Facility;
import org.oscarehr.PMmodule.model.Program;

import java.util.List;

/**
 */
public class FacilityManagerForm extends ActionForm {
    Integer agencyId;
    private Facility facility;

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    public Integer getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(Integer agencyId) {
        this.agencyId = agencyId;
    }

}
