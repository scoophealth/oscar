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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.oscarehr.util.MiscUtils;

import oscar.oscarTickler.TicklerCreator;
import oscar.util.SqlUtils;

public class CDMReminderHlp {
  public CDMReminderHlp() {
  }

  private String[] createCDMCodeArray(List codes) {
    String[] ret = new String[codes.size()];
    for (int i = 0; i < codes.size(); i++) {
      String[] row = (String[]) codes.get(i);
      ret[i] = row[0];
    }
    return ret;
  }

  /**
   * Adds CDM Counselling reminders to the tickler list if the specified provider
   * has patients that need counselling
   * @param provNo String
   */
  public void manageCDMTicklers(String[] alertCodes) throws Exception {
    //get all demographics with a problem that falls within CDM category
    TicklerCreator crt = new TicklerCreator();
    ServiceCodeValidationLogic lgc = new ServiceCodeValidationLogic();
    List cdmServiceCodes = lgc.getCDMCodes();
    alertCodes = createCDMCodeArray(cdmServiceCodes);

    final String remString = "SERVICE CODE";
    List cdmPatients = (List)this.getCDMPatients(alertCodes);
    List cdmPatientNos = extractPatientNos(cdmPatients);
    crt.resolveTicklers(cdmPatientNos, remString);

    for (Iterator iter = cdmPatients.iterator(); iter.hasNext(); ) {
    	MiscUtils.checkShutdownSignaled();
    	
      String[] dxRecord = (String[]) iter.next();
      String demoNo = dxRecord[0];
      String provNo = dxRecord[1];
      String dxcode = dxRecord[2];
      for (Iterator iterb = cdmServiceCodes.iterator(); iterb.hasNext(); ) {
        String[] cdmRecord = (String[]) iterb.next();
        String cdmCode = cdmRecord[0]; //A declared cdm code
        String cdmServiceCode = cdmRecord[1]; //The associated service code for the specified cdm
        //if the specified patient has one one the specified chronic diseases
        if (cdmCode.equals(dxcode)) {
          /**
           * Check If the associated service code was billed in the past calendar year
           */
          int daysPast = lgc.daysSinceCodeLastBilled(demoNo, cdmServiceCode);
          if (daysPast > 365) {
            GregorianCalendar cal = new GregorianCalendar();
            cal.add(cal.DAY_OF_YEAR,-daysPast);
            java.util.Date dateLastBilled = cal.getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
            String newfmt = formatter.format(dateLastBilled);
            String message = remString + " " + cdmServiceCode + " - Last Billed On: " + newfmt;
            crt.createTickler(demoNo, provNo, message);
          }
          else if (daysPast < 0) {
            String message =
                remString + " " + cdmServiceCode + " - Never billed for this patient";
            crt.createTickler(demoNo, provNo, message);
          }
        }
      }
    }
  }

  private List extractPatientNos(List cdmPatients) {
    ArrayList cdmPatientNos = new ArrayList();
    for (Iterator iter = cdmPatients.iterator(); iter.hasNext(); ) {
      String[] item = (String[]) iter.next();
      cdmPatientNos.add(item[0]);
    }
    return cdmPatientNos;
  }

  private Vector getCDMDemoNos(Enumeration demoNos) {
    Vector cdmPatientNos = new Vector();
    while (demoNos.hasMoreElements()) {
      cdmPatientNos.add(demoNos.nextElement());
    }
    return cdmPatientNos;
  }

  /**
   * Returns a String list of demographic numbers for patients that are associated with the
   * specified provider number and who have been diagnosed with a chronic disease
   * @param provNo String
   * @return ArrayList
   */
  private List getCDMPatients(String[] codes) {
    SqlUtils ut = new SqlUtils();

    String qry = "SELECT de.demographic_no,de.provider_no,dxresearch_code FROM dxresearch d, demographic de WHERE de.demographic_no=d.demographic_no " +
        " and d.dxresearch_code ";
    qry += ut.constructInClauseString(codes, true);
    qry +=
        " and status = 'A' and patient_status = 'AC' order by de.demographic_no";
    List lst = ut.getQueryResultsList(qry);
    return lst == null ? new ArrayList() : lst;
  }
}
