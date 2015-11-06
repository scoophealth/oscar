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


package oscar.form.study.HSFO.pageUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.soap.SOAPPart;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;

import noNamespace.*;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.BEthnicBangladeshi;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.BEthnicBlack;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.BEthnicChinese;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.BEthnicEIndian;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.BEthnicFirstNation;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.BEthnicHispanic;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.BEthnicJapanese;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.BEthnicKorean;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.BEthnicOther;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.BEthnicPakistani;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.BEthnicRefused;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.BEthnicSriLankan;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.BEthnicUnknown;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.BEthnicWhite;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.DatBirthDate;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.DatConsentDate;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.DatDropDate;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.DblHeight;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SelHeightUnit;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SelHtnDxAgoPreBsl;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SelSex;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BABPM;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BBPAP;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BCommunRes;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BFamHxCHD;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BFamHxDM;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BFamHxDyslipidemia;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BFamHxHtn;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BFamHxKidney;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BFamHxObesity;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BFamHxStrokeTIA;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BGoalAlcohol;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BGoalDASHDiet;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BGoalPhysActivity;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BGoalSalt;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BGoalSmoking;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BGoalStress;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BHomeMon;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BHxCHD;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BHxDM;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BHxDyslipidemia;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BHxKidney;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BHxObesity;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BHxStrokeTIA;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BPPAgreement;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BReferHCP;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BRiskAlcohol;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BRiskDiet;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BRiskPhysActivity;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BRiskSmoking;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BRiskStress;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BRiskWeight;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BRxCurrentAce;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BRxCurrentArb;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BRxCurrentBb;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BRxCurrentCcb;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BRxCurrentDiu;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BRxCurrentIns;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BRxCurrentOha;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BRxCurrentOthhtn;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BRxCurrentOthlip;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BRxCurrentSta;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BRxSideEffectsAce;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BRxSideEffectsArb;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BRxSideEffectsBb;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BRxSideEffectsCcb;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BRxSideEffectsDiu;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BRxSideEffectsIns;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BRxSideEffectsOha;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BRxSideEffectsOthhtn;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BRxSideEffectsOthlip;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BRxSideEffectsSta;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.BTPOff;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.DblA1CPercent;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.DblHDLMM;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.DblLDLMM;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.DblTCtoHDL;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.DblWaistCircumf;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.DblWeight;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.IntAlcoholDrinksPerWk;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.IntDBPMmHg;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.IntDbpGoalMmHg;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.IntExerciseMinPerWk;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.IntGoalConfidence;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.IntGoalImportance;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.IntMissedMedsPerWk;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.IntSBPMmHg;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.IntSbpGoalMmHg;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.IntSmokingCigsPerDay;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.SelAdequateDrugCoverage;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.SelBpTru;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.SelDASHdiet;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.SelFollowUp;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.SelHerbalMeds;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.SelHighSalt;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.SelHtnDxType;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.SelPatientView;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.SelRxTodayAce;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.SelRxTodayArb;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.SelRxTodayBb;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.SelRxTodayCcb;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.SelRxTodayDiu;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.SelRxTodayIns;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.SelRxTodayOha;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.SelRxTodayOthhtn;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.SelRxTodayOthlip;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.SelRxTodaySta;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.SelStressed;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.SelWaistCircumfUnit;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.SitePatientVisit.SelWeightUnit;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.TxtEmrHcpID;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.TxtGivenNames;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.TxtPharmacyLocation;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.TxtPharmacyName;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.TxtPostalCodeFSA;
import noNamespace.HsfoHbpsDataDocument.HsfoHbpsData.Site.SitePatient.TxtSurname;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlCalendar;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.tool.PrettyPrinter;
import org.oscarehr.util.LoggedInInfo;
import org.w3c.dom.Node;

import oscar.OscarProperties;
import oscar.form.study.HSFO.HSFODAO;
import oscar.form.study.HSFO.PatientData;
import oscar.form.study.HSFO.VisitData;
import oscar.oscarDemographic.data.DemographicData;
import oscar.oscarProvider.data.ProviderData;


public class XMLTransferUtil
{
	private static Logger logger = Logger
	.getLogger(XMLTransferUtil.class);

	SimpleDateFormat dformat1 = new SimpleDateFormat(
	"yyyy-MM-dd'T'HH:mm:ss");

	SimpleDateFormat dformat2 = new SimpleDateFormat("yyyy-MM-dd");

	String defaultweb = "https://www.clinforma.net/HsfoHbps/DataVaultWS/definition.asmx";

	String actionString = "https://www.clinforma.net/HsfoHbps/DataVaultWS/UploadToCDV";

	String soaplink = "https://www.clinforma.net/HsfoHbps/DataVaultWS";

	HSFODAO hdao = new HSFODAO();

	public Integer getSiteID()
	{
		OscarProperties props = OscarProperties.getInstance();
		String id = props.getProperty("hsfo.loginSiteCode", "");
		return new Integer(id);
	}

	public String getUserId()
	{
		OscarProperties props = OscarProperties.getInstance();
		return props.getProperty("hsfo.userID", "");

	}

	public String getLoginPasswd()
	{
		OscarProperties props = OscarProperties.getInstance();
		return props.getProperty("hsfo.loginPassword", "");
	}

	public String getVersionDate()
	{
		OscarProperties props = OscarProperties.getInstance();
		return props.getProperty("hsfo.xmlVersionDate", "2007-02-12");
	}

	public String getWebUrl()
	{
		OscarProperties props = OscarProperties.getInstance();
		return props.getProperty("hsfo.webServiceURL", defaultweb);
	}

	public String getProviderName(String providerNo)
	{
		ProviderData pd = new ProviderData(providerNo);
		if (pd == null)
			return "";
		else
		{

			String firstName = pd.getFirst_name() == null ? "" : pd
					.getFirst_name();
			String lastName = pd.getLast_name() == null ? "" : pd
					.getLast_name();
			return (firstName + " " + lastName).trim();
		}
	}

	public PatientData getDemographic(String demoNo)
	{

		return hdao.retrievePatientRecord(demoNo);

	}

	public VisitData getSignedVisit(String patientId)
	{
		List pList = hdao.nullSafeRetrVisitRecord(patientId);
		if (pList == null || pList.size() == 0)
			return null;
		VisitData vs = (VisitData) pList.get(0);
		Date vd = vs.getFormEdited();
		// get the latest signed record
		for (int i = 1; i < pList.size() - 1; i++)
		{
			VisitData tv = (VisitData) pList.get(i);
			Date tvd = tv.getFormEdited();
			if (tvd != null && vd != null && tvd.after(vd))
			{
				vs = tv;
				vd = tvd;
			}
		}
		return vs;
	}

	public String getSignedProvider(String patientId)
	{
		VisitData vs = getSignedVisit(patientId);
		return getProviderName(vs.getProvider_Id());
	}

	public Date getSignedDate(String patientId)
	{
		VisitData vs = getSignedVisit(patientId);
		return vs.getFormEdited();
	}

