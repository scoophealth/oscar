/**
 * Copyright (c) 2012- Centre de Medecine Integree
 *
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
 * This software was written for
 * Centre de Medecine Integree, Saint-Laurent, Quebec, Canada to be provided
 * as part of the OSCAR McMaster EMR System
 */
package oscar.dms;


import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public final class IncomingDocUtil {

    private ArrayList<String> pdfListModifiedDate = new ArrayList<String>();
    private static final Comparator<File> lastModified = new Comparator<File>() {
        @Override
        public int compare(File o1, File o2) {
            return o1.lastModified() == o2.lastModified() ? 0 : (o1.lastModified() > o2.lastModified() ? 1 : -1);
        }
    };
   
    public ArrayList getPdfListModifiedDate() {
        return pdfListModifiedDate;

    }

    public ArrayList getDocList(String directory) {
        ArrayList<String> docList = new ArrayList<String>();

        String docName;
        pdfListModifiedDate.clear();

        FilenameFilter pdfFilter;

        pdfFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return (name.toLowerCase().endsWith(".pdf"));
            }
        };

        File dir = new File(directory);
        File[] listOfFiles = dir.listFiles(pdfFilter);
        if (listOfFiles != null) {

            Arrays.sort(listOfFiles, lastModified);

            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    docName = listOfFiles[i].getName();
                    long dateTime = listOfFiles[i].lastModified();
                    Date d = new Date(dateTime);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String dateString = sdf.format(d);
                    docList.add(docName);
                    pdfListModifiedDate.add(dateString);
                }
            }
        }
        return docList;
    }

    public static int getNumOfPages(String queueId, String pdfDir, String pdfName) {
        String filePath = getIncomingDocumentFilePathName(queueId, pdfDir, pdfName);
        int numOfPages = 0;
        PdfReader reader = null;
        try {
            reader = new PdfReader(filePath);
            numOfPages = reader.getNumberOfPages();
        } catch (Exception e) {
            MiscUtils.getLogger().error("Error", e);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return numOfPages;
    }

    public static String getIncomingDocumentFilePathName(String queueId, String pdfDir, String pdfName) {
        String filePathName = getIncomingDocumentFilePath(queueId, pdfDir);
        filePathName += File.separator + pdfName;

        return filePathName;
    }

        public static String getAndCreateIncomingDocumentFilePathName(String queueId, String pdfDir, String pdfName) {
        String filePathName = getAndCreateIncomingDocumentFilePath(queueId, pdfDir);
        filePathName += File.separator + pdfName;

        return filePathName;
    }

    public static String getIncomingDocumentDeletedFilePath(String queueId, String pdfDir) {
        String filePath;

        filePath = oscar.OscarProperties.getInstance().getProperty("INCOMINGDOCUMENT_DIR");

        if (!filePath.endsWith(File.separator)) {
            filePath += File.separator;
        }
        filePath += queueId + File.separator;
        File deletedPathDir = new File(filePath, pdfDir + "_deleted");

        if (!deletedPathDir.exists()) {
            deletedPathDir.mkdir();
        }

        if (pdfDir.equals("Fax")
                || pdfDir.equals("Mail")
                || pdfDir.equals("File")
                || pdfDir.equals("Refile")) {
            filePath = filePath + pdfDir + "_deleted";
        }
        return filePath;
    }

    public static String getIncomingDocumentFilePath(String queueId, String pdfDir) {
        String filePath;

        filePath = oscar.OscarProperties.getInstance().getProperty("INCOMINGDOCUMENT_DIR");

        if (!filePath.endsWith(File.separator)) {
            filePath += File.separator;
        }

        filePath += queueId + File.separator;

        if (pdfDir.equals("Fax")
                || pdfDir.equals("Mail")
                || pdfDir.equals("File")
                || pdfDir.equals("Refile")) {
            filePath = filePath + pdfDir;
        }

        return filePath;
    }

    public static String getAndCreateIncomingDocumentFilePath(String queueId, String pdfDir) {
        String filePath=getIncomingDocumentFilePath( queueId, pdfDir);
        File filePathDir = new File(filePath);

        if (!filePathDir.exists()) {
            filePathDir.mkdir();
        }
        return filePath;
        }

    public static void rotatePage(String queueId, String myPdfDir, String myPdfName, String MyPdfPageNumber, int degrees) throws Exception 
    {
        long lastModified;
        String filePathName, tempFilePathName;
        int rot;
        int rotatedegrees;

        tempFilePathName = getIncomingDocumentFilePath(queueId, myPdfDir) + File.separator + "T" + myPdfName;
        filePathName = getIncomingDocumentFilePathName(queueId, myPdfDir, myPdfName);

        File f = new File(filePathName);
        lastModified = f.lastModified();

        PdfReader reader = null;
        PdfStamper stp = null;

        try {
            reader = new PdfReader(filePathName);
            rot = reader.getPageRotation(Integer.parseInt(MyPdfPageNumber));
            rotatedegrees = rot + degrees;
            rotatedegrees = rotatedegrees % 360;

            reader.getPageN(Integer.parseInt(MyPdfPageNumber)).put(PdfName.ROTATE, new PdfNumber(rotatedegrees));
            stp = new PdfStamper(reader, new FileOutputStream(tempFilePathName));


        } catch (Exception e) {
                throw (e);                
        } finally {
            try {
                if (stp != null) {
                    stp.close();
                }

                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                throw (e);                
            }
        }

        
        boolean success = f.delete();

        if (success) {
            File f1 = new File(tempFilePathName);
            f1.setLastModified(lastModified);
            success = f1.renameTo(new File(filePathName));
            if (!success) {
                throw new Exception("Error in renaming file from:" + tempFilePathName + " to " + filePathName);
                }
        } else {
            throw new Exception("Error in deleting file:" + filePathName);
            }
    }

    public static void rotateAlPages(String queueId, String myPdfDir, String myPdfName, int degrees)  throws Exception
    {
        long lastModified;
        String filePathName, tempFilePathName;
        int rot;
        int rotatedegrees;

        tempFilePathName = getIncomingDocumentFilePath(queueId, myPdfDir) + File.separator + "T" + myPdfName;
        filePathName = getIncomingDocumentFilePathName(queueId, myPdfDir, myPdfName);

        File f = new File(filePathName);
        lastModified = f.lastModified();

        PdfReader reader = null;
        PdfStamper stp = null;

        try {
            reader = new PdfReader(filePathName);

            for (int p = 1; p <= reader.getNumberOfPages(); ++p) {
                rot = reader.getPageRotation(p);
                rotatedegrees = rot + degrees;
                rotatedegrees = rotatedegrees % 360;

                reader.getPageN(p).put(PdfName.ROTATE, new PdfNumber(rotatedegrees));
            }
            stp = new PdfStamper(reader, new FileOutputStream(tempFilePathName));

        } catch (Exception e) {
           throw (e);
        } finally {
            try {
                if (stp != null) {
                    stp.close();
                }

                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                throw (e);
            }
        }

        boolean success = f.delete();

        if (success) {
            File f1 = new File(tempFilePathName);
            f1.setLastModified(lastModified);
            success = f1.renameTo(new File(filePathName));
            if (!success) {
                throw new Exception("Error in renaming file from:" + tempFilePathName + "to " + filePathName);
                }
        } else {
                throw new Exception("Error in deleting file:" + filePathName);
        }
    }

    public static void deletePage(String queueId, String myPdfDir, String myPdfName, String PageNumberToDelete) throws Exception
    {
        long lastModified;
        String filePathName, tempFilePathName;

        tempFilePathName = getIncomingDocumentFilePath(queueId, myPdfDir) + File.separator + "T" + myPdfName;
        filePathName = getIncomingDocumentFilePathName(queueId, myPdfDir, myPdfName);

        File f = new File(filePathName);
        lastModified = f.lastModified();
        f.setReadOnly();

        String deletePath = getIncomingDocumentDeletedFilePath(queueId, myPdfDir) + File.separator;
        String deletePathFileName = "";
        int index = myPdfName.indexOf(".pdf");

        String myPdfNameF = myPdfName.substring(0, index);
        String myPdfNameExt = myPdfName.substring(index, myPdfName.length());

        PdfReader reader = null;
        Document document = null;
        PdfCopy copy = null;
        PdfCopy deleteCopy = null;

        try {
            reader = new PdfReader(filePathName);
            deletePathFileName = deletePath + myPdfNameF + "d" + PageNumberToDelete + "of" + Integer.toString(reader.getNumberOfPages()) + myPdfNameExt;

            document = new Document(reader.getPageSizeWithRotation(1));
            copy = new PdfCopy(document, new FileOutputStream(tempFilePathName));
            deleteCopy = new PdfCopy(document, new FileOutputStream(deletePathFileName));
            document.open();

            for (int pageNumber = 1; pageNumber <= reader.getNumberOfPages(); pageNumber++) {
                if (!(pageNumber == (Integer.parseInt(PageNumberToDelete)))) {
                    copy.addPage(copy.getImportedPage(reader, pageNumber));
                } else {
                    deleteCopy.addPage(copy.getImportedPage(reader, pageNumber));
                }
            }
        } catch (Exception e) {
            throw (e);
        } finally {
            try {
                if (copy != null) {
                    copy.close();
                }
                if (deleteCopy != null) {
                    deleteCopy.close();
                }

                if (document != null) {
                    document.close();
                }

                if (reader != null) {
                    reader.close();
                }

            } catch (Exception e) {
                throw (e);
            }
        }

        boolean success;
        if (!oscar.OscarProperties.getInstance().getBooleanProperty("INCOMINGDOCUMENT_RECYCLEBIN", "true")) {
            File f1 = new File(deletePathFileName);
            success = f1.delete();
            if (!success) {
                throw new Exception("Error in deleting file:" + deletePathFileName);
            }
        }

        success = f.delete();
        if (success) {
            File f1 = new File(tempFilePathName);
            f1.setLastModified(lastModified);
            success = f1.renameTo(new File(filePathName));
            if (!success) {
                throw new Exception("Error in renaming file from:" + tempFilePathName + "to " + filePathName);
            }
        } else {
            throw new Exception("Error in deleting file:" + filePathName);
        }
    }

    public static void extractPage(String queueId, String myPdfDir, String myPdfName, String pageNumbersToExtract) throws Exception
    {
        long lastModified;
        String filePathName, tempFilePathName;

        tempFilePathName = getIncomingDocumentFilePath(queueId, myPdfDir) + File.separator + "T" + myPdfName;
        filePathName = getIncomingDocumentFilePathName(queueId, myPdfDir, myPdfName);

        File f = new File(filePathName);
        lastModified = f.lastModified();
        f.setReadOnly();

        String extractPath = getIncomingDocumentFilePath(queueId, myPdfDir) + File.separator;
        int index = myPdfName.toLowerCase().indexOf(".pdf");
        String myPdfNameF = myPdfName.substring(0, index);
        String myPdfNameExt = myPdfName.substring(index, myPdfName.length());

        ArrayList<String> extractList = new ArrayList<String>();
        int startPage, endPage;
        boolean cancelExtract = false;

        PdfReader reader = null;
        Document document = null;
        PdfCopy copy = null;
        PdfCopy extractCopy = null;

        try {
            reader = new PdfReader(filePathName);
            extractPath = extractPath + myPdfNameF + "E" + Integer.toString(reader.getNumberOfPages()) + myPdfNameExt;

            for (int pgIndex = 0; pgIndex <= reader.getNumberOfPages(); pgIndex++) {
                extractList.add(pgIndex, "0");
            }

            String tmpPageNumbersToExtract = pageNumbersToExtract;
            String[] pageList = tmpPageNumbersToExtract.split(",");
            for (int i = 0; i < pageList.length; i++) {
                if (!pageList[i].isEmpty()) {
                    String[] rangeList = pageList[i].split("-");
                    if (rangeList.length > 2) {
                        cancelExtract = true;
                    }
                    for (int j = 0; j < rangeList.length; j++) {
                        if (!rangeList[j].matches("^[0-9]+$")) {
                            cancelExtract = true;
                        }
                    }
                    if (!cancelExtract) {
                        if (rangeList.length == 1) {
                            startPage = Integer.parseInt(rangeList[0], 10);
                            if (startPage > extractList.size() || startPage == 0) {
                                cancelExtract = true;
                            } else {
                                extractList.set(startPage, "1");
                            }
                        } else if (rangeList.length == 2) {
                            startPage = Integer.parseInt(rangeList[0], 10);
                            endPage = Integer.parseInt(rangeList[1], 10);

                            for (int k = startPage; k <= endPage; k++) {

                                if (k > extractList.size() || k == 0) {
                                    cancelExtract = true;
                                } else {
                                    extractList.set(k, "1");
                                }
                            }
                        }
                    }
                }
            }
            if (!cancelExtract) {
                cancelExtract = true;
                for (int pageNumber = 1; pageNumber <= reader.getNumberOfPages(); pageNumber++) {
                    if (!(extractList.get(pageNumber).equals("1"))) {
                        cancelExtract = false;
                    }
                }
            }
            if (cancelExtract == true) {
                reader.close();
                throw new Exception(myPdfName + " : Invalid Pages to Extract " + pageNumbersToExtract);
            }

            document = new Document(reader.getPageSizeWithRotation(1));
            copy = new PdfCopy(document, new FileOutputStream(tempFilePathName));
            extractCopy = new PdfCopy(document, new FileOutputStream(extractPath));
            document.open();
            for (int pageNumber = 1; pageNumber <= reader.getNumberOfPages(); pageNumber++) {
                if (!(extractList.get(pageNumber).equals("1"))) {
                    copy.addPage(copy.getImportedPage(reader, pageNumber));
                } else {
                    extractCopy.addPage(copy.getImportedPage(reader, pageNumber));
                }
            }


        } catch (Exception e) {
            throw (e);
        } finally {
            try {
                if (copy != null) {
                    copy.close();
                }
                if (extractCopy != null) {
                    extractCopy.close();
                }

                if (document != null) {
                    document.close();
                }

                if (reader != null) {
                    reader.close();
                }

            } catch (Exception e) {
                throw (e);
            }
        }

        boolean success = f.delete();

        if (success) {
            File f1 = new File(tempFilePathName);
            f1.setLastModified(lastModified);
            success = f1.renameTo(new File(filePathName));
            if (!success) {
                throw new Exception("Error in renaming file from:" + tempFilePathName + "to " + filePathName);
            }

            File f2 = new File(extractPath);
            f2.setLastModified(lastModified);
        } else {
            throw new Exception("Error in deleting file:" + filePathName);
        }
    }

    public static void DeletePDF(String queueId, String myPdfDir, String myPdfName) throws Exception
    {
        String filePathName;
        boolean success;

        filePathName = getIncomingDocumentFilePathName(queueId, myPdfDir, myPdfName);
        File f = new File(filePathName);

        String deletePathName = getIncomingDocumentDeletedFilePath(queueId, myPdfDir) + File.separator + myPdfName;

        File deletef = new File(deletePathName);

        if (oscar.OscarProperties.getInstance().getBooleanProperty("INCOMINGDOCUMENT_RECYCLEBIN", "true")) {
            success = f.renameTo(deletef);
            if (!success) {
                throw new Exception("Error in renaming file from:" + filePathName + " to " + deletePathName);
            }
        } else {
            success = f.delete();
            if (!success) {
                throw new Exception("Error in deleting file:" + filePathName);
            }
        }
    }

    public static String getAndSetIncomingDocQueue(String user_no, String selectedQueue) {
        String queue;
        UserPropertyDAO pref = (UserPropertyDAO) SpringUtils.getBean("UserPropertyDAO");

        UserProperty up = pref.getProp(user_no, UserProperty.INCOMING_DOCUMENT_DEFAULT_QUEUE);
        if (up == null) {
            up = new UserProperty();
            up.setName(UserProperty.INCOMING_DOCUMENT_DEFAULT_QUEUE);
            up.setProviderNo(user_no);
        }


        if (selectedQueue == null) {

            if (up.getValue() == null) {
                queue = "1";
            } else {
                queue = up.getValue();
            }
        } else {
            queue = selectedQueue;
        }

        if (up.getValue() == null || !(up.getValue().equals(queue))) {
            up.setValue(queue);
            pref.saveProp(up);
        }
        return queue;
    }

    public static String getAndSetViewDocumentAs(String user_no, String selectedImageType) {

        String imageType;

        UserPropertyDAO pref = (UserPropertyDAO) SpringUtils.getBean("UserPropertyDAO");
        UserProperty up = pref.getProp(user_no, UserProperty.VIEW_DOCUMENT_AS);

        if (up == null) {
            up = new UserProperty();
            up.setName(UserProperty.VIEW_DOCUMENT_AS);
            up.setProviderNo(user_no);
        }

        if (selectedImageType == null) {
            if (up.getValue() == null || up.getValue().equals("Pdf")) {
                imageType = "Pdf";
            } else {
                imageType = "Image";
            }
        } else {
            imageType = selectedImageType;
        }

        if (up.getValue() == null || !(up.getValue().equals(imageType))) {
            up.setValue(imageType);
            pref.saveProp(up);
        }
        return imageType;
    }

    public static String getAndSetEntryMode(String user_no, String selectedEntryMode) {

        String entryMode;

        UserPropertyDAO pref = (UserPropertyDAO) SpringUtils.getBean("UserPropertyDAO");
        UserProperty up = pref.getProp(user_no, UserProperty.INCOMING_DOCUMENT_ENTRY_MODE);

        if (up == null) {
            up = new UserProperty();
            up.setName(UserProperty.INCOMING_DOCUMENT_ENTRY_MODE);
            up.setProviderNo(user_no);
        }

        if (selectedEntryMode == null) {
            if (up.getValue() == null) {
                entryMode = "Normal";
            } else {
                entryMode = up.getValue();
            }
        } else {
            entryMode = selectedEntryMode;
        }

        if (up.getValue() == null || !(up.getValue().equals(entryMode))) {
            up.setValue(entryMode);
            pref.saveProp(up);
        }
        return entryMode;
    }

    public static void doPagesAction(String pdfAction, String queueIdStr, String pdfDir, String pdfName, String pdfPageNumber, String pdfExtractPageNumber, Locale locale) throws Exception
    {
        String filePathName = getIncomingDocumentFilePathName(queueIdStr, pdfDir, pdfName);
        ResourceBundle props = ResourceBundle.getBundle("oscarResources", locale);
        int degree = 0;

        if (pdfAction.equals("Rotate180")
                || pdfAction.equals("Rotate90")
                || pdfAction.equals("RotateM90")) {

            if (pdfAction.equals("Rotate180")) {
                degree=180;
            } else if (pdfAction.equals("Rotate90")) {
                degree=90;
            } else if (pdfAction.equals("RotateM90")) {
                degree=-90;
            }
            try {
                rotatePage(queueIdStr, pdfDir, pdfName, pdfPageNumber, degree);
            }
            catch (Exception e)
            {
                MiscUtils.getLogger().error("Error",e);
                throw new Exception(filePathName + " : " + props.getString("dms.incomingDocs.cannotRotatePage") + pdfPageNumber);
            }
        }

        if (pdfAction.equals("RotateAll180")
                || pdfAction.equals("RotateAll90")
                || pdfAction.equals("RotateAllM90")) {

            if (pdfAction.equals("RotateAll180")) {
                degree=180;
            } else if (pdfAction.equals("RotateAll90")) {
                degree=90;
            } else if (pdfAction.equals("RotateAllM90")) {
                degree=-90;
            }
            try {
                rotateAlPages(queueIdStr, pdfDir, pdfName, degree);
            } catch (Exception e)
            {
                MiscUtils.getLogger().error("Error",e);
                throw new Exception(filePathName + " : " + props.getString("dms.incomingDocs.cannotRotateAllPages"));
            }
        }


        if (pdfAction.equals("DeletePage")) {
            try {
                deletePage(queueIdStr, pdfDir, pdfName, pdfPageNumber);
            } catch (Exception e)
            {
                MiscUtils.getLogger().error("Error",e);
                throw new Exception(filePathName + " : " + props.getString("dms.incomingDocs.cannotDeletePage") + pdfPageNumber);
            }
        }

        if (pdfAction.equals("DeletePDF")) {
            try {
                DeletePDF(queueIdStr, pdfDir, pdfName);
            } catch (Exception e)
            {
                MiscUtils.getLogger().error("Error",e);
                throw new Exception(props.getString("dms.incomingDocs.cannotDelete") + filePathName);
            }
        }

        if (pdfAction.equals("ExtractPagePDF")) {
            try {
                extractPage(queueIdStr, pdfDir, pdfName, pdfExtractPageNumber);
            } catch (Exception e)
            {
                MiscUtils.getLogger().error("Error",e);
                throw e;
            }
        }
    }
}
