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


package oscar.oscarDemographic.data;

import java.util.ArrayList;

import oscar.OscarProperties;

public class ProvinceNames extends ArrayList<String> {

    public static ProvinceNames getInstance() {
        return pNames;
    }

    private static boolean isDefined = true;
    static ProvinceNames pNames = new ProvinceNames();

    private ProvinceNames() {
        OscarProperties props = OscarProperties.getInstance();
        if (props.getProperty("province_names") == null || props.getProperty("province_names").equals("")) {
            isDefined = false;
            return;
        }
        String[] pNamesStr = props.getProperty("province_names").split("\\|");
        for (int i = 0; i < pNamesStr.length; i++) {
            add(pNamesStr[i]);
        }
    }

    public boolean isDefined() {
        return isDefined;
    }
}
