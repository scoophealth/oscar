package oscar.oscarProvider.pageUtil;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;

public final class ProEditSignatureForm extends ActionForm {

    String signature;
    public String getSignature()
    {
        if(signature == null)
            signature = new String();
        return signature;
    }

    public void setSignature(String str)
    {
        System.out.println("signature has been set");
        signature = str;
    }

}
