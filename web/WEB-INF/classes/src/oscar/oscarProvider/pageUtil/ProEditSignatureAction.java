package oscar.oscarProvider.pageUtil;

import java.io.IOException;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import oscar.oscarDB.DBHandler;
import oscar.oscarProvider.data.*;

public class ProEditSignatureAction extends Action {

    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        String providerNo = (String) request.getSession().getAttribute("user");
        if ( providerNo == null)
              return mapping.findForward("eject");

        ProEditSignatureForm frm = (ProEditSignatureForm)form;
        
        ProSignatureData sigData = new ProSignatureData();
        String sig = frm.getSignature();
        sigData.enterSignature(providerNo, sig);
        

 
        return mapping.findForward("success");
        
    }
}
