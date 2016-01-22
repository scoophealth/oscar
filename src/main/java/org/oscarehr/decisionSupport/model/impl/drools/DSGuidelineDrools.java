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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.decisionSupport.model.impl.drools;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PostUpdate;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.drools.FactException;
import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.jdom.Element;
import org.jdom.Namespace;
import org.oscarehr.decisionSupport.model.DSCondition;
import org.oscarehr.decisionSupport.model.DSConsequence;
import org.oscarehr.decisionSupport.model.DSDemographicAccess;
import org.oscarehr.decisionSupport.model.DSGuideline;
import org.oscarehr.decisionSupport.model.DSParameter;
import org.oscarehr.decisionSupport.model.DecisionSupportException;
import org.oscarehr.drools.RuleBaseFactory;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

import oscar.oscarEncounter.oscarMeasurements.util.RuleBaseCreator;

/**
 *
 * @author apavel
 */
@Entity
@DiscriminatorValue("drools")
public class DSGuidelineDrools extends DSGuideline {
	private static final Logger log = MiscUtils.getLogger();

	public static final Namespace namespace = Namespace.getNamespace("http://drools.org/rules");
	public static final Namespace javaNamespace = Namespace.getNamespace("java", "http://drools.org/semantics/java");
	// public static final Namespace xsNs = Namespace.getNamespace("xs", "http://www.w3.org/2001/XMLSchema-instance");

	private static final String demographicAccessObjectClassPath = "org.oscarehr.decisionSupport.model.DSDemographicAccess";

	@Transient
	private RuleBase _ruleBase = null;
	@Transient
	int ruleCount = 0;

	public String getRuleBaseFactoryKey()
	{
		return("DSGuidelineDrools:"+getId());
	}
	
	public List<DSConsequence> evaluate(LoggedInInfo loggedInInfo, String demographicNo) throws DecisionSupportException {
		if (_ruleBase == null) generateRuleBase();
		//at this point _ruleBase WILL be set or exception is thrown in generateRuleBase()
		WorkingMemory workingMemory = _ruleBase.newWorkingMemory();
		DSDemographicAccess dsDemographicAccess = new DSDemographicAccess(loggedInInfo, demographicNo);
		//put "bob" in working memory
		try {

			workingMemory.assertObject(dsDemographicAccess);

			for (DSCondition dsc : this.getConditions()) {
				if (dsc.getParam() != null && !dsc.getParam().isEmpty()) {
					log.debug("PARAM:" + dsc.getParam().toString());
					workingMemory.assertObject(dsc.getParam());
				}
			}

			List<DSParameter> lDSP = this.getParameters();
			if (lDSP != null) {
				for (DSParameter dsp : lDSP) {
					Class clas = Class.forName(dsp.getStrClass());
					Constructor constructor = clas.getConstructor();
					Object obj = constructor.newInstance();

					workingMemory.assertObject(obj);
				}
			}

			workingMemory.fireAllRules();
			if (dsDemographicAccess.isPassedGuideline()) {
				List<DSConsequence> returnDsConsequences = new ArrayList<DSConsequence>();
				if (this.getConsequences() == null) return returnDsConsequences;
				else {
					for (DSConsequence dsConsequence : this.getConsequences()) {
						if (dsConsequence.getConsequenceType() != DSConsequence.ConsequenceType.java) {
							returnDsConsequences.add(dsConsequence);
						} else if (dsConsequence.getConsequenceType() == DSConsequence.ConsequenceType.java) {
							@SuppressWarnings("unchecked")
							List<Object> javaConsequences = workingMemory.getObjects();
							dsConsequence.setObjConsequence(javaConsequences);
							returnDsConsequences.add(dsConsequence);
						}
					}
					return returnDsConsequences;
				}
			} else {
				return null;
			}
		} catch (FactException factException) {
			throw new DecisionSupportException("Unable to assert guideline", factException);
		} catch (ClassNotFoundException e) {
			throw new DecisionSupportException("Unable to instantiate class", e);
		} catch (NoSuchMethodException e) {
			throw new DecisionSupportException("Unable to instantiate class", e);
		} catch (InstantiationException e) {
			throw new DecisionSupportException("Unable to instantiate class", e);
		} catch (IllegalAccessException e) {
			throw new DecisionSupportException("Unable to instantiate class", e);
		} catch (InvocationTargetException e) {
			throw new DecisionSupportException("Unable to instantiate class", e);
		}
	}

