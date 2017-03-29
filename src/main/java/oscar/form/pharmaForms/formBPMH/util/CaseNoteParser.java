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

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;

/*
 * Author: Dennis Warren 
 * Company: Colcamex Resources
 * Date: November 2014
 * For: UBC Pharmacy Clinic and McMaster Department of Family Medicine
 */
public class CaseNoteParser {
	
	private final static String FAMILY_DR_KEY = "familyphysician";
	private final static String PHONE_KEY = "p";
	private final static String FAX_KEY = "f";
	private final static String WS = "\\W"; // remove whitespace.
	private final static Pattern DEMO_NOTES_PATTERN = Pattern.compile("<unotes>(.+?)</unotes>");
	private final static String COMMA = ",";
	private final static String COLON = ":";
	
	/**
	 * preset key = "familyphysician"
	 * @param note
	 * @return
	 */
	public static String getFamilyDr(String note) {
		return getValue( note, FAMILY_DR_KEY );
	}
	
	/**
	 * preset phone number key = P
	 * @param note
	 * @return
	 */
	public static String getPhoneNumber(String note) {
		return getValue( note, PHONE_KEY );
	}
	
	/**
	 * preset fax number key = k
	 * @param note
	 * @return
	 */
	public static String getFaxNumber(String note) {
		return getValue( note, FAX_KEY );
	}
	
	/**
	 * Enter a note with comma delimited; colon delimited key-value pairs.
	 * Key must be free of spaces and all lower case.
	 * ie: familydr : john smith, p : 456-4874
	 * This is a very simple method and could use expansion.
	 * 
	 * @param note text
	 * @param key relationship to value desired.
	 * @return
	 */
	public static String getValue( String note, final String key ) {

		String value = "";
		Matcher matcher = null;
		String[] keyArray = null;
		
		if( ! StringUtils.isBlank(note) ) {
			
			matcher = DEMO_NOTES_PATTERN.matcher(note);
			if(matcher.find()) {				
				note = matcher.group(1);
			}

			if( note.contains(COMMA) ) {

				String noteArray[] = note.split(COMMA);

				for(int i = 0; i < noteArray.length; i++) {

					if( noteArray[i].contains(COLON) ) {
						
						keyArray = noteArray[i].split(COLON);

						if(keyArray[0].replaceAll(CaseNoteParser.WS, "").equalsIgnoreCase(key)) {
							value = keyArray[1].trim();
						}
					}
				}
				
			} else if (note.contains(COLON)) {
				keyArray = note.split(COLON);

				if(keyArray[0].replaceAll(CaseNoteParser.WS, "").equalsIgnoreCase(key)) {
					value = keyArray[1].trim();
				}
			}

		}

		return value;
		
	} 
	

}
