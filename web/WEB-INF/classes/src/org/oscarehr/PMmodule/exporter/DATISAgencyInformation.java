/*
* 
* Copyright (c) 2001-2009. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/
package org.oscarehr.PMmodule.exporter;

import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.oscarehr.PMmodule.model.IntakeAnswer;

public class DATISAgencyInformation extends AbstractIntakeExporter {

	public DATISAgencyInformation() {
	}
	
	public DATISAgencyInformation(Integer clientId, Integer programId, Integer facilityId) {
		super.setClientId(clientId);
		super.setProgramId(programId);
		super.setFacilityId(facilityId);
	}
	
	@Override
	protected String exportData() throws ExportException {
		Set<IntakeAnswer> answers = intake.getAnswers();
		StringBuilder buf = new StringBuilder();
		int counter = 0;
		for(IntakeAnswer ans : answers) {
			if(counter == fields.size()) {
				break;
			}
			final String lbl = ans.getNode().getLabelStr();
			DATISField found = (DATISField)CollectionUtils.find(fields, new Predicate() {

				@Override
				public boolean evaluate(Object arg0) {
					DATISField field = (DATISField)arg0;
					if(lbl.startsWith(field.getName())) {
						return true;
					}
					return false;
				}
				
			});
			
			if(found != null) {
				buf.append(found.getName() + " = " + ans.getValue() + "\n");
				counter++;
			}
		}
		
		return buf.toString();
	}

}
