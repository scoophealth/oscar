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

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import oscar.util.StringUtils;

public class AssociateCodesActionForm
    extends ActionForm {
  private String xml_diagnostic_detail1 = "";
  private String xml_diagnostic_detail2 = "";
  private String xml_diagnostic_detail3 = "";
  private String xml_other1 = "";
  private String mode = "";
  private ServiceCodeAssociation svcAssoc;
  public String getXml_diagnostic_detail1() {
    return xml_diagnostic_detail1;
  }

  public void setXml_diagnostic_detail1(String xml_diagnostic_detail1) {
    this.xml_diagnostic_detail1 = xml_diagnostic_detail1;
  }

  public void setXml_other1(String xml_other1) {
    this.xml_other1 = xml_other1;
  }

  public void setXml_diagnostic_detail3(String xml_diagnostic_detail3) {
    this.xml_diagnostic_detail3 = xml_diagnostic_detail3;
  }

  public void setXml_diagnostic_detail2(String xml_diagnostic_detail2) {
    this.xml_diagnostic_detail2 = xml_diagnostic_detail2;
  }

  public void setMode(String mode) {
    this.mode = mode;
  }

  public String getXml_diagnostic_detail2() {
    return xml_diagnostic_detail2;
  }

  public String getXml_diagnostic_detail3() {
    return xml_diagnostic_detail3;
  }

  public String getXml_other1() {
    return xml_other1;
  }

  public String getMode() {
    return mode;
  }

  /**
   * Set the form parameters with the values of the ServiceCodesAssociation
   * @param assoc ServiceCodeAssociation
   */
  public void setServiceCodeAssociation(ServiceCodeAssociation assoc) {
    this.xml_other1 = assoc.getServiceCode();
    List dxcodes = assoc.getDxCodes();
    for (int i = 0; i < dxcodes.size(); i++) {
      if (i == 0) {
        this.xml_diagnostic_detail1 = (String) dxcodes.get(i);
      }
      else if (i == 1) {
        this.xml_diagnostic_detail2 = (String) dxcodes.get(i);
      }
      else if (i == 2) {
        this.xml_diagnostic_detail3 = (String) dxcodes.get(i);
      }
    }
  }

  public ServiceCodeAssociation getSvcAssoc() {
    ServiceCodeAssociation svc = new ServiceCodeAssociation();
    svc.setServiceCode(this.xml_other1);
    svc.addDXCode(this.xml_diagnostic_detail1);
    svc.addDXCode(this.xml_diagnostic_detail2);
    svc.addDXCode(this.xml_diagnostic_detail3);
    return svc;
  }

  public ActionErrors validate(ActionMapping mapping,
                               HttpServletRequest request) {
    ActionErrors errors = new ActionErrors();
    oscar.util.StringUtils ut = new oscar.util.StringUtils();
    if (!StringUtils.isNumeric(this.xml_other1)) {
      errors.add("",
                 new ActionMessage(
                     "oscar.billing.CA.BC.billingBC.error.nullservicecode"));
    }
    if (!StringUtils.isNumeric(this.xml_diagnostic_detail1) &&
        !StringUtils.isNumeric(this.xml_diagnostic_detail2) &&
        !StringUtils.isNumeric(this.xml_diagnostic_detail3)) {
      errors.add("",
                 new ActionMessage(
                     "oscar.billing.CA.BC.billingBC.error.nulldxcodes"));
    }
    return errors;
  }
}
