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

package oscar.oscarBilling.ca.bc.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;
import oscar.util.SqlUtils;

/**
 * Responsible for CRUD operation a user Billing Module Preferences
 * @author not attributable
 * @version 1.0
 */
public class BillingPreferencesDAO {
  public BillingPreferencesDAO() {
  }

  public BillingPreference getUserBillingPreference(String providerNo) {
    BillingPreference pref = null;
    List res = SqlUtils.getBeanList(
        "select * from billing_preferences where providerNo = " +
        providerNo, BillingPreference.class);
    if (!res.isEmpty()) {
      pref = (BillingPreference) res.get(0);
    }
    return pref;
  }

  /**
   * Saves the preferences for a specific user, if a record exists for the specific user,
   * the values in that record are updated otherwise a new record is created
   * @param demographicNo String
   * @return List
   */
  public void saveUserPreferences(BillingPreference pref) {
    
    ResultSet rs = null;
    String recordExistsQRY =
        "SELECT * from billing_preferences where providerNo = " +
        pref.getProviderNo();
    try {
      
      rs = DBHandler.GetSQL(recordExistsQRY);
      if (rs.next()) {
        String updateSQL = "update billing_preferences set referral = " +
            pref.getReferral() + ",defaultPayeeNo = " + pref.getDefaultPayeeNo() +
            " where providerNo = " + pref.getProviderNo();
        DBHandler.RunSQL(updateSQL);
      }
      else {
        String insertSQL =
            "insert into billing_preferences(referral,providerNo,defaultPayeeNo) values(" +
            pref.getReferral() + "," + pref.getProviderNo() + "," + pref.getDefaultPayeeNo() + ")";
        DBHandler.RunSQL(insertSQL);
      }
    }
    catch (SQLException ex) {MiscUtils.getLogger().error("Error", ex);
    }
    finally {
      if (rs != null) {
        try {
          rs.close();
        }
        catch (SQLException ex2) {MiscUtils.getLogger().error("Error", ex2);
        }
      }
    }

  }

}