	public List<DSConsequence> evaluate(LoggedInInfo loggedInInfo, String demographicNo, String providerNo) throws DecisionSupportException {
		if (_ruleBase == null) generateRuleBase();
		//at this point _ruleBase WILL be set or exception is thrown in generateRuleBase()
		WorkingMemory workingMemory = _ruleBase.newWorkingMemory();
		DSDemographicAccess dsDemographicAccess = new DSDemographicAccess(loggedInInfo, demographicNo, providerNo);
		//put "bob" in working memory
		try {

			workingMemory.assertObject(dsDemographicAccess);

			for (DSCondition dsc : this.getConditions()) {
				if (dsc.getParam() != null && !dsc.getParam().isEmpty()) {
					log.debug("PARAM:" + dsc.getParam().toString());
					workingMemory.assertObject(dsc.getParam());
				}
			}

			List<DSParameter> lDSP = this.getParameters();
			if (lDSP != null) {
				for (DSParameter dsp : lDSP) {
					Class clas = Class.forName(dsp.getStrClass());
					Constructor constructor = clas.getConstructor();
					Object obj = constructor.newInstance();

					workingMemory.assertObject(obj);
				}
			}

			workingMemory.fireAllRules();
			if (dsDemographicAccess.isPassedGuideline()) {
				List<DSConsequence> returnDsConsequences = new ArrayList<DSConsequence>();
				if (this.getConsequences() == null) return returnDsConsequences;
				else {
					for (DSConsequence dsConsequence : this.getConsequences()) {
						if (dsConsequence.getConsequenceType() != DSConsequence.ConsequenceType.java) {
							returnDsConsequences.add(dsConsequence);
						} else if (dsConsequence.getConsequenceType() == DSConsequence.ConsequenceType.java) {
							@SuppressWarnings("unchecked")
							List<Object> javaConsequences = workingMemory.getObjects();
							dsConsequence.setObjConsequence(javaConsequences);
							returnDsConsequences.add(dsConsequence);
						}
					}
					return returnDsConsequences;
				}
			} else {
				return null;
			}
		} catch (FactException factException) {
			throw new DecisionSupportException("Unable to assert guideline", factException);
		} catch (ClassNotFoundException e) {
			throw new DecisionSupportException("Unable to instantiate class", e);
		} catch (NoSuchMethodException e) {
			throw new DecisionSupportException("Unable to instantiate class", e);
		} catch (InstantiationException e) {
			throw new DecisionSupportException("Unable to instantiate class", e);
		} catch (IllegalAccessException e) {
			throw new DecisionSupportException("Unable to instantiate class", e);
		} catch (InvocationTargetException e) {
			throw new DecisionSupportException("Unable to instantiate class", e);
		}
	}

