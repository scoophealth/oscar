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

import oscar.OscarProperties;

public class IsPropertiesOn {

	public static boolean propertiesOn(String proName) {

		OscarProperties proper = OscarProperties.getInstance();

		if (proper.getProperty(proName, "").equalsIgnoreCase("yes")
				|| proper.getProperty(proName, "").equalsIgnoreCase("true")
				|| proper.getProperty(proName, "").equalsIgnoreCase("on"))
			return true;
		else
			return false;

	}

	public static boolean propertiesOff(String proName) {

		OscarProperties proper = OscarProperties.getInstance();

		if (proper.getProperty(proName, null) == null
				|| proper.getProperty(proName, "").equalsIgnoreCase("off")
				|| proper.getProperty(proName, "").equalsIgnoreCase("false"))
			return true;
		else
			return false;

	}

	public static String getProperty(String proName) {
		OscarProperties proper = OscarProperties.getInstance();
		return proper.getProperty(proName, null);
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
}
