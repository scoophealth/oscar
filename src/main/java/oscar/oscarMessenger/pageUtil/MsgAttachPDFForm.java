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

public final class MsgAttachPDFForm extends ActionForm {
    String attachmentCount =  "0";
    String attachment = null;
    String attachmentTitle = "";
    String file_id = null;
    String srcText = "";
    boolean isPreview = false;
    boolean isAttaching = false;
    boolean isNew = true;
    String[] uriArray;
    String[] titleArray;
    String[] indexArray;
    String demographic_no;
    
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
    
    public String[] getUriArray(){
        return uriArray;
    }
    
    public void setUriArray(String[] uriArray ){
        this.uriArray = uriArray;
    }
    
    public String[] getTitleArray(){
        return titleArray;
    }
    
    public void setTitleArray(String[] titleArray ){
        this.titleArray = titleArray;
    }
    
    public String[] getIndexArray(){
        return indexArray;
    }
    
    public void setIndexArray(String[] indexArray ){
        this.indexArray = indexArray;
    }
    
    
    public void setIndexArray(String value, int i ){
        this.indexArray[i] = value;
    }
    
    public void setIsAttaching(boolean isAttaching ){
        this.isAttaching =  isAttaching;
    }
    
    public boolean getIsAttaching(){
        return isAttaching;
    }
    
    public void setIsNew(boolean isNew ){
        this.isNew =  isNew;
    }
    
    public boolean getIsNew(){
        return isNew;
    }
    
    public void setAttachmentCount(String attachmentCount ){
        this.attachmentCount =  attachmentCount;
    }
    
    public String getAttachmentCount(){
        return attachmentCount;
    }
    
    public void setAttachmentTitle(String attachmentTitle ){
        this.attachmentTitle =  attachmentTitle;
    }
    
    public String getAttachmentTitle(){
        return attachmentTitle;
    }
    
    public void setDemographic_no(String demographic_no ){
        this.demographic_no =  demographic_no;
    }
    
    public String getDemographic_no(){
        return demographic_no;
    }
    
    
    /*
    public void setAttachment(String attachment ){
        this.attachment =  attachment;
    }
     
    public String getAttachment(){
        return attachment;
    }
     
    public void setFile_id(String file_id ){
        this.file_id =  file_id;
    }
     
    public String getFile_id (){
        return file_id;
    }
     
     
     
        public String getJsessionid(){
        return jsessionid;
    }
     
    public void setJsessionid(String jsessionid ){
        this.jsessionid = jsessionid;
    }
     */
    
}
