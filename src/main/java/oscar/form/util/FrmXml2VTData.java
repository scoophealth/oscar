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
 * Created on Nov 18, 2004
 */
package oscar.form.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Properties;

import noNamespace.SitePatientVisitRecordsDocument;
import noNamespace.SitePatientVisitRecordsDocument.SitePatientVisitRecords;

import org.apache.commons.lang.WordUtils;
import org.apache.log4j.Logger;

import oscar.form.data.FrmVTData;

/**
 * @author yilee18
 */
public class FrmXml2VTData {
    private static final Logger _logger = Logger.getLogger(FrmXml2VTData.class);

    static String[] elementAttrName = new String[] { "value", "signed_when", "signed_who", "signed_how" };

    // HashTable:
    // "VTData":VTData; "VisitDrug":Vector-VisitDrug; "PatientContactInfo":PatientContactInfo
    public FrmVTData getObjectFromXmlStr(String strXml) throws Exception {


        FrmVTData vtData = new FrmVTData();

        // Parse XML string.
        SitePatientVisitRecordsDocument xmlDoc = SitePatientVisitRecordsDocument.Factory.parse(strXml);

        // Get object reference of root element SitePatientVisitRecords.
        SitePatientVisitRecords visit = xmlDoc.getSitePatientVisitRecords();
        SitePatientVisitRecords.SitePatientVisit [] visitRec = visit.getSitePatientVisitArray();
        // Get version value
        String version = visit.getVersion();

        // Get output Objects
        Class vtDataC = oscar.form.data.FrmVTData.class;
        Field[] vtFields = vtDataC.getDeclaredFields();
        Method[] vtMethods = vtDataC.getMethods();

        // Init - get properties object of Object attribute name
        Properties propVtFieldName = initPropFieldName(vtFields);

        // Assume only one visit record in the xml file, use the first element of the array
        // Set visitRecord attribute
        /*vtData.setVersion(version);
        vtData.setPatient_cod(visitRec[0].getPatientCod());
        vtData.setVisit_cod(visitRec[0].getVisitCod());
        vtData.setSite_cod(visitRec[0].getSiteCod());
        */
        // Map xml object methods to output object methods
        Class xmlInfo = visitRec[0].getClass();
        Method[] xmlMethods = xmlInfo.getDeclaredMethods();

        setObjectsProperty(xmlMethods, visitRec[0], vtData, vtDataC, propVtFieldName);

        return vtData;
    }

    static private void setObjectsProperty(Method[] xmlMethods, Object rec, Object ret, Class objC,
            Properties propPFieldName) {
        for (int i = 0; i < xmlMethods.length; i++) {
            String xmlMethodsName = xmlMethods[i].getName();
            // Only interested in method name startsWith get/is
            // is qualified xml method, for element type, i.e. (value, signed_when, ...)
            if (!isQualXmlMethod(xmlMethods[i], "(get|is).*"))
                continue;

            // Get xml value
            for (int k = 0; k < elementAttrName.length; k++) {
                String tempXmlValue = getXmlMethodValue(xmlMethods[i], rec, ("get" + getStdMethodName(elementAttrName[k])));

                if (tempXmlValue == null)
                    continue;

                // Set obj prop
                String xmlTempMethod = (k == 0) ? xmlMethodsName : (xmlMethodsName + (getStdMethodName("$"
                        + elementAttrName[k])));
                if (_logger.isDebugEnabled()) {
                    //_logger.debug("setObjectsProperty() -  : xmlTempMethod = " + xmlTempMethod);
                    //_logger.debug("setObjectsProperty() - : tempXmlValue = " + tempXmlValue);
                }

                setObjectProperty(ret, objC, propPFieldName, xmlTempMethod, tempXmlValue);
            }
        }
    }

    static private Properties initPropFieldName(Field[] fields) {
        Properties ret = new Properties();
        for (int i = 0; i < fields.length; i++) {
            String fieldName = fields[i].getName();
            String xmlFieldName = getStdMethodName(fieldName);
            ret.setProperty(xmlFieldName, WordUtils.capitalize(fieldName));
        }
        return ret;
    }

    static private Object setObjectProperty(Object ret, Class objC, Properties propPFieldName, String xmlMethodsName,
            String xmlValue) {
        if (xmlMethodsName.length() > 3 && propPFieldName.containsKey(xmlMethodsName.substring(3))) {
            try {
                Class[] argClass = new Class[] { String.class };
                Object[] arguments = new Object[] { xmlValue };
                // Find the obj set method and set prop
                Method tempPMethod = objC.getMethod("set"
                        + propPFieldName.getProperty(xmlMethodsName.substring(3), ""), argClass);
                tempPMethod.invoke(ret, arguments);

            } catch (Exception e) {
                // do nothing
            }
        }
        return ret;
    }

    static private String getXmlMethodValue(Method xmlMethod, Object rec, String methodName) {
        String tempXmlValue = null;
        try {
            Class tempC = xmlMethod.getReturnType();
            Method tempXmlMethod = tempC.getMethod(methodName);
            Object tempXmlObj = xmlMethod.invoke(rec);

            String tempXmlType = tempXmlMethod.getReturnType().getName();
            // Handle with types: txt_ ; b_ ; int_ ; dbl_ ; dat_
            if ("java.lang.String".equals(tempXmlType)) {
                tempXmlValue = (String) tempXmlMethod.invoke(tempXmlObj);
            } else if ("boolean".equals(tempXmlType)) {
                tempXmlValue = "" + ((Boolean) tempXmlMethod.invoke(tempXmlObj)).booleanValue();
            } else if ("int".equals(tempXmlType)) {
                tempXmlValue = "" + ((Integer) tempXmlMethod.invoke(tempXmlObj)).intValue();
            } else if ("double".equals(tempXmlType)) {
                tempXmlValue = "" + ((Double) tempXmlMethod.invoke(tempXmlObj)).doubleValue();
            } else {

                tempXmlValue = "" + tempXmlMethod.invoke(tempXmlObj);
            }
        } catch (Exception e) {
            // do nothing
        }
        return tempXmlValue;
    }

    // if method return a xml itme class, return true
    static private boolean isQualXmlMethod(Method xmlMethod, String reg) {
        boolean ret = false;
        if (xmlMethod.getName().matches(reg)) {
            Class tempC = xmlMethod.getReturnType();
            // Only interested in Classes (not drug part) rather than primitive data type
            if ("java.lang.String".equals(tempC.getName()) || "boolean".equals(tempC.getName())
                    || "getSitePatientVisitDrugArray".equals(xmlMethod.getName()))
                ret = false;
            else
                ret = true;
        }
        return ret;
    }

    static public String getStdMethodName(String str) {
        String ret = "";
        if (str != null) {
            String[] temp = str.split("_");
            for (int i = 0; i < temp.length; i++) {
                ret += WordUtils.capitalize(temp[i]);
            }
        }
        return ret;
    }
}
