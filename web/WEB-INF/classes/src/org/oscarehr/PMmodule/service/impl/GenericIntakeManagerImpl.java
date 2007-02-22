package org.oscarehr.PMmodule.service.impl;

import java.util.List;

import org.oscarehr.PMmodule.dao.GenericIntakeDAO;
import org.oscarehr.PMmodule.dao.GenericIntakeInstanceDAO;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.model.Agency;
import org.oscarehr.PMmodule.model.IntakeAnswer;
import org.oscarehr.PMmodule.model.IntakeInstance;
import org.oscarehr.PMmodule.model.IntakeNode;
import org.oscarehr.PMmodule.service.GenericIntakeManager;

public class GenericIntakeManagerImpl implements GenericIntakeManager {
	
	private GenericIntakeDAO genericIntakeDAO;
	private GenericIntakeInstanceDAO genericIntakeInstanceDAO;
	private ProgramDao programDAO;
	
	public void setGenericIntakeDAO(GenericIntakeDAO intakeDAO) {
	    this.genericIntakeDAO = intakeDAO;
    }
	
	public void setGenericIntakeInstanceDAO(GenericIntakeInstanceDAO intakeInstanceDAO) {
	    this.genericIntakeInstanceDAO = intakeInstanceDAO;
    }
	
	public void setProgramDAO(ProgramDao programDAO) {
	    this.programDAO = programDAO;
    }
	
	public IntakeInstance createQuickIntake(String providerNo) {
		IntakeNode quickIntake = getIntake(Agency.getLocalAgency().getIntakeQuick());
		IntakeInstance quickIntakeInstance = createInstance(quickIntake, null, providerNo);
		
		return quickIntakeInstance;
	}

	public IntakeInstance createIndepthIntake(String providerNo) {
		IntakeNode indepthIntake = getIntake(Agency.getLocalAgency().getIntakeIndepth());
		IntakeInstance indepthIntakeInstance = createInstance(indepthIntake, null, providerNo);

		return indepthIntakeInstance;
	}

	public IntakeInstance createProgramIntake(Integer programId, String providerNo) {
		IntakeNode programIntake = getIntake(programDAO.getProgram(programId).getIntakeProgram());
		IntakeInstance programIntakeInstance = createInstance(programIntake, null, providerNo);
		
		return programIntakeInstance;
	}
	
	public IntakeInstance copyQuickIntake(Integer clientId, String staffId) {
		IntakeNode quickIntake = getIntake(Agency.getLocalAgency().getIntakeQuick());
		IntakeInstance quickIntakeInstance = copyInstance(quickIntake, clientId, staffId);
		
	    return quickIntakeInstance;
	}
	
	public IntakeInstance copyIndepthIntake(Integer clientId, String staffId) {
		IntakeNode indepthIntake = getIntake(Agency.getLocalAgency().getIntakeIndepth());
		IntakeInstance indepthIntakeInstance = copyInstance(indepthIntake, clientId, staffId);
		
	    return indepthIntakeInstance;
	}
	
	public IntakeInstance copyProgramIntake(Integer clientId, Integer programId, String staffId) {
		IntakeNode programIntake = getIntake(programDAO.getProgram(programId).getIntakeProgram());
		IntakeInstance programIntakeInstance = copyInstance(programIntake, clientId, staffId);

	    return programIntakeInstance;
	}
	
	public Integer saveInstance(IntakeInstance instance) {
	    return genericIntakeInstanceDAO.saveIntakeInstance(instance);
	}
	
	private IntakeNode getIntake(Integer intakeNodeId) {
		IntakeNode intakeNode = genericIntakeDAO.getIntakeNode(intakeNodeId);
		
		if (!intakeNode.isIntake()) {
			throw new IllegalStateException("node with id : " + intakeNodeId + " is not an intake");
		}
		
		return intakeNode;
	}
	
	private IntakeInstance copyInstance(IntakeNode intakeRoot, Integer clientId, String staffId) {
		IntakeInstance instance = genericIntakeInstanceDAO.getInstance(intakeRoot, clientId);
		
		if (instance == null) {
			throw new IllegalStateException(String.format("Could not find instance for node id (%s) and client id (%s)", new Object[] { clientId, staffId }));
		}
		
		IntakeInstance copyInstance = createInstance(intakeRoot, clientId, staffId);
		
		for (IntakeAnswer answer : instance.getAnswers()) {
			copyInstance.getAnswerMapped(answer.getNode().getIdStr()).setValue(answer.getValue());
		}

		return copyInstance;
	}
	
	private IntakeInstance createInstance(IntakeNode intakeRoot, Integer clientId, String staffId) {
		IntakeInstance instance = IntakeInstance.create(intakeRoot, clientId, staffId);
        createAnswers(instance, instance.getNode().getChildren());
		
		return instance;
	}
	
	private void createAnswers(IntakeInstance instance, List<IntakeNode> children) {
		for (IntakeNode child : children) {
	        if (child.isAnswer()) {
	        	IntakeAnswer.create(instance, child);
	        }
	        
	        createAnswers(instance, child.getChildren());
        }
	}

}