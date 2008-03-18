/*
 * LabUploadForm.java
 *
 * Created on June 12, 2007, 2:32 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package oscar.oscarLab.ca.all.pageUtil;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;


public class LabUploadForm extends ActionForm {
   
   private FormFile importFile = null;
   
   
   public LabUploadForm() {
   }
   
   public FormFile getImportFile(){
      return importFile;
   }
   
   public void setImportFile(FormFile file){
      this.importFile = file;
   }
}