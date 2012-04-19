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


package oscar.oscarMessenger.pageUtil;
import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;

public class MsgDocListForm implements java.io.Serializable {
  private java.util.Vector providerNoVector;
  private java.util.Vector providerFirstName;
  private java.util.Vector providerLastName;

  /**
   * If the vectors used to set up the checkbox list are no itialize this will call
   * setUpVector to init them
   * @return Vector, With strings of provider No
   */
  public java.util.Vector getProviderNoVector(){
    if (providerNoVector == null){
      setUpVector();
    }
    return this.providerNoVector;
  }

  /**
   * If the vectors used to set up the checkbox list are no itialize this will call
   * setUpVector to init them
   * @return Vector, With Strings of providers first names
   */
  public java.util.Vector getProviderFirstName(){
    if (providerFirstName == null){
      setUpVector();
    }
    return this.providerFirstName;
  }

  /**
   * If the vectors used to set up the checkbox list are no itialize this will call
   * setUpVector to init them
   * @return Vector, With Strings of providers last names
   */
  public java.util.Vector getProviderLastName(){
    if (providerLastName == null){
      setUpVector();
    }
    return this.providerLastName;
  }

  /**
   * Used to set the Provider No Vector
   * @param noVector
   */
  public void setProviderNoVector(java.util.Vector noVector){
    this.providerNoVector = noVector;
  }

  /**
   * Used to set the Providers last name
   * @param lName Vector,
   */
  public void setProviderLastName(java.util.Vector lName){
    this.providerLastName = lName;
  }

  /**
   * Used to set the Providers first name
   * @param fName
   */
  public void setProviderFirstName(java.util.Vector fName){
    this.providerFirstName = fName;
  }



  /**
   * Sets up three vectors used in for the CreateMessage.jsp page
   * They are used to init the checkboxes that can be checked to send messages
   */
  private void setUpVector(){
      providerNoVector = new java.util.Vector();
      providerLastName = new java.util.Vector();
      providerFirstName = new java.util.Vector();
      try{
         
         java.sql.ResultSet rs;
         String sql = "select * from provider order by first_name asc";
         rs = DBHandler.GetSQL(sql);
         while (rs.next()) {
            providerNoVector.add(oscar.Misc.getString(rs, "provider_no"));
            providerFirstName.add(oscar.Misc.getString(rs, "first_name"));
            providerLastName.add(oscar.Misc.getString(rs, "last_name"));
         }

       rs.close();

      }catch (java.sql.SQLException e){MiscUtils.getLogger().error("Error", e); }
  }//setUpDaVectorJaySTyle


}//DocListForm
