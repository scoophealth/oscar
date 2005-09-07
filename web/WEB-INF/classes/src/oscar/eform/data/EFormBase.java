
package oscar.eform.data;

import oscar.util.*;
import java.util.Properties;
import oscar.OscarProperties;

public class EFormBase {
    protected final String imageMarker = "${oscar_image_path}";
    protected String fdid;
    protected String fid; 
    protected String formName;
    protected String formSubject;
    protected String formHtml;
    protected String demographicNo;
    protected String providerNo;
    protected String formDate;
    protected String formTime;
    
    public void setImagePath() {
        Properties prop = oscar.OscarProperties.getInstance();
        String projHome = prop.getProperty("project_home");
        String output = "../../OscarDocument/" + projHome + "/eform/images/";
        StringBuffer html = new StringBuffer(formHtml);
        int pointer = html.indexOf(imageMarker);
        if (pointer >= 0) {
            html = html.replace(pointer, pointer+imageMarker.length(), output);
            formHtml = html.toString();
        }
    }
    
    //------------getters/setters----
    public String getFormTime() {
        return formTime;
    }    
    public void setFormTime(String formTime) {
        this.formTime = formTime;
    }
    public String getFormDate() {
        return formDate;
    }
    public void setFormDate(String formDate) {
        this.formDate = formDate;
    }
    public java.lang.String getFid() {
        return fid;
    }
    public void setFid(String fid) {
        this.fid = fid;
    }
    public String getFormName() {
        return formName;
    }
    public void setFormName(String formName) {
        this.formName = formName;
    }
    public String getFormHtml() {
        return formHtml;
    }
    public void setFormHtml(String formHtml) {
        this.formHtml = formHtml;
    }
    public String getDemographicNo() {
        return demographicNo;
    }
    public void setDemographicNo(String demographicNo) {
        this.demographicNo = demographicNo;
    }
    public String getFormSubject() {
        if (formSubject == null) {
            return "";
        }
        return formSubject;
    }
    public void setFormSubject(String formSubject) {
        this.formSubject = formSubject;
    }
    
    public String getProviderNo() {
        return providerNo;
    }
    
    public void setProviderNo(String providerNo) {
        this.providerNo = providerNo;
    }
    
}
