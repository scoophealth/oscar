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
package org.oscarehr.e2e.model.export.template;

import java.util.Arrays;

import org.marc.everest.datatypes.EN;
import org.marc.everest.datatypes.ENXP;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.NullFlavor;
import org.marc.everest.datatypes.ST;
import org.marc.everest.datatypes.generic.CE;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Consumable;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.LabeledDrug;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ManufacturedProduct;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Material;
import org.marc.everest.rmim.uv.cdar2.vocabulary.DrugEntity;
import org.marc.everest.rmim.uv.cdar2.vocabulary.EntityDeterminerDetermined;
import org.marc.everest.rmim.uv.cdar2.vocabulary.RoleClassManufacturedProduct;
import org.oscarehr.common.model.Drug;
import org.oscarehr.e2e.constant.Constants;
import org.oscarehr.e2e.model.PatientExport.Immunization;
import org.oscarehr.e2e.util.EverestUtils;

public class ConsumableModel {
	private Consumable consumable;

	public ConsumableModel() {
		consumable = new Consumable();
		consumable.setManufacturedProduct(new ManufacturedProduct());
	}

	public Consumable getConsumable(Immunization immunization) {
		if(immunization == null) {
			immunization = new Immunization(null, null);
		}

		ManufacturedProduct manufacturedProduct = consumable.getManufacturedProduct();
		manufacturedProduct.setClassCode(RoleClassManufacturedProduct.ManufacturedProduct);

		Material material = new Material();
		material.setDeterminerCode(EntityDeterminerDetermined.Described);
		material.setCode(getImmunizationCode(immunization));
		material.setName(getImmunizationName(immunization));
		material.setLotNumberText(getImmunizationLotNumber(immunization));

		manufacturedProduct.setManufacturedDrugOrOtherMaterial(material);

		return consumable;
	}

	private CE<String> getImmunizationCode(Immunization immunization) {
		CE<String> code = new CE<String>();

		if(!EverestUtils.isNullorEmptyorWhitespace(EverestUtils.getPreventionType(immunization.getPrevention().getPreventionType()))) {
			code.setCode(EverestUtils.getPreventionType(immunization.getPrevention().getPreventionType()));
			code.setCodeSystem(Constants.CodeSystems.ATC_OID);
			code.setCodeSystemName(Constants.CodeSystems.ATC_NAME);
		} else {
			code.setNullFlavor(NullFlavor.NoInformation);
		}

		return code;
	}

	private EN getImmunizationName(Immunization immunization) {
		EN name = new EN();

		if(!EverestUtils.isNullorEmptyorWhitespace(immunization.getPrevention().getPreventionType())) {
			name.setParts(Arrays.asList(new ENXP(immunization.getPrevention().getPreventionType())));
		} else {
			name.setNullFlavor(NullFlavor.NoInformation);
		}

		return name;
	}

	private ST getImmunizationLotNumber(Immunization immunization) {
		ST lot = null;
		if(!EverestUtils.isNullorEmptyorWhitespace(immunization.getPreventionMap().get(Constants.PreventionExtKeys.lot.toString()))) {
			lot = new ST(immunization.getPreventionMap().get(Constants.PreventionExtKeys.lot.toString()));
		}
		return lot;
	}

	// TODO [MARC-HI] Add e2e namespace extension fields
	public Consumable getConsumable(Drug drug) {
		if(drug == null) {
			drug = new Drug();
		}

		consumable.setTemplateId(Arrays.asList(new II(Constants.TemplateOids.MEDICATION_IDENTIFICATION_TEMPLATE_ID)));

		ManufacturedProduct manufacturedProduct = consumable.getManufacturedProduct();
		manufacturedProduct.setClassCode(RoleClassManufacturedProduct.ManufacturedProduct);

		LabeledDrug labeledDrug = new LabeledDrug();
		labeledDrug.setDeterminerCode(EntityDeterminerDetermined.Described);
		labeledDrug.setCode(getDrugCode(drug));
		labeledDrug.setName(getDrugName(drug));

		manufacturedProduct.setManufacturedDrugOrOtherMaterial(labeledDrug);

		return consumable;
	}

	private CE<DrugEntity> getDrugCode(Drug drug) {
		CE<DrugEntity> code = new CE<DrugEntity>();

		if(!EverestUtils.isNullorEmptyorWhitespace(drug.getRegionalIdentifier())) {
			code.setCodeEx(new DrugEntity(drug.getRegionalIdentifier().trim(), Constants.CodeSystems.DIN_OID));
			code.setCodeSystemName(Constants.CodeSystems.DIN_NAME);
		} else if(!EverestUtils.isNullorEmptyorWhitespace(drug.getAtc())) {
			code.setCodeEx(new DrugEntity(drug.getAtc().trim(), Constants.CodeSystems.ATC_OID));
			code.setCodeSystemName(Constants.CodeSystems.ATC_NAME);
		} else {
			code.setNullFlavor(NullFlavor.NoInformation);
		}

		return code;
	}

	private EN getDrugName(Drug drug) {
		EN name = new EN();

		if(!EverestUtils.isNullorEmptyorWhitespace(drug.getGenericName())) {
			name.setParts(Arrays.asList(new ENXP(drug.getGenericName())));
		} else if(!EverestUtils.isNullorEmptyorWhitespace(drug.getBrandName())) {
			name.setParts(Arrays.asList(new ENXP(drug.getBrandName())));
		} else {
			name.setNullFlavor(NullFlavor.NoInformation);
		}

		return name;
	}
}
