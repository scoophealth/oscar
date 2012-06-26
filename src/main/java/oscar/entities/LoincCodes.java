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

package oscar.entities;

;

/**
 * Encapsulates data from table loincCodes
 *
 */
public class LoincCodes {
  private String loincNum;
  private String component;
  private String property;
  private String timeAspct;
  private String system;
  private String scaleTyp;
  private String methodTyp;
  private String relatNms;
  private String _class;
  private String source;
  private String euclideCd;
  private String astmCd;
  private String iupacCd;
  private String dtLastCh;
  private String chngReas;
  private String chngType;
  private String comments;
  private String answerlist;
  private String status;
  private String mapTo;
  private String scope;
  private String snowmedCd;
  private String vaCd;
  private String metpathCd;
  private String hcfaCd;
  private String cdcCd;
  private String normRange;
  private String exUsUnits;
  private String ipccUnits;
  private String gpiCd;
  private String reference;
  private String exactCmpSy;
  private String molarMass;
  private String iupcAnltCd;
  private String classtype;
  private String formula;
  private String multumCd;
  private String deedsCd;
  private String cscqFrnchNm;
  private String cscqGrmnNm;
  private String spnshNm;
  private String cscqItlnNm;
  private String species;
  private String exmplAnswers;
  private String acssym;
  private String moleid;
  private String baseName;
  private String _final;
  private String geneId;
  private String naaccrId;
  private String codeTable;
  private String setRoot;
  private String panelElements;
  private String relatedNames2;
  private String surveyQuestText;
  private String surveyQuestSrc;
  private String unitsRequired;
  private String shortname;
  private String commonTests;
  private String usEuProperty;
  private String orderObs;

  /**
   * Class constructor with no arguments.
   */
  public LoincCodes() {}

  /**
   * Full constructor
   *
   * @param loincNum String
   * @param component String
   * @param property String
   * @param timeAspct String
   * @param system String
   * @param scaleTyp String
   * @param methodTyp String
   * @param relatNms String
   * @param _class String
   * @param source String
   * @param euclideCd String
   * @param astmCd String
   * @param iupacCd String
   * @param dtLastCh String
   * @param chngReas String
   * @param chngType String
   * @param comments String
   * @param answerlist String
   * @param status String
   * @param mapTo String
   * @param scope String
   * @param snowmedCd String
   * @param vaCd String
   * @param metpathCd String
   * @param hcfaCd String
   * @param cdcCd String
   * @param normRange String
   * @param exUsUnits String
   * @param ipccUnits String
   * @param gpiCd String
   * @param reference String
   * @param exactCmpSy String
   * @param molarMass String
   * @param iupcAnltCd String
   * @param classtype String
   * @param formula String
   * @param multumCd String
   * @param deedsCd String
   * @param cscqFrnchNm String
   * @param cscqGrmnNm String
   * @param spnshNm String
   * @param cscqItlnNm String
   * @param species String
   * @param exmplAnswers String
   * @param acssym String
   * @param moleid String
   * @param baseName String
   * @param _final String
   * @param geneId String
   * @param naaccrId String
   * @param codeTable String
   * @param setRoot String
   * @param panelElements String
   * @param relatedNames2 String
   * @param surveyQuestText String
   * @param surveyQuestSrc String
   * @param unitsRequired String
   * @param shortname String
   * @param commonTests String
   * @param usEuProperty String
   * @param orderObs String
   */
  public LoincCodes(String loincNum, String component, String property,
                    String timeAspct, String system, String scaleTyp,
                    String methodTyp, String relatNms, String _class,
                    String source, String euclideCd, String astmCd,
                    String iupacCd, String dtLastCh, String chngReas,
                    String chngType, String comments, String answerlist,
                    String status, String mapTo, String scope, String snowmedCd,
                    String vaCd, String metpathCd, String hcfaCd, String cdcCd,
                    String normRange, String exUsUnits, String ipccUnits,
                    String gpiCd, String reference, String exactCmpSy,
                    String molarMass, String iupcAnltCd, String classtype,
                    String formula, String multumCd, String deedsCd,
                    String cscqFrnchNm, String cscqGrmnNm, String spnshNm,
                    String cscqItlnNm, String species, String exmplAnswers,
                    String acssym, String moleid, String baseName,
                    String _final, String geneId, String naaccrId,
                    String codeTable, String setRoot, String panelElements,
                    String relatedNames2, String surveyQuestText,
                    String surveyQuestSrc, String unitsRequired,
                    String shortname
                    , String commonTests, String usEuProperty, String orderObs) {
    this.loincNum = loincNum;
    this.component = component;
    this.property = property;
    this.timeAspct = timeAspct;
    this.system = system;
    this.scaleTyp = scaleTyp;
    this.methodTyp = methodTyp;
    this.relatNms = relatNms;
    this._class = _class;
    this.source = source;
    this.euclideCd = euclideCd;
    this.astmCd = astmCd;
    this.iupacCd = iupacCd;
    this.dtLastCh = dtLastCh;
    this.chngReas = chngReas;
    this.chngType = chngType;
    this.comments = comments;
    this.answerlist = answerlist;
    this.status = status;
    this.mapTo = mapTo;
    this.scope = scope;
    this.snowmedCd = snowmedCd;
    this.vaCd = vaCd;
    this.metpathCd = metpathCd;
    this.hcfaCd = hcfaCd;
    this.cdcCd = cdcCd;
    this.normRange = normRange;
    this.exUsUnits = exUsUnits;
    this.ipccUnits = ipccUnits;
    this.gpiCd = gpiCd;
    this.reference = reference;
    this.exactCmpSy = exactCmpSy;
    this.molarMass = molarMass;
    this.iupcAnltCd = iupcAnltCd;
    this.classtype = classtype;
    this.formula = formula;
    this.multumCd = multumCd;
    this.deedsCd = deedsCd;
    this.cscqFrnchNm = cscqFrnchNm;
    this.cscqGrmnNm = cscqGrmnNm;
    this.spnshNm = spnshNm;
    this.cscqItlnNm = cscqItlnNm;
    this.species = species;
    this.exmplAnswers = exmplAnswers;
    this.acssym = acssym;
    this.moleid = moleid;
    this.baseName = baseName;
    this._final = _final;
    this.geneId = geneId;
    this.naaccrId = naaccrId;
    this.codeTable = codeTable;
    this.setRoot = setRoot;
    this.panelElements = panelElements;
    this.relatedNames2 = relatedNames2;
    this.surveyQuestText = surveyQuestText;
    this.surveyQuestSrc = surveyQuestSrc;
    this.unitsRequired = unitsRequired;
    this.shortname = shortname;
    this.commonTests = commonTests;
    this.usEuProperty = usEuProperty;
    this.orderObs = orderObs;
  }