	public void addPatientToSite(LoggedInInfo loggedInInfo, Site site, PatientData pd)
	{
		String dateString2 = dformat2.format(pd.getConsentDate());
		String dateString1 = dformat1.format(getSignedDate(pd.getPatient_Id()));
		XmlCalendar when = new XmlCalendar(dateString1);
		XmlCalendar dob = new XmlCalendar(dformat2.format(pd.getBirthDate()));

		// add patient
		SitePatient patient = site.addNewSitePatient();
		patient.setEmrPatientKey(pd.getPatient_Id());

		DatConsentDate dcd = patient.addNewDatConsentDate();

		dcd.setValue(new XmlCalendar(dateString2));

		dcd.setSignedWhen(when);
		String who = getSignedProvider(pd.getPatient_Id());
		dcd.setSignedWho(who);

		DatDropDate ddd = patient.addNewDatDropDate();
		ddd.setSignedWhen(when);
		ddd.setSignedWho(who);

		TxtEmrHcpID tehid = patient.addNewTxtEmrHcpID();
		DemographicData demoData = new DemographicData();
		String providerId=demoData.getDemographic(loggedInInfo, patient.getEmrPatientKey()).getProviderNo();

//		if (pd.getEmrHCPId() == null)
//			tehid.setValue("");
//		else
//			tehid.setValue(pd.getEmrHCPId());
		/*get EmrHcpId from demographic table, not hsfo_patient table*/
		if (providerId == null)
			tehid.setValue("");
		else
			tehid.setValue(providerId);
		tehid.setSignedWhen(when);
		tehid.setSignedWho(who);

		TxtSurname tsn = patient.addNewTxtSurname();
		tsn.setValue(pd.getLName());
		tsn.setSignedWhen(when);
		tsn.setSignedWho(who);

		TxtGivenNames tgn = patient.addNewTxtGivenNames();
		tgn.setValue(pd.getFName());
		tgn.setSignedWhen(when);
		tgn.setSignedWho(who);

		DatBirthDate dbd = patient.addNewDatBirthDate();
		dbd.setValue(dob);
		dbd.setSignedWhen(when);
		dbd.setSignedWho(who);

		SelSex ss = patient.addNewSelSex();
		String sex = pd.getSex();
		if ("m".equalsIgnoreCase(sex))
			ss.setValue(StringSexNonEmpty.MALE);
		else
			ss.setValue(StringSexNonEmpty.FEMALE);
		ss.setSignedWhen(when);
		ss.setSignedWho(who);

		TxtPostalCodeFSA tpcfsa = patient.addNewTxtPostalCodeFSA();
		tpcfsa.setValue(pd.getPostalCode());
		tpcfsa.setSignedWhen(when);
		tpcfsa.setSignedWho(who);

		TxtPharmacyName tpn = patient.addNewTxtPharmacyName();
		String phName = pd.getPharmacyName();
		if (phName == null)
			phName = "";
		tpn.setValue(phName);
		tpn.setSignedWhen(when);
		tpn.setSignedWho(who);
		// optional add valuedate

		TxtPharmacyLocation tpl = patient.addNewTxtPharmacyLocation();
		String phLocation = pd.getPharmacyLocation();
		if (phLocation == null)
			phLocation = "";
		tpl.setValue(phLocation);
		tpl.setSignedWhen(when);
		tpl.setSignedWho(who);

		DblHeight dh = patient.addNewDblHeight();
		dh.setValue(pd.getHeight());
		dh.setSignedWhen(when);
		dh.setSignedWho(who);

		SelHeightUnit shu = patient.addNewSelHeightUnit();
		String heightU = pd.getHeight_unit();
		if ("cm".equalsIgnoreCase(heightU))
			shu.setValue(StringLengthUnit.CM);
		else
			shu.setValue(StringLengthUnit.INCHES);
		shu.setSignedWhen(when);
		shu.setSignedWho(who);

		BEthnicWhite bew = patient.addNewBEthnicWhite();
		bew.setValue(pd.isEthnic_White());
		bew.setSignedWhen(when);
		bew.setSignedWho(who);

		BEthnicBlack beb = patient.addNewBEthnicBlack();
		beb.setValue(pd.isEthnic_Black());
		beb.setSignedWhen(when);
		beb.setSignedWho(who);

		BEthnicEIndian beei = patient.addNewBEthnicEIndian();
		beei.setValue(pd.isEthnic_EIndian());
		beei.setSignedWhen(when);
		beei.setSignedWho(who);

		BEthnicPakistani bep = patient.addNewBEthnicPakistani();
		bep.setValue(pd.isEthnic_Pakistani());
		bep.setSignedWhen(when);
		bep.setSignedWho(who);

		BEthnicSriLankan besl = patient.addNewBEthnicSriLankan();
		besl.setValue(pd.isEthnic_SriLankan());
		besl.setSignedWhen(when);
		besl.setSignedWho(who);

		BEthnicBangladeshi bebl = patient.addNewBEthnicBangladeshi();
		bebl.setValue(pd.isEthnic_Bangladeshi());
		bebl.setSignedWhen(when);
		bebl.setSignedWho(who);

		BEthnicChinese bec = patient.addNewBEthnicChinese();
		bec.setValue(pd.isEthnic_Chinese());
		bec.setSignedWhen(when);
		bec.setSignedWho(who);

		BEthnicJapanese bej = patient.addNewBEthnicJapanese();
		bej.setValue(pd.isEthnic_Japanese());
		bej.setSignedWhen(when);
		bej.setSignedWho(who);

		BEthnicKorean bek = patient.addNewBEthnicKorean();
		bek.setValue(pd.isEthnic_Korean());
		bek.setSignedWhen(when);
		bek.setSignedWho(who);

		BEthnicHispanic behp = patient.addNewBEthnicHispanic();
		behp.setValue(pd.isEthnic_Hispanic());
		behp.setSignedWhen(when);
		behp.setSignedWho(who);

		BEthnicFirstNation befn = patient.addNewBEthnicFirstNation();
		befn.setValue(pd.isEthnic_FirstNation());
		befn.setSignedWhen(when);
		befn.setSignedWho(who);

		BEthnicOther beo = patient.addNewBEthnicOther();
		beo.setValue(pd.isEthnic_Other());
		beo.setSignedWhen(when);
		beo.setSignedWho(who);

		BEthnicRefused ber = patient.addNewBEthnicRefused();
		ber.setValue(pd.isEthnic_Refused());
		ber.setSignedWhen(when);
		ber.setSignedWho(who);

		BEthnicUnknown beu = patient.addNewBEthnicUnknown();
		beu.setValue(pd.isEthnic_Unknown());
		beu.setSignedWhen(when);
		beu.setSignedWho(who);

		SelHtnDxAgoPreBsl shdapb = patient.addNewSelHtnDxAgoPreBsl();
		String seltimeago = pd.getSel_TimeAgoDx();
		if ("NA".equalsIgnoreCase(seltimeago))
			shdapb.setValue(StringTimeAgoHtnDx.NA);
		else if ("AtLeast1YrAgo".equalsIgnoreCase(seltimeago))
			shdapb.setValue(StringTimeAgoHtnDx.AT_LEAST_1_YR_AGO);
		else if ("Under1yrAgo".equalsIgnoreCase(seltimeago))
			shdapb.setValue(StringTimeAgoHtnDx.UNDER_1_YR_AGO);
		else
			shdapb.setValue(StringTimeAgoHtnDx.X);
		shdapb.setSignedWhen(when);
		shdapb.setSignedWho(who);

		addAllPatientVisit(patient, pd.getPatient_Id());

	}

	public void addAllPatientVisit(SitePatient patient, String patientId)

	{
		List pList = hdao.nullSafeRetrVisitRecord(patientId);
		if (pList == null || pList.size() == 0)
			return;
		for (int i = 0; i < pList.size(); i++)
		{
			VisitData vsd = (VisitData) pList.get(i);
			addVisitData(patient, vsd);
		}
	}

