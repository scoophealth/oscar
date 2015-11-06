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

package oscar.form.study.hsfo2.pageUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlCalendar;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.tool.PrettyPrinter;
import org.hsfo.v2.*;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.BFamHxCHD;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.BFamHxDM;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.BFamHxDepression;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.BFamHxDyslipidemia;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.BFamHxHtn;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.BFamHxKidney;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.BFamHxObesity;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.BFamHxStrokeTIA;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.BHxCHD;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.BHxDM;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.BHxDepression;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.BHxDyslipidemia;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.BHxKidney;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.BHxObesity;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.BHxStrokeTIA;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.CmpBP;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.DatBirthDate;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.DatHsfHmpStatusDate;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.DblA1CFraction;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.DblACRMgPermmol;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.DblFBSMM;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.DblHDLMM;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.DblHeightCm;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.DblLDLMM;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.DblTCtoHDL;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.DblTriglyceridesMM;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.DblWaistCircumfCm;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.DblWeightKg;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.BBpMonAmbul;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.BBpMonHome;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.BGoalAlcohol;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.BGoalDashDiet;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.BGoalPhysActivity;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.BGoalSalt;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.BGoalSmoking;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.BGoalStress;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.BGoalWeight;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.BHsfToolBPAP;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.BHsfToolTPO;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.BReferCommunRes;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.BReferHCP;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.BRxSideEffectsAce;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.BRxSideEffectsArb;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.BRxSideEffectsAsa;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.BRxSideEffectsBb;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.BRxSideEffectsCcb;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.BRxSideEffectsDiu;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.BRxSideEffectsIns;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.BRxSideEffectsOha;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.BRxSideEffectsOthhtn;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.BRxSideEffectsOthlip;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.BRxSideEffectsSta;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.CmpFollowUp;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.IntAlcoholDrinksPerWk;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.IntGoalConfidence;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.IntGoalImportance;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.IntMissedMedsPerWk;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.IntPhysActivityMinPerWk;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.IntSmokingCigsPerDay;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.SelAdequateDrugCoverage;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.SelDashDiet;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.SelGoalView;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.SelHerbalMeds;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.SelHighSalt;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.SelRiskAlcohol;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.SelRiskDiet;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.SelRiskPhysActivity;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.SelRiskSmoking;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.SelRiskStress;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.SelRiskWeight;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.SelRxTodayAce;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.SelRxTodayArb;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.SelRxTodayAsa;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.SelRxTodayBb;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.SelRxTodayCcb;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.SelRxTodayDiu;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.SelRxTodayIns;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.SelRxTodayOha;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.SelRxTodayOthhtn;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.SelRxTodayOthlip;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.SelRxTodaySta;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheet.SelStressed;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheetBaseline;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheetBaseline.BEthnicBangladeshi;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheetBaseline.BEthnicBlack;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheetBaseline.BEthnicChinese;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheetBaseline.BEthnicEIndian;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheetBaseline.BEthnicFirstNation;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheetBaseline.BEthnicHispanic;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheetBaseline.BEthnicJapanese;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheetBaseline.BEthnicKorean;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheetBaseline.BEthnicOther;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheetBaseline.BEthnicPakistani;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheetBaseline.BEthnicRefused;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheetBaseline.BEthnicSriLankan;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheetBaseline.BEthnicUnknown;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheetBaseline.BEthnicWhite;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheetBaseline.BRxAtHmpBslAce;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheetBaseline.BRxAtHmpBslArb;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheetBaseline.BRxAtHmpBslAsa;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheetBaseline.BRxAtHmpBslBb;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheetBaseline.BRxAtHmpBslCcb;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheetBaseline.BRxAtHmpBslDiu;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheetBaseline.BRxAtHmpBslIns;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheetBaseline.BRxAtHmpBslOha;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheetBaseline.BRxAtHmpBslOthhtn;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheetBaseline.BRxAtHmpBslOthlip;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheetBaseline.BRxAtHmpBslSta;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheetBaseline.DatHmpBslVisitDate;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.FormHsfHmpFlowsheetBaseline.SelHtnDxAgoAtHmpBsl;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.IntEGFRMLPerMin;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.SelHsfHmpStatus;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.SelHtnDxType;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.SelSex;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.TxtEmrHcpID;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.TxtGivenNames;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.TxtPostalCode;
import org.hsfo.v2.HsfHmpDataDocument.HsfHmpData.Site.SitePatient.TxtSurname;
import org.oscarehr.common.model.Hsfo2Patient;
import org.oscarehr.common.model.Hsfo2Visit;
import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;
import oscar.form.study.hsfo2.HSFODAO;
import oscar.oscarProvider.data.ProviderData;

public class XMLTransferUtil
{
	private static Logger logger = MiscUtils.getLogger();
  public static enum SoapElementKey
  {
	  DataVaultStatusStrResult, StatusMessage, responseStatusCode, DataVaultResult, GetDataDateRangeResult, DataBeginDate, DataEndDate,

  }

  SimpleDateFormat dformat1     = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss" );
  SimpleDateFormat dformat2     = new SimpleDateFormat( "yyyy-MM-dd" );

  private String defaultweb = "https://www.clinforma.net/Hsf/HmpVaultWS/";
  //"https://www.clinforma.net/P-Prompt/DataReceiveWS/";
//  https://www.clinforma.net/Hsf/HmpVaultTestWS/
 // private String getDataDateRangeAction = "https://www.clinforma.net/P-Prompt/DataReceiveWS/GetDataDateRange";
  private String dataVaultAction = "https://www.clinforma.net/P-Prompt/DataReceiveWS/DataVault";
 // private String namespace = "https://www.clinforma.net/P-Prompt/DataReceiveWS/";

//  String                  soaplink     = "https://www.clinforma.net/HsfoHbps/DataVaultWS";

  HSFODAO                 hdao         = new HSFODAO();

  {
    if( isTest() )
    {
      defaultweb="https://www.clinforma.net/P-Prompt/DataReceiveTestWS/";
      //"https://www.clinforma.net/P-Prompt/DataReceiveTestWS/";
    //  getDataDateRangeAction = "https://www.clinforma.net/P-Prompt/DataReceiveTestWS/GetDataDateRange";
      dataVaultAction = "https://www.clinforma.net/P-Prompt/DataReceiveTestWS/DataVault";
//      namespace = "https://www.clinforma.net/P-Prompt/DataReceiveTestWS/";    //same as production
    }
  }

  public Integer getSiteID()
  {
    OscarProperties props = OscarProperties.getInstance();
    String id = props.getProperty( "hsfo2.loginSiteCode", "" );
    return new Integer( id );
  }

  public String getUserId()
  {
    OscarProperties props = OscarProperties.getInstance();
    return props.getProperty( "hsfo2.userID", "" );

  }

  public String getLoginPasswd()
  {
    OscarProperties props = OscarProperties.getInstance();
    return props.getProperty( "hsfo2.loginPassword", "" );
  }

  public String getVersionDate()
  {
    OscarProperties props = OscarProperties.getInstance();
    return props.getProperty( "hsfo2.xmlVersionDate", "2010-12-09" );
  }

  public boolean isTest()
  {
    OscarProperties props = OscarProperties.getInstance();
    return "true".equalsIgnoreCase( props.getProperty( "hsfo2.isTest", "false" ) );
  }
  public String getWebUrl()
  {
    OscarProperties props = OscarProperties.getInstance();
    return props.getProperty( "hsfo2.webServiceURL", defaultweb );
  }

  public String getProviderName( String providerNo )
  {
    ProviderData pd = new ProviderData( providerNo );
    if ( pd == null )
      return "";
    else
    {

      String firstName = pd.getFirst_name() == null ? "" : pd.getFirst_name();
      String lastName = pd.getLast_name() == null ? "" : pd.getLast_name();
      return ( firstName + " " + lastName ).trim();
    }
  }

  public Hsfo2Patient getDemographic( String demoNo ) 
  {

    return hdao.retrievePatientRecord( demoNo );

  }

  public Hsfo2Visit getSignedVisit( String patientId, String startDate, String endDate ) 
  {
    List pList = hdao.nullSafeRetrVisitRecord( patientId, startDate, endDate );
    if ( pList == null || pList.size() == 0 )
      return null;
    Hsfo2Visit vs = (Hsfo2Visit) pList.get( 0 );
    Date vd = vs.getFormEdited();
    // get the latest signed record
    for ( int i = 1; i < pList.size() - 1; i++ )
    {
      Hsfo2Visit tv = (Hsfo2Visit) pList.get( i );
      Date tvd = tv.getFormEdited();
      if ( tvd != null && vd != null && tvd.after( vd ) )
      {
        vs = tv;
        vd = tvd;
      }
    }
    return vs;
  }

  public String getSignedProvider( String patientId, String startDate, String endDate ) 
  {
    Hsfo2Visit vs = getSignedVisit( patientId, startDate, endDate );
     return getProviderName( vs.getProvider_no());
  }

  public Date getSignedDate( String patientId, String startDate, String endDate ) 
  {
    Hsfo2Visit vs = getSignedVisit( patientId, startDate, endDate );
    if( vs!=null)
    	return vs.getFormEdited();
    else
	return null;
  }

