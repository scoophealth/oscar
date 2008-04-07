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
package org.oscarehr.PMmodule.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Map.Entry;

import org.oscarehr.PMmodule.dao.AdmissionDao;
import org.oscarehr.PMmodule.dao.ClientDao;
import org.oscarehr.PMmodule.dao.GenericIntakeDAO;
import org.oscarehr.PMmodule.dao.GenericIntakeNodeDAO;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.model.Admission;
import org.oscarehr.PMmodule.model.Agency;
import org.oscarehr.PMmodule.model.Intake;
import org.oscarehr.PMmodule.model.IntakeAnswer;
import org.oscarehr.PMmodule.model.IntakeNode;
import org.oscarehr.PMmodule.model.IntakeNodeLabel;
import org.oscarehr.PMmodule.model.IntakeNodeTemplate;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.common.model.ReportStatistic;

public class GenericIntakeManager {

	private GenericIntakeNodeDAO genericIntakeNodeDAO;
	private GenericIntakeDAO genericIntakeDAO;
	private ProgramDao programDao;
	private AdmissionDao admissionDao;
	private ClientDao clientDao;
	
	public void setGenericIntakeNodeDAO(GenericIntakeNodeDAO genericIntakeNodeDAO) {
		this.genericIntakeNodeDAO = genericIntakeNodeDAO;
	}

	public void setGenericIntakeDAO(GenericIntakeDAO genericIntakeDAO) {
		this.genericIntakeDAO = genericIntakeDAO;
	}

	public void setProgramDao(ProgramDao programDao) {
		this.programDao = programDao;
	}

	public void setAdmissionDao(AdmissionDao admissionDao) {
		this.admissionDao = admissionDao;
	}

	// Copy

	/**
	 * @see org.oscarehr.PMmodule.service.GenericIntakeManager#copyQuickIntake(java.lang.Integer, java.lang.String)
	 */
	public Intake copyQuickIntake(Integer clientId, String staffId, Integer facilityId) {
		//return copyIntake(getQuickIntakeNode(), clientId, null, staffId);
		return copyIntakeWithId(getQuickIntakeNode(), clientId, null, staffId, facilityId);
	}

	public Intake copyRegIntake(Integer clientId, String staffId, Integer facilityId) {
		//return copyIntake(getQuickIntakeNode(), clientId, null, staffId);
		return copyIntakeWithId(getLatestRegIntakeNodes(clientId), clientId, null, staffId, facilityId);
	}

	/**
	 * @see org.oscarehr.PMmodule.service.GenericIntakeManager#copyIndepthIntake(java.lang.Integer, java.lang.String)
	 */
	public Intake copyIndepthIntake(Integer clientId, String staffId, Integer facilityId) {
		//return copyIntake(getIndepthIntakeNode(), clientId, null, staffId);
		return copyIntakeWithId(getIndepthIntakeNode(), clientId, null, staffId, facilityId);
	}

	/**
	 * @see org.oscarehr.PMmodule.service.GenericIntakeManager#copyProgramIntake(java.lang.Integer, java.lang.Integer, java.lang.String)
	 */
	public Intake copyProgramIntake(Integer clientId, Integer programId, String staffId, Integer facilityId) {
		//return copyIntake(getProgramIntakeNode(programId), clientId, programId, staffId);
		return copyIntakeWithId(getProgramIntakeNode(programId), clientId, programId, staffId, facilityId);
	}

	// Create

	/**
	 * @see org.oscarehr.PMmodule.service.GenericIntakeManager#createQuickIntake(java.lang.String)
	 */
	public Intake createQuickIntake(String providerNo) {
		return createIntake(getQuickIntakeNode(), null, null, providerNo);
	}

	/**
	 * @see org.oscarehr.PMmodule.service.GenericIntakeManager#createIndepthIntake(java.lang.String)
	 */
	public Intake createIndepthIntake(String providerNo) {
		return createIntake(getIndepthIntakeNode(), null, null, providerNo);
	}

	/**
	 * @see org.oscarehr.PMmodule.service.GenericIntakeManager#createProgramIntake(java.lang.Integer, java.lang.String)
	 */
	public Intake createProgramIntake(Integer programId, String providerNo) {
		return createIntake(getProgramIntakeNode(programId), null, programId, providerNo);
	}

	// Get

	/**
	 * @see org.oscarehr.PMmodule.service.GenericIntakeManager#getMostRecentQuickIntake(java.lang.Integer)
	 */
	public Intake getMostRecentQuickIntakeByFacility(Integer clientId, Integer facilityId) {
		return genericIntakeDAO.getLatestIntakeByFacility(getQuickIntakeNode(), clientId, null, facilityId);
	}

