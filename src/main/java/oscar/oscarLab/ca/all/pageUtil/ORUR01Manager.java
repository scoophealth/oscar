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
