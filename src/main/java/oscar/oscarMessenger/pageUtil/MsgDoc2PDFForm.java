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


package oscar.oscarMessenger.pageUtil;

import org.apache.struts.action.ActionForm;

public final class MsgDoc2PDFForm extends ActionForm {
    String srcText;
    boolean isPreview;
    String jsessionid;
    String[] uriArray;
    String attachmentNumber = null;
    String pdfTitle;
    
    public String getSrcText(){
        return srcText;
    }
    
    public void setSrcText(String srcText ){
        this.srcText = srcText;
    }
    

   public boolean getIsPreview(){
        return isPreview;
    }
    
    public void setIsPreview(boolean isPreview ){
        this.isPreview = isPreview;
    }

    public String getJsessionid(){
        return jsessionid;
    }
    
    public void setJsessionid(String jsessionid ){
        this.jsessionid = jsessionid;
    }
    
    public String getPdfTitle(){
        return pdfTitle;
    }
    
    public void setPdfTitle(String pdfTitle ){
        this.pdfTitle = pdfTitle;
    }    
    
    public String[] getUriArray(){
        return uriArray;
    }
    
    public void setUriArray(String[] uriArray ){
        this.uriArray = uriArray;
    }    
    
    public void setAttachmentNumber(String attachmentNumber ){
        this.attachmentNumber =  attachmentNumber;
    }
    
    public String getAttachmentNumber(){
        return attachmentNumber;
    }    
}
