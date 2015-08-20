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
package oscar.form.pharmaForms.formBPMH.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oscar.form.pharmaForms.formBPMH.bean.BpmhDrug;

/*
 * Author: Dennis Warren 
 * Company: Colcamex Resources
 * Date: November 2014
 * For: UBC Pharmacy Clinic and McMaster Department of Family Medicine
 */

public class SortDrugList {
	
	public static void byPositionOrder (List<BpmhDrug> drugList) {		
		Collections.sort(drugList, byPositionOrder);
	}
	
	private static Comparator<BpmhDrug> byPositionOrder = new Comparator<BpmhDrug>() {

		@Override
		public int compare(BpmhDrug s1, BpmhDrug s2) {

			String patternString = "\\[([1-9]?[1-9]*?)\\]";
			Pattern pattern = Pattern.compile(patternString);

			String n1String = s1.getPosition();
			String n2String = s2.getPosition();
			
			Integer n1Integer = 0;
			Integer n2Integer = 0;

			Matcher n1matcher = pattern.matcher(n1String);
			Matcher n2matcher = pattern.matcher(n2String);

			if(n1matcher.find()) {
				n1Integer = Integer.parseInt(n1matcher.group(1));
			}
			
			if(n2matcher.find()){
				n2Integer = Integer.parseInt(n2matcher.group(1));
			}
									
			if (n1Integer == 0) {
				return -1;
			}

			if (n2Integer == 0) {
				return -1;
			}
			
			return n1Integer.compareTo(n2Integer);
		}

	};
	
}
