package org.oscarehr.integration.fhir.utils;

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

import org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender;
import org.oscarehr.common.Gender;

public final class EnumMappingUtil {

	public static final AdministrativeGender genderToAdministrativeGender( final Gender gender ) {
		AdministrativeGender genderCode = AdministrativeGender.UNKNOWN;
		switch( gender ) { 
		case M : genderCode = AdministrativeGender.MALE;
			break;
		case F : genderCode = AdministrativeGender.FEMALE;
			break;
		case T : genderCode = AdministrativeGender.OTHER;
			break;
		case U : genderCode = AdministrativeGender.UNKNOWN;
			break;
		default: genderCode = AdministrativeGender.UNKNOWN;
			break;
		}
		return genderCode;
	}
	
	public static final Gender administrativeGenderToGender( final AdministrativeGender gender ) {
		Gender genderCode = Gender.U;
		switch( gender ) { 
		case MALE : genderCode = Gender.M;
			break;
		case FEMALE : genderCode = Gender.F;
			break;
		case OTHER : genderCode = Gender.O;
			break;
		case UNKNOWN : genderCode = Gender.U;
			break;
		default: genderCode = Gender.U;
			break;
		}
		return genderCode;
	}

}
