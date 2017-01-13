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
package org.oscarehr.integration.born;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.marc.everest.datatypes.AD;
import org.marc.everest.datatypes.BL;
import org.marc.everest.datatypes.ENXP;
import org.marc.everest.datatypes.EntityNamePartType;
import org.marc.everest.datatypes.EntityNameUse;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.INT;
import org.marc.everest.datatypes.ON;
import org.marc.everest.datatypes.PN;
import org.marc.everest.datatypes.PostalAddressUse;
import org.marc.everest.datatypes.TEL;
import org.marc.everest.datatypes.TelecommunicationsAddressUse;
import org.marc.everest.datatypes.generic.CE;
import org.marc.everest.datatypes.generic.CS;
import org.marc.everest.datatypes.generic.SET;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.AssignedAuthor;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.AssignedCustodian;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Author;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Component2;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Custodian;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.CustodianOrganization;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.NonXMLBody;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Organization;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Patient;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.PatientRole;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.Person;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.RecordTarget;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ActRelationshipHasComponent;
import org.marc.everest.rmim.uv.cdar2.vocabulary.AdministrativeGender;
import org.marc.everest.rmim.uv.cdar2.vocabulary.BindingRealm;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ContextControl;
import org.marc.everest.rmim.uv.cdar2.vocabulary.ParticipationFunction;
import org.marc.everest.rmim.uv.cdar2.vocabulary.x_BasicConfidentialityKind;
import org.marc.shic.cda.datatypes.CDAStandard;
import org.marc.shic.cda.datatypes.Code;
import org.marc.shic.cda.level1.Level1Document;
import org.marc.shic.cda.utils.CdaUtils;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Provider;

import oscar.OscarProperties;

public class BornCDADocument extends Level1Document{

	public static final String TITLE_A1A2 = "Antenatal Record";
	public static final String TITLE_18M ="Well Baby";
	public static final String TITLE_CSD ="Well Baby CSD";
	
	public static final String OID_CDA_ONTARIO_EHEALTH = "2.16.840.1.113883.3.239.34.1";
	public static final String OID_CONFIDENTIALITY = "2.16.840.1.113883.5.25";
	public static final String OID_LOINC = "2.16.840.1.113883.6.1";
	public static final String OID_DEMOGRAPHIC_HIN = "2.16.840.1.113883.4.59";
	public static final String OID_DEMOGRAPHIC_GENDER = "2.16.840.1.113883.5.1";
	public static final String OID_DEMOGRAPHIC_MARITAL_STATUS ="2.16.840.1.113883.5.2";
	public static final String OID_DEMOGRAPHIC_ID="2.16.840.1.113883.3.239.36.1.1.2";
	
	private BORNCDADocumentType type = BORNCDADocumentType.A1A2; 
	
    public enum BORNCDADocumentType {
        A1A2(1),
        EighteenMonth(2),
        CSD(3);
        
        private Integer value;
        
        private BORNCDADocumentType(Integer value) {
                this.value = value;
        }
        
