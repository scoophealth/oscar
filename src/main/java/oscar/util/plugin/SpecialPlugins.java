/**
 * Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package oscar.util.plugin;

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
			OscarProperties proper = OscarProperties.getInstance();
			
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
