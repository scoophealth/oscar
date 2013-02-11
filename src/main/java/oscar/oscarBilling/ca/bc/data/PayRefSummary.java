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

package oscar.oscarBilling.ca.bc.data;

import java.text.NumberFormat;

import org.oscarehr.util.MiscUtils;

import oscar.oscarBilling.ca.bc.MSP.MSPReconcile;

/**
 * <p>Title: PayRefSummary</p>
 *
 * <p>Description: </p>
 * Represent a Summary for the payments and refunds report.
 * This class is just for convenience to avoid the awkward
 * grouping calculations that would have been necessary in the report design
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class PayRefSummary {
  private double cash = 0.0;
  private double cheque = 0.0;
  private double visa = 0.0;
  private double mc = 0.0;
  private double amex = 0.0;
  private double electronic = 0.0;
  private double debit = 0.0;
  private double other = 0.0;
  private double adjustmentAmountTotal = 0.0;
  String line = "";
  public PayRefSummary() {
  }

  /**
   * Increments the value of the specified payment type, with the supplied
   * String reprentation of a double value
   * @param paymentMethod String
   * @param value double
   */
  public void addIncValue(String paymentMethod, double value) {
    paymentMethod = paymentMethod == null ? "" : paymentMethod;
    try {

      if (paymentMethod.equals(MSPReconcile.PAYTYPE_CASH)) {
        this.cash += value;
      }
      else if (paymentMethod.equals(MSPReconcile.PAYTYPE_CHEQUE)) {
        this.cheque += value;
      }
      else if (paymentMethod.equals(MSPReconcile.PAYTYPE_VISA)) {
        this.visa += value;
      }
      else if (paymentMethod.equals(MSPReconcile.PAYTYPE_MC)) {
        this.mc += value;
      }
      else if (paymentMethod.equals(MSPReconcile.PAYTYPE_AMEX)) {
        this.amex += value;
      }
      else if (paymentMethod.equals(MSPReconcile.PAYTYPE_ELECTRONIC)) {

        this.electronic += value;
        line+= String.valueOf(electronic) + " " + String.valueOf(value) + "\n";
      }
      else if (paymentMethod.equals(MSPReconcile.PAYTYPE_DEBIT)) {
        this.debit += value;
      }
      else if (paymentMethod.equals(MSPReconcile.PAYTYPE_OTHER)) {
        this.other += value;
      }
      else{
        this.other += value;
      }
    }
    catch (Exception e) {
      MiscUtils.getLogger().error("Error", e);
    }
  }

  public String getCash() {
    return NumberFormat.getCurrencyInstance().format(cash);
  }

  public String getCheque() {
    return NumberFormat.getCurrencyInstance().format(cheque);
  }

  public String getVisa() {
    return NumberFormat.getCurrencyInstance().format(visa);
  }

  public String getMc() {
    return NumberFormat.getCurrencyInstance().format(mc);
  }

  public String getAmex() {
    return NumberFormat.getCurrencyInstance().format(amex);
  }

  public String getElectronic() {
    return NumberFormat.getCurrencyInstance().format(electronic);
  }

  public String getDebit() {
    return NumberFormat.getCurrencyInstance().format(debit);
  }

  public String getOther() {
    return NumberFormat.getCurrencyInstance().format(other);
  }

  public String getTotal() {
    return NumberFormat.getCurrencyInstance().format(getRawTotal());
  }

  public double getRawTotal() {
    return cash + cheque + visa + mc + amex + electronic + debit + other;
  }

  /**
   *
   * @param adjAmt String
   */
  public void addAdjustmentAmount(String adjAmt) {

    try {
      if (adjAmt != null && !"".equals(adjAmt)) {
        Double amt = new Double(adjAmt);
        this.electronic += amt.doubleValue();
      }
    }
    catch (NumberFormatException e) {
      MiscUtils.getLogger().error("Error", e);
    }
  }

  public void setAdjustmentAmountTotal(double adjustmentAmountTotal) {
    this.adjustmentAmountTotal = adjustmentAmountTotal;
  }

  public double getAdjustmentAmountTotal() {
    return adjustmentAmountTotal;
  }

}
