
package oscar.eform.data;

import org.apache.struts.action.*;
import java.util.*;
import oscar.eform.EFormUtil;

public class AddToGroupForm extends ActionForm {
    private String fid;
    private String groupName;
    
    public AddToGroupForm() {
    }
    
    public String getFid() {
        return fid;
    }
    
    public void setFid(String fid) {
        this.fid = fid;
    }
    
    public String getGroupName() {
        return groupName;
    }
    
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    
}
