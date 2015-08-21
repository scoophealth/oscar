/**
 * Copyright (c) 2013-2015. Department of Computer Science, University of Victoria. All Rights Reserved.
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
 * Department of Computer Science
 * LeadLab
 * University of Victoria
 * Victoria, Canada
 */
package org.oscarehr.e2e.model.export.body;

import java.util.ArrayList;
import java.util.Arrays;

import org.marc.everest.datatypes.BL;
import org.marc.everest.datatypes.ED;
import org.marc.everest.datatypes.ENXP;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.NullFlavor;
import org.marc.everest.datatypes.PN;
import org.marc.everest.datatypes.TS;
import org.marc.everest.datatypes.generic.CD;
import org.marc.everest.datatypes.generic.CE;
import org.marc.everest.datatypes.generic.IVL;
import org.marc.everest.datatypes.generic.SET;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.EntryRelationship;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Observation;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Participant2;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ParticipantRole;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.PlayingEntity;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ActStatus;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ContextControl;
import org.marc.everest.rmim.uv.cdar2.vocabulary.EntityClassRoot;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ParticipationType;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActMoodDocumentObservation;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActRelationshipEntryRelationship;
import org.oscarehr.common.model.Allergy;
import org.oscarehr.e2e.constant.Constants;
import org.oscarehr.e2e.constant.Mappings;
import org.oscarehr.e2e.model.export.template.observation.LifestageObservationModel;
import org.oscarehr.e2e.model.export.template.observation.ReactionObservationModel;
import org.oscarehr.e2e.model.export.template.observation.SeverityObservationModel;
import org.oscarehr.e2e.util.EverestUtils;

public class AllergiesModel {
	private Allergy allergy;

	private SET<II> ids;
	private CD<String> code;
	private ActStatus statusCode;
	private IVL<TS> effectiveTime;
	private EntryRelationship allergyObservation;

	public AllergiesModel(Allergy allergy) {
		if(allergy == null) {
			this.allergy = new Allergy();
		} else {
			this.allergy = allergy;
		}

		setIds();
		setCode();
		setStatusCode();
		setEffectiveTime();
		setAllergyObservation();
	}

	public String getTextSummary() {
		StringBuilder sb = new StringBuilder();

		if(!EverestUtils.isNullorEmptyorWhitespace(allergy.getDescription())) {
			sb.append(allergy.getDescription());
		}
		if(allergy.getEntryDate() != null) {
			sb.append(" " + allergy.getEntryDate().toString());
		}

		return sb.toString();
	}

	public SET<II> getIds() {
		return ids;
	}

	private void setIds() {
		this.ids = EverestUtils.buildUniqueId(Constants.IdPrefixes.Allergies, allergy.getId());
	}

	public CD<String> getCode() {
		return code;
	}

	private void setCode() {
		CD<String> code = new CD<String>("48765-2", Constants.CodeSystems.LOINC_OID,
				Constants.CodeSystems.LOINC_NAME, Constants.CodeSystems.LOINC_VERSION);
		this.code = code;
	}

	public ActStatus getStatusCode() {
		return statusCode;
	}

	private void setStatusCode() {
		if(allergy.getArchived()) {
			this.statusCode = ActStatus.Completed;
		} else {
			this.statusCode = ActStatus.Active;
		}
	}

	public IVL<TS> getEffectiveTime() {
		return effectiveTime;
	}

	private void setEffectiveTime() {
		IVL<TS> ivl = new IVL<TS>();
		TS startTime = EverestUtils.buildTSFromDate(allergy.getEntryDate());
		if(startTime != null) {
			ivl.setLow(startTime);
		} else {
			ivl.setNullFlavor(NullFlavor.Unknown);
		}

		this.effectiveTime = ivl;
	}

	public EntryRelationship getAllergyObservation() {
		return allergyObservation;
	}

	private void setAllergyObservation() {
		EntryRelationship entryRelationship = new EntryRelationship(x_ActRelationshipEntryRelationship.HasComponent, new BL(true));
		Observation observation = new Observation(x_ActMoodDocumentObservation.Eventoccurrence);
		ArrayList<EntryRelationship> entryRelationships = new ArrayList<EntryRelationship>();

		observation.setCode(getAdverseEventCode());
		observation.setEffectiveTime(getOnsetDate());
		observation.setParticipant(getAllergen());

		entryRelationships.add(getAllergenGroup());
		entryRelationships.add(getLifestage());
		entryRelationships.add(getReaction());
		entryRelationships.add(getSeverity());
		entryRelationships.add(getClinicalStatus());

		observation.setEntryRelationship(entryRelationships);
		entryRelationship.setClinicalStatement(observation);

		this.allergyObservation = entryRelationship;
	}

