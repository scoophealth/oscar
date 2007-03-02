/**
 * Copyright (C) 2007.
 * Centre for Research on Inner City Health, St. Michael's Hospital, Toronto, Ontario, Canada.
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.oscarehr.PMmodule.service.impl;

import java.util.List;

import org.oscarehr.PMmodule.dao.GenericIntakeDAO;
import org.oscarehr.PMmodule.dao.GenericIntakeNodeDAO;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.model.Agency;
import org.oscarehr.PMmodule.model.Intake;
import org.oscarehr.PMmodule.model.IntakeAnswer;
import org.oscarehr.PMmodule.model.IntakeNode;
import org.oscarehr.PMmodule.service.GenericIntakeManager;

public class GenericIntakeManagerImpl implements GenericIntakeManager {
	
	private GenericIntakeNodeDAO genericIntakeNodeDAO;
	private GenericIntakeDAO genericIntakeDAO;
	private ProgramDao programDAO;
	
	public void setGenericIntakeNodeDAO(GenericIntakeNodeDAO genericIntakeNodeDAO) {
	    this.genericIntakeNodeDAO = genericIntakeNodeDAO;
    }
	
	public void setGenericIntakeDAO(GenericIntakeDAO genericIntakeDAO) {
	    this.genericIntakeDAO = genericIntakeDAO;
    }
	
	public void setProgramDAO(ProgramDao programDAO) {
	    this.programDAO = programDAO;
    }
	
	public Intake createQuickIntake(String providerNo) {
		IntakeNode quickIntakeNode = getIntakeNode(Agency.getLocalAgency().getIntakeQuick());
		Intake quickIntake = createIntake(quickIntakeNode, null, providerNo);
		
		return quickIntake;
	}

	public Intake createIndepthIntake(String providerNo) {
		IntakeNode indepthIntakeNode = getIntakeNode(Agency.getLocalAgency().getIntakeIndepth());
		Intake indepthIntake = createIntake(indepthIntakeNode, null, providerNo);

		return indepthIntake;
	}

	public Intake createProgramIntake(Integer programId, String providerNo) {
		IntakeNode programIntakeNode = getIntakeNode(programDAO.getProgram(programId).getIntakeProgram());
		Intake programIntake = createIntake(programIntakeNode, null, providerNo);
		
		return programIntake;
	}
	
	public Intake copyQuickIntake(Integer clientId, String staffId) {
		IntakeNode quickIntakeNode = getIntakeNode(Agency.getLocalAgency().getIntakeQuick());
		Intake quickIntake = copyIntake(quickIntakeNode, clientId, staffId);
		
	    return quickIntake;
	}
	
	public Intake copyIndepthIntake(Integer clientId, String staffId) {
		IntakeNode indepthIntakeNode = getIntakeNode(Agency.getLocalAgency().getIntakeIndepth());
		Intake indepthIntake = copyIntake(indepthIntakeNode, clientId, staffId);
		
	    return indepthIntake;
	}
	
	public Intake copyProgramIntake(Integer clientId, Integer programId, String staffId) {
		IntakeNode programIntakeNode = getIntakeNode(programDAO.getProgram(programId).getIntakeProgram());
		Intake programIntake = copyIntake(programIntakeNode, clientId, staffId);

	    return programIntake;
	}
	
	public Integer saveIntake(Intake intake) {
	    return genericIntakeDAO.saveIntake(intake);
	}
	
	private IntakeNode getIntakeNode(Integer intakeNodeId) {
		IntakeNode intakeNode = genericIntakeNodeDAO.getIntakeNode(intakeNodeId);
		
		if (!intakeNode.isIntake()) {
			throw new IllegalStateException("node with id : " + intakeNodeId + " is not an intake");
		}
		
		return intakeNode;
	}
	
	private Intake copyIntake(IntakeNode intakeRoot, Integer clientId, String staffId) {
		Intake intake = genericIntakeDAO.getIntake(intakeRoot, clientId);
		
		if (intake == null) {
			throw new IllegalStateException(String.format("Could not find intake for node id (%s) and client id (%s)", new Object[] { clientId, staffId }));
		}
		
		Intake intakeCopy = createIntake(intakeRoot, clientId, staffId);
		
		for (IntakeAnswer answer : intake.getAnswers()) {
			intakeCopy.getAnswerMapped(answer.getNode().getIdStr()).setValue(answer.getValue());
		}

		return intakeCopy;
	}
	
	private Intake createIntake(IntakeNode intakeRoot, Integer clientId, String staffId) {
		Intake intake = Intake.create(intakeRoot, clientId, staffId);
        createAnswers(intake, intake.getNode().getChildren());
		
		return intake;
	}
	
	private void createAnswers(Intake intake, List<IntakeNode> children) {
		for (IntakeNode child : children) {
	        if (child.isScalarAnswer()) {
	        	intake.addToanswers(IntakeAnswer.create(child));
	        }
	        
	        createAnswers(intake, child.getChildren());
        }
	}

}