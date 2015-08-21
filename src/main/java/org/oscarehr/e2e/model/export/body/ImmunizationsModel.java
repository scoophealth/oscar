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
import org.marc.everest.datatypes.interfaces.ISetComponent;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Author;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Consumable;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.EntryRelationship;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Participant2;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ParticipantRole;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.PlayingEntity;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.SubstanceAdministration;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ContextControl;
import org.marc.everest.rmim.uv.cdar2.vocabulary.EntityClassRoot;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ParticipationType;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_ActRelationshipEntryRelationship;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_DocumentSubstanceMood;
import org.oscarehr.e2e.constant.Constants;
import org.oscarehr.e2e.model.PatientExport.Immunization;
import org.oscarehr.e2e.model.export.template.AuthorParticipationModel;
import org.oscarehr.e2e.model.export.template.ConsumableModel;
import org.oscarehr.e2e.model.export.template.observation.CommentObservationModel;
import org.oscarehr.e2e.model.export.template.observation.DateObservationModel;
import org.oscarehr.e2e.model.export.template.observation.ReasonObservationModel;
import org.oscarehr.e2e.util.EverestUtils;

public class ImmunizationsModel {
	private Immunization immunization;

	private BL negationInd;
	private SET<II> ids;
	private CD<String> code;
	private ArrayList<ISetComponent<TS>> effectiveTime;
	private CE<String> route;
	private Consumable consumable;
	private ArrayList<Author> authors;
	private ArrayList<Participant2> participant;
	private EntryRelationship antigenType;
	private EntryRelationship refusalReason;
	private EntryRelationship nextDate;
	private EntryRelationship comment;

	public ImmunizationsModel(Immunization immunization) {
		if(immunization == null) {
			this.immunization = new Immunization(null, null);
		} else {
			this.immunization = immunization;
		}

		setNegationInd();
		setIds();
		setCode();
		setEffectiveTime();
		setRoute();
		// Dose not included because freetext dose can't be converted to IVL_PQ representation safely
		setConsumable();
		setAuthor();
		setParticipant();
		setAntigenType();
		setRefusalReason();
		setNextDate();
		setComment();
	}

	public String getTextSummary() {
		StringBuilder sb = new StringBuilder();

		if(!EverestUtils.isNullorEmptyorWhitespace(immunization.getPrevention().getPreventionType())) {
			sb.append(immunization.getPrevention().getPreventionType());
		}
		if(immunization.getPrevention().getPreventionDate() != null) {
			sb.append(" " + immunization.getPrevention().getPreventionDate());
		}
		if(immunization.getPrevention().isRefused()) {
			sb.append(" Refused");
		} else if(immunization.getPrevention().isIneligible()) {
			sb.append(" Ineligible");
		} else {
			sb.append(" Completed");
		}

		return sb.toString();
	}

	public BL getNegationInd() {
		return negationInd;
	}

	private void setNegationInd() {
		Boolean isNegated = immunization.getPrevention().isRefused() || immunization.getPrevention().isIneligible();
		this.negationInd = new BL(isNegated);
	}

	public SET<II> getIds() {
		return ids;
	}

	private void setIds() {
		this.ids = EverestUtils.buildUniqueId(Constants.IdPrefixes.Immunizations, immunization.getPrevention().getId());
	}

	public CD<String> getCode() {
		return code;
	}

	private void setCode() {
		this.code = new CD<String>(Constants.SubstanceAdministrationType.IMMUNIZ.toString(), Constants.CodeSystems.ACT_CODE_CODESYSTEM_OID);
		this.code.setCodeSystemName(Constants.CodeSystems.ACT_CODE_CODESYSTEM_NAME);
	}

	public ArrayList<ISetComponent<TS>> getEffectiveTime() {
		return effectiveTime;
	}

	private void setEffectiveTime() {
		TS startTime = EverestUtils.buildTSFromDate(immunization.getPrevention().getPreventionDate());

		IVL<TS> ivl = new IVL<TS>();
		if(startTime == null) {
			startTime = new TS();
			startTime.setNullFlavor(NullFlavor.Unknown);
		}
		ivl.setLow(startTime);

		this.effectiveTime = new ArrayList<ISetComponent<TS>>();
		this.effectiveTime.add(ivl);
	}

