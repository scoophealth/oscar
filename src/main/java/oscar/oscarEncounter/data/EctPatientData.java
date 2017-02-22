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


package oscar.oscarEncounter.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;
import oscar.log.LogAction;
import oscar.oscarDB.DBHandler;
import oscar.util.UtilDateUtilities;

public class EctPatientData {

    public static String getProviderNo(LoggedInInfo loggedInInfo, String demographicNo) {
        String ret = "";
        ResultSet rs = null;
        try {

           rs = DBHandler.GetSQL("SELECT provider_no FROM demographic WHERE demographic_no = "
                            + demographicNo);
            if (rs.next())
                ret = oscar.Misc.getString(rs, "provider_no");
            
        } catch (SQLException e) {
            MiscUtils.getLogger().debug("error - EctPatientData.getProviderNo");
        }
        finally {
			if( rs != null ) {
				try {
					rs.close();
				} catch (SQLException e) {
					
				}
			}
		}
        LogAction.addLogSynchronous(loggedInInfo, "EctPatientData.getProviderNo", "demographicNo=" + demographicNo);
        
        
        return ret;
    }

    public Patient getPatient(LoggedInInfo loggedInInfo, String demographicNo) {

        Patient p = null;
        ResultSet rs = null;
        try {
             rs = DBHandler.GetSQL("SELECT demographic_no, last_name, first_name, sex, year_of_birth, month_of_birth, date_of_birth, address, city, postal, phone, roster_status FROM demographic WHERE demographic_no = "
                            + demographicNo);
            if (rs.next())
                p = new Patient(rs.getInt("demographic_no"), oscar.Misc.getString(rs, "last_name"), oscar.Misc.getString(rs, "first_name"),
                        oscar.Misc.getString(rs, "sex"), UtilDateUtilities.calcDate(oscar.Misc.getString(rs, "year_of_birth"), rs
                                .getString("month_of_birth"), oscar.Misc.getString(rs, "date_of_birth")),
                        oscar.Misc.getString(rs, "address"), oscar.Misc.getString(rs, "city"), oscar.Misc.getString(rs, "postal"), oscar.Misc.getString(rs, "phone"),
                        oscar.Misc.getString(rs, "roster_status"));
            
        } catch (SQLException e) {
            MiscUtils.getLogger().error("Error", e);
        }
        finally {
        	if( rs != null ) {
        		try {        	
        			rs.close();
        		} catch (SQLException e) {
				
        		}
        	}
        }
        
        LogAction.addLogSynchronous(loggedInInfo, "EctPatientData.getPatient", "demographicNo=" + demographicNo);
        
        return p;
    }

    public class Patient {
        int demographicNo;
        String surname;
        String firstName;
        String sex;
        Date DOB;
        String address;
        String city;
        String postal;
        String phone;
        String roster;

        public Patient(int demographicNo, String surname, String firstName, String sex, Date DOB, String address,
                String city, String postal, String phone, String roster) {
            this.demographicNo = demographicNo;
            this.surname = surname;
            this.firstName = firstName;
            this.sex = sex;
            this.DOB = DOB;
            this.address = address;
            this.city = city;
            this.postal = postal;
            this.phone = phone;
            this.roster = roster;
        }

        public int getDemographicNo() {
            return demographicNo;
        }

        public String getSurname() {
            return surname;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getSex() {
            return sex;
        }

        public Date getDOB() {
            return DOB;
        }

        public String getAge() {
            return UtilDateUtilities.calcAge(getDOB());
        }

        public String getAddress() {
            return address;
        }

        public String getCity() {
            return city;
        }

        public String getPostal() {
            return postal;
        }

        public String getPhone() {
            return phone;
        }

        public String getRosterStatus() {
            return roster;
        }

        public eChart getEChart() {
            return new eChart();
        }

        /*******************************************************************************************
         * class eChart
         ******************************************************************************************/
        public class eChart {
            private Date eChartTimeStamp = null;
            private String socialHistory = "";
            private String familyHistory = "";
            private String medicalHistory = "";
            private String ongoingConcerns = "";
            private String reminders = "";
            private String encounter = "";
            private String subject = "";

            eChart() {
                init();
            }

            private void init() {
            	OscarProperties properties = OscarProperties.getInstance();
        		if( !Boolean.parseBoolean(properties.getProperty("AbandonOldChart", "false"))) {
        		    ResultSet rs = null;
	                try {

	                    String sql = "select * from eChart where demographicNo=" + demographicNo
	                            + " ORDER BY eChartId DESC";
	//                            + " ORDER BY eChartId DESC limit 1";
	                    rs = DBHandler.GetSQL(sql);
	                    if (rs.next()) {
	                        this.eChartTimeStamp = rs.getTimestamp("timeStamp");
	                        this.socialHistory = oscar.Misc.getString(rs, "socialHistory");
	                        this.familyHistory = oscar.Misc.getString(rs, "familyHistory");
	                        this.medicalHistory = oscar.Misc.getString(rs, "medicalHistory");
	                        this.ongoingConcerns = oscar.Misc.getString(rs, "ongoingConcerns");
	                        this.reminders = oscar.Misc.getString(rs, "reminders");
	                        this.encounter = oscar.Misc.getString(rs, "encounter");
	                        this.subject = oscar.Misc.getString(rs, "subject");
	                    }
	                    
	                } catch (SQLException e) {
	                    MiscUtils.getLogger().error("Error", e);
	                }
	                finally {
						if( rs != null ) {
							try {
								rs.close();
							} catch (SQLException e) {
								
							}
						}
					}
        		}
            }

            public Date getEChartTimeStamp() {
                return this.eChartTimeStamp;
            }

            public String getSocialHistory() {
                return this.socialHistory;
            }

            public String getFamilyHistory() {
                return this.familyHistory;
            }

            public String getMedicalHistory() {
                return this.medicalHistory;
            }

            public String getOngoingConcerns() {
                return this.ongoingConcerns;
            }

            public String getReminders() {
                return this.reminders;
            }

            public String getEncounter() {
                return this.encounter;
            }

            public String getSubject() {
                return this.subject;
            }
        }
    }
}
