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

import ca.uvic.leadlab.obibconnector.facades.datatypes.*;
import ca.uvic.leadlab.obibconnector.facades.exceptions.OBIBException;
import ca.uvic.leadlab.obibconnector.facades.receive.IDocument;
import ca.uvic.leadlab.obibconnector.facades.send.ISubmitDoc;
import ca.uvic.leadlab.obibconnector.impl.send.SubmitDoc;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.integration.cdx.dao.CdxProvenanceDao;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import javax.xml.bind.DatatypeConverter;
import java.util.Date;

public class SubmitDocTest extends FacadesBaseTest {
    private final String base64Pdf = "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TVAgQ29yZSA1LjQtYzAwNiA4MC4xNTk4MjUsIDIwMTYvMDkvMTYtMDM6MzE6MDggICAgICAgICI+CiAgIDxyZGY6UkRGIHhtbG5zOnJkZj0iaHR0cDovL3d3dy53My5vcmcvMTk5OS8wMi8yMi1yZGYtc3ludGF4LW5zIyI+CiAgICAgIDxyZGY6RGVzY3JpcHRpb24gcmRmOmFib3V0PSIiCiAgICAgICAgICAgIHhtbG5zOmRjPSJodHRwOi8vcHVybC5vcmcvZGMvZWxlbWVudHMvMS4xLyIKICAgICAgICAgICAgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIgogICAgICAgICAgICB4bWxuczpwZGY9Imh0dHA6Ly9ucy5hZG9iZS5jb20vcGRmLzEuMy8iCiAgICAgICAgICAgIHhtbG5zOnhtcE1NPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvbW0vIj4KICAgICAgICAgPGRjOmZvcm1hdD5hcHBsaWNhdGlvbi9wZGY8L2RjOmZvcm1hdD4KICAgICAgICAgPGRjOmNyZWF0b3I+CiAgICAgICAgICAgIDxyZGY6U2VxPgogICAgICAgICAgICAgICA8cmRmOmxpPk1vcmdhbiBQcmljZTwvcmRmOmxpPgogICAgICAgICAgICA8L3JkZjpTZXE+CiAgICAgICAgIDwvZGM6Y3JlYXRvcj4KICAgICAgICAgPGRjOnRpdGxlPgogICAgICAgICAgICA8cmRmOkFsdD4KICAgICAgICAgICAgICAgPHJkZjpsaSB4bWw6bGFuZz0ieC1kZWZhdWx0Ij5MRUFETGFiTG9nbzIwMTYtMDEuZ3JhZmZsZTwvcmRmOmxpPgogICAgICAgICAgICA8L3JkZjpBbHQ+CiAgICAgICAgIDwvZGM6dGl0bGU+CiAgICAgICAgIDx4bXA6Q3JlYXRlRGF0ZT4yMDE4LTAyLTA5VDIzOjIwOjE0WjwveG1wOkNyZWF0ZURhdGU+CiAgICAgICAgIDx4bXA6Q3JlYXRvclRvb2w+T21uaUdyYWZmbGUgNy42LjE8L3htcDpDcmVhdG9yVG9vbD4KICAgICAgICAgPHhtcDpNb2RpZnlEYXRlPjIwMTgtMDItMDlUMTY6MTctMDg6MDA8L3htcDpNb2RpZnlEYXRlPgogICAgICAgICA8eG1wOk1ldGFkYXRhRGF0ZT4yMDE4LTAyLTA5VDE2OjE3LTA4OjAwPC94bXA6TWV0YWRhdGFEYXRlPgogICAgICAgICA8cGRmOlByb2R1Y2VyPk1hYyBPUyBYIDEwLjEzLjMgUXVhcnR6IFBERkNvbnRleHQ8L3BkZjpQcm9kdWNlcj4KICAgICAgICAgPHhtcE1NOkRvY3VtZW50SUQ+dXVpZDphMDcxYTMyNS0zMDFjLTQ3NDItYjIwZi0yNzRmYmUyODgwOTk8L3htcE1NOkRvY3VtZW50SUQ+CiAgICAgICAgIDx4bXBNTTpJbnN0YW5jZUlEPnV1aWQ6YTA3MDg0YzQtYjk4MC0yYjRjLTg5YTEtZjQ0MTk4YWVlNDdhPC94bXBNTTpJbnN0YW5jZUlEPgogICAgICA8L3JkZjpEZXNjcmlwdGlvbj4KICAgPC9yZGY6UkRGPgo8L3g6eG1wbWV0YT4KICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgIAo8P3hwYWNrZXQgZW5kPSJ3Ij8+DWVuZHN0cmVhbQ1lbmRvYmoNMiAwIG9iag08PC9GaWx0ZXIvRmxhdGVEZWNvZGUvRmlyc3QgNC9MZW5ndGggNDgvTiAxL1R5cGUvT2JqU3RtPj5zdHJlYW0NCmjeMlUwULCx0XfOL80rUTDU985MKY62BIoFxeqHVBak6gckpqcW29kBBBgA1ncLgA1lbmRzdHJlYW0NZW5kb2JqDTMgMCBvYmoNPDwvRmlsdGVyL0ZsYXRlRGVjb2RlL0ZpcnN0IDQvTGVuZ3RoIDE3My9OIDEvVHlwZS9PYmpTdG0+PnN0cmVhbQ0KaN5cjUELgjAYhv/Kd9Md3L5pqIUI4qqLolGH6LZ0mmAOxoTo1zeoU7cXXp7niQEhy1ix2oc2fq3NKBdozdQpwkqjpJ30IqRVvtiFyFMMcRtGbm1uv99RzXOZjkYOw6wgoTHlhNW6/6N4zBPEAFMP0SOsNbpfO+WSsoPmDFfgSHlEIzit0tg3tOJQ6sWqlyXsMtlZ+dW+EJW8V3rUThoHyOn4rZI8/wgwABjsOscNZW5kc3RyZWFtDWVuZG9iag00IDAgb2JqDTw8L0RlY29kZVBhcm1zPDwvQ29sdW1ucyAzL1ByZWRpY3RvciAxMj4+L0ZpbHRlci9GbGF0ZURlY29kZS9JRFs8REMwNkJDMzZEQzVGN0NGQkNDMkE3MDVCQjZGREIzMDU+PDgwMjgwNTdBNDUwRjRGOTZCMjM5MzlFRDM2QUY5MDRDPl0vSW5mbyA2IDAgUi9MZW5ndGggMzcvUm9vdCA4IDAgUi9TaXplIDcvVHlwZS9YUmVmL1dbMSAyIDBdPj5zdHJlYW0NCmjeYmJgYGBitFvCxMB3jYmBsRuIOZkYN31hYmBgBAgwADN9BBQNZW5kc3RyZWFtDWVuZG9iag1zdGFydHhyZWYNMTE2DSUlRU9GDQ==";
    private final String clinicIdA = "cdxpostprod-otca";
    private final CdxProvenanceDao cdxProvenanceDao = SpringUtils.getBean(CdxProvenanceDao.class);

