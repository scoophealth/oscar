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


package org.oscarehr.integration.cdx;

import ca.uvic.leadlab.obibconnector.facades.Config;
import ca.uvic.leadlab.obibconnector.facades.datatypes.*;
import ca.uvic.leadlab.obibconnector.impl.send.SubmitDoc;
import ca.uvic.leadlab.obibconnector.models.document.ClinicalDocument;
import ca.uvic.leadlab.obibconnector.models.queries.SearchClinicCriteria;
import ca.uvic.leadlab.obibconnector.models.queries.SearchDocumentCriteria;
import ca.uvic.leadlab.obibconnector.models.queries.SearchProviderCriteria;
import ca.uvic.leadlab.obibconnector.models.response.ListClinicsResponse;
import ca.uvic.leadlab.obibconnector.models.response.ListDocumentsResponse;
import ca.uvic.leadlab.obibconnector.models.response.ListProvidersResponse;
import ca.uvic.leadlab.obibconnector.models.response.SubmitDocumentResponse;
import ca.uvic.leadlab.obibconnector.rest.IOscarInformation;
import ca.uvic.leadlab.obibconnector.rest.OBIBRequestException;
import ca.uvic.leadlab.obibconnector.rest.RestClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.util.MiscUtils;

import java.util.Date;

public class TestRestClient {

    private ObjectMapper mapper = new ObjectMapper();
    private String obibUrl = "http://192.168.100.101:8081";
    private String clinicId = "cdxpostprod-otca";

    private Config config = new Config() {
        @Override
        public String getUrl() {
            return obibUrl;
        }

        @Override
        public String getClinicId() {
            return clinicId;
        }
    };

    @BeforeClass
    public static void beforeClass() throws Exception {
        SchemaUtils.restoreTable("cdx_provenance", "cdx_attachment");
    }

    @Test
    public void testSubmitDocument() throws Exception {
        IOscarInformation restClient = new RestClient(obibUrl, clinicId);

        ClinicalDocument document = ((SubmitDoc) new SubmitDoc(config).newDoc()
                .documentType(DocumentType.REFERRAL_NOTE)
                .patient()
                    .id("2222")
                    .name(NameType.LEGAL, "Joe", "Wine")
                    .address(AddressType.HOME, "111 Main St", "Victoria", "BC", "V8V Z9Z", "CA")
                    .phone(TelcoType.HOME, "250-111-1234")
                    .gender(Gender.MALE)
                    .birthday("1980", "08", "19")
                .and().author()
                    .id("3333")
                    .time(new Date())
                    .name(NameType.LEGAL, "Joseph", "Cloud")
                    .address(AddressType.HOME, "111 Main St", "Victoria", "BC", "V8V Z9Z", "CA")
                    .phone(TelcoType.HOME, "250-111-1234")
                .and().recipient()
                    .primary()
                    .id("4444")
                    .name(NameType.LEGAL, "Joseph", "Cloud")
                    .address(AddressType.HOME, "111 Main St", "Victoria", "BC", "V8V Z9Z", "CA")
                    .phone(TelcoType.HOME, "250-111-1234")
                .and().recipient()
                    .id("6666")
                    .name(NameType.LEGAL, "Joseph", "Cloud")
                    .address(AddressType.HOME, "111 Main St", "Victoria", "BC", "V8V Z9Z", "CA")
                    .phone(TelcoType.HOME, "250-111-1234")
                .and().participant()
                    .id("5555")
                    .name(NameType.LEGAL, "Joseph", "Cloud")
                    .address(AddressType.HOME, "111 Main St", "Victoria", "BC", "V8V Z9Z", "CA")
                    .phone(TelcoType.HOME, "250-111-1234")
                .and().inFulfillmentOf()
                    .id("1111")
                .and()
                    .receiverId("cdxpostprod-obctc")
                    .content("Plain text document content"))
                .getDocument();
        MiscUtils.getLogger().info("testSubmitDocument document:");
        MiscUtils.getLogger().info(mapper.writeValueAsString(document));

        MiscUtils.getLogger().info("testSubmitDocument response:");
        SubmitDocumentResponse response = restClient.submitCDA(document);
        MiscUtils.getLogger().info(mapper.writeValueAsString(response));

        Assert.assertNotNull("response from testSubmitDocument was null", response);
    }

    @Test
    public void testListDocument() throws Exception {
        IOscarInformation restClient = new RestClient(obibUrl, clinicId);

        ListDocumentsResponse response = restClient.listDocument();
        MiscUtils.getLogger().info("testListDocument response:");
        MiscUtils.getLogger().info(mapper.writeValueAsString(response));

        Assert.assertNotNull("response from testListDocument was null", response);
    }

    @Test
    public void testSearchDocument() throws Exception {
        IOscarInformation restClient = new RestClient(obibUrl, clinicId);

        ListDocumentsResponse response = null;
        MiscUtils.getLogger().info("testSearchDocument response:");
        try {
            response = restClient.searchDocument(SearchDocumentCriteria.byClinicId("cdxpostprod-otca"));
            MiscUtils.getLogger().info(mapper.writeValueAsString(response));
        } catch (OBIBRequestException e) {
            MiscUtils.getLogger().error(e.getMessage());
        }
//"Error submitting request to OBIB Server."
        Assert.assertTrue(response!=null);
    }

    @Test
    public void testGetDocument() throws Exception {
        IOscarInformation restClient = new RestClient(obibUrl, clinicId);

        ListDocumentsResponse response = restClient.getDocument(SearchDocumentCriteria
                .byDocumentId("ad0007b5-c846-e911-a96a-0050568c55a6"));
        MiscUtils.getLogger().info("testGetDocument response:");
        MiscUtils.getLogger().info(mapper.writeValueAsString(response));

        Assert.assertNotNull("response from testGetDocument was null", response);
    }

    @Test
    public void testListClinics() throws Exception {
        IOscarInformation restClient = new RestClient(obibUrl, clinicId);

        ListClinicsResponse response = restClient.listClinics(SearchClinicCriteria.byClinicId("cdxpostprod-otca"));
        MiscUtils.getLogger().info("testListClinics response:");
        MiscUtils.getLogger().info(mapper.writeValueAsString(response));

        Assert.assertNotNull("resopnse from testListCLinics was null", response);
    }

    @Test
    public void testListProviders() throws Exception {
        IOscarInformation restClient = new RestClient(obibUrl, clinicId);

        ListProvidersResponse response = restClient.listProviders(SearchProviderCriteria.byProviderId("93188"));
        MiscUtils.getLogger().info("testListProviders response:");
        MiscUtils.getLogger().info(mapper.writeValueAsString(response));

        Assert.assertNotNull("response from testListProvider was null", response);
    }
}
