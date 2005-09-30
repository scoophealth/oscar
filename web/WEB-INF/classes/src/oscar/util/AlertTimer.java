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

package oscar.util;

import java.util.*;
import oscar.OscarProperties;

/**
 *
 * <p>Title:AlertTimer </p>
 *
 * <p>Description: </p>
 * AlertTimer is responsible for managing the execution Schedule of the CDM Reminders(or any other future alerts)
 * at regular intervals.
 * @author not Joel Legris
 * @version 1.0
 */
public class AlertTimer {
  private static AlertTimer alerts = null;
  private static Timer timer;
  private AlertTimer() {
    timer = new Timer(true);

    //triggers alerts 5 seconds after instantiation and every 120 second interval
    timer.scheduleAtFixedRate(new ReminderClass(),5000,120000);
    System.err.println("Loaded Alerts Object");
  }

  public static AlertTimer getInstance() {
    if (alerts == null) {
      alerts = new AlertTimer();
    }
    return alerts;
  }

  /**
   * The helper class whcih is responsible for triggering the alerts
   */
  class ReminderClass
      extends TimerTask {
    public void run() {
      long loadAlerts = System.currentTimeMillis();
      String alertCodes[] = OscarProperties.getInstance().getProperty(
          "CDM_ALERTS").split(",");
      oscar.oscarBilling.ca.bc.MSP.CDMReminderHlp hlp = new oscar.oscarBilling.
          ca.bc.MSP.CDMReminderHlp();
      try {
        hlp.manageCDMTicklers(alertCodes);
      }
      catch (Exception e) {
        e.printStackTrace();
      }
      long finloadAlerts = System.currentTimeMillis();
      System.out.println("LOADED ALERTS" + (finloadAlerts - loadAlerts) * .001);
    }
  }

}
