
package oscar.oscarBilling.ca.bc.MSP;

public class ServiceCodeValidator {
  protected boolean valid = true;
  protected String serviceCode = "";
  protected String description = "";
  public ServiceCodeValidator() {
  }

  public void setValid(boolean valid) {

    this.valid = valid;
  }

  public void setServiceCode(String serviceCode) {
    this.serviceCode = serviceCode;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean isValid() {
    return valid;
  }

  public String getServiceCode() {
    return serviceCode;
  }

  public String getDescription() {
    return description;
  }

}
