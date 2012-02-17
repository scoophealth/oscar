
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oscarehr.casemgmt.common;

import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;

/**
 *
 * @author jackson
 */
public class Colour {

	public static Colour getInstance() {
		Colour c = null;
		try {
			String colourClass = OscarProperties.getInstance().getProperty("ColourClass", "org.oscarehr.casemgmt.common.Colour");
			if(colourClass.length()>0) {
				c = (Colour)Class.forName(colourClass).newInstance();
			}
		}catch(Exception e) {
			MiscUtils.getLogger().error("Error",e);
		}

		if(c == null)
			return new Colour();
		return c;
	}

    public String prevention = "009999";
    public String tickler = "FF6600";
    public  String disease = "5A5A5A";
    public  String forms = "917611";
    public  String eForms = "008000";
    public  String documents = "476BB3";
    public  String labs = "A0509C";
    public  String messages = "7F462C";
    public  String measurements = "344887";
    public  String rx="7D2252";
    public  String allergy="C85A17";
    public  String omed="306754";
    public  String riskFactors="993333";
    public  String familyHistory="006600";
    public  String unresolvedIssues="CC9900";
    public  String resolvedIssues="151B8D";
    public  String socialHistory="996633";
    public  String medicalHistory="996633";
    public  String ongoingConcerns="996633";
    public  String reminders="996633";
    public  String invoices="254117";
    public  String consultation="6C2DC7";
}
