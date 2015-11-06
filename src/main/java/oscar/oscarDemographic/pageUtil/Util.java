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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlCalendar;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.CaseManagementNoteExt;
import org.oscarehr.common.dao.PartialDateDao;
import org.oscarehr.common.model.PartialDate;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarPrevention.PreventionDisplayConfig;
import oscar.oscarProvider.data.ProviderData;
import oscar.util.StringUtils;
import oscar.util.UtilDateUtilities;

/**
 *
 * @author Ronnie
 */
public class Util {
	static private final Logger logger = MiscUtils.getLogger();
	static private final PartialDateDao partialDateDao = (PartialDateDao)SpringUtils.getBean("partialDateDao");
    
    static public String addLine(String baseStr, String addStr) {
	return addLine(baseStr, "", addStr);
    }
    
    static public String addLine(String baseStr, String label, String addStr) {
	String newStr = StringUtils.noNull(baseStr);
	if (StringUtils.filled(newStr)) {
	    newStr += StringUtils.filled(addStr) ? "\n"+StringUtils.noNull(label)+addStr : "";
	} else {
	    newStr += StringUtils.filled(addStr) ? StringUtils.noNull(label)+addStr : "";
	}
	return newStr;
    }

    static public String addSummary(String label, String item) {
	return addSummary("", label, item);
    }

    static public String addSummary(String summary, String label, String item) {
        if (summary==null) summary = addSummary(label, item);
        if (StringUtils.empty(item)) return summary;

        if (StringUtils.filled(summary)) summary += ",";
        summary += "["+label+"]:"+item;
	
	return summary;
    }
    
    static public XmlCalendar calDate(Date inDate) {
		String date = UtilDateUtilities.DateToString(inDate, "yyyy-MM-dd");
		String time = UtilDateUtilities.DateToString(inDate, "HH:mm:ss");
		try {
			XmlCalendar x = new XmlCalendar(date+"T"+time);
			return x;
		} catch (Exception ex) {
			XmlCalendar x = new XmlCalendar("0001-01-01T00:00:00");
			return x;
		}
    }

	static public XmlCalendar calDate(String inDate) {
		Date dateTime = UtilDateUtilities.StringToDate(inDate,"yyyy-MM-dd HH:mm:ss");
		if (dateTime==null) {
			dateTime = UtilDateUtilities.StringToDate(inDate,"yyyy-MM-dd");
		}
		if (dateTime==null) { //inDate may contain time only
			try {
				XmlCalendar x = new XmlCalendar(inDate);
				return x;
			} catch (Exception ex) { //inDate format is wrong
				XmlCalendar x = new XmlCalendar("0001-01-01T00:00:00");
				return x;
			}
		} else {
			return calDate(dateTime);
		}
	}

    static public boolean checkDir(String dirName) throws Exception {
        dirName = fixDirName(dirName);
        try {
            Runtime rt = Runtime.getRuntime();
            String[] env = {""};
            File dir = new File(dirName);
            Process proc = rt.exec("touch null.tmp", env, dir);
            int ecode = proc.waitFor();
            if (ecode == 0) {
                cleanFile("null.tmp", dirName);
                return true;
            } else {
                return false;
            }
        } catch (IOException ex) {logger.error("Error", ex);
        }
        logger.error("Error! Cannot write to directory [" + dirName + "]");
        return false;
    }

    static public boolean cleanFile(String filename, String dirname) {
        dirname = fixDirName(dirname);
	File f = new File(dirname+filename);
	return cleanFile(f);
    }

    static public boolean cleanFile(String filename) {
	File f = new File(filename);
	return cleanFile(f);
    }

    static public boolean cleanFile(File file) {
	if (!file.delete()) {
		logger.error("Error! Cannot delete file ["+file.getPath()+"]");
            return false;
        }
        return true;
    }
    
    static public void cleanFiles(ArrayList<File> files) {
        for (File file : files) {
            cleanFile(file);
        }
    }

    static public boolean convert10toboolean(String s){
	Boolean ret = false;
	if ( s!= null && s.trim().equals("1") ){
	    ret = true;
	}
	return ret;
    }

