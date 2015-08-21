/**
 * Copyright (c) 2013-2015. Department of Computer Science, University of Victoria. All Rights Reserved.
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
 * Department of Computer Science
 * LeadLab
 * University of Victoria
 * Victoria, Canada
 */
package org.oscarehr.e2e.util;

import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.io.OutputStream;

import org.junit.Test;
import org.marc.everest.formatters.interfaces.IFormatterGraphResult;
import org.marc.everest.formatters.xml.datatypes.r1.DatatypeFormatter;
import org.marc.everest.formatters.xml.its1.XmlIts1Formatter;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ClinicalDocument;

public class E2EEverestValidatorTest {
	@SuppressWarnings("unused")
	@Test(expected=UnsupportedOperationException.class)
	public void instantiationTest() {
		new E2EEverestValidator();
	}

	@Test
	public void isValidCDATest() {
		XmlIts1Formatter fmtr = new XmlIts1Formatter();
		fmtr.setValidateConformance(true);
		fmtr.getGraphAides().add(new DatatypeFormatter());
		IFormatterGraphResult details = fmtr.graph(new NullOutputStream(), new ClinicalDocument());

		assertFalse(E2EEverestValidator.isValidCDA(details));
	}

	// Creates an OutputStream that does nothing
	public class NullOutputStream extends OutputStream {
		@Override
		public void write(int b) throws IOException {
		}
	}
}
