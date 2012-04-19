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


package oscar.oscarTickler;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;
import oscar.oscarDemographic.data.DemographicNameAgeString;
import oscar.util.UtilDateUtilities;

/**
 *
 * @author Jay Gallagher
 */
public class TicklerWorker extends Thread {

    public String provider = null;
    public String ticklerMessage = null;
    public String status = TicklerData.ACTIVE;
    public String priority = TicklerData.NORMAL;

    public TicklerWorker() {

    }

    public void run() {

		LoggedInInfo.setLoggedInInfoToCurrentClassAndMethod();

        try {
            TicklerData td = new TicklerData();
            
            String sql = "select * from consultationRequests where to_days(now()) - to_days(referalDate) > 14 and status = '1' and providerNo = '" + provider + "' ";

            ResultSet rs = DBHandler.GetSQL(sql);
            while (rs.next()) {
                String demo = oscar.Misc.getString(rs, "demographicNo");
                String date = oscar.Misc.getString(rs, "referalDate");

                ticklerMessage = DemographicNameAgeString.getInstance().getNameAgeString(demo) + " has an Consultation Request with a status of 'Nothing Done'. Referral Date was " + date;
                if (!td.hasTickler(demo, provider, ticklerMessage)) {
                    td.addTickler(demo, ticklerMessage, status, UtilDateUtilities.getToday("yyyy-MM-dd"), "0", priority, provider);
                }
            }
            rs.close();
        }
        catch (SQLException e) {
            MiscUtils.getLogger().error("Error", e);
        }
        finally {
    		LoggedInInfo.loggedInInfo.remove();
            DbConnectionFilter.releaseAllThreadDbResources();
        }
        /// check to see if tickler is needed
        //if so, check to see if one is already added
        //if so add it

    }
}
