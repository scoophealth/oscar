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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * <p>Title: ServiceCodeAssociation</p>
 *
 * <p>Description: Represents an association between one service code and one to many diagnostic codes</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author Joel Legris for Mcmaster University
 * @version 1.0
 */
public class ServiceCodeAssociation {
  private List dxCodes = new ArrayList();
  private String serviceCode;
  public ServiceCodeAssociation() {

  }

  /**
   * Returns a String List of associated diagnostic codes
   * @return List
   */
  public List getDxCodes() {
    return dxCodes;
  }

  /**
   * Returns the service code
   * @return String
   */
  public String getServiceCode() {
    return serviceCode;
  }

  /**
   * Sets a String list of associated diagonostic codes
   * @param dxCodes List
   */
  public void setDxCodes(List dxCodes) {
    this.dxCodes = dxCodes;
  }

  /**
   * Sets the service code
   * @param serviceCode String
   */
  public void setServiceCode(String serviceCode) {
    this.serviceCode = serviceCode;
  }

  /**
   * Associates a diagnostic code with the service code
   * @param code String
   */
  public void addDXCode(String code) {
    this.dxCodes.add(code);
  }

  /**
   * Returns true if this service code is associate with atleast one dx code
   * @return boolean
   */
  public boolean hasDXCodes(){
   return !this.dxCodes.isEmpty();
  }
}
