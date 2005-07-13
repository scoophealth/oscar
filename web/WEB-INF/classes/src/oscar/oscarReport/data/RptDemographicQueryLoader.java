package oscar.oscarReport.data;
import oscar.oscarReport.pageUtil.*;
import oscar.oscarDB.DBHandler;
import java.sql.*;
import org.w3c.dom.*;
import oscar.oscarMessenger.docxfer.util.MsgCommxml;


public class RptDemographicQueryLoader {

    public RptDemographicQueryLoader() {
    }

    public RptDemographicReportForm queryLoader(RptDemographicReportForm frm){
        String qId = frm.getSavedQuery();
        RptDemographicReportForm dRF = new RptDemographicReportForm();
        try{
                  DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                  java.sql.ResultSet rs;
                  String mSelect         = null;
                  String mAge           = null;
                  String mStartYear = null;
                  String mEndYear   = null;
                  String mFirstName = null;
                  String mLastName  = null;
                  String mRosterStatus  = null;
                  String mSex   = null;
                  String mProviderNo    = null;
                  String mPatientStatus = null;






                  rs = db.GetSQL("select * from demographicQueryFavourites where favId = '"+qId+"'");

                  if (rs.next()){
                        mSelect         = rs.getString("selects");
                        mAge            = rs.getString("age");
                        mStartYear      = rs.getString("startYear");
                        mEndYear        = rs.getString("endYear");
                        mFirstName      = rs.getString("firstName");
                        mLastName       = rs.getString("lastName");
                        mRosterStatus   = rs.getString("rosterStatus");
                        mSex            = rs.getString("sex");
                        mProviderNo     = rs.getString("providerNo");
                        mPatientStatus  = rs.getString("patientStatus");
                  }

                  if (mSelect != null && mSelect.length() != 0){
                    String[] t = fromXMLtoArray(mSelect);
                    dRF.setSelect(t);
                  }
                  if (mAge != null && mAge.length() != 0){
                    dRF.setAge(mAge);
                  }
                  if (mStartYear != null && mStartYear.length() != 0){
                    dRF.setStartYear(mStartYear);
                  }
                  if (mEndYear != null && mEndYear.length() != 0){
                    dRF.setEndYear(mEndYear);
                  }
                  if (mFirstName != null && mFirstName.length() != 0){
                    dRF.setFirstName(mFirstName);
                  }
                  if (mLastName != null && mLastName.length() != 0){
                    dRF.setLastName(mLastName);
                  }


                  if (mRosterStatus != null && mRosterStatus.length() != 0){
                    String[] t = fromXMLtoArray(mRosterStatus);
                    dRF.setRosterStatus(t);
                  }
                  if (mSex != null && mSex.length() != 0){
                    dRF.setSex(mSex);
                  }
                  if (mRosterStatus != null && mRosterStatus.length() != 0){
                    String[] t = fromXMLtoArray(mRosterStatus);
                    dRF.setRosterStatus(t);
                  }
                  if (mProviderNo != null && mProviderNo.length() != 0){
                    String[] t = fromXMLtoArray(mProviderNo);
                    dRF.setProviderNo(t);
                  }
                  if (mPatientStatus != null && mPatientStatus.length() != 0){
                    String[] t = fromXMLtoArray(mPatientStatus);
                    dRF.setPatientStatus(t);
                  }


                  db.CloseConn();
       }catch (java.sql.SQLException e){ System.out.println(e.getMessage()); }




    return dRF;
    }

     String[] fromXMLtoArray(String xStr){
        System.out.println("xStr "+xStr);
        Document doc = MsgCommxml.parseXML(xStr);

        Element docRoot = doc.getDocumentElement();

        NodeList nlst = docRoot.getChildNodes();

          String[] retval ;
         retval = new String [nlst.getLength()];
        for (int i = 0; i < nlst.getLength(); i++){


           Element temp = (Element) nlst.item(i);
           retval[i] = temp.getAttribute("value");
        }

        return retval;
    }
}