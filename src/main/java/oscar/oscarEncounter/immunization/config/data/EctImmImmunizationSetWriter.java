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


package oscar.oscarEncounter.immunization.config.data;

import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import oscar.util.UtilXML;

public class EctImmImmunizationSetWriter
{

    public EctImmImmunizationSetWriter()
    {
        headers = "true";
    }

    public void doWrite(int flag, Vector headingVector, Vector immunizationVector, Vector yearAgeVector, String setName, String providerNo)
    {
        if(flag == 0)
            headers = "false";
        Document doc = UtilXML.newDocument();
        Element root = UtilXML.addNode(doc, "immunizationSet");
        root.setAttribute("headers", headers);
        root.setAttribute("name", setName);
        if(flag == 1)
        {
            Element columnList = UtilXML.addNode(root, "columnList");
            for(int i = 0; i < headingVector.size(); i++)
            {
                EctImmHeading hHead = (EctImmHeading)headingVector.elementAt(i);
                Element column = UtilXML.addNode(columnList, "column");
                column.setAttribute("name", hHead.headVal);
            }

        }
        Element rowList = UtilXML.addNode(root, "rowList");
        for(int i = 0; i < immunizationVector.size(); i++)
        {
            EctImmImmunizations immunizations = (EctImmImmunizations)immunizationVector.elementAt(i);
            immunizations.sortAgeVector();
            Vector indexes = immunizations.indexAge;
            Element row = UtilXML.addNode(rowList, "row");
            row.setAttribute("name", immunizations.immunizationName);
            for(int j = 0; j < indexes.size(); j++)
            {
                String ind = (String)indexes.elementAt(j);
                Element cell = UtilXML.addNode(row, "cell");
                cell.setAttribute("index", ind);
            }

        }

        EctImmImmunizationSetData immunizationSetData = new EctImmImmunizationSetData();
        immunizationSetData.addImmunizationSet(setName, doc, providerNo);
    }

    String headers;
}