  /**
   * Gets the loincNum
   * @return String loincNum
   */
  public String getLoincNum() {
    return (loincNum != null ? loincNum : "");
  }

  /**
   * Gets the component
   * @return String component
   */
  public String getComponent() {
    return (component != null ? component : "");
  }

  /**
   * Gets the property
   * @return String property
   */
  public String getProperty() {
    return (property != null ? property : "");
  }

  /**
   * Gets the timeAspct
   * @return String timeAspct
   */
  public String getTimeAspct() {
    return (timeAspct != null ? timeAspct : "");
  }

  /**
   * Gets the system
   * @return String system
   */
  public String getSystem() {
    return (system != null ? system : "");
  }

  /**
   * Gets the scaleTyp
   * @return String scaleTyp
   */
  public String getScaleTyp() {
    return (scaleTyp != null ? scaleTyp : "");
  }

  /**
   * Gets the methodTyp
   * @return String methodTyp
   */
  public String getMethodTyp() {
    return (methodTyp != null ? methodTyp : "");
  }

  /**
   * Gets the relatNms
   * @return String relatNms
   */
  public String getRelatNms() {
    return (relatNms != null ? relatNms : "");
  }

  /**
   * Gets the _class
   * @return String _class
   */
  public String get_class() {
    return (_class != null ? _class : "");
  }

  /**
   * Gets the source
   * @return String source
   */
  public String getSource() {
    return (source != null ? source : "");
  }

  /**
   * Gets the euclideCd
   * @return String euclideCd
   */
  public String getEuclideCd() {
    return (euclideCd != null ? euclideCd : "");
  }

  /**
   * Gets the astmCd
   * @return String astmCd
   */
  public String getAstmCd() {
    return (astmCd != null ? astmCd : "");
  }

  /**
   * Gets the iupacCd
   * @return String iupacCd
   */
  public String getIupacCd() {
    return (iupacCd != null ? iupacCd : "");
  }

