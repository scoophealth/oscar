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

import java.util.ArrayList;
import java.util.List;

import org.oscarehr.ws.rest.to.model.DrugSearchTo1;

public class MockDrugLookUpManager extends DrugLookUpManager{

    public MockDrugLookUpManager(){

    }

    public List<DrugSearchTo1> search(String name){

        List<DrugSearchTo1> toReturn = new ArrayList<DrugSearchTo1>();

        // we treat aspirin as a special case for testing purposes.
        if(name.equals("aspirin")){
            DrugSearchTo1 d = new DrugSearchTo1();
            d.setName("aspirin");
            d.setId(1);
            toReturn.add(d);
        }

        return toReturn;

    }

    public DrugSearchTo1 details(String id){

        DrugSearchTo1 s = new DrugSearchTo1();

        if(id.equals("1")){
            List<DrugSearchTo1.DrugComponentTo1> components = new ArrayList<DrugSearchTo1.DrugComponentTo1>();
            DrugSearchTo1.DrugComponentTo1 c = new DrugSearchTo1.DrugComponentTo1();
            c.setStrength(1.0);
            c.setUnit("mg");
            components.add(c);
            s.setComponents(components);
            return s;
        }

        return null;

    }

}
