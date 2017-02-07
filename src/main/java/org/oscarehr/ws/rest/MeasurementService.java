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
package org.oscarehr.ws.rest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.oscarehr.common.model.Measurement;
import org.oscarehr.managers.MeasurementManager;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.ws.rest.to.MeasurementResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Path("/measurements")
@Component("measurementService")
public class MeasurementService extends AbstractServiceImpl {
    @Autowired
    private SecurityInfoManager securityInfoManager;
    @Autowired
    private MeasurementManager measurementManager;

    @POST
    @Path("/{demographicNo}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public MeasurementResponse getMeasurements(JSONObject json, @PathParam("demographicNo") Integer demoId) {
        if(!securityInfoManager.hasPrivilege(getLoggedInInfo(), "_measurement", "r", null)) {
            throw new SecurityException("Access Denied: Missing required security object (_measurement)");
        }
        MeasurementResponse response = new MeasurementResponse();
        JSONArray jsonArray = json.getJSONArray("types");
        String[] types = (String[]) JSONArray.toArray(jsonArray, String.class);
        if (types.length < 1) {
            return response;
        }

        List<Measurement> measurements = measurementManager.getMeasurementByType(getLoggedInInfo(), demoId, new ArrayList<String>(Arrays.asList(types)));
        response.addMeasurements(measurements);
        return response;
    }
}
