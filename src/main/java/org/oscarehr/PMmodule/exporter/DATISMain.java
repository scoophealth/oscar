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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.model.IntakeAnswer;
import org.oscarehr.PMmodule.model.IntakeNode;

public class DATISMain extends AbstractIntakeExporter {

	private static final Logger log = Logger.getLogger(DATISMain.class);
	
	private static final String FILE_PREFIX_MAIN = "Main";
	private static final String FILE_PREFIX_GAMBLING = "GAMBLING";
	
	@Override
	protected String exportData() throws ExportException {
		List<IntakeNode> intakeNodes = intake.getNode().getChildren();
		StringBuilder buf = new StringBuilder();
		
		IntakeNode mainNode = null;
		IntakeNode gamblingNode = null;
		
		for(IntakeNode inode : intakeNodes) {
			if(inode.getLabelStr().startsWith(FILE_PREFIX_MAIN)) {
				mainNode = inode;
			} else if(inode.getLabelStr().startsWith(FILE_PREFIX_GAMBLING)) {
				gamblingNode = inode;
			}
		}
		
		Set<IntakeAnswer> answers = intake.getAnswers();
		
		int counter = 0;
		for(IntakeAnswer ans : answers) {
			if(counter == fields.size()) {
				break;
			}
			if(ans.getNode().getGrandParent().equals(mainNode) || ans.getNode().getGrandParent().equals(gamblingNode)) {
				final String lbl = ans.getNode().getParent().getLabelStr().toUpperCase();
				DATISField found = (DATISField)CollectionUtils.find(fields, new Predicate() {
	
					public boolean evaluate(Object arg0) {
						DATISField field = (DATISField)arg0;
						if(lbl.startsWith(field.getName())) {
							return true;
						}
						return false;
					}
					
				});
				
				if(found != null) {
					writeCSV(buf, ans, found);
					//writeData(buf, ans, found);
					counter++;
				}
			}
		}

		if(buf.lastIndexOf(",") == -1) {
			return buf.toString();
		}
		
		return buf.substring(0, buf.lastIndexOf(",")).toString();
	}

}