	public Intake getMostRecentQuickIntake(Integer clientId, Integer facilityId) {
		return genericIntakeDAO.getLatestIntake(getQuickIntakeNode(), clientId, null, facilityId);
	}
	
	public Intake getRegIntakeById(Integer intakeId, Integer facilityId) {
		return genericIntakeDAO.getIntakeById(getIntakeNodeByIntakeId(intakeId), intakeId, null, facilityId);
	}
	
	/**
	 * @see org.oscarehr.PMmodule.service.GenericIntakeManager#getMostRecentIndepthIntake(java.lang.Integer)
	 */
	public Intake getMostRecentIndepthIntake(Integer clientId, Integer facilityId) {
		return genericIntakeDAO.getLatestIntake(getIndepthIntakeNode(), clientId, null, facilityId);
	}

	/**
	 * @see org.oscarehr.PMmodule.service.GenericIntakeManager#getMostRecentProgramIntake(java.lang.Integer, java.lang.Integer)
	 */
	public Intake getMostRecentProgramIntake(Integer clientId, Integer programId, Integer facilityId) {
		return genericIntakeDAO.getLatestIntake(getProgramIntakeNode(programId), clientId, programId, facilityId);
	}

	/**
	 * @see org.oscarehr.PMmodule.service.GenericIntakeManager#getQuickIntakes(java.lang.Integer)
	 */
	public List<Intake> getQuickIntakes(Integer clientId, Integer facilityId) {
		return genericIntakeDAO.getIntakes(getQuickIntakeNode(), clientId, null, facilityId);
	}

	public List<Intake> getRegIntakes(Integer clientId, Integer facilityId) {
		return genericIntakeDAO.getRegIntakes(getRegIntakeNodes(clientId), clientId, null, facilityId);
	}

	/**
	 * @see org.oscarehr.PMmodule.service.GenericIntakeManager#getIndepthIntakes(java.lang.Integer)
	 */
	public List<Intake> getIndepthIntakes(Integer clientId, Integer facilityId) {
		return genericIntakeDAO.getIntakes(getIndepthIntakeNode(), clientId, null, facilityId);
	}

	/**
	 * @see org.oscarehr.PMmodule.service.GenericIntakeManager#getProgramIntakes(java.lang.Integer)
	 */
	public List<Intake> getProgramIntakes(Integer clientId, Integer facilityId) {
		List<Intake> programIntakes = new ArrayList<Intake>();

		for (Program program : getProgramsWithIntake(clientId)) {
			programIntakes.addAll(genericIntakeDAO.getIntakes(getProgramIntakeNode(program), clientId, program.getId(), facilityId));
		}

		return programIntakes;
	}

	/**
	 * @see org.oscarehr.PMmodule.service.GenericIntakeManager#getProgramsWithIntake(java.lang.Integer)
	 */
	public List<Program> getProgramsWithIntake(Integer clientId) {
		List<Program> programsWithIntake = new ArrayList<Program>();

		List<?> serviceProgramAdmissions = admissionDao.getCurrentServiceProgramAdmission(programDao, clientId);
		if (serviceProgramAdmissions != null) {
			for (Object o : serviceProgramAdmissions) {
				Admission admission = (Admission) o;
				Program program = programDao.getProgram(admission.getProgramId());

				if (program != null && program.getIntakeProgram() != null) {
					programsWithIntake.add(program);
				}
			}
		}

		return programsWithIntake;
	}

	// Save

	public Integer saveIntake(Intake intake) {
		return genericIntakeDAO.saveIntake(intake);
	}

	// Save Or Update
	
	public void saveUpdateIntake(Intake intake) {
		genericIntakeDAO.saveUpdateIntake(intake);
	}
	
	// Report

	public GenericIntakeDAO.GenericIntakeReportStatistics getQuestionStatistics2(String intakeType, Integer programId, Date startDate, Date endDate) throws SQLException {

		// get node
		IntakeNode node = null;

		if (Intake.QUICK.equalsIgnoreCase(intakeType)) {
			node = getQuickIntakeNode();
		} else if (Intake.INDEPTH.equalsIgnoreCase(intakeType)) {
			node = getIndepthIntakeNode();
		} else if (Intake.PROGRAM.equalsIgnoreCase(intakeType)) {
			node = getProgramIntakeNode(programId);
		}

		Calendar endCal=Calendar.getInstance();
		endCal.setTime(endDate);
		endCal.add(Calendar.DAY_OF_YEAR, 1);
		List<Integer> intakeIds = genericIntakeDAO.getIntakeIds2(node.getId(), startDate, endCal.getTime());
		Set<Integer> choiceAnswerIds = node.getChoiceAnswerIds();
		
		GenericIntakeDAO.GenericIntakeReportStatistics reportStatistics = genericIntakeDAO.getReportStatistics2(intakeIds, choiceAnswerIds);
		
		return reportStatistics;
	}

