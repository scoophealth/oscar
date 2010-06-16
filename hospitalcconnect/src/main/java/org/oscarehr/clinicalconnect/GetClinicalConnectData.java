/*
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 *  This software is published under the GPL GNU General Public License.
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 *
 *  This software was written for the
 *  Department of Family Medicine
 *  McMaster University
 *  Hamilton
 *  Ontario, Canada
 *
 *
 */
package org.oscarehr.clinicalconnect;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oscarehr.clinicalconnect.util.EmrDownloadEngine;
import org.oscarehr.clinicalconnect.ws.GeneratedFile;
import org.oscarehr.clinicalconnect.ws.GeneratedFileInfo;

/**
 *
 * @author jaygallagher
 */
public class GetClinicalConnectData {
    private static final Log log = LogFactory.getLog(GetClinicalConnectData.class);

    /**
     * Load a configuration file, (name of the file is either passed in or the file ClinicalConnect will be searched in the local directory)
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        GetClinicalConnectData clinConnect = new GetClinicalConnectData();

        Properties properties = new Properties();

        String group = "";
        String password = "";

        String serviceUsername = "";
        String servicePassword = "";
        String serviceLocation = "";

        String keyLocation = "";
        String URL = "";
        String fileName = "";  //Not sure what these do
        String directory = ""; //Not sure what these do


        String incomingHL7dir	= "";
        String errorHL7dir	= "";
        String completedHL7dir  = "";
        String emptyHL7dir      = "";


        String propFileName = null;
        if (args.length == 1){
            propFileName = args[0];
        }else{
            char sep = System.getProperty("file.separator").toCharArray()[0];
            propFileName = System.getProperty("user.home") + sep + "ClinicalConnect.properties";
        }


        try{
            FileInputStream fis = new FileInputStream(propFileName);
            properties.load(fis);
            fis.close();

            group = properties.getProperty("group");
            password = properties.getProperty("password");

            serviceUsername = properties.getProperty("serviceUsername");
            servicePassword = properties.getProperty("servicePassword");
            serviceLocation = properties.getProperty("serviceLocation");

            keyLocation = properties.getProperty("keyLocation");
            URL         = properties.getProperty("URL");
            fileName    = properties.getProperty("fileName","");
            directory   = properties.getProperty("directory","");


            incomingHL7dir	 = properties.getProperty("incomingHL7dir");
            errorHL7dir	 = properties.getProperty("errorHL7dir");
            completedHL7dir = properties.getProperty("completedHL7dir");
            emptyHL7dir     = properties.getProperty("emptyHL7dir");



        }catch(Exception e){
                e.printStackTrace();
                System.err.print(propFileName+" was not able to load ");
                System.exit(1);
        }

        

        File incomingDir = new File(incomingHL7dir);
        File errorDir = new File(errorHL7dir);
        File completedDir = new File(completedHL7dir);
        File emptyDir = new File(emptyHL7dir);

        File zipfile = pollClinicalConnectForFile(serviceLocation,serviceUsername, servicePassword, group, password, incomingDir);
        log.debug("AFTER pollClinicalConnectForFile");
        //I think i should check to see if this file is empty here

        clinConnect.sendDataToOscar(incomingDir,completedDir,errorDir,emptyDir,fileName, directory, keyLocation, URL);
    }

    private void sendDataToOscar(File incomingDir,File completedDir,File errorDir,File emptyDir,String fileName, String directory, String keyLocation, String URL){
        Uploader uploader = new Uploader();
        //Lock for files in incoming directory
        log.debug("incoming "+incomingDir);
        String[] incomingFiles = incomingDir.list();
        for(String incomingFile: incomingFiles){
            File zipfile = new File(incomingDir,incomingFile);
            byte[] fileBytes = null;
            try{
               log.info("incomingFile "+incomingFile+" -- zipfile "+zipfile);
               fileBytes = getByteArrayFromZip(zipfile);  //This isn't erroring correctly
            }catch(Exception getFileException){
                getFileException.printStackTrace();
                //What to do here?
            }
            log.debug("File size in bytes "+fileBytes.length);

            if (fileBytes.length > 0){
               
                try{
                   uploader.Upload(fileBytes, fileName, directory, keyLocation, URL);
                //If it worked need to move to completed directory
                }catch(Exception e){
                    e.printStackTrace();
                   //If it didn't work need to decide if it should stay put and wait for the next send or move to the error directory
                }
                //Uploaded successfully
                boolean success = zipfile.renameTo(new File(completedDir, zipfile.getName()+"."+System.currentTimeMillis()));
                if (!success) {
                    log.error("Not able to move "+zipfile.getName()  +" to "+completedDir.getName());
                    // File was not successfully moved
                }

            }else{
                log.info("FILE EMPTY - No Need to send to OSCAR");
                //Move File to empty directory
                boolean success = zipfile.renameTo(new File(emptyDir, zipfile.getName()+"."+System.currentTimeMillis()));
                if (!success) {
                    log.error("Not able to move "+zipfile.getName());
                    // File was not successfully moved
                }
            }
        }
    }

    private static File pollClinicalConnectForFile(String serviceLocation,String serviceUsername, String servicePassword, String group, String password, File incomingDir) {
        EmrDownloadEngine engine = new EmrDownloadEngine();
        engine.setServiceLocation(serviceLocation);
        engine.setServiceUserName(serviceUsername);
        engine.setServicePassword(servicePassword); //Ham1lton-Wat3rloo
        engine.setEnabled(true);
        engine.init();
        Long fileId = engine.generateFile(group, password, "Random Comment", "A4334234QRF");
        System.out.println("File ID:" + fileId);
        GeneratedFile file = engine.downloadFile(group, password, fileId);
        System.out.println("File Name:" + file.getFileName());
        System.out.println("Content Type:" + file.getContentType());
        System.out.println("Transaction ID:" + file.getTransactionId());
        System.out.println("Created Date:" + file.getCreatedDate());
        System.out.println("Download Date:" + file.getDownloadDate());
        //import data into EMR
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] filebytes = file.getData();
        System.out.println(new String(file.getData()));
        File zipfile = new File(incomingDir, "outfilename" + System.currentTimeMillis());
        try {
            FileOutputStream bout = new FileOutputStream(zipfile);
            bout.write(filebytes);
            bout.close();
        } catch (IOException e) {
        }
        //remove file
        engine.deleteFile(group, password, fileId);
        List<GeneratedFileInfo> files = engine.listFiles(group, password);
        if (files != null) {
            for (GeneratedFileInfo fileInfo : files) {
                if (fileInfo.isEmpty()) {
                    engine.deleteFile(group, password, fileInfo.getId());
                } else {
                    System.out.println(fileInfo.getFileName() + " / " + fileInfo.getCreatedDate() + " / " + fileInfo.getDownloadDate());
                }
            }
        }
        return zipfile;
    }


    public byte[] getByteArrayFromZip(File src) throws Exception{
        try { // Open the ZIP file

            ZipInputStream in = new ZipInputStream(new FileInputStream(src));
            // Get the first entry
            ZipEntry entry = in.getNextEntry();
            ByteArrayOutputStream out = new ByteArrayOutputStream();// new Long(((File) src).length()).intValue());  //SHOULD DO THIS WITH THE ZIP FILE
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            // Close the streams
            out.close();
            in.close();
            return out.toByteArray();

        } catch (IOException e) { e.printStackTrace();}
        return null;
    }
}
