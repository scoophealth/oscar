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
// * McMaster University 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar.oscarEncounter.oscarConsultationRequest.config.pageUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;

public class EctConDisplayServiceUtil
{

    public String getServiceDesc(String serId)
    {
        String retval = new String();
        try
        {
            
            String sql = String.valueOf(String.valueOf((new StringBuilder("select serviceDesc from consultationServices where serviceId = '")).append(serId).append("' order by serviceDesc")));
            ResultSet rs = DBHandler.GetSQL(sql);
            if(rs.next())
                retval = oscar.Misc.getString(rs, "serviceDesc");
            rs.close();
        }
        catch(SQLException e)
        {
            MiscUtils.getLogger().error("Error", e);
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
            
            String sql = "select * from professionalSpecialists order by lName ";
            ResultSet rs;
            for(rs = DBHandler.GetSQL(sql); rs.next(); specIdVec.add(oscar.Misc.getString(rs, "specId")))
            {
                fNameVec.add(oscar.Misc.getString(rs, "fName"));
                lNameVec.add(oscar.Misc.getString(rs, "lName"));
                proLettersVec.add(oscar.Misc.getString(rs, "proLetters"));
                addressVec.add(oscar.Misc.getString(rs, "address"));
                phoneVec.add(oscar.Misc.getString(rs, "phone"));
                faxVec.add(oscar.Misc.getString(rs, "fax"));
                websiteVec.add(oscar.Misc.getString(rs, "website"));
                emailVec.add(oscar.Misc.getString(rs, "email"));
                specTypeVec.add(oscar.Misc.getString(rs, "specType"));
            }

            rs.close();
        }
        catch(SQLException e)
        {
            MiscUtils.getLogger().error("Error", e);
        }
    }

    public Vector getSpecialistInField(String serviceId)
    {
        Vector vector = new Vector();
        try
        {
            
            String sql = String.valueOf(String.valueOf((new StringBuilder("select * from serviceSpecialists where serviceId = '")).append(serviceId).append("'")));
            ResultSet rs;
            for(rs = DBHandler.GetSQL(sql); rs.next(); vector.add(oscar.Misc.getString(rs, "specId")));
            rs.close();
        }
        catch(SQLException e)
        {
            MiscUtils.getLogger().error("Error", e);
        }
        return vector;
    }

    public void estServicesVectors()
    {
        serviceId = new Vector();
        serviceName = new Vector();
        try
        {
            
            String sql = "select * from consultationServices where active = '1' order by serviceDesc";
            ResultSet rs;
            for(rs = DBHandler.GetSQL(sql); rs.next(); serviceName.add(oscar.Misc.getString(rs, "serviceDesc")))
                serviceId.add(oscar.Misc.getString(rs, "serviceId"));

            rs.close();
        }
        catch(SQLException e)
        {
            MiscUtils.getLogger().error("Error", e);
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
