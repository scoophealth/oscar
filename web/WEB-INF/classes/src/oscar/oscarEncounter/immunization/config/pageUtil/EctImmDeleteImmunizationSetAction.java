package oscar.oscarEncounter.immunization.config.pageUtil;

import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.*;
import oscar.oscarDB.DBHandler;

public class EctImmDeleteImmunizationSetAction extends Action
{

    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        EctImmDeleteImmunizationSetForm frm = (EctImmDeleteImmunizationSetForm)form;
        String sets[] = frm.getImmuSets();
        StringBuffer stringBuffer = new StringBuffer(" Update config_Immunization set archived = 1 where ");
        for(int i = 0; i < sets.length; i++)
        {
            System.out.println(String.valueOf(String.valueOf((new StringBuffer("set len ")).append(sets.length - 1).append(" i = ").append(i))));
            if(i == sets.length - 1)
                stringBuffer.append("setId = ".concat(String.valueOf(String.valueOf(sets[i]))));
            else
                stringBuffer.append(String.valueOf(String.valueOf((new StringBuffer("setId = ")).append(sets[i]).append(" or "))));
        }

        if(sets.length > 0)
            try
            {
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                String sql = stringBuffer.toString();
                db.RunSQL(sql);
                db.CloseConn();
            }
            catch(SQLException e)
            {
                System.out.println(e.getMessage());
            }
        return mapping.findForward("success");
    }
}
