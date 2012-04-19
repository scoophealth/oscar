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


package oscar;

/**
* 
* Description - BillingPatientDataBean
*/ 


/**
* JavaDoc comment for this bean
*/

public class BillingPatientDataBean extends java.lang.Object implements java.io.Serializable
{

/*   *
   * Instance variable for the BillingData properties
   */
   
   protected String demoname = "";
   protected String address="";
   protected String province="";
   protected String postal="";
   protected String city="";
   protected String sex="";
   protected String hin="";
   protected String dob="";
   protected String status="";
   

/*   ****************************************************************************
   * Set method for the PARAMETER property
   * 
   * @param value the new value for the property
   */
   public void setDemoname(String value)
   {
     demoname = value;
   }

/*   ****************************************************************************
   * Get method for the PARAMETER property
   * 
   * @return the value of the property
   */
   public String getDemoname()
   {
      return demoname;
   }

/*   ****************************************************************************
   * Set method for the PARAMETER property
   * 
   * @param value the new value for the property
   */
   public void setAddress(String value)
   {
      address = value;
   }

/*   ****************************************************************************
   * Get method for the PARAMETER property
   * 
   * @return the value of the property
   */
   public String getAddress()
   {
      return address;
   }
   
/*   ****************************************************************************
   * Set method for the PARAMETER property
   * 
   * @param value the new value for the property
   */
   public void setProvince(String value)
   {
      province = value;
   }

/*   ****************************************************************************
   * Get method for the PARAMETER property
   * 
   * @return the value of the property
   */
   public String getProvince()
   {
      return province;
   }
   
/*   ****************************************************************************
   * Set method for the PARAMETER property
   * 
   * @param value the new value for the property
   */
   public void setPostal(String value)
   {
      postal = value;
   }

/*   ****************************************************************************
   * Get method for the PARAMETER property
   * 
   * @return the value of the property
   */
   public String getPostal()
   {
      return postal;
   }
/*   ****************************************************************************
   * Set method for the PARAMETER property
   * 
   * @param value the new value for the property
   */
   public void setCity(String value)
   {
      city = value;
   }

/*   ****************************************************************************
   * Get method for the PARAMETER property
   * 
   * @return the value of the property
   */
   public String getCity()
   {
      return city;
   }

   
    /*   ****************************************************************************
   * Set method for the PARAMETER property
   * 
   * @param value the new value for the property
   */
   public void setHin(String value)
   {
      hin = value;
   }

/*   ****************************************************************************
   * Get method for the PARAMETER property
   * 
   * @return the value of the property
   */
   public String getHin()
   {
      return hin;
   }



   /*   ****************************************************************************
   * Set method for the PARAMETER property
   * 
   * @param value the new value for the property
   */
   public void setStatus(String value)
   {
      status = value;
   }

/*   ****************************************************************************
   * Get method for the PARAMETER property
   * 
   * @return the value of the property
   */
   public String getStatus()
   {
      return status;
   }   
   /*   ****************************************************************************
   * Set method for the PARAMETER property
   * 
   * @param value the new value for the property
   */
   public void setDob(String value)
   {
      dob = value;
   }

/*   ****************************************************************************
   * Get method for the PARAMETER property
   * 
   * @return the value of the property
   */
   public String getDob()
   {
      return dob;
   }
   /*   ****************************************************************************
   * Set method for the PARAMETER property
   * 
   * @param value the new value for the property
   */
   public void setSex(String value)
   {
      sex = value;
   }

/*   ****************************************************************************
   * Get method for the PARAMETER property
   * 
   * @return the value of the property
   */
   public String getSex()
   {
      return sex;
   }

}
