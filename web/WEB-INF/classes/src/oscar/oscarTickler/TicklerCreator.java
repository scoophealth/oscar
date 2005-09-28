/**
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
 * Jason Gallagher
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada   Creates a new instance of LabTag
 *
 *
 * TicklerTag.java
 *
 * Created on May 4, 2005, 11:15 AM
 */
package oscar.oscarTickler;

import java.sql.*;
import java.util.*;

import oscar.oscarDB.*;

public class TicklerCreator {
  public TicklerCreator() {
  }

  /**
   * createTickler
   *
   * @param string String
   * @param provNo int
   * @param reason String
   */
  public void createTickler(String demoNo, String provNo, String message) {
    if (!ticklerExists(demoNo, message)) {
      GregorianCalendar now = new GregorianCalendar();
      int curYear = now.get(Calendar.YEAR);
      int curMonth = (now.get(Calendar.MONTH) + 1);
      int curDay = now.get(Calendar.DAY_OF_MONTH);
      String nowDate = String.valueOf(curYear) + "/" + String.valueOf(curMonth) +
          "/" + String.valueOf(curDay) + " " + now.get(Calendar.HOUR_OF_DAY) +
          ":" + now.get(Calendar.MINUTE) + ":" + now.get(Calendar.SECOND);

      String sql = "insert into tickler (demographic_no, message, status, update_date, service_date, creator, priority, task_assigned_to) " +
          " values(" + demoNo + " ,'" + message + "','A','" + nowDate +
          "',now()," + provNo + ",'4','" + provNo + "')";

      DBHandler db = null;
      try {
        db = new DBHandler(DBHandler.OSCAR_DATA);
        db.RunSQL(sql);
      }
      catch (SQLException ex) {
        ex.printStackTrace();
      }
      finally {
        try {
          db.CloseConn();
        }
        catch (SQLException ex1) {
          ex1.printStackTrace();
        }
      }
    }
  }

  /**
   * Returns true if a tickler with the specified parameters exists
   *
   * @param demoNo String
   * @param provNo String
   * @param message String
   * @return boolean
   */
  private boolean ticklerExists(String demoNo, String message) {
    String sql = "select * from tickler where demographic_no = " + demoNo +
        " and message = '" + message + "'" +
        " and status = 'A'";
    DBHandler db = null;
    ResultSet rs = null;
    try {
      db = new DBHandler(DBHandler.OSCAR_DATA);
     rs = db.GetSQL(sql);
      return rs.next();
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
    }return false;

  }

  public void resolveTickler(String demoNo,String remString) {
    String sql = "update tickler set status = 'D' where demographic_no = '" + demoNo + "'"+
        " and message like '%"+remString+"%'" +
        " and status = 'A'";
   DBHandler db = null;
   try {
     db = new DBHandler(DBHandler.OSCAR_DATA);
     db.RunSQL(sql);
   }
   catch (SQLException ex) {
     ex.printStackTrace();
   }
   finally {
     try {
       db.CloseConn();
     }
     catch (SQLException ex1) {
       ex1.printStackTrace();
     }
   }
  }
}
