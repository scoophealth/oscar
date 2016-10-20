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
package org.oscarehr.dashboard.handler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Test;

public class IndicatorTemplateHandlerTest {

	private static IndicatorTemplateHandler templateHandler;
	
	@BeforeClass
	public static void setUpBeforeClass(){
		URL url = Thread.currentThread().getContextClassLoader().getResource("indicatorXMLTemplates/diabetes_hba1c_test.xml");

		try {
			InputStream is = url.openStream();
			templateHandler = new IndicatorTemplateHandler( IOUtils.toByteArray(is) );
			StringBuilder message = new StringBuilder();
			//templateHandler.validate( message );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetIndicatorTemplateDocument() {
		//defail
	}

	@Test
	public void testGetIndicatorTemplateEntity() {
		//defail
	}

	@Test
	public void testGetIndicatorTemplateXML() {
		//defail
	}

}
