/**
 * Copyright (c) 2013-2015. Department of Computer Science, University of Victoria. All Rights Reserved.
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
 * Department of Computer Science
 * LeadLab
 * University of Victoria
 * Victoria, Canada
 */
package org.oscarehr.managers;


import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.ws.rest.to.model.DrugSearchTo1;
import org.springframework.stereotype.Service;
import oscar.oscarRx.util.RxDrugRef;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

@Service
public class DrugLookUpManager implements DrugLookUp {

    private static Logger logger = MiscUtils.getLogger();

    public List<DrugSearchTo1> search(String s) {

        RxDrugRef dr = new RxDrugRef();

        List<DrugSearchTo1> drugs = new ArrayList<DrugSearchTo1>();

        try {

            // has structure: {isInactive=BOOLEAN, name=STRING, category=INT, id=INT},


            // This isn't the best approach to managing results from the RxDrugRef
            // we should have a hashtable type.
            // Would require refactor to the RxDrugRef class.
            Vector<Hashtable> v = (Vector<Hashtable>) dr.list_drug_element3(s, false);

            DrugSearchTo1 temp;

            for (Hashtable h : v) {

                temp = new DrugSearchTo1();
                temp.setName((String) h.get("name"));
                temp.setActive(!((Boolean) h.get("isInactive")));
                temp.setId((Integer) h.get("id"));
                temp.setCategory((Integer) h.get("category"));

                drugs.add(temp);

            }

        } catch (Exception e) {
            logger.error(e.getStackTrace());
            return null;
        }

        return drugs;

    }

    public DrugSearchTo1 details(String id) throws Exception{

        RxDrugRef dr = new RxDrugRef();

        DrugSearchTo1 toReturn = new DrugSearchTo1();

        // TODO: This is not finished! Needs more work once drug ref gets better.

        // { name=STRING, atc=STRING, product=STRING, regional_identifier=STRING, components=VECTOR, drugForm=STRING }

        Hashtable drug = dr.getDrug2(id, true);

        this.extractAndPopulateDetails(toReturn, drug, id);

        return toReturn;

    }

    protected void extractAndPopulateDetails(DrugSearchTo1 t, Hashtable h, String id) throws Exception {

        RxDrugRef dr = new RxDrugRef();

        t.setAtc((String) h.get("atc"));
        t.setRegionalId(Integer.parseInt((String) h.get("regional_identifier")));
        t.setForm((String) h.get("drugForm"));
        t.setName((String) h.get("name"));

        // Component: { name=STRING, strength=INT, unit=STRING }
        Vector<Hashtable<String, Object>> components = (Vector<Hashtable<String, Object>>) h.get("components");

        DrugSearchTo1.DrugComponentTo1 comp;

        for (Hashtable<String, Object> o : components) {
            comp = new DrugSearchTo1.DrugComponentTo1();
            comp.setName((String) o.get("name"));
            comp.setStrength((Double) o.get("strength"));
            comp.setUnit((String) o.get("unit"));
            t.addComponent(comp);
        }

        // Generic Name : { name=STRING, category=INT, id=INT }
        Hashtable generic = dr.getGenericName(id);
        String gn = (String) generic.get("name");

        // ugh, this is gross...
        // drugref should return null if nothing is found.
        if (gn.toLowerCase().equals("none found")) {

            // if there is no generic name found then we try the following in order:
            //  1) the active ingredient
            //  2) the name of the drug
            //
            // This solution is not ideal, drugref seems to do some weird things....

            if (t.getComponents().size() >= 1) {
                t.setGenericName(t.getComponents().get(0).getName());
            } else {
                t.setGenericName(t.getName());
            }

        } else {
            t.setGenericName(gn);
        }

    }
}