  /**
   * this method in fact generate all xml for one patient. it all patient data and all visit datas to xml
   *
   * @param site
   * @param pd
   * @param baseLineVd
   *          : the baseLine visit data, namely the initial patient data
   * @throws Exception
   */
  public void addPatientToSite( Site site, Hsfo2Patient pd, Hsfo2Visit baseLineVd, Calendar startDate, Calendar endDate)
  {
    if ( baseLineVd == null )
      baseLineVd = ( new HSFODAO() ).getPatientBaseLineVisitData( pd );

    //If the baseline form not completed
    if(baseLineVd == null)
    	return;


    String startDateStr = String.valueOf(startDate.get(Calendar.YEAR)) + "-" + String.valueOf(startDate.get(Calendar.MONTH)+1)+ "-" + String.valueOf(startDate.get(Calendar.DATE)) ;
    String endDateStr = String.valueOf(endDate.get(Calendar.YEAR)) + "-" + String.valueOf(endDate.get(Calendar.MONTH)+1)+ "-" + String.valueOf(endDate.get(Calendar.DATE)) ;

    //String dateString2 = dformat2.format( pd.getConsentDate() );  //e.g. "2012-03-13"
    Date signedDate = getSignedDate( pd.getPatient_Id(), startDateStr, endDateStr );
    if(signedDate == null) return;

    String dateString1 = dformat1.format( signedDate ); //e.g. "2012-03-13T08:48:50"

    final XmlCalendar when = new XmlCalendar( dateString1 );
    final String who = getSignedProvider( pd.getPatient_Id(), startDateStr, endDateStr );

    XmlCalendar dob = new XmlCalendar( dformat2.format( pd.getBirthDate() ) );
    //final Calendar visitDate = ConvertUtil.dateToCalendar( baseLineVd.getVisitDate_Id() );
    final XmlCalendar visitDate = new XmlCalendar( dformat2.format( baseLineVd.getVisitDate_Id() ));

    // add patient
    SitePatient patient = site.addNewSitePatient();
    patient.setEmrPatientKey( pd.getPatient_Id() );

    // DatConsentDate dcd = patient.addNewDatConsentDate();

    // dcd.setValue(new XmlCalendar(dateString2));
    //
    // dcd.setSignedWhen(when);
    //
    // dcd.setSignedWho(who);
    //
    // DatDropDate ddd = patient.addNewDatDropDate();
    // ddd.setSignedWhen(when);
    // ddd.setSignedWho(who);
    //
    // TxtEmrHcpID tehid = patient.addNewTxtEmrHcpID();
    // DemographicData demoData = new DemographicData();
    // String providerId=demoData.getDemographic(patient.getEmrPatientKey()).getProviderNo();

    // if (pd.getEmrHCPId() == null)
    // tehid.setValue("");
    // else
    // tehid.setValue(pd.getEmrHCPId());
    /* get EmrHcpId from demographic table, not hsfo_patient table */
    // if (providerId == null)
    // tehid.setValue("");
    // else
    // tehid.setValue(providerId);
    // tehid.setSignedWhen(when);
    // tehid.setSignedWho(who);

    // ---- Initial section (unnamed): 8 fields are obtained independently of HSF HMP forms.
    //8 fields for patient demographics and EmrHcpID (each minOccurs="1" and maxOccurs="1"):
    //These data are obtained independently of HFS HMP forms

    // Surname
    TxtSurname tsn = patient.addNewTxtSurname();
    tsn.setValue( pd.getLName() );
    tsn.setSignedWhen( when );
    tsn.setSignedWho( who );

    // GivenNames
    TxtGivenNames tgn = patient.addNewTxtGivenNames();
    tgn.setValue( pd.getFName() );
    tgn.setSignedWhen( when );
    tgn.setSignedWho( who );

    DatBirthDate dbd = patient.addNewDatBirthDate();
    dbd.setValue( dob );
    dbd.setSignedWhen( when );
    dbd.setSignedWho( who );

    SelSex ss = patient.addNewSelSex();
    String sex = pd.getSex();
    if ( "m".equalsIgnoreCase( sex ) )
      ss.setValue( StringSex.MALE );
    else
      ss.setValue( StringSex.FEMALE );
    ss.setSignedWhen( when );
    ss.setSignedWho( who );

    // Txt_EmrHcpID
    {
      TxtEmrHcpID hcpID = patient.addNewTxtEmrHcpID();
      if ( pd.getEmrHCPId() == null )
        hcpID.setValue( "" );
      else
        hcpID.setValue( pd.getEmrHCPId() );
      hcpID.setSignedWhen( when );
      hcpID.setSignedWho( who );
    }

    //Txt_PostalCode
    {
      TxtPostalCode tpcfsa = patient.addNewTxtPostalCode();
      tpcfsa.setValue( ConvertUtil.toUpperCase( pd.getPostalCode() ) );
      tpcfsa.setSignedWhen( when );
      tpcfsa.setSignedWho( who );
    }
    // sel_HsfHmpStatus
    {
      SelHsfHmpStatus hsfHmpStatus = patient.addNewSelHsfHmpStatus();
      String status = pd.getStatusInHmp();
      if( "Enrolled".equalsIgnoreCase( status ) )
        status = "Enrolled";
      else if( "NotEnrolled".equalsIgnoreCase( status ) )
        status = "NotEnrolled";
      hsfHmpStatus.setValue( org.hsfo.v2.StringHsfHmpStatus.Enum.forString( status )  );
      hsfHmpStatus.setSignedWhen( when );
      hsfHmpStatus.setSignedWho( who );
    }

    // dat_HsfHmpStatusDate
    {
      DatHsfHmpStatusDate hsfHmpStatusDate = patient.addNewDatHsfHmpStatusDate();
      //hsfHmpStatusDate.setValue( ConvertUtil.dateToCalendar( pd.getDateOfHmpStatus() ) );
      hsfHmpStatusDate.setValue( new XmlCalendar( dformat2.format(pd.getDateOfHmpStatus())));
      hsfHmpStatusDate.setSignedWhen( when );
      hsfHmpStatusDate.setSignedWho( who );
    }

    // ----- form_HsfHmpFlowsheet_Baseline section ------
    //The fields are from HSF HMP Baseline only:
    //These data are obtained only once, on HSF HMP’s baseline form

    Calendar visitDateValue = Calendar.getInstance();
    visitDateValue.setTime( baseLineVd.getVisitDate_Id() );
    if(visitDateValue.after(startDate) && visitDateValue.before(endDate)) {
    	addBaseLineData( patient, pd, baseLineVd, when, who );
    }

    // ------form_HsfHmpFlowsheet section ------
    // Fields from both HSF HMP Baseline and Follow-up:
    //These data are obtained repeatedly on HSF HMP forms, always on the baseline form
    //first and then again serially on any number of successive follow-up forms.
    //The effective date of these data is captured in the section’s “VisitDate_key" attribute.

    addAllPatientVisit( patient, pd.getPatient_Id(), startDateStr, endDateStr );

    // ----- Final section: Fields for patient medical diagnoses, family history, physical exam results and lab test -----
    //These data are obtained either directly on HSF HMP’s Baseline and Follow-up forms or
    //independently of HSF HMP forms at any number of other times during patient care.
    //Each of these data have a “valueDate" attribute that captures the effective date of the
    //“value" attribute, for example, the date when the lab test result was measured or the
    //the patient provided the answer, or the measurement on physical exam was taken.

    addAllPatientVisitFinalSection( patient, pd.getPatient_Id(), startDateStr, endDateStr, startDate, endDate, visitDateValue, visitDate, when, who );

  }
  
  
  public void addFinalSection( Hsfo2Visit baseLineVd, SitePatient patient, String patientId, Calendar startDate, Calendar endDate, Calendar visitDateValue, XmlCalendar visitDate, XmlCalendar when, String who ) 
  {	
	if(visitDateValue.after(startDate) && visitDateValue.before(endDate)) 
	{    
      {
      // sel_HtnDx_type
      String hdt = baseLineVd.getHtnDxType();
      StringHtnDxType.Enum dxType = StringHtnDxType.X;
      if ( "PrimaryHtn".equalsIgnoreCase( hdt ) )
        dxType = StringHtnDxType.PRIMARY_HTN;
      if ( "ElevatedBpReadings".equalsIgnoreCase( hdt ) )
        dxType = StringHtnDxType.ELEVATED_BP_READINGS;
      else
        // if ("null".equalsIgnoreCase(hdt))
        dxType = StringHtnDxType.X;

      SelHtnDxType element = patient.addNewSelHtnDxType();
      element.setValue( dxType );
      element.setValueDate( visitDate );
      element.setSignedWhen( when );
      element.setSignedWho( who );

    }


    {
      // b_Hx_Dyslipidemia
      BHxDyslipidemia element = patient.addNewBHxDyslipidemia();
      element.setValue( baseLineVd.isDyslipid() );
      element.setValueDate( visitDate );
      element.setSignedWhen( when );
      element.setSignedWho( who );
    }

    {
      // b_Hx_DM
      BHxDM element = patient.addNewBHxDM();
      element.setValue( baseLineVd.isDiabetes() );
      element.setValueDate( visitDate );
      element.setSignedWhen( when );
      element.setSignedWho( who );
    }

    {
      // b_Hx_Kidney
      BHxKidney element = patient.addNewBHxKidney();
      element.setValue( baseLineVd.isKidneyDis() );
      element.setValueDate( visitDate );
      element.setSignedWhen( when );
      element.setSignedWho( who );
    }

    {
      // b_Hx_Obesity
      BHxObesity element = patient.addNewBHxObesity();
      element.setValue( baseLineVd.isObesity() );
      element.setValueDate( visitDate );
      element.setSignedWhen( when );
      element.setSignedWho( who );
    }

    {
      // b_Hx_CHD
      BHxCHD element = patient.addNewBHxCHD();
      element.setValue( baseLineVd.isCHD() );
      element.setValueDate( visitDate );
      element.setSignedWhen( when );
      element.setSignedWho( who );
    }

    {
      // b_Hx_StrokeTIA
      BHxStrokeTIA element = patient.addNewBHxStrokeTIA();
      element.setValue( baseLineVd.isStroke_TIA() );
      element.setValueDate( visitDate );
      element.setSignedWhen( when );
      element.setSignedWho( who );
    }
    {
      // b_Hx_Depression
      BHxDepression element = patient.addNewBHxDepression();
      element.setValue( baseLineVd.isDepression() );
      element.setValueDate( visitDate );
      element.setSignedWhen( when );
      element.setSignedWho( who );
    }
    {
      // b_FamHx_Htn
      BFamHxHtn element = patient.addNewBFamHxHtn();
      element.setValue( baseLineVd.isFamHx_Htn() );
      element.setValueDate( visitDate );
      element.setSignedWhen( when );
      element.setSignedWho( who );
    }
    {
      // b_FamHx_Dyslipidemia
      BFamHxDyslipidemia element = patient.addNewBFamHxDyslipidemia();
      element.setValue( baseLineVd.isFamHx_Dyslipid() );
      element.setValueDate( visitDate );
      element.setSignedWhen( when );
      element.setSignedWho( who );
    }
    {
      // b_FamHx_DM
      BFamHxDM element = patient.addNewBFamHxDM();
      element.setValue( baseLineVd.isFamHx_Diabetes() );
      element.setValueDate( visitDate );
      element.setSignedWhen( when );
      element.setSignedWho( who );
    }
    {
      // b_FamHx_Kidney
      BFamHxKidney element = patient.addNewBFamHxKidney();
      element.setValue( baseLineVd.isFamHx_KidneyDis() );
      element.setValueDate( visitDate );
      element.setSignedWhen( when );
      element.setSignedWho( who );
    }
    {
      // b_FamHx_Obesity
      BFamHxObesity element = patient.addNewBFamHxObesity();
      element.setValue( baseLineVd.isFamHx_Obesity() );
      element.setValueDate( visitDate );
      element.setSignedWhen( when );
      element.setSignedWho( who );
    }
    {
      // b_FamHx_CHD
      BFamHxCHD element = patient.addNewBFamHxCHD();
      element.setValue( baseLineVd.isFamHx_CHD() );
      element.setValueDate( visitDate );
      element.setSignedWhen( when );
      element.setSignedWho( who );
    }
    {
      // b_FamHx_StrokeTIA
      BFamHxStrokeTIA element = patient.addNewBFamHxStrokeTIA();
      element.setValue( baseLineVd.isFamHx_Stroke_TIA() );
      element.setValueDate( visitDate );
      element.setSignedWhen( when );
      element.setSignedWho( who );
    }
    {
      // b_FamHx_Depression
      BFamHxDepression element = patient.addNewBFamHxDepression();
      element.setValue( baseLineVd.isFamHx_Depression() );
      element.setValueDate( visitDate );
      element.setSignedWhen( when );
      element.setSignedWho( who );
    }

    if(baseLineVd.getSBP() > 0)
    {
      // cmp_BP
      CmpBP element = patient.addNewCmpBP();
      element.setSystolic( Integer.valueOf(baseLineVd.getSBP()) );
      element.setDiastolic( Integer.valueOf(baseLineVd.getDBP()) );
      element.setAutoOfficeMonitor( toYesNo( baseLineVd.isMonitor() ) );
      element.setValueDate( visitDate ); // FIXME:???? which value date to set??
      element.setSignedWhen( when );
      element.setSignedWho( who );
    }


    if( baseLineVd.getHeight() > 0 )
    {
      // dbl_Height_cm
      double height = baseLineVd.getHeight();
      String heightUnit = baseLineVd.getHeight_unit();
      if ( "inch".equalsIgnoreCase( heightUnit ) )
      {
        // convert inch to cm
        height *= 2.54;
      }

      DblHeightCm element = patient.addNewDblHeightCm();
      element.setValue( height );
      element.setValueDate( visitDate );
      element.setSignedWhen( when );
      element.setSignedWho( who );
    }

    if( baseLineVd.getWeight() > 0 )
    {
      // dbl_Weight_kg
      double weight = baseLineVd.getWeight();
      String weightUnit = baseLineVd.getWeight_unit();
      if ( "lb".equalsIgnoreCase( weightUnit ) )
      {
        // convert lb to kg
        weight *= 0.45359237;
      }

      DblWeightKg element = patient.addNewDblWeightKg();
      element.setValue( weight );
      element.setValueDate( visitDate );
      element.setSignedWhen( when );
      element.setSignedWho( who );
    }

    if( baseLineVd.getWaist() > 0 )
    {
      // dbl_WaistCircumf_cm
      double waist = baseLineVd.getWaist();
      String waistUnit = baseLineVd.getWaist_unit();
      if ( "inch".equalsIgnoreCase( waistUnit ) )
      {
        // convert inch to cm
        waist *= 2.54;
      }

      DblWaistCircumfCm element = patient.addNewDblWaistCircumfCm();
      element.setValue( waist );
      element.setSignedWhen( when );
      element.setSignedWho( who );
      element.setValueDate( visitDate);
    }
	}


    if(baseLineVd.getTC_HDL_LabresultsDate() != null) {
    	final XmlCalendar labWorkSection1Date = new XmlCalendar( dformat2.format( baseLineVd.getTC_HDL_LabresultsDate() ));


    Calendar labDate1 = Calendar.getInstance();
    labDate1.setTime( baseLineVd.getTC_HDL_LabresultsDate() );
    if(labDate1.after(startDate) && labDate1.before(endDate)) {
    if( baseLineVd.getLDL() > 0 )
    {
      // dbl_LDL_mM
      DblLDLMM element = patient.addNewDblLDLMM();
      element.setValue( baseLineVd.getLDL() );
      element.setValueDate( labWorkSection1Date );
      element.setSignedWhen( when );
      element.setSignedWho( who );
    }

    if( baseLineVd.getTC_HDL() > 0 )
    {
      // dbl_TCtoHDL
      DblTCtoHDL element = patient.addNewDblTCtoHDL();
      element.setValue( baseLineVd.getTC_HDL() );
      element.setValueDate( labWorkSection1Date );
      element.setSignedWhen( when );
      element.setSignedWho( who );
    }

    if( baseLineVd.getHDL() > 0 )
    {
      // dbl_HDL_mM
      DblHDLMM element = patient.addNewDblHDLMM();
      element.setValue( baseLineVd.getHDL() );
      element.setValueDate( labWorkSection1Date );
      element.setSignedWhen( when );
      element.setSignedWho( who );
    }

    if( baseLineVd.getTriglycerides() > 0 )
    {
      // dbl_Triglycerides_mM
      DblTriglyceridesMM element = patient.addNewDblTriglyceridesMM();
      element.setValue( baseLineVd.getTriglycerides() );
      element.setValueDate( labWorkSection1Date );
      element.setSignedWhen( when );
      element.setSignedWho( who );
    }
   }
  }
    if(baseLineVd.getA1C_LabresultsDate()!=null) {
	    final XmlCalendar labWorkSection2Date  = new XmlCalendar( dformat2.format( baseLineVd.getA1C_LabresultsDate() ));

	    Calendar labDate2 = Calendar.getInstance();
	    labDate2.setTime( baseLineVd.getA1C_LabresultsDate() );

	    if(labDate2.after(startDate) && labDate2.before(endDate)) {

		    if( baseLineVd.getA1C() > 0 )
		    {
		      // dbl_A1C_fraction
		      DblA1CFraction element = patient.addNewDblA1CFraction();
		      element.setValue( baseLineVd.getA1C()/100 );
		      element.setValueDate( labWorkSection2Date );
		      element.setSignedWhen( when );
		      element.setSignedWho( who );
		    }

		    if( baseLineVd.getFBS() > 0 )
		    {
		      // dbl_FBS_mM
		      DblFBSMM element = patient.addNewDblFBSMM();
		      element.setValue( baseLineVd.getFBS() );
		      element.setValueDate( labWorkSection2Date );
		      element.setSignedWhen( when );
		      element.setSignedWho( who );
		    }
	   }
    }

    if(baseLineVd.getEgfrDate() != null)  {
    	final XmlCalendar labWorkSection3Date = new XmlCalendar( dformat2.format( baseLineVd.getEgfrDate() ));

    //final Calendar labWorkSection3Date = ConvertUtil.dateToCalendar( baseLineVd.getEgfrDate() );   //the UI use EGFR date

    Calendar labDate3 = Calendar.getInstance();
    labDate3.setTime( baseLineVd.getEgfrDate() );
    if(labDate3.after(startDate) && labDate3.before(endDate)) {

    if( baseLineVd.getEgfr() > 0 )
    {
      // int_eGFR_mLPerMin
      IntEGFRMLPerMin element = patient.addNewIntEGFRMLPerMin();
      element.setValue( baseLineVd.getEgfr() );
      element.setValueDate( labWorkSection3Date );
      element.setSignedWhen( when );
      element.setSignedWho( who );
    }

    if( baseLineVd.getAcr() > 0 )
    {
      // dbl_ACR_mgPermmol
      DblACRMgPermmol element = patient.addNewDblACRMgPermmol();
      element.setValue( baseLineVd.getAcr() );
      element.setValueDate( labWorkSection3Date );
      element.setSignedWhen( when );
      element.setSignedWho( who );
    }

    // HsfSiteCode_key
    }
    }
 }

