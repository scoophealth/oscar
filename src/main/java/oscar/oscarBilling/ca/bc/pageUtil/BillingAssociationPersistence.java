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

package oscar.oscarBilling.ca.bc.pageUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.oscarehr.common.dao.DiagnosticCodeDao;
import org.oscarehr.common.model.DiagnosticCode;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarDB.DBHandler;

/**
 *
 * <p>Title: BillingAssociationPersistence</p>
 *
 * <p>Description:Responsible for performing database CRUD operations on service code/dxcode associations </p>
 * <p>Involved Database Tables: ctl_servicecodes_dxcodes
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author Joel Legris for Mcmaster University
 * @version 1.0
 */
public class BillingAssociationPersistence {


	private DiagnosticCodeDao diagnosticCodeDao = SpringUtils.getBean(DiagnosticCodeDao.class);


  public BillingAssociationPersistence() {

  }

  /**
   * Saves a ServiceCodeAssociation object to the database
   * @param assoc ServiceCodeAssociation
   * @return boolean
   */
  public void saveServiceCodeAssociation(ServiceCodeAssociation assoc,
                                         String mode) {
    if (mode.equals("edit")) {
      this.deleteServiceCodeAssoc(assoc.getServiceCode());
    }
    List<String> dxcodes = assoc.getDxCodes();
    for (Iterator<String> iter = dxcodes.iterator(); iter.hasNext(); ) {
      String item = iter.next();
      if (!item.equals("")) {
        this.newServiceCodeAssoc(assoc.getServiceCode(),
                                 item);
      }
    }
  }

  /**
   * A new service code association is save to the database
   * @param svc String
   * @param dx String
   * @return boolean
   */
  private boolean newServiceCodeAssoc(String svc, String dx) {
    boolean ret = false;
    String qry =
        "insert into ctl_servicecodes_dxcodes(service_code,dxcode) values('" +
        svc + "','" + dx + "')";

    try {

      ret = DBHandler.RunSQL(qry);

    }
    catch (SQLException ex) {MiscUtils.getLogger().error("Error", ex);
    }
    return ret;
  }

  /**
   * Deletes a service code association from the database
   * @param id String
   * @return boolean
   */
  public boolean deleteServiceCodeAssoc(String svcCode) {
    boolean ret = false;
    String qry = "delete from ctl_servicecodes_dxcodes where service_code = '" +
        svcCode + "'";

    try {

      ret = DBHandler.RunSQL(qry);
    }
    catch (SQLException ex) {MiscUtils.getLogger().error("Error", ex);

    }
    return ret;

  }

  /**
   * Retrieves a list of ServiceCodeAssociation objects
   * @return List
   */
  public List<ServiceCodeAssociation> getServiceCodeAssocs() {
    ArrayList<ServiceCodeAssociation> list = new ArrayList<ServiceCodeAssociation>();

    ResultSet rs = null;
    try {
      String qry = "select * from ctl_servicecodes_dxcodes";

      rs = DBHandler.GetSQL(qry);
      String curSvcCode = "";
      ServiceCodeAssociation assoc = new ServiceCodeAssociation();
      //create the first object to establish an initial reference service code
      while (rs.next()) {

        String svcCode = rs.getString(2);
        String dxcode = rs.getString(3);
        if (!svcCode.equals(curSvcCode)) {
          assoc = new ServiceCodeAssociation();
          assoc.addDXCode(dxcode);
          assoc.setServiceCode(svcCode);
          list.add(assoc);
        }
        else {
          assoc.addDXCode(dxcode);
        }
        curSvcCode = svcCode;
      }
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
    return list;
  }

  /**
   * Returns true if the provided service code is already associated
   * @param code String
   * @return boolean
   */
  public boolean assocExists(String code) {
    String qry =
        "select * from ctl_servicecodes_dxcodes where service_code = '" +
        code + "'";
    return recordExists(qry);
  }

  /**
   * Returns true if the provided dxcode exists in the database
   * @param code String
   * @return boolean
   */
  public boolean dxcodeExists(String code) {
	  List<DiagnosticCode> dcode = diagnosticCodeDao.findByDiagnosticCode(code);
	  if(dcode.size()>0) {
		  return true;
	  }

	  return false;
  }

  /**
   * Returns true if the provided service code exists in the database
   * @param code String
   * @return boolean
   */
  public boolean serviceCodeExists(String code) {
    String qry =
        "select service_code from billingservice where service_code = '" + code +
        "'";
    return recordExists(qry);
  }


  /**
   * Returns true if the specified select query returns a result
   * @param qry String
   * @return boolean
   */
  public boolean recordExists(String qry) {
    boolean ret = false;

    ResultSet rs = null;
    try {

      rs = DBHandler.GetSQL(qry);
      if (rs.next()) {
        ret = true;
      }
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

    return ret;
  }

  /**
   * getServiceCodeAssocByCode
   *
   * @param svcCode String
   */
  public ServiceCodeAssociation getServiceCodeAssocByCode(String svcCode) {
    ServiceCodeAssociation assoc = new ServiceCodeAssociation();

    ResultSet rs = null;
    try {
      String qry =
          "select * from ctl_servicecodes_dxcodes where service_code = '" +
          svcCode + "'";

      rs = DBHandler.GetSQL(qry);

      while (rs.next()) {

        String dxcode = rs.getString(3);
        assoc.addDXCode(dxcode);
        assoc.setServiceCode(svcCode);
      }
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

    return assoc;
  }
}
