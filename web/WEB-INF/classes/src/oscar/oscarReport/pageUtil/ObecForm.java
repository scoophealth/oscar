package oscar.oscarReport.pageUtil;

import java.io.PrintStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;

public final class ObecForm extends ActionForm {

    String xml_vdate;
    String xml_appointment_date;

    public String getXml_vdate(){
       return xml_vdate;
    }

    public void setXml_vdate(String id ){
       this.xml_vdate = id;
    }

        public String getXml_appointment_date(){
	       return xml_appointment_date;
	    }

	    public void setXml_appointment_date(String id ){
	       this.xml_appointment_date = id;
    }

}
