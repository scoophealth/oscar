package oscar.oscarBilling.ca.bc.MSP;

import java.util.Vector;

public class AgeValidator
    extends ServiceCodeValidator {
  private int maxAge = 150;
  private int minAge = 0;
  private int inputAge;
  public AgeValidator() {
  }
  public AgeValidator(String svcCode,int inputAge) {
    this.serviceCode = svcCode;
    this.inputAge = inputAge;
  }
  public String getDescription(){
    Vector v = new Vector();

    return minAge + " - " + maxAge;
  }

  public void setMaxAge(int maxAge) {
    this.maxAge = maxAge;
  }

  public void setMinAge(int minAge) {
    this.minAge = minAge;
  }

  public void setInputAge(int inputAge) {
    this.inputAge = inputAge;
  }

  public int getMaxAge() {
    return maxAge;
  }

  public int getMinAge() {
    return minAge;
  }

  public int getInputAge() {
    return inputAge;
  }

  public boolean isValid(){
    return (this.inputAge >= minAge && this.inputAge<= maxAge);
  }
}
