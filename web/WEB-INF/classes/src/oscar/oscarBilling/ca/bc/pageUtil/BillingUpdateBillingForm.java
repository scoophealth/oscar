/*
 * BillingUpdateBillingForm.java
 *
 * Created on August 30, 2004, 1:56 PM
 */

package oscar.oscarBilling.ca.bc.pageUtil;

import org.apache.struts.action.ActionForm;

/**
 *
 * @author  root
 */
public class BillingUpdateBillingForm extends ActionForm {

   String status = null;
   String messageNotes = null;
   String billingMasterNo = null;
   String billingNo = null;
  private String billPatient;

  /** Creates a new instance of BillingUpdateBillingForm */
   public BillingUpdateBillingForm() {
   }

   /**
    * Getter for property status.
    * @return Value of property status.
    */
   public java.lang.String getStatus() {
      return status;
   }

   /**
    * Setter for property status.
    * @param status New value of property status.
    */
   public void setStatus(java.lang.String status) {
      this.status = status;
   }

   /**
    * Getter for property messageNotes.
    * @return Value of property messageNotes.
    */
   public java.lang.String getMessageNotes() {
      return messageNotes;
   }

   /**
    * Setter for property messageNotes.
    * @param messageNotes New value of property messageNotes.
    */
   public void setMessageNotes(java.lang.String messageNotes) {
      this.messageNotes = messageNotes;
   }

   /**
    * Getter for property billingMasterNo.
    * @return Value of property billingMasterNo.
    */
   public java.lang.String getBillingMasterNo() {
      return billingMasterNo;
   }

   /**
    * Setter for property billingMasterNo.
    * @param billingMasterNo New value of property billingMasterNo.
    */
   public void setBillingMasterNo(java.lang.String billingMasterNo) {
      this.billingMasterNo = billingMasterNo;
   }

   /**
    * Getter for property billingNo.
    * @return Value of property billingNo.
    */
   public java.lang.String getBillingNo() {
      return billingNo;
   }

  public String getBillPatient() {
    return billPatient;
  }

  /**
    * Setter for property billingNo.
    * @param billingNo New value of property billingNo.
    */
   public void setBillingNo(java.lang.String billingNo) {
      this.billingNo = billingNo;
   }

  public void setBillPatient(String billPatient) {
    this.billPatient = billPatient;
  }

}
