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
package org.oscarehr.ws.rest.to;

import org.oscarehr.common.model.Measurement;
import org.oscarehr.e2e.constant.Mappings;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XmlRootElement
public class MeasurementResponse implements Serializable {
    private Map<String, List<Measurement>> measurementMap = new HashMap<String, List<Measurement>>();

    public void addMeasurements(List<Measurement> measurementList) {
        for (Measurement measurement : measurementList) {
            String type = measurement.getType();
            if ( !measurementMap.containsKey(type) ) {
                measurementMap.put(type, new ArrayList<Measurement>());
            }
            measurementMap.get(type).add(measurement);
        }
    }

    public Map<String, List<Measurement>> getMeasurements() {
        return measurementMap;
    }

    public Map<String, String> getMeasurementUnits() {
        return Mappings.measurementUnitMap;
    }
}
