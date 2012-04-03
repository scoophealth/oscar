package oscar.oscarBilling.ca.on.data;

import java.util.List;

import org.oscarehr.common.dao.DiagnosticCodeDao;
import org.oscarehr.common.model.DiagnosticCode;
import org.oscarehr.util.SpringUtils;

public class JdbcBillingDxImpl {
	private DiagnosticCodeDao  diagnosticCodeDao = SpringUtils.getBean(DiagnosticCodeDao.class);

	BillingONDataHelp dbObj = new BillingONDataHelp();

	public String getDxDescription(String dxCode) {
		String ret = "";
		List<DiagnosticCode> dcodes = diagnosticCodeDao.findByDiagnosticCode(dxCode);
		for(DiagnosticCode dcode:dcodes) {
			ret = dcode.getDescription();
		}
		return ret;
	}

}
