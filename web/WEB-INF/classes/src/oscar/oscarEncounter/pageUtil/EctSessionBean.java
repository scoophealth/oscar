package oscar.oscarEncounter.pageUtil;
import oscar.oscarDB.*;
import oscar.util.*;
import java.util.*;
import java.sql.ResultSet;
import oscar.oscarEncounter.oscarConsultation.data.EctConProviderData;

public class EctSessionBean {
    //data passed from the oscar appointment screen these members are constant for the duration of a session
    public Date currentDate;
    public Date eChartTimeStamp;
    public String providerNo;
    public String userName;
    public String demographicNo;
    public String appointmentNo;
    public String curProviderNo;
    public String reason;
    public String appointmentDate;
    public String startTime;
    public String status;
    public String date;
    public String check;
    public String patientFirstName;
    public String patientLastName;
    public String patientSex;
    public String patientAge;
    public String familyDoctorNo;
    public String monthOfBirth;
    public String yearOfBirth;
    public String dateOfBirth;
    public String address;
    public String city;
    public String postal;
    public String phone;
    public String roster;
    public String team = null;
    public String consultationRequestId = null;
    public String currentTeam = null;
    public String socialHistory;
    public String familyHistory;
    public String medicalHistory;
    public String ongoingConcerns;
    public String reminders;
    public String encounter;
    public String subject;
    public String template = "";
    public ArrayList eChartIdArray;
    public ArrayList socialHistoryArray;
    public ArrayList familyHistoryArray;
    public ArrayList medicalHistoryArray;
    public ArrayList ongoingConcernsArray;
    public ArrayList remindersArray;
    public ArrayList appointmentsIdArray;
    public ArrayList appointmentsNamesArray;
    public ArrayList templateNames;
    public ArrayList templateValues;
    public ArrayList measurementGroupNames;
    
    public void resetAll(){
      eChartTimeStamp = null;
      patientLastName ="";
      patientFirstName ="";
      yearOfBirth ="";
      monthOfBirth ="";
      dateOfBirth ="";
      patientSex ="";
      patientAge ="";
      familyDoctorNo ="";
      socialHistory = "";
      familyHistory = "";
      medicalHistory = "";
      ongoingConcerns = "";
      reminders = "";
      encounter = "";
      subject = "";
      address = "";
      city = "";
      postal = "";
      phone = "";
      roster = "";
      template ="";
    }

