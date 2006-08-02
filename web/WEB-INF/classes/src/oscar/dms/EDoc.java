
package oscar.dms;

import oscar.oscarTags.*;
import oscar.util.*;

public class EDoc extends TagObject {
    private String docId;
    private String description = "";
    private String dateTimeStamp = "";
    private String type = "";
    private String fileName = "";
    private String xml = "";
    private String creatorId = "";
    private char status;
    private String module = "";
    private String moduleId = "";
    private String contentType = "";
    
    /** Creates a new instance of EDoc */
    public EDoc() {
    }
    
    public EDoc(String description, String type, String fileName, String xml, String creatorId, char status, String module, String moduleId) {
        this.description = description.trim();
        this.type = type.trim();
        this.fileName = fileName.trim();
        this.xml = xml;
        this.creatorId = creatorId.trim();
        this.status = status;
        this.module = module.trim();
        this.moduleId = moduleId.trim();
        preliminaryProcessing();
    }
    
    
    private void preliminaryProcessing() {
        this.dateTimeStamp = EDocUtil.getDmsDateTime();
        if (fileName.length() != 0) {
            String filenamePrefix = UtilDateUtilities.DateToString(UtilDateUtilities.now(), "yyyyMMdd") + UtilDateUtilities.DateToString(UtilDateUtilities.now(), "HHmmss");
            this.fileName = filenamePrefix + fileName;
        }
    }
    
    //Getter/Setter methods...

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateTimeStamp() {
        return dateTimeStamp;
    }

    public void setDateTimeStamp(String dateTimeStamp) {
        this.dateTimeStamp = dateTimeStamp;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }
    
    public String getModuleName() {
        String moduleName = EDocUtil.getModuleName(module, moduleId);
        return moduleName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public String getCreatorId() {
        return creatorId;
    }
    
    public String getCreatorName() {
        String creatorName = EDocUtil.getModuleName("provider", creatorId);
        return creatorName;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    /**
     *Returns true if document a PDF.
     */
    public boolean isPDF(){
        if ( this.contentType != null && this.contentType.equalsIgnoreCase("application/pdf")){
            return true;
        }
        return false;
    }
}
