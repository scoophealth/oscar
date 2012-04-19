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

import java.io.Serializable;

public class BillingItemBean extends Object implements Serializable{
  private String service_code;
  private String desc;
  private String service_value;
  private String percentage;
  private String diag_code;
  private float total;
  private float service_price;
  private String quantity;

  /**
   * Constructor
   */
  public BillingItemBean() {

   total = 0;
   service_price=0;
  }

  public String getService_code() {
    return service_code;
  }

  public synchronized void setService_code(String value) {
    service_code = value;
  }

  public String getDesc() {
    return desc;
  }

  public synchronized void setDesc(String value) {
    desc = value;
  }

    public String getService_value() {
    return service_value;
  }

  public synchronized void setService_value(String value) {
    service_value = value;
  }

  public String getPercentage() {
    return percentage;
  }

   public synchronized void setDiag_code(String value) {
    diag_code = value;
  }

  public String getDiag_code() {
    return diag_code;
  }

  public synchronized void setPercentage(String value) {
    percentage = value;
  }


  public float getService_price() {
    return service_price;
  }

  public synchronized void setService_price(float newPrice) {
    service_price = newPrice;
  }

  public String getQuantity() {
    return quantity;
  }

  public synchronized void setQuantity(String newQuantity) {
    quantity = newQuantity;
  }

  public float getTotal() {
    return total;
  }
    public synchronized void setTotal(float newTotal) {
    total = newTotal;
  }
}
