/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package oscar.form.pharmaForms.formBPMH.bean;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

/*
 * Author: Dennis Warren 
 * Company: Colcamex Resources
 * Date: November 2014
 * For: UBC Pharmacy Clinic and McMaster Department of Family Medicine
 * 
 * Defines medication data for:
 * 	WHAT I TAKE : Generic name / dose / delivery
 * 	HOW I TAKE IT : Once per day with food / bid, pid etc...
 * 	WHY I TAKE IT : ICD9 code / special reasons
 *  SPECIAL INSTRUCTIONS : additional instructions
 */
public class BpmhDrug {
	
	private String id;
	private String demographicId;
	private String genericName;
	private String atc;
	private String customName;
	private String takeMin;
	private String takeMax;
	private String freqCode;
	private String duration;
	private String durUnit;
	private String quantity;
	private String repeat;
	private String prn;
	private String special;
	private String unit;
	private String method;
	private String drugForm;
	private String route;
	private String dosage;
	private String position;
	private String customInstructions;
	private String special_instruction;
	private String outsideProviderName;
	private String outsideProviderOhip;
	private String customNote;
	private String comment; // used as instructions in the BPMH. saved in drugs.comment
	// drug reason - why I take it - drugReason table
	private String codingSystem;
	private String code;
	private String comments; // used as Why I Take it custom comments. saved in drugReason.comments
	// dx interpret - diagnosticcode table

	private String what;
	private String how;
	private String why;
	private String instruction;
	
	/*
	 * Treats what disease or reason?
	 */
	public String getWhy() {
		if(this.why == null) {
			return "";
		}
		return this.why;
	}
	
	public void setWhy(String why) {
		this.why = why;
	}
	
	/*
	 * Prescription details. Take once per day with food
	 */
	public String getHow() {
		
		if( this.how != null ) {
			return this.how;
		}
		
		StringBuilder stringBuilder = new StringBuilder("");		
		String direction = "";
		String[] specialArray = null;

		if( getSpecial() != null ) {
			// instruction is normally on the second line.
			specialArray = getSpecial().split("\\n");
			if( ( specialArray != null ) && ( specialArray.length > 1 ) ) {
				direction = specialArray[1];
			}
		}
		
		if( ! StringUtils.isBlank( direction ) ) {
			
			stringBuilder.append( direction );
			
		} else {

			if( ! getMethod().isEmpty() ) {
				stringBuilder.append( getMethod() + " " );
			}
			if( ! getTakeMin().isEmpty() ) {
				stringBuilder.append( getTakeMin() + " " );
			}
			if( ! getTakeMax().isEmpty() && ! getTakeMax().equals("0") && ! getTakeMax().equals( getTakeMin() ) ) {
				stringBuilder.append( "or " +  getTakeMax() + " ");
			}
			if( ! getDrugForm().isEmpty() ) {
				stringBuilder.append( getDrugForm() + " ");
			}

			if( ! getFreqCode().isEmpty() ) {
				stringBuilder.append( getFreqCode() + " ");
			}

			if( ! getRoute().isEmpty() ) {
				stringBuilder.append( getRoute() + " " );
			}
			
			if( ! getSpecial_instruction().isEmpty() ) {
				stringBuilder.append( getSpecial_instruction() + " " );
			}
			
			if( ! getCustomInstructions().isEmpty() ) {
				stringBuilder.append( getCustomInstructions() );
			}
		} 

		setHow( stringBuilder.toString() );
		
		return this.how;
	}
	
	public void setHow(String how) {
		this.how = how;
	}
	
	/*
	 * Drug Generic name, Strength, Dose/Delivery
	 */
	public String getWhat() {
		
		if(this.what != null) {
			return this.what;
		}
		
		StringBuilder stringBuilder = new StringBuilder("");
		String drugName = "";
		String unit = "";

		if( ! getGenericName().isEmpty() ) {
			
			stringBuilder.append( getGenericName() + " " );
			drugName = getGenericName();
			
		} else if ( ! getCustomName().isEmpty() ) { // if generic is null then check for custom
			
			stringBuilder.append( getCustomName() + " " );
			drugName = getCustomName();
		}


		if( ! drugName.contains( getDosage() ) ) {
			stringBuilder.append( getDosage() + " ");
		}
	
		if( ! getUnit().isEmpty() ) {
			unit = getUnit();
		}
		
		if( ! stringBuilder.toString().contains(unit) ) {
			stringBuilder.append(unit + " ");
		}
		
		if( ! getDrugForm().isEmpty() ) {
			stringBuilder.append( getDrugForm() );
		}
		
		setWhat( stringBuilder.toString() );
		
		return what;
	}
	