  public void addAllPatientVisit( SitePatient patient, String patientId, String startDate, String endDate )
  {
    List pList = hdao.nullSafeRetrVisitRecord( patientId, startDate, endDate );
    if ( pList == null || pList.size() == 0 )
      return;
    for ( int i = 0; i < pList.size(); i++ )
    {
      Hsfo2Visit vsd = (Hsfo2Visit) pList.get( i );
      addVisitData( patient, vsd );
    }
  }


  public void addAllPatientVisitFinalSection( SitePatient patient, String patientId, String startDateStr, String endDateStr, Calendar startDate, Calendar endDate, Calendar visitDateValue, XmlCalendar visitDate, XmlCalendar when, String who ) 
  {
    List pList = hdao.nullSafeRetrVisitRecord( patientId, startDateStr, endDateStr );
    if ( pList == null || pList.size() == 0 )
      return;
    for ( int i = 0; i < pList.size(); i++ )
    {
      Hsfo2Visit vsd = (Hsfo2Visit) pList.get( i );
      addFinalSection( vsd, patient, patientId, startDate, endDate, visitDateValue, visitDate, when, who  );

    }

    String visitDateStr = String.valueOf(visitDateValue.get(Calendar.YEAR)) + "-" + String.valueOf(visitDateValue.get(Calendar.MONTH)+1)+ "-" + String.valueOf(visitDateValue.get(Calendar.DATE)) ;

    List pList2 = hdao.getLabWorkInDateRange( patientId, startDateStr, endDateStr, visitDateStr);
    if ( pList2 == null || pList2.size() == 0 )
      return;

    //Only get the latest one
    Hsfo2Visit vsd = (Hsfo2Visit) pList2.get(0);
    addFinalSection( vsd, patient, patientId, startDate, endDate, visitDateValue, visitDate, when, who  );
 }

  public void addBaseLineData( SitePatient patient, Hsfo2Patient pd, Hsfo2Visit baseLineVd, final XmlCalendar when,
                               final String who )
  {
    // ///////////////////////////////////////////////
    // FormHsfHmpFlowsheetBaseline min=0, max=1
    // ///////////////////////////////////////////////
    FormHsfHmpFlowsheetBaseline baseLine = patient.addNewFormHsfHmpFlowsheetBaseline();

    final XmlCalendar visitDateCalendar;
    if(baseLineVd.getVisitDate_Id() != null)
    	//labWorkSection1Date = ConvertUtil.dateToCalendar( baseLineVd.getTC_HDL_LabresultsDate() );   //the UI use TC_HDL date
    	visitDateCalendar = new XmlCalendar( dformat2.format( baseLineVd.getVisitDate_Id() ));
    else
    	visitDateCalendar = null;


    // dat_HmpBslVisitDate
    {
      DatHmpBslVisitDate visitDate = baseLine.addNewDatHmpBslVisitDate();
      visitDate.setValue( visitDateCalendar );
      visitDate.setSignedWhen( when );
      visitDate.setSignedWho( who );
    }

    {
      BEthnicWhite bew = baseLine.addNewBEthnicWhite();
      bew.setValue( pd.isEthnic_White() );
      bew.setSignedWhen( when );
      bew.setSignedWho( who );
    }

    {
      BEthnicBlack beb = baseLine.addNewBEthnicBlack();
      beb.setValue( pd.isEthnic_Black() );
      beb.setSignedWhen( when );
      beb.setSignedWho( who );
    }

    BEthnicEIndian beei = baseLine.addNewBEthnicEIndian();
    beei.setValue( pd.isEthnic_EIndian() );
    beei.setSignedWhen( when );
    beei.setSignedWho( who );

    BEthnicPakistani bep = baseLine.addNewBEthnicPakistani();
    bep.setValue( pd.isEthnic_Pakistani() );
    bep.setSignedWhen( when );
    bep.setSignedWho( who );

    BEthnicSriLankan besl = baseLine.addNewBEthnicSriLankan();
    besl.setValue( pd.isEthnic_SriLankan() );
    besl.setSignedWhen( when );
    besl.setSignedWho( who );

    BEthnicBangladeshi bebl = baseLine.addNewBEthnicBangladeshi();
    bebl.setValue( pd.isEthnic_Bangladeshi() );
    bebl.setSignedWhen( when );
    bebl.setSignedWho( who );

    BEthnicChinese bec = baseLine.addNewBEthnicChinese();
    bec.setValue( pd.isEthnic_Chinese() );
    bec.setSignedWhen( when );
    bec.setSignedWho( who );

    BEthnicJapanese bej = baseLine.addNewBEthnicJapanese();
    bej.setValue( pd.isEthnic_Japanese() );
    bej.setSignedWhen( when );
    bej.setSignedWho( who );

    BEthnicKorean bek = baseLine.addNewBEthnicKorean();
    bek.setValue( pd.isEthnic_Korean() );
    bek.setSignedWhen( when );
    bek.setSignedWho( who );

    BEthnicHispanic behp = baseLine.addNewBEthnicHispanic();
    behp.setValue( pd.isEthnic_Hispanic() );
    behp.setSignedWhen( when );
    behp.setSignedWho( who );

    BEthnicFirstNation befn = baseLine.addNewBEthnicFirstNation();
    befn.setValue( pd.isEthnic_FirstNation() );
    befn.setSignedWhen( when );
    befn.setSignedWho( who );

    BEthnicOther beo = baseLine.addNewBEthnicOther();
    beo.setValue( pd.isEthnic_Other() );
    beo.setSignedWhen( when );
    beo.setSignedWho( who );

    BEthnicRefused ber = baseLine.addNewBEthnicRefused();
    ber.setValue( pd.isEthnic_Refused() );
    ber.setSignedWhen( when );
    ber.setSignedWho( who );

    BEthnicUnknown beu = baseLine.addNewBEthnicUnknown();
    beu.setValue( pd.isEthnic_Unknown() );
    beu.setSignedWhen( when );
    beu.setSignedWho( who );

    SelHtnDxAgoAtHmpBsl htnDxAgo = baseLine.addNewSelHtnDxAgoAtHmpBsl();
    String seltimeago = pd.getSel_TimeAgoDx();
    if ( "NA".equalsIgnoreCase( seltimeago ) )
      htnDxAgo.setValue( StringTimeAgoHtnDx.NOT_DIAG );
    else if ( "AtLeast1YrAgo".equalsIgnoreCase( seltimeago ) )
      htnDxAgo.setValue( StringTimeAgoHtnDx.AT_LEAST_1_YR_AGO );
    else if ( "Under1yrAgo".equalsIgnoreCase( seltimeago ) )
      htnDxAgo.setValue( StringTimeAgoHtnDx.UNDER_1_YR_AGO );
    else
      htnDxAgo.setValue( StringTimeAgoHtnDx.X );
    htnDxAgo.setSignedWhen( when );
    htnDxAgo.setSignedWho( who );

    // b_RxAtHmpBsl_diu
    {
      BRxAtHmpBslDiu diu = baseLine.addNewBRxAtHmpBslDiu();
      diu.setValue( baseLineVd.isDiuret_rx() );
      diu.setSignedWhen( when );
      diu.setSignedWho( who );
    }

    {
      BRxAtHmpBslAce brca = baseLine.addNewBRxAtHmpBslAce();
      brca.setValue( baseLineVd.isAce_rx() );
      brca.setSignedWhen( when );
      brca.setSignedWho( who );
    }

    {
      BRxAtHmpBslArb brcab = baseLine.addNewBRxAtHmpBslArb();
      brcab.setValue( baseLineVd.isArecept_rx() );
      brcab.setSignedWhen( when );
      brcab.setSignedWho( who );
    }

    {
      BRxAtHmpBslBb brcbb = baseLine.addNewBRxAtHmpBslBb();
      brcbb.setValue( baseLineVd.isBeta_rx() );
      brcbb.setSignedWhen( when );
      brcbb.setSignedWho( who );
    }

    {
      BRxAtHmpBslCcb brcc = baseLine.addNewBRxAtHmpBslCcb();
      brcc.setValue( baseLineVd.isCalc_rx() );
      brcc.setSignedWhen( when );
      brcc.setSignedWho( who );
    }

    {
      BRxAtHmpBslOthhtn brcon = baseLine.addNewBRxAtHmpBslOthhtn();
      brcon.setValue( baseLineVd.isAnti_rx() );
      brcon.setSignedWhen( when );
      brcon.setSignedWho( who );
    }

    {
      BRxAtHmpBslSta brcs = baseLine.addNewBRxAtHmpBslSta();
      brcs.setValue( baseLineVd.isStatin_rx() );
      brcs.setSignedWhen( when );
      brcs.setSignedWho( who );
    }

    {
      BRxAtHmpBslOthlip brcop = baseLine.addNewBRxAtHmpBslOthlip();
      brcop.setValue( baseLineVd.isLipid_rx() );
      brcop.setSignedWhen( when );
      brcop.setSignedWho( who );
    }

    {
      BRxAtHmpBslOha brcoa = baseLine.addNewBRxAtHmpBslOha();
      brcoa.setValue( baseLineVd.isHypo_rx() );
      brcoa.setSignedWhen( when );
      brcoa.setSignedWho( who );
    }

    {
      BRxAtHmpBslIns brci = baseLine.addNewBRxAtHmpBslIns();
      brci.setValue( baseLineVd.isInsul_rx() );
      brci.setSignedWhen( when );
      brci.setSignedWho( who );
    }

    // b_RxAtHmpBsl_asa
    {
      BRxAtHmpBslAsa asa = baseLine.addNewBRxAtHmpBslAsa();
      asa.setValue( baseLineVd.isASA_rx() );
      asa.setSignedWhen( when );
      asa.setSignedWho( who );
    }
  }

  public static StringYesNo.Enum toYesNo( Boolean condition )
  {
	  if(condition == null)
		  return StringYesNo.NO;
	  else
		  return condition ? StringYesNo.YES : StringYesNo.NO;
  }

  public static StringFrequency.Enum getFrequency( String value )
  {
    if ( "Always".equalsIgnoreCase( value ) )
      return StringFrequency.ALWAYS;
    if ( "Often".equalsIgnoreCase( value ) )
      return StringFrequency.OFTEN;
    if ( "Sometimes".equalsIgnoreCase( value ) )
      return StringFrequency.SOMETIMES;
    if ( "Never".equalsIgnoreCase( value ) )
      return StringFrequency.NEVER;
    else
      return StringFrequency.X;
  }

  public static StringRxToday.Enum getRxToday( String value )
  {
    if ( "Same".equalsIgnoreCase( value ) )
      return StringRxToday.SAME;
    if ( "Increase".equalsIgnoreCase( value ) )
      return StringRxToday.INCREASE;
    if ( "Decrease".equalsIgnoreCase( value ) )
      return StringRxToday.DECREASE;
    if ( "Stop".equalsIgnoreCase( value ) )
      return StringRxToday.STOP;
    if ( "Start".equalsIgnoreCase( value ) )
      return StringRxToday.START;
    if ( "InClassSwitch".equalsIgnoreCase( value ) )
      return StringRxToday.IN_CLASS_SWITCH;
    else
      // if ("null".equalsIgnoreCase(value))
      return StringRxToday.X;
  }

  protected static boolean isVisitDataComplete( Hsfo2Visit vsd )
  {
//    if( vsd.getChange_importance() == 0 )
//      return false;
//    if( vsd.getChange_confidence() == 0 )
//      return false;

    return true;
  }