	protected CD<String> getAdverseEventCode() {
		CD<String> code = new CD<String>();
		if(Mappings.reactionTypeCode.containsKey(allergy.getTypeCode())) {
			code.setCodeEx(Mappings.reactionTypeCode.get(allergy.getTypeCode()));
			code.setCodeSystem(Constants.CodeSystems.ACT_CODE_CODESYSTEM_OID);
			code.setCodeSystemName(Constants.CodeSystems.ACT_CODE_CODESYSTEM_NAME);
		} else {
			code.setNullFlavor(NullFlavor.Unknown);
		}

		return code;
	}

	protected IVL<TS> getOnsetDate() {
		IVL<TS> ivl = new IVL<TS>();
		TS startTime = EverestUtils.buildTSFromDate(allergy.getStartDate());
		if(startTime != null) {
			ivl.setLow(startTime);
		} else {
			ivl.setNullFlavor(NullFlavor.Unknown);
		}

		return ivl;
	}

	protected ArrayList<Participant2> getAllergen() {
		Participant2 participant = new Participant2(ParticipationType.Consumable, ContextControl.OverridingPropagating);
		ParticipantRole participantRole = new ParticipantRole(new CD<String>(Constants.RoleClass.MANU.toString()));
		PlayingEntity playingEntity = new PlayingEntity(EntityClassRoot.ManufacturedMaterial);

		CE<String> code = new CE<String>();
		if(!EverestUtils.isNullorEmptyorWhitespace(allergy.getRegionalIdentifier())) {
			code.setCodeEx(allergy.getRegionalIdentifier());
			code.setCodeSystem(Constants.CodeSystems.DIN_OID);
			code.setCodeSystemName(Constants.CodeSystems.DIN_NAME);
		} else {
			code.setNullFlavor(NullFlavor.NoInformation);
		}

		SET<PN> names = new SET<PN>(new PN(null, Arrays.asList(new ENXP(allergy.getDescription()))));

		playingEntity.setCode(code);
		playingEntity.setName(names);

		participantRole.setPlayingEntityChoice(playingEntity);
		participant.setParticipantRole(participantRole);

		return new ArrayList<Participant2>(Arrays.asList(participant));
	}

	protected EntryRelationship getAllergenGroup() {
		EntryRelationship entryRelationship = new EntryRelationship(x_ActRelationshipEntryRelationship.HasComponent, new BL(true));
		Observation observation = new Observation(x_ActMoodDocumentObservation.Eventoccurrence);

		CD<String> code = new CD<String>(Constants.ObservationType.ALRGRP.toString(), Constants.CodeSystems.ACT_CODE_CODESYSTEM_OID);
		code.setCodeSystemName(Constants.CodeSystems.ACT_CODE_CODESYSTEM_NAME);

		CD<String> value = new CD<String>();
		value.setNullFlavor(NullFlavor.NoInformation);

		observation.setCode(code);
		observation.setValue(value);

		entryRelationship.setClinicalStatement(observation);
		return entryRelationship;
	}

	protected EntryRelationship getLifestage() {
		return new LifestageObservationModel().getEntryRelationship(allergy.getLifeStage());
	}

	protected EntryRelationship getReaction() {
		return new ReactionObservationModel().getEntryRelationship(allergy.getReaction(), allergy.getStartDate(), allergy.getProviderNo(), allergy.getEntryDate(), allergy.getSeverityOfReaction());
	}

	protected EntryRelationship getSeverity() {
		return new SeverityObservationModel().getEntryRelationship(allergy.getSeverityOfReaction());
	}

	protected EntryRelationship getClinicalStatus() {
		EntryRelationship entryRelationship = new EntryRelationship(x_ActRelationshipEntryRelationship.SUBJ, new BL(true));
		Observation observation = new Observation(x_ActMoodDocumentObservation.Eventoccurrence);

		CD<String> code = new CD<String>(Constants.ObservationType.CLINSTAT.toString(), Constants.CodeSystems.OBSERVATIONTYPE_CA_PENDING_OID);
		code.setCodeSystemName(Constants.CodeSystems.OBSERVATIONTYPE_CA_PENDING_NAME);

		ED text = new ED();
		text.setNullFlavor(NullFlavor.NoInformation);

		CD<String> value = new CD<String>();
		value.setNullFlavor(NullFlavor.NoInformation);

		observation.setCode(code);
		observation.setText(text);
		observation.setValue(value);

		entryRelationship.setClinicalStatement(observation);
		return entryRelationship;
	}
}