	public List<DSConsequence> evaluate(LoggedInInfo loggedInInfo, String demographicNo, String providerNo, List<Object> dynamicArgs) throws DecisionSupportException {
		if (_ruleBase == null) generateRuleBase();
		//at this point _ruleBase WILL be set or exception is thrown in generateRuleBase()
		WorkingMemory workingMemory = _ruleBase.newWorkingMemory();
		DSDemographicAccess dsDemographicAccess = new DSDemographicAccess(loggedInInfo, demographicNo, providerNo, dynamicArgs);
		//put "bob" in working memory
		try {

			workingMemory.assertObject(dsDemographicAccess);

			for (DSCondition dsc : this.getConditions()) {
				if (dsc.getParam() != null && !dsc.getParam().isEmpty()) {
					log.debug("PARAM:" + dsc.getParam().toString());
					workingMemory.assertObject(dsc.getParam());
				}
			}

			List<DSParameter> lDSP = this.getParameters();
			if (lDSP != null) {
				for (DSParameter dsp : lDSP) {
					Class clas = Class.forName(dsp.getStrClass());
					Constructor constructor = clas.getConstructor();
					Object obj = constructor.newInstance();

					workingMemory.assertObject(obj);
				}
			}

			workingMemory.fireAllRules();
			if (dsDemographicAccess.isPassedGuideline()) {
				List<DSConsequence> returnDsConsequences = new ArrayList<DSConsequence>();
				if (this.getConsequences() == null) return returnDsConsequences;
				else {
					for (DSConsequence dsConsequence : this.getConsequences()) {
						if (dsConsequence.getConsequenceType() != DSConsequence.ConsequenceType.java) {
							returnDsConsequences.add(dsConsequence);
						} else if (dsConsequence.getConsequenceType() == DSConsequence.ConsequenceType.java) {
							@SuppressWarnings("unchecked")
							List<Object> javaConsequences = workingMemory.getObjects();
							dsConsequence.setObjConsequence(javaConsequences);
							returnDsConsequences.add(dsConsequence);
						}
					}
					return returnDsConsequences;
				}
			} else {
				return null;
			}
		} catch (FactException factException) {
			throw new DecisionSupportException("Unable to assert guideline", factException);
		} catch (ClassNotFoundException e) {
			throw new DecisionSupportException("Unable to instantiate class", e);
		} catch (NoSuchMethodException e) {
			throw new DecisionSupportException("Unable to instantiate class", e);
		} catch (InstantiationException e) {
			throw new DecisionSupportException("Unable to instantiate class", e);
		} catch (IllegalAccessException e) {
			throw new DecisionSupportException("Unable to instantiate class", e);
		} catch (InvocationTargetException e) {
			throw new DecisionSupportException("Unable to instantiate class", e);
		}
	}

	public void generateRuleBase() throws DecisionSupportException {
		long timer = System.currentTimeMillis();
		try {
			String ruleBaseFactoryKey=getRuleBaseFactoryKey();
			RuleBase result=RuleBaseFactory.getRuleBase(ruleBaseFactoryKey);
			if (result!=null) 
			{
				_ruleBase=result;
				return;
			}
			
			ArrayList<Element> rules = new ArrayList<Element>();
			ArrayList<Element> conditionElements = new ArrayList<Element>();
			ArrayList<Element> lParameterElements = new ArrayList<Element>();

			if (this.getParameters() != null) {
				for (DSParameter dsParameter : this.getParameters()) {
					Element parameterElement = this.getDroolsParameter(dsParameter);
					lParameterElements.add(parameterElement);
				}
			}
			int paramCount = 0;

			for (DSCondition condition : this.getConditions()) {
				if (condition.getParam() != null && !condition.getParam().isEmpty()) {
					condition.setLabel("param" + paramCount);
					paramCount++;
				}
				Element conditionElement = getDroolsCondition(condition);
				conditionElements.add(conditionElement);
			}

			Element consequencesElement = this.getDroolsConsequences(this.getConsequences());

			rules.add(this.getRule(conditionElements, lParameterElements, consequencesElement,ruleCount++));

			RuleBaseCreator ruleBaseCreator = new RuleBaseCreator();
			try {
				_ruleBase = ruleBaseCreator.getRuleBase(ruleBaseFactoryKey, rules);
				RuleBaseFactory.putRuleBase(ruleBaseFactoryKey, _ruleBase);
			} catch (Exception e) {
				throw new DecisionSupportException("Could not create a rule base for guideline '" + this.getTitle() + "'", e);
			}
		} finally {
			log.debug("generateRuleBase TimeMs : " + (System.currentTimeMillis() - timer));
		}
	}

	private Element getRule(List<Element> conditionElements, List<Element> parameterElements, Element consequenceElement,int ruleCount) {
		Element ruleElement = new Element("rule", DSGuidelineDrools.namespace);
		ruleElement.setAttribute("name", getRuleBaseFactoryKey()+"."+ruleCount);

		Element accessClassParameter = new Element("parameter", DSGuidelineDrools.namespace);
		accessClassParameter.setAttribute("identifier", "a");
		Element accessClass = new Element("class", DSGuidelineDrools.namespace);
		accessClass.addContent(demographicAccessObjectClassPath);
		accessClassParameter.addContent(accessClass);
		ruleElement.addContent(accessClassParameter);

		ruleElement.addContent(parameterElements);

		for (DSCondition condition : this.getConditions()) {
			if (condition.getParam() != null && !condition.getParam().isEmpty()) {
				Element paramsHashEle = new Element("parameter", DSGuidelineDrools.namespace);
				paramsHashEle.setAttribute("identifier", condition.getLabel());
				Element paramClass = new Element("class", DSGuidelineDrools.namespace);
				paramClass.addContent("java.util.Hashtable");
				paramsHashEle.addContent(paramClass);
				ruleElement.addContent(paramsHashEle);
			}
		}

		ruleElement.addContent(conditionElements);
		ruleElement.addContent(consequenceElement);
		return ruleElement;
	}