        public Integer getValue() {
                return value;
        }
        
    }

	
 	public BornCDADocument(CDAStandard cdaStandard, BORNCDADocumentType type, Demographic demographic, List<Provider> providers, BornHialProperties props, Calendar effectiveDate, String id) {
		super(cdaStandard);
		this.type = type;
		
		CdaUtils.addTemplateRuleID(getRoot(), OID_CDA_ONTARIO_EHEALTH);
		this.removeTemplateRule("2.16.840.1.113883.10.20.3");
		this.removeTemplateRule("2.16.840.1.113883.10.20.22.1.1");
		
		
		
		setCode(getBORNCode());
		getRoot().setTitle(getBORNTitle());
		getRoot().setRealmCode(getBORNRealm());
		getRoot().setLanguageCode("eng-CA");
		
		
		
		getRoot().setEffectiveTime(effectiveDate,8);
		
		
		/*
		 * Unique ID generated by BORN or by a local system. Extension is the actual identifier. Root is the appropriate Document ID OID from the OIDs tab
		 */
		this.addId(props.getIdCodingSystem(), id);
		
		//TODO: For Updates (setId and Version). Not sure yet how this works.
		//<identifier that is common across all document revisions/versions>
		//Shall be present if version is populated"
		getRoot().setSetId(props.getSetIdCodingSystem(),id);
		INT ver = new INT(1);
		getRoot().setVersionNumber(ver);
		
		
		getRoot().setConfidentialityCode(getBORNConfidentialityCode());
				
		RecordTarget rt = new RecordTarget();
		
		CS<ContextControl> cc = new CS<ContextControl>();
		cc.setCodeEx(ContextControl.OverridingPropagating);
		rt.setContextControlCode(cc);
		
		
		PatientRole pr = new PatientRole();
		II localId = new II(OID_DEMOGRAPHIC_ID,demographic.getDemographicNo().toString());
		II ohip = new II(OID_DEMOGRAPHIC_HIN,demographic.getHin());
		SET<II> ids = new SET<II>();
		ids.add(ohip);
		ids.add(localId);
		pr.setId(ids);
		
		AD addr = AD.fromSimpleAddress(PostalAddressUse.HomeAddress, demographic.getAddress(), "", 
				demographic.getCity(), demographic.getProvince(), null, demographic.getPostal());
		SET<AD> addrSet = new SET<AD>();
		addrSet.add(addr);
		pr.setAddr(addrSet);
		
		TEL tel = new TEL("TEL: +1" + demographic.getPhone(), TelecommunicationsAddressUse.Home);
		
		SET<TEL> telS = new SET<TEL>();
		telS.add(tel);
		pr.setTelecom(telS);
		rt.setPatientRole(pr);
		
	
		Patient pat = new Patient();
		
		SET<PN> nameSet = new SET<PN>();
		PN pn = PN.fromEN(PN.createEN(EntityNameUse.Legal, new ENXP(demographic.getLastName(), EntityNamePartType.Family), 
				new ENXP(demographic.getFirstName(), EntityNamePartType.Given)));
		
		nameSet.add(pn);
		pat.setName(nameSet);
		
		if("M".equals(demographic.getSex())) {
			pat.setAdministrativeGenderCode(AdministrativeGender.Male, OID_DEMOGRAPHIC_GENDER);	
		} else if("F".equals(demographic.getSex())) {
			pat.setAdministrativeGenderCode(AdministrativeGender.Female, OID_DEMOGRAPHIC_GENDER);	
		} else {
			pat.setAdministrativeGenderCode(AdministrativeGender.Undifferentiated, OID_DEMOGRAPHIC_GENDER);	
		}
		
		pat.setBirthTime(demographic.getBirthDay(),8);
		
		//pat.setMaritalStatusCode("S",OID_DEMOGRAPHIC_MARITAL_STATUS);
		pr.setPatient(pat);
		
		//end of patient
		
		ArrayList<RecordTarget> rtList = new ArrayList<RecordTarget>();
		rtList.add(rt);
		getRoot().setRecordTarget(rtList);
		
		
		
		Author author = new Author();
		author.setContextControlCode(ContextControl.OverridingPropagating);
		
		author.setFunctionCode(ParticipationFunction.AttendingPhysician, "2.16.840.1.113883.5.88");
		Calendar cal3 = Calendar.getInstance();
		author.setTime(cal3,8);
		
		ArrayList<Author> authorList = new ArrayList<Author>();
		for(Provider provider:providers) {
			AssignedAuthor aa = new AssignedAuthor();
			
			SET<II> authorIdSet = new SET<II>();
			String provId = provider.getOhipNo();
			if(StringUtils.isEmpty(provId)) {
				provId = provider.getProviderNo();
			}
			//II authorId = new II("2.16.840.1.113883.3.239.36.1.1.3",provId);
			II authorId = new II(OscarProperties.getInstance().getProperty("born_author_oid","2.16.840.1.113883.3.239.36.1.4.3"),provId);
			authorIdSet.add(authorId);
			aa.setId(authorIdSet);
			
			
			Person person = new Person();
			SET<PN> nameSet2 = new SET<PN>();
			PN pn2 = PN.fromEN(PN.createEN(EntityNameUse.Legal, new ENXP(provider.getLastName(), EntityNamePartType.Family), 
					new ENXP(provider.getFirstName(), EntityNamePartType.Given)));
			
			nameSet2.add(pn2);
			person.setName(nameSet2);
			aa.setAssignedAuthorChoice(person);
			
			Organization org =new Organization();
			II orgId = new II(props.getOrganization(),props.getOrganizationName());
			SET<II> orgIds = new SET<II>();
			orgIds.add(orgId);
			org.setId(orgIds);
			SET<ON> onSet = new SET<ON>();
			onSet.add(ON.createON(EntityNameUse.Legal,new ENXP(props.getOrganizationName())));
			org.setName(onSet);
			
			aa.setRepresentedOrganization(org);
			
			author.setAssignedAuthor(aa);
			authorList.add(author);
			
		}
		
		if(authorList.isEmpty()) {
			AssignedAuthor aa = new AssignedAuthor();
			
			SET<II> authorIdSet = new SET<II>();
			String provId = props.getOrganization();
			
			II authorId = new II("2.16.840.1.113883.3.239.36.1.1.3",provId);
			authorIdSet.add(authorId);
			aa.setId(authorIdSet);
			
			
			Person person = new Person();
			SET<PN> nameSet2 = new SET<PN>();
			PN pn2 = PN.fromEN(PN.createEN(EntityNameUse.Legal, new ENXP(props.getOrganizationName(), EntityNamePartType.Family)));
			
			nameSet2.add(pn2);
			person.setName(nameSet2);
			aa.setAssignedAuthorChoice(person);
			
			Organization org =new Organization();
			II orgId = new II(props.getOrganization(),props.getOrganizationName());
			SET<II> orgIds = new SET<II>();
			orgIds.add(orgId);
			org.setId(orgIds);
			SET<ON> onSet = new SET<ON>();
			onSet.add(ON.createON(EntityNameUse.Legal,new ENXP(props.getOrganizationName())));
			org.setName(onSet);
			
			aa.setRepresentedOrganization(org);
			
			author.setAssignedAuthor(aa);
			authorList.add(author);
			
		}
		getRoot().setAuthor(authorList);
	
		
		Custodian custodian = new Custodian();
		
		AssignedCustodian assignedCustodian  = new AssignedCustodian();
		ON custName = new ON();
		ENXP enxp = new ENXP(props.getOrganizationName());
		ArrayList<ENXP> enxpList = new ArrayList<ENXP>();
		enxpList.add(enxp);
		custName.setParts(enxpList);
		
		Organization org =new Organization();
		II orgId = new II(props.getOrganization());
		SET<II> orgIds = new SET<II>();
		orgIds.add(orgId);
		org.setId(orgIds);
		CustodianOrganization custOrg = new CustodianOrganization(orgIds);
		custOrg.setName(custName);
		
		assignedCustodian.setRepresentedCustodianOrganization(custOrg);
		custodian.setAssignedCustodian(assignedCustodian);
		getRoot().setCustodian(custodian);
		
		//TODO: Parents document info also related to updates. Will determine soon.
		/*
		ArrayList<RelatedDocument> relatedDocs = new ArrayList<RelatedDocument>();
		RelatedDocument rd = new RelatedDocument();
		
		
		ParentDocument pd= new ParentDocument();
		II pdId = new II("2.16.840.1.113883.3.239.36.1.1.1","123344");
		SET<II> pdIds = new SET<II>();
		pdIds.add(pdId);
		pd.setId(pdIds);
		
		CD<String> code = new CD<String>();
		code.setCodeSystem("2.16.840.1.113883.6.1");
		code.setCode("51848-0");
		pd.setCode(code);
		
		pd.setSetId("2.16.840.1.113883.3.239.36.1.1.14", "1234567");
		
		pd.setVersionNumber(2);
		
		rd.setParentDocument(pd);
		
		x_ActRelationshipDocument ard = new x_ActRelationshipDocument("RPLC",null);
		
		rd.setTypeCode(ard);
		
		relatedDocs.add(rd);
		
		getRoot().setRelatedDocument(relatedDocs);
		
		*/
	}
	
