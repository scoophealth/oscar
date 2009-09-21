/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.decisionSupport.model.impl.drools;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.drools.FactException;
import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.oscarehr.decisionSupport.model.DSCondition;
import org.oscarehr.decisionSupport.model.DSConsequence;
import org.oscarehr.decisionSupport.model.DSDemographicAccess;
import org.oscarehr.decisionSupport.model.DSGuideline;
import org.oscarehr.decisionSupport.model.DecisionSupportException;
import oscar.oscarEncounter.oscarMeasurements.util.RuleBaseCreator;


/**
 *
 * @author apavel
 */
public class DSGuidelineDrools extends DSGuideline {
    private static final Log log = LogFactory.getLog(RuleBaseCreator.class);
    
    Namespace namespace = Namespace.getNamespace("http://drools.org/rules");
    Namespace javaNamespace = Namespace.getNamespace("java", "http://drools.org/semantics/java");
    Namespace xsNs = Namespace.getNamespace("xs", "http://www.w3.org/2001/XMLSchema-instance");
    
    
    private final String demographicAccessObjectClassPath =  "org.oscarehr.decisionSupport.model.DSDemographicAccess";


    private RuleBase _ruleBase = null;

    public List<DSConsequence> evaluate(String demographicNo) throws DecisionSupportException {
        if (_ruleBase == null)
            generateRuleBase();
        //at this point _ruleBase WILL be set or exception is thrown in generateRuleBase()
        WorkingMemory workingMemory = _ruleBase.newWorkingMemory( );
        DSDemographicAccess dsDemographicAccess = new DSDemographicAccess(demographicNo);
        //put "bob" in working memory
        try {
            //System.out.println(dsDemographicAccess.getDemographicNo() + "NO");
            //System.out.println("a.isAgeAll(\"&gt;=5 y,&lt;611 y\")" + dsDemographicAccess.isAgeAll(">=5 y,<611 y"));
            //System.out.println("a.hasDxCodesAny(\"icd9:'4438',icd9:'1331'\")" + dsDemographicAccess.hasDxCodesAny("icd9:'4438',icd9:'1331'"));
            //System.out.println("isSexAny(\"F\")" + dsDemographicAccess.isSexAny("F"));
            //System.out.println("Note contains: " + dsDemographicAccess.noteContainsAny("test"));
            workingMemory.assertObject(dsDemographicAccess);

            for(DSCondition dsc :this.getConditions()){
                if (dsc.getParam() != null && !dsc.getParam().isEmpty()){
                    System.out.println("PARAM:"+dsc.getParam().toString());
                    workingMemory.assertObject(dsc.getParam());
                }
            }

            workingMemory.fireAllRules();
            if (dsDemographicAccess.isPassedGuideline()) {
                List<DSConsequence> returnDsConsequences = new ArrayList();
                if (this.getConsequences() == null) return returnDsConsequences;
                else {
                    for (DSConsequence dsConsequence: this.getConsequences()) {
                        if (dsConsequence.getConsequenceType() != DSConsequence.ConsequenceType.java)
                            returnDsConsequences.add(dsConsequence);
                    }
                    return returnDsConsequences;
                }
            } else {
                return null;
            }
        } catch (FactException factException) {
            throw new DecisionSupportException("Unable to assert guideline", factException);
        }
    }

    public void generateRuleBase() throws DecisionSupportException {
        ArrayList<Element> rules = new ArrayList();
        ArrayList<Element> conditionElements = new ArrayList();
        
        int paramCount=0;
       
        for (DSCondition condition: this.getConditions()) {
            if (condition.getParam() != null && !condition.getParam().isEmpty()){
                condition.setLabel("param"+paramCount);
                paramCount++;
            }
            Element conditionElement = getDroolsCondition(condition);
            conditionElements.add(conditionElement);
        }

        Element consequencesElement = this.getDroolsConsequences(this.getConsequences());

        rules.add(this.getRule(conditionElements, consequencesElement));
        
        RuleBaseCreator ruleBaseCreator = new RuleBaseCreator();
        try {
            XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
            xmlOutputter.output(rules, System.out);
            _ruleBase = ruleBaseCreator.getRuleBase(this.getTitle(), rules);
        } catch (Exception e) {
            throw new DecisionSupportException("Could not create a rule base for guideline '" + this.getTitle() + "'", e);
        }
    }

    private Element getRule(List<Element> conditionElements, Element consequenceElement) {
        Element ruleElement = new Element("rule", namespace);
        ruleElement.setAttribute("name", this.getTitle());

        Element accessClassParameter = new Element("parameter", namespace);
        accessClassParameter.setAttribute("identifier", "a");
        Element accessClass = new Element("class", namespace);
        accessClass.addContent(demographicAccessObjectClassPath);
        accessClassParameter.addContent(accessClass);
        ruleElement.addContent(accessClassParameter);

        
        for (DSCondition condition: this.getConditions()) {
            if (condition.getParam() != null && !condition.getParam().isEmpty()){
                Element paramsHashEle = new Element("parameter", namespace);
                paramsHashEle.setAttribute("identifier", condition.getLabel());
                Element paramClass = new Element("class", namespace);
                paramClass.addContent("java.util.Hashtable");
                paramsHashEle.addContent(paramClass);
                ruleElement.addContent(paramsHashEle);   
            }
        }

        ruleElement.addContent(conditionElements);
        ruleElement.addContent(consequenceElement);
        return ruleElement;
    }

    private Element getRule(Element conditionElement, Element consequenceElement) {
        List<Element> conditionElements = new ArrayList();
        conditionElements.add(conditionElement);
        return getRule(conditionElements, consequenceElement);
    }
    
    //multiple conditions because to handle OR statements, need to have multiple 
    public Element getDroolsCondition(DSCondition condition) throws DecisionSupportException {
        String accessMethod = condition.getConditionType().getAccessMethod();
        Element javaCondition = new Element("condition", javaNamespace);
        String parameters = "\"" + StringUtils.join(condition.getValues(), ",") + "\"";
        accessMethod = accessMethod + StringUtils.capitalize(condition.getListOperator().name());
        String functionStr = "a." + accessMethod + "(" + parameters; // + ")";
        if( condition.getParam() != null && !condition.getParam().isEmpty()){
            //functionStr += ",\"" +condition.getParam().toString()+"\"";
            functionStr += ","+condition.getLabel();
        }
        functionStr += ")";

        javaCondition.addContent(functionStr);
        return javaCondition;
    }

    public Element getDroolsConsequences(List<DSConsequence> consequences) throws DecisionSupportException {
        Element javaElement = new Element("consequence", javaNamespace);
        String consequencesStr = "System.out.println(\"FINISHED\"); a.setPassedGuideline(true);";
        for (DSConsequence consequence: consequences) {
            if (consequence.getConsequenceType() == DSConsequence.ConsequenceType.java) {
                consequencesStr = consequencesStr + "\n" + consequence.getText();
            }
        }
        javaElement.addContent(consequencesStr);
        return javaElement;
    }

}

/* exmaple:
 *     <rule name="A1C">
        <parameter identifier="m">
            <class>oscar.oscarEncounter.oscarMeasurements.util.MeasurementDSHelper</class>
        </parameter>
        <java:condition>m.getDataAsDouble() &gt;= 7</java:condition>
        <java:consequence>
              System.out.println("A1C RULES IS GETTING RUN");
              m.setIndicationColor("HIGH");
        </java:consequence>
    </rule>
 * */
