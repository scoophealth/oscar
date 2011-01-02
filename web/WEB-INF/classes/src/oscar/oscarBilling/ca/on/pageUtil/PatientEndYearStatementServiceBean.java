package oscar.oscarBilling.ca.on.pageUtil;

import java.util.Date;

/**
* @author Eugene Katyukhin
*/
public class PatientEndYearStatementServiceBean {
	private String code;
	private String fee;
	public PatientEndYearStatementServiceBean(String code, String fee) {
		super();
		this.code = code;
		this.fee = fee;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	
}
