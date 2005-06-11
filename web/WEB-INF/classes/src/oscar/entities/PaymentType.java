package oscar.entities;

public class PaymentType {
  private String id;
  private String paymentType;
  public PaymentType() {
  }
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public String getPaymentType() {
    return paymentType;
  }
  public void setPaymentType(String paymentType) {
    this.paymentType = paymentType;
  }

}
