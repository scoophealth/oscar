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
package org.oscarehr.integration.hl7.generators;

import org.oscarehr.common.model.Demographic;

import org.oscarehr.common.hl7.v2.HL7A04Data;

import org.oscarehr.util.MiscUtils;
import org.apache.log4j.Logger;


public class HL7A04Generator {
	
	Logger logger = MiscUtils.getLogger();
	
	public HL7A04Generator() {
	}
	
	/**
      * method generateHL7A04
      * 
      * Creates an HL7 A04 object, and then saves it to the disk.
      * 
      * @param demo The Demographic object used to create the HL7 A04 object.
      */ 
     public void generateHL7A04(Demographic demo) {
		try {
			// generate A04 HL7
			HL7A04Data A04Obj = new HL7A04Data(demo);
			A04Obj.save();
		} catch (Exception e) {
			logger.error("Unable to generate HL7 A04 file", e);
		}
	 }
}
