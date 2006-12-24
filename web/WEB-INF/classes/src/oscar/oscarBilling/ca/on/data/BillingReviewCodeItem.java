package oscar.oscarBilling.ca.on.data;

public class BillingReviewCodeItem {
	String codeName;
	String codeUnit;
	String codeFee;
	String codeTotal;
	String msg;
	public String getCodeFee() {
		return codeFee;
	}
	public void setCodeFee(String codeFee) {
		this.codeFee = codeFee;
	}
	public String getCodeName() {
		return codeName;
	}
	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}
	public String getCodeUnit() {
		return codeUnit;
	}
	public void setCodeUnit(String codeUnit) {
		this.codeUnit = codeUnit;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getCodeTotal() {
		return codeTotal;
	}
	public void setCodeTotal(String codeTotal) {
		this.codeTotal = codeTotal;
	}
}