    static public void downloadFile(String fileName, String dirName, HttpServletResponse rsp) {
        try {
            dirName = fixDirName(dirName);
            if (rsp==null) return;

            rsp.setContentType("application/octet-stream");
            rsp.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            InputStream in = new FileInputStream(dirName + fileName);
            OutputStream out = rsp.getOutputStream();
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (IOException ex) {logger.error("Error", ex);
        }
    }

    static public String fixDirName(String dirName) {
        if (StringUtils.filled(dirName)) {
            if (dirName.charAt(dirName.length()-1)!='/') dirName = dirName + '/';
        }
        return StringUtils.noNull(dirName);
    }

    static public String mimeToExt(String mimeType) {
	String ret = "";
	if (!StringUtils.filled(mimeType)) return ret;
	if (mimeType.charAt(0)=='.') return mimeType;

	String type_ext = "application/envoy=evy|application/fractals=fif|application/futuresplash=spl|application/hta=hta|application/internet-property-stream=acx|application/mac-binhex40=hqx|application/msword=doc|application/msword=dot|application/octet-stream=bin|application/octet-stream=class|application/octet-stream=dms|application/octet-stream=exe|application/octet-stream=lha|application/octet-stream=lzh|application/oda=oda|application/olescript=axs|application/pdf=pdf|application/pics-rules=prf|application/pkcs10=p10|application/pkix-crl=crl|application/postscript=ai|application/postscript=eps|application/postscript=ps|application/rtf=rtf|application/set-payment-initiation=setpay|application/set-registration-initiation=setreg|application/vnd.ms-excel=xla|application/vnd.ms-excel=xlc|application/vnd.ms-excel=xlm|application/vnd.ms-excel=xls|application/vnd.ms-excel=xlt|application/vnd.ms-excel=xlw|application/vnd.ms-outlook=msg|application/vnd.ms-pkicertstore=sst|application/vnd.ms-pkiseccat=cat|application/vnd.ms-pkistl=stl|application/vnd.ms-powerpoint=pot|application/vnd.ms-powerpoint=pps|application/vnd.ms-powerpoint=ppt|application/vnd.ms-project=mpp|application/vnd.ms-works=wcm|application/vnd.ms-works=wdb|application/vnd.ms-works=wks|application/vnd.ms-works=wps|application/winhlp=hlp|application/x-bcpio=bcpio|application/x-cdf=cdf|application/x-compress=z|application/x-compressed=tgz|application/x-cpio=cpio|application/x-csh=csh|application/x-director=dcr|application/x-director=dir|application/x-director=dxr|application/x-dvi=dvi|application/x-gtar=gtar|application/x-gzip=gz|application/x-hdf=hdf|application/x-internet-signup=ins|application/x-internet-signup=isp|application/x-iphone=iii|application/x-javascript=js|application/x-latex=latex|application/x-msaccess=mdb|application/x-mscardfile=crd|application/x-msclip=clp|application/x-msdownload=dll|application/x-msmediaview=m13|application/x-msmediaview=m14|application/x-msmediaview=mvb|application/x-msmetafile=wmf|application/x-msmoney=mny|application/x-mspublisher=pub|application/x-msschedule=scd|application/x-msterminal=trm|application/x-mswrite=wri|application/x-netcdf=cdf|application/x-netcdf=nc|application/x-perfmon=pma|application/x-perfmon=pmc|application/x-perfmon=pml|application/x-perfmon=pmr|application/x-perfmon=pmw|application/x-pkcs12=p12|application/x-pkcs12=pfx|application/x-pkcs7-certificates=p7b|application/x-pkcs7-certificates=spc|application/x-pkcs7-certreqresp=p7r|application/x-pkcs7-mime=p7c|application/x-pkcs7-mime=p7m|application/x-pkcs7-signature=p7s|application/x-sh=sh|application/x-shar=shar|application/x-shockwave-flash=swf|application/x-stuffit=sit|application/x-sv4cpio=sv4cpio|application/x-sv4crc=sv4crc|application/x-tar=tar|application/x-tcl=tcl|application/x-tex=tex|application/x-texinfo=texi|application/x-texinfo=texinfo|application/x-troff=roff|application/x-troff=t|application/x-troff=tr|application/x-troff-man=man|application/x-troff-me=me|application/x-troff-ms=ms|application/x-ustar=ustar|application/x-wais-source=src|application/x-x509-ca-cert=cer|application/x-x509-ca-cert=crt|application/x-x509-ca-cert=der|application/ynd.ms-pkipko=pko|application/zip=zip|audio/basic=au|audio/basic=snd|audio/mid=mid|audio/mid=rmi|audio/mpeg=mp3|audio/x-aiff=aif|audio/x-aiff=aifc|audio/x-aiff=aiff|audio/x-mpegurl=m3u|audio/x-pn-realaudio=ra|audio/x-pn-realaudio=ram|audio/x-wav=wav|image/png=png|image/bmp=bmp|image/cis-cod=cod|image/gif=gif|image/ief=ief|image/jpeg=jpe|image/jpeg=jpeg|image/jpeg=jpg|image/pipeg=jfif|image/svg+xml=svg|image/tiff=tif|image/tiff=tiff|image/x-cmu-raster=ras|image/x-cmx=cmx|image/x-icon=ico|image/x-portable-anymap=pnm|image/x-portable-bitmap=pbm|image/x-portable-graymap=pgm|image/x-portable-pixmap=ppm|image/x-rgb=rgb|image/x-xbitmap=xbm|image/x-xpixmap=xpm|image/x-xwindowdump=xwd|message/rfc822=mht|message/rfc822=mhtml|message/rfc822=nws|text/css=css|text/h323=323|text/html=htm|text/html=html|text/html=stm|text/iuls=uls|text/plain=bas|text/plain=c|text/plain=h|text/plain=txt|text/richtext=rtx|text/scriptlet=sct|text/tab-separated-values=tsv|text/webviewhtml=htt|text/x-component=htc|text/x-setext=etx|text/x-vcard=vcf|video/mpeg=mp2|video/mpeg=mpa|video/mpeg=mpe|video/mpeg=mpeg|video/mpeg=mpg|video/mpeg=mpv2|video/quicktime=mov|video/quicktime=qt|video/x-la-asf=lsf|video/x-la-asf=lsx|video/x-ms-asf=asf|video/x-ms-asf=asr|video/x-ms-asf=asx|video/x-msvideo=avi|video/x-sgi-movie=movie|x-world/x-vrml=flr|x-world/x-vrml=vrml|x-world/x-vrml=wrl|x-world/x-vrml=wrz|x-world/x-vrml=xaf|x-world/x-vrml=xof|";
	mimeType = mimeType.toLowerCase();
	type_ext = type_ext.toLowerCase();
	int pos = type_ext.indexOf(mimeType);
	if (pos>-1) {
	    pos = pos + mimeType.length() + 1;
	    int end_pos = type_ext.indexOf('|', pos);
	    ret = "." + type_ext.substring(pos, end_pos);
	}
	return ret;
    }

    static public cdsDt.HealthCardProvinceCode.Enum setProvinceCode(String provinceCode) {
	provinceCode = setCountrySubDivCode(provinceCode);
	if (provinceCode.equals("US")) return cdsDt.HealthCardProvinceCode.X_50; //Not available, temporarily
	if (provinceCode.equals("non-Canada/US")) return cdsDt.HealthCardProvinceCode.X_90; //Not applicable
	return cdsDt.HealthCardProvinceCode.Enum.forString(provinceCode);
    }
	
    static public String setCountrySubDivCode(String countrySubDivCode) {
	countrySubDivCode = countrySubDivCode.trim();
	if (StringUtils.filled(countrySubDivCode)) {
	    if (countrySubDivCode.equals("OT")) return "Other";
	    if (!countrySubDivCode.startsWith("US")) return "CA-"+countrySubDivCode;
	}
	return "";
    }

    static public String onlyNum(String s) {
        if (s==null) return null;

        for (int i=0; i<s.length(); i++) {
            if (!"0123456789".contains(s.substring(i, i+1))) {
                s = s.substring(0,i)+s.substring(i+1,s.length());
                i--;
            }
        }
        return s;
    }

    static public String leadingNum(String s) {
    	if (s==null) return "";
        s = s.trim();
        for (int i=0; i<s.length(); i++) {
            if (!".0123456789".contains(s.substring(i,i+1))) {
                s = s.substring(0, i);
                break;
            }
        }
        return s;
    }

    static public float leadingNumF(String s) {
    	s = leadingNum(s);
        try {
        	float f = Float.parseFloat(s);
        	return f;
        } catch(NumberFormatException e) {
        	return 0;
        }       
    }

    static public String trailingTxt(String s) {
        s = s.trim();
        for (int i=0; i<s.length(); i++) {
            if (!".0123456789".contains(s.substring(i,i+1))) {
                s = s.substring(i);
                break;
            }
        }
        return s.trim();
    }

    
    static public void writeNameSimple(cdsDt.PersonNameSimpleWithMiddleName personName, String firstName, String lastName) {
	if (!StringUtils.filled(firstName)) firstName = "";
	if (!StringUtils.filled(lastName))  lastName = "";
	personName.setFirstName(firstName);
	personName.setLastName(lastName);
    }
    
    static public void writeNameSimple(cdsDt.PersonNameSimple personName, String firstName, String lastName) {
	if (!StringUtils.filled(firstName)) firstName = "";
	if (!StringUtils.filled(lastName))  lastName = "";
	personName.setFirstName(firstName);
	personName.setLastName(lastName);
    }
    
    static public void writeNameSimple(cdsDt.PersonNameSimple personName, String name) {
	if (!StringUtils.filled(name)) name = "";
        if (name.contains(",")) {
            String[] names = name.split(",");
            String firstName = "";
            for (int i=1; i<names.length; i++) firstName += StringUtils.filled(firstName) ? ","+names[i] : names[i];
            personName.setFirstName(firstName);
            personName.setLastName(names[0]);
        } else if (name.contains(" ")) {
            String[] names = name.split(" ");
            String firstName = "";
            for (int i=0; i<names.length-1; i++) firstName += StringUtils.filled(firstName) ? " "+names[i] : names[i];
            personName.setFirstName(firstName);
            personName.setLastName(names[names.length-1]);
        } else {
	    personName.setFirstName(name);
	    personName.setLastName("");
	}
    }
    
    static public cdsDt.YnIndicatorsimple.Enum yn(String YorN) {
	YorN = YorN.trim().toLowerCase();
	if (YorN.equals("yes") || YorN.equals("y")) return cdsDt.YnIndicatorsimple.Y;
	else return cdsDt.YnIndicatorsimple.N;
    }
    
    static public boolean zipFiles(ArrayList<File> files, String zipFileName, String dirName) throws Exception {
        try {
            if (files == null) {
            	logger.error("Error! No source file for zipping");
                return false;
            }
            if (!StringUtils.filled(zipFileName)) {
            	logger.error("Error! Zip filename not given");
                return false;
            }
            if (!checkDir(dirName)) {
                return false;
            }
            dirName = fixDirName(dirName);
            byte[] buf = new byte[1024];
            ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(dirName + zipFileName));
            for (File f : files) {
                if (f == null) continue;

                FileInputStream fin = new FileInputStream(f.getAbsolutePath());

                // Add ZIP entry to output stream
                zout.putNextEntry(new ZipEntry(f.getName()));

                // Transfer bytes from the input files to the ZIP file
                int len;
                while ((len = fin.read(buf)) > 0) {
                    zout.write(buf, 0, len);
                }

                // Complete the entry
                zout.closeEntry();
                fin.close();
            }
            // Complete the ZIP file
            zout.close();
            return true;

        } catch (IOException ex) {logger.error("Error", ex);
        }
        return false;
    }

