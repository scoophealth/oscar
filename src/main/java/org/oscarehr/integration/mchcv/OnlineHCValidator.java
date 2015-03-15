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
package org.oscarehr.integration.mchcv;

import ca.ontario.health.hcv.Faultexception;
import ca.ontario.health.hcv.HCValidation;
import ca.ontario.health.hcv.HcvRequest;
import ca.ontario.health.hcv.HcvResults;
import ca.ontario.health.hcv.Person;
import ca.ontario.health.hcv.Requests;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.commons.lang.time.DateFormatUtils;
import org.oscarehr.integration.ebs.client.EdtClientBuilder;
import org.oscarehr.integration.ebs.client.EdtClientBuilderConfig;
import oscar.OscarProperties;

public class OnlineHCValidator implements HCValidator {

    private HCValidation validation;

    public OnlineHCValidator() {
        init();
    }

    private void init() {

        OscarProperties properties = OscarProperties.getInstance();
        EdtClientBuilderConfig config = new EdtClientBuilderConfig();
        config.setLoggingRequired(!Boolean.valueOf(properties.getProperty("hcv.logging.skip")));
        config.setKeystoreUser(properties.getProperty("hcv.keystore.user"));
        config.setKeystorePassword(properties.getProperty("hcv.keystore.pass"));
        config.setUserNameTokenUser(properties.getProperty("hcv.service.user"));
        config.setUserNameTokenPassword(properties.getProperty("hcv.service.pass"));
        config.setServiceUrl(properties.getProperty("hcv.service.url"));
        config.setConformanceKey(properties.getProperty("hcv.service.conformanceKey"));
        config.setServiceId(properties.getProperty("hcv.service.id"));

        EdtClientBuilder builder = new EdtClientBuilder(config);
        validation = builder.build(HCValidation.class);
    }

    @Override
    public HCValidationResult validate(String healthCardNumber, String versionCode) {
        Requests requests = new Requests();
        HcvRequest request = new HcvRequest();
        request.setHealthNumber(healthCardNumber);
        request.setVersionCode(versionCode);
        requests.getHcvRequest().add(request);
        HcvResults results = null;
        try {
            results = validation.validate(requests, "en");
        } catch (Faultexception ex) {
            throw new RuntimeException(ex.getMessage());
        }

        List persons = results.getResults();
        Person person = (Person) persons.get(0);

        HCValidationResult result = new HCValidationResult();
        result.setResponseCode(person.getResponseCode());
        result.setResponseDescription(person.getResponseDescription());
        result.setResponseAction(person.getResponseAction());
        result.setFirstName(person.getFirstName());
        result.setLastName(person.getLastName());
        result.setGender(person.getGender());

        String birthDate = null;
        XMLGregorianCalendar xmlBirthDate = person.getDateOfBirth();
        if (xmlBirthDate != null) {
            birthDate = makeDate(xmlBirthDate.getYear(), xmlBirthDate.getMonth(), xmlBirthDate.getDay());
        }
        result.setBirthDate(birthDate);

        String expiryDate = null;
        XMLGregorianCalendar xmlExpiryDate = person.getExpiryDate();
        if (xmlExpiryDate != null) {
            expiryDate = makeDate(xmlExpiryDate.getYear(), xmlExpiryDate.getMonth(), xmlExpiryDate.getDay());
        }
        result.setExpiryDate(expiryDate);        
        return result;
    }

    private String makeDate(int year, int month, int day) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(year, month - 1, day, 0, 0, 0);
        return DateFormatUtils.format(calendar, "yyyyMMdd");
    }
}
