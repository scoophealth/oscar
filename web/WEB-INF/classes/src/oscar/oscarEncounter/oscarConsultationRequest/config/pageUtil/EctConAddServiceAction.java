package oscar.oscarEncounter.oscarConsultationRequest.config.pageUtil;

import java.io.IOException;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.*;
import oscar.oscarDB.DBHandler;

public class EctConAddServiceAction extends Action
{

    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        EctConAddServiceForm addServiceForm = (EctConAddServiceForm)form;
        String service = addServiceForm.getService();
        try
        {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = String.valueOf(String.valueOf((new StringBuffer("insert into consultationServices (serviceDesc,active) values ('")).append(service).append("','1')")));
            db.RunSQL(sql);
            db.CloseConn();
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        request.setAttribute("SERVADD", service);
        return mapping.findForward("success");
    }
}
