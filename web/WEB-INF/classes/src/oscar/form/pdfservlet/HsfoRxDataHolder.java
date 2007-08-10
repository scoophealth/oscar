package oscar.form.pdfservlet;

import java.util.Collection;
import java.util.Map;

public class HsfoRxDataHolder {
	private Map params;
	private Collection outlines;

	public static class ValueBean {
		private String fullOutline;

		public ValueBean(String str) {
			this.fullOutline = str;
		}

		public String getFullOutline() {
			return fullOutline;
		}

		public void setFullOutline(String fullOutline) {
			this.fullOutline = fullOutline;
		}
	}

	public Map getParams() {
		return params;
	}

	public void setParams(Map params) {
		this.params = params;
	}

	public Collection getOutlines() {
		return outlines;
	}

	public void setOutlines(Collection outlines) {
		this.outlines = outlines;
	}

	

}