    /**
     * sets up the encounter page as befits entrance into the oscarEncounter module
     * from the oscar appointment scheduling screen
     */
    public void setUpEncounterPage() {
        resetAll();
        String tmp;
        DBHandler db =  null;
        String sql ;
        ResultSet rs ;
        
        appointmentsIdArray = new ArrayList();
        appointmentsNamesArray = new ArrayList();
        templateNames = new ArrayList();
        templateValues = new ArrayList();
        measurementGroupNames = new ArrayList();
        
        //This block gets the patient age and
        try {
            db =  new DBHandler(DBHandler.OSCAR_DATA);
            sql = "select * from demographic where demographic_no=" + demographicNo ;
            rs = db.GetSQL(sql);
            while(rs.next()){
                patientLastName = rs.getString("last_name");
                patientFirstName = rs.getString("first_name");
                address = rs.getString("address");
                city = rs.getString("city");
                postal = rs.getString("postal");
                phone = rs.getString("phone");
                familyDoctorNo = rs.getString("provider_no");
                yearOfBirth = rs.getString("year_of_birth");
                monthOfBirth = rs.getString("month_of_birth");
                dateOfBirth = rs.getString("date_of_birth");
                roster =rs.getString("roster_status");
                patientSex = rs.getString("sex");

                if(yearOfBirth.equals("null")){yearOfBirth = "0";}
                if(monthOfBirth.equals("null")){monthOfBirth = "0";}
                if(dateOfBirth.equals("null")){dateOfBirth = "0";}
            }
            rs.close();
            db.CloseConn();
            UtilDateUtilities dateUtil = new UtilDateUtilities();
            patientAge = dateUtil.calcAge(dateUtil.calcDate(yearOfBirth,monthOfBirth,dateOfBirth));
        }catch (java.sql.SQLException e){ System.out.println(e.getMessage()); }
        try{
            sql = "select * from appointment where provider_no='"+curProviderNo+"' and appointment_date='"+appointmentDate+"'";
            rs = db.GetSQL(sql);
            while(rs.next()){
                tmp = Integer.toString(rs.getInt("appointment_no"));
                appointmentsIdArray.add(tmp);
                appointmentsNamesArray.add(rs.getString("name")+" "+rs.getString("start_time"));
//                System.out.println(tmp);
            }
            rs.close();
            sql = "select * from encountertemplate order by encountertemplate_name";
            rs = db.GetSQL(sql);
            while(rs.next()){
                templateNames.add(rs.getString("encountertemplate_name"));
                templateValues.add(rs.getString("encountertemplate_value"));
            }
            rs.close();
            
            sql = "SELECT groupName from measurementGroupStyle ORDER BY groupName";
            rs = db.GetSQL(sql);
            while(rs.next()){
                measurementGroupNames.add(rs.getString("groupName"));
            }
            rs.close();
            
            db.CloseConn();
        }catch (java.sql.SQLException e){ System.out.println(e.getMessage()); }
        try{
            sql = "select * from eChart where demographicNo="+demographicNo+" ORDER BY eChartId DESC limit 1";
            rs = db.GetSQL(sql);
            if(rs.next()){
                eChartTimeStamp = rs.getDate("timeStamp");
                socialHistory = rs.getString("socialHistory");
                familyHistory = rs.getString("familyHistory");
                medicalHistory = rs.getString("medicalHistory");
                ongoingConcerns =rs.getString("ongoingConcerns");
                reminders = rs.getString("reminders");
                encounter = rs.getString("encounter");
                subject = rs.getString("subject");
            } else {
                eChartTimeStamp = null;
                socialHistory = "";
                familyHistory = "";
                medicalHistory = "";
                ongoingConcerns = "";
                reminders = "";
                encounter = "";
                subject = "";
            }
            rs.close();
            db.CloseConn();
        }catch (java.sql.SQLException e){ System.out.println(e.getMessage()); }
    }
    /**
     * over loaded method sets up the encounter page as befits entrance from the select box
     * of today's appointments on the oscarEncounter.Index.jsp page
     * @param appointmentNo
     */
    public void setUpEncounterPage(String appointmentNo){
        resetAll();
        appointmentsIdArray = new ArrayList();
        appointmentsNamesArray = new ArrayList();
        templateNames = new ArrayList();
        templateValues = new ArrayList();

        String tmp;
        DBHandler db = null;
        ResultSet rs;
        String sql ;

        try{
            db = new DBHandler(DBHandler.OSCAR_DATA);
            sql = "select * from appointment where appointment_no="+appointmentNo ;
            rs = db.GetSQL(sql);
            while(rs.next()){
                demographicNo = rs.getString("demographic_no");
                appointmentNo = appointmentNo;
                reason=rs.getString("reason");
                appointmentDate=rs.getString("appointment_date");
                startTime=rs.getString("start_time");
                status=rs.getString("status");
            }
            rs.close();
            db.CloseConn();
        }catch (java.sql.SQLException e){ System.out.println(e.getMessage()); }
        try{
            sql = "select * from appointment where provider_no='"+curProviderNo+"' and appointment_date='"+appointmentDate+"'" ;
            rs = db.GetSQL(sql);
            while(rs.next()){
                tmp = Integer.toString(rs.getInt("appointment_no"));
                appointmentsIdArray.add(tmp);
                appointmentsNamesArray.add(rs.getString("name")+" "+rs.getString("start_time"));
            }
            rs.close();
            sql = "select * from encountertemplate order by encountertemplate_name";
            rs = db.GetSQL(sql);
            while(rs.next()){
                templateNames.add(rs.getString("encountertemplate_name"));
                templateValues.add(rs.getString("encountertemplate_value"));
            }
            rs.close();
            db.CloseConn();
        }catch (java.sql.SQLException e){ System.out.println(e.getMessage()); }
        try{
//            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            sql = "select * from eChart where demographicNo='"+demographicNo+"' ORDER BY eChartId DESC limit 1" ;
            rs = db.GetSQL(sql);;
            if(rs.next()){
                eChartTimeStamp = rs.getDate("timeStamp");
                socialHistory = rs.getString("socialHistory");
                familyHistory = rs.getString("familyHistory");
                medicalHistory = rs.getString("medicalHistory");
                ongoingConcerns =rs.getString("ongoingConcerns");
                reminders = rs.getString("reminders");
                encounter = rs.getString("encounter");
                subject = rs.getString("subject");
            } else {
                eChartTimeStamp = null;
                socialHistory = "";
                familyHistory = "";
                medicalHistory = "";
                ongoingConcerns = "";
                reminders = "";
                encounter = "";
                subject = "";
            }
            rs.close();
            db.CloseConn();
        }catch (java.sql.SQLException e){ System.out.println(e.getMessage()); }
        //apointmentsIdArray and the appointmentsNamesArray are
        //already set up so no need to get them again
        try{
//            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            sql = "select * from demographic where demographic_no="+demographicNo ;
            rs = db.GetSQL(sql);
            while(rs.next()){
                patientLastName = rs.getString("last_name");
                patientFirstName = rs.getString("first_name");
                address = rs.getString("address");
                city = rs.getString("city");
                postal = rs.getString("postal");
                phone = rs.getString("phone");
                familyDoctorNo = rs.getString("provider_no");
                yearOfBirth = rs.getString("year_of_birth");
                monthOfBirth = rs.getString("month_of_birth");
                dateOfBirth = rs.getString("date_of_birth");
                roster =rs.getString("roster_status");
                patientSex = rs.getString("sex");
                if(yearOfBirth.equals("null")){yearOfBirth = "0";}
                if(monthOfBirth.equals("null")){monthOfBirth = "0";}
                if(dateOfBirth.equals("null")){dateOfBirth = "0";}
            }
            rs.close();
            db.CloseConn();
            UtilDateUtilities dateUtil = new oscar.util.UtilDateUtilities();
            patientAge = UtilDateUtilities.calcAge(UtilDateUtilities.calcDate(yearOfBirth,monthOfBirth,dateOfBirth));
        }catch (java.sql.SQLException e){ System.out.println(e.getMessage()); }
    }
    /**
     * over loaded method sets up the encounter page for print
     * @param eChartId, demographic_no
     */
    public void setUpEncounterPage(String echartid, String demographicNo){
        resetAll();

        String tmp;
        DBHandler db = null;
        ResultSet rs;
        String sql ;

        try{
            db = new DBHandler(DBHandler.OSCAR_DATA);
            sql = "select * from eChart where eChartId = " + echartid + " and demographicNo="+demographicNo ;
            rs = db.GetSQL(sql);;
            if (rs.next()){
                eChartTimeStamp = rs.getDate("timeStamp");
                socialHistory = rs.getString("socialHistory");
                familyHistory = rs.getString("familyHistory");
                medicalHistory = rs.getString("medicalHistory");
                ongoingConcerns =rs.getString("ongoingConcerns");
                reminders = rs.getString("reminders");
                encounter = rs.getString("encounter");
                subject = rs.getString("subject");
            }
            rs.close();
            db.CloseConn();
        }catch (java.sql.SQLException e){ System.out.println(e.getMessage()); }

        try{
            sql = "select * from demographic where demographic_no="+demographicNo ;
            rs = db.GetSQL(sql);
            while(rs.next()){
                patientLastName = rs.getString("last_name");
                patientFirstName = rs.getString("first_name");
                address = rs.getString("address");
                city = rs.getString("city");
                postal = rs.getString("postal");
                phone = rs.getString("phone");
                familyDoctorNo = rs.getString("provider_no");
                yearOfBirth = rs.getString("year_of_birth");
                monthOfBirth = rs.getString("month_of_birth");
                dateOfBirth = rs.getString("date_of_birth");
                roster =rs.getString("roster_status");
                patientSex = rs.getString("sex");
                if(yearOfBirth.equals("null")){yearOfBirth = "0";}
                if(monthOfBirth.equals("null")){monthOfBirth = "0";}
                if(dateOfBirth.equals("null")){dateOfBirth = "0";}
            }
            rs.close();
            db.CloseConn();
            UtilDateUtilities dateUtil = new oscar.util.UtilDateUtilities();
            patientAge = UtilDateUtilities.calcAge(UtilDateUtilities.calcDate(yearOfBirth,monthOfBirth,dateOfBirth));
        }catch (java.sql.SQLException e){ System.out.println(e.getMessage()); }
    }

