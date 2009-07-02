package oscar.util.plugin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import oscar.OscarProperties;

public class SpecialPlugins extends TagSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String moduleName;
	private boolean reverse = false;

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public boolean propertiesOn(String proName, OscarProperties proper) {

		if (proper.getProperty(proName, "").equalsIgnoreCase("yes")
				|| proper.getProperty(proName, "").equalsIgnoreCase("true")
				|| proper.getProperty(proName, "").equalsIgnoreCase("on"))
			return true;
		else
			return false;

	}

	public int doStartTag() throws JspException {
		String[] mNameArray = moduleName.split(",");
		boolean flag=false;
		try {
			HttpServletRequest request = (HttpServletRequest) pageContext
					.getRequest();
			String propFile = request.getContextPath().substring(1)
					+ ".properties";
			String sep = System.getProperty("file.separator");
			String propFileName = System.getProperty("user.home") + sep
					+ propFile;
			OscarProperties proper = OscarProperties.getInstance();
			proper.loader(propFileName);
			
			for (int i = 0; i < mNameArray.length; i++) {
				String mname=mNameArray[i];
				if (propertiesOn(mname, proper)) {
					flag=true;
				}
			}
			
		} catch (Exception e) {
			throw new JspException("Failed to get module load info", e);

		}
		if (reverse&&!flag || !reverse&&flag)
			return EVAL_BODY_INCLUDE;
		else
			return SKIP_BODY;

	}

	public void setReverse(String reverse) {
		this.reverse = "true".equalsIgnoreCase(reverse)
				|| "yes".equalsIgnoreCase(reverse);
	}
}
