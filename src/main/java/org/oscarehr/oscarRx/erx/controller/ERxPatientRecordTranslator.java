/**
 * Copyright (C) 2011-2012  PeaceWorks Technology Solutions
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


package org.oscarehr.oscarRx.erx.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.oscarehr.common.model.Demographic;
import org.oscarehr.oscarRx.erx.model.ERxPatientData;

/**
 * An object that translates between Patient objects and ERxPatientData objects.
 * 
 * FUTURE: org.oscarehr.oscarRx.erx should, in the future, support more than one
 * external prescription service, which is unlikely to use the current patient
 * data model. When this support is added, this class should be renamed to
 * org.oscarehr.oscarRx.erx.PatientRecordTranslator, which should
 * implement a new interface named
 * org.oscarehr.oscarRx.erx.controller.ERxPatientRecordTranslator. This new
 * interface should define two methods: ERxPatientData
 * translateToExternal(Patient) and Patient translateToInternal(ERxPatientData).
 */
public class ERxPatientRecordTranslator {
    /**
     * Generate an ERxPatientData given a Patient.
     * 
     * @param patient
     *            The patient data to translate
     * @return The translated patient data
     */
    public static ERxPatientData translateToExternal(Demographic patient)
            throws NumberFormatException {
        ERxPatientData out;
        SimpleDateFormat df = new SimpleDateFormat();
        Date birthdate;
        
        // Use basic constructor to set mandatory fields
        out = new ERxPatientData(patient.getDemographicNo().toString(), patient.getFirstName(),
                patient.getLastName());
       
        out.setPhone1(patient.getPhone());
        out.setGender(patient.getSex());
        out.setAddress(patient.getAddress());
        out.setCity(patient.getCity());
        out.setState(patient.getProvince());
        out.setZipCode(patient.getPostal());
        out.setPhone2(patient.getPhone2());
        out.setChartNumber(patient.getChartNo());

        // Set more-complex fields
        // Note that months in GregorianCalendar are 0-based (0 = January)
        birthdate = patient.getBirthDay().getTime();
        out.setValidBirthDate(birthdate);
        out.setBirthdate(df.format(birthdate));

        return out;
    }
}
