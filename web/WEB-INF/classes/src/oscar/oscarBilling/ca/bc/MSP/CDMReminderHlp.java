/*
 *
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 * <OSCAR TEAM>
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */

package oscar.oscarBilling.ca.bc.MSP;

import java.sql.*;
import java.util.*;

import oscar.oscarDB.*;
import oscar.oscarTickler.*;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.*;

public class CDMReminderHlp {
  public CDMReminderHlp() {
  }

  /**
   * Adds CDM Counselling reminders to the tickler list if the specified provider
   * has patients that need counselling
   * @param provNo String
   */
  public void manageCDMTicklers(String[] alertCodes) {
    //get all demographics with a problem that falls within CDM category
    Hashtable cdms = (Hashtable)this.getCDMPatients(alertCodes);
    Enumeration demoNos = cdms.keys();
    TicklerCreator crt = new TicklerCreator();
    String remString = "SERVICE CODE: 13050 Reminder";
    while (demoNos.hasMoreElements()) {
      String demoNo = (String) demoNos.nextElement();
      String provNo = (String)cdms.get(demoNo);
      ServiceCodeValidationLogic lgc = new ServiceCodeValidationLogic();
      int daysPast = lgc.daysSinceLast13050(demoNo);
      if (daysPast > 365) {
        String oldfmt = lgc.getDateofLast13050(demoNo);
        String newfmt = "";
        DateFormat formatter = new SimpleDateFormat("yyyyMMdd");

        try {
          java.util.Date newDate = formatter.parse(oldfmt);
          formatter = new SimpleDateFormat("dd-MMM-yy");
          newfmt = formatter.format(newDate);
        }
        catch (ParseException ex) {
          ex.printStackTrace();
        }

        String message = remString + " - Last Billed On: " + newfmt;
        crt.resolveTickler(demoNo, remString);
        crt.createTickler(demoNo, provNo, message);
      }
      else if (daysPast < 0) {
        String message =
            remString + " - Never billed for this patient";
        crt.resolveTickler(demoNo, remString);
        crt.createTickler(demoNo, provNo, message);
      }
      else {
        //This code has been billed for this patient within the last year
        //Resolve unnecessary ticklers
        crt.resolveTickler(demoNo, remString);
      }
    }
  }

  /**
   * Returns a String list of demographic numbers for patients that are associated with the
   * specified provider number and who have been diagnosed with a chronic disease
   * @param provNo String
   * @return ArrayList
   */
  private Map getCDMPatients(String[] codes) {
    Hashtable lst = new Hashtable();
    String qry = "SELECT distinct de.demographic_no,de.provider_no FROM dxresearch d, demographic de WHERE de.demographic_no=d.demographic_no " +
    " and d.dxresearch_code in(";
     for (int i = 0; i < codes.length; i++) {
       qry+=codes[i];
       if(i<codes.length-1){
         qry+=",";
       }
     }
    qry += ") and status = 'A'";
    DBHandler db = null;
    ResultSet rs = null;
    try {
      db = new DBHandler(DBHandler.OSCAR_DATA);
      rs = db.GetSQL(qry);
      while (rs.next()) {
        lst.put(rs.getString(1),rs.getString(2));
      }
    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }
    finally {
      try {
        db.CloseConn();
        rs.close();
      }
      catch (SQLException ex1) {
        ex1.printStackTrace();
      }
    }
    return lst;
  }
}
