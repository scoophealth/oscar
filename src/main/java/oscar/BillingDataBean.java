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
* Description - BillingDataBean
*/ 


/**
* JavaDoc comment for this bean
*/

public class BillingDataBean extends java.lang.Object implements java.io.Serializable
{

/*   *
   * Instance variable for the BillingData properties
   */
   
   protected String billing_no = "";
   protected String clinic_no="";
   protected String demographic_no="";
   protected String appointment_no="";
   protected String provider_no="";
   protected String organization_spec_code="";
   protected String demographic_name="";
   protected String hin="";
   protected String update_date="";
   protected String update_time="";
   protected String billing_date="";
   protected String billing_time="";
   protected String clinic_ref_code="";
   protected String content="";
   protected String total="";
   protected String status="";
   protected String dob="";
   protected String visitdate ="";
   protected String visittype = "";
   protected String provider_ohip_no="";
   protected String provider_rma_no="";


/*   ****************************************************************************
   * Set method for the PARAMETER property
   * 
   * @param value the new value for the property
   */
   public void setBilling_no(String value)
   {
      billing_no = value;
   }

/*   ****************************************************************************
   * Get method for the PARAMETER property
   * 
   * @return the value of the property
   */
   public String getBilling_no()
   {
      return billing_no;
   }

/*   ****************************************************************************
   * Set method for the PARAMETER property
   * 
   * @param value the new value for the property
   */
   public void setClinic_no(String value)
   {
      clinic_no = value;
   }

/*   ****************************************************************************
   * Get method for the PARAMETER property
   * 
   * @return the value of the property
   */
   public String getClinic_no()
   {
      return clinic_no;
   }
   
/*   ****************************************************************************
   * Set method for the PARAMETER property
   * 
   * @param value the new value for the property
   */
   public void setDemographic_no(String value)
   {
      demographic_no = value;
   }

/*   ****************************************************************************
   * Get method for the PARAMETER property
   * 
   * @return the value of the property
   */
   public String getDemographic_no()
   {
      return demographic_no;
   }
   
/*   ****************************************************************************
   * Set method for the PARAMETER property
   * 
   * @param value the new value for the property
   */
   public void setAppointment_no(String value)
   {
      appointment_no = value;
   }

/*   ****************************************************************************
   * Get method for the PARAMETER property
   * 
   * @return the value of the property
   */
   public String getAppointment_no()
   {
      return appointment_no;
   }
/*   ****************************************************************************
   * Set method for the PARAMETER property
   * 
   * @param value the new value for the property
   */
   public void setProviderNo(String value)
   {
      provider_no = value;
   }

/*   ****************************************************************************
   * Get method for the PARAMETER property
   * 
   * @return the value of the property
   */
   public String getProviderNo()
   {
      return provider_no;
   }
/*   ****************************************************************************
   * Set method for the PARAMETER property
   * 
   * @param value the new value for the property
   */
   public void setOrganization_spec_code(String value)
   {
      organization_spec_code = value;
   }

/*   ****************************************************************************
   * Get method for the PARAMETER property
   * 
   * @return the value of the property
   */
   public String getOrganization_spec_code()
   {
      return organization_spec_code;
   }
   
   /*   ****************************************************************************
   * Set method for the PARAMETER property
   * 
   * @param value the new value for the property
   */
   public void setDemographic_name(String value)
   {
      demographic_name = value;
   }

/*   ****************************************************************************
   * Get method for the PARAMETER property
   * 
   * @return the value of the property
   */
   public String getDemographic_name()
   {
      return demographic_name;
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
   public void setUpdate_date(String value)
   {
      update_date = value;
   }

/*   ****************************************************************************
   * Get method for the PARAMETER property
   * 
   * @return the value of the property
   */
   public String getUpdate_date()
   {
      return update_date;
   } 
    /*   ****************************************************************************
   * Set method for the PARAMETER property
   * 
   * @param value the new value for the property
   */
   public void setUpdate_time(String value)
   {
      update_time = value;
   }

/*   ****************************************************************************
   * Get method for the PARAMETER property
   * 
   * @return the value of the property
   */
   public String getUpdate_time()
   {
      return update_time;
   } 
    /*   ****************************************************************************
   * Set method for the PARAMETER property
   * 
   * @param value the new value for the property
   */
   public void setBilling_date(String value)
   {
      billing_date = value;
   }

/*   ****************************************************************************
   * Get method for the PARAMETER property
   * 
   * @return the value of the property
   */
   public String getBilling_date()
   {
      return billing_date;
   } 
    /*   ****************************************************************************
   * Set method for the PARAMETER property
   * 
   * @param value the new value for the property
   */
   public void setBilling_time(String value)
   {
      billing_time = value;
   }

/*   ****************************************************************************
   * Get method for the PARAMETER property
   * 
   * @return the value of the property
   */
   public String getBilling_time()
   {
      return billing_time;
   } 
   
    /*   ****************************************************************************
   * Set method for the PARAMETER property
   * 
   * @param value the new value for the property
   */
   public void setClinic_ref_code(String value)
   {
      clinic_ref_code = value;
   }

/*   ****************************************************************************
   * Get method for the PARAMETER property
   * 
   * @return the value of the property
   */
   public String getClinic_ref_code()
   {
      return clinic_ref_code;
   } 
 /*   ****************************************************************************
   * Set method for the PARAMETER property
   * 
   * @param value the new value for the property
   */
   public void setContent(String value)
   {
      content = value;
   }

/*   ****************************************************************************
   * Get method for the PARAMETER property
   * 
   * @return the value of the property
   */
   public String getContent()
   {
      return content;
   }   
   
     
   /*   ****************************************************************************
   * Set method for the PARAMETER property
   * 
   * @param value the new value for the property
   */
   public void setTotal(String value)
   {
      total = value;
   }

/*   ****************************************************************************
   * Get method for the PARAMETER property
   * 
   * @return the value of the property
   */
   public String getTotal()
   {
      return total;
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
   public void setVisitdate(String value)
   {
     visitdate = value;
   }

/*   ****************************************************************************
   * Get method for the PARAMETER property
   * 
   * @return the value of the property
   */
   public String getVisitdate()
   {
      return visitdate;
   } 
   
   /*   ****************************************************************************
   * Set method for the PARAMETER property
   * 
   * @param value the new value for the property
   */
   public void setVisittype(String value)
   {
      visittype = value;
   }

/*   ****************************************************************************
   * Get method for the PARAMETER property
   * 
   * @return the value of the property
   */
   public String getVisittype()
   {
      return visittype;
   }      
   /*   ****************************************************************************
   * Set method for the PARAMETER property
   * 
   * @param value the new value for the property
   */
   public void setProvider_ohip_no(String value)
   {
      provider_ohip_no = value;
   }

/*   ****************************************************************************
   * Get method for the PARAMETER property
   * 
   * @return the value of the property
   */
   public String getProvider_ohip_no()
   {
      return provider_ohip_no;
   }   
   
    /*   ****************************************************************************
   * Set method for the PARAMETER property
   * 
   * @param value the new value for the property
   */
   public void setProvider_rma_no(String value)
   {
      provider_rma_no = value;
   }

/*   ****************************************************************************
   * Get method for the PARAMETER property
   * 
   * @return the value of the property
   */
   public String getProvider_rma_no()
   {
      return provider_rma_no;
   }  
   
}
