/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.PMmodule.exporter;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.model.IntakeAnswer;
import org.oscarehr.PMmodule.model.IntakeNode;

public class DATISNonClientService extends AbstractIntakeExporter {

	private static final String FILE_PREFIX = "File6";
	
	private static final Logger log = Logger.getLogger(DATISNonClientService.class);
	
	@Override
	protected String exportData() throws ExportException {
		List<IntakeNode> intakeNodes = intake.getNode().getChildren();
		StringBuilder buf = new StringBuilder();
		
		IntakeNode file6Node = null;
		
		for(IntakeNode inode : intakeNodes) {
			if(inode.getLabelStr().startsWith(FILE_PREFIX)) {
				file6Node = inode;
				break;
			}
		}
		
		Set<IntakeAnswer> answers = intake.getAnswers();
		
		for(DATISField field : fields) {
			String fieldName = field.getName();
			String lbl = null;
			for(IntakeAnswer ans : answers) {
				if(ans.getNode().getGrandParent().equals(file6Node)) {
					lbl = ans.getNode().getParent().getLabelStr().toUpperCase();
					if(lbl.startsWith(fieldName)) {
						writeCSV(buf, ans, field);
						//writeData(buf, ans, field);
					}
				}
			}
		}
		
		if(buf.lastIndexOf(",") == -1) {
			return buf.toString();
		}
		
		return buf.substring(0, buf.lastIndexOf(",")).toString();
	}
	
}