	public CE<String> getRoute() {
		return route;
	}

	private void setRoute() {
		CE<String> route = null;
		if(!EverestUtils.isNullorEmptyorWhitespace(immunization.getPreventionMap().get(Constants.PreventionExtKeys.route.toString()))) {
			route = new CE<String>();
			route.setNullFlavor(NullFlavor.Other);
			route.setCodeSystem(Constants.CodeSystems.ROUTE_OF_ADMINISTRATION_OID);
			route.setCodeSystemName(Constants.CodeSystems.ROUTE_OF_ADMINISTRATION_NAME);
			route.setOriginalText(new ED(immunization.getPreventionMap().get(Constants.PreventionExtKeys.route.toString())));
		}

		this.route = route;
	}

	public Consumable getConsumable() {
		return consumable;
	}

	private void setConsumable() {
		this.consumable = new ConsumableModel().getConsumable(immunization);
	}

	public ArrayList<Author> getAuthor() {
		return authors;
	}

	private void setAuthor() {
		ArrayList<Author> authors = new ArrayList<Author>();
		authors.add(new AuthorParticipationModel(immunization.getPrevention().getProviderNo()).getAuthor(immunization.getPrevention().getCreationDate()));
		this.authors = authors;
	}

	public ArrayList<Participant2> getParticipant() {
		return participant;
	}

	private void setParticipant() {
		Participant2 participant = new Participant2(ParticipationType.LOC, ContextControl.OverridingPropagating);
		ParticipantRole participantRole = new ParticipantRole(new CD<String>(Constants.RoleClass.SDLOC.toString()));
		PlayingEntity playingEntity = new PlayingEntity(EntityClassRoot.Organization);

		if(!EverestUtils.isNullorEmptyorWhitespace(immunization.getPreventionMap().get(Constants.PreventionExtKeys.location.toString()))) {
			SET<PN> names = new SET<PN>();
			ArrayList<ENXP> name = new ArrayList<ENXP>();
			name.add(new ENXP(immunization.getPreventionMap().get(Constants.PreventionExtKeys.location.toString())));
			names.add(new PN(null, name));
			playingEntity.setName(names);
		}

		participantRole.setPlayingEntityChoice(playingEntity);
		participant.setParticipantRole(participantRole);

		this.participant = new ArrayList<Participant2>(Arrays.asList(participant));
	}

	public EntryRelationship getAntigenType() {
		return antigenType;
	}

	private void setAntigenType() {
		Immunization immunization = null;
		EntryRelationship entryRelationship = new EntryRelationship();
		Consumable consumable = new ConsumableModel().getConsumable(immunization);
		SubstanceAdministration substanceAdministration = new SubstanceAdministration(x_DocumentSubstanceMood.Eventoccurrence, consumable);

		entryRelationship.setTypeCode(x_ActRelationshipEntryRelationship.HasComponent);
		entryRelationship.setContextConductionInd(true);
		entryRelationship.setClinicalStatement(substanceAdministration);

		this.antigenType = entryRelationship;
	}

	public EntryRelationship getRefusalReason() {
		return refusalReason;
	}

	private void setRefusalReason() {
		this.refusalReason = new ReasonObservationModel().getEntryRelationship(immunization.getPreventionMap().get(Constants.PreventionExtKeys.neverReason.toString()), immunization.getPrevention().getCreationDate(), immunization.getPrevention().getProviderNo());
	}

	public EntryRelationship getNextDate() {
		return nextDate;
	}

	private void setNextDate() {
		this.nextDate = new DateObservationModel().getEntryRelationship(immunization.getPrevention().getNextDate());
	}

	public EntryRelationship getComment() {
		return comment;
	}

	private void setComment() {
		this.comment = new CommentObservationModel().getEntryRelationship(immunization.getPreventionMap().get(Constants.PreventionExtKeys.comments.toString()), immunization.getPrevention().getCreationDate(), immunization.getPrevention().getProviderNo());
	}
}