  /**
   * this is for follow up: form_HsfHmpFlowsheet
   * only add this flowsheet when all the required field inside were set data
   * maxOccur=unbounded, minOccurs=0
   * @param patient
   * @param vsd
   */
  public void addVisitData( SitePatient patient, Hsfo2Visit vsd )
  {
    if( !isVisitDataComplete( vsd ) )
      return;
    String visitDate = dformat2.format( vsd.getVisitDate_Id() );

    String signT = dformat1.format( vsd.getFormEdited() );
   // String signT2 = dformat2.format( vsd.getFormEdited() );
    XmlCalendar when = new XmlCalendar( signT );
   // XmlCalendar when2 = new XmlCalendar( signT2 );
    String who = getProviderName( vsd.getProvider_no());

    FormHsfHmpFlowsheet flowsheet = patient.addNewFormHsfHmpFlowsheet();

    flowsheet.setVisitDateKey( new XmlCalendar( visitDate ) );

    // sel_Risk_Weight
    SelRiskWeight weight = flowsheet.addNewSelRiskWeight();
    weight.setValue( toYesNo( vsd.isRisk_weight() ) );
    weight.setSignedWhen( when );
    weight.setSignedWho( who );

    SelRiskPhysActivity brpa = flowsheet.addNewSelRiskPhysActivity();
    brpa.setValue( toYesNo( vsd.isRisk_activity() ) );
    brpa.setSignedWhen( when );
    brpa.setSignedWho( who );

    SelRiskDiet brdt = flowsheet.addNewSelRiskDiet();
    brdt.setValue( toYesNo( vsd.isRisk_diet() ) );
    brdt.setSignedWhen( when );
    brdt.setSignedWho( who );

    SelRiskSmoking brsk = flowsheet.addNewSelRiskSmoking();
    brsk.setValue( toYesNo( vsd.isRisk_smoking() ) );
    brsk.setSignedWhen( when );
    brsk.setSignedWho( who );

    SelRiskAlcohol brah = flowsheet.addNewSelRiskAlcohol();
    brah.setValue( toYesNo( vsd.isRisk_alcohol() ) );
    brah.setSignedWhen( when );
    brah.setSignedWho( who );

    SelRiskStress brst = flowsheet.addNewSelRiskStress();
    brst.setValue( toYesNo( vsd.isRisk_stress() ) );
    brst.setSignedWhen( when );
    brst.setSignedWho( who );

    // goals
    {
      final String lifeGoal = vsd.getLifeGoal();

      {
        BGoalWeight goalWeight = flowsheet.addNewBGoalWeight();
        goalWeight.setValue( "Goal_weight".equalsIgnoreCase( lifeGoal ) );
        goalWeight.setSignedWhen( when );
        goalWeight.setSignedWho( who );
      }

      {
        BGoalPhysActivity goalPhyActivity = flowsheet.addNewBGoalPhysActivity();
        goalPhyActivity.setValue( "Goal_activity".equalsIgnoreCase( lifeGoal ) );
        goalPhyActivity.setSignedWhen( when );
        goalPhyActivity.setSignedWho( who );
      }

      {
        BGoalSalt goalSalt = flowsheet.addNewBGoalSalt();
        goalSalt.setValue( "Goal_dietSalt".equalsIgnoreCase( lifeGoal ) );
        goalSalt.setSignedWhen( when );
        goalSalt.setSignedWho( who );
      }

      {
        BGoalDashDiet goalDashDiet = flowsheet.addNewBGoalDashDiet();
        goalDashDiet.setValue( "Goal_dietDash".equalsIgnoreCase( lifeGoal ) );
        goalDashDiet.setSignedWhen( when );
        goalDashDiet.setSignedWho( who );
      }

      {
        BGoalSmoking goalSmoking = flowsheet.addNewBGoalSmoking();
        goalSmoking.setValue( "Goal_smoking".equalsIgnoreCase( lifeGoal ) );
        goalSmoking.setSignedWhen( when );
        goalSmoking.setSignedWho( who );
      }

      {
        BGoalAlcohol goalAlcohol = flowsheet.addNewBGoalAlcohol();
        goalAlcohol.setValue( "Goal_alcohol".equalsIgnoreCase( lifeGoal ) );
        goalAlcohol.setSignedWhen( when );
        goalAlcohol.setSignedWho( who );
      }

      {
        BGoalStress goalStress = flowsheet.addNewBGoalStress();
        goalStress.setValue( "Goal_stress".equalsIgnoreCase( lifeGoal ) );
        goalStress.setSignedWhen( when );
        goalStress.setSignedWho( who );
      }
    }

    // Current assessment of CV risk factors
    {
      IntPhysActivityMinPerWk element = flowsheet.addNewIntPhysActivityMinPerWk();
      element.setValue( vsd.getAssessActivity() );
      element.setSignedWhen( when );
      element.setSignedWho( who );
    }
    {
      // int_Smoking_cigsPerDay
      IntSmokingCigsPerDay element = flowsheet.addNewIntSmokingCigsPerDay();
      element.setValue( vsd.getAssessSmoking() );
      element.setSignedWhen( when );
      element.setSignedWho( who );
    }
    {
      // int_Alcohol_drinksPerWk
      IntAlcoholDrinksPerWk element = flowsheet.addNewIntAlcoholDrinksPerWk();
      element.setValue( vsd.getAssessAlcohol() );
      element.setSignedWhen( when );
      element.setSignedWho( who );
    }
    {
      // sel_HighSalt
      SelHighSalt element = flowsheet.addNewSelHighSalt();
      element.setValue( getFrequency( vsd.getSel_HighSaltFood() ) );
      element.setSignedWhen( when );
      element.setSignedWho( who );
    }
    {
      // sel_DashDiet
      SelDashDiet element = flowsheet.addNewSelDashDiet();
      element.setValue( getFrequency( vsd.getSel_DashDiet() ) );
      element.setSignedWhen( when );
      element.setSignedWho( who );
    }
    {
      // sel_Stressed
      SelStressed element = flowsheet.addNewSelStressed();
      element.setValue( getFrequency( vsd.getSel_Stressed() ) );
      element.setSignedWhen( when );
      element.setSignedWho( who );
    }

    {
      // Patient view of selected lifestyle goal
      // sel_GoalView
      final String patientView = vsd.getPtView();
      StringGoalView.Enum goalView;
      if ( "Uninterested".equalsIgnoreCase( patientView ) )
        goalView = StringGoalView.UNINTERESTED;
      if ( "Thinking".equalsIgnoreCase( patientView ) )
        goalView = StringGoalView.THINKING;
      if ( "Deciding".equalsIgnoreCase( patientView ) )
        goalView = StringGoalView.DECIDING;
      if ( "TakingAction".equalsIgnoreCase( patientView ) )
        goalView = StringGoalView.TAKING_ACTION;
      if ( "Maintaining".equalsIgnoreCase( patientView ) )
        goalView = StringGoalView.MAINTAINING;
      if ( "Relapsing".equalsIgnoreCase( patientView ) )
        goalView = StringGoalView.RELAPSING;
      else
        // if ("null".equalsIgnoreCase(patientView))
        goalView = StringGoalView.X;

      SelGoalView element = flowsheet.addNewSelGoalView();
      element.setValue( goalView );
      element.setSignedWhen( when );
      element.setSignedWho( who );
    }

    // Current assessment of selected lifestyle goal
    {
      // int_GoalImportance
      IntGoalImportance element = flowsheet.addNewIntGoalImportance();
      element.setValue( vsd.getChange_importance() == 0 ? "" : vsd.getChange_importance() );
      element.setSignedWhen( when );
      element.setSignedWho( who );
    }
    {
      // int_GoalConfidence
      IntGoalConfidence element = flowsheet.addNewIntGoalConfidence();
      element.setValue( vsd.getChange_confidence() == 0 ? "" : vsd.getChange_confidence() );
      element.setSignedWhen( when );
      element.setSignedWho( who );
    }

    // Medications
    {
      {
        // b_RxSideEffects_diu
        BRxSideEffectsDiu element = flowsheet.addNewBRxSideEffectsDiu();
        element.setValue( vsd.isDiuret_SideEffects() );
        element.setSignedWhen( when );
        element.setSignedWho( who );
      }
      {
        // sel_RxToday_diu
        SelRxTodayDiu element = flowsheet.addNewSelRxTodayDiu();
        element.setValue( getRxToday( vsd.getDiuret_RxDecToday() ) );
        element.setSignedWhen( when );
        element.setSignedWho( who );
      }
    }
    {
      {
        // b_RxSideEffects_ace
        BRxSideEffectsAce element = flowsheet.addNewBRxSideEffectsAce();
        element.setValue( vsd.isAce_SideEffects() );
        element.setSignedWhen( when );
        element.setSignedWho( who );
      }
      {
        // sel_RxToday_ace
        SelRxTodayAce element = flowsheet.addNewSelRxTodayAce();
        element.setValue( getRxToday( vsd.getAce_RxDecToday() ) );
        element.setSignedWhen( when );
        element.setSignedWho( who );
      }
    }
    {
      {
        // b_RxSideEffects_arb
        BRxSideEffectsArb element = flowsheet.addNewBRxSideEffectsArb();
        element.setValue( vsd.isArecept_rx() );
        element.setSignedWhen( when );
        element.setSignedWho( who );
      }
      {
        // sel_RxToday_arb
        SelRxTodayArb element = flowsheet.addNewSelRxTodayArb();
        element.setValue( getRxToday( vsd.getArecept_RxDecToday() ) );
        element.setSignedWhen( when );
        element.setSignedWho( who );
      }
    }
    {
      {
        // b_RxSideEffects_bb
        BRxSideEffectsBb element = flowsheet.addNewBRxSideEffectsBb();
        element.setValue( vsd.isBeta_rx() );
        element.setSignedWhen( when );
        element.setSignedWho( who );
      }
      {
        // sel_RxToday_bb
        SelRxTodayBb element = flowsheet.addNewSelRxTodayBb();
        element.setValue( getRxToday( vsd.getBeta_RxDecToday() ) );
        element.setSignedWhen( when );
        element.setSignedWho( who );
      }
    }
    {
      {
        // b_RxSideEffects_ccb
        BRxSideEffectsCcb element = flowsheet.addNewBRxSideEffectsCcb();
        element.setValue( vsd.isCalc_rx() );
        element.setSignedWhen( when );
        element.setSignedWho( who );
      }
      {
        // sel_RxToday_ccb
        SelRxTodayCcb element = flowsheet.addNewSelRxTodayCcb();
        element.setValue( getRxToday( vsd.getCalc_RxDecToday() ) );
        element.setSignedWhen( when );
        element.setSignedWho( who );
      }
    }
    {
      {
        // b_RxSideEffects_othhtn
        BRxSideEffectsOthhtn element = flowsheet.addNewBRxSideEffectsOthhtn();
        element.setValue( vsd.isAnti_rx() );
        element.setSignedWhen( when );
        element.setSignedWho( who );
      }
      {
        // sel_RxToday_othhtn
        SelRxTodayOthhtn element = flowsheet.addNewSelRxTodayOthhtn();
        element.setValue( getRxToday( vsd.getAnti_RxDecToday() ) );
        element.setSignedWhen( when );
        element.setSignedWho( who );
      }
    }
    {
      {
        // b_RxSideEffects_sta
        BRxSideEffectsSta element = flowsheet.addNewBRxSideEffectsSta();
        element.setValue( vsd.isStatin_rx() );
        element.setSignedWhen( when );
        element.setSignedWho( who );
      }
      {
        // sel_RxToday_sta
        SelRxTodaySta element = flowsheet.addNewSelRxTodaySta();
        element.setValue( getRxToday( vsd.getStatin_RxDecToday() ) );
        element.setSignedWhen( when );
        element.setSignedWho( who );
      }
    }
    {
      {
        // b_RxSideEffects_othlip
        BRxSideEffectsOthlip element = flowsheet.addNewBRxSideEffectsOthlip();
        element.setValue( vsd.isLipid_rx() );
        element.setSignedWhen( when );
        element.setSignedWho( who );
      }
      {
        // sel_RxToday_othlip
        SelRxTodayOthlip element = flowsheet.addNewSelRxTodayOthlip();
        element.setValue( getRxToday( vsd.getLipid_RxDecToday() ) );
        element.setSignedWhen( when );
        element.setSignedWho( who );
      }
    }
    {
      {
        // b_RxSideEffects_oha
        BRxSideEffectsOha element = flowsheet.addNewBRxSideEffectsOha();
        element.setValue( vsd.isHypo_rx() );
        element.setSignedWhen( when );
        element.setSignedWho( who );
      }
      {
        // sel_RxToday_oha
        SelRxTodayOha element = flowsheet.addNewSelRxTodayOha();
        element.setValue( getRxToday( vsd.getHypo_RxDecToday() ) );
        element.setSignedWhen( when );
        element.setSignedWho( who );
      }
    }

    {
      {
        // b_RxSideEffects_ins
        BRxSideEffectsIns element = flowsheet.addNewBRxSideEffectsIns();
        element.setValue( vsd.isInsul_rx() );
        element.setSignedWhen( when );
        element.setSignedWho( who );
      }
      {
        // sel_RxToday_ins
        SelRxTodayIns element = flowsheet.addNewSelRxTodayIns();
        element.setValue( getRxToday( vsd.getInsul_RxDecToday() ) );
        element.setSignedWhen( when );
        element.setSignedWho( who );
      }
    }
    {
      {
        // b_RxSideEffects_asa
        BRxSideEffectsAsa element = flowsheet.addNewBRxSideEffectsAsa();
        element.setValue( vsd.isASA_rx() );
        element.setSignedWhen( when );
        element.setSignedWho( who );
      }
      {
        // sel_RxToday_asa
        SelRxTodayAsa element = flowsheet.addNewSelRxTodayAsa();
        element.setValue( getRxToday( vsd.getASA_RxDecToday() ) );
        element.setSignedWhen( when );
        element.setSignedWho( who );
      }
    }

    //
    {
      // int_MissedMeds_perWk
      IntMissedMedsPerWk element = flowsheet.addNewIntMissedMedsPerWk();
      element.setValue( vsd.getOften_miss() );
      element.setSignedWhen( when );
      element.setSignedWho( who );
    }
    {
      // sel_HerbalMeds
      SelHerbalMeds element = flowsheet.addNewSelHerbalMeds();
      element.setValue( toYesNo( "Yes".equalsIgnoreCase( vsd.getHerbal() ) ) );
      element.setSignedWhen( when );
      element.setSignedWho( who );
    }
    {
      // sel_AdequateDrugCoverage
      SelAdequateDrugCoverage element = flowsheet.addNewSelAdequateDrugCoverage();
      element.setValue( toYesNo( "Yes".equalsIgnoreCase( vsd.getDrugcoverage() ) ) );
      element.setSignedWhen( when );
      element.setSignedWho( who );
    }

    {
      // cmp_FollowUp: Next visit in
      int nextVisitInMonths = vsd.getNextVisitInMonths();
      int nextVisitInWeeks = vsd.getNextVisitInWeeks();
      int interval;
      StringFollowUpIntervalUnits.Enum units;
      if ( nextVisitInMonths != 0 )
      {
        interval = nextVisitInMonths;
        units = StringFollowUpIntervalUnits.MO;
      }
      else if ( nextVisitInWeeks != 0 )
      {
        interval = nextVisitInWeeks;
        units = StringFollowUpIntervalUnits.WK;
      }
      else
      {
        interval = 0;
        units = StringFollowUpIntervalUnits.X;
      }

      CmpFollowUp element = flowsheet.addNewCmpFollowUp();
      element.setInterval( interval == 0 ? "" : interval );
      element.setUnits( units );
      element.setSignedWhen( when );
      element.setSignedWho( who );
    }

    // Tools Provided
    {
      // b_HsfTool_TPO
      BHsfToolTPO element = flowsheet.addNewBHsfToolTPO();
      element.setValue( vsd.isPressureOff() );
      element.setSignedWhen( when );
      element.setSignedWho( who );
    }
    {
      // b_HsfTool_BPAP
      BHsfToolBPAP element = flowsheet.addNewBHsfToolBPAP();
      element.setValue( vsd.isBpactionplan() );
      element.setSignedWhen( when );
      element.setSignedWho( who );
    }
    {
      // b_BpMon_Home
      BBpMonHome element = flowsheet.addNewBBpMonHome();
      element.setValue( vsd.isHome() );
      element.setSignedWhen( when );
      element.setSignedWho( who );
    }
    {
      // b_BpMon_Ambul
      BBpMonAmbul element = flowsheet.addNewBBpMonAmbul();
      element.setValue( vsd.isABPM() );
      element.setSignedWhen( when );
      element.setSignedWho( who );
    }
    {
      // b_Refer_HCP
      BReferHCP element = flowsheet.addNewBReferHCP();
      element.setValue( vsd.isProRefer() );
      element.setSignedWhen( when );
      element.setSignedWho( who );
    }
    {
      // b_Refer_CommunRes
      BReferCommunRes element = flowsheet.addNewBReferCommunRes();
      element.setValue( vsd.isCommunityRes() );
      element.setSignedWhen( when );
      element.setSignedWho( who );
    }
    // end of form_HsfHmpFlowsheet
  }