  /**
   * Gets the dtLastCh
   * @return String dtLastCh
   */
  public String getDtLastCh() {
    return (dtLastCh != null ? dtLastCh : "");
  }

  /**
   * Gets the chngReas
   * @return String chngReas
   */
  public String getChngReas() {
    return (chngReas != null ? chngReas : "");
  }

  /**
   * Gets the chngType
   * @return String chngType
   */
  public String getChngType() {
    return (chngType != null ? chngType : "");
  }

  /**
   * Gets the comments
   * @return String comments
   */
  public String getComments() {
    return (comments != null ? comments : "");
  }

  /**
   * Gets the answerlist
   * @return String answerlist
   */
  public String getAnswerlist() {
    return (answerlist != null ? answerlist : "");
  }

  /**
   * Gets the status
   * @return String status
   */
  public String getStatus() {
    return (status != null ? status : "");
  }

  /**
   * Gets the mapTo
   * @return String mapTo
   */
  public String getMapTo() {
    return (mapTo != null ? mapTo : "");
  }

  /**
   * Gets the scope
   * @return String scope
   */
  public String getScope() {
    return (scope != null ? scope : "");
  }

  /**
   * Gets the snowmedCd
   * @return String snowmedCd
   */
  public String getSnowmedCd() {
    return (snowmedCd != null ? snowmedCd : "");
  }

  /**
   * Gets the vaCd
   * @return String vaCd
   */
  public String getVaCd() {
    return (vaCd != null ? vaCd : "");
  }

  /**
   * Gets the metpathCd
   * @return String metpathCd
   */
  public String getMetpathCd() {
    return (metpathCd != null ? metpathCd : "");
  }

  /**
   * Gets the hcfaCd
   * @return String hcfaCd
   */
  public String getHcfaCd() {
    return (hcfaCd != null ? hcfaCd : "");
  }

  /**
   * Gets the cdcCd
   * @return String cdcCd
   */
  public String getCdcCd() {
    return (cdcCd != null ? cdcCd : "");
  }

  /**
   * Gets the normRange
   * @return String normRange
   */
  public String getNormRange() {
    return (normRange != null ? normRange : "");
  }

  /**
   * Gets the exUsUnits
   * @return String exUsUnits
   */
  public String getExUsUnits() {
    return (exUsUnits != null ? exUsUnits : "");
  }

  /**
   * Gets the ipccUnits
   * @return String ipccUnits
   */
  public String getIpccUnits() {
    return (ipccUnits != null ? ipccUnits : "");
  }

  /**
   * Gets the gpiCd
   * @return String gpiCd
   */
  public String getGpiCd() {
    return (gpiCd != null ? gpiCd : "");
  }

  /**
   * Gets the reference
   * @return String reference
   */
  public String getReference() {
    return (reference != null ? reference : "");
  }

  /**
   * Gets the exactCmpSy
   * @return String exactCmpSy
   */
  public String getExactCmpSy() {
    return (exactCmpSy != null ? exactCmpSy : "");
  }

  /**
   * Gets the molarMass
   * @return String molarMass
   */
  public String getMolarMass() {
    return (molarMass != null ? molarMass : "");
  }

  /**
   * Gets the iupcAnltCd
   * @return String iupcAnltCd
   */
  public String getIupcAnltCd() {
    return (iupcAnltCd != null ? iupcAnltCd : "");
  }

  /**
   * Gets the classtype
   * @return String classtype
   */
  public String getClasstype() {
    return (classtype != null ? classtype : "");
  }

  /**
   * Gets the formula
   * @return String formula
   */
  public String getFormula() {
    return (formula != null ? formula : "");
  }

  /**
   * Gets the multumCd
   * @return String multumCd
   */
  public String getMultumCd() {
    return (multumCd != null ? multumCd : "");
  }

  /**
   * Gets the deedsCd
   * @return String deedsCd
   */
  public String getDeedsCd() {
    return (deedsCd != null ? deedsCd : "");
  }

  /**
   * Gets the cscqFrnchNm
   * @return String cscqFrnchNm
   */
  public String getCscqFrnchNm() {
    return (cscqFrnchNm != null ? cscqFrnchNm : "");
  }

  /**
   * Gets the cscqGrmnNm
   * @return String cscqGrmnNm
   */
  public String getCscqGrmnNm() {
    return (cscqGrmnNm != null ? cscqGrmnNm : "");
  }

  /**
   * Gets the spnshNm
   * @return String spnshNm
   */
  public String getSpnshNm() {
    return (spnshNm != null ? spnshNm : "");
  }

