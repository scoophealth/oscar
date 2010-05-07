/*
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 *  This software is published under the GPL GNU General Public License.
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 *  Jason Gallagher
 *
 *  This software was written for the
 *  Department of Family Medicine
 *  McMaster University
 *  Hamilton
 *  Ontario, Canada
 *
 * RenalDosingFactory.java
 *
 * Created on December 21, 2006, 11:13 AM
 *
 */
package oscar.oscarRx.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.filter.ElementFilter;
import org.jdom.input.SAXBuilder;

/**
 * Parses xml file, creating DosingRecomendation Objects storing them in a hashtable with the ATC code as the key
 * @author jay
 */
public class LimitedUseLookup {

    private static Log log = LogFactory.getLog(LimitedUseLookup.class);

    static Hashtable<String, ArrayList> luLookup = new Hashtable();
    static boolean loaded = false;

    /** Creates a new instance of RenalDosingFactory */
    protected LimitedUseLookup() {
    }

    static public ArrayList<LimitedUseCode> getLUInfoForDin(String din) {
        loadLULookupInformation();
        if (din == null){
            return null;
        }
        return luLookup.get(din);
    }

    static public LimitedUseCode makeLUNote(Element e) {
        LimitedUseCode lu = new LimitedUseCode();
        lu.setSeq(getVal(e, "seq"));
        lu.setUseId(getVal(e, "reasonForUseId"));
        lu.setType(getVal(e, "type"));
        lu.setTxt(e.getText());
        return lu;
    }

    static public String getVal(Element e, String name) {
        if (e.getAttribute(name) != null) {
            return e.getAttribute(name).getValue();
        }
        return "";
    }
    static private void loadLULookupInformation() {
        log.debug("current LU lookup size " + luLookup.size());
        if (!loaded) {
            String dosing = "oscar/oscarRx/data_extract_20100422.xml";
            LimitedUseLookup rdf = new LimitedUseLookup();
            InputStream is = rdf.getClass().getClassLoader().getResourceAsStream(dosing);

            try {
                SAXBuilder parser = new SAXBuilder();
                Document doc = parser.build(is);
                Element root = doc.getRootElement();
                Element formulary = root.getChild("formulary");
                Iterator<Element> items = formulary.getDescendants(new ElementFilter("pcgGroup"));

                while (items.hasNext()) {
                    Element pcgGroup = items.next();
                    List<Element> lccNoteList = pcgGroup.getChildren("lccNote");

                    if (lccNoteList.size() > 0) {
                        ArrayList<LimitedUseCode> luList = new ArrayList();
                        for (Element lccNo : lccNoteList) {
                            luList.add(makeLUNote(lccNo));
                        }

                        Iterator<Element> drugs = pcgGroup.getDescendants(new ElementFilter("drug"));
                        while (drugs.hasNext()) {
                            Element drug = drugs.next();
                            String din = drug.getAttribute("id").getValue();
                            luLookup.put(din, luList);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            loaded = true;
        }

    }
}
