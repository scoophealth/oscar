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
import java.util.Date;
import java.util.List;

import org.caisi.dao.TicklerDAO;
import org.caisi.model.Tickler;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarDB.DBHandler;

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
    	Tickler t = new Tickler();
    	t.setDemographic_no(demoNo);
    	t.setMessage(message);
    	t.setStatus('A');
    	t.setUpdate_date(new Date());
    	t.setService_date(new Date());
    	t.setCreator(provNo);
    	t.setPriority("4");
    	t.setTask_assigned_to(provNo);
    	TicklerDAO dao = (TicklerDAO)SpringUtils.getBean("ticklerDAOT");
    	dao.saveTickler(t);
    	

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
  public boolean ticklerExists(String demoNo, String message) {
    String sql = "select * from tickler where demographic_no = " + demoNo +
        " and message = '" + message + "'" +
        " and status = 'A'";
    
    ResultSet rs = null;
    try {
      
      rs = DBHandler.GetSQL(sql);
      return rs.next();
    }
    catch (SQLException ex) {MiscUtils.getLogger().error("Error", ex);
    }
    finally {
      try {
        rs.close();
      }
      catch (SQLException ex1) {MiscUtils.getLogger().error("Error", ex1);
      }
    }
    return false;

  }

  /**
   * resolveTicklers
   *
   * @param cdmPatientNos Vector
   * @param remString String
   */
  public void resolveTicklers(List cdmPatientNos, String remString) {
    String qry = "delete from tickler where demographic_no in(";
    for (int i = 0; i < cdmPatientNos.size(); i++) {
      qry += cdmPatientNos.get(i);
      if (i < cdmPatientNos.size() - 1) {
        qry += ",";
      }
    }
    qry += cdmPatientNos.size()==0 ? "0" : "";

    qry += ") and message like '%" + remString + "%' and status = 'A'";
    

    try {
      
      DBHandler.RunSQL(qry);
    }
    catch (SQLException ex) {MiscUtils.getLogger().error("Error", ex);
    }
  }
}
