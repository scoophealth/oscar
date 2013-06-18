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
package org.oscarehr.integration.nclass.clientRegistry.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.UUID;

import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.TS;
import org.marc.everest.datatypes.generic.LIST;
import org.marc.everest.rmim.ca.r020403.vocabulary.AcknowledgementCondition;
import org.marc.everest.rmim.ca.r020403.vocabulary.AdministrativeGender;
import org.marc.everest.rmim.ca.r020403.vocabulary.ProcessingID;
import org.marc.everest.rmim.ca.r020403.vocabulary.ResponseMode;

public class Utils {

	public static AdministrativeGender toAdminGender(String sex) {
		if (sex == null || sex.isEmpty()) {
			return AdministrativeGender.Undifferentiated;
		}

		if (sex.charAt(0) == 'M' || sex.charAt(0) == 'm') {
			return AdministrativeGender.Male;
		}

		if (sex.charAt(0) == 'F' || sex.charAt(0) == 'f') {
			return AdministrativeGender.Female;
		}

		return AdministrativeGender.Undifferentiated;
	}
	
	@SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<T> type) {
		Method method = null;
		try {
			ResponseMode responseMode = ResponseMode.Immediate;
			
	        method = type.getDeclaredMethod("defaultInteractionId", new Class[] {});
	        II defaultInteractionId = (II) method.invoke(null, (Object[]) new Class[] {});
	        
	        method = type.getDeclaredMethod("defaultProfileId", new Class[] {});
	        LIST<II> defaultProfileId = (LIST<II>) method.invoke(null, (Object[]) new Class[] {});
	        
	        ProcessingID processingId = ProcessingID.Training; 
	        AcknowledgementCondition acknowledgementCondition = AcknowledgementCondition.Always;
	        
	        Constructor<T> constructor = 
	        		type.getConstructor(II.class, TS.class, ResponseMode.class, II.class, LIST.class, ProcessingID.class, AcknowledgementCondition.class);
	        
	        return constructor.newInstance(new II(UUID.randomUUID()), TS.now(), responseMode, defaultInteractionId, defaultProfileId, processingId, acknowledgementCondition);
        } catch (Exception e) {
        	throw new RuntimeException("Unable to instantiate " + type.getName(), e);
        }
	}

}
