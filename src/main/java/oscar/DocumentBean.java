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


package oscar;

/**
 *
 * Description - DocumentBean
 */


/**
 * JavaDoc comment for this bean
 */

public class DocumentBean extends java.lang.Object implements java.io.Serializable {
    
/*   *
 * Instance variable for the documentData properties
 */
    protected String filename = "";
    protected String foldername = "";
    protected String filedesc = "";
    protected String function = "";
    protected String function_id = "";
    protected String createdate = "";
    protected String docxml = "";
    protected String doccreator = "";
    protected String doctype = "";
    protected String oscar_prop="";
    protected String docDivert="";
    
    public void setDocDivert(String value) {
        docDivert = value;
    }
        
    public String getDocDivert() {
        return docDivert;
    }
                
    public void setFilename(String value) {
        filename = value;
    }
        
    public String getFilename() {
        return filename;
    }
    
    public void setOscar_prop(String value) {
        oscar_prop = value;
    }
        
    public String getOscar_prop() {
        return oscar_prop;
    }
    
    public void setFoldername(String value) {
        foldername = value;
    }
        
    public String getFoldername() {
        return foldername;
    }
    
    public void setFileDesc(String value) {
        filedesc = value;
    }
        
    public String getFileDesc() {
        return filedesc;
    }
        
    public void setFunction(String value) {
        function = value;
    }
    
    public String getFunction() {
        return function;
    }
    
    public void setFunctionID(String value) {
        function_id = value;
    }
    
    public String getFunctionID() {
        return function_id;
    }
    
    public void setDocXML(String value) {
        docxml = value;
    }
    
    public String getDocXML() {
        return docxml;
    }    
    
    public void setCreateDate(String value) {
        createdate = value;
    }
    
    public String getCreateDate() {
        return createdate;
    }
    
    public void setDocCreator(String value) {
        doccreator = value;
    }
    
    public String getDocCreator() {
        return doccreator;
    }
    
    public void setDocType(String value) {
        doctype = value;
    }
    
    public String getDocType() {
        return doctype;
    }    
}