	public void addVisitData(SitePatient patient, VisitData vsd)
	{

		String date = dformat2.format(vsd.getVisitDate_Id());

		String signT = dformat1.format(vsd.getFormEdited());
		String signT2 = dformat2.format(vsd.getFormEdited());
		XmlCalendar when = new XmlCalendar(signT);
		XmlCalendar when2 = new XmlCalendar(signT2);
		String who = getProviderName(vsd.getProvider_Id());

		SitePatientVisit spvs = patient.addNewSitePatientVisit();
		spvs.setVisitDateKey(new XmlCalendar(date));

		String co = vsd.getDrugcoverage();
		if (co != null)
		{
			SelAdequateDrugCoverage sadc = spvs.addNewSelAdequateDrugCoverage();

			if ("yes".equalsIgnoreCase(co))
				sadc.setValue(StringYesNo.YES);
			else if ("no".equalsIgnoreCase(co))
				sadc.setValue(StringYesNo.NO);
			else if ("null".equalsIgnoreCase(co))
				sadc.setValue(StringYesNo.X);
			sadc.setSignedWhen(when);
			sadc.setSignedWho(who);
		}

		String hdt = vsd.getHtnDxType();
		if (hdt != null)
		{
			SelHtnDxType shdt = spvs.addNewSelHtnDxType();

			if ("PrimaryHtn".equalsIgnoreCase(hdt))
				shdt.setValue(StringHtnDxType.PRIMARY_HTN);
			else if ("ElevatedBpReadings".equalsIgnoreCase(hdt))
				shdt.setValue(StringHtnDxType.ELEVATED_BP_READINGS);
			else if ("null".equalsIgnoreCase(hdt))
				shdt.setValue(StringHtnDxType.X);
			shdt.setSignedWhen(when);
			shdt.setSignedWho(who);
		}
		BHxDyslipidemia bhd = spvs.addNewBHxDyslipidemia();
		bhd.setValue(vsd.isDyslipid());
		bhd.setSignedWhen(when);
		bhd.setSignedWho(who);

		BHxDM bhdm = spvs.addNewBHxDM();
		bhdm.setValue(vsd.isDiabetes());
		bhdm.setSignedWhen(when);
		bhdm.setSignedWho(who);

		BHxKidney bhk = spvs.addNewBHxKidney();
		bhk.setValue(vsd.isKidneyDis());
		bhk.setSignedWhen(when);
		bhk.setSignedWho(who);

		BHxObesity bho = spvs.addNewBHxObesity();
		bho.setValue(vsd.isObesity());
		bho.setSignedWhen(when);
		bho.setSignedWho(who);

		BHxCHD bhchd = spvs.addNewBHxCHD();
		bhchd.setValue(vsd.isCHD());
		bhchd.setSignedWhen(when);
		bhchd.setSignedWho(who);

		BHxStrokeTIA bhst = spvs.addNewBHxStrokeTIA();
		bhst.setValue(vsd.isStroke_TIA());
		bhst.setSignedWhen(when);
		bhst.setSignedWho(who);

		BFamHxHtn bfhh = spvs.addNewBFamHxHtn();
		bfhh.setValue(vsd.isFamHx_Htn());
		bfhh.setSignedWhen(when);
		bfhh.setSignedWho(who);

		BFamHxDyslipidemia bfhdp = spvs.addNewBFamHxDyslipidemia();
		bfhdp.setValue(vsd.isFamHx_Dyslipid());
		bfhdp.setSignedWhen(when);
		bfhdp.setSignedWho(who);

		BFamHxDM bfhdm = spvs.addNewBFamHxDM();
		bfhdm.setValue(vsd.isFamHx_Diabetes());
		bfhdm.setSignedWhen(when);
		bfhdm.setSignedWho(who);

		BFamHxKidney bfhk = spvs.addNewBFamHxKidney();
		bfhk.setValue(vsd.isFamHx_KidneyDis());
		bfhk.setSignedWhen(when);
		bfhk.setSignedWho(who);

		BFamHxObesity bfho = spvs.addNewBFamHxObesity();
		bfho.setValue(vsd.isFamHx_Obesity());
		bfho.setSignedWhen(when);
		bfho.setSignedWho(who);

		BFamHxCHD bfhchd = spvs.addNewBFamHxCHD();
		bfhchd.setValue(vsd.isFamHx_CHD());
		bfhchd.setSignedWhen(when);
		bfhchd.setSignedWho(who);

		BFamHxStrokeTIA bfhstia = spvs.addNewBFamHxStrokeTIA();
		bfhstia.setValue(vsd.isFamHx_Stroke_TIA());
		bfhstia.setSignedWhen(when);
		bfhstia.setSignedWho(who);

		int tempi = vsd.getSBP();
		if (tempi != Integer.MIN_VALUE)
		{
			IntSBPMmHg isbgmh = spvs.addNewIntSBPMmHg();
			if (tempi != 0)
				isbgmh.setValue(tempi);
			isbgmh.setSignedWhen(when);
			isbgmh.setSignedWho(who);
		}

		tempi = vsd.getDBP();
		if (tempi != Integer.MIN_VALUE)
		{
			IntDBPMmHg idbgmh = spvs.addNewIntDBPMmHg();
			idbgmh.setValue(vsd.getDBP());
			idbgmh.setSignedWhen(when);
			idbgmh.setSignedWho(who);
		}

		String used = vsd.getBptru_used();
		if (used != null)
		{
			SelBpTru sbt = spvs.addNewSelBpTru();

			if ("yes".equalsIgnoreCase(used))
				sbt.setValue(StringYesNo.YES);
			else if ("no".equalsIgnoreCase(used))
				sbt.setValue(StringYesNo.NO);
			else if ("null".equalsIgnoreCase(used))
				sbt.setValue(StringYesNo.X);
			sbt.setSignedWhen(when);
			sbt.setSignedWho(who);
		}

		tempi = vsd.getSBP_goal();
		if (tempi != Integer.MIN_VALUE)
		{
			IntSbpGoalMmHg isgmh = spvs.addNewIntSbpGoalMmHg();

			if (tempi != 0)
				isgmh.setValue(tempi);
			isgmh.setSignedWhen(when);
			isgmh.setSignedWho(who);
		}

		tempi = vsd.getDBP_goal();
		if (tempi != Integer.MIN_VALUE)
		{
			IntDbpGoalMmHg idgmh = spvs.addNewIntDbpGoalMmHg();
			idgmh.setValue(vsd.getDBP_goal());
			idgmh.setSignedWhen(when);
			idgmh.setSignedWho(who);
		}

		double tempd = vsd.getWeight();
		if (tempd != Double.MIN_VALUE)
		{
			DblWeight dw = spvs.addNewDblWeight();
			if (vsd.getWeight() != 0)
				dw.setValue(vsd.getWeight());
			dw.setSignedWhen(when);
			dw.setSignedWho(who);

			String wunit = vsd.getWeight_unit();
			if (wunit != null)
			{
				SelWeightUnit swu = spvs.addNewSelWeightUnit();

				if ("kg".equalsIgnoreCase(wunit))
					swu.setValue(StringMassUnit.KG);
				else if ("lb".equalsIgnoreCase(wunit))
					swu.setValue(StringMassUnit.LBS);
				else if ("null".equalsIgnoreCase(wunit))
					swu.setValue(StringMassUnit.X);
				swu.setSignedWhen(when);
				swu.setSignedWho(who);
			}
		}

		tempd = vsd.getWaist();
		if (tempd != Double.MIN_VALUE)
		{
			DblWaistCircumf dwc = spvs.addNewDblWaistCircumf();
			if (vsd.getWaist() != 0)
				dwc.setValue(vsd.getWaist());
			dwc.setSignedWhen(when);
			dwc.setSignedWho(who);

			String waistu = vsd.getWaist_unit();
			if (waistu != null)
			{
				SelWaistCircumfUnit swcu = spvs.addNewSelWaistCircumfUnit();

				if ("cm".equalsIgnoreCase(waistu))
					swcu.setValue(StringLengthUnit.CM);
				else if ("inch".equalsIgnoreCase(waistu))
					swcu.setValue(StringLengthUnit.INCHES);
				else if ("null".equalsIgnoreCase(waistu))
					swcu.setValue(StringLengthUnit.X);
				swcu.setSignedWhen(when);
				swcu.setSignedWho(who);
			}
		}

		tempd = vsd.getTC_HDL();
		if (tempd != Double.MIN_VALUE)
		{
			DblTCtoHDL dtchdl = spvs.addNewDblTCtoHDL();
			if (vsd.getTC_HDL() != 0)
				dtchdl.setValue(vsd.getTC_HDL());
			dtchdl.setSignedWhen(when);
			dtchdl.setSignedWho(who);
			Date hdldate = vsd.getTC_HDL_LabresultsDate();
			if (hdldate == null)
				dtchdl.setValueDate(when2);
			else
				dtchdl.setValueDate(new XmlCalendar(dformat2.format(hdldate)));
		}

		tempd = vsd.getLDL();
		if (tempd != Double.MIN_VALUE)
		{
			DblLDLMM dldlmm = spvs.addNewDblLDLMM();
			if (vsd.getLDL() != 0)
				dldlmm.setValue(vsd.getLDL());
			dldlmm.setSignedWhen(when);
			dldlmm.setSignedWho(who);
			Date ldldate = vsd.getLDL_LabresultsDate();
			if (ldldate == null)
				dldlmm.setValueDate(when2);
			else
				dldlmm.setValueDate(new XmlCalendar(dformat2.format(ldldate)));
		}

		tempd = vsd.getHDL();
		if (tempd != Double.MIN_VALUE)
		{
			DblHDLMM dhdlmm = spvs.addNewDblHDLMM();
			if (vsd.getHDL() != 0)
				dhdlmm.setValue(vsd.getHDL());
			dhdlmm.setSignedWhen(when);
			dhdlmm.setSignedWho(who);
			Date ldldate = vsd.getHDL_LabresultsDate();
			if (ldldate == null)
				dhdlmm.setValueDate(when2);
			else
				dhdlmm.setValueDate(new XmlCalendar(dformat2.format(ldldate)));
		}

		tempd = vsd.getA1C();
		if (tempd != Double.MIN_VALUE)
		{
			DblA1CPercent dacp = spvs.addNewDblA1CPercent();
			if (vsd.getA1C() != 0)
				dacp.setValue(vsd.getA1C());
			dacp.setSignedWhen(when);
			dacp.setSignedWho(who);
			Date ldldate = vsd.getA1C_LabresultsDate();
			if (ldldate == null)
				dacp.setValueDate(when2);
			else
				dacp.setValueDate(new XmlCalendar(dformat2.format(ldldate)));
		}

		BRiskWeight brwt = spvs.addNewBRiskWeight();
		brwt.setValue(vsd.isRisk_weight());
		brwt.setSignedWhen(when);
		brwt.setSignedWho(who);

		BRiskPhysActivity brpa = spvs.addNewBRiskPhysActivity();
		brpa.setValue(vsd.isRisk_activity());
		brpa.setSignedWhen(when);
		brpa.setSignedWho(who);

		BRiskDiet brdt = spvs.addNewBRiskDiet();
		brdt.setValue(vsd.isRisk_diet());
		brdt.setSignedWhen(when);
		brdt.setSignedWho(who);

		BRiskSmoking brsk = spvs.addNewBRiskSmoking();
		brsk.setValue(vsd.isRisk_smoking());
		brsk.setSignedWhen(when);
		brsk.setSignedWho(who);

		BRiskAlcohol brah = spvs.addNewBRiskAlcohol();
		brah.setValue(vsd.isRisk_alcohol());
		brah.setSignedWhen(when);
		brah.setSignedWho(who);

		BRiskStress brst = spvs.addNewBRiskStress();
		brst.setValue(vsd.isRisk_stress());
		brst.setSignedWhen(when);
		brst.setSignedWho(who);

		String life = vsd.getLifeGoal();
		boolean pa = false, dietdash = false, dietsalt = false, smoking = false, alcohol = false, stress = false;
		if ("Goal_activity".equalsIgnoreCase(life))
			pa = true;
		else if ("Goal_dietDash".equalsIgnoreCase(life))
			dietdash = true;
		else if ("Goal_dietSalt".equalsIgnoreCase(life))
			dietsalt = true;
		else if ("Goal_smoking".equalsIgnoreCase(life))
			smoking = true;
		else if ("Goal_alcohol".equalsIgnoreCase(life))
			alcohol = true;
		else if ("Goal_stress".equals(life))
			stress = true;

		BGoalPhysActivity bgpa = spvs.addNewBGoalPhysActivity();
		bgpa.setValue(pa);
		bgpa.setSignedWhen(when);
		bgpa.setSignedWho(who);

		BGoalDASHDiet bgdash = spvs.addNewBGoalDASHDiet();
		bgdash.setValue(dietdash);
		bgdash.setSignedWhen(when);
		bgdash.setSignedWho(who);

		BGoalSalt bgs = spvs.addNewBGoalSalt();
		bgs.setValue(dietsalt);
		bgs.setSignedWhen(when);
		bgs.setSignedWho(who);

		BGoalSmoking bgsm = spvs.addNewBGoalSmoking();
		bgsm.setValue(smoking);
		bgsm.setSignedWhen(when);
		bgsm.setSignedWho(who);

		BGoalAlcohol bga = spvs.addNewBGoalAlcohol();
		bga.setValue(alcohol);
		bga.setSignedWhen(when);
		bga.setSignedWho(who);

		BGoalStress bgst = spvs.addNewBGoalStress();
		bgst.setValue(stress);
		bgst.setSignedWhen(when);
		bgst.setSignedWho(who);

		String pView = vsd.getPtView();
		if (pView != null)
		{
			SelPatientView spv = spvs.addNewSelPatientView();

			if ("Uninterested".equalsIgnoreCase(pView))
				spv.setValue(StringPtChangeState.UNINTERESTED);
			else if ("Thinking".equalsIgnoreCase(pView))
				spv.setValue(StringPtChangeState.THINKING);
			else if ("Deciding".equalsIgnoreCase(pView))
				spv.setValue(StringPtChangeState.DECIDING);
			else if ("TakingAction".equalsIgnoreCase(pView))
				spv.setValue(StringPtChangeState.TAKING_ACTION);
			else if ("Maintaining".equalsIgnoreCase(pView))
				spv.setValue(StringPtChangeState.MAINTAINING);
			else if ("Relapsing".equalsIgnoreCase(pView))
				spv.setValue(StringPtChangeState.RELAPSING);
			else if ("null".equalsIgnoreCase(pView))
				spv.setValue(StringPtChangeState.X);
			spv.setSignedWhen(when);
			spv.setSignedWho(who);
		}

		tempi = vsd.getChange_importance();
		if (tempi != Integer.MIN_VALUE)
		{
			IntGoalImportance igi = spvs.addNewIntGoalImportance();
			if (vsd.getChange_importance() != 0)
				igi.setValue(vsd.getChange_importance());
			igi.setSignedWhen(when);
			igi.setSignedWho(who);
		}

		tempi = vsd.getChange_confidence();
		if (tempi != Integer.MIN_VALUE)
		{
			IntGoalConfidence igc = spvs.addNewIntGoalConfidence();
			if (vsd.getChange_confidence() != 0)
				igc.setValue(vsd.getChange_confidence());
			igc.setSignedWhen(when);
			igc.setSignedWho(who);
		}

		tempi = vsd.getExercise_minPerWk();
		if (tempi != Integer.MIN_VALUE)
		{
			IntExerciseMinPerWk ieampw = spvs.addNewIntExerciseMinPerWk();
			ieampw.setValue(vsd.getExercise_minPerWk());
			ieampw.setSignedWhen(when);
			ieampw.setSignedWho(who);
		}

		tempi = vsd.getSmoking_cigsPerDay();
		if (tempi != Integer.MIN_VALUE)
		{
			IntSmokingCigsPerDay iscpd = spvs.addNewIntSmokingCigsPerDay();
			iscpd.setValue(vsd.getSmoking_cigsPerDay());
			iscpd.setSignedWhen(when);
			iscpd.setSignedWho(who);
		}

		tempi = vsd.getAlcohol_drinksPerWk();
		if (tempi != Integer.MIN_VALUE)
		{
			IntAlcoholDrinksPerWk iadpwk = spvs.addNewIntAlcoholDrinksPerWk();
			iadpwk.setValue(vsd.getAlcohol_drinksPerWk());
			iadpwk.setSignedWhen(when);
			iadpwk.setSignedWho(who);
		}

		String ddiet = vsd.getSel_DashDiet();
		if (ddiet != null)
		{
			SelDASHdiet sdash = spvs.addNewSelDASHdiet();

			if ("Always".equalsIgnoreCase(ddiet))
				sdash.setValue(StringFrequency.ALWAYS);
			else if ("Often".equalsIgnoreCase(ddiet))
				sdash.setValue(StringFrequency.OFTEN);
			else if ("Sometimes".equalsIgnoreCase(ddiet))
				sdash.setValue(StringFrequency.SOMETIMES);
			else if ("Never".equalsIgnoreCase(ddiet))
				sdash.setValue(StringFrequency.NEVER);
			else
				sdash.setValue(StringFrequency.X);
			sdash.setSignedWhen(when);
			sdash.setSignedWho(who);
		}

		String ssalt = vsd.getSel_HighSaltFood();
		if (ssalt != null)
		{
			SelHighSalt shs = spvs.addNewSelHighSalt();

			if ("Always".equalsIgnoreCase(ssalt))
				shs.setValue(StringFrequency.ALWAYS);
			else if ("Often".equalsIgnoreCase(ssalt))
				shs.setValue(StringFrequency.OFTEN);
			else if ("Sometimes".equalsIgnoreCase(ssalt))
				shs.setValue(StringFrequency.SOMETIMES);
			else if ("Never".equalsIgnoreCase(ssalt))
				shs.setValue(StringFrequency.NEVER);
			else
				shs.setValue(StringFrequency.X);
			shs.setSignedWhen(when);
			shs.setSignedWho(who);
		}

		String sstress = vsd.getSel_Stressed();
		if (sstress != null)
		{
			SelStressed ss = spvs.addNewSelStressed();

			if ("Always".equalsIgnoreCase(sstress))
				ss.setValue(StringFrequency.ALWAYS);
			else if ("Often".equalsIgnoreCase(sstress))
				ss.setValue(StringFrequency.OFTEN);
			else if ("Sometimes".equalsIgnoreCase(sstress))
				ss.setValue(StringFrequency.SOMETIMES);
			else if ("Never".equalsIgnoreCase(sstress))
				ss.setValue(StringFrequency.NEVER);
			else
				ss.setValue(StringFrequency.X);
			ss.setSignedWhen(when);
			ss.setSignedWho(who);
		}

		BRxCurrentDiu brcd = spvs.addNewBRxCurrentDiu();
		brcd.setValue(vsd.isDiuret_rx());
		brcd.setSignedWhen(when);
		brcd.setSignedWho(who);

		BRxCurrentAce brca = spvs.addNewBRxCurrentAce();
		brca.setValue(vsd.isAce_rx());
		brca.setSignedWhen(when);
		brca.setSignedWho(who);

		BRxCurrentArb brcab = spvs.addNewBRxCurrentArb();
		brcab.setValue(vsd.isArecept_rx());
		brcab.setSignedWhen(when);
		brcab.setSignedWho(who);

		BRxCurrentBb brcbb = spvs.addNewBRxCurrentBb();
		brcbb.setValue(vsd.isBeta_rx());
		brcbb.setSignedWhen(when);
		brcbb.setSignedWho(who);

		BRxCurrentCcb brcc = spvs.addNewBRxCurrentCcb();
		brcc.setValue(vsd.isCalc_rx());
		brcc.setSignedWhen(when);
		brcc.setSignedWho(who);

		BRxCurrentOthhtn brcon = spvs.addNewBRxCurrentOthhtn();
		brcon.setValue(vsd.isAnti_rx());
		brcon.setSignedWhen(when);
		brcon.setSignedWho(who);

		BRxCurrentSta brcs = spvs.addNewBRxCurrentSta();
		brcs.setValue(vsd.isStatin_rx());
		brcs.setSignedWhen(when);
		brcs.setSignedWho(who);

		BRxCurrentOthlip brcop = spvs.addNewBRxCurrentOthlip();
		brcop.setValue(vsd.isLipid_rx());
		brcop.setSignedWhen(when);
		brcop.setSignedWho(who);

		BRxCurrentOha brcoa = spvs.addNewBRxCurrentOha();
		brcoa.setValue(vsd.isHypo_rx());
		brcoa.setSignedWhen(when);
		brcoa.setSignedWho(who);

		BRxCurrentIns brci = spvs.addNewBRxCurrentIns();
		brci.setValue(vsd.isInsul_rx());
		brci.setSignedWhen(when);
		brci.setSignedWho(who);

		BRxSideEffectsDiu brsed = spvs.addNewBRxSideEffectsDiu();
		brsed.setValue(vsd.isDiuret_SideEffects());
		brsed.setSignedWhen(when);
		brsed.setSignedWho(who);

		BRxSideEffectsAce brsea = spvs.addNewBRxSideEffectsAce();
		brsea.setValue(vsd.isAce_SideEffects());
		brsea.setSignedWhen(when);
		brsea.setSignedWho(who);

		BRxSideEffectsArb brseab = spvs.addNewBRxSideEffectsArb();
		brseab.setValue(vsd.isArecept_SideEffects());
		brseab.setSignedWhen(when);
		brseab.setSignedWho(who);

		BRxSideEffectsBb brseb = spvs.addNewBRxSideEffectsBb();
		brseb.setValue(vsd.isBeta_SideEffects());
		brseb.setSignedWhen(when);
		brseb.setSignedWho(who);

		BRxSideEffectsCcb brsec = spvs.addNewBRxSideEffectsCcb();
		brsec.setValue(vsd.isCalc_SideEffects());
		brsec.setSignedWhen(when);
		brsec.setSignedWho(who);

		BRxSideEffectsOthhtn brseon = spvs.addNewBRxSideEffectsOthhtn();
		brseon.setValue(vsd.isAnti_SideEffects());
		brseon.setSignedWhen(when);
		brseon.setSignedWho(who);

		BRxSideEffectsSta brses = spvs.addNewBRxSideEffectsSta();
		brses.setValue(vsd.isStatin_SideEffects());
		brses.setSignedWhen(when);
		brses.setSignedWho(who);

		BRxSideEffectsOthlip brseop = spvs.addNewBRxSideEffectsOthlip();
		brseop.setValue(vsd.isLipid_SideEffects());
		brseop.setSignedWhen(when);
		brseop.setSignedWho(who);

		BRxSideEffectsOha brseoa = spvs.addNewBRxSideEffectsOha();
		brseoa.setValue(vsd.isHypo_SideEffects());
		brseoa.setSignedWhen(when);
		brseoa.setSignedWho(who);

		BRxSideEffectsIns brsei = spvs.addNewBRxSideEffectsIns();
		brsei.setValue(vsd.isInsul_SideEffects());
		brsei.setSignedWhen(when);
		brsei.setSignedWho(who);

		String result = null;
		result = vsd.getDiuret_RxDecToday();
		if (result != null)
		{
			SelRxTodayDiu srtd = spvs.addNewSelRxTodayDiu();

			if ("Same".equalsIgnoreCase(result))
				srtd.setValue(StringRxToday.SAME);
			else if ("Increase".equalsIgnoreCase(result))
				srtd.setValue(StringRxToday.INCREASE);
			else if ("Decrease".equalsIgnoreCase(result))
				srtd.setValue(StringRxToday.DECREASE);
			else if ("Stop".equalsIgnoreCase(result))
				srtd.setValue(StringRxToday.STOP);
			else if ("Start".equalsIgnoreCase(result))
				srtd.setValue(StringRxToday.START);
			else if ("InClassSwitch".equalsIgnoreCase(result))
				srtd.setValue(StringRxToday.IN_CLASS_SWITCH);
			else if ("null".equalsIgnoreCase(result))
				srtd.setValue(StringRxToday.X);
			srtd.setSignedWhen(when);
			srtd.setSignedWho(who);
		}

		result = vsd.getAce_RxDecToday();
		if (result != null)
		{
			SelRxTodayAce srta = spvs.addNewSelRxTodayAce();

			if ("Same".equalsIgnoreCase(result))
				srta.setValue(StringRxToday.SAME);
			else if ("Increase".equalsIgnoreCase(result))
				srta.setValue(StringRxToday.INCREASE);
			else if ("Decrease".equalsIgnoreCase(result))
				srta.setValue(StringRxToday.DECREASE);
			else if ("Stop".equalsIgnoreCase(result))
				srta.setValue(StringRxToday.STOP);
			else if ("Start".equalsIgnoreCase(result))
				srta.setValue(StringRxToday.START);
			else if ("InClassSwitch".equalsIgnoreCase(result))
				srta.setValue(StringRxToday.IN_CLASS_SWITCH);
			else if ("null".equalsIgnoreCase(result))
				srta.setValue(StringRxToday.X);
			srta.setSignedWhen(when);
			srta.setSignedWho(who);
		}

		result = vsd.getArecept_RxDecToday();
		if (result != null)
		{
			SelRxTodayArb srtb = spvs.addNewSelRxTodayArb();

			if ("Same".equalsIgnoreCase(result))
				srtb.setValue(StringRxToday.SAME);
			else if ("Increase".equalsIgnoreCase(result))
				srtb.setValue(StringRxToday.INCREASE);
			else if ("Decrease".equalsIgnoreCase(result))
				srtb.setValue(StringRxToday.DECREASE);
			else if ("Stop".equalsIgnoreCase(result))
				srtb.setValue(StringRxToday.STOP);
			else if ("Start".equalsIgnoreCase(result))
				srtb.setValue(StringRxToday.START);
			else if ("InClassSwitch".equalsIgnoreCase(result))
				srtb.setValue(StringRxToday.IN_CLASS_SWITCH);
			else if ("null".equalsIgnoreCase(result))
				srtb.setValue(StringRxToday.X);
			srtb.setSignedWhen(when);
			srtb.setSignedWho(who);
		}

		result = vsd.getBeta_RxDecToday();
		if (result != null)
		{
			SelRxTodayBb srtbb = spvs.addNewSelRxTodayBb();

			if ("Same".equalsIgnoreCase(result))
				srtbb.setValue(StringRxToday.SAME);
			else if ("Increase".equalsIgnoreCase(result))
				srtbb.setValue(StringRxToday.INCREASE);
			else if ("Decrease".equalsIgnoreCase(result))
				srtbb.setValue(StringRxToday.DECREASE);
			else if ("Stop".equalsIgnoreCase(result))
				srtbb.setValue(StringRxToday.STOP);
			else if ("Start".equalsIgnoreCase(result))
				srtbb.setValue(StringRxToday.START);
			else if ("InClassSwitch".equalsIgnoreCase(result))
				srtbb.setValue(StringRxToday.IN_CLASS_SWITCH);
			else if ("null".equalsIgnoreCase(result))
				srtbb.setValue(StringRxToday.X);
			srtbb.setSignedWhen(when);
			srtbb.setSignedWho(who);
		}

		result = vsd.getCalc_RxDecToday();
		if (result != null)
		{
			SelRxTodayCcb srtc = spvs.addNewSelRxTodayCcb();

			if ("Same".equalsIgnoreCase(result))
				srtc.setValue(StringRxToday.SAME);
			else if ("Increase".equalsIgnoreCase(result))
				srtc.setValue(StringRxToday.INCREASE);
			else if ("Decrease".equalsIgnoreCase(result))
				srtc.setValue(StringRxToday.DECREASE);
			else if ("Stop".equalsIgnoreCase(result))
				srtc.setValue(StringRxToday.STOP);
			else if ("Start".equalsIgnoreCase(result))
				srtc.setValue(StringRxToday.START);
			else if ("InClassSwitch".equalsIgnoreCase(result))
				srtc.setValue(StringRxToday.IN_CLASS_SWITCH);
			else if ("null".equalsIgnoreCase(result))
				srtc.setValue(StringRxToday.X);
			srtc.setSignedWhen(when);
			srtc.setSignedWho(who);
		}

		result = vsd.getAnti_RxDecToday();
		if (result != null)
		{
			SelRxTodayOthhtn srton = spvs.addNewSelRxTodayOthhtn();

			if ("Same".equalsIgnoreCase(result))
				srton.setValue(StringRxToday.SAME);
			else if ("Increase".equalsIgnoreCase(result))
				srton.setValue(StringRxToday.INCREASE);
			else if ("Decrease".equalsIgnoreCase(result))
				srton.setValue(StringRxToday.DECREASE);
			else if ("Stop".equalsIgnoreCase(result))
				srton.setValue(StringRxToday.STOP);
			else if ("Start".equalsIgnoreCase(result))
				srton.setValue(StringRxToday.START);
			else if ("InClassSwitch".equalsIgnoreCase(result))
				srton.setValue(StringRxToday.IN_CLASS_SWITCH);
			else if ("null".equalsIgnoreCase(result))
				srton.setValue(StringRxToday.X);
			srton.setSignedWhen(when);
			srton.setSignedWho(who);
		}

		result = vsd.getStatin_RxDecToday();
		if (result != null)
		{
			SelRxTodaySta srts = spvs.addNewSelRxTodaySta();

			if ("Same".equalsIgnoreCase(result))
				srts.setValue(StringRxToday.SAME);
			else if ("Increase".equalsIgnoreCase(result))
				srts.setValue(StringRxToday.INCREASE);
			else if ("Decrease".equalsIgnoreCase(result))
				srts.setValue(StringRxToday.DECREASE);
			else if ("Stop".equalsIgnoreCase(result))
				srts.setValue(StringRxToday.STOP);
			else if ("Start".equalsIgnoreCase(result))
				srts.setValue(StringRxToday.START);
			else if ("InClassSwitch".equalsIgnoreCase(result))
				srts.setValue(StringRxToday.IN_CLASS_SWITCH);
			else if ("null".equalsIgnoreCase(result))
				srts.setValue(StringRxToday.X);
			srts.setSignedWhen(when);
			srts.setSignedWho(who);
		}

		result = vsd.getLipid_RxDecToday();
		if (result != null)
		{
			SelRxTodayOthlip srtop = spvs.addNewSelRxTodayOthlip();
			if ("Same".equalsIgnoreCase(result))
				srtop.setValue(StringRxToday.SAME);
			else if ("Increase".equalsIgnoreCase(result))
				srtop.setValue(StringRxToday.INCREASE);
			else if ("Decrease".equalsIgnoreCase(result))
				srtop.setValue(StringRxToday.DECREASE);
			else if ("Stop".equalsIgnoreCase(result))
				srtop.setValue(StringRxToday.STOP);
			else if ("Start".equalsIgnoreCase(result))
				srtop.setValue(StringRxToday.START);
			else if ("InClassSwitch".equalsIgnoreCase(result))
				srtop.setValue(StringRxToday.IN_CLASS_SWITCH);
			else if ("null".equalsIgnoreCase(result))
				srtop.setValue(StringRxToday.X);
			srtop.setSignedWhen(when);
			srtop.setSignedWho(who);
		}

		result = vsd.getHypo_RxDecToday();
		if (result != null)
		{
			SelRxTodayOha srtoa = spvs.addNewSelRxTodayOha();

			if ("Same".equalsIgnoreCase(result))
				srtoa.setValue(StringRxToday.SAME);
			else if ("Increase".equalsIgnoreCase(result))
				srtoa.setValue(StringRxToday.INCREASE);
			else if ("Decrease".equalsIgnoreCase(result))
				srtoa.setValue(StringRxToday.DECREASE);
			else if ("Stop".equalsIgnoreCase(result))
				srtoa.setValue(StringRxToday.STOP);
			else if ("Start".equalsIgnoreCase(result))
				srtoa.setValue(StringRxToday.START);
			else if ("InClassSwitch".equalsIgnoreCase(result))
				srtoa.setValue(StringRxToday.IN_CLASS_SWITCH);
			else if ("null".equalsIgnoreCase(result))
				srtoa.setValue(StringRxToday.X);
			srtoa.setSignedWhen(when);
			srtoa.setSignedWho(who);
		}

		result = vsd.getInsul_RxDecToday();
		if (result != null)
		{
			SelRxTodayIns srti = spvs.addNewSelRxTodayIns();

			if ("Same".equalsIgnoreCase(result))
				srti.setValue(StringRxToday.SAME);
			else if ("Increase".equalsIgnoreCase(result))
				srti.setValue(StringRxToday.INCREASE);
			else if ("Decrease".equalsIgnoreCase(result))
				srti.setValue(StringRxToday.DECREASE);
			else if ("Stop".equalsIgnoreCase(result))
				srti.setValue(StringRxToday.STOP);
			else if ("Start".equalsIgnoreCase(result))
				srti.setValue(StringRxToday.START);
			else if ("InClassSwitch".equalsIgnoreCase(result))
				srti.setValue(StringRxToday.IN_CLASS_SWITCH);
			else if ("null".equalsIgnoreCase(result))
				srti.setValue(StringRxToday.X);
			srti.setSignedWhen(when);
			srti.setSignedWho(who);
		}

		tempi = vsd.getOften_miss();
		if (tempi != Integer.MIN_VALUE)
		{
			IntMissedMedsPerWk immp = spvs.addNewIntMissedMedsPerWk();
			immp.setValue(vsd.getOften_miss());
			immp.setSignedWhen(when);
			immp.setSignedWho(who);
		}

		result = vsd.getHerbal();
		if (result != null)
		{
			SelHerbalMeds shmd = spvs.addNewSelHerbalMeds();

			if ("yes".equalsIgnoreCase(result))
				shmd.setValue(StringYesNo.YES);
			else if ("no".equalsIgnoreCase(result))
				shmd.setValue(StringYesNo.NO);
			else if ("null".equalsIgnoreCase(result))
				shmd.setValue(StringYesNo.X);
			shmd.setSignedWhen(when);
			shmd.setSignedWho(who);
		}

		result = vsd.getNextvisit();
		if (result != null)
		{
			SelFollowUp sfu = spvs.addNewSelFollowUp();

			if ("Under1Mo".equalsIgnoreCase(result))
				sfu.setValue(StringFollowUpInterval.UNDER_1_MO);
			else if ("1to2Mo".equalsIgnoreCase(result))
				sfu.setValue(StringFollowUpInterval.X_1_TO_2_MO);
			else if ("3to6Mo".equalsIgnoreCase(result))
				sfu.setValue(StringFollowUpInterval.X_3_TO_6_MO);
			else if ("Over6Mo".equalsIgnoreCase(result))
				sfu.setValue(StringFollowUpInterval.OVER_6_MO);
			else if ("null".equalsIgnoreCase(result))
				sfu.setValue(StringFollowUpInterval.X);
			sfu.setSignedWhen(when);
			sfu.setSignedWho(who);
		}

		BBPAP bbpap = spvs.addNewBBPAP();
		bbpap.setValue(vsd.isBpactionplan());
		bbpap.setSignedWhen(when);
		bbpap.setSignedWho(who);

		BTPOff btpoff = spvs.addNewBTPOff();
		btpoff.setValue(vsd.isPressureOff());
		btpoff.setSignedWhen(when);
		btpoff.setSignedWho(who);

		BPPAgreement bppa = spvs.addNewBPPAgreement();
		bppa.setValue(vsd.isPatientProvider());
		bppa.setSignedWhen(when);
		bppa.setSignedWho(who);

		BABPM babpm = spvs.addNewBABPM();
		babpm.setValue(vsd.isABPM());
		babpm.setSignedWhen(when);
		babpm.setSignedWho(who);

		BHomeMon bhmn = spvs.addNewBHomeMon();
		bhmn.setValue(vsd.isHome());
		bhmn.setSignedWhen(when);
		bhmn.setSignedWho(who);

		BCommunRes bcr = spvs.addNewBCommunRes();
		bcr.setValue(vsd.isCommunityRes());
		bcr.setSignedWhen(when);
		bcr.setSignedWho(who);

		BReferHCP brhcp = spvs.addNewBReferHCP();
		brhcp.setValue(vsd.isProRefer());
		brhcp.setSignedWhen(when);
		brhcp.setSignedWho(who);

	}