    public String getTeam() {
        if ( team == null){
//          oscar.oscarEncounter.oscarConsultation.data.ProviderData providerData;
//          providerData = new oscar.oscarEncounter.oscarConsultation.data.ProviderData();
          EctConProviderData providerData = new EctConProviderData();
          team = providerData.getTeam(providerNo);
        }
        return team;
    }

    public void setDemographicNo(String str){
        demographicNo = str;
    }

    public String getDemographicNo(){
        return demographicNo;
    }

    public String getCurProviderNo(){
        return curProviderNo;
    }
    
    public void setConsultationRequestId(String str){
        //System.out.println("CON ID setting too "+str);
        consultationRequestId = str;
    }

    public String getConsultationRequestId(){
        //System.out.println("CON ID in session bean getter "+consultationRequestId);
        return consultationRequestId;
    }

    public void unsetConsultationRequestId(){
        System.err.println("UNSETTING REQUEST ID");
        consultationRequestId = null;
    }


    public String getCurrentTeam (){
        if ( currentTeam == null){
            currentTeam = new String();
        }
        return currentTeam ;
    }

    public void setCurrentTeam (String str){
        //System.out.println("Setting curr team = " +str);
        currentTeam = str;
    }

    public void unsetCurrentTeam (){
        currentTeam = null;
    }
    public boolean isCurrentTeam(){
        boolean retval = true;
        if (currentTeam == null){
            retval = false;
        }
        return retval;
    }

    public boolean isValid() {
        return demographicNo.length() > 0 && providerNo != null && providerNo.length() > 0;
    }
    
    public String getPatientLastName(){
        return patientLastName;
    }
    
    public String getPatientFirstName(){
        return patientFirstName;
    }
    
    public String getPatientSex(){
        return patientSex;
    }
    
    public String getPatientAge(){
        return patientAge;
    }
        
}