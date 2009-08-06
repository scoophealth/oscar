/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oscar.eform;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import oscar.eform.actions.DisplayImageAction;
import oscar.eform.data.EForm;

/**
 *
 * @author apavel
 */
public class EFormExportZip {
    public void exportForm(String formId, OutputStream os) throws IOException, Exception {
        EForm eForm = new EForm(formId);
        String html = eForm.getFormHtml();
        Properties properties = new Properties();
        properties.setProperty("form.name", eForm.getFormName());
        properties.setProperty("form.details", eForm.getFormSubject());
        properties.setProperty("form.htmlFilename", eForm.getFormFileName());
        properties.setProperty("form.creator", "Paul");
        properties.setProperty("form.date", eForm.getFormDate());
        
        StringReader stringReader = new StringReader(html);
        
        ZipOutputStream zos = new ZipOutputStream(os);
        zos.setLevel(9);

        //add directory
        ZipEntry formFolderZipEntry = new ZipEntry(eForm.getFormName().replaceAll("\\s", "") + "/");
        zos.putNextEntry(formFolderZipEntry);
        zos.closeEntry();

        //write properties file
        ZipEntry propertiesZipEntry = new ZipEntry("eform.properteis");
        zos.putNextEntry(propertiesZipEntry);
        properties.store(zos, "");
        zos.closeEntry();

        //write html
        ZipEntry htmlZipEntry = new ZipEntry(eForm.getFormFileName().replaceAll("\\s", ""));
        zos.putNextEntry(htmlZipEntry);
        byte[] bytes = html.getBytes("UTF-8");
        InputStream is = new ByteArrayInputStream(bytes);
        writeZos(zos, is);
        zos.closeEntry();

        //get Images, must do html search for image name
        Pattern eformImagePattern = Pattern.compile("\\${oscar_image_path}.+?[\"|'|>]");
        Matcher matcher = eformImagePattern.matcher(html);
        int groupCount = matcher.groupCount();
        for (int i = 0; i<groupCount; i++) {
            String match = matcher.group(i);
            int length = "${oscar_image_path}".length();
            String imageFileName = match.substring(length);
            System.out.println("Image Name: " + imageFileName);
            DisplayImageAction displayImageAction = new DisplayImageAction();
            File imageFile = displayImageAction.getImageFile(imageFileName);
            FileInputStream fis = new FileInputStream(imageFile);
            writeZos(zos, fis);
        }
        zos.closeEntry();
        zos.close();
    }
    
    private void writeZos(ZipOutputStream zos, InputStream is) throws IOException {
        byte[] buf = new byte[1024];
        int len;
        while ((len = is.read(buf)) > 0) {
                zos.write(buf, 0, len);
        }
    }

    public void importForm(String formId , InputStream importInputStream) {
        
    }
}
