package oscar.oscarReport.pageUtil;

import java.io.PrintStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;

public final class ObecForm extends ActionForm {

    String xml_vdate;
    int numDays;

    public String getXml_vdate(){
       return xml_vdate;
    }

    public void setXml_vdate(String id ){
       this.xml_vdate = id;
    }

    public int getNumDays(){
       return numDays;
    }

    public void setNumDays(int numDays ){
       this.numDays = numDays;
    }

}
