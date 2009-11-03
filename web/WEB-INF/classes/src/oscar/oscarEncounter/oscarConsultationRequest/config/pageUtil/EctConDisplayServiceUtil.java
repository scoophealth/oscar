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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import oscar.oscarDB.DBHandler;

public class EctConDisplayServiceUtil
{

    public String getServiceDesc(String serId)
    {
        String retval = new String();
        try
        {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = String.valueOf(String.valueOf((new StringBuffer("select serviceDesc from consultationServices where serviceId = '")).append(serId).append("' order by serviceDesc")));
            ResultSet rs = db.GetSQL(sql);
            if(rs.next())
                retval = db.getString(rs,"serviceDesc");
            rs.close();
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return retval;
    }

    public void estSpecialistVector()
    {
        fNameVec = new Vector();
        lNameVec = new Vector();
        proLettersVec = new Vector();
        addressVec = new Vector();
        phoneVec = new Vector();
        faxVec = new Vector();
        websiteVec = new Vector();
        emailVec = new Vector();
        specTypeVec = new Vector();
        specIdVec = new Vector();
        try
        {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "select * from professionalSpecialists order by lName ";
            ResultSet rs;
            for(rs = db.GetSQL(sql); rs.next(); specIdVec.add(db.getString(rs,"specId")))
            {
                fNameVec.add(db.getString(rs,"fName"));
                lNameVec.add(db.getString(rs,"lName"));
                proLettersVec.add(db.getString(rs,"proLetters"));
                addressVec.add(db.getString(rs,"address"));
                phoneVec.add(db.getString(rs,"phone"));
                faxVec.add(db.getString(rs,"fax"));
                websiteVec.add(db.getString(rs,"website"));
                emailVec.add(db.getString(rs,"email"));
                specTypeVec.add(db.getString(rs,"specType"));
            }

            rs.close();
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public Vector getSpecialistInField(String serviceId)
    {
        Vector vector = new Vector();
        try
        {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = String.valueOf(String.valueOf((new StringBuffer("select * from serviceSpecialists where serviceId = '")).append(serviceId).append("'")));
            ResultSet rs;
            for(rs = db.GetSQL(sql); rs.next(); vector.add(db.getString(rs,"specId")));
            rs.close();
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return vector;
    }

    public void estServicesVectors()
    {
        serviceId = new Vector();
        serviceName = new Vector();
        try
        {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "select * from consultationServices where active = '1' order by serviceDesc";
            ResultSet rs;
            for(rs = db.GetSQL(sql); rs.next(); serviceName.add(db.getString(rs,"serviceDesc")))
                serviceId.add(db.getString(rs,"serviceId"));

            rs.close();
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public Vector fNameVec;
    public Vector lNameVec;
    public Vector proLettersVec;
    public Vector addressVec;
    public Vector phoneVec;
    public Vector faxVec;
    public Vector websiteVec;
    public Vector emailVec;
    public Vector specTypeVec;
    public Vector specIdVec;
    public Vector serviceName;
    public Vector serviceId;
}
