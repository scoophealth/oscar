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

import ca.ontario.health.edt.EDTDelegate;
import ca.ontario.health.edt.UploadData;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.tools.ant.util.DateUtils;
import org.oscarehr.integration.mcedt.DelegateFactory;
import org.oscarehr.util.MiscUtils;
import oscar.OscarProperties;
import oscar.oscarReport.data.ObecData;

public class OBECRunner {

    private int futureDaysCount = 7;
    private OscarProperties properties;
    private static Logger logger = MiscUtils.getLogger();

    public OBECRunner() {
        properties = OscarProperties.getInstance();
    }

    public void run() {

        Calendar startDate = new GregorianCalendar();
        Calendar endDate = new GregorianCalendar();
        endDate.add(Calendar.DAY_OF_MONTH, futureDaysCount);

        ObecData obecData = new ObecData();
        String sDate = DateUtils.format(startDate.getTime(), "yyyy-MM-dd");
        String eDate = DateUtils.format(endDate.getTime(), "yyyy-MM-dd");
        String fileName = obecData.generateOBEC(sDate, eDate, properties);

        // send generated file to EDT service
        UploadData uploadData = toUpload(fileName);
        sendUploadData(uploadData);
    }

    public UploadData toUpload(String fileName) {
        UploadData result = new UploadData();
        result.setDescription("Overnight Batch Eligibility CheckiupdoadDatang");
        result.setResourceType("TXT");
        try {
            String oscar_home = properties.getProperty("DOCUMENT_DIR");
            RandomAccessFile f = new RandomAccessFile(oscar_home + fileName, "r");
            byte[] b = new byte[(int) f.length()];
            f.read(b);
            result.setContent(b);
        } catch (Exception e) {
            throw new RuntimeException("Unable to read upload file", e);
        }
        return result;
    }

    private void sendUploadData(UploadData uploadData) {

        List<UploadData> uploads = new ArrayList<UploadData>();
        uploads.add(uploadData);
        try {
            EDTDelegate delegate = DelegateFactory.newDelegate();
            delegate.upload(uploads);
        } catch (Exception e) {
            logger.error("Unable to upload to MCEDT", e);
        }
    }
}