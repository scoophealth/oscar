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


package oscar.oscarBilling.ca.bc.Teleplan;

import java.util.Arrays;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;

/**
 *
 * @author jaygallagher
 */
public class WCBCodes {

    private static Logger log = MiscUtils.getLogger();
    private static WCBCodes wcbCodes = new WCBCodes();
    String[] formRequiredCodes = null;

    /**
     * @return WCBCodes the instance of WCBCodes
     */
    public static WCBCodes getInstance() {
        return wcbCodes;
    }

    /* Do not use this constructor. Use getInstance instead */
    private WCBCodes() {
        log.debug("WCBCodes CONSTRUCTOR");
    }

    public String[] getFormCodes() {
        if (formRequiredCodes == null) {
            String wcbFormNeededCodes = OscarProperties.getInstance().getProperty("WCB_FORM_REQUIRED_CODES", "19937,19938,19939,19940,19941,19943,19944,19167,19173,19174,19175,19134,19135");
            formRequiredCodes = wcbFormNeededCodes.split(",");
            Arrays.sort(formRequiredCodes);
        }
        return formRequiredCodes;
    }

    public boolean isFormNeeded(String wcbCode) {
        String[] codes = getFormCodes();
        log.debug("codes "+codes+"  == "+wcbCode+"  - "+Arrays.binarySearch(codes, wcbCode));
        if (Arrays.binarySearch(codes, wcbCode) < 0) {
            return false;
        }
        return true;
    }

    @Override
     public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
