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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oscar.dms;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author apavel
 */
public class EDocFactory {
    public enum Status {
        ACTIVE('A'),
        DELETED('D'),
        SENT('S');

        char statusCharacter;
        private Status(char statusCharacter) { this.statusCharacter = statusCharacter; }
        public char getStatusCharacter() { return statusCharacter; }
        @Override
        public String toString() { return statusCharacter + ""; }
    }

    public enum Module {
        demographic, provider;
    }

    public EDoc createEDoc(String description, String type, String fileName, String contentType, String html, String creatorId, String responsibleId, String source, Status status, Date observationDate, String reviewerId, Date reviewDateTime, Module module, String moduleId) {
        SimpleDateFormat reviewDateTimeFormat = new SimpleDateFormat(EDocUtil.REVIEW_DATETIME_FORMAT);
        SimpleDateFormat observationDateFormat = new SimpleDateFormat(EDocUtil.DMS_DATE_FORMAT);
        String reviewDateTimeStr = reviewDateTimeFormat.format(reviewDateTime);
        String observationDateStr = observationDateFormat.format(observationDate);
        EDoc eDoc = new EDoc(description, type, fileName, html, creatorId, responsibleId, source, status.getStatusCharacter(), observationDateStr, reviewerId, reviewDateTimeStr, module + "", moduleId);
        eDoc.setContentType(contentType);
        return eDoc;
    }
}
