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


package oscar.oscarBilling.ca.bc.MSP;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class CreateBillingReportActionForm extends ActionForm {
  private String docFormat;
  private String selAccount;
  private String selPayee;
  private String selProv;
  private String showICBC;
  private String showMSP;
  private String showPRIV;
  private String showWCB;
  private String verCode;
  private String xml_appointment_date;
  private String xml_vdate;
  private String repType;
  public String getDocFormat() {
    return docFormat;
  }
  public void setDocFormat(String docFormat) {
    this.docFormat = docFormat;
  }
  public String getSelAccount() {
    return selAccount;
  }
  public void setSelAccount(String selAccount) {
    this.selAccount = selAccount;
  }
  public String getSelPayee() {
    return selPayee;
  }
  public void setSelPayee(String selPayee) {
    this.selPayee = selPayee;
  }
  public String getSelProv() {
    return selProv;
  }
  public void setSelProv(String selProv) {
    this.selProv = selProv;
  }
  public String getShowICBC() {
    return showICBC;
  }
  public void setShowICBC(String showICBC) {
    this.showICBC = showICBC;
  }
  public String getShowMSP() {
    return showMSP;
  }
  public void setShowMSP(String showMSP) {
    this.showMSP = showMSP;
  }
  public String getShowPRIV() {
    return showPRIV;
  }
  public void setShowPRIV(String showPRIV) {
    this.showPRIV = showPRIV;
  }
  public String getShowWCB() {
    return showWCB;
  }
  public void setShowWCB(String showWCB) {
    this.showWCB = showWCB;
  }
  public String getVerCode() {
    return verCode;
  }
  public void setVerCode(String verCode) {
    this.verCode = verCode;
  }
  public String getXml_appointment_date() {
    return xml_appointment_date;
  }
  public void setXml_appointment_date(String xml_appointment_date) {
    this.xml_appointment_date = xml_appointment_date;
  }
  public String getXml_vdate() {
    return xml_vdate;
  }
  public void setXml_vdate(String xml_vdate) {
    this.xml_vdate = xml_vdate;
  }
  public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest httpServletRequest) {
    /**@todo: finish this method, this is just the skeleton.*/
    return null;
  }
  public void reset(ActionMapping actionMapping, HttpServletRequest httpServletRequest) {
  }
  public String getRepType() {
    return repType;
  }
  public void setRepType(String repType) {
    this.repType = repType;
  }
}