	public Map<String, SortedSet<ReportStatistic>> getQuestionStatistics(String intakeType, Integer programId, Date startDate, Date endDate) {
		Map<String, SortedSet<ReportStatistic>> questionStatistics = new LinkedHashMap<String, SortedSet<ReportStatistic>>();

		// get node
		IntakeNode node = null;

		if (Intake.QUICK.equalsIgnoreCase(intakeType)) {
			node = getQuickIntakeNode();
		} else if (Intake.INDEPTH.equalsIgnoreCase(intakeType)) {
			node = getIndepthIntakeNode();
		} else if (Intake.PROGRAM.equalsIgnoreCase(intakeType)) {
			node = getProgramIntakeNode(programId);
		}

		// get report statistics
		Set<Integer> choiceAnswerIds = node.getChoiceAnswerIds();
		SortedSet<Integer> latestIntakeIds = genericIntakeDAO.getLatestIntakeIds(node.getId(), startDate, endDate);
		SortedMap<Integer, SortedMap<String, ReportStatistic>> reportStatistics = genericIntakeDAO.getReportStatistics(choiceAnswerIds, latestIntakeIds);

		// populate map
		if (!reportStatistics.isEmpty()) {
			for (IntakeNode question : node.getQuestionsWithChoiceAnswers()) {
				SortedSet<ReportStatistic> statistics = new TreeSet<ReportStatistic>();
				int total=0;
				
				for (IntakeNode answer : question.getChoiceAnswers()) {
					SortedMap<String, ReportStatistic> valueStatistics = reportStatistics.get(answer.getId());

					for (Entry<String, ReportStatistic> valueStatistic : valueStatistics.entrySet()) {
						ReportStatistic statistic = valueStatistic.getValue();
						statistic.setLabel(createStatisticLabel(answer.getLabelStr(), valueStatistic.getKey()));
						statistics.add(statistic);
						total=total+statistic.getCount();
					}
				}

				for (ReportStatistic reportStatistic : statistics)
				{
				    reportStatistic.setSize(total);
				}
				
				questionStatistics.put(question.getLabelStr(), statistics);
			}
		}

		return questionStatistics;
	}

	// Private

	private Intake copyIntake(IntakeNode node, Integer clientId, Integer programId, String staffId, Integer facilityId) {
		Intake source = genericIntakeDAO.getLatestIntake(node, clientId, programId, facilityId);
		Intake dest = createIntake(node, clientId, programId, staffId);

		if (source != null) {
			for (IntakeAnswer answer : source.getAnswers()) {
				dest.getAnswerMapped(answer.getNode().getIdStr()).setValue(answer.getValue());
			}
		}

		return dest;
	}

	private Intake createIntake(IntakeNode node, Integer clientId, Integer programId, String staffId) {
		Intake intake = Intake.create(node, clientId, programId, staffId);
		createAnswers(intake, intake.getNode().getChildren());

		return intake;
	}
	
	private Intake copyIntakeWithId(IntakeNode node, Integer clientId, Integer programId, String staffId, Integer facilityId) {
		Intake source = genericIntakeDAO.getLatestIntake(node, clientId, programId, facilityId);
		
		//Intake dest = createIntakeWithId(node, clientId, programId, staffId,source.getId(),source.getIntakeLocation());
		Intake dest=null;
		
		if (source != null) {
			dest = createIntakeWithId(node, clientId, programId, staffId,source.getId(),source.getIntakeLocation());
			
			for (IntakeAnswer answer : source.getAnswers()) {
				dest.getAnswerMapped(answer.getNode().getIdStr()).setValue(answer.getValue());
			}
		}

		return dest;
	}	

	private Intake createIntakeWithId(IntakeNode node, Integer clientId, Integer programId, String staffId, Integer intakeId, Integer intakeLocation) {
		Intake intake = Intake.create(node, clientId, programId, staffId, intakeId, intakeLocation);
		createAnswers(intake, intake.getNode().getChildren());

		return intake;
	}
	