    static public void putPartialDate(cdsDt.DateFullOrPartial dfp, CaseManagementNoteExt cme) {
    	putPartialDate(dfp, cme.getDateValue(), cme.getValue());
    }

    static public void putPartialDate(cdsDt.DateFullOrPartial dfp, Date dateValue, Integer tableName, Integer tableId, Integer fieldName) {
    	PartialDate pd = partialDateDao.getPartialDate(tableName, tableId, fieldName);
    	putPartialDate(dfp, dateValue, pd.getFormat());
    }

    static public void putPartialDate(cdsDt.DateFullOrPartial dfp, Date dateValue, String format) {
        if (dateValue!=null) {
            if (PartialDate.YEARONLY.equals(format)) dfp.setYearOnly(calDate(dateValue));
            else if (PartialDate.YEARMONTH.equals(format)) dfp.setYearMonth(calDate(dateValue));
            else dfp.setFullDate(calDate(dateValue));
        }
    }

    static public void putPartialDate(cdsDt.DateTimeFullOrPartial dfp, Date dateValue, String format) {
        if (dateValue!=null) {
            if (PartialDate.YEARONLY.equals(format)) dfp.setYearOnly(calDate(dateValue));
            else if (PartialDate.YEARMONTH.equals(format)) dfp.setYearMonth(calDate(dateValue));
            else dfp.setFullDate(calDate(dateValue));
        }
    }

