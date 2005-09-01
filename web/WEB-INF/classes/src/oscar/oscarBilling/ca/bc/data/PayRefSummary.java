package oscar.oscarBilling.ca.bc.data;

import java.text.NumberFormat;

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
  private double cheque=0.0;
  private double visa=0.0;
  private double mc=0.0;
  private double amex=0.0;
  private double electronic=0.0;
  private double debit=0.0;
  private double other=0.0;
  public PayRefSummary() {
  }

  /**
   * Increments the value of the specified payment type, with the supplied
   * String reprentation of a double value
   * @param item String
   * @param strValue String
   */
  public void addIncValue(String getPaymentMethod, String strValue) {
    try{
      Double value = new Double(strValue);
      if (getPaymentMethod.equals("1")) {
        this.cash += value.doubleValue();
      }
      else if (getPaymentMethod.equals("2")) {
        this.cheque += value.doubleValue();
      }
      else if (getPaymentMethod.equals("3")) {
        this.visa += value.doubleValue();
      }
      else if (getPaymentMethod.equals("4")) {
        this.mc += value.doubleValue();
      }
      else if (getPaymentMethod.equals("5")) {
        this.amex += value.doubleValue();
      }
      else if (getPaymentMethod.equals("6")) {
        this.electronic += value.doubleValue();
      }
      else if (getPaymentMethod.equals("7")) {
        this.debit += value.doubleValue();
      }
      else if (getPaymentMethod.equals("8")) {
        this.other += value.doubleValue();
      }
    }catch(Exception e){
      e.printStackTrace();
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
    return NumberFormat.getCurrencyInstance().format(cash+cheque+visa+mc+amex+electronic+debit+other);
  }

}
