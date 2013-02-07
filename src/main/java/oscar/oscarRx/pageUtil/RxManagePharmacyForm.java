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


/*
 * RxManagePharmacyForm.java
 *
 * Created on September 29, 2004, 3:22 PM
 */

package oscar.oscarRx.pageUtil;

import org.apache.struts.action.ActionForm;

/**
 *
 * @author  Jay Gallagher
 */
public final class RxManagePharmacyForm extends ActionForm {
   
        String    pharmacyAction = null;
        String    ID             = null;
        String    name = null;                                                              
        String    address = null;                                                              
        String    city = null;                                                              
        String    province = null;                                                              
        String    postalCode = null;                                                              
        String    phone1 = null;                                                              
        String    phone2 = null;                                                              
        String    fax = null;                                                              
        String    email = null;
        String    serviceLocationIdentifier = null;
        String    notes = null;
   
   /** Creates a new instance of RxManagePharmacyForm */
   public RxManagePharmacyForm() {
   }
   
   /**
    * Getter for property pharmacyAction.
    * @return Value of property pharmacyAction.
    */
   public java.lang.String getPharmacyAction() {
      return pharmacyAction;
   }
   
   /**
    * Setter for property pharmacyAction.
    * @param pharmacyAction New value of property pharmacyAction.
    */
   public void setPharmacyAction(java.lang.String pharmacyAction) {
      this.pharmacyAction = pharmacyAction;
   }
   
   /**
    * Getter for property ID.
    * @return Value of property ID.
    */
   public java.lang.String getID() {
      return ID;
   }
   
   /**
    * Setter for property ID.
    * @param ID New value of property ID.
    */
   public void setID(java.lang.String ID) {
      this.ID = ID;
   }
   
   /**
    * Getter for property name.
    * @return Value of property name.
    */
   public java.lang.String getName() {
      return name;
   }
   
   /**
    * Setter for property name.
    * @param name New value of property name.
    */
   public void setName(java.lang.String name) {
      this.name = name;
   }
   
   /**
    * Getter for property address.
    * @return Value of property address.
    */
   public java.lang.String getAddress() {
      return address;
   }
   
   /**
    * Setter for property address.
    * @param address New value of property address.
    */
   public void setAddress(java.lang.String address) {
      this.address = address;
   }
   
   /**
    * Getter for property city.
    * @return Value of property city.
    */
   public java.lang.String getCity() {
      return city;
   }
   
   /**
    * Setter for property city.
    * @param city New value of property city.
    */
   public void setCity(java.lang.String city) {
      this.city = city;
   }
   
   /**
    * Getter for property province.
    * @return Value of property province.
    */
   public java.lang.String getProvince() {
      return province;
   }
   
   /**
    * Setter for property province.
    * @param province New value of property province.
    */
   public void setProvince(java.lang.String province) {
      this.province = province;
   }
   
   /**
    * Getter for property postalCode.
    * @return Value of property postalCode.
    */
   public java.lang.String getPostalCode() {
      return postalCode;
   }
   
   /**
    * Setter for property postalCode.
    * @param postalCode New value of property postalCode.
    */
   public void setPostalCode(java.lang.String postalCode) {
      this.postalCode = postalCode;
   }
   
   /**
    * Getter for property phone1.
    * @return Value of property phone1.
    */
   public java.lang.String getPhone1() {
      return phone1;
   }
   
   /**
    * Setter for property phone1.
    * @param phone1 New value of property phone1.
    */
   public void setPhone1(java.lang.String phone1) {
      this.phone1 = phone1;
   }
   
   /**
    * Getter for property phone2.
    * @return Value of property phone2.
    */
   public java.lang.String getPhone2() {
      return phone2;
   }
   
   /**
    * Setter for property phone2.
    * @param phone2 New value of property phone2.
    */
   public void setPhone2(java.lang.String phone2) {
      this.phone2 = phone2;
   }
   
   /**
    * Getter for property fax.
    * @return Value of property fax.
    */
   public java.lang.String getFax() {
      return fax;
   }
   
   /**
    * Setter for property fax.
    * @param fax New value of property fax.
    */
   public void setFax(java.lang.String fax) {
      this.fax = fax;
   }
   
   /**
    * Getter for property email.
    * @return Value of property email.
    */
   public java.lang.String getEmail() {
      return email;
   }
   
   /**
    * Setter for property email.
    * @param email New value of property email.
    */
   public void setEmail(java.lang.String email) {
      this.email = email;
   }
   
   
    /**
    * Getter for service location identifier.
    * @return Value of service location identifier.
    */
   public java.lang.String getServiceLocationIdentifier() {
      return serviceLocationIdentifier;
   }
   
   /**
    * Setter for Service Location Identifier
    * @param serviceLocationIdentifier New value
    */
   public void setServiceLocationIdentifier(java.lang.String serviceLocationIdentifier) {
      this.serviceLocationIdentifier = serviceLocationIdentifier;
   }
   
   /**
    * Getter for property notes.
    * @return Value of property notes.
    */
   public java.lang.String getNotes() {
      return notes;
   }
   
   /**
    * Setter for property notes.
    * @param notes New value of property notes.
    */
   public void setNotes(java.lang.String notes) {
      this.notes = notes;
   }
   
}
