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
