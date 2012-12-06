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


package oscar.decision;

import java.util.Properties;

import org.oscarehr.util.MiscUtils;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class DesAnnualReviewPlannerRiskHandler extends DefaultHandler {

    private Locator locator;
    private String results, riskName; //currentElement
    private int colcount = 0;
    private int numcols = 1;
    private int interiortable = 0;
    private int href = 0;
    private Properties riskNameObj = null;

    //	private Hashtable savedparams;

    public void setDocumentLocator(Locator locator) {

        this.locator = locator;
    }

    public void startDocument() throws SAXException {

        riskNameObj = new Properties();
        results = "<center><table width='100%' border='0' cellpadding='0' CELLSPACING='0' BGCOLOR='ivory'><tr><td>\n";
    }

    public void endDocument() throws SAXException {

        if (interiortable == 1) { //close content table
            results += "</center></td></tr></table>";
        }
        results += "</td></tr></table></center>\n";
    }

    public void processingInstruction(String target, String data) throws SAXException {
        MiscUtils.getLogger().debug("PI: Target: " + target + " and Data: " + data);
    }


    public void startElement(String namespaceURI, String localName, String rawName, Attributes atts)
            throws SAXException {
        if (rawName.equals("section_title")) {
            if (interiortable == 1) { //close content table
                results += "</center></td></tr></table>";
                interiortable = 0;
                colcount = 0;
            }
            results += "<table border=0 cellspacing=1 cellpadding=1 width=\"100%\">\n";
            results += "<tr><td BGCOLOR='#009966' align='center'>\n";
            results += "<font size=-1 color='#FFFFFF'>";
        } else if (rawName.equals("subsection_title")) {
            if (interiortable == 1) { //close table
                results += "</center></td></tr></table>";
                interiortable = 0;
                colcount = 0;
            }
            results += "<center><table border=0 cellpadding=0 cellspacing=0 width=\"98%\">";
            results += "<tr><td BGCOLOR='#CCFFCC' align='center'>\n";
            results += "<font size=-1>";
        } else if (rawName.equals("risk") || rawName.equals("entry")) {
            if (interiortable == 0) { //table beginning
                results += "<center><table border=0 cellpadding=1 cellspacing=1 width=\"98%\" datasrc='#xml_risklist'>";
                interiortable = 1;
                colcount = 0;
            }
            if (colcount == 0)
                results += "<tr><td width=" + 100 / numcols + "% >"; //the first td
            if (colcount % numcols == 0) { //tr td new line beginning?
                results += "</td></tr>\n<tr><td width=" + 100 / numcols + "% >";
            } else {
                results += "</td><td width=" + 100 / numcols + "% >";
            }
            results += "<font size=-2>";
            colcount += 1;
        } else if (rawName.equals("heading")) {
            if (interiortable == 1) { //close table
                results += "</center></td></tr></table>";
                interiortable = 0;
                colcount = 0;
            }
            results += "<table border=0 cellpadding=0 cellspacing=0 width=\"98%\">";
            results += "<tr><td align='center'><font size=-2><b>\n";
        }
        for (int i = 0; i < atts.getLength(); i++) {
            if (atts.getQName(i) == "name") {
                riskName = atts.getValue(i);
                riskNameObj.setProperty(riskName, "risk_" + riskName);
            }
            if (atts.getQName(i) == "href") {
                results += "<a href=# onClick=\"popupPage(400,500,'" + atts.getValue(i) + "');return false;\">";
                href = 1; //there is a href there
            }

        }
    }

    public void endElement(String namespaceURI, String localName, String rawName) throws SAXException {
        if (href == 1) {
            results += "</a>";
            href = 0;
        }
        if (rawName.equals("section_title")) {
            results += "</font></td></tr></table>\n";
        } else if (rawName.equals("subsection_title")) {
            results += "</font></td></tr></table></center>\n";
        } else if (rawName.equals("heading")) {
            results += "</b></font></td></tr></table>\n";
        } else if (rawName.equals("risk")) {
            results += "</font></td><td><input type=checkbox name=\"xml_" + riskName
                    + "\" value='checked' id='xml_" + riskName + "'>";
            riskName = "";
        } else if (rawName.equals("entry")) {
            results += "</font></td><td><input type=text size=6 name=\"xml_" + riskName + "\" id='xml_"
                    + riskName + "'>";
            riskName = "";
        }

    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        String s = new String(ch, start, length);

        results += s;
    }

    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        String s = new String(ch, start, length);

    }

    public void skippedEntity(String name) throws SAXException {
        MiscUtils.getLogger().debug("Skipping entity " + name);
    }

    public String getResults() {
        return results;
    }

    public Properties getRiskNameObj() {
        return riskNameObj;
    }

}
