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

package oscar.oscarEncounter.data;

import java.sql.ResultSet;
import java.sql.SQLException;

import oscar.oscarDB.DBHandler;
import oscar.util.UtilMisc;

/**
 *
 * <p>Description: </p>
 * Performs CRUD operations on an Echart instance
 *
 * @author not attributable
 * @version 1.0
 */
public class EChartDAO {
  public EChartDAO() {
  }

  /**
   * Creates a new echart entry for the specified Echart instance
   * @param echart Echart
   */
  public void addEchartEntry(Echart echart) {
    String qry = "insert into eChart (timeStamp, demographicNo,providerNo,subject,socialHistory,familyHistory,medicalHistory,ongoingConcerns,reminders,encounter) values ('"
        + echart.getTimeStampToString() + "'," + echart.getDemographicNo() +
        ",'" + echart.getProviderNo() + "','" +
        UtilMisc.charEscape(echart.getSubject(), '\'') + "','" +
        UtilMisc.charEscape(echart.getSocialHistory(), '\'') + "','" +
        UtilMisc.charEscape(echart.getFamilyHistory(), '\'') + "','" +
        UtilMisc.charEscape(echart.getMedicalHistory(), '\'') + "','" +
        UtilMisc.charEscape(echart.getOngoingConcerns(), '\'') + "','" +
        UtilMisc.charEscape(echart.getReminders(), '\'') + "','" +
        UtilMisc.charEscape(echart.getEncounter(), '\'') + "')";
    DBHandler db = null;
    try {
      db = new DBHandler(DBHandler.OSCAR_DATA);
      db.RunSQL(qry);
      System.out.println("qry=" + qry);
    }
    catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Retrieve the most recent Echart entry for the specified demographic number
   * @param demographicNo String
   * @return Echart
   */
  public Echart getMostRecentEchart(String demographicNo) {
    Echart echart = null;

    try {
      DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
      ResultSet rs;
      String sql = "select * from eChart where demographicNo=" +
          demographicNo
          + " ORDER BY eChartId DESC";
//          + " ORDER BY eChartId DESC limit 1";
      rs = db.GetSQL(sql);
      if (rs.next()) {
        echart = new Echart();
        echart.setTimeStamp(rs.getTimestamp("timeStamp"));
        echart.setSocialHistory(db.getString(rs,"socialHistory"));
        echart.setFamilyHistory(db.getString(rs,"familyHistory"));
        echart.setMedicalHistory(db.getString(rs,"medicalHistory"));
        echart.setOngoingConcerns(db.getString(rs,"ongoingConcerns"));
        echart.setReminders(db.getString(rs,"reminders"));
        echart.setEncounter(db.getString(rs,"encounter"));
        echart.setSubject(db.getString(rs,"subject"));
        echart.setDemographicNo(String.valueOf(demographicNo));
        echart.setProviderNo(db.getString(rs,"providerNo"));
      }
      rs.close();
    }
    catch (SQLException e) {
      e.printStackTrace();
    }
    return echart;
  }
}
