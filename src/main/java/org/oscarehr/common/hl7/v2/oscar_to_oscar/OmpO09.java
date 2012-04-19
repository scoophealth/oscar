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


package org.oscarehr.common.hl7.v2.oscar_to_oscar;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.oscarehr.common.model.Clinic;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Drug;
import org.oscarehr.common.model.Prescription;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v26.datatype.CQ;
import ca.uhn.hl7v2.model.v26.datatype.CWE;
import ca.uhn.hl7v2.model.v26.datatype.NM;
import ca.uhn.hl7v2.model.v26.datatype.RPT;
import ca.uhn.hl7v2.model.v26.datatype.XAD;
import ca.uhn.hl7v2.model.v26.datatype.XON;
import ca.uhn.hl7v2.model.v26.datatype.XTN;
import ca.uhn.hl7v2.model.v26.group.OMP_O09_ORDER;
import ca.uhn.hl7v2.model.v26.group.OMP_O09_PATIENT;
import ca.uhn.hl7v2.model.v26.group.OMP_O09_TIMING;
import ca.uhn.hl7v2.model.v26.message.OMP_O09;
import ca.uhn.hl7v2.model.v26.segment.ORC;
import ca.uhn.hl7v2.model.v26.segment.RXO;
import ca.uhn.hl7v2.model.v26.segment.TQ1;

public final class OmpO09 {
	private static final Logger logger = MiscUtils.getLogger();

	private OmpO09() {
		// not meant to be instantiated
	}

	public static OMP_O09 makeOmpO09(Clinic clinic, Provider provider, Demographic demographic, Prescription prescription, List<Drug> drugs) throws HL7Exception, UnsupportedEncodingException {
		OMP_O09 prescriptionMsg = new OMP_O09();

		DataTypeUtils.fillMsh(prescriptionMsg.getMSH(), new Date(), clinic.getClinicName(), "OMP", "O09", "OMP_O09", DataTypeUtils.HL7_VERSION_ID);
		DataTypeUtils.fillSft(prescriptionMsg.getSFT(), OscarProperties.getBuildTag(), OscarProperties.getBuildDate());

		OMP_O09_PATIENT patient = prescriptionMsg.getPATIENT();
		DataTypeUtils.fillPid(patient.getPID(), 1, demographic);

		String rxComments = prescription.getComments();
		if (rxComments != null) DataTypeUtils.fillNte(prescriptionMsg.getNTE(0), "Rx Comments", null, rxComments.getBytes());

		int drugCounter = 0;
		for (Drug drug : drugs) {
			OMP_O09_ORDER order = prescriptionMsg.getORDER(drugCounter);
			fillOrc(order, prescription, provider, clinic);
			fillTq1(order, drug);
			fillRxo(order.getRXO(), drug);

			String special = drug.getSpecial();
			if (special != null) DataTypeUtils.fillNte(order.getNTE(0), "Prescription Text", null, special.getBytes());

			drugCounter++;
		}

		return (prescriptionMsg);
	}

	private static void fillRxo(RXO rxo, Drug drug) throws HL7Exception {

		CWE drugType = rxo.getRequestedGiveCode();

		StringBuilder drugTypeSb = new StringBuilder();
		if (drug.getGenericName() != null) drugTypeSb.append(drug.getGenericName());
		if (drug.getBrandName() != null) {
			drugTypeSb.append('(');
			drugTypeSb.append(drug.getBrandName());
			drugTypeSb.append(')');
		}
		drugType.getText().setValue(drugTypeSb.toString());

		NM dosageMin = rxo.getRequestedGiveAmountMinimum();
		dosageMin.setValue(drug.getDosage());

		CWE dosageUnits = rxo.getRequestedGiveUnits();
		dosageUnits.getText().setValue(drug.getUnit());

		CWE dosageForm = rxo.getRequestedDosageForm();
		dosageForm.getText().setValue(drug.getDrugForm());

		CWE specialInstructions = rxo.getProviderSPharmacyTreatmentInstructions(0);
		specialInstructions.getText().setValue(drug.getSpecialInstruction());

		CWE administraionRouteMethod = rxo.getProviderSAdministrationInstructions(0);
		StringBuilder routeMethodSb = new StringBuilder();
		if (drug.getRoute() != null) routeMethodSb.append(drug.getRoute());
		if (drug.getMethod() != null) {
			if (routeMethodSb.length() > 0) routeMethodSb.append(", ");
			routeMethodSb.append(drug.getMethod());
		}
		administraionRouteMethod.getText().setValue(routeMethodSb.toString());

		rxo.getAllowSubstitutions().setValue(String.valueOf(!drug.isNoSubs()));
	}