	private void createAnswers(Intake intake, List<IntakeNode> children) {
		for (IntakeNode child : children) {
			if (child.isAnswerScalar()) {
				intake.addToanswers(IntakeAnswer.create(child));
			}

			createAnswers(intake, child.getChildren());
		}
	}

	private IntakeNode getQuickIntakeNode() {
		Agency agency = Agency.getLocalAgency();
		Integer quickIntakeNodeId = (agency != null) ? agency.getIntakeQuick() : null;

		return getIntakeNode(quickIntakeNodeId);
	}

	private IntakeNode getIntakeNodeByIntakeId(Integer intakeId) {
		Integer intakeNodeId = genericIntakeDAO.getIntakeNodeIdByIntakeId(intakeId);
		return getIntakeNode(intakeNodeId);
	}

	private List<IntakeNode> getRegIntakeNodes(Integer clientId) {
		List<Integer> nodeIds = genericIntakeDAO.getIntakeNodesIdByClientId(clientId);
		List<IntakeNode> nodeList = new ArrayList();
		if (nodeIds.size()>0) {
		    for (Integer i : nodeIds) {
			nodeList.add(getIntakeNode(i));
		    }
		} else {
		    nodeList.add(getQuickIntakeNode());
		}
		return nodeList;
	}

	private IntakeNode getLatestRegIntakeNodes(Integer clientId) {
		List<Integer> nodeIds = genericIntakeDAO.getIntakeNodesIdByClientId(clientId);
		IntakeNode node = getIntakeNode(nodeIds.get(0));
		return node;
	}

	private IntakeNode getIndepthIntakeNode() {
		Agency agency = Agency.getLocalAgency();
		Integer indepthIntakeNodeId = (agency != null) ? agency.getIntakeIndepth() : null;

		return getIntakeNode(indepthIntakeNodeId);
	}

	private IntakeNode getProgramIntakeNode(Integer programId) {
		return getProgramIntakeNode(programDao.getProgram(programId));
	}

	private IntakeNode getProgramIntakeNode(Program program) {
		Integer programIntakeNodeId = (program != null) ? program.getIntakeProgram() : null;

		return getIntakeNode(programIntakeNodeId);
	}

        public List<IntakeNode> getIntakeNodes(){
            return genericIntakeNodeDAO.getIntakeNodes();
        }
        
        public void saveNodeLabel(IntakeNodeLabel intakeNodeLabel){
            genericIntakeNodeDAO.saveNodeLabel(intakeNodeLabel);
        }
        
        public void saveIntakeNode(IntakeNode intakeNode){
            genericIntakeNodeDAO.saveIntakeNode(intakeNode);
        }
	
	public void saveIntakeNodeTemplate(IntakeNodeTemplate intakeNodeTemplate) {
	    genericIntakeNodeDAO.saveIntakeNodeTemplate(intakeNodeTemplate);
	}
        
        ///
        public void updateNodeLabel(IntakeNodeLabel intakeNodeLabel){
            genericIntakeNodeDAO.updateNodeLabel(intakeNodeLabel);   
        }
	
	public void updateAgencyIntakeQuick(Integer intakeNodeId) {
	    Agency agency = Agency.getLocalAgency();
	    agency.setIntakeQuick(intakeNodeId);
	    genericIntakeNodeDAO.updateAgencyIntakeQuick(agency);
	}
	
        public IntakeNodeLabel getIntakeNodeLabel(Integer intakeNodeLabelId){
            return genericIntakeNodeDAO.getIntakeNodeLabel(intakeNodeLabelId);
        }

        public IntakeNodeTemplate getIntakeNodeTemplate(Integer intakeNodeTemplateId){
            return genericIntakeNodeDAO.getIntakeNodeTemplate(intakeNodeTemplateId);
        }
        
	public IntakeNode getIntakeNode(Integer nodeId) {
		if (nodeId == null) {
			throw new IllegalArgumentException("Parameter nodeId must be non-null");
		}

		IntakeNode node = genericIntakeNodeDAO.getIntakeNode(nodeId);

		if (!node.isIntake()) {
			throw new IllegalStateException("node with id : " + nodeId + " is not an intake");
		}

		return node;
	}

	private String createStatisticLabel(String answerLabel, String answerElement) {
		StringBuilder builder = new StringBuilder();

		builder.append(answerLabel);

		if (builder.length() > 0) {
			builder.append(":");
		}

		builder.append(answerElement);

		return builder.toString();
	}
/*street health report, not finished yet
	public List getCohort(Date beginDate, Date endDate) {
		return genericIntakeDAO.getCohort(beginDate, endDate, clientDao.getClients());
	}
	*/
}
