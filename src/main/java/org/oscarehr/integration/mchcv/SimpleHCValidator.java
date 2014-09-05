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
package org.oscarehr.integration.mchcv;

import org.apache.commons.lang.StringUtils;

public class SimpleHCValidator implements HCValidator {

    private static final String VALID_RESPONSE_CODE = "51"; 
    private static final String NOT_VALID_RESPONSE_CODE = "05";
    
    @Override
    public HCValidationResult validate(String hcNumber, String versionCode) {
        boolean isValid = isValid(hcNumber, versionCode);
        HCValidationResult result = new HCValidationResult();
        if (isValid) {
            result.setResponseCode(VALID_RESPONSE_CODE);
        }
        else {
            result.setResponseCode(NOT_VALID_RESPONSE_CODE);
        }
        return result;
    }
    
    private boolean isValid(String hcNumber, String versionCode) {
        if (hcNumber == null) {
            return false;
        }
        if (hcNumber.length() != 10) {
            return false;
        }
        if (!StringUtils.isNumeric(hcNumber)) {
            return false;
        }
        if (versionCode!=null) {
        	if (versionCode.length()>2 || !StringUtils.isAlpha(versionCode)) {
        		return false;
        	}
        }
        return true;        
    }
}