	public void setWhat(String what) {
		this.what = what;
	}
	
	/*
	 * Additional special instructions
	 * 
	 */
	public String getInstruction() {	
		return this.instruction;
	}
	
	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}
	
	
	// -------------------------------JPA POJO------------------------------------- //

	public String getTakeMin() {
		if(this.takeMin == null) {
			return "";
		}
		return takeMin;
	}

	public void setTakeMin(String takeMin) {
		this.takeMin = takeMin;
	}

	public String getTakeMax() {
		if(this.takeMax == null) {
			return "";
		}
		return takeMax;
	}

	public void setTakeMax(String takeMax) {
		this.takeMax = takeMax;
	}

	public String getFreqCode() {
		if(this.freqCode == null) {
			return "";
		}
		return freqCode;
	}

	public void setFreqCode(String freqCode) {
		this.freqCode = freqCode;
	}

	public String getDurUnit() {
		return durUnit;
	}

	public void setDurUnit(String durUnit) {
		this.durUnit = durUnit;
	}

	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getRepeat() {
		return repeat;
	}
	public void setRepeat(String repeat) {
		this.repeat = repeat;
	}
	public String getPrn() {
		return prn;
	}
	public void setPrn(String prn) {
		this.prn = prn;
	}
	public String getSpecial() {
		return special;
	}
	public void setSpecial(String special) {
		this.special = special;
	}
	public String getUnit() {
		if(unit == null) {
			return "";
		}
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getMethod() {
		if(this.method == null) {
			return "";
		}
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}

	public String getRoute() {
		if(this.route == null) {
			return "";
		}
		return route;
	}
	public void setRoute(String route) {
		this.route = route;
	}
	public String getDosage() {
		if(dosage == null) {
			return "";
		}
		return dosage;
	}
	public void setDosage(String dosage) {
		this.dosage = dosage;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCodingSystem() {
		return codingSystem;
	}
	public void setCodingSystem(String codingSystem) {
		this.codingSystem = codingSystem;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getSpecial_instruction() {
		if(this.special_instruction == null) {
			return "";
		}
		return special_instruction;
	}

	public void setSpecial_instruction(String special_instruction) {
		this.special_instruction = special_instruction;
	}

	public String getGenericName() {
		if(genericName == null) {
			return "";
		}
		return genericName;
	}

	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDemographicId() {
		return demographicId;
	}

	public void setDemographicId(String demographicId) {
		this.demographicId = demographicId;
	}

	public String getAtc() {
		return atc;
	}

	public void setAtc(String atc) {
		this.atc = atc;
	}

	public String getCustomName() {
		if(customName == null) {
			return "";
		}
		return customName;
	}

	public void setCustomName(String customName) {
		this.customName = customName;
	}

	public String getDrugForm() {
		if(this.drugForm == null) {
			return "";
		}
		return drugForm;
	}

	public void setDrugForm(String drugForm) {
		this.drugForm = drugForm;
	}

	public String getCustomInstructions() {
		if(this.customInstructions == null) {
			return "";
		}
		return customInstructions;
	}

	public void setCustomInstructions(String customInstructions) {
		this.customInstructions = customInstructions;
	}

	public String getOutsideProviderName() {
		return outsideProviderName;
	}

	public void setOutsideProviderName(String outsideProviderName) {
		this.outsideProviderName = outsideProviderName;
	}

	public String getOutsideProviderOhip() {
		return outsideProviderOhip;
	}

	public void setOutsideProviderOhip(String outsideProviderOhip) {
		this.outsideProviderOhip = outsideProviderOhip;
	}

	public String getCustomNote() {
		return customNote;
	}

	public void setCustomNote(String customNote) {
		this.customNote = customNote;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
