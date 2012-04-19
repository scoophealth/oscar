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

import org.apache.struts.action.ActionForm;

/**
 *
 * @author Jay Gallagher
 */
public class AddReferralDocForm extends ActionForm {
String referral_no;
String last_name;
               
String first_name;
String specialty;
String address1;
String address2;
String city;
String province;
String postal;
String phone;
String fax;
   

   public AddReferralDocForm() {
   }
   
   /**
    * Getter for property referral_no.
    * @return Value of property referral_no.
    */
   public java.lang.String getReferral_no() {
      return referral_no;
   }
   
   /**
    * Setter for property referral_no.
    * @param referral_no New value of property referral_no.
    */
   public void setReferral_no(java.lang.String referral_no) {
      this.referral_no = referral_no;
   }
   
   /**
    * Getter for property last_name.
    * @return Value of property last_name.
    */
   public java.lang.String getLast_name() {
      return last_name;
   }
   
   /**
    * Setter for property last_name.
    * @param last_name New value of property last_name.
    */
   public void setLast_name(java.lang.String last_name) {
      this.last_name = last_name;
   }
   
   /**
    * Getter for property first_name.
    * @return Value of property first_name.
    */
   public java.lang.String getFirst_name() {
      return first_name;
   }
   
   /**
    * Setter for property first_name.
    * @param first_name New value of property first_name.
    */
   public void setFirst_name(java.lang.String first_name) {
      this.first_name = first_name;
   }
   
   /**
    * Getter for property specialty.
    * @return Value of property specialty.
    */
   public java.lang.String getSpecialty() {
      return specialty;
   }
   
   /**
    * Setter for property specialty.
    * @param specialty New value of property specialty.
    */
   public void setSpecialty(java.lang.String specialty) {
      this.specialty = specialty;
   }
   
   /**
    * Getter for property address1.
    * @return Value of property address1.
    */
   public java.lang.String getAddress1() {
      return address1;
   }
   
   /**
    * Setter for property address1.
    * @param address1 New value of property address1.
    */
   public void setAddress1(java.lang.String address1) {
      this.address1 = address1;
   }
   
   /**
    * Getter for property address2.
    * @return Value of property address2.
    */
   public java.lang.String getAddress2() {
      return address2;
   }
   
   /**
    * Setter for property address2.
    * @param address2 New value of property address2.
    */
   public void setAddress2(java.lang.String address2) {
      this.address2 = address2;
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
    * Getter for property postal.
    * @return Value of property postal.
    */
   public java.lang.String getPostal() {
      return postal;
   }
   
   /**
    * Setter for property postal.
    * @param postal New value of property postal.
    */
   public void setPostal(java.lang.String postal) {
      this.postal = postal;
   }
   
   /**
    * Getter for property phone.
    * @return Value of property phone.
    */
   public java.lang.String getPhone() {
      return phone;
   }
   
   /**
    * Setter for property phone.
    * @param phone New value of property phone.
    */
   public void setPhone(java.lang.String phone) {
      this.phone = phone;
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
   
}
