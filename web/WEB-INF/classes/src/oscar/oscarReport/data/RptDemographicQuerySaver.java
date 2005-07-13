package oscar.oscarReport.data;
import oscar.oscarReport.pageUtil.*;
import oscar.oscarDB.DBHandler;
import java.sql.*;
import org.w3c.dom.*;
import oscar.oscarMessenger.docxfer.util.MsgCommxml;

public class RptDemographicQuerySaver {

    public RptDemographicQuerySaver() {
    }

    public void saveQuery(RptDemographicReportForm frm){

        String[] select = frm.getSelect();
        String yearStyle        = frm.getAge();
        String startYear        = frm.getStartYear();
        String endYear          = frm.getEndYear();
        String[] rosterStatus   = frm.getRosterStatus();
        String[] patientStatus  = frm.getPatientStatus();
        String[] providers      = frm.getProviderNo();

        String firstName        = frm.getFirstName();
        String lastName         = frm.getLastName();
        String sex              = frm.getSex();
        String queryName        = frm.getQueryName();
        String query            = frm.getQuery();

        if (firstName != null ){
            firstName = firstName.trim();
        }

        if (lastName != null ){
            lastName = lastName.trim();
        }

        if (sex != null){
            sex = sex.trim();
        }

        String sqSelects = new String();
        String sqAge = yearStyle;
        String sqStartYear  = startYear   ;
        String sqEndYear  = endYear;
        String sqFirstName  = firstName;

        String sqLastName  = lastName;
        String sqRosterStatus  = new String();
        String sqSex  = sex;
        String sqProviderNo  = new String();
        String sqPatientStatus  = new String();
        String sqQueryName  = queryName;



        Document doc = MsgCommxml.newDocument();
        Element docRoot = MsgCommxml.addNode(doc, "root");

        if (select != null){
            for (int i=0; i < select.length; i++ ){
                Element table = doc.createElement("item");
                table.setAttribute("value",select[i]);
                docRoot.appendChild(table);
            }
        sqSelects = MsgCommxml.toXML(docRoot);
        }

        doc = MsgCommxml.newDocument();
        docRoot = MsgCommxml.addNode(doc, "root");

        if (rosterStatus != null){
            for (int i=0; i < rosterStatus.length; i++ ){
                Element table = doc.createElement("item");
                table.setAttribute("value",rosterStatus[i]);
                docRoot.appendChild(table);
            }
        sqRosterStatus = MsgCommxml.toXML(docRoot);
        }

        doc = MsgCommxml.newDocument();
        docRoot = MsgCommxml.addNode(doc, "root");

        if (patientStatus != null){
            for (int i=0; i < patientStatus.length; i++ ){
                Element table = doc.createElement("item");
                table.setAttribute("value",patientStatus[i]);
                docRoot.appendChild(table);
            }
        sqPatientStatus = MsgCommxml.toXML(docRoot);
        }

        doc = MsgCommxml.newDocument();
        docRoot = MsgCommxml.addNode(doc, "root");

        if (providers != null){
            for (int i=0; i < providers.length; i++ ){
                Element table = doc.createElement("item");
                table.setAttribute("value",providers[i]);
                docRoot.appendChild(table);
            }
        sqProviderNo = MsgCommxml.toXML(docRoot);
        }
        oscar.oscarMessenger.util.MsgStringQuote s = new oscar.oscarMessenger.util.MsgStringQuote();
        try{
                  DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);

                  String sql = "insert into demographicQueryFavourites "
                  +"(selects,age,startYear,endYear,firstName,lastName,rosterStatus,sex,providerNo,patientStatus,queryName,archived)"
                  +" values "
                  +" ('"+sqSelects+"','"+sqAge+"','"+sqStartYear+"','"+sqEndYear+"','"+s.q(sqFirstName)+"','"+s.q(sqLastName)+"',"
                  +"  '"+sqRosterStatus+"', '"+s.q(sqSex)+"', '"+sqProviderNo+"' , '"+sqPatientStatus+"','"+sqQueryName+"','1')";;
                  System.out.println("sql statement : "+sql);
                  db.RunSQL(sql);


                  db.CloseConn();
       }catch (java.sql.SQLException e){ System.out.println(e.getMessage()); }





    }
}