  /**
   * Gets the cscqItlnNm
   * @return String cscqItlnNm
   */
  public String getCscqItlnNm() {
    return (cscqItlnNm != null ? cscqItlnNm : "");
  }

  /**
   * Gets the species
   * @return String species
   */
  public String getSpecies() {
    return (species != null ? species : "");
  }

  /**
   * Gets the exmplAnswers
   * @return String exmplAnswers
   */
  public String getExmplAnswers() {
    return (exmplAnswers != null ? exmplAnswers : "");
  }

  /**
   * Gets the acssym
   * @return String acssym
   */
  public String getAcssym() {
    return (acssym != null ? acssym : "");
  }

  /**
   * Gets the moleid
   * @return String moleid
   */
  public String getMoleid() {
    return (moleid != null ? moleid : "");
  }

  /**
   * Gets the baseName
   * @return String baseName
   */
  public String getBaseName() {
    return (baseName != null ? baseName : "");
  }

  /**
   * Gets the _final
   * @return String _final
   */
  public String get_final() {
    return (_final != null ? _final : "");
  }

  /**
   * Gets the geneId
   * @return String geneId
   */
  public String getGeneId() {
    return (geneId != null ? geneId : "");
  }

  /**
   * Gets the naaccrId
   * @return String naaccrId
   */
  public String getNaaccrId() {
    return (naaccrId != null ? naaccrId : "");
  }

  /**
   * Gets the codeTable
   * @return String codeTable
   */
  public String getCodeTable() {
    return (codeTable != null ? codeTable : "");
  }

  /**
   * Gets the setRoot
   * @return String setRoot
   */
  public String getSetRoot() {
    return (setRoot != null ? setRoot : "");
  }

  /**
   * Gets the panelElements
   * @return String panelElements
   */
  public String getPanelElements() {
    return (panelElements != null ? panelElements : "");
  }

  /**
   * Gets the relatedNames2
   * @return String relatedNames2
   */
  public String getRelatedNames2() {
    return (relatedNames2 != null ? relatedNames2 : "");
  }

  /**
   * Gets the surveyQuestText
   * @return String surveyQuestText
   */
  public String getSurveyQuestText() {
    return (surveyQuestText != null ? surveyQuestText : "");
  }

  /**
   * Gets the surveyQuestSrc
   * @return String surveyQuestSrc
   */
  public String getSurveyQuestSrc() {
    return (surveyQuestSrc != null ? surveyQuestSrc : "");
  }

  /**
   * Gets the unitsRequired
   * @return String unitsRequired
   */
  public String getUnitsRequired() {
    return (unitsRequired != null ? unitsRequired : "");
  }

  /**
   * Gets the shortname
   * @return String shortname
   */
  public String getShortname() {
    return (shortname != null ? shortname : "");
  }

  /**
   * Gets the commonTests
   * @return String commonTests
   */
  public String getCommonTests() {
    return (commonTests != null ? commonTests : "");
  }

  /**
   * Gets the usEuProperty
   * @return String usEuProperty
   */
  public String getUsEuProperty() {
    return (usEuProperty != null ? usEuProperty : "");
  }

  /**
   * Gets the orderObs
   * @return String orderObs
   */
  public String getOrderObs() {
    return (orderObs != null ? orderObs : "");
  }

  /**
   * Sets the loincNum
   * @param loincNum String
   */
  public void setLoincNum(String loincNum) {
    this.loincNum = loincNum;
  }

  /**
   * Sets the component
   * @param component String
   */
  public void setComponent(String component) {
    this.component = component;
  }

  /**
   * Sets the property
   * @param property String
   */
  public void setProperty(String property) {
    this.property = property;
  }

  /**
   * Sets the timeAspct
   * @param timeAspct String
   */
  public void setTimeAspct(String timeAspct) {
    this.timeAspct = timeAspct;
  }

  /**
   * Sets the system
   * @param system String
   */
  public void setSystem(String system) {
    this.system = system;
  }

  /**
   * Sets the scaleTyp
   * @param scaleTyp String
   */
  public void setScaleTyp(String scaleTyp) {
    this.scaleTyp = scaleTyp;
  }

  /**
   * Sets the methodTyp
   * @param methodTyp String
   */
  public void setMethodTyp(String methodTyp) {
    this.methodTyp = methodTyp;
  }

