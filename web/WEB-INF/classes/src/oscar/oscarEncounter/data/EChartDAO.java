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

import java.sql.*;
import oscar.oscarDB.*;
import oscar.util.*;

public class EChartDAO {
  public EChartDAO() {
  }

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
    finally {
      try {
        db.CloseConn();
      }
      catch (SQLException ex) {
        ex.printStackTrace();
      }
    }
  }

  public Echart getMostRecentEchart(String demographicNo) {
    Echart echart = new Echart();
    try {
      DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
      ResultSet rs;
      String sql = "select * from eChart where demographicNo=" +
          demographicNo
          + " ORDER BY eChartId DESC limit 1";
      rs = db.GetSQL(sql);
      if (rs.next()) {
        echart.setTimeStamp(rs.getTimestamp("timeStamp"));
        echart.setSocialHistory(rs.getString("socialHistory"));
        echart.setFamilyHistory(rs.getString("familyHistory"));
        echart.setMedicalHistory(rs.getString("medicalHistory"));
        echart.setOngoingConcerns(rs.getString("ongoingConcerns"));
        echart.setReminders(rs.getString("reminders"));
        echart.setEncounter(rs.getString("encounter"));
        echart.setSubject(rs.getString("subject"));
        echart.setDemographicNo(String.valueOf(demographicNo));
        echart.setProviderNo(rs.getString("providerNo"));
      }
      rs.close();
      db.CloseConn();
    }
    catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return echart;
  }
}