    @BeforeClass
    public static void beforeClass() throws Exception {
        SchemaUtils.restoreTable("cdx_provenance", "cdx_attachment");
    }

    @Test
    public void testSubmitDoc() {

        IDocument response = null;
        ISubmitDoc submitDoc = new SubmitDoc(configClinicC);
        String result = null;
        try {
            response = submitDoc.newDoc()
                    .documentType(DocumentType.REFERRAL_NOTE)
                    .patient()
                        .id("2222")
                        .name(NameType.LEGAL, "Joe", "Wine")
                        .address(AddressType.HOME, "111 Main St", "Victoria", "BC", "V8V Z9Z", "CA")
                        .phone(TelcoType.HOME, "250-111-1234")
                        .birthday("1980", "01", "01")
                        .gender(Gender.MALE)
                    .and().author()
                        .id("93188")
                        .time(new Date())
                        .name("Lucius", "Plisihb", "Dr.", "")
                    .and().recipient()
                        .primary()
                        .id("93190")
                        .name("Aaron", "Plisihd", "Dr.", "")
                    .and().participant()
                        .functionCode("PCP")
                        .id("93193")
                        .name("Mikel", "Plisihf", "Dr.", "")
                    .and().inFulfillmentOf()
                        .id("2")
                        .statusCode(OrderStatus.COMPLETED)
                    .and().documentationOf()
                        .statusCode(DocumentStatus.COMPLETED)
                        .effectiveTime(new Date())
                    .and()
                        .receiverId(clinicIdA)
                        .content("Referral test 1")
                    .submit();
            result = "success";
        } catch (OBIBException e) {
            result = "Error submitting document.";
            MiscUtils.getLogger().info(e.getMessage());
        } catch (Exception e) {
            MiscUtils.getLogger().info("Got unexpected exception");
            e.printStackTrace();
        }

        logResponse(response);
        Assert.assertTrue(result!=null && response!=null);
    }

