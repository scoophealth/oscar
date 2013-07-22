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

package org.oscarehr.PMmodule.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.dao.GenericIntakeDAO;
import org.oscarehr.PMmodule.dao.GenericIntakeNodeDAO;
import org.oscarehr.PMmodule.dao.ProgramDao;
import org.oscarehr.PMmodule.model.Agency;
import org.oscarehr.PMmodule.model.Intake;
import org.oscarehr.PMmodule.model.IntakeAnswer;
import org.oscarehr.PMmodule.model.IntakeAnswerElement;
import org.oscarehr.PMmodule.model.IntakeNode;
import org.oscarehr.PMmodule.model.IntakeNodeJavascript;
import org.oscarehr.PMmodule.model.IntakeNodeLabel;
import org.oscarehr.PMmodule.model.IntakeNodeTemplate;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.web.adapter.IntakeNodeHtmlAdapter;
import org.oscarehr.PMmodule.web.adapter.ocan.OcanXmlAdapterFactory;
import org.oscarehr.common.dao.AdmissionDao;
import org.oscarehr.common.model.Admission;
import org.oscarehr.common.model.ReportStatistic;
import org.oscarehr.util.MiscUtils;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class GenericIntakeManager {

	private GenericIntakeNodeDAO genericIntakeNodeDAO;
	private GenericIntakeDAO genericIntakeDAO;
	private ProgramDao programDao;
	private AdmissionDao admissionDao;
	private OcanXmlAdapterFactory adapterFactory = new OcanXmlAdapterFactory();

	private static Logger logger = MiscUtils.getLogger();

	public void setGenericIntakeNodeDAO(
			GenericIntakeNodeDAO genericIntakeNodeDAO) {
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

	public Intake copyQuickIntake(Integer clientId, String staffId,
			Integer facilityId) {
		// return copyIntake(getQuickIntakeNode(), clientId, null, staffId);
		return copyIntakeWithId(getQuickIntakeNode(), clientId, null, staffId,
				facilityId);
	}

	public Intake copyRegIntake(Integer clientId, String staffId,
			Integer facilityId) {
		// return copyIntake(getQuickIntakeNode(), clientId, null, staffId);
		IntakeNode latestInode = getLatestRegIntakeNodes(clientId);
		Intake intake = this.genericIntakeDAO.getLatestIntakeByFacility(
				latestInode, clientId, null, facilityId);
		if (intake != null) {
			return copyIntakeWithId(latestInode, clientId, null, staffId,
					facilityId);
		} else {
			IntakeNode qin = this.getQuickIntakeNode();
			if (qin.getLabel() == null) {
				return null;
			}
			List<IntakeNode> previousIntakeNodes = genericIntakeNodeDAO
					.getPublishedIntakeNodesByName(qin.getLabel().getLabel());
			for (IntakeNode in : previousIntakeNodes) {
				intake = genericIntakeDAO.getLatestIntakeByFacility(in,
						clientId, null, facilityId);
				if (intake != null)
					return copyOldIntake(latestInode, intake, clientId, null,
							staffId, facilityId);
			}
		}
		return null;
	}

	public Intake copyIndepthIntake(Integer clientId, String staffId,
			Integer facilityId) {
		// return copyIntake(getIndepthIntakeNode(), clientId, null, staffId);
		return copyIntakeWithId(getIndepthIntakeNode(), clientId, null,
				staffId, facilityId);
	}

	public Intake copyProgramIntake(Integer clientId, Integer programId,
			String staffId, Integer facilityId) {
		// return copyIntake(getProgramIntakeNode(programId), clientId,
		// programId, staffId);
		return copyIntakeWithId(getProgramIntakeNode(programId), clientId,
				programId, staffId, facilityId);
	}

	// Create

	public Intake createQuickIntake(String providerNo) {
		return createIntake(getQuickIntakeNode(), null, null, providerNo);
	}

	public Intake createIndepthIntake(String providerNo) {
		return createIntake(getIndepthIntakeNode(), null, null, providerNo);
	}

	public Intake createIntake(IntakeNode node, String providerNo) {
		return createIntake(node, null, null, providerNo);
	}

	public Intake createProgramIntake(Integer programId, String providerNo) {
		return createIntake(getProgramIntakeNode(programId), null, programId,
				providerNo);
	}

	// Get

	public Intake getMostRecentQuickIntakeByFacility(Integer clientId,
			Integer facilityId) {
		IntakeNode regIntakeNode = getQuickIntakeNode();
		Intake intake = genericIntakeDAO.getLatestIntakeByFacility(
				regIntakeNode, clientId, null, facilityId);

		if (intake == null && regIntakeNode.getLabel() != null) {
			/*
			 * search old registration intakes in proper order to make sure we
			 * return the latest one
			 */
			List<IntakeNode> previousIntakeNodes = genericIntakeNodeDAO
					.getPublishedIntakeNodesByName(regIntakeNode.getLabel()
							.getLabel());
			for (IntakeNode in : previousIntakeNodes) {
				logger.info("searching for intakes for node version: "
						+ in.getForm_version());
				intake = genericIntakeDAO.getLatestIntakeByFacility(in,
						clientId, null, facilityId);
				if (intake != null)
					break;
			}
		}

		return intake;
	}

	public Intake getMostRecentQuickIntake(Integer clientId, Integer facilityId) {
		return genericIntakeDAO.getLatestIntake(getQuickIntakeNode(), clientId,
				null, facilityId);
	}

	public Intake getRegIntakeById(Integer intakeId, Integer facilityId) {
		return genericIntakeDAO.getIntakeById(
				getIntakeNodeByIntakeId(intakeId), intakeId, null, facilityId);
	}

	public Intake getMostRecentIndepthIntake(Integer clientId,
			Integer facilityId) {
		return genericIntakeDAO.getLatestIntake(getIndepthIntakeNode(),
				clientId, null, facilityId);
	}

	public Intake getMostRecentProgramIntake(Integer clientId,
			Integer programId, Integer facilityId) {
		return genericIntakeDAO.getLatestIntake(
				getProgramIntakeNode(programId), clientId, programId,
				facilityId);
	}

	public List<Intake> getQuickIntakes(Integer clientId, Integer facilityId) {
		return genericIntakeDAO.getIntakes(getQuickIntakeNode(), clientId,
				null, facilityId);
	}

	public List<Intake> getRegIntakes(Integer clientId, Integer facilityId) {
		return genericIntakeDAO.getRegIntakes(getRegIntakeNodes(clientId),
				clientId, null, facilityId);
	}

	public List<Integer> getIntakeClientsByFacilityId(Integer facilityId) {
		return genericIntakeDAO.getIntakeClientsByFacilityId(facilityId);
	}

	public List<Integer> getIntakeFacilityIds() {
		return genericIntakeDAO.getIntakeFacilityIds();
	}

	public List<Intake> getIndepthIntakes(Integer clientId, Integer facilityId) {
		return genericIntakeDAO.getIntakes(getIndepthIntakeNode(), clientId,
				null, facilityId);
	}

	public List<Intake> getProgramIntakes(Integer clientId, Integer facilityId) {
		List<Intake> programIntakes = new ArrayList<Intake>();

		for (Program program : getProgramsWithIntake(clientId)) {
			programIntakes.addAll(genericIntakeDAO.getIntakes(
					getProgramIntakeNode(program), clientId, program.getId(),
					facilityId));
		}

		return programIntakes;
	}

	public List<Intake> getIntakesByType(Integer clientId, Integer facilityId, Integer formType) {
		return genericIntakeDAO.getIntakesByType(formType, clientId,
				null, facilityId);
	}

	public List<Program> getProgramsWithIntake(Integer clientId) {
		List<Program> programsWithIntake = new ArrayList<Program>();

		List<?> serviceProgramAdmissions = admissionDao
				.getCurrentServiceProgramAdmission(programDao, clientId);
		if (serviceProgramAdmissions != null) {
			for (Object o : serviceProgramAdmissions) {
				Admission admission = (Admission) o;
				Program program = programDao.getProgram(admission
						.getProgramId());

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

	public GenericIntakeDAO.GenericIntakeReportStatistics getQuestionStatistics2(
			String intakeType, Integer programId, Date startDate, Date endDate)
			throws SQLException {

		// get node
		IntakeNode node = null;

		if (Intake.QUICK.equalsIgnoreCase(intakeType)) {
			node = getQuickIntakeNode();
		} else if (Intake.INDEPTH.equalsIgnoreCase(intakeType)) {
			node = getIndepthIntakeNode();
		} else if (Intake.PROGRAM.equalsIgnoreCase(intakeType)) {
			node = getProgramIntakeNode(programId);
		}

		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);
		endCal.add(Calendar.DAY_OF_YEAR, 1);
		List<Integer> intakeIds = genericIntakeDAO.getIntakeIds2(node.getId(),
				startDate, endCal.getTime());
		Set<Integer> choiceAnswerIds = node.getChoiceAnswerIds();

		GenericIntakeDAO.GenericIntakeReportStatistics reportStatistics = genericIntakeDAO
				.getReportStatistics2(intakeIds, choiceAnswerIds);

		return reportStatistics;
	}

	public Map<String, SortedSet<ReportStatistic>> getQuestionStatistics(
			String nodeId, String intakeType, Integer programId, Date startDate, Date endDate,
			boolean includePast) throws SQLException {
		Map<String, SortedSet<ReportStatistic>> questionStatistics = new LinkedHashMap<String, SortedSet<ReportStatistic>>();

		// get node
		IntakeNode node = null;

		if (Intake.QUICK.equalsIgnoreCase(intakeType)) {
			node = getQuickIntakeNode();
		} else if (Intake.INDEPTH.equalsIgnoreCase(intakeType)) {
			node = getIndepthIntakeNode();
		} else if (Intake.PROGRAM.equalsIgnoreCase(intakeType)) {
			node = getProgramIntakeNode(programId);
		} else {
			node = getIntakeNode(Integer.valueOf(nodeId));
		}

		if(node==null) {
			logger.info("No node found with given parameters");
			return questionStatistics;
		}
		// get report statistics
		List<IntakeNode> nodeList = getEqIntakeNodes(node, includePast);
		Hashtable<Integer, Integer> choiceAnswerIds = new Hashtable<Integer, Integer>();
		SortedSet<Integer> latestIntakeIds = new TreeSet<Integer>();
		for (IntakeNode iN : nodeList) {
			choiceAnswerIds.putAll(getEqAnswerIds(iN.getChoiceAnswerIds(),
					includePast));
			latestIntakeIds.addAll(genericIntakeDAO.getLatestIntakeIds(iN
					.getId(), startDate, endDate));
		}
		if (includePast)
			buildlastIndex(choiceAnswerIds);

		SortedMap<Integer, SortedMap<String, ReportStatistic>> reportStatistics = genericIntakeDAO
				.getReportStatistics(choiceAnswerIds, latestIntakeIds);
		// populate map
		if (!reportStatistics.isEmpty()) {
			List<IntakeNode> questionList = new ArrayList<IntakeNode>();
			for (IntakeNode n : nodeList) {
				questionList.addAll(n.getQuestionsWithChoiceAnswers());
			}
			Hashtable<Integer, Integer> questionIds = getEqQuestionIds(
					questionList, includePast);
			if (includePast)
				buildlastIndex(questionIds);

			List<Integer> nodeIds = new ArrayList<Integer>();
			List<Integer> counts = new ArrayList<Integer>();
			List<Integer> totals = new ArrayList<Integer>();
			List<String> labels = new ArrayList<String>();
			List<String> keys = new ArrayList<String>();
			for (IntakeNode question : questionList) {
				for (IntakeNode answer : question.getChoiceAnswers()) {
					SortedMap<String, ReportStatistic> valueStatistics = reportStatistics
							.get(answer.getId());

					if (valueStatistics != null) {
						int total = 0;
						for (Entry<String, ReportStatistic> valueStatistic : valueStatistics
								.entrySet()) {
							ReportStatistic statistic = valueStatistic
									.getValue();
							nodeIds.add(answer.getId());
							counts.add(statistic.getCount());
							total += statistic.getCount();
							labels.add(answer.getLabelStr());
							keys.add(valueStatistic.getKey());
						}
						for (int i = totals.size(); i < counts.size(); i++) {
							totals.add(total);
						}
					}
				}
			}

			if (includePast) {
				List<Integer> chngttl = new ArrayList<Integer>();
				int maxttl = 0;
				for (int id : choiceAnswerIds.keySet()) {
					int cid = choiceAnswerIds.get(id);
					if (id == cid)
						continue;

					int x = nodeIds.indexOf(cid), idx_t = -1, idx_f = -1, cnt_t = 0, cnt_f = 0, ttl_t = 0, ttl_f = 0;
					for (int i = 0; i < nodeIds.size(); i++) {
						int nid = nodeIds.get(i);
						if (nid != id && nid != cid)
							continue;
						if (counts.get(i).equals(-1))
							continue;

						if (nid == id) {
							nodeIds.set(i, cid);
							if (x != -1)
								labels.set(i, labels.get(x));
						}
						if (keys.get(i).equals("T")) {
							cnt_t += counts.get(i);
							ttl_t += totals.get(i);
							idx_t = i;
						} else {
							cnt_f += counts.get(i);
							ttl_f += totals.get(i);
							idx_f = i;
						}
						counts.set(i, -1);
						chngttl.add(i);
					}
					if (idx_t != -1) {
						counts.set(idx_t, cnt_t);
						totals.set(idx_t, ttl_t);
					}
					if (idx_f != -1) {
						counts.set(idx_f, cnt_f);
						totals.set(idx_f, ttl_f);
					}
					maxttl = ttl_t > maxttl ? ttl_t : maxttl;
					maxttl = ttl_f > maxttl ? ttl_f : maxttl;
				}
				for (int i : chngttl)
					totals.set(i, maxttl);
			}

			List<ReportStatistic> missResponses = new ArrayList<ReportStatistic>();
			List<Integer> missResponseIds = new ArrayList<Integer>();
			for (IntakeNode question : questionList) {
				SortedSet<ReportStatistic> statistics = new TreeSet<ReportStatistic>();
				Integer eqQuestionId = questionIds.get(question.getId());
				boolean showQuestion = false;
				if (eqQuestionId.equals(question.getId()))
					showQuestion = true;

				for (IntakeNode answer : question.getChoiceAnswers()) {
					for (int i = 0; i < nodeIds.size(); i++) {
						if (!nodeIds.get(i).equals(answer.getId()))
							continue;
						if (counts.get(i).equals(-1))
							continue;

						ReportStatistic statistic = new ReportStatistic(counts
								.get(i), totals.get(i));
						if (showQuestion) {
							statistic.setLabel(createStatisticLabel(labels
									.get(i), keys.get(i)));
							statistics.add(statistic);
						} else {
							statistic.setLabel(createStatisticLabel(labels
									.get(i)
									+ " (missing responses)", keys.get(i)));
							missResponseIds.add(eqQuestionId);
							missResponses.add(statistic);
						}
					}
				}

				if (showQuestion) {
					for (int i = 0; i < missResponseIds.size(); i++) {
						if (!missResponseIds.get(i).equals(question.getId()))
							continue;

						statistics.add(missResponses.get(i));
						missResponseIds.set(i, -1);
					}
					questionStatistics.put(question.getLabelStr(), statistics);
				}
			}
		}
		return questionStatistics;
	}

	private Intake createIntake(IntakeNode node, Integer clientId,
			Integer programId, String staffId) {
		Intake intake = Intake.create(node, clientId, programId, staffId);
		createAnswers(intake, intake.getNode().getChildren());

		return intake;
	}

	public Intake copyIntakeWithId(IntakeNode node, Integer clientId,
			Integer programId, String staffId, Integer facilityId) {
		Intake source = genericIntakeDAO.getLatestIntake(node, clientId,
				programId, facilityId);

		// Intake dest = createIntakeWithId(node, clientId, programId,
		// staffId,source.getId(),source.getIntakeLocation());
		Intake dest = null;

		if (source != null) {
			dest = createIntakeWithId(node, clientId, programId, staffId,
					source.getId(), source.getIntakeLocation());

			for (IntakeAnswer answer : source.getAnswers()) {
				if(answer.getNode().getRepeating()) {
					dest.getRepeatingAnswerMapped(answer.getNode().getIdStr(), answer.getIndex()).setValue(answer.getValue());
				} else {
					dest.getAnswerMapped(answer.getNode().getIdStr()).setValue(	answer.getValue());
				}
			}
		}

		return dest;
	}

	private Intake copyOldIntake(IntakeNode sourceNode, Intake source,
			Integer clientId, Integer programId, String staffId,
			Integer facilityId) {
		Intake dest = null;
		IntakeNode destNode = getQuickIntakeNode();

		if (source != null) {
			dest = createIntake(destNode, clientId, programId, staffId);

			for (IntakeAnswer ia : dest.getAnswers()) {
				if (ia.isAnswerScalar()) {
					if (ia.getNode().getId().intValue() != ia.getNode()
							.getEq_to_id().intValue()) {
						try {
							String value = source.getAnswerMapped(
									String.valueOf(ia.getNode().getEq_to_id()))
									.getValue();

							ia.setValue(value);
						} catch (IllegalStateException e) {
							logger.warn("Warning", e);
						}
					}
				}
			}
		}

		return dest;
	}

	private Intake createIntakeWithId(IntakeNode node, Integer clientId,
			Integer programId, String staffId, Integer intakeId,
			Integer intakeLocation) {
		Intake intake = Intake.create(node, clientId, programId, staffId,
				intakeId, intakeLocation);
		createAnswers(intake, intake.getNode().getChildren());

		return intake;
	}

	private void createAnswers(Intake intake, List<IntakeNode> children) {
		for (IntakeNode child : children) {
			if (child != null) {
				if (child.isAnswerScalar()) {
					intake.addToanswers(IntakeAnswer.create(child));
				}

				createAnswers(intake, child.getChildren());
			}
		}
	}

	private IntakeNode getQuickIntakeNode() {
		Agency agency = Agency.getLocalAgency();
		Integer quickIntakeNodeId = (agency != null) ? agency.getIntakeQuick()
				: null;

		return getIntakeNode(quickIntakeNodeId);
	}

	private IntakeNode getIntakeNodeByIntakeId(Integer intakeId) {
		Integer intakeNodeId = genericIntakeDAO
				.getIntakeNodeIdByIntakeId(intakeId);
		return getIntakeNode(intakeNodeId);
	}

	private List<IntakeNode> getRegIntakeNodes(Integer clientId) {
		List<Integer> nodeIds = genericIntakeDAO
				.getIntakeNodesIdByClientId(clientId,1);
		List<IntakeNode> nodeList = new ArrayList<IntakeNode>();
		if (nodeIds.size() > 0) {
			for (Integer i : nodeIds) {
				nodeList.add(getIntakeNode(i));
			}
		} else {
			nodeList.add(getQuickIntakeNode());
		}
		return nodeList;
	}

	private IntakeNode getLatestRegIntakeNodes(Integer clientId) {
		// List<Integer> nodeIds =
		// genericIntakeDAO.getIntakeNodesIdByClientId(clientId);

		// IntakeNode node = getIntakeNode(nodeIds.get(0));
		// return node;

		Agency agency = Agency.getLocalAgency();
		Integer quickIntakeNodeId = (agency != null) ? agency.getIntakeQuick()
				: null;
		return getIntakeNode(quickIntakeNodeId);
	}

	private IntakeNode getIndepthIntakeNode() {
		Agency agency = Agency.getLocalAgency();
		Integer indepthIntakeNodeId = (agency != null) ? agency
				.getIntakeIndepth() : null;

		return getIntakeNode(indepthIntakeNodeId);
	}

	private IntakeNode getProgramIntakeNode(Integer programId) {
		return getProgramIntakeNode(programDao.getProgram(programId));
	}

	private IntakeNode getProgramIntakeNode(Program program) {
		Integer programIntakeNodeId = (program != null) ? program
				.getIntakeProgram() : null;

		return getIntakeNode(programIntakeNodeId);
	}

	private List<IntakeNode> getEqIntakeNodes(IntakeNode iNode, boolean incpast)
			throws SQLException {
		if (incpast) {
			return genericIntakeNodeDAO.getIntakeNodeByEqToId(iNode
					.getEq_to_id());
		}
		List<IntakeNode> iNodeList = new ArrayList<IntakeNode>();
		iNodeList.add(iNode);
		return iNodeList;
	}

	private Hashtable<Integer, Integer> getEqAnswerIds(Set<Integer> caIds,
			boolean incpast) throws SQLException {
		Hashtable<Integer, Integer> eqNodeIdsHash = new Hashtable<Integer, Integer>();

		for (Integer Id : caIds) {
			if (incpast) {
				for (Integer nodeId : genericIntakeNodeDAO
						.getEqToIdByIntakeNodeId(Id)) {
					eqNodeIdsHash.put(Id, nodeId);
				}
			} else {
				eqNodeIdsHash.put(Id, Id);
			}
		}
		return eqNodeIdsHash;
	}

	private Hashtable<Integer, Integer> getEqQuestionIds(
			List<IntakeNode> qNodes, boolean incpast) throws SQLException {
		Hashtable<Integer, Integer> eqNodeIdsHash = new Hashtable<Integer, Integer>();

		for (IntakeNode qNode : qNodes) {
			if (incpast) {
				for (Integer nodeId : genericIntakeNodeDAO
						.getEqToIdByIntakeNodeId(qNode.getId())) {
					eqNodeIdsHash.put(qNode.getId(), nodeId);
				}
			} else {
				eqNodeIdsHash.put(qNode.getId(), qNode.getId());
			}
		}
		return eqNodeIdsHash;
	}

	private void buildlastIndex(Hashtable<Integer, Integer> eqNodeIds) {
		Hashtable<Integer, Integer> eqNodeIdsLastIdx = new Hashtable<Integer, Integer>();

		for (Integer i : eqNodeIds.keySet()) {
			int nid = -1;
			for (Integer j : eqNodeIds.keySet()) {
				if (i == j)
					continue;
				if (eqNodeIds.get(i) == eqNodeIds.get(j)) {
					nid = i > j ? i : j;
				}
			}
			nid = nid == -1 ? i : nid;
			eqNodeIdsLastIdx.put(i, nid);
		}
		eqNodeIds.clear();
		eqNodeIds.putAll(eqNodeIdsLastIdx);
	}

	public List<IntakeNode> getIntakeNodes() {
		return genericIntakeNodeDAO.getIntakeNodes();
	}

	public void saveNodeLabel(IntakeNodeLabel intakeNodeLabel) {
		genericIntakeNodeDAO.saveNodeLabel(intakeNodeLabel);
	}

	public void saveIntakeNode(IntakeNode intakeNode) {
		genericIntakeNodeDAO.saveIntakeNode(intakeNode);
	}

	public void deleteIntakeForm(IntakeNode intakeNode) {
		genericIntakeNodeDAO.deleteIntakeNode(intakeNode);
	}

	public void updateIntakeNode(IntakeNode intakeNode) {
		genericIntakeNodeDAO.updateIntakeNode(intakeNode);
	}

	public void saveIntakeNodeTemplate(IntakeNodeTemplate intakeNodeTemplate) {
		genericIntakeNodeDAO.saveIntakeNodeTemplate(intakeNodeTemplate);
	}

	public void saveIntakeAnswerElement(IntakeAnswerElement intakeAnswerElement) {
		genericIntakeNodeDAO.saveIntakeAnswerElement(intakeAnswerElement);
	}

	// /
	public void updateNodeLabel(IntakeNodeLabel intakeNodeLabel) {
		genericIntakeNodeDAO.updateNodeLabel(intakeNodeLabel);
	}

	public void updateAgencyIntakeQuick(Integer intakeNodeId) {
		Agency agency = Agency.getLocalAgency();
		agency.setIntakeQuick(intakeNodeId);
		genericIntakeNodeDAO.updateAgencyIntakeQuick(agency);
	}

	public IntakeNodeLabel getIntakeNodeLabel(Integer intakeNodeLabelId) {
		return genericIntakeNodeDAO.getIntakeNodeLabel(intakeNodeLabelId);
	}

	public IntakeNodeTemplate getIntakeNodeTemplate(Integer intakeNodeTemplateId) {
		return genericIntakeNodeDAO.getIntakeNodeTemplate(intakeNodeTemplateId);
	}

	public IntakeNode getIntakeNode(Integer nodeId) {
		if (nodeId == null) {
			throw new IllegalArgumentException(
					"Parameter nodeId must be non-null");
		}

		IntakeNode node = genericIntakeNodeDAO.getIntakeNode(nodeId);

		if (!node.isIntake()) {
			throw new IllegalStateException("node with id : " + nodeId
					+ " is not an intake");
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

	public List<IntakeNode> getIntakeNodesByType(Integer formType) {
		return genericIntakeDAO.getIntakeNodesByType(formType);
	}

	public List<Map<String, String>> getIntakeListforOcan(Calendar after) {
		Map<Integer, Map<String, Intake>> pairs = new HashMap<Integer, Map<String, Intake>>();

		List<Object[]> triads = genericIntakeDAO.getOcanIntakesAfterDate(after);
		for (Object[] triad : triads) {
			Intake i = (Intake) triad[0];
			IntakeNode n = (IntakeNode) triad[1];
			IntakeNodeLabel l = (IntakeNodeLabel) triad[2];
			String key = l.getLabel().indexOf("Client") > -1 ? "client" : "staff";
			i.setNode(n);
			if (!pairs.containsKey(i.getClientId())) {
				// new client
				Map<String, Intake> pair = new HashMap<String, Intake>();
				pair.put(key, i);
				pairs.put(i.getClientId(), pair);
			} else if (!pairs.get(i.getClientId()).containsKey(key)) {
				// new key
				Map<String, Intake> pair = pairs.get(i.getClientId());
				pair.put(key, i);
			}
		}

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		for (Integer clientId : pairs.keySet()) {
			Map<String, Intake> pair = pairs.get(clientId);
			if (pair.size() == 2) {
				// need both client and staff
				Map<String, String> entry = new HashMap<String, String>();
				entry.put("clientId", clientId.toString());
				entry.put("client", toXml(pair, "client"));
				entry.put("staff", toXml(pair, "staff"));
				list.add(entry);
			}
		}

		return list;
	}

	private String toXml(Map<String, Intake> intakeMap, String type) {
		StringBuilder xml = new StringBuilder();
		Intake intake = intakeMap.get(type);
		if ("client".equals(type)) {
			toClientXml(xml, intake, intake.getNode());
		} else {
			toStaffXml(xml, intake, intake.getNode());
		}
		return xml.toString();
	}

	private void toClientXml(StringBuilder builder, Intake intake, IntakeNode node) {
		if (node == null)
			return;
		IntakeNodeHtmlAdapter htmlAdapter = adapterFactory.getOcanClientXmlAdapter(0, node, intake);

		builder.append(htmlAdapter.getPreBuilder());

		for (IntakeNode child : node.getChildren()) {
			toClientXml(builder, intake, child);
		}

		builder.append(htmlAdapter.getPostBuilder());
	}

	private void toStaffXml(StringBuilder builder, Intake intake, IntakeNode node) {
		if (node == null)
			return;
		IntakeNodeHtmlAdapter htmlAdapter = adapterFactory.getOcanStaffXmlAdapter(0, node, intake);

		builder.append(htmlAdapter.getPreBuilder());

		for (IntakeNode child : node.getChildren()) {
			toStaffXml(builder, intake, child);
		}

		builder.append(htmlAdapter.getPostBuilder());
	}

	/*
	 * street health report, not finished yet public List getCohort(Date
	 * beginDate, Date endDate) { return genericIntakeDAO.getCohort(beginDate,
	 * endDate, demographicDao.getClients()); }
	 */


	public List<IntakeNodeJavascript> getIntakeNodeJavascriptLocation(String questionId) {
		if(questionId != null && questionId.length()>0) {
			return this.genericIntakeNodeDAO.getIntakeNodeJavascriptLocation(questionId);
		}
		return null;
	}
}
