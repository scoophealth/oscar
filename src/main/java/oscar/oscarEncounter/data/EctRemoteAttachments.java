/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


package oscar.oscarEncounter.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;

public class EctRemoteAttachments
{

    public EctRemoteAttachments()
    {
        demoNo = null;
        messageIds = null;
        savedBys = null;
        dates = null;
    }

    public void estMessageIds(String demo)
    {
        demoNo = demo;
        messageIds = new ArrayList<String>();
        savedBys = new ArrayList<String>();
        dates = new ArrayList<String>();
        try
        {

            ResultSet rs;
            String sql ="Select * from remoteAttachments where demographic_no = '"+demoNo+"' order by date";
            MiscUtils.getLogger().debug("sql message "+sql);
            rs = DBHandler.GetSQL(sql);
            //for(rs = DBHandler.GetSQL(String.valueOf(String.valueOf((new StringBuilder("SELECT * FROM remoteAttachments WHERE demographic_no = '")).append(demoNo).append("' order by date ")))); rs.next(); dates.add(oscar.Misc.getString(rs,"date")))
            while(rs.next())
	    {
		dates.add(oscar.Misc.getString(rs, "date"));
                messageIds.add(oscar.Misc.getString(rs, "messageid"));
                savedBys.add(oscar.Misc.getString(rs, "savedBy"));
            }

            rs.close();
        }
        catch(SQLException e)
        {
            MiscUtils.getLogger().debug("CrAsH");
        }
    }

    public ArrayList<String> getFromLocation(String messId)
    {
        ArrayList<String> retval = new ArrayList<String>();
        try
        {

            ResultSet rs;
            String sql = "select ocl.locationDesc, mess.thesubject from messagetbl mess, oscarcommlocations ocl where mess.sentByLocation = ocl.locationId and mess.messageid = '"+messId+"' ";
	    MiscUtils.getLogger().debug("sql ="+sql);
	    rs = DBHandler.GetSQL(sql);
//            for(rs = DBHandler.GetSQL(String.valueOf(String.valueOf((new StringBuilder("SELECT ocl.locationDesc, mess.thesubject FROM messagetbl mess, oscarcommlocations ocl where mess.sentByLocation = ocl.locationId and mess.messageid = '")).append(messId).append("'"))));
             while ( rs.next()){
                 retval.add(oscar.Misc.getString(rs, "thesubject"));
                 retval.add(oscar.Misc.getString(rs, "locationDesc"));
 	     }
            rs.close();
        }
        catch(SQLException e)
        {
            MiscUtils.getLogger().debug("CrAsH");
            MiscUtils.getLogger().error("Error", e);
        }
        return retval;
    }

    String demoNo;
    public ArrayList<String> messageIds;
    public ArrayList<String> savedBys;
    public ArrayList<String> dates;
}
