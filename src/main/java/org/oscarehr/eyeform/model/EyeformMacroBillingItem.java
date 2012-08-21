/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

package org.oscarehr.eyeform.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.oscarehr.common.model.AbstractModel;

@Entity
@Table(name = "eyeform_macro_billing")
public class EyeformMacroBillingItem extends AbstractModel<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Integer macroId;
	private String billingServiceCode;
	private Double multiplier;


	@Override
	public Integer getId() {
		return id;
	}

	public String getBillingServiceCode() {
		return billingServiceCode;
	}

	public void setBillingServiceCode(String billingServiceCode) {
		this.billingServiceCode = billingServiceCode;
	}

	public Double getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(Double multiplier) {
		this.multiplier = multiplier;
	}

	public void setMacroId(Integer macroId) {
		this.macroId = macroId;
	}

	public Integer getMacroId() {
		return macroId;
	}
}
