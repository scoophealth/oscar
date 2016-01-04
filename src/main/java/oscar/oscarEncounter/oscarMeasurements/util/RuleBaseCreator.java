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

package oscar.oscarEncounter.oscarMeasurements.util;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.drools.RuleBase;
import org.drools.io.RuleBaseLoader;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.oscarehr.drools.RuleBaseFactory;
import org.oscarehr.util.MiscUtils;

/**
 * Class used to create Drools XML files
 * @author jaygallagher
 */
public class RuleBaseCreator {
	private static final Logger log = MiscUtils.getLogger();

	Namespace namespace = Namespace.getNamespace("http://drools.org/rules");
	Namespace javaNamespace = Namespace.getNamespace("java", "http://drools.org/semantics/java");
	Namespace xsNs = Namespace.getNamespace("xs", "http://www.w3.org/2001/XMLSchema-instance");

	public RuleBase getRuleBase(String rulesetName, List<Element> elementRules) throws Exception {
		long timer = System.currentTimeMillis();
		try {
			Element va = new Element("rule-set");

			addAttributeifValueNotNull(va, "name", rulesetName);

			va.setNamespace(namespace);
			va.addNamespaceDeclaration(javaNamespace);
			va.addNamespaceDeclaration(xsNs);
			va.setAttribute("schemaLocation", "http://drools.org/rules rules.xsd http://drools.org/semantics/java java.xsd", xsNs);

			for (Element ele : elementRules) {
				va.addContent(ele);
			}

			XMLOutputter outp = new XMLOutputter();
			outp.setFormat(Format.getPrettyFormat());
			String ooo = outp.outputString(va);

			log.debug(ooo);
			
			RuleBase ruleBase=RuleBaseFactory.getRuleBase("RuleBaseCreator:"+ooo);
			if (ruleBase!=null) return(ruleBase);
			
			ruleBase = RuleBaseLoader.loadFromInputStream(new ByteArrayInputStream(ooo.getBytes()));
			RuleBaseFactory.putRuleBase("RuleBaseCreator:"+ooo, ruleBase);
			return ruleBase;
		} finally {
			log.debug("generateRuleBase TimeMs : " + (System.currentTimeMillis() - timer));
		}
	}

	public void test() {

		ArrayList elementList = new ArrayList();
		ArrayList list = new ArrayList();

		list.add(new DSCondition("getLastDateRecordedInMonths", "REBG", ">=", "3"));
		list.add(new DSCondition("getLastDateRecordedInMonths", "REBG", "<", "6"));

		Element ruleElement = getRule("REBG1", "oscar.oscarEncounter.oscarMeasurements.MeasurementInfo", list, "MiscUtils.getLogger().debug(\"REBG 1 getting called\");");
		elementList.add(ruleElement);

		list = new ArrayList();
		list.add(new DSCondition("getLastDateRecordedInMonths", "REBG", ">", "6"));
		ruleElement = getRule("REBG2", "oscar.oscarEncounter.oscarMeasurements.MeasurementInfo", list, "MiscUtils.getLogger().debug(\"REBG 1 getting called\");");
		elementList.add(ruleElement);

		list = new ArrayList();
		list.add(new DSCondition("getLastDateRecordedInMonths", "REBG", "==", "-1"));
		ruleElement = getRule("REBG3", "oscar.oscarEncounter.oscarMeasurements.MeasurementInfo", list, "MiscUtils.getLogger().debug(\"REBG 1 getting called\");");
		elementList.add(ruleElement);
	}

	void addAttributeifValueNotNull(Element element, String attr, String value) {
		if (value != null) {
			element.setAttribute(attr, value);
		}
	}

	public Element getRule(String ruleName, String incomingClass, List<DSCondition> conditions, String consequence) {
		Element rule = new Element("rule", namespace);
		addAttributeifValueNotNull(rule, "name", ruleName);
		Element param = new Element("parameter", namespace);
		addAttributeifValueNotNull(param, "identifier", "m");
		Element classEle = new Element("class", namespace);
		classEle.setText(incomingClass);

		rule.addContent(param);
		param.addContent(classEle);

		for (DSCondition cond : conditions) {
			Element condElement = new Element("condition", javaNamespace);
			condElement.setText("m." + cond.getType() + " " + cond.getComparision() + " " + cond.getValue());
			rule.addContent(condElement);
		}

		Element conseq = new Element("consequence", javaNamespace);
		conseq.addContent(consequence);

		rule.addContent(conseq);
		log.debug("Return Rule" + rule);
		return rule;
	}
}
