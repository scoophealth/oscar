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


package oscar.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;
public class zip
{
	
	private static Logger logger = MiscUtils.getLogger();
	
    /**
     * default contructor
     * this constructor is used to avoid unused local variables/for clean code
     */
    zip(){}
    zip(String fileformat){
        write2Zip(fileformat);
    }
    public void write2Zip(String fileformat){
        MiscUtils.getLogger().debug("writing to Zip");
        try {
            BufferedInputStream origin = null;
            int BUFFER = 1024;
            String form_record_path = OscarProperties.getInstance().getProperty("form_record_path", "/root"); 
            FileOutputStream dest = new FileOutputStream(form_record_path + "formRecords.zip");
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            out.setMethod(ZipOutputStream.DEFLATED);
            byte data[] = new byte[BUFFER];
            //get a list of files from current directory
            File f = new File(form_record_path+".");        
            String files[] = f.list();

            for (int i=0; i<files.length; i++) {
                MiscUtils.getLogger().debug("Adding: "+files[i]);
                if(files[i].endsWith("."+fileformat)){
                    FileInputStream fi = new FileInputStream(form_record_path+files[i]);
                    origin = new BufferedInputStream(fi, BUFFER);
                    ZipEntry entry = new ZipEntry(files[i]);                  
                    out.putNextEntry(entry);
                    int count;
                    while((count = origin.read(data, 0, BUFFER)) != -1) {
                       out.write(data, 0, count);
                    }
                    origin.close();    
                }
            }
            out.close();
        } 
        catch(Exception e) {
            MiscUtils.getLogger().error("Error", e);
        }    
    }
    
    public static boolean unzipXML(String dirName, String fName) {
    	int BUFFER = 2048;
    	
    	Enumeration<? extends ZipEntry> entries;
    	boolean result = false;
    	String fullpath = dirName + fName;	
    	if (!fName.substring(fName.length()-4).equalsIgnoreCase(".zip")) {
    		logger.error("unzipXML: "+fName+" does not have .zip extension.");
    		return result;
    	}
    	BufferedOutputStream dest = null;
    	BufferedInputStream is = null;
    	ZipEntry entry;

    	try {
    		ZipFile zipfile = new ZipFile(fullpath);

    		entries = zipfile.entries();
    		while(entries.hasMoreElements()) {
    			entry = entries.nextElement();
    			String zName = entry.getName();
    			is = new BufferedInputStream(zipfile.getInputStream(entry));
    			int count;
    			byte data[] = new byte[BUFFER];
    			if (!zName.substring(zName.length()-4).equalsIgnoreCase(".zip")) {
    				zName = zName+".xml";
    			}
    			File z = new File(dirName+zName);
    			FileOutputStream fos = new FileOutputStream(z);
    			dest = new BufferedOutputStream(fos, BUFFER);
    			while ((count = is.read(data, 0, BUFFER)) != -1) {
    				dest.write(data, 0, count);
    			}
    			dest.flush();
    			dest.close();
    			is.close();
    		}
    		zipfile.close();
    		//nee to move zip file to archive folder
    		File afile = new File(fullpath);
    		File dir = new File(dirName+"unzip_archive/"); 
    		Boolean success = afile.renameTo(new File(dir, afile.getName()));
    		if (!success) {
    			logger.error("oscar.util.zip.unzipXML: the zip file "+fullpath+" was not archived");
    		}
    	} catch(Exception e) {
    		logger.error("oscar.util.zip.unzipXML Unhandled exception:", e);
    		return result;
    	} 
    	result = true;
    	return result;
    }
}

