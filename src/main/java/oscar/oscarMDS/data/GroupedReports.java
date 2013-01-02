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

package oscar.oscarMDS.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class GroupedReports {

	public String associatedOBR;
	public ArrayList<Results> resultsArray;
	public String timeStamp;
	public List<String> codes;

	GroupedReports(String oBR, String hL7TimeStamp, List<String> codes) {
		associatedOBR = oBR;
		this.codes = codes;
		try {
			GregorianCalendar cal = new GregorianCalendar(Locale.ENGLISH);
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy HH:mm", Locale.ENGLISH);

			// boneheaded calendar numbers months from 0
			cal.set(Integer.parseInt(hL7TimeStamp.substring(0, 4)), Integer.parseInt(hL7TimeStamp.substring(4, 6)) - 1, Integer.parseInt(hL7TimeStamp.substring(6, 8)), Integer.parseInt(hL7TimeStamp.substring(8, 10)), Integer.parseInt(hL7TimeStamp.substring(10, 12)), Integer.parseInt(hL7TimeStamp.substring(12, 14)));

			timeStamp = dateFormat.format(cal.getTime());
		} catch (Exception e) {
			timeStamp = "";
		}
	}

}
