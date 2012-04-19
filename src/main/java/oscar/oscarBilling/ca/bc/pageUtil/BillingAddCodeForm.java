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

public final class BillingAddCodeForm extends ActionForm {

   String codeId;
   String code;
   String desc; 
   String value;
   String whereTo;
   
   /** Creates a new instance of BillingEditCodeForm */
   public BillingAddCodeForm() {
   }
   
   /**
    * Getter for property codeId.
    * @return Value of property codeId.
    */
   public java.lang.String getCodeId() {
      return codeId;
   }
   
   /**
    * Setter for property codeId.
    * @param codeId New value of property codeId.
    */
   public void setCodeId(java.lang.String codeId) {
      this.codeId = codeId;
   }
   
   /**
    * Getter for property code.
    * @return Value of property code.
    */
   public java.lang.String getCode() {
      return code;
   }
   
   /**
    * Setter for property code.
    * @param code New value of property code.
    */
   public void setCode(java.lang.String code) {
      this.code = code;
   }
   
   /**
    * Getter for property desc.
    * @return Value of property desc.
    */
   public java.lang.String getDesc() {
      return desc;
   }
   
   /**
    * Setter for property desc.
    * @param desc New value of property desc.
    */
   public void setDesc(java.lang.String desc) {
      this.desc = desc;
   }
   
   /**
    * Getter for property value.
    * @return Value of property value.
    */
   public java.lang.String getValue() {
      return value;
   }
   
   /**
    * Setter for property value.
    * @param value New value of property value.
    */
   public void setValue(java.lang.String value) {
      this.value = value;
   }
   
   /**
    * Getter for property whereTo.
    * @return Value of property whereTo.
    */
   public java.lang.String getWhereTo() {
      return whereTo;
   }
   
   /**
    * Setter for property whereTo.
    * @param whereTo New value of property whereTo.
    */
   public void setWhereTo(java.lang.String whereTo) {
      this.whereTo = whereTo;
   }
   
}