    static public void putPartialDate(cdsDtCihi.DateFullOrPartial dfp, CaseManagementNoteExt cme) {
    	if (cme!=null) putPartialDate(dfp, cme.getDateValue(), cme.getValue());
    }

    static public void putPartialDate(cdsDtCihi.DateFullOrPartial dfp, Date dateValue, Integer tableName, Integer tableId, Integer fieldName) {
    	String format = null;
    	PartialDate pd = partialDateDao.getPartialDate(tableName, tableId, fieldName);
    	if (pd!=null) format = pd.getFormat();
    	putPartialDate(dfp, dateValue, format);
    }

    static public void putPartialDate(cdsDtCihi.DateFullOrPartial dfp, Date dateValue, String format) {
        if (dateValue!=null) {
            if (PartialDate.YEARONLY.equals(format)) dfp.setYearOnly(calDate(dateValue));
            else if (PartialDate.YEARMONTH.equals(format)) dfp.setYearMonth(calDate(dateValue));
            else dfp.setFullDate(calDate(dateValue));
        }
    }
    
    static public String readPartialDate(CaseManagementNoteExt cme) {
        String type = cme.getValue();
        String val = null;

        if (StringUtils.filled(type)) {
            if (type.equals(PartialDate.YEARONLY))
                val = oscar.util.UtilDateUtilities.DateToString(cme.getDateValue(),"yyyy");
            else if (type.equals(PartialDate.YEARMONTH))
                val = oscar.util.UtilDateUtilities.DateToString(cme.getDateValue(),"yyyy-MM");
            else val = oscar.util.UtilDateUtilities.DateToString(cme.getDateValue(),"yyyy-MM-dd");
        } else {
            val = oscar.util.UtilDateUtilities.DateToString(cme.getDateValue(),"yyyy-MM-dd");
        }
        return val;
    }
    
