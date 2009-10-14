package oscar.caisi;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import oscar.OscarProperties;

public class IsModuleLoadTag extends TagSupport {
	
	private String moduleName;
	private boolean reverse = false;

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public int doStartTag() throws JspException {
		try { 
			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
         
                        
			String propFile = request.getContextPath().substring(1) + ".properties";
                     
			String sep = System.getProperty("file.separator");
          
			String propFileName = System.getProperty("user.home") + sep + propFile;
             
			OscarProperties proper = OscarProperties.getInstance();

//			proper.loader(propFileName);
			if (proper.getProperty(moduleName, "").equalsIgnoreCase("yes") || proper.getProperty(moduleName, "").equalsIgnoreCase("true") || proper.getProperty(moduleName, "").equalsIgnoreCase("on"))
				if (reverse)
					return SKIP_BODY;
				else
					return EVAL_BODY_INCLUDE;
		} catch (Exception e) {
			throw new JspException("Failed to get module load info", e);

		}
		if (reverse)
			return EVAL_BODY_INCLUDE;
		else
			return SKIP_BODY;

	}

	public void setReverse(String reverse) {
		this.reverse = "true".equalsIgnoreCase(reverse) || "yes".equalsIgnoreCase(reverse);
	}
	
}