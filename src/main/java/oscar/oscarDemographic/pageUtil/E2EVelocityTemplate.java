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

package oscar.oscarDemographic.pageUtil;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.VelocityUtils;

/**
 * @author Jeremy Ho
 * This class is meant to create a data model of the E2E document in velocity format for E2E Template Exporting
 */

public class E2EVelocityTemplate {
	private static final Logger logger = MiscUtils.getLogger();
	
	private static final String E2E_VELOCITY_TEMPLATE_FILE = "/e2etemplate.vm";
	
	private VelocityContext context;
	
	public E2EVelocityTemplate() {
	}
	
	// Creates the velocity context
	private void loadPatient(Patient record) {
		context = VelocityUtils.createVelocityContextWithTools();
		
		context.put("patient", record);
		//context.put("date", new DateTool());
		
		// Temporary Author/Custodian Hardcode
		context.put("authorId", "hhippocrates");
		context.put("authorIdRoot", "DCCD2C68-389B-44C4-AD99-B8FB2DAD1493");
		context.put("custodianId", "123");
		context.put("custodianIdRoot", "7EEF0BCC-F03E-4742-A736-8BAC57180C5F");
	}
	
	// Assembles the data model & predefined velocity template to yield an E2E document
	public String export(Patient record) throws Exception {
		// Create Data Model
		loadPatient(record);
		
		// Import Template
		InputStream is = null;
		String template = null;
		try {
			is = E2EVelocityTemplate.class.getResourceAsStream(E2E_VELOCITY_TEMPLATE_FILE);
			template = IOUtils.toString(is);
		} finally {
			if (is != null) is.close();
		}
		
		// Merge Template & Data Model
		return VelocityUtils.velocityEvaluate(context, template);
	}
}