  /***********************
   * public void oldCode1( VisitData vsd ) { String co = vsd.getDrugcoverage(); if (co != null) {
   * SelAdequateDrugCoverage sadc = spvs.addNewSelAdequateDrugCoverage();
   *
   * if ("yes".equalsIgnoreCase(co)) sadc.setValue(StringYesNo.YES); else if ("no".equalsIgnoreCase(co))
   * sadc.setValue(StringYesNo.NO); else if ("null".equalsIgnoreCase(co)) sadc.setValue(StringYesNo.X);
   * sadc.setSignedWhen(when); sadc.setSignedWho(who); }
   *
   * String hdt = vsd.getHtnDxType(); if (hdt != null) { SelHtnDxType shdt = spvs.addNewSelHtnDxType();
   *
   * if ("PrimaryHtn".equalsIgnoreCase(hdt)) shdt.setValue(StringHtnDxType.PRIMARY_HTN); else if
   * ("ElevatedBpReadings".equalsIgnoreCase(hdt)) shdt.setValue(StringHtnDxType.ELEVATED_BP_READINGS); else if
   * ("null".equalsIgnoreCase(hdt)) shdt.setValue(StringHtnDxType.X); shdt.setSignedWhen(when); shdt.setSignedWho(who);
   * } BHxDyslipidemia bhd = spvs.addNewBHxDyslipidemia(); bhd.setValue(vsd.isDyslipid()); bhd.setSignedWhen(when);
   * bhd.setSignedWho(who);
   *
   * BHxDM bhdm = spvs.addNewBHxDM(); bhdm.setValue(vsd.isDiabetes()); bhdm.setSignedWhen(when); bhdm.setSignedWho(who);
   *
   * BHxKidney bhk = spvs.addNewBHxKidney(); bhk.setValue(vsd.isKidneyDis()); bhk.setSignedWhen(when);
   * bhk.setSignedWho(who);
   *
   * BHxObesity bho = spvs.addNewBHxObesity(); bho.setValue(vsd.isObesity()); bho.setSignedWhen(when);
   * bho.setSignedWho(who);
   *
   * BHxCHD bhchd = spvs.addNewBHxCHD(); bhchd.setValue(vsd.isCHD()); bhchd.setSignedWhen(when);
   * bhchd.setSignedWho(who);
   *
   * BHxStrokeTIA bhst = spvs.addNewBHxStrokeTIA(); bhst.setValue(vsd.isStroke_TIA()); bhst.setSignedWhen(when);
   * bhst.setSignedWho(who);
   *
   * BFamHxHtn bfhh = spvs.addNewBFamHxHtn(); bfhh.setValue(vsd.isFamHx_Htn()); bfhh.setSignedWhen(when);
   * bfhh.setSignedWho(who);
   *
   * BFamHxDyslipidemia bfhdp = spvs.addNewBFamHxDyslipidemia(); bfhdp.setValue(vsd.isFamHx_Dyslipid());
   * bfhdp.setSignedWhen(when); bfhdp.setSignedWho(who);
   *
   * BFamHxDM bfhdm = spvs.addNewBFamHxDM(); bfhdm.setValue(vsd.isFamHx_Diabetes()); bfhdm.setSignedWhen(when);
   * bfhdm.setSignedWho(who);
   *
   * BFamHxKidney bfhk = spvs.addNewBFamHxKidney(); bfhk.setValue(vsd.isFamHx_KidneyDis()); bfhk.setSignedWhen(when);
   * bfhk.setSignedWho(who);
   *
   * BFamHxObesity bfho = spvs.addNewBFamHxObesity(); bfho.setValue(vsd.isFamHx_Obesity()); bfho.setSignedWhen(when);
   * bfho.setSignedWho(who);
   *
   * BFamHxCHD bfhchd = spvs.addNewBFamHxCHD(); bfhchd.setValue(vsd.isFamHx_CHD()); bfhchd.setSignedWhen(when);
   * bfhchd.setSignedWho(who);
   *
   * BFamHxStrokeTIA bfhstia = spvs.addNewBFamHxStrokeTIA(); bfhstia.setValue(vsd.isFamHx_Stroke_TIA());
   * bfhstia.setSignedWhen(when); bfhstia.setSignedWho(who);
   *
   * int tempi = vsd.getSBP(); if (tempi != Integer.MIN_VALUE) { IntSBPMmHg isbgmh = spvs.addNewIntSBPMmHg(); if (tempi
   * != 0) isbgmh.setValue(tempi); isbgmh.setSignedWhen(when); isbgmh.setSignedWho(who); }
   *
   * tempi = vsd.getDBP(); if (tempi != Integer.MIN_VALUE) { IntDBPMmHg idbgmh = spvs.addNewIntDBPMmHg();
   * idbgmh.setValue(vsd.getDBP()); idbgmh.setSignedWhen(when); idbgmh.setSignedWho(who); }
   *
   * String used = vsd.getBptru_used(); if (used != null) { SelBpTru sbt = spvs.addNewSelBpTru();
   *
   * if ("yes".equalsIgnoreCase(used)) sbt.setValue(StringYesNo.YES); else if ("no".equalsIgnoreCase(used))
   * sbt.setValue(StringYesNo.NO); else if ("null".equalsIgnoreCase(used)) sbt.setValue(StringYesNo.X);
   * sbt.setSignedWhen(when); sbt.setSignedWho(who); }
   *
   * tempi = vsd.getSBP_goal(); if (tempi != Integer.MIN_VALUE) { IntSbpGoalMmHg isgmh = spvs.addNewIntSbpGoalMmHg();
   *
   * if (tempi != 0) isgmh.setValue(tempi); isgmh.setSignedWhen(when); isgmh.setSignedWho(who); }
   *
   * tempi = vsd.getDBP_goal(); if (tempi != Integer.MIN_VALUE) { IntDbpGoalMmHg idgmh = spvs.addNewIntDbpGoalMmHg();
   * idgmh.setValue(vsd.getDBP_goal()); idgmh.setSignedWhen(when); idgmh.setSignedWho(who); }
   *
   * double tempd = vsd.getWeight(); if (tempd != Double.MIN_VALUE) { DblWeight dw = spvs.addNewDblWeight(); if
   * (vsd.getWeight() != 0) dw.setValue(vsd.getWeight()); dw.setSignedWhen(when); dw.setSignedWho(who);
   *
   * String wunit = vsd.getWeight_unit(); if (wunit != null) { SelWeightUnit swu = spvs.addNewSelWeightUnit();
   *
   * if ("kg".equalsIgnoreCase(wunit)) swu.setValue(StringMassUnit.KG); else if ("lb".equalsIgnoreCase(wunit))
   * swu.setValue(StringMassUnit.LBS); else if ("null".equalsIgnoreCase(wunit)) swu.setValue(StringMassUnit.X);
   * swu.setSignedWhen(when); swu.setSignedWho(who); } }
   *
   * tempd = vsd.getWaist(); if (tempd != Double.MIN_VALUE) { DblWaistCircumf dwc = spvs.addNewDblWaistCircumf(); if
   * (vsd.getWaist() != 0) dwc.setValue(vsd.getWaist()); dwc.setSignedWhen(when); dwc.setSignedWho(who);
   *
   * String waistu = vsd.getWaist_unit(); if (waistu != null) { SelWaistCircumfUnit swcu =
   * spvs.addNewSelWaistCircumfUnit();
   *
   * if ("cm".equalsIgnoreCase(waistu)) swcu.setValue(StringLengthUnit.CM); else if ("inch".equalsIgnoreCase(waistu))
   * swcu.setValue(StringLengthUnit.INCHES); else if ("null".equalsIgnoreCase(waistu))
   * swcu.setValue(StringLengthUnit.X); swcu.setSignedWhen(when); swcu.setSignedWho(who); } }
   *
   * tempd = vsd.getTC_HDL(); if (tempd != Double.MIN_VALUE) { DblTCtoHDL dtchdl = spvs.addNewDblTCtoHDL(); if
   * (vsd.getTC_HDL() != 0) dtchdl.setValue(vsd.getTC_HDL()); dtchdl.setSignedWhen(when); dtchdl.setSignedWho(who); Date
   * hdldate = vsd.getTC_HDL_LabresultsDate(); if (hdldate == null) dtchdl.setValueDate(when2); else
   * dtchdl.setValueDate(new XmlCalendar(dformat2.format(hdldate))); }
   *
   * tempd = vsd.getLDL(); if (tempd != Double.MIN_VALUE) { DblLDLMM dldlmm = spvs.addNewDblLDLMM(); if (vsd.getLDL() !=
   * 0) dldlmm.setValue(vsd.getLDL()); dldlmm.setSignedWhen(when); dldlmm.setSignedWho(who); Date ldldate =
   * vsd.getLDL_LabresultsDate(); if (ldldate == null) dldlmm.setValueDate(when2); else dldlmm.setValueDate(new
   * XmlCalendar(dformat2.format(ldldate))); }
   *
   * tempd = vsd.getHDL(); if (tempd != Double.MIN_VALUE) { DblHDLMM dhdlmm = spvs.addNewDblHDLMM(); if (vsd.getHDL() !=
   * 0) dhdlmm.setValue(vsd.getHDL()); dhdlmm.setSignedWhen(when); dhdlmm.setSignedWho(who); Date ldldate =
   * vsd.getHDL_LabresultsDate(); if (ldldate == null) dhdlmm.setValueDate(when2); else dhdlmm.setValueDate(new
   * XmlCalendar(dformat2.format(ldldate))); }
   *
   * tempd = vsd.getA1C(); if (tempd != Double.MIN_VALUE) { DblA1CPercent dacp = spvs.addNewDblA1CPercent(); if
   * (vsd.getA1C() != 0) dacp.setValue(vsd.getA1C()); dacp.setSignedWhen(when); dacp.setSignedWho(who); Date ldldate =
   * vsd.getA1C_LabresultsDate(); if (ldldate == null) dacp.setValueDate(when2); else dacp.setValueDate(new
   * XmlCalendar(dformat2.format(ldldate))); }
   *
   * String life = vsd.getLifeGoal(); boolean pa = false, dietdash = false, dietsalt = false, smoking = false, alcohol =
   * false, stress = false; if ("Goal_activity".equalsIgnoreCase(life)) pa = true; else if
   * ("Goal_dietDash".equalsIgnoreCase(life)) dietdash = true; else if ("Goal_dietSalt".equalsIgnoreCase(life)) dietsalt
   * = true; else if ("Goal_smoking".equalsIgnoreCase(life)) smoking = true; else if
   * ("Goal_alcohol".equalsIgnoreCase(life)) alcohol = true; else if ("Goal_stress".equals(life)) stress = true;
   *
   * BGoalPhysActivity bgpa = spvs.addNewBGoalPhysActivity(); bgpa.setValue(pa); bgpa.setSignedWhen(when);
   * bgpa.setSignedWho(who);
   *
   * BGoalDASHDiet bgdash = spvs.addNewBGoalDASHDiet(); bgdash.setValue(dietdash); bgdash.setSignedWhen(when);
   * bgdash.setSignedWho(who);
   *
   * BGoalSalt bgs = spvs.addNewBGoalSalt(); bgs.setValue(dietsalt); bgs.setSignedWhen(when); bgs.setSignedWho(who);
   *
   * BGoalSmoking bgsm = spvs.addNewBGoalSmoking(); bgsm.setValue(smoking); bgsm.setSignedWhen(when);
   * bgsm.setSignedWho(who);
   *
   * BGoalAlcohol bga = spvs.addNewBGoalAlcohol(); bga.setValue(alcohol); bga.setSignedWhen(when);
   * bga.setSignedWho(who);
   *
   * BGoalStress bgst = spvs.addNewBGoalStress(); bgst.setValue(stress); bgst.setSignedWhen(when);
   * bgst.setSignedWho(who);
   *
   * String pView = vsd.getPtView(); if (pView != null) { SelPatientView spv = spvs.addNewSelPatientView();
   *
   * if ("Uninterested".equalsIgnoreCase(pView)) spv.setValue(StringPtChangeState.UNINTERESTED); else if
   * ("Thinking".equalsIgnoreCase(pView)) spv.setValue(StringPtChangeState.THINKING); else if
   * ("Deciding".equalsIgnoreCase(pView)) spv.setValue(StringPtChangeState.DECIDING); else if
   * ("TakingAction".equalsIgnoreCase(pView)) spv.setValue(StringPtChangeState.TAKING_ACTION); else if
   * ("Maintaining".equalsIgnoreCase(pView)) spv.setValue(StringPtChangeState.MAINTAINING); else if
   * ("Relapsing".equalsIgnoreCase(pView)) spv.setValue(StringPtChangeState.RELAPSING); else if
   * ("null".equalsIgnoreCase(pView)) spv.setValue(StringPtChangeState.X); spv.setSignedWhen(when);
   * spv.setSignedWho(who); }
   *
   * tempi = vsd.getChange_importance(); if (tempi != Integer.MIN_VALUE) { IntGoalImportance igi =
   * spvs.addNewIntGoalImportance(); if (vsd.getChange_importance() != 0) igi.setValue(vsd.getChange_importance());
   * igi.setSignedWhen(when); igi.setSignedWho(who); }
   *
   * tempi = vsd.getChange_confidence(); if (tempi != Integer.MIN_VALUE) { IntGoalConfidence igc =
   * spvs.addNewIntGoalConfidence(); if (vsd.getChange_confidence() != 0) igc.setValue(vsd.getChange_confidence());
   * igc.setSignedWhen(when); igc.setSignedWho(who); }
   *
   * tempi = vsd.getExercise_minPerWk(); if (tempi != Integer.MIN_VALUE) { IntExerciseMinPerWk ieampw =
   * spvs.addNewIntExerciseMinPerWk(); ieampw.setValue(vsd.getExercise_minPerWk()); ieampw.setSignedWhen(when);
   * ieampw.setSignedWho(who); }
   *
   * tempi = vsd.getSmoking_cigsPerDay(); if (tempi != Integer.MIN_VALUE) { IntSmokingCigsPerDay iscpd =
   * spvs.addNewIntSmokingCigsPerDay(); iscpd.setValue(vsd.getSmoking_cigsPerDay()); iscpd.setSignedWhen(when);
   * iscpd.setSignedWho(who); }
   *
   * tempi = vsd.getAlcohol_drinksPerWk(); if (tempi != Integer.MIN_VALUE) { IntAlcoholDrinksPerWk iadpwk =
   * spvs.addNewIntAlcoholDrinksPerWk(); iadpwk.setValue(vsd.getAlcohol_drinksPerWk()); iadpwk.setSignedWhen(when);
   * iadpwk.setSignedWho(who); }
   *
   * String ddiet = vsd.getSel_DashDiet(); if (ddiet != null) { SelDASHdiet sdash = spvs.addNewSelDASHdiet();
   *
   * if ("Always".equalsIgnoreCase(ddiet)) sdash.setValue(StringFrequency.ALWAYS); else if
   * ("Often".equalsIgnoreCase(ddiet)) sdash.setValue(StringFrequency.OFTEN); else if
   * ("Sometimes".equalsIgnoreCase(ddiet)) sdash.setValue(StringFrequency.SOMETIMES); else if
   * ("Never".equalsIgnoreCase(ddiet)) sdash.setValue(StringFrequency.NEVER); else sdash.setValue(StringFrequency.X);
   * sdash.setSignedWhen(when); sdash.setSignedWho(who); }
   *
   * String ssalt = vsd.getSel_HighSaltFood(); if (ssalt != null) { SelHighSalt shs = spvs.addNewSelHighSalt();
   *
   * if ("Always".equalsIgnoreCase(ssalt)) shs.setValue(StringFrequency.ALWAYS); else if
   * ("Often".equalsIgnoreCase(ssalt)) shs.setValue(StringFrequency.OFTEN); else if
   * ("Sometimes".equalsIgnoreCase(ssalt)) shs.setValue(StringFrequency.SOMETIMES); else if
   * ("Never".equalsIgnoreCase(ssalt)) shs.setValue(StringFrequency.NEVER); else shs.setValue(StringFrequency.X);
   * shs.setSignedWhen(when); shs.setSignedWho(who); }
   *
   * String sstress = vsd.getSel_Stressed(); if (sstress != null) { SelStressed ss = spvs.addNewSelStressed();
   *
   * if ("Always".equalsIgnoreCase(sstress)) ss.setValue(StringFrequency.ALWAYS); else if
   * ("Often".equalsIgnoreCase(sstress)) ss.setValue(StringFrequency.OFTEN); else if
   * ("Sometimes".equalsIgnoreCase(sstress)) ss.setValue(StringFrequency.SOMETIMES); else if
   * ("Never".equalsIgnoreCase(sstress)) ss.setValue(StringFrequency.NEVER); else ss.setValue(StringFrequency.X);
   * ss.setSignedWhen(when); ss.setSignedWho(who); }
   *
   * BRxCurrentDiu brcd = spvs.addNewBRxCurrentDiu(); brcd.setValue(vsd.isDiuret_rx()); brcd.setSignedWhen(when);
   * brcd.setSignedWho(who);
   *
   * BRxCurrentAce brca = spvs.addNewBRxCurrentAce(); brca.setValue(vsd.isAce_rx()); brca.setSignedWhen(when);
   * brca.setSignedWho(who);
   *
   * BRxCurrentArb brcab = spvs.addNewBRxCurrentArb(); brcab.setValue(vsd.isArecept_rx()); brcab.setSignedWhen(when);
   * brcab.setSignedWho(who);
   *
   * BRxCurrentBb brcbb = spvs.addNewBRxCurrentBb(); brcbb.setValue(vsd.isBeta_rx()); brcbb.setSignedWhen(when);
   * brcbb.setSignedWho(who);
   *
   * BRxCurrentCcb brcc = spvs.addNewBRxCurrentCcb(); brcc.setValue(vsd.isCalc_rx()); brcc.setSignedWhen(when);
   * brcc.setSignedWho(who);
   *
   * BRxCurrentOthhtn brcon = spvs.addNewBRxCurrentOthhtn(); brcon.setValue(vsd.isAnti_rx()); brcon.setSignedWhen(when);
   * brcon.setSignedWho(who);
   *
   * BRxCurrentSta brcs = spvs.addNewBRxCurrentSta(); brcs.setValue(vsd.isStatin_rx()); brcs.setSignedWhen(when);
   * brcs.setSignedWho(who);
   *
   * BRxCurrentOthlip brcop = spvs.addNewBRxCurrentOthlip(); brcop.setValue(vsd.isLipid_rx());
   * brcop.setSignedWhen(when); brcop.setSignedWho(who);
   *
   * BRxCurrentOha brcoa = spvs.addNewBRxCurrentOha(); brcoa.setValue(vsd.isHypo_rx()); brcoa.setSignedWhen(when);
   * brcoa.setSignedWho(who);
   *
   * BRxCurrentIns brci = spvs.addNewBRxCurrentIns(); brci.setValue(vsd.isInsul_rx()); brci.setSignedWhen(when);
   * brci.setSignedWho(who);
   *
   * BRxSideEffectsDiu brsed = spvs.addNewBRxSideEffectsDiu(); brsed.setValue(vsd.isDiuret_SideEffects());
   * brsed.setSignedWhen(when); brsed.setSignedWho(who);
   *
   * BRxSideEffectsAce brsea = spvs.addNewBRxSideEffectsAce(); brsea.setValue(vsd.isAce_SideEffects());
   * brsea.setSignedWhen(when); brsea.setSignedWho(who);
   *
   * BRxSideEffectsArb brseab = spvs.addNewBRxSideEffectsArb(); brseab.setValue(vsd.isArecept_SideEffects());
   * brseab.setSignedWhen(when); brseab.setSignedWho(who);
   *
   * BRxSideEffectsBb brseb = spvs.addNewBRxSideEffectsBb(); brseb.setValue(vsd.isBeta_SideEffects());
   * brseb.setSignedWhen(when); brseb.setSignedWho(who);
   *
   * BRxSideEffectsCcb brsec = spvs.addNewBRxSideEffectsCcb(); brsec.setValue(vsd.isCalc_SideEffects());
   * brsec.setSignedWhen(when); brsec.setSignedWho(who);
   *
   * BRxSideEffectsOthhtn brseon = spvs.addNewBRxSideEffectsOthhtn(); brseon.setValue(vsd.isAnti_SideEffects());
   * brseon.setSignedWhen(when); brseon.setSignedWho(who);
   *
   * BRxSideEffectsSta brses = spvs.addNewBRxSideEffectsSta(); brses.setValue(vsd.isStatin_SideEffects());
   * brses.setSignedWhen(when); brses.setSignedWho(who);
   *
   * BRxSideEffectsOthlip brseop = spvs.addNewBRxSideEffectsOthlip(); brseop.setValue(vsd.isLipid_SideEffects());
   * brseop.setSignedWhen(when); brseop.setSignedWho(who);
   *
   * BRxSideEffectsOha brseoa = spvs.addNewBRxSideEffectsOha(); brseoa.setValue(vsd.isHypo_SideEffects());
   * brseoa.setSignedWhen(when); brseoa.setSignedWho(who);
   *
   * BRxSideEffectsIns brsei = spvs.addNewBRxSideEffectsIns(); brsei.setValue(vsd.isInsul_SideEffects());
   * brsei.setSignedWhen(when); brsei.setSignedWho(who);
   *
   * String result = null; result = vsd.getDiuret_RxDecToday(); if (result != null) { SelRxTodayDiu srtd =
   * spvs.addNewSelRxTodayDiu();
   *
   * if ("Same".equalsIgnoreCase(result)) srtd.setValue(StringRxToday.SAME); else if
   * ("Increase".equalsIgnoreCase(result)) srtd.setValue(StringRxToday.INCREASE); else if
   * ("Decrease".equalsIgnoreCase(result)) srtd.setValue(StringRxToday.DECREASE); else if
   * ("Stop".equalsIgnoreCase(result)) srtd.setValue(StringRxToday.STOP); else if ("Start".equalsIgnoreCase(result))
   * srtd.setValue(StringRxToday.START); else if ("InClassSwitch".equalsIgnoreCase(result))
   * srtd.setValue(StringRxToday.IN_CLASS_SWITCH); else if ("null".equalsIgnoreCase(result))
   * srtd.setValue(StringRxToday.X); srtd.setSignedWhen(when); srtd.setSignedWho(who); }
   *
   * result = vsd.getAce_RxDecToday(); if (result != null) { SelRxTodayAce srta = spvs.addNewSelRxTodayAce();
   *
   * if ("Same".equalsIgnoreCase(result)) srta.setValue(StringRxToday.SAME); else if
   * ("Increase".equalsIgnoreCase(result)) srta.setValue(StringRxToday.INCREASE); else if
   * ("Decrease".equalsIgnoreCase(result)) srta.setValue(StringRxToday.DECREASE); else if
   * ("Stop".equalsIgnoreCase(result)) srta.setValue(StringRxToday.STOP); else if ("Start".equalsIgnoreCase(result))
   * srta.setValue(StringRxToday.START); else if ("InClassSwitch".equalsIgnoreCase(result))
   * srta.setValue(StringRxToday.IN_CLASS_SWITCH); else if ("null".equalsIgnoreCase(result))
   * srta.setValue(StringRxToday.X); srta.setSignedWhen(when); srta.setSignedWho(who); }
   *
   * result = vsd.getArecept_RxDecToday(); if (result != null) { SelRxTodayArb srtb = spvs.addNewSelRxTodayArb();
   *
   * if ("Same".equalsIgnoreCase(result)) srtb.setValue(StringRxToday.SAME); else if
   * ("Increase".equalsIgnoreCase(result)) srtb.setValue(StringRxToday.INCREASE); else if
   * ("Decrease".equalsIgnoreCase(result)) srtb.setValue(StringRxToday.DECREASE); else if
   * ("Stop".equalsIgnoreCase(result)) srtb.setValue(StringRxToday.STOP); else if ("Start".equalsIgnoreCase(result))
   * srtb.setValue(StringRxToday.START); else if ("InClassSwitch".equalsIgnoreCase(result))
   * srtb.setValue(StringRxToday.IN_CLASS_SWITCH); else if ("null".equalsIgnoreCase(result))
   * srtb.setValue(StringRxToday.X); srtb.setSignedWhen(when); srtb.setSignedWho(who); }
   *
   * result = vsd.getBeta_RxDecToday(); if (result != null) { SelRxTodayBb srtbb = spvs.addNewSelRxTodayBb();
   *
   * if ("Same".equalsIgnoreCase(result)) srtbb.setValue(StringRxToday.SAME); else if
   * ("Increase".equalsIgnoreCase(result)) srtbb.setValue(StringRxToday.INCREASE); else if
   * ("Decrease".equalsIgnoreCase(result)) srtbb.setValue(StringRxToday.DECREASE); else if
   * ("Stop".equalsIgnoreCase(result)) srtbb.setValue(StringRxToday.STOP); else if ("Start".equalsIgnoreCase(result))
   * srtbb.setValue(StringRxToday.START); else if ("InClassSwitch".equalsIgnoreCase(result))
   * srtbb.setValue(StringRxToday.IN_CLASS_SWITCH); else if ("null".equalsIgnoreCase(result))
   * srtbb.setValue(StringRxToday.X); srtbb.setSignedWhen(when); srtbb.setSignedWho(who); }
   *
   * result = vsd.getCalc_RxDecToday(); if (result != null) { SelRxTodayCcb srtc = spvs.addNewSelRxTodayCcb();
   *
   * if ("Same".equalsIgnoreCase(result)) srtc.setValue(StringRxToday.SAME); else if
   * ("Increase".equalsIgnoreCase(result)) srtc.setValue(StringRxToday.INCREASE); else if
   * ("Decrease".equalsIgnoreCase(result)) srtc.setValue(StringRxToday.DECREASE); else if
   * ("Stop".equalsIgnoreCase(result)) srtc.setValue(StringRxToday.STOP); else if ("Start".equalsIgnoreCase(result))
   * srtc.setValue(StringRxToday.START); else if ("InClassSwitch".equalsIgnoreCase(result))
   * srtc.setValue(StringRxToday.IN_CLASS_SWITCH); else if ("null".equalsIgnoreCase(result))
   * srtc.setValue(StringRxToday.X); srtc.setSignedWhen(when); srtc.setSignedWho(who); }
   *
   * result = vsd.getAnti_RxDecToday(); if (result != null) { SelRxTodayOthhtn srton = spvs.addNewSelRxTodayOthhtn();
   *
   * if ("Same".equalsIgnoreCase(result)) srton.setValue(StringRxToday.SAME); else if
   * ("Increase".equalsIgnoreCase(result)) srton.setValue(StringRxToday.INCREASE); else if
   * ("Decrease".equalsIgnoreCase(result)) srton.setValue(StringRxToday.DECREASE); else if
   * ("Stop".equalsIgnoreCase(result)) srton.setValue(StringRxToday.STOP); else if ("Start".equalsIgnoreCase(result))
   * srton.setValue(StringRxToday.START); else if ("InClassSwitch".equalsIgnoreCase(result))
   * srton.setValue(StringRxToday.IN_CLASS_SWITCH); else if ("null".equalsIgnoreCase(result))
   * srton.setValue(StringRxToday.X); srton.setSignedWhen(when); srton.setSignedWho(who); }
   *
   * result = vsd.getStatin_RxDecToday(); if (result != null) { SelRxTodaySta srts = spvs.addNewSelRxTodaySta();
   *
   * if ("Same".equalsIgnoreCase(result)) srts.setValue(StringRxToday.SAME); else if
   * ("Increase".equalsIgnoreCase(result)) srts.setValue(StringRxToday.INCREASE); else if
   * ("Decrease".equalsIgnoreCase(result)) srts.setValue(StringRxToday.DECREASE); else if
   * ("Stop".equalsIgnoreCase(result)) srts.setValue(StringRxToday.STOP); else if ("Start".equalsIgnoreCase(result))
   * srts.setValue(StringRxToday.START); else if ("InClassSwitch".equalsIgnoreCase(result))
   * srts.setValue(StringRxToday.IN_CLASS_SWITCH); else if ("null".equalsIgnoreCase(result))
   * srts.setValue(StringRxToday.X); srts.setSignedWhen(when); srts.setSignedWho(who); }
   *
   * result = vsd.getLipid_RxDecToday(); if (result != null) { SelRxTodayOthlip srtop = spvs.addNewSelRxTodayOthlip();
   * if ("Same".equalsIgnoreCase(result)) srtop.setValue(StringRxToday.SAME); else if
   * ("Increase".equalsIgnoreCase(result)) srtop.setValue(StringRxToday.INCREASE); else if
   * ("Decrease".equalsIgnoreCase(result)) srtop.setValue(StringRxToday.DECREASE); else if
   * ("Stop".equalsIgnoreCase(result)) srtop.setValue(StringRxToday.STOP); else if ("Start".equalsIgnoreCase(result))
   * srtop.setValue(StringRxToday.START); else if ("InClassSwitch".equalsIgnoreCase(result))
   * srtop.setValue(StringRxToday.IN_CLASS_SWITCH); else if ("null".equalsIgnoreCase(result))
   * srtop.setValue(StringRxToday.X); srtop.setSignedWhen(when); srtop.setSignedWho(who); }
   *
   * result = vsd.getHypo_RxDecToday(); if (result != null) { SelRxTodayOha srtoa = spvs.addNewSelRxTodayOha();
   *
   * if ("Same".equalsIgnoreCase(result)) srtoa.setValue(StringRxToday.SAME); else if
   * ("Increase".equalsIgnoreCase(result)) srtoa.setValue(StringRxToday.INCREASE); else if
   * ("Decrease".equalsIgnoreCase(result)) srtoa.setValue(StringRxToday.DECREASE); else if
   * ("Stop".equalsIgnoreCase(result)) srtoa.setValue(StringRxToday.STOP); else if ("Start".equalsIgnoreCase(result))
   * srtoa.setValue(StringRxToday.START); else if ("InClassSwitch".equalsIgnoreCase(result))
   * srtoa.setValue(StringRxToday.IN_CLASS_SWITCH); else if ("null".equalsIgnoreCase(result))
   * srtoa.setValue(StringRxToday.X); srtoa.setSignedWhen(when); srtoa.setSignedWho(who); }
   *
   * result = vsd.getInsul_RxDecToday(); if (result != null) { SelRxTodayIns srti = spvs.addNewSelRxTodayIns();
   *
   * if ("Same".equalsIgnoreCase(result)) srti.setValue(StringRxToday.SAME); else if
   * ("Increase".equalsIgnoreCase(result)) srti.setValue(StringRxToday.INCREASE); else if
   * ("Decrease".equalsIgnoreCase(result)) srti.setValue(StringRxToday.DECREASE); else if
   * ("Stop".equalsIgnoreCase(result)) srti.setValue(StringRxToday.STOP); else if ("Start".equalsIgnoreCase(result))
   * srti.setValue(StringRxToday.START); else if ("InClassSwitch".equalsIgnoreCase(result))
   * srti.setValue(StringRxToday.IN_CLASS_SWITCH); else if ("null".equalsIgnoreCase(result))
   * srti.setValue(StringRxToday.X); srti.setSignedWhen(when); srti.setSignedWho(who); }
   *
   * tempi = vsd.getOften_miss(); if (tempi != Integer.MIN_VALUE) { IntMissedMedsPerWk immp =
   * spvs.addNewIntMissedMedsPerWk(); immp.setValue(vsd.getOften_miss()); immp.setSignedWhen(when);
   * immp.setSignedWho(who); }
   *
   * result = vsd.getHerbal(); if (result != null) { SelHerbalMeds shmd = spvs.addNewSelHerbalMeds();
   *
   * if ("yes".equalsIgnoreCase(result)) shmd.setValue(StringYesNo.YES); else if ("no".equalsIgnoreCase(result))
   * shmd.setValue(StringYesNo.NO); else if ("null".equalsIgnoreCase(result)) shmd.setValue(StringYesNo.X);
   * shmd.setSignedWhen(when); shmd.setSignedWho(who); }
   *
   * result = vsd.getNextvisit(); if (result != null) { SelFollowUp sfu = spvs.addNewSelFollowUp();
   *
   * if ("Under1Mo".equalsIgnoreCase(result)) sfu.setValue(StringFollowUpInterval.UNDER_1_MO); else if
   * ("1to2Mo".equalsIgnoreCase(result)) sfu.setValue(StringFollowUpInterval.X_1_TO_2_MO); else if
   * ("3to6Mo".equalsIgnoreCase(result)) sfu.setValue(StringFollowUpInterval.X_3_TO_6_MO); else if
   * ("Over6Mo".equalsIgnoreCase(result)) sfu.setValue(StringFollowUpInterval.OVER_6_MO); else if
   * ("null".equalsIgnoreCase(result)) sfu.setValue(StringFollowUpInterval.X); sfu.setSignedWhen(when);
   * sfu.setSignedWho(who); }
   *
   * BBPAP bbpap = spvs.addNewBBPAP(); bbpap.setValue(vsd.isBpactionplan()); bbpap.setSignedWhen(when);
   * bbpap.setSignedWho(who);
   *
   * BTPOff btpoff = spvs.addNewBTPOff(); btpoff.setValue(vsd.isPressureOff()); btpoff.setSignedWhen(when);
   * btpoff.setSignedWho(who);
   *
   * BPPAgreement bppa = spvs.addNewBPPAgreement(); bppa.setValue(vsd.isPatientProvider()); bppa.setSignedWhen(when);
   * bppa.setSignedWho(who);
   *
   * BABPM babpm = spvs.addNewBABPM(); babpm.setValue(vsd.isABPM()); babpm.setSignedWhen(when); babpm.setSignedWho(who);
   *
   * BHomeMon bhmn = spvs.addNewBHomeMon(); bhmn.setValue(vsd.isHome()); bhmn.setSignedWhen(when);
   * bhmn.setSignedWho(who);
   *
   * BCommunRes bcr = spvs.addNewBCommunRes(); bcr.setValue(vsd.isCommunityRes()); bcr.setSignedWhen(when);
   * bcr.setSignedWho(who);
   *
   * BReferHCP brhcp = spvs.addNewBReferHCP(); brhcp.setValue(vsd.isProRefer()); brhcp.setSignedWhen(when);
   * brhcp.setSignedWho(who);
   *
   * } /
   *******************/