	public HsfoHbpsDataDocument generateXML(LoggedInInfo loggedInInfo, String providerNo,
			Integer demographicNo)
	{
		HsfoHbpsDataDocument doc = HsfoHbpsDataDocument.Factory.newInstance();

		// add HsfoHbpsData
		HsfoHbpsData root = doc.addNewHsfoHbpsData();
		root.setVersionDate(new XmlCalendar(getVersionDate()));
		root.setExtractedWhen(new XmlCalendar(dformat1.format(new Date())));

		root.setExtractedWho(getProviderName(providerNo));

		// add site
		Site site = root.addNewSite();
		site.setSiteCodeKey(getSiteID().intValue());

		if (demographicNo.intValue() == 0)
		{
			// add all patients data
			List patientIdList = hdao.getAllPatientId();
			if (patientIdList != null)
				for (int i = 0; i < patientIdList.size(); i++)
				{
					String pid = (String) patientIdList.get(i);
					PatientData pdata = getDemographic(pid);
					if (pdata != null && pdata.getPatient_Id() != null)
						addPatientToSite(loggedInInfo, site, pdata);
				}
			if (patientIdList == null || patientIdList.size() == 0)
				doc = null;
		} else
		{
			PatientData pdata = getDemographic(demographicNo.toString());
			if (pdata != null && pdata.getPatient_Id() != null)
				addPatientToSite(loggedInInfo, site, pdata);
			else
				doc = null;
		}

		return doc;

	}

