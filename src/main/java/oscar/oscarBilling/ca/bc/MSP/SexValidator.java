
package oscar.oscarBilling.ca.bc.MSP;

/**
 *
 * <p>Title: SexValidator</p>
 *
 * <p>Description: </p>
 *  Thi validator represents the rules governing an MSP service code
 *  with regards to a patients sex
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class SexValidator
    extends ServiceCodeValidator {
  private String gender = "DEFAULT"; //Not all service codes will have a rule
  private String inputSex = ""; //sex of the patient that is being validated
  /**
   * Creates a new SexValidator
   */
  public SexValidator() {
  }

  /**
   * Creates a new SexValidator initializing the service code and input gender
   * @param svcCode String
   * @param inputSex int
   */
  public SexValidator(String svcCode, String inputSex) {
    this.serviceCode = svcCode;
    this.inputSex = inputSex;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public void setInputSex(String inputSex) {
    this.inputSex = inputSex;
  }

  public String getGender() {
    return gender;
  }

  public String getInputSex() {
    return inputSex;
  }

  public boolean isValid() {
    return gender.equals("DEFAULT")?true:(this.inputSex.equals(this.gender));
  }
}
