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

import org.apache.log4j.Logger;
import org.marc.everest.formatters.interfaces.IFormatterGraphResult;
import org.marc.everest.interfaces.IResultDetail;
import org.marc.everest.interfaces.ResultDetailType;

public class E2EEverestValidator {
	private static Logger log = Logger.getLogger(E2EEverestValidator.class.getName());

	E2EEverestValidator() {
		throw new UnsupportedOperationException();
	}

	public static Boolean isValidCDA(IFormatterGraphResult details) {
		Boolean result = true;

		for(IResultDetail dtl : details.getDetails()) {
			if(!EverestUtils.isNullorEmptyorWhitespace(dtl.getMessage())) {
				if(dtl.getType() == ResultDetailType.INFORMATION) {
					log.info(dtl.getMessage());
				} else if (dtl.getType() == ResultDetailType.WARNING) {
					log.warn(dtl.getMessage());
				} else {
					log.error(dtl.getMessage(), dtl.getException());
				}
			}

			result = false;
		}

		return result;
	}
}
