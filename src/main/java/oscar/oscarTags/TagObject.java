
package oscar.oscarTags;

import java.util.ArrayList;


public class TagObject {
    private ArrayList assignedTags;
    private String objectId;
    private String objectClass;
    
    public void assignTag(String tagName) {
        getAssignedTags().add(tagName);
    }

    public ArrayList getAssignedTags() {
        return assignedTags;
    }

    public void setAssignedTags(ArrayList assignedTags) {
        this.assignedTags = assignedTags;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectClass() {
        return objectClass;
    }

    public void setObjectClass(String objectClass) {
        this.objectClass = objectClass;
    }
    
    
    
}
