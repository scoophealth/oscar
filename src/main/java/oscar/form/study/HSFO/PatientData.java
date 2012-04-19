/**
 * Copyright (C) 2007  Heart & Stroke Foundation
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


package oscar.form.study.HSFO;

import java.util.Date;

/**
 * Class used by the HSFO Study
 * 
 */
public class PatientData {
    
    /** Creates a new instance of PatientData */
    public PatientData() {
    }
    
    PatientData patientData;
	
	String SiteCode;
	String Patient_Id;
	String FName;
	String LName;
	Date BirthDate;
	String Sex; // enum('m', 'f');
	String PostalCode;
	double Height; // double(3, 1);
	String Height_unit; // enum('cm', 'inch');
	boolean Ethnic_White;
	boolean Ethnic_Black;
	boolean Ethnic_EIndian;
	boolean Ethnic_Pakistani;
	boolean Ethnic_SriLankan;
	boolean Ethnic_Bangladeshi;
	boolean Ethnic_Chinese;
	boolean Ethnic_Japanese;
	boolean Ethnic_Korean;
	boolean Ethnic_Hispanic;
	boolean Ethnic_FirstNation;
	boolean Ethnic_Other;
	boolean Ethnic_Refused;
	boolean Ethnic_Unknown;
	String PharmacyName;
	String PharmacyLocation;
	String sel_TimeAgoDx; // enum('AtLeast1YrAgo', 'Under1YrAgo', 'NA');
	String EmrHCPId;
	Date consentDate;
		
	
	public PatientData getPatientData() {
		return patientData;
	}

	public void setPatientData(
			String SiteCode,
			String Patient_Id,
			String FName,
			String LName,
			Date BirthDate,
			String Sex,
			String PostalCode,
			double Height,
			String Height_unit, 
			boolean Ethnic_White,
			boolean Ethnic_Black,
			boolean Ethnic_EIndian,
			boolean Ethnic_Pakistani,
			boolean Ethnic_SriLankan,
			boolean Ethnic_Bangladeshi,
			boolean Ethnic_Chinese,
			boolean Ethnic_Japanese,
			boolean Ethnic_Korean,
			boolean Ethnic_Hispanic,
			boolean Ethnic_FirstNation,
			boolean Ethnic_Other,
			boolean Ethnic_Refused,
			boolean Ethnic_Unknown,
			String PharmacyName,
			String PharmacyLocation,
			String sel_TimeAgoDx, 
			String EmrHCPId,
			Date consentDate) 
	{
		this.SiteCode = SiteCode;
		this.Patient_Id = Patient_Id;
		this.FName = FName;
		this.LName = LName;
		this.BirthDate = BirthDate;
		this.Sex = Sex;
		this.PostalCode = PostalCode;
		this.Height = Height;
		this.Height_unit = Height_unit; 
		this.Ethnic_White = Ethnic_White;
		this.Ethnic_Black = Ethnic_Black;
		this.Ethnic_EIndian = Ethnic_EIndian;
		this.Ethnic_Pakistani = Ethnic_Pakistani;
		this.Ethnic_SriLankan = Ethnic_SriLankan;
		this.Ethnic_Bangladeshi = Ethnic_Bangladeshi;
		this.Ethnic_Chinese = Ethnic_Chinese;
		this.Ethnic_Japanese = Ethnic_Japanese;
		this.Ethnic_Korean = Ethnic_Korean;
		this.Ethnic_Hispanic = Ethnic_Hispanic;
		this.Ethnic_FirstNation = Ethnic_FirstNation;
		this.Ethnic_Other = Ethnic_Other;
		this.Ethnic_Refused = Ethnic_Refused;
		this.Ethnic_Unknown = Ethnic_Unknown;
		this.PharmacyName = PharmacyName;
		this.PharmacyLocation = PharmacyLocation;
		this.sel_TimeAgoDx = sel_TimeAgoDx; 
		this.EmrHCPId = EmrHCPId;
		this.consentDate = consentDate;
	}

	
	
	public Date getBirthDate() {
		return BirthDate;
	}

	public void setBirthDate(Date birthDate) {
		BirthDate = birthDate;
	}

	

	public String getEmrHCPId() {
		return EmrHCPId;
	}

	public void setEmrHCPId(String emrHCPId) {
		EmrHCPId = emrHCPId;
	}

	public boolean isEthnic_Bangladeshi() {
		return Ethnic_Bangladeshi;
	}

	public void setEthnic_Bangladeshi(boolean ethnic_Bangladeshi) {
		Ethnic_Bangladeshi = ethnic_Bangladeshi;
	}