    public void setNonXmlBody(byte[] content, String mediaType) {
        NonXMLBody dataBody = new NonXMLBody();
        Component2 componentBody = new Component2(ActRelationshipHasComponent.HasComponent, BL.TRUE, dataBody);
        dataBody.setText(content, mediaType);
        
        getRoot().setComponent(componentBody);
    }

    //LOINC code for Assessment Note
	protected Code<String> getBORNCode() {
		Code<String> code = new Code<String>();
		code.setCode("51848-0");
		code.setCodeSystem(OID_LOINC);
		code.setCodeSystemName("LOINC");
		code.setDisplayName("Assessment Note");
		
		return code;
	}
	
	protected String getBORNTitle() {
		if(type == BORNCDADocumentType.A1A2) {
			return TITLE_A1A2;
		}
		if(type == BORNCDADocumentType.EighteenMonth) {
			return TITLE_18M;
		}
		if(type == BORNCDADocumentType.CSD) {
			return TITLE_CSD;
		}
		return null;
	}
	
	protected SET<CS<BindingRealm>> getBORNRealm() {
		SET<CS<BindingRealm>> result = new SET<CS<BindingRealm>>();
		CS<BindingRealm> cs = new CS<BindingRealm>();
		BindingRealm realm = new BindingRealm("CA-ON", "");
		cs.setCodeEx(realm);
		result.add(cs);
		
		return result;
	}
	
	
	protected CE<x_BasicConfidentialityKind> getBORNConfidentialityCode() {
		x_BasicConfidentialityKind conf = new x_BasicConfidentialityKind("R", OID_CONFIDENTIALITY);
		CE<x_BasicConfidentialityKind> confCE = new CE<x_BasicConfidentialityKind>();
		confCE.setCodeEx(conf);
		
		return confCE;
	}
	
}
