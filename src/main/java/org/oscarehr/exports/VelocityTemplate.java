/**
 * Copyright (c) 2013. Department of Family Practice, University of British Columbia. All Rights Reserved.
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
 * Department of Family Practice
 * Faculty of Medicine
 * University of British Columbia
 * Vancouver, Canada
 */
package org.oscarehr.exports;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.VelocityUtils;

/**
 * Abstract data model for velocity based template exports
 * 
 * @author Jeremy Ho
 */
public abstract class VelocityTemplate {
	protected static Logger log = MiscUtils.getLogger();
	protected VelocityContext context = VelocityUtils.createVelocityContextWithTools();

	public VelocityTemplate() {
		super();
	}

	/**
	 * This function populates the template string with the desired template.
	 * It should be called in the constructor of the object and definitely before export.
	 */
	protected abstract void loadTemplate();

	/**
	 * Assembles the data model & predefined velocity template to yield an output string
	 * 
	 * @param p PatientExport object model
	 * @return String of the merged output if successful. Otherwise an empty string is returned.
	 */
	public abstract String export(PatientExport p);
}
