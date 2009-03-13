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

import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.model.IntakeAnswer;
import org.oscarehr.PMmodule.model.IntakeNode;

public class DATISGamingForm extends AbstractIntakeExporter {

	private static final Logger log = Logger.getLogger(DATISGamingForm.class);
	
	private static final String FILE_PREFIX = "File5";
	
	public DATISGamingForm() {}
	
	public DATISGamingForm(Integer clientId, Integer programId, Integer facilityId) {
		super.setClientId(clientId);
		super.setProgramId(programId);
		super.setFacilityId(facilityId);
	}
	
	@Override
	protected String exportData() throws ExportException {
		List<IntakeNode> intakeNodes = intake.getNode().getChildren();
		StringBuilder buf = new StringBuilder();
		
		IntakeNode file5Node = null;
		
		for(IntakeNode inode : intakeNodes) {
			if(StringUtils.deleteWhitespace(inode.getLabelStr()).startsWith(FILE_PREFIX)) {
				file5Node = inode;
				break;
			}
		}
		
		Set<IntakeAnswer> answers = intake.getAnswers();
		
		for(DATISField field : fields) {
			String fieldName = field.getName();
			String fieldQuestion = field.getQuestion();
			String lbl = null;
			for(IntakeAnswer ans : answers) {
				if(ans.getNode().getGrandParent().equals(file5Node)) {
					lbl = ans.getNode().getParent().getLabelStr().toUpperCase();
					lbl = StringUtils.deleteWhitespace(lbl);
					fieldQuestion = StringUtils.deleteWhitespace(fieldQuestion);
					if(lbl.equals(fieldQuestion)) {
						buf.append(fieldName + " = " + ans.getValue() + "\n");
						//writeData(buf, ans, field);
					}
				}
			}
		}
		
		return buf.toString();
	}

}
