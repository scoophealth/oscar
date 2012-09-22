/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package org.oscarehr.common.printing;

import com.lowagie.text.pdf.BaseFont;

public class FontSettings {

	public static final FontSettings HELVETICA_6PT = new FontSettings();
	public static final FontSettings HELVETICA_10PT = new FontSettings();
	public static final FontSettings HELVETICA_12PT = new FontSettings();
	static {
		HELVETICA_6PT.fontSize = 6;
		HELVETICA_10PT.fontSize = 10;
		HELVETICA_12PT.fontSize = 12;
	}

	private String font = BaseFont.HELVETICA;
	private String codePage = BaseFont.WINANSI;
	private boolean embedded = BaseFont.NOT_EMBEDDED;
	private int fontSize;

	public FontSettings() {
	}

	public FontSettings(String font, String codePage, boolean embedded, int fontSize) {
		this.font = font;
		this.codePage = codePage;
		this.embedded = embedded;
		this.fontSize = fontSize;
	}

	/**
	 * Creates the specified font encapsulated by this instance
	 * 
	 * @return
	 * 		Returns new base font instance
	 */
	public BaseFont createFont() {
		try {
			return BaseFont.createFont(getFont(), getCodePage(), isEmbedded());
		} catch (Exception e) {
			throw new RuntimeException("Unable to create font", e);
		}
	}

	public int getFontSize() {
		return fontSize;
	}

	public String getFont() {
		return font;
	}

	public String getCodePage() {
		return codePage;
	}

	public boolean isEmbedded() {
		return embedded;
	}

}
