package oscar.oscarMDS.data;

import oscar.oscarDB.*;
import java.util.*;
import java.sql.*;
import java.text.SimpleDateFormat;

public class PatientData
{

    public Patient getPatient(String demographicNo)
        throws SQLException
    {
        DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
        ResultSet rs;
        Patient p = null;

        try
        {
            rs = db.GetSQL("SELECT patientName, dOB, healthNumber, sex, homePhone, "
                + "altPatientID "
                + "FROM mdsPID WHERE segmentID = " + demographicNo);

            if(rs.next())
            {
                p = new Patient(rs.getString("patientName"), rs.getString("dOB"),
                    rs.getString("healthNumber"), rs.getString("sex"),
                    rs.getString("homePhone"), rs.getString("altPatientID"));
            }

            rs.close();
            db.CloseConn();
        } catch(SQLException e)
        {
            System.out.println(e.getMessage());
	    }

        return p;
    }

    public class Patient
    {
        String patientName;
        String dOB;
        String sex;
        String age;
        String healthNumber;
        String homePhone;
        String workPhone;
        String patientLocation;

        public Patient(String patientName, String dOB, String healthNumber, String sex,
            String homePhone, String patientLocation)
        {
            int patientAge;
            GregorianCalendar cal = new GregorianCalendar(Locale.ENGLISH);
            GregorianCalendar now = new GregorianCalendar(Locale.ENGLISH);
            java.util.Date curTime = new java.util.Date();
            now.setTime(curTime);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        
            if (dOB.length() >= 8) {
            // boneheaded calendar numbers months from 0
                cal.set(Integer.parseInt(dOB.substring(0,4)), Integer.parseInt(dOB.substring(4,6))-1, Integer.parseInt(dOB.substring(6,8)));
                   
                this.dOB = dateFormat.format(cal.getTime());
            } else {
                this.dOB = "";
            }

            try {
                this.patientName = patientName.substring(0, patientName.indexOf("^")) + ", "
                + patientName.substring(patientName.indexOf("^") + 1).replace('^', ' ');
            } catch (IndexOutOfBoundsException e) {
                this.patientName = patientName.replace('^', ' ');
            }
            this.sex = sex;            
            this.homePhone = homePhone;
            this.healthNumber = healthNumber;
            // this.patientLocation = patientLocation;
            this.patientLocation = "";
            if ( dOB.length() > 0 ) {
                patientAge = now.get(Calendar.YEAR) - cal.get(Calendar.YEAR);
                if ( now.get(Calendar.MONTH) < cal.get(Calendar.MONTH) ) {
                    patientAge--;
                }
                if ( now.get(Calendar.MONTH) == cal.get(Calendar.MONTH) && now.get(Calendar.DAY_OF_MONTH) < cal.get(Calendar.DAY_OF_MONTH) ) {
                    patientAge--;
                }
                this.age = String.valueOf(patientAge);
            } else {
                this.age = "N/A";
            }
            this.workPhone = "";
        }


        public String getPatientName()
        {
                return this.patientName;
        }


        public String getSex()
        {
                return this.sex;
        }

        public String getDOB()
        {
            return this.dOB;
        }

        public String getAge()
        {
                return this.age;
        }


        public String getHomePhone()
        {
            return this.homePhone;
        }


        public String getWorkPhone()
        {
            return this.workPhone;
        }

        public String getPatientLocation()
        {
            return this.patientLocation;
        }

        public String getHealthNumber() 
        {
	    return this.healthNumber;
	}

    }

}