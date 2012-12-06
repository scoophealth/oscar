
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
  
  public String getRepType() {
    return repType;
  }
  public void setRepType(String repType) {
    this.repType = repType;
  }
}
