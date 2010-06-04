/*
 * AcknowledgementData.java
 *
 * Created on July 9, 2007, 11:49 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package oscar.oscarLab.ca.all;

import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import oscar.oscarDB.DBHandler;
import oscar.oscarMDS.data.ReportStatus;

/**
 *
 * @author wrighd
 */
public class AcknowledgementData {

    Logger logger = Logger.getLogger(AcknowledgementData.class);

    /** Creates a new instance of AcknowledgementData */
    public AcknowledgementData() {
    }

    public ArrayList getAcknowledgements(String segmentID) {
        ArrayList acknowledgements = null;
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);

            acknowledgements = new ArrayList();
            String sql = "select provider.first_name, provider.last_name, provider.provider_no, providerLabRouting.status, providerLabRouting.comment, providerLabRouting.timestamp from provider, providerLabRouting where provider.provider_no = providerLabRouting.provider_no and providerLabRouting.lab_no='" + segmentID + "' and providerLabRouting.lab_type='HL7'";
            ResultSet rs = db.GetSQL(sql);
            while (rs.next()) {
                acknowledgements.add(new ReportStatus(db.getString(rs, "first_name") + " " + db.getString(rs, "last_name"), db.getString(rs, "provider_no"), db.getString(rs, "status"), db.getString(rs, "comment"), db.getString(rs, "timestamp"), segmentID));
            }
            rs.close();
        } catch (Exception e) {
            logger.error("Could not retrieve acknowledgement data", e);
        }
        return acknowledgements;
    }

    public ArrayList getAcknowledgements(String docType, String segmentID) {
        ArrayList acknowledgements = null;
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);

            acknowledgements = new ArrayList();
            String sql = "select provider.first_name, provider.last_name, provider.provider_no, providerLabRouting.status, providerLabRouting.comment, providerLabRouting.timestamp from provider, providerLabRouting where provider.provider_no = providerLabRouting.provider_no and providerLabRouting.lab_no='" + segmentID + "' and providerLabRouting.lab_type='" + docType + "'";
            ResultSet rs = db.GetSQL(sql);
            while (rs.next()) {
                acknowledgements.add(new ReportStatus(db.getString(rs, "first_name") + " " + db.getString(rs, "last_name"), db.getString(rs, "provider_no"), db.getString(rs, "status"), db.getString(rs, "comment"), db.getString(rs, "timestamp"), segmentID));
            }
            rs.close();
        } catch (Exception e) {
            logger.error("Could not retrieve acknowledgement data", e);
        }
        //System.out.println("acknowledgements="+acknowledgements);
        return acknowledgements;
    }
}
