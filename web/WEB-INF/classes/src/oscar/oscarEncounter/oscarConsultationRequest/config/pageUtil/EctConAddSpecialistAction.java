// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
// * This software is published under the GPL GNU General Public License. 
// * This program is free software; you can redistribute it and/or 
// * modify it under the terms of the GNU General Public License 
// * as published by the Free Software Foundation; either version 2 
// * of the License, or (at your option) any later version. * 
// * This program is distributed in the hope that it will be useful, 
// * but WITHOUT ANY WARRANTY; without even the implied warranty of 
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
// * along with this program; if not, write to the Free Software 
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
// * 
// * <OSCAR TEAM>
// * This software was written for the 
// * Department of Family Medicine 
// * McMaster Unviersity 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------
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
        address = address.replace('\n',' ');
 	address = address.replace('\r',' ');
	int whichType = addSpecailistForm.getwhichType();
        if(whichType == 1)
            try
            {
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                String sql = String.valueOf(String.valueOf((new StringBuffer("insert into professionalSpecialists (fName,lName,proLetters,address,phone,fax,website,email,specType) values ('")).append(fName).append("','").append(lName).append("','").append(proLetters).append("','").append(address).append("','").append(phone).append("','").append(fax).append("','").append(website).append("','").append(email).append("','").append(specType).append("')")));
                ResultSet rs = db.GetSQL(sql);
                rs.close();
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
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                String sql = String.valueOf(String.valueOf((new StringBuffer("update professionalSpecialists set fName = '")).append(fName).append("',lName = '").append(lName).append("', ").append(" proLetters = '").append(proLetters).append("', address = '").append(address).append("', phone = '").append(phone).append("',").append(" fax = '").append(fax).append("', website = '").append(website).append("', email = '").append(email).append("', specType = '").append(specType).append("' ").append(" where specId = '").append(specId).append("'")));
                ResultSet rs = db.GetSQL(sql);
                rs.close();
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
        addSpecailistForm.setWebsite("");
        addSpecailistForm.setEmail("");
        addSpecailistForm.setSpecType("");
        addSpecailistForm.setSpecId("");
        addSpecailistForm.whichType = 0;
        request.setAttribute("Added", String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(fName)))).append(" ").append(lName))));
        return mapping.findForward("success");
    }
}
