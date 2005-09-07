
package oscar.eform.data;

import oscar.eform.*;
import java.sql.*;
import java.util.*;

public class EFormCheck extends EFormBase {
    
    public EFormCheck() {
    }
    
    public EFormCheck(String fid) {
        loadEForm(fid);
    }
    
    public void loadEForm(String fid) {
        Hashtable loaded = (Hashtable) EFormUtil.loadEForm(fid);
        fid = (String) loaded.get("fid");
        formName = (String) loaded.get("formName");
        formHtml = (String) loaded.get("formHtml");
    }
    
}
