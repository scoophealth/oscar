package oscar.util;

import java.io.*;
import java.util.*;
import java.util.zip.*;

import oscar.OscarProperties;
public class zip
{
    zip(String fileformat){
        write2Zip(fileformat);
    }
    public void write2Zip(String fileformat){
        System.out.println("writing to Zip");
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
                System.out.println("Adding: "+files[i]);
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
            e.printStackTrace();
        }    
    }
}