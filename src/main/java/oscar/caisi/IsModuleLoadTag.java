/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 *
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package oscar.caisi;

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

			OscarProperties proper = OscarProperties.getInstance();

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
