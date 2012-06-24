/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

/**
 * 
 */
package oscar.oscarLab.ca.all.pageUtil;

/**
 * @author dritan
 * 
 */
public class ORUR01Manager {

	/*public static ca.uhn.hl7v2.model.v21.message.ORU_R01 getORUR01_21(Object obj) {
		return (ca.uhn.hl7v2.model.v21.message.ORU_R01) obj;
	}*/

	public static ca.uhn.hl7v2.model.v22.message.ORU_R01 getORUR01_22(Object obj) {
		return (ca.uhn.hl7v2.model.v22.message.ORU_R01) obj;
	}

	public static ca.uhn.hl7v2.model.v23.message.ORU_R01 getORUR01_23(Object obj) {
		return (ca.uhn.hl7v2.model.v23.message.ORU_R01) obj;
	}

	/*public static ca.uhn.hl7v2.model.v24.message.ORU_R01 getORUR01_24(Object obj) {
		return (ca.uhn.hl7v2.model.v24.message.ORU_R01) obj;
	}*/

	public static ca.uhn.hl7v2.model.v25.message.ORU_R01 getORUR01_25(Object obj) {
		return (ca.uhn.hl7v2.model.v25.message.ORU_R01) obj;
	}

	public static ca.uhn.hl7v2.model.v26.message.ORU_R01 getORUR01_26(Object obj) {
		return (ca.uhn.hl7v2.model.v26.message.ORU_R01) obj;
	}

	/**
	 * Removes the dots between the version number and returns the pure integer representation of the version
	 * 
	 * @param version
	 *            the string representation of the lab version, usually 2.5
	 * @return the integer representation of the version, without the decimal
	 */
	public static int getVersion(String version) {

		String v2 = version.replaceAll("(\\n)*\\.(\\n)*", "");
		if (v2.length() > 0)
			return Integer.parseInt(v2);
		return 0;
	}
}
