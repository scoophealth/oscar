/*******************************************************************************
 * Copyright (c) 2008, 2009 Quatro Group Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU General Public License
 * which accompanies this distribution, and is available at
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * Contributors:
 *     <Quatro Group Software Systems inc.>  <OSCAR Team>
 *******************************************************************************/
package com.quatro.util;

public class HTMLPropertyBean {
	String readOnly;
	String visible;
	String enable;

	public String getEnable() {
		if(enable==null) enable="";
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public String getReadOnly() {
		if(readOnly==null) readOnly="";
		return readOnly;
	}

	public void setReadOnly(String readOnly) {
		this.readOnly = readOnly;
	}

	public String getVisible() {
		if(visible==null) visible="";
		return visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}
}