  public HsfHmpDataDocument generateDataVaultXML( String providerNo, Integer demographicNo ) 
  {
    // using a long range of beginDate/endDate
    Calendar beginDate = Calendar.getInstance();
    beginDate.add( Calendar.YEAR, -5 );
    Calendar endDate = Calendar.getInstance();
    endDate.add( Calendar.YEAR, 5 );
    return generateDataVaultXML( providerNo, demographicNo, beginDate, endDate );
  }

  public HsfHmpDataDocument generateDataVaultXML( String providerNo, Integer demographicNo, Calendar dataBeginDate,
                                                  Calendar dataEndDate ) 
  {
    HsfHmpDataDocument doc = HsfHmpDataDocument.Factory.newInstance();

    // add HsfoHbpsData
    HsfHmpData root = doc.addNewHsfHmpData();
    root.setVersionDate( new XmlCalendar( getVersionDate() ) );
    root.setDataBeginDate( dataBeginDate );
    root.setDataEndDate( dataEndDate );
    root.setExtractedWhen( new XmlCalendar( dformat1.format( new Date() ) ) );
    root.setExtractedWho( getProviderName( providerNo ) );

    // add site
    Site site = root.addNewSite();
    site.setHsfSiteCodeKey( getSiteID().intValue() );
    dataBeginDate.add(Calendar.DATE, -1);
	dataEndDate.add(Calendar.DATE, 1);

    if ( demographicNo.intValue() == 0 )
    {
      // add all patients data
      List patientIdList = hdao.getAllPatientId();
      if ( patientIdList != null )
        for ( int i = 0; i < patientIdList.size(); i++ )
        {
          String pid = (String) patientIdList.get( i );
          Hsfo2Patient pdata = getDemographic( pid );
          if ( pdata != null && pdata.getPatient_Id() != null ) {
        	  Calendar dateOfHmpStatus = Calendar.getInstance();
        	  dateOfHmpStatus.setTime(pdata.getDateOfHmpStatus());
        	  addPatientToSite( site, pdata, null, dataBeginDate, dataEndDate); // FIXME: add parameter for first visit data        	  
          }
        }
      if ( patientIdList == null || patientIdList.size() == 0 )
        doc = null;
    }
    else
    {
      Hsfo2Patient pdata = getDemographic( demographicNo.toString() );
      if ( pdata != null && pdata.getPatient_Id() != null )
        addPatientToSite( site, pdata, null, dataBeginDate, dataEndDate ); // FIXME: add parameter for first visit data
      else
        doc = null;
    }

    return doc;

  }