	public boolean isEthnic_Black() {
		return Ethnic_Black;
	}

	public void setEthnic_Black(boolean ethnic_Black) {
		Ethnic_Black = ethnic_Black;
	}

	public boolean isEthnic_Chinese() {
		return Ethnic_Chinese;
	}

	public void setEthnic_Chinese(boolean ethnic_Chinese) {
		Ethnic_Chinese = ethnic_Chinese;
	}

	public boolean isEthnic_EIndian() {
		return Ethnic_EIndian;
	}

	public void setEthnic_EIndian(boolean ethnic_EIndian) {
		Ethnic_EIndian = ethnic_EIndian;
	}

	public boolean isEthnic_FirstNation() {
		return Ethnic_FirstNation;
	}

	public void setEthnic_FirstNation(boolean ethnic_FirstNation) {
		Ethnic_FirstNation = ethnic_FirstNation;
	}

	public boolean isEthnic_Hispanic() {
		return Ethnic_Hispanic;
	}

	public void setEthnic_Hispanic(boolean ethnic_Hispanic) {
		Ethnic_Hispanic = ethnic_Hispanic;
	}

	public boolean isEthnic_Japanese() {
		return Ethnic_Japanese;
	}

	public void setEthnic_Japanese(boolean ethnic_Japanese) {
		Ethnic_Japanese = ethnic_Japanese;
	}

	public boolean isEthnic_Korean() {
		return Ethnic_Korean;
	}

	public void setEthnic_Korean(boolean ethnic_Korean) {
		Ethnic_Korean = ethnic_Korean;
	}

	public boolean isEthnic_Other() {
		return Ethnic_Other;
	}

	public void setEthnic_Other(boolean ethnic_Other) {
		Ethnic_Other = ethnic_Other;
	}

	public boolean isEthnic_Pakistani() {
		return Ethnic_Pakistani;
	}

	public void setEthnic_Pakistani(boolean ethnic_Pakistani) {
		Ethnic_Pakistani = ethnic_Pakistani;
	}

	public boolean isEthnic_Refused() {
		return Ethnic_Refused;
	}

	public void setEthnic_Refused(boolean ethnic_Refused) {
		Ethnic_Refused = ethnic_Refused;
	}

	public boolean isEthnic_SriLankan() {
		return Ethnic_SriLankan;
	}

	public void setEthnic_SriLankan(boolean ethnic_SriLankan) {
		Ethnic_SriLankan = ethnic_SriLankan;
	}

	public boolean isEthnic_Unknown() {
		return Ethnic_Unknown;
	}

	public void setEthnic_Unknown(boolean ethnic_Unknown) {
		Ethnic_Unknown = ethnic_Unknown;
	}

	public boolean isEthnic_White() {
		return Ethnic_White;
	}

	public void setEthnic_White(boolean ethnic_White) {
		Ethnic_White = ethnic_White;
	}

	

	public String getFName() {
		return FName;
	}

	public void setFName(String name) {
		FName = name;
	}


	public double getHeight() {
		return Height;
	}

	public void setHeight(double height) {
		Height = height;
	}

	public String getHeight_unit() {
		return Height_unit;
	}

	public void setHeight_unit(String height_unit) {
		Height_unit = height_unit;
	}

	

	public String getLName() {
		return LName;
	}

	public void setLName(String name) {
		LName = name;
	}

	public String getPatient_Id() {
		return Patient_Id;
	}

	public void setPatient_Id(String patient_Id) {
		Patient_Id = patient_Id;
	}

	public String getPharmacyLocation() {
		return PharmacyLocation;
	}

	public void setPharmacyLocation(String pharmacyLocation) {
		PharmacyLocation = pharmacyLocation;
	}

	public String getPharmacyName() {
		return PharmacyName;
	}

	public void setPharmacyName(String pharmacyName) {
		PharmacyName = pharmacyName;
	}

	public String getPostalCode() {
		return PostalCode;
	}

	public void setPostalCode(String postalCode) {
		PostalCode = postalCode;
	}

	public String getSel_TimeAgoDx() {
		return sel_TimeAgoDx;
	}

	public void setSel_TimeAgoDx(String sel_TimeAgoDx) {
		this.sel_TimeAgoDx = sel_TimeAgoDx;
	}

	public String getSex() {
		return Sex;
	}

	public void setSex(String sex) {
		Sex = sex;
	}

	public String getSiteCode() {
		return SiteCode;
	}

	public void setSiteCode(String siteCode) {
		SiteCode = siteCode;
	}

	public Date getConsentDate() {
		return consentDate;
	}

	public void setConsentDate(Date consentDate) {
		this.consentDate = consentDate;
	}
    
}
