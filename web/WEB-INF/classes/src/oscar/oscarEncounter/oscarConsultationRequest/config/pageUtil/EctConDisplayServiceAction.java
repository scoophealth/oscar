package oscar.oscarEncounter.oscarConsultationRequest.config.pageUtil;

import java.io.IOException;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.*;
import oscar.oscarDB.DBHandler;

public class EctConDisplayServiceAction extends Action
{

    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        EctConDisplayServiceForm displayServiceForm = (EctConDisplayServiceForm)form;
        String serviceId = displayServiceForm.getServiceId();
        String specialists[] = displayServiceForm.getSpecialists();
        System.out.println("service id ".concat(String.valueOf(String.valueOf(serviceId))));
        System.out.println("num specs".concat(String.valueOf(String.valueOf(specialists.length))));
        try
        {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = String.valueOf(String.valueOf((new StringBuffer("delete from serviceSpecialists where serviceId = '")).append(serviceId).append("'")));
            db.RunSQL(sql);
            for(int i = 0; i < specialists.length; i++)
            {
                sql = String.valueOf(String.valueOf((new StringBuffer("insert into serviceSpecialists (serviceId,specId) values ('")).append(serviceId).append("','").append(specialists[i]).append("')")));
                db.RunSQL(sql);
            }

            db.CloseConn();
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        EctConConstructSpecialistsScriptsFile constructSpecialistsScriptsFile = new EctConConstructSpecialistsScriptsFile();
        constructSpecialistsScriptsFile.makeString();
        return mapping.findForward("success");
    }
}

