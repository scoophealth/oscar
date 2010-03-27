package oscar.facility;

import org.apache.struts.action.ActionForm;
import org.oscarehr.common.model.Facility;


public class FacilityManagerForm extends ActionForm {
    private Facility facility;
    private boolean removeDemographicIdentity=true;
    private Integer updateInterval;

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }
    public boolean getRemoveDemographicIdentity() {
        return removeDemographicIdentity;
    }

    public void setRemoveDemographicIdentity(boolean removeDemographicIdentity) {
        this.removeDemographicIdentity = removeDemographicIdentity;
    }

    public Integer getUpdateInterval() {
        return updateInterval;
    }

    public void setUpdateInterval(Integer updateInterval) {
        this.updateInterval = updateInterval;
    }

}
