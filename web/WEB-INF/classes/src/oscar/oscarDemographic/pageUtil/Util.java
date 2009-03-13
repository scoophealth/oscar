/*
 * Util.java
 *
 * Created on March 7, 2009, 7:51 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package oscar.oscarDemographic.pageUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.xmlbeans.GDateBuilder;
import org.apache.xmlbeans.XmlCalendar;

/**
 *
 * @author Ronnie
 */
public class Util {
    
    static String appendLine(String baseStr, String addStr) {
	return appendLine(baseStr, "", addStr);
    }
    
    static String appendLine(String baseStr, String label, String addStr) {
	String newStr = noNull(baseStr);
	if (filled(newStr)) {
	    newStr += filled(addStr) ? "\n"+noNull(label)+addStr : "";
	} else {
	    newStr += filled(addStr) ? noNull(label)+addStr : "";
	}
	return newStr;
    }
    
    static public XmlCalendar calDate(Date inDate) {
	if (inDate==null) return null;
	
	GDateBuilder gd = new GDateBuilder();
	gd.setDate(inDate);
	if (gd.getYear()<1600) {
	    return null;
	} else {
	    gd.clearTimeZone();
	    return gd.getCalendar();
	}
    }
    
    static boolean cleanFile(String filename) {
	File f = new File(filename);
	return f.delete();
    }
    
    static public boolean convert10toboolean(String s){
	Boolean ret = false;
	if ( s!= null && s.trim().equals("1") ){
	    ret = true; 
	}
	return ret;
    }
    
    static public boolean filled(String s) {
	return (s!=null && s.trim().length()>0);
    }
    
    static public String noNull(String maybeNullText) {
	return filled(maybeNullText) ? maybeNullText : "";
    }
    
    static public cdsDt.HealthCardProvinceCode.Enum setProvinceCode(String provinceCode) {
	provinceCode = setCountrySubDivCode(provinceCode);
	if (provinceCode.equals("US")) return cdsDt.HealthCardProvinceCode.X_50; //Not available, temporarily
	if (provinceCode.equals("non-Canada/US")) return cdsDt.HealthCardProvinceCode.X_90; //Not applicable
	return cdsDt.HealthCardProvinceCode.Enum.forString(provinceCode);
    }
	
    static public String setCountrySubDivCode(String countrySubDivCode) {
	countrySubDivCode = countrySubDivCode.trim();
	if (filled(countrySubDivCode)) {
	    if (countrySubDivCode.equals("OT")) return "non-Canada/US";
	    if (!countrySubDivCode.substring(0,2).equals("US")) countrySubDivCode = "CA-"+countrySubDivCode;
	}
	return "";
    }
    
    static public void writeNameSimple(cdsDt.PersonNameSimpleWithMiddleName personName, String firstName, String lastName) {
	if (!filled(firstName)) firstName = "";
	if (!filled(lastName))  lastName = "";
	personName.setFirstName(firstName);
	personName.setLastName(lastName);
    }
    
    static public void writeNameSimple(cdsDt.PersonNameSimple personName, String firstName, String lastName) {
	if (!filled(firstName)) firstName = "";
	if (!filled(lastName))  lastName = "";
	personName.setFirstName(firstName);
	personName.setLastName(lastName);
    }
    
    static public void writeNameSimple(cdsDt.PersonNameSimple personName, String name) {
        if (name.contains(",")) {
            String[] names = name.split(",");
            String firstName = "";
            for (int i=1; i<names.length; i++) firstName += filled(firstName) ? ","+names[i] : names[i];
            personName.setFirstName(firstName);
            personName.setLastName(names[0]);
        } else if (name.contains(" ")) {
            String[] names = name.split(" ");
            String firstName = "";
            for (int i=0; i<names.length-1; i++) firstName += filled(firstName) ? " "+names[i] : names[i];
            personName.setFirstName(firstName);
            personName.setLastName(names[names.length-1]);
        }
    }
    
    static public cdsDt.YnIndicatorsimple.Enum yn(String YorN) {
	YorN = YorN.trim().toLowerCase();
	if (YorN.equals("yes") || YorN.equals("y")) return cdsDt.YnIndicatorsimple.Y;
	else return cdsDt.YnIndicatorsimple.N;
    }
    
    static boolean zipFiles(File[] files, String zipFileName) throws Exception {
        /*                                                       */
        /* Zip file placed in the same directory as the 1st file */
        /*                                                       */
	byte[] buf = new byte[1024];
	ZipOutputStream zout = null;	
	try {		
		zout = new ZipOutputStream(new FileOutputStream(files[0].getParent()+"/"+zipFileName));
	} catch (IOException ex) {
		ex.printStackTrace();
		throw new Exception("Error: Cannot create ZIP file");
	}

	// Compress the input files
	for (int i=0; i<files.length; i++) {
	    String filePath = files[i].getAbsolutePath();
	    String fileName = files[i].getName();
	    FileInputStream fin = null;
	    try {
		    fin = new FileInputStream(filePath);
	    } catch (FileNotFoundException ex) {
		    ex.printStackTrace();
		    throw new Exception("Error: While zipping, Export File not found - " + fileName);
	    }
	    try {
		    // Add ZIP entry to output stream
		    zout.putNextEntry(new ZipEntry(fileName));
	    } catch (IOException ex) {
		    ex.printStackTrace();
		    throw new Exception("Error: Cannot add file to ZIP - " + fileName);
	    }

	    // Transfer bytes from the input files to the ZIP file
	    int len;
	    try {
		    while ((len = fin.read(buf)) > 0) zout.write(buf, 0, len);
	    } catch (IOException ex) {
		    ex.printStackTrace();
		    throw new Exception("Error: Cannot write data to ZIP - " + fileName);
	    }
	    try {
		    // Complete the entry
		    zout.closeEntry();
	    } catch (IOException ex) {
		    ex.printStackTrace();
		    throw new Exception("Error: Cannot complete ZIP entry - " + fileName);
	    }
	    try {
		    fin.close();
	    } catch (IOException ex) {
		    ex.printStackTrace();
		    throw new Exception("Error: Cannot close input file - " + fileName);
	    }
	}
	try {
		// Complete the ZIP file
		zout.close();
	} catch (IOException ex) {
		ex.printStackTrace();
		throw new Exception("Error: Cannot close ZIP file");
	}
	return true;
    }
}
