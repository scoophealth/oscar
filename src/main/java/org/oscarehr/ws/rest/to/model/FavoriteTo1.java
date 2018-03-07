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

package org.oscarehr.ws.rest.to.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="favorite")
public class FavoriteTo1 implements Serializable {

	private static final long serialVersionUID = 1L;

    private Integer favoriteId;

    private String brandName;

    private String genericName;

    private String atc;

    private String favoriteName;

    // DIN number in Canada.
    private String regionalIdentifier;

    private Integer providerNo;

    private float takeMin;

    private float takeMax;

    private String frequency;

    private Integer duration;

    private String durationUnit;

    private Integer quantity;

    private String route;

    private String form;

    private String method;

    private Boolean prn;

    private Integer repeats;

    private String instructions;

    private Float strength;

    private String strengthUnit;

    private String externalProvider;

    private Boolean longTerm;

    private Boolean noSubstitutions;

    public Integer getId() {
        return favoriteId;
    }

    public void setId(Integer id) {
        this.favoriteId = id;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public String getAtc() {
        return atc;
    }

    public void setAtc(String atc) {
        this.atc = atc;
    }

    public String getFavoriteName() {
        return favoriteName;
    }

    public void setFavoriteName(String favoriteName) {
        this.favoriteName = favoriteName;
    }

    public String getRegionalIdentifier() {
        return regionalIdentifier;
    }

    public void setRegionalIdentifier(String regionalIdentifier) {
        this.regionalIdentifier = regionalIdentifier;
    }

    public Integer getProviderNo() {
        return providerNo;
    }

    public void setProviderNo(Integer providerNo) {
        this.providerNo = providerNo;
    }

    public float getTakeMin() {
        return takeMin;
    }

    public void setTakeMin(float takeMin) {
        this.takeMin = takeMin;
    }

    public float getTakeMax() {
        return takeMax;
    }

    public void setTakeMax(float takeMax) {
        this.takeMax = takeMax;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getDurationUnit() {
        return durationUnit;
    }

    public void setDurationUnit(String durationUnit) {
        this.durationUnit = durationUnit;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Boolean getPrn() {
        return prn;
    }

    public void setPrn(Boolean prn) {
        this.prn = prn;
    }

    public Integer getRepeats() {
        return repeats;
    }

    public void setRepeats(Integer repeats) {
        this.repeats = repeats;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Float getStrength() {
        return strength;
    }

    public void setStrength(Float strength) {
        this.strength = strength;
    }

    public String getStrengthUnit() {
        return strengthUnit;
    }

    public void setStrengthUnit(String strengthUnit) {
        this.strengthUnit = strengthUnit;
    }

    public String getExternalProvider() {
        return externalProvider;
    }

    public void setExternalProvider(String externalProvider) {
        this.externalProvider = externalProvider;
    }

    public Boolean getLongTerm() {
        return longTerm;
    }

    public void setLongTerm(Boolean longTerm) {
        this.longTerm = longTerm;
    }

    public Boolean getNoSubstitutions() {
        return noSubstitutions;
    }

    public void setNoSubstitutions(Boolean noSubstitutions) {
        this.noSubstitutions = noSubstitutions;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