  /***********************/

  public String printErrors( ArrayList validationErrors )
  {
    StringBuilder sb = new StringBuilder( "++++++++++++Invalid XML!++++++++++++\n" );

    sb.append( "Errors discovered during validation: \n" );
    Iterator iter = validationErrors.iterator();
    while ( iter.hasNext() )
    {
      sb.append( ">> " + iter.next() + "\n" );

    }
    return sb.toString();
  }

  public ArrayList validateDoc( HsfHmpDataDocument doc ) throws IOException, XmlException
  {
      logger.debug( "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + "\n" + PrettyPrinter.indent( doc.xmlText() ) );

    ArrayList<StringBuilder> messageArray = new ArrayList<StringBuilder>();
    XmlOptions option = new XmlOptions();
    ArrayList<XmlError> validationErrors = new ArrayList<XmlError>();
    option.setErrorListener( validationErrors );
    StringBuilder sb = new StringBuilder();

    if ( doc.validate( option ) == false )
    {
    	Iterator iter = validationErrors.iterator();
        while ( iter.hasNext() ) {
        	XmlError error = (XmlError)iter.next();
        	String errorMessage = error.getMessage();
        	XmlCursor cursor = error.getCursorLocation();
        	cursor.toParent();
        	String loc = cursor.xmlText();

        	String name = getPatientName(doc.xmlText(), loc);
        	sb.append("\n Patient Name: "+name + " \n  ** Error Message: " + errorMessage + "  ");
        	sb.append( loc.substring(loc.indexOf("signedWhen"), loc.indexOf("/")) + " --- ");
        	messageArray.add( sb );
        }
      //String sb = printErrors( validationErrors );

      messageArray.add( sb );
      return messageArray;
    }
    else
      return messageArray;
  }

  private String getPatientName(String xmlText, String location) {

	  StringBuilder body = new StringBuilder(xmlText);
	  String a1 = body.substring(0,body.indexOf(location));
	  String a2 = a1.substring(a1.lastIndexOf("txt_Surname value"));
	  String surname= a2.substring(19, a2.indexOf("signedWhen")-2);
	  String a3 = a2.substring(a2.indexOf("txt_GivenNames value"));
	  String givenname = a3.substring(22, a3.indexOf("signedWhen")-2);

	  return givenname+ " " + surname;
  }

  public String base64Encoding( byte[] input )
  {
    return ( new String( Base64.encodeBase64( input ) ) );
  }

  public byte[] zipCompress( String fileName, byte[] input ) throws IOException
  {

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ZipOutputStream zos = new ZipOutputStream( baos );

    try
    {
      zos.putNextEntry( new ZipEntry( fileName ) );
      zos.write( input );
      zos.closeEntry();
      zos.finish();

      return baos.toByteArray();
    }
    finally
    {
      zos.close();
    }
  }


 /**
  * 
  * @param siteCode
  * @param userId
  * @param passwd
  * @return map
  * @throws Exception
  */
  public Map< SoapElementKey, Object > soapHttpCallGetDataDateRange( int siteCode, String userId, String passwd ) throws Exception
  {
    final int fileType = 18;      //GetDataDateRange file type is 18?
    userId = userId.replaceAll( "&", "&amp;" );
    passwd = passwd.replaceAll( "&", "&amp;" );

    PostMethod post = new PostMethod( getWebUrl() );
//    post.setRequestHeader( "SOAPAction", getDataDateRangeAction );
    post.setRequestHeader( "Content-Type", "text/xml; charset=utf-8" );

//    String soapMsg = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
//                     + "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
//                     + "<soap-env:Body>"
//                     + "<GetDataDateRange xmlns=\"" + namespace + "\">"
//                     + "<Site>" + siteCode + "</Site>" + "<UserID>" + userId + "</UserID>" + "<Password>" + passwd + "</Password>"
//                     + "<FileType>" + fileType + "</FileType>"
//                     + "</GetDataDateRange>" + "</soap:Body>"
//                     + "</soap:Envelope>";

    String soapMsg =
      "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:dat=\"https://www.clinforma.net/P-Prompt/DataReceiveWS/\">"
    + "<soapenv:Header/>"
    + "<soapenv:Body>"
    + "<dat:GetDataDateRange>"
    + "  <dat:Site>" + siteCode + "</dat:Site>"
    + "  <dat:UserID>" + userId + "</dat:UserID>"
    + "  <dat:Password>" + passwd + "</dat:Password>"
    + "  <dat:FileType>" + fileType + "</dat:FileType>"
    + "</dat:GetDataDateRange>"
    + "</soapenv:Body>"
    + "</soapenv:Envelope>";

    RequestEntity re = new StringRequestEntity( soapMsg, "text/xml", "utf-8" );

    post.setRequestEntity( re );

    HttpClient httpclient = new HttpClient();
    // Execute request
    try
    {
      Map< SoapElementKey, Object > output = new HashMap< SoapElementKey, Object >();
      int result = httpclient.executeMethod( post );
      // Display status code

      output.put( SoapElementKey.responseStatusCode, result );
      String rsXml = post.getResponseBodyAsString();
      logger.debug( "response xml of GetDataDateRange: \n" + rsXml );
      if ( result != 200 )
      {
        logger.error( "GetDataDateRange result code: " + result );
        return null;
      }

      output.put( SoapElementKey.GetDataDateRangeResult, getElementValue( rsXml, "GetDataDateRangeResult" ) );

      output.put( SoapElementKey.DataBeginDate, getElementValue( rsXml, "DataBeginDate" ) );
      output.put( SoapElementKey.DataEndDate, getElementValue( rsXml, "DataEndDate" ) );

      return output;
    }
    finally
    {
      post.releaseConnection();
    }
  }

  protected static String getElementValue( final String xml, final String element )
  {
    int begin = xml.indexOf( "<" + element + ">" );
    int end = xml.indexOf( "</" + element + ">" );
    if ( begin < 0 || end < 0 || begin >= end )
      return null;
    return xml.substring( begin + ( "<" + element + ">" ).length(), end );
  }


/**
 * 
 * @param siteCode
 * @param userId
 * @param passwd
 * @param xml
 * @return map
 * @throws Exception
 */
  public Map< SoapElementKey, Object > soapHttpCallDataVault( int siteCode, String userId, String passwd, String xml )
      throws Exception
  {
    final int fileType = 18;
    final String fileName = "hsfoData.xml";

    userId = userId.replaceAll( "&", "&amp;" );
    passwd = passwd.replaceAll( "&", "&amp;" );

    final String webUrl = getWebUrl();
    PostMethod post = new PostMethod( webUrl );
//    post.setRequestHeader( "SOAPAction", dataVaultAction );     //no soap action required
    post.setRequestHeader( "Content-Type", "text/xml; charset=utf-8" );

    {
      final String key = "dataBeginDate";
      int begin = xml.indexOf( key );
      int end = xml.indexOf( " ", begin );
      if( begin > 0 && end > 0 )
      {
        String sValue = xml.substring( begin + key.length(), end );
        String sValueNew = sValue.substring( 0, 12 ) + "\"";    // + "          ".substring( sBeginDateLen - 12 );
        xml = xml.replaceFirst( key+sValue, key+sValueNew );
      }
    }
    {
      final String key = "dataEndDate";
      int begin = xml.indexOf( key );
      int end = xml.indexOf( " ", begin );
      if( begin > 0 && end > 0 )
      {
        String sValue = xml.substring( begin + key.length(), end );
        String sValueNew = sValue.substring( 0, 12 ) + "\"";    // + "          ".substring( sBeginDateLen - 12 );
        xml = xml.replaceFirst( key+sValue, key+sValueNew );
      }
    }

//    String soapMsg = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
//                     + "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
//                     + "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
//                     + "<soap-env:Body>"
//                     + "<DataVault xmlns=\"" + namespace + "\">"
//                     + "<Site>" + siteCode + "</Site>" + "<UserID>" + userId + "</UserID>" + "<Password>" + passwd
//                     + "</Password>" + "<FileType>" + fileType + "</FileType>" + "<FileName>" + "HsfoData.xml"
//                     + "</FileName>" + "<FileAsBinary>"
//                     + base64Encoding( zipCompress( "HsfoData.xml", xml.getBytes( "UTF-8" ) ) ) + "</FileAsBinary>"
//                     + "</DataVault>" + "</soap:Body>" + "</soap:Envelope>";

    /*****/
    String soapMsg =
      "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:dat=\"https://www.clinforma.net/P-Prompt/DataReceiveWS/\">"
    + "<soapenv:Header/>"
    + "<soapenv:Body>"
    + "<dat:DataVaultStatusStr>" //"<dat:DataVault>"
    + "  <dat:Site>" + siteCode + "</dat:Site>"
    + "  <dat:UserID>" + userId + "</dat:UserID>"
    + "  <dat:Password>" + passwd + "</dat:Password>"
    + "  <dat:FileType>" + fileType + "</dat:FileType>"
    + "  <dat:FileName>" + fileName + "</dat:FileName>"
//    + "  <dat:FileAsBinary>" + base64Encoding( zipCompress( "HsfoData.xml", xml.getBytes( "UTF-8" ) ) ) + "</dat:FileAsBinary>"
//    + "  <dat:FileAsBinary>" + xml + "</dat:FileAsBinary>"
    + "  <dat:FileAsBinary>" + base64Encoding(  xml.getBytes( "UTF-8" ) ) + "</dat:FileAsBinary>"   //correct when don't zip
    + "</dat:DataVaultStatusStr>"  //"</dat:DataVault>"
    + "</soapenv:Body>"
    + "</soapenv:Envelope>";
    /*****/

    /******
    String soapMsgPart1 =
      "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:dat=\"https://www.clinforma.net/P-Prompt/DataReceiveWS/\">"
    + "<soapenv:Header/>"
    + "<soapenv:Body>"
    + "<dat:DataVaultStatusStr>" //"<dat:DataVault>"
    + "  <dat:Site>" + siteCode + "</dat:Site>"
    + "  <dat:UserID>" + userId + "</dat:UserID>"
    + "  <dat:Password>" + passwd + "</dat:Password>"
    + "  <dat:FileType>" + fileType + "</dat:FileType>"
    + "  <dat:FileName>" + fileName + "</dat:FileName>"
    + "  <dat:FileAsBinary>";

    String soapMsgPart2 =
      "</dat:FileAsBinary>"
    + "  <dat:FileAsBinary>" + xml + "</dat:FileAsBinary>"
    + "</dat:DataVaultStatusStr>"  //"</dat:DataVault>"
    + "</soapenv:Body>"
    + "</soapenv:Envelope>";

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    outputStream.write( soapMsgPart1.getBytes("UTF-8") );
//    outputStream.write( zipCompress( "HsfoData.xml", xml.getBytes( "UTF-8" ) ) );
//    outputStream.write( xml.getBytes( "UTF-8" ) );

    outputStream.write( soapMsgPart2.getBytes("UTF-8") );
    outputStream.flush();
    /******/

    boolean saveXml = false;
    if( saveXml )
    {
      {
        File fxml = new File( "c:\\tmp\\xml.txt" );
        OutputStream os = new FileOutputStream( fxml );
        byte[] bxml = xml.getBytes();
        os.write( bxml );
        os.flush();
        os.close();
      }

      {
        File file = new File( "c:\\tmp\\HsfoData.xml.zip" );
        byte[] zipBytes = zipCompress( "HsfoData.xml", xml.getBytes( "UTF-8" ) );
        OutputStream os = new FileOutputStream( file );
        os.write( zipBytes );
        os.flush();
        os.close();
      }

      {
        File file = new File( "c:\\tmp\\HsfoData.xml.zip.base64" );
        String base64 = base64Encoding( zipCompress( "HsfoData.xml", xml.getBytes( "UTF-8" ) ) );
        OutputStream os = new FileOutputStream( file );
        os.write( base64.getBytes() );
        os.flush();
        os.close();
      }
    }

//    ByteArrayInputStream bis = new ByteArrayInputStream( outputStream.toByteArray() );
//    RequestEntity re = new InputStreamRequestEntity( bis, "text/xml");
    RequestEntity re = new StringRequestEntity( soapMsg, "text/xml", "utf-8" );

    post.setRequestEntity( re );

    HttpClient httpclient = new HttpClient();
    // Execute request
    try
    {
      Map< SoapElementKey, Object > output = new HashMap< SoapElementKey, Object >();

      int result = httpclient.executeMethod( post );
      // Display status code

      output.put( SoapElementKey.responseStatusCode, result );
      String rsXml = post.getResponseBodyAsString();
      logger.info( "url: " + webUrl );
      logger.info( "dataVaultAction: " + dataVaultAction );
      logger.info( "xml: =======" );
      logger.info( xml );
      logger.info( "xml end: =======" );
      logger.info( "response xml of DataVault: \n" + rsXml );
      logger.error( "DataVault result code: " + result );
      if ( result != 200 )
      {
        logger.error( "ERROR: DataVault result code: " + result );
        return output;
      }

      //output.put( SoapElementKey.DataVaultResult, getElementValue( rsXml, "DataVaultResult" ) );
      output.put( SoapElementKey.DataVaultStatusStrResult, getElementValue( rsXml, "DataVaultStatusStrResult" ) );
      output.put( SoapElementKey.StatusMessage, getElementValue( rsXml, "StatusMessage" ) );
      return output;

    }
    finally
    {
      // Release current connection to the connection pool
      post.releaseConnection();
    }
  }

}
