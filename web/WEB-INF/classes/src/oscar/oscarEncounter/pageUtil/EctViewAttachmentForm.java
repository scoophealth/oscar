package oscar.oscarEncounter.pageUtil;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;


public final class EctViewAttachmentForm extends ActionForm {



    String mesId;
    ////mesId///////////////////////////////////////////////////////////////////
    public String getMesId (){
        if ( this.mesId == null){
            this.mesId = new String();
        }
        return this.mesId ;
    }

    public void setMesId (String str){
        this.mesId = str;
    }
    //=-------------------------------------------------------------------------
}