	private Element getRule(List<Element> conditionElements, Element consequenceElement,int ruleCount) {
		Element ruleElement = new Element("rule", DSGuidelineDrools.namespace);
		ruleElement.setAttribute("name", getRuleBaseFactoryKey()+"."+ruleCount);

		Element accessClassParameter = new Element("parameter", DSGuidelineDrools.namespace);
		accessClassParameter.setAttribute("identifier", "a");
		Element accessClass = new Element("class", DSGuidelineDrools.namespace);
		accessClass.addContent(demographicAccessObjectClassPath);
		accessClassParameter.addContent(accessClass);
		ruleElement.addContent(accessClassParameter);

		for (DSCondition condition : this.getConditions()) {
			if (condition.getParam() != null && !condition.getParam().isEmpty()) {
				Element paramsHashEle = new Element("parameter", DSGuidelineDrools.namespace);
				paramsHashEle.setAttribute("identifier", condition.getLabel());
				Element paramClass = new Element("class", DSGuidelineDrools.namespace);
				paramClass.addContent("java.util.Hashtable");
				paramsHashEle.addContent(paramClass);
				ruleElement.addContent(paramsHashEle);
			}
		}

		ruleElement.addContent(conditionElements);
		ruleElement.addContent(consequenceElement);
		return ruleElement;
	}

	protected Element getRule(Element conditionElement, Element consequenceElement,int ruleCounter) {
		List<Element> conditionElements = new ArrayList<Element>();
		conditionElements.add(conditionElement);
		return getRule(conditionElements, consequenceElement,ruleCounter);
	}

	//multiple conditions because to handle OR statements, need to have multiple
	public Element getDroolsCondition(DSCondition condition) {
		String accessMethod = condition.getConditionType().getAccessMethod();
		Element javaCondition = new Element("condition", DSGuidelineDrools.javaNamespace);
		String parameters = "\"" + StringUtils.join(condition.getValues(), ",") + "\"";
		accessMethod = accessMethod + StringUtils.capitalize(condition.getListOperator().name());
		String functionStr = "a." + accessMethod + "(" + parameters; // + ")";
		if (condition.getParam() != null && !condition.getParam().isEmpty()) {
			//functionStr += ",\"" +condition.getParam().toString()+"\"";
			functionStr += "," + condition.getLabel();
		}
		functionStr += ")";

		javaCondition.addContent(functionStr);
		return javaCondition;
	}

	public Element getDroolsParameter(DSParameter dsParameter) {
		Element accessClassParameter = new Element("parameter", DSGuidelineDrools.namespace);
		accessClassParameter.setAttribute("identifier", dsParameter.getStrAlias());
		Element accessClass = new Element("class", DSGuidelineDrools.namespace);
		accessClass.addContent(dsParameter.getStrClass());
		accessClassParameter.addContent(accessClass);

		return accessClassParameter;
	}

	public Element getDroolsConsequences(List<DSConsequence> consequences) {
		Element javaElement = new Element("consequence", DSGuidelineDrools.javaNamespace);
		String consequencesStr = "a.setPassedGuideline(true);";
		for (DSConsequence consequence : consequences) {
			if (consequence.getConsequenceType() == DSConsequence.ConsequenceType.java) {
				consequencesStr = consequencesStr + "\n" + consequence.getText();
			}
		}
		javaElement.addContent(consequencesStr);
		return javaElement;
	}

	@PostUpdate
	public void afterSave() {
		RuleBaseFactory.removeRuleBase(getRuleBaseFactoryKey());
	}
}