  /**
   * Sets the relatNms
   * @param relatNms String
   */
  public void setRelatNms(String relatNms) {
    this.relatNms = relatNms;
  }

  /**
   * Sets the _class
   * @param _class String
   */
  public void set_class(String _class) {
    this._class = _class;
  }

  /**
   * Sets the source
   * @param source String
   */
  public void setSource(String source) {
    this.source = source;
  }

  /**
   * Sets the euclideCd
   * @param euclideCd String
   */
  public void setEuclideCd(String euclideCd) {
    this.euclideCd = euclideCd;
  }

  /**
   * Sets the astmCd
   * @param astmCd String
   */
  public void setAstmCd(String astmCd) {
    this.astmCd = astmCd;
  }

  /**
   * Sets the iupacCd
   * @param iupacCd String
   */
  public void setIupacCd(String iupacCd) {
    this.iupacCd = iupacCd;
  }

  /**
   * Sets the dtLastCh
   * @param dtLastCh String
   */
  public void setDtLastCh(String dtLastCh) {
    this.dtLastCh = dtLastCh;
  }

  /**
   * Sets the chngReas
   * @param chngReas String
   */
  public void setChngReas(String chngReas) {
    this.chngReas = chngReas;
  }

  /**
   * Sets the chngType
   * @param chngType String
   */
  public void setChngType(String chngType) {
    this.chngType = chngType;
  }

  /**
   * Sets the comments
   * @param comments String
   */
  public void setComments(String comments) {
    this.comments = comments;
  }

  /**
   * Sets the answerlist
   * @param answerlist String
   */
  public void setAnswerlist(String answerlist) {
    this.answerlist = answerlist;
  }

  /**
   * Sets the status
   * @param status String
   */
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * Sets the mapTo
   * @param mapTo String
   */
  public void setMapTo(String mapTo) {
    this.mapTo = mapTo;
  }

  /**
   * Sets the scope
   * @param scope String
   */
  public void setScope(String scope) {
    this.scope = scope;
  }

  /**
   * Sets the snowmedCd
   * @param snowmedCd String
   */
  public void setSnowmedCd(String snowmedCd) {
    this.snowmedCd = snowmedCd;
  }

  /**
   * Sets the vaCd
   * @param vaCd String
   */
  public void setVaCd(String vaCd) {
    this.vaCd = vaCd;
  }

  /**
   * Sets the metpathCd
   * @param metpathCd String
   */
  public void setMetpathCd(String metpathCd) {
    this.metpathCd = metpathCd;
  }

  /**
   * Sets the hcfaCd
   * @param hcfaCd String
   */
  public void setHcfaCd(String hcfaCd) {
    this.hcfaCd = hcfaCd;
  }

  /**
   * Sets the cdcCd
   * @param cdcCd String
   */
  public void setCdcCd(String cdcCd) {
    this.cdcCd = cdcCd;
  }

  /**
   * Sets the normRange
   * @param normRange String
   */
  public void setNormRange(String normRange) {
    this.normRange = normRange;
  }

  /**
   * Sets the exUsUnits
   * @param exUsUnits String
   */
  public void setExUsUnits(String exUsUnits) {
    this.exUsUnits = exUsUnits;
  }

  /**
   * Sets the ipccUnits
   * @param ipccUnits String
   */
  public void setIpccUnits(String ipccUnits) {
    this.ipccUnits = ipccUnits;
  }

  /**
   * Sets the gpiCd
   * @param gpiCd String
   */
  public void setGpiCd(String gpiCd) {
    this.gpiCd = gpiCd;
  }

  /**
   * Sets the reference
   * @param reference String
   */
  public void setReference(String reference) {
    this.reference = reference;
  }

  /**
   * Sets the exactCmpSy
   * @param exactCmpSy String
   */
  public void setExactCmpSy(String exactCmpSy) {
    this.exactCmpSy = exactCmpSy;
  }

  /**
   * Sets the molarMass
   * @param molarMass String
   */
  public void setMolarMass(String molarMass) {
    this.molarMass = molarMass;
  }

  /**
   * Sets the iupcAnltCd
   * @param iupcAnltCd String
   */
  public void setIupcAnltCd(String iupcAnltCd) {
    this.iupcAnltCd = iupcAnltCd;
  }

  /**
   * Sets the classtype
   * @param classtype String
   */
  public void setClasstype(String classtype) {
    this.classtype = classtype;
  }

