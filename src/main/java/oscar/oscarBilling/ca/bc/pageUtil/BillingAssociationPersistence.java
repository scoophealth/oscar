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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.oscarehr.billing.CA.BC.dao.CtlServiceCodesDxCodesDao;
import org.oscarehr.billing.CA.BC.model.CtlServiceCodesDxCodes;
import org.oscarehr.common.dao.BillingServiceDao;
import org.oscarehr.common.dao.DiagnosticCodeDao;
import org.oscarehr.common.model.DiagnosticCode;
import org.oscarehr.util.SpringUtils;

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
	private CtlServiceCodesDxCodesDao dao = SpringUtils.getBean(CtlServiceCodesDxCodesDao.class);
	private BillingServiceDao billingServiceDao = (BillingServiceDao)SpringUtils.getBean(BillingServiceDao.class);
	

  public BillingAssociationPersistence() {

  }

  /**
   * Saves a ServiceCodeAssociation object to the database
   * @param assoc ServiceCodeAssociation
   * @param mode
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
	  
	  CtlServiceCodesDxCodes c = new CtlServiceCodesDxCodes();
	  c.setServiceCode(svc);
	  c.setDxCode(dx);
	  dao.persist(c);
	 
	  return true;
  }

  /**
   * Deletes a service code association from the database
   * @param svcCode String
   * @return boolean
   */
  public boolean deleteServiceCodeAssoc(String svcCode) {
	  List<CtlServiceCodesDxCodes> results = dao.findByServiceCode(svcCode);
	  for(CtlServiceCodesDxCodes c:results) {
		  dao.remove(c.getId());
	  }
	  return true;
  }

  /**
   * Retrieves a list of ServiceCodeAssociation objects
   * @return List
   */
  public List<ServiceCodeAssociation> getServiceCodeAssocs() {
    ArrayList<ServiceCodeAssociation> list = new ArrayList<ServiceCodeAssociation>();

   List<CtlServiceCodesDxCodes> results =  dao.findAll();
   String curSvcCode = "";
   ServiceCodeAssociation assoc = new ServiceCodeAssociation();
   
   for(CtlServiceCodesDxCodes c:results) {
	   String svcCode = c.getServiceCode();
       String dxcode = c.getDxCode();
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
   
    return list;
  }

  /**
   * Returns true if the provided service code is already associated
   * @param code String
   * @return boolean
   */
  public boolean assocExists(String code) {
	  if(dao.findByServiceCode(code).size()>0) {
		  return true;
	  }
	  return false;
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
	  if(billingServiceDao.findByServiceCode(code).size()>0) 
		  return true;
    return false;
  }

  /**
   * getServiceCodeAssocByCode
   *
   * @param svcCode String
   */
  public ServiceCodeAssociation getServiceCodeAssocByCode(String svcCode) {
    ServiceCodeAssociation assoc = new ServiceCodeAssociation();

    List<CtlServiceCodesDxCodes> results = dao.findByServiceCode(svcCode);
    for(CtlServiceCodesDxCodes c:results) {
    	String dxcode = c.getDxCode();
        assoc.addDXCode(dxcode);
        assoc.setServiceCode(svcCode);
    }
    return assoc;
  }
}