    @Test
    public void testSubmitDocWithAttachment() throws Exception {
        ISubmitDoc submitDoc = new SubmitDoc(configClinicC);

        IDocument response = submitDoc.newDoc()
                .documentType(DocumentType.REFERRAL_NOTE)
                .patient()
                    .id("2222")
                    .name(NameType.LEGAL, "Joe", "Wine")
                    .address(AddressType.HOME, "111 Main St", "Victoria", "BC", "V8V Z9Z", "CA")
                    .phone(TelcoType.HOME, "250-111-1234")
                    .birthday("1980", "1", "1")
                    .gender(Gender.MALE)
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
                .and().participant()
                    .functionCode("PCP")
                    .id("555")
                    .name(NameType.LEGAL, "Joseph", "Cloud")
                    .address(AddressType.HOME, "111 Main St", "Victoria", "BC", "V8V Z9Z", "CA")
                    .phone(TelcoType.HOME, "250-111-1234")
                .and()
                    .receiverId(clinicIdA)
                    .content("New referral with multiple attachments...")
                    .attach(AttachmentType.PDF, "logo.pdf", DatatypeConverter.parseBase64Binary(base64Pdf))
                    .attach(AttachmentType.PDF, "logo2.pdf", DatatypeConverter.parseBase64Binary(base64Pdf))
                .submit();
        logResponse(response);
        Assert.assertNotNull(response);
    }

    @Test
    public void testSubmitDocSaveProvenance() {

        IDocument response = null;
        ISubmitDoc submitDoc = new SubmitDoc(configClinicC);
        String result = null;
        try {
            response = submitDoc.newDoc()
                    .documentType(DocumentType.REFERRAL_NOTE)
                    .patient()
                        .id("2222")
                        .name(NameType.LEGAL, "Joe", "Wine")
                        .address(AddressType.HOME, "111 Main St", "Victoria", "BC", "V8V Z9Z", "CA")
                        .phone(TelcoType.HOME, "250-111-1234")
                        .birthday("1980", "1", "1")
                        .gender(Gender.MALE)
                    .and().author()
                        .id("93188")
                        .time(new Date())
                        .name("Lucius", "Plisihb", "Dr.", "")
                    .and().recipient()
                        .primary()
                        .id("93190")
                        .name("Aaron", "Plisihd", "Dr.", "")
                    .and().participant()
                        .functionCode("PCP")
                        .id("93193")
                        .name("Mikel", "Plisihf", "Dr.", "")
                    .and()
                        .receiverId(clinicIdA)
                        .content("Referral test 2 - with multiple attachments")
                        .attach(AttachmentType.PDF, "logo.pdf", DatatypeConverter.parseBase64Binary(base64Pdf))
                        .attach(AttachmentType.PDF, "logo2.pdf", DatatypeConverter.parseBase64Binary(base64Pdf))
                    .submit();
            result = "success";
        } catch (OBIBException e) {
            result = "Error submitting document.";
            MiscUtils.getLogger().info(e.getMessage());
        } catch (Exception e) {
            MiscUtils.getLogger().info("Got unexpected exception");
            e.printStackTrace();
        }


        if (response != null) {
            cdxProvenanceDao.logSentAction(response);
        } else {
            MiscUtils.getLogger().error("Response was null but shouldn't be!");
        }
        logResponse(response);
        Assert.assertTrue(result!=null && response!=null);

    }

    @Test
    public void testSubmitDocWithAttachmentSaveProvenance() throws OBIBException {
        ISubmitDoc submitDoc = new SubmitDoc(configClinicC);
        String result = null;

        IDocument response = submitDoc.newDoc()
                .documentType(DocumentType.REFERRAL_NOTE)
                .patient()
                    .id("2222")
                    .name(NameType.LEGAL, "Joe", "Wine")
                    .address(AddressType.HOME, "111 Main St", "Victoria", "BC", "V8V Z9Z", "CA")
                    .phone(TelcoType.HOME, "250-111-1234")
                    .birthday("1980", "1", "1")
                    .gender(Gender.MALE)
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
                .and().participant()
                    .functionCode("PCP")
                    .id("555")
                    .name(NameType.LEGAL, "Joseph", "Cloud")
                    .address(AddressType.HOME, "111 Main St", "Victoria", "BC", "V8V Z9Z", "CA")
                    .phone(TelcoType.HOME, "250-111-1234")
                .and()
                .receiverId(clinicIdA)
                .attach(AttachmentType.PDF, "logo.pdf", DatatypeConverter.parseBase64Binary(base64Pdf))
                .submit();

        if (response != null) {

            try {
                cdxProvenanceDao.logSentAction(response);
                result = "success";
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            MiscUtils.getLogger().error("Response was null but shouldn't be!");
        }
        logResponse(response);
        Assert.assertTrue(result!=null);
    }

    private boolean logResponse(IDocument doc) {
        boolean result = false;
        ObjectMapper mapper = new ObjectMapper();
        try {
            String docStr = mapper.writeValueAsString(doc);
            MiscUtils.getLogger().info(docStr);
            result = true;
        } catch (JsonProcessingException e) {
            MiscUtils.getLogger().error(e.getMessage());
        }
        return result;
    }
}
