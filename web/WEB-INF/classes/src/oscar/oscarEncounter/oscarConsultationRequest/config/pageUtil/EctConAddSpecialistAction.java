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
import oscar.oscarMessenger.util.MsgStringQuote;

public class EctConAddSpecialistAction extends Action
{

    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        System.err.println("Addspecialist Action Jackson");
        EctConAddSpecialistForm addSpecailistForm = (EctConAddSpecialistForm)form;
        String fName = addSpecailistForm.getFirstName();
        String lName = addSpecailistForm.getLastName();
        String proLetters = addSpecailistForm.getProLetters();
        String address = addSpecailistForm.getAddress();
        String phone = addSpecailistForm.getPhone();
        String fax = addSpecailistForm.getFax();
        String website = addSpecailistForm.getWebsite();
        String email = addSpecailistForm.getEmail();
        String specType = addSpecailistForm.getSpecType();
        String specId = addSpecailistForm.getSpecId();
        int whichType = addSpecailistForm.getwhichType();
        MsgStringQuote str = new MsgStringQuote();

        if(whichType == 1)
            try
            {
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                String sql = String.valueOf(String.valueOf((new StringBuffer("insert into professionalSpecialists (fName,lName,proLetters,address,phone,fax,website,email,specType) values ('")).append(str.q(fName)).append("','").append(str.q(lName)).append("','").append(str.q(proLetters)).append("','").append(str.q(address)).append("','").append(str.q(phone)).append("','").append(str.q(fax)).append("','").append(str.q(website)).append("','").append(str.q(email)).append("','").append(str.q(specType)).append("')")));
                db.RunSQL(sql);
                db.CloseConn();
            }
            catch(SQLException e)
            {
                System.out.println(e.getMessage());
            }
        else
        if(whichType == 2)
        {
            try
            {
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);                String sql = String.valueOf(String.valueOf((new StringBuffer("update professionalSpecialists set fName = '")).append(str.q(fName)).append("',lName = '").append(str.q(lName)).append("', ").append(" proLetters = '").append(str.q(proLetters)).append("', address = '").append(str.q(address)).append("', phone = '").append(str.q(phone)).append("',").append(" fax = '").append(str.q(fax)).append("', website = '").append(str.q(website)).append("', email = '").append(str.q(email)).append("', specType = '").append(str.q(specType)).append("' ").append(" where specId = '").append(specId).append("'")));
                db.RunSQL(sql);
                db.CloseConn();
            }
            catch(SQLException e)
            {
                System.out.println(e.getMessage());
            }
            return mapping.findForward("edited");
        }
        addSpecailistForm.setFirstName("");
        addSpecailistForm.setLastName("");
        addSpecailistForm.setProLetters("");
        addSpecailistForm.setAddress("");
        addSpecailistForm.setPhone("");
        addSpecailistForm.setFax("");
        addSpecailistForm.setWebsite("");        addSpecailistForm.setEmail("");        addSpecailistForm.setSpecType("");        addSpecailistForm.setSpecId("");
        addSpecailistForm.whichType = 0;        request.setAttribute("Added", String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(fName)))).append(" ").append(lName))));
        return mapping.findForward("success");
    }
}