    static public String formatIcd9(String code) {
        if (StringUtils.empty(code)) return code;
        if (code.length()<=3) return code;

        String codeFormatted = code.substring(0,3);
        code = code.substring(3);
        while (code.length()>3) {
            codeFormatted += "."+code.substring(0,3);
            code = code.substring(3);
        }
        if (code.length()>0) codeFormatted += "."+code;

        return codeFormatted;
    }
    
    static public final SpokenLangProperties spokenLangProperties = SpokenLangProperties.getInstance();
    static public final RosterTermReasonProperties rosterTermReasonProperties = RosterTermReasonProperties.getInstance();
    
    static public String convertLanguageToCode(String lang) {
    	return spokenLangProperties.getCodeByLang(lang);
    }

    static public String convertCodeToLanguage(String code) {
    	return spokenLangProperties.getLangByCode(code);
    }

	static private final String verified = "[Verified and Signed";
	
    static public boolean isVerified(CaseManagementNote cmn) {
    	if (cmn==null) return false;
    	
    	String note = cmn.getNote();
    	if (StringUtils.empty(note)) return false;
    	if (!note.contains("\n")) return false;
    	
    	int marker = note.substring(0, note.lastIndexOf("\n")).lastIndexOf("\n");
    	if (marker<0) return false;
    	
    	String vtext = verified;
    	if (cmn.getUpdate_date()!=null) vtext += " on "+UtilDateUtilities.DateToString(cmn.getUpdate_date(), "dd-MMM-yyyy HH:mm");
    	String providerName = ProviderData.getProviderName(cmn.getProviderNo());
    	if (StringUtils.filled(providerName)) vtext += " by "+providerName;

    	return note.substring(marker+1).startsWith(vtext);
    }
    
