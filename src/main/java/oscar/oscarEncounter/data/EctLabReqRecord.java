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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarDB.DBHandler;
import oscar.util.ConversionUtils;
import oscar.util.UtilDateUtilities;

public class EctLabReqRecord
{
    private static Logger logger = MiscUtils.getLogger(); 

    public Properties getLabReqRecord(int demographicNo, int existingID, int provNo)
            throws SQLException
    {
        Properties props = new Properties();

        
        ResultSet rs;
        String sql;

        if(existingID <= 0) {
        	DemographicDao dao = SpringUtils.getBean(DemographicDao.class);
        	Demographic demo = dao.getDemographicById(demographicNo);
        	
            if(demo != null) {
                    java.util.Date dob = ConversionUtils.fromDateString(demo.getBirthDayAsString());

                    props.setProperty("demographic_no", "" + demo.getDemographicNo());
                    props.setProperty("patientName", "" + demo.getFullName());
                    props.setProperty("healthNumber", demo.getHin());
                    props.setProperty("version", demo.getVer());
                    props.setProperty("formCreated", UtilDateUtilities.DateToString(new Date(), "yyyy/MM/dd"));
                    props.setProperty("formEdited", UtilDateUtilities.DateToString(new Date(), "yyyy/MM/dd"));
                    props.setProperty("birthDate", UtilDateUtilities.DateToString(dob, "yyyy/MM/dd"));
                    props.setProperty("homePhone", demo.getPhone());
                    props.setProperty("patientAddress", demo.getAddress());
                    props.setProperty("patientCity", demo.getCity());
                    props.setProperty("patientPC", demo.getPostal());
                    props.setProperty("province", demo.getProvince());
                    props.setProperty("sex", demo.getSex());
                }

                // from provider table
            	ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
            	Provider provider = providerDao.getProvider("" + provNo);

                if(provider != null)
                {
                    String num = provider.getOhipNo();
                    props.setProperty("provName", provider.getFormattedName());
                    props.setProperty("practitionerNo", "0000-"+num+"-00");
                }
           
        }
        else
        {
            sql = "SELECT * FROM formLabReq WHERE demographic_no = " + demographicNo + " AND ID = " + existingID;

            rs = DBHandler.GetSQL(sql);

            if(rs.next())
            {
                ResultSetMetaData md = rs.getMetaData();

                for(int i=1; i<=md.getColumnCount(); i++)
                {
                    String name = md.getColumnName(i);
                    String value;

                    if(md.getColumnTypeName(i).equalsIgnoreCase("TINY"))
                    {
                        if(rs.getInt(i)==1)
                        {
                            value = "checked='checked'";
                        }
                        else
                        {
                            value = "";
                        }
                    }
                    else
                    {
                        if(md.getColumnTypeName(i).equalsIgnoreCase("date"))
                        {
                            value = UtilDateUtilities.DateToString(rs.getDate(i), "yyyy/MM/dd");
                        }
                        else
                        {
                            value = oscar.Misc.getString(rs, i);
                        }
                    }

                    if(value!=null)
                    {
                        props.setProperty(name, value);
                    }
                }
            }
            rs.close();
        }
        return props;
    }

    public int saveLabReqRecord(Properties props)  throws SQLException  {
        String demographic_no = props.getProperty("demographic_no");

        
        String sql="SELECT * FROM formLabReq WHERE demographic_no=" + demographic_no + " AND ID=0";
        ResultSet rs = DBHandler.GetSQL(sql, true);

        rs.moveToInsertRow();

        ResultSetMetaData md = rs.getMetaData();

        for(int i=1; i<=md.getColumnCount(); i++)
        {
            String name = md.getColumnName(i);

            if(name.equalsIgnoreCase("ID"))
            {
                rs.updateNull(name);
            }
            else
            {
                String value = props.getProperty(name, null);

                if(md.getColumnTypeName(i).equalsIgnoreCase("TINY"))
                {
                    if(value!=null)
                    {
                        if(value.equalsIgnoreCase("on"))
                        {
                            rs.updateInt(name, 1);
                        }
                        else
                        {
                            rs.updateInt(name, 0);
                        }
                    }
                    else
                    {
                        rs.updateInt(name, 0);
                    }
                }
                else
                {
                    if(md.getColumnTypeName(i).equalsIgnoreCase("date"))
                    {
                        java.util.Date d;

                        if(md.getColumnName(i).equalsIgnoreCase("formEdited"))
                        {
                            d = new Date();
                        }
                        else
                        {
                            d = UtilDateUtilities.StringToDate(value, "yyyy/MM/dd");
                        }

                        if(d==null)
                        {
                            rs.updateNull(name);
                        }
                        else
                        {
                            rs.updateDate(name, new java.sql.Date(d.getTime()));
                        }
                    }
                    else
                    {
                        if(value==null)
                        {
                            rs.updateNull(name);
                        }
                        else
                        {
                            rs.updateString(name, value);
                        }
                    }
                }
            }
        }

        rs.insertRow();

        rs.close();

        int ret = 0;

        sql = "SELECT LAST_INSERT_ID()";
        rs = DBHandler.GetSQL(sql);
        if(rs.next())
        {
            ret = rs.getInt(1);
        }
        rs.close();
        return ret;
    }
}