  /**
   * Sets the formula
   * @param formula String
   */
  public void setFormula(String formula) {
    this.formula = formula;
  }

  /**
   * Sets the multumCd
   * @param multumCd String
   */
  public void setMultumCd(String multumCd) {
    this.multumCd = multumCd;
  }

  /**
   * Sets the deedsCd
   * @param deedsCd String
   */
  public void setDeedsCd(String deedsCd) {
    this.deedsCd = deedsCd;
  }

  /**
   * Sets the cscqFrnchNm
   * @param cscqFrnchNm String
   */
  public void setCscqFrnchNm(String cscqFrnchNm) {
    this.cscqFrnchNm = cscqFrnchNm;
  }

  /**
   * Sets the cscqGrmnNm
   * @param cscqGrmnNm String
   */
  public void setCscqGrmnNm(String cscqGrmnNm) {
    this.cscqGrmnNm = cscqGrmnNm;
  }

  /**
   * Sets the spnshNm
   * @param spnshNm String
   */
  public void setSpnshNm(String spnshNm) {
    this.spnshNm = spnshNm;
  }

  /**
   * Sets the cscqItlnNm
   * @param cscqItlnNm String
   */
  public void setCscqItlnNm(String cscqItlnNm) {
    this.cscqItlnNm = cscqItlnNm;
  }

  /**
   * Sets the species
   * @param species String
   */
  public void setSpecies(String species) {
    this.species = species;
  }

  /**
   * Sets the exmplAnswers
   * @param exmplAnswers String
   */
  public void setExmplAnswers(String exmplAnswers) {
    this.exmplAnswers = exmplAnswers;
  }

  /**
   * Sets the acssym
   * @param acssym String
   */
  public void setAcssym(String acssym) {
    this.acssym = acssym;
  }

  /**
   * Sets the moleid
   * @param moleid String
   */
  public void setMoleid(String moleid) {
    this.moleid = moleid;
  }

  /**
   * Sets the baseName
   * @param baseName String
   */
  public void setBaseName(String baseName) {
    this.baseName = baseName;
  }

  /**
   * Sets the _final
   * @param _final String
   */
  public void set_final(String _final) {
    this._final = _final;
  }

  /**
   * Sets the geneId
   * @param geneId String
   */
  public void setGeneId(String geneId) {
    this.geneId = geneId;
  }

  /**
   * Sets the naaccrId
   * @param naaccrId String
   */
  public void setNaaccrId(String naaccrId) {
    this.naaccrId = naaccrId;
  }

  /**
   * Sets the codeTable
   * @param codeTable String
   */
  public void setCodeTable(String codeTable) {
    this.codeTable = codeTable;
  }

  /**
   * Sets the setRoot
   * @param setRoot String
   */
  public void setSetRoot(String setRoot) {
    this.setRoot = setRoot;
  }

  /**
   * Sets the panelElements
   * @param panelElements String
   */
  public void setPanelElements(String panelElements) {
    this.panelElements = panelElements;
  }

  /**
   * Sets the relatedNames2
   * @param relatedNames2 String
   */
  public void setRelatedNames2(String relatedNames2) {
    this.relatedNames2 = relatedNames2;
  }

  /**
   * Sets the surveyQuestText
   * @param surveyQuestText String
   */
  public void setSurveyQuestText(String surveyQuestText) {
    this.surveyQuestText = surveyQuestText;
  }

  /**
   * Sets the surveyQuestSrc
   * @param surveyQuestSrc String
   */
  public void setSurveyQuestSrc(String surveyQuestSrc) {
    this.surveyQuestSrc = surveyQuestSrc;
  }

  /**
   * Sets the unitsRequired
   * @param unitsRequired String
   */
  public void setUnitsRequired(String unitsRequired) {
    this.unitsRequired = unitsRequired;
  }

  /**
   * Sets the shortname
   * @param shortname String
   */
  public void setShortname(String shortname) {
    this.shortname = shortname;
  }

  /**
   * Sets the commonTests
   * @param commonTests String
   */
  public void setCommonTests(String commonTests) {
    this.commonTests = commonTests;
  }

  /**
   * Sets the usEuProperty
   * @param usEuProperty String
   */
  public void setUsEuProperty(String usEuProperty) {
    this.usEuProperty = usEuProperty;
  }

  /**
   * Sets the orderObs
   * @param orderObs String
   */
  public void setOrderObs(String orderObs) {
    this.orderObs = orderObs;
  }

}
