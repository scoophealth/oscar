package oscar.util.plugin;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import oscar.OscarProperties;

public class SpecialEncounter extends TagSupport
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String moduleName;
	private boolean reverse = false;
	private boolean exactEqual=false;

	public void setModuleName(String moduleName)
	{
		this.moduleName = moduleName;
	}

	public int doStartTag() throws JspException
	{
		try
		{
			HttpServletRequest request = (HttpServletRequest) pageContext
					.getRequest();
			String propFile = request.getContextPath().substring(1)
					+ ".properties";
			String sep = System.getProperty("file.separator");
			String propFileName = System.getProperty("user.home") + sep
					+ propFile;
			OscarProperties proper = OscarProperties.getInstance();
			proper.loader(propFileName);
			if (!isExactEqual()&&(proper.getProperty("specialencounter", "").indexOf(moduleName)>=0)){
							
				if (reverse) return SKIP_BODY;
				else return EVAL_BODY_INCLUDE;
			}else if(isExactEqual()&&proper.getProperty("specialencounter", "").equalsIgnoreCase(moduleName)){
							
				if (reverse) return SKIP_BODY;
				else return EVAL_BODY_INCLUDE;
			}
		} catch (Exception e)
		{
			throw new JspException("Failed to get module load info", e);

		}
		if (reverse) return EVAL_BODY_INCLUDE;
		else return SKIP_BODY ;

	}

	public void setReverse(String reverse)
	{
		this.reverse = "true".equalsIgnoreCase(reverse)
		|| "yes".equalsIgnoreCase(reverse);
	}

	

	public boolean isExactEqual() {
		return exactEqual;
	}

	public void setExactEqual(boolean exactEqual) {
		this.exactEqual = exactEqual;
	}
}