	private static void fillTq1(OMP_O09_ORDER order, Drug drug) throws HL7Exception {
		OMP_O09_TIMING timing = order.getTIMING(0);
		TQ1 tq1 = timing.getTQ1();

		CQ cq = tq1.getQuantity();
		NM quantity = cq.getQuantity();
		quantity.setValue(drug.getQuantity());
		CWE units = cq.getUnits();
		units.getText().setValue(drug.getUnit());
		units.getNameOfCodingSystem().setValue(drug.getUnitName());

		RPT rpt = tq1.getRepeatPattern(0);
		rpt.getGeneralTimingSpecification().setValue(drug.getFreqCode());

		CQ serviceDuration = tq1.getServiceDuration();
		serviceDuration.getQuantity().setValue(drug.getDuration());
		serviceDuration.getUnits().getNameOfCodingSystem().setValue(drug.getDurUnit());

		tq1.getStartDateTime().setValue(DataTypeUtils.getAsHl7FormattedString(drug.getRxDate()));
		tq1.getEndDateTime().setValue(DataTypeUtils.getAsHl7FormattedString(drug.getEndDate()));
	}

	private static void fillOrc(OMP_O09_ORDER order, Prescription prescription, Provider provider, Clinic clinic) throws HL7Exception {
		ORC orc = order.getORC();

		// NW = new order request
		orc.getOrderControl().setValue("NW");

		// the EI of the placer order number should be the prescription id
		if (prescription.getId() != null) {
			orc.getPlacerOrderNumber().getEntityIdentifier().setValue(prescription.getId().toString());
		}

		// set provider
		DataTypeUtils.fillXcn(orc.getOrderingProvider(0), provider);

		// set prescription date
		orc.getOrderEffectiveDateTime().setValue(DataTypeUtils.getAsHl7FormattedString(prescription.getDatePrescribed()));

		// set facility name / doctors office name
		XON xon = orc.getOrderingFacilityName(0);
		xon.getOrganizationName().setValue(StringUtils.trimToNull(clinic.getClinicName()));

		XAD xad = orc.getOrderingFacilityAddress(0);
		DataTypeUtils.fillXAD(xad, clinic, null, "O");

		XTN xtn = orc.getOrderingFacilityPhoneNumber(0);
		xtn.getUnformattedTelephoneNumber().setValue(StringUtils.trimToNull(clinic.getClinicPhone()));
	}

	public static void main(String... argv) throws Exception {
		// this is here just to test some of the above functions since
		// we are not using junit tests...

		Clinic clinic = new Clinic();
		clinic.setClinicName("test clinic");
		clinic.setClinicAddress("123 my street");
		clinic.setClinicCity("my city");
		clinic.setClinicProvince("bc");
		clinic.setClinicPhone("12345");

		Provider provider = new Provider();
		provider.setProviderNo("9999");
		provider.setLastName("p_lname");
		provider.setFirstName("p_fname");
		provider.setTitle("DR");

		Demographic demographic = new Demographic();
		demographic.setDemographicNo(8383);
		demographic.setLastName("d_lname");
		demographic.setFirstName("d_fname");
		demographic.setSex("M");
		demographic.setBirthDay(new GregorianCalendar(1902, 04, 05));

		Prescription prescription = new Prescription();
		prescription.setDatePrescribed(new Date());
		prescription.setComments("this is a fake prescription");

		ArrayList<Drug> drugs = new ArrayList<Drug>();

		OMP_O09 prescriptionMsg = makeOmpO09(clinic, provider, demographic, prescription, drugs);

		String messageString = OscarToOscarUtils.pipeParser.encode(prescriptionMsg);
		logger.info(messageString);
	}

}
