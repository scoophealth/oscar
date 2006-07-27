
package oscar.dms.data;

import org.apache.struts.action.*;
import org.apache.struts.upload.*;
import javax.servlet.http.*;

public class AddEditDocumentForm extends ActionForm {
    private String function = "";
    private String functionId = "";
    private String docType = "";
    private String docDesc = "";
    private String docCreator = "";
    private FormFile docFile;
    private String mode = "";
    
    public AddEditDocumentForm() {
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getFunctionId() {
        return functionId;
    }

    public void setFunctionId(String functionId) {
        this.functionId = functionId;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getDocDesc() {
        return docDesc;
    }

    public void setDocDesc(String docDesc) {
        this.docDesc = docDesc;
    }

    public String getDocCreator() {
        return docCreator;
    }

    public void setDocCreator(String docCreator) {
        this.docCreator = docCreator;
    }

    public FormFile getDocFile() {
        return docFile;
    }

    public void setDocFile(FormFile docFile) {
        this.docFile = docFile;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
    
}
