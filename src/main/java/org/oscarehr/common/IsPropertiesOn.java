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

package org.oscarehr.common;

import oscar.OscarProperties;

public class IsPropertiesOn {

	public static boolean propertiesOn(String proName) {
		OscarProperties proper = OscarProperties.getInstance();

		if (proper.getProperty(proName, "").equalsIgnoreCase("yes") || proper.getProperty(proName, "").equalsIgnoreCase("true") || proper.getProperty(proName, "").equalsIgnoreCase("on"))
			return true;
		else
			return false;

	}

	public static boolean isCaisiEnable() {
		return propertiesOn("caisi");
	}

	public static boolean isProgramEnable() {
		return propertiesOn("program");
	}

	public static boolean isTicklerPlusEnable() {
		return propertiesOn("ticklerplus");
	}
	
	public static boolean isMultisitesEnable() {
		return propertiesOn("multisites");
	}

	public static boolean isProviderFormalizeEnable() {
		return propertiesOn("multioffice.formalize.provider.id");
	}
	
	public static boolean isIndivicaRichTextLetterEnable() {
		return propertiesOn("indivica_rich_text_letter_enabled");
	}
}