    static public void writeVerified(CaseManagementNote cmn) {
    	if (isVerified(cmn)) return;
    	
    	String note = cmn.getNote();
    	if (StringUtils.empty(note)) return;
    	
    	String vtext = verified;
    	if (cmn.getUpdate_date()!=null) vtext += " on "+UtilDateUtilities.DateToString(cmn.getUpdate_date(), "dd-MMM-yyyy HH:mm");
    	String providerName = ProviderData.getProviderName(cmn.getProviderNo());
    	if (StringUtils.filled(providerName)) vtext += " by "+providerName;
    	
    	note = Util.addLine(note, "\n"+vtext+"]\n");
    	cmn.setNote(note);
    }
    
    
    
    public static Map<String,Object> getPreventionTypes(LoggedInInfo loggedInInfo) {
        HashMap<String,String> preventionToImmunizationType = new HashMap<String,String>(); 
        HashMap<String,String> immunizationToPreventionType = new HashMap<String,String>();
        ArrayList<String> nonImmunizationPreventionType = new ArrayList<String>();


        PreventionDisplayConfig pdc = PreventionDisplayConfig.getInstance(loggedInInfo);
        ArrayList<HashMap<String,String>> prevTypeList = pdc.getPreventions(loggedInInfo);
        
        for (HashMap<String,String> prevTypeHash : prevTypeList) {
            if (prevTypeHash != null && StringUtils.filled(prevTypeHash.get("layout"))) {
            	if (prevTypeHash.get("layout").equals("injection")) {
	            	preventionToImmunizationType.put(prevTypeHash.get("name"), prevTypeHash.get("healthCanadaType"));
	            	immunizationToPreventionType.put(prevTypeHash.get("healthCanadaType"), prevTypeHash.get("name"));
            	} else {
            		nonImmunizationPreventionType.add(prevTypeHash.get("name"));
            	}
            }
        }
        
        Map<String,Object> results = new HashMap<String,Object>();
        results.put("preventionToImmunizationType", preventionToImmunizationType);
        results.put("immunizationToPreventionType", immunizationToPreventionType);
        results.put("nonImmunizationPreventionType", nonImmunizationPreventionType);
        
        return results;
    }
    
    static public String getImmunizationType(LoggedInInfo loggedInInfo, String preventionType,Map<String,Object> prevTypesContainer) {
    	HashMap<String,String> preventionToImmunizationType = (HashMap<String,String>)prevTypesContainer.get("preventionToImmunizationType");
    	if(preventionToImmunizationType != null) {
    		return preventionToImmunizationType.get(preventionType);
    	}
    	return null;
    }
    
    static public String getPreventionType(LoggedInInfo loggedInInfo, String immunizationType, Map<String,Object> prevTypesContainer) {
    	HashMap<String,String> immunizationToPreventionType =  (HashMap<String,String>)prevTypesContainer.get("immunizationToPreventionType");
    	if(immunizationToPreventionType != null) {
    		return immunizationToPreventionType.get(immunizationType);
    	}
    	return null;
    }

    static public boolean isNonImmunizationPrevention(LoggedInInfo loggedInInfo, String type, Map<String,Object> prevTypesContainer) {
    	ArrayList<String> nonImmunizationPreventionType = (ArrayList<String>)prevTypesContainer.get("nonImmunizationPreventionType");
    	if(nonImmunizationPreventionType != null) {
    		return nonImmunizationPreventionType.contains(type);
    	}
    	return false;
    }
    
    static public String replaceTags(String s) {
    	if (StringUtils.empty(s)) return s;
    	
    	String tag = "<br />";
    	s = s.replace(tag, "\n");
    	tag = "<br/>";
    	s = s.replace(tag, "\n");
    	tag = "<br>";
    	s = s.replace(tag, "\n");
    	tag = "&nbsp;";
    	s = s.replace(tag, " ");
    	
    	return s;
    }
}