	public String printErrors(ArrayList validationErrors)
	{
		StringBuilder sb = new StringBuilder(
				"++++++++++++Invalid XML!++++++++++++\n");

		sb.append("Errors discovered during validation: \n");
		Iterator iter = validationErrors.iterator();
		while (iter.hasNext())
		{
			sb.append(">> " + iter.next() + "\n");
		}
		return sb.toString();
	}

	public ArrayList validateDoc(HsfoHbpsDataDocument doc) throws IOException,
			XmlException
	{
		if (logger.isDebugEnabled())
			logger.debug("<?xml version=\"1.0\" encoding=\"utf-8\"?>" + "\n"
					+ PrettyPrinter.indent(doc.xmlText()));

		ArrayList messageArray = new ArrayList();
		XmlOptions option = new XmlOptions();
		ArrayList validationErrors = new ArrayList();
		option.setErrorListener(validationErrors);
		if (doc.validate(option) == false)
		{
			String sb = printErrors(validationErrors);
			messageArray.add(sb);
			return messageArray;
		} else
			return messageArray;
	}

	public String base64Encoding(byte[] input)
	{
	    return(new String(Base64.encodeBase64(input)));
	}

	public byte[] zipCompress(String fileName, byte[] input) throws IOException
	{

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);

		try
		{
			zos.putNextEntry(new ZipEntry(fileName));
			zos.write(input);
			zos.closeEntry();
			zos.finish();

			return baos.toByteArray();
		} finally
		{
			zos.close();
		}
	}

	private static DOMResult transform(SOAPPart part) throws Exception
	{
		Transformer trans = TransformerFactory.newInstance().newTransformer();
		DOMResult rs = new DOMResult();
		trans.transform(part.getContent(), rs);

		return rs;
	}

	private static String getValueByTagName(Node n, String tag)
	{
		if (n == null)
			return null;
		if (tag.equals(n.getLocalName()))
			return n.getFirstChild().getNodeValue();
		if (n.hasChildNodes())
			return getValueByTagName(n.getFirstChild(), tag);
		else if (n.getNextSibling() != null)
			return getValueByTagName(n.getNextSibling(), tag);
		else
			return getValueByTagName(n.getParentNode().getNextSibling(), tag);
	}

	public ArrayList soapHttpCall(int siteCode, String userId, String passwd,
			String xml) throws Exception
	{
		userId = userId.replaceAll("&", "&amp;");
		passwd = passwd.replaceAll("&", "&amp;");

		PostMethod post = new PostMethod(defaultweb);
		post.setRequestHeader("SOAPAction", actionString);
		post.setRequestHeader("Content-Type", "text/xml; charset=utf-8");

		String soapMsg = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><soap-env:Envelope xmlns:soap-env=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
				+ "<soap-env:Header/><soap-env:Body><UploadToCDV xmlns=\""
				+ soaplink
				+ "\"><LoginSiteCode>"
				+ siteCode
				+ "</LoginSiteCode>"
				+ "<LoginUserID>"
				+ userId
				+ "</LoginUserID>"
				+ "<LoginPassword>"
				+ passwd
				+ "</LoginPassword>"
				+ "<FileAsBinary>"
				+ base64Encoding(zipCompress("HsfoData.xml", xml
						.getBytes("UTF-8")))
				+ "</FileAsBinary>"
				+ "</UploadToCDV></soap-env:Body></soap-env:Envelope>";

		RequestEntity re = new StringRequestEntity(soapMsg, "text/xml", "utf-8");

		post.setRequestEntity(re);

		HttpClient httpclient = new HttpClient();
		// Execute request
		try
		{
			int result = httpclient.executeMethod(post);
			// Display status code

			if (result != 200)
			{
				ArrayList rList = new ArrayList();
				rList.add(new Integer(result).toString());
				rList.add("Fail to upload patient data to " + soaplink);
				return rList;

			}
			String rsXml = post.getResponseBodyAsString();

			int p = rsXml.indexOf("<UploadToCDVResult>");
			int q = rsXml.indexOf("</UploadToCDVResult>");
			String code = rsXml
					.substring(p + "<UploadToCDVResult>".length(), q);

			p = rsXml.indexOf("<MessageForResult>");
			q = rsXml.indexOf("</MessageForResult>");
			String message = rsXml.substring(p + "<MessageForResult>".length(),
					q);

			ArrayList reList = new ArrayList();
			reList.add(code);
			reList.add(message);
			return reList;
		} finally
		{
			// Release current connection to the connection pool
			post.releaseConnection();
		}
	}

	// public ArrayList soapCall(String hsfoString) throws Exception {
	// logger.info("=============inside soapcall===========");
	// SOAPConnectionFactory fact;
	// fact = SOAPConnectionFactory.newInstance();
	// SOAPConnection con = fact.createConnection();
	//
	// try {
	// SOAPMessage smsg = MessageFactory.newInstance().createMessage();
	//
	// MimeHeaders mh = smsg.getMimeHeaders();
	// mh.addHeader("SOAPAction",actionString);
	//
	// SOAPPart prt = smsg.getSOAPPart();
	// SOAPEnvelope env = prt.getEnvelope();
	// env.addNamespaceDeclaration("xsd",
	// "http://www.w3.org/2001/XMLSchema");
	// env.addNamespaceDeclaration("xsi",
	// "http://www.w3.org/2001/XMLSchema-instance");
	//
	// SOAPBody body = env.getBody();
	//
	// // UploadToCDVDocument doc =
	// // UploadToCDVDocument.Factory.newInstance();
	// // UploadToCDV upload = doc.addNewUploadToCDV();
	// // upload.setLoginUserID(getUserId());
	// // upload.setLoginPassword(getLoginPasswd());
	// // upload.setLoginSiteCode(getSiteID().intValue());
	// //
	// // upload.setFileAsBinary(zipCompress("HSFOdata.xml",
	// // hsfoString.getBytes(
	// // "UTF-8")));
	// //
	// // Node nd = doc.getDomNode();
	// // body.addDocument((Document) nd);
	// SOAPElement be = body.addChildElement(env.createName("UploadToCDV",
	// null, soaplink));
	//
	// be.addChildElement("LoginSiteCode").addTextNode(
	// getSiteID().toString());
	// be.addChildElement("LoginUserID").addTextNode(getUserId());
	// be.addChildElement("LoginPassword").addTextNode(getLoginPasswd());
	// be.addChildElement("FileAsBinary").addTextNode(
	// base64Encoding(zipCompress("HSFOdata.xml", hsfoString
	// .getBytes("UTF-8"))));
	//
	// smsg.saveChanges();
	//
	// URL endpoint = new URL(defaultweb);
	//
	// SOAPMessage response = con.call(smsg, endpoint);
	//
	// // UploadToCDVResponseDocument respDoc =
	// // UploadToCDVResponseDocument.Factory
	// // .parse(response.getSOAPBody().getFirstChild());
	// // String rs=respDoc.getUploadToCDVResponse().getMessageForResult();
	// // int code=respDoc.getUploadToCDVResponse().getUploadToCDVResult();
	//
	// DOMResult rs = transform(response.getSOAPPart());
	// Node n = rs.getNode();
	//
	// String rsId = getValueByTagName(n, "UploadToCDVResult");
	// String rsMsg = getValueByTagName(n, "MessageForResult");
	//
	// ArrayList reList = new ArrayList();
	// reList.add(rsId);
	// reList.add(rsMsg);
	//
	// return reList;
	// } finally {
	// con.close();
	// }
	//
	// }
}
